package greencode.ir.pillcci.utils;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.objects.EndUseFields;
import greencode.ir.pillcci.objects.GeneralFields;
import greencode.ir.pillcci.objects.UsageFields;
import greencode.ir.pillcci.retrofit.SyncController;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/22/18.
 */

public class CalcTimesAndSaveUsage {

    public static PillUsage makepillUsage(PillObject pillObject, long timeStamp, int j,long id) {

        String pillName=pillObject.getMidname();
        PersianDate persianCalendar = new PersianDate(timeStamp);

        String time=PersianCalculater.getHourseAndMin(persianCalendar.getTime());
        long usageTime=timeStamp;
        int state =0; // 0 = not used ## 1 = used
        String useTime="";// time that we use pill
        long usedTime = 0;
        int hasDelay=0;
        String description=pillObject.getDescription();
        String catNme=pillObject.getCatName();
        int catColor=pillObject.getCatColor();
        String catRingtone=pillObject.getCatring();
        String drName=pillObject.getDrName();
        String unit=pillObject.getUnitUse();
        String[]unitCount = pillObject.getUnitsCount().split(",");
        String unitAmount="";
        if(unitCount.length!=0) {
            int positionOdUnit = j % unitCount.length;
            unitAmount = unitCount[positionOdUnit];
        }

        String countPerDay=getCountPerDay(pillObject.getCountOfUsagePerDay());
        return new PillUsage(id,pillObject.getid(),pillName,time,usageTime,state,useTime,hasDelay,description,
                catNme,catColor,catRingtone,drName,unit,unitAmount,countPerDay,usedTime,usageTime);
    }

    private static String getCountPerDay(int countOfUsagePerDay) {
        String countPerDay;
        switch (countOfUsagePerDay){
            case 1:
                countPerDay="هر ۲۴ ساعت";
                break;
            case 2:
                countPerDay="هر ۱۲ ساعت";
                break;
            case 3:
                countPerDay="هر ۸ ساعت";
                break;
            case 4:
                countPerDay="هر ۶ ساعت";
                break;
            case 5:
                countPerDay="هر ۵ ساعت";
                break;
            case 6:
                countPerDay="هر ۴ ساعت";
                break;
            case 7:
                countPerDay="هر ۳.۵ ساعت";
                break;
            case 8:
                countPerDay="هر ۳ ساعت";
                break;
            case 12:
                countPerDay="هر ۲ ساعت";
                break;
            default:
                countPerDay="هر ۱ ساعت";
                break;
        }
        return countPerDay;
    }



