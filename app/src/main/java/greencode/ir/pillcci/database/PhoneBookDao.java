package greencode.ir.pillcci.database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

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

    @Query("DELETE FROM phoneBook WHERE  id = :id")
    void deletePhone(long id);
    @Update
    void update(PhoneBook phoneBook);


}
