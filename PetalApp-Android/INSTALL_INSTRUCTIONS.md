# 📱 INSTRUCCIONES DE INSTALACIÓN - Petal Handsfree

## 🚀 **Ruta Exacta para Instalar via Gmail/WhatsApp**

### **PASO 1: Compilar APK (en tu ordenador)**

```bash
# Abrir terminal y ejecutar:
cd /home/dani/Petal/PetalApp-Android

# Si tienes Android Studio instalado:
# Abrir Android Studio > Open Project > Seleccionar esta carpeta
# Build > Build Bundle(s)/APK(s) > Build APK(s)

# Si NO tienes Android Studio:
# Necesitas instalar Android SDK y Gradle
```

---

## **ALTERNATIVA RÁPIDA - Sin compilar**

Como compilar puede ser complejo sin Android Studio, aquí tienes **dos opciones más fáciles**:

### **🔥 Opción A: Usar Android Studio Online**

1. Ve a: **https://ide.goorm.io/** (IDE online gratuito)
2. Crear nuevo contenedor con "Android"
3. Subir tu proyecto
4. Compilar APK desde ahí

### **📦 Opción B: APK Pre-compilada (Recomendada)**

He preparado el código para que puedas enviárselo a alguien con Android Studio:

**Archivo ZIP completo:** `/home/dani/Petal/PetalApp-Android/`

---

## **📧 ENVÍO POR GMAIL/WHATSAPP**

### **Para Gmail:**
```bash
# 1. Comprimir el proyecto:
cd /home/dani/Petal/
zip -r PetalApp-Android.zip PetalApp-Android/

# 2. El archivo estará en:
/home/dani/Petal/PetalApp-Android.zip

# 3. Adjuntar a Gmail y enviar
```

### **Para WhatsApp:**
```bash
# WhatsApp no permite APK, pero sí ZIP
# Mismo proceso que Gmail
# Enviar el ZIP y que alguien lo compile
```

---

## **📱 EN TU MÓVIL (cuando tengas la APK)**

### **1. Preparar el móvil:**
- Ajustes > Seguridad > **Permitir instalación de fuentes desconocidas** ✅
- O: Ajustes > Aplicaciones > **Instalar aplicaciones desconocidas** ✅

### **2. Instalar APK:**
- Descargar APK desde Gmail/WhatsApp
- Tocar el archivo APK
- **Instalar** > **Aceptar permisos**

### **3. Configurar permisos:**
Cuando abras la app por primera vez:
- ✅ **Micrófono** (OBLIGATORIO)
- ✅ **Teléfono/Llamadas** (OBLIGATORIO)  
- ✅ **Ubicación** (para navegación)
- ✅ **Notificaciones** (para servicio)

### **4. Desactivar optimización de batería:**
- Ajustes > Batería > Optimización de batería
- Buscar "Petal Handsfree" 
- Cambiar a **"No optimizar"**

---

## **🎤 PROBAR LA APP**

1. Abrir **Petal Handsfree**
2. Presionar **"Iniciar Servicio"**
3. Escucharás: *"Petal manos libres activo. Te escucho."*
4. **Probar comandos:**
   - *"Batería"* (funciona offline)
   - *"¿Qué hora es?"* (funciona offline)
   - *"Llama a 112"* (TEST - no llamará realmente)
   - *"Ir a Valencia"* (abre Google Maps)

---

## **❗ IMPORTANTE**

- **Tu API Key ya está configurada**: `AIzaSyA2YHSeiSyOhDcagX-p6nK138qYU0dgJao`
- **Necesitas conexión a Internet** para comandos avanzados
- **Habla en español** claramente
- **El servicio se mantiene activo** en segundo plano

---

## **🚨 TROUBLESHOOTING**

**❌ "La app no responde a voz"**
→ Verificar permisos de micrófono

**❌ "No puede hacer llamadas"**  
→ Verificar permisos de teléfono

**❌ "El servicio se cierra"**
→ Desactivar optimización de batería

**❌ "Gemini no responde"**
→ Verificar conexión a Internet

---

**¡Pedalea seguro con Petal! 🚴‍♂️🎤**