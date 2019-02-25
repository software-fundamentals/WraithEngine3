package net.whg.we.rendering;

import net.whg.we.utils.DefaultWindowListener;
import net.whg.we.utils.Screen;
import net.whg.we.window.QueuedWindow;
import net.whg.we.window.WindowBuilder;

/**
 * The GraphicsPipeline class initializes a window with associated
 * graphics and sets the window of the screen.
 */
public class GraphicsPipeline
{
	private Graphics _graphics;
	private QueuedWindow _window;

	/**
	 * GraphicsEngine initializes an OPENGL_ENGINE and a GLFW Window with
	 * width 800 and height 600 and attaches a new DefaultWindowListener to
	 * the window.
	 */
	public GraphicsPipeline()
	{
		_graphics = GraphicsFactory.createInstance(GraphicsFactory.OPENGL_ENGINE);
		_window = new WindowBuilder(WindowBuilder.WINDOW_ENGINE_GLFW).setName("WraithEngine")
				.setResizable(false).setSize(800, 600).setVSync(false)
				.setListener(new DefaultWindowListener()).setGraphicsEngine(_graphics).build();
		_graphics.init();
		Screen.setWindow(_window);
	}

	/**
	 * getGraphics returns the current Graphics.
	 * @return the current Graphics.
	 */
	public Graphics getGraphics()
	{
		return _graphics;
	}

	/**
	 * getWindow returns the current Window.
	 * @return the current Window.
	 */
	public QueuedWindow getWindow()
	{
		return _window;
	}

	/**
	 * requestClose closes the window.
	 */
	public void requestClose()
	{
		_window.requestClose();
	}

	/**
	 * dispse destroys the window.
	 */
	public void dispose()
	{
		_graphics.dispose();
	}
}
