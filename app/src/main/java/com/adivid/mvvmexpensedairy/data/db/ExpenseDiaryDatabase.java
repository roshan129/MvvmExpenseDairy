package com.adivid.mvvmexpensedairy.data.db;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import static com.adivid.mvvmexpensedairy.utils.Constants.KEY_DB_NAME;

@Database(entities = {ExpenseEntity.class}, version = 4, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ExpenseDiaryDatabase extends RoomDatabase {

    private static ExpenseDiaryDatabase databaseInstance;

    public abstract ExpenseDao getExpenseDao();

    public static synchronized ExpenseDiaryDatabase getInstance(Context context){
        if(databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    ExpenseDiaryDatabase.class, KEY_DB_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return databaseInstance;
    }

    private static final RoomDatabase.Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
