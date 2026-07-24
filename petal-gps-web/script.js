// PetalGPS Web Application
class PetalGPS {
    constructor() {
        this.gpsActive = false;
        this.bluetoothConnected = false;
        this.bluetoothConnecting = false;
        this.audioRecording = false;
        this.watchId = null;
        this.mediaRecorder = null;
        this.audioStream = null;
        this.audioContext = null;
        this.analyser = null;
        
        this.settings = {
            speedUnits: 'kmh',
            gpsInterval: 1000,
            bluetoothDevices: ['HC-06', 'RIEGO2', 'Petal']
        };
        
        this.init();
    }
    
    init() {
        this.loadSettings();
        this.setupEventListeners();
        this.updateUI();
        this.setupAudioVisualizer();
    }
    
    setupEventListeners() {
        // GPS Controls
        document.getElementById('start-gps').addEventListener('click', () => this.startGPS());
        document.getElementById('stop-gps').addEventListener('click', () => this.stopGPS());
        
        // Bluetooth Controls
        document.getElementById('bluetooth-switch').addEventListener('change', (e) => {
            if (e.target.checked) {
                this.connectBluetooth();
            } else {
                this.disconnectBluetooth();
            }
        });
        document.getElementById('connect-btn').addEventListener('click', () => this.connectBluetooth());
        
        // Audio Controls
        document.getElementById('record-btn').addEventListener('click', () => this.startRecording());
        document.getElementById('stop-record-btn').addEventListener('click', () => this.stopRecording());
        
        // Settings
        document.getElementById('speed-units').addEventListener('change', (e) => {
            this.settings.speedUnits = e.target.value;
            this.saveSettings();
        });
        
        document.getElementById('gps-interval').addEventListener('change', (e) => {
            this.settings.gpsInterval = parseInt(e.target.value);
            this.saveSettings();
        });
        
        document.getElementById('bt-devices').addEventListener('change', (e) => {
            this.settings.bluetoothDevices = e.target.value.split(',').map(d => d.trim());
            this.saveSettings();
        });
    }
    
    // GPS Functions
    async startGPS() {
        if (!navigator.geolocation) {
            this.showNotification('GPS no disponible en este dispositivo', 'error');
            return;
        }
        
        try {
            // Solicitar permisos
            const permission = await navigator.permissions.query({name: 'geolocation'});
            if (permission.state === 'denied') {
                this.showNotification('Permisos de ubicación denegados', 'error');
                return;
            }
            
            const options = {
                enableHighAccuracy: true,
                timeout: 10000,
                maximumAge: 0
            };
            
            this.watchId = navigator.geolocation.watchPosition(
                (position) => this.updateGPSData(position),
                (error) => this.handleGPSError(error),
                options
            );
            
            this.gpsActive = true;
            this.updateUI();
            this.showNotification('GPS iniciado correctamente', 'success');
            
        } catch (error) {
            this.showNotification('Error al iniciar GPS: ' + error.message, 'error');
        }
    }
    
    stopGPS() {
        if (this.watchId) {
            navigator.geolocation.clearWatch(this.watchId);
            this.watchId = null;
        }
        this.gpsActive = false;
        this.clearGPSData();
        this.updateUI();
        this.showNotification('GPS detenido', 'info');
    }
    
    updateGPSData(position) {
        const coords = position.coords;
        
        // Convertir a formato de minutos (como en la app original)
        const lat = this.convertToMinutes(coords.latitude);
        const lon = this.convertToMinutes(coords.longitude);
        
        // Calcular velocidad (convertir de m/s a km/h como en la app original)
        let speed = coords.speed || 0;
        speed = speed * 3.6; // m/s a km/h
        
        if (this.settings.speedUnits === 'mph') {
            speed = speed * 0.621371; // km/h a mph
        }
        
        // Actualizar UI
        document.getElementById('latitude').textContent = lat;
        document.getElementById('longitude').textContent = lon;
        document.getElementById('speed').textContent = `${speed.toFixed(1)} ${this.settings.speedUnits}`;
        
        // Simular información de satélites (no disponible en web)
        this.updateSatelliteInfo();
    }
    
    convertToMinutes(decimal) {
        const degrees = Math.floor(Math.abs(decimal));
        const minutes = (Math.abs(decimal) - degrees) * 60;
        const mins = Math.floor(minutes);
        const secs = ((minutes - mins) * 60).toFixed(1);
        const direction = decimal >= 0 ? (degrees === Math.floor(decimal) ? 'N' : 'E') : 
                                        (degrees === Math.floor(Math.abs(decimal)) ? 'S' : 'W');
        return `${degrees}°${mins}'${secs}"${direction}`;
    }
    
    updateSatelliteInfo() {
        // Simular información de satélites
        const satelliteCount = Math.floor(Math.random() * 8) + 4; // 4-12 satélites
        document.getElementById('satellites').textContent = satelliteCount;
    }
    
