package net.minecraftforge.client.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;

@Cancelable
public class RenderGameOverlayEvent extends Event {
	public enum ElementType {
		ALL, HELMET, PORTAL, CROSSHAIRS, BOSSHEALTH, ARMOR, HEALTH, FOOD, AIR, HOTBAR, EXPERIENCE, TEXT, HEALTHMOUNT, JUMPBAR, CHAT, PLAYER_LIST, DEBUG
	}

	public final float partialTicks;
	public final ScaledResolution resolution;
	public final int mouseX;
	public final int mouseY;
	public final ElementType type;

	public RenderGameOverlayEvent(float partialTicks, ScaledResolution resolution, int mouseX, int mouseY) {
		this.partialTicks = partialTicks;
		this.resolution = resolution;
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		type = null;
	}

	private RenderGameOverlayEvent(RenderGameOverlayEvent parent, ElementType type) {
		partialTicks = parent.partialTicks;
		resolution = parent.resolution;
		mouseX = parent.mouseX;
		mouseY = parent.mouseY;
		this.type = type;
	}

	public static class Pre extends RenderGameOverlayEvent {
		public Pre(RenderGameOverlayEvent parent, ElementType type) {
			super(parent, type);
		}
	}

	public static class Post extends RenderGameOverlayEvent {
		public Post(RenderGameOverlayEvent parent, ElementType type) {
			super(parent, type);
		}

		@Override
		public boolean isCancelable() {
			return false;
		}
	}

	public static class Text extends Pre {
		public final ArrayList<String> left;
		public final ArrayList<String> right;

		public Text(RenderGameOverlayEvent parent, ArrayList<String> left, ArrayList<String> right) {
			super(parent, ElementType.TEXT);
			this.left = left;
			this.right = right;
		}
	}

	public static class Chat extends Pre {
		public int posX;
		public int posY;

		public Chat(RenderGameOverlayEvent parent, int posX, int posY) {
			super(parent, ElementType.CHAT);
			this.posX = posX;
			this.posY = posY;
		}
	}
}
