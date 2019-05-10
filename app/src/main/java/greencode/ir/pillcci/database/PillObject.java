package greencode.ir.pillcci.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import greencode.ir.pillcci.utils.PersianCalculater;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/22/18.
 */
@Entity(tableName = "pil")
public class PillObject {
    @PrimaryKey(autoGenerate = false)
    int id;
    String b64;// image of pill
    String midname; // name of pill
    String causeOfUse;// reason of use;
    String drName;// dr name that write this pill

    String catName; // category name : default value  is "public"
    int catColor; // it has default to
    String catring;// it has default too

    int typeOfUsage; // 1 = every day ## 2= some days with regular repeat ## 3= difrent days inweek // 4= birth control;
    int isRegular;// if type 1 or 2 this is true and for 3 this is false
    int repeatUsageDay;// if type=2 this is reapeat of usage;
    String days; // list of days that we should use pill , it seprate with ,
    double diffrenceOfUsage;// diffrence between two usage per hours for example 3 means between this time and next time we has 3 hours
    int countOfUsagePerDay;//count of usage per day
    int startHour;// initial and first start hours
    int startMin;// initial and first start min
    String unitUse; // unit of usage
    String unitCount; // its amount of usage for each usage that seprate with ,
    String unitTimes;//its time of usage time for each usage in day that seprate with ,
    int useType; //1= continuesly with unknown dead line ## 2=  finished by finish days ## 3 == finished by finish allusecount;
    int allUseDays;// days that we used this pill
    double totalAmounts;// amount that we finished usage of this pill
    long firstAlarmTime; // nearest time we should use this med
    int daysOfUse; // days that we used pill
    double amountOfUse ; // amount pill that we used
    String description;
    int hasLight;
    int hasVibrate;
    int reminderDays;// tedade rooz hayi ke ghabl az etmam bayad yad avari konim
    double allPillCount;//tedade kole darooye mojood;
    int state;// 1==active 2== deactive 3==deleted 4==finished
    int isSync;
    public void setAllPillCount(double allPillCount) {
        this.allPillCount = allPillCount;
    }

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
        return causeOfUse;
    }

    public String getDrName() {
        if(drName.equals("")){
            return "نامشخص";
        }
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

    public int isRegular() {
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
        return startHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public String getUnitUse() {
        return unitUse;
    }

    public String getUnitsCount() {
        return unitCount;
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
        PersianDate date = new PersianDate(firstAlarmTime);
        date.setSecond(0);
        return date.getTime();
    }

    public int getDaysOfUse() {
        return daysOfUse;
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

 /*   public PillObject(String b64, String midname, String couseOfUse, String drName, String catName,
                      int catColor, String catring, int typeOfUsage, boolean isRegular,
                      int repeatUsageDay, String days, double diffrenceOfUsage, int countOfUsagePerDay,
                      int stardHour, int startMin, String unitUse, String unitsCount, String unitTimes,
                      int useType, int allUseDays, double totalAmounts, long firstAlarmTime, int daysOfUse,
                      double amountOfUse, String description, int reminderDays, double allPillCount,
                      int hasLight,int hasVibrate) {
        this.b64 = b64;
        this.midname = midname;
        this.couseOfUse = couseOfUse;
        this.drName = drName;
        this.catName = catName;
        this.catColor = catColor;
        this.catring = catring;
        this.typeOfUsage = typeOfUsage;
        this.isRegular = isRegular;
        this.repeatUsageDay = repeatUsageDay;
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
        this.daysOfUse = daysOfUse;
        this.amountOfUse = amountOfUse;
        this.description = description;
        this.reminderDays = reminderDays;
        this.allPillCount = allPillCount;
        this.state = 1;
        this.hasLight=hasLight;
        this.hasVibrate=hasVibrate;
        this.isSync = false;
    }*/

    public PillObject(int id, String b64, String midname,
                      String causeOfUse, String drName,
                      String catName, int catColor, String catring,
                      int typeOfUsage, int isRegular,
                      int repeatUsageDay, String days,
                      double diffrenceOfUsage, int countOfUsagePerDay,
                      int startHour, int startMin,
                      String unitUse, String unitCount,
                      String unitTimes, int useType, int allUseDays, double totalAmounts,
                      long firstAlarmTime, int daysOfUse, double amountOfUse, String description,
                      int hasLight, int hasVibrate, int reminderDays, double allPillCount, int state, int isSync) {
        this.id = id;
        this.b64 = b64;
        this.midname = midname;
        this.causeOfUse = causeOfUse;
        this.drName = drName;
        this.catName = catName;
        this.catColor = catColor;
        this.catring = catring;
        this.typeOfUsage = typeOfUsage;
        this.isRegular = isRegular;
        this.repeatUsageDay = repeatUsageDay;
        this.days = days;
        this.diffrenceOfUsage = diffrenceOfUsage;
        this.countOfUsagePerDay = countOfUsagePerDay;
        this.startHour = startHour;
        this.startMin = startMin;
        this.unitUse = unitUse;
        this.unitCount = unitCount;
        this.unitTimes = unitTimes;
        this.useType = useType;
        this.allUseDays = allUseDays;
        this.totalAmounts = totalAmounts;
        this.firstAlarmTime = firstAlarmTime;
        this.daysOfUse = daysOfUse;
        this.amountOfUse = amountOfUse;
        this.description = description;
        this.hasLight = hasLight;
        this.hasVibrate = hasVibrate;
        this.reminderDays = reminderDays;
        this.allPillCount = allPillCount;
        this.state = state;
        this.isSync = isSync;
    }



    public void setSync(int sync) {
        isSync = sync;
    }

    public int isSync() {
        return isSync;
    }



    public int getLight() {
        return hasLight;
    }

    public int getVibrate() {
        return hasVibrate;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getReminderDays() {
        return reminderDays;
    }

    public double getAllPillCount() {
        return allPillCount;
    }

    public void setFristAlarmTime(long fristAlarmTime) {
        this.firstAlarmTime = fristAlarmTime;
    }

    public String getshowDays() {
        PersianDate date = new PersianDate(firstAlarmTime);
        String startDate = date.getShYear()+"/"+date.getShMonth()+"/"+date.getShDay();
        if(typeOfUsage==1)
        {

            return "هر روز شروع از:"+ startDate;

        }else if(typeOfUsage==2){
            return "هر "+ repeatUsageDay+" روز یکبار شروع از : "+ startDate;
        }else if(typeOfUsage==3){
            return "روزهای "+ days +" شروع از : "+startDate;
        }else {
            String[]dayArr = days.split(",");
            return "چرخه ضد بارداری، روزهای مصرف "+dayArr[0]+" روز" + " و روزهای توقف "+dayArr[1]+" روز";
        }
    }

    public String getEachTime() {
        String[]usageCount = unitCount.split(",");
        boolean isSame = true;
        for(String usage : usageCount){
            if(!usage.equals(usageCount[0])){
                isSame=false;
            }
        }
        for(int i=0;i< usageCount.length;i++){
            try {

                double count = Double.parseDouble(usageCount[i]);
                if(count-(int)count==0){
                    usageCount[i] =  (int)count+"";
                }

            }catch (NumberFormatException ex){

                ex.printStackTrace();
            }
        }


        if(isSame) {


            return usageCount[0] + " " + unitUse;
        }else {
            return unitCount+" "+unitUse;
        }


    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getuseDays() {
        String finalString;
        if(typeOfUsage==1){
            finalString="هر روز ";
        }else if(typeOfUsage==2){
            finalString = "هر " + repeatUsageDay +" روز ";
        }else if(typeOfUsage==3){
            finalString = "روزهای "+days+" \n";
        }else {
            finalString = "چرخه ضد بارداری ";
        }
        finalString=finalString+"-شروع از " +PersianCalculater.getYearMonthAndDay(firstAlarmTime);
        return finalString;
    }

    public String getusepartDate() {

        return"هر "+(int)diffrenceOfUsage+" ساعت";
    }

    public String getEachUseTime() {
        String[]usageCount = unitCount.split(",");
        String finalStr = "";
        boolean isSame = true;
        for(int i = 0 ; i<usageCount.length-1;i++){
            if(!usageCount[i].equals(usageCount[i+1])){
                isSame=false;
                break;
            }
        }
        if(!isSame){
            finalStr=" به ترتیب ";
            for(String usage:usageCount){
                double doubleCount = Double.parseDouble(usage);
                if(doubleCount-(int)doubleCount==0){
                    finalStr+=(int)doubleCount+",";
                }else {
                    finalStr+=doubleCount+",";
                }
            }
            finalStr =finalStr.substring(0,finalStr.length()-1);
            finalStr= finalStr+" "+unitUse+" در ساعت(های):";

        }else {
            double doubleCount = Double.parseDouble(usageCount[0]);

            if(doubleCount-(int)doubleCount==0){
                finalStr+=(int)doubleCount+",";
            }else {
                finalStr+=doubleCount+",";
            }
            finalStr =finalStr.substring(0,finalStr.length()-1);
            finalStr= finalStr+" "+unitUse+" در ساعت(های):";
        }
        String[]usageTime = unitTimes.split(",");
        String times = "";
        for(String usage:usageTime){
            long timeStamp = Long.parseLong(usage);
            times+= PersianCalculater.getHourseAndMin(timeStamp)+",";
        }
        times = times.substring(0,times.length()-1);
        finalStr+= times;
        return finalStr;
    }


    public void setB64(String b64) {
        this.b64 = b64;
    }

    public void setMidname(String midname) {
        this.midname = midname;
    }

    public void setCouseOfUse(String couseOfUse) {
        this.causeOfUse = couseOfUse;
    }

    public void setDrName(String drName) {
        this.drName = drName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public void setCatColor(int catColor) {
        this.catColor = catColor;
    }

    public void setCatring(String catring) {
        this.catring = catring;
    }

    public void setTypeOfUsage(int typeOfUsage) {
        this.typeOfUsage = typeOfUsage;
    }

    public void setRegular(int regular) {
        isRegular = regular;
    }

    public void setRepeatUsageDay(int repeatUsageDay) {
        this.repeatUsageDay = repeatUsageDay;
    }

    public void setDiffrenceOfUsage(double diffrenceOfUsage) {
        this.diffrenceOfUsage = diffrenceOfUsage;
    }

    public void setCountOfUsagePerDay(int countOfUsagePerDay) {
        this.countOfUsagePerDay = countOfUsagePerDay;
    }

    public void setStardHour(int stardHour) {
        this.startHour = stardHour;
    }

    public void setStartMin(int startMin) {
        this.startMin = startMin;
    }

    public void setUnitUse(String unitUse) {
        this.unitUse = unitUse;
    }

    public void setUnitsCount(String unitsCount) {
        this.unitCount = unitsCount;
    }

    public void setUnitTimes(String unitTimes) {
        this.unitTimes = unitTimes;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    public void setAllUseDays(int allUseDays) {
        this.allUseDays = allUseDays;
    }

    public void setTotalAmounts(double totalAmounts) {
        this.totalAmounts = totalAmounts;
    }

    public void setFirstAlarmTime(long firstAlarmTime) {
        this.firstAlarmTime = firstAlarmTime;
    }

    public void setDaysOfUse(int daysOfUse) {
        this.daysOfUse = daysOfUse;
    }

    public void setAmountOfUse(double amountOfUse) {
        this.amountOfUse = amountOfUse;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHasLight(int hasLight) {
        this.hasLight = hasLight;
    }

    public void setHasVibrate(int hasVibrate) {
        this.hasVibrate = hasVibrate;
    }

    public void setReminderDays(int reminderDays) {
        this.reminderDays = reminderDays;
    }
}
