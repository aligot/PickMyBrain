package com.example.pickmybrain;

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
        btnValidate = findViewById(R.id.btnValidate);

        lang = getIntent().getStringExtra("language");
        wordListForRevision = getIntent().getStringArrayListExtra("wordListRevision");
        tradListForRevision = getIntent().getStringArrayListExtra("tradListRevision");
        listcounter = getIntent().getStringArrayListExtra("listCounter");
        listDate = getIntent().getStringArrayListExtra("listDate");
        wordIndex = getIntent().getIntExtra("indexW",0);
        score = 0;

        Toast toast3 = Toast.makeText(Revision.this,
                "5 element Topic Quizz begins !", Toast.LENGTH_SHORT);
        toast3.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        View toastView1 = toast3.getView();
        toastView1.setBackgroundResource(R.drawable.custom_background_4);
        toast3.show();





        databaseReference = FirebaseDatabase.getInstance().getReference().child("Languages").child(lang);
        this.setTitle(lang+"Topic Quizz");
        Random rd = new Random();
        boolean bool = rd.nextBoolean();//bool généré pour pour l'apparition du 1er mot de la rev

        System.out.println("je fais la PREMIERE REVISION");

        //necessaire de mettre un if ici?

        if (revisionFaite) {
            if(bool){ revisionAlgoWord(upperbound);
            setHintTranslation(editText);}//bool=true, on génere un mot et faut trouver trad
            else{ revisionAlgoTrad(upperbound);
            setHintLanguage(editText);} //inversement si bool = false
        }

        btnValidate.setOnClickListener(view -> {
            Random rd1 = new Random();
            boolean bool1 = rd1.nextBoolean(); //on genere un bool
            cnt = Integer.parseInt(listcounter.get(counterLocal));
            //quand dans le if ya premierMotATraduire, ça veut dire qu'on regarde si on est au 1er element testé
            //du topic quizz ou si on est aux suivants.
            //y'a surement bcp plus simple d'écriture. Genre utiliser le cntTest plutot que ce boolean
            if(bool2 && premierMotATraduire){ //donc si cette condition est vraie, c'est qu'on est
                //plus au premier mot du test et qu'on va devoir régénérer un mot
                if(counterLocal==wordListForRevision.size()){
                    counterLocal-=1;
                }
                traduction = editText.getText().toString().trim();
                premierMotATraduire = true; //si on est rentré dans le if avec bool2 && premierMotATraduire,
                // c'est que ce dernier etait true, donc est-ce qu'il faut vrmt le remettre a true?
                if (traduction.equals(tradListForRevision.get(counterLocal))) {
                    if (cnt > 1 && cnt < 6) {
                        decrement();
                    }
                    else{ //si la traduction est correcte mais que ton cnt vaut 1: on met juste qu'on a la bonne rep
                        //mais on decremente ap le cnt
                        score += 1;
                        toastCorrect();
                    }
                } else { //si traduction donnée est fausse
                    if (cnt > 0 && cnt < 5) { //et que le compteur du mot est entre 0 et 5
                        increment(tradListForRevision.
                                get(wordListForRevision.indexOf(textWord.getText().toString()))); //alors on incrémente
                    } else if (cnt == 5) {
                        MediaPlayer incorrect= MediaPlayer.create(Revision.this,R.raw.incorrect);
                        incorrect.start();
                        Toast toast= Toast.makeText(getApplicationContext(),
                                "Wrong value !\nIt was "+tradListForRevision.
                                        get(wordListForRevision.indexOf(textWord.getText().toString())), Toast.LENGTH_SHORT);

                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                        View toastView = toast.getView();
                        toastView.setBackgroundResource(R.drawable.custom_background);
                        toast.show();
                        }
                }setHintLanguage(editText);
            }

            else if (!bool2 && premierMotATraduire){// si cette condition est vraie, c'est qu'on est
                //plus au premier mot du test et qu'on va devoir régénérer une trad
                if(counterLocal==wordListForRevision.size()){
                    counterLocal-=1;
                }
                mot = editText.getText().toString().trim();
                premierMotATraduire = true;
                if(mot.equals(wordListForRevision.get(counterLocal))){//si le mot donné est bon

                    if (cnt > 1 && cnt < 6) { //si le compteur/score du mot est compris entre 1 et 6 non inclu
                        //et qu'on a le bon mot: alors on peut le décrémenter.
                        decrement();
                    }
                    else{
                        score += 1;
                        toastCorrect();
                    }
                }
                else { //si mot donné est faux

                    if (cnt > 0 && cnt < 5) { //et que le compteur/score du mot est entre 0 et 5
                        increment(wordListForRevision.
                                get(tradListForRevision.indexOf(textWord.getText().toString()))); //alors on incrémente
                    } else if (cnt == 5) { //
                        MediaPlayer incorrect= MediaPlayer.create(Revision.this,R.raw.incorrect);
                        incorrect.start();
                        Toast toast= Toast.makeText(getApplicationContext(),
                                "Wrong key !\nIt was "+wordListForRevision.
                                        get(tradListForRevision.indexOf(textWord.getText().toString())), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                        View toastView = toast.getView();
                        toastView.setBackgroundResource(R.drawable.custom_background);
                        toast.show();
                    }
                }setHintTranslation(editText);
            }

            else if(bool){ //faut la trad
                //si ça c'est vrai c'est qu'on est au premier element et qu'on génère un mot
                //(cf le 1er test avant le Listener sur btnValidate)

                traduction = editText.getText().toString().trim();
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
                        increment(tradListForRevision.
                                get(wordListForRevision.indexOf(textWord.getText().toString()))); //alors on incrémente
                    } else if (cnt == 5) {
                        MediaPlayer incorrect= MediaPlayer.create(Revision.this,R.raw.incorrect);
                        incorrect.start();
                        Toast toast= Toast.makeText(getApplicationContext(),
                                "Wrong value !\nIt was "+tradListForRevision.
                                        get(wordListForRevision.indexOf(textWord.getText().toString())), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                        View toastView = toast.getView();
                        toastView.setBackgroundResource(R.drawable.custom_background);
                        toast.show();
                    }
                }setHintLanguage(editText);
            }

            else{ //faut le mot
                //si ça c'est vrai c'est qu'on est au premier element et qu'on génère une trad
                //(cf le 1er test avant le Listener sur btnValidate)
                mot = editText.getText().toString().trim();
                premierMotATraduire = true;
                if(mot.equals(wordListForRevision.get(counterLocal))){ //si le mot donné est bon
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
                        increment(wordListForRevision.
                                get(tradListForRevision.indexOf(textWord.getText().toString()))); //alors on incrémente
                    } else if (cnt == 5) {
                        MediaPlayer incorrect= MediaPlayer.create(Revision.this,R.raw.incorrect);
                        incorrect.start();
                        Toast toast= Toast.makeText(getApplicationContext(),
                            "Wrong key !\nIt was "+wordListForRevision.
                                    get(tradListForRevision.indexOf(textWord.getText().toString())), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                        View toastView = toast.getView();
                        toastView.setBackgroundResource(R.drawable.custom_background);
                        toast.show();
                    }
                }
            }

            //pas sur de savoir pq incrementer counterLocal puisque je suis
            //pas la liste telle quelle mais je prends un élément random
            //a la limité cntTest c'est ok puisqu'il doit augmenter.
            //en vrai logique d'incrementer counterLocal puisque tant qu'il augmente mais qu'il est
            //< listcounter.size() alors alors on continue d'incrémenter cntTest
            //mais c'est bizarrement fait, on pourrait faire un while global cntTest < 5 p.ex
            if (counterLocal < listcounter.size()) { //counterLocal
                // c'est l'index i.e. c'est ce qui permet de savoir a quel mot on est dans la liste
                //cntTest c'est pour savoir cmb de mots on a fait dans le test
                counterLocal += 1;
                cntTest += 1;
            }



            if (cntTest < 5) { //si on s'est teste sur moins de 5 mots, i.e. on refait un test
                upperbound = 0;
                //on genere un bool2 pour l'apparition d'un nouveau mot
                Random rd2 = new Random();
                bool2 = rd2.nextBoolean();//donc mtn dans nos conditions ligne 79, il nous faut checker
                //bool2 et plus bool. sauf que je dois quand meme checker bool
                if(bool2){ //aléatoirement
                    revisionAlgoWord(upperbound); //on regenere un mot
                    setHintTranslation(editText);}
                else {
                    revisionAlgoTrad(upperbound); //soit on regenere une trad
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

    public void revisionAlgoTrad(int upperbound) {//algorithme appelé qui propose une value avec une
        // certaine probabilité selon les scores de chaque element (élevée quand le score est bas
        // et basse quand il est haut) et demande la key correspondante
        Toast toast= Toast.makeText(getApplicationContext(), "Now give the corresponding key", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0,0 );
        toast.show();
        for (int i = 0; i < listcounter.size(); i++) {
            upperbound += Integer.parseInt(listcounter.get(i)); //create an upperbound=sum of score
            //of each element
        }
        upperbound+=1;
        int min = 1;
        borneSup = 1;
        int int_random = (int) (Math.random() * (upperbound - min ) + min)+1;
        for (int i = 0; i < wordListForRevision.size() ; i++) {
            borneInf = borneSup;
            borneSup += Integer.parseInt(listcounter.get(i));
            if (int_random <= borneSup && int_random > borneInf && Integer.parseInt(listcounter.get(i)) > 0) {
                textWord.setText(tradListForRevision.get(i));
                counterLocal = i;
                break;
            }
        }
    }

    public void revisionAlgoWord(int upperbound) { //algorithme appelé qui propose une key avec une
        // certaine probabilité selon les scores de chaque element (élevée quand le score est bas
        // et basse quand il est haut) et demande la value correspondante
        Toast toast= Toast.makeText(getApplicationContext(), "Now give the corresponding value", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0,0 );
        toast.show();

        for (int i = 0; i < listcounter.size(); i++) {
            upperbound += Integer.parseInt(listcounter.get(i));
        }
        upperbound+=1;
        int min = 1;
        borneSup = 1;
        int int_random = (int) (Math.random() * (upperbound - min ) + min)+1;
        for (int i = 0; i < wordListForRevision.size() ; i++) {
            borneInf = borneSup;
            borneSup += Integer.parseInt(listcounter.get(i));
            if (int_random <= borneSup && int_random > borneInf && Integer.parseInt(listcounter.get(i)) > 0) {
                textWord.setText(wordListForRevision.get(i));
                counterLocal = i;
                break;
            }
        }
    }
//rappel:
// cnt cest le compteur associe a chaque mot et en effet ligne 82 on définit
// cnt = Integer.parseInt(listcounter.get(counterLocal));
//    //counterlocal c'est l'index i.e. c'est ce qui permet de savoir a quel mot on est dans la liste
    //mais du coup en faisant cnt -=1 et x-=1 je diminue pas 2 fois le listcounter.get(counterLocal)
    //score =
    public void decrement() { //fct qui defini ce qu'il se passe lorsqu'on a une bonne réponse
        cnt -= 1;
        score += 1;
        int x = Integer.parseInt(listcounter.get(counterLocal)); //on converti le score du mot en int
        x = x-1;
        listcounter.set(counterLocal, String.valueOf(x));
        databaseReference.child(String.valueOf(counterLocal + 1)).child("compteur").setValue(cnt);
        toastCorrect();
    }

    public void increment(String str) { // fct qui defini ce qu'il se passe lorsqu'on fait une erreur
        cnt += 1;
        MediaPlayer incorrect= MediaPlayer.create(Revision.this,R.raw.incorrect);
        incorrect.start();

        int x = Integer.parseInt(listcounter.get(counterLocal));
        x=x+1;
        listcounter.set(counterLocal, String.valueOf(x));
        databaseReference.child(String.valueOf(counterLocal + 1)).child("compteur").setValue(cnt);

        Toast toast= Toast.makeText(getApplicationContext(),
                "Wrong value.\nIt was " + str, Toast.LENGTH_SHORT);
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
    }

    public void score(Object obj ){ //enonce le score quand quiz fini
        Toast toast = Toast.makeText(getApplicationContext(),
                "Topic Quizz is finished!\nTQ Score : " + score + "/5", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        View toastView = toast.getView();
        toastView.setBackgroundResource((Integer) obj);
        toast.show();
    }

    public void toastCorrect(){ //toast+musique lorsque réponse correcte
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

    @Override
    public void onBackPressed() { //defini ce que fait l'app quand on click sur btn retour du tel
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
                    startActivity(intent);
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //remplace les entries par ce qu'il faut
    public void setHintTranslation(EditText et1) {
        et1.setText("");
        et1.setHint("Value");
    }

    public void setHintLanguage(EditText et1){
        et1.setText("");
        et1.setHint("Key");//et1.setHint(lang);
    }

}