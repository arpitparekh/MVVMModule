package arpit.parekh.mvvmmodule.mvvm_room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Student.class},exportSchema = false,version = 1)
public abstract class StudentDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "StudentDatabase";
    public static StudentDatabase database;
    private static final Object LOCK = new Object();

    public abstract StudentDao getDao();

    public static StudentDatabase getDatabase(Context context){

        if(database==null){

            synchronized(LOCK){   // only one class can access database object

                database = Room.databaseBuilder(context,StudentDatabase.class,DATABASE_NAME)
//                        .allowMainThreadQueries()   // we will only run queries on background thread in repo class
                        .build();

            }
        }
        return database;

    }

}
