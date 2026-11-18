package com.automatas.tec.Practica_3;
//Importación de librerías para control de tablas y columnas, ER, funciones simples y personalización
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
public class    ControllerPractica3 implements Initializable{

    @FXML
    private Button btnIdentificar;
    @FXML
    private Label lbTitulo;
    @FXML
    private Label lbSubtitulo1;
    @FXML
    private Label lbSubtitulo2;
    @FXML
    private Label lbTokens;

    //Tabla y columnas declaradas con <String[]> o <String[], String> para agregar solo valores String desde un arreglo (también String)
    @FXML
    private TableColumn<String[],String> tcPR;
    @FXML
    private TableColumn<String[],String> tcID;
    @FXML
    private TableColumn<String[],String> tcLiteral;
    @FXML
    private TableColumn<String[],String> tcOP;
    @FXML
    private TableColumn<String[],String> tcDEL;
    @FXML
    private TableColumn<String[],String> tcCOM;
    @FXML
    private TableView<String[]> tblHistorial;
    @FXML
    private TextArea taResultados;
    @FXML
    private TextField tfInstruccion;


    private ObservableList<String[]> datosRegistros=FXCollections.observableArrayList();

    private final Set<String> PALABRAS_RESERVADAS=new HashSet<>(Arrays.asList(
            "abstract","assert","boolean","break","byte","case","catch","char","class","const",
            "continue","default","do","double","else","enum","extends","final","finally","float",
            "for","goto","if","implements","import","instanceof","int","interface","long","native",
            "new","package","private","protected","public","return","short","static","strictfp","super",
            "switch","synchronized","this","throw","throws","transient","try","void","volatile","while",
            "String"));

    private int obtenerNumeroToken(String token) {

        String[] palabrasReservadas = {
                "abstract", "boolean", "break", "byte", "byvalue", "case",
                "cast", "catch", "char", "class", "const", "continue",
                "default", "do", "double", "else", "extends", "final",
                "finally", "float", "for", "future", "generic", "goto",
                "if", "implements", "import", "inner", "instanceof", "int",
                "interface", "long", "native", "new", "null", "operator",
                "outer", "package", "private", "protected", "public", "rest",
                "return", "short", "static", "super", "switch", "synchronized",
                "this", "threadsafe", "throw", "transient", "try", "var",
                "void", "while"
        };

        for (int i = 0; i < palabrasReservadas.length; i++) {
            if (token.equals(palabrasReservadas[i])) {
                return i + 1;
            }
        }

        if (token.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            return 57;
        }

        if (token.matches("-?([0-9])+")) return 58;
        if (token.matches("-?([0-9])+L?")) return 59;
        if (token.matches("-?[0-9]*\\.[0-9]+[dD]")) return 60;
        if (token.matches("-?[0-9]*\\.[0-9]+[fF]")) return 61;
        if (token.equals("true") || token.equals("false")) return 62;
        if (token.matches("'.'")) return 63;
        if (token.startsWith("\"") && token.endsWith("\"")) return 64;

        String[] operadores = {"++", "--", "!", "*", "/", "%", "+", "-", "<", ">", "<=", ">=", "==", "!=", "&&", "||", "?"};

        for (int i = 0; i < operadores.length; i++) {
            if (token.equals(operadores[i])) {
                return 65 + i;
            }
        }

        if (token.equals("=")) return 82;

        String[] delimitadores = {"(", ")", "{", "}", "[", "]", ";", ",", "."};

        for (int i = 0; i < delimitadores.length; i++) {
            if (token.equals(delimitadores[i])) {
                return 83 + i;
            }
        }

        if (token.equals("//") || token.startsWith("//")) return 92;
        if (token.equals("/*")) return 93;
        if (token.equals("*/")) return 94;

        return -1;
    }

