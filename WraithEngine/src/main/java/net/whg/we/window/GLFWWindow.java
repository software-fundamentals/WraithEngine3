package net.whg.we.window;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import net.whg.we.rendering.Graphics;
import net.whg.we.utils.logging.Log;

/**
 * The GLFWWindow class implements the Window interface and
 * ties with GLFW, a C++ library for handling window management.
 */
public class GLFWWindow implements Window
{
	private String _name = "WraithEngine Project";
	private boolean _resizable = false;
	private boolean _vSync = false;
	private int _width = 640;
	private int _height = 480;
	private long _windowId = 0;
	private WindowManager _windowManager;
	private Object _lock = new Object();
	private float _mouseX;
	private float _mouseY;

	/**
	 * setName changes the name of the window and adds the setNameInstant
	 * function to the WindowManager event queue. If the window
	 * is open, the function returns.
	 * @param name String with the new name.
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
	 * setResizable changes the resizable state and adds the setReziableInstant
	 * function to the WindowManager event queue.  If the window is open,
	 * the function returns.
	 * @param resizable boolean with the new resizable value.
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
	 * setVSync changes the vSync state and adds the setVSyncInstant
	 * function to the WindowManager event queue. If the window is open,
	 * the function returns.
	 * @param vSync boolean with the new vSync value.
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
	 * setSize changes the width and height and adds the
	 * setSizeInstant function to the WindowManager event queue. If the
	 * window is open, the function returns.
	 * @param width  int with the new width.
	 * @param height int with the new height.
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
	 * isWindowOpen checks if the window is open by checking
	 * if the _windowId has been set.
	 * @return true if the window is open, false otherwise.
	 */
	public boolean isWindowOpen()
	{
		return _windowId != MemoryUtil.NULL;
	}

	/**
	 * buildWindow builds the current window by initializing the
	 * GLFW library, setting window hint values, creating callbacks,
	 * centering the window and making it visible.
	 */
	@Override
	public void buildWindow()
	{
		Log.info("Building GLFW window.");
		Log.indent();

		synchronized (_lock)
		{
			Log.trace("Requesting default error output stream.");
			GLFWErrorCallback.createPrint(System.err).set();

			if (!GLFW.glfwInit())
			{
				Log.unindent();
				throw new IllegalStateException("Unable to initialize GLFW!");
			}

			Log.trace("Setting window default hints.");
			Log.debug("Requesting OpenGL version 3.3");
			Log.trace("Requesting Subsampling 4.");

			GLFW.glfwDefaultWindowHints();
			GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
			GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

			Log.tracef("Requesting resizable, %s", _resizable);
			if (!_resizable)
				GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

			Log.trace("Building hidden window.");
			_windowId =
					GLFW.glfwCreateWindow(_width, _height, _name, MemoryUtil.NULL, MemoryUtil.NULL);
			Log.tracef("  Recieved window id %d.", _windowId);
			if (_windowId == 0)
				throw new RuntimeException("Failed to create GLFW window!");

			Log.trace("Creating GLFW callbacks.");
			{
				Log.indent();

				Log.trace("Creating window size callback.");
				GLFW.glfwSetWindowSizeCallback(_windowId, (long window, int width, int height) ->
				{
					if (_windowManager != null)
						_windowManager.addEvent(() ->
						{
							_windowManager.setSizeInstant(_width, _height);
						});
				});

				Log.trace("Creating key callback.");
				GLFW.glfwSetKeyCallback(_windowId,
						(long window, int key, int scancode, int action, int mods) ->
						{
							final KeyState state;

							if (action == GLFW.GLFW_PRESS)
								state = KeyState.PRESSED;
							else if (action == GLFW.GLFW_RELEASE)
								state = KeyState.RELEASED;
							else
								state = KeyState.REPEATED;

							if (_windowManager != null)
								_windowManager.addEvent(() ->
								{
									_windowManager.onKey(key, state, mods);
								});
						});

				Log.trace("Creating mouse position callback.");
				GLFW.glfwSetCursorPosCallback(_windowId,
						(long window, double mouseX, double mouseY) ->
						{
							_mouseX = (float) mouseX;
							_mouseY = (float) mouseY;
						});

				Log.trace("Creating text input callback.");
				GLFW.glfwSetCharModsCallback(_windowId, (long window, int key, int mods) ->
				{
					if (_windowManager != null)
						_windowManager.addEvent(() ->
						{
							_windowManager.onType(key, mods);
						});
				});

				Log.unindent();
			}

			Log.trace("Centering window on primary monitor.");
			try (MemoryStack stack = MemoryStack.stackPush())
			{
				GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

				int x = (vidmode.width() - _width) / 2;
				int y = (vidmode.height() - _height) / 2;
				GLFW.glfwSetWindowPos(_windowId, x, y);

				Log.tracef("Set window location to %d, %d.", x, y);
			}

			Log.trace("Making window visible.");
			GLFW.glfwShowWindow(_windowId);
		}

		Log.unindent();
	}

