package net.whg.we.window;

import java.util.LinkedList;
import net.whg.we.rendering.Graphics;

/**
 * The QueuedWindow class is responsible for handling the synchronized
 * communication between the main thread and the window thread by making use
 * of thread-safe message queues.
 */
public class QueuedWindow
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

	/**
	 * WindowBuilder takes an engine type as argument and if valid,
	 * initializes that window. Then a QueuedWindow is initialized
	 * for that window as well as a Thread for the QueuedWindow.
	 * @param  engine The type of window that should be initialized.
	 */

	/**
	 * QueuedWindow takes a Window as argument, sets it to the current
	 * window and assigns itself as its QueuedWindow.
	 * @param  window The Window that the QueuedWindow should communicate with.
	 */
	QueuedWindow(Window window)
	{
		_window = window;
		_window.setQueuedWindow(this);
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
	 * setWindowListener assigns a WindowListener to the QueuedWindow.
	 * @param  listener The WindowListener that should be assigned.
	 */
	public void setWindowListener(WindowListener listener)
	{
		_listener = listener;
	}

	/**
	 * getWindow returns the current Window
	 * @return The Window assigned to the _window variable.
	 */
	Window getWindow()
	{
		return _window;
	}

	void buildWindow()
	{
		addRequest(() ->
		{
			_window.buildWindow();
		});
	}

	public void setCursorEnabled(boolean cursorEnabled)
	{
		addRequest(() ->
		{
			_window.setCursorEnabled(cursorEnabled);
		});
	}

	/**
	 * setCursorEnabledInstant assigns true or false to the
	 *  _cursorEnabled variable.
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
	 * @return the name of the QueuedWindow.
	 */
	public String getName()
	{
		return _name;
	}

	/**
	 * isResizable returns the value of _isResizable.
	 * @return true if _isResizableis true, false otherwise.
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
	 * getWidth returns the width of the QueuedWindow.
	 * @return the width of the QueuedWindow.
	 */
	public int getWidth()
	{
		return _width;
	}

	/**
	 * getHeight returns the height of the QueuedWindow
	 * @return the height of the QueuedWindow.
	 */
	public int getHeight()
	{
		return _height;
	}

	public void setName(String name)
	{
		addRequest(() ->
		{
			_window.setName(name);
		});
	}

	void setNameInstant(String name)
	{
		_name = name;
	}

	public void setResizable(boolean resizable)
	{
		addRequest(() ->
		{
			_window.setResizable(resizable);
		});
	}

	void setResizableInstant(boolean resizable)
	{
		_isResizable = resizable;
	}

	public void setVSync(boolean vSync)
	{
		addRequest(() ->
		{
			_window.setVSync(vSync);
		});
	}

	void setVSyncInstant(boolean vSync)
	{
		_vSync = vSync;
	}

	public void setSize(int width, int height)
	{
		addRequest(() ->
		{
			_window.setSize(width, height);
		});
	}

	void setSizeInstant(int width, int height)
	{
		_width = width;
		_height = height;

		if (_listener != null)
			_listener.onWindowResized(width, height);
	}

	void onKey(int key, KeyState state, int mods)
	{
		if (_listener != null)
			_listener.onKey(key, state, mods);
	}

	void onType(int key, int mods)
	{
		if (_listener != null)
			_listener.onType(key, mods);
	}

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

	void initGraphics(Graphics graphics)
	{
		blockUntilRequestsFinish();
		_window.initGraphics(graphics);
	}
}
