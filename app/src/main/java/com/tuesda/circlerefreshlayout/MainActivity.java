package com.tuesda.circlerefreshlayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends TitleActivity {

    //    判断是否是人为刷新界面
    protected boolean change;
    //    刷新选择
    protected int checkedItem;

    //    接收天气信息，位置信息，刷新信息的接收器
    protected Handler todayweatherdatahandler;
    protected Handler dayweatherdatahandler;
    protected Handler locationdatahandler;
    protected Handler refreshdelayhandler;
    //    接收回的天气信息存入该类
    protected ArrayList<DayWeatherDataClass> dayWeatherDatas;
    protected TodayWeatherDataClass todayWeatherData;
    //    定位的城市天气代码
    protected int locationCode;

    //    mList的适配器
    private ListAdapter adapter;

    //    百度定位器以及其监听器
    public LocationClient locationClient;
    private BDAbstractLocationListener locationListener;

    //    刷新动画布局
    private CircleRefreshLayout mRefreshLayout;
    private ListView mList;

    private long lastBack = 0;

    private SharedPreferences.Editor editor;
    private SharedPreferences read;

    @SuppressLint({"ResourceAsColor", "HandlerLeak", "ClickableViewAccessibility", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        设置标题栏信息
        showBackwardView("", false);
        showForwardView("∷", true);
        showLocationIcon(false);

//        开始进入时自动刷新
        change = false;

        mRefreshLayout = (CircleRefreshLayout) findViewById(R.id.refresh_layout);
        mList = (ListView) findViewById(R.id.list);

//        初始化百度sdk
        locationListener = new WeatherLocationListener();
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(locationListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        option.setCoorType("bd09ll");
//        option.setLocationNotify(true);
//        option.setIgnoreKillProcess(false);
//        option.SetIgnoreCacheException(false);
//        option.setWifiCacheTimeOut(5*60*1000);
//        option.setEnableSimulateGps(false);
//        option.setOpenGps(true);
//        option.setScanSpan(10000);
        locationClient.setLocOption(option);

        todayweatherdatahandler = new TodayWeatherDataHandler();
        dayweatherdatahandler = new DayWeatherDataHandler();
        locationdatahandler = new LocationDataHandler();
        refreshdelayhandler = new Refreshdelayhandler();

        mRefreshLayout.setOnRefreshListener(
                new CircleRefreshLayout.OnCircleRefreshListener() {
                    @Override
                    public void refreshing() {
                        change = true;
                        dayWeatherDatas.clear();
                        todayWeatherData = null;
                        // do something when refresh starts
//                        Toast.makeText(MainActivity.this,"开始刷新",Toast.LENGTH_LONG).show();
                        if(IsNetworkConnected(MainActivity.this)){
                            new GetLocationDataThread().start();
                        }
                        else {
                            Toast.makeText(MainActivity.this,"刷新失败",Toast.LENGTH_SHORT).show();
                            mRefreshLayout.finishRefreshing();
                        }
                    }

                    @Override
                    public void completeRefresh() {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                //显示dialog
                                mRefreshLayout.finishRefreshing();
                            }
                        }, 1000);
                    }
                });

    }

    //    接收从子线程传来的数据
    class TodayWeatherDataHandler extends Handler {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            dayWeatherDatas= (ArrayList<DayWeatherDataClass>) msg.obj;
            todayWeatherData = (TodayWeatherDataClass) msg.obj;
//            dayWeatherDatas=msg.getData().getParcelableArrayList("dayWeatherData");
//            todayWeatherData=(TodayWeatherDataClass) msg.getData().getSerializable("todayWeatherDate");
//            System.out.println("........."+msg.what);
//            if (dayWeatherDatas != null) {
//                System.out.println("今天天气:"+dayWeatherDatas.get(1).getdate());
//            }
            if (todayWeatherData != null) {
                System.out.println("今天天气:" + todayWeatherData.getcity());
            }
        }
    }

    class DayWeatherDataHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dayWeatherDatas = (ArrayList<DayWeatherDataClass>) msg.obj;
            if (dayWeatherDatas != null && todayWeatherData != null) {
//                写入天气数据到app，以便无网络时显示
                new OutputUtil<ArrayList<DayWeatherDataClass>>().writeObjectIntoLocal(MainActivity.this, "dayweatherdatas", dayWeatherDatas);
                new OutputUtil<TodayWeatherDataClass>().writeObjectIntoLocal(MainActivity.this, "todayweatherdata", todayWeatherData);

                if (change) {
                    mRefreshLayout.finishRefreshing();
                    Toast.makeText(MainActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                }
                adapter = new ListAdapter(MainActivity.this, todayWeatherData, dayWeatherDatas);
                adapter.notifyDataSetChanged();
                mList.setAdapter(adapter);

                System.out.println("今天天气:" + dayWeatherDatas.get(0).getnightfengli());
            }
        }
    }

    //    接收定位数据
    class LocationDataHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            locationCode = (int) msg.obj;
            System.out.println("CityCode" + locationCode);
            new GetWeatherDataThread("http://wthrcdn.etouch.cn/WeatherApi?citykey=" + locationCode, todayweatherdatahandler, dayweatherdatahandler).start();
