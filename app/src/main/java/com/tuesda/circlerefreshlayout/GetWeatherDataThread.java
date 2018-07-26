package com.tuesda.circlerefreshlayout;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

//获取天气信息子线程，需要传入网络地址，地理位置形式为代码
public class GetWeatherDataThread extends Thread {

    protected String url;
    public Handler todayweatherdatahandler;
    public Handler dayweatherdatahandler;

    public GetWeatherDataThread(String url,Handler todayweatherdatahandler,Handler dayweatherdatahandler){
        this.url=url;
        this.todayweatherdatahandler=todayweatherdatahandler;
        this.dayweatherdatahandler=dayweatherdatahandler;
    }
    public GetWeatherDataThread(String url,Handler todayweatherdatahandler){
        this.url=url;
        this.todayweatherdatahandler=todayweatherdatahandler;
        this.dayweatherdatahandler=null;
    }

    @Override
    public void run() {
        try {
            URL weatherUrl=new URL(url);
            try {


                HttpURLConnection weatherUrlConnectrion= (HttpURLConnection) weatherUrl.openConnection();
                weatherUrlConnectrion.setRequestMethod("GET");
                weatherUrlConnectrion.setReadTimeout(5000);
                weatherUrlConnectrion.setReadTimeout(5000);

                WeatherDataPullClass weatherData= new WeatherDataPullClass(weatherUrlConnectrion.getInputStream());

                TodayWeatherDataClass todayWeatherDataClass=weatherData.getTodayWeatherDataClass();
                List<DayWeatherDataClass> dayWeatherDataClasses=weatherData.getDayWeatherDataClasses();
//                System.out.println("今日天气："+todayWeatherDataClass.getaqi());

//                从该子线程将数据打包传送给主线程
                Message todayweatherdatamsg=Message.obtain();
                todayweatherdatamsg.obj=todayWeatherDataClass;
                if (todayweatherdatahandler!=null) {
                    todayweatherdatahandler.sendMessage(todayweatherdatamsg);
                }

                Message dayweatherdatamsg=Message.obtain();
                dayweatherdatamsg.obj=dayWeatherDataClasses;
                if (dayweatherdatahandler!=null) {
                    dayweatherdatahandler.sendMessage(dayweatherdatamsg);
                }


//                BufferedReader reader=new BufferedReader(new InputStreamReader(weatherUrlConnectrion.getInputStream()));
//                StringBuffer stringBuffer=new StringBuffer();
//                String str;
//                while ((str=reader.readLine())!=null){
//                    stringBuffer.append(str);//获取的数据封装，需要解析
//                    System.out.println("获取的数据："+str);
//                }
//                System.out.println("应答："+stringBuffer.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
