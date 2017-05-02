import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.opengl.GL11;

/**
 * Created by gabriel on 5/2/17.
 */
public class Camera {
	private Vector3f position;
	private Vector3f lPosition;

	private float yaw;
	private float pitch;
	private Vector3f me;

	public Camera(float x, float y, float z) {
		position = new Vector3f(x,y,z);
		lPosition = new Vector3f(0f,15f,0f);

	}

	public void yaw(float f) {
		yaw += f;
	}

	public void pitch(float f) {
		pitch -= f;
	}

	public void lookThrough() {
		GL11.glRotatef(pitch,1f,0f,0f);
		GL11.glRotatef(yaw,0f,1f,0f);
		GL11.glTranslatef(position.x, position.y, position.z);
	}
}
