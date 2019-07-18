package com.angelmaker.japaneseflashcards.supportFiles;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.angelmaker.japaneseflashcards.R;
import com.angelmaker.japaneseflashcards.activities.WordSelector;
import com.angelmaker.japaneseflashcards.database.LessonWord;
import com.angelmaker.japaneseflashcards.database.Word;
import com.angelmaker.japaneseflashcards.database.WordActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class LessonListAdapter extends RecyclerView.Adapter<LessonListAdapter.ActivityViewHolder>{

    public List<String> lessons;
    WordActivityViewModel viewModel;
    WordSelector wordSelector;
    List<LessonWord> lessonWords;

    //Constructor that determines context to inflate in
    private final LayoutInflater inflater;

    public LessonListAdapter(Context context, Application application, WordSelector parent)
    {
        wordSelector = parent;
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
        if (lessons != null)
        {
            final String lessonName = lessons.get(position);

            //Identifying text
            holder.tvWord.setText(lessonName);

            //Delete button
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewModel.removeLesson(lessonName);
                }
            });

            holder.tvWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), lessonName + " loaded", Toast.LENGTH_LONG).show();
                    new getLessonWords().execute(lessonName);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if (lessons != null)
            return lessons.size();
        else return 0;
    }

    public void setLessonList(List<String> newLessons)
    {
        lessons = newLessons;
        notifyDataSetChanged();
    }



    private class getLessonWords extends AsyncTask<String, Void, ArrayList<LessonWord>> {
        @Override
        protected ArrayList<LessonWord> doInBackground(String... lessonName) {

            return (ArrayList) viewModel.getLessonWords(lessonName[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<LessonWord> lessonWords) {
            //Clear existing words
            wordSelector.wordsEtoJ.clear();
            wordSelector.wordsJtoE.clear();

            //Lookup of lessonWord to Word and add to word lists
            for(LessonWord lessonWord : lessonWords)
            {
                for(Word word : wordSelector.allWords)
                {
                    if(lessonWord.getWordID() == word.getId())
                    {
                        switch(lessonWord.getSelectionCode())
                        {
                            case 0:
                                wordSelector.wordsEtoJ.add(word);
                                break;
                            case 1:
                                wordSelector.wordsJtoE.add(wordSelector.flipWord(word));
                                break;
                            case 2:
                                wordSelector.wordsEtoJ.add(word);
                                wordSelector.wordsJtoE.add(wordSelector.flipWord(word));
                                break;
                        }

                        break;
                    }
                }
            }

            wordSelector.refreshWSLA();
        }
    }
}
