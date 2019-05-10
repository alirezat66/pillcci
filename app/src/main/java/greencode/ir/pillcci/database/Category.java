package greencode.ir.pillcci.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Created by alireza on 5/19/18.
 */
@Entity(tableName = "category")
public class Category {
    @PrimaryKey
    @NonNull
    String name;
    int color;
    String ringtone;

    public Category(String name, int color, String ringtone) {
        this.name = name;
        this.color = color;
        this.ringtone = ringtone;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getRingtone() {
        return ringtone;
    }

    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
    }
}
