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

	// method: draw
	// purpose: for each face, draws a each vertex in the defined color
	public void draw();
}
