package com.angelmaker.japaneseflashcards.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.angelmaker.japaneseflashcards.R;
import com.angelmaker.japaneseflashcards.database.Word;
import com.angelmaker.japaneseflashcards.database.WordActivityViewModel;

public class NewWord extends AppCompatActivity {
    //Activity elements
    private EditText etEnglishWord;
    private EditText etJapaneseWord;
    private EditText etHintEtoJ;
    private EditText etHintJtoE;
    private Button btnAddWord;

    WordActivityViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        viewModel = new WordActivityViewModel(getApplication());

        linkActivityElements();
        setClickables();
    }

    //Find and set the activity elements
    private void linkActivityElements()
    {
        etEnglishWord = findViewById(R.id.etEnglishWord);
        etJapaneseWord = findViewById(R.id.etJapaneseWord);
        etHintEtoJ = findViewById(R.id.etHintEtoJ);
        etHintJtoE = findViewById(R.id.etHintJtoE);
        btnAddWord = findViewById(R.id.btnAddWord);
    }

    //Set clickable actions
    private void setClickables()
    {
        btnAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word newWord = new Word();
                newWord.setEnglish(etEnglishWord.getText().toString());
                newWord.setJapanese(etJapaneseWord.getText().toString());
                newWord.setHintEtoJ(etHintEtoJ.getText().toString());
                newWord.setHintJtoE(etHintJtoE.getText().toString());

                viewModel.addWord(newWord);
                finish();
            }
        });
    }
}
