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

import Client.Logger;
import Client.Settings;
import Client.Util;
import Game.MouseHandler.BufferedMouseClick;
import java.awt.*;
import java.text.NumberFormat;

/** Handles rendering the XP bar and hover information */
public class XPBar {
  public static boolean showingMenu = false;
  public static boolean hoveringOverMenu = false;
  private static float alpha;
  public static boolean pinnedBar = false;
  public static int pinnedSkill = -1;
  public static int drawGoalInputState = 0;

  // when replay is initialized, client.username_login will be set to this
  public static final String excludeUsername = "excludemefromxpbartracking";

  public static Dimension bounds = new Dimension(110, 16);
  public static Dimension menuBounds = new Dimension(110, 30);
  public static int xp_bar_x;
  // Don't need to set this more than once; we are always positioning the xp_bar to be vertically
  // center aligned with
  // the Settings wrench.
  public static final int xp_bar_y = 20 + Renderer.GAME_RENDER_OFFSET - (bounds.height / 2);

  public static final int TIMER_LENGTH = 5000;
  public static final long TIMER_FADEOUT = 2000;

  public int current_skill;

  /** Keeps track of whether the XP bar from the last XP drop is still showing. */
  private boolean last_timer_finished = false;

  private long m_timer;

  public XPBar() {
    current_skill = -1;
  }

  void setSkill(int skill) {
    current_skill = skill;
    last_timer_finished = m_timer - Renderer.time <= 0;
    m_timer = Renderer.time + TIMER_LENGTH;
  }

  /**
   * Handles rendering the XP bar and hover information
   *
   * @param g the Graphics2D object
   */
  void draw(Graphics2D g, BufferedMouseClick bufferedMouseClick) {

    // current_skill from skill not known
    /*if (Renderer.time > m_timer && !pinnedBar) {
      current_skill = -1;
      return;
    }*/
    if (pinnedSkill >= 0) {
      current_skill = pinnedSkill;
    }

    if (current_skill == -1) return;

    long delta = m_timer - Renderer.time;
    alpha = 1.0f;
    if (!pinnedBar) {
      // Don't fade in if XP bar is already displayed
      if (delta >= TIMER_LENGTH - 250 && last_timer_finished) {
        // Fade in over 1/4th second
        alpha = (float) (TIMER_LENGTH - delta) / 250.0f;
      } else if (delta < TIMER_FADEOUT) {
        // Less than TIMER_FADEOUT milliseconds left to display the XP bar
        alpha = (float) delta / TIMER_FADEOUT;
      }
    }

    // Draw bar

    // Check and set the appropriate display position
    if (Settings.CENTER_XPDROPS.get(Settings.currentProfile))
      xp_bar_x = (Renderer.width - bounds.width) / 2; // Position in the center
    else
      xp_bar_x = Renderer.width - 210 - bounds.width; // Position to the left of the Settings wrench

    // need to 	exit *after* setting xp_bar_x because XPDropHandler needs it
    if (!Settings.SHOW_XP_BAR.get(Settings.currentProfile)) {
      return;
    }

    int currLvl = Client.base_level[current_skill];
    String username = Util.formatString(Client.player_name, 50);
    int goalLvl;
    try {
      goalLvl =
          (int)
              Math.floor(
                  Client.lvlGoals.containsKey(username)
                      ? Client.lvlGoals.get(username)[current_skill]
                      : currLvl + 1);
    } catch (Exception e) {
      goalLvl = currLvl + 1;
    }
    goalLvl = Math.max(currLvl + 1, goalLvl);

    int skill_current_xp = (int) Client.getXPforLevel(Client.getBaseLevel(current_skill));
    int skill_next_xp = (int) Client.getXPforLevel(Client.getBaseLevel(current_skill) + 1);
    int skill_goal_xp = (int) Client.getXPforLevel(goalLvl);

    int xp_progress = skill_next_xp - skill_current_xp;

    int percent = skill_current_xp * (bounds.width - 2) / skill_goal_xp;
    int percent_progress =
        (xp_progress * (bounds.width - 2) / skill_goal_xp) + (percent > 0 ? 1 : 0);

    boolean post99xp = Client.base_level[current_skill] == 99;

    if (percent > bounds.width - 2) { // happens after virtual lvl 100
      percent = bounds.width - 2;
      percent_progress = 0;
    }

    int x = xp_bar_x;
    int y = xp_bar_y;
    alpha = 1.0f;
    Renderer.setAlpha(g, alpha);
    g.setColor(Renderer.color_gray);
    g.fillRect(x - 1, y - 1, bounds.width + 2, bounds.height + 2);

    g.setColor(Renderer.color_shadow);
    g.fillRect(x, y, bounds.width, bounds.height);

    if (!post99xp) {
      g.setColor(Renderer.color_hp);
    } else {
      g.setColor(Renderer.color_fatigue);
    }
    g.fillRect(x + 1, y + 1, percent, bounds.height - 2);
    if (percent_progress > 0.0f) {
      g.setColor(Renderer.color_fatigue);
      g.fillRect(x + percent + 1, y + 1, percent_progress, bounds.height - 2);
    }

    Renderer.drawShadowText(
        g,
        Client.skill_name[current_skill] + " (" + Client.base_level[current_skill] + ")",
        x + (bounds.width / 2),
        y + (bounds.height / 2) - 2,
        Renderer.color_text,
        true);

    // Draw additional menus
    hoveringOverMenu =
        (MouseHandler.x >= x
            && MouseHandler.x <= x + menuBounds.width
            && MouseHandler.y > y
            && MouseHandler.y < y + bounds.height + menuBounds.height
            && showingMenu);

    if (hoveringOverBar()) {
      if (bufferedMouseClick.isRightClick()) {
        showingMenu = true;
      } else {
        if (!showingMenu) {
          drawInfoBox(g, x, y, current_skill);
        }
      }
      // Don't allow XP bar to disappear while user is still interacting with it.
      if (delta < TIMER_FADEOUT + 100) {
        m_timer += TIMER_FADEOUT + 1500;
        last_timer_finished = false;
      }
    } else {
      if (!hoveringOverMenu) {
        showingMenu = false;
      } else {
        if (delta < TIMER_FADEOUT + 100) {
          m_timer += TIMER_FADEOUT + 1500;
          last_timer_finished = false;
        }
      }
    }

    if (showingMenu) drawMenu(g, x, y, bufferedMouseClick);

    Renderer.setAlpha(g, 1.0f);
  }