    public static PillObject makePillObject(GeneralFields generalFields, UsageFields usageFields, EndUseFields endUseFields,int id) {
        String b64 = generalFields.getB64();// image of pill
        String midname=generalFields.getMidName(); // name of pill
        String couseOfUse=generalFields.getCouse();// reason of use;
        String drName=generalFields.getDrName();// dr name that write this pill

        String catName=(generalFields.getCatName().length()==0) ?"":generalFields.getCatName(); // category name : default value  is "public"
        int catColor=generalFields.getCatColour(); // it has default too
        String catring=generalFields.getAlarmUrl();// it has default too

        int typeOfUsage = usageFields.getTypeOfdayUsage(); // 1 = every day ## 2= some days with regular repeat ## 3= difrent days inweek;
        int isRegular=usageFields.isRegular();// if type 1 or 2 this is true and for 3 this is false

        String days=""; // list of days that we should use pill , it seprate with ,
        for(String day:usageFields.getDays()){
            days+=day;
            days+=",";
        }
        if(days.length()>0){
            days = days.substring(0,days.length()-1);
        }
        double diffrenceOfUsage=usageFields.getDiffrenceOfUsage();// diffrence between two usage per hours for example 3 means between this time and next time we has 3 hours
        int countOfUsagePerDay=usageFields.getCountOfUsagePerDay();//count of usage per day
        int stardHour=usageFields.getStartHour();// initial and first start hours
        int startMin=usageFields.getStartMin();// initial and first start min
        String unitUse=usageFields.getUnitUsage(); // unit of usage
        String unitsCount=""; // its amount of usage for each usage that seprate with ,
        for(String unitcount:usageFields.getUnitsCount()){
            unitsCount+=unitcount;
            unitsCount+=",";
        }
        if(unitsCount.length()>0){
            unitsCount = unitsCount.substring(0,unitsCount.length()-1);
        }

        String unitTimes="";//its time of usage time for each usage in day that seprate with ,
        for(long timeStr:usageFields.getUnitTimes()){
            unitTimes+=timeStr;
            unitTimes+=",";
        }
        if(unitTimes.length()>0){
            unitTimes = unitTimes.substring(0,unitTimes.length()-1);
        }
        int useType=endUseFields.getUseType(); //1= continuesly with unknown dead line ## 2=  finished by finish days ## 3 == finished by finish allusecount;
        int allUseDays=endUseFields.getAlluseDay();// days that we used this pill
        double totalAmounts = endUseFields.getAlluseCount();// amount that we finished usage of this pill

        String[]dates = usageFields.getUsageStartDate().split("/");
        PersianDate calendar = new PersianDate(usageFields.getStartTimeStamp());
        int year = Integer.parseInt(dates[0]);
        int month=  Integer.parseInt(dates[1])-1;
        int day= Integer.parseInt(dates[2]);
        calendar.setHour(usageFields.getStartHour());
        calendar.setMinute(usageFields.getStartMin());
        PersianDate date = new PersianDate(calendar.getTime());
        date.setSecond(0);
        int reminderDay = endUseFields.getDayReminder();
        double reminderCountPill = endUseFields.getRemindAllPill();





        PillObject pillObject=  new PillObject(id,b64,midname,couseOfUse,drName,catName,catColor,catring,
                typeOfUsage,(isRegular),usageFields.getRepeatDays(),days,diffrenceOfUsage
                ,countOfUsagePerDay,stardHour,startMin,unitUse,unitsCount,unitTimes,
                useType,allUseDays,
                totalAmounts,date.getTime(),0,0,usageFields.getDescription(),generalFields.getIsLight(), generalFields.getIsVibrate(),reminderDay,reminderCountPill,
               1,0);
        return pillObject;

    }
    public static PillObject editPillObject(Context context,PillObject lastPill,GeneralFields generalFields, UsageFields usageFields, EndUseFields endUseFields) {
        String b64 = generalFields.getB64();// image of pill
        String midname=generalFields.getMidName(); // name of pill
        String couseOfUse=generalFields.getCouse();// reason of use;
        String drName=generalFields.getDrName();// dr name that write this pill

        String catName=(generalFields.getCatName().length()==0) ?"":generalFields.getCatName(); // category name : default value  is "public"
        int catColor=generalFields.getCatColour(); // it has default too
        String catring=generalFields.getAlarmUrl();// it has default too

        int typeOfUsage = usageFields.getTypeOfdayUsage(); // 1 = every day ## 2= some days with regular repeat ## 3= difrent days inweek;
        int isRegular=usageFields.isRegular();// if type 1 or 2 this is true and for 3 this is false

        String days=""; // list of days that we should use pill , it seprate with ,
        for(String day:usageFields.getDays()){
            days+=day;
            days+=",";
        }
        if(days.length()>0){
            days = days.substring(0,days.length()-1);
        }
        double diffrenceOfUsage=usageFields.getDiffrenceOfUsage();// diffrence between two usage per hours for example 3 means between this time and next time we has 3 hours
        int countOfUsagePerDay=usageFields.getCountOfUsagePerDay();//count of usage per day
        int stardHour=usageFields.getStartHour();// initial and first start hours
        int startMin=usageFields.getStartMin();// initial and first start min
        String unitUse=usageFields.getUnitUsage(); // unit of usage
        String unitsCount=""; // its amount of usage for each usage that seprate with ,
        for(String unitcount:usageFields.getUnitsCount()){
            unitsCount+=unitcount;
            unitsCount+=",";
        }
        if(unitsCount.length()>0){
            unitsCount = unitsCount.substring(0,unitsCount.length()-1);
        }

        String unitTimes="";//its time of usage time for each usage in day that seprate with ,
        for(long timeStr:usageFields.getUnitTimes()){
            unitTimes+=timeStr;
            unitTimes+=",";
        }
        if(unitTimes.length()>0){
            unitTimes = unitTimes.substring(0,unitTimes.length()-1);
        }
        int useType=endUseFields.getUseType(); //1= continuesly with unknown dead line ## 2=  finished by finish days ## 3 == finished by finish allusecount;
        int allUseDays=endUseFields.getAlluseDay();// days that we used this pill
        double totalAmounts = endUseFields.getAlluseCount();// amount that we finished usage of this pill

        String[]dates = usageFields.getUsageStartDate().split("/");
        PersianDate calendar = new PersianDate(usageFields.getStartTimeStamp());
        int year = Integer.parseInt(dates[0]);
        int month=  Integer.parseInt(dates[1])-1;
        int day= Integer.parseInt(dates[2]);
        calendar.setHour(usageFields.getStartHour());
        calendar.setMinute(usageFields.getStartMin());
        PersianDate date = new PersianDate(calendar.getTime());
        date.setSecond(0);
        int reminderDay = endUseFields.getDayReminder();
        double reminderCountPill = endUseFields.getRemindAllPill();


        lastPill.setB64(b64);
        lastPill.setMidname(midname);
        lastPill.setCouseOfUse(couseOfUse);
        lastPill.setDrName(drName);
        lastPill.setCatName(catName);
        lastPill.setCatColor(catColor);
        lastPill.setCatring(catring);
        lastPill.setTypeOfUsage(typeOfUsage);
        lastPill.setRegular(isRegular);
        lastPill.setRepeatUsageDay(usageFields.getRepeatDays());
        lastPill.setDays(days);
        lastPill.setDiffrenceOfUsage(diffrenceOfUsage);
        lastPill.setCountOfUsagePerDay(countOfUsagePerDay);
        lastPill.setStardHour(stardHour);
        lastPill.setStartMin(startMin);
        lastPill.setUnitUse(unitUse);
        lastPill.setUnitsCount(unitsCount);
        lastPill.setUnitTimes(unitTimes);
        lastPill.setUseType(useType);
        lastPill.setAllUseDays(allUseDays);
        lastPill.setTotalAmounts(totalAmounts);
        lastPill.setFirstAlarmTime(date.getTime());
        lastPill.setDescription(usageFields.getDescription());
        lastPill.setReminderDays(reminderDay);
        lastPill.setAllPillCount(reminderCountPill);
        lastPill.setHasLight(generalFields.getIsLight());
        lastPill.setHasVibrate(generalFields.getIsVibrate());
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        lastPill.setSync(0);
        database.pillObjectDao().update(lastPill);

        SyncController sync = new SyncController();
        sync.checkDataBaseForUpdate();
        return lastPill;

    }

