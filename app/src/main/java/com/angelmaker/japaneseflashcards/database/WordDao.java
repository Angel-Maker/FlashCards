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

    //Persistent word list
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

    @Query ("SELECT * FROM words_table WHERE english LIKE :subString OR japanese LIKE :subString")
    LiveData<List<Word>> getSearchedWordsLive(String subString);


    //Ongoing word list
    @Insert
    void addOngoingWord(OngoingWord ongoingWord);

    @Query ("DELETE FROM ongoing_words_table")
    void dropTable();

    @Query ("UPDATE ongoing_words_table SET isCorrect = :result WHERE ID = :id")
    void updateOngoingWord(int id, int result);

    @Query ("SELECT * FROM ongoing_words_table")
    List<OngoingWord> getOngoingWords();

    @Query ("SELECT COUNT(*) FROM ongoing_words_table")
    int getOngoingWordsSize();


    //Lessons list
    @Insert
    void addLessonWord(LessonWord lessonWord);

    @Query ("SELECT DISTINCT lessonName FROM lesson_words_table")
    LiveData<List<String>> getLessonsLive();

    @Query ("SELECT * FROM lesson_words_table WHERE lessonName IS :lessonListName")
    List<LessonWord> getLessonWords(String lessonListName);

    @Query ("DELETE FROM lesson_words_table WHERE lessonName IS :lessonListName")
    void removeLesson(String lessonListName);
}
