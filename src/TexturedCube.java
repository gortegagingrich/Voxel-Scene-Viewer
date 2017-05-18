import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/***************************************************************
 * file: TexturedCube.java
 * author: G. Ortega-Gingrich, C. Kim, N.H. Alsufiani, Y. Yan
 * class: CS 445 – Computer Graphics
 *
 * assignment: Quarter Project - Checkpoint 2
 * date last modified: 5/17/2017
 *
 * purpose: Cube implementation that allows for drawing textured
 * objects
 *
 ****************************************************************/
public class TexturedCube implements Cube {
	private int     type;
	private boolean active;
	ArrayList<float[][]> faces;
	private float[][] vertices;

	public static final HashMap<Integer, Object[]> textureLibrary = new HashMap<>();
	public static Texture texture;

	// constructor: TexturedCube(xPos, yPos, zPos, edgeLength)
	// purpose: creates textured cube with a random type
	public TexturedCube(float x, float y, float z, float edgeLength) {
		this(x,y,z,edgeLength,1+(int)(Math.random()*6));
	}

	// constructor: TexturedCube(xPos, yPos, zPos, edgeLength, type)
	// purpose: creates textured cube with a given type
	public TexturedCube(float x, float y, float z, float edgeLength, int type) {
		this.faces = new ArrayList<>();
		this.type = type;
		this.active = true;
		int type2;

		type2 = (type == GRASS) ? (DIRT) : type;

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

		// modified these to work with test texture
		addFace(2,3,0,1, type2);
		addFace(6,2,1,5, type2);
		addFace(7,6,5,4, type2);
		addFace(3,7,4,0, type2);
		addFace(3,2,6,7, type);
		addFace(1,0,4,5, type2);
	}

	// method: addFace
	// purpose: adds an active face with the given vertex indeces
	private void addFace(int v0, int v1, int v2, int v3, int type) {
		faces.add(new float[][] {
				  vertices[v0],vertices[v1],vertices[v2],vertices[v3],
				  {(float)Math.random(),(float)Math.random(),(float)Math.random(), 1f, type}
		});
	}

	// method: isActive
	// purpose: quick check if the cube is active
	public boolean isActive() {
		return active;
	}

	// method: deactivateFace
	// purpose: deactivates a particular face or set of faces
	public void deactivateFace(int... face) {
		for (int i: face) {
			faces.get(i)[4][3] = 0f;
		}
	}

	// method: addActiveFaces
	// purpose: adds only the active faces to the given list
	public void addActiveFaces(ArrayList<float[][]> list) {
		faces.stream()
				  .forEach(face -> {
				  	   // changed to this because I'm not 100% sure how filter is implemented
				  	   if (face[4][3] == 1f) {
				  	   	list.add(face);
				      }
				  });
	}

	// method: initTextureLibrary
	// purpose: builds the textureLibrary and defines specific textures corresponding to different cube types
	public static void initTextureLibrary(String imgPath, String csvPath) {
		float texWidth, texHeight;

		textureLibrary.clear();

		System.out.println("Building texture library...");

		try {
			// load texture
			texture = TextureLoader.getTexture("PNG",
			                                   ResourceLoader.getResourceAsStream(imgPath));
			texWidth = 32f/texture.getImageWidth();
			texHeight = 32f/texture.getImageHeight();

			// parse associated csv file
			File file;
			Scanner scan;
			String line;
			String[] values;
			int lineNumber;

			System.out.printf("Texture \"%s\" added\n"+
					  "Parsing texture configurations in \"%s\"\n", imgPath, csvPath);

			file = new File(csvPath);
			scan = new Scanner(file);
			lineNumber = 1;

			while (scan.hasNextLine()) {
				line = scan.nextLine();
				values = line.split(",");

				if (values.length == 5 && !values[0].equals("type")) {
					addTextureEntry(Integer.parseInt(values[0]),
							  Float.parseFloat(values[1]) * texWidth,
							  Float.parseFloat(values[2]) * texHeight,
							  Float.parseFloat(values[3]) * texWidth,
							  Float.parseFloat(values[4]) * texHeight);
				} else if (!values[0].equals("type")) {
					System.out.printf("Skipping line %d: \"%s\n\"", lineNumber, line);
				}

				lineNumber++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// method: addTextureEntry
	// purpose: adds an entry to the textureLibrary
	private static void addTextureEntry(int type, float xOffset, float yOffset, float texWidth, float texHeight) {
		Object[] value;

		value = new Object[] {xOffset, yOffset, texWidth, texHeight};
		textureLibrary.put(type, value);

		System.out.printf("added type mapping: %d, %.4f, %.4f, %.4f, %.4f\n",
				  type, xOffset, yOffset, texWidth, texHeight);
	}
}
