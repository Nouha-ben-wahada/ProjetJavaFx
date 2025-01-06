package ConnectionDB;

//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;

import java.sql.*;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;

public class DataBaseConnection {
    public Connection databaseLink;
    private static DataBaseConnection data;

    private DataBaseConnection() {
        String databaseName = "projetjava";
        String databaseUser = "root";
        String databasePassword = "";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
            System.out.println("connexion établie");
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public  Connection getConnection() {


        return databaseLink;
    }



    public static DataBaseConnection getInstance(){
        if(data == null){
            data = new DataBaseConnection();
        }
        return data;
    }
//    public ObservableList<ShopController.cart> getDataCart(){
//
//        DatabaseConnection connectNow = new DatabaseConnection();
//        Connection connectDB = connectNow.getConnection();
//        ObservableList<ShopController.cart> list= FXCollections.observableArrayList();
//
//        try{
//            PreparedStatement ps = connectDB.prepareStatement("Select * from shoppingcart");
//            ResultSet rs = ps.executeQuery();
//
//            while(rs.next()){
//                list.add(new ShopController.cart(Integer.parseInt(rs.getString("id")),Integer.parseInt(rs.getString("price")),rs.getString("prodDetails"),rs.getString("ProdBrand")));
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        return list;
//    }

}
