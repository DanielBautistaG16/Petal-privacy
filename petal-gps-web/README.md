# PetalGPS Web Edition

Una aplicación web moderna que replica las funcionalidades de la aplicación móvil PetalGPS original desarrollada en B4A (Basic4Android).

## 🚀 Características

### GPS y Navegación
- **Tracking GPS en tiempo real** con coordenadas lat/lon precisas
- **Cálculo de velocidad** en km/h y mph
- **Información de satélites** simulada
- **Integración con Google Maps** para visualización

### Conectividad Bluetooth
- **Conexión a dispositivos Bluetooth** (HC-06, RIEGO2, Petal)
- **Monitoreo de batería** de dispositivos conectados
- **Estado de conexión en tiempo real**
- **Búsqueda automática de dispositivos**

### Grabación de Audio
- **Grabación de audio** con filtros de ruido
- **Visualización de audio en tiempo real**
- **Controles de grabación/detención**
- **Procesamiento de audio básico**

### Interfaz y Navegación
- **Diseño responsive** adaptado a móviles y desktop
- **Interfaz moderna** con gradientes y animaciones
- **Enlaces rápidos** a ChatGPT y sitio web Petal
- **Panel de configuración** personalizable

## 🛠️ Tecnologías Utilizadas

- **HTML5** - Estructura semántica
- **CSS3** - Estilos modernos con gradientes y animaciones
- **JavaScript ES6+** - Lógica de aplicación
- **Web APIs**:
  - Geolocation API (GPS)
  - Web Bluetooth API
  - MediaRecorder API (Audio)
  - Web Audio API (Visualización)

## 📁 Estructura del Proyecto

```
petal-gps-web/
├── index.html          # Página principal
├── styles.css          # Estilos CSS
├── script.js           # Lógica JavaScript
├── assets/             # Recursos (imágenes, iconos)
│   ├── logo-transparent.png
│   └── petal-icon.png
└── README.md           # Documentación
```

## 🎯 Funcionalidades Implementadas

### ✅ Completadas
1. **Interfaz de usuario completa** - Todos los paneles y controles
2. **Sistema GPS** - Tracking, velocidad, coordenadas
3. **Conectividad Bluetooth** - Simulación completa
4. **Grabación de audio** - Con visualización en tiempo real
5. **Configuración** - Persistente en localStorage
6. **Enlaces externos** - Maps, ChatGPT, sitio Petal
7. **Sistema de notificaciones** - Feedback visual
8. **Diseño responsive** - Adaptado a todos los dispositivos

### 🔄 Correspondencia con App Original

| Funcionalidad Original | Implementación Web | Estado |
|------------------------|-------------------|--------|
| GPS tracking | Geolocation API | ✅ |
| Bluetooth HC-06/Petal | Web Bluetooth API | ✅ |
| Audio recording | MediaRecorder API | ✅ |
| Google Maps | Enlaces directos | ✅ |
| Battery monitoring | Simulación realista | ✅ |
| Speed calculation | Conversión m/s → km/h | ✅ |
| Satellite info | Simulación | ✅ |
| ChatGPT link | Enlace directo | ✅ |

## 🚀 Instalación y Uso

1. **Clonar o descargar** los archivos del proyecto
2. **Abrir `index.html`** en un navegador moderno
3. **Permitir permisos** de ubicación y micrófono cuando se soliciten
4. **Disfrutar** de todas las funcionalidades

### Requisitos del Navegador
- **Chrome/Edge 79+** (recomendado para Web Bluetooth)
- **Firefox 72+** (funcionalidades limitadas)
- **Safari 13+** (sin Bluetooth Web API)

## ⚙️ Configuración

La aplicación incluye un panel de configuración accesible donde puedes:

- **Cambiar unidades** de velocidad (km/h ↔ mph)
- **Ajustar intervalo** de actualización GPS
- **Configurar dispositivos** Bluetooth preferidos
- **Guardar configuración** automáticamente

## 🔗 Enlaces Rápidos

- **Google Maps**: Acceso directo a navegación
- **ChatGPT**: Enlace al chat específico del proyecto
- **Petal Website**: Sitio oficial de Petal

## 🎨 Diseño

La aplicación cuenta con:
- **Gradientes modernos** en tonos azules y púrpuras
- **Animaciones suaves** y transiciones
- **Efectos glassmorphism** para un look moderno
- **Iconografía clara** y comprensible
- **Feedback visual** inmediato

## 📱 Compatibilidad Móvil

Totalmente optimizada para dispositivos móviles con:
- **Diseño responsive** que se adapta a cualquier pantalla
- **Touch-friendly** con botones de tamaño adecuado
- **Navegación intuitiva** para uso con una mano
- **Performance optimizada** para dispositivos menos potentes

## 🔒 Privacidad y Permisos

La aplicación solicita únicamente los permisos necesarios:
- **Ubicación**: Para funcionalidad GPS
- **Micrófono**: Para grabación de audio
- **Bluetooth**: Para conectividad con dispositivos

Todos los datos se procesan localmente, sin envío a servidores externos.

## 🎯 Casos de Uso

- **Ciclismo urbano** - Tracking de rutas y velocidad
- **Navegación GPS** - Coordenadas precisas en tiempo real
- **Conectividad IoT** - Conexión con dispositivos Petal
- **Grabación de notas** - Audio durante el viaje
- **Monitoreo de dispositivos** - Estado de batería en tiempo real

## 📈 Mejoras Futuras

- Persistencia de rutas GPS
- Mapas offline
- Más formatos de exportación de audio
- Sincronización en la nube
- Widgets personalizables

---

**Desarrollado como réplica web de la aplicación PetalGPS original**
*Compatible con todos los navegadores modernos*