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

import java.applet.Applet;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import Client.Settings;
import Client.Logger;
import Client.Util;

public class Client {

  // Game's client instance
  public static Object instance;

  public static MouseHandler handler_mouse;
  public static KeyboardHandler handler_keyboard;
  
  public static final int STATE_LOGIN = 1;
  public static final int STATE_GAME = 2;
  
  public static final int SCREEN_CLICK_TO_LOGIN = 0; // also is this value while logged in
  public static final int SCREEN_TERMS_CONDITIONS = 1;
  public static final int SCREEN_USERNAME_PASSWORD_LOGIN = 2;
  public static final int SCREEN_REGISTER_NEW_ACCOUNT = 3;
  
  public static int state = STATE_LOGIN;

  public static boolean is_displaying_fps;
  public static int connection_port = 43594;

  public static int[] base_level;
  public static int[] xp;
  public static int[] current_level;
  public static String[] skill_name;
  
  public static int login_screen;

  public static boolean show_questionmenu = false; // TODO: implement

  public static XPDropHandler xpdrop_handler = new XPDropHandler();
  public static XPBar xpbar = new XPBar();

  public static String xpUsername = "";
  public static String modal_text;
  public static String modal_enteredText;
  public static String username_login;

  public static final int NUM_SKILLS = 19; // TODO: ?
  public static boolean firstTimeRunningRSCTimes = false;

  /**
   * A boolean array that stores if the XP per hour should be shown for a given skill when hovering
   * on the XP bar.
   *
   * <p>This should only be false for a skill if there has been less than 2 XP drops during the
   * current tracking session, since there is not enough data to calculate the XP per hour.
   */
  private static HashMap<String, Boolean[]> showXpPerHour = new HashMap<String, Boolean[]>();

  /** An array to store the XP per hour for a given skill */
  private static HashMap<String, Double[]> xpPerHour = new HashMap<String, Double[]>();

  // the total XP gained in a given skill within the sample period
  private static final int TOTAL_XP_GAIN = 0;
  // the time of the last XP drop in a given skill
  private static final int TIME_OF_LAST_XP_DROP = 1;
  // the time of the first XP drop in a given skill within the sample period
  private static final int TIME_OF_FIRST_XP_DROP = 2;
  // the total number of XP drops recorded within the sample period, plus 1
  private static final int TOTAL_XP_DROPS = 3;
  // the amount of XP gained since last processed
  private static final int LAST_XP_GAIN = 4;

  // first dimension of this array is skill ID.
  // second dimension is the constants in block above.
  private static HashMap<String, Double[][]> lastXpGain = new HashMap<String, Double[][]>();

  // holds players XP since last processing xp drops
  private static HashMap<String, Float[]> xpLast = new HashMap<String, Float[]>();

  public static HashMap<String, Integer[]> xpGoals = new HashMap<String, Integer[]>();
  public static HashMap<String, Float[]> lvlGoals = new HashMap<String, Float[]>();

  private static float[] xpGain = new float[18];



  public static final int MENU_NONE = 0;
  public static final int MENU_INVENTORY = 1;
  public static final int MENU_MINIMAP = 2;
  public static final int MENU_STATS = 3;
  public static final int MENU_FRIENDS = 4;
  public static final int MENU_SETTINGS = 5;

  public static final int CHAT_NONE = 0;
  public static final int CHAT_PRIVATE = 1;
  public static final int CHAT_PRIVATE_OUTGOING = 2;
  public static final int CHAT_QUEST = 3;
  public static final int CHAT_CHAT = 4;
  public static final int CHAT_PRIVATE_LOG_IN_OUT = 5;
  public static final int CHAT_TRADE_REQUEST_RECEIVED = 6;
  public static final int CHAT_OTHER = 7;
  // used for when you send a player a duel/trade request, follow someone, or drop an item

  public static final int CHAT_INCOMING_OPTION = 8;
  public static final int CHAT_CHOSEN_OPTION = 9;
  public static final int CHAT_WINDOWED_MSG = 10;

  public static final int COMBAT_CONTROLLED = 0;
  public static final int COMBAT_AGGRESSIVE = 1;
  public static final int COMBAT_ACCURATE = 2;
  public static final int COMBAT_DEFENSIVE = 3;
  
  public static int tileSize;
  public static int regionX = -1;
  public static int regionY = -1;
  public static int worldX = -1;
  public static int worldY = -1;
  public static int coordX = -1;
  public static int coordY = -1;
  public static int localRegionX = -1; // not a thing on early mud
  public static int localRegionY = -1; // not a thing on early mud
  public static int planeWidth = -1;
  public static int planeHeight = -1;
  public static int planeIndex = -1;
  public static boolean loadingArea = false;


  public static void init() {

    handler_mouse = new MouseHandler();
    handler_keyboard = new KeyboardHandler();

    Applet applet = Game.getInstance().getApplet();
    applet.addMouseListener(handler_mouse);
    applet.addMouseMotionListener(handler_mouse);
    applet.addMouseWheelListener(handler_mouse);
    applet.addKeyListener(handler_keyboard);
    applet.setFocusTraversalKeysEnabled(false);
  }

