package greencode.ir.pillcci.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface PillObjectDao {

    @Query("select * from  pil")
    List<PillObject> address();

    @Query("select midname from pil")
    List<String> getAllPillNames();
    @Query("select distinct drName from pil ")
    List<String> getAllDrNames();
    @Query("select * from pil where id= :id limit 1")
    PillObject specialPil(int id);

    @Query("select * from pil where midname =:name limit 1")
    PillObject specialPil(String name);

    @Insert(onConflict = REPLACE)
    void insertPill(PillObject address);


    @Query("DELETE FROM category")
    public void nukeTable();
}