    public static ArrayList<PillUsage> makePillUsageInPerFreeMood(PillObject pillObject, Context context) {
        ArrayList<PillUsage>usageList = new ArrayList<>();
        long startTimeStamp=pillObject.getFirstAlarmTime();
        String[] usageTime=pillObject.getUnitTimes().split(",");
        ArrayList<Long>usageTimeLong = new ArrayList<>();
        for(String useTime : usageTime){
            usageTimeLong.add(Long.parseLong(useTime));
        }

        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        long lastLocal = database.pillUsageDao().getLastId();
        if(pillObject.isRegular()==1){
            // monazame yani rooze shoro dare ya har rooz ya har chand rooz
            if(pillObject.getTypeOfUsage()==1){
                // har rooz


                for(int i = 0 ; i <30;/* mikhaym 30 rooz add konim*/ i++){
                    for( int j=0;j<usageTimeLong.size();j++){
                        PersianDate date = new PersianDate(usageTimeLong.get(j));
                        date = addDays(i,date);

                        PillUsage pillUsage = makepillUsage(pillObject,date.getTime(),j, ++lastLocal);
                        usageList.add(pillUsage);

                    }
                }
            }else if(pillObject.getTypeOfUsage()==2) {
                //har n rooz
                for(int i = 0 ; i <30*pillObject.getRepeatUsageDay();/* mikhaym 30 rooz add konim*/ i+=pillObject.getRepeatUsageDay()){
                    for( int j=0;j<usageTimeLong.size();j++){
                        PersianDate date = new PersianDate(usageTimeLong.get(j));
                        date = addDays(i,date);
                        PillUsage pillUsage = makepillUsage(pillObject,date.getTime(),j, ++lastLocal);
                        usageList.add(pillUsage);
                    }
                }
            }
            else {
                // charkhe zede bardari
                String[] useAndStop = pillObject.getDays().split(",");
                int useDays = Integer.parseInt(useAndStop[0]);
                int stopDays = Integer.parseInt(useAndStop[1]);
                int pastDays = Integer.parseInt(useAndStop[2]);
                for(int i = 0 ; i <useDays-pastDays;/* mikhaym useday rooz add konim*/ i++){
                    for( int j=0;j<usageTimeLong.size();j++){
                        PersianDate date = new PersianDate(usageTimeLong.get(j));
                        date = addDays(i,date);
                        PillUsage pillUsage = makepillUsage(pillObject,date.getTime(),j, ++lastLocal);
                        usageList.add(pillUsage);

                    }
                }

            }
        }else {
            // monazam nist yani roozasho khodesh tayin karde;

            String[] days = pillObject.getDays().split(",");
            ArrayList<String> dayArr = new ArrayList<>();
            for (String day : days) {
                dayArr.add(day);
            }
            int distanceDay = 0;
            PersianDate startDateEachCount = new PersianDate(System.currentTimeMillis());
            for (int i = 0; i < 30;/* mikhaym 30 rooz add konim*/ i++) {

                Log.d("HiLevel","حلقه بیرونی "+i);
                Log.d("HiLevel","حلقه داخلی زمانی که باید اضافه شود"+distanceDay+"روز");
                for (int j = 0; j < usageTimeLong.size(); j++) {
                    PersianDate date = new PersianDate(usageTimeLong.get(j));
                    date = addDays(distanceDay,date);
                    Log.d("HiLevel","حلقه داخلی پس از اضافه شدن روز"+PersianCalculater.getMonthAndDay(date.getTime())+"---"+PersianCalculater.getHourseAndMin(date.getTime()));
                    PillUsage pillUsage = makepillUsage(pillObject, date.getTime(), j, ++lastLocal);
                    usageList.add(pillUsage);
                    if(j==0){
                        startDateEachCount=date;
                    }
                }



                int indexDay = dayArr.indexOf(startDateEachCount.dayName());
                String next = "";
                if (indexDay != -1) {
                    if (indexDay == dayArr.size() - 1) {
                        next = dayArr.get(0);
                    } else {
                        next = dayArr.get(indexDay + 1);
                    }
                }else {
                    next=dayArr.get(0);
                }
                next = next.substring(0,2);
                Log.d("HiLevel","روز بعدی =  "+next);

                while (!startDateEachCount.dayName().startsWith(next)) {
                    startDateEachCount.addDay(1);
                    distanceDay++;

                }
                if(dayArr.size()==1){
                    distanceDay+=7;
                }
                Log.d("HiLevel","روزهای بعدی "+distanceDay+" "+startDateEachCount.dayName()+"---"+next);

            }
        }



        return usageList;
    }
    public static ArrayList<PillUsage> editPillUsageInPerFreeMood(PillObject pillObject, Context context) {
        ArrayList<PillUsage>usageList = new ArrayList<>();
        long startTimeStamp=pillObject.getFirstAlarmTime();
        String[] usageTime=pillObject.getUnitTimes().split(",");
        ArrayList<Long>usageTimeLong = new ArrayList<>();
        for(String useTime : usageTime){
            usageTimeLong.add(Long.parseLong(useTime));
        }
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        long lastLocal = database.pillUsageDao().getLastId();

        if(pillObject.isRegular()==1){
            // monazame yani rooze shoro dare ya har rooz ya har chand rooz
            if(pillObject.getTypeOfUsage()==1){
                // har rooz
                for(int i = 0 ; i <30;/* mikhaym 30 rooz add konim*/ i++){
                    for( int j=0;j<usageTimeLong.size();j++){
                        PersianDate date = new PersianDate(usageTimeLong.get(j));
                        date = addDays(i,date);
                        PillUsage pillUsage = makepillUsage(pillObject,date.getTime(),j,++lastLocal);
                        usageList.add(pillUsage);

                    }
                }
            }else if(pillObject.getTypeOfUsage()==2) {
                //har n rooz
                for(int i = 0 ; i <30*pillObject.getRepeatUsageDay();/* mikhaym 30 rooz add konim*/ i+=pillObject.getRepeatUsageDay()){
                    for( int j=0;j<usageTimeLong.size();j++){
                        PersianDate date = new PersianDate(usageTimeLong.get(j));
                        date = addDays(i,date);
                        PillUsage pillUsage = makepillUsage(pillObject,date.getTime(),j,++lastLocal);
                        usageList.add(pillUsage);
                    }
                }
            }
            else {
                // charkhe zede bardari
                String[] useAndStop = pillObject.getDays().split(",");
                int useDays = Integer.parseInt(useAndStop[0]);
                int stopDays = Integer.parseInt(useAndStop[1]);
                int pastDays = Integer.parseInt(useAndStop[2]);
                for(int i = 0 ; i <useDays-pastDays;/* mikhaym useday rooz add konim*/ i++){
                    for( int j=0;j<usageTimeLong.size();j++){
                        PersianDate date = new PersianDate(usageTimeLong.get(j));
                        date = addDays(i,date);
                        PillUsage pillUsage = makepillUsage(pillObject,date.getTime(),j,++lastLocal);
                        usageList.add(pillUsage);

                    }
                }

            }
        }else {
            // monazam nist yani roozasho khodesh tayin karde;

            String[] days = pillObject.getDays().split(",");
            ArrayList<String> dayArr = new ArrayList<>();
            for (String day : days) {
                dayArr.add(day);
            }
            int distanceDay = 0;
            PersianDate startDateEachCount = new PersianDate(System.currentTimeMillis());
            for (int i = 0; i < 30;/* mikhaym 30 rooz add konim*/ i++) {

                Log.d("HiLevel","حلقه بیرونی "+i);
                Log.d("HiLevel","حلقه داخلی زمانی که باید اضافه شود"+distanceDay+"روز");
                for (int j = 0; j < usageTimeLong.size(); j++) {
                    PersianDate date = new PersianDate(usageTimeLong.get(j));
                    date = addDays(distanceDay,date);
                    Log.d("HiLevel","حلقه داخلی پس از اضافه شدن روز"+PersianCalculater.getMonthAndDay(date.getTime())+"---"+PersianCalculater.getHourseAndMin(date.getTime()));
                    PillUsage pillUsage = makepillUsage(pillObject, date.getTime(), j,++lastLocal);
                    usageList.add(pillUsage);
                    if(j==0){
                        startDateEachCount=date;
                    }
                }



                int indexDay = dayArr.indexOf(startDateEachCount.dayName());
                String next = "";
                if (indexDay != -1) {
                    if (indexDay == dayArr.size() - 1) {
                        next = dayArr.get(0);
                    } else {
                        next = dayArr.get(indexDay + 1);
                    }
                }else {
                    next=dayArr.get(0);
                }
                next = next.substring(0,2);
                Log.d("HiLevel","روز بعدی =  "+next);

                while (!startDateEachCount.dayName().startsWith(next)) {
                    startDateEachCount.addDay(1);
                    distanceDay++;

                }
                Log.d("HiLevel","روزهای بعدی "+distanceDay+" "+startDateEachCount.dayName()+"---"+next);

            }
        }



        return usageList;
    }
    public static ArrayList<PillUsage> makePillUsageInPerFreeMoodReStart(PillObject pillObject, Context context) {
        ArrayList<PillUsage>usageList = new ArrayList<>();
        String[] usageTime=pillObject.getUnitTimes().split(",");
        ArrayList<Long>usageTimeLong = new ArrayList<>();
        for(String useTime : usageTime){
            usageTimeLong.add(Long.parseLong(useTime));
        }
        usageTimeLong = calcNewUsageTime(usageTimeLong);
        long currentTimeStamp = System.currentTimeMillis();

        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        long lastLocal = database.pillUsageDao().getLastId();
        if(pillObject.isRegular()==1){
            // monazame yani rooze shoro dare ya har rooz ya har chand rooz
            if(pillObject.getTypeOfUsage()==1){
                // har rooz
                for(int i = 0 ; i <30;/* mikhaym 30 rooz add konim*/ i++){
                    for( int j=0;j<usageTimeLong.size();j++){
                        PersianDate date = new PersianDate(usageTimeLong.get(j));
                        if(i!=0||currentTimeStamp<=date.getTime()) {

                            date = addDays(i, date);
                            PillUsage pillUsage = makepillUsage(pillObject, date.getTime(), j,++lastLocal);
                            usageList.add(pillUsage);
                        }
                    }
                }
            }else {
                //har n rooz
                for (int i = 0; i < 30 * pillObject.getRepeatUsageDay();/* mikhaym 30 rooz add konim*/ i += pillObject.getRepeatUsageDay()) {
                    for (int j = 0; j < usageTimeLong.size(); j++) {
                        PersianDate date = new PersianDate(usageTimeLong.get(j));
                        if (i != 0 || currentTimeStamp <= date.getTime()) {
                            date = addDays(i, date);
                            PillUsage pillUsage = makepillUsage(pillObject, date.getTime(), j,++lastLocal);
                            usageList.add(pillUsage);
                        }
                    }
                }
            }

        }else {
            // monazam nist yani roozasho khodesh tayin karde;

            String[] days = pillObject.getDays().split(",");
            ArrayList<String> dayArr = new ArrayList<>();
            for (String day : days) {
                dayArr.add(day);
            }
            int distanceDay = 0;
            PersianDate startDateEachCount = new PersianDate(System.currentTimeMillis());
            for (int i = 0; i < 30;/* mikhaym 30 rooz add konim*/ i++) {

                Log.d("HiLevel","حلقه بیرونی "+i);
                Log.d("HiLevel","حلقه داخلی زمانی که باید اضافه شود"+distanceDay+"روز");
                for (int j = 0; j < usageTimeLong.size(); j++) {
                    PersianDate date = new PersianDate(usageTimeLong.get(j));
                    if(i!=0||currentTimeStamp<=date.getTime()) {
                        date = addDays(distanceDay, date);
                        Log.d("HiLevel", "حلقه داخلی پس از اضافه شدن روز" + PersianCalculater.getMonthAndDay(date.getTime()) + "---" + PersianCalculater.getHourseAndMin(date.getTime()));
                        PillUsage pillUsage = makepillUsage(pillObject, date.getTime(), j,++lastLocal);
                        usageList.add(pillUsage);
                        if (j == 0) {
                            startDateEachCount = date;
                        }
                    }
                }



                int indexDay = dayArr.indexOf(startDateEachCount.dayName());
                String next = "";
                if (indexDay != -1) {
                    if (indexDay == dayArr.size() - 1) {
                        next = dayArr.get(0);
                    } else {
                        next = dayArr.get(indexDay + 1);
                    }
                }else {
                    next=dayArr.get(0);
                }
                next = next.substring(0,2);
                Log.d("HiLevel","روز بعدی =  "+next);

                while (!startDateEachCount.dayName().startsWith(next)) {
                    startDateEachCount.addDay(1);
                    distanceDay++;

                }
                Log.d("HiLevel","روزهای بعدی "+distanceDay+" "+startDateEachCount.dayName()+"---"+next);

            }
        }



        return usageList;
    }

