package com.automatas.tec.Practica_3;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
public class ApplicationPractica3 extends Application{
    @Override
    public void start(Stage stage)throws IOException{
        FXMLLoader fxmlLoader=new FXMLLoader(ApplicationPractica3.class.getResource("practica_3-view.fxml"));
        Scene scene=new Scene(fxmlLoader.load(),1000,500);
        stage.setTitle("Práctica 3");
        stage.setScene(scene);
        stage.show();
    }
}
