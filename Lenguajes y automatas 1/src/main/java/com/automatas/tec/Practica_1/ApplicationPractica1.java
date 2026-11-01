package com.automatas.tec.Practica_1;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
public class ApplicationPractica1 extends Application{
    @Override
    public void start(Stage stage)throws IOException{
        FXMLLoader fxmlLoader=new FXMLLoader(ApplicationPractica1.class.getResource("practica_1-view.fxml"));
        Scene scene=new Scene(fxmlLoader.load(),320,240);
        stage.setTitle("Práctica 1");
        stage.setScene(scene);
        stage.show();
    }
}