package com.example.listviewinlistview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class add_language extends AppCompatActivity {
    private EditText editText;
    private Button button;
    private CircleImageView languageImage;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    List<String> wordListFetched,tradListFetched,listCounter,languageList,dateList;
    int wordIndex;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_language);
        this.setTitle("Add a new Language");
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.btn);
        //languageImage = (CircleImageView) findViewById(R.id.languageImage);
        dateList = getIntent().getStringArrayListExtra("listDate");
        wordListFetched = getIntent().getStringArrayListExtra("listWord");
        tradListFetched = getIntent().getStringArrayListExtra("listTrad");
        listCounter = getIntent().getStringArrayListExtra("listCounter");
        languageList = getIntent().getStringArrayListExtra("listLang");
        wordIndex = getIntent().getIntExtra("indexW",0);
        //btnRetour = findViewById(R.id.buttonBack);

/*
        languageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "select picture"), PICK_IMAGE);
            }
        });

        btnRetour.setOnClickListener(view -> {
            Intent intent = new Intent(add_language.this, MainActivity.class);
            startActivity(intent);
        });*/

        button.setOnClickListener(view -> {
            Boolean exists = false;
            String languageName = editText.getText().toString();
            if(languageName.equals("")) //verifie qu'on ecrit bien qqch
                Toast.makeText(add_language.this, "No language written", Toast.LENGTH_SHORT).show();
            else{//verifie que si qqch est ecrit, qu'il n'existe pas deja auparavant
                for (int i=0; i<languageList.size(); i++){
                    if(languageList.get(i).equals(languageName)){
                        System.out.println("dans le fooor "+i);
                        Toast.makeText(this, "Language already exists", Toast.LENGTH_SHORT).show();
                        exists = true;
                        break;
                    }
                }
                if(!exists){ //si la langue n'existait pas, on vient lui ajouter des mots
                    Intent intent = new Intent(add_language.this, itemfetch.class);
                    //System.out.println("dans add_language on a: "+languageName);
                    intent.putExtra("language", languageName);
                    editText.setHint("Add a new language...");
                    intent.putStringArrayListExtra("listWord", (ArrayList<String>) wordListFetched); //to pass an Arraylist from one activity to another
                    intent.putStringArrayListExtra("listTrad", (ArrayList<String>) tradListFetched);
                    intent.putStringArrayListExtra("listCounter", (ArrayList<String>) listCounter);
                    intent.putStringArrayListExtra("listDate", (ArrayList<String>) dateList);
                    intent.putExtra("indexW", wordIndex);
                    System.out.println("dans add_language wordindex vaut: " + wordIndex);
                    startActivity(intent);
                    Toast.makeText(add_language.this, "You can add some vocabulary", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intentBack = new Intent(add_language.this, HomeActivity.class);
        startActivity(intentBack);
    }



/*

    private void uploadToFireBase(Uri uri){
        System.out.println("In function");
        StorageReference fileRef = reference.child(System.currentTimeMillis()+"." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Model model = new Model(uri.toString());
                        String modelId = databaseReference.push().getKey();
                        databaseReference.child(modelId).setValue(model);
                        Toast.makeText(add_language.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(add_language.this, "Uploading failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }*/
}