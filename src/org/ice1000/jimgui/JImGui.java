package org.ice1000.jimgui;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.nio.charset.StandardCharsets;

/**
 * @author ice1000
 * @since v0.1
 */
@SuppressWarnings("WeakerAccess")
public class JImGui implements AutoCloseable, Closeable {
	/** package-private by design */
	long nativeObjectPtr;
	private @NotNull JImVec4 background;
	private @Nullable JImGuiIO io;

	public JImGui() {
		nativeObjectPtr = allocateNativeObjects();
		io = new JImGuiIO();
		background = new JImVec4(1.0f, 0.55f, 0.60f, 1.00f);
	}

	@Override
	public void close() {
		background.close();
		deallocateNativeObjects(nativeObjectPtr);
		io = null;
	}

	public void demoMainLoop() {
		demoMainLoop(background.nativeObjectPtr);
	}

	public void text(@NotNull String text) {
		text(text.getBytes(StandardCharsets.UTF_8));
	}

	public void textDisabled(@NotNull String text) {
		textDisabled(text.getBytes(StandardCharsets.UTF_8));
	}

	public void textWrapped(@NotNull String text) {
		textWrapped(text.getBytes(StandardCharsets.UTF_8));
	}

	public void text(@NotNull JImVec4 color, @NotNull String text) {
		textColored(color.nativeObjectPtr, text.getBytes(StandardCharsets.UTF_8));
	}

	public void sameLine() {
		sameLine(0, -1);
	}

	public boolean button(@NotNull String text) {
		return button(text.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Create {@link java.awt.Button} like text button
	 *
	 * @param text the text to display
	 * @return true if clicked
	 */
	public boolean smallButton(@NotNull String text) {
		if (io == null) alreadyDisposed();
		return smallButton(text.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Create {@link java.awt.Button} like text button
	 *
	 * @param text   the text to display
	 * @param height button height
	 * @param width  button width
	 * @return true if clicked
	 */
	public boolean button(@NotNull String text, float width, float height) {
		return button(text.getBytes(StandardCharsets.UTF_8), width, height);
	}

	public @Nullable JImGuiIO tryGetIO() {
		return io;
	}

	public @NotNull JImGuiIO getIO() {
		if (io == null) alreadyDisposed();
		return io;
	}

	/** @return shouldn't be closed, will close automatically */
	public @NotNull JImVec4 getBackground() {
		return background;
	}

	/** @param background shouldn't be closed, will close automatically */
	public void setBackground(@NotNull JImVec4 background) {
		this.background.close();
		this.background = background;
	}

	public boolean windowShouldClose() {
		return windowShouldClose(nativeObjectPtr);
	}

	@Contract(" -> fail")
	private void alreadyDisposed() {
		throw new IllegalStateException("Window already disposed.");
	}

	public void render() {
		render(nativeObjectPtr, background.nativeObjectPtr);
	}

	public native void sameLine(float posX, float spacingW);
	public native void separator();
	public native void newLine();
	public native void spacing();
	public native void initNewFrame();
	public native float getTextLineHeight();
	public native float getTextLineHeightWithSpacing();
	public native float getFrameHeight();
	public native float getFrameHeightWithSpacing();
	public native float getCursorPosX();
	public native float getCursorPosY();
	public native void setCursorPos(float newX, float newY);
	public native void setCursorPosX(float newValue);
	public native void setCursorPosY(float newValue);

	private static native long allocateNativeObjects();
	private static native void deallocateNativeObjects(long nativeObjectPtr);
	private static native void demoMainLoop(long colorPtr);
	private static native boolean windowShouldClose(long nativeObjectPtr);
	private static native void render(long nativeObjectPtr, long colorPtr);
	private static native void text(byte[] text);
	private static native void textDisabled(byte[] text);
	private static native void textWrapped(byte[] text);
	private static native void textColored(long colorPtr, byte[] text);
	private static native boolean button(byte[] text);
	private static native boolean smallButton(byte[] text);
	private static native boolean button(byte[] text, float width, float height);
}
