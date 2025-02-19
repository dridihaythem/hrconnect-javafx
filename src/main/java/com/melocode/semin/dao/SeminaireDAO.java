package com.melocode.semin.dao;

import com.melocode.semin.models.Seminaire;
import com.melocode.semin.utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class SeminaireDAO {

    public static void ajouterSeminaire(Seminaire seminaire) {
        String query = "INSERT INTO seminaire (Nom_Seminaire, Description, Date_debut, Date_fin, Lieu, Formateur, Cout, Type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, seminaire.getNom());
            stmt.setString(2, seminaire.getDescription());
            stmt.setDate(3, Date.valueOf(seminaire.getDateDebut()));
            stmt.setDate(4, Date.valueOf(seminaire.getDateFin()));
            stmt.setString(5, seminaire.getLieu());
            stmt.setString(6, seminaire.getFormateur());
            stmt.setDouble(7, seminaire.getCout());
            stmt.setString(8, seminaire.getType());

            stmt.executeUpdate();

            // Set the generated ID for the seminar (if needed)
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                seminaire.setId(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void supprimerSeminaire(int id) {
        String query = "DELETE FROM seminaire WHERE ID_Seminaire = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Seminaire> recupererTousLesSeminaires() {
        ObservableList<Seminaire> seminaires = FXCollections.observableArrayList();
        String query = "SELECT * FROM seminaire";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Seminaire seminaire = new Seminaire(
                        rs.getInt("ID_Seminaire"),
                        rs.getString("Nom_Seminaire"),
                        rs.getString("Description"),
                        rs.getDate("Date_debut").toLocalDate(),
                        rs.getDate("Date_fin").toLocalDate(),
                        rs.getString("Lieu"),
                        rs.getString("Formateur"),
                        rs.getDouble("Cout"),
                        rs.getString("Type")
                );
                seminaires.add(seminaire);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seminaires;
    }

    public static void modifierSeminaire(Seminaire seminaire) {
        String query = "UPDATE seminaire SET Nom_Seminaire = ?, Description = ?, Date_debut = ?, Date_fin = ?, Lieu = ?, Formateur = ?, Cout = ?, Type = ? WHERE ID_Seminaire = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, seminaire.getNom());
            stmt.setString(2, seminaire.getDescription());
            stmt.setDate(3, java.sql.Date.valueOf(seminaire.getDateDebut()));
            stmt.setDate(4, java.sql.Date.valueOf(seminaire.getDateFin()));
            stmt.setString(5, seminaire.getLieu());
            stmt.setString(6, seminaire.getFormateur());
            stmt.setDouble(7, seminaire.getCout());
            stmt.setString(8, seminaire.getType());
            stmt.setInt(9, seminaire.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
