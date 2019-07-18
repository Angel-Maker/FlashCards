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


    //Update word from DB
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
    public LiveData<List<Word>> getSearchedWordsLive(String subString){return wordDao.getSearchedWordsLive(subString);}



//////////////////////////////////////////////////////////////////////////


    //Add ongoing word to DB
    public void addOngoingWords(OngoingWord ongoingWord)
    {
        new addOngoingWordsAsyncTask(wordDao).execute(ongoingWord);
    }

    private static class addOngoingWordsAsyncTask extends AsyncTask<OngoingWord, Void, Void> {

        private WordDao asyncTaskDao;

        addOngoingWordsAsyncTask(WordDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(OngoingWord... ongoingWord) {
            asyncTaskDao.addOngoingWord(ongoingWord[0]);
            return null;
        }
    }



    //Remove word from DB
    public void dropTable()
    {
        new removeOngoingAsyncTask(wordDao).execute();
    }

    private static class removeOngoingAsyncTask extends AsyncTask<Void, Void, Void> {

        private WordDao asyncTaskDao;

        removeOngoingAsyncTask(WordDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.dropTable();
            return null;
        }
    }

    //Update word from DB
    public void updateOngoingWord(int id, int result)
    {
        new updateOngoingWordAsyncTask(wordDao).execute(id, result);
    }

    private static class updateOngoingWordAsyncTask extends AsyncTask<Integer, Void, Void> {

        private WordDao asyncTaskDao;

        updateOngoingWordAsyncTask(WordDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... ints) {
            asyncTaskDao.updateOngoingWord(ints[0], ints[1]);
            return null;
        }
    }


    //Get a specific OngoingWord
    public List<OngoingWord> getOngoingWords() {return wordDao.getOngoingWords();}
    public int getOngoingWordsSize() {return wordDao.getOngoingWordsSize();}


    //////////////////////////////////////////////////////////////////////////
    //Add word to DB
    public void addLessonWord(LessonWord lessonWord) { new addLessonWordAsyncTask(wordDao).execute(lessonWord); }

    private static class addLessonWordAsyncTask extends AsyncTask<LessonWord, Void, Void> {

        private WordDao asyncTaskDao;

        addLessonWordAsyncTask(WordDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(LessonWord... lessonWords) {
            asyncTaskDao.addLessonWord(lessonWords[0]);
            return null;
        }
    }

    public LiveData<List<String>> getLessonsLive(){return wordDao.getLessonsLive();}
    public List<LessonWord> getLessonWords(String lessonListName){return wordDao.getLessonWords(lessonListName);}


    //Remove word from DB
    public void removeLesson(String lessonListName) { new removeLessonAsyncTask(wordDao).execute(lessonListName); }

    private static class removeLessonAsyncTask extends AsyncTask<String, Void, Void> {

        private WordDao asyncTaskDao;

        removeLessonAsyncTask(WordDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... lessonListName) {
            asyncTaskDao.removeLesson(lessonListName[0]);
            return null;
        }
    }
}
