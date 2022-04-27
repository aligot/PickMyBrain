package com.example.listviewinlistview;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class add_theme extends AppCompatActivity {
    private EditText editText;
    private Button button;
    List<String> wordListFetched,tradListFetched,listCounter,languageList,dateList;
    int wordIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_theme);
        this.setTitle("Add a new Topic");
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.btn);

        dateList = getIntent().getStringArrayListExtra("listDate");
        wordListFetched = getIntent().getStringArrayListExtra("listWord");
        tradListFetched = getIntent().getStringArrayListExtra("listTrad");
        listCounter = getIntent().getStringArrayListExtra("listCounter");
        languageList = getIntent().getStringArrayListExtra("listLang");
        wordIndex = getIntent().getIntExtra("indexW",0);

        button.setOnClickListener(view -> {
            Boolean exists = false;
            String languageName = editText.getText().toString();
            if(languageName.equals("")) //verifie qu'on ecrit bien qqch
                Toast.makeText(add_theme.this, "No Topic is written", Toast.LENGTH_SHORT).show();
            else{//verifie que si qqch est ecrit, qu'il n'existe pas deja auparavant
                for (int i=0; i<languageList.size(); i++){
                    if(languageList.get(i).equals(languageName)){
                        Toast.makeText(this, "Topic already exists", Toast.LENGTH_SHORT).show();
                        exists = true;
                        break;
                    }
                }
                if(!exists){ //si la langue n'existait pas, on vient lui ajouter des mots
                    Intent intent = new Intent(add_theme.this, itemfetch.class);
                    intent.putExtra("language", languageName);
                    editText.setHint("Add a new Topic...");
                    intent.putStringArrayListExtra("listWord", (ArrayList<String>) wordListFetched); //to pass an Arraylist from one activity to another
                    intent.putStringArrayListExtra("listTrad", (ArrayList<String>) tradListFetched);
                    intent.putStringArrayListExtra("listCounter", (ArrayList<String>) listCounter);
                    intent.putStringArrayListExtra("listDate", (ArrayList<String>) dateList);
                    intent.putExtra("indexW", wordIndex);
                    startActivity(intent);
                    Toast.makeText(add_theme.this, "You can add some Elements", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intentBack = new Intent(add_theme.this, HomeActivity.class);
        startActivity(intentBack);
    }

}