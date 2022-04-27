package com.example.listviewinlistview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class itemfetch extends AppCompatActivity {

    List<String> wordListFetched,tradListFetched,listCounter,listDate;
    TextView textView;
    ArrayList<Word> arrayOfWords = new ArrayList<>();
    private FloatingActionButton btnAddWord,btnRevisions;
    DatabaseReference databaseReference;
    String previousLang, myLanguageClicked;
    SearchView searchViewWord;
    Word word;
    int wordIndex;
    WordsAdapter adapter;
    String myImage;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemfetch);

        textView = findViewById(R.id.textView);
        btnAddWord = findViewById(R.id.add);
        btnRevisions = findViewById(R.id.button3);
        searchViewWord = findViewById(R.id.searchItem);
        word = new Word();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Languages");

        listDate = getIntent().getStringArrayListExtra("listDate");
        wordListFetched = getIntent().getStringArrayListExtra("listWord");
        tradListFetched = getIntent().getStringArrayListExtra("listTrad");
        listCounter = getIntent().getStringArrayListExtra("listCounter");
        previousLang = getIntent().getStringExtra("language");
        myLanguageClicked = getIntent().getStringExtra("myLanguageClicked");
        myImage = getIntent().getStringExtra("myImage");
        wordIndex = getIntent().getIntExtra("indexW", 0);

        if (wordListFetched != null) {
            for (int i = 0; i < wordListFetched.size(); i++) {
                Word word = new Word(wordListFetched.get(i), tradListFetched.get(i), listDate.get(i),Integer.parseInt(listCounter.get(i)));
                arrayOfWords.add(i, word);
                // Construct the data source
                // Create the adapter to convert the array to views
                adapter = new WordsAdapter(this, arrayOfWords);
                ListView itemListView = findViewById(R.id.itemListView);
                itemListView.setAdapter(adapter);
                registerForContextMenu(itemListView);
            }
        } else {
            arrayOfWords = new ArrayList<>();
            WordsAdapter adapter = new WordsAdapter(this, arrayOfWords);
        }
        if (previousLang == null) {
            this.setTitle(myLanguageClicked);
        } else
            this.setTitle(previousLang);

        btnAddWord.setOnClickListener(view -> {
            Intent intentA = new Intent(itemfetch.this, add_word.class);
            passToIntent(intentA);
            startActivity(intentA);
        });

        btnRevisions.setOnClickListener(view -> {
            if(wordListFetched.size()!=0){
                MediaPlayer ring= MediaPlayer.create(getApplicationContext(),R.raw.revision);
                ring.start();
                Intent intentC = new Intent(itemfetch.this, Revision.class);
                if (previousLang == null) {
                    intentC.putExtra("language", myLanguageClicked);
                } else intentC.putExtra("language", previousLang);
                intentC.putExtra("myImage", myImage);
                intentC.putStringArrayListExtra("wordListRevision", (ArrayList<String>) wordListFetched);
                intentC.putStringArrayListExtra("tradListRevision", (ArrayList<String>) tradListFetched);
                intentC.putStringArrayListExtra("listCounter", (ArrayList<String>) listCounter);
                intentC.putStringArrayListExtra("listDate", (ArrayList<String>) listDate);
                intentC.putExtra("indexW", wordIndex);
                startActivity(intentC);
            }else{
                Toast.makeText(this, "1st add some entries for Topic Quizz",Toast.LENGTH_SHORT).show();
            }

        });

        searchViewWord.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intentBack = new Intent(itemfetch.this, HomeActivity.class);
        intentBack.putExtra("myImage", myImage);
        startActivity(intentBack);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.example_menu,menu);
        // Get the list item position
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        position = info.position;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.Modify:
                Intent intentD = new Intent(itemfetch.this, ModifyWord.class);
                System.out.println(previousLang);
                System.out.println(myLanguageClicked);
                passToIntent(intentD);
                intentD.putExtra("indexW", position);
                startActivity(intentD);
                return true;
            case R.id.Inspect:
                Intent intentInspect = new Intent(itemfetch.this, inspect.class);
                passToIntent(intentInspect);
                intentInspect.putExtra("indexW", position);
                startActivity(intentInspect);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void passToIntent(Intent intent) {
        if (previousLang == null) {
            intent.putExtra("correspondingLang", myLanguageClicked);
        } else intent.putExtra("correspondingLang", previousLang);
        intent.putStringArrayListExtra("listWord", (ArrayList<String>) wordListFetched);
        intent.putStringArrayListExtra("listTrad", (ArrayList<String>) tradListFetched);
        intent.putStringArrayListExtra("listCounter", (ArrayList<String>) listCounter);
        intent.putStringArrayListExtra("listDate", (ArrayList<String>) listDate);
        intent.putExtra("indexW", wordIndex);
    }
}