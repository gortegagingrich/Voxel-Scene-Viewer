import java.util.ArrayList;

/***************************************************************
 * file: SimpleChunk.java
 * author: G. Ortega-Gingrich, C. Kim, N.H. Alsufiani, Y. Yan
 * class: CS 445 – Computer Graphics
 *
 * assignment: Quarter Project - Checkpoint 3
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

	// constructor: (xPosition, yPosition, zPosition, edgeLength, height (in cubes), type)
	// purpose: creates the given number of cubes stacked on top of each other at the given position
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

	// method: merge
	// purpose: adds the contents of the given simple chunk
	public void merge(SimpleChunk chunk) {
		int curSize = cubes.size();

		chunk.cubes.forEach(cube ->  {
			if (cube.getY() > cubes.get(cubes.size() - 1).getY()) {
				cubes.add(cube);
			}
		});

		if (curSize != cubes.size()) {
			cubes.get(curSize-1).deactivateFace(Cube.TOP);
		}
	}

	// method: getTop
	// purpose: returns the height of the simple chunk
	public TexturedCube getTop() {
		return cubes.get(cubes.size() - 1);
	}

	// method: addCube
	// purpose: adds a cube to the simple chunk
	public void addCube(TexturedCube cube) {
		cubes.add(cube);
	}
}
