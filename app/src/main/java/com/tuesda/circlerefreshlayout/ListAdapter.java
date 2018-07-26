package com.tuesda.circlerefreshlayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater=null;
    private AlertDialog dialog;
    private int status;

    protected ArrayList<DayWeatherDataClass> dayWeatherDatas;
    protected TodayWeatherDataClass todayWeatherData;

    public ListAdapter(Context context,TodayWeatherDataClass todayWeatherData,ArrayList<DayWeatherDataClass> dayWeatherDatas){

        this.context=context;
        this.inflater= LayoutInflater.from(context);
        this.todayWeatherData=todayWeatherData;
        this.dayWeatherDatas=dayWeatherDatas;
        status=0;

    }
    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.weather_layout,null);
            holder.updatatime=convertView.findViewById(R.id.updatatime);
            holder.date=convertView.findViewById(R.id.date);
            holder.weatherpng=convertView.findViewById(R.id.weatherpng);
            holder.weather=convertView.findViewById(R.id.weather);
            holder.temperature=convertView.findViewById(R.id.temperature);
            holder.lowtemperature=convertView.findViewById(R.id.lowtemperature);
            holder.hightemperature=convertView.findViewById(R.id.hightemperature);
//            holder.airquality=convertView.findViewById(R.id.airquality);
            holder.firstday=convertView.findViewById(R.id.firstday);
            holder.firstweather=convertView.findViewById(R.id.firstweather);
            holder.firsthighlow=convertView.findViewById(R.id.firsthighlow);
            holder.secondday=convertView.findViewById(R.id.secondday);
            holder.secondweather=convertView.findViewById(R.id.secondweather);
            holder.secondhighlow=convertView.findViewById(R.id.secondhighlow);
            holder.thirdday=convertView.findViewById(R.id.thirdday);
            holder.thirdweather=convertView.findViewById(R.id.thirdweather);
            holder.thirdhighlow=convertView.findViewById(R.id.thirdhighlow);
            holder.fourthday=convertView.findViewById(R.id.fourthday);
            holder.fourthweather=convertView.findViewById(R.id.fourthweather);
            holder.fourthhighlow=convertView.findViewById(R.id.fourthhighlow);
            holder.humidity=convertView.findViewById(R.id.humidity);
            holder.winddirection=convertView.findViewById(R.id.winddirection);
            holder.windpower=convertView.findViewById(R.id.windpower);
            holder.sunrise=convertView.findViewById(R.id.sunrise);
            holder.sunset=convertView.findViewById(R.id.sunset);
            holder.nightweather=convertView.findViewById(R.id.nightweather);
            holder.nightwinddirection=convertView.findViewById(R.id.nightwinddirection);
            holder.nightwindpower=convertView.findViewById(R.id.nightwindpower);
            holder.chenlian=convertView.findViewById(R.id.chenlian);
            holder.shushidu=convertView.findViewById(R.id.shushidu);
            holder.chuanyi=convertView.findViewById(R.id.chuanyi);
            holder.ganmao=convertView.findViewById(R.id.ganmao);
            holder.liangshai=convertView.findViewById(R.id.liangshai);
            holder.lvxing=convertView.findViewById(R.id.lvxing);
            holder.ziwaixian=convertView.findViewById(R.id.ziwaixian);
            holder.xiche=convertView.findViewById(R.id.xiche);
            holder.yundong=convertView.findViewById(R.id.yundong);
            holder.yuehui=convertView.findViewById(R.id.yuehui);
            holder.yusan=convertView.findViewById(R.id.yusan);
            holder.tpprogressbar=convertView.findViewById(R.id.tpprogressbar);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.updatatime.setText(todayWeatherData.getupdatetime()+" 发布");
        holder.date.setText(dayWeatherDatas.get(0).getdate());
        holder.weather.setText(dayWeatherDatas.get(0).getdaytype());
        holder.temperature.setText(todayWeatherData.getwendu());
        holder.lowtemperature.setText(dayWeatherDatas.get(0).getlow()+"°-------");
        holder.hightemperature.setText("-------"+dayWeatherDatas.get(0).gethigh()+"°");
