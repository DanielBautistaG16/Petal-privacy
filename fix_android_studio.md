# 🔧 Configurar Android Studio Windows para WSL

## Problema:
```
Gradle JVM option is incorrect: 'C:\Program Files\Android\Android Studio\jbr'.
The project is located on WSL(Ubuntu).
Use the JDK installed on the same WSL distribution.
```

## 📋 Solución Paso a Paso:

### 1. **Abrir Android Studio (Windows)**
   - File → Settings (Ctrl+Alt+S)

### 2. **Ir a Gradle Settings:**
   - Build, Execution, Deployment → Build Tools → Gradle

### 3. **Cambiar Gradle JVM:**
   - En "Gradle JVM" cambiar de:
     ```
     C:\Program Files\Android\Android Studio\jbr
     ```
   - A:
     ```
     \\wsl.localhost\Ubuntu\usr\lib\jvm\java-11-openjdk-amd64
     ```
   - O si no existe, usar:
     ```
     \\wsl.localhost\Ubuntu\usr\lib\jvm\default-java
     ```

### 4. **Alternativa - Usar JDK de Windows:**
   - Mover el proyecto de WSL a Windows:
     ```
     C:\Users\TuUsuario\AndroidStudioProjects\PetalApp-Android
     ```

### 5. **Verificar:**
   - File → Sync Project with Gradle Files
   - Build → Clean Project
   - Build → Rebuild Project

## 🚀 Alternativa Más Fácil:

### **Copiar proyecto a Windows:**
```bash
# En WSL, comprimir el proyecto:
cd /home/dani/Petal
tar -czf PetalApp-Windows.tar.gz PetalApp-Android/

# Copiar a Windows:
cp PetalApp-Windows.tar.gz /mnt/c/Users/[TuUsuario]/Desktop/
```

### **En Windows:**
1. Extraer en `C:\Users\[TuUsuario]\AndroidStudioProjects\`
2. Abrir con Android Studio desde ahí
3. Build → Build APK

## ✅ **Esto debería resolver el error de Gradle JVM!**