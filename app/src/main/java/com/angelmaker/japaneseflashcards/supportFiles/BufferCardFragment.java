package com.angelmaker.japaneseflashcards.supportFiles;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angelmaker.japaneseflashcards.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BufferCardFragment extends Fragment {


    public BufferCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buffer_card, container, false);
    }

}
