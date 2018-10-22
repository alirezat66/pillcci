package greencode.ir.pillcci.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface PillUsageDao {

    @Query("select * from  pilusage where  state<> 4")
    List<PillUsage> listPillUsage();
    @Query("select * from  pilusage where pillName=:pillName and catName=:catName and state<> 4")
    List<PillUsage> listSpecialPillUsage(String pillName,String catName);

    @Query("select * from pilusage where state== 4")
    List<PillUsage> allDeleted();



    @Query("select * from  pilusage where  isSync = 0 and state<> 4")
    List<PillUsage> allNotSyncPill();

   @Query("select * from  pilusage where pillName=:pillName and state=1 and catName=:catname" )
   List<PillUsage> listSpecialUsedPillUsage(String pillName,String catname);
    @Query("select * from pilusage where id= :id  limit 1 ")
    PillUsage specialPilUsage(long id);
    @Query("select * from pilusage where usageTime>= :startTime and usageTime<= :endTime and state<> 4 order by setedTime")
    List<PillUsage> listPillToday(long startTime, long endTime);



    @Insert(onConflict = REPLACE)
    void insertPillList(List<PillUsage>list);
    @Insert(onConflict = REPLACE)
    void insertPill(PillUsage address);


    @Query("DELETE FROM pilusage")
    public void nukeTable();

    @Delete
    void delete(PillUsage pillUsage);

   @Query("DELETE FROM pilusage WHERE pillName = :pillName and catName= :pillCat")
   public void deletePill(String pillName,String pillCat);
    @Update
    void update(PillUsage pillUsage);
    @Update
    void update(List<PillUsage>usages);

    @Query("select * from pilusage where usageTime< :nowTime and state = 0  ")
    List<PillUsage> getAllExpendedPillUsage(long nowTime);

    @Query("select * from pilusage where usageTime > :thisTime and state=0  order by usageTime  limit 1")
    PillUsage getNearestUsage(long thisTime);

    @Query("select * from pilusage where usageTime > :thisTime and state=0 and pillName=:name and catName=:catname order by usageTime  limit 1")
    PillUsage getNextPill(long thisTime,String name,String catname);

    @Query("select * from pilusage where usageTime <= :thisTime and state=0 order by usageTime desc  limit 1")
    PillUsage getNearestUsed(long thisTime);

    @Query("select * from pilusage where usageTime <= :thisTime and state=0 order by usageTime desc")
    List<PillUsage> getNearestUsedList(long thisTime);


    @Query("select * from pilusage where usageTime < :thisTime and state=1 and pillName=:name and catName=:catname  order by usageTime desc  limit 1")
    PillUsage getLastPill(long thisTime,String name,String catname);

    @Query("select * from pilusage where pillName= :pillName and catName=:catname  and state<> 4 order by usageTime desc limit 1")
    PillUsage getPillLastUsage(String pillName,String catname);


    @Query("select * from pilusage where usageTime < :nextMonthTime and (state =1 or state = 2 or state = 3) order by usageTime  ")
    List<PillUsage> getHistory(long nextMonthTime);
    @Query("SELECT Count(*) FROM pilusage where pillName=:midname and catName=:catname and (state = 1 or state = 3)" )
    int getCountOfAllUsage(String midname,String catname);
 @Query("DELETE FROM pilusage WHERE pillName = :name and catName= :catName and setedTime>:time and state<> 4")
    void deletePill(String name, String catName, Long time);
    @Query("select * from pilusage where pillName = :pillName and catName=:catNme and state=0 order by usageTime desc")
    List<PillUsage> getAllNotUsed(String pillName, String catNme);
    @Query("select id from (select * from pilusage order by id desc limit 1)")
    long getLastId();

    @Query("update pilusage set  state = 4  WHERE pillName = :name and catName= :catName and setedTime>:time ")
    void deleteTempPill(String name, String catName, long time);
    @Query("update pilusage set  state = 4  WHERE pillName = :name and catName= :catName ")
    public void deleteTempPill(String name,String catName);

    @Query("DELETE FROM pilusage WHERE  state = 4")
    void deleteAllDeleted();
    @Query("select * from pilusage where state = 2 and pillId = :pillId")
    List<PillUsage> getCanceledUsages(int pillId);
}
