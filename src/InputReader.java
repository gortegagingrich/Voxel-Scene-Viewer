import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.util.HashMap;

/***************************************************************
 * file: InputReader.java
 * author: G. Ortega-Gingrich, C. Kim, N.H. Alsufiani, Y. Yan
 * class: CS 445 – Computer Graphics
 *
 * assignment: Quarter Project - Checkpoint 2
 * date last modified: 5/9/2017
 *
 * purpose: This Runnable class handles reading all of the
 * keyboard and mouse inputs and any of the associated actions.
 *
 ****************************************************************/
public class InputReader implements Runnable {
   private Main parent;
   private HashMap<Integer, boolean[]> keyStates;
   private float mouseSensitivity;

   // constructor: InputReader(Main)
   // purpose: sets parent object and initializes keyStates
   public InputReader(Main parent) {
      this.parent = parent;
      this.keyStates = new HashMap<>();
      this.mouseSensitivity = 0.5f;

      Mouse.setGrabbed(true);

      // key id gets mapped to {current state, consumed}
      addKey(Keyboard.KEY_ESCAPE);
      addKey(Keyboard.KEY_W);
      addKey(Keyboard.KEY_A);
      addKey(Keyboard.KEY_S);
      addKey(Keyboard.KEY_D);
      addKey(Keyboard.KEY_SPACE);
      addKey(Keyboard.KEY_LSHIFT);
      addKey(Keyboard.KEY_TAB);
   }

   // method: run
   // purpose: contains loop that updates the states in keyStates and performs actions associated with those keys
   // when necessary using keyEvents() and consumeKeyEvents()
   public void run() {
      long start;
      while (!parent.getExit()) {
         start = System.currentTimeMillis();

         // update key states
         updateKeyStates();

         // perform associated events
         keyEvents();
         mouseEvents();
         consumeKeyEvents();

         while (System.currentTimeMillis() - start < 9) {
            // wait
         }
      }
   }

   // method: addKey
	// purpose: adds a key to keyStates for further monitoring
	// Each key is mapped to a boolean[2] containing whether or not it is pressed and whether or not it was consumed
   private void addKey(int key) {
      keyStates.put(key, new boolean[] {false, true});
   }

   // method: updateKeyStates
   // purpose: updates the state in keyStates associated with each key ID
   private void updateKeyStates() {
      keyStates.forEach((keyID, state) -> {
         if (Keyboard.isKeyDown(keyID)) {
            if (!state[0]) {
               state[0] = true;
               state[1] = false;
            }
         } else {
            state[0] = false;
         }
      });
   }

   // method: keyEvents
   // purpose: performs actions associated with key presses that can continue to happen as long as the key is pressed
   // Currently handles camera movements and exiting with escape
   private void keyEvents() {
   	float leftRight,frontBack,upDown;

	   leftRight = 0;
	   frontBack = 0;

      if (keyStates.get(Keyboard.KEY_ESCAPE)[0]) {
         parent.setExit();
      }

      if (keyStates.get(Keyboard.KEY_W)[0]) {
         // move forward if W is pressed
         frontBack += 1;
      }
      if (keyStates.get(Keyboard.KEY_S)[0]) {
         // move backward if S is pressed
         frontBack -= 1;
      }

      if (keyStates.get(Keyboard.KEY_A)[0]) {
         // move left if A is pressed
         leftRight -= 1;
      }
      if (keyStates.get(Keyboard.KEY_D)[0]) {
         // move right if D is pressed
         leftRight += 1;
      }

      if (keyStates.get(Keyboard.KEY_SPACE)[0]) {
         // move up if SPACE is pressed
         upDown = 1;
      } else if (keyStates.get(Keyboard.KEY_LSHIFT)[0]) {
         // move down is LSHIFT is pressed
         upDown = -1;
      } else {
      	upDown = 0;
      }

      // multipley leftRight and frontBack speeds by 1/sqrt(2)
      if (leftRight != 0 && frontBack != 0) {
      	leftRight *= 0.70710678f;
      	frontBack *= 0.70710678f;
      }

      parent.getCamera().move3f(leftRight, frontBack, upDown);
   }

   // method: consumeKeyEvents
   // purpose: performs actions associated with key presses that should only happen once per key press
   // Currently only pays attention to the Tab key, which toggles whether or not the mouse is grabbed
   private void consumeKeyEvents() {
      if (!keyStates.get(Keyboard.KEY_TAB)[1]) {
         Mouse.setGrabbed(!Mouse.isGrabbed());
         keyStates.get(Keyboard.KEY_TAB)[1] = true;
      }
   }

   // method: mouseEvents
	// purpose: adjusts the camera's pitch and yaw based on the change in camera position
	// only works when mouse is grabbed
   private void mouseEvents() {
	   int dx,dy;
	   Camera camera;

	   if (Mouse.isGrabbed()) {
         camera = parent.getCamera();
         dx = Mouse.getDX();
         dy = Mouse.getDY();

         if (dy != 0) {
            camera.pitch(dy * mouseSensitivity);
         }

         if (dx != 0) {
            camera.yaw(dx * mouseSensitivity);
         }
      }
   }
}
