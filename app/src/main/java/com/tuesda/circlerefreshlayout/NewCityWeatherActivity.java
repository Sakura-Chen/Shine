package com.tuesda.circlerefreshlayout;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.LocationClient;
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NewCityWeatherActivity extends TitleActivity {

    protected  boolean change;

    protected Handler todayweatherdatahandler;
    protected Handler dayweatherdatahandler;
    protected ArrayList<DayWeatherDataClass> dayWeatherDatas;
    protected TodayWeatherDataClass todayWeatherData;
    protected Intent intent;
    protected String cityname;
    private int citycode;

    private ListAdapter adapter;


    private CircleRefreshLayout mRefreshLayout;
    private ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showForwardView("",false);
        showBackwardView("◁",true);
        showLocationIcon(false);


        change=false;

        mRefreshLayout = (CircleRefreshLayout) findViewById(R.id.refresh_layout);
        mList = (ListView) findViewById(R.id.list);

        todayweatherdatahandler=new TodayWeatherDataHandler();
        dayweatherdatahandler=new DayWeatherDataHandler();

        intent=getIntent();
        cityname=intent.getStringExtra("newcityname");
        setTitle(cityname);
        String result=getJson("WeatherCityCode.txt");
        try {
            JSONObject jsonObject=new JSONObject(result);
            citycode=jsonObject.getInt(cityname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new GetWeatherDataThread("http://wthrcdn.etouch.cn/WeatherApi?citykey="+citycode, todayweatherdatahandler,dayweatherdatahandler).start();
        mRefreshLayout.setOnRefreshListener(
                new CircleRefreshLayout.OnCircleRefreshListener() {
                    @Override
                    public void refreshing() {
                        change=true;
                        dayWeatherDatas.clear();
                        todayWeatherData=null;
                        // do something when refresh starts
//                        Toast.makeText(MainActivity.this,"开始刷新",Toast.LENGTH_LONG).show();
                        if(MainActivity.IsNetworkConnected(NewCityWeatherActivity.this)){
                            new GetWeatherDataThread("http://wthrcdn.etouch.cn/WeatherApi?citykey="+citycode, todayweatherdatahandler,dayweatherdatahandler).start();
                        }
                        else {
                            Toast.makeText(NewCityWeatherActivity.this,"刷新失败",Toast.LENGTH_SHORT).show();
                            mRefreshLayout.finishRefreshing();
                        }
                    }

                    @Override
                    public void completeRefresh() {
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                //显示dialog
                                mRefreshLayout.finishRefreshing();
                            }
                        }, 2000);
                    }
                });


    }

    class TodayWeatherDataHandler extends Handler {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            todayWeatherData= (TodayWeatherDataClass) msg.obj;
            if (todayWeatherData != null) {
                System.out.println("今天天气:"+todayWeatherData.getcity());
            }
        }
    }
    class DayWeatherDataHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dayWeatherDatas= (ArrayList<DayWeatherDataClass>) msg.obj;
            if(dayWeatherDatas!=null&&todayWeatherData!=null){
                if(change) {
                    mRefreshLayout.finishRefreshing();
                    Toast.makeText(NewCityWeatherActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
                }
                adapter = new ListAdapter(NewCityWeatherActivity.this,todayWeatherData,dayWeatherDatas);
                adapter.notifyDataSetChanged();
                mList.setAdapter(adapter);

                System.out.println("今天天气:"+dayWeatherDatas.get(0).getnightfengli());
            }
        }
    }

    public String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = NewCityWeatherActivity.this.getAssets();
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

}
