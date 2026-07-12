package net.autoenchant;

import net.autoenchant.gui.AutoEnchantScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AutoEnchantClient implements ClientModInitializer {

    // Phím mở/đóng giao diện Auto Enchant - đổi được trong Options > Controls > Key Binds > Auto Enchant Client
    public static final KeyBinding TOGGLE_GUI_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.autoenchant.toggle_gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_APOSTROPHE, // Phím mặc định: dấu nháy đơn ' — có thể đổi trong Controls
            "key.categories.autoenchant"
    ));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            return;
        }

        while (TOGGLE_GUI_KEY.wasPressed()) {
            if (client.currentScreen == null) {
                client.setScreen(new AutoEnchantScreen());
            } else if (client.currentScreen instanceof AutoEnchantScreen) {
                client.setScreen(null);
            }
        }

        AutoEnchantManager.tick(client);
        AutoClickManager.tick(client);
    }
}
