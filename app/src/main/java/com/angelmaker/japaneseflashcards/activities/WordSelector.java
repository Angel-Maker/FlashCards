package com.angelmaker.japaneseflashcards.activities;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.angelmaker.japaneseflashcards.R;
import com.angelmaker.japaneseflashcards.database.OngoingWord;
import com.angelmaker.japaneseflashcards.database.Word;
import com.angelmaker.japaneseflashcards.database.WordActivityViewModel;
import com.angelmaker.japaneseflashcards.supportFiles.LessonListAdapter;
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
    private Button btnSearch;
    private EditText etSearch;

    SlidingPaneLayout pane;

    private Button btnAddLesson;
    private EditText etAddLesson;
    RecyclerView rvLessonLists;

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

    WordSelectorListAdapter adapterWS;

    private void setMainRecyclerView()
    {
        //RecyclerView Setup
        RecyclerView recyclerView = findViewById(R.id.rvWordsList);
        adapterWS = new WordSelectorListAdapter(this);
        recyclerView.setAdapter(adapterWS);
        adapterWS.setWordsList(allWords, allWordsFlipped);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Add dividers between cells
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    LessonListAdapter adapterL;

    private void setLessonListRecyclerView()
    {
        //RecyclerView Setup
        RecyclerView recyclerView = findViewById(R.id.rvLessonLists);
        adapterL = new LessonListAdapter(this, getApplication(), this);
        recyclerView.setAdapter(adapterL);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Add dividers between cells
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        //Link view model to database
        viewModel.getAllWordsLive().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable final List<Word> lessons) {
                //Executed whenever the observed object changes
                adapterL.setLessonList(lessons);   // Update the cached copy of the words in the adapter.
            }
        });
    }

    //Find and set the activity elements
    private void linkActivityElements()
    {
        tvEnglish = findViewById(R.id.tvEnglish);
        tvJapanese = findViewById(R.id.tvJapanese);
        btnStart = findViewById(R.id.btnStart);
        tglbtnWordMix = findViewById(R.id.tglbtnWordMix);

        btnSearch = findViewById(R.id.btnSearch);
        etSearch = findViewById(R.id.etSearch);

        pane = (SlidingPaneLayout) findViewById(R.id.SlidingPanel);
        pane.setPanelSlideListener(new PaneListener());
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

                adapterWS.setWordsList(allWords, allWordsFlipped);
            }
        });

        tvJapanese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wordsJtoE.size() != allWordsFlipped.size()) { wordsJtoE = new ArrayList<>(allWordsFlipped); }
                else{ wordsJtoE = new ArrayList<>(); }

                adapterWS.setWordsList(allWords, allWordsFlipped);
            }
        });

        final LifecycleOwner wordSelector = this;
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getSearchedWordsLive(etSearch.getText().toString()).observe(wordSelector, new Observer<List<Word>>() {
                    @Override
                    public void onChanged(@Nullable final List<Word> words) {
                        //Executed whenever the observed object changes
                        allWords = (ArrayList) words;
                        allWordsFlipped = flipList((ArrayList)words);
                        adapterWS.setWordsList(allWords, allWordsFlipped);   // Update the cached copy of the words in the adapter.
                    }
                });
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
            setMainRecyclerView();
            setLessonListRecyclerView();
        }
    }


    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        // Result code 1 is finish
        if (resultCode == 1){
            viewModel.dropTable();
        }

        // Result code 2 is retry wrong answers
        if (resultCode == 2) {
            viewModel.dropTable();
            ArrayList<Word> wrongWords = (ArrayList<Word>) data.getSerializableExtra("wrongWords");

            if (wrongWords.size() != 0) {
                Intent FlashCards = new Intent(this, FlashCards.class);

                addAllWords(wrongWords);
                FlashCards.putExtra("wordList", wrongWords);
                startActivityForResult(FlashCards, 1);
            }
        }
    }



    private class PaneListener implements SlidingPaneLayout.PanelSlideListener {

        @Override
        public void onPanelClosed(View view) {
            System.out.println("Panel closed");
        }

        @Override
        public void onPanelOpened(View view) {
            System.out.println("Panel opened");
        }

        @Override
        public void onPanelSlide(View view, float arg1) {
            System.out.println("Panel sliding");
        }
    }
}
