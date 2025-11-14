package com.tp.accessguard.controller;

import com.tp.accessguard.dao.SectorDao;
import com.tp.accessguard.dao.jdbc.JdbcSectorDao;
import com.tp.accessguard.model.Sector;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class SectoresController {

    @FXML private TableView<Sector> sectorTable;
    @FXML private TableColumn<Sector, String> colName;
    @FXML private TableColumn<Sector, String> colCode;
    @FXML private TableColumn<Sector, String> colActive;

    @FXML private TextField nameField;
    @FXML private TextField codeField;
    @FXML private CheckBox activeCheck;
    @FXML private Label messageLabel;

    private final ObservableList<Sector> data = FXCollections.observableArrayList();
    private SectorDao sectorDao;

    @FXML
    public void initialize() {
        sectorDao = new JdbcSectorDao();

        colName.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getName()));
        colCode.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getCode()));
        colActive.setCellValueFactory(cd ->
                new SimpleStringProperty(cd.getValue().isActive() ? "Sí" : "No")
        );

        loadFromDb();

        sectorTable.setItems(data);

        sectorTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) loadToForm(newSel);
        });

        clearForm();
        messageLabel.setText("Gestión de sectores lista (BD).");
    }

    private void loadFromDb() {
        data.clear();
        try {
            List<Sector> all = sectorDao.findAll();
            data.addAll(all);
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error al cargar sectores: " + e.getMessage());
        }
    }

    private void loadToForm(Sector s) {
        nameField.setText(s.getName());
        codeField.setText(s.getCode());
        activeCheck.setSelected(s.isActive());
    }

    private void clearForm() {
        nameField.clear();
        codeField.clear();
        activeCheck.setSelected(true);
        messageLabel.setText("");
    }

    @FXML
    public void onNew() {
        sectorTable.getSelectionModel().clearSelection();
        clearForm();
        messageLabel.setText("Nuevo sector listo para completar.");
    }

    @FXML
    public void onSave() {
        String name = nameField.getText().trim();
        String code = codeField.getText().trim();
        boolean active = activeCheck.isSelected();

        if (name.isEmpty() || code.isEmpty()) {
            messageLabel.setText("Nombre y código son obligatorios.");
            return;
        }

        Sector selected = sectorTable.getSelectionModel().getSelectedItem();
        try {
            if (selected == null) {
                Sector s = new Sector();
                s.setName(name);
                s.setCode(code);
                s.setActive(active);

                sectorDao.insert(s);
                data.add(s);
                sectorTable.getSelectionModel().select(s);
                messageLabel.setText("Sector agregado (BD).");
            } else {
                selected.setName(name);
                selected.setCode(code);
                selected.setActive(active);

                sectorDao.update(selected);
                sectorTable.refresh();
                messageLabel.setText("Sector actualizado (BD).");
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error al guardar sector: " + e.getMessage());
        }
    }

    @FXML
    public void onDelete() {
        Sector selected = sectorTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Debe seleccionar un sector para eliminar.");
            return;
        }
        try {
            sectorDao.deleteById(selected.getId());
            data.remove(selected);
            clearForm();
            messageLabel.setText("Sector eliminado (BD).");
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error al eliminar sector: " + e.getMessage());
        }
    }

    @FXML
    public void onToggleActive() {
        Sector selected = sectorTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Debe seleccionar un sector.");
            return;
        }
        try {
            selected.setActive(!selected.isActive());
            sectorDao.update(selected);
            sectorTable.refresh();
            activeCheck.setSelected(selected.isActive());
            messageLabel.setText(selected.isActive() ? "Sector activado." : "Sector desactivado.");
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error al cambiar estado: " + e.getMessage());
        }
    }
}

