package net.whg.we.window;

import net.whg.we.utils.logging.Log;

public abstract class AbstractDesktopWindow implements Window {
	
	private String _name;
	private boolean _resizable = false;
	private boolean _vSync = false;
	private int _width = 640;
	private int _height = 480;
	private QueuedWindow _window;

	/**
	 * isWindowOpen checks if the window is open by checking
	 * if the _windowId has been set.
	 * @return true if the window is open, false otherwise.
	 */
	public abstract boolean isWindowOpen();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String name)
	{
		if (isWindowOpen())
			return;

		_name = name;
		Log.infof("Changed window title to %s.", name);

		if (_window != null)
			_window.addEvent(() ->
			{
				_window.setNameInstant(_name);
			});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setResizable(boolean resizable)
	{
		if (isWindowOpen())
			return;

		_resizable = resizable;
		Log.infof("Changed window resizable to %s.", resizable);

		if (_window != null)
			_window.addEvent(() ->
			{
				_window.setResizableInstant(_resizable);
			});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVSync(boolean vSync)
	{
		if (isWindowOpen())
			return;

		_vSync = vSync;
		Log.infof("Changed window VSync to %s.", vSync);

		if (_window != null)
			_window.addEvent(() ->
			{
				_window.setVSyncInstant(_vSync);
			});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSize(int width, int height)
	{
		if (isWindowOpen())
			return;

		_width = width;
		_height = height;
		Log.infof("Changed window size to %dx%d.", width, height);

		if (_window != null)
			_window.addEvent(() ->
			{
				_window.setSizeInstant(_width, _height);
			});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setQueuedWindow(QueuedWindow window)
	{
		_window = window;
	}

	protected String name() {
		return _name;
	}

	protected boolean resizable() {
		return _resizable;
	}

	protected boolean vSync() {
		return _vSync;
	}

	protected int width() {
		return _width;
	}

	protected int height() {
		return _height;
	}

	protected QueuedWindow window() {
		return _window;
	}
}
