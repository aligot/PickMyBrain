package com.example.pickmybrain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class WordsAdapter extends ArrayAdapter<Word> implements Filter {
    public WordsAdapter(Context context, ArrayList<Word> words){
        super(context, 0, words);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        // Get the data item for this position
        Word word = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_word, parent, false);
        }
        // Lookup view for data population
        TextView textWord = (TextView) convertView.findViewById(R.id.textWord);
        TextView textTraduction = (TextView) convertView.findViewById(R.id.textTraduction);
        // Populate the data into the template view using the data object
        textWord.setText(word.mot);
        textTraduction.setText(word.traduction);
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public boolean isLoggable(LogRecord logRecord) {
        return false;
    }
}
