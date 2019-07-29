package com.angelmaker.japaneseflashcards.supportFiles;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.angelmaker.japaneseflashcards.R;
import com.angelmaker.japaneseflashcards.activities.FlashCards;
import com.angelmaker.japaneseflashcards.activities.WordSelector;
import com.angelmaker.japaneseflashcards.database.OngoingWord;
import com.angelmaker.japaneseflashcards.database.Word;
import com.angelmaker.japaneseflashcards.database.WordActivityViewModel;
import com.angelmaker.japaneseflashcards.supportFiles.FlashCardSwipeAdapter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Observer;

/**
 * A simple {@link Fragment} subclass.
 */

@SuppressLint("ValidFragment")
public class FlashCardFragment extends Fragment {
    TextView tvPosition;
    TextView tvWordToTranslate;
    TextView tvHint;
    TextView tvShowAnswer;
    EditText etAnswer;
    Button btnSubmit;
    ImageView ivResult;
    ImageButton ibIncorrect;
    ImageButton ibCorrect;
    ImageButton ibTimeToggle;
    Button btnPlusTen;
    Button btnPlusHundred;
    FlashCardSwipeAdapter parent;
    private WordActivityViewModel viewModel;

    int position;
    int wordsListSize;

    private ObservableBoolean paused;
    private Observer pausedChanged = new Observer() {
        @Override
        public void update(java.util.Observable observable, Object paused) {
            if(((Boolean) paused)){
                ibTimeToggle.setImageResource(android.R.drawable.ic_media_play);
            }
            else{
                ibTimeToggle.setImageResource(android.R.drawable.ic_media_pause);
            }
        }
    };


    @SuppressLint("ValidFragment")
    public FlashCardFragment(WordActivityViewModel newViewModel, FlashCardSwipeAdapter newParent) {
        viewModel = newViewModel;
        parent = newParent;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        checkCorrectList();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flash_card, container, false);

        // Link all activity elements
        linkActivityElements(view);

        // Get passed in Word
        final Bundle bundle = getArguments();

        paused = (ObservableBoolean) bundle.getSerializable("pauseState");
        paused.addObserver(pausedChanged);

        position = bundle.getInt("position");
        wordsListSize = bundle.getInt("wordsListSize");
        tvPosition.setText("Word: " + (position+1) + " / " + wordsListSize);

        // Setup view elements
        tvWordToTranslate.setText(bundle.getString("testWord"));
        setClickables(bundle);

        // Update pause button to reflect pause status
        if(paused.getState()){ibTimeToggle.setImageResource(android.R.drawable.ic_media_play);}
        else{ibTimeToggle.setImageResource(android.R.drawable.ic_media_pause);}


        // If already answered, display previous result
        if (FlashCardSwipeAdapter.scoreList[position] == 1){setCorrect();}
        if (FlashCardSwipeAdapter.scoreList[position] == -1){setIncorrect();}

        return view;
    }

    private void checkCorrectList(){
        for(int score : FlashCardSwipeAdapter.scoreList){
            if(score != 0){ // 0 indicates unanswered so we are looking to see if even one question has been scored
                return;     //Scorelist in not empty and does not need to be pulled from DB
            }
        }

        new syncDBCorrectList().execute();  //Scorelist may have been cleared
    }


    private class syncDBCorrectList extends AsyncTask<Void, Void, ArrayList<OngoingWord>> {
        @Override
        protected ArrayList<OngoingWord> doInBackground(final Void... voids) {
            return (ArrayList) viewModel.getOngoingWords();
        }

        @Override
        protected void onPostExecute(ArrayList<OngoingWord> ongoingWords) {
            //For each ongoing word set the image to correct or incorrect based on the recorded score
            for(OngoingWord ongoingWord : ongoingWords) {
                Log.d("zzzFCF", "getID is: " + ongoingWord.getId());
                Log.d("zzzFCF", "The word is : " + ongoingWord.getEnglish());
                FlashCardSwipeAdapter.scoreList[ongoingWord.getId()-1] = ongoingWord.getIsCorrect();
                if(position == (ongoingWord.getId()-1) && ongoingWord.getIsCorrect() == 1){ivResult.setImageResource(R.mipmap.check_circle);}
                else if(position == (ongoingWord.getId()-1) && ongoingWord.getIsCorrect() == -1){ivResult.setImageResource(R.mipmap.cross_circle);}
            }
        }
    }


    private void setCorrect() {
        ivResult.setImageResource(R.mipmap.check_circle);
        FlashCardSwipeAdapter.scoreList[position] = 1;
        updateDBCorrect(1);
    }

    private void setIncorrect() {
        ivResult.setImageResource(R.mipmap.cross_circle);
        FlashCardSwipeAdapter.scoreList[position] = -1;
        updateDBCorrect(-1);
    }

    private void updateDBCorrect(int result){
        viewModel.updateOngoingWord(position+1, result);
    }


    //Find and set the activity elements
    private void linkActivityElements(View view)
    {
        tvPosition = view.findViewById(R.id.tvPosition);
        tvWordToTranslate = view.findViewById(R.id.tvWordToTranslate);
        tvHint = view.findViewById(R.id.tvHint);
        tvShowAnswer = view.findViewById(R.id.tvShowAnswer);
        etAnswer = view.findViewById(R.id.etAnswer);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        ivResult = view.findViewById(R.id.ivResult);
        ibIncorrect = view.findViewById(R.id.ibIncorrect);
        ibCorrect = view.findViewById(R.id.ibCorrect);
        ibTimeToggle = view.findViewById(R.id.ibTimeToggle);
        btnPlusTen = view.findViewById(R.id.btnPlusTen);
        btnPlusHundred = view.findViewById(R.id.btnPlusHundred);
    }


    //Set clickable actions
    private void setClickables(final Bundle bundle)
    {
        final String actualAnswer = bundle.getString("answerWord");

        tvHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvHint.setText(bundle.getString("hint"));
            }
        });

        tvShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvShowAnswer.getText().toString().equals(getString(R.string.flash_card_text_view_show_answer))) {
                    tvShowAnswer.setText(bundle.getString("answerWord"));
                    tvShowAnswer.setTextColor(Color.parseColor("#ff000000"));
                }
                else {
                    tvShowAnswer.setText(R.string.flash_card_text_view_show_answer);
                    tvShowAnswer.setTextColor(Color.parseColor("#ff606060"));

                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredAnswer = etAnswer.getText().toString();
                if (enteredAnswer.equalsIgnoreCase(actualAnswer)){
                    setCorrect();
                }
                else{
                    setIncorrect();
                }
            }
        });

        ibIncorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIncorrect();
            }
        });
        ibCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCorrect();
            }
        });

        ibTimeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paused.getState()){paused.setState(false);}
                else{paused.setState(true);}
            }
        });

        btnPlusTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("zzzFCF", "plus 10");
                parent.advancePosition(10);
            }
        });

        btnPlusHundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("zzzFCF", "plus 100");
                parent.advancePosition(100);
            }
        });
    }
}
