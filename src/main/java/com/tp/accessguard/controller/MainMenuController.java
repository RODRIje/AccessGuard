package com.tp.accessguard.controller;

import com.tp.accessguard.HelloApplication;
import com.tp.accessguard.model.SysUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML private Button btnPersonas;
    @FXML private Button btnSectores;
    @FXML private Button btnPermisos;
    @FXML private Button btnSimulador;
    @FXML private Button btnReportes;

    private SysUser loggedUser;

    public void setLoggedUser(SysUser user) {
        this.loggedUser = user;
        aplicarPermisosPorRol();
    }

    private void aplicarPermisosPorRol() {
        if (loggedUser == null) {
            return;
        }

        String role = loggedUser.getSystemRole().name(); // "ADMIN", "SECURITY", "AUDITOR"

        switch (role) {
            case "ADMIN" -> {
                // Admin puede todo
                setAllButtons(true);
            }
            case "SECURITY" -> {
                // Seguridad: simulador + ver personas/sectores, quizás sin modificar
                btnSimulador.setDisable(false);
                btnPersonas.setDisable(false);
                btnSectores.setDisable(false);
                btnPermisos.setDisable(false);
                if (btnReportes != null) btnReportes.setDisable(false);
            }
            case "AUDITOR" -> {
                // Auditor: solo reportes (cuando estén) + quizá lectura
                if (btnPersonas != null) btnPersonas.setDisable(true);
                if (btnSectores != null) btnSectores.setDisable(true);
                if (btnPermisos != null) btnPermisos.setDisable(true);
                if (btnSimulador != null) btnSimulador.setDisable(true);
                if (btnReportes != null) btnReportes.setDisable(false);
            }
        }
    }

    private void setAllButtons(boolean enabled) {
        if (btnPersonas != null) btnPersonas.setDisable(!enabled);
        if (btnSectores != null) btnSectores.setDisable(!enabled);
        if (btnPermisos != null) btnPermisos.setDisable(!enabled);
        if (btnSimulador != null) btnSimulador.setDisable(!enabled);
        if (btnReportes != null) btnReportes.setDisable(!enabled);
    }

    @FXML
    private void openPermisos(ActionEvent e) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxml = new FXMLLoader(HelloApplication.class.getResource("/com/tp/accessguard/PermisosView.fxml"));
        stage.setScene(new Scene(fxml.load()));
        stage.setTitle("Gestión de Permisos");
        stage.show();
    }

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

    @FXML
    private void openPermisos() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tp/accessguard/PermisosView.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setTitle("Gestión de Permisos");
        stage.setScene(scene);
        stage.show();
    }

    private void openWindow(String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle(title);
        stage.show();
    }
}

