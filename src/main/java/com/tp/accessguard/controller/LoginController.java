package com.tp.accessguard.controller;

import com.tp.accessguard.HelloApplication;
import com.tp.accessguard.service.AuthService;
import com.tp.accessguard.service.impl.AuthServiceImpl;
import com.tp.accessguard.model.SysUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final AuthService authService = new AuthServiceImpl();

    @FXML
    public void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Optional<SysUser> ou = authService.login(username, password);

        if (ou.isEmpty()) {
            messageLabel.setText("Usuario o contraseña inválidos.");
            return;
        }

        SysUser user = ou.get();

        // Abrir menú principal y pasar usuario logueado
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/com/tp/accessguard/MainMenuView.fxml"));
            Scene scene = new Scene(loader.load());
            MainMenuController controller = loader.getController();
            controller.setLoggedUser(user);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("AccessGuard - Menú Principal");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Error cargando menú principal.");
        }
    }
}
