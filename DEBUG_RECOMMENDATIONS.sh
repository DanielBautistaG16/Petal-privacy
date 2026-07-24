#!/bin/bash

echo "================================================"
echo "PETAL GEMINI DEBUG - Versión Detallada"
echo "================================================"
echo ""
echo "Este script mostrará TODOS los logs de Gemini"
echo ""
echo "PASOS:"
echo "1. Asegúrate de que la app está abierta"
echo "2. Start Service en la app"
echo "3. Di: 'Petal recomiéndame un café'"
echo ""
echo "Buscando:"
echo "  - Request URL"
echo "  - Response code"
echo "  - Response body"
echo "  - Errores de API"
echo ""
echo "Presiona Ctrl+C para detener"
echo "================================================"
echo ""

# Clear previous logs
adb logcat -c

# Show filtered logs with colors
adb logcat | grep --line-buffered -E "GeminiClient|==== GEMINI|Voice command:|API Key configured:|Request URL:|Response code:|Response body:|Error body:|Gemini text content:|Processed online|RecommendationHandler" | while read line; do
    if [[ $line == *"ERROR"* ]] || [[ $line == *"Error"* ]]; then
        echo -e "\033[0;31m$line\033[0m"  # Red
    elif [[ $line == *"Response code: 200"* ]]; then
        echo -e "\033[0;32m$line\033[0m"  # Green
    else
        echo "$line"
    fi
done
