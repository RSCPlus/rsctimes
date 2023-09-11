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

import Client.KeybindSet;
import Client.Settings;
import Client.WorldMapWindow;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

/** Listens to keyboard events to trigger specified operations */
public class KeyboardHandler implements KeyListener {

  public static int dialogue_option = -1;
  public static boolean keyLeft = false;
  public static boolean keyRight = false;
  public static boolean keyUp = false;
  public static boolean keyDown = false;
  public static boolean keyShift = false;

  /** ArrayList containing all registered KeybindSet values */
  public static ArrayList<KeybindSet> keybindSetList = new ArrayList<KeybindSet>();

  /**
   * Hashmap containing all default KeybindSet values. This is used in the ConfigWindow restore
   * default keybinds method.
   */
  public static HashMap<String, KeybindSet> defaultKeybindSetList =
      new HashMap<String, KeybindSet>();

  @Override
  public void keyPressed(KeyEvent e) {
    if (WorldMapWindow
        .hasFocus()) { // TODO: figure out why this is the only KeyboardHandler that works
      WorldMapWindow.keyPressed(e);
      WorldMapWindow.keyTyped(
          e); // TODO: idk why keyTyped events aren't happening. just putting the functionality
      // here.
      return;
    }

    boolean shouldConsume = false;

    boolean altgr = e.isControlDown() && e.isAltDown() || e.isAltGraphDown();

    // implement alt-f4
    if ((altgr || e.isAltDown()) && e.getKeyCode() == 115) {
      System.exit(0);
    }

    // Handle CTRL + Alt modifiers
    //  Note: KeybindSet does not support multiple modifiers
    if (e.isControlDown() && e.isAltDown() && !e.isAltGraphDown()) {

      // Special debug key combo
      if (e.getKeyCode() == KeyEvent.VK_D) {
        Settings.toggleDebug();
        shouldConsume = true;
      }

      if (shouldConsume) {
        e.consume();
      }

      // Handle CTRL modifier
    } else if (e.isControlDown() && !altgr) {
      for (KeybindSet kbs : keybindSetList) {
        if (kbs.getModifier() == KeybindSet.KeyModifier.CTRL && e.getKeyCode() == kbs.getKey()) {
          shouldConsume = Settings.processKeybindCommand(kbs.getCommandName());
          if (shouldConsume) {
            e.consume();
          }
        }
      }

      // Handle shift modifier
    } else if (e.isShiftDown()) {
      for (KeybindSet kbs : keybindSetList) {
        if (kbs.getModifier() == KeybindSet.KeyModifier.SHIFT && e.getKeyCode() == kbs.getKey()) {
          shouldConsume = Settings.processKeybindCommand(kbs.getCommandName());
          if (shouldConsume) {
            e.consume();
          }
        }
      }

      // Handle Alt modifier
    } else if (e.isAltDown() && !altgr) {
      for (KeybindSet kbs : keybindSetList) {
        if (kbs.getModifier() == KeybindSet.KeyModifier.ALT && e.getKeyCode() == kbs.getKey()) {
          shouldConsume = Settings.processKeybindCommand(kbs.getCommandName());
          if (shouldConsume) {
            e.consume();
          }
        }
      }

      // Handle all other keys
    } else {
      for (KeybindSet kbs : keybindSetList) {
        if (kbs.getModifier() == KeybindSet.KeyModifier.NONE && e.getKeyCode() == kbs.getKey()) {
          shouldConsume = Settings.processKeybindCommand(kbs.getCommandName());
          if (shouldConsume) {
            e.consume();
          }
        }
      }
    }

    /*
    if (Replay.isRecording && !e.isConsumed()) {
      Replay.dumpKeyboardInput(
              e.getKeyCode(), Replay.KEYBOARD_PRESSED, e.getKeyChar(), e.getModifiers());
    }
     */

    if (Client.show_questionmenu && !e.isConsumed() && !Replay.isPlaying) {
      if (e.getKeyCode() == KeyEvent.VK_1 || e.getKeyCode() == KeyEvent.VK_NUMPAD1)
        dialogue_option = 0;
      else if (e.getKeyCode() == KeyEvent.VK_2 || e.getKeyCode() == KeyEvent.VK_NUMPAD2)
        dialogue_option = 1;
      else if (e.getKeyCode() == KeyEvent.VK_3 || e.getKeyCode() == KeyEvent.VK_NUMPAD3)
        dialogue_option = 2;
      else if (e.getKeyCode() == KeyEvent.VK_4 || e.getKeyCode() == KeyEvent.VK_NUMPAD4)
        dialogue_option = 3;
      else if (e.getKeyCode() == KeyEvent.VK_5 || e.getKeyCode() == KeyEvent.VK_NUMPAD5)
        dialogue_option = 4;
      if (dialogue_option >= 0) e.consume();
    }

    if (Client.state == Client.STATE_GAME
        && e.getKeyCode() == KeyEvent.VK_TAB
        && !Client.isInterfaceOpen()) {
      if (!Replay.isPlaying && Client.lastpm_username != null) {
        Client.pm_text = "";
        Client.pm_enteredText = "";
        Client.setPmUserHash(Utility.userNameToHash(Client.lastpm_username));
        Client.show_friends = 2;
      }
      e.consume();
    }

    // Handle camera keys
    if (!e.isConsumed()) {
      if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        keyLeft = true;
        if (Settings.CAMERA_ROTATABLE.get(Settings.currentProfile)) e.consume();
      }
      if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        keyRight = true;
        if (Settings.CAMERA_ROTATABLE.get(Settings.currentProfile)) e.consume();
      }
      if (e.getKeyCode() == KeyEvent.VK_UP) {
        keyUp = true;
        if (Settings.CAMERA_ZOOMABLE.get(Settings.currentProfile)) e.consume();
      }
      if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        keyDown = true;
        if (Settings.CAMERA_ZOOMABLE.get(Settings.currentProfile)) e.consume();
      }

      keyShift = e.isShiftDown();
    }

    // Translate modern java keycodes to ancient java keycodes
    if (!e.isConsumed() && !WorldMapWindow.hasFocus()) {
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
    if (WorldMapWindow.hasFocus()) {
      WorldMapWindow.keyReleased(e);
      return;
    }

    /* TODO: implement replays
    if (Replay.isRecording) {
      Replay.dumpKeyboardInput(
              e.getKeyCode(), Replay.KEYBOARD_RELEASED, e.getKeyChar(), e.getModifiers());
    }
     */

    // Reset dialogue option
    if (dialogue_option >= 0 && !Replay.isPlaying) {
      dialogue_option = -1;
      e.consume();
    }

    // TODO: implement tab to respond to pm
    // if (e.getKeyCode() == KeyEvent.VK_TAB) e.consume();

    // Handle camera keys
    if (!e.isConsumed()) {
      if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        keyLeft = false;
        if (Settings.CAMERA_ROTATABLE.get(Settings.currentProfile)) e.consume();
      }
      if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        keyRight = false;
        if (Settings.CAMERA_ROTATABLE.get(Settings.currentProfile)) e.consume();
      }
      if (e.getKeyCode() == KeyEvent.VK_UP) {
        keyUp = false;
        if (Settings.CAMERA_ZOOMABLE.get(Settings.currentProfile)) e.consume();
      }
      if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        keyDown = false;
        if (Settings.CAMERA_ZOOMABLE.get(Settings.currentProfile)) e.consume();
      }

      keyShift = e.isShiftDown();
    }

    // Translate modern java keycodes to ancient java keycodes
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
    if (WorldMapWindow.hasFocus()) {
      WorldMapWindow.keyTyped(e);
      return;
    }

    if (dialogue_option >= 0 && !Replay.isPlaying) e.consume();

    // TODO: Handle camera rotation keys
    /*
    if (!e.isConsumed()) {
      if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        keyLeft = true;
        if (Settings.CAMERA_ROTATABLE.get(Settings.currentProfile)) e.consume();
      }
      if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        keyRight = true;
        if (Settings.CAMERA_ROTATABLE.get(Settings.currentProfile)) e.consume();
      }
      if (e.getKeyCode() == KeyEvent.VK_UP) {
        keyUp = true;
        if (Settings.CAMERA_ZOOMABLE.get(Settings.currentProfile)) e.consume();
      }
      if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        keyDown = true;
        if (Settings.CAMERA_ZOOMABLE.get(Settings.currentProfile)) e.consume();
      }

      keyShift = e.isShiftDown();
    }
     */

  }
}
