package com.angelmaker.japaneseflashcards.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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
    public LiveData<List<Word>> getSearchedWordsLive(String subString){return repository.getSearchedWordsLive("%"+subString+"%");}


    //TODO - Change add words to accept array and add all words in the array
    public void addOngoingWords(OngoingWord ongoingWord){repository.addOngoingWords(ongoingWord);}
    public void dropTable(){repository.dropTable();}
    public void updateOngoingWord(int id, int result){repository.updateOngoingWord(id, result);}
    public List<OngoingWord> getOngoingWords(){return repository.getOngoingWords();}
    public int getOngoingWordsSize(){return repository.getOngoingWordsSize();}
}
