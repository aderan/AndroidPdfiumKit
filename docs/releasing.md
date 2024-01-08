Releasing
=========

Cutting a Release
-----------------

1. Update `CHANGELOG.md`.
   > Fix:
   > New: new api, feature, etc.
   > Update: bump dependencies
2. Set versions:

    ```
    export RELEASE_VERSION=X.Y.Z
    ```
3. Update versions:
   ```shell
   sed -i "" \
   "s/\"com.github.aderan.AndroidPdfiumKit:\([^\:]*\):[^\"]*\"/\"com.github.aderan.AndroidPdfiumKit:\1:$RELEASE_VERSION\"/g" \
   README.md README_zh_CN.md
   
   sed -i "" \
   "s/\"com.github.aderan.AndroidPdfiumKit:\([^\:]*\):[^\"]*\"/\"com.github.aderan.AndroidPdfiumKit:\1:$RELEASE_VERSION\"/g" \
   app/build.gradle
    ```
4. Tag the release and push to GitHub.
   ```shell
   git commit -am "Prepare for release $RELEASE_VERSION"
   git tag -a $RELEASE_VERSION -m "Version $RELEASE_VERSION"
   git push && git push --tags
   ```

5. Trigger jitpack build
   ```shell
   curl https://jitpack.io/api/builds/com.github.netless-io/fastboard-android/$RELEASE_VERSION
   ```