  static boolean hoveringOverBar() {
    return MouseHandler.x >= xp_bar_x
        && MouseHandler.x <= xp_bar_x + bounds.width
        && MouseHandler.y > xp_bar_y
        && MouseHandler.y < xp_bar_y + bounds.height
        && alpha > 0.01;
  }

  private void drawMenu(Graphics2D g, int x, int y, BufferedMouseClick bufferedMouseClick) {
    x = xp_bar_x;
    y = xp_bar_y + bounds.height;

    g.setColor(Renderer.color_gray);
    Renderer.setAlpha(g, 0.7f);
    g.fillRect(x, y, menuBounds.width, menuBounds.height);

    Renderer.setAlpha(g, 1.0f);

    x += 8;
    Color textColour;

    int offset = 2;
    int textHeight = 12 + offset;

    // Option 0
    if (MouseHandler.y > y + offset && MouseHandler.y < y + textHeight) {
      textColour = Renderer.color_yellow;
      if (bufferedMouseClick.isMouseClicked()) {
        setLvlGoal();
      }
    } else {
      textColour = Renderer.color_text;
    }
    y += 12;
    Renderer.drawShadowText(
        g, hasGoalForSkill(current_skill) ? "Clear Goal" : "Set Goal", x, y, textColour, false);

    // Option 1
    if (MouseHandler.y > y + offset && MouseHandler.y < y + textHeight) {
      textColour = Renderer.color_yellow;
      if (bufferedMouseClick.isMouseClicked()) {
        togglePinnedBar();
      }
    } else {
      textColour = Renderer.color_text;
    }
    y += 12;
    Renderer.drawShadowText(g, pinnedBar ? "Unpin bar" : "Pin bar", x, y, textColour, false);
  }

  private void pinSkill() {
    if (pinnedSkill > 0) {
      pinnedSkill = -1;
    } else {
      pinnedSkill = current_skill;
    }
  }

  private void setLvlGoal() {
    if (hasGoalForSkill(current_skill)) {
      String username = Util.formatString(Client.player_name, 50);
      Client.lvlGoals.get(username)[current_skill] = (float) 1;
      Settings.save();
    } else {
      Client.modal_enteredText = "";
      Client.modal_text = "";
      drawGoalInputState = 8;
    }
  }

  public void setLvlGoal(int lvlGoal) {
    String username = Util.formatString(Client.player_name, 50);
    if (lvlGoal >= 1 && lvlGoal <= 99) {
      Client.lvlGoals.get(username)[current_skill] = (float) lvlGoal;
    } else {
      Logger.Error("Could not set LVL goal! Please report this.");
      Logger.Error("username: " + username + " current_skill: " + current_skill);
    }
    Settings.save();
  }

