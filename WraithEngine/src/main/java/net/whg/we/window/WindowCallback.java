package net.whg.we.window;

class WindowCallback
{
	private WindowManager _windowManager;

	WindowCallback(WindowManager windowManager)
	{
		_windowManager = windowManager;
	}

	void setSize(int width, int height)
	{
		_windowManager.addEvent(() ->
		{
			_windowManager.setSizeInstant(width, height);
		});
	}

	void onKey(int key, KeyState state, int mods)
	{
		_windowManager.addEvent(() ->
		{
			_windowManager.onKey(key, state, mods);
		});
	}

	void onType(int key, int mods)
	{
		_windowManager.addEvent(() ->
		{
			_windowManager.onType(key, mods);
		});
	}

	void onMouseMove(float mouseX, float mouseY)
	{
		_windowManager.onMouseMove(mouseX, mouseY);
	}
}
