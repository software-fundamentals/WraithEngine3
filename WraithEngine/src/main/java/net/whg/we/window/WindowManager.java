package net.whg.we.window;

import java.util.LinkedList;
import net.whg.we.rendering.Graphics;

/**
 * The WindowManager class is responsible for handling the synchronized
 * communication between the main thread and the window thread by making use
 * of thread-safe message queues.
 */
public class WindowManager
{
	private Window _window;
	private LinkedList<WindowRequest> _requests = new LinkedList<>();
	private LinkedList<WindowEvent> _events = new LinkedList<>();

	private boolean _requestClose;
	private String _name;
	private boolean _isResizable;
	private boolean _vSync;
	private int _width;
	private int _height;
	private WindowListener _listener;
	private boolean _cursorEnabled = true;

	WindowManager(Window window, WindowListener listener) {
		_window = window;
		_window.setWindowManager(this);
		_listener = listener;
	}

	/**
	 * WindowManager takes a Window as argument, sets it to the current
	 * window and assigns itself as its WindowManager.
	 * @param  window The Window that the WindowManager should communicate with.
	 */
	WindowManager(Window window)
	{
		_window = window;
		_window.setWindowManager(this);
	}

	/**
	 * waitForEvents looks at the variable _requestClose and while it's false
	 * it keeps calling the updateWindow function of _window. Inside that loop
	 * it lets one thread look at the requests and run the first request in the line.
	 */
	void waitForEvents()
	{
		while (!_requestClose)
		{
			_window.updateWindow();

			synchronized (_requests)
			{
				while (!_requests.isEmpty())
					_requests.removeFirst().run();
			}
		}

		_window.disposeWindow();
	}
	/**
	 * getWindow returns the current Window
	 * @return The Window assigned to the _window variable.
	 */
	Window getWindow()
	{
		return _window;
	}

	/**
	 * buildWindow adds the Window function buildWindow
	 * to the list of requests.
	 */
	void buildWindow()
	{
		addRequest(() ->
		{
			_window.buildWindow();
		});
	}

	/**
	 * setCursorEnabled adds the Window function setCursorEnabled
	 * to the list of requests.
	 * @param cursorEnabled the boolean to sent as parameter to the function.
	 */
	public void setCursorEnabled(boolean cursorEnabled)
	{
		addRequest(() ->
		{
			_window.setCursorEnabled(cursorEnabled);
		});
	}

	/**
	 * setCursorEnabledInstant sets the cursorEnabled value instead of putting
	 * it in the request queue.
	 * @param cursorEnabled boolean that should be assigned to _cursorEnabled.
	 */
	void setCursorEnabledInstant(boolean cursorEnabled)
	{
		_cursorEnabled = cursorEnabled;
	}

	/**
	 * isCursorEnabled returns the value of _cursorEnabled.
	 * @return true if _cursorEnabled is true, false otherwise.
	 */
	public boolean isCursorEnabled()
	{
		return _cursorEnabled;
	}

	public void requestClose()
	{
		addRequest(() ->
		{
			_window.requestClose();
		});
	}

	/**
	 * addRequest lets one thread add a WindowRequest to the
	 * _request LinkedList.
	 * @param r The WindowRequest to be added.
	 */
	private void addRequest(WindowRequest r)
	{
		synchronized (_requests)
		{
			_requests.add(r);
		}
	}

	/**
	 * addEvent lets one thread add a WindowEvent to the
	 * _events LinkedList.
	 * @param e The WindowEvent to be added.
	 */
	void addEvent(WindowEvent e)
	{
		synchronized (_events)
		{
			_events.add(e);
		}
	}

	/**
	 * getName returns the value of _name.
	 * @return the name of the WindowManager.
	 */
	public String getName()
	{
		return _name;
	}

	/**
	 * isResizable returns the value of _isResizable.
	 * @return true if _isResizable is true, false otherwise.
	 */
	public boolean isResizable()
	{
		return _isResizable;
	}

	/**
	 * isVSync returns the value of _vSync.
	 * @return true if _vSync is true, false otherwise.
	 */
	public boolean isVSync()
	{
		return _vSync;
	}

	/**
	 * getWidth returns the width of the WindowManager.
	 * @return the width of the WindowManager.
	 */
	public int getWidth()
	{
		return _width;
	}

