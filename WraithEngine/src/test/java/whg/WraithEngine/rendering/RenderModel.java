package whg.WraithEngine.rendering;

import java.io.File;
import java.io.IOException;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.Version;
import org.lwjgl.opengl.GL11;
import whg.WraithEngine.DefaultGameLoop;
import whg.WraithEngine.FileUtils;
import whg.WraithEngine.Model;
import whg.WraithEngine.FirstPersonCamera;
import whg.WraithEngine.InitalizerEntity;
import whg.WraithEngine.Location;
import whg.WraithEngine.Material;
import whg.WraithEngine.Mesh;
import whg.WraithEngine.ModelLoader;
import whg.WraithEngine.ModelScene;
import whg.WraithEngine.QuitGameListener;
import whg.WraithEngine.Shader;
import whg.WraithEngine.SkinnedMesh;
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
				
				String path = "C:\\Users\\TheDudeFromCI\\Downloads\\twist_object.fbx";
				ModelScene scene = ModelLoader.loadModel(new File(path));
				
				Mesh floorMesh = buildMesh();
				SkinnedMesh columnMesh = (SkinnedMesh)scene._meshes.get(0);

				columnMesh.getSkeleton().getBones()[2].getLocalTransform().rotateLocalX((float) (Math.PI/2f));
				columnMesh.getSkeleton().getBones()[3].getLocalTransform().rotateLocalX((float) (-Math.PI/2f));
				columnMesh.getSkeleton().getBones()[4].getLocalTransform().rotateLocalX((float) (Math.PI/2f));
				columnMesh.rebuildTransformBuffer();

				Shader shader = buildShader();
				shader.bind();
				
				Material baseMaterial = new Material(shader);
				Model floor = new Model(floorMesh, new Location(), baseMaterial);
				Model column = new Model(columnMesh, new Location(), baseMaterial);
				
				column.getLocation().setRotation(new Quaternionf().rotateLocalX((float) (-Math.PI/2f)));
				
				world.addEntity(floor);
				world.addEntity(column);

				columnMesh.sendToShader(shader);
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
		int vertexSize = 3 + 3 + 4 + 4;
		float[] vertices = new float[cellCount * 4 * vertexSize];
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

				// Vertex 1
				// Pos
				vertices[vertIndex++] = a;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = b;

				// Normal
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 1f;
				vertices[vertIndex++] = 0f;

				// Bone Weights
				vertices[vertIndex++] = 10f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 1f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 0f;

				// Vertex 2
				// Pos
				vertices[vertIndex++] = a + cellCenterSize;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = b;

				// Normal
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 1f;
				vertices[vertIndex++] = 0f;

				// Bone Weights
				vertices[vertIndex++] = 10f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 1f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 0f;

				// Vertex 3
				// Pos
				vertices[vertIndex++] = a + cellCenterSize;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = b + cellCenterSize;

				// Normal
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 1f;
				vertices[vertIndex++] = 0f;

				// Bone Weights
				vertices[vertIndex++] = 10f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 1f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 0f;

				// Vertex 4
				// Pos
				vertices[vertIndex++] = a;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = b + cellCenterSize;

				// Normal
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 1f;
				vertices[vertIndex++] = 0f;

				// Bone Weights
				vertices[vertIndex++] = 10f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 1f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 0f;
				vertices[vertIndex++] = 0f;

				triangles[triIndex++] = (short) (quadCount * 4 + 0);
				triangles[triIndex++] = (short) (quadCount * 4 + 1);
				triangles[triIndex++] = (short) (quadCount * 4 + 2);
				triangles[triIndex++] = (short) (quadCount * 4 + 0);
				triangles[triIndex++] = (short) (quadCount * 4 + 2);
				triangles[triIndex++] = (short) (quadCount * 4 + 3);
				
				quadCount++;
			}
		}

		int[] attribs = new int[] {3, 3, 4, 4};
		
		VertexData data = new VertexData(vertices, triangles, attribs);
		return new Mesh(data);
	}
	
	private static Shader buildShader()
	{
		String vert, frag;
		try
		{
			vert = FileUtils.loadFileAsString(FileUtils.getResource("shader.vert"));
			frag = FileUtils.loadFileAsString(FileUtils.getResource("shader.frag"));
		}
		catch(IOException exception)
		{
			System.err.println("Failed to load shader!");
			// TODO Return default shader
			return null;
		}
		
		Shader shader = new Shader(vert, frag);
		shader.loadUniform("_mvpMat");
		shader.loadUniform("_bones");
		return shader;
	}
 }
