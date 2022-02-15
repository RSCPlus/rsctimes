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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;

import Client.WikiURL;

/** Listens to mouse events and stores relevant information about them */
public class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {

  public static int x = 0;
  public static int y = 0;
  public static boolean mouseClicked = false;
  public static boolean rightClick = false;

  private boolean m_rotating = false;
  private Point m_rotatePosition;
  private float m_rotateX = 0.0f;

  public static boolean inBounds(Rectangle bounds) {
    if (bounds == null) return false;
    return false;
  }

  public boolean inConsumableButton() {
    return WikiURL.shouldConsume();
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

    mouseClicked = true;
    rightClick = SwingUtilities.isRightMouseButton(e);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (inConsumableButton()) {
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
    e.consume();
  }
}
