package net.whg.we.rendering;

import net.whg.we.utils.Screen;
import net.whg.we.window.WindowManager;
import net.whg.we.window.WindowBuilder;
import net.whg.we.window.WindowEngine;
import net.whg.we.window.WindowListenerType;

/**
 * The GraphicsPipeline class initializes a window with associated
 * graphics and sets the window of the screen.
 */
public class GraphicsPipeline
{
	private Graphics _graphics;
	private WindowManager _windowManager;

	/**
	 * GraphicsEngine initializes an OPENGL_ENGINE and a GLFW Window with
	 * width 800 and height 600 and attaches a new DefaultWindowListener to
	 * the window.
	 */
	public GraphicsPipeline()
	{
		_graphics = GraphicsFactory.createInstance(GraphicsFactory.OPENGL_ENGINE);
		_windowManager = new WindowBuilder(WindowEngine.GLFW, WindowListenerType.DEFAULT).setName("WraithEngine")
				.setResizable(false).setSize(800, 600).setVSync(false).setGraphicsEngine(_graphics).build();
		_graphics.init();
		Screen.setWindow(_windowManager);
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
	 * getWindow returns the current WindowManager.
	 * @return the current Window.
	 */
	public WindowManager getWindow()
	{
		return _windowManager;
	}

	/**
	 * requestClose closes the window.
	 */
	public void requestClose()
	{
		_windowManager.requestClose();
	}

	/**
	 * dispse destroys the window.
	 */
	public void dispose()
	{
		_graphics.dispose();
	}
}
