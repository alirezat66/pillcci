package greencode.ir.pillcci.utils;


import java.util.ArrayList;

import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.objects.EndUseFields;
import greencode.ir.pillcci.objects.GeneralFields;
import greencode.ir.pillcci.objects.UsageFields;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/22/18.
 */

public class CalcTimesAndSaveUsage {

    public static PillUsage makepillUsage(PillObject pillObject, long timeStamp, int j) {

        String pillName=pillObject.getMidname();
        PersianDate persianCalendar = new PersianDate(timeStamp);

        String time=persianCalendar.getHour()+":"+persianCalendar.getMinute();
        long usageTime=timeStamp;
        int state =0; // 0 = not used ## 1 = used
        String useTime="";// time that we use pill
        long usedTime = 0;
        boolean hasDelay=false;
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
        String countPerDay="";
        switch (pillObject.getCountOfUsagePerDay()){
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


        return new PillUsage(pillObject.getid(),pillName,time,usageTime,state,useTime,hasDelay,description,
                catNme,catColor,catRingtone,drName,unit,unitAmount,countPerDay,usedTime);
    }

    public static int calcNextDayCount(PersianDate tempPersianCalendar, String nextday) {
        int countDay=0;
        while (!tempPersianCalendar.dayName().equals(nextday.trim())){
            tempPersianCalendar.addDay(1);
            countDay++;
        }
        return countDay;
    }

    public static long calculateNext(long timeStamp, double diffrenceOfUsage, int diffrenceDay,int startTime,int startMin,int countusageDay,boolean isregular ) {
        final int constEachMinToMill = 60000;
        final int eachHoursMin = 60;
        final int eachDayToHours = 24;
        if(diffrenceDay==0) {
            return timeStamp  + (int) (diffrenceOfUsage * eachHoursMin * constEachMinToMill);
        }else if(diffrenceDay!=0 && !isregular){
            long newTimeStamp=timeStamp+ (diffrenceDay * eachDayToHours  * eachHoursMin * constEachMinToMill);
            // inja time stamp rooze jadid ba akharin masrafesho darmiarim ama in avalin time nist, hala bayad biaim begim masalan 3 shanbe avalin time
            PersianDate persianCalendar = new PersianDate(newTimeStamp);
            persianCalendar.setHour(startTime);
            persianCalendar.setMinute(startMin);
            persianCalendar.setSecond(0);
            return persianCalendar.getTime();
        }else {
            // bayad mohasebe konim kolan baad az etmam in dore aya mirim to rooze digeyi ya na , masalan age ghorsa az 11 om shoroo mishe
            // va to 12 om saate 4 sobh shoroo mishe , 4 rooz dige nabayad beshe 16 om bayad beshe 15 om
            double distanceBetweenAllUsages = (countusageDay-1)*diffrenceOfUsage;
            int distanceMin = (int) (distanceBetweenAllUsages*60);//min

            long newTimeStamp = 0;
            if(distanceBetweenAllUsages+(startTime*60)+startMin>3600){
                newTimeStamp=timeStamp+ (diffrenceDay * eachDayToHours  * eachHoursMin * constEachMinToMill);

            }else {
                PersianDate calendar = new PersianDate(timeStamp);
                calendar.addDay(diffrenceDay-1);
                calendar.setHour(startTime);
                calendar.setMinute(startMin);
                calendar.setSecond(0);
                newTimeStamp=calendar.getTime();

            }
            PersianDate persianCalendar = new PersianDate(newTimeStamp);
            persianCalendar.setHour(startTime);
            persianCalendar.setMinute(startMin);
            persianCalendar.setSecond(0);
            return persianCalendar.getTime();

        }
    }

    public static PillObject makePillObject(GeneralFields generalFields, UsageFields usageFields, EndUseFields endUseFields) {
        String b64 = generalFields.getB64();// image of pill
        String midname=generalFields.getMidName(); // name of pill
        String couseOfUse=generalFields.getCouse();// reason of use;
        String drName=generalFields.getDrName();// dr name that write this pill

        String catName=(generalFields.getCatName().length()==0) ?"عمومی":generalFields.getCatName(); // category name : default value  is "public"
        int catColor=generalFields.getCatColour(); // it has default too
        String catring=generalFields.getAlarmUrl();// it has default too

        int typeOfUsage = usageFields.getTypeOfdayUsage(); // 1 = every day ## 2= some days with regular repeat ## 3= difrent days inweek;
        boolean isRegular=usageFields.isRegular();// if type 1 or 2 this is true and for 3 this is false

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
        for(String timeStr:usageFields.getUnitTimes()){
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
        calendar.setSecond(0);
        long startTime= calendar.getTime();

        return  new PillObject(b64,midname,couseOfUse,drName,catName,catColor,catring,typeOfUsage,isRegular,usageFields.getRepeatDays(),days,diffrenceOfUsage
                ,countOfUsagePerDay,stardHour,startMin,unitUse,unitsCount,unitTimes,usageFields.getDescription(),useType,allUseDays,totalAmounts,startTime,0,0);
    }

    public static ArrayList<PillUsage> makePillUsageInPerFreeMood(PillObject pillObject) {
        ArrayList<PillUsage>usageList = new ArrayList<>();
        long timeStamp=pillObject.getFirstAlarmTime();
        for(int j=0;j<pillObject.getCountOfUsagePerDay();j++){
            // hale faseleye rooz ro hesab kardim , ma bayad next time stamp ro hesab konim , faghat inja nokte ine ke
            // ma age bedast ovordim 3 faghat baraye 1 bar bayad 3 rooz ro mohasebe konim to in halghe baaghie faghat bayad ba
            // faseleye timi moshakhas beshan
            if(j==0){
                // kolan bare avale time stamp mikhaym bedim ke mishe shoro
                timeStamp = pillObject.getFirstAlarmTime();
            }
            else {
                timeStamp=calculateNext(timeStamp,pillObject.getDiffrenceOfUsage(),0,pillObject.getStardHour(),pillObject.getStartMin(),pillObject.getCountOfUsagePerDay(),pillObject.isRegular());
            }
            PillUsage pillUsage = makepillUsage(pillObject,timeStamp,j);

            usageList.add(pillUsage);

        }

        return usageList;
    }

    public static ArrayList<PillUsage> makePillUsageInPerAmountMood(PillObject pillObject) {
        ArrayList<PillUsage>usageList = new ArrayList<>();
        long timeStamp=pillObject.getFirstAlarmTime();
        int nextDayDefrence=0;
        String[]days = pillObject.getDays().split(",");
        double amountOfUse = pillObject.getTotalAmounts();
        double countingAmount = 0;
        int countLoop=0;
        while (amountOfUse>countingAmount){
            if(pillObject.isRegular()){
                if(pillObject.getTypeOfUsage()==2){
                    // age monazam bood yani ya har rooz bood ya har chand rooz bood
                    if(countLoop!=0){
                        // age bar aval nabood tedad roozhayi ke bayad berim joloo mishe andazeye fielde har chand rooz
                        nextDayDefrence = pillObject.getRepeatUsageDay();
                    }
                    /// toye halate else yani bar avale pas nextesh mishe 0 rooz
                }
                // toye halate elsesh yani roozane boode va nextesh mishe 0 rooz
            }else {
                // halati ke tartibesh maloom nist
                if(days.length>0) {
                    // vaghti day bozorgtar az 0 bashe ke az crashe ehtemali jologiri konim
                    int position = countLoop % days.length;
                    // maloom mikone positione rooze badimoon chand shanbe ast;
                    // farz kon ma listamoon ["shanbe" , yekshanbe , doshanbe ]
                    // alan roze 5 ome estefade hastim ghaadetan darim darmorede 1 shanbe sohbat mikonim
                    String nextday = days[position];
                    // timestamp dar har halgheye i , timeakharin sabto neshoon mide
                    PersianDate tempPersianCalendar = new PersianDate(timeStamp);
                    // in method neshoon mide chand rooz masalan ba yekshanbe fasele darim;
                    nextDayDefrence=calcNextDayCount(tempPersianCalendar,nextday);


                }


                //iregular and some days
            }
            ////////////////////// mohasebeye rooze baad tamam shood///////////////
            for(int j=0;j<pillObject.getCountOfUsagePerDay();j++){
                // hale faseleye rooz ro hesab kardim , ma bayad next time stamp ro hesab konim , faghat inja nokte ine ke
                // ma age bedast ovordim 3 faghat baraye 1 bar bayad 3 rooz ro mohasebe konim to in halghe baaghie faghat bayad ba
                // faseleye timi moshakhas beshan
                if(countLoop==0 && j==0){
                    // kolan bare avale time stamp mikhaym bedim ke mishe shoro
                    timeStamp = pillObject.getFirstAlarmTime();
                }else if(j==0 && nextDayDefrence!=0){
                    // yani next day bayad to mohasebe hesab beshe
                    timeStamp = calculateNext(timeStamp,pillObject.getDiffrenceOfUsage(),nextDayDefrence,pillObject.getStardHour(),pillObject.getStartMin(),pillObject.getCountOfUsagePerDay(),pillObject.isRegular());
                }
                else {
                    timeStamp=calculateNext(timeStamp,pillObject.getDiffrenceOfUsage(),0,pillObject.getStardHour(),pillObject.getStartMin(),pillObject.getCountOfUsagePerDay(),pillObject.isRegular());
                }
                PillUsage pillUsage = makepillUsage(pillObject,timeStamp,j);
                countingAmount+=Double.parseDouble(pillUsage.getUnitAmount());
                if(countingAmount<amountOfUse){
                    usageList.add(pillUsage);
                }
            }

            countLoop++;

        }
        return usageList;
    }

    public static ArrayList<PillUsage> makePillUsageInPerCountMood(PillObject pillObject) {
        ArrayList<PillUsage>usageList = new ArrayList<>();
        long timeStamp=pillObject.getFirstAlarmTime();
        int nextDayDefrence=0;
        String[]days = pillObject.getDays().split(",");
        for(int i = 0;i<pillObject.getAllUseDays();i++){// be tedade kole roozhaye estefadamoon for mizanim
            if(pillObject.isRegular()){
                if(pillObject.getTypeOfUsage()==2){
                    // age monazam bood yani ya har rooz bood ya har chand rooz bood
                    if(i!=0){
                        // age bar aval nabood tedad roozhayi ke bayad berim joloo mishe andazeye fielde har chand rooz
                        nextDayDefrence = pillObject.getRepeatUsageDay();
                    }
                    /// toye halate else yani bar avale pas nextesh mishe 0 rooz
                }
                // toye halate elsesh yani roozane boode va nextesh mishe 0 rooz
            }else {
                // halati ke tartibesh maloom nist
                if(days.length>0) {
                    // vaghti day bozorgtar az 0 bashe ke az crashe ehtemali jologiri konim
                    int position = i % days.length;
                    // maloom mikone positione rooze badimoon chand shanbe ast;
                    // farz kon ma listamoon ["shanbe" , yekshanbe , doshanbe ]
                    // alan roze 5 ome estefade hastim ghaadetan darim darmorede 1 shanbe sohbat mikonim
                    String nextday = days[position];
                    // timestamp dar har halgheye i , timeakharin sabto neshoon mide
                    PersianDate tempPersianCalendar = new PersianDate(timeStamp);
                    // in method neshoon mide chand rooz masalan ba yekshanbe fasele darim;
                    nextDayDefrence=calcNextDayCount(tempPersianCalendar,nextday);


                }
                //iregular and some days
            }
            for(int j=0;j<pillObject.getCountOfUsagePerDay();j++){
                // hale faseleye rooz ro hesab kardim , ma bayad next time stamp ro hesab konim , faghat inja nokte ine ke
                // ma age bedast ovordim 3 faghat baraye 1 bar bayad 3 rooz ro mohasebe konim to in halghe baaghie faghat bayad ba
                // faseleye timi moshakhas beshan
                if(i==0 && j==0){
                    // kolan bare avale time stamp mikhaym bedim ke mishe shoro
                    timeStamp = pillObject.getFirstAlarmTime();
                }else if(j==0 && nextDayDefrence!=0){
                    // yani next day bayad to mohasebe hesab beshe
                    timeStamp = calculateNext(timeStamp,pillObject.getDiffrenceOfUsage(),nextDayDefrence,pillObject.getStardHour(),pillObject.getStartMin(),pillObject.getCountOfUsagePerDay(),pillObject.isRegular());
                }
                else {

                    timeStamp=calculateNext(timeStamp,pillObject.getDiffrenceOfUsage(),0,pillObject.getStardHour(),pillObject.getStartMin(),pillObject.getCountOfUsagePerDay(),pillObject.isRegular());
                }

                usageList.add(makepillUsage(pillObject,timeStamp,j));
            }
        }
        return usageList;
    }
}
