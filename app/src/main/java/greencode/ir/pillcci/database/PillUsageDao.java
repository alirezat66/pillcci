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

    @Query("select * from pilusage where id= :id limit 1")
    PillUsage specialPilUsage(long id);
    @Query("select * from pilusage where usageTime>= :startTime and usageTime<= :endTime order by usageTime")
    List<PillUsage> listPillToday(long startTime, long endTime);


    @Insert(onConflict = REPLACE)
    void insertPillList(List<PillUsage>list);
    @Insert(onConflict = REPLACE)
    void insertPill(PillUsage address);


    @Query("DELETE FROM pilusage")
    public void nukeTable();

    @Delete
    void delete(PillUsage pillUsage);

    @Update
    void update(PillUsage pillUsage);

    @Query("select * from pilusage where usageTime > :thisTime and state=0  order by usageTime  limit 1")
    PillUsage getNearestUsage(long thisTime);
    @Query("select * from pilusage where usageTime > :thisTime and state=0 and pillName=:name  order by usageTime  limit 1")
    PillUsage getNextPill(long thisTime,String name);
    @Query("select * from pilusage where usageTime < :thisTime and state=0 order by usageTime desc  limit 1")
    PillUsage getNearestUsed(long thisTime);

    @Query("select * from pilusage where usageTime < :thisTime and state=1 and pillName=:name order by usageTime desc  limit 1")
    PillUsage getLastPill(long thisTime,String name);
}
