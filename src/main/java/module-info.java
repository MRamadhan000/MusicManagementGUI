module org.example.tesrouter {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;
    requires jlayer;
    requires mp3agic;


    opens org.example.MusicManagement to javafx.fxml;
    exports org.example.MusicManagement;
}