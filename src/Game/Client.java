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
import Client.NotificationsHandler;
import Client.Util;
import Client.NotificationsHandler.NotifType;

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

  public static long magic_timer = 0L;
  public static long poison_timer = 0L; // compat in case player was in newer rsc world
  public static boolean is_poisoned = false; // compat in case player was in newer rsc world
  public static boolean is_displaying_fps;
  public static int connection_port = 43594;
  
  public static String[] menuOptions;

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

  public static final int MESSAGE_CHAT = 2;
  public static final int MESSAGE_GAME = 3;
  public static final int MESSAGE_INVENTORY = 4;
  public static final int MESSAGE_QUEST = 5;
  public static final int MESSAGE_PRIVATE = 6;

  public static final int CHAT_NONE = 0;
  public static final int CHAT_PRIVATE = 1;
  public static final int CHAT_PRIVATE_OUTGOING = 2;
  public static final int CHAT_QUEST = 3;
  public static final int CHAT_CHAT = 4;
  public static final int CHAT_PRIVATE_LOG_IN_OUT = 5;
  public static final int CHAT_TRADE_REQUEST_RECEIVED =
      6; // only used when another player sends you a trade request. (hopefully!)
  public static final int CHAT_OTHER =
      7; // used for when you send a player a duel/trade request, follow someone, or drop an item

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
      displayMessage("@dre@Error checking latest version", MESSAGE_GAME);
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
      displayMessage("@gre@A new version of RSCx is available!", MESSAGE_QUEST);
      // TODO: before Y10K update this to %9.6f
      displayMessage(
              "The latest version is @gre@" + String.format("%8.6f", latestVersion), MESSAGE_QUEST);
      displayMessage(
              "~034~ Your version is @red@" + String.format("%8.6f", Settings.VERSION_NUMBER),
              MESSAGE_QUEST);
      if (Settings.CHECK_UPDATES.get(Settings.currentProfile)) {
        displayMessage(
                "~034~ You will receive the update next time you restart rsctimes", MESSAGE_QUEST);
      }
    } else if (announceIfUpToDate) {
      displayMessage(
              "You're up to date: @gre@" + String.format("%8.6f", latestVersion), MESSAGE_QUEST);
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
   * This method hooks all chat messages.
   *
   * @param inMessage the content of the message
   * @param messageType the type of message being displayed
   */
  public static void messageHook(
      String inMessage, int messageType) {
	  
	  String username = null; //TODO: decide if extraction of username should be done
	  // since message is (optionally username) + message
	  String message = inMessage;
	  int type = CHAT_NONE;
	  
	  if (messageType == 2 || messageType == 4 || messageType == 6) {
          for (; message.length() > 5 && message.charAt(0) == '@' && message.charAt(4) == '@'; message = message.substring(5)) ;
      } else if (messageType == 5) {
    	  if (message.length() > 5 && message.startsWith("@que@")) {
    		  for (; message.length() > 5 && message.charAt(0) == '@' && message.charAt(4) == '@'; message = message.substring(5)) ;
    	  } else if (message.length() > 7 && !message.startsWith("@que@")
    			  && message.contains("@que@")) {
    		  message = message.replaceFirst("@que@", "");
    	  }
      }

    // Close dialogues when player says something in-game in quest chat
    /*if (Replay.isPlaying) {
      if (username != null && username.equals(Client.player_name) && type == CHAT_QUEST) {
        Replay.closeDialogue = true;
      }
    }

    if (username != null)
      // Prevents non-breaking space in colored usernames appearing as an accented 'a' in console
      username = username.replace("\u00A0", " ");
    if (message != null)
      // Prevents non-breaking space in colored usernames appearing as an accented 'a' in console
      message = message.replace("\u00A0", " ");

    if (message != null && username != null) {
      Speedrun.checkMessageCompletions(message);
    }*/

    if (messageType == MESSAGE_GAME) {
    	username = null;
    	type = CHAT_NONE;
    	if (message.contains("The spell fails! You may try again in 20 seconds"))
            magic_timer = Renderer.time + 21000L;
          else if (Settings.TRAY_NOTIFS.get(Settings.currentProfile)
              && message.contains(
                  "You have been standing here for 5 mins! Please move to a new area")) {
            NotificationsHandler.notify(
                NotifType.LOGOUT, "Logout Notification", "You're about to log out");
          }
          // while the message is really You @gr2@are @gr1@poisioned! @gr2@You @gr3@lose @gr2@3
          // @gr1@health.
          // it can be known looking for "poisioned!"
          else if (message.contains("poisioned!")) {
            is_poisoned = true;
            poison_timer = Renderer.time + 21000L;
          } else if (message.contains("You drink") && message.contains("poison")) {
            is_poisoned = false;
            poison_timer = Renderer.time;
          } else if (message.contains("You retain your skills. Your objects land where you died")
              && is_poisoned) {
            is_poisoned = false;
            poison_timer = Renderer.time;
          }
    } else if (messageType == MESSAGE_PRIVATE) {
    	// should extract sender/receiver here
    	if (message.matches("^(?:@pri@|)You tell.*$")) {
    		type = CHAT_PRIVATE_OUTGOING;
    		NotificationsHandler.notify(NotifType.PM, "PM sent", message);
    	} else {
    		type = CHAT_PRIVATE;
    		NotificationsHandler.notify(NotifType.PM, "PM received", message);
    	}
    } else if (messageType == MESSAGE_INVENTORY) {
    	if (message.contains(" wishes to duel with you")) {
            type = CHAT_OTHER;
    		NotificationsHandler.notify(
                NotifType.DUEL, "Duel Request", message.replaceAll("@...@", ""));
    	}
    	else if (message.contains(" wishes to trade with you")) {
    		type = CHAT_TRADE_REQUEST_RECEIVED;
            NotificationsHandler.notify(
                    NotifType.TRADE, "Trade Request", message.replaceAll("@...@", ""));
    	}
    } else if (messageType == MESSAGE_QUEST) {
    	type = CHAT_QUEST;
    } else if (messageType == MESSAGE_CHAT) {
    	type = CHAT_CHAT;
    }

    // Don't output private messages if option is turned on and replaying
    // TODO when replay is a thing
    /*if (Settings.HIDE_PRIVATE_MSGS_REPLAY.get(Settings.currentProfile) && Replay.isPlaying) {
      if (type == CHAT_PRIVATE_LOG_IN_OUT || type == CHAT_PRIVATE || type == CHAT_PRIVATE_OUTGOING)
        return;
    }*/

    String originalLog =
        "("
            + formatChatType(type)
            + ") "
            + ((username == null) ? "" : formatUsername(username, type))
            + message;
    String colorizedLog =
        "@|white ("
            + formatChatType(type)
            + ")|@ "
            + ((username == null) ? "" : colorizeUsername(formatUsername(username, type), type))
            + colorizeMessage(message, type);
    Logger.Chat(colorizedLog, originalLog);
  }
  
  private static String formatChatType(int type) {
	    String chatType = getChatTypeName(type).toUpperCase();

	    // Make text fixed width so it aligns properly
	    final int fixedWidth = getChatTypeName(CHAT_INCOMING_OPTION).length();
	    while (chatType.length() < fixedWidth) chatType = " " + chatType;

	    return chatType;
	  }

	  private static String getChatTypeName(int type) {
	    switch (type) {
	      case CHAT_NONE:
	        return "none";
	      case CHAT_PRIVATE:
	        return "pm_in";
	      case CHAT_PRIVATE_OUTGOING:
	        return "pm_out";
	      case CHAT_QUEST:
	        return "quest";
	      case CHAT_CHAT:
	        return "chat";
	      case CHAT_PRIVATE_LOG_IN_OUT:
	        return "pm_log";
	      case CHAT_TRADE_REQUEST_RECEIVED:
	        return "trade";
	      case CHAT_OTHER:
	        return "other";
	      case CHAT_INCOMING_OPTION:
	        return "option";
	      case CHAT_CHOSEN_OPTION:
	        return "select";
	      case CHAT_WINDOWED_MSG:
	        return "window";
	      default:
	        return Integer.toString(type);
	    }
	  }

	  /**
	   * Formats the username clause preceding a chat message for use in the console.
	   *
	   * @param username the username associated with the message
	   * @param type the type of message being displayed
	   * @return the formatted username clause
	   */
	  private static String formatUsername(String username, int type) {
	    switch (type) {
	      case CHAT_PRIVATE:
	        // Username tells you:
	        username = username + " tells you: ";
	        break;
	      case CHAT_PRIVATE_OUTGOING:
	        // You tell Username:
	        username = "You tell " + username + ": ";
	        break;
	      case CHAT_QUEST:
	        // If username != null during CHAT_QUEST, then this is your player name
	        username = username + ": ";
	        break;
	      case CHAT_CHAT:
	        username = username + ": ";
	        break;
	      case CHAT_TRADE_REQUEST_RECEIVED: // happens when player trades you
	        username = username + " wishes to trade with you.";
	        break;
	        /* username will not appear in these chat types, but just to cover it I'm leaving code commented out here
	        case CHAT_NONE:
	        case CHAT_PRIVATE_LOG_IN_OUT:
	        case CHAT_PLAYER_INTERRACT_OUT:
	        */
	      default:
	        Logger.Info("Username specified for unhandled chat type, please report this: " + type);
	        username = username + ": ";
	    }

	    return username;
	  }

	  /**
	   * Adds color to the username clause preceding a chat message for use in the console.
	   *
	   * @param colorMessage the username clause to colorize
	   * @param type the type of message being displayed
	   * @return the colorized username clause
	   */
	  public static String colorizeUsername(String colorMessage, int type) {
	    switch (type) {
	      case CHAT_PRIVATE:
	        // Username tells you:
	        colorMessage = "@|cyan,intensity_bold " + colorMessage + "|@";
	        break;
	      case CHAT_PRIVATE_OUTGOING:
	        // You tell Username:
	        colorMessage = "@|cyan,intensity_bold " + colorMessage + "|@";
	        break;
	      case CHAT_QUEST:
	        // If username != null during CHAT_QUEST, then this is your player name, which is usually
	        // white
	        colorMessage = "@|white,intensity_faint " + colorMessage + "|@";
	        break;
	      case CHAT_CHAT:
	        // just bold username for chat
	        colorMessage = "@|yellow,intensity_bold " + colorMessage + "|@";
	        break;
	      case CHAT_TRADE_REQUEST_RECEIVED: // happens when player trades you
	        colorMessage = "@|white " + colorMessage + "|@";
	        break;
	        /* username will not appear in these chat types, but just to cover it I'm leaving code commented out here
	        case CHAT_NONE:
	        case CHAT_PRIVATE_LOG_IN_OUT:
	        case CHAT_PLAYER_INTERRACT_OUT:
	        */

	      default:
	        Logger.Info("Username specified for unhandled chat type, please report this: " + type);
	        colorMessage = "@|white,intensity_bold " + colorMessage + "|@";
	    }
	    return colorMessage;
	  }

	  /**
	   * Adds color to the contents of a chat message for use in the console.
	   *
	   * @param colorMessage the message to colorize
	   * @param type the type of message being displayed
	   * @return the colorized message
	   */
	  public static String colorizeMessage(String colorMessage, int type) {
	    boolean whiteMessage = colorMessage.contains("Welcome to RuneScape!"); // want this to be bold
	    boolean blueMessage =
	        (type == CHAT_NONE)
	            && (colorMessage.contains(
	                "You have been standing here for 5 mins! Please move to a new area"));
	    boolean yellowMessage =
	        (type == CHAT_NONE) && (colorMessage.contains("Well Done")); // tourist trap completion
	    boolean screenshotMessage =
	        (type == CHAT_NONE)
	            && (colorMessage.contains("You just advanced ")
	                || (colorMessage.contains("quest point") && colorMessage.endsWith("!"))
	                || colorMessage.contains("ou have completed"));
	    boolean greenMessage =
	        screenshotMessage
	            || (type == CHAT_NONE
	                && (colorMessage.contains("poisioned!")
	                    || colorMessage.contains("***"))); // "***" is for Tourist Trap completion

	    /*if (screenshotMessage
	        && Settings.AUTO_SCREENSHOT.get(Settings.currentProfile)
	        && !Replay.isPlaying) {
	      Renderer.takeScreenshot(true);
	    }*/

	    if (blueMessage) { // this is one of the messages which we must overwrite expected color for
	      return "@|cyan,intensity_faint " + colorReplace(colorMessage) + "|@";
	    } else if (greenMessage) {
	      return "@|green,intensity_bold " + colorReplace(colorMessage) + "|@";
	    } else if (whiteMessage) {
	      // if (colorMessage.contains("Welcome to RuneScape!")) {
	      // this would be necessary if whiteMessage had more than one .contains()
	      // }

	      return "@|white,intensity_bold " + colorMessage + "|@";
	    } else if (yellowMessage) {
	      return "@|yellow,intensity_bold " + colorMessage + "|@";
	    }

	    switch (type) {
	      case CHAT_PRIVATE:
	      case CHAT_PRIVATE_OUTGOING:
	        // message to/from PMs
	        colorMessage = "@|cyan,intensity_faint " + colorReplace(colorMessage) + "|@";
	        break;
	      case CHAT_QUEST:
	        if (colorMessage.contains(":") && !colorMessage.startsWith("***")) {
	          // this will be like "banker: would you like to access your bank account?" which should be
	          // yellow. Avoids yellow print the message of tourist trap
	          colorMessage = "@|yellow,intensity_faint " + colorReplace(colorMessage) + "|@";
	        } else {
	          // this is usually skilling
	          colorMessage = "@|white,intensity_faint " + colorReplace(colorMessage) + "|@";
	        }
	        break;
	      case CHAT_CHAT:
	        colorMessage = "@|yellow,intensity_faint " + colorReplace(colorMessage) + "|@";
	        break;
	      case CHAT_PRIVATE_LOG_IN_OUT:
	        // don't need to colorReplace, this is just "username has logged in/out"
	        colorMessage = "@|cyan,intensity_faint " + colorMessage + "|@";
	        break;
	      case CHAT_NONE: // have to replace b/c @cya@Screenshot saved...
	      case CHAT_TRADE_REQUEST_RECEIVED:
	      case CHAT_WINDOWED_MSG:
	      case CHAT_OTHER:
	        colorMessage = "@|white " + colorReplace(colorMessage) + "|@";
	        break;
	      case CHAT_INCOMING_OPTION:
	        colorMessage = "@|cyan,intensity_faint " + colorReplace(colorMessage) + "|@";
	        break;
	        // faint red since it would have been a hover/selection over the item
	      case CHAT_CHOSEN_OPTION:
	        colorMessage = "@|red,intensity_faint " + colorReplace(colorMessage) + "|@";
	        break;
	      default: // this should never happen, only 10 Chat Types
	        Logger.Info("Unhandled chat type in colourizeMessage, please report this:" + type);
	        colorMessage = "@|white,intensity_faint " + colorReplace(colorMessage) + "|@";
	    }
	    return colorMessage;
	  }

	  public static String colorReplace(String colorMessage) {
	    final String[] colorDict = { // TODO: Make this a class variable
	      // less common colors should go at the bottom b/c we can break search loop early
	      "(?i)@cya@", "|@@|cyan ",
	      "(?i)@whi@", "|@@|white ",
	      "(?i)@red@", "|@@|red ",
	      "(?i)@gre@", "|@@|green ",
	      "(?i)@lre@", "|@@|red,intensity_faint ",
	      "(?i)@dre@", "|@@|red,intensity_bold ",
	      "(?i)@ran@", "|@@|red,blink_fast ", // TODO: consider handling this specially
	      "(?i)@yel@", "|@@|yellow ",
	      "(?i)@mag@", "|@@|magenta,intensity_bold ",
	      "(?i)@gr1@", "|@@|green ",
	      "(?i)@gr2@", "|@@|green ",
	      "(?i)@gr3@", "|@@|green ",
	      "(?i)@ora@", "|@@|red,intensity_faint ",
	      "(?i)@or1@", "|@@|red,intensity_faint ",
	      "(?i)@or2@",
	          "|@@|red,intensity_faint ", // these are all basically the same color, even in game
	      "(?i)@or3@", "|@@|red ",
	      "(?i)@blu@", "|@@|blue ",
	      "(?i)@bla@", "|@@|black "
	    };
	    for (int i = 0; i + 1 < colorDict.length; i += 2) {
	      if (!colorMessage.matches(".*@.{3}@.*")) { // if doesn't contain any color codes: break;
	        break;
	      }
	      colorMessage = colorMessage.replaceAll(colorDict[i], colorDict[i + 1]);
	    }

	    // we could replace @.{3}@ with "" to remove "@@@@@" or "@dne@" (i.e. color code which does not
	    // exist) just like
	    // in chat box, but I think it's more interesting to leave the misspelled stuff in terminal

	    // could also respect ~xxx~ but not really useful.

	    return colorMessage;
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
    if (Client.state != Client.STATE_GAME || Reflection.displayMessage == null) return;

    try {
      Reflection.displayMessage.invoke(
              Client.instance, message, 0);
    } catch (Exception e) {
    }
  }
  
  /**
   * This method hooks all options received and adds them to console
   *
   * @param menuOptions The options received from server
   * @param count The count for the options
   */
  public static void receivedOptionsHook(String[] menuOptions, int count) {
    /*if (Settings.PARSE_OPCODES.get(Settings.currentProfile)
        && (Replay.isSeeking || Replay.isRestarting)) return;*/
	  

    Client.printReceivedOptions(menuOptions, count);
  }

  public static void printReceivedOptions(String[] menuOptions, int count) {
    int type = CHAT_INCOMING_OPTION;

    String option = "";
    Client.menuOptions = menuOptions;
    for (int i = 0; i < count; i++) {
      option = menuOptions[i];

      String originalLog = "(" + formatChatType(type) + ") " + option;
      String colorizedLog =
          "@|white (" + formatChatType(type) + ")|@ " + colorizeMessage(option, type);
      Logger.Chat(colorizedLog, originalLog);
    }
  }

  /**
   * This method hooks into selected option from incoming options and adds the selection to console
   *
   * @param possibleOptions The possible options that the user saw from server
   * @param selection The chosen option
   */
  public static void selectedOptionHook(String[] possibleOptions, int selection) {
    // Do not run anything below here while seeking or playing as is handled separately
    //if (Replay.isPlaying || Replay.isSeeking || Replay.isRestarting) return;

    Client.printSelectedOption(possibleOptions, selection);
  }

  public static void printSelectedOption(String[] possibleOptions, int selection) {
    int type = CHAT_CHOSEN_OPTION;
    if (selection < 0) return;

    int select =
        (KeyboardHandler.dialogue_option == -1) ? selection : KeyboardHandler.dialogue_option;
    String option = possibleOptions[select];

    String originalLog = "(" + formatChatType(type) + ") " + option;
    String colorizedLog =
        "@|white (" + formatChatType(type) + ")|@ " + colorizeMessage(option, type);
    Logger.Chat(colorizedLog, originalLog);
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
