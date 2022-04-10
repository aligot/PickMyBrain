package com.example.listviewinlistview;

public class Word {
    String mot, traduction, dateAdded;
    int compteur;

    public Word() {
    }
    public Word(String mot, String traduction, String dateAdded, int compteur) {
        this.mot = mot;
        this.traduction = traduction;
        this.compteur = compteur;
        this.dateAdded = dateAdded;
    }/*ArrayAdapter's built-in Filter uses the toString() return from the model class (i.e., its
    type parameter) to perform its filtering comparisons. You don't necessarily need a custom Filter
    implementation if you're able to override User's toString() method to return what you want to
    compare (provided its filtering algorithm is suitable to your situation). In this case:*/
    @Override
    public String toString() {
        return mot;
    }
    public int getCompteur() {
        return compteur;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setCompteur(int compteur) {
        this.compteur = compteur;
    }

    public String getMot() {
        return mot;
    }

    public void setMot(String mot) {
        this.mot = mot;
    }

    public String getTraduction() {
        return traduction;
    }

    public void setTraduction(String traduction) {
        this.traduction = traduction;
    }
/*
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }*/
}
