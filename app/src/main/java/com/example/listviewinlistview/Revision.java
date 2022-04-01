package com.example.listviewinlistview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Revision extends AppCompatActivity {

    EditText editText;
    TextView textWord;
    Button btnValidate;
    FloatingActionButton btnBack;
    String lang, traduction, mot;
    List<String> wordListForRevision, tradListForRevision, listcounter;
    DatabaseReference databaseReference;
    int counterLocal, cnt, cntTest, score, upperbound, borneInf, borneSup; //cnt cest le compteur associe a chaque mot
    //counterlocal c'est l'index i.e. c'est ce qui permet de savoir a quel mot on est
    //cntTest : c'est pour savoir a cmb de mots on est dans le test
    //upperbound c'est la valeur maximale qui peut etre generee aleatoirement
    //lowerbound c'est une borne qui augmente en ajoutant la valeur des compteurs
    boolean revisionFaite = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revision);
        editText = findViewById(R.id.traduction);
        textWord = findViewById(R.id.textWord);
        //btnBack = findViewById(R.id.btnBack);
        btnValidate = findViewById(R.id.btnValidate);
        lang = getIntent().getStringExtra("language");
        wordListForRevision = getIntent().getStringArrayListExtra("wordListRevision");
        tradListForRevision = getIntent().getStringArrayListExtra("tradListRevision");
        listcounter = getIntent().getStringArrayListExtra("listcounter");
        score = 0;
        Toast toast3 = Toast.makeText(Revision.this,
                "5 words test begins !", Toast.LENGTH_SHORT);
        toast3.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        View toastView1 = toast3.getView();
        toastView1.setBackgroundResource(R.drawable.custom_background_4);
        toast3.show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Languages").child(lang);
        this.setTitle("Revisions: " + lang);
        if (revisionFaite) {
            revisionAlgo(upperbound);
        }
        btnValidate.setOnClickListener(view -> {
            traduction = editText.getText().toString().trim(); //trim() nécessiare?
            System.out.println("traduction: " + traduction);
            cnt = Integer.parseInt(listcounter.get(counterLocal));
            System.out.println("cnt: " + cnt);
            System.out.println("tradListForRevision.get(counterLocal): " + tradListForRevision.get(counterLocal));
            if (traduction.equals(tradListForRevision.get(counterLocal))) {
                if (cnt > 1 && cnt < 6) {
                    decrement();
                    System.out.println("l'index a été incrementé et vaut: " + cnt);
                }
            } else { //si traduction donnée est fausse
                if (cnt > 0 && cnt < 5) { //et que le compteur du mot est entre 0 et 5
                    increment(); //alors on incrémente
                } else if (cnt == 5) {
                    Toast toast= Toast.makeText(getApplicationContext(),
                            "Wrong traduction ! It was " + tradListForRevision.
                                    get(wordListForRevision.indexOf(textWord.getText().toString())), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.drawable.custom_background);
                    toast.show();
                }

            }
            if (counterLocal < listcounter.size()) {
                counterLocal += 1;
                cntTest += 1;
            }
            setHintAndText(editText);
            if (cntTest < 5) { //si on s'est teste sur moins de 5 mots
                upperbound = 0;
                revisionAlgo(upperbound);
            } else {
                if (score<2){
                    score(R.drawable.custom_background);
                }
                else if (score == 2 || score == 3){
                    score(R.drawable.custom_background_3);
                }
                else{
                    score(R.drawable.custom_background_2);
                }
                Intent intent = new Intent(Revision.this, MainActivity.class);
                startActivity(intent);
            }
        });
        /*
        btnBack.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Exit");
            builder.setMessage("Are you sure you want to exit?");
            builder.setPositiveButton("Confirm",
                    (dialog, which) -> {
                        Intent intent = new Intent(Revision.this, MainActivity.class);
                        startActivity(intent);
                    });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        });*/
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    Intent intent = new Intent(Revision.this, MainActivity.class);
                    startActivity(intent);
                });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void setHintAndText(EditText et1) {
        et1.setText("");
        et1.setHint("Traduction...");
    }

    public void revisionAlgo(int upperbound) {
        for (int i = 0; i < listcounter.size(); i++) {
            upperbound += Integer.parseInt(listcounter.get(i));
        }
        int min = 2;
        borneSup = 1;
        int int_random = (int) (Math.random() * (upperbound - min + 1) + min);
        System.out.println("int_random: " + int_random);
        for (int i = 0; i < wordListForRevision.size() - 1; i++) { //i va de 0 a 7
            borneInf = borneSup;
            borneSup += Integer.parseInt(listcounter.get(i));
            System.out.println("borneInf: " + borneInf);
            System.out.println("borneSup: " + borneSup);
            if (int_random <= borneSup && int_random > borneInf && Integer.parseInt(listcounter.get(i)) > 0) {
                textWord.setText(wordListForRevision.get(i));
                System.out.println(wordListForRevision.get(i));
                counterLocal = i;
                System.out.println("counterLocal: " + counterLocal);
                break;
            }
        }
    }

    public void decrement() {
        cnt -= 1;
        score += 1;
        listcounter.add(counterLocal, String.valueOf(cnt));
        databaseReference.child(String.valueOf(counterLocal + 1)).child("compteur").setValue(cnt);
        //Toast.makeText(Revision.this, "Correct !", Toast.LENGTH_SHORT).show();
        Toast toast= Toast.makeText(getApplicationContext(), "Correct !", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0,0 );
        View toastView = toast.getView();
        toastView.setBackgroundResource(R.drawable.custom_background_2);
        toast.show();
    }

    public void increment() {
        cnt += 1;
        listcounter.add(counterLocal, String.valueOf(cnt));
        databaseReference.child(String.valueOf(counterLocal + 1)).child("compteur").setValue(cnt);
        Toast toast= Toast.makeText(getApplicationContext(),
                "Wrong traduction ! It was " + tradListForRevision.
                        get(wordListForRevision.indexOf(textWord.getText().toString())), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        View toastView = toast.getView();
        toastView.setBackgroundResource(R.drawable.custom_background);
        toast.show();
    }

    public void score(Object obj ){
        Toast toast = Toast.makeText(getApplicationContext(),
                "Test is finished ! Score : " + score + "/5", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        View toastView = toast.getView();
        toastView.setBackgroundResource((Integer) obj);
        toast.show();
    }
}