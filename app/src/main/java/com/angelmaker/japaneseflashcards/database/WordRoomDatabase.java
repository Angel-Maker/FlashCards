package com.angelmaker.japaneseflashcards.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

@Database(entities = {Word.class, OngoingWord.class, LessonWord.class}, version = 6, exportSchema = false)
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
                            .addMigrations(MIGRATION_3_4)
                            .addMigrations(MIGRATION_4_5)
                            .addMigrations(MIGRATION_5_6)
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

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE lesson_words_table " +
                            "(id INTEGER PRIMARY KEY NOT NULL, " +
                            "lessonName TEXT," +
                            "wordID INTEGER NOT NULL," +
                            "FOREIGN KEY (wordID) REFERENCES words_table(id) ON UPDATE NO ACTION ON DELETE CASCADE)"
            );
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE lesson_words_table " +
                            "ADD selectionCode INTEGER NOT NULL DEFAULT 2");
        }
    };
}
