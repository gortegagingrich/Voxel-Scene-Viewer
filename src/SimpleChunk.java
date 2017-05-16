import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

import java.util.ArrayList;

/**
 * Created by Gabriel on 2017/05/13.
 */
public class SimpleChunk {
	ArrayList<float[][]> faces;
	ArrayList<TexturedCube> cubes;

	public SimpleChunk(float x, float y, float z, float edgeLength, int height) {
		TexturedCube cube;

		this.faces = new ArrayList<>();
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

	public void setFaces() {
		faces.clear();
		cubes.stream().filter(cube -> cube.isActive()).forEach(cube -> cube.addActiveFaces(faces));
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

			// unbind texture
			TextureImpl.bindNone();

		});
	}

	public void deactivateRange(int bot, int top, int... face) {
		while (bot < cubes.size() && bot <= top) {
			cubes.get(bot).deactivateFace(face);
			bot++;
		}
	}
}
