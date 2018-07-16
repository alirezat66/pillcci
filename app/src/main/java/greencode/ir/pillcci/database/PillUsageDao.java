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

    @Query("select * from  pilusage")
    List<PillUsage> listPillUsage();
 @Query("select * from  pilusage where pillName=:pillName and catNme=:catName")
    List<PillUsage> listSpecialPillUsage(String pillName,String catName);
   @Query("select * from  pilusage where pillName=:pillName and state=1 and catNme=:catname" )
   List<PillUsage> listSpecialUsedPillUsage(String pillName,String catname);
    @Query("select * from pilusage where id= :id limit 1")
    PillUsage specialPilUsage(long id);
    @Query("select * from pilusage where usageTime>= :startTime and usageTime<= :endTime order by setedTime")
    List<PillUsage> listPillToday(long startTime, long endTime);



    @Insert(onConflict = REPLACE)
    void insertPillList(List<PillUsage>list);
    @Insert(onConflict = REPLACE)
    void insertPill(PillUsage address);


    @Query("DELETE FROM pilusage")
    public void nukeTable();

    @Delete
    void delete(PillUsage pillUsage);
   @Query("DELETE FROM pilusage WHERE pillName = :pillName and catNme= :pillCat")
   public void deletePill(String pillName,String pillCat);
    @Update
    void update(PillUsage pillUsage);
    @Update
    void update(List<PillUsage>usages);

    @Query("select * from pilusage where usageTime< :nowTime and state = 0  ")
    List<PillUsage> getAllExpendedPillUsage(long nowTime);

    @Query("select * from pilusage where usageTime > :thisTime and state=0  order by usageTime  limit 1")
    PillUsage getNearestUsage(long thisTime);

    @Query("select * from pilusage where usageTime > :thisTime and state=0 and pillName=:name and catNme=:catname order by usageTime  limit 1")
    PillUsage getNextPill(long thisTime,String name,String catname);

    @Query("select * from pilusage where usageTime <= :thisTime and state=0 order by usageTime desc  limit 1")
    PillUsage getNearestUsed(long thisTime);

    @Query("select * from pilusage where usageTime <= :thisTime and state=0 order by usageTime desc")
    List<PillUsage> getNearestUsedList(long thisTime);


    @Query("select * from pilusage where usageTime < :thisTime and state=1 and pillName=:name and catNme=:catname  order by usageTime desc  limit 1")
    PillUsage getLastPill(long thisTime,String name,String catname);

    @Query("select * from pilusage where pillName= :pillName and catNme=:catname order by usageTime desc limit 1")
    PillUsage getPillLastUsage(String pillName,String catname);


    @Query("select * from pilusage where usageTime < :nextMonthTime and state<>0 order by usageTime  ")
    List<PillUsage> getHistory(long nextMonthTime);
    @Query("SELECT Count(*) FROM pilusage where pillName=:midname and catNme=:catname and state<>2" )
    int getCountOfAllUsage(String midname,String catname);
 @Query("DELETE FROM pilusage WHERE pillName = :name and catNme= :catName and setedTime>:time ")
    void deletePill(String name, String catName, Long time);
    @Query("select * from pilusage where pillName = :pillName and catNme=:catNme and state=0 order by usageTime desc")
    List<PillUsage> getAllNotUsed(String pillName, String catNme);
}
