package net.whg.we.window;

import net.whg.we.rendering.Graphics;

/**
 * The WindowBuilder class initializes a Window as well as
 * a QueuedWindow for the Window and implements functions
 * which in turn assigns different values to QueuedWindow variables.
 * The class also implements a build function which builds the
 * QueuedWindow.
 */
public class WindowBuilder
{
	public static final int WINDOW_ENGINE_GLFW = 0;

	private QueuedWindow _window;
	private boolean _built;
	private Graphics _graphics;

	/**
	 * WindowBuilder takes an engine type as argument and if valid,
	 * initializes that window. Then a QueuedWindow is initialized
	 * for that window as well as a Thread for the QueuedWindow.
	 * @param  engine The type of window that should be initialized.
	 */
	public WindowBuilder(int engine)
	{
		Window window;

		switch (engine)
		{
			case WINDOW_ENGINE_GLFW:
				window = new GLFWWindow();
				break;
			default:
				throw new IllegalArgumentException("Unknown window engine type!");
		}

		_window = new QueuedWindow(window);
		Thread windowThread = new Thread(() ->
		{
			_window.waitForEvents();
		});
		windowThread.setName("Window");
		windowThread.setDaemon(false);
		windowThread.start();
	}

	/**
	 * setListener assigns a WindowListener to the current QueuedWindow.
	 * @param  listener The WindowListener that should be assigned.
	 * @return          The current WindowBuilder.
	 */
	public WindowBuilder setListener(WindowListener listener)
	{
		_window.setWindowListener(listener);
		return this;
	}

	/**
	 * setName assigns a name to the current QueuedWindow if it's
	 * not already built.
	 * @param  name The String that should be assigned.
	 * @return      The current WindowBuilder.
	 */
	public WindowBuilder setName(String name)
	{
		if (_built)
			throw new IllegalStateException("Window already built!");

		_window.setName(name);
		return this;
	}

	/**
	 * setResizable assigns a resizable boolean value to the current
	 * QueuedWindow if it's not already built.
	 * @param  resizable The boolean attribute that should be assigned.
	 * @return           The current WindowBuilder.
	 */
	public WindowBuilder setResizable(boolean resizable)
	{
		if (_built)
			throw new IllegalStateException("Window already built!");

		_window.setResizable(resizable);
		return this;
	}

	/**
	 * setVSync assigns a vSync boolean value to the current
	 * QueuedWindow if it's not already built.
	 * @param  vSync 	 The boolean attribute that should be assigned.
	 * @return           The current WindowBuilder.
	 */
	public WindowBuilder setVSync(boolean vSync)
	{
		if (_built)
			throw new IllegalStateException("Window already built!");

		_window.setVSync(vSync);
		return this;
	}

	/**
	 * setSize assigns a height and width to the current QueuedWindow
	 * if it's not alerady built.
	 * @param  width  The int that should be set as width.
	 * @param  height The int that should be set as height.
	 * @return        The current WindowBuilder.
	 */
	public WindowBuilder setSize(int width, int height)
	{
		if (_built)
			throw new IllegalStateException("Window already built!");

		_window.setSize(width, height);
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
	 * build calls the QueuedWindow functions buildWindow() and initGraphics()
	 * if the window is not already built and the graphics are not null.
	 * @return The QueuedWindow that has been built.
	 */
	public QueuedWindow build()
	{
		if (_built)
			throw new IllegalStateException("Window already built!");
		if (_graphics == null)
			throw new IllegalStateException("Graphics engine not assigned!");

		_built = true;
		_window.buildWindow();
		_window.initGraphics(_graphics);
		return _window;
	}
}
