package greencode.ir.pillcci.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by alireza on 5/22/18.
 */
@Entity(tableName = "pilusage")
public class PillUsage {
    @PrimaryKey(autoGenerate = true)
    long id;
    int pillId;
    String pillName;
    String time;
    long usageTime;
    int state ; // 0 = not used ## 1 = used ## 3=canceled by user
    String useTime;// time that we use pill
    boolean hasDelay;
    String description;
    String catNme;
    int catColor;
    String catRingtone;
    String drName;
    String unit;
    String unitAmount;
    String countPerDay;
    long usedTime;
    int snoozCount;
    public PillUsage(int pillId,String pillName, String time, long usageTime, int state, String useTime, boolean hasDelay, String description, String catNme, int catColor, String catRingtone, String drName, String unit, String unitAmount, String countPerDay,long usedTime) {
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
        return unitAmount;
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

    public void setSnoozCount(int snoozCount) {
        this.snoozCount = snoozCount;
    }
}
