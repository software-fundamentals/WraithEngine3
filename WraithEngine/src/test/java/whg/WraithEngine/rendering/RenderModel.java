package whg.WraithEngine.rendering;

import java.io.File;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.Version;
import org.lwjgl.opengl.GL11;

import whg.WraithEngine.DefaultGameLoop;
import whg.WraithEngine.Model;
import whg.WraithEngine.FirstPersonCamera;
import whg.WraithEngine.InitalizerEntity;
import whg.WraithEngine.Location;
import whg.WraithEngine.Material;
import whg.WraithEngine.Mesh;
import whg.WraithEngine.ModelLoader;
import whg.WraithEngine.QuitGameListener;
import whg.WraithEngine.Shader;
import whg.WraithEngine.Universe;
import whg.WraithEngine.VertexData;
import whg.WraithEngine.Window;
import whg.WraithEngine.WindowManager;
import whg.WraithEngine.World;

public class RenderModel
{
	public static void main(String[] args)
	{
		System.out.println("LWJGL Version: " + Version.getVersion());
		
		WindowManager windowManager = new WindowManager();
		windowManager.init();
		
		Window window = windowManager.createNewWindow();
		window.setWindowName("Wraith Engine Test");
		window.setResizable(true);
		
		DefaultGameLoop gameLoop = new DefaultGameLoop();
		window.setRenderLoop(gameLoop);
		
		Universe universe = gameLoop.getUniverse();
		World world = new World(gameLoop.getCamera());
		universe.addWorld(world);
		
		FirstPersonCamera firstPersonCamera = new FirstPersonCamera(gameLoop.getCamera());
		firstPersonCamera.setMouseSensitivity(1f);
		world.addEntity(firstPersonCamera);
		
		world.addEntity(new QuitGameListener(window, "escape"));
		world.addEntity(new InitalizerEntity(world)
		{
			@Override
			public void run()
			{
				GL11.glClearColor(0.2f, 0.4f, 0.8f, 1f);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				
				world.getCamera().getLocation().setPosition(new Vector3f(0f, 3f, 5f));
				
				Mesh floorMesh = buildMesh();
				Mesh columnMesh = ModelLoader.loadModel(new File("C:\\Users\\TheDudeFromCI\\Downloads\\twist_object.fbx"));

				Shader shader = buildShader();
				shader.bind();
				
				Material baseMaterial = new Material(shader);
				Model floor = new Model(floorMesh, new Location(), baseMaterial);
				Model column = new Model(columnMesh, new Location(), baseMaterial);
				
				column.getLocation().setRotation(new Quaternionf(-1f, 0f, 0f, 1f));
				
				world.addEntity(floor);
				world.addEntity(column);
			}
		});
		
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
 }
