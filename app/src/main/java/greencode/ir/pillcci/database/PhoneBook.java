package greencode.ir.pillcci.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by alireza on 5/30/18.
 */
@Entity(tableName = "phoneBook")
public class PhoneBook {
    @PrimaryKey(autoGenerate = true)
    long id;
    String fName;
    String lName;
    String img;
    String phone;
    String relation;
    boolean isInitial;
    int state; // 0  not send 1 == sent 2== deleted


    public long getid() {
        return id;
    }

    public PhoneBook(String fName, String lName, String img, String phone, String relation, boolean isInitial) {
        this.fName = fName;
        this.lName = lName;
        this.img = img;
        this.phone = phone;
        this.relation = relation;
        this.isInitial = isInitial;
        this.state = 0;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public boolean isInitial() {
        return isInitial;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getImg() {
        return img;
    }

    public String getPhone() {
        return phone;
    }

    public String getRelation() {
        return relation;
    }
}
