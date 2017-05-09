import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.util.HashMap;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;

/**
 * Created by Gabriel on 2017/05/09.
 */
public class TexturedCube extends Cube {
	private static final HashMap<Integer, Texture> textureLibrary = new HashMap<>();

	public TexturedCube(float x, float y, float z, float edgeLength) {
		super(x, y, z, edgeLength);
	}

	public void draw() {
		textureLibrary.get(0).bind();

		faces.forEach(side -> {
			GL11.glTexCoord2f(0,0);
			GL11.glVertex3f(side[0][0], side[0][1], side[0][2]);

			GL11.glTexCoord2f(1,0);
			GL11.glVertex3f(side[1][0], side[1][1], side[1][2]);

			GL11.glTexCoord2f(1,1);
			GL11.glVertex3f(side[2][0], side[2][1], side[2][2]);

			GL11.glTexCoord2f(0,1);
			GL11.glVertex3f(side[3][0], side[3][1], side[3][2]);
		});
	}

	public static void initTextureLibrary() {
		Texture texture;

		textureLibrary.clear();

		try {
			texture = TextureLoader.getTexture("PNG",
			                                   ResourceLoader.getResourceAsStream("textures/test.png"));
			textureLibrary.put(0,texture);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
