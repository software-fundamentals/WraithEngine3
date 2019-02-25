package net.whg.we.window;

/**
 * The WindowListener is an interface whose purpose is to
 * listen for certain events being sent from WindowManager
 * and take appropriate actions.
 */
public interface WindowListener
{
	public void onWindowResized(int width, int height);

	public void onKey(int key, KeyState action, int mods);

	public void onType(int key, int mods);

	public void onMouseMoved(float mouseX, float mouseY);
}
