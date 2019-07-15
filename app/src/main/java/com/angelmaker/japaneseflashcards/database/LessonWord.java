package com.angelmaker.japaneseflashcards.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "lesson_words_table",
        foreignKeys = @ForeignKey(entity = Word.class,
        parentColumns = "id",
        childColumns = "wordID",
        onDelete = ForeignKey.CASCADE))
public class LessonWord implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "wordID")
    private int mWordID;

    @ColumnInfo(name = "lessonName")
    private String mLessonName;


    public void setId(int newId){this.id = newId;}
    public void setWordID(int wordID){this.mWordID = wordID;}
    public void setLessonName(String lessonName){this.mLessonName = lessonName;}

    public int getId(){return this.id;}
    public int getWordID(){return this.mWordID;}
    public String getLessonName(){return this.mLessonName;}
}
