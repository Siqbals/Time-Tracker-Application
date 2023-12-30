#include "TTJNI/com_cmpt370_timetracker_JNI.h"

#include <windows.h>
#include <optional>
#include <map>
#include <iostream>
#include <thread>

// Each hotkey registered uses a unique identifier that is never recycled.
// Therefore this variable simply gets incremented every time a hotkey is registered.
int atomIncrementer = 1;

// Stores the pressed state of every registered hotkey.
std::map<int, bool> HOTKEY_STATES;

HWND HOTKEY_DUMMY_WINDOW = NULL;

// This is our dummy window event loop. It simply exists to retrieve any hotkey
// events sent to this dummy window, so that we can update HOTKEY_STATES
LRESULT HotkeyCallback(
    HWND window,
    UINT msg,
    WPARAM wParam,
    LPARAM lParam)
{
    switch (msg)
    {
    case WM_HOTKEY:
    {
        HOTKEY_STATES[(int)wParam] = true;
    }
    default:
    {
        // We are uninterested in everything else, just pass it on
        return DefWindowProc(window, msg, wParam, lParam);
        break;
    }
    }
}

// Converts a JavaFX KeyCode into a Win32 virtual key code. Returns an empty option
// if an error occurs.
std::optional<int> vkCodeFromKeyCode(JNIEnv *jenv, jobject keyCode)
{
    // Look up the KeyCode class
    jclass keyCodeClass = jenv->FindClass("javafx/scene/input/KeyCode");
    if (keyCodeClass == NULL)
    {
        return std::nullopt;
    }

    // Now we retrieve the underlying windows vk keycode
    jmethodID getRawKeyCodeFn = jenv->GetMethodID(keyCodeClass, "getCode", "()I");
    int vk = jenv->CallIntMethod(keyCode, getRawKeyCodeFn);
    return vk;
}

// Parses which modifier keys were pressed down during a KeyEvent
// Returns an empty option if an error occurs.
std::optional<UINT> ParseModifiers(JNIEnv *jenv, jobject keyEvent)
{
    UINT modifiers = MOD_NOREPEAT;
    // Lookup the KeyEvent class
    jclass keyEventClass = jenv->FindClass("javafx/scene/input/KeyEvent");
    if (keyEventClass == NULL)
    {
        return std::nullopt;
    }

    // Get Method IDs for parsing each modifier
    jmethodID altDownFn = jenv->GetMethodID(keyEventClass, "isAltDown", "()Z");
    jmethodID controlDownFn = jenv->GetMethodID(keyEventClass, "isControlDown", "()Z");
    jmethodID shiftDownFn = jenv->GetMethodID(keyEventClass, "isShiftDown", "()Z");
    jmethodID metaDownFn = jenv->GetMethodID(keyEventClass, "isMetaDown", "()Z");
    if (altDownFn == NULL)
    {
        return std::nullopt;
    }

    // Which modifiers were actually held down during this event?
    if (jenv->CallBooleanMethod(keyEvent, altDownFn))
    {
        modifiers |= MOD_ALT;
    }
    if (jenv->CallBooleanMethod(keyEvent, controlDownFn))
    {
        modifiers |= MOD_CONTROL;
    }
    if (jenv->CallBooleanMethod(keyEvent, shiftDownFn))
    {
        modifiers |= MOD_SHIFT;
    }
    if (jenv->CallBooleanMethod(keyEvent, metaDownFn))
    {
        modifiers |= MOD_WIN;
    }

    return modifiers;
}

JNIEXPORT jboolean JNICALL Java_com_cmpt370_timetracker_JNI_QueryHotkeyState(JNIEnv *, jobject, jint id)
{
    // Look up the saved state. If we received a WM_HOTKEY event from Win32,
    // this map will let us know
    bool state = HOTKEY_STATES[id];
    // After every attempt to query a hotkey state, we have to remember to
    // wipe the state back to "unactivated"
    HOTKEY_STATES[id] = false;

    return state;
}

JNIEXPORT jboolean JNICALL Java_com_cmpt370_timetracker_JNI_InitializeHotkeyPolling(JNIEnv *, jobject)
{
    // Construct a dummy window that will never appear to the user. It exists only to
    // register a window callback so that we can access global hotkey events

    // Normally I would just register a hotkey with NULL as the HWND, which would just
    // post the events to the message queue and I could pull them out with PeekMessage.
    // With JavaFX, this does not work, presumably because the JavaFX Application thread
    // consumes all messages in the message queue before I can call PeekMessage,
    // resulting in this DLL never getting the message, because JavaFX consumed it.
    // If instead I associate hotkey events with a specific HWND, JavaFX's message pump
    // will helpfully forward the event to my callback function.
    WNDCLASSW windowClass = {};
    windowClass.lpfnWndProc = HotkeyCallback;
    windowClass.lpszClassName = L"HotkeyDummyClass";
    windowClass.hInstance = GetModuleHandleW(NULL);
    RegisterClassW(&windowClass);
    if (HOTKEY_DUMMY_WINDOW != NULL)
    {
        // Something has gone terribly wrong. This function should only ever be called
        // once!
        return false;
    }

    // Create the dummy window
    HOTKEY_DUMMY_WINDOW = CreateWindowExW(0, windowClass.lpszClassName,
                                          L"", 0, 0, 0, 0, 0, NULL, NULL,
                                          GetModuleHandleW(NULL), NULL);
    return HOTKEY_DUMMY_WINDOW != NULL;
}

JNIEXPORT jint JNICALL Java_com_cmpt370_timetracker_JNI_RegisterHotKey(JNIEnv *jenv, jobject thisObj, jobject keyCode)
{
    // Generate new hotkey ID
    LPTSTR idStr = MAKEINTATOM(atomIncrementer++);
    int id = AddAtom(idStr);

    // Get the underlying Win32 keycode
    auto vkCodeRes = vkCodeFromKeyCode(jenv, keyCode);
    if (!vkCodeRes.has_value())
    {
        return 0;
    }
    int vkCode = vkCodeRes.value();

    bool res = RegisterHotKey(HOTKEY_DUMMY_WINDOW, id, 0, vkCode);
    HOTKEY_STATES[id] = false;

    return id;
}

JNIEXPORT jboolean JNICALL Java_com_cmpt370_timetracker_JNI_UnregisterHotKey(JNIEnv *, jobject, jint id)
{
    return (bool)UnregisterHotKey(HOTKEY_DUMMY_WINDOW, id);
}