	/**
	 * disposeWindow tries to destroy the current window. If the window is
	 * open, the function returns.
	 */
	@Override
	public void disposeWindow()
	{
		if (!isWindowOpen())
		{
			Log.warn("Tried to dispose window, but window is not currently open.");
			return;
		}

		Log.info("Disposing GLFW window.");
		synchronized (_lock)
		{
			Log.debug("  Freeing all callbacks.");
			Callbacks.glfwFreeCallbacks(_windowId);

			Log.debug("  Destroying window.");
			GLFW.glfwDestroyWindow(_windowId);
			_windowId = MemoryUtil.NULL;

			Log.debug("  Destroying GLFW instance.");
			GLFW.glfwTerminate();
			GLFW.glfwSetErrorCallback(null).free();
		}
	}

	/**
	 * requestClose makes a request to close the window and
	 * adds the setSizeInstant function to the WindowManager
	 * event queue .
	 */
	@Override
	public void requestClose()
	{
		Log.debug("Requesting window to close.");
		synchronized (_lock)
		{
			GLFW.glfwSetWindowShouldClose(_windowId, true);
		}

		if (_windowManager != null)
			_windowManager.addEvent(() ->
			{
				_windowManager.setSizeInstant(_width, _height);
			});
	}

	/**
	 * endFrame sets the mouse position and swaps the front
	 * and back buffers of the window.
	 * @return the value of the close flag of the window.
	 */
	@Override
	public boolean endFrame()
	{
		if (_windowManager != null)
			_windowManager.onMouseMove(_mouseX, _mouseY);

		synchronized (_lock)
		{
			GLFW.glfwSwapBuffers(_windowId);
			return GLFW.glfwWindowShouldClose(_windowId);
		}
	}

	/**
	 * initGraphics is a synchronized function which initializes
	 * the specified graphics and makes the window the current of
	 * the calling thread. If _vSync is true, there has to be one
	 * screen update before swapping buffers, otherwise no updates
	 * are required.
	 * @param graphics the Graphics that should be initialized.
	 */
	@Override
	public void initGraphics(Graphics graphics)
	{
		Log.info("Linking GLFW window to OpenGL.");

		synchronized (_lock)
		{
			GLFW.glfwMakeContextCurrent(_windowId);
			graphics.init();

			if (_vSync)
				GLFW.glfwSwapInterval(1);
			else
				GLFW.glfwSwapInterval(0);
		}
	}

	/**
	 * setWindowManager sets the WindowManager that should manage this windows'
	 * communication with the main thread.
	 * @param window the WindowManager.
	 */
	@Override
	public void setWindowManager(WindowManager windowManager)
	{
		_windowManager = windowManager;
	}

	/**
	 * updateWindow puts the thread to sleep until there is at least one event in the event queue
	 * or the timeout (0.001 seconds) is reached.
	 */
	@Override
	public void updateWindow()
	{
		GLFW.glfwWaitEventsTimeout(0.001f);
	}

	/**
	 * setCursorEnabled toggles the visibility of the cursor.
	 * @param cursorEnabled boolean that displays (true) or hides (false) the cursor.
	 */
	@Override
	public void setCursorEnabled(boolean cursorEnabled)
	{
		if (cursorEnabled)
			GLFW.glfwSetInputMode(_windowId, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		else
			GLFW.glfwSetInputMode(_windowId, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
	}
}
