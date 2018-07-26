package com.tuesda.circlerefreshlayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("Registered")
public class CityManegementActivity extends Activity {

    private ListView listView;
    private Button addcity;
    private Button back;
//    公共Dialog
    private Dialog dialog;
//    家庭成员数据缓存
    public List<String> citylist;
    public List<String> nameList;
    public List<String> telephonenumberList;
    public CardAdapter adapter;
    public SQLiteDatabase familyData;

//    添加城市Dialog中的EditText
    private EditText addcityname;

    private  TodayWeatherDataHandler todayWeatherDataHandler;
    protected TodayWeatherDataClass todayWeatherData;

    private String telephonenumber;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citymanagement_layout);

        citylist=new ArrayList<>();
        nameList=new ArrayList<>();
        telephonenumberList=new ArrayList<>();

//        打开SQLite数据文件
        familyData=openOrCreateDatabase("user.db",MODE_PRIVATE,null);
//        创建表
        familyData.execSQL("create table if not exists user (_id integer primary key autoincrement,name text,telephonenumber text,city text not null)");
//        读取数据到三个list中
        Cursor c=familyData.rawQuery("select * from user",null);
        if(c!=null){
            while (c.moveToNext()){
                citylist.add(c.getString(c.getColumnIndex("city")));
                nameList.add(c.getString(c.getColumnIndex("name")));
                telephonenumberList.add(c.getString(c.getColumnIndex("telephonenumber")));
            }
            c.close();
        }

        listView=findViewById(R.id.cards_list);
        addcity=findViewById(R.id.addcity);
        back=findViewById(R.id.back);
//
//
//        添加按钮监听器
        addcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitDialog();
                dialog.show();
            }
        });
//        返回按钮监听器
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter=new CardAdapter(CityManegementActivity.this,citylist,nameList,telephonenumberList);
        listView.setAdapter(adapter);

//        删除按钮监听器，删除该Item
        adapter.setOnDeleteClickListener(new CardAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
//                删除Item项操作
                citylist.remove(position);
                nameList.remove(position);
                telephonenumberList.remove(position);
                adapter.notifyDataSetChanged();
                listView.invalidate();
//                数据存储更新
                DataBaseUpdate();
            }
        });
//        城市按钮监听器,开启新的Activity,显示新的城市的天气信息
        adapter.setOnNewcityClickListener(new CardAdapter.OnNewcityClickListener() {
            @Override
            public void onNewcityClick(int position) {
                if(MainActivity.IsNetworkConnected(CityManegementActivity.this)){
                    Intent intent=new Intent();
//                Activity之间传递城市名字
                    intent.putExtra("newcityname",citylist.get(position));
                    intent.setClass(CityManegementActivity.this,NewCityWeatherActivity.class);
                    startActivity(intent);
                }
                if(!MainActivity.IsNetworkConnected(CityManegementActivity.this)){
                    Toast.makeText(CityManegementActivity.this,"网络未连接，无法获取天气信息",Toast.LENGTH_SHORT).show();
                }
            }
        });
//        保存按钮监听器,更新数据存储
        adapter.setOnUpdataClickListener(new CardAdapter.OnUpdataClickListener() {
            @Override
            public void onUpdataClick() {
                DataBaseUpdate();
            }
        });
    }
//    接收天气信息,并显示确认发送短信的提醒
    class TodayWeatherDataHandler extends Handler {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            todayWeatherData= (TodayWeatherDataClass) msg.obj;
            if (todayWeatherData != null) {
                InitMessageDialog();
                dialog.show();
            }
        }
    }

