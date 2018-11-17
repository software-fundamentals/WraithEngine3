package whg.WraithEngine;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FirstPersonCamera extends Camera
{
	private static final float MAX_ANGLE = (float)Math.toRadians(89);
	private static final float TAU = (float)Math.PI * 2f;

	private Vector3f _baseRotation;
	private Vector3f _extraRotation;
	private float _mouseSensitivity = 10f;

	private Vector3f _rotationBuffer = new Vector3f();
	private Quaternionf _rotationStorageBuffer = new Quaternionf();
	
	public FirstPersonCamera()
	{
		_baseRotation = new Vector3f();
		_extraRotation = new Vector3f();
	}
	
	public void setMouseSensitivity(float mouseSensitivity)
	{
		_mouseSensitivity = mouseSensitivity;
	}
	
	public float getMouseSensitivity()
	{
		return _mouseSensitivity;
	}
	
	public Vector3f getBaseRotation(Vector3f buffer)
	{
		buffer.set(_baseRotation);
		return buffer;
	}
	
	public Vector3f getBaseRotation()
	{
		return getBaseRotation(new Vector3f());
	}
	
	public Vector3f getExtraRotation(Vector3f buffer)
	{
		buffer.set(_extraRotation);
		return buffer;
	}
	
	public Vector3f getExtraRotation()
	{
		return getExtraRotation(new Vector3f());
	}
	
	public void updateCameraRotation()
	{
		float yaw = Input.getDeltaMouseX() * Time.deltaTime() * _mouseSensitivity;
		float pitch = Input.getDeltaMouseY() * Time.deltaTime() * _mouseSensitivity;
		
		_rotationBuffer.x = clamp(_baseRotation.x - pitch, -MAX_ANGLE, MAX_ANGLE);
		_rotationBuffer.y = (_baseRotation.y - yaw) % TAU;
		_rotationBuffer.z = _baseRotation.z;

		if (!isValid(_rotationBuffer))
			return;
		
		_baseRotation.set(_rotationBuffer);
		_rotationBuffer.add(_extraRotation);
		
		_rotationStorageBuffer.identity();
		_rotationStorageBuffer.rotateY(_rotationBuffer.y);
		_rotationStorageBuffer.rotateX(_rotationBuffer.x);
		_rotationStorageBuffer.rotateZ(_rotationBuffer.z);
		getLocation().setRotation(_rotationStorageBuffer);
	}
	
	private boolean isValid(Vector3f vec)
	{
		if (Float.isNaN(vec.x))
			return false;
		if (Float.isNaN(vec.y))
			return false;
		if (Float.isNaN(vec.z))
			return false;
		return true;
	}	

	private float clamp(float x, float min, float max)
	{
		if (x < min)
			return min;
		if (x > max)
			return max;
		return x;
	}
}
