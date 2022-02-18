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
import java.lang.reflect.Field;
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

  private static final String DISPLAYMESSAGE = "public void mudclient.gk(java.lang.String,int)";
  private static final String SETRESPONSEMESSAGE =
      "public void mudclient.ob(java.lang.String,java.lang.String)";

  private static final String SETCAMERASIZE =
      "public void jagex.client.j.yh(int,int,int,int,int,int)";
  private static final String SETGAMEBOUNDS = "public void jagex.client.i.kf(int,int,int,int)";

  public static Method mouseMove = null;
  public static Method mouseDrag = null;
  public static Method mouseUp = null;
  public static Method mouseDown = null;
  public static Method keyUp = null;
  public static Method keyDown = null;
  public static Method gameFrame = null;

  public static Field characterName = null;
  public static Field characterWaypointX = null;
  public static Field characterWaypointY = null;

  public static Field interlace = null;

  public static Field menuX = null;
  public static Field menuY = null;
  public static Field menuScroll = null;
  public static Field menuWidth = null;
  public static Field menuHeight = null;

  public static Method displayMessage = null;
  public static Method setResponseMessage = null;
  public static Method setCameraSize = null;
  public static Method setGameBounds = null;

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

      // Client
      c = classLoader.loadClass("mudclient");
      methods = c.getDeclaredMethods();
      for (Method method : methods) {
        if (method.toGenericString().equals(DISPLAYMESSAGE)) {
          displayMessage = method;
          Logger.Info("Found displayMessage");
        } else if (method.toGenericString().equals(SETRESPONSEMESSAGE)) {
          setResponseMessage = method;
          Logger.Info("Found setResponseMessage");
        }
      }

      // Camera
      c = classLoader.loadClass("jagex.client.j");
      methods = c.getDeclaredMethods();
      for (Method method : methods) {
        if (method.toGenericString().equals(SETCAMERASIZE)) {
          setCameraSize = method;
          Logger.Info("Found setCameraSize");
        }
      }

      // Renderer
      c = classLoader.loadClass("jagex.client.i");
      interlace = c.getDeclaredField("rk");
      methods = c.getDeclaredMethods();
      for (Method method : methods) {
        if (method.toGenericString().equals(SETGAMEBOUNDS)) {
          setGameBounds = method;
          Logger.Info("Found setGameBounds");
        }
      }

      // Character
      c = classLoader.loadClass("l");
      characterName = c.getDeclaredField("zq");
      characterWaypointX = c.getDeclaredField("cr");
      characterWaypointY = c.getDeclaredField("dr");
      if (characterName != null) characterName.setAccessible(true);
      if (characterWaypointX != null) characterWaypointX.setAccessible(true);
      if (characterWaypointY != null) characterWaypointY.setAccessible(true);

      // Menu
      c = classLoader.loadClass("jagex.client.g");
      menuX = c.getDeclaredField("ze");
      menuY = c.getDeclaredField("af");
      // menuScroll = c.getDeclaredField("j"); //TODO:fix
      menuWidth = c.getDeclaredField("cf");
      // this menu height for chats I believe
      menuHeight = c.getDeclaredField("df");

      // Set all accessible
      if (mouseMove != null) mouseMove.setAccessible(true);
      if (mouseDrag != null) mouseDrag.setAccessible(true);
      if (mouseUp != null) mouseUp.setAccessible(true);
      if (mouseDown != null) mouseDown.setAccessible(true);
      if (keyUp != null) keyUp.setAccessible(true);
      if (keyDown != null) keyDown.setAccessible(true);
      if (interlace != null) interlace.setAccessible(true);
      if (gameFrame != null) gameFrame.setAccessible(true);
      if (menuX != null) menuX.setAccessible(true);
      if (menuY != null) menuY.setAccessible(true);
      if (menuScroll != null) menuScroll.setAccessible(true);
      if (menuWidth != null) menuWidth.setAccessible(true);
      if (menuHeight != null) menuHeight.setAccessible(true);
      if (displayMessage != null) displayMessage.setAccessible(true);
      if (setResponseMessage != null) setResponseMessage.setAccessible(true);
      if (setCameraSize != null) setCameraSize.setAccessible(true);
      if (setGameBounds != null) setGameBounds.setAccessible(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
