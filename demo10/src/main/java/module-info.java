module com.example.demo10 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.demo10 to javafx.fxml;
    exports com.example.demo10;



}