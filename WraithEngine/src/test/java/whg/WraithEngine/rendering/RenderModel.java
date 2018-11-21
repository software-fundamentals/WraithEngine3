package whg.WraithEngine.rendering;

import java.io.File;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.Version;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import whg.WraithEngine.DefaultInputHandler;
import whg.WraithEngine.Entity;
import whg.WraithEngine.FPSLogger;
import whg.WraithEngine.FirstPersonCamera;
import whg.WraithEngine.Input;
import whg.WraithEngine.KeyboardEventsHandler;
import whg.WraithEngine.Location;
import whg.WraithEngine.Material;
import whg.WraithEngine.Mesh;
import whg.WraithEngine.ModelLoader;
import whg.WraithEngine.MouseEventsHandler;
import whg.WraithEngine.RenderLoop;
import whg.WraithEngine.Screen;
import whg.WraithEngine.Shader;
import whg.WraithEngine.Time;
import whg.WraithEngine.VertexData;
import whg.WraithEngine.Window;
import whg.WraithEngine.WindowEventsHandler;
import whg.WraithEngine.WindowManager;

public class RenderModel implements RenderLoop, WindowEventsHandler
{
	public static void main(String[] args)
	{
		System.out.println("LWJGL Version: " + Version.getVersion());
		
		WindowManager windowManager = new WindowManager();
		windowManager.init();
		
		Window window = windowManager.createNewWindow();
		window.setWindowName("Wraith Engine Test");
		window.setRenderLoop(new RenderModel());
		window.setResizable(true);

		window.build();
		window.loop();
		
		windowManager.dispose();
	}
	
	private static Mesh buildMesh()
	{
		int gridSize = 7;
		float cellSize = 5f;
		float cellBorderSize = 1f;
		float cellCenterSize = cellSize - cellBorderSize;
		
		int cellCount = (int)Math.pow(gridSize * 2 + 1, 2);
		float[] vertices = new float[cellCount * 4 * 3];
		short[] triangles = new short[cellCount * 6];
		
		int vertIndex = 0;
		int triIndex = 0;
		short quadCount = 0;
		for (int x = -gridSize; x <= gridSize; x++)
		{
			for (int z = -gridSize; z <= gridSize; z++)
			{
				float a = x * cellSize + cellBorderSize;
				float b = z * cellSize + cellBorderSize;

				vertices[vertIndex++] = a;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = b;

				vertices[vertIndex++] = a + cellCenterSize;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = b;

				vertices[vertIndex++] = a + cellCenterSize;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = b + cellCenterSize;

				vertices[vertIndex++] = a;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = b + cellCenterSize;

				triangles[triIndex++] = (short) (quadCount * 4 + 0);
				triangles[triIndex++] = (short) (quadCount * 4 + 1);
				triangles[triIndex++] = (short) (quadCount * 4 + 2);
				triangles[triIndex++] = (short) (quadCount * 4 + 0);
				triangles[triIndex++] = (short) (quadCount * 4 + 2);
				triangles[triIndex++] = (short) (quadCount * 4 + 3);
				
				quadCount++;
			}
		}

		int[] attribs = new int[] {3};
		
		VertexData data = new VertexData(vertices, triangles, attribs);
		return new Mesh(data);
	}
	
	private static Shader buildShader()
	{
		String vert = "#version 330 core\n"
				+ "uniform mat4 mvpMat;\n"
				+ "layout(location = 0) in vec3 vertPos;\n"
				+ "void main(){\n"
				+ "gl_Position = mvpMat * vec4(vertPos, 1.0);\n"
				+ "}";
		String frag = "#version 330 core\n"
				+ "out vec4 color;\n"
				+ "void main(){\n"
				+ "color = vec4(0.7, 0.7, 0.7, 1.0);\n"
				+ "}";
		
		Shader shader = new Shader(vert, frag);
		shader.loadUniform("mvpMat");
		return shader;
	}
	
	private FirstPersonCamera _camera;
	private DefaultInputHandler _inputHandler;
	
