package greencode.ir.pillcci.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.objects.PillShelf;
import greencode.ir.pillcci.retrofit.SyncController;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 6/9/18.
 */

public class DatabaseManager {
    public static List<PillUsage> getAllExpendedPillUsage(Context context){
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        List<PillUsage>pillUsages = database.pillUsageDao().getAllExpendedPillUsage(System.currentTimeMillis());
        return pillUsages;
    }
    public static void updateToExpendedMode(Context context,List<PillUsage>pillUsages){
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        for(PillUsage pillUsage:pillUsages){
            pillUsage.setState(3);
            pillUsage.setIsSync(0);
            database.pillUsageDao().update(pillUsage);
        }

    }

    public static void cancelUsage(Context context, PillUsage item) {
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        item.setState(2);
        item.setIsSync(0);
        database.pillUsageDao().update(item);
    }

    public static void addToEnd(Context context, PillUsage item) {
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        PillUsage lastPill = database.pillUsageDao().getPillLastUsage(item.getPillName(),item.getCatNme());
        PillObject object = database.pillObjectDao().specialPil(lastPill.getPillName(),lastPill.getCatNme());
        addToEndInFreeMode(0,lastPill, object, database);
    }

    public static void makeNextUsage(Context context, PillUsage pillUsage) {
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        PillUsage lastPill = database.pillUsageDao().getPillLastUsage(pillUsage.getPillName(),pillUsage.getCatNme());
        PillObject object = database.pillObjectDao().specialPil(lastPill.getPillName(),lastPill.getCatNme());
        addToEndInFreeMode(0,lastPill,object,database);

    }
    public static void makeNextBirthControl(Context context, PillUsage pillUsage){
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        PillUsage lastPill = database.pillUsageDao().getPillLastUsage(pillUsage.getPillName(),pillUsage.getCatNme());
        PillObject object = database.pillObjectDao().specialPil(lastPill.getPillName(),lastPill.getCatNme());
        String[] days = object.getDays().split(",");
        int use = Integer.parseInt(days[0]);
        int stop = Integer.parseInt(days[1]);
        int past  = Integer.parseInt(days[2]);

        List<PillUsage> allUsage = database.pillUsageDao().listSpecialPillUsage(object.getMidname(),object.getCatName());
        if((allUsage.size()%use)-past==0){
            // yani bayad fasele bedim
            String day = use+","+stop+","+0;
            object.setDays(day);
            object.setSync(0);
            database.pillObjectDao().update(object);

            addToEndInFreeMode(stop,lastPill,object,database);

        }else {
            //yani nabayad fasele bedim;
            addToEndInFreeMode(0,lastPill,object,database);
        }



    }




