package whg.WraithEngine;

import java.util.ArrayList;

public class ResourceLoader
{
	private static ArrayList<DisposableResource> _resources = new ArrayList<>();
	
	public static void addResource(DisposableResource resource)
	{
		_resources.add(resource);
	}
	
	public static void removeResource(DisposableResource resource)
	{
		_resources.remove(resource);
	}
	
	public static void disposeAllResources()
	{
		for (int i = _resources.size() - 1; i >= 0; i--)
		{
			DisposableResource res = _resources.get(i);
			if (!res.isDisposed())
				res.dispose();
		}
		_resources.clear();
	}
}
