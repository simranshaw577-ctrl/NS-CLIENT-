
package dev.nsclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class NSClientScreen extends Screen {

    private static final int CYAN = 0xFF55FFFF;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int MUTED = 0xFFAAAAAA;
    private static final int PANEL = 0xEE101827;
    private static final int CARD = 0xFF1A2638;

    public NSClientScreen() {
        super(Text.literal("NS CLIENT"));
    }

    @Override
    protected void init() {
        clearChildren();

        int panelWidth = Math.min(340, this.width - 16);
        int left = (this.width - panelWidth) / 2;
        int top = Math.max(8, (this.height - 250) / 2);
        int buttonWidth = Math.min(145, (panelWidth - 28) / 2);
        int gap = 6;
        int rightX = left + panelWidth - buttonWidth - 10;
        int leftX = left + 10;
        int y = top + 48;
        int buttonHeight = 20;

        addDrawableChild(ButtonWidget.builder(
                toggleText("Coordinates", NSClient.coordinatesEnabled),
                b -> {
                    NSClient.coordinatesEnabled = !NSClient.coordinatesEnabled;
                    refresh();
                }).dimensions(leftX, y, buttonWidth, buttonHeight).build());

        addDrawableChild(ButtonWidget.builder(
                toggleText("FPS Counter", NSClient.fpsEnabled),
                b -> {
                    NSClient.fpsEnabled = !NSClient.fpsEnabled;
                    refresh();
                }).dimensions(rightX, y, buttonWidth, buttonHeight).build());

        y += buttonHeight + gap;

        addDrawableChild(ButtonWidget.builder(
                toggleText("Armor Status", NSClient.armorEnabled),
                b -> {
                    NSClient.armorEnabled = !NSClient.armorEnabled;
                    refresh();
                }).dimensions(leftX, y, buttonWidth, buttonHeight).build());

        addDrawableChild(ButtonWidget.builder(
                toggleText("Potion Status", NSClient.potionEnabled),
                b -> {
                    NSClient.potionEnabled = !NSClient.potionEnabled;
                    refresh();
                }).dimensions(rightX, y, buttonWidth, buttonHeight).build());

        y += buttonHeight + gap;

        addDrawableChild(ButtonWidget.builder(
                toggleText("Health", NSClient.healthEnabled),
                b -> {
                    NSClient.healthEnabled = !NSClient.healthEnabled;
                    refresh();
                }).dimensions(leftX, y, buttonWidth, buttonHeight).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Health Mode: " + modeName()),
                b -> {
                    NSClient.healthMode = (NSClient.healthMode + 1) % 3;
                    refresh();
                }).dimensions(rightX, y, buttonWidth, buttonHeight).build());

        y += buttonHeight + 12;

        int moveWidth = Math.min(72, (panelWidth - 28) / 3);
        int moveGap = 4;
        int rowWidth = moveWidth * 3 + moveGap * 2;
        int moveX = left + (panelWidth - rowWidth) / 2;

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Left"),
                b -> {
                    NSClient.hudX = Math.max(0, NSClient.hudX - 5);
                    refresh();
                }).dimensions(moveX, y, moveWidth, buttonHeight).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Up"),
                b -> {
                    NSClient.hudY = Math.max(12, NSClient.hudY - 5);
                    refresh();
                }).dimensions(moveX + moveWidth + moveGap, y,
                moveWidth, buttonHeight).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Right"),
                b -> {
                    NSClient.hudX = Math.min(this.width - 100, NSClient.hudX + 5);
                    refresh();
                }).dimensions(moveX + (moveWidth + moveGap) * 2, y,
                moveWidth, buttonHeight).build());

        y += buttonHeight + gap;

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Down"),
                b -> {
                    NSClient.hudY = Math.min(this.height - 30, NSClient.hudY + 5);
                    refresh();
                }).dimensions(moveX + moveWidth + moveGap, y,
                moveWidth, buttonHeight).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("HUD Editor: "
                        + (NSClient.hudEditorEnabled ? "ON" : "OFF")),
                b -> {
                    NSClient.hudEditorEnabled = !NSClient.hudEditorEnabled;
                    refresh();
                }).dimensions(leftX, y + buttonHeight + gap,
                buttonWidth, buttonHeight).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Close"),
                b -> close()).dimensions(rightX, y + buttonHeight + gap,
                buttonWidth, buttonHeight).build());
    }

    private void refresh() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.setScreen(new NSClientScreen());
        }
    }

    private static Text toggleText(String name, boolean enabled) {
        return Text.literal(name + ": " + (enabled ? "ON" : "OFF"));
    }

    private static String modeName() {
        return switch (NSClient.healthMode) {
            case 0 -> "Hearts";
            case 1 -> "Numeric";
            default -> "Both";
        };
    }

    @Override
    public void render(DrawContext context, int mouseX,
                       int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);

        int panelWidth = Math.min(340, this.width - 16);
        int panelHeight = 250;
        int left = (this.width - panelWidth) / 2;
        int top = Math.max(8, (this.height - panelHeight) / 2);

        context.fill(left - 2, top - 2,
                left + panelWidth + 2, top + panelHeight + 2, CYAN);
        context.fill(left, top,
                left + panelWidth, top + panelHeight, PANEL);
        context.fill(left, top,
                left + panelWidth, top + 3, CYAN);

        context.drawText(
                this.textRenderer,
                Text.literal("NS CLIENT"),
                left + 10, top + 10, CYAN, true
        );

        context.drawText(
                this.textRenderer,
                Text.literal("HUD CONTROL CENTER"),
                left + 10, top + 25, MUTED, true
        );

        context.drawText(
                this.textRenderer,
                Text.literal("HUD position: " + NSClient.hudX
                        + ", " + NSClient.hudY),
                left + 10, top + panelHeight - 28, WHITE, true
        );

        context.drawText(
                this.textRenderer,
                Text.literal("Nimrit Love Shivam"),
                left + 10, top + panelHeight - 16, CYAN, true
        );

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(null);
        }
    }
}
