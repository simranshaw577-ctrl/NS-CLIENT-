
package dev.nsclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class NSClientScreen extends Screen {

    public NSClientScreen() {
        super(Text.literal("NS CLIENT"));
    }

    @Override
    protected void init() {
        int x = this.width / 2;
        int y = this.height / 2;

        addDrawableChild(ButtonWidget.builder(
                coordinatesText(),
                button -> {
                    NSClient.coordinatesEnabled =
                            !NSClient.coordinatesEnabled;
                    button.setMessage(coordinatesText());
                }
        ).dimensions(x - 100, y - 35, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(
                fpsText(),
                button -> {
                    NSClient.fpsEnabled = !NSClient.fpsEnabled;
                    button.setMessage(fpsText());
                }
        ).dimensions(x - 100, y - 5, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Close"),
                button -> close()
        ).dimensions(x - 50, y + 35, 100, 20).build());
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
        context.fill(
                0, 0, this.width, this.height,
                0xE8101824
        );

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("NS CLIENT"),
                this.width / 2,
                this.height / 2 - 75,
                0x55FFFF
        );

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("HUD SETTINGS"),
                this.width / 2,
                this.height / 2 - 55,
                0xFFFFFF
        );

        super.render(context, mouseX, mouseY, delta);
    }
}
