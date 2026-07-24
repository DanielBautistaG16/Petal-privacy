#!/bin/bash

echo "🔍 Verificando estructura del proyecto..."
PROJECT_PATH="/home/dani/Petal/PetalApp-Android"

echo ""
echo "📁 Archivos raíz:"
ls -la "$PROJECT_PATH" | grep -E "(build\.gradle|settings\.gradle|gradle\.properties)"

echo ""
echo "📁 Archivo app/build.gradle:"
if [ -f "$PROJECT_PATH/app/build.gradle" ]; then
    echo "✅ app/build.gradle existe"
else
    echo "❌ app/build.gradle NO existe"
fi

echo ""
echo "📁 Gradle wrapper:"
ls -la "$PROJECT_PATH/gradle/wrapper/"

echo ""
echo "🔧 Creando versión limpia para Windows..."