    handleGPSError(error) {
        let message = 'Error de GPS: ';
        switch(error.code) {
            case error.PERMISSION_DENIED:
                message += 'Permisos de ubicación denegados';
                break;
            case error.POSITION_UNAVAILABLE:
                message += 'Información de ubicación no disponible';
                break;
            case error.TIMEOUT:
                message += 'Tiempo de espera agotado';
                break;
            default:
                message += 'Error desconocido';
                break;
        }
        this.showNotification(message, 'error');
    }
    
    clearGPSData() {
        document.getElementById('latitude').textContent = '--°--\'--"';
        document.getElementById('longitude').textContent = '--°--\'--"';
        document.getElementById('speed').textContent = '-- km/h';
        document.getElementById('satellites').textContent = '--';
    }
    
    // Bluetooth Functions
    async connectBluetooth() {
        if (!navigator.bluetooth) {
            this.showNotification('Bluetooth Web API no disponible', 'error');
            return;
        }
        
        try {
            this.bluetoothConnecting = true;
            this.updateUI();
            this.showProgressBar();
            
            // Simular búsqueda de dispositivos
            await this.simulateDeviceDiscovery();
            
            const device = await navigator.bluetooth.requestDevice({
                acceptAllDevices: true,
                optionalServices: ['battery_service']
            });
            
            const server = await device.gatt.connect();
            
            this.bluetoothConnected = true;
            this.bluetoothConnecting = false;
            this.updateUI();
            this.hideProgressBar();
            
            this.showNotification(`Conectado a ${device.name}`, 'success');
            this.simulateBatteryLevel();
            
        } catch (error) {
            this.bluetoothConnecting = false;
            this.bluetoothConnected = false;
            this.updateUI();
            this.hideProgressBar();
            this.showNotification('Error de conexión Bluetooth: ' + error.message, 'error');
        }
    }
    
    disconnectBluetooth() {
        this.bluetoothConnected = false;
        this.bluetoothConnecting = false;
        this.updateUI();
        this.hideProgressBar();
        this.showNotification('Bluetooth desconectado', 'info');
        
        // Limpiar datos de batería
        document.querySelector('.battery-display').textContent = '-- %';
    }
    
    async simulateDeviceDiscovery() {
        // Simular búsqueda de dispositivos como HC-06, RIEGO2, Petal
        return new Promise(resolve => {
            setTimeout(() => {
                console.log('Buscando dispositivos:', this.settings.bluetoothDevices);
                resolve();
            }, 2000);
        });
    }
    
    simulateBatteryLevel() {
        // Simular nivel de batería como en la app original
        const batteryLevel = Math.floor(Math.random() * 100);
        const batteryDisplay = document.querySelector('.battery-display');
        batteryDisplay.textContent = `${batteryLevel} %`;
        
        // Actualizar cada 30 segundos
        setInterval(() => {
            if (this.bluetoothConnected) {
                const newLevel = Math.max(0, batteryLevel - Math.floor(Math.random() * 5));
                batteryDisplay.textContent = `${newLevel} %`;
            }
        }, 30000);
    }
    
    showProgressBar() {
        document.getElementById('progress-bar').style.display = 'block';
    }
    
    hideProgressBar() {
        document.getElementById('progress-bar').style.display = 'none';
    }
    
    // Audio Recording Functions
    async startRecording() {
        try {
            this.audioStream = await navigator.mediaDevices.getUserMedia({ 
                audio: {
                    echoCancellation: true,
                    noiseSuppression: true,
                    autoGainControl: true
                } 
            });
            
            this.mediaRecorder = new MediaRecorder(this.audioStream);
            this.audioRecording = true;
            this.updateUI();
            
            document.getElementById('audio-status').textContent = 'Grabando...';
            
            this.mediaRecorder.start();
            this.startAudioVisualization();
            
            this.showNotification('Grabación iniciada', 'success');
            
        } catch (error) {
            this.showNotification('Error al acceder al micrófono: ' + error.message, 'error');
        }
    }
    
    stopRecording() {
        if (this.mediaRecorder) {
            this.mediaRecorder.stop();
        }
        
        if (this.audioStream) {
            this.audioStream.getTracks().forEach(track => track.stop());
        }
        
        this.audioRecording = false;
        this.updateUI();
        
        document.getElementById('audio-status').textContent = 'Grabación detenida';
        this.stopAudioVisualization();
        
        this.showNotification('Grabación detenida', 'info');
    }
    
    setupAudioVisualizer() {
        const canvas = document.getElementById('audio-visualizer');
        this.canvasContext = canvas.getContext('2d');
    }
    
