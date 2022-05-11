package com.example.pickmybrain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ModifyWord extends AppCompatActivity {
    private EditText wordEditText, traductionEditText;
    private Button btnModify;
    DatabaseReference databaseReference;
    String previousLang, myImage, wordName,tradName;
    List<String> wordListFetched,tradListFetched,listCounter,listDate;
    int position,compteur;
    Boolean exists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_word);
        wordEditText = findViewById(R.id.editTextB);
        traductionEditText = findViewById(R.id.editTextC);
        btnModify = findViewById(R.id.btnModify);

        previousLang = getIntent().getStringExtra("correspondingLang");
        myImage= getIntent().getStringExtra("myImage");
        wordListFetched = getIntent().getStringArrayListExtra("listWord");
        tradListFetched = getIntent().getStringArrayListExtra("listTrad");
        listCounter = getIntent().getStringArrayListExtra("listCounter");
        listDate = getIntent().getStringArrayListExtra("listDate");
        position = getIntent().getIntExtra("indexW",0);

        wordEditText.setText(wordListFetched.get(position));
        traductionEditText.setText(tradListFetched.get(position));
        this.setTitle("Modify the element: "+  wordListFetched.get(position));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Languages");
        btnModify.setOnClickListener(view -> {
            exists = false;
            wordName = wordEditText.getText().toString();
            tradName = traductionEditText.getText().toString();
            if(wordName.equals("") || tradName.equals("")){
                Toast.makeText(ModifyWord.this, "Missing the key and/or the value",
                        Toast.LENGTH_SHORT).show();
            }else{
                for (int i = 0; i<wordListFetched.size();i++){
                    if(wordListFetched.get(i).equals(wordName) && tradListFetched.get(i).equals(tradName)){
                        Toast.makeText(ModifyWord.this, "This element already exists", Toast.LENGTH_SHORT).show();
                        exists = true;
                        break;
                    }else if(wordListFetched.get(i).equals(wordName) || tradListFetched.get(i).equals(tradName)){
                        Toast.makeText(ModifyWord.this, "The key or the value already exists", Toast.LENGTH_SHORT).show();
                        exists = true;
                        break;
                    }
                }
                if(!exists) {
                    compteur = 5; //par defaut si on change le mot, on remet son compteur a 5
                    Word word = new Word(wordName, tradName, listDate.get(position), compteur);
                    modify(wordName,tradName,compteur);
                    Toast.makeText(ModifyWord.this, "Element has been modified", Toast.LENGTH_SHORT).show();
                    databaseReference.child(previousLang).child(String.valueOf(position+1)).setValue(word);
                    nextIntent();
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        listCounter = getIntent().getStringArrayListExtra("listCounter");
        nextIntent();
    }

    public void nextIntent(){
        Intent intent = new Intent(ModifyWord.this, itemfetch.class);
        intent.putExtra("myImage", myImage);
        intent.putStringArrayListExtra("listWord", (ArrayList<String>) wordListFetched);
        intent.putStringArrayListExtra("listTrad", (ArrayList<String>) tradListFetched);
        intent.putStringArrayListExtra("listCounter", (ArrayList<String>) listCounter);
        intent.putStringArrayListExtra("listDate", (ArrayList<String>) listDate);
        intent.putExtra("indexW",position);
        intent.putExtra("language", previousLang);
        startActivity(intent);
    }
    public void modify(String word, String trad, int count){
        wordListFetched.set(position, word);
        tradListFetched.set(position, trad);
        listCounter.set(position, String.valueOf(count));
    }
}