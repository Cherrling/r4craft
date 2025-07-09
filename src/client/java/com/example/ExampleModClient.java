package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class ExampleModClient implements ClientModInitializer {
	
	private static KeyBinding tpupKey;
	
	@Override
	public void onInitializeClient() {
		// 注册按键绑定
		tpupKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.modid.tpup", // 按键的翻译键
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_U, // 默认绑定到 U 键
			"category.modid.keys" // 按键类别
		));
		
		// 注册客户端命令
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			// 设置高度命令
			dispatcher.register(ClientCommandManager.literal("tpup")
				.then(ClientCommandManager.literal("set")
					.then(ClientCommandManager.argument("height", DoubleArgumentType.doubleArg(0.1, 10.0))
						.executes(this::setHeight)
					)
				)
				.then(ClientCommandManager.literal("get")
					.executes(this::getHeight)
				)
				.executes(this::performTpUp) // 直接执行 /tpup 进行传送
			);
		});
		
		// 注册客户端tick事件来检测按键
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (tpupKey.wasPressed()) {
				if (client.player != null) {
					performTeleport(client);
				}
			}
		});
	}
	
	private int setHeight(CommandContext<FabricClientCommandSource> context) {
		double height = DoubleArgumentType.getDouble(context, "height");
		TpUpConfig.setHeight(height);
		context.getSource().sendFeedback(Text.literal("传送高度已设置为: " + TpUpConfig.getHeightString() + " 格"));
		return 1;
	}
	
	private int getHeight(CommandContext<FabricClientCommandSource> context) {
		context.getSource().sendFeedback(Text.literal("当前传送高度: " + TpUpConfig.getHeightString() + " 格"));
		return 1;
	}
	
	private int performTpUp(CommandContext<FabricClientCommandSource> context) {
		var client = context.getSource().getClient();
		if (client.player != null) {
			performTeleport(client);
		}
		return 1;
	}
	
	private void performTeleport(net.minecraft.client.MinecraftClient client) {
		Vec3d currentPos = client.player.getPos();
		Vec3d newPos = currentPos.add(0, TpUpConfig.getHeight(), 0);
		
		client.player.setPosition(newPos);
		client.player.sendMessage(Text.literal("你已被提升 " + TpUpConfig.getHeightString() + " 格！"), false);
		client.player.networkHandler.sendChatCommand("flag");
	}
}