//    添加Card中的发送按钮监听事件，触发新线程获取天气信息
    @Override
    protected void onResume() {
        super.onResume();
        todayWeatherDataHandler=new TodayWeatherDataHandler();
        adapter.setOnSendMessageClickListener(new CardAdapter.OnSendMessageClickListener() {
            @Override
            public void onSendMessageClick(String cityname,String telephone) {
                if(MainActivity.IsNetworkConnected(CityManegementActivity.this)) {
//                从Card中获取电话号码
                    if (!telephone.isEmpty()) {
                        telephonenumber = telephone;
                        //                获取城市天气代码
                        String result = getJson("WeatherCityCode.txt");
                        int locationCode = 0;
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            locationCode = jsonObject.getInt(cityname);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                开启获取天气信息的线程
                        new GetWeatherDataThread("http://wthrcdn.etouch.cn/WeatherApi?citykey=" + locationCode, todayWeatherDataHandler).start();
                    } else {
                        Toast.makeText(CityManegementActivity.this, "没有发送对象，请完整设置家庭成员信息!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(CityManegementActivity.this,"网络未连接，无法获取信息!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//    发送信息并返回相关信息
    public void sendSMS(String message) {

//        注册处理返回发送状态的广播
        String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(CityManegementActivity.this, 0, sentIntent, 0);

//        注册处理返回接收状态的广播
        String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(CityManegementActivity.this, 0, deliverIntent, 0);

        //获取短信管理器
        SmsManager smsm = SmsManager.getDefault();
//        拆分短信
        ArrayList<String> divideContents = smsm.divideMessage(message);
        ArrayList<PendingIntent> pendingIntents = new ArrayList<>();
        pendingIntents.add(sentPI);
        pendingIntents.add(deliverPI);
//        发送短信，接收的短信将只有一个
        smsm.sendMultipartTextMessage(telephonenumber, null, divideContents, pendingIntents, null);

//        处理返回的发送状态
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(CityManegementActivity.this,"短信发送成功", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        break;
                }
            }
        }, new IntentFilter(SENT_SMS_ACTION));

//        处理返回的接收状态
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                Toast.makeText(CityManegementActivity.this,"收信人已经成功接收", Toast.LENGTH_SHORT).show();
            }
        }, new IntentFilter(DELIVERED_SMS_ACTION));
    }

//    初始化发送消息界面
    public void InitMessageDialog(){

        View layoutview=null;
        LayoutInflater inflater=LayoutInflater.from(CityManegementActivity.this);
        layoutview=inflater.inflate(R.layout.message_layout,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(CityManegementActivity.this).setView(layoutview);
        dialog=builder.create();
        dialog.create();

        final EditText greetmessage=dialog.findViewById(R.id.greetmessage);
        Button send=dialog.findViewById(R.id.send);
        Button canclesend=dialog.findViewById(R.id.cancelsend);

        String sendinformation="尊敬的客户，家庭成员发来提醒："+todayWeatherData.getcity()+ todayWeatherData.getshushidu()+"\n\n"+"ShineWeather提醒您注意天气变化，祝您身体健康，生活愉快！";
        greetmessage.setText(sendinformation);
//        dialog发送按钮监听器
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS(greetmessage.getText().toString());
                dialog.dismiss();
            }
        });
//        dialog取消按钮监听器
        canclesend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
//    添加城市dialog初始化
    public void InitDialog(){
        View layoutview=null;
        LayoutInflater inflater=LayoutInflater.from(CityManegementActivity.this);
        layoutview=inflater.inflate(R.layout.addcity_layout,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(CityManegementActivity.this).setView(layoutview);
        dialog=builder.create();
        dialog.create();

        addcityname=dialog.findViewById(R.id.addcityname);
        Button okadd=dialog.findViewById(R.id.okadd);
        Button canceladd=dialog.findViewById(R.id.canceladd);
        okadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                辨别该城市是否存在
                String newcity=addcityname.getText().toString();
                String result=getJson("WeatherCityCode.txt");
                try {
                    JSONObject jsonObject=new JSONObject(result);
//                    如果存在
                    if(jsonObject.optInt(newcity,0) != 0){
//                        三个list同时更新添加数据
                        citylist.add(newcity);
                        nameList.add("");
                        telephonenumberList.add("");
//                        familyData.execSQL("insert into user(position,city) values("+(list.size()-1)+",'"+newcity+"')");
                        addcityname.setText(null);
                        dialog.dismiss();
//                        更新存储数据
                        DataBaseUpdate();
                    }
//                    如果不存在
                    else {
                        Toast.makeText(CityManegementActivity.this,"该城市不存在",Toast.LENGTH_SHORT).show();
                        addcityname.setText(null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        dialog取消按钮监听器
        canceladd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcityname.setText(null);
                dialog.dismiss();
            }
        });
    }

//    解析json格式文件
    public String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = CityManegementActivity.this.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
//    数据存储更新方法
    public void DataBaseUpdate(){
        nameList=adapter.getName();
        telephonenumberList=adapter.getTelephoneNumber();
//        删除原有数据，清空表
        familyData.execSQL("delete from user");
        familyData.execSQL("delete from sqlite_sequence");
//        重新写入数据
        for (int i=0;i<citylist.size();i++) {
            familyData.execSQL("insert into user (name,telephonenumber,city)values ('"+nameList.get(i)+"','"+telephonenumberList.get(i)+"','"+citylist.get(i)+"')");
        }
    }

//    Activity销毁时更新村粗数据并释放存储空间
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataBaseUpdate();
        familyData.close();
    }
}
