package greencode.ir.pillcci.objects;

import java.util.ArrayList;

/**
 * Created by alireza on 5/21/18.
 */

public class UsageFields {
    int typedayUsage;
    String usageStartDate;
    boolean isRegular;
    ArrayList<String>days;
    int typeOfdayUsage;
    int repeatDays ;
    double diffrenceOfUsage;
    int countOfUsagePerDay;
    int startHour;
    int startMin;
    long startTimeStamp;
    String unitUsage;
    ArrayList<String>unitsCount;
    ArrayList<String>unitTimes;
    String description;
    public UsageFields(int typedayUsage, String usageStartDate, int repeatDays,boolean isRegular, ArrayList<String> days, int typeOfdayUsage, double diffrenceOfUsage, int countOfUsagePerDay, int startHour, int startMin, String unitUsage, ArrayList<String> unitsCount, ArrayList<String> unitTimes,String description,long startTimeStamp) {
        this.typedayUsage = typedayUsage;
        this.usageStartDate = usageStartDate;
        this.isRegular = isRegular;
        this.days = days;
        this.typeOfdayUsage = typeOfdayUsage;
        this.diffrenceOfUsage = diffrenceOfUsage;
        this.countOfUsagePerDay = countOfUsagePerDay;
        this.startHour = startHour;
        this.startMin = startMin;
        this.unitUsage = unitUsage;
        this.unitsCount = unitsCount;
        this.unitTimes = unitTimes;
        this.description = description;
        this.repeatDays = repeatDays;
        this.startTimeStamp = startTimeStamp;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public int getRepeatDays() {
        return repeatDays;
    }

    public String getDescription() {
        return description;
    }

    public int getTypedayUsage() {
        return typedayUsage;
    }

    public String getUsageStartDate() {
        return usageStartDate;
    }

    public boolean isRegular() {
        return isRegular;
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public int getTypeOfdayUsage() {
        return typeOfdayUsage;
    }

    public double getDiffrenceOfUsage() {
        return diffrenceOfUsage;
    }

    public int getCountOfUsagePerDay() {
        return countOfUsagePerDay;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public String getUnitUsage() {
        return unitUsage;
    }

    public ArrayList<String> getUnitsCount() {
        return unitsCount;
    }

    public ArrayList<String> getUnitTimes() {
        return unitTimes;
    }
}
