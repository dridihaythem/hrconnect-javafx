package services;

import entities.MdpGen;
import entities.MdpHash;
import entities.Utilisateur;
import entities.enums.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UtilisateurCrud implements IUtilisateurCrud<Utilisateur> {
    private List<Utilisateur> registeredUsers;
    Connection cnx2;

    public UtilisateurCrud() {
        cnx2 = MyConnection.getInstance().getCnx();
        registeredUsers = getAllUtilisateurs();
    }

    // Méthode pour charger la liste des utilisateurs enregistrés depuis la base de données
    private List<Utilisateur> loadRegisteredUsers() {
        List<Utilisateur> users = new ArrayList<>();
        return users;
    }

    // Méthode pour obtenir la liste des utilisateurs enregistrés
    public List<Utilisateur> getRegisteredUsers() {
        if (registeredUsers == null) {
            // Charger la liste des utilisateurs enregistrés si elle n'a pas déjà été chargée
            registeredUsers = loadRegisteredUsers();
        }
        return registeredUsers;
    }

    @Override
    public void ajouterEntite(Utilisateur u) {
        String req1 = "INSERT INTO user (email, roles, password, nom, prenom, tel, cin) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx2.prepareStatement(req1)) {
            pst.setString(1, u.getEmail());
            pst.setString(2, u.getroles().name());
            pst.setString(3, MdpHash.hashPassword(u.getpassword()));
            pst.setString(4, u.getNom());
            pst.setString(5, u.getPrenom());
            pst.setString(6, u.gettel());
            pst.setInt(7, u.getCin());
            pst.executeUpdate();
            System.out.println("Utilisateur ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Utilisateur> afficherEntite() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String req3 = "SELECT * FROM user";
        try (Statement stm = cnx2.createStatement(); ResultSet rs = stm.executeQuery(req3)) {
            while (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setId(rs.getInt("id"));
                u.setCin(rs.getInt("cin"));
                u.settel(rs.getString("tel"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setEmail(rs.getString("email"));
                u.setpassword(rs.getString("password"));
                u.setroles(Role.valueOf(rs.getString("roles")));
                utilisateurs.add(u);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return utilisateurs;
    }

    @Override
    public void modifierEntite(Utilisateur u) {
        String req2 = "UPDATE user SET cin=?, tel=?, nom=?, prenom=?, email=?, password=?, roles=? WHERE id=?";
        try (PreparedStatement pst = cnx2.prepareStatement(req2)) {
            pst.setInt(1, u.getCin());
            pst.setString(2, u.gettel());
            pst.setString(3, u.getNom());
            pst.setString(4, u.getPrenom());
            pst.setString(5, u.getEmail());
            pst.setString(6, MdpHash.hashPassword(u.getpassword()));
            pst.setString(7, u.getroles().name());
            pst.setInt(8, u.getId());
            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Utilisateur modifié avec succès.");
            } else {
                System.out.println("Aucune modification effectuée pour l'utilisateur.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimerEntite(int id) {
        String req3 = "DELETE FROM user WHERE id=?";
        try (PreparedStatement pst = cnx2.prepareStatement(req3)) {
            pst.setInt(1, id);
            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Utilisateur supprimé");
            } else {
                System.out.println("Aucun utilisateur supprimé.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Utilisateur> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try (PreparedStatement statement = cnx2.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(resultSet.getInt("id"));
                utilisateur.setCin(resultSet.getInt("cin"));
                utilisateur.settel(resultSet.getString("tel"));
                utilisateur.setNom(resultSet.getString("nom"));
                utilisateur.setPrenom(resultSet.getString("prenom"));
                utilisateur.setEmail(resultSet.getString("email"));
                utilisateur.setpassword(resultSet.getString("password"));
                utilisateur.setroles(Role.valueOf(resultSet.getString("roles")));
                utilisateurs.add(utilisateur);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return utilisateurs;
    }
    public Utilisateur getUtilisateurByEmail(String email) {
        cnx2 = MyConnection.getInstance().getCnx();

        String query = "SELECT * FROM user WHERE email = ?";
        Utilisateur user = null;
        try {
            PreparedStatement pst = cnx2.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                user = new Utilisateur();
                user.setId(rs.getInt("id"));
                user.setCin(rs.getInt("cin"));
                user.settel(rs.getString("tel"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setpassword(rs.getString("password"));
                user.setroles(entities.enums.Role.valueOf(rs.getString("roles")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Utilisateur getUtilisateurById(int id) {
        Utilisateur utilisateur = null;
        String query = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement pst = cnx2.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getInt("id"));
                    utilisateur.setCin(rs.getInt("cin"));
                    utilisateur.settel(rs.getString("tel"));
                    utilisateur.setNom(rs.getString("nom"));
                    utilisateur.setPrenom(rs.getString("prenom"));
                    utilisateur.setEmail(rs.getString("email"));
                    utilisateur.setpassword(rs.getString("password"));
                    utilisateur.setroles(Role.valueOf(rs.getString("roles")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;
    }

    public boolean authenticateUser(String email, String password) {
        Utilisateur user = registeredUsers.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
        if (user != null) {
            String hashedPassword = MdpHash.hashPassword(password);
            return user.getpassword().equals(hashedPassword);
        } else {
            return false;
        }
    }

    public boolean utilisateurExisteDeja(String cin, String email) {
        String query = "SELECT COUNT(*) FROM user WHERE cin = ? OR email = ?";
        try (PreparedStatement pst = cnx2.prepareStatement(query)) {
            pst.setString(1, cin);
            pst.setString(2, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ObservableList<Utilisateur> getUtilisateursByRole(Role role) {
        List<Utilisateur> utilisateursFiltres = new ArrayList<>();
        for (Utilisateur utilisateur : getAllUtilisateurs()) {
            if (utilisateur.getroles().equals(role)) {
                utilisateursFiltres.add(utilisateur);
            }
        }
        return FXCollections.observableArrayList(utilisateursFiltres);
    }


    public List<Utilisateur> searchRHByNameOrEmail(String searchTerm) {
        List<Utilisateur> rhMembers = new ArrayList<>();
        String query = "SELECT * FROM user WHERE (nom LIKE ? OR prenom LIKE ? OR email LIKE ?) AND roles = ?";
        try (PreparedStatement pst = cnx2.prepareStatement(query)) {
            String searchPattern = "%" + searchTerm + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            pst.setString(3, searchPattern);
            pst.setString(4, Role.RH.name());
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Utilisateur rh = new Utilisateur();
                    rh.setId(rs.getInt("id"));
                    rh.setNom(rs.getString("nom"));
                    rh.setPrenom(rs.getString("prenom"));
                    rh.setEmail(rs.getString("email"));
                    rh.setpassword(rs.getString("password"));
                    rh.setroles(Role.valueOf(rs.getString("roles")));
                    rhMembers.add(rh);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la recherche des RH: " + e.getMessage());
        }
        return rhMembers;
    }

    public List<Utilisateur> searchByNameOrEmail(String searchTerm) {
        List<Utilisateur> users = new ArrayList<>();
        String query = "SELECT * FROM user WHERE nom LIKE ? OR prenom LIKE ? OR email LIKE ?";
        try (PreparedStatement pst = cnx2.prepareStatement(query)) {
            String searchPattern = "%" + searchTerm + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            pst.setString(3, searchPattern);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Utilisateur user = new Utilisateur();
                    user.setId(rs.getInt("id"));
                    user.setNom(rs.getString("nom"));
                    user.setPrenom(rs.getString("prenom"));
                    user.setEmail(rs.getString("email"));
                    user.setpassword(rs.getString("password"));
                    user.setroles(Role.valueOf(rs.getString("roles")));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la recherche des utilisateurs: " + e.getMessage());
        }
        return users;
    }

    public List<Utilisateur> searchByNameOrEmail(String searchTerm, Role role) {
        List<Utilisateur> users = new ArrayList<>();
        String query = "SELECT * FROM user WHERE (nom LIKE ? OR prenom LIKE ? OR email LIKE ?) AND roles = ?";
        try (PreparedStatement pst = cnx2.prepareStatement(query)) {
            String searchPattern = "%" + searchTerm + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            pst.setString(3, searchPattern);
            pst.setString(4, role.name());
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Utilisateur user = new Utilisateur();
                    user.setId(rs.getInt("id"));
                    user.setNom(rs.getString("nom"));
                    user.setPrenom(rs.getString("prenom"));
                    user.setEmail(rs.getString("email"));
                    user.setpassword(rs.getString("password"));
                    user.setroles(Role.valueOf(rs.getString("roles")));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la recherche des utilisateurs: " + e.getMessage());
        }
        return users;
    }
    public List<Utilisateur> sortUtilisateurs(String sortBy) {
        List<Utilisateur> users = getAllUtilisateurs();
        switch (sortBy) {
            case "id":
                users.sort(Comparator.comparingInt(Utilisateur::getId));
                break;
            case "nom":
                users.sort(Comparator.comparing(Utilisateur::getNom));
                break;
            case "prenom":
                users.sort(Comparator.comparing(Utilisateur::getPrenom));
                break;
            case "email":
                users.sort(Comparator.comparing(Utilisateur::getEmail));
                break;
            case "role":
                users.sort(Comparator.comparing(u -> u.getroles().name()));
                break;
            default:
                // No sorting
                break;
        }
        return users;
    }


    public static String genererNouveauMdp() {
        return MdpGen.genererMdp();
    }
}