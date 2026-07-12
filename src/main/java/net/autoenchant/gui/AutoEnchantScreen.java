package net.autoenchant.gui;

import net.autoenchant.AutoEnchantManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/**
 * Giao diện đơn giản chỉ có đúng 1 chức năng: nút bật/tắt Auto Enchant.
 * Mở bằng tổ hợp Shift + Click chuột phải (xem AutoEnchantClient).
 */
public class AutoEnchantScreen extends Screen {

    public AutoEnchantScreen() {
        super(Text.literal("Auto Enchant"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        ButtonWidget toggleButton = ButtonWidget.builder(buttonLabel(), button -> {
            AutoEnchantManager.enabled = !AutoEnchantManager.enabled;
            button.setMessage(buttonLabel());
        }).dimensions(centerX - 100, centerY - 10, 200, 20).build();

        this.addDrawableChild(toggleButton);
    }

    private Text buttonLabel() {
        String trangThai = AutoEnchantManager.enabled ? "BẬT" : "TẮT";
        return Text.literal("Auto Enchant: " + trangThai);
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
        return this.height / 2 - 40;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
