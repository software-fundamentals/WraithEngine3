package window_handling;

import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.GraphicsFactory;
import net.whg.we.utils.Screen;
import net.whg.we.utils.logging.Log;
import net.whg.we.window.WindowBuilder;
import net.whg.we.window.WindowEngine;
import net.whg.we.window.WindowListenerType;
import net.whg.we.window.WindowManager;

import static org.junit.Assert.*;



import org.junit.Before;
import org.junit.Test;
public class WindowTest {
	
	
	@Before
	public void init() {

	}
	
	
	/*
	 * Tests changing the size of the window, and checking
	 * that the Screen is appropriately set to the same size.
	 */
    @Test
    public void TestSize() throws InterruptedException
    {
    	Log.infof("------------TestSize started-----------");
		Graphics _graphics = GraphicsFactory.createInstance(GraphicsFactory.OPENGL_ENGINE);
		
		WindowBuilder _windowBuilder = new WindowBuilder(WindowEngine.GLFW,WindowListenerType.DEFAULT).setName("WraithEngine")
	                .setResizable(true).setSize(800, 600).setVSync(false)
	             .setGraphicsEngine(_graphics);
		_graphics.init();
    	_windowBuilder.setSize(600,400);
    	
    	
    	WindowManager _window = _windowBuilder.build();
        _window.endFrame();
        //Check that window has the correct width of 600
		Log.infof("WindowManager width: 600");
        assertEquals(600,_window.getWidth());
        //Check that Screen width is 600.
		Log.infof("Screen width: 600");
		assertEquals(600,Screen.width());

		_window.setSize(1000, 1000);
		_window.endFrame();
		Thread.sleep(100);
		//Size is still 600 because window is now open, so no resizing.
		assertEquals(600,_window.getWidth());
	
    }
    
    /*
     * Tests that the window will properly through a null pointer exception if no WindowEngine is given.
     */
    @Test(expected = NullPointerException.class)
    public void TestWindowBuilderNull() {
    	Log.infof("------------TestWindowBuilderNull started-----------");    	

    	Graphics _graphics = GraphicsFactory.createInstance(GraphicsFactory.OPENGL_ENGINE);
        WindowBuilder _windowBuilder = new WindowBuilder(null,null);

    	
    }
    
    /*
     * Tests WindowBuilder if given no listener.
     */
    @Test
    public void TestWindowBuilderNoListener() {
    	Log.infof("------------TestWindowBuilderNoListener started-----------");
    	Graphics _graphics = GraphicsFactory.createInstance(GraphicsFactory.OPENGL_ENGINE);
    	
    	WindowBuilder noListener = new WindowBuilder(WindowEngine.GLFW,WindowListenerType.NO_LISTENER).setName("WraithEngine")
                    .setResizable(true).setSize(800, 600).setVSync(false)
                 .setGraphicsEngine(_graphics);
    	
    	noListener.setSize(600,400);
    	
    	
    	WindowManager _window = noListener.build();
        _window.endFrame();
        //Window will have changed size.
		Log.infof("WindowManager size: 600");
        assertEquals(600,_window.getWidth());

        //Not changed because no listener, so default screen size.
		Log.infof("Screen size: 640");
		assertEquals(640,Screen.width());
    }
    
    /*
     * Tests setVSync: assigns a vSync boolean value to the current
	 * WindowManager if it's not already built.
     */
    @Test
    public void TestWindowSetVSync() {
    	Log.infof("------------TestWindowSetVSync started-----------");
		Graphics _graphics = GraphicsFactory.createInstance(GraphicsFactory.OPENGL_ENGINE);
		
		WindowBuilder _windowBuilder = new WindowBuilder(WindowEngine.GLFW,WindowListenerType.DEFAULT).setName("WraithEngine")
	                .setResizable(true).setSize(800, 600).setVSync(false)
	             .setGraphicsEngine(_graphics);
		_graphics.init();
    	
    	_windowBuilder.setVSync(true);
    	
    	WindowManager _window = _windowBuilder.build();
    	
        _window.endFrame();

		Log.infof("WindowManager vSync: true");
        assertEquals(true,_window.isVSync());
        
        
        _window.setVSync(false);
        
        // Not changed because the windowmanager is already built
        Log.infof("WindowManager vSync: true");
        assertEquals(true,_window.isVSync());

    }
    
    /*
     * Tests setName: assigns a string name to window,
	 * only if a window isn't open already
     */
    @Test
    public void TestWindowName() {
    	Log.infof("------------TestWindowSetName started-----------");
		Graphics _graphics = GraphicsFactory.createInstance(GraphicsFactory.OPENGL_ENGINE);
		
		WindowBuilder _windowBuilder = new WindowBuilder(WindowEngine.GLFW,WindowListenerType.DEFAULT).setName("WraithEngine")
	                .setResizable(true).setSize(800, 600).setVSync(false)
	             .setGraphicsEngine(_graphics);
		_graphics.init();
    	
    	_windowBuilder.setName("testName");
    	
    	WindowManager _window = _windowBuilder.build();
    	
    	 // Not changed because the window is open
    	_window.setName("thisNameShouldNotSet");
    	_window.endFrame();
    	
    	Log.infof(_window.getName());
		Log.infof("WindowManager name: testName");
        assertEquals("testName",_window.getName());
    }
    
    /*
     * Tests setResizable: assigns a boolean to windows resizable prop,
	 * only if a window isn't open already
     */
    @Test
    public void TestWindowResizable() {
    	Log.infof("------------TestWindowResizable started-----------");
		Graphics _graphics = GraphicsFactory.createInstance(GraphicsFactory.OPENGL_ENGINE);
		
		WindowBuilder _windowBuilder = new WindowBuilder(WindowEngine.GLFW,WindowListenerType.DEFAULT).setName("WraithEngine")
	                .setResizable(true).setSize(800, 600).setVSync(false)
	             .setGraphicsEngine(_graphics);
		_graphics.init();
    	
    	_windowBuilder.setResizable(true);
    	
    	WindowManager _window = _windowBuilder.build();
    	
    	 // Not changed because the window is open
    	_window.setResizable(false);
    	_window.endFrame();
    	
		Log.infof("WindowManager resizable: true");
        assertEquals(true,_window.isResizable());
        
        
    	Log.infof("------------ALL WINDOW TESTS COMPLETE-----------");
        
    }
    
    

}
