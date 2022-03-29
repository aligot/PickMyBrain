package com.example.listviewinlistview;
public class Language {
    private String languageName;
    private String image;
    public Language() {
        // Default constructor required for calls to DataSnapshot.getValue(Language.class)
    }

    public Language(String languageName, String image) {
        this.languageName = languageName;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

}

