package greencode.ir.pillcci.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.utils.PersianCalculater;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/22/18.
 */
@Entity(tableName = "pilusage")
public class PillUsage {
    @PrimaryKey(autoGenerate = true)
    long id;
    int pillId;
    String pillName;//name of pill
    String time;
    long usageTime;// time of usage
    int state ; // 0 = not used ## 1 = used ## 2=canceled by user ## 3= any action not set
    String useTime;// time that we use pill
    boolean hasDelay;// has any delay
    String description;
    String catNme;
    int catColor;
    String catRingtone;
    String drName;
    String unit;
    String unitAmount;//unit use amount
    String countPerDay;
    long usedTime;
    int snoozCount;
    long setedTime;
    public PillUsage(int pillId,String pillName, String time, long usageTime,
                     int state, String useTime, boolean hasDelay, String description, String catNme,
                     int catColor, String catRingtone, String drName, String unit, String unitAmount,
                     String countPerDay,long usedTime,long setedTime) {
        this.pillId = pillId;
        this.pillName = pillName;
        this.time = time;
        this.usageTime = usageTime;
        this.state = state;
        this.useTime = useTime;
        this.hasDelay = hasDelay;
        this.description = description;
        this.catNme = catNme;
        this.catColor = catColor;
        this.catRingtone = catRingtone;
        this.drName = drName;
        this.unit = unit;
        this.unitAmount = unitAmount;
        this.countPerDay = countPerDay;
        this.usedTime=usedTime;
        this.snoozCount =0;
        this.setedTime = setedTime;
    }

    public long getSetedTime() {
        return setedTime;
    }

    public int getPillId() {
        return pillId;
    }

    public long getid() {
        return id;
    }

    public String getPillName() {
        return pillName;
    }

    public String getTime() {
        return time;
    }

    public long getUsageTime() {
        return usageTime;
    }

    public int getState() {
        return state;
    }

    public String getUseTime() {
        return useTime;
    }

    public boolean isHasDelay() {
        return hasDelay;
    }

    public String getDescription() {
        return description;
    }

    public String getCatNme() {
        return catNme;
    }

    public int getCatColor() {
        return catColor;
    }

    public String getCatRingtone() {
        return catRingtone;
    }

    public String getDrName() {
        return drName;
    }

    public String getUnit() {
        return unit;
    }

    public String getUnitAmount() {
        try {

            double count = Double.parseDouble(unitAmount);
            if(count-(int)count==0) {
                return (int)count+"";
            }else {
                return unitAmount;
            }

        }catch (NumberFormatException ex){

            ex.printStackTrace();
            return unitAmount;
        }
    }

    public String getCountPerDay() {
        return countPerDay;
    }

    public long getUsedTime() {
        return usedTime;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUsageTime(long usageTime) {
        this.usageTime = usageTime;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public void setHasDelay(boolean hasDelay) {
        this.hasDelay = hasDelay;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCatNme(String catNme) {
        this.catNme = catNme;
    }

    public void setCatColor(int catColor) {
        this.catColor = catColor;
    }

    public void setCatRingtone(String catRingtone) {
        this.catRingtone = catRingtone;
    }

    public void setDrName(String drName) {
        this.drName = drName;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setUnitAmount(String unitAmount) {
        this.unitAmount = unitAmount;
    }

    public void setCountPerDay(String countPerDay) {
        this.countPerDay = countPerDay;
    }

    public void setUsedTime(long usedTime) {
        this.usedTime = usedTime;
    }

    public int getSnoozCount() {
        return snoozCount;
    }

    public String getPersianUsageTime(){

        long timeStamp = (state==0?usageTime:usedTime);
        PersianDate date=new PersianDate(timeStamp);
        return PersianCalculater.getYearMonthAndDay(date.getTime())+" - "+PersianCalculater.getHourseAndMin(date.getTime());
    }
    public void setSnoozCount(int snoozCount) {
        this.snoozCount = snoozCount;
    }

    public String getStateText() {
        String stateText ="";
        if(state==0){
            stateText="در انتظار مصرف";
        }else if(state==1){
            stateText="مصرف شده";
        }else if(state==2){
            stateText="مصرف نشده";
        }
        return stateText;
    }


    public int getStateColor(Context context) {
        int stateColor;
        if(state == 1){
            stateColor = context.getResources().getColor(R.color.teal);
        }else if(state ==0){
            stateColor = context.getResources().getColor(R.color.orange);
        }else {
            stateColor = context.getResources().getColor(R.color.colorAccent);
        }
        return stateColor;
    }
}
