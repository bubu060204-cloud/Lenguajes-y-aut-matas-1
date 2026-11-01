package com.automatas.tec.Practica_2;
//Importación de librerías para alertas, expresiones regulares y herramientas FX
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
    //Declaración e inicialización de arreglo String con tipos de datos y reglas de ID para mostrar como opciones en el ComboBox
    String[] tiposDeDatos={"Reglas de ID","Byte","Short","Integer","Long","Float","Double","Boolean","Character","String"};

    //ID para personalización de la interfaz y manipulación de instrucciones.
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

    //Sobre-escritura del método main de la interfaz
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        lbResultado.setText("");//Se elimina el texto "Mensaje de resultado" del Label
        //Se agregan/establecen todos los valores del arreglo anterior para mostrar como opciones en el ComboBox
        cmbOpciones.setItems(FXCollections.observableArrayList(tiposDeDatos));
    }

    //Método asociado al botón para comprobar la literal o el identificador ingresado
    @FXML
    private void comprobar(){
        //Se obtienen los valores de la opción elegida y de la literal o identificador
        String opcion=cmbOpciones.getValue();
        String literal=tfValor.getText();
        //Se verifica que se haya seleccionado una opción
        if(opcion==null||opcion.isBlank()){
            //De estar vacío se hace un llamado al método para mostrar alertas y que interrumpirá la ejecución del método con return
            mostrarAlerta("Error","Selección de opción","No se ha seleccionado ninguna opción.");
            return;
        }
        //Se verifica que se haya escrito una literal en el TextField
        //Se hace una excepción con char y String en caso de que solo haya espacios vacíos
        if((literal==null||(literal.isBlank())&&(!opcion.equals("Character")&&!opcion.equals("String")))){
            //De estar vacía y tener otra opción en lugar de char o String, se envía otra alerta
            mostrarAlerta("Advertencia","No hay literal","No se ha ingresado nada.");
            return;
        }
        Pattern patron;//Se declara el patrón para las ER
        switch (opcion){
            //Se usa "^" y "$" en las expresiones para mejorar la precisión en Java. Indican el inicio y fin de la ER
            case "Reglas de ID":{
                patron=Pattern.compile("^[a-zA-Z_$][a-zA-Z0-9_$]*$");
                break;
            }
            case "Byte":{
                patron=Pattern.compile("^-?(12[0-7]|1[0-1][0-9]|[1-9]?[0-9])$");
                break;
            }
            case "Short":{
                patron=Pattern.compile("^-?(3276[0-7]|32[0-5][0-9]{2}|3[0-1][0-9]{4}|[1-2][0-9]{4}|[1-9]?[0-9]{1,3})$");
                break;
            }
            case "Integer":{
                patron=Pattern.compile("^-?(214748364[0-7]|21474836[0-3][0-9]|214748[0-5][0-9]{2}|21474[0-8][0-9]{3}|214[0-7][0-9]{4}|21[0-4][0-9]{5}|[1-2][0-9]{6,9}|[1-9][0-9]{0,8}|0)$");
                break;
            }
            case "Long":{
                patron=Pattern.compile("^-?\\d+[lL]?$");
                break;
            }
            case "Float":{
                patron=Pattern.compile("^-?\\d+\\.\\d+[fF]$");
                break;
            }
            case "Double":{
                patron=Pattern.compile("^-?\\d+\\.\\d+([dD])?$");
                break;
            }
            case "Boolean":{
                patron=Pattern.compile("^(true|false)$");
                break;
            }
            case "Character":{
                patron=Pattern.compile("^.$");
                break;
            }
            case "String":{
                patron=Pattern.compile("^.*$");
                break;
            }
            default:{
                //De no coincidir con ninguna opción, simplemente terminar la ejecución del método
                return;
            }
        }
        //Se compara la ER con el ID o la literal
        Matcher mat=patron.matcher(literal);
        //Se comprueba si son iguales o no
        if(mat.matches()){
            lbResultado.setText("La literal/identificador es válid@");
        }
        else{
            lbResultado.setText("La literal/identificador no es válid@");
        }
    }

    //Método para reutilizar el bloque de código que muestra las alertas
    //Los parámetros son las partes que requiere la alerta
    private void mostrarAlerta(String titulo,String motivo,String mensaje){
        //Declaración e inicialización de la alerta con un icono
        Alert alerta=new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);//Título de la ventana
        alerta.setHeaderText(motivo);//Título o encabezado de la alerta
        alerta.setContentText(mensaje);//Mensaje de la alerta
        alerta.showAndWait();//Instrucción para mostrar la alerta
    }
}