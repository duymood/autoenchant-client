package net.autoenchant;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

/**
 * Quản lý trạng thái bật/tắt và toàn bộ logic tự động:
 * 1) Cứ mỗi 1 giây (20 tick) kiểm tra level kinh nghiệm người chơi.
 * 2) Nếu level >= 60 -> gửi lệnh "/enchant".
 * 3) Chờ GUI (HandledScreen) mở ra, click vào slot 13.
 * 4) Đóng GUI lại, chờ 1 giây rồi lặp lại nếu vẫn còn bật.
 */
public final class AutoEnchantManager {

    public static volatile boolean enabled = false;

    private static final int CHECK_INTERVAL_TICKS = 20;
    private static final int MAX_WAIT_FOR_SCREEN_TICKS = 40;
    private static final int MIN_LEVEL = 60;
    private static final int TARGET_SLOT = 13;

    private static int cooldownTicks = 0;
    private static int waitForScreenTicks = -1;

    private AutoEnchantManager() {
    }

    public static void tick(MinecraftClient client) {
        if (!enabled || client.player == null) {
            waitForScreenTicks = -1;
            return;
        }

        if (waitForScreenTicks >= 0) {
            if (client.currentScreen instanceof HandledScreen<?> handledScreen) {
                clickTargetSlotAndClose(client, handledScreen);
                waitForScreenTicks = -1;
                cooldownTicks = CHECK_INTERVAL_TICKS;
            } else {
                waitForScreenTicks++;
                if (waitForScreenTicks > MAX_WAIT_FOR_SCREEN_TICKS) {
                    waitForScreenTicks = -1;
                    cooldownTicks = CHECK_INTERVAL_TICKS;
                }
            }
            return;
        }

        if (cooldownTicks > 0) {
            cooldownTicks--;
            return;
        }

        if (client.player.experienceLevel >= MIN_LEVEL) {
            client.player.networkHandler.sendChatCommand("enchant");
            waitForScreenTicks = 0;
        } else {
            cooldownTicks = CHECK_INTERVAL_TICKS;
        }
    }

    private static void clickTargetSlotAndClose(MinecraftClient client, HandledScreen<?> screen) {
        ScreenHandler handler = screen.getScreenHandler();
        if (client.interactionManager != null && client.player != null) {
            client.interactionManager.clickSlot(
                    handler.syncId,
                    TARGET_SLOT,
                    0,
                    SlotActionType.PICKUP,
                    client.player
            );
        }
        if (client.player != null) {
            client.player.closeHandledScreen();
        }
    }
}