//            Toast.makeText(MainActivity.this,"CityCode"+locationCode,Toast.LENGTH_LONG).show();
        }
    }

    class Refreshdelayhandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int check = msg.what;
            int delay = 0;
            switch (check) {
                case 0:
                    delay = 3600;
                case 1:
                    delay = 3600 * 2;
                case 2:
                    delay = 3600 * 6;
                case 3:
                    delay = 3600 * 12;
                case 4:
                    delay = 3600 * 24;
            }
            final int finalDelay = delay;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    new GetLocationDataThread().start();
                    refreshdelayhandler.postDelayed(this, finalDelay * 1000);
                }
            };
            refreshdelayhandler.postDelayed(runnable, finalDelay * 1000);
        }
    }

    public class WeatherLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
//            if(location!=null)
//            {
//                Toast.makeText(MainActivity.this,"定位",Toast.LENGTH_LONG).show();
//            }
//            double latitude = location.getLatitude();    //获取纬度信息
//            double longitude = location.getLongitude();    //获取经度信息
//            System.out.println("精度"+latitude+"维度"+longitude);

//            String addr = location.getAddrStr();    //获取详细地址信息
//            String country = location.getCountry();    //获取国家
//            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            System.out.println("定位城市");
//            city=city.substring(0,city.length()-1);
//
//            Toast.makeText(MainActivity.this,city+location.getCityCode(),Toast.LENGTH_LONG).show();
            String district = location.getDistrict();    //获取区县
            district = district.substring(0, district.length() - 1);
//            写入当前定位信息到内存，以便无网路时显示
            editor = getSharedPreferences("refreshdata", MODE_PRIVATE).edit();
            editor.putString("gpscity", district);
            editor.apply();

            setTitle(district);
            showLocationIcon(true);
//            String street = location.getStreet();    //获取街道信息
            String result = getJson("WeatherCityCode.txt");
            try {
                JSONObject jsonObject = new JSONObject(result);
                Message locationdatamsg = Message.obtain();
                locationdatamsg.obj = jsonObject.getInt(district);
                locationdatahandler.sendMessage(locationdatamsg);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //动态权限获取，如果成功获取权限则开启定位
    private void startLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS};
            List<String> mPermissionList = new ArrayList<>();
            mPermissionList.clear();
            int checkPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED)
                    mPermissionList.add(permissions[i]);
            }
            if (mPermissionList.isEmpty()) {
                locationClient.stop();
                locationClient.start();
                System.out.println("开始定位");
            } else {
                String[] permission = mPermissionList.toArray(new String[mPermissionList.size()]);
                ActivityCompat.requestPermissions(MainActivity.this, permission, 1);
            }
        } else {
            locationClient.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case 1:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        //获取到权限，做相应处理
                        //调用定位SDK应确保相关权限均被授权，否则会引起定位失败
                        locationClient.start();
                    } else {
                        //没有获取到权限，做特殊处理
                        Toast.makeText(MainActivity.this, "无法获取权限", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                break;
        }
    }

    //    获取信息的线程
    class GetLocationDataThread extends Thread {
        @Override
        public void run() {
            startLocation();
        }
    }

    //    解析json格式的方法
    public String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = MainActivity.this.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
