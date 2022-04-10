package com.example.listviewinlistview;
public class Language {
    private String languageName;
    public Language() {
        // Default constructor required for calls to DataSnapshot.getValue(Language.class)
    }
    public Language(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

}

