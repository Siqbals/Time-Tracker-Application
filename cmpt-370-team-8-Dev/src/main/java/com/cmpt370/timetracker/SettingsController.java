package com.cmpt370.timetracker;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import org.controlsfx.control.PropertySheet;

import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

public class SettingsController {
    @FXML
    private PropertySheet SettingsSheet;

    Model model;

    public SettingsController(Model model) {
        this.model = model;
    }

    public void SaveData() {
        DataSave.serialize((double)SettingsSheet.getItems().get(0).getValue(), "settings.ser");

        List<KeyCode> keycodes = new ArrayList<KeyCode>();
        keycodes.add((KeyCode)SettingsSheet.getItems().get(1).getValue());
        keycodes.add((KeyCode)SettingsSheet.getItems().get(2).getValue());
        DataSave.serialize(keycodes, "keycodes.ser");
    }

    @FXML
    public void initialize() throws NoSuchFileException {
        Object idleSave = null;
        try {
            idleSave = DataSave.deserialize("settings.ser");
            model.getIdleDetector().setIdleDelay((double)idleSave);
            SettingsSheet.getItems().add(new IdleSetting(model.getIdleDetector()));
        } catch (Exception e) {
            // This must be a first run....

            // Add Setting widgets to our Settings window.
            SettingsSheet.getItems().add(new IdleSetting(model.getIdleDetector()));
        }

        try {
            List<KeyCode> keycodes = DataSave.deserialize("keycodes.ser");
            SettingsSheet.getItems().add(new HotkeyStartTimerSetting(model.getHotkeyHandler(), keycodes.get(0)));
            SettingsSheet.getItems().add(new HotkeyStopTimerSetting(model.getHotkeyHandler(), keycodes.get(1)));
        } catch (Exception e) {
            // This must be a first run....
            // Set keycodes to null
            SettingsSheet.getItems().add(new HotkeyStartTimerSetting(model.getHotkeyHandler(), null));
            SettingsSheet.getItems().add(new HotkeyStopTimerSetting(model.getHotkeyHandler(), null));
        }
    }
}
