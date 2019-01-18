package com.angelmaker.japaneseflashcards.activities;
//TODO - 30m - Re-understand codebase
//TODO - 30m - Change DB migration from destructive to upgrade
//TODO - 3h  - Create a second DB for an ongoing test attempt
//TODO - 2h  - Reorganize FlashCardFragment (if needed)
//TODO - 2h  - Hook up new DB to active attempts
//TODO - 1h  - Add Main Menu option to continue last attempt

//TODO - 1h  - Add search feature to Edit Words
//TODO - 2h  - Segment FlashCardFragment so only the middle section moves
//TODO - 1h  - Make timer stop when app closes

//TODO - 8h  - Create arbitrary DBs from word select to create word lists

//TODO - 2h  - Add counter to each word for # of attempts

//TODO - Add metrics
//TODO - Create new list of words at end of attempt based filled with incorrect answers
//TODO - Reorganize FlashCardFragment UI
//TODO - Add UI to manage word lists

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.angelmaker.japaneseflashcards.R;

public class HomeMenu extends AppCompatActivity {
    //Activity elements
    private Button btnUpdateWords;
    private Button btnTestYourself;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        linkActivityElements();
        setClickables();
    }

    //Find and set the activity elements
    private void linkActivityElements()
    {
        btnUpdateWords = findViewById(R.id.btnUpdateWords);
        btnTestYourself = findViewById(R.id.btnTestYourself);
    }

    //Set clickable actions
    private void setClickables()
    {
        btnUpdateWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateWords = new Intent(view.getContext(), UpdateWords.class);
                startActivity(updateWords);
            }
        });


        btnTestYourself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent testYourself = new Intent(view.getContext(), WordSelector.class);
                startActivity(testYourself);
            }
        });
    }
}
