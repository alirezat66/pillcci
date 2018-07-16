package greencode.ir.pillcci.objects;

/**
 * Created by alireza on 5/21/18.
 */

public class EndUseFields {
    int useType;
    int AlluseDay;
    double alluseCount;
    int dayReminder;
    double remindAllPill;
    public EndUseFields(int useType, int alluseDay, double alluseCount,int dayReminder,double remindAllPill) {
        this.useType = useType;
        this.AlluseDay = alluseDay;
        this.alluseCount = alluseCount;
        this.dayReminder=dayReminder;
        this.remindAllPill=remindAllPill;
    }

    public int getDayReminder() {
        return dayReminder;
    }

    public double getRemindAllPill() {
        return remindAllPill;
    }

    public int getUseType() {
        return useType;
    }

    public int getAlluseDay() {
        return AlluseDay;
    }

    public double getAlluseCount() {
        return alluseCount;
    }
}
