package greencode.ir.pillcci.retrofit.reqobject;

import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.utils.ToJsonClass;

public class SendedDrug extends ToJsonClass {


    int localId;
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
    int state;// 1==active 2== deactive

    String user_id;


    public SendedDrug(PillObject object , String user_id) {
        this.user_id = user_id;
        this.localId = object.getid();
        this.b64 = object.getB64();
        this.midname = object.getMidname();
        this.causeOfUse = object.getCouseOfUse();
        this.drName = object.getDrName();
        this.catName = object.getCatName();
        this.catColor = object.getCatColor();
        this.catring = object.getCatring();
        this.typeOfUsage = object.getTypeOfUsage();
        this.isRegular = object.isRegular();
        this.repeatUsageDay = object.getRepeatUsageDay();
        this.days = object.getDays();
        this.diffrenceOfUsage = object.getDiffrenceOfUsage();
        this.countOfUsagePerDay = object.getCountOfUsagePerDay();
        this.startHour = object.getStardHour();
        this.startMin = object.getStartMin();
        this.unitUse = object.getUnitUse();
        this.unitCount = object.getUnitsCount();
        this.unitTimes = object.getUnitTimes();
        this.useType = object.getUseType();
        this.allUseDays = object.getAllUseDays();
        this.totalAmounts = object.getTotalAmounts();
        this.firstAlarmTime = object.getFirstAlarmTime();
        this.daysOfUse = object.getDaysOfUse();
        this.amountOfUse = object.getAmountOfUse();
        this.description = object.getDescription();
        this.hasLight = object.getLight();
        this.hasVibrate = object.getVibrate();
        this.reminderDays = object.getReminderDays();
        this.allPillCount = object.getAllPillCount();
        this.state = object.getState();
    }
}