  private void togglePinnedBar() {
    pinnedBar = !pinnedBar;
  }

  private static void drawInfoBox(Graphics2D g, int x, int y, int current_skill) {
    // Draw info box
    x = MouseHandler.x;
    y = MouseHandler.y + 24;
    g.setColor(Renderer.color_gray);
    Renderer.setAlpha(g, 0.7f);
    String labelColour = "@lre@";
    String textColour = "@whi@";
    String highlightColour = "@gre@";

    int height = 25;

    if (hasGoalForSkill(current_skill)) {
      height += 20;
    }

    // Draw new rect
    g.fillRect(x - 100, y, 200, height);

    Renderer.setAlpha(g, 1.0f);
    y += 10;

    String username = Util.formatString(Client.player_name, 50);
    if (hasGoalForSkill(current_skill)) {
      Renderer.drawColoredText(
          g,
          labelColour
              + "Current Goal Level: "
              + textColour
              + Client.lvlGoals.get(username)[current_skill],
          x,
          y,
          true);
      y += 12;
      y += 8;
    }
    Renderer.drawShadowText(g, "Right-click for more options", x, y, Renderer.color_text, true);
  }

  public static boolean hasGoalForSkill(int skill) {
    String username = Util.formatString(Client.player_name, 50);
    try {
      return Client.lvlGoals.get(username)[skill] > 1;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Rounds up a double to the nearest integer and adds commas, periods, etc. according to the
   * local of the user
   *
   * @param number the number to round
   * @return a formatted version of the double as a String
   */
  public static String formatXP(double number) {
    return NumberFormat.getIntegerInstance().format(Math.ceil(number));
  }

  /*
   *   Goal Input code below this line
   * ===================================
   */

  public static boolean shouldShowGoalInput() {
    return drawGoalInputState != 0;
  }

  public static boolean shouldConsume() {
    if (XPBar.hoveringOverMenu || XPBar.hoveringOverBar()) {
      XPBar.hoveringOverMenu = false;
      return true;
    }
    return false;
  }

  public static boolean shouldConsumeKey() {
    return shouldShowGoalInput();
  }

  public static int keyHandler() {
    return (shouldConsumeKey() ? 1 : 0);
  }

  public static void drawGoalLvlInput(int mouseX, int mouseY, int mouseButtonClick) {
    if (mouseButtonClick != 0 && !hoveringOverMenu) {
      mouseButtonClick = 0;
      if (mouseX < Renderer.width / 2 - 150
          || mouseY < Renderer.height / 2 - 32
          || mouseX > Renderer.width / 2 + 150
          || mouseY > Renderer.height / 2 + 28) {
        drawGoalInputState = 0;
        return;
      }
    }
    int yPos = Renderer.height / 2 - 32;
    Renderer.drawBox(Renderer.width / 2 - 150, yPos, 300, 60, 0);
    Renderer.drawBoxBorder(Renderer.width / 2 - 150, yPos, 300, 60, 0xFFFFFF);
    yPos += 22;

    if (drawGoalInputState == 8) {
      Renderer.drawStringCenter(
          "Please enter your level goal", Renderer.width / 2, yPos, 4, 0xFFFFFF);
      yPos += 25;

      Renderer.drawStringCenter(
          Client.modal_enteredText + "*", Renderer.width / 2, yPos, 4, 0xFFFFFF);
      if (Client.modal_text.length() > 0) {
        int goal = -1;
        try {
          goal = Integer.parseInt(Client.modal_text);
        } catch (NumberFormatException e) {
          drawGoalInputState = 9;
          return;
        }

        Client.xpbar.setLvlGoal(goal);
        Client.modal_enteredText = "";
        Client.modal_text = "";
        drawGoalInputState = 0;
        return;
      }

    } else if (drawGoalInputState == 9) {
      Renderer.drawStringCenter("Numbers only, please", Renderer.width / 2, yPos, 4, 0xFFFFFF);
      yPos += 25;

      Renderer.drawStringCenter(
          Client.modal_enteredText + "*", Renderer.width / 2, yPos, 4, 0xFFFFFF);
      if (Client.modal_text.length() > 0) {
        int goal = -1;
        try {
          goal = Integer.parseInt(Client.modal_text);
        } catch (NumberFormatException e) {
          drawGoalInputState = 9;
          return;
        }

        Client.xpbar.setLvlGoal(goal);
        Client.modal_enteredText = "";
        Client.modal_text = "";
        drawGoalInputState = 0;
        return;
      }
    }
  }
}
