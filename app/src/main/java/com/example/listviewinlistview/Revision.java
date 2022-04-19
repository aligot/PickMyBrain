package com.example.listviewinlistview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Revision extends AppCompatActivity {

    EditText editText;
    TextView textWord;
    Button btnValidate;
    String lang, traduction,mot;
    List<String> wordListForRevision, tradListForRevision, listcounter,listDate;
    DatabaseReference databaseReference;
    int counterLocal, cnt, cntTest, score, upperbound, borneInf, borneSup,wordIndex;
    //cnt cest le compteur associe a chaque mot
    //counterlocal c'est l'index i.e. c'est ce qui permet de savoir a quel mot on est dans la liste
    //cntTest : c'est pour savoir a cmb de mots on est dans le test
    //upperbound c'est la valeur maximale qui peut etre generee aleatoirement
    //lowerbound c'est une borne qui augmente en ajoutant la valeur des compteurs
    //on passe quand meme wordIndex pour pas le perdre entre differentes activites
    boolean revisionFaite = true;
    boolean premierMotATraduire = false;
    boolean bool2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision);
        editText = findViewById(R.id.translation);
        textWord = findViewById(R.id.textWord);
        //btnBack = findViewById(R.id.btnBack);
        btnValidate = findViewById(R.id.btnValidate);


        lang = getIntent().getStringExtra("language");
        wordListForRevision = getIntent().getStringArrayListExtra("wordListRevision");
        tradListForRevision = getIntent().getStringArrayListExtra("tradListRevision");
        listcounter = getIntent().getStringArrayListExtra("listCounter");
        listDate = getIntent().getStringArrayListExtra("listDate");
        wordIndex = getIntent().getIntExtra("indexW",0);
        score = 0;
        Toast toast3 = Toast.makeText(Revision.this,
                "5 words test begins !", Toast.LENGTH_SHORT);
        toast3.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        View toastView1 = toast3.getView();
        toastView1.setBackgroundResource(R.drawable.custom_background_4);
        toast3.show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Languages").child(lang);
        this.setTitle("Revisions for: " + lang);
        Random rd = new Random();
        boolean bool = rd.nextBoolean();//bool généré pour pour l'apparition du 1er mot de la rev

        System.out.println("je fais la PREMIERE REVISION");
        if (revisionFaite) {
            if(bool){ revisionAlgoWord(upperbound);
            setHintTranslation(editText);}//bool=true, on génere un mot et faut trouver trad
            else{ revisionAlgoTrad(upperbound);
            setHintLanguage(editText);} //inversement si bool = false
        }
        btnValidate.setOnClickListener(view -> {
            System.out.println("on rentre dans btnValidate");
            Random rd1 = new Random();
            boolean bool1 = rd1.nextBoolean(); //on genere un bool
            System.out.println("on crée le bool1: "+bool1);
            System.out.println("counterLocal apres btn validate: "+counterLocal);
            cnt = Integer.parseInt(listcounter.get(counterLocal));
            if(bool2 && premierMotATraduire){
                if(counterLocal==wordListForRevision.size()){
                    counterLocal-=1;
                }
                traduction = editText.getText().toString().trim(); //trim() nécessiare?
                System.out.println("counterLocal si bool=true: "+counterLocal);
                System.out.println("donc faut introduire la trad");
                System.out.println("moi je mets: "+traduction);
                premierMotATraduire = true;
                if (traduction.equals(tradListForRevision.get(counterLocal))) {
                    if (cnt > 1 && cnt < 6) {
                        decrement();
                        System.out.println("l'index a été décrémenté car j'ai trouvé la bonne " +
                                "traduction et vaut: " + cnt);
                    }
                    else{ //si la traduction est correcte mais que ton cnt vaut 1: on met juste qu'on a la bonne rep
                        //mais on decremente ap le cnt
                        score += 1;
                        toastCorrect();
                    }
                }
                else { //si traduction donnée est fausse
                    if (cnt > 0 && cnt < 5) { //et que le compteur du mot est entre 0 et 5
                        increment(tradListForRevision.
                                get(wordListForRevision.indexOf(textWord.getText().toString()))); //alors on incrémente
                    } else if (cnt == 5) {
                        MediaPlayer incorrect= MediaPlayer.create(Revision.this,R.raw.incorrect);
                        incorrect.start();
                        Toast toast= Toast.makeText(getApplicationContext(),
                                "Wrong traduction !\nIt was "+tradListForRevision.
                                        get(wordListForRevision.indexOf(textWord.getText().toString())), Toast.LENGTH_SHORT);

                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                        View toastView = toast.getView();
                        toastView.setBackgroundResource(R.drawable.custom_background);
                        toast.show();
                        //System.out.println("Wrong traduction ! It was "+tradListForRevision.
                         //       get(wordListForRevision.indexOf(textWord.getText().toString())+tradListForRevision.size()));
                    }
                }setHintLanguage(editText);
            }
            else if (!bool2 && premierMotATraduire){
                if(counterLocal==wordListForRevision.size()){
                    counterLocal-=1;
                }
                mot = editText.getText().toString().trim();
                System.out.println("counterLocal si bool=false: "+counterLocal);
                System.out.println("donc faut introduire le mot");
                System.out.println("moi je mets: "+mot);
                premierMotATraduire = true;
                System.out.println("cnt: "+cnt);
                if(mot.equals(wordListForRevision.get(counterLocal))){//si le mot donné est bon
                    System.out.println("on a le bon mot !");
                    if (cnt > 1 && cnt < 6) { //si le compteur du mot est compris entre 1 et 6 non inclu
                        //et qu'on a le bon mot: alors on peut le décrémenter.
                        decrement();
                    }
                    else{
                        score += 1;
                        toastCorrect();
                    }
                }
                else { //si mot donné est faux
                    if (cnt > 0 && cnt < 5) { //et que le compteur du mot est entre 0 et 5
                       // System.out.println("wordListForRevision.get(wordListForRevision.indexOf(textWord.getText().toString())): "+wordListForRevision.
                         //       get(wordListForRevision.indexOf(textWord.getText().toString())));
                        System.out.println("wordListForRevision.indexOf(textWord.getText().toString()): "+wordListForRevision.indexOf(textWord.getText().toString()));
                        System.out.println("textWord.getText().toString(): "+textWord.getText().toString());
                        increment(wordListForRevision.
                                get(tradListForRevision.indexOf(textWord.getText().toString()))); //alors on incrémente
                    } else if (cnt == 5) {
                        MediaPlayer incorrect= MediaPlayer.create(Revision.this,R.raw.incorrect);
                        incorrect.start();
                        Toast toast= Toast.makeText(getApplicationContext(),
                                "Wrong word !\nIt was "+wordListForRevision.
                                        get(tradListForRevision.indexOf(textWord.getText().toString())), Toast.LENGTH_SHORT);
                      //  System.out.println("Wrong word ! It was "+wordListForRevision.
                        //        get(tradListForRevision.indexOf(textWord.getText().toString())+tradListForRevision.size()));
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                        View toastView = toast.getView();
                        toastView.setBackgroundResource(R.drawable.custom_background);
                        toast.show();
                    }
                }setHintTranslation(editText);
            }
            else if(bool){ //faut la trad
                // mtn on veut traiter le 1er mot qui est apparu
                traduction = editText.getText().toString().trim(); //trim() nécessiare?
                System.out.println("counterLocal si bool=true: "+counterLocal);
                System.out.println("donc faut introduire la trad");
                System.out.println("moi je mets: "+traduction);
                premierMotATraduire = true;
                if (traduction.equals(tradListForRevision.get(counterLocal))) {
                    if (cnt > 1 && cnt < 6) {
                        decrement();
                    }
                    else{
                        score += 1;
                        toastCorrect();
                    }
                }
                else { //si traduction donnée est fausse
                    if (cnt > 0 && cnt < 5) { //et que le compteur du mot est entre 0 et 5
                        System.out.println(tradListForRevision);
                        System.out.println(textWord.getText().toString());
                        System.out.println(tradListForRevision.indexOf(textWord.getText().toString()));
                        increment(tradListForRevision.
                                get(wordListForRevision.indexOf(textWord.getText().toString()))); //alors on incrémente
                    } else if (cnt == 5) {
                        MediaPlayer incorrect= MediaPlayer.create(Revision.this,R.raw.incorrect);
                        incorrect.start();
                        Toast toast= Toast.makeText(getApplicationContext(),
                                "Wrong traduction !\nIt was "+tradListForRevision.
                                        get(wordListForRevision.indexOf(textWord.getText().toString())), Toast.LENGTH_SHORT);
                        //System.out.println("Wrong traduction ! It was "+tradListForRevision.
                           //     get(tradListForRevision.indexOf(textWord.getText().toString())+tradListForRevision.size()));
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                        View toastView = toast.getView();
                        toastView.setBackgroundResource(R.drawable.custom_background);
                        toast.show();
                    }
                }setHintLanguage(editText);
            } else{ //bool = faux: on veut mot
                mot = editText.getText().toString().trim();
                System.out.println("counterLocal si bool=false: "+counterLocal);
                System.out.println("donc faut introduire le mot");
                System.out.println("moi je mets: "+mot);
                System.out.println("cnt: "+cnt);
                premierMotATraduire = true;
                if(mot.equals(wordListForRevision.get(counterLocal))){ //si le mot donné est bon
                    System.out.println("on a le bon mot !");

                    if (cnt > 1 && cnt < 6) {
                        decrement();
                    }
                    else{
                        score += 1;
                        toastCorrect();
                    }
                }
                else { //si mot donné est faux donnée est fausse
                    if (cnt > 0 && cnt < 5) { //et que le compteur du mot est entre 0 et 5
                        System.out.println(textWord.getText().toString());
                        System.out.println(wordListForRevision);
                        System.out.println(wordListForRevision.indexOf(textWord.getText().toString()));
                        increment(wordListForRevision.
                                get(tradListForRevision.indexOf(textWord.getText().toString()))); //alors on incrémente
                    } else if (cnt == 5) {
                        MediaPlayer incorrect= MediaPlayer.create(Revision.this,R.raw.incorrect);
                        incorrect.start();
                        Toast toast= Toast.makeText(getApplicationContext(),
                            "Wrong word !\nIt was "+wordListForRevision.
                                    get(tradListForRevision.indexOf(textWord.getText().toString())), Toast.LENGTH_SHORT);
                        System.out.println("Wrong word ! It was "+wordListForRevision.
                                get(tradListForRevision.indexOf(textWord.getText().toString())));
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                        View toastView = toast.getView();
                        toastView.setBackgroundResource(R.drawable.custom_background);
                        toast.show();
                    }
                }
            }


            System.out.println("counterLocal: "+counterLocal);
            if (counterLocal < listcounter.size()) { //counterLocal
                // c'est l'index i.e. c'est ce qui permet de savoir a quel mot on est dans la liste
                //cntTest c'est pour savoir a cmb de mots on est dans le test
                counterLocal += 1;
                cntTest += 1;
                System.out.println("j'incremente COUNTERLOCAL et CNTTEST");
            }

            if (cntTest < 5) { //si on s'est teste sur moins de 5 mots
                upperbound = 0;
                //on genere un bool2 pour l'apparition d'un nouveau mot
                Random rd2 = new Random();
                bool2 = rd2.nextBoolean();//donc mtn dans nos conditions ligne 79, il nous faut checker
                //bool2 et plus bool. sauf que je dois quand meme checker bool
                if(bool2){
                    revisionAlgoWord(upperbound);
                    setHintTranslation(editText);}
                else {
                    revisionAlgoTrad(upperbound);
                    setHintLanguage(editText);}
            } else { //si oui, on arrete le test et on donne le score
                if (score<2){
                    score(R.drawable.custom_background);
                    MediaPlayer fail= MediaPlayer.create(Revision.this,R.raw.fail);
                    fail.start();
                }
                else if (score == 2 || score == 3){
                    score(R.drawable.custom_background_3);
                }
                else{
                    score(R.drawable.custom_background_2);
                    MediaPlayer applause= MediaPlayer.create(Revision.this,R.raw.applause);
                    applause.start();
                }
                Intent intent = new Intent(Revision.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void revisionAlgoTrad(int upperbound) {
        Toast toast= Toast.makeText(getApplicationContext(), "Now give the corresponding word", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0,0 );
        toast.show();
        System.out.println("Give the corresponding word");
        System.out.println(listcounter);
        for (int i = 0; i < listcounter.size(); i++) {
            upperbound += Integer.parseInt(listcounter.get(i));
        }
        upperbound+=1;
        int min = 1;
        borneSup = 1;
        System.out.println("UPPERBOUND POUR LE INT_RANDOM VUAT= "+upperbound);
        int int_random = (int) (Math.random() * (upperbound - min ) + min)+1;
        System.out.println("int_random: " + int_random);
        for (int i = 0; i < wordListForRevision.size() ; i++) {
            System.out.println("i vaut: "+i);
            borneInf = borneSup;
            borneSup += Integer.parseInt(listcounter.get(i));
            if (int_random <= borneSup && int_random > borneInf && Integer.parseInt(listcounter.get(i)) > 0) {
                System.out.println("ce qui va apparaitre: "+tradListForRevision.get(i));
                System.out.println("ce qu'il faut mettre: "+wordListForRevision.get(i));
                System.out.println("counterLocal: " + counterLocal);
                textWord.setText(tradListForRevision.get(i));
                counterLocal = i;
                break;
            }
        }
    }
    public void revisionAlgoWord(int upperbound) {
        System.out.println("Give the corresponding translation");
        Toast toast= Toast.makeText(getApplicationContext(), "Now give the corresponding translation", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0,0 );
        toast.show();
        System.out.println(listcounter);
        for (int i = 0; i < listcounter.size(); i++) {
            upperbound += Integer.parseInt(listcounter.get(i));
        }
        upperbound+=1;
        int min = 1;
        borneSup = 1;
        System.out.println("UPPERBOUND POUR LE INT_RANDOM VUAT= "+upperbound);
        int int_random = (int) (Math.random() * (upperbound - min ) + min)+1;
        System.out.println("int_random: " + int_random);
        System.out.println(wordListForRevision);
        for (int i = 0; i < wordListForRevision.size() ; i++) {
            borneInf = borneSup;
            borneSup += Integer.parseInt(listcounter.get(i));
            System.out.println("borneInf: " + borneInf);
            System.out.println("borneSup: " + borneSup);
            System.out.println("i vaut= "+i);
            if (int_random <= borneSup && int_random > borneInf && Integer.parseInt(listcounter.get(i)) > 0) {
                System.out.println("ce qui va apparaitre: "+wordListForRevision.get(i));
                System.out.println("ce qu'il faut mettre: "+tradListForRevision.get(i));

                System.out.println("counterLocal: " + counterLocal);
                textWord.setText(wordListForRevision.get(i));
                counterLocal = i;

                break;
            }
        }
    }



    public void decrement() {
        System.out.println("Correct ! je decremente cnt et incremente score");
        cnt -= 1;
        score += 1;

        //listcounter.add(counterLocal, String.valueOf(cnt));
        System.out.println(listcounter);
        System.out.println("listcounter.get(counterLocal): "+listcounter.get(counterLocal));
        System.out.println("counterLocal: "+counterLocal);

        int x = Integer.parseInt(listcounter.get(counterLocal));
        x = x-1;
        listcounter.set(counterLocal, String.valueOf(x));
        System.out.println(listcounter);
        databaseReference.child(String.valueOf(counterLocal + 1)).child("compteur").setValue(cnt);
        toastCorrect();
    }

    public void increment(String str) {
        cnt += 1;
        MediaPlayer incorrect= MediaPlayer.create(Revision.this,R.raw.incorrect);
        incorrect.start();
        System.out.println(listcounter);
        System.out.println("listcounter.get(counterLocal): "+listcounter.get(counterLocal));
        System.out.println("counterLocal: "+counterLocal);
        int x = Integer.parseInt(listcounter.get(counterLocal));
        x=x+1;
        listcounter.set(counterLocal, String.valueOf(x));

        //listcounter.add(counterLocal, String.valueOf(cnt));
        System.out.println(listcounter);
        System.out.println("Wrong traduction! It was " + str);

        databaseReference.child(String.valueOf(counterLocal + 1)).child("compteur").setValue(cnt);
        Toast toast= Toast.makeText(getApplicationContext(),
                "Wrong traduction.\nIt was " + str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        CountDownTimer toastCountDown;
        int toastDurationInMilliSeconds = 7000;
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
        View toastView = toast.getView();
        toastView.setBackgroundResource(R.drawable.custom_background);
        toast.show();
        toastCountDown.start();
        System.out.println("counterLocal: "+counterLocal);
    }
    public void score(Object obj ){
        Toast toast = Toast.makeText(getApplicationContext(),
                "Test is finished!\nScore : " + score + "/5", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        View toastView = toast.getView();
        toastView.setBackgroundResource((Integer) obj);
        toast.show();
        System.out.println("counterLocal: "+counterLocal);
    }

    public void toastCorrect(){
        MediaPlayer success= MediaPlayer.create(Revision.this,R.raw.success);
        success.start();
        Toast toast= Toast.makeText(getApplicationContext(), "Correct !", Toast.LENGTH_SHORT);
        CountDownTimer toastCountDown;
        int toastDurationInMilliSeconds = 2000;
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
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0,0 );
        View toastView = toast.getView();
        toastView.setBackgroundResource(R.drawable.custom_background_2);
        toast.show();
        toastCountDown.start();
    }

    //public void toastIncorrect(String str){ }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    Intent intent = new Intent(Revision.this, itemfetch.class);
                    intent.putExtra("language",lang);
                    intent.putStringArrayListExtra("listWord", (ArrayList<String>) wordListForRevision);
                    intent.putStringArrayListExtra("listTrad", (ArrayList<String>) tradListForRevision);
                    intent.putStringArrayListExtra("listCounter", (ArrayList<String>) listcounter);
                    intent.putStringArrayListExtra("listDate", (ArrayList<String>) listDate);
                    intent.putExtra("indexW",wordIndex);
                    System.out.println(wordListForRevision);
                    System.out.println(tradListForRevision);
                    System.out.println(listcounter);
                    System.out.println(listDate);
                    startActivity(intent);
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setHintTranslation(EditText et1) {
        et1.setText("");
        et1.setHint("Translation...");
    }

    public void setHintLanguage(EditText et1){
        et1.setText("");
        et1.setHint(lang);
    }

}