  public static Double fetchLatestVersionNumber() {
    try {
      Double currentVersion = 0.0;
      URL updateURL =
              new URL(
                      "https://raw.githubusercontent.com/RSCPlus/rsctimes/master/src/Client/Settings.java");

      // Open connection
      URLConnection connection = updateURL.openConnection();
      connection.setConnectTimeout(3000);
      connection.setReadTimeout(3000);
      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      // in our current client version, we are looking at the source file of Settings.java in the
      // main repository in order to parse what the current version number is.
      String line;
      while ((line = in.readLine()) != null) {
        if (line.contains("VERSION_NUMBER")) {
          currentVersion =
                  Double.parseDouble(line.substring(line.indexOf('=') + 1, line.indexOf(';')));
          Logger.Info(String.format("@|green Current Version: %f|@", currentVersion));
          break;
        }
      }

      // Close connection
      in.close();
      return currentVersion;
    } catch (Exception e) {
      displayMessage("@dre@Error checking latest version", CHAT_NONE);
      return Settings.VERSION_NUMBER;
    }
  }

  /**
   * Compares the local value of {@link Settings#VERSION_NUMBER} to the value on the GitHub master
   * branch.
   *
   * <p>Used to check if there is a newer version of the client available.
   *
   * @param announceIfUpToDate if a message should be displayed in chat if the client is up-to-date
   */
  public static void checkForUpdate(boolean announceIfUpToDate) {
    double latestVersion = fetchLatestVersionNumber();
    if (latestVersion > Settings.VERSION_NUMBER) {
      displayMessage("@gre@A new version of RSCx is available!", CHAT_QUEST);
      // TODO: before Y10K update this to %9.6f
      displayMessage(
              "The latest version is @gre@" + String.format("%8.6f", latestVersion), CHAT_QUEST);
      displayMessage(
              "~034~ Your version is @red@" + String.format("%8.6f", Settings.VERSION_NUMBER),
              CHAT_QUEST);
      if (Settings.CHECK_UPDATES.get(Settings.currentProfile)) {
        displayMessage(
                "~034~ You will receive the update next time you restart rsctimes", CHAT_QUEST);
      }
    } else if (announceIfUpToDate) {
      displayMessage(
              "You're up to date: @gre@" + String.format("%8.6f", latestVersion), CHAT_QUEST);
    }
  }
  
  public static void init_login() {
	    //Camera.init();
	    state = STATE_LOGIN;
	    //isGameLoaded = false;
	    Renderer.replayOption = 0;

	    //twitch.disconnect();

	    /*if (skipToLogin()) {
	      login_screen = SCREEN_USERNAME_PASSWORD_LOGIN;
	    }*/

	    //resetLoginMessage();
	    //Replay.closeReplayPlayback();
	    //Replay.closeReplayRecording();
	    //adaptStrings();
	    //player_name = "";
	  }
  
  public static void init_game() {
	    //Camera.init();
	    //combat_style = Settings.COMBAT_STYLE.get(Settings.currentProfile);
	    state = STATE_GAME;
	    // bank_active_page = 0; // TODO: config option? don't think this is very important.
	    // combat_timer = 0;
	  }
  
  
  /**
   * Tells the client that the adjacent region is loading, so not to do spikes in position printing
   * 
   * @param loaded - the flag for loaded
   */
  public static void isLoadingHook(boolean loaded) {
	boolean doLoad = !loaded;
    if (worldX == -1 && worldY == -1) {
      worldX = coordX;
      worldY = coordY;
    } else {
      if (doLoad) {
        Camera.reset_lookat();
      } else {
        worldX = coordX;
        worldY = coordY;
      }
    }
  }
  
  /** Returns the coordinates of the player */
  public static String getCoords() {
    return "(" + worldX + "," + worldY + ")";
  }
  
  
  public static int check_draw_string(String inputString, int position, int n, boolean isPos) {
	  int ret = !isPos ? n : 0;
	  if (inputString.charAt(position) == '~' && position + 5 < inputString.length() && inputString.charAt(position + 5) == '~') {
          char c = inputString.charAt(position + 1);
          char c1 = inputString.charAt(position + 2);
          char c2 = inputString.charAt(position + 3);
          char c3 = inputString.charAt(position + 4);
          if (isPos) {
        	  ret = 1;
          } else if (c >= '0' && c <= '9' && c1 >= '0' && c1 <= '9' && c2 >= '0' && c2 <= '9'
          		&& c3 >= '0' && c3 <= '9') {
              ret = Integer.parseInt(inputString.substring(position + 1, position + 5));
          }
	  }
	  return ret;
  }

  /**
   * Prints a client-side message in chat.
   *
   * @param message a message to print
   * @param chat_type the type of message to send
   */
  public static synchronized void displayMessage(String message, int chat_type) {
    switch (chat_type) {
      case CHAT_QUEST:
        message = "@que@" + message;
        break;
    }
    Logger.Info(message); // TODO: remove once this is properly reflected
    // TODO: rehook this for mud38
    if (Client.state != Client.STATE_GAME || Reflection.displayMessage == null) return;

    try {
      Reflection.displayMessage.invoke(
              Client.instance, message, 0);
    } catch (Exception e) {
    }
  }
  
