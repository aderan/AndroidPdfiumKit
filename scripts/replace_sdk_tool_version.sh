#!/bin/bash

compileSdkVersion=34
minSdkVersion=21
targetSdkVersion=34

# 查找所有的 build.gradle 文件并替换 compileSdk 版本
find . -name "build.gradle" -type f -exec sed -i "" "s/compileSdk .*/compileSdk $compileSdkVersion/g" {} \;
find . -name "build.gradle" -type f -exec sed -i "" "s/minSdk .*/minSdk $minSdkVersion/g" {} \;
find . -name "build.gradle" -type f -exec sed -i "" "s/targetSdk .*/targetSdk $targetSdkVersion/g" {} \;
