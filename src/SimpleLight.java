import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

/***************************************************************
 * file: Main.java
 * author: G. Ortega-Gingrich, C. Kim, N.H. Alsufiani, Y. Yan
 * class: CS 445 – Computer Graphics
 *
 * assignment: Quarter Project - Checkpoint 3
 * date last modified: 5/30/2017
 *
 * purpose: Class used to configure a simple light source
 *
 ****************************************************************/
public class SimpleLight {
	private FloatBuffer lightPosition, whiteLight;
	private boolean tint;

	// constructor: SimpleLight
	// purpse: initializes lightPosition and whiteLight buffers
	public SimpleLight() {
		lightPosition = BufferUtils.createFloatBuffer(4);
		whiteLight = BufferUtils.createFloatBuffer(4);

		lightPosition.put(new float[] {0, 0, 0, 1});
		whiteLight.put(new float[] {0.9f,1,1,0});
		lightPosition.flip();
		whiteLight.flip();

		tint = false;
	}

	// method: setLightBuffer
	// purpose: changes the position of the light source.
	// used to compensate for camera movement
	public void setLightBuffer(Vector3f lPos) {
		lightPosition = BufferUtils.createFloatBuffer(4);
		//lightPosition.put(new float[] {lPos.x, lPos.y, lPos.z,1});
		lightPosition.put(lPos.x).put(lPos.y).put(lPos.z).put(1).flip();

		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPosition);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, whiteLight);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, whiteLight);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, whiteLight);
	}

	// method: setTint
	// purpose: used for water collisions to say whether the screen should be tinted blue
	public void setTint(boolean tint) {
		if (this.tint != tint) {
			this.tint = tint;
			whiteLight = BufferUtils.createFloatBuffer(4);
			whiteLight.put((tint) ? new float[]{0.5f, 0.5f, 1f, 0} : new float[]{0.9f, 1, 1, 0});
			whiteLight.flip();
		}
	}

	// method: init
	// purpose: sets GL settings for lighting
	public void init() {
		GL11.glLightModelf(GL11.GL_LIGHT_MODEL_TWO_SIDE, GL11.GL_TRUE);

		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPosition);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, whiteLight);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, whiteLight);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, whiteLight);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
	}
}
