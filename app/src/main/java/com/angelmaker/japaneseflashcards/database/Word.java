package com.angelmaker.japaneseflashcards.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "words_table")
public class Word implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "english")
    private String mEnglish;            //English version of translation

    @ColumnInfo(name = "japanese")
    private String mJapanese;           //Japanese version of translation

    @ColumnInfo(name = "hintEtoJ")
    private String mHintEtoJ;               //Hint of translation

    @ColumnInfo(name = "hintJtoE")
    private String mHintJtoE;               //Hint of translation


    public void setId(int newId){this.id = newId;}
    public void setEnglish(String newEnglish){this.mEnglish = newEnglish;}
    public void setJapanese(String newJapanese){this.mJapanese = newJapanese;}
    public void setHintEtoJ(String newHintEtoJ){this.mHintEtoJ = newHintEtoJ;}
    public void setHintJtoE(String newHintJtoE){this.mHintJtoE = newHintJtoE;}


    public int getId(){return this.id;}
    public String getEnglish(){return this.mEnglish;}
    public String getJapanese(){return this.mJapanese;}
    public String getHintEtoJ(){return this.mHintEtoJ;}
    public String getHintJtoE(){return this.mHintJtoE;}
}
