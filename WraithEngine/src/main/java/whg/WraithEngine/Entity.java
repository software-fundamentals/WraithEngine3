package whg.WraithEngine;

public class Entity
{
	private Mesh _mesh;
	private Location _location;
	private Material _material;
	
	public Entity(Mesh mesh, Location location, Material material)
	{
		_mesh = mesh;
		_location = new Location(location);
		_material = material;
	}
	
	public Mesh getMesh()
	{
		return _mesh;
	}
	
	public Location getLocation()
	{
		return _location;
	}
	
	public Material getMaterial()
	{
		return _material;
	}
	
	public void render(Camera camera)
	{
		_material.setMVPUniform(camera, _location);
		_mesh.render();
	}
}
