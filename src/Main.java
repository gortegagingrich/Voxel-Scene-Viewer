import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import java.util.ArrayList;

public class Main {
	private int screenWidth, screenHeight, frameRate;
	private volatile boolean         shouldExit;
	private          Camera          camera;
	private          ArrayList<Cube> cubes;

	private static final String CAPTION = "Program _";
	private static final int CUBE_COUNT = 1;

	public Main(int width, int height, float oX, float oY, int frameRate) {
		this.screenWidth = width;
		this.screenHeight = height;
		this.frameRate = frameRate;
		this.shouldExit = false;
		this.camera = new Camera(0, 0, 0);
		this.cubes = new ArrayList<>();

		for (int i = 0; i < CUBE_COUNT; i++) {
			this.cubes.add(new Cube((float)(Math.random() * 10) * 16,
			                        (float)(Math.random() * 10) * 16,
			                        (float)(Math.random() * 10) * 16,
			                        (float)Math.random() * 32 + 16));
		}
	}

	public void setExit() {
		shouldExit = true;
	}

	public Camera getCamera() { // to be used by InputReader
		return camera;
	}

	public boolean getExit() {
		return shouldExit;
	}

	public void start() throws LWJGLException {
		Display.setFullscreen(false);

		Display.setDisplayMode(new DisplayMode(screenWidth, screenHeight));
		Display.setTitle(CAPTION);
		Display.create();

		inputInit();
		glInit();
		render();
	}

	private void glInit() {
		GL11.glClearColor(0, 0, 0, 0);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(480f, screenWidth/(float)screenHeight, 0.1f, 500f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	}

	private void inputInit() {
		Thread thread = new Thread(new InputReader(this));
		thread.start();
	}

	private void render() {
		while (!shouldExit && !Display.isCloseRequested()) {
			GL11.glLoadIdentity();
			camera.lookThrough();
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

			// insert render stuff

			GL11.glBegin(GL11.GL_QUADS);

			cubes.forEach(cube -> {
				cube.draw();
			});

			GL11.glEnd();

			Display.update();
			Display.sync(frameRate);
		}

		shouldExit = true;
		Display.destroy();
	}

	public static void main(String[] args) {
		Main main = new Main(640, 480, 0, 0, 60);
		try {
			main.start();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
}
