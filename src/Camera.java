import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.opengl.GL11;

/***************************************************************
 * file: Main.java
 * author: G. Ortega-Gingrich, C. Kim, N.H. Alsufiani, Y. Yan
 * class: CS 445 – Computer Graphics
 *
 * assignment: Quarter Project - Checkpoint 3
 * date last modified: 5/30/2017
 *
 * purpose: This program creates an OpenGL window to display a
 * cube and adjust the camera using they keyboard and mouse
 *
 ****************************************************************/
public class Camera {
	private static final float MIN_PITCH = -90;
	private static final float MAX_PITCH = 90;
	private static final float SPEED = 1f;
	private SimpleLight simpleLight;
	private Vector3f position;
	private Vector3f lightPosition;
	private Main parent;
	private float    yaw;
	private float    pitch;

	// constructor: Camera(float, float, float)
	// purpose: initializes the position to the given point and initializes yaw and pitch.
	public Camera(float x, float y, float z) {
		position = new Vector3f(x,y,z);
		lightPosition = new Vector3f(x,y,z);
		yaw = 0;
		pitch = 0;
		simpleLight = null;
		parent = null;
	}

	// method: setParent
	// purpose: binds private field parent to an instance of Main
	public void setParent(Main parent) {
		this.parent = parent;
	}

	// method: yaw
	// purpose: shift the yaw by the given amount
	// called based on horizontal mouse movement
	public void yaw(float f) {
		yaw += f;
	}

	// method: pitch
	// purpose: shift pitch by given amount
	// called based on vertical mouse movement
	public void pitch(float f) {
		pitch -= f;

		// make sure the pitch doesn't go too high or too low
		if (pitch > MAX_PITCH) {
			pitch = MAX_PITCH;
		} else if (pitch < MIN_PITCH) {
			pitch = MIN_PITCH;
		}
	}

	// method: move3f
	// purpose: adjusts X and Z position based on first two floats relative to yaw
	// and Y position based on third float
	public void move3f(float leftRight, float frontBack, float upDown) {
		float dx, dz;

		if (parent.getScene() == null) {
			return; // no need to move if there is no scene
		}

		dx = 0;
		dz = 0;

		// set x and z offsets for moving left or right
		if (leftRight != 0) {
			dx += SPEED * leftRight * (float) Math.sin(Math.toRadians(yaw + 90f));
			dz += SPEED * leftRight * (float) Math.cos(Math.toRadians(yaw + 90f));
		}

		// set x and z offsets for moving forward or backward
		if (frontBack != 0) {
			dx += SPEED * frontBack * (float) Math.sin(Math.toRadians(yaw));
			dz += SPEED * frontBack * (float) Math.cos(Math.toRadians(yaw));
		}

		// move according to calculated offsets
		// might want to change this so that it doesn't move significantly faster diagonally
		if (!parent.getScene().pointCollision(position.x-dx, position.y, position.z)) {
			position.x -= dx;
			lightPosition.x -= dx;
		}

		if (!parent.getScene().pointCollision(position.x, position.y, position.z+dz)) {
			position.z += dz;
			lightPosition.z += dz;
		}

		// move up or down
		if (!parent.getScene().pointCollision(position.x, position.y - upDown * SPEED, position.z)) {
			position.y -= upDown * SPEED;
		}

		if (parent != null && parent.getScene() != null) {
			if (parent.getScene().isUnderWater(this.position)) {
				simpleLight.setTint(true);
			} else {
				simpleLight.setTint(false);
			}
		}
	}

	// method: lookThrough
	// purpose: performs the required rotations and translation to view from the camera's position with the
	// camera's yaw and pitch
	public void lookThrough() {
		GL11.glRotatef(pitch,1f,0f,0f);
		GL11.glRotatef(yaw,0f,1f,0f);
		GL11.glTranslatef(position.x, position.y, position.z);

		if (simpleLight == null) {
			simpleLight = new SimpleLight();
			simpleLight.init();
		}
		simpleLight.setLightBuffer(lightPosition);
	}

	// method: getPosition
	// purpose: returns a copy of the Vector3f containing the camera's position
	public Vector3f getPosition() {
		return new Vector3f(position.x, position.y, position.z);
	}
}
