package com.rove.datalayer.Data;

import android.app.Application;
import android.content.Context;

import com.rove.datalayer.Util.Converters;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Entity_Note.class},version = 1)
@TypeConverters({Converters.class})
public abstract class DatabaseInteractor extends RoomDatabase {
    public abstract mDao getDao();
    private static DatabaseInteractor instance;

    public static synchronized DatabaseInteractor getDatabaseInstance(Context context){
      if(instance==null){
          instance = Room.databaseBuilder(context.getApplicationContext(),DatabaseInteractor.class,"NotesDB")
                  .fallbackToDestructiveMigration().build();
      }
      return instance;
    }
}
