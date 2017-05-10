import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;

/**
 * Created by Gabriel on 2017/05/09.
 */
public class TexturedCube implements Cube {
	private int type;
	ArrayList<float[][]> faces;
	private float[][] vertices;

	public static final int CUBE = 0;
	private static final HashMap<Integer, Object[]> textureLibrary = new HashMap<>();

	public TexturedCube(float x, float y, float z, float edgeLength) {
		this(x,y,z,edgeLength,CUBE);
	}

	public TexturedCube(float x, float y, float z, float edgeLength, int type) {
		this.faces = new ArrayList<>();
		this.type = type;

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

	public void draw() {
		float[][] side;
		Object[] value;
		float x,y,size;

		value = textureLibrary.get(type);
		((Texture)value[0]).bind();
		x = (Float)value[1];
		y = (Float)value[2];
		size = (Float)value[3];

		// assumes that the associated texture is of the following format:
		// SIDES  TOP
		// BOT    []

		// sides
		for (int i = 0; i < 4; i++) {
			side = faces.get(i);

			GL11.glTexCoord2f(x, y);
			GL11.glVertex3f(side[0][0], side[0][1], side[0][2]);

			GL11.glTexCoord2f(x+size, y);
			GL11.glVertex3f(side[1][0], side[1][1], side[1][2]);

			GL11.glTexCoord2f(x+size,y+size);
			GL11.glVertex3f(side[2][0], side[2][1], side[2][2]);

			GL11.glTexCoord2f(x,y+size);
			GL11.glVertex3f(side[3][0], side[3][1], side[3][2]);
		}

		// top
		side = faces.get(4);
		GL11.glTexCoord2f(x+size,y);
		GL11.glVertex3f(side[0][0], side[0][1], side[0][2]);

		GL11.glTexCoord2f(x+2*size,y);
		GL11.glVertex3f(side[1][0], side[1][1], side[1][2]);

		GL11.glTexCoord2f(x+2*size,y+size);
		GL11.glVertex3f(side[2][0], side[2][1], side[2][2]);

		GL11.glTexCoord2f(x+size,y+size);
		GL11.glVertex3f(side[3][0], side[3][1], side[3][2]);

		// bot
		side = faces.get(5);

		GL11.glTexCoord2f(x,y+size);
		GL11.glVertex3f(side[0][0], side[0][1], side[0][2]);

		GL11.glTexCoord2f(x+size,y+size);
		GL11.glVertex3f(side[1][0], side[1][1], side[1][2]);

		GL11.glTexCoord2f(x+size,y+size*2);
		GL11.glVertex3f(side[2][0], side[2][1], side[2][2]);

		GL11.glTexCoord2f(x,y+size*2);
		GL11.glVertex3f(side[3][0], side[3][1], side[3][2]);

		// unbind texture
		TextureImpl.bindNone();
	}

	private void addFace(int v0, int v1, int v2, int v3) {
		faces.add(new float[][] {
				  vertices[v0],vertices[v1],vertices[v2],vertices[v3],
				  {(float)Math.random(),(float)Math.random(),(float)Math.random()}
		});
	}

	public static void initTextureLibrary() {
		Texture texture;
		Object[] value;
		float sideLength;

		textureLibrary.clear();

		System.out.println("Building texture library...");

		try {
			texture = TextureLoader.getTexture("PNG",
			                                   ResourceLoader.getResourceAsStream("textures/block.png"));
			sideLength = 32f/texture.getImageWidth();

			// define for each type as follows:
			addTextureEntry(CUBE, texture, 0f, 0f, sideLength);
			addTextureEntry(1, texture,2*sideLength, 2*sideLength, sideLength);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void addTextureEntry(int type, Texture texture, float xOffset, float yOffset, float sideLength) {
		Object[] value;

		// {texture, xOffset, yOffset, sideLength}
		value = new Object[] {texture, xOffset, yOffset, sideLength};
		textureLibrary.put(type, value);
	}
}
