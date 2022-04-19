package com.example.listviewinlistview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class add_word extends AppCompatActivity {
    private EditText wordEditText, traductionEditText;
    private Button btnAddWord;
    DatabaseReference databaseReference;
    String previousLang, myImage;
    private CircleImageView languageImage;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;

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
        languageImage = (CircleImageView) findViewById(R.id.languageImage);
        //myImage= getIntent().getStringExtra("myImage");
        previousLang = getIntent().getStringExtra("correspondingLang");
        wordIndex = getIntent().getIntExtra("indexW",0);
        listCounter = getIntent().getStringArrayListExtra("listCounter");
        listDate = getIntent().getStringArrayListExtra("listDate");

        wordListFetched = getIntent().getStringArrayListExtra("listWord");
        tradListFetched = getIntent().getStringArrayListExtra("listTrad");
        setHintAndText(wordEditText, traductionEditText);
        System.out.println(wordListFetched);
        System.out.println(tradListFetched);
        System.out.println(listCounter);
        System.out.println(listDate);
        this.setTitle("Add a new word");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Languages");
        btnAddWord.setOnClickListener(view -> {
            boolean exists = false;
            String wordName = wordEditText.getText().toString().trim();
            String tradName = traductionEditText.getText().toString().trim();
            Calendar calendar = Calendar.getInstance();
            String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
            System.out.println("wordIndex: "+wordIndex);
            if(wordIndex == 0){ // cas ou il ya encore 0 mots dans la langue qu'on cree
                if(wordName.equals("") && tradName.equals("")){ //
                    Toast.makeText(add_word.this, "Missing the translation and word",
                        Toast.LENGTH_SHORT).show();
                }
                else if(wordName.equals("") || tradName.equals("")){ //
                    Toast toast4 = Toast.makeText(add_word.this, "Missing the translation and word",
                            Toast.LENGTH_SHORT);
                    toast4.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast4.show();
                }
                else{
                    Word word = new Word(wordName, tradName, currentDate,5);
                    System.out.println("LAAAAA");
                    System.out.println(wordListFetched);
                    System.out.println(tradListFetched);
                    System.out.println(listCounter);
                    System.out.println(listDate);
                    newAndAdd(word);
                    System.out.println(wordListFetched);
                    System.out.println(tradListFetched);
                    System.out.println(listCounter);
                    System.out.println(listDate);
                    System.out.println("1 : cnt vaut: "+ cnt);
                    databaseReference.child(previousLang).child(String.valueOf(cnt)).setValue(word);
                    //databaseReference.child(previousLang).child(String.valueOf(0)).child("ImageUri").setValue(myImage);
                    setHintAndText(wordEditText, traductionEditText);
                    wordIndex += 1;
                    System.out.println("dans add_word if1 wordindex vaut: " + wordIndex);
                    Toast toast1 = Toast.makeText(add_word.this, "New word added", Toast.LENGTH_SHORT);
                    toast1.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast1.show();
                    nextIntent();
                }
            }else{ //cas ou il y a deja des mots
                /*
                for (int i=0; i<wordIndex;i++){
                    if(wordName.equals(wordListFetched.get(i))||tradName.equals(tradListFetched.get(i))){
                        Toast.makeText(add_word.this, "The word or the translation already " +
                                        "exists. If that is a synonym, please add a hint in " +
                                        "parenthesis in order to recognize it.", Toast.LENGTH_SHORT).show();
                    }
                }*/
                System.out.println("DANS LE ELSE");
                if(wordName.equals("") || tradName.equals("")){
                    Toast toast3 = Toast.makeText(add_word.this, "Missing the translation or word",
                            Toast.LENGTH_SHORT);
                    toast3.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast3.show();
                }
                else if(wordName.equals("") && tradName.equals("")){
                    Toast toast2 = Toast.makeText(add_word.this, "Missing the translation and word",
                            Toast.LENGTH_SHORT);
                    toast2.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast2.show();

                }
                else {
                    System.out.println("DANS LE ELSE: CAS OU LE MOT EST ACCEPTE");
                    //listDate = getIntent().getStringArrayListExtra("listDate");
                    for (int i = 0; i < wordListFetched.size(); i++) { //on regarde si on a trad ou mot en double
                        if (wordListFetched.get(i).equals(wordName) || tradListFetched.get(i).equals(tradName)) {
                            Toast toast = Toast.makeText(add_word.this, "The word or " +
                                    "the translation already " +
                                    "exists. If that is a synonym, please add a hint in " +
                                    "parenthesis in order to better recognize it.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                            CountDownTimer toastCountDown;
                            int toastDurationInMilliSeconds = 15000;
                            toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
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
                    System.out.println("DANS LE ELSE: IL N'Y A PAS DE MOT EN DOUBLE");

                    if (!exists) {
                        int compteur = 5;
                        Word word = new Word(wordName, tradName, currentDate, compteur);
                        System.out.println("ouiiiiiiiii");
                        System.out.println(wordListFetched);
                        System.out.println(tradListFetched);
                        System.out.println(listCounter);
                        System.out.println(listDate);
                        add(word);
                        System.out.println(wordListFetched);
                        System.out.println(tradListFetched);
                        System.out.println(listCounter);
                        System.out.println(listDate);
                        System.out.println("dans add_word if2 wordindex vaut: " + wordIndex);
                        Toast.makeText(add_word.this, "New word added", Toast.LENGTH_SHORT).show();
                        System.out.println("2 : cnt vaut: " + cnt);
                        databaseReference.child(previousLang).child(String.valueOf(wordListFetched.size())).setValue(word);
                        System.out.println("DANS ADDWORD LA WORDINDEX EST: "+wordIndex);

                        wordIndex += 1;
                        setHintAndText(wordEditText, traductionEditText);
                        System.out.println("on va nextIntent la");
                        nextIntent();
                        //}
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
        et1.setHint(previousLang);
        et2.setText("");
        et2.setHint("Corresponding translation...");
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


        //intent.putExtra("myImage", myImage);
        intent.putExtra("language", previousLang);
        intent.putStringArrayListExtra("listWord", (ArrayList<String>) wordListFetched);
        intent.putStringArrayListExtra("listTrad", (ArrayList<String>) tradListFetched);
        intent.putStringArrayListExtra("listCounter", (ArrayList<String>) listCounter);
        intent.putStringArrayListExtra("listDate", (ArrayList<String>) listDate);
        System.out.println(wordListFetched);
        System.out.println(tradListFetched);
        System.out.println(listCounter);
        System.out.println(listDate);
        intent.putExtra("indexW", wordIndex);


        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==PICK_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                languageImage.setImageBitmap(bitmap);

            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}