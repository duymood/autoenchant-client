package net.autoenchant;

import net.minecraft.client.MinecraftClient;

/**
 * Tự động "bấm chuột phải" (giả lập phím Use Item) theo chu kỳ,
 * độc lập với chức năng Auto Enchant.
 */
public final class AutoClickManager {

    public static volatile boolean enabled = false;

    // 1.1 giây = 22 tick (1 giây = 20 tick trong Minecraft)
    private static final int INTERVAL_TICKS = 22;

    private static int cooldownTicks = 0;
    private static boolean keyHeldThisClick = false;

    private AutoClickManager() {
    }

    public static void tick(MinecraftClient client) {
        if (!enabled || client.player == null) {
            releaseKeyIfHeld(client);
            cooldownTicks = 0;
            return;
        }

        if (keyHeldThisClick) {
            releaseKeyIfHeld(client);
            return;
        }

        if (cooldownTicks > 0) {
            cooldownTicks--;
            return;
        }

        client.options.useKey.setPressed(true);
        keyHeldThisClick = true;
        cooldownTicks = INTERVAL_TICKS;
    }

    private static void releaseKeyIfHeld(MinecraftClient client) {
        if (keyHeldThisClick) {
            client.options.useKey.setPressed(false);
            keyHeldThisClick = false;
        }
    }
}
