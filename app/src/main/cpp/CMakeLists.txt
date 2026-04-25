cmake_minimum_required(VERSION 3.18.1)

project("openwebui-servo")

# Set C++ standard
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Add Servo library (will be compiled from Rust)
add_library(openwebui-servo SHARED
    servo_wrapper.cpp
)

# Find Android libraries
find_library(log-lib log)
find_library(android-lib android)

# Link libraries
target_link_libraries(openwebui-servo
    ${log-lib}
    ${android-lib}
)

# Set output directory
set_target_properties(openwebui-servo PROPERTIES
    LIBRARY_OUTPUT_DIRECTORY
    ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}
)
