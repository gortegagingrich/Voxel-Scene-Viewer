import javafx.util.Pair;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by gabriel on 5/2/17.
 */
public class Cube {
	private ArrayList<Pair<float[],float[][]>> sides; // key = avg pos; value = set of vertices in the side
	private Camera                               camera;
	private Vector3f cameraPos;

	public Cube(float x, float y, float z, float edgeLength, Camera camera) {
		this.camera = camera;
		this.cameraPos = new Vector3f(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
		this.sides = new ArrayList<>();

		// define all 6 sides and color
		// keep z constant
		sides.add(new Pair<>(new float[] {
				  x+0.5f*edgeLength,y+0.5f*edgeLength,z
		}, new float[][] {
				  {x,y,z},
				  {x+edgeLength,y,z},
				  {x+edgeLength,y+edgeLength,z},
				  {x,y+edgeLength,z},
				  {(float)Math.random(),(float)Math.random(),(float)Math.random()}
		}));
		sides.add(new Pair<>(new float[] {
				  x+0.5f*edgeLength, y+0.5f*edgeLength, z+edgeLength
		}, new float[][] {
				  {x,y,z+edgeLength},
				  {x+edgeLength,y,z+edgeLength},
				  {x+edgeLength,y+edgeLength,z+edgeLength},
				  {x,y+edgeLength,z+edgeLength},
				  {(float)Math.random(),(float)Math.random(),(float)Math.random()}
		}));

		// keep y constant
		sides.add(new Pair<>(new float[] {
				  x+0.5f*edgeLength,y,z+0.5f*edgeLength
		}, new float[][] {
				  {x,y,z},
				  {x+edgeLength,y,z},
				  {x+edgeLength,y,z+edgeLength},
				  {x,y,z+edgeLength},
				  {(float)Math.random(),(float)Math.random(),(float)Math.random()}
		}));
		sides.add(new Pair<>(new float[] {
				  x+0.5f*edgeLength, y+edgeLength, z + 0.5f*edgeLength
		}, new float[][] {
				  {x,y+edgeLength,z},
				  {x+edgeLength,y+edgeLength,z},
				  {x+edgeLength,y+edgeLength,z+edgeLength},
				  {x,y+edgeLength,z+edgeLength},
				  {(float)Math.random(),(float)Math.random(),(float)Math.random()}
		}));

		// keep x constant
		sides.add(new Pair<>(new float[] {
				  x, y+0.5f*edgeLength, z+0.5f*edgeLength
		}, new float[][] {
				  {x,y,z},
				  {x,y+edgeLength,z},
				  {x,y+edgeLength,z+edgeLength},
				  {x,y,z+edgeLength},
				  {(float)Math.random(),(float)Math.random(),(float)Math.random()}
		}));
		sides.add(new Pair<>(new float[] {
				  x+edgeLength,y+0.5f*edgeLength,z+0.5f*edgeLength
		}, new float[][] {
				  {x+edgeLength,y,z},
				  {x+edgeLength,y+edgeLength,z},
				  {x+edgeLength,y+edgeLength,z+edgeLength},
				  {x+edgeLength,y,z+edgeLength},
				  {(float)Math.random(),(float)Math.random(),(float)Math.random()}
		}));
	}

	public void reorderSides() {
		// check to see if camera moved
		// if camera moved, sort sides by distance to camera
		// this is to make it draw the furthest sides first
		cameraPos = camera.getPosition();
		sides.sort((side0, side1) -> {
			float dist0, dist1;

			cameraPos = camera.getPosition();
			dist0 = (float)Math.sqrt(
					  (side0.getKey()[0] - cameraPos.x)*(side0.getKey()[0] - cameraPos.x) +
					  (side0.getKey()[1] - cameraPos.y)*(side0.getKey()[1] - cameraPos.y) +
					  (side0.getKey()[2] - cameraPos.z)*(side0.getKey()[2] - cameraPos.z)
			);
			dist1 = (float)Math.sqrt(
					  (side1.getKey()[0] - cameraPos.x)*(side1.getKey()[0] - cameraPos.x) +
					  (side1.getKey()[1] - cameraPos.y)*(side1.getKey()[1] - cameraPos.y) +
					  (side1.getKey()[2] - cameraPos.z)*(side1.getKey()[2] - cameraPos.z)
			);

			return Float.compare(dist0, dist1);
		});
	}

	public synchronized void draw() {
		//reorderSides();

		sides.forEach(side -> {
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(side.getValue()[4][0], side.getValue()[4][1], side.getValue()[4][2]);

			for (int i = 0; i < 4; i++) {
				GL11.glVertex3f(side.getValue()[i][0], side.getValue()[i][1], side.getValue()[i][2]);
			}

			GL11.glEnd();
		});
	}
}
