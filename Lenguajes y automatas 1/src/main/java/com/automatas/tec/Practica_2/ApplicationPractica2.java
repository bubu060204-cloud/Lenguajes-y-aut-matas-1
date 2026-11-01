package com.automatas.tec.Practica_2;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
public class ApplicationPractica2 extends Application{
    @Override
    public void start(Stage stage)throws IOException{
        FXMLLoader fxmlLoader=new FXMLLoader(ApplicationPractica2.class.getResource("practica_2-view.fxml"));
        Scene scene=new Scene(fxmlLoader.load(),680,290);
        stage.setTitle("Práctica 2");
        stage.setScene(scene);
        stage.show();
    }
}