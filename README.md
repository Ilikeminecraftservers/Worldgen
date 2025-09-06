# HybridSurvivalPlugin

**Minecraft Plugin für fortschrittliche Welt- und Gebäude-Generierung**

---

## Features

### Terrain
- Kombination aus **Arnis Terrain** und **TerraPlusMinus** für realistische Landschaften
- Unterstützt **32×32 Chunks** pro Zone
- **4k Blöcke Höhe** möglich
- **Progressive Detail Levels** optional über Config

### Gebäude & Immobilien
- Automatisch generierte Gebäude in Städten
- Jede Immobilie hat eine **Adresse**
- Kauf über **Schild-Interaktion** oder Command (`/building buy <Adresse>`)
- Verkauf und Übertragung möglich (`/building sell <Adresse>`)
- Teleport zu eigenem Gebäude (`/building teleport <Adresse>`)
- Eigentümerrechte: Nur der Eigentümer darf bauen oder betreten
- Optional: GUI-Liste aller Gebäude

### Interiors & Shops
- Spieler bauen eigene Interiors und Shops
- OSM-Interiors optional, können aktiviert werden

### Optimierung & Performance
- **Chunk Preload**: Lädt Chunks vor Spielerbewegung
- **Lazy Light**: Lichtberechnung verzögert, reduziert Lag
- **RPC & Paste Threads**: Asynchrone Generierung für Performance
- **Chunk Cache**: Zwischenspeicherung bereits generierter Chunks
- **Prefetch Spawn**: Spawnpunkt-Chunks werden vorgeladen
- Alle Optimierungen **ein- oder ausschaltbar via Config**

---

## Installation

1. Kopiere `HybridSurvivalPlugin.jar` in den `plugins/` Ordner deines Servers.
2. Starte den Server einmal, um die **Config-Dateien** zu erzeugen.
3. Passe `config.yml` nach deinen Bedürfnissen an.
4. Starte den Server neu.

---

## Commands

| Command | Beschreibung |
|---------|-------------|
| `/building buy <Adresse>` | Kauf eines Gebäudes |
| `/building sell <Adresse>` | Verkauf eines Gebäudes |
| `/building info <Adresse>` | Anzeige von Besitzer & Status |
| `/building teleport <Adresse>` | Teleport zum eigenen Gebäude |

---

## Konfiguration

Beispiel `config.yml`:

```yaml
progressive-detail: true
chunk-preload: true
lazy-light: true
rpc-threading: true
paste-threading: true
generate-buildings: true
building-purchase: true
sign-purchase: true
osm-interiors: false
shop-system: false
synchronize-height: true
teleport-to-building: true
command-purchase: true
building-sell: true
gui-building-list: true
max-chunks-per-tick: 16
lights-per-tick: 2
preload-radius: 16
async-events: true
telemetry: true
chunk-cache: true
prefetch-spawn: true
