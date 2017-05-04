import javafx.util.Pair;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by gabriel on 5/2/17.
 */
public class Cube  {
	private ArrayList<float[][]> faces;

	public Cube(float x, float y, float z, float edgeLength) {
		this.faces = new ArrayList<>();

		// define all 6 sides and color
		// keep z constant
		faces.add( new float[][] {
			{x,y,z},
			{x+edgeLength,y,z},
			{x+edgeLength,y+edgeLength,z},
			{x,y+edgeLength,z},
			{(float)Math.random(),(float)Math.random(),(float)Math.random()}
		});
		faces.add(new float[][] {
			{x,y,z+edgeLength},
			{x+edgeLength,y,z+edgeLength},
			{x+edgeLength,y+edgeLength,z+edgeLength},
			{x,y+edgeLength,z+edgeLength},
			{(float)Math.random(),(float)Math.random(),(float)Math.random()}
		});

		// keep y constant
		faces.add(new float[][] {
			{x,y,z},
			{x+edgeLength,y,z},
			{x+edgeLength,y,z+edgeLength},
			{x,y,z+edgeLength},
			{(float)Math.random(),(float)Math.random(),(float)Math.random()}
		});
		faces.add(new float[][] {
			{x,y+edgeLength,z},
			{x+edgeLength,y+edgeLength,z},
			{x+edgeLength,y+edgeLength,z+edgeLength},
			{x,y+edgeLength,z+edgeLength},
			{(float)Math.random(),(float)Math.random(),(float)Math.random()}
		});

		// keep x constant
		faces.add(new float[][] {
			{x,y,z},
			{x,y+edgeLength,z},
			{x,y+edgeLength,z+edgeLength},
			{x,y,z+edgeLength},
			{(float)Math.random(),(float)Math.random(),(float)Math.random()}
		});
		faces.add(new float[][] {
			{x+edgeLength,y,z},
			{x+edgeLength,y+edgeLength,z},
			{x+edgeLength,y+edgeLength,z+edgeLength},
			{x+edgeLength,y,z+edgeLength},
			{(float)Math.random(),(float)Math.random(),(float)Math.random()}
		});
	}

	public synchronized void draw() {
		GL11.glBegin(GL11.GL_QUADS);

		faces.forEach(side -> {
			GL11.glColor3f(side[4][0], side[4][1], side[4][2]);

			for (int i = 0; i < 4; i++) {
				GL11.glVertex3f(side[i][0], side[i][1], side[i][2]);
			}
		});

		GL11.glEnd();
	}
}
