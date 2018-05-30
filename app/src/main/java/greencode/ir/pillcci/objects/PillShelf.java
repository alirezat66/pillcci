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

    public PillShelf(String name, String lastUse, String nextUsage,String catName,String img) {
        this.name = name;
        this.lastUse = lastUse;
        this.nextUsage = nextUsage;
        this.catName = catName;
        this.img = img;
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
}
