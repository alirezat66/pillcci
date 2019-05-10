package greencode.ir.pillcci.database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CategoryDao {

    @Query("select * from  category")
    List<Category> listOfCats();

    @Query("select * from category where name= :name limit 1")
    Category specialCat(String name);

    @Insert(onConflict = REPLACE)
    void insertCat(Category category);

    @Query("DELETE FROM category")
    public void nukeTable();
}
