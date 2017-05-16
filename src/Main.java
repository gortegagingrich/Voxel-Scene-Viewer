import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import java.util.ArrayList;

/***************************************************************
 * file: Main.java
 * author: G. Ortega-Gingrich, C. Kim, N.H. Alsufiani, Y. Yan
 * class: CS 445 – Computer Graphics
 *
 * assignment: Quarter Project - Checkpoint 1
 * date last modified: 5/9/2017
 *
 * purpose: This program creates an OpenGL window to display a
 * cube and adjust the camera using they keyboard and mouse
 *
 ****************************************************************/
public class Main {
	private int screenWidth, screenHeight, frameRate;
	private volatile boolean         shouldExit;
	private          Camera          camera;
	private          ArrayList<Cube> cubes;
	private ArrayList<SimpleChunk> chunks;
	private SimpleScene scene;

	private static final String CAPTION = "Program _";
	public static final int CUBE_COUNT = 30;

	// constructor: Main(int, int, float, float, int)
	// purpose: sets window properties and creates a camera and a random cube
	public Main(int width, int height, float oX, float oY, int frameRate) {
		this.screenWidth = width;
		this.screenHeight = height;
		this.frameRate = frameRate;
		this.shouldExit = false;
		this.camera = new Camera(0, 0, 0);
		this.cubes = new ArrayList<>();
		this.chunks = new ArrayList<>();
		scene = new SimpleScene();
	}

	// method: setExit
	// purpose: sets private variable to tell main loop to break
	public void setExit() {
		shouldExit = true;
	}

	// method: getCamera
	// purpose: returns reference to the camera instance
	public Camera getCamera() { // to be used by InputReader
		return camera;
	}

	// method: getExit
	// purpose: returns whether or not the main loop has exited or should exit
	public boolean getExit() {
		return shouldExit;
	}

	// method: start
	// purpose: creates display, initializes GL, and starts main loop
	public void start() throws LWJGLException {
		Display.setFullscreen(false);

		Display.setDisplayMode(new DisplayMode(screenWidth, screenHeight));
		Display.setTitle(CAPTION);
		Display.create();

		TexturedCube.initTextureLibrary();
		inputInit();
		glInit();
		render();
	}

	// method: glInit
	// purpose: initializes GL to be able to display a 3d scene correctly with a black background
	private void glInit() {
		GL11.glClearColor(0.8667f, 0.9333f, 1f, 1f);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(screenHeight, screenWidth/(float)screenHeight, 0.1f, screenWidth);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	}

	// method: inputInit
	// purpose: creates and starts a new thread to read keyboard inputs
	private void inputInit() {
		Thread thread = new Thread(new InputReader(this));
		thread.start();
	}

	// method: render
	// purpose: contains the main loop that looks through the camera and draws each cube
	private void render() {
		while (!shouldExit && !Display.isCloseRequested()) {
			GL11.glLoadIdentity();
			camera.lookThrough();
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			// insert render stuff

			GL11.glBegin(GL11.GL_QUADS);

			// uses stream() to filter cubes and draw each active one
			scene.draw();

			GL11.glEnd();

			Display.update();
			Display.sync(frameRate);
		}

		shouldExit = true;
		Display.destroy();
	}

	// method: main
	// purpose: static method called to start the program.
	public static void main(String[] args) {
		Main main = new Main(640, 480, 0, 0, 60);
		try {
			main.start();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
}
