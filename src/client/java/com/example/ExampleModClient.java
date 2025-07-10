package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class ExampleModClient implements ClientModInitializer {
	private static KeyBinding teleportKey;
    private static boolean wasPressed = false;

	@Override
	public void onInitializeClient() {
		teleportKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.teleportmod.teleport",
			GLFW.GLFW_KEY_Z,
			"category.teleportmod.main"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (teleportKey.isPressed() && !wasPressed) {
				wasPressed = true;
				teleportUp();
			} else if (!teleportKey.isPressed()) {
				wasPressed = false;
			}
		});
	}

	private void teleportUp() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        Vec3d currentPos = client.player.getPos();
        Vec3d newPos = currentPos.add(0, 0.3, 0);
        
        client.player.setPosition(newPos);
            
        client.player.networkHandler.sendChatCommand("flag");
    }
}