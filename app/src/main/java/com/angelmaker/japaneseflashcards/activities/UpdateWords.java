package com.angelmaker.japaneseflashcards.activities;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.angelmaker.japaneseflashcards.R;
import com.angelmaker.japaneseflashcards.database.Word;
import com.angelmaker.japaneseflashcards.database.WordActivityViewModel;
import com.angelmaker.japaneseflashcards.supportFiles.UpdateWordsListAdapter;

import java.util.List;

public class UpdateWords extends AppCompatActivity {
    //Activity elements
    private Button btnAddWord;

    WordActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_words);

        viewModel = new WordActivityViewModel(getApplication());

        linkActivityElements();
        setClickables();
        setRecyclerView();

        //new getWords().execute();
    }


    //Find and set the activity elements
    private void linkActivityElements()
    {
        btnAddWord = findViewById(R.id.btnAddWord);
    }

    //Set clickable actions
    private void setClickables()
    {
        btnAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newWord = new Intent(view.getContext(), NewWord.class);
                startActivity(newWord);
            }
        });
    }

    private void setRecyclerView()
    {
        //RecyclerView Setup
        RecyclerView recyclerView = findViewById(R.id.rvWordsList);
        final UpdateWordsListAdapter adapter = new UpdateWordsListAdapter(this, getApplication(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Add dividers between cells
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        //Link view model to database
        viewModel.getAllWordsLive().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable final List<Word> words) {
                //Executed whenever the observed object changes
                adapter.setWordsList(words);   // Update the cached copy of the words in the adapter.
            }
        });
    }

    public void editWord(Word word){
        Intent editWord = new Intent(this, EditWord.class);
        editWord.putExtra("Word", word);
        startActivity(editWord);
    }

/*
    private class getWords extends AsyncTask<Void, Void, LiveData<Word>> {
        @Override
        protected LiveData<Word> doInBackground(final Void... voids) {

            LiveData<Word> words = viewModel.getAllWordsLive();

            return words;
        }

        @Override
        protected void onPostExecute(LiveData<Word> newWords) {
            setRecyclerView(newWords);
        }
    }*/
}
