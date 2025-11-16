package com.tp.accessguard.controller;

import com.tp.accessguard.dao.AccessEventDao;
import com.tp.accessguard.dao.PersonDao;
import com.tp.accessguard.dao.SectorDao;
import com.tp.accessguard.dao.jdbc.JdbcAccessEventDao;
import com.tp.accessguard.dao.jdbc.JdbcPersonDao;
import com.tp.accessguard.dao.jdbc.JdbcSectorDao;
import com.tp.accessguard.service.AccessResult;
import com.tp.accessguard.service.AccessService;
import com.tp.accessguard.service.impl.AccessServiceImpl;
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
            AccessEventDao eventDao = new JdbcAccessEventDao();

            this.service = new AccessServiceImpl(personDao, sectorDao, eventDao);

            if (resultLabel != null) {
                resultLabel.setText("Listo para validar accesos.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (resultLabel != null) {
                resultLabel.setText("Error inicializando servicio: " + e.getMessage());
            }
        }
    }

    @FXML
    public void onValidate() {
        if (service == null) {
            resultLabel.setText("Servicio no disponible.");
            return;
        }

        String badge = badgeField.getText();
        String sector = sectorField.getText();

        AccessResult res = service.checkAccess(badge, sector, LocalDateTime.now());


        String prefix = res.isAllowed() ? "✅ " : "❌ ";
        resultLabel.setText(prefix + res.getMessage());
    }
}

