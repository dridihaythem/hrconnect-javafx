package models;

import javafx.beans.property.*;

public class Employe {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty prenom = new SimpleStringProperty();
    private final IntegerProperty soldeConges = new SimpleIntegerProperty();

    public Employe() {}

    public Employe(String nom, String prenom, int soldeConges) {
        setNom(nom);
        setPrenom(prenom);
        setSoldeConges(soldeConges);
    }

    // Getters/Setters
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    public String getNom() { return nom.get(); }
    public void setNom(String value) { nom.set(value); }
    public StringProperty nomProperty() { return nom; }

    public String getPrenom() { return prenom.get(); }
    public void setPrenom(String value) { prenom.set(value); }
    public StringProperty prenomProperty() { return prenom; }

    public int getSoldeConges() { return soldeConges.get(); }
    public void setSoldeConges(int value) { soldeConges.set(value); }
    public IntegerProperty soldeCongesProperty() { return soldeConges; }
}