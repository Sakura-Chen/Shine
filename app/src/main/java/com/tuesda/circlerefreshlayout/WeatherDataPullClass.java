package com.tuesda.circlerefreshlayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//解析XML数据并以类形式保存
public class WeatherDataPullClass {

    private List<DayWeatherDataClass> dayWeatherDataClasses;
    private TodayWeatherDataClass todayWeatherDataClass;
    private DayWeatherDataClass dayWeatherDataClass;

    public WeatherDataPullClass(InputStream inputStream) throws IOException {

//        System.out.println("开始解析");
        try {
            XmlPullParserFactory xmlPullParserFactory=XmlPullParserFactory.newInstance();
            XmlPullParser parser= xmlPullParserFactory.newPullParser();
            parser.setInput(inputStream,"UTF-8");
            int eventType=parser.getEventType();
            String str="";
            while(eventType!=XmlPullParser.END_DOCUMENT){
                if(eventType==XmlPullParser.START_DOCUMENT){
                    String tagName=parser.getName();
//                System.out.println("遇到文档开始"+tagName);
                    dayWeatherDataClasses=new ArrayList<DayWeatherDataClass>();
                }
                else if(eventType==XmlPullParser.START_TAG){
                    String tagName=parser.getName();
//                System.out.println("遇到标签开始"+tagName);
                    tagName=str.concat(tagName);
                    switch (tagName) {
                        case "resp":
                            todayWeatherDataClass = new TodayWeatherDataClass();break;
                        case "city":
                            todayWeatherDataClass.setcity(parser.nextText());break;
                        case "updatetime":
                            todayWeatherDataClass.setupdatetime(parser.nextText());break;
                        case "wendu":
                            todayWeatherDataClass.setwendu(parser.nextText());break;
                        case "shidu":
                            todayWeatherDataClass.setshidu(parser.nextText());break;
                        case "sunrise_1":
                            todayWeatherDataClass.setsunrise(parser.nextText());break;
                        case "sunset_1":
                            todayWeatherDataClass.setsunset(parser.nextText());break;
                        case "aqi":
                            todayWeatherDataClass.setaqi(parser.nextText());break;
                        case "pm25":
                            todayWeatherDataClass.setpm25(parser.nextText());break;
                        case "quality":
                            todayWeatherDataClass.setquality(parser.nextText());break;
                        case "o3":
                            todayWeatherDataClass.seto3(parser.nextText());break;
                        case "co":
                            todayWeatherDataClass.setco(parser.nextText());break;
                        case "pm10":
                            todayWeatherDataClass.setpm10(parser.nextText());break;
                        case "so2":
                            todayWeatherDataClass.setso2(parser.nextText());break;
                        case "no2":
                            todayWeatherDataClass.setno2(parser.nextText());break;
                        case "weather":
                            dayWeatherDataClass=new DayWeatherDataClass();break;
                        case "date":
                            dayWeatherDataClass.setdate(parser.nextText());break;
                        case "high":
                            dayWeatherDataClass.sethigh(parser.nextText());break;
                        case "low":
                            dayWeatherDataClass.setlow(parser.nextText());break;
                        case "day":
                            str="day";break;
                        case "daytype":
                            dayWeatherDataClass.setdaytype(parser.nextText());
//                            System.out.println(" "+dayWeatherDataClass.getdaytype());
                            break;
                        case "dayfengxiang":
                            dayWeatherDataClass.setdayfengxiang(parser.nextText());break;
                        case "dayfengli":
                            dayWeatherDataClass.setdayfengli(parser.nextText());break;
                        case "night":
                            str="night";break;
                        case "nighttype":
                            dayWeatherDataClass.setnighttype(parser.nextText());
//                            System.out.println(dayWeatherDataClass.getnighttype());
                            break;
                        case "nightfengxiang":
                            dayWeatherDataClass.setnightfengxiang(parser.nextText());break;
                        case "nightfengli":
                            dayWeatherDataClass.setnightfengli(parser.nextText());break;
                        case "name":
                            switch (parser.nextText()){
                                case "晨练指数":
                                    str="chenlian";break;
                                case "舒适度":
                                    str="shushi";break;
                                case "穿衣指数":
                                    str="chuanyi";break;
                                case "感冒指数":
                                    str="ganmao";break;
                                case "晾晒指数":
                                    str="liangshai";break;
                                case "旅游指数":
                                    str="lvyou";break;
                                case "紫外线强度":
                                    str="ziwaixian";break;
                                case "洗车指数":
                                    str="xiche";break;
                                case "运动指数":
                                    str="yundong";break;
                                case "约会指数":
                                    str="yuehui";break;
                                case "雨伞指数":
                                    str="yusan";break;
                            }break;
                        case "chenliandetail":
                            todayWeatherDataClass.setchenlian(parser.nextText());break;
                        case "shushidetail":
                            todayWeatherDataClass.setshushidu(parser.nextText());break;
                        case "chuanyidetail":
                            todayWeatherDataClass.setchuanyi(parser.nextText());break;
                        case "ganmaodetail":
                            todayWeatherDataClass.setganmao(parser.nextText());break;
                        case "liangshaidetail":
                            todayWeatherDataClass.setliangshai(parser.nextText());break;
                        case "lvyoudetail":
                            todayWeatherDataClass.setlvyou(parser.nextText());break;
                        case "ziwaixiandetail":
                            todayWeatherDataClass.setziwaixian(parser.nextText());break;
                        case "xichedetail":
                            todayWeatherDataClass.setxiche(parser.nextText());break;
                        case "yundongdetail":
                            todayWeatherDataClass.setyundong(parser.nextText());break;
                        case "yuehuidetail":
                            todayWeatherDataClass.setyuehui(parser.nextText());break;
                        case "yusandetail":
                            todayWeatherDataClass.setyusan(parser.nextText());break;
                    }
                }
                else if(eventType==XmlPullParser.END_TAG){
                    String tagName=parser.getName();
                    if(tagName!=null){
                        switch (tagName) {
                            case "weather":
                                dayWeatherDataClasses.add(dayWeatherDataClass);
                                dayWeatherDataClass=null;break;
                            case "day":
                                str="";break;
                            case "night":
                                str="";break;
                            case "zhishu":
                                str="";break;
                        }
                    }
                }
                eventType=parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public List<DayWeatherDataClass> getDayWeatherDataClasses() {
        return dayWeatherDataClasses;
    }

    public TodayWeatherDataClass getTodayWeatherDataClass() {
        return todayWeatherDataClass;
    }
}
