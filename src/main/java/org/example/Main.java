package org.example;

import ConnectionDB.DataBaseConnection;
import entités.*;

import java.sql.*;

import Service.*;
import java.util.List;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

       // DataBaseConnection dataBaseConnection = new DataBaseConnection();
        Connection con = DataBaseConnection.getInstance().getConnection();
        System.out.println(con);


//        DataBaseConnection data1=DataBaseConnection.getInstance();
//
//        DataBaseConnection data2=DataBaseConnection.getInstance();
//
//        System.out.println(data1);
//        System.out.println(data2);
//
//        Connection con=DataBaseConnection.getInstance().getCon();
//        Client client = new Client(12, "nouha123",                // nomClient
//                "Ben",                  // prenomClient
//                 "987654321",
//                "nouha.ben@example.com",// emailClient
//                "123 Rue de Tunis",     // adresse
//                "nouha123",             // userName
//                "password123",          // password
//                "2024-11-30"            // dateCreationCompte
//        );
//        ServiceClient serviceClient = new ServiceClient();
//        try {
//            serviceClient.ajouter(client);
//        } catch (SQLException e) {
//            System.out.println(e);
//        }
//
//        try {
//            serviceClient.supprimer(client);
//        } catch (SQLException e) {
//            System.out.println(e);
//        }
//        try {
//            List <Client> clients =serviceClient.getAll();
//            System.out.println(clients);
//        } catch (SQLException e) {
//            System.out.println(e);
//        }


        ServiceLogin serviceLogin = new ServiceLogin();

        String username = "nouha123";
        String password = "password123";

//        try {
//            boolean isLoggedIn = serviceLogin.login(username, password);
//            if (!isLoggedIn) {
//                System.out.println("La connexion a échoué.");
//            }
//            System.out.println("Login Ok .");
//        } catch (SQLException e) {
//            System.out.println("Erreur lors de la tentative de connexion : " + e.getMessage());
//        }

    }
}