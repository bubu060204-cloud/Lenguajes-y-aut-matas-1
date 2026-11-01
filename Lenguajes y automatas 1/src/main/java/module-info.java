module com.automatas.tec{
    requires javafx.controls;
    requires javafx.fxml;

    opens com.automatas.tec.Practica_1 to javafx.fxml;
    opens com.automatas.tec.Practica_2 to javafx.fxml;
    opens com.automatas.tec.Practica_3 to javafx.fxml;

    exports com.automatas.tec.Practica_1;
    exports com.automatas.tec.Practica_2;
    exports com.automatas.tec.Practica_3;
}