package com.angelmaker.japaneseflashcards.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.angelmaker.japaneseflashcards.R;
import com.angelmaker.japaneseflashcards.database.Word;
import com.angelmaker.japaneseflashcards.database.WordActivityViewModel;

public class EditWord extends AppCompatActivity {
    //Activity elements
    private TextView tvPreviousE;
    private TextView tvPreviousJ;
    private EditText etEnglishWord;
    private EditText etJapaneseWord;
    private EditText etHintEtoJ;
    private EditText etHintJtoE;
    private Button btnAddWord;

    WordActivityViewModel viewModel;

    Word word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        viewModel = new WordActivityViewModel(getApplication());
        word = (Word) getIntent().getSerializableExtra("Word");

        linkActivityElements();
        setClickables();

        setTextFields();
    }


    private void setTextFields(){
        tvPreviousE.setText("Previous spelling: " + word.getEnglish());
        etEnglishWord.setText(word.getEnglish());

        tvPreviousJ.setText("Previous spelling: " + word.getJapanese());
        etJapaneseWord.setText(word.getJapanese());

        etHintEtoJ.setText(word.getHintEtoJ());
        etHintJtoE.setText(word.getHintJtoE());
    }


    //Find and set the activity elements
    private void linkActivityElements()
    {
        tvPreviousE = findViewById(R.id.tvPreviousE);
        tvPreviousJ = findViewById(R.id.tvPreviousJ);
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
                newWord.setId(word.getId());
                newWord.setEnglish(etEnglishWord.getText().toString());
                newWord.setJapanese(etJapaneseWord.getText().toString());
                newWord.setHintEtoJ(etHintEtoJ.getText().toString());
                newWord.setHintJtoE(etHintJtoE.getText().toString());;

                viewModel.updateWord(newWord);
                finish();
            }
        });
    }
}
