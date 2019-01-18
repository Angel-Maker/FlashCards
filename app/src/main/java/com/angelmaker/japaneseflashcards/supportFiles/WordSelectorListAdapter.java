package com.angelmaker.japaneseflashcards.supportFiles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.angelmaker.japaneseflashcards.R;
import com.angelmaker.japaneseflashcards.activities.WordSelector;
import com.angelmaker.japaneseflashcards.database.Word;

import java.util.ArrayList;
import java.util.List;

public class WordSelectorListAdapter extends RecyclerView.Adapter<WordSelectorListAdapter.ActivityViewHolder>{

    ArrayList<Word> eWords;
    ArrayList<Word> jWords;

    //Constructor that determines context to inflate in
    private final LayoutInflater inflater;

    public WordSelectorListAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }

    class ActivityViewHolder extends RecyclerView.ViewHolder
    {
        private final Button btnEnglish;
        private final Button btnJapanese;

        public ActivityViewHolder(View itemView) {
            super(itemView);

            btnEnglish = itemView.findViewById(R.id.btnEnglish);
            btnJapanese = itemView.findViewById(R.id.btnJapanese);
        }
    }

    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recycler_view_word_selector, parent, false);
        return new ActivityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ActivityViewHolder holder, int position) {
        if (eWords != null && jWords != null) {
            final Word eWord = eWords.get(position);
            final Word jWord = jWords.get(position);

            holder.btnEnglish.setText(eWord.getEnglish());
            holder.btnJapanese.setText(jWord.getEnglish());     // Japanese words have been flipped so the J word is in E

            // If word is already in list, highlight it
            if(WordSelector.wordsEtoJ.contains(eWord)){holder.btnEnglish.setBackgroundColor(0xFFF8E831);}
            else{holder.btnEnglish.setBackgroundColor(0xFFd4d4d4);}
            if(WordSelector.wordsJtoE.contains(jWord)){holder.btnJapanese.setBackgroundColor(0xFFF8E831);}
            else{holder.btnJapanese.setBackgroundColor(0xFFd4d4d4);}

            //If word is clicked, add/remove it and change colour
            holder.btnEnglish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!WordSelector.wordsEtoJ.contains(eWord))
                    {
                        WordSelector.wordsEtoJ.add(eWord);
                        holder.btnEnglish.setBackgroundColor(0xFFF8E831);
                    }
                    else{
                        WordSelector.wordsEtoJ.remove(eWord);
                        holder.btnEnglish.setBackgroundColor(0xFFd4d4d4);
                    }
                }
            });

            holder.btnJapanese.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!WordSelector.wordsJtoE.contains(jWord))
                    {
                        WordSelector.wordsJtoE.add(jWord);
                        holder.btnJapanese.setBackgroundColor(0xFFF8E831);
                    }
                    else{
                        WordSelector.wordsJtoE.remove(jWord);
                        holder.btnJapanese.setBackgroundColor(0xFFd4d4d4);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if (eWords != null && jWords != null)
            return eWords.size();
        else return 0;
    }

    public void setWordsList(ArrayList<Word> newEWords, ArrayList<Word> newJWords)
    {
        eWords = newEWords;
        jWords = newJWords;
        notifyDataSetChanged();
    }
}