    //Método main para la interfaz gráfica
    @Override
    public void initialize(URL location,ResourceBundle resources){
        //Configuración de cada columna para que sus registros sean los valores de posiciones específicas de un arreglo X
        tcPR.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[0]));
        tcID.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[1]));
        tcLiteral.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[2]));
        tcOP.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[3]));
        tcDEL.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[4]));
        tcCOM.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[5]));
        //Se establecen los valores del ObservableList a la tabla
        tblHistorial.setItems(datosRegistros);
    }

    //Método para procesar la instrucción y agregarla a la tabla (sí cumple las reglas)
    @FXML
    private void procesarInstruccion(){

        String instruccion=tfInstruccion.getText();

        if(instruccion==null||instruccion.trim().isEmpty()){

            mostrarAlerta("Campo Vacío","Por favor escribe una instrucción de Java.",Alert.AlertType.WARNING);
            return;
        }

        if(!validarCadenaGeneral(instruccion)){

            mostrarAlerta("Error de Sintaxis","La instrucción contiene caracteres o símbolos inválidos.",Alert.AlertType.ERROR);
            return;
        }

        List<String> prList=new ArrayList<>();
        List<String> idList=new ArrayList<>();
        List<String> litList=new ArrayList<>();
        List<String> opList=new ArrayList<>();
        List<String> delList=new ArrayList<>();
        List<String> comList=new ArrayList<>();


        String regexToken="//.*|/\\*[\\s\\S]*?\\*/|\"([^\"\\\\]|\\\\.)*\"|'[^']'|\\b[a-zA-Z_][a-zA-Z0-9_]*\\b|\\d+(?:\\.\\d+)?|[+\\-*/%=<>&!]+|[{}()\\[\\];,\\.]";
        Pattern pattern=Pattern.compile(regexToken);
        Matcher matcher=pattern.matcher(instruccion);
        int totalTokens=0;

        while(matcher.find()){
            String token=matcher.group();
            totalTokens++;

            if(token.startsWith("//")||token.startsWith("/*")){

                if(!comList.contains(token))
                    comList.add(token);
            }
            else if(token.startsWith("\"")||token.startsWith("'")||token.matches("\\d+(?:\\.\\d+)?")||token.equals("true")||token.equals("false")){
                if(!litList.contains(token))
                    litList.add(token);
            }
            else if(PALABRAS_RESERVADAS.contains(token)){
                if(!prList.contains(token))
                    prList.add(token);
            }
            else if(token.matches("[a-zA-Z_][a-zA-Z0-9_]*")){
                if(!idList.contains(token))
                    idList.add(token);
            }
            else if(token.matches("[+\\-*/%=<>&!]+")){
                if(!opList.contains(token))
                    opList.add(token);
            }
            else if(token.matches("[{}()\\[\\];,\\.]")){
                if(!delList.contains(token))
                    delList.add(token);
            }
        }

        if(prList.isEmpty()&&idList.isEmpty()&&litList.isEmpty()&&opList.isEmpty()&&delList.isEmpty()&&comList.isEmpty()){

            mostrarAlerta("Sin Tokens","No se encontraron elementos válidos en la instrucción ingresada.",Alert.AlertType.WARNING);
            return;
        }

        String[] registro=new String[]{
                String.join(", ",prList),String.join(", ",idList),
                String.join(", ",litList),String.join(", ",opList),
                String.join(", ",delList),String.join(", ",comList)};

        datosRegistros.add(registro);

        actualizarResultados(registro,totalTokens);

        tfInstruccion.clear();
    }


    private boolean validarCadenaGeneral(String cadena){
        String regexValida="^[a-zA-Z0-9_\\s\\+\\-\\*/%=<>!&\\|\\(\\)\\{\\}\\[\\];,\\.\"'/]+$";
        return cadena.matches(regexValida);
    }


    private void mostrarAlerta(String titulo,String mensaje,Alert.AlertType tipo){

        Alert alerta=new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }


    private void actualizarResultados(String[] registro, int totalTokens) {

        StringBuilder sb = new StringBuilder();

        sb.append("RESULTADOS DEL ÚLTIMO REGISTRO\n\n");

        String[] titulos = {"PALABRAS RESERVADAS", "IDENTIFICADORES", "LITERALES", "OPERADORES", "DELIMITADORES", "COMENTARIOS"};

        for (int i = 0; i < registro.length; i++) {

            sb.append(titulos[i]).append("\n");

            if (!registro[i].isEmpty()) {

                String[] elementos = registro[i].split(", ");

                for (String elemento : elementos) {

                    int numero = obtenerNumeroToken(elemento);

                    if (numero != -1) {
                        sb.append(numero).append(". ").append(elemento).append("\n");
                    }
                }
            }

            sb.append("\n");
        }

        taResultados.setText(sb.toString());

        if (lbTokens != null) {
            lbTokens.setText("" + totalTokens);
        }
    }
}
