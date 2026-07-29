# Aviso Gmail Corporativo (versión de bajo riesgo)

Extensión de Chrome que detecta correo nuevo en Gmail y manda un aviso
**genérico y sin contenido** a tu celular vía ntfy.sh.

## Por qué esta versión reduce el riesgo de filtración
- **No usa la Gmail API.** No pide permisos OAuth, no hay client ID, no
  hay tokens de acceso a tu cuenta.
- **No lee el contenido de tus correos.** Solo observa el número de no
  leídos en el título de la pestaña (`(3) Recibidos - Gmail`) — el mismo
  dato que ya ves en el navegador.
- **El único dato que sale de la red corporativa es un booleano**: "hay
  correo nuevo". Nunca asunto, remitente, ni cuerpo del mensaje.
- No guarda ni transmite nada a servidores propios: solo el push
  genérico a ntfy.sh.

## Lo que se sacrifica a cambio
- **Necesitas la pestaña de Gmail cargada en Chrome** (puede estar
  minimizada o en segundo plano, pero abierta). La versión con API podía
  funcionar sin tener Gmail abierto; esta no.
- No incluye Google Chat en esta versión — detectar mensajes de Chat sin
  tocar contenido es más difícil de garantizar de forma genérica, y se
  dejó fuera a propósito para no reintroducir riesgo.

## Instalación

### 1. App en tu celular
Instala **ntfy** (Android/iOS) y suscríbete a un tema único, ej.
`avisos-gmail-jperez-8271` (evita nombres obvios: cualquiera que sepa el
nombre puede suscribirse).

### 2. Carga la extensión en Chrome
1. Ve a `chrome://extensions`.
2. Activa "Modo desarrollador".
3. "Cargar descomprimida" → selecciona esta carpeta.

### 3. Configura el topic
1. Clic derecho en el ícono de la extensión → "Opciones".
2. Escribe el mismo nombre de topic que usaste en el celular.
3. Guardar.

### 4. Deja Gmail abierto
Abre `mail.google.com` en una pestaña de Chrome y déjala ahí (puede estar
en segundo plano). Listo — en cuanto llegue un correo nuevo, te llega el
aviso al celular.

## Lo que esto NO resuelve
Ninguna versión de este proyecto elimina el riesgo de política interna:
si tu empresa no permite instalar extensiones de Chrome no aprobadas, o
considera que cualquier automatización sobre el correo corporativo debe
pasar por IT, esta versión sigue siendo una extensión no sancionada —
solo que ahora filtra prácticamente cero información si algo sale mal
(el peor caso es que alguien vea "tienes correo nuevo", sin más
contexto). Aun así, lo más seguro para ti es comentarlo con IT antes de
dejarlo corriendo de forma permanente.
