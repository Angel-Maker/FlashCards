package com.angelmaker.japaneseflashcards.supportFiles;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.angelmaker.japaneseflashcards.R;
import com.angelmaker.japaneseflashcards.activities.NewWord;
import com.angelmaker.japaneseflashcards.activities.UpdateWords;
import com.angelmaker.japaneseflashcards.database.Word;
import com.angelmaker.japaneseflashcards.database.WordActivityViewModel;

import java.util.List;

public class UpdateWordsListAdapter extends RecyclerView.Adapter<UpdateWordsListAdapter.ActivityViewHolder>{

    private List<Word> words;
    WordActivityViewModel viewModel;
    UpdateWords updateWords;

    //Constructor that determines context to inflate in
    private final LayoutInflater inflater;

    public UpdateWordsListAdapter(Context context, Application application, UpdateWords parent)
    {
        updateWords = parent;
        inflater = LayoutInflater.from(context);
        viewModel = new WordActivityViewModel(application);
    }

    class ActivityViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView tvWord;
        private final ImageButton btnDelete;

        public ActivityViewHolder(View itemView) {
            super(itemView);

            tvWord = itemView.findViewById(R.id.tvWord);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recycler_view_update_words, parent, false);
        return new ActivityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        if (words != null)
        {
            final Word word = words.get(position);

            //Identifying text
            holder.tvWord.setText(word.getEnglish());

            //Delete button
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.removeWord(word);
                }
            });

            holder.tvWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateWords.editWord(word);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if (words != null)
            return words.size();
        else return 0;
    }

    public void setWordsList(List<Word> newWords)
    {
        words = newWords;
        notifyDataSetChanged();
    }
}
