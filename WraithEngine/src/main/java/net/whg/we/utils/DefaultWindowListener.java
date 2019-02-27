package net.whg.we.utils;

import org.lwjgl.opengl.GL11;
import net.whg.we.window.KeyState;
import net.whg.we.window.WindowListener;

/**
 * The DefaultWindowListener class implements the WindowListener interface
 * and listens for events being sent from WindowManager.
 */
public class DefaultWindowListener implements WindowListener
{
	/**
	 * onWindowResized updates the width and height of the screen and
	 * sets the viewport accordingly.
	 * @param width  the int that represents the new width.
	 * @param height the int that represents the new height.
	 */
	@Override
	public void onWindowResized(int width, int height)
	{
		Screen.updateSize(width, height);
		// _camera.rebuildProjectionMatrix();
		GL11.glViewport(0, 0, width, height);
	}

	/**
	 * onKey notifies Input of which key that was pressed.
	 * @param key   the affected key.
	 * @param state which state the key is in
	 * @param mods  possible modification key.
	 */
	@Override
	public void onKey(int key, KeyState state, int mods)
	{
		Input.setKeyPressed(key, state, mods);
	}

	/**
	 * onMouseMoved notifies Input of how the mouse has moved.
	 * @param mouseX the X-value of the mouse.
	 * @param mouseY the Y-value of the mouse.
	 */
	@Override
	public void onMouseMoved(float mouseX, float mouseY)
	{
		Input.setMousePosition(mouseX, mouseY);
	}

	/**
	 * onType notifies Input of which key that was typed.
	 * @param key  the key that was pressed.
	 * @param mods possible modification key.
	 */
	@Override
	public void onType(int key, int mods)
	{
		Input.addTypedKey(key, mods);
	}
}