//                Log.d("AAA", line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    //    判断网络活动状态
    public static boolean IsNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null)
                return networkInfo.isAvailable();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void openOptionsMenu() {
        super.openOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    //    菜单栏相应选项的监听设置(不用onCreateOptionsMenu的目的是使菜单悬浮在标题栏下面)
    @Override
    public void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.refreshrate:
                        RefreshrateChoose();
                        break;
                    case R.id.editcity:
                        Intent editcityintent = new Intent();
                        editcityintent.setClass(MainActivity.this, CityManegementActivity.class);
                        startActivity(editcityintent);
                        break;
                    case R.id.about:
                        Intent aboutintent = new Intent();
                        aboutintent.setClass(MainActivity.this, AboutActivity.class);
                        startActivity(aboutintent);
                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
//                Toast.makeText(getApplicationContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();

    }

    //    刷新频率选择
    public void RefreshrateChoose() {
        final String items[] = {"1小时", "2小时", "6小时", "12小时", "24小时", "不自动刷新"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, 0);
//        存入选择的刷新信息，以便下次启动时初始化
        read = getSharedPreferences("refreshdata", MODE_PRIVATE);
        checkedItem = read.getInt("position", 5);
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor = getSharedPreferences("refreshdata", MODE_PRIVATE).edit();
                editor.putInt("position", which);
                editor.apply();
                Toast.makeText(MainActivity.this, items[which],
                        Toast.LENGTH_SHORT).show();
                Message msg = Message.obtain();
                msg.what = checkedItem;
                refreshdelayhandler.sendMessage(msg);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (IsNetworkConnected(MainActivity.this)) {
            //        启动线程获取定位信息
            new GetLocationDataThread().start();
        }
        if(!IsNetworkConnected(MainActivity.this)) {
            setDefaultData();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (IsNetworkConnected(MainActivity.this)) {
            //        启动线程获取定位信息
            new GetLocationDataThread().start();
        }
        if(!IsNetworkConnected(MainActivity.this)) {
            setDefaultData();
        }
    }

    //    再次返回键退出程序
    @Override
    public void onBackPressed() {
        if (lastBack == 0 || System.currentTimeMillis() - lastBack > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            lastBack = System.currentTimeMillis();
            return;
        }
        super.onBackPressed();
    }

    public class OutputUtil<T> {
        /**
         * 将对象保存到本地
         *
         * @param context
         * @param fileName 文件名
         * @param bean     对象
         * @return true 保存成功
         */
        public boolean writeObjectIntoLocal(Context context, String fileName, T bean) {
            try {
                // 通过openFileOutput方法得到一个输出流，方法参数为创建的文件名（不能有斜杠），操作模式
                @SuppressWarnings("deprecation")
                FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(bean);//写入
                fos.close();//关闭输入流
                oos.close();
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //Toast.makeText(WebviewTencentActivity.this, "出现异常1",Toast.LENGTH_LONG).show();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(WebviewTencentActivity.this, "出现异常2",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }
    public class InputUtil<T> {
        /**
         * 读取本地对象
         *
         * @param context
         * @param fielName 文件名
         * @return
         */
        @SuppressWarnings("unchecked")
        public T readObjectFromLocal(Context context, String fielName) {
            T bean;
            try {
                FileInputStream fis = context.openFileInput(fielName);//获得输入流
                ObjectInputStream ois = new ObjectInputStream(fis);
                bean = (T) ois.readObject();
                fis.close();
                ois.close();
                return bean;
            } catch (StreamCorruptedException e) {
                //Toast.makeText(ShareTencentActivity.this,"出现异常3",Toast.LENGTH_LONG).show();//弹出Toast消息
                e.printStackTrace();
                return null;
            } catch (OptionalDataException e) {
                //Toast.makeText(ShareTencentActivity.this,"出现异常4",Toast.LENGTH_LONG).show();//弹出Toast消息
                e.printStackTrace();
                return null;
            } catch (FileNotFoundException e) {
                //Toast.makeText(ShareTencentActivity.this,"出现异常5",Toast.LENGTH_LONG).show();//弹出Toast消息
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (ClassNotFoundException e) {
                //Toast.makeText(ShareTencentActivity.this,"出现异常6",Toast.LENGTH_LONG).show();//弹出Toast消息
                e.printStackTrace();
                return null;
            }
        }
    }
    public void setDefaultData(){
        Toast.makeText(MainActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
        read = getSharedPreferences("refreshdata", MODE_PRIVATE);
        showLocationIcon(true);
        setTitle(read.getString("gpscity",""));
        todayWeatherData=new InputUtil<TodayWeatherDataClass>().readObjectFromLocal(this,"todayweatherdata");
        dayWeatherDatas=new InputUtil<ArrayList<DayWeatherDataClass>>().readObjectFromLocal(this,"dayweatherdatas");
        adapter = new ListAdapter(MainActivity.this, todayWeatherData, dayWeatherDatas);
        adapter.notifyDataSetChanged();
        mList.setAdapter(adapter);
    }
}
