#!/bin/bash

# Script de diagnóstico para crash en Samsung A25
# Autor: Petal Team
# Fecha: 2026-02-06

echo "=========================================="
echo "DIAGNÓSTICO DE CRASH - PETAL APP"
echo "=========================================="
echo ""

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 1. Verificar que el dispositivo esté conectado
echo -e "${YELLOW}[1/5] Verificando conexión con dispositivo...${NC}"
DEVICE=$(adb devices | grep -w "device" | head -n 1)
if [ -z "$DEVICE" ]; then
    echo -e "${RED}✗ No se detectó ningún dispositivo Android conectado${NC}"
    echo "Por favor:"
    echo "  1. Conecta tu Samsung A25 por USB"
    echo "  2. Habilita 'Depuración USB' en Opciones de Desarrollador"
    echo "  3. Acepta el diálogo de autorización en el teléfono"
    exit 1
fi
echo -e "${GREEN}✓ Dispositivo conectado${NC}"
echo ""

# 2. Obtener información del dispositivo
echo -e "${YELLOW}[2/5] Obteniendo información del dispositivo...${NC}"
MANUFACTURER=$(adb shell getprop ro.product.manufacturer)
MODEL=$(adb shell getprop ro.product.model)
ANDROID_VERSION=$(adb shell getprop ro.build.version.release)
SDK_VERSION=$(adb shell getprop ro.build.version.sdk)

echo "  Fabricante: $MANUFACTURER"
echo "  Modelo: $MODEL"
echo "  Android: $ANDROID_VERSION (SDK $SDK_VERSION)"
echo ""

# 3. Limpiar logs anteriores
echo -e "${YELLOW}[3/5] Limpiando logs anteriores...${NC}"
adb logcat -c
echo -e "${GREEN}✓ Logs limpiados${NC}"
echo ""

# 4. Desinstalar app anterior (si existe)
echo -e "${YELLOW}[4/5] Desinstalando versión anterior de Petal...${NC}"
adb uninstall com.petal.handsfree 2>/dev/null
echo -e "${GREEN}✓ App desinstalada${NC}"
echo ""

# 5. Instalar nueva versión
echo -e "${YELLOW}[5/5] Instalando nueva versión de Petal...${NC}"
APK_PATH="PetalApp-Android/app/build/outputs/apk/debug/app-debug.apk"

if [ ! -f "$APK_PATH" ]; then
    echo -e "${RED}✗ No se encontró el APK en: $APK_PATH${NC}"
    echo ""
    echo "Compilando la app primero..."
    cd PetalApp-Android
    ./gradlew assembleDebug
    cd ..
fi

if [ -f "$APK_PATH" ]; then
    adb install -r "$APK_PATH"
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ App instalada correctamente${NC}"
    else
        echo -e "${RED}✗ Error al instalar la app${NC}"
        exit 1
    fi
else
    echo -e "${RED}✗ No se pudo compilar el APK${NC}"
    exit 1
fi

echo ""
echo "=========================================="
echo "INICIANDO CAPTURA DE LOGS"
echo "=========================================="
echo ""
echo -e "${YELLOW}Por favor:${NC}"
echo "  1. Abre la app Petal en tu Samsung A25"
echo "  2. Si crashea, espera 5 segundos"
echo "  3. Presiona Ctrl+C para detener la captura de logs"
echo ""
echo -e "${GREEN}Esperando logs del crash...${NC}"
echo ""

# Capturar logs en tiempo real y también guardarlos
LOG_FILE="crash_log_$(date +%Y%m%d_%H%M%S).txt"
adb logcat -v time | tee "$LOG_FILE" | grep -E "AndroidRuntime|FATAL|petal|handsfree" --color=always

echo ""
echo -e "${GREEN}✓ Logs guardados en: $LOG_FILE${NC}"
echo ""
echo "Por favor envía este archivo al equipo de desarrollo."
