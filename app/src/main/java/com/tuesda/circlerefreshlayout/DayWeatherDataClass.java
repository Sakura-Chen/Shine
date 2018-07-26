package com.tuesda.circlerefreshlayout;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

//预报天气状况
public class DayWeatherDataClass implements Serializable {

    private String date;
    private String high;
    private String low;
    private String daytype;
    private String dayfengxiang;
    private String dayfengli;
    private String nighttype;
    private String nightfengxiang;
    private String nightfengli;



    public DayWeatherDataClass(){}

    protected DayWeatherDataClass(Parcel in) {
        date = in.readString();
        high = in.readString();
        low = in.readString();
        daytype = in.readString();
        dayfengxiang = in.readString();
        dayfengli = in.readString();
        nighttype = in.readString();
        nightfengxiang = in.readString();
        nightfengli = in.readString();
    }

    public static final Parcelable.Creator<DayWeatherDataClass> CREATOR = new Parcelable.Creator<DayWeatherDataClass>() {
        @Override
        public DayWeatherDataClass createFromParcel(Parcel in) {
            return new DayWeatherDataClass(in);
        }

        @Override
        public DayWeatherDataClass[] newArray(int size) {
            return new DayWeatherDataClass[size];
        }
    };

    public void setdate(String date){
        this.date=date.substring(date.length()-3,date.length());
    }
    public String getdate(){
        return this.date;
    }
    public void sethigh(String high){
        this.high=high.substring(3,high.length()-1);
    }
    public String gethigh(){
        return this.high;
    }
    public void setlow(String low){
        this.low=low.substring(3,low.length()-1);
    }
    public String getlow(){
        return this.low;
    }
    public void setdaytype(String daytype){
        this.daytype=daytype;
    }
    public String getdaytype(){
        return this.daytype;
    }
    public void setdayfengxiang(String dayfengxiang){
        this.dayfengxiang=dayfengxiang;
    }
    public String getdayfengxiang(){
        return this.dayfengxiang;
    }
    public void setdayfengli(String dayfengli){
        this.dayfengli=dayfengli;
    }
    public String getdayfengli(){
        return this.dayfengli;
    }
    public void setnighttype(String nighttype){
        this.nighttype=nighttype;
    }
    public String getnighttype(){
        return this.nighttype;
    }
    public void setnightfengxiang(String nightfengxiang){
        this.nightfengxiang=nightfengxiang;
    }
    public String getnightfengxiang(){
        return this.nightfengxiang;
    }
    public void setnightfengli(String nightfengli){
        this.nightfengli=nightfengli;
    }
    public String getnightfengli(){
        return this.nightfengli;
    }

}

