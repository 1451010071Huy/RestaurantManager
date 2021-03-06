/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restaurant.manager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Luxury
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(getClass()
//                .getResource("MainFXML.fxml"));
        Parent root = FXMLLoader.load(getClass()
                .getResource("views/LoginFXML.fxml"));
        Scene scene = new Scene(root);

        String css = Main.class.getResource(
                "css/mainfxml.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Đăng nhập");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
