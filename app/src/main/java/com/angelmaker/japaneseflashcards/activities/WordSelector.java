package com.angelmaker.japaneseflashcards.activities;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.angelmaker.japaneseflashcards.R;
import com.angelmaker.japaneseflashcards.database.OngoingWord;
import com.angelmaker.japaneseflashcards.database.Word;
import com.angelmaker.japaneseflashcards.database.WordActivityViewModel;
import com.angelmaker.japaneseflashcards.supportFiles.UpdateWordsListAdapter;
import com.angelmaker.japaneseflashcards.supportFiles.WordSelectorListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordSelector extends AppCompatActivity {
    //Activity elements
    private Button btnStart;
    private ToggleButton tglbtnWordMix;
    private TextView tvEnglish;
    private TextView tvJapanese;

    private WordActivityViewModel viewModel;
    public ArrayList<Word> allWords;
    public ArrayList<Word> allWordsFlipped;
    public static ArrayList<Word> wordsEtoJ;
    public static ArrayList<Word> wordsJtoE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_selector);

        viewModel = new WordActivityViewModel(getApplication());

        wordsEtoJ = new ArrayList<>();
        wordsJtoE = new ArrayList<>();

        new getWords().execute();

        linkActivityElements();
        setClickables();
    }

    WordSelectorListAdapter adapter;

    private void setRecyclerView()
    {
        //RecyclerView Setup
        RecyclerView recyclerView = findViewById(R.id.rvWordsList);
        adapter = new WordSelectorListAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setWordsList(allWords, allWordsFlipped);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Add dividers between cells
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    //Find and set the activity elements
    private void linkActivityElements()
    {
        tvEnglish = findViewById(R.id.tvEnglish);
        tvJapanese = findViewById(R.id.tvJapanese);
        btnStart = findViewById(R.id.btnStart);
        tglbtnWordMix = findViewById(R.id.tglbtnWordMix);
     }

    //Set clickable actions
    private void setClickables()
    {
        btnStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // As long as there are any words selected
                if (wordsEtoJ.size() != 0 || wordsJtoE.size() != 0)
                {
                    ArrayList<Word> wordList = new ArrayList<>();

                    Intent FlashCards = new Intent(view.getContext(), FlashCards.class);
                    //wordsJtoE = flipList(wordsJtoE);

                    // Even mix of words - Add words, then mix
                    if(tglbtnWordMix.isChecked())
                    {
                        Log.i("zzzWS", "mixed");
                        wordList.addAll(wordsEtoJ);
                        wordList.addAll(wordsJtoE);
                        Collections.shuffle(wordList);
                    }
                    // All English words, then Japanese words
                    else{
                        Collections.shuffle(wordsEtoJ);
                        Collections.shuffle(wordsJtoE);
                        wordList.addAll(wordsEtoJ);
                        wordList.addAll(wordsJtoE);
                    }

                    viewModel.dropTable();
                    FlashCards.putExtra("wordList", wordList);
                    addAllWords(wordList);
                    startActivityForResult(FlashCards, 1);
                }

                else
                {
                    Toast.makeText(view.getContext(), "Word list is empty!", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wordsEtoJ.size() != allWords.size()) { wordsEtoJ = new ArrayList<>(allWords); }
                else{ wordsEtoJ = new ArrayList<>(); }

                adapter.setWordsList(allWords, allWordsFlipped);
            }
        });

        tvJapanese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wordsJtoE.size() != allWordsFlipped.size()) { wordsJtoE = new ArrayList<>(allWordsFlipped); }
                else{ wordsJtoE = new ArrayList<>(); }

                adapter.setWordsList(allWords, allWordsFlipped);
            }
        });
    }

    private void addAllWords(ArrayList<Word> wordList){
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


    // Flips E and J for a list of words
    private static ArrayList<Word> flipList(ArrayList<Word> wordList)
    {
        ArrayList<Word> newWordList = new ArrayList<>();
        for( int i = 0 ; i < wordList.size() ; i++)
        {
            newWordList.add(flipWord(wordList.get(i)));
        }
        return newWordList;
    }

    // Flips a single word
    private static Word flipWord(Word word)
    {
        Word flippedWord = new Word();
        flippedWord.setId(word.getId());

        // Swap position of english and japanese words
        String englishWord = word.getEnglish();
        flippedWord.setEnglish(word.getJapanese());
        flippedWord.setJapanese(englishWord);

        // Swap position of hints
        String englishHint = word.getHintEtoJ();
        flippedWord.setHintEtoJ(word.getHintJtoE());
        flippedWord.setHintJtoE(englishHint);

        return flippedWord;
    }


    private class getWords extends AsyncTask<Void, Void, ArrayList<Word>> {
        @Override
        protected ArrayList<Word> doInBackground(final Void... voids) {

            return (ArrayList) viewModel.getAllWords();
        }

        @Override
        protected void onPostExecute(ArrayList<Word> words) {
            allWords = words;
            allWordsFlipped = flipList(words);
            setRecyclerView();
        }
    }


    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        // Result code 1 is finish
        if (resultCode == 1){
            viewModel.dropTable();
        }

        // Result code 2 is retry wrong answers
        if (resultCode == 2) {
            ArrayList<Word> wrongWords = (ArrayList<Word>) data.getSerializableExtra("wrongWords");

            if (wrongWords.size() != 0) {
                Intent FlashCards = new Intent(this, FlashCards.class);

                FlashCards.putExtra("wordList", wrongWords);
                startActivityForResult(FlashCards, 1);
            }
        }
    }
}
