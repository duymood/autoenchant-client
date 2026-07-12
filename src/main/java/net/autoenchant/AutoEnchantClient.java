package net.autoenchant;

import net.autoenchant.gui.AutoEnchantScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * Entry point phía client.
 * - Mỗi tick kiểm tra tổ hợp phím Shift + Chuột phải để mở giao diện Auto Enchant.
 * - Gọi AutoEnchantManager.tick() để xử lý logic tự động dùng /enchant.
 */
public class AutoEnchantClient implements ClientModInitializer {

    private boolean prevRightMouseDown = false;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            return;
        }

        net.minecraft.client.util.Window window = client.getWindow();
        long windowHandle = window.getHandle();

        boolean shiftDown = InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_LEFT_SHIFT)
                || InputUtil.isKeyPressed(window, GLFW.GLFW_KEY_RIGHT_SHIFT);
        boolean rightMouseDown = GLFW.glfwGetMouseButton(windowHandle, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;

        boolean justPressed = rightMouseDown && !this.prevRightMouseDown;
        this.prevRightMouseDown = rightMouseDown;

        if (shiftDown && justPressed && client.currentScreen == null) {
            client.setScreen(new AutoEnchantScreen());
            return;
        }

        AutoEnchantManager.tick(client);
    }
}
