package greencode.ir.pillcci.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface PillObjectDao {

    @Query("select * from  pil")
    List<PillObject> address();

    @Query("select midname from pil")
    List<String> getAllPillNames();
    @Query("select distinct midname from pil")
    List<String> getAllDistinctPillNames();
    @Query("select distinct catName from pil")
    List<String> getAllDistinctPillCatNames();
    @Query("select distinct drName from pil ")
    List<String> getAllDrNames();
    @Query("select * from pil where id= :id limit 1")
    PillObject specialPil(int id);

    @Query("select * from pil where midname =:name and catName=:catName limit 1")
    PillObject specialPil(String name,String catName);

    @Insert(onConflict = REPLACE)
    void insertPill(PillObject address);

    @Query("DELETE FROM pil WHERE midname = :pillName and catName= :pillCat")
    public void deletePill(String pillName,String pillCat);
    @Query("DELETE FROM category")
    public void nukeTable();
    @Update
    void update(PillObject pillObject);

    @Query("select distinct couseOfUse from pil")
    List<String> getReults();
}
