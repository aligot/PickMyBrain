package com.example.pickmybrain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class add_word extends AppCompatActivity {
    private EditText wordEditText, traductionEditText;
    private Button btnAddWord;
    DatabaseReference databaseReference;
    String previousLang;
    int wordIndex;
    int cnt =1;
    List<String> wordListFetched,tradListFetched,listCounter,listDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        wordEditText = findViewById(R.id.editTextB);
        traductionEditText = findViewById(R.id.editTextC);
        btnAddWord = findViewById(R.id.btnModify);
        previousLang = getIntent().getStringExtra("correspondingLang");
        wordIndex = getIntent().getIntExtra("indexW",0);
        listCounter = getIntent().getStringArrayListExtra("listCounter");
        listDate = getIntent().getStringArrayListExtra("listDate");

        wordListFetched = getIntent().getStringArrayListExtra("listWord");
        tradListFetched = getIntent().getStringArrayListExtra("listTrad");
        setHintAndText(wordEditText, traductionEditText);
        this.setTitle(previousLang);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Languages");
        btnAddWord.setOnClickListener(view -> {
            boolean exists = false;
            String wordName = wordEditText.getText().toString().trim();
            String tradName = traductionEditText.getText().toString().trim();
            Calendar calendar = Calendar.getInstance();
            String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
            if(wordIndex == 0){ // cas ou il ya encore 0 elements dans le theme qu'on cree
                if(wordName.equals("") && tradName.equals("")){ //
                    Toast.makeText(add_word.this, "Missing the key and the value",
                        Toast.LENGTH_SHORT).show();
                }
                else if(wordName.equals("") || tradName.equals("")){ //
                    Toast toast4 = Toast.makeText(add_word.this, "Missing the key or the value",
                            Toast.LENGTH_SHORT);
                    toast4.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast4.show();
                }
                else{
                    Word word = new Word(wordName, tradName, currentDate,5);
                    newAndAdd(word);
                    databaseReference.child(previousLang).child(String.valueOf(cnt)).setValue(word);
                    setHintAndText(wordEditText, traductionEditText);
                    wordIndex += 1;
                    Toast toast1 = Toast.makeText(add_word.this, "New element added", Toast.LENGTH_SHORT);
                    toast1.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast1.show();
                    nextIntent();
                }
            }else{ // cas ou il ya deja des elements dans la
                if(wordName.equals("") || tradName.equals("")){
                    Toast toast3 = Toast.makeText(add_word.this, "Missing the key or the value",
                            Toast.LENGTH_SHORT);
                    toast3.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast3.show();
                }
                else if(wordName.equals("") && tradName.equals("")){
                    Toast toast2 = Toast.makeText(add_word.this, "Missing the key and the value",
                            Toast.LENGTH_SHORT);
                    toast2.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast2.show();

                }
                else {
                    for (int i = 0; i < wordListFetched.size(); i++) { //on regarde si on a trad ou mot en double
                        if (wordListFetched.get(i).equals(wordName) || tradListFetched.get(i).equals(tradName)) {
                            Toast toast = Toast.makeText(add_word.this, "The key or " +
                                    "the value already " +
                                    "exists. If that is a synonym, please add a hint in " +
                                    "parenthesis in order to better recognize it.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                            CountDownTimer toastCountDown;
                            int toastDurationInMilliSeconds = 15000;
                            toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000) {
                                @Override
                                public void onTick(long l) {
                                    toast.show();
                                }

                                @Override
                                public void onFinish() {
                                    toast.cancel();
                                }
                            };
                            toast.show();
                            toastCountDown.start();
                            exists = true; //S'IL N'Y AVAIT PAS DE MOT EN DOUBLE, MTN IL Y EN A
                            break;
                        }
                    }
                    if (!exists) {
                        int compteur = 5;
                        Word word = new Word(wordName, tradName, currentDate, compteur);
                        add(word);
                        Toast.makeText(add_word.this, "New entry added", Toast.LENGTH_SHORT).show();
                        databaseReference.child(previousLang).child(String.valueOf(wordListFetched.size())).setValue(word);
                        wordIndex += 1;
                        setHintAndText(wordEditText, traductionEditText);
                        nextIntent();
                    }
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        wordListFetched = getIntent().getStringArrayListExtra("listWord");
        tradListFetched = getIntent().getStringArrayListExtra("listTrad");
        listCounter = getIntent().getStringArrayListExtra("listCounter");
        listDate=getIntent().getStringArrayListExtra("listDate");
        nextIntent();
    }
    public void setHintAndText(EditText et1, EditText et2){
        et1.setText("");
        et1.setHint("Key");//et1.setHint(previousLang);
        et2.setText("");
        et2.setHint("Corresponding value...");
    }

    public void newAndAdd(Word word){
        wordListFetched = new ArrayList<>();
        tradListFetched = new ArrayList<>();
        listCounter = new ArrayList<>();
        listDate = new ArrayList<>();
        wordListFetched.add(word.getMot());
        tradListFetched.add(word.getTraduction());
        listCounter.add(String.valueOf(word.getCompteur()));
        listDate.add(word.getDateAdded());
    }

    public void add(Word word){
        wordListFetched.add(word.getMot());
        tradListFetched.add(word.getTraduction());
        listCounter.add(String.valueOf(word.getCompteur()));
        listDate.add(word.getDateAdded());
    }

    public void nextIntent(){
        Intent intent = new Intent(add_word.this, itemfetch.class);
        intent.putExtra("language", previousLang);
        intent.putStringArrayListExtra("listWord", (ArrayList<String>) wordListFetched);
        intent.putStringArrayListExtra("listTrad", (ArrayList<String>) tradListFetched);
        intent.putStringArrayListExtra("listCounter", (ArrayList<String>) listCounter);
        intent.putStringArrayListExtra("listDate", (ArrayList<String>) listDate);
        intent.putExtra("indexW", wordIndex);
        startActivity(intent);
    }

}