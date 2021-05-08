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

import Client.JClassLoader;
import Client.Launcher;
import Client.Logger;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Reflection {

  // Method descriptions
  private static final String MOUSE_MOVE =
      "public synchronized boolean jagex.client.k.mouseMove(java.awt.Event,int,int)";
  private static final String MOUSE_DRAG =
      "public synchronized boolean jagex.client.k.mouseDrag(java.awt.Event,int,int)";
  private static final String MOUSE_UP =
      "public synchronized boolean jagex.client.k.mouseUp(java.awt.Event,int,int)";
  private static final String MOUSE_DOWN =
      "public synchronized boolean jagex.client.k.mouseDown(java.awt.Event,int,int)";

  private static final String KEY_UP =
      "public synchronized boolean jagex.client.k.keyUp(java.awt.Event,int)";
  private static final String KEY_DOWN =
      "public synchronized boolean jagex.client.k.keyDown(java.awt.Event,int)";

  private static final String GAME_FRAME = "public java.awt.Frame jagex.client.k.ij()";

  public static Method mouseMove = null;
  public static Method mouseDrag = null;
  public static Method mouseUp = null;
  public static Method mouseDown = null;
  public static Method keyUp = null;
  public static Method keyDown = null;
  public static Method gameFrame = null;

  public static void Load() {
    try {
      JClassLoader classLoader = Launcher.getInstance().getClassLoader();
      ArrayList<String> leftMethods =
          new ArrayList<String>(); // expected virtual methods to find in given class

      Class<?> c = classLoader.loadClass("jagex.client.k");
      Method[] methods = c.getDeclaredMethods();
      for (Method method : methods) {
        if (method.toGenericString().equals(MOUSE_MOVE)) {
          mouseMove = method;
          Logger.Info("Found mouseMove");
        } else if (method.toGenericString().equals(MOUSE_DRAG)) {
          mouseDrag = method;
          Logger.Info("Found mouseDrag");
        } else if (method.toGenericString().equals(MOUSE_UP)) {
          mouseUp = method;
          Logger.Info("Found mouseUp");
        } else if (method.toGenericString().equals(MOUSE_DOWN)) {
          mouseDown = method;
          Logger.Info("Found mouseDown");
        } else if (method.toGenericString().equals(KEY_UP)) {
          keyUp = method;
          Logger.Info("Found keyUp");
        } else if (method.toGenericString().equals(KEY_DOWN)) {
          keyDown = method;
          Logger.Info("Found keyDown");
        } else if (method.toGenericString().equals(GAME_FRAME)) {
          gameFrame = method;
          Logger.Info("Found gameFrame");
        }
      }

      // Set all accessible
      if (mouseMove != null) mouseMove.setAccessible(true);
      if (mouseDrag != null) mouseDrag.setAccessible(true);
      if (mouseUp != null) mouseUp.setAccessible(true);
      if (mouseDown != null) mouseDown.setAccessible(true);
      if (keyUp != null) keyUp.setAccessible(true);
      if (keyDown != null) keyDown.setAccessible(true);
      if (gameFrame != null) gameFrame.setAccessible(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
