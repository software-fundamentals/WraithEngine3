package whg.WraithEngine;

import org.lwjgl.glfw.GLFW;

public class DefaultKeyboardHandler implements KeyboardEventsHandler
{
	@Override
	public void onKeyPressed(int key, int action, int mods)
	{
		boolean pressed;
		switch (action)
		{
			case GLFW.GLFW_PRESS:
				pressed = true;
				break;
			case GLFW.GLFW_RELEASE:
				pressed = false;
				break;
			default:
				return;
		}

		Input.setKeyPressed(key, pressed);
	}
}
