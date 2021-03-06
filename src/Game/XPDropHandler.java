/**
 * rscplus
 *
 * <p>This file is part of rscplus.
 *
 * <p>rscplus is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * <p>rscplus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * <p>You should have received a copy of the GNU General Public License along with rscplus. If not,
 * see <http://www.gnu.org/licenses/>.
 *
 * <p>Authors: see <https://github.com/RSCPlus/rscplus>
 */
package Game;

import Client.Settings;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Handles the rendering and behavior of XP drops */
public class XPDropHandler {

  private long m_timer;
  private List<XPDrop> m_list = new ArrayList<>();

  public void add(String text, Color color) {
    // No XP drops while seeking
    // if (Replay.isSeeking) return;

    XPDrop xpdrop = new XPDrop(text, color);
    m_list.add(xpdrop);
  }

  public void draw(Graphics2D g) {
    for (Iterator<XPDrop> iterator = m_list.iterator(); iterator.hasNext(); ) {
      XPDrop xpdrop = iterator.next();
      xpdrop.process(g);
      if (xpdrop.y < 0
          || xpdrop.y > Renderer.height
          || (Settings.SHOW_XP_BAR.get(Settings.currentProfile)
              && xpdrop.y <= XPBar.xp_bar_y + 5)) {
        iterator.remove();
      }
    }
  }

  class XPDrop {

    XPDrop(String text, Color color) {
      this.text = text;
      this.color = color;
      y = (float) Renderer.height / 4.0f;
      active = false;
    }

    public void process(Graphics2D g) {
      if (!active) {
        if (Renderer.time > m_timer) {
          if (Settings.SHOW_XP_BAR.get(Settings.currentProfile)) {
            if (y > XPBar.xp_bar_y + 5) {
              m_timer = Renderer.time + 400;
              active = true;
            }
          } else {
            m_timer = Renderer.time + 400;
            active = true;
          }
        } else {
          return;
        }
      }

      Renderer.drawShadowText(
          g, text, (XPBar.xp_bar_x + (XPBar.bounds.width / 2)), (int) y, this.color, true);
      y -= (float) Renderer.height / 12.0f * Renderer.delta_time;
    }

    private String text;
    private Color color;
    private boolean active;
    public float y;
  }
}
