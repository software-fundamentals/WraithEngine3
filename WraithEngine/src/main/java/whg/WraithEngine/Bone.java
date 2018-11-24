package whg.WraithEngine;

import org.joml.Matrix4f;

public class Bone
{
	private String _boneName;
	private Matrix4f _offset;
	private Matrix4f _transform;
	private Matrix4f _defaultPose;
	
	public Bone(String boneName, Matrix4f offset, Matrix4f defaultPose)
	{
		_boneName = boneName;
		_offset = offset;
		_defaultPose = defaultPose;
		_transform = new Matrix4f();
	}
	
	public String getBoneName()
	{
		return _boneName;
	}
	
	public Matrix4f getOffsetMatrix()
	{
		return _offset;
	}
	
	public Matrix4f getTransform()
	{
		return _transform;
	}
	
	public Matrix4f getDefaultPose()
	{
		return _defaultPose;
	}
}
