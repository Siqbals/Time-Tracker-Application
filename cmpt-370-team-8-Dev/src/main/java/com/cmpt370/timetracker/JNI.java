package com.cmpt370.timetracker;

import javafx.scene.input.KeyCode;

import java.util.Optional;

public class JNI {
    private static Optional<JNI> instance = null;

    /**
     * This class is a singleton since it represents singular access
     * to a C++ DLL. Use this method to gain access to the singleton object
     *
     */
    public static Optional<JNI> get() {
        // Lazy initialization of the Singleton
        if (instance == null) {
            instance = Initialize();
        }

        return instance;
    }

    /**
     * Gets the current idle time in seconds.
     *
     * The current idle time is measured as how long since the last mouse or
     * keyboard input was detected by the OS.  Due to platform constraints,
     * this function cannot measure idle times greater than 49.7 days.
     *
     * @return The current idle time, measured in seconds.
     */
    public native double CurrentIdleTime();

    /**
     * Registers a global hotkey with the underlying OS.
     *
     * JavaFX is not (normally) capable of processing input from outside the JavaFX windows.
     * Instead, this function registers a global hotkey with the OS itself, bypassing JavaFX.
     * Check whether a global hotkey has been pressed by the user by calling QueryHotkeyState.
     * Attempting to register the same hotkey combination twice will fail. Attempting to register
     * too many hotkeys (roughly several thousand) will fail.
     *
     * @param event The key that you would like to register as a hotkey.
     * @return A unique integer ID representing this hotkey, or 0 if an error occurred.
     */
    public native int RegisterHotKey(KeyCode event);

    /**
     * Unregisters a global hotkey with the underlying OS.
     *
     * @param hotkeyID The unique integer ID representing the hotkey you want to unregister.
     * @return True if the hotkey was successfully unregistered, false otherwise.
     */
    public native boolean UnregisterHotKey(int hotkeyID);

    /**
     * Query whether a specific hotkey has been activated since the previous invocation of this method.
     *
     * Normally, you might want to run this function periodically (say, every 300ms) to occasionally
     * check if the user has pressed this hotkey.
     *
     * @param hotkeyID The unique integer ID representing the hotkey you wish to query
     * @return True if the hotkey has been activated by the user, false otherwise.
     */
    public native boolean QueryHotkeyState(int hotkeyID);

    /**
     * Gets the fully qualified path name of the currently focused Application.
     *
     * Note, Windows doesn't always have a notion of a focused Application. For example,
     * when switching between two processes, there may be a brief period where this method
     * returns null, as no focused application exists.
     * @return The fully qualified path name of the currently focused application, or null if an
     *         error occured or if there is no focused application.
     */
    public native String GetFocusedAppPath();


    /**
     * Gets the executable name of the currently focused application.
     *
     * See GetFocusedAppPath for more details.
     * @return The name of the executable file (including file extension such as exe), or null,
     *         if the name cannot be retrieved.
     */
    public String GetFocusedAppExeName() {
        String path = GetFocusedAppPath();
        if (path == null) {
            return null;
        }
        // Windows backslash because windows
        return path.substring(path.lastIndexOf("\\") + 1);
    }

    private native boolean InitializeHotkeyPolling();

    private JNI() {
        if (!InitializeHotkeyPolling()) {
            throw new RuntimeException();
        }
    }

    private static Optional<JNI> Initialize() {
        String platform = System.getProperty("os.name");
        // Check for supported platform. Currently, only windows is supported.
        if (platform.toLowerCase().contains("windows")) {
            // Platform supported!
            // We expect the dll file to be in the native/ directory relative to the jar file
            try {
                System.load(System.getProperty("user.dir") + "/native/TimeTrackerJNI.dll");
            } catch (UnsatisfiedLinkError e) {
                // We couldn't load the dll file, something has gone wrong...
                return Optional.empty();
            }
            try {
                return Optional.of(new JNI());
            } catch (RuntimeException e) {
                // Constructor throws an exception if any DLL initialization functions fail
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}