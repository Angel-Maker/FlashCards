package com.angelmaker.japaneseflashcards.supportFiles;

import android.app.Activity;
import android.content.Intent;
import android.database.Observable;
import android.icu.util.TimeUnit;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.angelmaker.japaneseflashcards.activities.FlashCards;
import com.angelmaker.japaneseflashcards.database.Word;
import com.angelmaker.japaneseflashcards.database.WordActivityViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Observer;

public class FlashCardSwipeAdapter extends FragmentStatePagerAdapter{
    ArrayList<Word> wordsList;
    public static int scoreList[];     //-1: Incorrect      0: Untested       1: Correct
    ArrayList<Long> pauseTimes;
    Boolean fromRight = false;
    Date pauseStartTime;
    Boolean showAnswers = false;
    Activity flashCardsActivity;
    Date startTime;
    Date endTime;
    private WordActivityViewModel viewModel;


    private ObservableBoolean paused;
    private Observer pausedChanged = new Observer() {
        @Override
        public void update(java.util.Observable observable, Object paused) {
            if(((Boolean) paused)){
                pauseStartTime = Calendar.getInstance().getTime();
            }
            else{
                Date pauseEndTime = Calendar.getInstance().getTime();
                Long pauseTimeTotal = pauseEndTime.getTime() - pauseStartTime.getTime();
                pauseTimes.add(pauseTimeTotal);
            }
        }
    };


    public FlashCardSwipeAdapter(FragmentManager fm, Activity parent) {
        super(fm);
        viewModel = new WordActivityViewModel(parent.getApplication());
        flashCardsActivity = parent;
        startTime = Calendar.getInstance().getTime();
        pauseTimes = new ArrayList<>();
        paused = new ObservableBoolean();
        paused.setState(false);
        paused.addObserver(pausedChanged);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle bundle = new Bundle();

        // Restart timer if returning to quiz from end
        if(position <= (wordsList.size()-1))
        {
            if(fromRight)
            {
                paused.setState(false);
                fromRight = false;
            }
        }

        // If page is a test question page
        if(position < wordsList.size())
        {
            fragment = new FlashCardFragment(viewModel);

            Word word = wordsList.get(position);
            bundle.putInt("position", position);
            bundle.putInt("wordsListSize", wordsList.size());
            bundle.putString("testWord", word.getEnglish());

            if (word.getHintEtoJ().equals("")) {
                bundle.putString("hint", "No hint entered");
            } else {
                bundle.putString("hint", word.getHintEtoJ());
            }

            bundle.putString("answerWord", word.getJapanese());
            bundle.putSerializable("pauseState", paused);
        }

        // If page is buffer page
        else if (position == wordsList.size()){
            fragment = new BufferCardFragment();
        }


        // Page must be results page
        else{
            // If pause was active, toggle to update pause time list
            if(paused.getState()) {
                paused.setState(false);
                paused.setState(true);
            }
            // If pause was not active, start auto-pause
            else{
                paused.setState(true);
                fromRight = true;
            }


            endTime = Calendar.getInstance().getTime();
            fragment = new ResultsCardFragment(flashCardsActivity);

            bundle.putInt("correct", getCorrect());
            bundle.putInt("total", scoreList.length);

            Long rawTimeTotal = endTime.getTime() - startTime.getTime();
            Long timeTotal = removePauses(rawTimeTotal);
            String timeTotalString = milliToUTCString(timeTotal);
            String timePerString = milliToUTCString(timeTotal/wordsList.size());

            bundle.putString("timeTotal", timeTotalString);
            bundle.putString("timePer", timePerString);

            ArrayList<Word> wrongWords = getWrongWords();
            bundle.putSerializable("wrongWords", wrongWords);
        }

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        if (wordsList != null)
            return (wordsList.size()+2);        // Size = size of list of test words plus buffer page (+1) plus result page (+1)
        else
            return 0;
    }


    // Tally correct answers
    private int getCorrect(){
        int correct = 0;
        for ( int i = 0 ; i < scoreList.length ; i++ )
        {
            if(scoreList[i] == 1)
            {
                correct++;
            }
        }

        return correct;
    }

    public void setWords(ArrayList<Word> newWords){
        wordsList = newWords;
        notifyDataSetChanged();
    }

    public void initializeScoreList(int size){
        scoreList = new int[size];
    }


    public ArrayList<Word> getWrongWords(){
        ArrayList<Word> wrongWords = new ArrayList<>();
        for (int i = 0 ; i < wordsList.size() ; i++){
            if (scoreList[i] != 1)
                wrongWords.add(wordsList.get(i));
        }

        return wrongWords;
    }



    // Subtracts time while paused
    private long removePauses(long milliseconds){
        for(long time : pauseTimes){
            milliseconds = milliseconds - time;
        }
        return milliseconds;
    }


    // Converts milliseconds to a displayable string (should be optimized to use mod instead of while loops)
    public static String milliToUTCString(long milliseconds)
    {
        String time;

        // If time is greater than one minute, use form 00:00:00
        if(milliseconds > 60000){
            int hours = 0;
            int minutes = 0;
            int seconds = 0;

            String hoursString;
            String minutesString;
            String secondsString;

            while(milliseconds >= 3600000){
                milliseconds = milliseconds - 3600000;
                hours++;
            }

            while(milliseconds >= 60000){
                milliseconds = milliseconds - 60000;
                minutes++;
            }

            while(milliseconds >= 1000){
                milliseconds = milliseconds - 1000;
                seconds++;
            }

            if (hours < 10) hoursString = "0" + Integer.toString(hours);
            else hoursString = Integer.toString(hours);

            if (minutes < 10) minutesString = "0" + Integer.toString(minutes);
            else minutesString = Integer.toString(minutes);

            if (seconds < 10) secondsString = "0" + Integer.toString(seconds);
            else secondsString = Integer.toString(seconds);

            time = hoursString + ":" + minutesString + ":" + secondsString;
        }

        // If time is less than one minute, use 00.00
        else{

            int seconds = 0;

            String centisecondsString;

            while(milliseconds >= 1000){
                milliseconds = milliseconds - 1000;
                seconds++;
            }

            if (milliseconds < 100) centisecondsString = "00" + Long.toString(milliseconds);
            else if (milliseconds < 10) centisecondsString = "0" + Long.toString(milliseconds);
            else centisecondsString = Long.toString(milliseconds);

            time = Integer.toString(seconds) + "." + centisecondsString;
        }


        return time;
    }
}
