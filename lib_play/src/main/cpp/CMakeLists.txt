cmake_minimum_required(VERSION 3.4.1)

add_library( native-lib
             SHARED
             native-lib.cpp
             audio/audioRecord.cpp
             audio/audioTrack.cpp
             audio/audioShare.hpp
        )

find_library( log-lib
              log )

target_link_libraries( native-lib
                       oboe
                       ${log-lib})

# Set the path to the Oboe directory.
set (OBOE_DIR ../../../../../oboe)

# Add the Oboe library as a subdirectory in your project.
# add_subdirectory tells CMake to look in this directory to
# compile oboe source files using oboe's CMake file.
# ./oboe specifies where the compiled binaries will be stored
add_subdirectory (${OBOE_DIR} ./oboe)

# Specify the path to the Oboe header files.
# This allows targets compiled with this CMake (application code)
# to see public Oboe headers, in order to access its API.
include_directories (${OBOE_DIR}/include)