    private static ArrayList<Long> calcNewUsageTime(ArrayList<Long>usageTimeLong) {
        PersianDate today = new PersianDate(System.currentTimeMillis());
        int defrenceDay = diffrenceDay(today,usageTimeLong.get(0));
        ArrayList<Long>tempUsage = new ArrayList<>();
        for(Long lon :usageTimeLong){
            tempUsage.add(lon);
        }
        usageTimeLong.clear();
        for(Long useTime:tempUsage){
            PersianDate date = new PersianDate(useTime);
            date = addDays(defrenceDay,date);
            usageTimeLong.add(date.getTime());
        }
        return usageTimeLong;
    }

    private static int diffrenceDay(PersianDate today, Long aLong) {
       long diffrence= today.getTime()-aLong;
       return (int) (diffrence/(1000  * 60 * 60 * 24));
    }

    public static PersianDate addDays(int distanceDay, PersianDate date) {
        for(int i = 0 ; i<distanceDay;i++){
            date.addDay(1);
        }
        return date;
    }



    public static ArrayList<PillUsage> makePillUsageInPerAmountMood(PillObject pillObject,Context context) {
        ArrayList<PillUsage>usageList = new ArrayList<>();
        long startTimeStamp=pillObject.getFirstAlarmTime();
        String[] usageTime=pillObject.getUnitTimes().split(",");
        ArrayList<Long>usageTimeLong = new ArrayList<>();
        for(String useTime : usageTime){
            usageTimeLong.add(Long.parseLong(useTime));
        }

        double amountOfUse = pillObject.getTotalAmounts();
        double countingAmount = 0;
        int usageDay = 0;

        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        long lastLocal = database.pillUsageDao().getLastId();
        if(pillObject.isRegular()==1){
            // monazame yani rooze shoro dare ya har rooz ya har chand rooz
            if(pillObject.getTypeOfUsage()==1){
                // har rooz
               while (amountOfUse>countingAmount && usageDay <30) {
                   for (int j = 0; j < usageTimeLong.size(); j++) {
                       PersianDate date = new PersianDate(usageTimeLong.get(j));
                       date = addDays(usageDay, date);
                       PillUsage pillUsage = makepillUsage(pillObject, date.getTime(), j,++lastLocal);
                       usageList.add(pillUsage);
                       countingAmount += Double.parseDouble(pillUsage.getUnitAmount());
                   }
                   usageDay++;
               }

            }else if(pillObject.getTypeOfUsage()==2){
                //har n rooz
                while (amountOfUse>countingAmount && usageDay <30){
                    for( int j=0;j<usageTimeLong.size();j++){
                        PersianDate date = new PersianDate(usageTimeLong.get(j));
                        date = addDays(usageDay*pillObject.getRepeatUsageDay(),date);
                        PillUsage pillUsage = makepillUsage(pillObject,date.getTime(),j,++lastLocal);
                        usageList.add(pillUsage);
                        countingAmount+=Double.parseDouble(pillUsage.getUnitAmount());
                    }
                    usageDay++;
                }


            }else {

                //charkhe zede bardari
                String[] useAndStop = pillObject.getDays().split(",");
                int useDays = Integer.parseInt(useAndStop[0]);
                int stopDays = Integer.parseInt(useAndStop[1]);
                int pastDays = Integer.parseInt(useAndStop[2]);
                while (amountOfUse>countingAmount && usageDay <useDays-pastDays) {
                    for (int j = 0; j < usageTimeLong.size(); j++) {
                        PersianDate date = new PersianDate(usageTimeLong.get(j));
                        date = addDays(usageDay, date);
                        PillUsage pillUsage = makepillUsage(pillObject, date.getTime(), j,++lastLocal);
                        usageList.add(pillUsage);
                        countingAmount += Double.parseDouble(pillUsage.getUnitAmount());
                    }
                    usageDay++;
                }
            }
        }else {
            // monazam nist yani roozasho khodesh tayin karde;

            String[] days = pillObject.getDays().split(",");
            ArrayList<String> dayArr = new ArrayList<>();
            for (String day : days) {
                dayArr.add(day);
            }
            int distanceDay = 0;
            PersianDate startDateEachCount = new PersianDate(System.currentTimeMillis());
            while (amountOfUse>countingAmount && usageDay <30){
                for (int j = 0; j < usageTimeLong.size(); j++) {
                    PersianDate date = new PersianDate(usageTimeLong.get(j));
                    date = addDays(distanceDay,date);
                    Log.d("HiLevel","حلقه داخلی پس از اضافه شدن روز"+PersianCalculater.getMonthAndDay(date.getTime())+"---"+PersianCalculater.getHourseAndMin(date.getTime()));
                    PillUsage pillUsage = makepillUsage(pillObject, date.getTime(), j,++lastLocal);
                    usageList.add(pillUsage);
                    countingAmount+=Double.parseDouble(pillUsage.getUnitAmount());
                    if(j==0){
                        startDateEachCount=date;
                    }

                }



                int indexDay = dayArr.indexOf(startDateEachCount.dayName());
                String next = "";
                if (indexDay != -1) {
                    if (indexDay == dayArr.size() - 1) {
                        next = dayArr.get(0);
                    } else {
                        next = dayArr.get(indexDay + 1);
                    }
                }else {
                    next=dayArr.get(0);
                }
                next = next.substring(0,2);
                Log.d("HiLevel","روز بعدی =  "+next);

                while (!startDateEachCount.dayName().startsWith(next)) {
                    startDateEachCount.addDay(1);
                    distanceDay++;

                }

                usageDay++;
                Log.d("HiLevel","روزهای بعدی "+distanceDay+" "+startDateEachCount.dayName()+"---"+next);

            }
        }

        return usageList;
    }
    public static ArrayList<PillUsage> makePillUsageInPerAmountMoodReStart(PillObject pillObject,Context context) {
        ArrayList<PillUsage>usageList = new ArrayList<>();
        long startTimeStamp=pillObject.getFirstAlarmTime();
        String[] usageTime=pillObject.getUnitTimes().split(",");
        ArrayList<Long>usageTimeLong = new ArrayList<>();
        for(String useTime : usageTime){
            usageTimeLong.add(Long.parseLong(useTime));
        }
        usageTimeLong = calcNewUsageTime(usageTimeLong);
        double amountOfUse = pillObject.getTotalAmounts();
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        ArrayList<PillUsage>usedPill = new ArrayList<>(database.pillUsageDao().listSpecialUsedPillUsage(pillObject.getMidname(),pillObject.getCatName()));
        long lastLocal = database.pillUsageDao().getLastId();
        double allAmount = 0;
        for(PillUsage usage :usedPill){
            allAmount += Double.parseDouble(usage.getUnitAmount());
        }
        double countingAmount =allAmount;
        int usageDay=0;
        long currentTimeStamp = System.currentTimeMillis();
        if(pillObject.isRegular()==1){
            // monazame yani rooze shoro dare ya har rooz ya har chand rooz

            if(pillObject.getTypeOfUsage()==1){
                // har rooz
                while (amountOfUse>countingAmount && usageDay <30) {
                   for (int j = 0; j < usageTimeLong.size(); j++) {
                       PersianDate date = new PersianDate(usageTimeLong.get(j));
                       if(usageDay!=0||currentTimeStamp<=date.getTime()) {
                           date = addDays(usageDay, date);
                           PillUsage pillUsage = makepillUsage(pillObject, date.getTime(), j,++lastLocal);
                           usageList.add(pillUsage);
                           countingAmount += Double.parseDouble(pillUsage.getUnitAmount());
                       }
                   }

                   usageDay++;
               }


            }else {
                //har n rooz
                while (amountOfUse>countingAmount && usageDay <30){
                    for( int j=0;j<usageTimeLong.size();j++){
                        PersianDate date = new PersianDate(usageTimeLong.get(j));
                        if(usageDay!=0||currentTimeStamp<=date.getTime()) {

                            date = addDays(usageDay * pillObject.getRepeatUsageDay(), date);
                            PillUsage pillUsage = makepillUsage(pillObject, date.getTime(), j,++lastLocal);
                            usageList.add(pillUsage);
                            countingAmount += Double.parseDouble(pillUsage.getUnitAmount());
                        }
                    }
                    usageDay++;
                }


            }
        }else {
            // monazam nist yani roozasho khodesh tayin karde;

            String[] days = pillObject.getDays().split(",");
            ArrayList<String> dayArr = new ArrayList<>();
            for (String day : days) {
                dayArr.add(day);
            }
            int distanceDay = 0;
            PersianDate startDateEachCount = new PersianDate(System.currentTimeMillis());
            while (amountOfUse>countingAmount && usageDay <30){
                for (int j = 0; j < usageTimeLong.size(); j++) {
                    PersianDate date = new PersianDate(usageTimeLong.get(j));
                    if(usageDay!=0||currentTimeStamp<=date.getTime()) {

                        date = addDays(distanceDay, date);
                        Log.d("HiLevel", "حلقه داخلی پس از اضافه شدن روز" + PersianCalculater.getMonthAndDay(date.getTime()) + "---" + PersianCalculater.getHourseAndMin(date.getTime()));
                        PillUsage pillUsage = makepillUsage(pillObject, date.getTime(), j,++lastLocal);
                        usageList.add(pillUsage);
                        countingAmount += Double.parseDouble(pillUsage.getUnitAmount());
                    }
                        if (j == 0) {
                            startDateEachCount = date;
                        }


                }



                int indexDay = dayArr.indexOf(startDateEachCount.dayName());
                String next = "";
                if (indexDay != -1) {
                    if (indexDay == dayArr.size() - 1) {
                        next = dayArr.get(0);
                    } else {
                        next = dayArr.get(indexDay + 1);
                    }
                }else {
                    next=dayArr.get(0);
                }
                next = next.substring(0,2);
                Log.d("HiLevel","روز بعدی =  "+next);

                while (!startDateEachCount.dayName().startsWith(next)) {
                    startDateEachCount.addDay(1);
                    distanceDay++;

                }

                usageDay++;
                Log.d("HiLevel","روزهای بعدی "+distanceDay+" "+startDateEachCount.dayName()+"---"+next);

            }
        }

        return usageList;
    }

