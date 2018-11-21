package whg.WraithEngine;

import java.io.File;

import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

public class ModelLoader
{
	public static Mesh loadModel(File file)
	{
		AIScene scene = Assimp.aiImportFile(file.toString(),
				Assimp.aiProcess_Triangulate |
				Assimp.aiProcess_GenSmoothNormals |
				Assimp.aiProcess_FlipUVs |
				Assimp.aiProcess_CalcTangentSpace |
				Assimp.aiProcess_LimitBoneWeights
			);
		
		if (scene == null || scene.mNumMeshes() == 0)
			return null;
		
		AIMesh mesh = AIMesh.create(scene.mMeshes().get(0));
		
		int sizeOfVertex = 3;
		int vertexCount = mesh.mNumVertices();
		float[] vertices = new float[vertexCount * sizeOfVertex];

		int index = 0;
		for (int v = 0; v < vertexCount; v++)
		{
			AIVector3D pos = mesh.mVertices().get(v);
			vertices[index++] = pos.x();
			vertices[index++] = pos.y();
			vertices[index++] = pos.z();
		}
		
		int triangleCount = mesh.mNumFaces();
		short[] triangles = new short[triangleCount * 3];

		// TODO If mesh has larger than 65k vertices, load as int array, not short array.
		// Or break into multiple meshes.
		index = 0;
		for (int f = 0; f < triangleCount; f++)
		{
			AIFace face = mesh.mFaces().get(f);
			triangles[index++] = (short)face.mIndices().get(0);
			triangles[index++] = (short)face.mIndices().get(1);
			triangles[index++] = (short)face.mIndices().get(2);
		}
		
		int[] attributes = new int[] {3};
		VertexData vertexData = new VertexData(vertices, triangles, attributes);
		return new Mesh(vertexData);
	}
}
