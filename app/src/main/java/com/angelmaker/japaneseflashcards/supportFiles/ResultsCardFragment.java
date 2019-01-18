package com.angelmaker.japaneseflashcards.supportFiles;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.angelmaker.japaneseflashcards.R;
import com.angelmaker.japaneseflashcards.activities.FlashCards;
import com.angelmaker.japaneseflashcards.database.Word;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultsCardFragment extends Fragment {
    TextView tvScoreNumber;
    TextView tvScorePercent;
    TextView tvTotalTime;
    TextView tvAverageTime;
    Button btnRetry;
    Button btnFinish;
    Activity flashCardsActivity;

    public ResultsCardFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ResultsCardFragment(Activity parentActivity) {
        flashCardsActivity = parentActivity;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_results_card, container, false);

        // Get results
        final Bundle bundle = getArguments();

        linkActivityElements(view);
        setClickables(bundle);

        int correct = bundle.getInt("correct");
        int total = bundle.getInt("total");
        float percentFloat = (((float)correct)/((float)total))*100;
        int percent = (int) percentFloat;

        tvScoreNumber.setText(correct + "/" + total);
        tvScorePercent.setText("%" + percent);

        tvTotalTime.setText("Total time taken: " + bundle.getString("timeTotal"));
        tvAverageTime.setText("Average time per word: " + bundle.getString("timePer"));

        return view;
    }


    //Find and set the activity elements
    private void linkActivityElements(View view)
    {
        tvScoreNumber = view.findViewById(R.id.tvScoreNumber);
        tvScorePercent = view.findViewById(R.id.tvScorePercent);
        tvTotalTime = view.findViewById(R.id.tvTotalTime);
        tvAverageTime = view.findViewById(R.id.tvAverageTime);
        btnRetry = view.findViewById(R.id.btnRetry);
        btnFinish = view.findViewById(R.id.btnFinish);
    }


    //Set clickable actions     final Bundle bundle
    private void setClickables(final Bundle bundle)
    {
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashCardsActivity.setResult(1);
                flashCardsActivity.finish();
            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Word> wrongWords = (ArrayList<Word>) bundle.getSerializable("wrongWords");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("wrongWords", wrongWords);
                flashCardsActivity.setResult(2, returnIntent);
                flashCardsActivity.finish();
            }
        });
    }
}
