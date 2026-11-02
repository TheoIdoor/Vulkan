# Vulkan

Eine Forge-Mod für Minecraft 1.20.1 mit einem seltenen Vulkan-Biom, einem großen Vulkanbauwerk und einem Bosskampf.

## Inhalte

- **Vulkanisches Ödland:** heißes, trockenes Oberwelt-Biom mit dunklem Basalt und Blackstone.
- **Vulkanbauwerk:** seltene, in diesem Biom generierte Vulkanstruktur mit sichtbarem Höhleneingang.
- **Innenräume:** Schmiede mit Blast Furnace, Ambossen und Schmiedetisch, Vorratskammer, Schlafraum sowie eine Bossarena.
- **Boss:** der Magmawächter wartet in der Arena, feuert mit Flammen und setzt Gegner im Nahkampf in Brand.

TerraBlender wird benötigt, damit das Biom in die Oberwelt-Generierung integriert wird. Beide Mods müssen im Forge-Mods-Ordner installiert sein.

## Bauen

Voraussetzung ist Java 17. Unter Windows:

```powershell
.\gradlew.bat build
```

Die Mod-JAR liegt anschließend unter `build\libs`.

Zum Testen des Bioms kann eine Welt mit dem Welt-Seed-Befehl `/locate biome vulkan:volcanic_wastes` genutzt werden.
