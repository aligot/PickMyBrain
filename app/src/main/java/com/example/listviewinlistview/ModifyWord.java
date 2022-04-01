package com.example.listviewinlistview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ModifyWord extends AppCompatActivity {
    private EditText wordEditText, traductionEditText;
    private Button btnModify;
    DatabaseReference databaseReference;
    String previousLang, myImage, wordName,tradName;
    int wordIndex;
    List<String> wordListFetched,tradListFetched,listCounter;
    int position,compteur;
    FloatingActionButton btnBack;
    Boolean exists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_word);
        wordEditText = findViewById(R.id.editTextB);
        traductionEditText = findViewById(R.id.editTextC);
        btnModify = findViewById(R.id.btnModify);
        //btnBack = findViewById(R.id.buttonBack3);
        previousLang = getIntent().getStringExtra("correspondingLang");
        myImage= getIntent().getStringExtra("myImage");
        wordListFetched = getIntent().getStringArrayListExtra("WordList");
        tradListFetched = getIntent().getStringArrayListExtra("TradList");
        listCounter = getIntent().getStringArrayListExtra("CounterList");
        position = getIntent().getIntExtra("theWordIndex",0);
        System.out.println("le mot est : "+ wordListFetched.get(position));
        wordEditText.setText(wordListFetched.get(position));
        traductionEditText.setText(tradListFetched.get(position));
        this.setTitle("Modify the word: "+  wordListFetched.get(position));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Languages");

        for (int i=0; i<listCounter.size();i++){
            listCounter.get(i);
        }
        /*
        btnBack.setOnClickListener(view -> {
            listCounter = getIntent().getStringArrayListExtra("CounterList");
            nextIntent();
        });*/

        btnModify.setOnClickListener(view -> {
            exists = false;
            wordName = wordEditText.getText().toString();
            tradName = traductionEditText.getText().toString();
            if(wordName.equals("") || tradName.equals("")){
                Toast.makeText(ModifyWord.this, "Missing the translation and/or word",
                        Toast.LENGTH_SHORT).show();
            }else{
                for (int i = 0; i<wordListFetched.size();i++){
                    if(wordListFetched.get(i).equals(wordName) && tradListFetched.get(i).equals(tradName)){
                        Toast.makeText(ModifyWord.this, "This word already exists", Toast.LENGTH_SHORT).show();
                        exists = true;
                        break;
                    }
                }
                System.out.println("exists= "+ exists);
                if(!exists) {
                    compteur = 5; //par defaut si on change le mot, on remet son compteur a 5
                    Word word = new Word(wordName, tradName, compteur);
                    modify(wordName,tradName,compteur);
                    System.out.println("dans add_word if2 wordindex vaut: " + wordIndex);
                    Toast.makeText(ModifyWord.this, "Word has been modified", Toast.LENGTH_SHORT).show();
                    databaseReference.child(previousLang).child(String.valueOf(position+1)).setValue(word);
                    nextIntent();
                    //setHintAndText(wordEditText, traductionEditText);
                }
            }
        });
    }


    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        listCounter = getIntent().getStringArrayListExtra("CounterList");
        for (int i=0; i<listCounter.size();i++){
            System.out.println(listCounter.get(i));
        }
        nextIntent();
    }

    public void nextIntent(){
        Intent intent = new Intent(ModifyWord.this, itemfetch.class);
        intent.putExtra("myImage", myImage);
        intent.putStringArrayListExtra("listWord", (ArrayList<String>) wordListFetched);
        intent.putStringArrayListExtra("listTrad", (ArrayList<String>) tradListFetched);
        intent.putStringArrayListExtra("listCounter", (ArrayList<String>) listCounter);
        intent.putExtra("indexW", wordIndex);
        intent.putExtra("myLanguage", previousLang);
        startActivity(intent);
    }
    public void modify(String word, String trad, int count){
        wordListFetched.set(position, word);
        tradListFetched.set(position, trad);
        listCounter.set(position, String.valueOf(count));
    }
}