	@Override
	public void loop(Window window)
	{
		// DEBUG
		System.out.println("OpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));
		System.out.println("GLSL Version: " + GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));

		// INIT
		FPSLogger fps = new FPSLogger();
		GL11.glClearColor(0.2f, 0.4f, 0.8f, 1f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		Mesh floorMesh = buildMesh();
		Mesh columnMesh = ModelLoader.loadModel(new File("C:\\Users\\TheDudeFromCI\\Downloads\\twist_object.fbx"));

		Shader shader = buildShader();
		shader.bind();
		
		Material baseMaterial = new Material(shader);
		Entity floor = new Entity(floorMesh, new Location(), baseMaterial);
		Entity column = new Entity(columnMesh, new Location(), baseMaterial);
		
		column.getLocation().setRotation(new Quaternionf(-1f, 0f, 0f, 1f));

		_camera = new FirstPersonCamera();
		_camera.setMouseSensitivity(1f);
		_camera.getLocation().setPosition(new Vector3f(0f, 5f, 0f));
		
		// LOOP
		while(!window.isRequestingClose())
		{
			// UPDATE
			Time.updateTime();
			fps.logFramerate();
			window.pollEvents();

			updateCameraPosition();
			_camera.updateCameraRotation();
			updateQuitGame(window);
			
			// RENDER
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			floor.render(_camera);
			column.render(_camera);
			
			// ERROR CHECK
			int error;
			while ((error = GL11.glGetError()) != GL11.GL_NO_ERROR)
			{
				String codeName;
				switch (error)
				{
					case GL11.GL_INVALID_OPERATION:
						codeName = "Invalid Operation";
						break;
					case GL11.GL_INVALID_ENUM:
						codeName = "Invalid Enum";
						break;
					case GL11.GL_INVALID_VALUE:
						codeName = "Invalid Value";
						break;
					case GL11.GL_OUT_OF_MEMORY:
						codeName = "Out of Memory";
						break;
					case GL30.GL_INVALID_FRAMEBUFFER_OPERATION:
						codeName = "Invalid Framebuffer Operation";
						break;
					default:
						codeName = "Unknown Error";
						break;
				}

				System.err.println("OpenGL Error. " + codeName);
				window.requestClose();
			}
			
			// FINISH
			Input.endFrame();
			window.endFrame();
		}
		
		// DISPOSE
		floorMesh.dispose();
		columnMesh.dispose();
		shader.dispose();
	}
	
	private void updateCameraPosition()
	{
		Vector3f velocity = new Vector3f();
		float movementSpeed = 7f;
		
		if (Input.isKeyHeld("w"))
			velocity.z -= 1f;
		if (Input.isKeyHeld("s"))
			velocity.z += 1f;
		if (Input.isKeyHeld("a"))
			velocity.x -= 1f;
		if (Input.isKeyHeld("d"))
			velocity.x += 1f;
		
		if (velocity.lengthSquared() != 0f)
		{
			velocity.normalize();
			velocity.mulDirection(_camera.getLocation().getMatrix());
		}

		velocity.y = 0f;
		if (Input.isKeyHeld("space"))
			velocity.y += 1f;
		if (Input.isKeyHeld("shift"))
			velocity.y -= 1f;

		if (velocity.lengthSquared() == 0f)
			return;
		
		velocity.mul(Time.deltaTime()).mul(movementSpeed);
		
		Vector3f pos = _camera.getLocation().getPosition();
		pos.add(velocity);
		_camera.getLocation().setPosition(pos);
	}
	
	private void updateQuitGame(Window window)
	{
		if (Input.isKeyDown("escape"))
			window.requestClose();
	}

	@Override
	public void onWindowResized(Window window, int width, int height)
	{
		Screen.updateSize(width, height);
		_camera.rebuildProjectionMatrix();
		GL11.glViewport(0, 0, width, height);
	}

	@Override
	public WindowEventsHandler getWindowEventsHandler()
	{
		return this;
	}

	@Override
	public KeyboardEventsHandler getKeyboardEventsHandler()
	{
		if (_inputHandler == null)
			_inputHandler = new DefaultInputHandler();
		return _inputHandler;
	}

	@Override
	public MouseEventsHandler getMouseEventsHandler()
	{
		if (_inputHandler == null)
			_inputHandler = new DefaultInputHandler();
		return _inputHandler;
	}
 }
