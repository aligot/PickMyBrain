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

public class add_word extends AppCompatActivity {
    private EditText wordEditText, traductionEditText;
    private Button btnAddWord;
    DatabaseReference databaseReference;
    String previousLang, myImage;
    int wordIndex;
    int cnt =1;
    List<String> wordListFetched,tradListFetched,listCounter;
    FloatingActionButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        wordEditText = findViewById(R.id.editTextB);
        traductionEditText = findViewById(R.id.editTextC);
        btnAddWord = findViewById(R.id.btnModify);
        btnBack = findViewById(R.id.buttonBack3);

        myImage= getIntent().getStringExtra("myImage");
        previousLang = getIntent().getStringExtra("correspondingLang");
        wordIndex = getIntent().getIntExtra("indexW",0);

        this.setTitle("Add a new word");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Languages");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordListFetched = getIntent().getStringArrayListExtra("WordList");
                tradListFetched = getIntent().getStringArrayListExtra("TradList");
                listCounter = getIntent().getStringArrayListExtra("CounterList");
                nextIntent();
            }
        });
        btnAddWord.setOnClickListener(view -> {
            String wordName = wordEditText.getText().toString().trim();
            String tradName = traductionEditText.getText().toString().trim();
            Boolean exists = false;

            if(wordIndex == 0){ // cas ou il ya encore 0 mots dans la langue qu'on cree
                if(wordName.equals("") && tradName.equals("")){
                    Toast.makeText(add_word.this, "Missing the translation and/or word",
                        Toast.LENGTH_SHORT).show();
                }
                else{
                    //int compteur = 0;
                    Word word = new Word(wordName, tradName, 5);
                    newAndAdd(word);
                    databaseReference.child(previousLang).child(String.valueOf(cnt)).setValue(word);
                    //databaseReference.child(previousLang).child(String.valueOf(0)).child("ImageUri").setValue(myImage);
                    setHintAndText(wordEditText, traductionEditText);
                    wordIndex += 1;
                    System.out.println("dans add_word if1 wordindex vaut: " + wordIndex);
                    Toast toast1 = Toast.makeText(add_word.this, "New word added", Toast.LENGTH_SHORT);
                    toast1.show();
                    nextIntent();
                }
            }else{ //cas ou il y a deja des mots
                if(wordName.equals("") || tradName.equals("")){
                    Toast.makeText(add_word.this, "Missing the translation and/or word",
                            Toast.LENGTH_SHORT).show();
                }else{
                    wordListFetched = getIntent().getStringArrayListExtra("WordList");
                    tradListFetched = getIntent().getStringArrayListExtra("TradList");
                    for (int i = 0; i<wordListFetched.size();i++){
                        if(wordListFetched.get(i).equals(wordName) && tradListFetched.get(i).equals(tradName)){
                            Toast.makeText(add_word.this, "This word already exists", Toast.LENGTH_SHORT).show();
                            exists = true;
                            break;
                        }
                    }
                    if(!exists) {
                        int compteur = 0;
                        Word word = new Word(wordName, tradName, compteur);
                        listCounter = getIntent().getStringArrayListExtra("CounterList");
                        add(word);
                        System.out.println("dans add_word if2 wordindex vaut: " + wordIndex);
                        Toast.makeText(add_word.this, "New word added", Toast.LENGTH_SHORT).show();
                        databaseReference.child(previousLang).child(String.valueOf(wordIndex + 2)).setValue(word);
                        wordIndex += 1;
                        setHintAndText(wordEditText, traductionEditText);
                        nextIntent();
                    }
                }
            }
        });
    }
    public void setHintAndText(EditText et1, EditText et2){
        et1.setText("");
        et1.setHint("Add a new word...");
        et2.setText("");
        et2.setHint("Corresponding traduction...");
    }

    public void newAndAdd(Word word){
        wordListFetched = new ArrayList<>();
        tradListFetched = new ArrayList<>();
        listCounter = new ArrayList<>();
        wordListFetched.add(word.getMot());
        tradListFetched.add(word.getTraduction());
        listCounter.add(String.valueOf(word.getCompteur()));
    }

    public void add(Word word){
        wordListFetched.add(word.getMot());
        tradListFetched.add(word.getTraduction());
        listCounter.add(String.valueOf(word.getCompteur()));
    }

    public void nextIntent(){
        Intent intent = new Intent(add_word.this, itemfetch.class);
        intent.putExtra("myImage", myImage);
        intent.putStringArrayListExtra("listWord", (ArrayList<String>) wordListFetched);
        intent.putStringArrayListExtra("listTrad", (ArrayList<String>) tradListFetched);
        intent.putStringArrayListExtra("listCounter", (ArrayList<String>) listCounter);
        intent.putExtra("indexW", wordIndex);
        intent.putExtra("myLanguage", previousLang);
        startActivity(intent);
    }

}