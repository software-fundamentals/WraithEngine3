package whg.WraithEngine;

public class SkinnedMesh extends Mesh
{
	private Skeleton _skeleton;

	public SkinnedMesh(VertexData vertexData, Skeleton skeleton)
	{
		super(vertexData);
		
		_skeleton = skeleton;
	}
	
	public Skeleton getSkeleton()
	{
		return _skeleton;
	}
	
	public void setSkeleton(Skeleton skeleton)
	{
		_skeleton = skeleton;
	}
}
