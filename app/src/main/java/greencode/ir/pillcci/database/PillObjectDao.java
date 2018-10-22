package greencode.ir.pillcci.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface PillObjectDao {

    @Query("select * from  pil where state<>3")
    List<PillObject> getAllPill();
    @Query("select * from  pil where isSync = 0 and state<> 3")
    List<PillObject> getAllUnSyncPill();


    @Query("select * from  pil where state = 3")
    List<PillObject> getAllDeletedPill();

    @Query("select midname from pil where state <>3")
    List<String> getAllPillNames();
    @Query("select distinct midname from pil")
    List<String> getAllDistinctPillNames();
    @Query("select distinct catName from pil ")
    List<String> getAllDistinctPillCatNames();
    @Query("select distinct drName from pil ")
    List<String> getAllDrNames();
    @Query("select * from pil where id= :id limit 1")
    PillObject specialPil(int id);

    @Query("select * from pil where midname =:name and catName=:catName and state<> 3 limit 1")
    PillObject specialPil(String name,String catName);

    @Insert(onConflict = REPLACE)
    void insertPill(PillObject address);

    @Query("DELETE FROM pil WHERE midname = :pillName and catName= :pillCat")
    public void deletePill(String pillName,String pillCat);
    @Query("DELETE FROM pil")
    public void nukeTable();
    @Update
    void update(PillObject pillObject);

    @Query("select distinct causeOfUse from pil")
    List<String> getReults();
    @Query("select id from (select * from pil order by id desc limit 1)")
    int getLastId();
    @Query("update pil set state = 3  WHERE midname = :name and catName= :catName")
    void deleteTempPill(String name, String catName);

}
