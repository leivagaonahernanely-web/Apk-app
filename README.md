# Roblox Helper

App Android que usa IA para escribir scripts en **Luau** para **Roblox Studio**.
Describes en español lo que necesitas (por ejemplo, "una puerta que se abra al
pulsar E y se cierre sola a los 3 segundos") y la app te devuelve el código
listo para pegar en tu juego. Puedes copiarlo o compartirlo.

## Configurar la clave API

La app usa la API de Gemini (modelo `gemini-3.5-flash-lite`):

1. Obtén una clave en Google AI Studio.
2. En la app, pulsa **Configurar clave API** y pégala.

La clave se guarda solo en el almacenamiento privado de la app del dispositivo.
No la subas al repositorio ni compartas el APK con la clave ya configurada.

## Cómo compilar el APK

Requisitos: [Android Studio](https://developer.android.com/studio) (incluye el SDK y JDK 17).

1. Clona el repositorio y ábrelo en Android Studio (`File > Open` y elige la carpeta).
2. Espera a que termine la sincronización de Gradle.
3. Ve a `Build > Build Bundle(s) / APK(s) > Build APK(s)`.
4. El APK queda en `app/build/outputs/apk/debug/app-debug.apk`.

No tiene dependencias externas. Compila para Android 5.0 (API 21) o superior.

## Estructura

```
app/src/main/java/com/leivagaonahernanely/robloxhelper/
  MainActivity.java   Pantalla principal, entrada, resultado y clave API
  ApiClient.java      Llamada a la API de Gemini (generateContent)
  CodeCleaner.java    Extrae el código de la respuesta
```

El modelo se cambia en `ApiClient.MODELO`. Las instrucciones que recibe la IA
están en `ApiClient.SISTEMA`.

## Aviso

La app crea scripts para juegos propios en Roblox Studio. No está pensada para
alterar ni explotar otros juegos, lo cual va contra las normas de Roblox.
