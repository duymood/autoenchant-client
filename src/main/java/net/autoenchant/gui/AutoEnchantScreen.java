package net.autoenchant.gui;

import net.autoenchant.AutoClickManager;
import net.autoenchant.AutoEnchantManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class AutoEnchantScreen extends Screen {

    public AutoEnchantScreen() {
        super(Text.literal("Auto Enchant"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        ButtonWidget enchantButton = ButtonWidget.builder(enchantButtonLabel(), button -> {
            AutoEnchantManager.enabled = !AutoEnchantManager.enabled;
            button.setMessage(enchantButtonLabel());
        }).dimensions(centerX - 100, centerY - 26, 200, 20).build();

        ButtonWidget clickButton = ButtonWidget.builder(clickButtonLabel(), button -> {
            AutoClickManager.enabled = !AutoClickManager.enabled;
            button.setMessage(clickButtonLabel());
        }).dimensions(centerX - 100, centerY + 4, 200, 20).build();

        this.addDrawableChild(enchantButton);
        this.addDrawableChild(clickButton);
    }

    private Text enchantButtonLabel() {
        String trangThai = AutoEnchantManager.enabled ? "BẬT" : "TẮT";
        return Text.literal("Auto Enchant: " + trangThai);
    }

    private Text clickButtonLabel() {
        String trangThai = AutoClickManager.enabled ? "BẬT" : "TẮT";
        return Text.literal("Auto Click phải (1.1s): " + trangThai);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(
                this.textRenderer, this.title, this.width / 2, centerYForTitle(), 0xFFFFFF
        );
    }

    private int centerYForTitle() {
        return this.height / 2 - 56;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
