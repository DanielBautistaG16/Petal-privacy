#!/bin/bash

API_KEY="AIzaSyBLmyDn8E9Ta8TUYF-_LayylV96K4jkdtU"

echo "=========================================="
echo "TEST: GEMINI API KEY"
echo "=========================================="
echo ""
echo "API Key: ${API_KEY:0:20}..."
echo ""
echo "Testeando conexión a Gemini..."
echo ""

response=$(curl -s -X POST \
  "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$API_KEY" \
  -H 'Content-Type: application/json' \
  -d '{
    "contents": [{
      "parts": [{
        "text": "Responde SOLO con este JSON: {\"intent\":\"RECOMMEND_PLACE\",\"argument\":\"Café de las Horas. Local histórico.|6 minutos|¿Te llevo?\"}"
      }]
    }]
  }')

echo "Respuesta de Gemini:"
echo "$response" | jq . 2>/dev/null || echo "$response"
echo ""

if echo "$response" | grep -q "\"error\""; then
    echo "❌ ERROR: La API key no funciona o hay un problema"
    echo ""
    echo "Posibles causas:"
    echo "  1. API key inválida"
    echo "  2. Quota excedida"
    echo "  3. Proyecto sin acceso a Gemini API"
    echo ""
    echo "Solución:"
    echo "  - Ve a: https://aistudio.google.com/app/apikey"
    echo "  - Verifica que la key existe"
    echo "  - O genera una nueva key"
elif echo "$response" | grep -q "\"candidates\""; then
    echo "✅ SUCCESS: La API key funciona correctamente"
    echo ""
    echo "Contenido generado:"
    echo "$response" | jq -r '.candidates[0].content.parts[0].text' 2>/dev/null || echo "No se pudo extraer el texto"
else
    echo "⚠️  UNKNOWN: Respuesta inesperada"
fi

echo ""
echo "=========================================="
