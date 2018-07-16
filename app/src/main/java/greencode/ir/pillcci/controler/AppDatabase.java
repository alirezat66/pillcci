package greencode.ir.pillcci.controler;

//import com.raizlabs.android.dbflow.annotation.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import greencode.ir.pillcci.database.Category;
import greencode.ir.pillcci.database.CategoryDao;
import greencode.ir.pillcci.database.PhoneBook;
import greencode.ir.pillcci.database.PhoneBookDao;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillObjectDao;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.database.PillUsageDao;
import greencode.ir.pillcci.database.Profile;
import greencode.ir.pillcci.database.ProfileDao;

@Database(entities = {Category.class,PillUsage.class, PillObject.class, PhoneBook.class, Profile.class}, version =3)

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    public abstract CategoryDao categoryDao();
    public abstract PillObjectDao pillObjectDao();
    public abstract PillUsageDao pillUsageDao();
    public abstract PhoneBookDao phoneBookDao();
    public abstract ProfileDao profileDao();


    public static AppDatabase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,AppDatabase.class.getName())
                            // To simplify the codelab, allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}