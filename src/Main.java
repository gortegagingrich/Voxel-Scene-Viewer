import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Main {
    private int screenWidth, screenHeight, frameRate;
    private float originX, originY;
    private volatile boolean shouldExit;
    private Camera camera;
    private DisplayMode displayMode;

    private static final String CAPTION = "Program _";

    public Main(int width, int height, float oX, float oY, int frameRate) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.frameRate = frameRate;
        this.originX = oX;
        this.originY = oY;
        this.shouldExit = false;
        this.camera = new Camera(0,0,0);
        this.displayMode = null;
    }

    public synchronized void setExit() {
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

       DisplayMode[] modes = Display.getAvailableDisplayModes();

       for (int i = 0; i < modes.length; i++) {
          if (modes[i].getBitsPerPixel() == 32 && modes[i].getHeight() == 480 && modes[i].getWidth() == 640) {
             System.out.println("found");
             displayMode = modes[i];
             break;
          }
       }

       inputInit();
       glInit();
       render();
    }

    private void glInit() {
       GL11.glClearColor(0,0,0,0);
       GL11.glMatrixMode(GL11.GL_PROJECTION);
       GL11.glLoadIdentity();
	    GLU.gluPerspective(100.0f, (float)displayMode.getWidth()/(float)displayMode.getHeight(), 0.1f, 300.0f);
       GL11.glOrtho(originX, originX + screenWidth, originY, originY + screenHeight,300,-400);
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
          GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);


          // insert render stuff
          GL11.glBegin(GL11.GL_POLYGON);
            GL11.glColor3f(1f,1f,1f);
            GL11.glVertex3f(170,90,50);
            GL11.glVertex3f(420,90,50);
            GL11.glVertex3f(420,90,350);
            GL11.glVertex3f(170,90,350);
          GL11.glEnd();

          GL11.glBegin(GL11.GL_POLYGON);
          GL11.glColor3f(0.7f,0.7f,0.7f);
             GL11.glVertex3f(170,390,50);
             GL11.glVertex3f(420,390,50);
             GL11.glVertex3f(420,390,350);
             GL11.glVertex3f(170,390,350);
          GL11.glEnd();

          GL11.glBegin(GL11.GL_POLYGON);
             GL11.glColor3f(0.6f,0.6f,0.6f);
             GL11.glVertex3f(170,90,-150);
             GL11.glVertex3f(170,390,-150);
             GL11.glVertex3f(170,390,350);
             GL11.glVertex3f(170,90,350);
          GL11.glEnd();

          GL11.glBegin(GL11.GL_POLYGON);
             GL11.glColor3f(0.5f,0.5f,0.5f);
             GL11.glVertex3f(420,90,50);
             GL11.glVertex3f(420,390,50);
             GL11.glVertex3f(420,390,350);
             GL11.glVertex3f(420,90,350);
          GL11.glEnd();

          GL11.glBegin(GL11.GL_POLYGON);
             GL11.glColor3f(0.4f,0.4f,0.4f);
             GL11.glVertex3f(420,90,50);
             GL11.glVertex3f(420,390,50);
             GL11.glVertex3f(170,390,50);
             GL11.glVertex3f(170,90, 50);
          GL11.glEnd();

          Display.update();
          Display.sync(frameRate);
       }

       shouldExit = true;
       Display.destroy();
    }

    public static void main(String[] args) {
	// write your code here
      Main main = new Main(640,480,0,0,60);
       try {
          main.start();
       } catch (LWJGLException e) {
          e.printStackTrace();
       }
    }
}
