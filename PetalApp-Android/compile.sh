#!/bin/bash

echo "🔧 Descargando Gradle Wrapper..."
mkdir -p gradle/wrapper
cd gradle/wrapper

# Descargar Gradle Wrapper JAR
wget -O gradle-wrapper.jar https://github.com/gradle/gradle/raw/v8.4.0/gradle/wrapper/gradle-wrapper.jar

cd ../..

echo "📦 Compilando APK..."
java -jar gradle/wrapper/gradle-wrapper.jar assembleDebug

echo "✅ APK compilada en: app/build/outputs/apk/debug/app-debug.apk"