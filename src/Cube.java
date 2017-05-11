import org.lwjgl.opengl.GL11;
import java.util.ArrayList;

/***************************************************************
 * file: Cube.java
 * author: G. Ortega-Gingrich, C. Kim, N.H. Alsufiani, Y. Yan
 * class: CS 445 – Computer Graphics
 *
 * assignment: Quarter Project - Checkpoint 1
 * date last modified: 5/9/2017
 *
 * purpose: This class describes a cube object with a given
 * position and edge length with random colored faces.
 *
 ****************************************************************/
public interface Cube  {
	// cube types
	public static final int CUBE = 0, GRASS = 1, SAND = 2, WATER = 3, DIRT = 4, STONE = 5, BEDROCK = 6;
	// face IDs
	public static final int FRONT = 0, RIGHT = 1, BACK = 2, LEFT = 3, TOP = 4, BOT = 5;

	// method: draw
	// purpose: for each face, draws a each vertex in the defined color
	public void draw();

	public void deactivate();
	public boolean isActive();
	public void deactivateFace(int... face);
}
