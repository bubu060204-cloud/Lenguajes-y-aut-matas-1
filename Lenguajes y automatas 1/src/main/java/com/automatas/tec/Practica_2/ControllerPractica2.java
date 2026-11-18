package com.automatas.tec.Practica_2;

import javafx.scene.control.Alert;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;
public class ControllerPractica2 implements Initializable{

    String[] tiposDeDatos={"Reglas de ID","Integer","Long","Float","Double","Boolean","Character","String"};


    @FXML
    private Label lbTitulo;
    @FXML
    private Label lbResultado;
    @FXML
    private Button btnComprobar;
    @FXML
    private TextField tfValor;
    @FXML
    private ComboBox<String> cmbOpciones;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        lbResultado.setText("");

        cmbOpciones.setItems(FXCollections.observableArrayList(tiposDeDatos));
    }


    @FXML
    private void comprobar(){

        String opcion=cmbOpciones.getValue();
        String literal=tfValor.getText();

        if(opcion==null||opcion.isBlank()){

            mostrarAlerta("Error","Selección de opción","No se ha seleccionado ninguna opción.");
            return;
        }

        if((literal==null||(literal.isBlank())&&(!opcion.equals("Character")&&!opcion.equals("String")))){

            mostrarAlerta("Advertencia","No hay literal","No se ha ingresado nada.");
            return;
        }
        Pattern patron;
        switch (opcion){

            case "Reglas de ID":{
                patron=Pattern.compile("[a_zA-Z_$][a-zA-Z0-9_$]*");
                break;
            }

            case "Integer":{
                patron=Pattern.compile("-?([0-9])+");
                break;
            }
            case "Long":{
                patron=Pattern.compile("-?([0-9])+L?");
                break;
            }
            case "Float":{
                patron=Pattern.compile("-?[0-9]*\\.[0-9]+[fF]");
                break;
            }
            case "Double":{
                patron=Pattern.compile("-?[0-9]*\\.?[0-9]+[dD]");
                break;
            }
            case "Boolean":{
                patron=Pattern.compile("true|false");
                break;
            }
            case "Character":{
                patron=Pattern.compile(".");
                break;
            }
            case "String":{
                patron=Pattern.compile(".*");
                break;
            }
            default:{

                return;
            }
        }

        Matcher mat=patron.matcher(literal);

        if(mat.matches()){
            lbResultado.setText("La literal/identificador es válid@");
        }
        else{
            lbResultado.setText("La literal/identificador no es válid@");
        }
    }


    private void mostrarAlerta(String titulo,String motivo,String mensaje){

        Alert alerta=new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(motivo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
