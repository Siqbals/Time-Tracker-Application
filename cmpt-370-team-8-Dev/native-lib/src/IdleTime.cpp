#include "TTJNI/com_cmpt370_timetracker_JNI.h"

#include <windows.h>

JNIEXPORT jdouble JNICALL Java_com_cmpt370_timetracker_JNI_CurrentIdleTime(JNIEnv *, jobject)
{
    // How many ticks ago did the OS receive an input event?
    LASTINPUTINFO info{};
    info.cbSize = sizeof(LASTINPUTINFO);
    GetLastInputInfo(&info);

    // Get time elapsed between now and last input event
    DWORD elapsed = GetTickCount() - info.dwTime;

    // Convert from ms to seconds
    return elapsed / 1000.0;
}