    public static ArrayList<PillUsage> makePillUsageInPerCountMood(PillObject pillObject,Context context) {
        ArrayList<PillUsage>usageList = new ArrayList<>();
        int usageCount = 0;
        if(pillObject.getAllUseDays()<=30){
            usageCount = pillObject.getAllUseDays();
        }else {
            usageCount =30;
        }
        long startTimeStamp=pillObject.getFirstAlarmTime();
        String[] usageTime=pillObject.getUnitTimes().split(",");
        ArrayList<Long>usageTimeLong = new ArrayList<>();
        for(String useTime : usageTime){
            usageTimeLong.add(Long.parseLong(useTime));
        }
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        long lastLocal = database.pillUsageDao().getLastId();
        if(pillObject.isRegular()==1){
            // monazame yani rooze shoro dare ya har rooz ya har chand rooz
            if(pillObject.getTypeOfUsage()==1){
                // har rooz
                for(int i = 0 ; i <usageCount;/* mikhaym 30 rooz add konim*/ i++){
                    for( int j=0;j<usageTimeLong.size();j++){
                        PersianDate date = new PersianDate(usageTimeLong.get(j));
                        date = addDays(i,date);
                        PillUsage pillUsage = makepillUsage(pillObject,date.getTime(),j,++lastLocal);
                        usageList.add(pillUsage);
                    }
                }
            }else if(pillObject.getTypeOfUsage()==2) {
                //har n rooz
                for(int i = 0 ; i <usageCount*pillObject.getRepeatUsageDay();/* mikhaym 30 rooz add konim*/ i+=pillObject.getRepeatUsageDay()){
                    for( int j=0;j<usageTimeLong.size();j++){
                        PersianDate date = new PersianDate(usageTimeLong.get(j));
                        date = addDays(i,date);
                        PillUsage pillUsage = makepillUsage(pillObject,date.getTime(),j,++lastLocal);
                        usageList.add(pillUsage);
                    }
                }
            }else {
                //charkhe zede bar dari
                String[] useAndStop = pillObject.getDays().split(",");
                int useDays = Integer.parseInt(useAndStop[0]);
                int stopDays = Integer.parseInt(useAndStop[1]);
                int pastDays = Integer.parseInt(useAndStop[2]);
                for(int i = 0 ; i <useDays-pastDays;/* mikhaym 30 rooz add konim*/ i++){
                    for( int j=0;j<usageTimeLong.size();j++){
                        PersianDate date = new PersianDate(usageTimeLong.get(j));
                        date = addDays(i,date);
                        PillUsage pillUsage = makepillUsage(pillObject,date.getTime(),j,++lastLocal);
                        usageList.add(pillUsage);
                    }
                }

            }
        }else {
            // monazam nist yani roozasho khodesh tayin karde;
            String[] days = pillObject.getDays().split(",");
            ArrayList<String> dayArr = new ArrayList<>();
            for (String day : days) {
                dayArr.add(day);
            }
            int distanceDay = 0;
            PersianDate startDateEachCount = new PersianDate(System.currentTimeMillis());
            for (int i = 0; i < usageCount;/* mikhaym 30 rooz add konim*/ i++) {
                Log.d("HiLevel","حلقه بیرونی "+i);
                Log.d("HiLevel","حلقه داخلی زمانی که باید اضافه شود"+distanceDay+"روز");
                for (int j = 0; j < usageTimeLong.size(); j++) {
                    PersianDate date = new PersianDate(usageTimeLong.get(j));
                    date = addDays(distanceDay,date);
                    Log.d("HiLevel","حلقه داخلی پس از اضافه شدن روز"+PersianCalculater.getMonthAndDay(date.getTime())+"---"+PersianCalculater.getHourseAndMin(date.getTime()));
                    PillUsage pillUsage = makepillUsage(pillObject, date.getTime(), j,++lastLocal);
                    usageList.add(pillUsage);
                    if(j==0){
                        startDateEachCount=date;
                    }
                }
                int indexDay = dayArr.indexOf(startDateEachCount.dayName());
                String next = "";
                if (indexDay != -1) {
                    if (indexDay == dayArr.size() - 1) {
                        next = dayArr.get(0);
                    } else {
                        next = dayArr.get(indexDay + 1);
                    }
                }else {
                    next=dayArr.get(0);
                }
                next = next.substring(0,2);
                Log.d("HiLevel","روز بعدی =  "+next);
                while (!startDateEachCount.dayName().startsWith(next)) {
                    startDateEachCount.addDay(1);
                    distanceDay++;
                }
                Log.d("HiLevel","روزهای بعدی "+distanceDay+" "+startDateEachCount.dayName()+"---"+next);
            }
        }
        return usageList;

    }
    public static ArrayList<PillUsage> makePillUsageInPerCountMoodReStart(PillObject pillObject,Context context) {
        ArrayList<PillUsage>usageList = new ArrayList<>();
        int usageCount = 0;
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        List<PillUsage> lastUsages = database.pillUsageDao().listSpecialPillUsage(pillObject.getMidname(),pillObject.getCatName());
        long lastLocal = database.pillUsageDao().getLastId();
        long startTimeStamp=pillObject.getFirstAlarmTime();
        String[] usageTime=pillObject.getUnitTimes().split(",");
        ArrayList<Long>usageTimeLong = new ArrayList<>();
        for(String useTime : usageTime){
            usageTimeLong.add(Long.parseLong(useTime));
        }
        int countOfUsedDay = lastUsages.size()/usageTimeLong.size();
        if(countOfUsedDay==pillObject.getAllUseDays()){
            usageCount=0;
        }
        else if(pillObject.getAllUseDays()<=30){

            usageCount = pillObject.getAllUseDays()-countOfUsedDay;
        }else {
            if(pillObject.getAllUseDays()-countOfUsedDay>=30) {
                usageCount = 30;
            }else {
                usageCount = pillObject.getAllUseDays()-countOfUsedDay;
            }
        }
        usageTimeLong = calcNewUsageTime(usageTimeLong);
        long currentTime = System.currentTimeMillis();
        if(pillObject.isRegular()==1){
            // monazame yani rooze shoro dare ya har rooz ya har chand rooz
            if(pillObject.getTypeOfUsage()==1){
                // har rooz
                for(int i = 0 ; i <usageCount;/* mikhaym 30 rooz add konim*/ i++){
                    for( int j=0;j<usageTimeLong.size();j++){
                        if(i!=0 || currentTime<usageTimeLong.get(j)) {
                            PersianDate date = new PersianDate(usageTimeLong.get(j));
                            date = addDays(i, date);
                            PillUsage pillUsage = makepillUsage(pillObject, date.getTime(), j,++lastLocal);
                            usageList.add(pillUsage);
                        }
                    }
                }
            }else {
                //har n rooz
                for(int i = 0 ; i <usageCount*pillObject.getRepeatUsageDay();/* mikhaym 30 rooz add konim*/ i+=pillObject.getRepeatUsageDay()){
                    for( int j=0;j<usageTimeLong.size();j++){
                        if(i!=0 || currentTime<usageTimeLong.get(j)) {
                            PersianDate date = new PersianDate(usageTimeLong.get(j));
                            date = addDays(i, date);
                            PillUsage pillUsage = makepillUsage(pillObject, date.getTime(), j,++lastLocal);
                            usageList.add(pillUsage);
                        }
                    }
                }
            }
        }else {
            // monazam nist yani roozasho khodesh tayin karde;
            String[] days = pillObject.getDays().split(",");
            ArrayList<String> dayArr = new ArrayList<>();
            for (String day : days) {
                dayArr.add(day);
            }
            int distanceDay = 0;
            PersianDate startDateEachCount = new PersianDate(System.currentTimeMillis());
            for (int i = 0; i < usageCount;/* mikhaym 30 rooz add konim*/ i++) {
                Log.d("HiLevel","حلقه بیرونی "+i);
                Log.d("HiLevel","حلقه داخلی زمانی که باید اضافه شود"+distanceDay+"روز");
                for (int j = 0; j < usageTimeLong.size(); j++) {
                    if(i!=0 || currentTime<usageTimeLong.get(j)) {
                        PersianDate date = new PersianDate(usageTimeLong.get(j));
                        date = addDays(distanceDay, date);
                        Log.d("HiLevel", "حلقه داخلی پس از اضافه شدن روز" + PersianCalculater.getMonthAndDay(date.getTime()) + "---" + PersianCalculater.getHourseAndMin(date.getTime()));
                        PillUsage pillUsage = makepillUsage(pillObject, date.getTime(), j,++lastLocal);
                        usageList.add(pillUsage);
                        if (j == 0) {
                            startDateEachCount = date;
                        }
                    }
                }
                int indexDay = dayArr.indexOf(startDateEachCount.dayName());
                String next = "";
                if (indexDay != -1) {
                    if (indexDay == dayArr.size() - 1) {
                        next = dayArr.get(0);
                    } else {
                        next = dayArr.get(indexDay + 1);
                    }
                }else {
                    next=dayArr.get(0);
                }
                next = next.substring(0,2);
                Log.d("HiLevel","روز بعدی =  "+next);
                while (!startDateEachCount.dayName().startsWith(next)) {
                    startDateEachCount.addDay(1);
                    distanceDay++;
                }
                Log.d("HiLevel","روزهای بعدی "+distanceDay+" "+startDateEachCount.dayName()+"---"+next);
            }
        }
        return usageList;

    }

}
