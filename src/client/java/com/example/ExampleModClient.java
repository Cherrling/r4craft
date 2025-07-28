
package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import org.lwjgl.glfw.GLFW;

public class ExampleModClient implements ClientModInitializer {

	private double lastYVelocity = 0.0;
	private boolean modifyPosition = false;

	@Override
	public void onInitializeClient() {
		KeyBinding modifyPositionKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.mymod.move",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_M,
				"category.mymod"));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null)
				return;

			if (modifyPositionKey.wasPressed()) {
				modifyPosition = !modifyPosition;
				client.player.sendMessage(
						Text.literal("Position modification " + (modifyPosition ? "enabled" : "disabled")),
						false);
			}

			ClientPlayerEntity player = client.player;
			Vec3d currentVelocity = player.getVelocity();

			if (currentVelocity.y > 0 && currentVelocity.y < 0.01) {
				if (modifyPosition) {
					client.player.setPosition(client.player.getX(), client.player.getY() + 0.249, client.player.getZ());
				}
			}

			if (lastYVelocity > 0 && currentVelocity.y <= 0) {
				client.player.networkHandler.sendChatCommand("flag");
			}

			lastYVelocity = currentVelocity.y;
		});
	}
}