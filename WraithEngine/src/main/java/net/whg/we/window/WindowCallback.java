package net.whg.we.window;

public class WindowCallback
{
	private WindowManager _windowManager;

	public WindowCallback(WindowManager windowManager)
	{
		_windowManager = windowManager;
	}

	public void setSize(int width, int height)
	{
		_windowManager.addEvent(() ->
		{
			_windowManager.setSizeInstant(width, height);
		});
	}

	public void onKey(int key, KeyState state, int mods)
	{
		_windowManager.addEvent(() ->
		{
			_windowManager.onKey(key, state, mods);
		});
	}

	public void onType(int key, int mods)
	{
		_windowManager.addEvent(() ->
		{
			_windowManager.onType(key, mods);
		});
	}

	public void onMouseMove(float mouseX, float mouseY)
	{
		_windowManager.onMouseMove(mouseX, mouseY);
	}
}
