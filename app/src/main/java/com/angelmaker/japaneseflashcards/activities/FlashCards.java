package com.angelmaker.japaneseflashcards.activities;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.angelmaker.japaneseflashcards.R;
import com.angelmaker.japaneseflashcards.database.Word;
import com.angelmaker.japaneseflashcards.supportFiles.FlashCardSwipeAdapter;

import java.util.ArrayList;

public class FlashCards extends FragmentActivity {
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_cards);

        ArrayList<Word> words = (ArrayList<Word>) getIntent().getSerializableExtra("wordList");
        createViewPager(words);
    }


    private void createViewPager(ArrayList<Word> words){
        viewPager = findViewById(R.id.pagerQuestions);
        FlashCardSwipeAdapter flashCardSwipeAdapter = new FlashCardSwipeAdapter(getSupportFragmentManager(), this);
        flashCardSwipeAdapter.setWords(words);
        flashCardSwipeAdapter.initializeScoreList(words.size());
        viewPager.setAdapter(flashCardSwipeAdapter);
    }
}
