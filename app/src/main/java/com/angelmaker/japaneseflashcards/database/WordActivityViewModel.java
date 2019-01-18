package com.angelmaker.japaneseflashcards.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class WordActivityViewModel extends AndroidViewModel{

    private WordRepository repository;

    public WordActivityViewModel(Application application) {
        super(application);
        repository = new WordRepository(application);
    }

    public void addWord(Word word){repository.addWord(word);}
    public void removeWord(Word word){repository.removeWord(word);}
    public void updateWord(Word word){repository.updateWord(word);}
    public List<Word> getAllWords(){return repository.getAllWords();}
    public LiveData<List<Word>> getAllWordsLive(){return repository.getAllWordsLive();}

}
