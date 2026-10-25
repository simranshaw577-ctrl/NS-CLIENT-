
package dev.nsclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class NSClientScreen extends Screen {
    private String category = "HUD";

    private static final int BG = 0xF20A101B;
    private static final int PANEL = 0xFF111D2B;
    private static final int CARD = 0xFF172536;
    private static final int CYAN = 0xFF35DDF5;
    private static final int MUTED = 0xFF91A9BE;
    private static final int WHITE = 0xFFFFFFFF;

    public NSClientScreen() {
        super(Text.literal("NS CLIENT"));
    }

    @Override
    protected void init() {
        clearChildren();

        int panelW = Math.min(440, width - 16);
        int panelH = Math.min(270, height - 16);
        int left = (width - panelW) / 2;
        int top = (height - panelH) / 2;

        int sideW = Math.min(108, panelW / 3);
        int contentX = left + sideW + 12;
        int contentW = panelW - sideW - 20;

        String[] tabs = {
            "HUD", "VISUAL", "COMBAT",
            "ANIMATIONS", "CHAT", "SETTINGS"
        };

        for (int i = 0; i < tabs.length; i++) {
            final String tab = tabs[i];
            addDrawableChild(ButtonWidget.builder(
                Text.literal((category.equals(tab) ? "> " : "")
                    + tab),
                b -> {
                    category = tab;
                    clearAndInit();
                }
            ).dimensions(
                left + 8,
                top + 48 + i * 27,
                sideW - 14,
                22
            ).build());
        }

        int cardY = top + 58;
        int cardW = contentW;

        if (category.equals("HUD")) {
            addDrawableChild(ButtonWidget.builder(
                coordinatesText(),
                b -> {
                    NSClient.coordinatesEnabled =
                        !NSClient.coordinatesEnabled;
                    b.setMessage(coordinatesText());
                }
            ).dimensions(contentX, cardY, cardW, 24).build());

            addDrawableChild(ButtonWidget.builder(
                fpsText(),
                b -> {
                    NSClient.fpsEnabled = !NSClient.fpsEnabled;
                    b.setMessage(fpsText());
                }
            ).dimensions(contentX, cardY + 36, cardW, 24).build());
        }

        addDrawableChild(ButtonWidget.builder(
            Text.literal("CLOSE"),
            b -> close()
        ).dimensions(
            left + panelW - 78,
            top + panelH - 32,
            68,
            22
        ).build());
    }

    private Text coordinatesText() {
        return Text.literal("Coordinates  ["
            + (NSClient.coordinatesEnabled ? "ON" : "OFF") + "]");
    }

    private Text fpsText() {
        return Text.literal("FPS Counter  ["
            + (NSClient.fpsEnabled ? "ON" : "OFF") + "]");
    }

    @Override
    public void render(
        DrawContext context,
        int mouseX,
        int mouseY,
        float delta
    ) {
        context.fill(0, 0, width, height, BG);

        int panelW = Math.min(440, width - 16);
        int panelH = Math.min(270, height - 16);
        int left = (width - panelW) / 2;
        int top = (height - panelH) / 2;
        int sideW = Math.min(108, panelW / 3);

        context.fill(left, top, left + panelW, top + panelH, PANEL);
        context.fill(left, top, left + panelW, top + 3, CYAN);

        // Sidebar and content panel
        context.fill(
            left + 5, top + 38,
            left + sideW, top + panelH - 40,
            0xFF0B1420
        );

        context.fill(
            left + sideW + 5, top + 42,
            left + panelW - 8, top + panelH - 40,
            CARD
        );

        // Premium cyan accent
        context.fill(
            left + sideW + 5, top + 42,
            left + sideW + 7, top + panelH - 40,
            CYAN
        );

        context.drawText(
            textRenderer, Text.literal("NS CLIENT"),
            left + 10, top + 12, CYAN, true
        );

        context.drawText(
            textRenderer, Text.literal("PREMIUM CLIENT"),
            left + 10, top + 26, MUTED, false
        );

        context.drawText(
            textRenderer, Text.literal(category + " SETTINGS"),
            left + sideW + 14, top + 16, WHITE, true
        );

        context.drawText(
            textRenderer, Text.literal("Nimrit Love Shivam"),
            left + 10, top + panelH - 22, MUTED, false
        );

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
