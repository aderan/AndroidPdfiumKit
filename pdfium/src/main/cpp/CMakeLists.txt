cmake_minimum_required(VERSION 3.22.1)

# Set the path to the current directory
set(LOCAL_PATH ${CMAKE_CURRENT_SOURCE_DIR})

find_path(PDFium_INCLUDE_DIR
        NAMES "fpdfview.h"
        PATHS "${CMAKE_CURRENT_LIST_DIR}"
        PATH_SUFFIXES "include"
        )

## pdfium
add_library(pdfium SHARED IMPORTED)
set_target_properties(pdfium PROPERTIES IMPORTED_LOCATION ${LOCAL_PATH}/lib/${ANDROID_ABI}/libpdfium.so)

# Main JNI library
add_library(jniPdfium SHARED ${LOCAL_PATH}/src/mainJNILib.cpp)

# Use target_compile_definitions instead of add_definitions
target_compile_definitions(jniPdfium PUBLIC -DHAVE_PTHREADS)

# Include directories
target_include_directories(jniPdfium PUBLIC
        ${LOCAL_PATH}/include/
        ${LOCAL_PATH}/include/utils/
        ${LOCAL_PATH}/include/cpp/
        )

# Dependencies
target_link_libraries(
        jniPdfium PUBLIC
        pdfium
        log
        android
        jnigraphics
)