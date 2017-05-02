/** *************************************************************
 *		file: InputReader.java
 *		author: G. Ortega-Gingrich
 *		class: CS 445 - Computer Graphics
 *
 *		assignment: program _
 *		date last modified: 4/17/2017
 *
 *		purpose: This is a runnable class that allows for keyboard
 *		input checking to be handled in a separate thread.
 *
 *		Overview of key bindings:
 *		escape:	quit program
 *************************************************************** */

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.util.HashMap;

public class InputReader implements Runnable {
   private Main parent;
   private HashMap<Integer, boolean[]> keyStates;

   // constructor: InputReader()
   // purpose: sets parent object and initializes keyStates
   public InputReader(Main parent) {
      this.parent = parent;
      this.keyStates = new HashMap<>();

      // key id gets mapped to {current state, consumed}
      addKey(Keyboard.KEY_ESCAPE);
      addKey(Keyboard.KEY_W);
      addKey(Keyboard.KEY_A);
      addKey(Keyboard.KEY_S);
      addKey(Keyboard.KEY_D);
      addKey(Keyboard.KEY_SPACE);
      addKey(Keyboard.KEY_LSHIFT);
   }

   // method: run
   // purpose: contains loop that updates the states in keyStates and performs actions associated with those keys
   // when necessary using keyEvents() and consumeKeyEvents()
   public void run() {
      while (!parent.getExit()) {
         // update key states
         updateKeyStates();

         // perform associated events
         keyEvents();
         mouseEvents();
         consumeKeyEvents();
      }
   }

   private void addKey(int key) {
      keyStates.put(key, new boolean[] {false, false});
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
   // An example would be exiting on escape.
   private void keyEvents() {
      if (keyStates.get(Keyboard.KEY_ESCAPE)[0]) {
         parent.setExit();
      }

      if (keyStates.get(Keyboard.KEY_A)[0]) {
      	parent.getCamera().strafeLeft();
      }

      if (keyStates.get(Keyboard.KEY_D)[0]) {
      	parent.getCamera().strafeRight();
      }
   }

   // method: consumeKeyEvents
   // purpose: performs actions associated with key presses that should only happen once per key press
   // An example would be toggling a setting on a key press.
   private void consumeKeyEvents() {
      // none of these events yet
   }

   private void mouseEvents() {

   }
}
