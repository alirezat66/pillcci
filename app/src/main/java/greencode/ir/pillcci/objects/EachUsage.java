package greencode.ir.pillcci.objects;

/**
 * Created by alireza on 5/21/18.
 */

public class EachUsage {
    String timeStr;
    String unit;
    String EachUse;

    public EachUsage(String timeStr, String unit, String eachUse) {
        this.timeStr = timeStr;
        this.unit = unit;
        EachUse = eachUse;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getEachUse() {
        return EachUse;
    }

    public void setEachUse(String eachUse) {
        EachUse = eachUse;
    }
}
