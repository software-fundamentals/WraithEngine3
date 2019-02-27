package net.whg.we.window;

import net.whg.we.utils.logging.Log;

public abstract class AbstractDesktopWindow implements Window {

	private String _name;
	private boolean _resizable;
	private boolean _vSync;
	private int _width;
	private int _height;

	protected AbstractDesktopWindow(String name, boolean resizable, boolean vSync,
			int width, int height)
	{
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
	public boolean setName(String name)
	{
		if (isWindowOpen())
			return false;

		_name = name;
		Log.infof("Changed window title to %s.", name);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setResizable(boolean resizable)
	{
		if (isWindowOpen())
			return false ;

		_resizable = resizable;
		Log.infof("Changed window resizable to %s.", resizable);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setVSync(boolean vSync)
	{
		if (isWindowOpen())
			return false;

		_vSync = vSync;
		Log.infof("Changed window VSync to %s.", vSync);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setSize(int width, int height)
	{
		if (isWindowOpen())
			return false;

		_width = width;
		_height = height;
		Log.infof("Changed window size to %dx%d.", width, height);
		return true;
	}

	protected String name()
	{
		return _name;
	}

	protected boolean resizable()
	{
		return _resizable;
	}

	protected boolean vSync()
	{
		return _vSync;
	}

	protected int width()
	{
		return _width;
	}

	protected int height()
	{
		return _height;
	}
}
