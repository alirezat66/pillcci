package greencode.ir.pillcci.objects;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by alireza on 5/21/18.
 */

public class EachUsage implements Comparable<EachUsage>, Serializable {
    String timeStr;
    String unit;
    String EachUse;
    long startDay;
    boolean isStart;
    public EachUsage(String timeStr, String unit, String eachUse,long startDay,boolean isStart) {
        this.timeStr = timeStr;
        this.unit = unit;
        EachUse = eachUse;
        this.startDay =startDay;
        this.isStart = isStart;
    }

    public boolean isStart() {
        return isStart;
    }

    public String getTimeStr() {
        String[] times = timeStr.split(":");
        String finalTime="";
        for(String ti:times){
            int tiInt =Integer.parseInt(ti);
            if(tiInt>10){
                finalTime= finalTime+tiInt+"";
            }else {
                finalTime=finalTime+"0"+tiInt;
            }
            finalTime+=":";
        }
        finalTime = finalTime.substring(0,finalTime.length()-1);
        return finalTime;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getEachUse() {
        return EachUse;
    }

    public void setEachUse(String eachUse) {
        EachUse = eachUse;
    }


    public boolean isNight(){
        String[]hoursAndMin = timeStr.split(":");
        int hours = Integer.parseInt( hoursAndMin[0]);
        int min = Integer.parseInt( hoursAndMin[1]);
         if((hours>=22 && hours<24 )|| (hours>=0 && hours<=5)){
             return true;
         }else {
             return false;
         }
    }

    public long getStartDay() {
        return startDay;
    }

    public void setStartDay(long startDay) {
        this.startDay = startDay;
    }

    @Override
    public int compareTo(@NonNull EachUsage o) {
        if (startDay > o.getStartDay()) {
            return 1;
        }
        else if (startDay <  o.getStartDay()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
