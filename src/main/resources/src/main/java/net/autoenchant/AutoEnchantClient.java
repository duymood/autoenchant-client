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

    // Lưu trạng thái chuột phải ở tick trước để chỉ bắt sự kiện "vừa nhấn" (rising edge),
    // tránh việc GUI bị mở liên tục khi giữ chuột.
    private boolean prevRightMouseDown = false;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            return;
        }

        long windowHandle = client.getWindow().getHandle();

        boolean shiftDown = InputUtil.isKeyPressed(windowHandle, GLFW.GLFW_KEY_LEFT_SHIFT)
                || InputUtil.isKeyPressed(windowHandle, GLFW.GLFW_KEY_RIGHT_SHIFT);
        boolean rightMouseDown = GLFW.glfwGetMouseButton(windowHandle, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;

        boolean justPressed = rightMouseDown && !this.prevRightMouseDown;
        this.prevRightMouseDown = rightMouseDown;

        // Chỉ mở GUI khi: đang giữ Shift, vừa bấm chuột phải, và chưa có màn hình nào khác đang mở
        if (shiftDown && justPressed && client.currentScreen == null) {
            client.setScreen(new AutoEnchantScreen());
            return;
        }

        // Xử lý logic tự động enchant ở mỗi tick (bất kể GUI có đang mở hay không)
        AutoEnchantManager.tick(client);
    }
}
