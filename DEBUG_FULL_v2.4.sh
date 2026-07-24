#!/bin/bash

echo "=========================================================="
echo "PETAL v2.4.0 - DEBUG COMPLETO"
echo "=========================================================="
echo ""
echo "Este script muestra TODOS los logs relevantes en tiempo real"
echo ""
echo "INSTRUCCIONES:"
echo "1. Asegúrate de que la app Petal está abierta"
echo "2. Presiona 'Start Service' en la app"
echo "3. Deja este script corriendo"
echo "4. Di un comando de voz (ej: 'Petal tengo hambre')"
echo "5. Observa la salida aquí"
echo ""
echo "LOGS QUE VERÁS:"
echo "  🔵 VoiceProcessor → Qué comando detectó"
echo "  🟡 GeminiClient → Request/Response de la API"
echo "  🟢 RecommendationHandler → Cómo procesa la respuesta"
echo "  🔴 Errores → En rojo"
echo ""
echo "Presiona Ctrl+C para detener"
echo "=========================================================="
echo ""

# Clear previous logs
adb logcat -c 2>/dev/null

# Monitor logs with color coding
adb logcat 2>/dev/null | grep --line-buffered -E "VoiceProcessor|GeminiClient|RecommendationHandler|==== GEMINI|Voice command:|API Key configured:|Request URL:|Request body:|Calling Gemini API|Response code:|Response message:|Response body:|Error body:|Gemini raw response:|Gemini text content:|Cleaned content:|Parsed Gemini response:|Processed online|Processed offline|Processing place recommendation|Processing route recommendation|Place recommendation delivered|Route recommendation delivered" | while IFS= read -r line; do
    # Color coding
    if [[ $line == *"ERROR"* ]] || [[ $line == *"Error"* ]] || [[ $line == *"error"* ]]; then
        # RED for errors
        echo -e "\033[0;31m❌ $line\033[0m"
    elif [[ $line == *"Response code: 200"* ]] || [[ $line == *"SUCCESS"* ]]; then
        # GREEN for success
        echo -e "\033[0;32m✅ $line\033[0m"
    elif [[ $line == *"==== GEMINI REQUEST START ===="* ]]; then
        # CYAN for request start
        echo -e "\033[0;36m🔷 $line\033[0m"
    elif [[ $line == *"Processed online:"* ]]; then
        # YELLOW for online processing
        echo -e "\033[0;33m🟡 $line\033[0m"
    elif [[ $line == *"Processed offline:"* ]]; then
        # MAGENTA for offline processing
        echo -e "\033[0;35m🟣 $line\033[0m"
    elif [[ $line == *"Voice command:"* ]]; then
        # BOLD for voice commands
        echo -e "\033[1m🎤 $line\033[0m"
    elif [[ $line == *"recommendation"* ]]; then
        # GREEN for recommendations
        echo -e "\033[0;32m📍 $line\033[0m"
    else
        # Default
        echo "   $line"
    fi
done
