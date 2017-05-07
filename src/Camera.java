import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.opengl.GL11;

public class Camera {
	private static final float MIN_PITCH = -90;
	private static final float MAX_PITCH = 90;
	private static final float SPEED = 1f;
	private Vector3f position;
	private float    yaw;
	private float    pitch;

	public Camera(float x, float y, float z) {
		position = new Vector3f(x,y,z);
	}

	public void yaw(float f) {
		yaw += f;
	}

	public void pitch(float f) {
		pitch -= f;

		if (pitch > MAX_PITCH) {
			pitch = MAX_PITCH;
		} else if (pitch < MIN_PITCH) {
			pitch = MIN_PITCH;
		}
	}

	public void move3f(float leftRight, float forwardBackward, float upDown) {
		float dx, dz;

		dx = 0;
		dz = 0;

		// set x and z offsets for moving left or right
		if (leftRight != 0) {
			dx += SPEED * leftRight * (float) Math.sin(Math.toRadians(yaw + 90f));
			dz += SPEED * leftRight * (float) Math.cos(Math.toRadians(yaw + 90f));
		}

		// set x and z offsets for moving forward or backward
		if (forwardBackward != 0) {
			dx += SPEED * forwardBackward * (float) Math.sin(Math.toRadians(yaw));
			dz += SPEED * forwardBackward * (float) Math.cos(Math.toRadians(yaw));
		}

		// move according to calculated offsets
		// might want to change this so that it doesn't move significantly faster diagonally
		position.x -= dx;
		position.z += dz;

		// move up or down
		position.y -= upDown * SPEED;
	}

	public void lookThrough() {
		GL11.glRotatef(pitch,1f,0f,0f);
		GL11.glRotatef(yaw,0f,1f,0f);
		GL11.glTranslatef(position.x, position.y, position.z);
	}

	public Vector3f getPosition() {
		return new Vector3f(position.x, position.y, position.z);
	}
}
