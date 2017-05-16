import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import provided.SimplexNoise;

import java.util.ArrayList;

/**
 * Created by Gabriel on 2017/05/16.
 */
public class Chunk {
	private static final float EDGE_LENGTH = 16;
	private SimpleChunk[][] chunks;
	private int[][] heightMatrix;
	private ArrayList<float[][]> faces;

	public Chunk() {
		int i,j;
		SimplexNoise sn;

		sn = new SimplexNoise(20,0.15, (int)System.currentTimeMillis());
		heightMatrix = new int[Main.CUBE_COUNT][Main.CUBE_COUNT];
		chunks = new SimpleChunk[Main.CUBE_COUNT][Main.CUBE_COUNT];

		for (i = 0; i < Main.CUBE_COUNT; i++) {
			for (j = 0; j < Main.CUBE_COUNT; j++) {
				heightMatrix[i][j] = 10 + (int)(20*sn.getNoise(i,j));//(int)sn.getNoise(i,j);
				chunks[i][j] = new SimpleChunk(i*EDGE_LENGTH,0,j*EDGE_LENGTH,EDGE_LENGTH,heightMatrix[i][j]);
			}
		}

		faces = new ArrayList<>();
		cleanseFaces();
	}

	public void draw() {
		faces.forEach(face -> {
			float x,y, height, width;
			Object[] value;

			// bind texture and set values for offsets and sizes
			value = TexturedCube.textureLibrary.get((int)face[4][4]);
			((Texture)value[0]).bind();
			x = (Float)value[1];
			y = (Float)value[2];
			width = (Float)value[3];
			height = (Float)value[4];

			// draw faces
			GL11.glTexCoord2f(x, y);
			GL11.glVertex3f(face[0][0], face[0][1], face[0][2]);

			GL11.glTexCoord2f(x+width, y);
			GL11.glVertex3f(face[1][0], face[1][1], face[1][2]);

			GL11.glTexCoord2f(x+width,y+height);
			GL11.glVertex3f(face[2][0], face[2][1], face[2][2]);

			GL11.glTexCoord2f(x,y+height);
			GL11.glVertex3f(face[3][0], face[3][1], face[3][2]);

		});
	}

	private void cleanseFaces() {
		SimpleChunk chunk;

		faces.clear();

		for (int i = 0; i < Main.CUBE_COUNT; i++) {
			for (int j = 0; j < Main.CUBE_COUNT; j++) {
				chunk = chunks[i][j];

				if (i > 0) {
					chunk.deactivateRange(0,heightMatrix[i-1][j],Cube.LEFT);
				}
				if (i < Main.CUBE_COUNT-1) {
					chunk.deactivateRange(0,heightMatrix[i+1][j],Cube.RIGHT);
				}
				if (j > 0) {
					chunk.deactivateRange(0,heightMatrix[i][j-1],Cube.FRONT);
				}
				if (j < Main.CUBE_COUNT-1) {
					chunk.deactivateRange(0,heightMatrix[i][j+1],Cube.BACK);
				}

				chunk.setFaces();
				chunk.addFaces(faces);
			}
		}
	}
}
