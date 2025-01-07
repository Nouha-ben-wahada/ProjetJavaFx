package Controllers;

import Service.ServiceDeService;
import entités.Categorie;
import entités.Service;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;


public class ServiceController {
    private final ServiceDeService serviceDeService = new ServiceDeService();

    // FXML References
    @FXML private TextField idServiceField;
    @FXML private TextField nomServiceField;
    @FXML private ComboBox<String> categorieComboBox;
    @FXML private TextField durationField;
    @FXML private TextField prixField;
    @FXML private TextField descriptionField;
    @FXML private ListView<Service> serviceListView;  // Liste des services à afficher

    @FXML private TableView<Service> serviceTableView;

    @FXML private TableColumn<Service, Integer> idColumn;
    @FXML private TableColumn<Service, String> nomColumn;
    @FXML private TableColumn<Service, String> descriptionColumn;
    @FXML private TableColumn<Service, Integer> prixColumn;
    @FXML private TableColumn<Service, Integer> durationColumn;
    @FXML private TableColumn<Service, Categorie> categorieColumn;

    @FXML
    public void initialize() {
        // ... initialisation des autres éléments
        afficherServicesTableView();

        // Créer les colonnes du TableView
        TableColumn<Service, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idService"));

        TableColumn<Service, String> nomColumn = new TableColumn<>("Nom");
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomService"));

        TableColumn<Service, Categorie> categorieColumn = new TableColumn<>("Categorie");
        categorieColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));

        TableColumn<Service, Integer> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        TableColumn<Service, Integer> prixColumn = new TableColumn<>("Prix");
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));

        TableColumn<Service, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        serviceTableView.getColumns().addAll(idColumn, nomColumn, categorieColumn, durationColumn, prixColumn, descriptionColumn);
    }

    @FXML
    public void AfficherTousLesServices(ActionEvent event) {
        try {
            List<Service> services = serviceDeService.getAll();
            serviceTableView.setItems(FXCollections.observableArrayList(services));

            // Afficher le TableView (exemple en remplaçant la ListView)
            serviceTableView.setVisible(true);
        } catch (SQLException e) {
            afficherAlerte("Erreur lors de la récupération des services : " + e.getMessage());
        }
    }

    // Vérification de la cohérence des données avec alertes
    private boolean validerService(Service service) {
        boolean isValid = true;

        // Vérifier si le nom du service est non vide
        if (service.getNomService() == null || service.getNomService().trim().isEmpty()) {
            afficherAlerte("Erreur : Le nom du service ne peut pas être vide.");
            isValid = false;
        }

        System.out.println(service.getCategorie().toString().toUpperCase());
        // Vérifier si la catégorie est valide
        try {
            Service.Categorie.valueOf(service.getCategorie().toString().toUpperCase());

        } catch (IllegalArgumentException e) {
            afficherAlerte("Erreur : Catégorie invalide.");
            isValid = false;
        }

        // Vérifier que la durée est positive
        if (service.getDuration() <= 0) {
            afficherAlerte("Erreur : La durée doit être positive.");
            isValid = false;
        }

        // Vérifier que le prix est positif
        if (service.getPrix() <= 0) {
            afficherAlerte("Erreur : Le prix doit être positif.");
            isValid = false;
        }

        // Vérifier si la description est suffisamment longue (facultatif)
        if (service.getDescription().isEmpty())  { /*descriptionField.getText().isEmpty() )*/
            afficherAlerte("Erreur : La description ne peut pas etre vide.");
            isValid = false;
        }

        return isValid;
    }

    // Afficher une alerte (message d'erreur) dans la console et en popup
    private void afficherAlerte(String message) {
        // Affichage dans la console
        System.err.println(message);  // Affiche l'alerte en rouge dans la console

        // Affichage dans une fenêtre popup (JavaFX)
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // Affiche l'alerte dans un popup
    }

    // Ajouter un service
    @FXML
    public void ajouterService(ActionEvent event) {
        try {
            // Récupérer les valeurs des champs
            int idService = Integer.parseInt(idServiceField.getText());
            String nomService = nomServiceField.getText();
            String categorie = categorieComboBox.getValue().toUpperCase(); // Valeur sélectionnée dans le ComboBox
            int duration = Integer.parseInt(durationField.getText());
            int prix = Integer.parseInt(prixField.getText());
            String description = descriptionField.getText();

            // Convertir la catégorie en énumération
            Service.Categorie categorieEnum;

            try {
                categorieEnum = Service.Categorie.valueOf(categorie.toUpperCase());
            } catch (IllegalArgumentException e) {
                afficherAlerte("Erreur : Catégorie invalide.");
                return; // Arrêter si la catégorie est invalide
            }
            System.out.println(categorieEnum);
            // Créer le service
            Service service = new Service(idService, nomService, categorieEnum, duration, prix, description);

            // Valider le service avant l'ajout
            if (!validerService(service)) {
                return; // Arrêter si la validation échoue
            }

            // Ajouter le service
            serviceDeService.ajouter(service);
            afficherServicesTableView(); // Mettre à jour la liste affichée
            showAlert("Succès", "Le service a été ajouté avec succès.", AlertType.INFORMATION);
            resetFields();
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur : Les champs durée et prix doivent être des nombres valides.");
        } catch (SQLException e) {
            afficherAlerte("Erreur lors de l'ajout du service : " + e.getMessage());
        } catch (Exception e) {
            afficherAlerte("Erreur : Vérifiez que tous les champs sont remplis correctement.");
        }
    }


    @FXML
    public void updateService() {
        Service service = serviceTableView.getSelectionModel().getSelectedItem();
        if (service == null) {
            afficherAlerte("Erreur : Aucun service sélectionné pour la mise à jour.");
            return;
        }
            try {
                service.setNomService(nomServiceField.getText());
                service.setCategorie(Service.Categorie.valueOf(categorieComboBox.getValue()));
                service.setDuration(Integer.parseInt(durationField.getText()));
                service.setPrix(Integer.parseInt(prixField.getText()));
                service.setDescription(descriptionField.getText());
            } catch (NumberFormatException e) {
                afficherAlerte("Erreur : Les valeurs numériques doivent être des entiers.");
                return;
            } catch (IllegalArgumentException e) {
                afficherAlerte("Erreur : La catégorie spécifiée n'est pas valide.");
                return;
            }

            // Valider et effectuer la mise à jour
            if (validerService(service)) {
                try {
                    serviceDeService.update(service);
                    afficherServicesTableView();
                    afficherAlerte("Succès : Le service a été mis à jour.");
                    resetFields();
                } catch (SQLException e) {
                    afficherAlerte("Erreur lors de la mise à jour du service : " + e.getMessage());
                }
            }


    }

    // Supprimer un service par ID
    @FXML
    private void supprimerService(ActionEvent event) {
        try {
            int idService = Integer.parseInt(idServiceField.getText());
            if (serviceDeService.existe(idService)) {
                serviceDeService.supprimer(idService);
                afficherServicesTableView();
                afficherAlerte("Succès: Le service a été supprimée ayant l'ID " + idService + " .");
            } else {
                afficherAlerte("Erreur : Le service avec l'ID " + idService + " n'existe pas.");
            }
        } catch (Exception e) {
            afficherAlerte("Erreur lors de la suppression du service : " + e.getMessage());
        }
    }

    // Afficher tous les services dans un TableView
    private void afficherServicesTableView() {
        try {
            List<Service> services = serviceDeService.getAll();
        } catch (SQLException e) {
            afficherAlerte("Erreur lors de la récupération des services : " + e.getMessage());
        }
    }


    // Récupérer un service par ID
    public void getServiceById(int idService) {
        try {
            Service service = serviceDeService.getById(idService);
            if (service != null) {
                System.out.println("Service trouvé : " + service);
            } else {
                afficherAlerte("Erreur : Aucun service trouvé avec l'ID " + idService);
            }
        } catch (SQLException e) {
            afficherAlerte("Erreur lors de la récupération du service : " + e.getMessage());
        }
    }

    // Gestion de la sélection d'un service dans la liste
    @FXML
    public void onSelectService() {
        Service selectedService = serviceListView.getSelectionModel().getSelectedItem();
        if (selectedService != null) {
            // Remplir les champs de saisie avec les données du service sélectionné
            idServiceField.setText(String.valueOf(selectedService.getIdService()));
            nomServiceField.setText(String.valueOf(selectedService.getNomService()));
            categorieComboBox.setValue(selectedService.getCategorie().toString().toUpperCase());
            durationField.setText(String.valueOf(selectedService.getDuration()));
            prixField.setText(String.valueOf(selectedService.getPrix()));
            descriptionField.setText(selectedService.getDescription());
        }
    }

    // Méthode pour afficher tous les services
    @FXML
    public void afficherTousLesServices(ActionEvent event) {
        try {
            // Récupérer tous les services depuis le serviceDeService
            List<Service> services = serviceDeService.getAll();

            // Vérifier si la liste des services est vide
            if (services.isEmpty()) {
                afficherAlerte("Aucun service trouvé.");
            } else {
                // Mettre à jour la ListView avec les services récupérés
                serviceTableView.setItems(FXCollections.observableArrayList(services));
                //serviceListView.setItems(FXCollections.observableArrayList(services));
            }
        } catch (SQLException e) {
            afficherAlerte("Erreur lors de la récupération des services : " + e.getMessage());
        }
    }

    private void resetFields() {
        idServiceField.clear();
        nomServiceField.clear();
        categorieComboBox.getSelectionModel().clearSelection();
        durationField.clear();
        prixField.clear();
        descriptionField.clear();
    }

    // Méthode pour afficher un message de succès
    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
