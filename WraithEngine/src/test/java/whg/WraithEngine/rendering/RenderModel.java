package whg.WraithEngine.rendering;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import whg.WraithEngine.Camera;
import whg.WraithEngine.DefaultKeyboardHandler;
import whg.WraithEngine.FPSLogger;
import whg.WraithEngine.Input;
import whg.WraithEngine.KeyboardEventsHandler;
import whg.WraithEngine.Location;
import whg.WraithEngine.Mesh;
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
		float[] vertices = new float[]
			{
				-0.5f, -0.5f, 0f,
				1f, 0f, 0f,
				
				0.5f, -0.5f, 0f,
				0f, 1f, 0f,

				0f, 0.5f, 0f,
				0f, 0f, 1f,
			};
		short[] triangles = new short[] {0, 1, 2};
		int[] attribs = new int[] {3, 3};
		
		VertexData data = new VertexData(vertices, triangles, attribs);
		return new Mesh(data);
	}
	
	private static Shader buildShader()
	{
		String vert = "#version 330 core\n"
				+ "uniform mat4 mvpMat;\n"
				+ "layout(location = 0) in vec3 vertPos;\n"
				+ "layout(location = 1) in vec3 vertCol;\n"
				+ "out vec3 col;\n"
				+ "void main(){\n"
				+ "gl_Position = mvpMat * vec4(vertPos, 1.0);\n"
				+ "col = vertCol;\n"
				+ "}";
		String frag = "#version 330 core\n"
				+ "in vec3 col;\n"
				+ "out vec4 color;\n"
				+ "void main(){\n"
				+ "vec3 c = pow(col, vec3(1.0 / 2.2));\n" // Convert Linear Color Space
				+ "color = vec4(c, 1.0);\n"
				+ "}";
		
		Shader shader = new Shader(vert, frag);
		shader.loadUniform("mvpMat");
		return shader;
	}
	
	private Camera _camera;
	private DefaultKeyboardHandler _keyboardHandler;
	
	@Override
	public void loop(Window window)
	{
		// DEBUG
		System.out.println("OpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));
		System.out.println("GLSL Version: " + GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));

		// INIT
		FPSLogger fps = new FPSLogger();
		GL11.glClearColor(0.2f, 0.4f, 0.8f, 1f);

		Mesh mesh = buildMesh();
		Shader shader = buildShader();
		shader.bind();

		_camera = new Camera();
		_camera.getLocation().setPosition(new Vector3f(0f, 5f, 0f));
		_camera.getLocation().setRotation(new Quaternionf().rotateY(0.5f));
		
		Location meshLocation = new Location();
		meshLocation.setPosition(new Vector3f(0f, 0f, -10f));

		FloatBuffer matrixFloatBuffer = BufferUtils.createFloatBuffer(16);
		Matrix4f projectionMatrix = new Matrix4f();
		Matrix4f viewMatrix = new Matrix4f();
		Matrix4f modelMatrix = new Matrix4f();
		Matrix4f mvpMatrix = new Matrix4f(); 
		
		// LOOP
		while(!window.isRequestingClose())
		{
			// UPDATE
			Time.updateTime();
			fps.logFramerate();
			window.pollEvents();
			updatePlayerControls();
			
			// RENDER
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
			_camera.getProjectionMatrix(projectionMatrix);
			_camera.getViewMatrix(viewMatrix);
			meshLocation.getMatrix(modelMatrix);
			
			mvpMatrix.set(projectionMatrix);
			mvpMatrix.mul(viewMatrix);
			mvpMatrix.mul(modelMatrix);
			mvpMatrix.get(matrixFloatBuffer);
			shader.setUniformMat4("mvpMat", matrixFloatBuffer);

			mesh.render();
			
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
			window.endFrame();
		}
		
		// DISPOSE
		mesh.dispose();
		shader.dispose();
	}
	
	private void updatePlayerControls()
	{
		float delta = Time.deltaTime();
		Vector3f velocity = new Vector3f();
		
		if (Input.isKeyHeld("space"))
			velocity.y += 1f;
		if (Input.isKeyHeld("shift"))
			velocity.y -= 1f;
		if (Input.isKeyHeld("w"))
			velocity.z -= 1f;
		if (Input.isKeyHeld("s"))
			velocity.z += 1f;
		if (Input.isKeyHeld("a"))
			velocity.x -= 1f;
		if (Input.isKeyHeld("d"))
			velocity.x += 1f;
		
		if (velocity.lengthSquared() == 0f)
			return;

		velocity.normalize();
		velocity.mulDirection(_camera.getLocation().getMatrix());
		velocity.mul(delta).mul(7f); // 7 m/s
		
		Vector3f pos = _camera.getLocation().getPosition();
		pos.add(velocity);
		_camera.getLocation().setPosition(pos);
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
		if (_keyboardHandler == null)
			_keyboardHandler = new DefaultKeyboardHandler();
		return _keyboardHandler;
	}
 }
