package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import org.lwjgl.glfw.GLFW;

public class ExampleModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
    }
}

class KeyInputHandler {
    private static KeyBinding sendKey;
    private static KeyBinding sendJump;
    private static int tickCountdown = -1;
    private static boolean isFlagged = false;
private static int ticksLeft = 0;
private static int spamCount = 0;
    private static double startPos = 0.0;
    public static void register() {
        sendKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.examplemod.send",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "category.examplemod"
        ));
                sendJump = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.examplemod.jump",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "category.examplemod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            ClientPlayerEntity player = client.player;
            if (sendKey.wasPressed()) {
                player.setPosition(player.getX()+0.1, player.getY() + 0.2, player.getZ()+0.1);
            }
            
            if (sendJump.wasPressed()) {
                player.setPosition(player.getX(), player.getY() + 1, player.getZ());
            }


            if (!isFlagged && player.getY() > -56.7 && player.getBlockY() > -59) {
                player.networkHandler.sendChatMessage("Y = " + player.getY() + ", BlockY = " + player.getBlockY());
player.networkHandler.sendChatCommand("flag");
                isFlagged = true;
            }

        });
    }
}