import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
		addFace(2,3,0,1);
		addFace(6,2,1,5);
		addFace(7,6,5,4);
		addFace(3,7,4,0);
		addFace(3,2,6,7);
		addFace(1,0,4,5);
	}

	// method: draw
	// purpose: draws each of the cube's active faces with the type's corresponding texture
	public void draw() {
		Object[] value;
		float x,y,width, height;

		value = textureLibrary.get(type);
		((Texture)value[0]).bind();
		x = (Float)value[1];
		y = (Float)value[2];
		width = (Float)value[3];
		height = (Float)value[4];

		// only draw active activeFaces
		faces.stream()
				  .filter(face -> face[4][3] == 1f)
				  .forEach(face -> {
					  GL11.glTexCoord2f(x, y);
					  GL11.glVertex3f(face[0][0], face[0][1], face[0][2]);

					  GL11.glTexCoord2f(x+width, y);
					  GL11.glVertex3f(face[1][0], face[1][1], face[1][2]);

					  GL11.glTexCoord2f(x+width,y+height);
					  GL11.glVertex3f(face[2][0], face[2][1], face[2][2]);

					  GL11.glTexCoord2f(x,y+height);
					  GL11.glVertex3f(face[3][0], face[3][1], face[3][2]);
				  });

		// unbind texture
		TextureImpl.bindNone();
	}

	// method: addFace
	// purpose: adds an active face with the given vertex indeces
	private void addFace(int v0, int v1, int v2, int v3) {
		faces.add(new float[][] {
				  vertices[v0],vertices[v1],vertices[v2],vertices[v3],
				  {(float)Math.random(),(float)Math.random(),(float)Math.random(), 1f, type}
		});
	}

	// method: deactivate
	// purpose: set active to false and deactivate all faces
	public void deactivate() {
		this.active = false;
		deactivateFace(FRONT,RIGHT,BACK,LEFT,TOP,BOT);
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
				  .filter(face -> face[4][3] == 1f)
				  .forEach(face -> list.add(face));
	}

	// method: initTextureLibrary
	// purpose: builds the textureLibrary and defines specific textures corresponding to different cube types
	public static void initTextureLibrary() {
		Texture texture;
		Object[] value;
		float texWidth, texHeight;

		textureLibrary.clear();

		System.out.println("Building texture library...");

		try {
			texture = TextureLoader.getTexture("PNG",
			                                   ResourceLoader.getResourceAsStream("textures/block.png"));
			texWidth = 32f/texture.getImageWidth();
			texHeight = 32f/texture.getImageHeight();

			// define for each type as follows:
			addTextureEntry(CUBE, texture, 0f, 0f, texWidth, texHeight);
			addTextureEntry(GRASS, texture, texWidth, 0f, texWidth, texHeight);
			addTextureEntry(SAND, texture, texWidth*2, 0f, texWidth, texHeight);
			addTextureEntry(WATER, texture, texWidth*3, 0f, texWidth, texHeight);
			addTextureEntry(DIRT, texture, 0f, texHeight, texWidth, texHeight);
			addTextureEntry(STONE, texture, texWidth, texHeight, texWidth, texHeight);
			addTextureEntry(BEDROCK, texture, texWidth*2, texHeight, texWidth, texHeight);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// method: addTextureEntry
	// purpose: adds an entry to the textureLibrary
	private static void addTextureEntry(int type, Texture texture, float xOffset, float yOffset, float texWidth, float texHeight) {
		Object[] value;

		value = new Object[] {texture, xOffset, yOffset, texWidth, texHeight};
		textureLibrary.put(type, value);
	}
}
