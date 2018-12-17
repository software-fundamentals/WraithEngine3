package net.whg.we.utils;

import java.util.HashMap;
import org.lwjgl.glfw.GLFW;

public class Input
{
	public static final int SHIFT_MODIFIER = 1;
	public static final int CONTROL_MODIFIER = 2;
	public static final int ALT_MODIFIER = 4;
	public static final int SUPER_MODIFIER = 8;

	private static boolean[] _keys = new boolean[350];
	private static boolean[] _keysLastFrame = new boolean[350];
	private static HashMap<String, Integer> _keyMap;
	private static float _mouseX, _mouseY;
	private static float _lastMouseX, _lastMouseY;

	static
	{
		loadDefaultKeyBindings();
	}

	private static void loadDefaultKeyBindings()
	{
		_keyMap = new HashMap<>();

		// letters
		_keyMap.put("a", GLFW.GLFW_KEY_A);
		_keyMap.put("b", GLFW.GLFW_KEY_B);
		_keyMap.put("c", GLFW.GLFW_KEY_C);
		_keyMap.put("d", GLFW.GLFW_KEY_D);
		_keyMap.put("e", GLFW.GLFW_KEY_E);
		_keyMap.put("f", GLFW.GLFW_KEY_J);
		_keyMap.put("g", GLFW.GLFW_KEY_G);
		_keyMap.put("h", GLFW.GLFW_KEY_H);
		_keyMap.put("i", GLFW.GLFW_KEY_I);
		_keyMap.put("j", GLFW.GLFW_KEY_J);
		_keyMap.put("k", GLFW.GLFW_KEY_K);
		_keyMap.put("l", GLFW.GLFW_KEY_L);
		_keyMap.put("m", GLFW.GLFW_KEY_M);
		_keyMap.put("n", GLFW.GLFW_KEY_N);
		_keyMap.put("o", GLFW.GLFW_KEY_O);
		_keyMap.put("p", GLFW.GLFW_KEY_P);
		_keyMap.put("q", GLFW.GLFW_KEY_Q);
		_keyMap.put("r", GLFW.GLFW_KEY_R);
		_keyMap.put("s", GLFW.GLFW_KEY_S);
		_keyMap.put("t", GLFW.GLFW_KEY_T);
		_keyMap.put("u", GLFW.GLFW_KEY_U);
		_keyMap.put("v", GLFW.GLFW_KEY_V);
		_keyMap.put("w", GLFW.GLFW_KEY_W);
		_keyMap.put("x", GLFW.GLFW_KEY_X);
		_keyMap.put("y", GLFW.GLFW_KEY_Y);
		_keyMap.put("z", GLFW.GLFW_KEY_Z);

		// numbers
		_keyMap.put("0", GLFW.GLFW_KEY_0);
		_keyMap.put("1", GLFW.GLFW_KEY_1);
		_keyMap.put("2", GLFW.GLFW_KEY_2);
		_keyMap.put("3", GLFW.GLFW_KEY_3);
		_keyMap.put("4", GLFW.GLFW_KEY_4);
		_keyMap.put("5", GLFW.GLFW_KEY_5);
		_keyMap.put("6", GLFW.GLFW_KEY_6);
		_keyMap.put("7", GLFW.GLFW_KEY_7);
		_keyMap.put("8", GLFW.GLFW_KEY_8);
		_keyMap.put("9", GLFW.GLFW_KEY_9);

		// number pad keys
		_keyMap.put("numpad 0", GLFW.GLFW_KEY_KP_0);
		_keyMap.put("numpad 1", GLFW.GLFW_KEY_KP_1);
		_keyMap.put("numpad 2", GLFW.GLFW_KEY_KP_2);
		_keyMap.put("numpad 3", GLFW.GLFW_KEY_KP_3);
		_keyMap.put("numpad 4", GLFW.GLFW_KEY_KP_4);
		_keyMap.put("numpad 5", GLFW.GLFW_KEY_KP_5);
		_keyMap.put("numpad 6", GLFW.GLFW_KEY_KP_6);
		_keyMap.put("numpad 7", GLFW.GLFW_KEY_KP_7);
		_keyMap.put("numpad 8", GLFW.GLFW_KEY_KP_8);
		_keyMap.put("numpad 9", GLFW.GLFW_KEY_KP_9);
		_keyMap.put("numpad decimal", GLFW.GLFW_KEY_KP_DECIMAL);
		_keyMap.put("numpad divide", GLFW.GLFW_KEY_KP_DIVIDE);
		_keyMap.put("numpad multiply", GLFW.GLFW_KEY_KP_MULTIPLY);
		_keyMap.put("numpad subtract", GLFW.GLFW_KEY_KP_SUBTRACT);
		_keyMap.put("numpad add", GLFW.GLFW_KEY_KP_ADD);
		_keyMap.put("numpad enter", GLFW.GLFW_KEY_KP_ENTER);

		// arrow keys
		_keyMap.put("up", GLFW.GLFW_KEY_UP);
		_keyMap.put("left", GLFW.GLFW_KEY_LEFT);
		_keyMap.put("right", GLFW.GLFW_KEY_RIGHT);
		_keyMap.put("down", GLFW.GLFW_KEY_DOWN);

		// major keys
		_keyMap.put("escape", GLFW.GLFW_KEY_ESCAPE);
		_keyMap.put("space", GLFW.GLFW_KEY_SPACE);
		_keyMap.put("caps lock", GLFW.GLFW_KEY_CAPS_LOCK);
		_keyMap.put("number lock", GLFW.GLFW_KEY_NUM_LOCK);
		_keyMap.put("enter", GLFW.GLFW_KEY_ENTER);

		// secondary keys
		_keyMap.put("comma", GLFW.GLFW_KEY_COMMA);
		_keyMap.put("period", GLFW.GLFW_KEY_PERIOD);
		_keyMap.put("apostrophe", GLFW.GLFW_KEY_APOSTROPHE);
		_keyMap.put("backspace", GLFW.GLFW_KEY_BACKSPACE);

		// edge keys
		_keyMap.put("left shift", GLFW.GLFW_KEY_LEFT_SHIFT);
		_keyMap.put("left control", GLFW.GLFW_KEY_LEFT_CONTROL);
		_keyMap.put("left alt", GLFW.GLFW_KEY_LEFT_ALT);
		_keyMap.put("left super", GLFW.GLFW_KEY_LEFT_SUPER);
		_keyMap.put("right shift", GLFW.GLFW_KEY_RIGHT_SHIFT);
		_keyMap.put("right control", GLFW.GLFW_KEY_RIGHT_CONTROL);
		_keyMap.put("right alt", GLFW.GLFW_KEY_RIGHT_ALT);
		_keyMap.put("right super", GLFW.GLFW_KEY_RIGHT_SUPER);

		// aliases
		_keyMap.put("shift", GLFW.GLFW_KEY_LEFT_SHIFT);
		_keyMap.put("control", GLFW.GLFW_KEY_LEFT_CONTROL);
		_keyMap.put("alt", GLFW.GLFW_KEY_LEFT_ALT);
		_keyMap.put("super", GLFW.GLFW_KEY_LEFT_SUPER);
		_keyMap.put("numpad period", GLFW.GLFW_KEY_KP_DECIMAL);
	}

