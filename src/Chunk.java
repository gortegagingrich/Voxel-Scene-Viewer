import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import provided.SimplexNoise;

import java.util.ArrayList;

/***************************************************************
 * file: Chunk.java
 * author: G. Ortega-Gingrich, C. Kim, N.H. Alsufiani, Y. Yan
 * class: CS 445 – Computer Graphics
 *
 * assignment: Quarter Project - Checkpoint 2
 * date last modified: 5/17/2017
 *
 * purpose: Simple chunk implementation for a static scene
 *
 ****************************************************************/
public class Chunk {
	private static final float EDGE_LENGTH = 16;
	private SimpleChunk[][] chunks;
	private int[][] heightMatrix;
	private ArrayList<float[][]> activeFaces;

	// constructor: Chunk()
	// purpose: generate a height matrix using simplex noise and place 30x30 towers of cubes with generated heights
	public Chunk() {
		int i,j, height;
		int seed;
		SimplexNoise sn;

		seed = (int)System.currentTimeMillis();
		sn = new SimplexNoise(20,0.15, seed);
		heightMatrix = new int[Main.CUBE_COUNT][Main.CUBE_COUNT];
		chunks = new SimpleChunk[Main.CUBE_COUNT][Main.CUBE_COUNT];

		for (i = 0; i < Main.CUBE_COUNT; i++) {
			for (j = 0; j < Main.CUBE_COUNT; j++) {
				height = 1+(int)(20*sn.getNoise(i,j));
				heightMatrix[i][j] = (height > 0) ? height : 0;
				chunks[i][j] = new SimpleChunk(i*EDGE_LENGTH,0,j*EDGE_LENGTH,EDGE_LENGTH,heightMatrix[i][j], Cube.BEDROCK);
			}
		}

		seed *= seed;
		sn = new SimplexNoise(20,0.2, seed);

		for (i = 0; i < Main.CUBE_COUNT; i++) {
			for (j = 0; j < Main.CUBE_COUNT; j++) {
				height = 5+(int)(20*sn.getNoise(i,j));
				SimpleChunk chunk = new SimpleChunk(i*EDGE_LENGTH, 0,j*EDGE_LENGTH, EDGE_LENGTH, height, Cube.STONE);
				chunks[i][j].merge(chunk);
				heightMatrix[i][j] = Math.max(heightMatrix[i][j], height);
			}
		}

		seed *= seed;
		sn = new SimplexNoise(10,0.18, seed);

		for (i = 0; i < Main.CUBE_COUNT; i++) {
			for (j = 0; j < Main.CUBE_COUNT; j++) {
				height = 5+(int)(18*sn.getNoise(i,j));
				SimpleChunk chunk = new SimpleChunk(i*EDGE_LENGTH, 0,j*EDGE_LENGTH, EDGE_LENGTH, height, Cube.DIRT);
				chunks[i][j].merge(chunk);
				heightMatrix[i][j] = Math.max(heightMatrix[i][j], height);
			}
		}

		activeFaces = new ArrayList<>();
		resetFaces();
	}

	// method: draw
	// purpose: draws each active cube face with the proper texture
	public void draw() {
		TexturedCube.texture.bind();
		activeFaces.forEach(face -> {
			float x,y, height, width;
			Object[] value;

			// bind texture and set values for offsets and sizes
			value = TexturedCube.textureLibrary.get((int)face[4][4]);
			x = (Float)value[0];
			y = (Float)value[1];
			width = (Float)value[2];
			height = (Float)value[3];

			// draw activeFaces
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

	// method: resetFaces
	// purpose: clears current set of activeFaces, deactivates unneeded cubes and activeFaces, and rebuilds active face set
	private void resetFaces() {
		SimpleChunk chunk;

		activeFaces.clear();

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
				chunk.addFaces(activeFaces);
			}
		}
	}
}
