package greencode.ir.pillcci.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface PhoneBookDao {

    @Query("select * from  phoneBook where state == 0 and not isInitial")
    List<PhoneBook> allUnSyncPhone();

    @Query("select * from  phoneBook where state<>2")
    List<PhoneBook> listOfPhone();

    @Query("select * from phoneBook where phone= :phone and state<>2 limit 1")
    PhoneBook specialPhone(String phone);

    @Query("select * from phoneBook where id= :phone and state<>2 limit 1")
    PhoneBook specialPhone(long phone);

    @Insert(onConflict = REPLACE)
    void insertPhone(PhoneBook phoneBook);

    @Query("DELETE FROM phoneBook")
    public void nukeTable();
    @Update
    void update(PhoneBook phoneBook);


}
