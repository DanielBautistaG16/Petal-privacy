#!/bin/bash

echo "=========================================="
echo "COPIAR APK A WINDOWS"
echo "=========================================="
echo ""
echo "Este script copia el APK a tu carpeta de Descargas en Windows"
echo ""

APK_SOURCE="/home/dani/Petal/PetalApp-Android/apk-ready/PetalApp-v2.6.0-FIXED-PROMPT-debug.apk"
APK_DEST="/mnt/c/Users/dani/Downloads/PetalApp-v2.6.0-FIXED-PROMPT-debug.apk"

if [ ! -f "$APK_SOURCE" ]; then
    echo "❌ ERROR: No se encuentra el APK en: $APK_SOURCE"
    exit 1
fi

echo "Copiando APK..."
cp "$APK_SOURCE" "$APK_DEST"

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ SUCCESS: APK copiado a:"
    echo "   C:\\Users\\dani\\Downloads\\PetalApp-v2.6.0-FIXED-PROMPT-debug.apk"
    echo ""
    echo "SIGUIENTE PASO:"
    echo "1. Abre el Explorador de archivos en Windows"
    echo "2. Ve a Descargas"
    echo "3. Envía el APK al móvil por:"
    echo "   - WhatsApp (envíatelo a ti mismo)"
    echo "   - Google Drive"
    echo "   - Email"
    echo "   - Bluetooth"
    echo ""
    echo "4. En el móvil:"
    echo "   - Abre Descargas"
    echo "   - Toca el archivo .apk"
    echo "   - Instalar"
else
    echo ""
    echo "❌ ERROR: No se pudo copiar el APK"
    echo "   Verifica que la ruta existe: C:\\Users\\dani\\Downloads\\"
fi

echo ""
echo "=========================================="
