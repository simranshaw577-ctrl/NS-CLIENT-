# NS CLIENT 1.21.1 — source prototype v0.3.0

Original lightweight Fabric client prototype for Minecraft Java 1.21.1, intended to be tested on Android Mojo Launcher.

## Built-in menu (Right Shift)
- HUD Editor (master HUD on/off)
- FPS Counter
- Coordinates (X/Y/Z)
- Armor Status (equipped armor count)
- Potion Status (active effect count and up to two effect names/timers)
- Settings and Back buttons
- Visual Mods Settings screen with ON/OFF toggles and percentage sliders for Low Shield (default 50%), Low Fire (default 25%), and Small Totem Pop (default 35%)
- Settings persist in the Minecraft instance `config/nsclient.properties` file

The custom loading splash displays `NS CLIENT` and `Nimrit Love Shivam` in the lower-left corner. The visual-mod settings UI and persistence are included, but the actual shield/fire/totem rendering transformations are not yet wired into the game rendering hooks.

## Build
Requires JDK 21, Gradle 8.x, and internet access to resolve Fabric/Minecraft build dependencies. From this directory run `gradle build`. Install the remapped JAR from `build/libs/` whose filename does **not** contain `-dev` into the target instance's `mods` folder.

## Important status
This is source code, not a ready-to-install JAR. It has not been compiled or tested in Mojo Launcher in this environment. Test in a separate instance and keep a copy of the original files. If it crashes, share `latestlog.txt` so the next update can target the actual error.


### Health Indicator update (v0.4.0 source)
- Adds a Right Shift menu toggle for Health Indicator.
- When the crosshair is on a living entity, draws hearts and numeric current/max HP near the crosshair.
- Saves the toggle in `config/nsclient.properties` as `hud.healthIndicator`.
- The health value is whatever the client knows; multiplayer servers can restrict or alter visible entity health.
- Source-only update: not compiled or tested in Minecraft/Android Mojo Launcher yet.
