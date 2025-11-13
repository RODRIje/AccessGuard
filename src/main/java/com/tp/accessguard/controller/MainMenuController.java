package com.tp.accessguard.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    public void onOpenPersonas(ActionEvent e) throws IOException {
        openWindow("/com/tp/accessguard/PersonasView.fxml", "Gestión de Personal");
    }

    @FXML
    public void onOpenSectores(ActionEvent e) throws IOException {
        openWindow("/com/tp/accessguard/SectoresView.fxml", "Gestión de Sectores");
    }

    @FXML
    public void onOpenSimulador(ActionEvent e) throws IOException {
        openWindow("/com/tp/accessguard/hello-view.fxml", "Simulador de Acceso");
    }

    private void openWindow(String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle(title);
        stage.show();
    }
}

