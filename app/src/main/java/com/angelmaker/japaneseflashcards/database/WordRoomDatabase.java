package com.angelmaker.japaneseflashcards.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

@Database(entities = {Word.class, OngoingWord.class}, version = 3, exportSchema = false)
public abstract class WordRoomDatabase extends RoomDatabase {

    public abstract WordDao wordDao();

    private static WordRoomDatabase INSTANCE;

    public static WordRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (WordRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordRoomDatabase.class, "word_db")
                            .addMigrations(MIGRATION_2_3)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE ongoing_words_table " +
                            "(id INTEGER NOT NULL, " +
                            "english TEXT," +
                            "japanese TEXT," +
                            "hintEtoJ TEXT," +
                            "hintJtoE TEXT," +
                            "isCorrect INTEGER NOT NULL, " +
                            "PRIMARY KEY(id))");
        }
    };
}


/*
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE `ongoing_word_list`" +
                            "(`id` INTEGER, " +
                            "`wordID` INTEGER," +
                            "`correct` INTEGER," +
                            "`translationDirection` INTEGER" +
                            "PRIMARY KEY(`id`))");
        }
    };
 */