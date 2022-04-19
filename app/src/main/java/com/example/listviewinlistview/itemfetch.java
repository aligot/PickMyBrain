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
        //btnRetour = findViewById(R.id.button2);
        btnRevisions = findViewById(R.id.button3);
        searchViewWord = findViewById(R.id.searchItem);
        word = new Word();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Languages");

        listDate = getIntent().getStringArrayListExtra("listDate");
        wordListFetched = getIntent().getStringArrayListExtra("listWord");
        tradListFetched = getIntent().getStringArrayListExtra("listTrad");
        listCounter = getIntent().getStringArrayListExtra("listCounter");
        previousLang = getIntent().getStringExtra("language");
        System.out.println(wordListFetched);
        System.out.println(tradListFetched);
        System.out.println(listCounter);
        System.out.println(listDate);
        myLanguageClicked = getIntent().getStringExtra("myLanguageClicked");
        myImage = getIntent().getStringExtra("myImage");
        wordIndex = getIntent().getIntExtra("indexW", 0);

        System.out.println("dans itemfetch wordindex vaut: " + wordIndex);

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
                Toast.makeText(this, "1st add words for revision",Toast.LENGTH_SHORT).show();
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
                System.out.println("DANS ITEMFETCH LA POSITION EST: "+position);
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
        //intentA.putExtra("myImage", myImage);
        intent.putStringArrayListExtra("listWord", (ArrayList<String>) wordListFetched);
        intent.putStringArrayListExtra("listTrad", (ArrayList<String>) tradListFetched);
        intent.putStringArrayListExtra("listCounter", (ArrayList<String>) listCounter);
        intent.putStringArrayListExtra("listDate", (ArrayList<String>) listDate);
        intent.putExtra("indexW", wordIndex);

        System.out.println(wordListFetched);
        System.out.println(tradListFetched);
        System.out.println(listCounter);
        System.out.println(listDate);

    }
    /*
            case R.id.Delete:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                //wordListFetched.remove(position);
                //tradListFetched.remove(position);
                //listCounter.remove(position);
                for (int i = 0; i<wordListFetched.size(); i++){
                    System.out.println("word in wordlistfetched: " + wordListFetched.get(i));
                    System.out.println("corresponding translation: " + tradListFetched.get(i));
                }
                Intent intentDel = new Intent(itemfetch.this, itemfetch.class);

                System.out.println("position: " + position);

                if (previousLang == null){
                    System.out.println("databaseReference.child(myLanguageClicked).child(String.valueOf(position)).getKey(): "+databaseReference.child(myLanguageClicked).child(String.valueOf(position)).getKey());
                    Query query = databaseReference.orderByChild("mot").equalTo(wordListFetched.get(position));
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(databaseReference.child(myLanguageClicked).child(String.valueOf(position)).getKey().equals(String.valueOf(position))){
                                    for (int i=0; i<wordListFetched.size();i++){
                                        databaseReference.child(myLanguageClicked).child(String.valueOf(i)).toString();
                                    }
                                    databaseReference.child(myLanguageClicked).child(String.valueOf(position)).getRef().removeValue();
                                }
                            }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    //if(databaseReference.child(myLanguageClicked).child(String.valueOf(position)).toString().equals())


                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.child(myLanguageClicked).get(String.valueOf(position)).getRef().;
                            //System.out.println("snapshot.child(myLanguageClicked): "+snapshot.child(myLanguageClicked).child(String.valueOf(position)).getRef());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else{

                    //databaseReference.child(previousLang).child(String.valueOf(position)).removeValue();
                    //intentDel.putExtra("myLanguage",previousLang);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            System.out.println("222222 snapshot.child(myLanguageClicked): "+snapshot.child(myLanguageClicked).child(String.valueOf(position)));

                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                System.out.println(dataSnapshot.toString());
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });}


                //intentDel.putStringArrayListExtra("listWord", (ArrayList<String>) wordListFetched);
                //intentDel.putStringArrayListExtra("listTrad", (ArrayList<String>) tradListFetched);
                //intentDel.putStringArrayListExtra("listCounter", (ArrayList<String>) listCounter);
                //startActivity(intentDel);
                return true;
*/



}