package com.angelmaker.japaneseflashcards.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface WordDao {

    //Adds new word
    @Insert
    void addWord(Word word);

    //Removes word
    @Delete
    void removeWord(Word word);

    //Update existing word
    @Update
    void updateWord(Word word);

    @Query ("SELECT * FROM words_table")
    List<Word> getAllWords();

    @Query ("SELECT * FROM words_table")
    LiveData<List<Word>> getAllWordsLive();
}
