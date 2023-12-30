#include "TTJNI/com_cmpt370_timetracker_JNI.h"
#include <jni.h>

#include <windows.h>
#include <iostream>
#include <string>

JNIEXPORT jstring JNICALL Java_com_cmpt370_timetracker_JNI_GetFocusedAppPath(JNIEnv *env, jobject self)
{
    HWND foregroundHandle = GetForegroundWindow();

    DWORD procID = 0;
    DWORD res = GetWindowThreadProcessId(foregroundHandle, &procID);
    if (res == 0)
    {
        return NULL; // Failure
    }

    HANDLE procHandle = OpenProcess(PROCESS_QUERY_LIMITED_INFORMATION, FALSE, procID);

    // Windows knows exactly how long this string is, but refuses to tell us,
    // because windows wants to make our lives miserable at every opportunity.
    // Instead, we have to keep calling this function over and over with larger buffer
    // sizes until windows lets us know that yes, the buffer is finally large enough
    // to copy the data into
    std::wstring storage;
    // We start by setting the size of the buffer to the generous MAX_PATH size.
    // Note however that MAX_PATH isn't actually the real MAX_PATH, because windows
    // wants to make our lives miserable at every opportunity.
    DWORD len = MAX_PATH;
    storage.resize(len);

    while (QueryFullProcessImageNameW(procHandle, 0, storage.data(), &len) == ERROR_INSUFFICIENT_BUFFER)
    {
        len = len * 2;
        storage.resize(len);
    }

    // We finally successfully copied the string. Now shrink to fit what was copied.
    storage.resize(len);

    // Convert to a managed Java String
    return env->NewString((const jchar *)storage.data(), storage.length());
}