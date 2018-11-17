package whg.WraithEngine;

public interface RenderLoop
{
	public void loop(Window window);
	
	public WindowEventsHandler getWindowEventsHandler();
	
	public KeyboardEventsHandler getKeyboardEventsHandler();
	
	public MouseEventsHandler getMouseEventsHandler();
}
