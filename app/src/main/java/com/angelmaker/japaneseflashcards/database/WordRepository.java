package com.angelmaker.japaneseflashcards.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class WordRepository {

    private WordDao wordDao;

    public WordRepository(Application application){
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        wordDao = db.wordDao();
    }


    //Add word to DB
    public void addWord(Word word)
    {
        new addWordAsyncTask(wordDao).execute(word);
    }

    private static class addWordAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao asyncTaskDao;

        addWordAsyncTask(WordDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            asyncTaskDao.addWord(words[0]);
            return null;
        }
    }


    //Remove word from DB
    public void removeWord(Word word)
    {
        new removeWordAsyncTask(wordDao).execute(word);
    }

    private static class removeWordAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao asyncTaskDao;

        removeWordAsyncTask(WordDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            asyncTaskDao.removeWord(words[0]);
            return null;
        }
    }


    //Remove word from DB
    public void updateWord(Word word)
    {
        new updateWordAsyncTask(wordDao).execute(word);
    }

    private static class updateWordAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao asyncTaskDao;

        updateWordAsyncTask(WordDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            asyncTaskDao.updateWord(words[0]);
            return null;
        }
    }

    public List<Word> getAllWords(){return wordDao.getAllWords();}
    public LiveData<List<Word>> getAllWordsLive(){return wordDao.getAllWordsLive();}
}
