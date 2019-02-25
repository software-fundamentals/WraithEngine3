package net.whg.we.window;

import net.whg.we.utils.logging.Log;

public abstract class AbstractDesktopWindow implements Window {
	
	private String _name;
	private boolean _resizable;
	private boolean _vSync;
	private int _width;
	private int _height;
	private WindowManager _windowManager;

	protected AbstractDesktopWindow(String name, boolean resizable, boolean vSync,
			int width, int height) {
		_name = name;
		_resizable = resizable;
		_vSync = vSync;
		_width = width;
		_height = height;
	}

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

		if (_windowManager != null)
			_windowManager.addEvent(() ->
			{
				_windowManager.setNameInstant(_name);
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

		if (_windowManager != null)
			_windowManager.addEvent(() ->
			{
				_windowManager.setResizableInstant(_resizable);
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

		if (_windowManager != null)
			_windowManager.addEvent(() ->
			{
				_windowManager.setVSyncInstant(_vSync);
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

		if (_windowManager != null)
			_windowManager.addEvent(() ->
			{
				_windowManager.setSizeInstant(_width, _height);
			});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWindowManager(WindowManager windowManager)
	{
		_windowManager = windowManager;
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

	protected WindowManager windowManager() {
		return _windowManager;
	}
}
