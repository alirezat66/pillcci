package greencode.ir.pillcci.database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

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
