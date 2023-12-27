#!/bin/sh

## The script will be replaced with kotlin / python in a moment, here is a simple implementation of it

TGZDIRS=(pdfium-android-arm pdfium-android-arm64 pdfium-android-x86 pdfium-android-x64)
DOWNLOAD_URL_PREFIX="https://github.com/bblanchon/pdfium-binaries/releases/latest/download/"
TEMP_DIR="build/pdfium"
PDFIUM_DEST_DIR="pdfiumandroid/src/main/cpp"

# shellcheck disable=SC2068
for TGZFILE in ${TGZDIRS[@]}; do
  mkdir -p $TEMP_DIR $TEMP_DIR/$TGZFILE
  curl -L $DOWNLOAD_URL_PREFIX$TGZFILE.tgz -o $TEMP_DIR/$TGZFILE.tgz
  tar -xzvf $TEMP_DIR/$TGZFILE.tgz -C $TEMP_DIR/$TGZFILE
done

# pick one platform to copy
cp -r $TEMP_DIR/pdfium-android-arm/include $PDFIUM_DEST_DIR
cp -r $TEMP_DIR/pdfium-android-arm/VERSION $PDFIUM_DEST_DIR

# copy all platform libs
cp -r $TEMP_DIR/pdfium-android-arm/lib/ $PDFIUM_DEST_DIR/lib/armeabi-v7a
cp -r $TEMP_DIR/pdfium-android-arm64/lib/ $PDFIUM_DEST_DIR/lib/arm64-v8a
cp -r $TEMP_DIR/pdfium-android-x86/lib/ $PDFIUM_DEST_DIR/lib/x86
cp -r $TEMP_DIR/pdfium-android-x64/lib/ $PDFIUM_DEST_DIR/lib/x86_64

# rm -rf $TEMP_DIR
