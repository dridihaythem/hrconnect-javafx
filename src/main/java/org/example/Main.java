package org.example;

import models.*;
import services.DemandeCongeService;
import services.EmployeService;
import services.ValiderCongeService;
import utils.MyDb;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //test db connnection
        MyDb db = MyDb.getInstance();
        EmployeService employeService = new EmployeService();
        DemandeCongeService demandeService = new DemandeCongeService();
        ValiderCongeService validationService = new ValiderCongeService();

        try {
            // =================================================================
            // 1. CRUD EMPLOYÉ
            // =================================================================

            // Création d'un employé
            Employe employe = new Employe("Martin", "Sophie", 25);
            employeService.create(employe);
            System.out.println("✅ Employé créé - ID: " + employe.getId());

            // Mise à jour du solde
            employe.setSoldeConges(20);
            employeService.update(employe);
            System.out.println("🔄 Solde mis à jour: " + employe.getSoldeConges());

            // =================================================================
            // 2. CRUD DEMANDE DE CONGÉ
            // =================================================================

            // Création d'une demande
            /*Demande_Conge demande = new Demande_Conge(
                    employe,
                    TypeConge.MALADIE,
                    LocalDate.of(2023, 12, 1),
                    LocalDate.of(2023, 12, 5)
            );
            demandeService.create(demande);
            System.out.println("\n✅ Demande créée - ID: " + demande.getId());

            // Mise à jour du statut
            demande.setStatut(StatutDemande.APPROUVE);
            demandeService.update(demande);
            System.out.println("🔄 Statut mis à jour: " + demande.getStatut());

            // =================================================================
            // 3. CRUD VALIDATION
            // =================================================================

            // Validation de la demande
            Valider_Conge validation = new Valider_Conge(
                    demande,
                    true,
                    "Aucun problème détecté"
            );
            validationService.create(validation);
            System.out.println("\n✅ Validation créée - ID: " + validation.getId());

            // =================================================================
            // 4. RÉCUPÉRATION DES DONNÉES
            // =================================================================

            // Liste des employés
            List<Employe> employes = employeService.getAll();
            System.out.println("\n👥 Liste des employés:");
            employes.forEach(e -> System.out.println(
                    "ID: " + e.getId() +
                            " | Nom: " + e.getNom() +
                            " | Solde: " + e.getSoldeConges()
            ));

            // Liste des demandes
            List<Demande_Conge> demandes = demandeService.getAll();
            System.out.println("\n📋 Liste des demandes:");
            demandes.forEach(d -> System.out.println(
                    "ID: " + d.getId() +
                            " | Type: " + d.getTypeConge() +
                            " | Statut: " + d.getStatut()
            ));

            // Liste des validations
            List<Valider_Conge> validations = validationService.getAll();
            System.out.println("\n📝 Liste des validations:");
            validations.forEach(v -> System.out.println(
                    "ID: " + v.getId() +
                            " | Approuvé: " + v.isEstApprouve() +
                            " | Commentaire: " + v.getCommentaire()
            ));

            // =================================================================
            // 5. SUPPRESSION (Décommenter pour tester)
            // =================================================================
            /*
            validationService.delete(validation);
            demandeService.delete(demande);
            employeService.delete(employe);
            System.out.println("\n🗑️ Données supprimées");
            */

        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
