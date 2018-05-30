package greencode.ir.pillcci.objects;

/**
 * Created by alireza on 5/21/18.
 */

public class EndUseFields {
    int useType;
    int AlluseDay;
    double alluseCount;

    public EndUseFields(int useType, int alluseDay, double alluseCount) {
        this.useType = useType;
        AlluseDay = alluseDay;
        this.alluseCount = alluseCount;
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
