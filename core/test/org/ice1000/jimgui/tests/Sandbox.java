package org.ice1000.jimgui.tests;

import org.ice1000.jimgui.*;
import org.ice1000.jimgui.cpp.DeallocatableObjectManager;
import org.ice1000.jimgui.flag.JImDirection;
import org.ice1000.jimgui.flag.JImFontAtlasFlags;
import org.ice1000.jimgui.flag.JImMouseIndexes;
import org.ice1000.jimgui.util.JImGuiUtil;
import org.ice1000.jimgui.util.JniLoader;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Sandbox {
	public static void main(@NotNull String @NotNull ... args) {
		JniLoader.load();
		AtomicInteger count = new AtomicInteger();
		AtomicReference<String> ini = new AtomicReference<>("");
		NativeFloat aFloat = new NativeFloat();
		NativeFloat aFloat2 = new NativeFloat();
		long start = System.currentTimeMillis();
		DeallocatableObjectManager manager = new DeallocatableObjectManager();
		manager.add(aFloat);
		manager.add(aFloat2);
		JImGuiUtil.runWithinPer(9000, 10, imGui -> {
			JImFont font = imGui.getFont();
			font.setFallbackChar('*');
			if (imGui.beginMainMenuBar()) {
				if (imGui.beginMenu("Main", true)) {
					imGui.menuItem("Copy", "Ctrl+C");
					imGui.menuItem("Paste", "Ctrl+V");
					imGui.menuItem("Open");
					imGui.endMenu();
				}
				if (imGui.beginMenu("Styles")) {
					if (imGui.menuItem("Dark")) imGui.styleColorsDark();
					if (imGui.menuItem("Classic")) imGui.styleColorsClassic();
					if (imGui.menuItem("Light")) imGui.styleColorsLight();
					imGui.endMenu();
				}
				imGui.endMainMenuBar();
			}
			imGui.dragFloat("Wtf", aFloat);
			imGui.text("Float = " + aFloat.accessValue());
			imGui.dragFloatRange2("Wtf", aFloat, aFloat2);
			imGui.text("Float2 = " + aFloat2.accessValue());
			float bizarreValue = (System.currentTimeMillis() - start) / 2000f;
			imGui.getStyle().setWindowBorderSize(bizarreValue);
			MutableJImVec4 background = JImVec4.fromAWT(Color.BLUE);
			imGui.colorEdit4("Background", background);
			imGui.setBackground(background);
			JImGuiIO io = imGui.getIO();
			imGui.text("framerate: " + io.getFramerate());
			if (io.isKeyCtrl()) {
				imGui.text("[Ctrl]");
				imGui.sameLine();
			}
			if (io.isKeyAlt()) {
				imGui.text("[Alt]");
				imGui.sameLine();
			}
			if (io.isKeySuper()) {
				imGui.text("[Super]");
				imGui.sameLine();
			}
			if (io.isKeyShift()) {
				imGui.text("[Shift]");
				imGui.sameLine();
			}
			imGui.newLine();
			font.setFontSize(bizarreValue + 13);
			JImFontAtlas containerAtlas = font.getContainerAtlas();
			containerAtlas.setFlags(containerAtlas.getFlags() | JImFontAtlasFlags.NoMouseCursors);
			imGui.text(String.valueOf(font.getFontSize()));
			imGui.text(font.getDebugName());
			String inputString = io.getInputString();
			imGui.text("Input characters (len: " + inputString.length() + "): " + inputString);
			if (io.mouseClickedAt(JImMouseIndexes.Left)) imGui.text("Left is down.");
			if (io.mouseClickedAt(JImMouseIndexes.Right)) imGui.text("Right is down.");
			imGui.text("MousePos: [" + io.getMousePosX() + ", " + io.getMousePosY() + "]");
			imGui.text("MouseDelta: [" + io.getMouseDeltaX() + ", " + io.getMouseDeltaY() + "]");
			if (imGui.button("Click me!")) count.getAndIncrement();
			imGui.sameLine();
			imGui.text("Click count: " + count);
			imGui.bulletText("fps: " + io.getFramerate());
			imGui.text("Boy\u2642next\u26a8door\n就是邻\u26a2家男\u26a3孩");
			imGui.textWrapped("Boy\u2642next\u26a8door deep dark fantasy oh yes sir billy harrington van darkholm");
			imGui.textDisabled("Boy\u2642next\u26a8door\n就是邻\u26a2家男\u26a3孩");
			imGui.labelText("Boy\u2642next\u26a8door", "就是邻\u26a2家男\u26a3孩");
			imGui.smallButton("Boy\u2642next\u26a8door\n就是邻\u26a2家男\u26a3孩");
			imGui.newLine();
			imGui.newLine();
			if (io.isWantSaveIniSettings()) {
				ini.set(imGui.saveIniSettingsToMemory());
				io.setWantSaveIniSettings(false);
			}
			imGui.text(ini.get());
			imGui.newLine();
			try (MutableJImVec4 red = JImVec4.fromAWT(java.awt.Color.RED);
			     MutableJImVec4 yellow = JImVec4.fromAWT(java.awt.Color.YELLOW);
			     MutableJImVec4 green = JImVec4.fromJFX(javafx.scene.paint.Color.GREEN)) {
				imGui.textColored(red, "Woa!");
				imGui.textColored(imGui.getStyle().getColor(JImStyleColors.TextSelectedBg), "Woa!");
				imGui.separator();
				imGui.textColored(green, "Woa!");
				imGui.spacing();
				imGui.bullet();
				imGui.textColored(yellow, "Woa!");
				imGui.arrowButton("Woa!", JImDirection.Down);
			}
		});
		manager.deallocateAll();
	}
}
