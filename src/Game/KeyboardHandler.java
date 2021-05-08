/**
 * rsctimes
 *
 * <p>This file is part of rsctimes.
 *
 * <p>rsctimes is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * <p>rsctimes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * <p>You should have received a copy of the GNU General Public License along with rsctimes. If not,
 * see <http://www.gnu.org/licenses/>.
 *
 * <p>Authors: see <https://github.com/RSCPlus/rsctimes>
 */
package Game;

import java.awt.Event;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;

/** Listens to keyboard events to trigger specified operations */
public class KeyboardHandler implements KeyListener {

  @Override
  public void keyPressed(KeyEvent e) {
    if (!e.isConsumed()) {
      try {
        Event evt = new Event(Client.instance, 401, e);
        int converted = KeyboardMapHelper.convert(e.getKeyCode());
        int n =
            converted == e.getExtendedKeyCode()
                ? e.getKeyChar()
                : converted; // with getKeyCode() is always placing letter to upper
        Reflection.keyDown.invoke(Client.instance, evt, n);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      e.consume();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (!e.isConsumed()) {
      try {
        Event evt = new Event(Client.instance, 402, e);
        int converted = KeyboardMapHelper.convert(e.getKeyCode());
        int n =
            converted == e.getExtendedKeyCode()
                ? e.getKeyChar()
                : converted; // with getKeyCode() is always placing letter to upper
        Reflection.keyUp.invoke(Client.instance, evt, n);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      e.consume();
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
    System.out.println("Key typed...");
    /*if (!e.isConsumed()) {
    	e.consume();
    }*/
  }
}
