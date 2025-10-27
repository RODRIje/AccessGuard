package com.tp.accessguard.controller;

import com.tp.accessguard.Service.AccessService;
import com.tp.accessguard.Service.impl.AccessServiceImpl;
import com.tp.accessguard.dao.*;
import com.tp.accessguard.dao.jdbc.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;

public class AccessCheckController {

    @FXML private TextField badgeField;
    @FXML private TextField sectorField;
    @FXML private Label resultLabel;

    private AccessService service;

    public AccessCheckController() {

    }

    @FXML
    public void initialize() {
        try {
            PersonDao personDao = new JdbcPersonDao();
            SectorDao sectorDao = new JdbcSectorDao();
            PermissionDao permDao = new JdbcPermissionDao();


            AccessRuleDao ruleDao = new JdbcAccessRuleDao();

            AccessEventDao eventDao = new JdbcAccessEventDao();
            this.service = new AccessServiceImpl(personDao, sectorDao, permDao, ruleDao, eventDao);

            if (resultLabel != null) resultLabel.setText("Listo para validar.");
        } catch (Exception ex) {

            if (resultLabel != null) {
                resultLabel.setText("Error inicializando servicio: " + ex.getClass().getSimpleName());
            }
            ex.printStackTrace();
        }
    }

    @FXML
    public void onValidate() {
        if (service == null) {
            resultLabel.setText("Servicio no disponible.");
            return;
        }
        resultLabel.setText("Validando acceso para: " + badgeField.getText()
                + " en sector " + sectorField.getText() + " @ " + LocalDateTime.now());

    }
}
