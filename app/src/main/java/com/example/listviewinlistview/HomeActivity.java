package com.example.listviewinlistview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ListView listView;
    DatabaseReference databaseReference;
    private List<String> language_list, word_list, trad_list, count_list,date_list;
    private ArrayAdapter<String> arrayAdapter;
    private String languageName;
    Word word;
    ArrayList<Word> itemlistWord = new ArrayList<>();

    ArrayList<String> finalWordList = new ArrayList<>();
    ArrayList<String> finalTradList = new ArrayList<>();
    ArrayList<String> finalCountList = new ArrayList<>();
    ArrayList<String> finalDateList = new ArrayList<>();

    int wordIndex;

    String language_added;
    FloatingActionButton btnAddLang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.setTitle("Languages");
        listView = findViewById(R.id.listView);

        language_list = new ArrayList<>();
        btnAddLang = findViewById(R.id.fab);

        if (itemlistWord.size() !=0){
            itemlistWord.clear();
        }
        word_list = new ArrayList<>();
        trad_list = new ArrayList<>();
        count_list = new ArrayList<>();
        date_list = new ArrayList<>();
        language_added = getIntent().getStringExtra("language");
        if(language_added != null){
            language_list.add(language_added);
        }
        //myImage = getIntent().getStringExtra("myImage");

        databaseReference=FirebaseDatabase.getInstance().getReference("Languages");
        arrayAdapter = new ArrayAdapter<>(this,R.layout.item,R.id.textView, language_list);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()){
                    languageName = d.getKey();
                    language_list.add(languageName);
                }

                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener((adapterView, view, i, l) -> {
                    Intent intent1 = new Intent(HomeActivity.this, itemfetch.class);
                    intent1.putExtra("myLanguageClicked",language_list.get(i));

                    for(String langName:language_list){
                        if(langName.equals(language_list.get(i))){
                            if(itemlistWord.size() != 0){
                                itemlistWord.clear();
                            }
                            for (DataSnapshot d : dataSnapshot.child(langName).getChildren()){
                                if(language_list.size() != 0){
                                    language_list.clear();
                                }
                                word = d.getValue(Word.class);
                                word_list.add(word.getMot());
                                trad_list.add(word.getTraduction());
                                count_list.add("word.getDateAdded() vaut= "+String.valueOf(word.getCompteur()));
                                System.out.println(word.getDateAdded());
                                date_list.add(word.getDateAdded());
                                itemlistWord.add(word);
                                wordIndex = itemlistWord.indexOf(word)+1;
                            }
                            break;
                        }
                    }
                    for (int j = 0; j < count_list.size(); j++){
                        System.out.println(count_list.get(j));
                    }
                    System.out.println("dans item fetch mtn");

                    for(Word word: itemlistWord){
                        finalWordList.add(word.getMot());
                        finalTradList.add(word.getTraduction());
                        finalDateList.add(word.getDateAdded());
                        finalCountList.add(String.valueOf(word.getCompteur()));
                    }
                    passToIntent(intent1);
                    startActivity(intent1);
                    finalWordList.clear();
                    finalTradList.clear();
                    finalCountList.clear();
                    finalDateList.clear();

                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        btnAddLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(HomeActivity.this, add_language.class);
                passToIntent(intent2);
                intent2.putStringArrayListExtra("listLang", (ArrayList<String>) language_list);
                startActivity(intent2);
            }

        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void passToIntent(Intent intent) {
        intent.putStringArrayListExtra("listWord", finalWordList);
        intent.putStringArrayListExtra("listTrad", finalTradList);
        intent.putStringArrayListExtra("listCounter", finalCountList);
        intent.putStringArrayListExtra("listDate", finalDateList);
        intent.putExtra("indexW",wordIndex);
    }
/*
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
                Intent intentD = new Intent(MainActivity.this, ModifyLanguage.class);
                intentD.putStringArrayListExtra("languageList", (ArrayList<String>) language_list);
                intentD.putExtra("language",languageName);
                passToIntent(intentD);
                intentD.putExtra("theWordIndex", position);
                startActivity(intentD);
                return true;
            default:
                return super.onContextItemSelected(item);

        }
    }*/

}