	/**
	 * getHeight returns the height of the WindowManager
	 * @return the height of the WindowManager.
	 */
	public int getHeight()
	{
		return _height;
	}

	/**
	 * setName adds the Window function setName to the list of requests.
	 * @param name the String to be sent as parameter to the function.
	 */
	public void setName(String name)
	{
		addRequest(() ->
		{
			_window.setName(name);
		});
	}

	/**
	 * setNameInstant sets the name instead of putting it into the request queue.
	 * @param name the String with the new name.
	 */
	void setNameInstant(String name)
	{
		_name = name;
	}

	/**
	 * setResizable adds the Window function setResizable to the list
	 * of requests.
	 * @param resizable the boolean to be sent as parameter to the function.
	 */
	public void setResizable(boolean resizable)
	{
		addRequest(() ->
		{
			_window.setResizable(resizable);
		});
	}

	/**
	 * setResizableInstant sets the resizable value instead of putting
	 * it into the request queue.
	 * @param resizable the boolean with the new resizable value.
	 */
	void setResizableInstant(boolean resizable)
	{
		_isResizable = resizable;
	}

	/**
	 * setVSync adds the Window function setVSync to the the list
	 * of requests.
	 * @param vSync the boolean to be sent as parameter to the function.
	 */
	public void setVSync(boolean vSync)
	{
		addRequest(() ->
		{
			_window.setVSync(vSync);
		});
	}

	/**
	 * setVSyncInstant sets the vSync value instead of putting
	 * it into the request queue.
	 * @param vSync the boolean with the new vSync value.
	 */
	void setVSyncInstant(boolean vSync)
	{
		_vSync = vSync;
	}

	/**
	 * setSize adds the Window function setSize to the list
	 * of requests.
	 * @param width  the width to be sent as parameter to the function.
	 * @param height the height to be sent as parameter to the function.
	 */
	public void setSize(int width, int height)
	{
		addRequest(() ->
		{
			_window.setSize(width, height);
		});
	}

	/**
	 * setSizeInstant sets the height and width of the window
	 * instead of putting it into the request queue and notifies
	 * the WindowListener of this event.
	 * @param width  the int with the new width.
	 * @param height the int with the new height.
	 */
	void setSizeInstant(int width, int height)
	{
		_width = width;
		_height = height;

		if (_listener != null)
			_listener.onWindowResized(width, height);
	}

	/**
	 * onKey notifies the WindowListener of an onKey event.
	 * @param key   the affected key.
	 * @param state which state the key is in
	 * @param mods  possible modification key.
	 */
	void onKey(int key, KeyState state, int mods)
	{
		if (_listener != null)
			_listener.onKey(key, state, mods);
	}

	/**
	 * onType notifies the WindowListener of an onType event.
	 * @param key  the key that was pressed.
	 * @param mods possible modification key.
	 */
	void onType(int key, int mods)
	{
		if (_listener != null)
			_listener.onType(key, mods);
	}

	/**
	 * onMouseMove notifies the WindowListener of an onMouseMoved event.
	 * @param mouseX the X-value of the mouse.
	 * @param mouseY the Y-value of the mouse.
	 */
	void onMouseMove(float mouseX, float mouseY)
	{
		if (_listener != null)
			_listener.onMouseMoved(mouseX, mouseY);
	}

	/**
	 * Called at the end of each rendered frame to push the image to the monitor and
	 * poll input events.
	 *
	 * @return True if the window has requested a close. False otherwise.
	 */
	public boolean endFrame()
	{
		synchronized (_events)
		{
			while (!_events.isEmpty())
				_events.removeFirst().run();
		}

		if (_window.endFrame())
		{
			_requestClose = true;
			return true;
		}

		return false;
	}

	/**
	 * blockUntilRequestsFinish lets the Thread wait
	 * until the list of requests is empty.
	 */
	private void blockUntilRequestsFinish()
	{
		while (true)
		{
			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e)
			{
			}

			synchronized (_requests)
			{
				if (_requests.isEmpty())
					return;
			}
		}
	}

	/**
	 * initGraphics wait until there are no request
	 * and then initializes the Window graphics by calling
	 * the initGraphics function.
	 * @param graphics the Graphics that should be initialized.
	 */
	void initGraphics(Graphics graphics)
	{
		blockUntilRequestsFinish();
		_window.initGraphics(graphics);
	}
}
