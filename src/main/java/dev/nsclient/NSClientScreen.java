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

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Close"),
                button -> this.close()
        ).dimensions(x - 50, y + 35, 100, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xE8101824);
        context.drawCenteredTextWithShadow(
                this.textRenderer, Text.literal("NS CLIENT"),
                this.width / 2, this.height / 2 - 35, 0x55FFFF
        );
        context.drawCenteredTextWithShadow(
                this.textRenderer, Text.literal("Minecraft 1.21.1"),
                this.width / 2, this.height / 2 - 12, 0xFFFFFF
        );
        super.render(context, mouseX, mouseY, delta);
    }
}
