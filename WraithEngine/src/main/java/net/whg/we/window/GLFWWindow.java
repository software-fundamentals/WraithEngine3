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
public class GLFWWindow extends AbstractDesktopWindow
{
	private long _windowId = 0;
	private Object _lock = new Object();
	private float _mouseX;
	private float _mouseY;
	private WindowCallback _windowCallback;

	public GLFWWindow()
	{
		super("WraithEngine Project", false, false, 640, 480);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWindowCallback(WindowCallback windowCallback)
	{
		_windowCallback = windowCallback;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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

			Log.tracef("Requesting resizable, %s", resizable());
			if (!resizable())
				GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

			Log.trace("Building hidden window.");
			_windowId =
				GLFW.glfwCreateWindow(width(), height(), name(), MemoryUtil.NULL, MemoryUtil.NULL);
			Log.tracef("  Recieved window id %d.", _windowId);
			if (_windowId == 0)
				throw new RuntimeException("Failed to create GLFW window!");

			Log.trace("Creating GLFW callbacks.");
			{
				Log.indent();

				Log.trace("Creating window size callback.");
				GLFW.glfwSetWindowSizeCallback(_windowId, (long window, int width, int height) ->
				{
					if (_windowCallback != null)
						_windowCallback.setSize(width(), height());
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

							if (_windowCallback != null)
								_windowCallback.onKey(key, state, mods);
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
					if (_windowCallback != null)
						_windowCallback.onType(key, mods);
				});

				Log.unindent();
			}

			Log.trace("Centering window on primary monitor.");
			try (MemoryStack stack = MemoryStack.stackPush())
			{
				GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

				int x = (vidmode.width() - width()) / 2;
				int y = (vidmode.height() - height()) / 2;
				GLFW.glfwSetWindowPos(_windowId, x, y);

				Log.tracef("Set window location to %d, %d.", x, y);
			}

			Log.trace("Making window visible.");
			GLFW.glfwShowWindow(_windowId);
		}

		Log.unindent();
	}

	/**
	 * {@inheritDoc}
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
	 * sends its size to the callback object, if there is one.
	 */
	@Override
	public void requestClose()
	{
		Log.debug("Requesting window to close.");
		synchronized (_lock)
		{
			GLFW.glfwSetWindowShouldClose(_windowId, true);
		}

		if (_windowCallback != null)
			_windowCallback.setSize(width(), height());
	}

	/**
	 * endFrame sets the mouse position and swaps the front
	 * and back buffers of the window.
	 * @return the value of the close flag of the window.
	 */
	@Override
	public boolean endFrame()
	{
		if (_windowCallback != null)
			_windowCallback.onMouseMove(_mouseX, _mouseY);

		synchronized (_lock)
		{
			GLFW.glfwSwapBuffers(_windowId);
			return GLFW.glfwWindowShouldClose(_windowId);
		}
	}

	/**
	 * initGraphics is a synchronized function which initializes
	 * the specified graphics and makes the window the current of
	 * the calling thread. If vSync() is true, there has to be one
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

			if (vSync())
				GLFW.glfwSwapInterval(1);
			else
				GLFW.glfwSwapInterval(0);
		}
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
