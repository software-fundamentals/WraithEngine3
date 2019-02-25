package net.whg.we.window;

import net.whg.we.rendering.Graphics;
import net.whg.we.utils.DefaultWindowListener;

/**
 * The WindowBuilder class initializes a Window and a WindowListener and
 * ties them together by creating a WindowManager that handles the communication
 * between the Window thread and Main thread. The class also implements a
 * build function which builds the WindowManager.
 */
public class WindowBuilder
{
	public static final int WINDOW_ENGINE_GLFW = 0;
	private WindowManager _windowManager;
	private boolean _built;
	private Graphics _graphics;

	/**
	 * WindowBuilder takes an engine type as argument and if valid,
	 * initializes that window. Then a WindowManager is initialized
	 * for that window as well as a Thread for the WindowManager.
	 * @param  engine The type of window that should be initialized.
	 */
	public WindowBuilder(WindowEngine engine, WindowListenerType listener)
	{
		Window window;

		switch (engine)
		{
			case GLFW:
				window = new GLFWWindow();
				break;
			default:
				throw new IllegalArgumentException("Unknown window engine type!");
		}

		WindowListener windowListener;
		switch (listener)
		{
			case DEFAULT:
				windowListener = new DefaultWindowListener();
				break;
			case NO_LISTENER:
				windowListener = null;
				break;
			default:
				throw new IllegalArgumentException("Unknown window listener type!");
		}
		if (windowListener != null) {
			_windowManager = new WindowManager(window, windowListener);
		} else {
			_windowManager = new WindowManager(window);
		}
		Thread windowThread = new Thread(() ->
		{
			_windowManager.waitForEvents();
		});
		windowThread.setName("Window");
		windowThread.setDaemon(false);
		windowThread.start();
	}

	/**
	 * setName assigns a name to the current WindowManager if it's
	 * not already built.
	 * @param  name The String that should be assigned.
	 * @return      The current WindowBuilder.
	 */
	public WindowBuilder setName(String name)
	{
		if (_built)
			throw new IllegalStateException("Window already built!");

		_windowManager.setName(name);
		return this;
	}

	/**
	 * setResizable assigns a resizable boolean value to the current
	 * WindowManager if it's not already built.
	 * @param  resizable The boolean attribute that should be assigned.
	 * @return           The current WindowBuilder.
	 */
	public WindowBuilder setResizable(boolean resizable)
	{
		if (_built)
			throw new IllegalStateException("Window already built!");

		_windowManager.setResizable(resizable);
		return this;
	}

	/**
	 * setVSync assigns a vSync boolean value to the current
	 * WindowManager if it's not already built.
	 * @param  vSync 	 The boolean attribute that should be assigned.
	 * @return           The current WindowBuilder.
	 */
	public WindowBuilder setVSync(boolean vSync)
	{
		if (_built)
			throw new IllegalStateException("Window already built!");

		_windowManager.setVSync(vSync);
		return this;
	}

	/**
	 * setSize assigns a height and width to the current WindowManager
	 * if it's not alerady built.
	 * @param  width  The int that should be set as width.
	 * @param  height The int that should be set as height.
	 * @return        The current WindowBuilder.
	 */
	public WindowBuilder setSize(int width, int height)
	{
		if (_built)
			throw new IllegalStateException("Window already built!");

		_windowManager.setSize(width, height);
		return this;
	}

	/**
	 * setGraphicsEngine sets the the Graphics for the WindowBuilder.
	 * @param  graphics The Graphics that should be assigned.
	 * @return          The current WindowBuilder.
	 */
	public WindowBuilder setGraphicsEngine(Graphics graphics)
	{
		_graphics = graphics;
		return this;
	}

	/**
	 * build calls the WindowManager functions buildWindow() and initGraphics()
	 * if the window is not already built and the graphics are not null.
	 * @return The WindowManager that has been built.
	 */
	public WindowManager build()
	{
		if (_built)
			throw new IllegalStateException("Window already built!");
		if (_graphics == null)
			throw new IllegalStateException("Graphics engine not assigned!");

		_built = true;
		_windowManager.buildWindow();
		_windowManager.initGraphics(_graphics);
		return _windowManager;
	}
}
