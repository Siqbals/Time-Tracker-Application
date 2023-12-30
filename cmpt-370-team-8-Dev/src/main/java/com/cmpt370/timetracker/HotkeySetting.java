package com.cmpt370.timetracker;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import org.controlsfx.control.PropertySheet;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;


enum ValidCodes {
    F1,
    F2,
    F3,
    F4,
    F5,
    F6,
    F7,
    F8,
    F9,
    F10,
    F11,
    F12
}

/**
 * An abstract class representing logic common to all Hotkey Settings
 *
 */
public abstract class HotkeySetting implements PropertySheet.Item {
    public static Set<KeyCode> validCodes = EnumSet.of(KeyCode.F1, KeyCode.F2);
    KeyCode code;
    int registrationID = 0;
    HotkeyHandler hotkeyHandlerRef;

    public HotkeySetting(HotkeyHandler hotkeyHandlerRef, KeyCode code) {
        if (code != null) {
            this.code = code;
            hotkeyHandlerRef.TryRegisterHotkey(code, this.getAction());
        }
        this.hotkeyHandlerRef = hotkeyHandlerRef;
    }

    @Override
    public Class<ValidCodes> getType() {
        return ValidCodes.class;
    }

    @Override
    public String getCategory() {
        return "Hotkeys";
    }

    @Override
    public String getName() {
        return "Start Timer";
    }

    @Override
    public String getDescription() {
        return "The key combination to start the timer";
    }

    @Override
    public Object getValue() {
        return code;
    }

    @Override
    public void setValue(Object o) {

        KeyCode requestedCode;
        switch ((ValidCodes)o) {
            case F1: {
                requestedCode = KeyCode.F1;
                break;
            }
            case F2: {
                requestedCode = KeyCode.F2;
                break;
            }
            case F3: {
                requestedCode = KeyCode.F3;
                break;
            }
            case F4: {
                requestedCode = KeyCode.F4;
                break;
            }
            case F5: {
                requestedCode = KeyCode.F5;
                break;
            }
            case F6: {
                requestedCode = KeyCode.F6;
                break;
            }
            case F7: {
                requestedCode = KeyCode.F7;
                break;
            }
            case F8: {
                requestedCode = KeyCode.F8;
                break;
            }
            case F9: {
                requestedCode = KeyCode.F9;
                break;
            }
            case F10: {
                requestedCode = KeyCode.F10;
                break;
            }
            case F11: {
                requestedCode = KeyCode.F11;
                break;
            }
            case F12: {
                requestedCode = KeyCode.F12;
                break;
            }
            default: {
                return;
            }
        }


        // Bail early if we haven't actually set a keycode
        // This prevents a bug with displaying an error dialog when we shouldnt
        if (requestedCode ==  this.code) {
            return;
        }

        // Try setting the new keycode value
        int res = hotkeyHandlerRef.TryRegisterHotkey(requestedCode, this.getAction());
        if (res == 0) {
            // Couldn't set the value. Either a platform error occured,
            // or the user tried binding one keycode to multiple actions.
            Alert failed = new Alert(Alert.AlertType.ERROR);
            failed.setTitle("ERROR");
            failed.setHeaderText("Failed to set Hotkey");
            failed.setContentText("Did you try to bind a key to two actions?");
            failed.showAndWait();
            return;
        }

        // Make sure we unbind the previous keybind that was assigned to this action
        if (this.registrationID != 0) {
            this.hotkeyHandlerRef.UnregisterHotkey(this.registrationID);
        }

        this.registrationID = res;
        this.code = requestedCode;
    }

    @Override
    public Optional<ObservableValue<? extends Object>> getObservableValue() {
        return Optional.empty();
    }

    /**
     * Dictates what kind of action this event should trigger.
     */
    public abstract HotkeyAction getAction();
}
