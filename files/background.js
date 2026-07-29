// ============================================================
// background.js — service worker
// Recibe solo una señal booleana ("hay correo nuevo") del
// content script. Nunca ve asunto, remitente, ni cuerpo de
// ningún correo. Lo único que sale de la red corporativa es
// un push genérico sin contenido.
// ============================================================

const MIN_SECONDS_BETWEEN_PUSHES = 20; // evita ráfagas de avisos duplicados

let lastPushTimestamp = 0;

chrome.runtime.onMessage.addListener((message) => {
  if (message?.type === "newMailSignal") {
    handleNewMailSignal();
  }
});

async function handleNewMailSignal() {
  const now = Date.now();
  if (now - lastPushTimestamp < MIN_SECONDS_BETWEEN_PUSHES * 1000) {
    return; // ya avisamos hace muy poco, evita spam
  }
  lastPushTimestamp = now;

  const { ntfyTopic } = await chrome.storage.local.get(["ntfyTopic"]);
  if (!ntfyTopic) {
    console.warn("Configura tu ntfy topic en las opciones de la extensión.");
    return;
  }

  try {
    await fetch(`https://ntfy.sh/${encodeURIComponent(ntfyTopic)}`, {
      method: "POST",
      headers: { Title: "Gmail corporativo" },
      // Mensaje genérico, sin asunto, remitente ni contenido del correo.
      body: "Tienes correo nuevo sin leer.",
    });
  } catch (err) {
    console.error("Error enviando push a ntfy:", err);
  }
}
