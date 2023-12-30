cmake_minimum_required(VERSION 3.20)

project(TimeTrackerJNI)

set(CMAKE_CXX_STANDARD 17)

# TODO: Packaging of the native dll with the final built java artifact
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../native/$<0:>)

add_library(TimeTrackerJNI SHARED "src/Hotkeys.cpp"
                                    "src/IdleTime.cpp"
                                    "src/AppTracking.cpp")
set_target_properties(TimeTrackerJNI PROPERTIES
    MSVC_RUNTIME_LIBRARY "MultiThreaded")
target_include_directories(TimeTrackerJNI PUBLIC "include/" "${JDK_PATH}/include" "${JDK_PATH}/include/win32")