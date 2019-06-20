package com.angelmaker.japaneseflashcards.activities;
//TODO - 1h  - Add search feature to Edit Words
//TODO - 2h  - Segment FlashCardFragment so only the middle section moves
//TODO - 1h  - Make timer stop when app closes

//TODO - 8h  - Create arbitrary DBs from word select to create word lists

//TODO - 2h  - Add counter to each word for # of attempts

//TODO - Add metrics
//TODO - Create new list of words at end of attempt filled with incorrect answers
//TODO - Reorganize FlashCardFragment UI
//TODO - Add UI to manage word lists

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.angelmaker.japaneseflashcards.R;
import com.angelmaker.japaneseflashcards.database.OngoingWord;
import com.angelmaker.japaneseflashcards.database.Word;
import com.angelmaker.japaneseflashcards.database.WordActivityViewModel;

import java.util.ArrayList;

public class HomeMenu extends AppCompatActivity {
    //Activity elements
    private Button btnUpdateWords;
    private Button btnTestYourself;
    private Button btnResumeTest;
    private WordActivityViewModel viewModel;
    private boolean resumableTest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        viewModel = new WordActivityViewModel(getApplication());

        new checkIfExistsResumeTest().execute();
        linkActivityElements();
        setClickables();
    }

    //Find and set the activity elements
    private void linkActivityElements()
    {
        btnUpdateWords = findViewById(R.id.btnUpdateWords);
        btnTestYourself = findViewById(R.id.btnTestYourself);
        btnResumeTest = findViewById(R.id.btnResumeTest);
    }

    private void updateActivityElements(){
        if (resumableTest == true){
            btnResumeTest.setVisibility(View.VISIBLE);
        }
        else{
            btnResumeTest.setVisibility(View.INVISIBLE);
        }
    }

    //Set clickable actions
    private void setClickables() {
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
                startActivityForResult(testYourself, 1);
            }
        });

        btnResumeTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resumableTest == true) {
                    new resumeTest().execute();
                }
            }
        });
    }

    private class checkIfExistsResumeTest extends AsyncTask<Void, Void, ArrayList<OngoingWord>> {
        @Override
        protected ArrayList<OngoingWord> doInBackground(final Void... voids) {
            return (ArrayList) viewModel.getOngoingWords();
        }
        @Override
        protected void onPostExecute(ArrayList<OngoingWord> ongoingWords) {
            if (ongoingWords.size() == 0)
                resumableTest = false;
            else
                resumableTest = true;

            updateActivityElements();
        }
    }


    private class resumeTest extends AsyncTask<Void, Void, ArrayList<OngoingWord>> {
        @Override
        protected ArrayList<OngoingWord> doInBackground(final Void... voids) {
            return (ArrayList) viewModel.getOngoingWords();
        }

        @Override
        protected void onPostExecute(ArrayList<OngoingWord> ongoingWords) {
            ArrayList<Word> wordList = new ArrayList<>();
            for(OngoingWord ongoingWord : ongoingWords) {
                Word word = new Word();
                word.setEnglish(ongoingWord.getEnglish());
                word.setJapanese(ongoingWord.getJapanese());
                word.setHintEtoJ(ongoingWord.getHintEtoJ());
                word.setHintJtoE(ongoingWord.getHintJtoE());
                wordList.add(word);
            }

            Intent FlashCards = new Intent(getApplicationContext(), FlashCards.class);
            FlashCards.putExtra("wordList", wordList);
            startActivityForResult(FlashCards, 1);
        }
    }

    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        // Result code 1 is finish

        if (resultCode == 1){
            viewModel.dropTable();
        }


        // Result code 2 is retry wrong answers
        else if (resultCode == 2) {
            viewModel.dropTable();
            ArrayList<Word> wrongWords = (ArrayList<Word>) data.getSerializableExtra("wrongWords");

            if (wrongWords.size() != 0) {
                Intent FlashCards = new Intent(this, FlashCards.class);

                FlashCards.putExtra("wordList", wrongWords);
                addAllWords(wrongWords);
                startActivityForResult(FlashCards, 1);
            }
        }

        new checkIfExistsResumeTest().execute();
    }

    public void addAllWords(ArrayList<Word> wordList){
        for(Word word : wordList)
        {
            OngoingWord ongoingWord = new OngoingWord();
            ongoingWord.setEnglish(word.getEnglish());
            ongoingWord.setJapanese(word.getJapanese());
            ongoingWord.setHintEtoJ(word.getHintEtoJ());
            ongoingWord.setHintJtoE(word.getHintJtoE());
            ongoingWord.setIsCorrect(0);

            viewModel.addOngoingWords(ongoingWord);
        }
        return;
    }
}
