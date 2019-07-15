package com.angelmaker.japaneseflashcards.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "ongoing_words_table")
public class OngoingWord implements Serializable {

    @PrimaryKey()
    private int id;

    @ColumnInfo(name = "english")
    private String mEnglish;            //English version of translation

    @ColumnInfo(name = "japanese")
    private String mJapanese;           //Japanese version of translation

    @ColumnInfo(name = "hintEtoJ")
    private String mHintEtoJ;               //Hint of translation

    @ColumnInfo(name = "hintJtoE")
    private String mHintJtoE;               //Hint of translation

    @ColumnInfo(name = "isCorrect")
    private int mIsCorrect;               //Hint of translation


    public void setId(int newId){this.id = newId;}
    public void setEnglish(String newEnglish){this.mEnglish = newEnglish;}
    public void setJapanese(String newJapanese){this.mJapanese = newJapanese;}
    public void setHintEtoJ(String newHintEtoJ){this.mHintEtoJ = newHintEtoJ;}
    public void setHintJtoE(String newHintJtoE){this.mHintJtoE = newHintJtoE;}
    public void setIsCorrect(int newIsCorrect){this.mIsCorrect = newIsCorrect;}


    public int getId(){return this.id;}
    public String getEnglish(){return this.mEnglish;}
    public String getJapanese(){return this.mJapanese;}
    public String getHintEtoJ(){return this.mHintEtoJ;}
    public String getHintJtoE(){return this.mHintJtoE;}
    public int getIsCorrect(){return this.mIsCorrect;}
}
