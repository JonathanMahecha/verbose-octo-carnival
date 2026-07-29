const ntfyInput = document.getElementById("ntfyTopic");
const statusEl = document.getElementById("status");

async function load() {
  const data = await chrome.storage.local.get(["ntfyTopic"]);
  ntfyInput.value = data.ntfyTopic || "";
}

document.getElementById("save").addEventListener("click", async () => {
  await chrome.storage.local.set({ ntfyTopic: ntfyInput.value.trim() });
  statusEl.textContent = "Guardado ✅";
  setTimeout(() => (statusEl.textContent = ""), 2000);
});

load();
