// ============================================================
// content.js — corre dentro de la pestaña de Gmail
// Solo lee el título de la pestaña (el mismo número que ya ves
// tú en la pestaña del navegador). Nunca toca el contenido de
// los correos, ni el DOM del cuerpo de mensajes, ni hace
// llamadas a ninguna API de Google.
// ============================================================

let lastCount = null;

function extractUnreadCount(title) {
  // Formato típico: "(3) Recibidos - Gmail" → 3
  // Sin no leídos: "Recibidos - Gmail" → 0
  const match = title.match(/^\((\d+)\)/);
  return match ? parseInt(match[1], 10) : 0;
}

function checkTitle() {
  const count = extractUnreadCount(document.title);

  if (lastCount === null) {
    // Primer chequeo tras cargar la pestaña: solo establece la base,
    // no notifica (evita avisar de correos que ya estaban ahí).
    lastCount = count;
    return;
  }

  if (count > lastCount) {
    // Solo mandamos la señal "hay correo nuevo" — cero contenido.
    chrome.runtime.sendMessage({ type: "newMailSignal" });
  }

  lastCount = count;
}

// El título cambia vía JS dentro de Gmail (SPA), así que observamos
// el <title> con MutationObserver en vez de eventos de navegación.
const titleObserver = new MutationObserver(checkTitle);
const titleEl = document.querySelector("title");
if (titleEl) {
  titleObserver.observe(titleEl, { childList: true, characterData: true, subtree: true });
}

// Chequeo inicial
checkTitle();
