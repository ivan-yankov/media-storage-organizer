# https://www.booleanworld.com/creating-linux-apps-run-anywhere-appimage/

# preparation
./build.sh
./collect-app-image-files.sh

# detect machine's architecture
export ARCH=$(uname -m)

# get the missing tools if necessary
if [ ! -d ../build ]; then mkdir ../build; fi
if [ ! -x ./appimagetool-$ARCH.AppImage ]; then
  curl -L -o ./appimagetool-$ARCH.AppImage https://github.com/AppImage/AppImageKit/releases/download/continuous/appimagetool-$ARCH.AppImage
  chmod a+x ./appimagetool-$ARCH.AppImage 
fi

./appimagetool-$ARCH.AppImage AppDir
