package com.example.listviewinlistview;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class inspect extends AppCompatActivity {
    List<String> wordListFetched,tradListFetched,listCounter,dateListFetched;
    String previousLang, myImage;
    int position, scoreToDisplay;
    TextView textViewTrad, textViewWord, textViewScore, textViewDate;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect);
        textViewDate = findViewById(R.id.inspectDate);
        textViewScore = findViewById(R.id.inspectScore);
        textViewTrad = findViewById(R.id.inspectTrad);
        textViewWord = findViewById(R.id.inspectWord);
        previousLang = getIntent().getStringExtra("correspondingLang");
        myImage= getIntent().getStringExtra("myImage");
        wordListFetched = getIntent().getStringArrayListExtra("listWord");
        dateListFetched = getIntent().getStringArrayListExtra("listDate");
        tradListFetched = getIntent().getStringArrayListExtra("listTrad");
        listCounter = getIntent().getStringArrayListExtra("listCounter");
        position = getIntent().getIntExtra("indexW",0);
        textViewWord.setText(previousLang+": "+ wordListFetched.get(position));
        textViewTrad.setText("Translation: "+tradListFetched.get(position));
        textViewDate.setText("Word added the: \n"+dateListFetched.get(position));
        if(Integer.parseInt(listCounter.get(position))==5){
            scoreToDisplay = 1;
            textViewScore.setText("Knowledge level: \n"+scoreToDisplay +"/5");
        }else if(Integer.parseInt(listCounter.get(position))==4){
            scoreToDisplay = 2;
            textViewScore.setText("Knowledge level: \n"+scoreToDisplay +"/5");
        }
        else if(Integer.parseInt(listCounter.get(position))==3){
            scoreToDisplay = 3;
            textViewScore.setText("Knowledge level: \n"+scoreToDisplay +"/5");
        }else if(Integer.parseInt(listCounter.get(position))==2){
            scoreToDisplay = 4;
            textViewScore.setText("Knowledge level: \n"+scoreToDisplay +"/5");
        }else if(Integer.parseInt(listCounter.get(position))==1){
            scoreToDisplay = 5;
            textViewScore.setText("Knowledge level: \n"+scoreToDisplay +"/5");
        }
    }
}