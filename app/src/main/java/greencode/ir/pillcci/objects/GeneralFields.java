package greencode.ir.pillcci.objects;

/**
 * Created by alireza on 5/21/18.
 */

public class GeneralFields {
    public String midName;
    public String b64;
    public String couse;
    public String drName;
    public String catName;
    public int catColour;
    public String alarmUrl;


    public GeneralFields(String midName, String b64, String couse, String drName, String catName, int catColour, String alarmUrl) {
        this.midName = midName;
        this.b64 = b64;
        this.couse = couse;
        this.drName = drName;
        this.catName = catName;
        this.catColour = catColour;
        this.alarmUrl = alarmUrl;
    }

    public String getMidName() {
        return midName;
    }

    public String getB64() {
        return b64;
    }

    public String getCouse() {
        return couse;
    }

    public String getDrName() {
        return drName;
    }

    public String getCatName() {
        return catName;
    }

    public int getCatColour() {
        return catColour;
    }

    public String getAlarmUrl() {
        return alarmUrl;
    }
}
