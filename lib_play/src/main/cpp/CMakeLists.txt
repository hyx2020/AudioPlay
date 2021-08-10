cmake_minimum_required(VERSION 3.4.1)

set(APP_SOURCES
        jni_bridge.cpp
        audio/FullDuplexStream.cpp
        audio/LiveEffectEngine.cpp
        audio/AAssetDataSource.cpp
        audio/NDKExtractor.cpp
        )

add_library(audio-lib-hyx
        SHARED
        ${DEBUG_UTILS_SOURCES}
        ${APP_SOURCES}
        )

find_library(log-lib
        log)

set (TARGET_LIBS log android oboe GLESv2)
MESSAGE(STATUS "Using NDK media extractor")
add_definitions(-DUSE_FFMPEG=0)
target_sources(audio-lib-hyx PRIVATE ./audio/NDKExtractor.cpp )
set (TARGET_LIBS ${TARGET_LIBS} mediandk)

target_link_libraries(audio-lib-hyx
        oboe
        ${log-lib}
        ${TARGET_LIBS}
        )

# Set the path to the Oboe directory.
set(OBOE_DIR ../../../../../oboe)
add_subdirectory(${OBOE_DIR} ./oboe-bin)
include_directories(${OBOE_DIR}/include ${OBOE_DIR}/samples/shared)

# Debug utilities
set(DEBUG_UTILS_PATH "${OBOE_DIR}/samples/debug-utils")
set(DEBUG_UTILS_SOURCES ${DEBUG_UTILS_PATH}/trace.cpp)
include_directories(${DEBUG_UTILS_PATH})