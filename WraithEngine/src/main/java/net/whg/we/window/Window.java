package net.whg.we.window;

import net.whg.we.rendering.Graphics;

/**
 * The Window is an interface whose purpose is to modify
 * the state of the window.
 */
interface Window
{
	/**
	 * setQueuedWindow sets the QueuedWindow that should manage this windows'
	 * communication with the main thread.
	 * @param window the QueuedWindow.
	 */
	void setQueuedWindow(QueuedWindow window);

	/**
	 * setName changes the name of the window and adds the setNameInstant
	 * function to the QueuedWindow event queue. If the window
	 * is open, the function returns.
	 * @param name String with the new name.
	 */
	void setName(String name);

	/**
	 * setResizable changes the resizable state and adds the setReziableInstant
	 * function to the QueuedWindow event queue.  If the window is open,
	 * the function returns.
	 * @param resizable boolean with the new resizable value.
	 */
	void setResizable(boolean resizable);

	/**
	 * setVSync changes the vSync state and adds the setVSyncInstant
	 * function to the QueuedWindow event queue. If the window is open,
	 * the function returns.
	 * @param vSync boolean with the new vSync value.
	 */
	void setVSync(boolean vSync);

	/**
	 * setSize changes the width and height and adds the
	 * setSizeInstant function to the QueuedWindow event queue. If the
	 * window is open, the function returns.
	 * @param width  int with the new width.
	 * @param height int with the new height.
	 */
	void setSize(int width, int height);

	void buildWindow();

	void requestClose();

	/**
	 * disposeWindow tries to destroy the current window. If the window isn't
	 * open, the function returns.
	 */
	void disposeWindow();

	boolean endFrame();

	void updateWindow();

	void initGraphics(Graphics graphics);

	void setCursorEnabled(boolean cursorEnabled);
}
