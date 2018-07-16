package greencode.ir.pillcci.objects;

/**
 * Created by alireza on 5/30/18.
 */

public class PillShelf {
    String name;
    String lastUse;
    String nextUsage;
    String catName;
    String img;
    int catColer;
    String unit;
    String drName;
    String reapetCount;
    int state;
    int usageType;

    public PillShelf(String name, String lastUse, String nextUsage, String catName, String img, int catColer, String unit, String drName, String reapetCount,int state,int usageType) {
        this.name = name;
        this.lastUse = lastUse;
        this.nextUsage = nextUsage;
        this.catName = catName;
        this.img = img;
        this.catColer = catColer;
        this.unit = unit;
        this.drName = drName;
        this.reapetCount = reapetCount;
        this.state = state;
        this.usageType = usageType;
    }

    public int getUsageType() {
        return usageType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCatColer() {
        return catColer;
    }

    public String getUnit() {
        return unit;
    }

    public String getDrName() {
        if(drName.equals("")){
            return "نامشخص";
        }
        return drName;
    }

    public String getReapetCount() {
        return reapetCount;
    }

    public String getName() {
        return name;
    }

    public String getLastUse() {
        return lastUse;
    }

    public String getNextUsage() {
        return nextUsage;
    }

    public String getCatName() {
        return catName;
    }

    public String getImg() {
        return img;
    }
    public String getCount(){
        String title="";
        switch (this.getReapetCount()){
            case "1":
                title="هر ۲۴ ساعت";
                break;
            case "2":
                title="هر ۱۲ ساعت";
                break;
            case "3":
                title="هر ۸ ساعت";
                break;
            case "4":
                title="هر ۶ ساعت";
                break;
            case "5":
                title="هر ۵ ساعت";
                break;
            case "6":
                title="هر ۴ ساعت";
                break;
            case "7":
                title="هر ۳.۵ ساعت";
                break;
            case "8":
                title="هر ۳ ساعت";
                break;
            case "12":
                title="هر ۲ ساعت";
                break;
            case "24":
                title="هر ۱ ساعت";
                break;
            default:
                title="نا مشخص";
                break;

        }
        return title;
    }
}
