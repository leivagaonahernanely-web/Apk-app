# Roblox Helper

Aplicación Android con plantillas de scripts en **Luau** para **Roblox Studio**.
Eliges una plantilla, ajustas sus valores (por ejemplo, la velocidad o el daño),
y copias o compartes el código para pegarlo en tu juego.

## Plantillas incluidas

| Plantilla | Para qué sirve | Dónde va el script |
|---|---|---|
| Puerta con botón | Puerta que se abre y cierra con un aviso de proximidad | Dentro de la Part de la puerta |
| Monedas que suman | Recoger monedas y mostrar el marcador en la lista de jugadores | ServerScriptService |
| Lava que daña | Zona que quita vida al tocarla | Dentro de la Part de lava |
| Portal de teletransporte | Lleva al jugador a otra parte del mapa | Dentro de la Part del portal |
| Velocidad al aparecer | Fija la velocidad de caminar al aparecer | ServerScriptService |
| Botón en pantalla | Botón de interfaz que cambia su texto | LocalScript dentro de un ScreenGui |

## Cómo compilar el APK

Requisitos: [Android Studio](https://developer.android.com/studio) (incluye el SDK y JDK 17).

1. Clona el repositorio y ábrelo en Android Studio (`File > Open` y elige la carpeta).
2. Espera a que termine la sincronización de Gradle.
3. Ve a `Build > Build Bundle(s) / APK(s) > Build APK(s)`.
4. El APK queda en `app/build/outputs/apk/debug/app-debug.apk`.

La app no tiene dependencias externas, solo usa el SDK de Android.
Compila para Android 5.0 (API 21) en adelante.

## Estructura

```
app/src/main/java/com/leivagaonahernanely/robloxhelper/
  MainActivity.java       Lista de plantillas
  TemplateActivity.java   Detalle, valores personalizables, copiar y compartir
  RobloxTemplate.java     Modelo de plantilla y sustitución de marcadores
  Templates.java          Catálogo de scripts
```

Para añadir una plantilla, agrega una entrada en `Templates.TODAS`.
Los valores personalizables se escriben en el código como `{{NOMBRE}}`
y se declaran con su valor por defecto como último argumento.

## Aviso

Las plantillas son para crear juegos propios en Roblox Studio. No incluyen
herramientas para alterar o explotar otros juegos, lo cual va contra las
normas de Roblox.
