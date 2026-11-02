# Magmacraft 1.20.1 – Anleitung für den automatischen GitHub Cloud-Build

Mit diesem Paket musst du weder Java noch Gradle auf deinem eigenen PC installieren. GitHub kompiliert die fertige `.jar`-Datei kostenlos in der Cloud.

## Schritt-für-Schritt-Anleitung
1. Entpacke die Datei `Magmacraft_1.20.1_Complete.zip` in einen leeren Ordner.
2. Erstelle auf [github.com](https://github.com) ein neues Repository (z. B. `magmacraft-mod`).
3. Lade den gesamten entpackten Inhalt (inklusive des Ordners `.github`, `src`, `build.gradle`, `settings.gradle` und `TEXTUREN_UEBERSICHT.md`) in dein Repository hoch.
4. Klicke oben in deinem GitHub-Repository auf den Reiter **Actions**.
5. Dort läuft automatisch der Workflow **Build Magmacraft 1.20.1 JAR** an (dauert ca. 2–3 Minuten).
6. Sobald ein grüner Haken erscheint, klicke auf den Durchlauf und lade ganz unten unter **Artifacts** die Datei **Magmacraft-1.20.1-Forge-JAR** herunter.
7. Entpacke das Artifact und lege die enthaltene `magmacraft-1.0.0.jar` zusammen mit **TerraBlender (1.20.1)** in deinen Minecraft `mods`-Ordner!