  public static float getXPforLevel(int level) {
    if (level < 2) {
      return 0;
    }

    if (level > Util.xpLevelTable.length - 1) {
      // This probably doesn't ever happen since our lookup table already goes to virtual level 150.
      // levels 1 to 120 are from the official game, level 121 to 150 are from this formula below
      float xp = 0.0f;
      for (int x = 1; x < level; x++) xp += Math.floor(x + 300 * Math.pow(2, x / 7.0f)) / 4.0f;
      return (float) Math.floor(xp);
    }

    // speedier to use a lookup table than to always calculate
    return Util.xpLevelTable[level];
  }

  public static float getLevelFromXP(float xp) {

    // 136.53725 is the maximum level you can reach in RSC before XP rolls over negative
    int lvl = 1;
    while (lvl <= 137 && getXPforLevel(lvl) <= xp) {
      lvl++;
    }
    float xpToLevel = (float) Math.floor(getXPforLevel(lvl) - xp);
    if (xpToLevel > 0) {
      lvl--;
      float xpIntoLevel = (float) Math.floor(xp - getXPforLevel(lvl));
      float xpBetweenLevels = (getXPforLevel(lvl + 1) - getXPforLevel(lvl));
      return lvl + (xpIntoLevel / xpBetweenLevels);
    } else {
      return lvl;
    }
  }

  /**
   * Returns the minimum XP required until the user reaches the next level in a specified skill.
   *
   * @param skill an integer corresponding to a skill
   * @return the minimum XP required until the user reaches the next level in the specified skill
   */
  public static float getXPUntilLevel(int skill) {
    float xpNextLevel = getXPforLevel(base_level[skill] + 1);
    return xpNextLevel - getXP(skill);
  }

  public static float getXPUntilGoal(int skill) {
    return xpGoals.get(xpUsername)[skill] - getXP(skill);
  }

  public static Double getLastXpGain(int skill) {
    return lastXpGain.get(xpUsername)[skill][LAST_XP_GAIN];
  }

  /**
   * Returns the user's XP in a specified skill.
   *
   * @param skill an integer corresponding to a skill
   * @return the user's XP in the specified skill
   */
  public static float getXP(int skill) {
    return (float) xp[skill] / 4.0f;
  }

  /**
   * Returns the user's base level in a specified skill. This number is <b>not</b> affected by
   * skills boosts and debuffs.
   *
   * @param skill an integer corresponding to a skill
   * @return the user's base level in the specified skill
   */
  public static int getBaseLevel(int skill) {
    return base_level[skill];
  }


  public static Boolean[] getShowXpPerHour() {
    return showXpPerHour.get(xpUsername);
  }

  public static Double[] getXpPerHour() {
    return xpPerHour.get(xpUsername);
  }

  public static void resetXPDrops(boolean resetSession) {
    if (username_login.equals("")) {
      return;
    }

    xpUsername = Util.formatString(username_login, 50);
    if (lastXpGain.get(xpUsername) == null) {
      lastXpGain.put(xpUsername, new Double[NUM_SKILLS][5]);
      showXpPerHour.put(xpUsername, new Boolean[NUM_SKILLS]);
      xpPerHour.put(xpUsername, new Double[NUM_SKILLS]);
      xpLast.put(xpUsername, new Float[NUM_SKILLS]);
      for (int skill = 0; skill < NUM_SKILLS; skill++) {
        lastXpGain.get(xpUsername)[skill][TOTAL_XP_GAIN] = new Double(0);
        lastXpGain.get(xpUsername)[skill][TIME_OF_FIRST_XP_DROP] =
                lastXpGain.get(xpUsername)[skill][TIME_OF_LAST_XP_DROP] =
                        new Double(System.currentTimeMillis());
        lastXpGain.get(xpUsername)[skill][TOTAL_XP_DROPS] = new Double(0);

        showXpPerHour.get(xpUsername)[skill] = false;
        xpPerHour.get(xpUsername)[skill] = new Double(0);
      }
    }
    if (xpGoals.get(xpUsername) == null) {
      xpGoals.put(xpUsername, new Integer[NUM_SKILLS]);
      lvlGoals.put(xpUsername, new Float[NUM_SKILLS]);
    }

    for (int skill = 0; skill < NUM_SKILLS; skill++) {
      xpLast.get(xpUsername)[skill] = getXP(skill);

      if (resetSession) {

        lastXpGain.get(xpUsername)[skill][TOTAL_XP_GAIN] = new Double(0);
        lastXpGain.get(xpUsername)[skill][TIME_OF_FIRST_XP_DROP] =
                lastXpGain.get(xpUsername)[skill][TIME_OF_LAST_XP_DROP] =
                        (double) System.currentTimeMillis();
        lastXpGain.get(xpUsername)[skill][TOTAL_XP_DROPS] = new Double(0);
        showXpPerHour.get(xpUsername)[skill] = false;
      }
    }
  }





}
