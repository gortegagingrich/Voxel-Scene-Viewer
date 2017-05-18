import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

import java.util.ArrayList;

/***************************************************************
 * file: SimpleChunk.java
 * author: G. Ortega-Gingrich, C. Kim, N.H. Alsufiani, Y. Yan
 * class: CS 445 – Computer Graphics
 *
 * assignment: Quarter Project - Checkpoint 2
 * date last modified: 5/17/2017
 *
 * purpose: This class describes an object
 * that is essentially a tower of cubes with functions that
 * facilitate deactivating activeFaces across ranges of cubes.
 *
 ****************************************************************/
public class SimpleChunk {
	ArrayList<float[][]> activeFaces;
	ArrayList<TexturedCube> cubes;

	// constructor: (xPosition, yPosition, zPosition, edgeLength, height (in cubes))
	// purpose: creates the given number of cubes stacked on top of each other at the given position
	public SimpleChunk(float x, float y, float z, float edgeLength, int height) {
		TexturedCube cube;

		this.activeFaces = new ArrayList<>();
		this.cubes = new ArrayList<>();

		// bottom cube
		cube = new TexturedCube(x,y,z,edgeLength);
		cube.deactivateFace(Cube.TOP);
		cubes.add(cube);

		for (int i = 1; i < height; i++) {
			cube = new TexturedCube(x,i*edgeLength + y,z,edgeLength);
			cube.deactivateFace(Cube.TOP, Cube.BOT);
			cubes.add(cube);
		}

		// top cube
		cube = new TexturedCube(x,height*edgeLength + y,z,edgeLength);
		cube.deactivateFace(Cube.BOT);
		cubes.add(cube);
	}

	public SimpleChunk(float x, float y, float z, float edgeLength, int height, int type) {
		TexturedCube cube;

		this.activeFaces = new ArrayList<>();
		this.cubes = new ArrayList<>();

		// bottom cube
		cube = new TexturedCube(x,y,z,edgeLength, type);
		cube.deactivateFace(Cube.TOP);
		cubes.add(cube);

		for (int i = 1; i < height; i++) {
			cube = new TexturedCube(x,i*edgeLength + y,z,edgeLength, type);
			cube.deactivateFace(Cube.TOP, Cube.BOT);
			cubes.add(cube);
		}

		// top cube
		cube = new TexturedCube(x,height*edgeLength + y,z,edgeLength, type);
		cube.deactivateFace(Cube.BOT);
		cubes.add(cube);
	}

	// method: setFaces
	// purpose: rebuilds set of active faces
	public void setFaces() {
		activeFaces.clear();
		cubes.stream().filter(cube -> cube.isActive()).forEach(cube -> cube.addActiveFaces(activeFaces));
	}

	// method: draw
	// purpose: draws each active face with the appropriate texture
	public void draw() {
		activeFaces.forEach(face -> {
			float x,y, height, width;
			Object[] value;

			// bind texture and set values for offsets and sizes
			value = TexturedCube.textureLibrary.get((int)face[4][4]);
			((Texture)value[0]).bind();
			x = (Float)value[1];
			y = (Float)value[2];
			width = (Float)value[3];
			height = (Float)value[4];

			// draw activeFaces
			GL11.glTexCoord2f(x, y);
			GL11.glVertex3f(face[0][0], face[0][1], face[0][2]);

			GL11.glTexCoord2f(x+width, y);
			GL11.glVertex3f(face[1][0], face[1][1], face[1][2]);

			GL11.glTexCoord2f(x+width,y+height);
			GL11.glVertex3f(face[2][0], face[2][1], face[2][2]);

			GL11.glTexCoord2f(x,y+height);
			GL11.glVertex3f(face[3][0], face[3][1], face[3][2]);

			// unbind texture
			TextureImpl.bindNone();

		});
	}

	// method: deactivateRange
	// purpose: goes through set of cubes from the given minimum height to the given maximum height, and deactivates
	// the faces passed
	public void deactivateRange(int bot, int top, int... face) {
		while (bot < cubes.size() && bot <= top) {
			cubes.get(bot).deactivateFace(face);
			bot++;
		}
	}

	// method: addFaces
	// purpose: adds all active faces to the given list
	public void addFaces(ArrayList<float[][]> list) {
		list.addAll(this.activeFaces);
	}

	public void merge(SimpleChunk chunk) {
		chunk.cubes.forEach(cube -> cubes.add(cube));
	}
}
