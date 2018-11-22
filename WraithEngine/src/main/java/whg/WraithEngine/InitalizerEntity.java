package whg.WraithEngine;

public abstract class InitalizerEntity implements Entity, Updateable
{
	private World _world;
	
	public InitalizerEntity(World world)
	{
		_world = world;
	}

	@Override
	public void update()
	{
		run();
		_world.removeEntity(this);
	}

	@Override
	public Location getLocation()
	{
		return null;
	}
	
	public abstract void run();
}
