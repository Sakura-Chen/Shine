package com.tuesda.circlerefreshlayout;

import android.os.Parcel;

import java.io.Serializable;
import java.util.Calendar;

//今天天气的特殊参数如城市，更新时间，温度，湿度，太阳升落时间，环境因素等
public class TodayWeatherDataClass implements Serializable {

    private String city;
    private String updatetime;
    private String wendu;
    private String shidu;
    private String sunrise;
    private String sunset;
    private String aqi;
    private String pm25;
    private String quality;
    private String o3;
    private String co;
    private String pm10;
    private String so2;
    private String no2;

    private String chenlian;
    private String shushidu;
    private String chuanyi;
    private String ganmao;
    private String liangshai;
    private String lvyou;
    private String ziwaixian;
    private String xiche;
    private String yundong;
    private String yuehui;
    private String yusan;

    public TodayWeatherDataClass(){}

    protected TodayWeatherDataClass(Parcel in) {
        city = in.readString();
        updatetime = in.readString();
        shidu = in.readString();
        sunrise = in.readString();
        sunset = in.readString();
        aqi = in.readString();
        pm25 = in.readString();
        quality = in.readString();
        o3 = in.readString();
        co = in.readString();
        pm10 = in.readString();
        so2 = in.readString();
        no2 = in.readString();
    }

//    public static final Creator<TodayWeatherDataClass> CREATOR = new Creator<TodayWeatherDataClass>() {
//        @Override
//        public TodayWeatherDataClass createFromParcel(Parcel in) {
//            return new TodayWeatherDataClass(in);
//        }
//
//        @Override
//        public TodayWeatherDataClass[] newArray(int size) {
//            return new TodayWeatherDataClass[size];
//        }
//    };

    public void setcity(String city){
        this.city=city;
    }
    public String getcity(){
        return this.city;
    }
    public void setupdatetime(String updatetime){
//        Calendar c = Calendar.getInstance();
//        this.updatetime= String.valueOf(c.get(Calendar.HOUR_OF_DAY))+c.get(Calendar.MINUTE);
        this.updatetime=updatetime;
    }
    public String getupdatetime(){
        return this.updatetime;
    }
    public void setwendu(String wendu){
        this.wendu=wendu;
    }
    public String getwendu(){
        return this.wendu;
    }
    public void setshidu(String shidu){
        this.shidu=shidu;
    }
    public String getshidu(){
        return this.shidu;
    }
    public void setsunrise(String sunrise){
        this.sunrise=sunrise;
    }
    public String getsunrise(){
        return this.sunrise;
    }
    public void setsunset(String sunset){
        this.sunset=sunset;
    }
    public String getsunset(){
        return this.sunset;
    }
    public void setaqi(String aqi){
        this.aqi=aqi;
    }
    public String getaqi(){
        return this.aqi;
    }
    public void setpm25(String pm25){
        this.pm25=pm25;
    }
    public String getpm25(){
        return this.pm25;
    }
    public void setquality(String quality){
        this.quality=quality;
    }
    public String getquality(){
        return this.quality;
    }
    public void seto3(String o3){
        this.o3=o3;
    }
    public String geto3(){
        return this.o3;
    }
    public void setco(String co){
        this.co=co;
    }
    public String getco(){
        return this.co;
    }
    public void setpm10(String pm10){
        this.pm10=pm10;
    }
    public String getpm10(){
        return this.pm10;
    }
    public void setso2(String so2){
        this.so2=so2;
    }
    public String getso2(){
        return this.so2;
    }
    public void setno2(String no2){
        this.no2=no2;
    }
    public String getno2(){
        return this.no2;
    }
    public void setchenlian(String chenlian){
        this.chenlian=chenlian;
    }

    public String getchenlian(){
        return this.chenlian;
    }
    public void setshushidu(String shushidu){
        this.shushidu=shushidu;
    }
    public String getshushidu(){
        return this.shushidu;
    }
    public void setchuanyi(String chuanyi){
        this.chuanyi=chuanyi;
    }
    public String getchuanyi(){
        return this.chuanyi;
    }
    public void setganmao(String ganmao){
        this.ganmao=ganmao;
    }
    public String getganmao(){
        return this.ganmao;
    }
    public void setliangshai(String liangshai){
        this.liangshai=liangshai;
    }
    public String getliangshai(){
        return this.liangshai;
    }
    public void setlvyou(String lvyou){
        this.lvyou=lvyou;
    }
    public String getlvyou(){
        return this.lvyou;
    }
    public void setziwaixian(String ziwaixian){
        this.ziwaixian=ziwaixian;
    }
    public String getziwaixian(){
        return this.ziwaixian;
    }
    public void setxiche(String xiche){
        this.xiche=xiche;
    }
    public String getxiche(){
        return this.xiche;
    }
    public void setyundong(String yundong){
        this.yundong=yundong;
    }
    public String getyundong(){
        return this.yundong;
    }
    public void setyuehui(String yuehui){
        this.yuehui=yuehui;
    }
    public String getyuehui(){
        return this.yuehui;
    }
    public void setyusan(String yusan){
        this.yusan=yusan;
    }
    public String getyusan(){
        return this.yusan;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(city);
//        dest.writeString(updatetime);
//        dest.writeString(shidu);
//        dest.writeString(sunrise);
//        dest.writeString(sunset);
//        dest.writeString(aqi);
//        dest.writeString(pm25);
//        dest.writeString(quality);
//        dest.writeString(o3);
//        dest.writeString(co);
//        dest.writeString(pm10);
//        dest.writeString(so2);
//        dest.writeString(no2);
//    }
}