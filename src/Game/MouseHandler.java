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

import Client.ScaledWindow;
import Client.Settings;
import Client.WikiURL;
import java.awt.Event;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;

/** Listens to mouse events and stores relevant information about them */
public class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {

  public static int x = 0;
  public static int y = 0;
  public static boolean rightClick = false;
  public static long lastWheelMovement = 0L;
  public static long lastWheelClick = 0L;

  private boolean m_rotating = false;
  private Point m_rotatePosition;
  private float m_rotateX = 0.0f;

  public static boolean inBounds(Rectangle bounds) {
    if (XPBar.hoveringOverMenu || XPBar.hoveringOverBar()) {
      XPBar.hoveringOverMenu = false;
      return true;
    }
    if (bounds == null) return false;
    return false;
  }

  public boolean inConsumableButton() {
    return XPBar.shouldConsume() || WikiURL.shouldConsume();
  }

  /**
   * POJO responsible for storing properties about a mouse click, to be used for tracking clicks on
   * custom elements introduced by RSCx
   */
  public static class BufferedMouseClick {
    private final boolean mouseClicked;
    private final boolean rightClick;
    private final int x;
    private final int y;

    public BufferedMouseClick(boolean mouseClicked, boolean rightClick, int x, int y) {
      this.mouseClicked = mouseClicked;
      this.rightClick = rightClick;
      this.x = x;
      this.y = y;
    }

    public boolean isMouseClicked() {
      return mouseClicked;
    }

    public boolean isRightClick() {
      return rightClick;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }
  }

  /**
   * Grabs a {@link BufferedMouseClick} from the buffer, if available <br>
   * <br>
   * Note: this should only ever be called in one place to guarantee queue integrity
   */
  public static BufferedMouseClick getBufferedMouseClick() {
    BufferedMouseClick bufferedMouseClick = ScaledWindow.getInstance().getInputBuffer().peek();

    // Return a dummy instance when nothing is available in the queue, such
    // that we don't need to do null checks everywhere on the object.
    if (bufferedMouseClick == null) {
      return new BufferedMouseClick(false, false, -1, -1);
    }

    return ScaledWindow.getInstance().getInputBuffer().poll();
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (inConsumableButton()) {
      e.consume();
    }

    if (!e.isConsumed()) {
      x = e.getX();
      y = e.getY();
      e.consume();
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // Re-render the software cursor
    Settings.checkSoftwareCursor();

    if (!e.isConsumed()) {
      x = e.getX();
      y = e.getY();
      e.consume();
    }
  }

  @Override
  public void mouseExited(MouseEvent e) {
    if (!e.isConsumed()) {
      x = -100;
      y = -100;
      e.consume();
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (inConsumableButton()) {
      e.consume();
    }

    if (e.getButton() == MouseEvent.BUTTON2) {
      m_rotating = true;
      m_rotatePosition = e.getPoint();
      e.consume();
    }

    if (!e.isConsumed()) {
      x = e.getX();
      y = e.getY();
      try {
        Event evt =
            new Event(
                Client.instance, e.getWhen(), 501, e.getX(), e.getY(), 0, e.getModifiers(), e);
        Reflection.mouseDown.invoke(Client.instance, evt, x, y);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      e.consume();
    }

    // TODO: Determine if required
    rightClick = SwingUtilities.isRightMouseButton(e);

    ScaledWindow.getInstance().getInputBuffer().add(new BufferedMouseClick(true, rightClick, x, y));
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (inConsumableButton()) {
      e.consume();
    }

    if (e.getButton() == MouseEvent.BUTTON2) {
      m_rotating = false;
      e.consume();
    }

    if (!e.isConsumed()) {
      x = e.getX();
      y = e.getY();
      try {
        Event evt =
            new Event(
                Client.instance, e.getWhen(), 502, e.getX(), e.getY(), 0, e.getModifiers(), e);
        Reflection.mouseUp.invoke(Client.instance, evt, x, y);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      e.consume();
    }
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if (Settings.CAMERA_ROTATABLE.get(Settings.currentProfile) && m_rotating) {
      if (e.getWhen() > lastWheelClick + 10) {
        lastWheelClick = e.getWhen();
        m_rotateX += (float) (e.getX() - m_rotatePosition.x) / 2.0f;
        int xDist = (int) m_rotateX;

        Camera.addRotation(xDist);
        m_rotateX -= xDist;

        m_rotatePosition = e.getPoint();
      }
    }
    if (!e.isConsumed()) {
      x = e.getX();
      y = e.getY();
      try {
        Event evt = new Event(Client.instance, 506, e);
        Reflection.mouseDrag.invoke(Client.instance, evt, x, y);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      e.consume();
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    if (!e.isConsumed()) {
      x = e.getX();
      y = e.getY();
      try {
        Event evt = new Event(Client.instance, 503, e);
        Reflection.mouseMove.invoke(Client.instance, evt, x, y);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      e.consume();
    }
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    x = e.getX();
    y = e.getY();
    if ((e.getWhen() > lastWheelMovement + 10)
        && !(e.isShiftDown()
            && Settings.SHIFT_SCROLL_CAMERA_ROTATION.get(Settings.currentProfile))) {
      lastWheelMovement = e.getWhen();
      Camera.addZoom(e.getWheelRotation() * 16);
    } else if (e.isShiftDown()
        && Settings.SHIFT_SCROLL_CAMERA_ROTATION.get(Settings.currentProfile)) {
      // Allows compatible trackpads to rotate the camera on 2-finger swipe
      // motions. Also rotates the camera when holding shift in general.
      Camera.addRotation(
          e.getWheelRotation()
              * -(Settings.TRACKPAD_ROTATION_SENSITIVITY.get(Settings.currentProfile) / 2.0f));
    }
    e.consume();
  }
}
