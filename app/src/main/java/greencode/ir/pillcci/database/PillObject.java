package greencode.ir.pillcci.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;

/**
 * Created by alireza on 5/22/18.
 */
@Entity(tableName = "pil")

public class PillObject {
    @PrimaryKey(autoGenerate = true)
    int id;
    String b64;// image of pill
    String midname; // name of pill
    String couseOfUse;// reason of use;
    String drName;// dr name that write this pill

    String catName; // category name : default value  is "public"
    int catColor; // it has default too
    String catring;// it has default too


    int typeOfUsage; // 1 = every day ## 2= some days with regular repeat ## 3= difrent days inweek;
    boolean isRegular;// if type 1 or 2 this is true and for 3 this is false
    int repeatUsageDay;// if type=2 this is reapeat of usage;
    String days; // list of days that we should use pill , it seprate with ,
    double diffrenceOfUsage;// diffrence between two usage per hours for example 3 means between this time and next time we has 3 hours
    int countOfUsagePerDay;//count of usage per day
    int stardHour;// initial and first start hours
    int startMin;// initial and first start min
    String unitUse; // unit of usage
    String unitsCount; // its amount of usage for each usage that seprate with ,
    String unitTimes;//its time of usage time for each usage in day that seprate with ,
    int useType; //1= continuesly with unknown dead line ## 2=  finished by finish days ## 3 == finished by finish allusecount;
    int allUseDays;// days that we used this pill
    double totalAmounts;// amount that we finished usage of this pill
    long firstAlarmTime; // nearest time we should use this med
    int daysOdUse; // days that we used pill
    double amountOfUse ; // amount pill that we used
    String description;

    public int getid() {
        return id;
    }

    public String getB64() {
        return b64;
    }

    public String getMidname() {
        return midname;
    }

    public String getCouseOfUse() {
        return couseOfUse;
    }

    public String getDrName() {
        return drName;
    }

    public String getCatName() {
        return catName;
    }

    public int getCatColor() {
        return catColor;
    }

    public String getCatring() {
        return catring;
    }

    public int getTypeOfUsage() {
        return typeOfUsage;
    }

    public boolean isRegular() {
        return isRegular;
    }

    public String getDays() {
        return days;
    }

    public double getDiffrenceOfUsage() {
        return diffrenceOfUsage;
    }

    public int getCountOfUsagePerDay() {
        return countOfUsagePerDay;
    }

    public int getStardHour() {
        return stardHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public String getUnitUse() {
        return unitUse;
    }

    public String getUnitsCount() {
        return unitsCount;
    }

    public String getUnitTimes() {
        return unitTimes;
    }

    public int getUseType() {
        return useType;
    }

    public int getAllUseDays() {
        return allUseDays;
    }

    public double getTotalAmounts() {
        return totalAmounts;
    }

    public long getFirstAlarmTime() {
        return firstAlarmTime;
    }

    public int getDaysOdUse() {
        return daysOdUse;
    }

    public double getAmountOfUse() {
        return amountOfUse;
    }

    public int getRepeatUsageDay() {
        return repeatUsageDay;
    }

    public String getDescription() {
        return description;
    }

    public PillObject(String b64, String midname, String couseOfUse, String drName, String catName, int catColor, String catring, int typeOfUsage, boolean isRegular, int repeatUsageDay, String days, double diffrenceOfUsage, int countOfUsagePerDay, int stardHour, int startMin, String unitUse, String unitsCount, String unitTimes, String description, int useType, int allUseDays, double totalAmounts, long firstAlarmTime, int daysOdUse, double amountOfUse) {
        this.b64 = b64;
        this.midname = midname;
        this.couseOfUse = couseOfUse;
        this.drName = drName;
        this.catName = catName;
        this.catColor = catColor;
        this.catring = catring;
        this.typeOfUsage = typeOfUsage;
        this.isRegular = isRegular;
        this.days = days;
        this.diffrenceOfUsage = diffrenceOfUsage;
        this.countOfUsagePerDay = countOfUsagePerDay;
        this.stardHour = stardHour;
        this.startMin = startMin;
        this.unitUse = unitUse;
        this.unitsCount = unitsCount;
        this.unitTimes = unitTimes;
        this.useType = useType;
        this.allUseDays = allUseDays;
        this.totalAmounts = totalAmounts;
        this.firstAlarmTime = firstAlarmTime;
        this.daysOdUse = daysOdUse;
        this.amountOfUse = amountOfUse;
        this.repeatUsageDay=repeatUsageDay;
        this.description =description;
    }
}
