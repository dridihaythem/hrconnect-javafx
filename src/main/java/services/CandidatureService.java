package services;

import models.Candidature;
import utils.MyDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CandidatureService {

    private Connection conn;

    public CandidatureService() {
        this.conn = MyDb.getInstance().getConn();
    }

    // Méthode pour créer une candidature
    public void create(Candidature candidature) throws Exception {
        String sql = "INSERT INTO candidature (candidat_id, offre_emploi_id, cv) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, candidature.getCandidatId());
        stmt.setInt(2, candidature.getOffreEmploiId());
        stmt.setString(3, candidature.getCv());
        stmt.executeUpdate();
    }

    // Méthode pour mettre à jour une candidature
    public void update(Candidature candidature) throws Exception {
        String sql = "UPDATE candidature SET candidat_id = ?, offre_emploi_id = ?, cv = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, candidature.getCandidatId());
        stmt.setInt(2, candidature.getOffreEmploiId());
        stmt.setString(3, candidature.getCv());
        stmt.setInt(4, candidature.getId());
        stmt.executeUpdate();
    }

    // Méthode pour récupérer toutes les candidatures
    public List<Candidature> getAll() throws Exception {
        String sql = "SELECT * FROM candidature";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        List<Candidature> candidatures = new ArrayList<>();
        while (rs.next()) {
            Candidature candidature = new Candidature(
                    rs.getInt("id"),
                    rs.getInt("candidat_id"),
                    rs.getInt("offre_emploi_id"),
                    rs.getString("cv")
            );
            candidatures.add(candidature);
        }
        return candidatures;
    }

    // Méthode pour récupérer les candidatures par offre_emploi_id
    public List<Candidature> getCandidaturesByOffreEmploiId(int offreEmploiId) throws Exception {
        String sql = "SELECT * FROM candidature WHERE offre_emploi_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, offreEmploiId);
        ResultSet rs = stmt.executeQuery();
        List<Candidature> candidatures = new ArrayList<>();
        while (rs.next()) {
            Candidature candidature = new Candidature(
                    rs.getInt("id"),
                    rs.getInt("candidat_id"),
                    rs.getInt("offre_emploi_id"),
                    rs.getString("cv")
            );
            candidatures.add(candidature);
        }
        return candidatures;
    }

    // Méthode pour récupérer les candidatures par candidat_id
    public List<Candidature> getCandidaturesByCandidatId(int candidatId) throws Exception {
        String sql = "SELECT * FROM candidature WHERE candidat_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, candidatId);
        ResultSet rs = stmt.executeQuery();
        List<Candidature> candidatures = new ArrayList<>();
        while (rs.next()) {
            Candidature candidature = new Candidature(
                    rs.getInt("id"),
                    rs.getInt("candidat_id"),
                    rs.getInt("offre_emploi_id"),
                    rs.getString("cv")
            );
            candidatures.add(candidature);
        }
        return candidatures;
    }

    // Méthode pour supprimer une candidature
    public void delete(Candidature candidature) throws Exception {
        String sql = "DELETE FROM candidature WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, candidature.getId());
        stmt.executeUpdate();
    }

    // Méthode pour supprimer les candidatures par offre_emploi_id
    public void deleteByOffreEmploiId(int offreEmploiId) throws Exception {
        String sql = "DELETE FROM candidature WHERE offre_emploi_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, offreEmploiId);
        stmt.executeUpdate();
    }
}