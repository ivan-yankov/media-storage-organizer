# jvm version number: 1, 2, 3, ...
export JVM_VERSION=11

export JVM_XMS=2G
export JVM_XMX=6G
export MAIN_CLASS=yankov.mso.application.Main

# array with application jar files, paths are relative to the project directory
export JARS=("jar/*")

export APPLICATION_NAME=media-storage-organizer
export IS_TERMINAL_APPLICATION=false

# icon file name, relative to the project directory, if empty default icon will be used
export ICON_FILE=icon.png

# application parameters, parameters provided on AppImage run will be appended after this list
export PARAMETERS=""