//        holder.airquality.setText("空气质量:"+todayWeatherData.getquality());
        holder.firstday.setText(dayWeatherDatas.get(1).getdate());
        holder.firsthighlow.setText(dayWeatherDatas.get(1).gethigh()+"°"+dayWeatherDatas.get(1).getlow()+"°");
        holder.secondday.setText(dayWeatherDatas.get(2).getdate());
        holder.secondhighlow.setText(dayWeatherDatas.get(2).gethigh()+"°"+dayWeatherDatas.get(2).getlow()+"°");
        holder.thirdday.setText(dayWeatherDatas.get(3).getdate());
        holder.thirdhighlow.setText(dayWeatherDatas.get(3).gethigh()+"°"+dayWeatherDatas.get(3).getlow()+"°");
        holder.fourthday.setText(dayWeatherDatas.get(4).getdate());
        holder.fourthhighlow.setText(dayWeatherDatas.get(4).gethigh()+"°"+dayWeatherDatas.get(4).getlow()+"°");
        setWeatherIcon(holder.weatherpng,dayWeatherDatas.get(0).getdaytype());
        setWeatherIcon(holder.firstweather,dayWeatherDatas.get(1).getdaytype());
        setWeatherIcon(holder.secondweather,dayWeatherDatas.get(2).getdaytype());
        setWeatherIcon(holder.thirdweather,dayWeatherDatas.get(3).getdaytype());
        setWeatherIcon(holder.fourthweather,dayWeatherDatas.get(4).getdaytype());
        holder.humidity.setText(todayWeatherData.getshidu());
        holder.winddirection.setText(dayWeatherDatas.get(0).getdayfengxiang());
        holder.windpower.setText(dayWeatherDatas.get(0).getdayfengli());
        holder.sunrise.setText(todayWeatherData.getsunrise()+"-------");
        holder.sunset.setText("-------"+todayWeatherData.getsunset());
        holder.nightweather.setText(dayWeatherDatas.get(0).getnighttype());
        holder.nightwinddirection.setText(dayWeatherDatas.get(0).getnightfengxiang());
        holder.nightwindpower.setText(dayWeatherDatas.get(0).getnightfengli());
        @SuppressLint("HandlerLeak")
        final Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                holder.tpprogressbar.setProgress(status);
            }
        };
        new Thread(){
            @Override
            public void run() {
                int i=Integer.valueOf(todayWeatherData.getwendu()) - Integer.valueOf(dayWeatherDatas.get(0).getlow());
                int j=Integer.valueOf(dayWeatherDatas.get(0).gethigh()) - Integer.valueOf(dayWeatherDatas.get(0).getlow());
//                System.out.println("qqqq"+i+"  "+j);
                holder.tpprogressbar.setMax(j*80);
                while(status < i*80) {
                    //获取耗时操作的完成百分比
                    status++;
                    try {
                        sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //发送消息
                    mHandler.sendEmptyMessage(status);
                }
            }
        }.start();

        holder.chenlian.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) { ShowDialog(v); }
                                           });
        holder.shushidu.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) { ShowDialog(v);
                                               }
                                           });
        holder.chuanyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(v);
            }
        });
        holder.ganmao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(v);
            }
        });
        holder.liangshai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(v);
            }
        });
        holder.lvxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(v);
            }
        });
        holder.ziwaixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(v);
            }
        });
        holder.xiche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(v);
            }
        });
        holder.yundong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(v);
            }
        });
        holder.yuehui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(v);
            }
        });
        holder.yusan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(v);
            }
        });

        holder.firstweather.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) { ShowDialogImage(v);
                                                   }
                                               });
        holder.secondweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogImage(v);
            }
        });
        holder.thirdweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogImage(v);
            }
        });
        holder.fourthweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogImage(v);
            }
        });

        return convertView;
    }

    public void setWeatherIcon(ImageView view, String weather){
        switch (weather){
            case "晴":
                view.setImageResource(R.drawable.sunny);break;
            case "多云":
                view.setImageResource(R.drawable.cloudy);break;
            case "少云":
                view.setImageResource(R.drawable.fewclouds);break;
            case "晴间多云":
                view.setImageResource(R.drawable.partlycloudy);break;
            case "阴":
                view.setImageResource(R.drawable.overcast);break;
            case "有风":
                view.setImageResource(R.drawable.windy);break;
            case "平静":
                view.setImageResource(R.drawable.calm);break;
            case "微风":
                view.setImageResource(R.drawable.lightbreeze);break;
            case "和风":
                view.setImageResource(R.drawable.moderate);break;
            case "清风":
                view.setImageResource(R.drawable.freshbreeze);break;
            case "强风":
                view.setImageResource(R.drawable.strongbreeze);break;
            case "疾风":
                view.setImageResource(R.drawable.highwind);break;
            case "大风":
                view.setImageResource(R.drawable.gale);break;
            case "烈风":
                view.setImageResource(R.drawable.stronggale);break;
            case "风暴":
                view.setImageResource(R.drawable.storm);break;
            case "狂爆风":
                view.setImageResource(R.drawable.violentstorm);break;
            case "飓风":
                view.setImageResource(R.drawable.hurricane);break;
            case "龙卷风":
                view.setImageResource(R.drawable.tornado);break;
            case "热带风暴":
                view.setImageResource(R.drawable.tropicalstorm);break;
            case "阵雨":
                view.setImageResource(R.drawable.showerrain);break;
            case "强阵雨":
                view.setImageResource(R.drawable.heavyshowerrain);break;
            case "雷阵雨":
                view.setImageResource(R.drawable.thundershower);break;
            case "强雷阵雨":
                view.setImageResource(R.drawable.heavythunderstorm);break;
            case "雷阵雨伴有冰雹":
                view.setImageResource(R.drawable.hail);break;
            case "小雨":
                view.setImageResource(R.drawable.lightrain);break;
            case "中雨":
                view.setImageResource(R.drawable.moderaterain);break;
            case "大雨":
                view.setImageResource(R.drawable.heavyrain);break;
            case "极端降雨":
                view.setImageResource(R.drawable.extremerain);break;
            case "细雨":
                view.setImageResource(R.drawable.drizzlerain);break;
            case "暴雨":
                view.setImageResource(R.drawable.stormrain);break;
            case "大暴雨":
                view.setImageResource(R.drawable.heavystorm);break;
            case "特大暴雨":
                view.setImageResource(R.drawable.severestorm);break;
            case "冻雨":
                view.setImageResource(R.drawable.freezingrain);break;
            case "小雪":
                view.setImageResource(R.drawable.lightsnow);break;
            case "中雪":
                view.setImageResource(R.drawable.moderatesnow);break;
            case "大雪":
                view.setImageResource(R.drawable.heavysnow);break;
            case "暴雪":
                view.setImageResource(R.drawable.snowstorm);break;
            case "雨夹雪":
                view.setImageResource(R.drawable.sleet);break;
            case "雨雪天气":
                view.setImageResource(R.drawable.rainandsnow);break;
            case "阵雨夹雪":
                view.setImageResource(R.drawable.showersnow);break;
            case "阵雪":
                view.setImageResource(R.drawable.snowflurry);break;
            case "薄雾":
                view.setImageResource(R.drawable.mist);break;
            case "雾":
                view.setImageResource(R.drawable.foggy);break;
            case "霾":
                view.setImageResource(R.drawable.haze);break;
            case "扬沙":
                view.setImageResource(R.drawable.sand);break;
            case "浮尘":
                view.setImageResource(R.drawable.dust);break;
            case "沙尘暴":
                view.setImageResource(R.drawable.duststorm);break;
            case "强沙尘暴":
                view.setImageResource(R.drawable.sandstorm);break;
            case "热":
                view.setImageResource(R.drawable.hot);break;
            case "冷":
                view.setImageResource(R.drawable.cold);break;
            case "未知":
                view.setImageResource(R.drawable.unknown);break;
                default:view.setImageResource(R.drawable.unknown);break;
        }
    }

    public void ShowDialog(View view){
        View layoutview=null;
        LayoutInflater inflater=LayoutInflater.from(context);
        layoutview=inflater.inflate(R.layout.suggestion_layout,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(context).setView(layoutview);
        dialog=builder.create();
        dialog.show();
//        ((TextView)dialog.findViewById(R.id.suggstion)).setText(todayWeatherData.getchenlian());
        switch (view.getId()){
            case R.id.chenlian:
                ((TextView)dialog.findViewById(R.id.suggstion)).setText(todayWeatherData.getchenlian());break;
            case R.id.shushidu:
                ((TextView)dialog.findViewById(R.id.suggstion)).setText(todayWeatherData.getshushidu());break;
            case R.id.chuanyi:
                ((TextView)dialog.findViewById(R.id.suggstion)).setText(todayWeatherData.getchuanyi());break;
            case R.id.ganmao:
                ((TextView)dialog.findViewById(R.id.suggstion)).setText(todayWeatherData.getganmao());break;
            case R.id.liangshai:
                ((TextView)dialog.findViewById(R.id.suggstion)).setText(todayWeatherData.getliangshai());break;
            case R.id.lvxing:
                ((TextView)dialog.findViewById(R.id.suggstion)).setText(todayWeatherData.getlvyou());break;
            case R.id.ziwaixian:
                ((TextView)dialog.findViewById(R.id.suggstion)).setText(todayWeatherData.getziwaixian());break;
            case R.id.xiche:
                ((TextView)dialog.findViewById(R.id.suggstion)).setText(todayWeatherData.getxiche());break;
            case R.id.yundong:
                ((TextView)dialog.findViewById(R.id.suggstion)).setText(todayWeatherData.getyundong());break;
            case R.id.yuehui:
                ((TextView)dialog.findViewById(R.id.suggstion)).setText(todayWeatherData.getyuehui());break;
            case R.id.yusan:
                ((TextView)dialog.findViewById(R.id.suggstion)).setText(todayWeatherData.getyusan());break;

        }
    }

    public void ShowDialogImage(View view){
        View layoutview=null;
        LayoutInflater inflater=LayoutInflater.from(context);
        layoutview=inflater.inflate(R.layout.forecast_layout,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(context).setView(layoutview);
        dialog=builder.create();
        dialog.show();

        switch (view.getId()){
            case R.id.firstweather:
                ((TextView)dialog.findViewById(R.id.dayweatherforecast)).setText(dayWeatherDatas.get(1).getdaytype());
                ((TextView)dialog.findViewById(R.id.daywinddirectionforecast)).setText(dayWeatherDatas.get(1).getdayfengxiang());
                ((TextView)dialog.findViewById(R.id.daywindpowerforecast)).setText(dayWeatherDatas.get(1).getnightfengli());
                ((TextView)dialog.findViewById(R.id.nightweatherforecast)).setText(dayWeatherDatas.get(1).getnighttype());
                ((TextView)dialog.findViewById(R.id.nightwinddirectionforecast)).setText(dayWeatherDatas.get(1).getnightfengxiang());
                ((TextView)dialog.findViewById(R.id.nightwindpowerforecast)).setText(dayWeatherDatas.get(1).getnightfengli());break;
            case R.id.secondweather:
                ((TextView)dialog.findViewById(R.id.dayweatherforecast)).setText(dayWeatherDatas.get(2).getdaytype());
                ((TextView)dialog.findViewById(R.id.daywinddirectionforecast)).setText(dayWeatherDatas.get(2).getdayfengxiang());
                ((TextView)dialog.findViewById(R.id.daywindpowerforecast)).setText(dayWeatherDatas.get(2).getnightfengli());
                ((TextView)dialog.findViewById(R.id.nightweatherforecast)).setText(dayWeatherDatas.get(2).getnighttype());
                ((TextView)dialog.findViewById(R.id.nightwinddirectionforecast)).setText(dayWeatherDatas.get(2).getnightfengxiang());
                ((TextView)dialog.findViewById(R.id.nightwindpowerforecast)).setText(dayWeatherDatas.get(2).getnightfengli());break;
            case R.id.thirdweather:
                ((TextView)dialog.findViewById(R.id.dayweatherforecast)).setText(dayWeatherDatas.get(3).getdaytype());
                ((TextView)dialog.findViewById(R.id.daywinddirectionforecast)).setText(dayWeatherDatas.get(3).getdayfengxiang());
                ((TextView)dialog.findViewById(R.id.daywindpowerforecast)).setText(dayWeatherDatas.get(3).getnightfengli());
                ((TextView)dialog.findViewById(R.id.nightweatherforecast)).setText(dayWeatherDatas.get(3).getnighttype());
                ((TextView)dialog.findViewById(R.id.nightwinddirectionforecast)).setText(dayWeatherDatas.get(3).getnightfengxiang());
                ((TextView)dialog.findViewById(R.id.nightwindpowerforecast)).setText(dayWeatherDatas.get(3).getnightfengli());break;
            case R.id.fourthweather:
                ((TextView)dialog.findViewById(R.id.dayweatherforecast)).setText(dayWeatherDatas.get(4).getdaytype());
                ((TextView)dialog.findViewById(R.id.daywinddirectionforecast)).setText(dayWeatherDatas.get(4).getdayfengxiang());
                ((TextView)dialog.findViewById(R.id.daywindpowerforecast)).setText(dayWeatherDatas.get(4).getnightfengli());
                ((TextView)dialog.findViewById(R.id.nightweatherforecast)).setText(dayWeatherDatas.get(4).getnighttype());
                ((TextView)dialog.findViewById(R.id.nightwinddirectionforecast)).setText(dayWeatherDatas.get(4).getnightfengxiang());
                ((TextView)dialog.findViewById(R.id.nightwindpowerforecast)).setText(dayWeatherDatas.get(4).getnightfengli());break;
        }
    }
}
