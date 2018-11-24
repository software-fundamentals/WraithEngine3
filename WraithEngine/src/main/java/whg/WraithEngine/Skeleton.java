package whg.WraithEngine;

import org.joml.Matrix4f;

public class Skeleton
{
	private Matrix4f _globalInverseTransform;
	private Bone[] _bones;
	private Bone _root;
	
	public Skeleton(Matrix4f globalInverseTransform, Bone[] bones, Matrix4f root)
	{
		_globalInverseTransform = globalInverseTransform;
		_bones = bones;
		_root = new Bone("Root", root, globalInverseTransform);
	}
	
	public Matrix4f getGlobalInverseTransform()
	{
		return _globalInverseTransform;
	}
	
	public Bone[] getBones()
	{
		return _bones;
	}
	
	public Bone getRoot()
	{
		return _root;
	}
}
