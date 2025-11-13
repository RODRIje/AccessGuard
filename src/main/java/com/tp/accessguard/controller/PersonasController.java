package com.tp.accessguard.controller;

import com.tp.accessguard.dao.PersonDao;
import com.tp.accessguard.dao.jdbc.JdbcPersonDao;
import com.tp.accessguard.model.Person;
import com.tp.accessguard.model.enums.PersonStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class PersonasController {

    @FXML private TableView<Person> personTable;
    @FXML private TableColumn<Person, String> colFullName;
    @FXML private TableColumn<Person, String> colDocumentId;
    @FXML private TableColumn<Person, String> colBadgeId;
    @FXML private TableColumn<Person, String> colStatus;

    @FXML private TextField fullNameField;
    @FXML private TextField documentIdField;
    @FXML private TextField badgeIdField;
    @FXML private ComboBox<PersonStatus> statusCombo;
    @FXML private Label messageLabel;

    private final ObservableList<Person> data = FXCollections.observableArrayList();

    private PersonDao personDao;

    @FXML
    public void initialize() {
        this.personDao = new JdbcPersonDao();

        // Binding de columnas
        colFullName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFullName()));
        colDocumentId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDocumentId()));
        colBadgeId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBadgeId()));
        colStatus.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus().name()));

        // Combo estados
        statusCombo.setItems(FXCollections.observableArrayList(PersonStatus.values()));

        // Cargar desde BD
        loadFromDb();

        personTable.setItems(data);

        personTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                loadPersonToForm(newSel);
            }
        });

        clearForm();
        messageLabel.setText("Gestión de personal lista (BD).");
    }

    private void loadFromDb() {
        data.clear();
        try {
            List<Person> all = personDao.findAll();
            data.addAll(all);
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error al cargar personas: " + e.getMessage());
        }
    }

    private void loadPersonToForm(Person p) {
        fullNameField.setText(p.getFullName());
        documentIdField.setText(p.getDocumentId());
        badgeIdField.setText(p.getBadgeId());
        statusCombo.setValue(p.getStatus());
    }

    private void clearForm() {
        fullNameField.clear();
        documentIdField.clear();
        badgeIdField.clear();
        statusCombo.setValue(PersonStatus.ACTIVE);
        messageLabel.setText("");
    }

    @FXML
    public void onNew() {
        personTable.getSelectionModel().clearSelection();
        clearForm();
        messageLabel.setText("Nuevo registro listo para completar.");
    }

    @FXML
    public void onSave() {
        String fullName = fullNameField.getText().trim();
        String doc = documentIdField.getText().trim();
        String badge = badgeIdField.getText().trim();
        PersonStatus status = statusCombo.getValue();

        if (fullName.isEmpty() || doc.isEmpty() || badge.isEmpty() || status == null) {
            messageLabel.setText("Todos los campos son obligatorios.");
            return;
        }

        Person selected = personTable.getSelectionModel().getSelectedItem();
        try {
            if (selected == null) {
                // Alta nueva
                Person p = new Person();
                p.setFullName(fullName);
                p.setDocumentId(doc);
                p.setBadgeId(badge);
                p.setStatus(status);

                personDao.insert(p);  // inserta en BD y setea ID
                data.add(p);
                personTable.getSelectionModel().select(p);
                messageLabel.setText("Persona agregada correctamente (BD).");
            } else {
                // Modificación
                selected.setFullName(fullName);
                selected.setDocumentId(doc);
                selected.setBadgeId(badge);
                selected.setStatus(status);

                personDao.update(selected); // actualiza BD
                personTable.refresh();
                messageLabel.setText("Persona actualizada (BD).");
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error al guardar: " + e.getMessage());
        }
    }

    @FXML
    public void onDelete() {
        Person selected = personTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Debe seleccionar una persona para eliminar.");
            return;
        }

        try {
            personDao.deleteById(selected.getId());
            data.remove(selected);
            clearForm();
            messageLabel.setText("Persona eliminada (BD).");
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error al eliminar: " + e.getMessage());
        }
    }

    @FXML
    public void onToggleStatus() {
        Person selected = personTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Debe seleccionar una persona.");
            return;
        }
        try {
            if (selected.getStatus() == PersonStatus.ACTIVE) {
                selected.setStatus(PersonStatus.BLOCKED);
                messageLabel.setText("Persona bloqueada (BD).");
            } else {
                selected.setStatus(PersonStatus.ACTIVE);
                messageLabel.setText("Persona desbloqueada (BD).");
            }
            personDao.update(selected);
            personTable.refresh();
            statusCombo.setValue(selected.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error al cambiar estado: " + e.getMessage());
        }
    }
}


