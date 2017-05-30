import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

/**
 * Created by Habluka on 2017/05/29.
 */
public class SimpleLight {
	private FloatBuffer lightPosition, whiteLight;
	private boolean tint;

	public SimpleLight() {
		lightPosition = BufferUtils.createFloatBuffer(4);
		whiteLight = BufferUtils.createFloatBuffer(4);

		lightPosition.put(new float[] {0, 0, 0, 1});
		whiteLight.put(new float[] {0.9f,1,1,0});
		lightPosition.flip();
		whiteLight.flip();

		tint = false;
	}

	public void setLightBuffer(Vector3f lPos) {
		lightPosition = BufferUtils.createFloatBuffer(4);
		//lightPosition.put(new float[] {lPos.x, lPos.y, lPos.z,1});
		lightPosition.put(lPos.x).put(lPos.y).put(lPos.z).put(1).flip();

		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPosition);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, whiteLight);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, whiteLight);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, whiteLight);
	}

	public void setTint(boolean tint) {
		if (this.tint != tint) {
			this.tint = tint;
			whiteLight = BufferUtils.createFloatBuffer(4);
			whiteLight.put((tint) ? new float[]{0.5f, 0.5f, 1f, 0} : new float[]{0.9f, 1, 1, 0});
			whiteLight.flip();
		}
	}

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