	public static void setKeyPressed(int key, boolean pressed)
	{
		if (key < 0 || key >= 350)
			return;
		_keys[key] = pressed;
	}

	public static boolean isKeyHeld(int key)
	{
		return _keys[key];
	}

	public static boolean isKeyHeld(String key)
	{
		return isKeyHeld(getKeyId(key));
	}

	public static boolean isKeyDown(int key)
	{
		return _keys[key] && !_keysLastFrame[key];
	}

	public static boolean isKeyDown(String key)
	{
		return isKeyDown(getKeyId(key));
	}

	public static boolean isKeyUp(int key)
	{
		return !_keys[key] && _keysLastFrame[key];
	}

	public static boolean isKeyUp(String key)
	{
		return isKeyUp(getKeyId(key));
	}

	public static void endFrame()
	{
		for (int i = 0; i < _keys.length; i++)
			_keysLastFrame[i] = _keys[i];
		_lastMouseX = _mouseX;
		_lastMouseY = _mouseY;
	}

	public static int getKeyId(String name)
	{
		return _keyMap.get(name);
	}

	public static void setMousePosition(float mouseX, float mouseY)
	{
		_mouseX = mouseX;
		_mouseY = mouseY;
	}

	public static float getMouseX()
	{
		return _mouseX;
	}

	public static float getMouseY()
	{
		return _mouseY;
	}

	public static float getDeltaMouseX()
	{
		return _mouseX - _lastMouseX;
	}

	public static float getDeltaMouseY()
	{
		return _mouseY - _lastMouseY;
	}
}