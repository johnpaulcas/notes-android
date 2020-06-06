package com.johnpaulcas.notes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.johnpaulcas.notes.db.dao.NoteDao
import com.johnpaulcas.notes.db.model.Note

/**
 * Created by johnpaulcas on 06/06/2020.
 */
@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase {
            val instance = INSTANCE

            if (instance != null) {
                return instance
            }

            synchronized(this) {
                val databaseInstance = Room.databaseBuilder(context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database")
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = databaseInstance

                return databaseInstance
            }
        }
    }
}