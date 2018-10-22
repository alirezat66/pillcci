package greencode.ir.pillcci.retrofit.reqobject;

import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.utils.ToJsonClass;

public class SendedUsage extends ToJsonClass {
   String pillId;
   String localId ;
   String time;
   String useTime;
    String description;
    String usageTime;
    String hasDelay;
    String catName;

   String catColor;
   String catRingtone;

   String drName;
  String unit;

    String unitAmount;
    String countPerDay;
           String usedTime;
           String snoozCount;
           String setedTime;
    String user_id;
    String pillName;

    int state ;
    public SendedUsage(String user_id, PillUsage usage) {
        this.user_id = user_id;
        this.pillId = usage.getPillId()+"";
        this.localId = usage.getid()+"";
        this.time =usage.getTime()+"";
        this.useTime = usage.getUseTime()+"";
        this.description = usage.getDescription();
        this.usageTime = usage.getUsageTime()+"";
        this.hasDelay = usage.isHasDelay()+"";
        this.catName = usage.getCatNme();
        this.catColor =usage.getCatColor()+"";
        this.catRingtone = usage.getCatRingtone();
        this.drName = usage.getDrName();
        this.unit = usage.getUnit();
        this.unitAmount = usage.getUnitAmount();
        this.countPerDay = usage.getCountPerDay();
        this.usedTime = usage.getUsedTime()+"";
        this.snoozCount= usage.getSnoozCount()+"";
        this.setedTime = usage.getSetedTime()+"";
        this.pillName = usage.getPillName();
        this.state = usage.getState();
    }
}
