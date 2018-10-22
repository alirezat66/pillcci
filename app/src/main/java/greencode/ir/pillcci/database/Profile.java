package greencode.ir.pillcci.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by alireza on 6/1/18.
 */
@Entity(tableName = "profile")
public class Profile {
    @PrimaryKey(autoGenerate = false)
    int id;
    String phone;
    String fName;
    String lName;
    int sex; // 1=male , 2= female, 3= unknown;
    int blood ; //1=A+ ,  2= A-, 3=B+,4=B- , 5 = AB+ , 6 = AB-, 7 =  O+ , 8 = O-
    String birthDay;
    String age;
    String height;
    String weight;
    String sickness;
    String alergy;
    String img;
    boolean isSync;
    public Profile(String phone, String fName, String lName, int sex,int blood, String birthDay, String age, String height, String weight, String sickness, String alergy) {
        this.phone = phone;
        this.fName = fName;
        this.lName = lName;
        this.sex = sex;
        this.birthDay = birthDay;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.sickness = sickness;
        this.alergy = alergy;
        this.blood = blood;
        this.img ="";
        this.isSync = false;
    }
    public String getMyId(){
        return  this.id+"";
    }
    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public String getImg() {
        if(img==null){
            return "";
        }else {
            return img;
        }
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setSickness(String sickness) {
        this.sickness = sickness;
    }

    public void setAlergy(String alergy) {
        this.alergy = alergy;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPhone() {
        return phone;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public int getSex() {
        return sex;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public String getAge() {
        return age;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getSickness() {
        return sickness;
    }

    public String getAlergy() {
        return alergy;
    }

    public int getBlood() {
        return blood;
    }
}
