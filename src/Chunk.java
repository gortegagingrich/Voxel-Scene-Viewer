import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import provided.SimplexNoise;

import java.nio.FloatBuffer;
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
	private int vboVertex, vboColor, vboTexture;
	private FloatBuffer vertices, textures;

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
		SimpleChunk[][] stone = new SimpleChunk[Main.CUBE_COUNT][Main.CUBE_COUNT];
		SimpleChunk[][] dirt = new SimpleChunk[Main.CUBE_COUNT][Main.CUBE_COUNT];
		ArrayList<TexturedCube> water = new ArrayList<>();

		// add bedrock layer
		for (i = 0; i < Main.CUBE_COUNT; i++) {
			for (j = 0; j < Main.CUBE_COUNT; j++) {
				height = 1+(int)(20*sn.getNoise(i,j));
				heightMatrix[i][j] = (height > 0) ? height : 1;
				chunks[i][j] = new SimpleChunk(i*EDGE_LENGTH,0,j*EDGE_LENGTH,EDGE_LENGTH,heightMatrix[i][j], Cube.BEDROCK);
			}
		}

		seed *= seed;
		sn = new SimplexNoise(20,0.20, seed);

		// add stone layer
		for (i = 0; i < Main.CUBE_COUNT; i++) {
			for (j = 0; j < Main.CUBE_COUNT; j++) {
				height = 5+(int)(20*sn.getNoise(i,j));
				stone[i][j] = new SimpleChunk(i * EDGE_LENGTH, 0, j * EDGE_LENGTH, EDGE_LENGTH, height, Cube.STONE);

				if (height > heightMatrix[i][j]) {
					chunks[i][j].merge(stone[i][j]);
					heightMatrix[i][j] = height;
				}
			}
		}

		seed *= seed;
		sn = new SimplexNoise(20,0.20, seed);

		// add dirt layer
		for (i = 0; i < Main.CUBE_COUNT; i++) {
			for (j = 0; j < Main.CUBE_COUNT; j++) {
				height = 7+(int)(18*sn.getNoise(i,j));
				dirt[i][j] = new SimpleChunk(i * EDGE_LENGTH, 0, j * EDGE_LENGTH, EDGE_LENGTH, height, Cube.DIRT);

				if (height > heightMatrix[i][j]) {
					chunks[i][j].merge(dirt[i][j]);
					heightMatrix[i][j] = height;
				}
			}
		}

		// add grass and sand
		for (i = 0; i < Main.CUBE_COUNT; i++) {
			for (j = 0; j < Main.CUBE_COUNT; j++) {
				TexturedCube cube;

				if (chunks[i][j].getTop().getType() == Cube.DIRT) {
					cube = new TexturedCube(i*EDGE_LENGTH,chunks[i][j].getTop().getY() + EDGE_LENGTH, j*EDGE_LENGTH, EDGE_LENGTH, Cube.GRASS);
				} else {
					cube = new TexturedCube(i*EDGE_LENGTH,chunks[i][j].getTop().getY() + EDGE_LENGTH, j*EDGE_LENGTH, EDGE_LENGTH, Cube.SAND);
				}

				chunks[i][j].getTop().deactivateFace(Cube.TOP);
				chunks[i][j].addCube(cube);
				heightMatrix[i][j]++;
				cube.deactivateFace(Cube.BOT);
			}
		}

		// deal with water

		// find square with minimum height
		int minHeight;

		minHeight = 99999;

		for (i = 0; i < Main.CUBE_COUNT; i++) {
			for (j = 0; j < Main.CUBE_COUNT; j++) {
				if (heightMatrix[i][j] < minHeight) {
					minHeight = heightMatrix[i][j];
				}
			}
		}

		activeFaces = new ArrayList<>();
		resetFaces();

		// fill water
		fillWater(minHeight+3,Cube.BOT);
		fillWater(minHeight+2,Cube.TOP,Cube.BOT);
		fillWater(minHeight+1,Cube.TOP,Cube.BOT);
	}

	// method: draw
	// purpose: draws each active cube face with the proper texture
	public void draw() {
		TexturedCube.texture.bind();

		activeFaces.forEach(face -> {
			float x,y, height, width;
			float[] value;

			// bind texture and set values for offsets and sizes
			value = TexturedCube.textureLibrary.get((int)face[4][4]);
			x = value[0];
			y = value[1];
			width = value[2];
			height = value[3];

			// draw activeFaces
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glTexCoord2f(x, y);
			GL11.glVertex3f(face[0][0], face[0][1], face[0][2]);

			GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
			GL11.glTexCoord2f(x+width, y);
			GL11.glVertex3f(face[1][0], face[1][1], face[1][2]);

			GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
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

	private void fillWater(int y, int... faces) {
		int i,j;
		ArrayList<TexturedCube> water;
		TexturedCube tempCube;

		water = new ArrayList<>();

		for (i = 0; i < Main.CUBE_COUNT; i++) {
			for (j = 0; j < Main.CUBE_COUNT; j++) {
				if (heightMatrix[i][j] < y) {
					tempCube = new TexturedCube(i*EDGE_LENGTH,y*EDGE_LENGTH,j*EDGE_LENGTH,EDGE_LENGTH,Cube.WATER);
					tempCube.deactivateFace(faces);

					if (i != 0) {
						tempCube.deactivateFace(Cube.LEFT);
					}
					if (i != Main.CUBE_COUNT-1) {
						tempCube.deactivateFace(Cube.RIGHT);
					}
					if (j != 0) {
						tempCube.deactivateFace(Cube.FRONT);
					}
					if (j != Main.CUBE_COUNT-1) {
						tempCube.deactivateFace(Cube.BACK);
					}

					water.add(tempCube);
				}
			}
		}

		// add water cubes to active faces
		water.forEach(cube -> cube.addActiveFaces(activeFaces));
	}


	public int[][] getHeightMatrix() {
		// this would be used for collisions, so it would be called a lot
		// that means safely copying the matrix would not be a good idea
		// be sure not to modify the result
		return heightMatrix;
	}

	public float getHeight(float x, float z) {
		x /= EDGE_LENGTH;
		z /= EDGE_LENGTH;

		return (EDGE_LENGTH) * heightMatrix[(int)x][(int)z];
	}
}