    startAudioVisualization() {
        if (!this.audioStream) return;
        
        this.audioContext = new (window.AudioContext || window.webkitAudioContext)();
        this.analyser = this.audioContext.createAnalyser();
        const source = this.audioContext.createMediaStreamSource(this.audioStream);
        
        source.connect(this.analyser);
        this.analyser.fftSize = 256;
        
        const bufferLength = this.analyser.frequencyBinCount;
        const dataArray = new Uint8Array(bufferLength);
        
        const canvas = document.getElementById('audio-visualizer');
        const canvasContext = this.canvasContext;
        
        const draw = () => {
            if (!this.audioRecording) return;
            
            requestAnimationFrame(draw);
            
            this.analyser.getByteFrequencyData(dataArray);
            
            canvasContext.fillStyle = '#1a202c';
            canvasContext.fillRect(0, 0, canvas.width, canvas.height);
            
            const barWidth = (canvas.width / bufferLength) * 2.5;
            let barHeight;
            let x = 0;
            
            for (let i = 0; i < bufferLength; i++) {
                barHeight = (dataArray[i] / 255) * canvas.height;
                
                const gradient = canvasContext.createLinearGradient(0, canvas.height - barHeight, 0, canvas.height);
                gradient.addColorStop(0, '#4299e1');
                gradient.addColorStop(1, '#3182ce');
                
                canvasContext.fillStyle = gradient;
                canvasContext.fillRect(x, canvas.height - barHeight, barWidth, barHeight);
                
                x += barWidth + 1;
            }
        };
        
        draw();
    }
    
    stopAudioVisualization() {
        if (this.audioContext) {
            this.audioContext.close();
        }
        
        // Limpiar canvas
        const canvas = document.getElementById('audio-visualizer');
        this.canvasContext.fillStyle = '#1a202c';
        this.canvasContext.fillRect(0, 0, canvas.width, canvas.height);
    }
    
    // UI Update Functions
    updateUI() {
        // GPS buttons
        document.getElementById('start-gps').disabled = this.gpsActive;
        document.getElementById('stop-gps').disabled = !this.gpsActive;
        
        // Bluetooth UI
        const statusText = document.getElementById('status-text');
        const statusDot = document.getElementById('status-dot');
        const bluetoothSwitch = document.getElementById('bluetooth-switch');
        const connectBtn = document.getElementById('connect-btn');
        
        bluetoothSwitch.checked = this.bluetoothConnected;
        connectBtn.disabled = this.bluetoothConnected || this.bluetoothConnecting;
        
        if (this.bluetoothConnected) {
            statusText.textContent = 'Conectado';
            statusDot.className = 'status-dot connected';
        } else if (this.bluetoothConnecting) {
            statusText.textContent = 'Conectando...';
            statusDot.className = 'status-dot connecting';
        } else {
            statusText.textContent = 'Desconectado';
            statusDot.className = 'status-dot disconnected';
        }
        
        // Audio buttons
        document.getElementById('record-btn').disabled = this.audioRecording;
        document.getElementById('stop-record-btn').disabled = !this.audioRecording;
    }
    
    // External Links (like original app)
    openGoogleMaps() {
        window.open('https://www.google.com/maps/@?api=1&map_action=map&hl=es-419', '_blank');
    }
    
    openChatGPT() {
        window.open('https://chatgpt.com/share/678fb915-d3d0-8006-ac60-aeb54a0efa2e', '_blank');
    }
    
    openPetalWebsite() {
        window.open('https://ridepetal.com/', '_blank');
    }
    
    // Settings Functions
    toggleSettings() {
        const panel = document.getElementById('settings-panel');
        panel.classList.toggle('hidden');
    }
    
    saveSettings() {
        localStorage.setItem('petalGpsSettings', JSON.stringify(this.settings));
        this.showNotification('Configuración guardada', 'success');
    }
    
    loadSettings() {
        const saved = localStorage.getItem('petalGpsSettings');
        if (saved) {
            this.settings = { ...this.settings, ...JSON.parse(saved) };
        }
        
        // Aplicar configuración a la UI
        document.getElementById('speed-units').value = this.settings.speedUnits;
        document.getElementById('gps-interval').value = this.settings.gpsInterval;
        document.getElementById('bt-devices').value = this.settings.bluetoothDevices.join(', ');
    }
    
    // Notification System
    showNotification(message, type = 'info') {
        // Crear elemento de notificación
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;
        
        // Estilos
        notification.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 15px 20px;
            border-radius: 8px;
            color: white;
            font-weight: 600;
            z-index: 10000;
            opacity: 0;
            transform: translateX(100%);
            transition: all 0.3s ease;
            max-width: 300px;
        `;
        
        // Colores según tipo
        const colors = {
            success: '#48bb78',
            error: '#f56565',
            info: '#4299e1',
            warning: '#ed8936'
        };
        
        notification.style.backgroundColor = colors[type] || colors.info;
        
        document.body.appendChild(notification);
        
        // Animar entrada
        setTimeout(() => {
            notification.style.opacity = '1';
            notification.style.transform = 'translateX(0)';
        }, 100);
        
        // Remover después de 3 segundos
        setTimeout(() => {
            notification.style.opacity = '0';
            notification.style.transform = 'translateX(100%)';
            setTimeout(() => {
                document.body.removeChild(notification);
            }, 300);
        }, 3000);
    }
}

// External Functions (accessible from HTML)
function openGoogleMaps() {
    app.openGoogleMaps();
}

function openChatGPT() {
    app.openChatGPT();
}

function openPetalWebsite() {
    app.openPetalWebsite();
}

function toggleSettings() {
    app.toggleSettings();
}

function saveSettings() {
    app.saveSettings();
}

// Initialize app when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.app = new PetalGPS();
});