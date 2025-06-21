module org.example.juego {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;
    requires org.json;
    requires java.xml;

    opens org.example.juego to javafx.fxml;
    exports org.example.juego;
    exports org.example.juego.controlador;
    opens org.example.juego.controlador to javafx.fxml;
}