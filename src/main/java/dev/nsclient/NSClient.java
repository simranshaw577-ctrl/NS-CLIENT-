
package dev.nsclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.text.Text;

import org.lwjgl.glfw.GLFW;

public final class NSClient implements ClientModInitializer {

    public static boolean coordinatesEnabled = true;
    public static boolean fpsEnabled = true;
    public static boolean armorEnabled = true;
    public static boolean potionEnabled = true;
    public static boolean healthEnabled = true;

    public static boolean autoShieldBreakerEnabled = false;

    public static int healthMode = 2;

    public static int hudX = 6;
    public static int hudY = 6;
    public static boolean hudEditorEnabled = false;

    private KeyBinding openMenuKey;
    private KeyBinding toggleShieldBreakerKey;

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

        toggleShieldBreakerKey =
                KeyBindingHelper.registerKeyBinding(
                        new KeyBinding(
                                "key.nsclient.auto_shield_breaker",
                                InputUtil.Type.KEYSYM,
                                GLFW.GLFW_KEY_K,
                                "category.nsclient"
                        )
                );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new NSClientScreen());
                }
            }

            while (toggleShieldBreakerKey.wasPressed()) {
                autoShieldBreakerEnabled =
                        !autoShieldBreakerEnabled;
            }

            if (!autoShieldBreakerEnabled
                    || client.player == null
                    || client.interactionManager == null
                    || client.currentScreen != null) {
                return;
            }

            if (!client.options.attackKey.isPressed()) {
                return;
            }

            if (!(client.crosshairTarget
                    instanceof EntityHitResult hit)) {
                return;
            }

            Entity target = hit.getEntity();

            if (!(target instanceof PlayerEntity targetPlayer)) {
                return;
            }

            if (!targetPlayer.isBlocking()
                    || !targetPlayer.getActiveItem()
                    .isOf(Items.SHIELD)) {
                return;
            }

            int axeSlot = -1;

            for (int slot = 0; slot < 9; slot++) {
                ItemStack stack = client.player
                        .getInventory().getStack(slot);

                if (stack.isOf(Items.WOODEN_AXE)
                        || stack.isOf(Items.STONE_AXE)
                        || stack.isOf(Items.IRON_AXE)
                        || stack.isOf(Items.GOLDEN_AXE)
                        || stack.isOf(Items.DIAMOND_AXE)
                        || stack.isOf(Items.NETHERITE_AXE)) {
                    axeSlot = slot;
                    break;
                }
            }

            if (axeSlot == -1) {
                return;
            }

            if (client.player.getInventory()
                    .getSelectedSlot() != axeSlot) {
                client.player.getInventory()
                        .setSelectedSlot(axeSlot);
            }

            if (client.player.getAttackCooldownProgress(0.5F)
                    >= 1.0F) {
                client.interactionManager.attackEntity(
                        client.player, targetPlayer
                );
                client.player.swingHand(Hand.MAIN_HAND);
            }
        });

        HudRenderCallback.EVENT.register((context, tickCounter) -> {

            MinecraftClient client =
                    MinecraftClient.getInstance();

            if (client.player == null
                    || client.options.hudHidden) {
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
                        Text.literal("FPS: "
                                + client.getCurrentFps()),
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
                            java.util.Locale.ROOT,
                            "%.1f / %.1f HP", hp, maxHp
                    );
                } else {
                    healthText = String.format(
                            java.util.Locale.ROOT,
                            "Health: %s %.1f/%.1f",
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

                for (ItemStack stack :
                        client.player.getInventory().armor) {

                    if (!stack.isEmpty()) {
                        context.drawItem(
                                stack, armorX, lineY
                        );

                        if (stack.isDamageable()) {
                            int remaining = 100 - Math.round(
                                    stack.getDamage() * 100.0F
                                            / stack.getMaxDamage()
                            );

                            context.drawText(
                                    client.textRenderer,
                                    Text.literal(remaining + "%"),
                                    armorX, lineY + 17,
                                    white, true
                            );
                        }
                    }

                    armorX += 22;
                }

                lineY += 32;
            }

            if (potionEnabled) {
                boolean hasEffects = false;

                for (StatusEffectInstance effect :
                        client.player.getStatusEffects()) {

                    String name = effect.getEffectType()
                            .value().getName().getString();

                    int amplifier = effect.getAmplifier() + 1;
                    int seconds = effect.getDuration() / 20;

                    context.drawText(
                            client.textRenderer,
                            Text.literal(name + " " + amplifier
                                    + " (" + seconds + "s)"),
                            x, lineY,
                            0xAAFFAA, true
                    );

                    lineY += 12;
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

            if (autoShieldBreakerEnabled) {
                context.drawText(
                        client.textRenderer,
                        Text.literal("Shield Assist: ON"),
                        x, lineY + 12, green, true
                );
            }

            if (hudEditorEnabled) {
                context.drawText(
                        client.textRenderer,
                        Text.literal("HUD EDITOR: ON"),
                        x, Math.max(0, y - 12),
                        0xFFFF55, true
                );
            }
        });
    }

    private static String hearts(float health) {
        int count = Math.max(
                0, Math.round(health / 2.0F)
        );

        return "♥".repeat(Math.min(count, 20));
    }
            }
            