    private static void addToEndInFreeMode(int daysRest,PillUsage lastPill, PillObject object, AppDatabase database) {
        String[]allTimes = object.getUnitTimes().split(",");
        ArrayList<Long> allTimesLong = new ArrayList<>();
        for(String allT:allTimes){
            allTimesLong.add(Long.parseLong(allT));
        }
        PersianDate lastUsageDate = new PersianDate(lastPill.getSetedTime());
        int lastUsePosition=0;
        // می خوایم پوزیشن آخرین مصرف رو پیدا کنیم. ببینیم ساعت چنده
        for(int i = 0 ; i<allTimesLong.size();i++){
            PersianDate currentTimeDate =  new PersianDate(allTimesLong.get(i));
            if(currentTimeDate.getHour()==lastUsageDate.getHour()
                    && currentTimeDate.getMinute()==lastUsageDate.getMinute()){
                lastUsePosition=i;
                break;
            }
        }
        int nextPosition;
        PersianDate nextDate ;
        String amount = "0";
        String[]amountList = object.getUnitsCount().split(",");

        if(lastUsePosition==allTimesLong.size()-1){
            nextPosition=0;
            // inja yani dooreye 1 roozesh tamam shode bayad ba ezafe kardane date jadid roo hesab konim
            nextDate = new PersianDate(lastPill.getSetedTime());

            // saat ro roye avalin tanzim mikonim
            if(object.isRegular()==1){
                if(object.getTypeOfUsage()==1){
                    // har rooz
                    nextDate = addDays(1,nextDate);
                }else if(object.getTypeOfUsage()==2) {
                    nextDate = addDays(object.getRepeatUsageDay(),nextDate);
                    // har chand rooz
                }
                // ya n rooz yebare ya 1 rooz yebar
                else{
                    // charkhe zede bardari
                    nextDate = addDays(daysRest,nextDate);
                }
            }else {
                String[]days = object.getDays().split(",");
                ArrayList<String>dayArr = new ArrayList<>();
                for(String day : days){
                    dayArr.add(day);
                }
                String todayName = nextDate.dayName();
                todayName = todayName.substring(0,2);
                int todayPosition=0;
                for(int i =0;i<dayArr.size();i++){
                    if(dayArr.get(i).startsWith(todayName)){
                        todayPosition =i;
                        break;
                    }
                }
                int nextDayPosition;
                if(todayPosition==dayArr.size()-1){
                    nextDayPosition = 0;
                }else {
                    nextDayPosition = todayPosition+1;
                }
                String nextDay = dayArr.get(nextDayPosition);
                nextDay = nextDay.substring(0,2);
                while (!nextDate.dayName().startsWith(nextDay)){
                    nextDate = addDays(1,nextDate);
                }


                // rooze baadi bayad hesab beshe
            }

        }else {



            nextPosition=lastUsePosition+1;

           nextDate = new PersianDate(lastPill.getSetedTime());

            if(isNextDay(allTimesLong.get(nextPosition-1),allTimesLong.get(nextPosition))){
                // in neshoon mide to in dore masraf alan in ghorse male rooze starte ya fardashe
                nextDate = addDays(1,nextDate);
            }
        }

        amount = amountList[nextPosition];

        long nextTimeStamp = allTimesLong.get(nextPosition);

        PersianDate nextHourAndMin=new PersianDate(nextTimeStamp);
        nextDate.setHour(nextHourAndMin.getHour());
        nextDate.setMinute(nextHourAndMin.getMinute());
        nextDate.setSecond(0);
        String time=PersianCalculater.getHourseAndMin(nextDate.getTime());
        long lastLocal = database.pillUsageDao().getLastId();

        if(object.getUseType()==1){

            database.pillUsageDao().insertPill(new PillUsage(++lastLocal,lastPill.getPillId(),lastPill.getPillName(),time,nextDate.getTime(),
                    0,"",0,lastPill.getDescription(),lastPill.getCatNme(),lastPill.getCatColor(),
                    lastPill.getCatRingtone(),lastPill.getDrName(),lastPill.getUnit(),amount,lastPill.getCountPerDay(),0,nextDate.getTime()));

            // modam
        }else if(object.getUseType()==2){
            int countUsage = database.pillUsageDao().getCountOfAllUsage(object.getMidname(),object.getCatName());

            int countAllDaysUsage=object.getAllUseDays();

            if(countUsage/object.getCountOfUsagePerDay()<countAllDaysUsage){
                database.pillUsageDao().insertPill(new PillUsage(++lastLocal,lastPill.getPillId(),lastPill.getPillName(),time,nextDate.getTime(),
                        0,"",0,lastPill.getDescription(),lastPill.getCatNme(),lastPill.getCatColor(),
                        lastPill.getCatRingtone(),lastPill.getDrName(),lastPill.getUnit(),amount,lastPill.getCountPerDay(),0,nextDate.getTime()));
            }

            // tedade rooz
        }else {
            // bar asase meghdar

            ArrayList<PillUsage>usedPill = new ArrayList<>(database.pillUsageDao().listSpecialUsedPillUsage(object.getMidname(),object.getCatName()));
            double allAmount = 0;
            for(PillUsage usage :usedPill){
                allAmount += Double.parseDouble(usage.getUnitAmount());
            }

            if(allAmount<object.getTotalAmounts()){

                database.pillUsageDao().insertPill(new PillUsage(++lastLocal,lastPill.getPillId(),lastPill.getPillName(),time,nextDate.getTime(),
                        0,"",0,lastPill.getDescription(),lastPill.getCatNme(),lastPill.getCatColor(),
                        lastPill.getCatRingtone(),lastPill.getDrName(),lastPill.getUnit(),amount,lastPill.getCountPerDay(),0,nextDate.getTime()));
                List<PillUsage> canceledUsages = database.pillUsageDao().getCanceledUsages(lastPill.getPillId());
                for(PillUsage usage : canceledUsages){
                    usage.setCancelable(false);
                }
                database.pillUsageDao().update(canceledUsages);


            }
        }


    }

    private static boolean isNextDay(long first, long second) {

        PersianDate firstDate = new PersianDate(first);
        PersianDate secondDate = new PersianDate(second);
        if(firstDate.getHour()>secondDate.getHour()){
            return true;
        }else if(firstDate.getHour()==secondDate.getHour() && firstDate.getMinute()>secondDate.getMinute()){
            return true;
        }else {
            return false;
        }

    }


    private static PersianDate addDays(int distanceDay, PersianDate date) {
        for(int i = 0 ; i<distanceDay;i++){
            date.addDay(1);
        }
        return date;
    }

    public static void deletePill(PillShelf item,Context context) {
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        database.pillUsageDao().deleteTempPill(item.getName(),item.getCatName());
        database.pillObjectDao().deleteTempPill(item.getName(),item.getCatName());
        SyncController syncController = new SyncController();
        syncController.checkDataBaseForUpdate();
    }

    public static void resetFactoryUsage(PillUsage item, Context context) {
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        item.setUsageTime(item.getSetedTime());
        item.setState(0);
        item.setUsedTime(0);
        item.setHasDelay(0);
        item.setIsSync(0);
        database.pillUsageDao().update(item);
        SyncController sync = new SyncController();
        sync.checkDataBaseForUpdate();
    }

    public static ArrayList<String> getAllMidNames(Context context) {
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        ArrayList<String> allDistinct = new ArrayList<>(database.pillObjectDao().getAllDistinctPillNames());
        allDistinct.add(0,"همه داروها");
        return allDistinct;
    }

    public static ArrayList<String> getAllCatNames(Context context) {
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        ArrayList<String> allDistinct = new ArrayList<>(database.pillObjectDao().getAllDistinctPillCatNames());
        allDistinct.add(0,"همه مصرف کننده ها");
        return allDistinct;
    }

    public static void stopPill(PillShelf item, Context context) {
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);
        PersianDate date = new PersianDate(System.currentTimeMillis());
        database.pillUsageDao().deleteTempPill(item.getName(),item.getCatName(),date.getTime());
        PillObject pillObject = database.pillObjectDao().specialPil(item.getName(),item.getCatName());
        pillObject.setState(2);
        pillObject.setSync(0);
        database.pillObjectDao().update(pillObject);
        SyncController sync = new SyncController();
        sync.checkDataBaseForUpdate();
        Utility.reCalculateManager(context);
    }


}
