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
public class ControllerPractica3 implements Initializable{
    //Declaración de variables para manipulación/definición de datos y personalización de la interfaz
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

    //Creación de un ObservableList para actualizar la tabla en tiempo real
    private ObservableList<String[]> datosRegistros=FXCollections.observableArrayList();
    //Creación de arreglo con 51 palabras reservadas de Java
    //Declarado como Set<String> para mayor optimización en cuanto a búsquedas y final para mantenerlo como constante
    private final Set<String> PALABRAS_RESERVADAS=new HashSet<>(Arrays.asList(
            "abstract","assert","boolean","break","byte","case","catch","char","class","const",
            "continue","default","do","double","else","enum","extends","final","finally","float",
            "for","goto","if","implements","import","instanceof","int","interface","long","native",
            "new","package","private","protected","public","return","short","static","strictfp","super",
            "switch","synchronized","this","throw","throws","transient","try","void","volatile","while",
            "String"));

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
        //Se obtiene la instrucción del TextField
        String instruccion=tfInstruccion.getText();
        //Se verifica que se haya escrito algo
        if(instruccion==null||instruccion.trim().isEmpty()){
            //De no haber nada, se muestra la alerta y se interrumpe el método
            mostrarAlerta("Campo Vacío","Por favor escribe una instrucción de Java.",Alert.AlertType.WARNING);
            return;
        }
        //Validación de estructura general con caracteres permitidos incluyendo guiones bajos y puntos
        //Se usa otro método para todo el análisis general
        if(!validarCadenaGeneral(instruccion)){
            //De no cumplir con al menos una condición, el método devuelve false y muestra la alerta
            mostrarAlerta("Error de Sintaxis","La instrucción contiene caracteres o símbolos inválidos.",Alert.AlertType.ERROR);
            return;
        }

        List<String> prList=new ArrayList<>();
        List<String> idList=new ArrayList<>();
        List<String> litList=new ArrayList<>();
        List<String> opList=new ArrayList<>();
        List<String> delList=new ArrayList<>();
        List<String> comList=new ArrayList<>();

        // Expresión regular para soportar identificadores con "_", llamadas con puntos ".", números y cadenas
        String regexToken="//.*|/\\*[\\s\\S]*?\\*/|\"([^\"\\\\]|\\\\.)*\"|'[^']'|\\b[a-zA-Z_][a-zA-Z0-9_]*\\b|\\d+(?:\\.\\d+)?|[+\\-*/%=<>&!]+|[{}()\\[\\];,\\.]";
        Pattern pattern=Pattern.compile(regexToken);
        Matcher matcher=pattern.matcher(instruccion);
        int totalTokens=0;
        //Ciclo que se repite por cada vez que se haya encontrado una similitud entre la instrucción y la ER general
        while(matcher.find()){
            String token=matcher.group();
            totalTokens++;
            //Comprobaciones para determinar que tipo de "algo" es
            if(token.startsWith("//")||token.startsWith("/*")){
                //De ser identificado y sí no se repite con algo que ya esté en la lista, se agrega el valor a esta
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
        //Validar que existan tokens detectados
        if(prList.isEmpty()&&idList.isEmpty()&&litList.isEmpty()&&opList.isEmpty()&&delList.isEmpty()&&comList.isEmpty()){
            //De tener todas las listas vacías, significa que no hay tokens y que, por lo tanto, no se escribió nada
            mostrarAlerta("Sin Tokens","No se encontraron elementos válidos en la instrucción ingresada.",Alert.AlertType.WARNING);
            return;
        }
        //Creación del registro agrupando elementos múltiples con comas
        String[] registro=new String[]{
                String.join(", ",prList),String.join(", ",idList),
                String.join(", ",litList),String.join(", ",opList),
                String.join(", ",delList),String.join(", ",comList)};
        //Agregar el arreglo al ObservableList para actualizar en tiempo real la tabla
        datosRegistros.add(registro);
        // Actualizar resultados en taResultados y lbTokens
        actualizarResultados(registro,totalTokens);
        // Limpiar el TextField
        tfInstruccion.clear();
    }

    //Método para comprobación general de la cadena ingresada
    private boolean validarCadenaGeneral(String cadena){
        String regexValida="^[a-zA-Z0-9_\\s\\+\\-\\*/%=<>!&\\|\\(\\)\\{\\}\\[\\];,\\.\"'/]+$";
        //Retorna true sí Match encontró una igualdad entre la ER y la cadena
        return cadena.matches(regexValida);
    }

    //Método para mostrar alertas
    private void mostrarAlerta(String titulo,String mensaje,Alert.AlertType tipo){
        //Estructura de la alerta
        Alert alerta=new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    //Método para mostrar los resultados en el TextArea
    //Los parámetros son el arreglo con los tokens separados por grupos y el total de tokens
    private void actualizarResultados(String[] registro,int totalTokens){
        //Creación de la cadena con StringBuilder para mostrarlo correctamente en el TextArea
        StringBuilder sb=new StringBuilder();
        sb.append("RESULTADOS DEL ÚLTIMO REGISTRO\n");
        sb.append("\t1. Palabras Reservadas: ").append(registro[0].isEmpty()?"":registro[0]).append("\n");
        sb.append("\t2. Identificadores: ").append(registro[1].isEmpty()?"":registro[1]).append("\n");
        sb.append("\t3. Literales: ").append(registro[2].isEmpty()?"":registro[2]).append("\n");
        sb.append("\t4. Operadores: ").append(registro[3].isEmpty()?"":registro[3]).append("\n");
        sb.append("\t5. Delimitadores: ").append(registro[4].isEmpty()?"":registro[4]).append("\n");
        sb.append("\t6. Comentarios: ").append(registro[5].isEmpty()?"":registro[5]);
        taResultados.setText(sb.toString());
        if(lbTokens!=null){
            lbTokens.setText(""+totalTokens);
        }
    }
}