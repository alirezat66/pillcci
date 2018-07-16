package greencode.ir.pillcci.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ProfileDao {

    @Query("select * from  profile")
    List<Profile> listOfCats();
    @Query("select * from profile limit 1")
    Profile getMyProfile();

    @Query("select * from profile where phone= :phone limit 1")
    Profile specialProfile(String phone);

    @Insert(onConflict = REPLACE)
    void insertProfile(Profile profile);

    @Query("DELETE FROM profile")
    public void nukeTable();

    @Update
    void update(Profile profile);
}
