
package dev.nsclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.lwjgl.glfw.GLFW;

public final class NSClient implements ClientModInitializer {

    public static boolean coordinatesEnabled = true;
    public static boolean fpsEnabled = true;
    public static boolean armorEnabled = true;
    public static boolean potionEnabled = true;
    public static boolean healthEnabled = true;

    // 0 = Hearts, 1 = Numeric HP, 2 = Both
    public static int healthMode = 2;

    public static int hudX = 6;
    public static int hudY = 6;
    public static boolean hudEditorEnabled = false;

    private KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        openMenuKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.nsclient.open_menu",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_RIGHT_SHIFT,
                        "category.nsclient"
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new NSClientScreen());
                }
            }
        });

        HudRenderCallback.EVENT.register((context, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();

            if (client.player == null || client.options.hudHidden) {
                return;
            }

            int x = hudX;
            int y = hudY;
            int white = 0xFFFFFF;
            int cyan = 0x55FFFF;
            int green = 0x55FF55;

            context.drawText(
                    client.textRenderer,
                    Text.literal("NS CLIENT"),
                    x, y, cyan, true
            );

            int lineY = y + 12;

            if (coordinatesEnabled) {
                context.drawText(
                        client.textRenderer,
                        Text.literal("XYZ: "
                                + client.player.getBlockX() + " "
                                + client.player.getBlockY() + " "
                                + client.player.getBlockZ()),
                        x, lineY, white, true
                );
                lineY += 12;
            }

            if (fpsEnabled) {
                context.drawText(
                        client.textRenderer,
                        Text.literal("FPS: " + client.getCurrentFps()),
                        x, lineY, green, true
                );
                lineY += 12;
            }

            if (healthEnabled) {
                float hp = client.player.getHealth();
                float maxHp = client.player.getMaxHealth();

                String healthText;
                if (healthMode == 0) {
                    healthText = "Health: " + hearts(hp);
                } else if (healthMode == 1) {
                    healthText = String.format(
                            java.util.Locale.ROOT, "%.1f / %.1f HP", hp, maxHp
                    );
                } else {
                    healthText = String.format(
                            java.util.Locale.ROOT,
                            "Health: %s  %.1f/%.1f",
                            hearts(hp), hp, maxHp
                    );
                }

                context.drawText(
                        client.textRenderer,
                        Text.literal(healthText),
                        x, lineY, 0xFF7777, true
                );
                lineY += 12;
            }

            if (armorEnabled) {
                context.drawText(
                        client.textRenderer,
                        Text.literal("Armor"),
                        x, lineY, white, true
                );
                lineY += 12;

                int armorX = x;
                for (ItemStack stack : client.player.getInventory().armor) {
                    if (!stack.isEmpty()) {
                        context.drawItem(stack, armorX, lineY);

                        String durability = "";
                        if (stack.isDamageable()) {
                            int remaining = 100 - Math.round(
                                    stack.getDamage() * 100.0f
                                            / stack.getMaxDamage()
                            );
                            durability = remaining + "%";
                        }

                        if (!durability.isEmpty()) {
                            context.drawText(
                                    client.textRenderer,
                                    Text.literal(durability),
                                    armorX, lineY + 17, white, true
                            );
                        }
                    }
                    armorX += 22;
                }
                lineY += 32;
            }

            if (potionEnabled) {
                boolean hasEffects = false;

                for (StatusEffectInstance effect
                        : client.player.getStatusEffects()) {
                    if (hasEffects) {
                        lineY += 12;
                    }

                    String name = effect.getEffectType().value()
                            .getName().getString();

                    int amplifier = effect.getAmplifier() + 1;
                    int seconds = effect.getDuration() / 20;

                    context.drawText(
                            client.textRenderer,
                            Text.literal(name + " " + amplifier
                                    + " (" + seconds + "s)"),
                            x, lineY, 0xAAFFAA, true
                    );

                    hasEffects = true;
                }

                if (!hasEffects) {
                    context.drawText(
                            client.textRenderer,
                            Text.literal("Effects: None"),
                            x, lineY, white, true
                    );
                }
            }

            if (hudEditorEnabled) {
                context.drawText(
                        client.textRenderer,
                        Text.literal("HUD EDITOR: ON"),
                        x, Math.max(0, y - 12), 0xFFFF55, true
                );
            }
        });
    }

    private static String hearts(float health) {
        int fullHearts = Math.max(0, Math.round(health / 2.0f));
        return "♥".repeat(Math.min(fullHearts, 20));
    }
}
