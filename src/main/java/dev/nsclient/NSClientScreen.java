
package dev.nsclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class NSClientScreen extends Screen {

    private String category = "HUD";

    public NSClientScreen() {
        super(Text.literal("NS CLIENT"));
    }

    @Override
    protected void init() {
        clearChildren();

        int left = Math.max(8, this.width / 2 - 145);
        int top = Math.max(8, this.height / 2 - 95);
        int panelWidth = Math.min(290, this.width - 16);

        addDrawableChild(ButtonWidget.builder(
                Text.literal("HUD"),
                button -> openCategory("HUD")
        ).dimensions(left + 8, top + 35, 82, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("VISUAL"),
                button -> openCategory("VISUAL")
        ).dimensions(left + 8, top + 59, 82, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("COMBAT"),
                button -> openCategory("COMBAT")
        ).dimensions(left + 8, top + 83, 82, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("ANIMATIONS"),
                button -> openCategory("ANIMATIONS")
        ).dimensions(left + 8, top + 107, 82, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("CHAT"),
                button -> openCategory("CHAT")
        ).dimensions(left + 8, top + 131, 82, 20).build());

        int settingsX = left + 100;

        if (category.equals("HUD")) {
            addDrawableChild(ButtonWidget.builder(
                    coordinatesText(),
                    button -> {
                        NSClient.coordinatesEnabled =
                                !NSClient.coordinatesEnabled;
                        button.setMessage(coordinatesText());
                    }
            ).dimensions(settingsX, top + 45, 180, 20).build());

            addDrawableChild(ButtonWidget.builder(
                    fpsText(),
                    button -> {
                        NSClient.fpsEnabled = !NSClient.fpsEnabled;
                        button.setMessage(fpsText());
                    }
            ).dimensions(settingsX, top + 73, 180, 20).build());
        }

        addDrawableChild(ButtonWidget.builder(
                Text.literal("CLOSE"),
                button -> close()
        ).dimensions(left + panelWidth - 76, top + 159, 68, 20).build());
    }

    private void openCategory(String value) {
        category = value;
        clearAndInit();
    }

    private Text coordinatesText() {
        return Text.literal("Coordinates: "
                + (NSClient.coordinatesEnabled ? "ON" : "OFF"));
    }

    private Text fpsText() {
        return Text.literal("FPS Counter: "
                + (NSClient.fpsEnabled ? "ON" : "OFF"));
    }

    @Override
    public void render(
            DrawContext context,
            int mouseX,
            int mouseY,
            float delta
    ) {
        context.fill(0, 0, width, height, 0xD908101C);

        int left = Math.max(8, width / 2 - 145);
        int top = Math.max(8, height / 2 - 95);
        int panelWidth = Math.min(290, width - 16);

        context.fill(
                left, top, left + panelWidth, top + 188,
                0xFF111D2B
        );

        context.fill(
                left, top, left + panelWidth, top + 3,
                0xFF35DDF5
        );

        context.fill(
                left + 8, top + 30, left + 90, top + 154,
                0xFF0B1420
        );

        context.fill(
                left + 96, top + 30, left + panelWidth - 8, top + 154,
                0xFF172536
        );

        context.drawText(
                textRenderer,
                Text.literal("NS CLIENT"),
                left + 10, top + 10,
                0xFF55E8FF, true
        );

        context.drawText(
                textRenderer,
                Text.literal(category + " SETTINGS"),
                left + 103, top + 15,
                0xFFFFFFFF, true
        );

        context.drawText(
                textRenderer,
                Text.literal("Nimrit Love Shivam"),
                left + 10, top + 171,
                0xFF91A9BE, false
        );

        super.render(context, mouseX, mouseY, delta);
    }
}
