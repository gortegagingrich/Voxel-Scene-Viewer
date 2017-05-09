import org.lwjgl.opengl.GL11;
import java.util.ArrayList;

/***************************************************************
 * file: Cube.java
 * author: G. Ortega-Gingrich, C. Kim, N.H. Alsufiani, Y. Yan
 * class: CS 445 – Computer Graphics
 *
 * assignment: Quarter Project - Checkpoint 1
 * date last modified: 5/9/2017
 *
 * purpose: This class describes a cube object with a given
 * position and edge length with random colored faces.
 *
 ****************************************************************/
public class Cube  {
	private ArrayList<float[][]> faces;
	private float[][] vertices;

	// constructor: Cube
	// purpose: creates 8 vertices for a cube at the given position with the given edge length
	// then creates 6 faces with references to those vertices and random colors
	public Cube(float x, float y, float z, float edgeLength) {
		this.faces = new ArrayList<>();

		/*
		rough representation of which vertex is which:
		 7---6
		3---2|
		|4--|5
		0---1
		 */
		vertices = new float[][] {
				  {x,y,z},
				  {x+edgeLength,y,z},
				  {x+edgeLength,y+edgeLength,z},
				  {x,y+edgeLength,z},
				  {x,y,z+edgeLength},
				  {x+edgeLength,y,z+edgeLength},
				  {x+edgeLength,y+edgeLength,z+edgeLength},
				  {x,y+edgeLength,z+edgeLength}
		};

		// I'm not 100% sure about the vertex
		// order for the top and bottom faces
		addFace(0,1,2,3);
		addFace(1,5,6,2);
		addFace(5,4,7,6);
		addFace(4,0,3,7);
		addFace(3,2,6,7);
		addFace(5,4,0,1);
	}

	// method: addFace
	// purpose: adds a face to the set of faces with given vertices and a random color
	// Each face is represeted by a float[5][3], where the first four arrays are vertices
	// and the final is a randomly generated color.
	private void addFace(int v0, int v1, int v2, int v3) {
		faces.add(new float[][] {
				  vertices[v0],vertices[v1],vertices[v2],vertices[v3],
				  {(float)Math.random(),(float)Math.random(),(float)Math.random()}
		});
	}

	// method: draw
	// purpose: for each face, draws a each vertex in the defined color
	public synchronized void draw() {
		faces.forEach(side -> {
			GL11.glColor3f(side[4][0], side[4][1], side[4][2]);

			for (int i = 0; i < 4; i++) {
				GL11.glVertex3f(side[i][0], side[i][1], side[i][2]);
			}
		});
	}
}
