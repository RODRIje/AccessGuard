package com.tp.accessguard.controller;

import com.tp.accessguard.dao.PermissionDao;
import com.tp.accessguard.dao.PersonDao;
import com.tp.accessguard.dao.SectorDao;
import com.tp.accessguard.dao.jdbc.JdbcPermissionDao;
import com.tp.accessguard.dao.jdbc.JdbcPersonDao;
import com.tp.accessguard.dao.jdbc.JdbcSectorDao;
import com.tp.accessguard.model.Permission;
import com.tp.accessguard.model.Person;
import com.tp.accessguard.model.Sector;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;

public class PermisosController {
    @FXML
    private ComboBox<Person> personCombo;
    @FXML private ComboBox<Sector> sectorCombo;
    @FXML private DatePicker fromPicker;
    @FXML private DatePicker toPicker;

    @FXML private TableView<Permission> table;
    @FXML private TableColumn<Permission, String> personCol;
    @FXML private TableColumn<Permission, String> sectorCol;
    @FXML private TableColumn<Permission, LocalDateTime> fromCol;
    @FXML private TableColumn<Permission, LocalDateTime> toCol;

    @FXML private Label msgLabel;

    private final PersonDao personDao = new JdbcPersonDao();
    private final SectorDao sectorDao = new JdbcSectorDao();
    private final PermissionDao permissionDao = new JdbcPermissionDao();

    @FXML
    public void initialize() {

        personCombo.setItems(FXCollections.observableArrayList(personDao.findAll()));
        sectorCombo.setItems(FXCollections.observableArrayList(sectorDao.findAll()));

        personCol.setCellValueFactory(new PropertyValueFactory<>("personName"));
        sectorCol.setCellValueFactory(new PropertyValueFactory<>("sectorName"));
        fromCol.setCellValueFactory(new PropertyValueFactory<>("validFrom"));
        toCol.setCellValueFactory(new PropertyValueFactory<>("validTo"));

        refreshTable();
    }

    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(permissionDao.findAll()));
    }

    @FXML
    private void onAdd() {
        Person person = personCombo.getValue();
        Sector sector = sectorCombo.getValue();

        if (person == null || sector == null || fromPicker.getValue() == null) {
            msgLabel.setText("Complete todos los campos obligatorios.");
            return;
        }

        Permission p = new Permission();
        p.setPersonId(person.getId());
        p.setSectorId(sector.getId());
        p.setValidFrom(fromPicker.getValue().atStartOfDay());

        if (toPicker.getValue() != null) {
            p.setValidTo(toPicker.getValue().atStartOfDay());
        }

        permissionDao.insert(p);
        refreshTable();
        msgLabel.setText("Permiso agregado correctamente.");
    }

    @FXML
    private void onDelete() {
        Permission selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            msgLabel.setText("Seleccione un permiso para eliminar.");
            return;
        }

        permissionDao.delete(selected.getId());
        refreshTable();
        msgLabel.setText("Permiso eliminado.");
    }
}
