package com.cmpt370.timetracker;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class HotkeyHandler {
    Parent rootNode;
    // We store a map of hotkeys and what they are bound to.
    Map<KeyCode, Pair<Integer, HotkeyAction>> activeHotkeys = new HashMap<KeyCode, Pair<Integer, HotkeyAction>>();

    public HotkeyHandler(Parent rootNode) {
        // We have to store a reference to a node to be able to interact with the
        // Javafx event system.
        this.rootNode = rootNode;
    }

    /**
     * Attempts to register a hotkey, associating the hotkey with a given action.
     * @param code The physical keycode that we are binding
     * @param action The corresponding action that this keycode will trigger
     * @return A nonzero integer representing the hotkey on success, zero on failure.
     */
    public int TryRegisterHotkey(KeyCode code, HotkeyAction action) {
        // Can't duplicate bind one key to many actions. Fail early if this is attempted.
        if (activeHotkeys.containsKey(code)) {
            return 0;
        }

        // Try to register the hotkey with the native library.
        Integer id = JNI.get().map(singleton -> {
            return singleton.RegisterHotKey(code);
        }).orElse(0);

        // We successfully registered, store the binding in the map
        activeHotkeys.put(code, new Pair(id, action));
        return id;
    }

    /**
     * Unregisters a bound hotkey if it exists.
     * @param registrationID The unique integer ID for a hotkey that was returned by
     *                       TryRegisterHotkey.
     */
    public void UnregisterHotkey(int registrationID) {
        KeyCode code = null;

        // Search for the given hotkey binding by its integer ID
        for (var pair : activeHotkeys.entrySet()) {
            if (pair.getValue().getKey() == registrationID) {
                // We found this binding, unregister the hotkey.
                code = pair.getKey();
                JNI.get().get().UnregisterHotKey(registrationID);
                break;
            }
        }

        // Avoid concurrent removal in for loop by removing entry from the map here.
        if (code != null) {
            activeHotkeys.remove(code);
        }
    }

    /**
     * Call this periodically to check whether the user has triggered a hotkey.
     *
     * This update function sends events to any interested listeners. To handle events from this
     * Update function, you must register a JavaFX event handler that handles events of type HotkeyEvent.
     */
    public void Update() {
        JNI.get().ifPresent(singleton -> {
            activeHotkeys.forEach((code, pair) -> {
                if (singleton.QueryHotkeyState(pair.getKey())) {
                    HotkeyEvent event = new HotkeyEvent(pair.getValue());
                    this.rootNode.fireEvent(event);
                }
            });
        });
    }
}
