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
import Client.Logger;
import Client.NotificationsHandler;
import Client.NotificationsHandler.NotifType;
import Client.ScaledWindow;
import Client.Settings;
import Client.TwitchIRC;
import Client.Util;
import Client.WorldMapWindow;
import java.applet.Applet;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Client {

  // Game's client instance
  public static Object instance;

  public static List<NPC> npc_list = new ArrayList<>();
  public static List<NPC> npc_list_retained = new ArrayList<>();
  public static List<Item> item_list = new ArrayList<>();
  public static List<Item> item_list_retained = new ArrayList<>();

  public static final int SKILL_ATTACK = 0;
  public static final int SKILL_DEFENSE = 1;
  public static final int SKILL_STRENGTH = 2;
  public static final int SKILL_HP = 3;
  public static final int SKILL_RANGED = 4;
  public static final int SKILL_THIEVING = 5;
  public static final int SKILL_INFLUENCE = 6;
  public static final int SKILL_PRAYGOOD = 7;
  public static final int SKILL_PRAYEVIL = 8;
  public static final int SKILL_GOODMAGIC = 9;
  public static final int SKILL_EVILMAGIC = 10;
  public static final int SKILL_COOKING = 11;
  public static final int SKILL_TAILORING = 12;
  public static final int SKILL_WOODCUTTING = 13;
  public static final int SKILL_FIREMAKING = 14;
  public static final int SKILL_CRAFTING = 15;
  public static final int SKILL_SMITHING = 16;
  public static final int SKILL_MINING = 17;
  public static final int SKILL_HERBLAW = 18;

  public static Object player_object;
  public static String player_name = "";
  public static int player_posX = -1;
  public static int player_posY = -1;
  public static int player_height = -1;
  public static int player_width = -1;

  private static TwitchIRC twitch = new TwitchIRC();

  public static MouseHandler handler_mouse;
  public static KeyboardHandler handler_keyboard;

  public static boolean is_hover;

  private static long updateTimer = 0;
  private static long last_time = 0;

  public static boolean showRecordAlwaysDialogue = false;

  public static long update_timer;
  public static long updates;
  public static long updatesPerSecond;

  public static final int STATE_LOGIN = 1;
  public static final int STATE_GAME = 2;

  public static final int SCREEN_CLICK_TO_LOGIN = 0; // also is this value while logged in
  public static final int SCREEN_TERMS_CONDITIONS = 1;
  public static final int SCREEN_USERNAME_PASSWORD_LOGIN = 2;
  public static final int SCREEN_REGISTER_NEW_ACCOUNT = 3;

  public static int state = STATE_LOGIN;

  public static int max_inventory = 30;
  public static int inventory_count;
  public static int[] inventory_items;
  public static long magic_timer = 0L;
  public static long poison_timer = 0L; // compat in case player was in newer rsc world
  public static boolean is_poisoned = false; // compat in case player was in newer rsc world
  public static boolean is_displaying_fps;
  public static int connection_port = 43594;

  public static String[] menuOptions;

  public static int[] current_equipment_stats;
  public static int[] current_level;
  public static int[] base_level;
  public static int[] xp; // cannot be known from client
  public static String[] skill_name;

  public static int login_screen;

  public static int combat_timer;
  public static boolean isGameLoaded;

  public static int show_friends;
  public static int show_menu;
  public static boolean show_questionmenu;
  public static boolean show_shop;
  public static boolean show_trade;
  public static int show_changepk;
  public static boolean show_visitad;

  public static XPDropHandler xpdrop_handler = new XPDropHandler();
  public static XPBar xpbar = new XPBar();

  public static String xpUsername = "";
  public static String modal_text;
  public static String modal_enteredText;
  public static String username_login;

  public static final int NUM_SKILLS = 19; // TODO: ?
  public static boolean firstTimeRunningRSCTimes = false;

  public static int mouse_click;

  public static HashMap<String, Float[]> lvlGoals = new HashMap<String, Float[]>();

  public static final int MENU_NONE = 0;
  public static final int MENU_INVENTORY = 1;
  public static final int MENU_MINIMAP = 2;
  public static final int MENU_STATS = 3;
  public static final int MENU_MAGIC = 4;
  public static final int MENU_FRIENDS = 5;
  public static final int MENU_SETTINGS = 6;

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
  public static int combat_style;

  public static int friends_count;
  public static long[] friends_hash;
  public static int[] friends_online;

  public static int ignores_count;
  public static long[] ignores_hash;

  public static int tileSize;
  public static long menu_timer;
  public static String lastAction;
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

  public static boolean justLoggedIn = false;

  public static final String[] colorDict = {
    // less common colors should go at the bottom b/c we can break search loop early
    // several of the orange & green colours are basically the same colour, even in-game
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
    "(?i)@or2@", "|@@|red,intensity_faint ",
    "(?i)@or3@", "|@@|red ",
    "(?i)@blu@", "|@@|blue ",
    "(?i)@bla@", "|@@|black "
  };

  public static void init() {

    handler_mouse = new MouseHandler();
    handler_keyboard = new KeyboardHandler();

    Applet applet = Game.getInstance().getApplet();
    applet.addMouseListener(handler_mouse);
    applet.addMouseMotionListener(handler_mouse);
    applet.addMouseWheelListener(handler_mouse);
    applet.addKeyListener(handler_keyboard);
    applet.setFocusTraversalKeysEnabled(false);

    // Initialize login
    init_login();
  }

  /**
   * An updater that runs frequently to update calculations for XP drops, the XP bar, etc.
   *
   * <p>This updater does not handle any rendering, for rendering see {@link Renderer#present}
   */
  public static void update() {
    long time = System.currentTimeMillis();
    long nanoTime = System.nanoTime();

    float delta_time = (float) (nanoTime - last_time) / 1000000000.0f;
    last_time = nanoTime;

    Camera.setLookatTile(getPlayerWaypointX(), getPlayerWaypointY());
    Camera.update(delta_time);

    // Replay.update();

    /*if (Settings.RECORD_AUTOMATICALLY_FIRST_TIME.get(Settings.currentProfile)
        && showRecordAlwaysDialogue) {

      String confirmDefaultRecordMessage =
          "If you'd like, you can record your session every time you play by default.<br/>"
              + "<br/>"
              + "These recordings do not leave your computer unless you manually do it on purpose.<br/>"
              + "They also take up negligible space. You could fit about a 6 hour session on a floppy disk, depending on what you do.<br/>"
              + "<br/>"
              + "Recordings can be played back later, even offline, and capture the data the server sends and that you send the server.<br/>"
              + "Your password is not in the capture.<br/>"
              + "<br/>"
              + "Would you like to record all your play sessions by default?<br/>"
              + "<br/>"
              + "<b>NOTE:</b> This option can be toggled in the Settings interface (ctrl-o by default) under the Replay tab.";

      JPanel confirmDefaultRecordPanel = Util.createOptionMessagePanel(confirmDefaultRecordMessage);

      int response =
          JOptionPane.showConfirmDialog(
              Game.getInstance().getApplet(),
              confirmDefaultRecordPanel,
              "rscplus",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.INFORMATION_MESSAGE,
              Launcher.scaled_option_icon);
      if (response == JOptionPane.YES_OPTION || response == JOptionPane.CLOSED_OPTION) {
        Settings.RECORD_AUTOMATICALLY.put(Settings.currentProfile, true);
      } else if (response == JOptionPane.NO_OPTION) {
        Settings.RECORD_AUTOMATICALLY.put(Settings.currentProfile, false);
      }
      Settings.RECORD_AUTOMATICALLY_FIRST_TIME.put(Settings.currentProfile, false);
      Settings.save();
    }*/

    if (state == STATE_GAME) {
      Client.getPlayerName();
      if (Client.lvlGoals.get(Util.formatString(player_name, 50)) == null) {
        resetXPDrops(false);
      }
      // Client.adaptLoginInfo();
    }

    Game.getInstance().updateTitle();

    /*if (forceDisconnect) {
      Client.closeConnection(false);
      forceDisconnect = false;
    }

    if (forceReconnect) {
      Client.loseConnection(false);
      forceReconnect = false;
    }

    // Handle skipping to next replay
    if (!Replay.isPlaying && Replay.replayServer != null && Replay.replayServer.isDone) {
      if (ReplayQueue.currentIndex < ReplayQueue.queue.size()) {
        if (!ReplayQueue.skipped) {
          ReplayQueue.nextReplay();
        }
      }
      ReplayQueue.skipped = false;
    }

    // Process playback actions for replays
    Replay.processPlaybackAction();

    // Process playback queue for replays
    ReplayQueue.processPlaybackQueue();

    // Close replay, order matters on these two!
    if (runReplayCloseHook) {
      Replay.handleReplayClosing();
      runReplayCloseHook = false;
    }

    // Login hook on this thread
    if (runReplayHook && state == STATE_LOGIN) {
      Renderer.replayOption = 2;
      runReplayHook = false;
      login_hook();
    }*/

    WorldMapWindow.UpdateView();
    if (Client.state == Client.STATE_GAME) {
      WorldMapWindow.Update();
    } else {
      WorldMapWindow.Reset();
    }

    updates++;
    time = System.currentTimeMillis();
    if (time >= update_timer) {
      updatesPerSecond = updates;
      update_timer = time + 1000;
      updates = 0;
    }
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
            "~034~ You will receive the update next time you restart RSCTimes", MESSAGE_QUEST);
      }
    } else if (announceIfUpToDate) {
      displayMessage(
          "You're up to date: @gre@" + String.format("%8.6f", latestVersion), MESSAGE_QUEST);
    }
  }

  public static void init_login() {
    Camera.init();
    state = STATE_LOGIN;
    isGameLoaded = false;
    Renderer.replayOption = 0;

    twitch.disconnect();

    if (skipToLogin()) {
      login_screen = SCREEN_USERNAME_PASSWORD_LOGIN;
    }

    resetLoginMessage();
    // Replay.closeReplayPlayback();
    // Replay.closeReplayRecording();
    // adaptStrings();
    player_name = "";
  }

  public static void init_game() {
    Camera.init();
    combat_style = Settings.COMBAT_STYLE.get(Settings.currentProfile);
    state = STATE_GAME;
    // bank_active_page = 0; // TODO: config option? don't think this is very important.
    // combat_timer = 0;
    allTheWayLoggedIn();
  }

  public static boolean skipToLogin() {
    if (firstTimeRunningRSCTimes) {
      return false;
    }

    boolean skipToLogin = false;

    if (Settings.noWorldsConfigured
        || (Settings.WORLDS_TO_DISPLAY == 1 && Settings.WORLD.get(Settings.currentProfile) != 0)) {
      String curWorldURL = Settings.WORLD_URLS.get(1);
      try {
        String address = InetAddress.getByName(curWorldURL).toString();
        if (address.contains("localhost") || address.contains("127.0.0.1")) {
          // no configured world or localhost only
          skipToLogin = true;
        }
      } catch (UnknownHostException e) {
        skipToLogin = true;
      }
    }

    return skipToLogin || Settings.START_LOGINSCREEN.get(Settings.currentProfile);
  }

  /**
   * Method that gets called when starting game, normally would go to Welcome screen but if no world
   * configured (using RSC× for replay mode) skip directly to login for replays
   */
  public static void resetLoginHook() {
    if (skipToLogin()) {
      login_screen = SCREEN_USERNAME_PASSWORD_LOGIN;
    }
    resetLoginMessage();
  }

  // triggered after resetGame() completes on mudclient
  // since resetGame() also is called on reconnect has to be filtered out
  public static void allTheWayLoggedIn() {
    if (new Exception().getStackTrace()[3].getClassName().contains("jagex.client.e")) {
      justLoggedIn = true;
    }
  }

  public static void check_init() {
    if (justLoggedIn) {
      // triggered on receiving Welcome screen; opcode 182
      if (Settings.FIRST_TIME.get(Settings.currentProfile)) {
        Settings.FIRST_TIME.put(Settings.currentProfile, false);
        Settings.save();
      }

      // Get keybind to open the config window so that we can tell the player how to open it
      if (Settings.REMIND_HOW_TO_OPEN_SETTINGS.get(Settings.currentProfile)) {
        String configWindowShortcut = "";
        for (KeybindSet kbs : KeyboardHandler.keybindSetList) {
          if ("show_config_window".equals(kbs.getCommandName())) {
            configWindowShortcut = kbs.getFormattedKeybindText();
            break;
          }
        }
        if ("".equals(configWindowShortcut)) {
          Logger.Error("Could not find the keybind for the config window!");
          configWindowShortcut = "<Keybind error>";
        }

        displayMessage("@mag@Type @yel@::help@mag@ for a list of commands", CHAT_QUEST);
        displayMessage(
            "@mag@Open the settings by @yel@clicking the wrench icon@mag@, pressing @yel@"
                + configWindowShortcut
                + "@mag@, or from the @yel@tray icon",
            CHAT_QUEST);
      }

      if (TwitchIRC.isUsing()) twitch.connect();

      // Check for updates every login at most once per hour,
      // so users are notified when an update is available
      long currentTime = System.currentTimeMillis();
      if (Settings.CHECK_UPDATES.get(Settings.currentProfile) && currentTime >= updateTimer) {
        checkForUpdate(false);
        updateTimer = currentTime + (60 * 60 * 1000);
      }

      resetXPDrops(false);

      // Re-validate the current scaling upon logging in, in case something
      // went wrong during the initial window creation and resizing.
      ScaledWindow.getInstance().validateAppletSize();

      justLoggedIn = false;
    }
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

  public static void sortFriends() {
    if (!Settings.SORT_FRIENDS.get(Settings.currentProfile)) {
      return;
    }

    try {
      int count = (int) Reflection.friendCount.get(Client.instance);
      long[] hashes = (long[]) Reflection.friendHash.get(Client.instance);
      int[] online = (int[]) Reflection.friendOnline.get(Client.instance);

      int i = 1;
      while (i != 0) {
        i = 0;
        for (int j = 0; j < count - 1; j++) {
          if (online[j] < online[(j + 1)]) {
            int k = online[j];
            online[j] = online[(j + 1)];
            online[(j + 1)] = k;
            long l = hashes[j];
            hashes[j] = hashes[(j + 1)];
            hashes[(j + 1)] = l;
            i = 1;
          }
        }
      }

      Reflection.friendHash.set(Client.instance, hashes);
      Reflection.friendOnline.set(Client.instance, online);

    } catch (IllegalArgumentException | IllegalAccessException e) {
    }
  }

  /**
   * Extensible method hooking to draw other dialog boxes and consuming mouse method. Branching
   * should match conditions inside showTextInputDialog()
   */
  public static void drawTextInputDialogMouseHook(int mouseX, int mouseY, int mouseButtonClick) {
    if (XPBar.shouldShowGoalInput()) {
      XPBar.drawGoalLvlInput(mouseX, mouseY, mouseButtonClick);
    }
  }

  /**
   * Extensible method hooking to consume key press for panel, should return non-zero if we need to
   * stop doing future checks when returning
   */
  public static int gameKeyPressHook(int loggedIn, int key) {
    if (loggedIn != 1) {
      return 0;
    } else if (XPBar.shouldConsumeKey()) {
      return XPBar.keyHandler();
    }
    return 0;
  }

  /** Return true if there is pending render to show the text-input-box dialog */
  public static boolean shouldShowTextInputDialog() {
    return XPBar.shouldShowGoalInput();
  }

  public static void resetLoginMessage() {
    setResponseMessage("Please enter your", "username and password");
  }

  public static boolean shouldHideFPS() {
    return Settings.HIDE_FPS.get(Settings.currentProfile);
  }

  /** Called if Profile SAVE_LOGIN_INFO set, to not clear login info when selecting existing user */
  public static void keep_login_info_hook() {
    Client.login_screen = SCREEN_USERNAME_PASSWORD_LOGIN;
    setResponseMessage("Please enter your", "username and password");
    // Panel.setFocus(Client.panelLogin, Client.loginUserInput);
  }

  /**
   * Hooks the message that hovering over X thing gives in the client
   *
   * @param tooltipMessage - the message in raw color format
   */
  public static String mouse_action_hook(String tooltipMessage) {
    MouseText.mouseText = tooltipMessage;
    MouseText.regenerateCleanedMouseTexts();

    // Remove top-left action text in extended mode
    if (Settings.SHOW_MOUSE_TOOLTIP.get(Settings.currentProfile)
        && Settings.SHOW_EXTENDED_TOOLTIP.get(Settings.currentProfile)) return "";

    return tooltipMessage;
  }

  public static void getPlayerName() {
    try {
      String name = (String) Reflection.characterName.get(player_object);
      if (name != null) {
        if (!name.equals(player_name)) {
          player_name = name;
          Camera.reset_lookat();
        }
      }
    } catch (IllegalArgumentException | IllegalAccessException e1) {
      e1.printStackTrace();
    }
  }

  public static int getPlayerWaypointX() {
    int x = 0;
    try {
      x = (int) Reflection.characterWaypointX.get(player_object);
    } catch (Exception e) {
    }
    return x;
  }

  public static int getPlayerWaypointY() {
    int y = 0;
    try {
      y = (int) Reflection.characterWaypointY.get(player_object);
    } catch (Exception e) {
    }
    return y;
  }

  /** Returns the coordinates of the player */
  public static String getCoords() {
    return "(" + worldX + "," + worldY + ")";
  }

  public static int check_draw_string(String inputString, int position, int n, boolean isPos) {
    int ret = !isPos ? n : 0;
    if (inputString.charAt(position) == '~'
        && position + 5 < inputString.length()
        && inputString.charAt(position + 5) == '~') {
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
  public static void messageHook(String inMessage, int messageType) {

    String username = null; // TODO: decide if extraction of username should be done
    // since message is (optionally username) + message
    String message = inMessage;
    int type = CHAT_NONE;

    if (messageType == 2 || messageType == 4 || messageType == 6) {
      for (;
          message.length() > 5 && message.charAt(0) == '@' && message.charAt(4) == '@';
          message = message.substring(5)) ;
    } else if (messageType == 5) {
      if (message.length() > 5 && message.startsWith("@que@")) {
        for (;
            message.length() > 5 && message.charAt(0) == '@' && message.charAt(4) == '@';
            message = message.substring(5)) ;
      } else if (message.length() > 7
          && !message.startsWith("@que@")
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
      if (message.contains("The spell fails! You may try again in 20 seconds")) {
        magic_timer = Renderer.time + 21000L;
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
      } else if (message.contains("You are under attack!")) {
        NotificationsHandler.notify(NotifType.UNDER_ATTACK, "PVP", message);
      } else if (message.contains(" wishes to trade with you")) {
        type = CHAT_TRADE_REQUEST_RECEIVED;
        NotificationsHandler.notify(
            NotifType.TRADE, "Trade Request", message.replaceAll("@...@", ""));
      }
    } else if (messageType == MESSAGE_PRIVATE) {
      // should extract sender/receiver here
      if (message.matches("^(?:@pri@|)You tell.*$")) {
        type = CHAT_PRIVATE_OUTGOING;
        NotificationsHandler.notify(NotifType.PM, "PM sent", message);
      } else if (message.matches("^(?:@pri@|).*tells you.*$")) {
        type = CHAT_PRIVATE;
        NotificationsHandler.notify(NotifType.PM, "PM received", message);
      }
    } else if (messageType == MESSAGE_INVENTORY) {
      // TODO: Nothing here at the moment
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

    if (screenshotMessage
        && Settings.AUTO_SCREENSHOT.get(Settings.currentProfile)
        && !Replay.isPlaying) {
      Renderer.takeScreenshot(true);
    }

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
   * Intercepts chat messages sent by the user and parses them for commands.
   *
   * @param line a chat message sent by the user
   * @return a modified chat message
   */
  public static String processChatCommand(String line) {
    // TODO: Move Twitch related checks to their own method to stay consistent
    if (TwitchIRC.isUsing() && line.startsWith("/")) {
      String message = line.substring(1, line.length());
      String[] messageArray = message.split(" ");

      message = processClientChatCommand(message);

      if (messageArray.length > 1 && "me".equals(messageArray[0])) {
        message = message.substring(3);
        twitch.sendEmote(message, true);
      } else {
        twitch.sendMessage(message, true);
      }
      return "::null";
    }

    line = processClientChatCommand(line);
    processClientCommand(line);

    return line;
  }

  // TODO: Use processClientChatCommand instead of this method
  public static String processPrivateCommand(String line) {
    return processClientChatCommand(line);
  }

  /**
   * Parses a chat message sent by the user for client related commands.
   *
   * @param line a chat message sent by the user
   * @return a modified chat message
   */
  private static String processClientCommand(String line) {
    if (line.startsWith("::")) {
      String[] commandArray = line.substring(2, line.length()).toLowerCase().split(" ");

      switch (commandArray[0]) {
        case "togglescaling":
          Settings.toggleWindowScaling();
          break;
        case "scaleup":
          Settings.increaseScale();
          break;
        case "scaledown":
          Settings.decreaseScale();
          break;
          /*case "togglebypassattack":
            Settings.toggleAttackAlwaysLeftClick();
            break;
          case "toggleroofs":
            Settings.toggleHideRoofs();
            break;
          case "togglecombat":
            Settings.toggleCombatMenuShown();
            break;
          case "togglecolor":
            Settings.toggleColorTerminal();
            break;
          case "togglehitbox":
            Settings.toggleShowHitbox();
            break;
          case "toggletwitch":
            Settings.toggleTwitchHide();
            break;
          case "toggleplayerinfo":
            Settings.toggleShowPlayerNameOverlay();
            break;
          case "togglefriendinfo":
            Settings.toggleShowFriendNameOverlay();
            break;
          case "togglenpcinfo":
            Settings.toggleShowNPCNameOverlay();
            break;
          case "toggleidsinfo":
            Settings.toggleExtendIdsOverlay();
            break;
          case "toggleiteminfo":
            Settings.toggleShowItemGroundOverlay();
            break;*/
        case "screenshot":
          Renderer.takeScreenshot(false);
          break;
          /*case "debug":
            Settings.toggleDebug();
            break;
          case "fov":
            if (commandArray.length > 1) {
              Settings.setClientFoV(commandArray[1]);
            }
            break;
          case "logout":
            Client.logout();
            break;
          case "toggleposition":
            Settings.togglePosition();
            break;
          case "toggleretrofps":
            Settings.toggleRetroFPS();
            break;
          case "toggleinvcount":
            Settings.toggleInvCount();
            break;
          case "togglebuffs":
            Settings.toggleBuffs();
            break;
          case "togglestatusdisplay":
            Settings.toggleHpPrayerFatigueOverlay();
            break;*/
        case "help":
          try {
            Help.help(Integer.parseInt(commandArray[2]), commandArray[1]);
          } catch (Exception e) {
            Help.help(0, "help");
          }
          break;
          /*case "endrun":
            Settings.endSpeedrun();
            break;
          case "set_pitch":
            try {
              Camera.pitch_rsctimes = Integer.parseInt(commandArray[1]);
              if (Camera.pitch_rsctimes < 0) Camera.pitch_rsctimes = 0;
              if (Camera.pitch_rsctimes > 1023) Camera.pitch_rsctimes = 1023;
            } catch (ArrayIndexOutOfBoundsException ex) {
              displayMessage(
                  "You must specify a number to set the pitch to. 112 is default.", CHAT_QUEST);
            } catch (NumberFormatException ex) {
              displayMessage("That is not a number.", CHAT_QUEST);
            }
            break;
          case "set_height":
            try {
              Camera.offset_height = Integer.parseInt(commandArray[1]);
            } catch (ArrayIndexOutOfBoundsException ex) {
              displayMessage(
                  "You must specify a number to set the height offset to. 0 is default.", CHAT_QUEST);
            } catch (NumberFormatException ex) {
              displayMessage("That is not a number.", CHAT_QUEST);
            }
            break;*/
        default:
          if (commandArray[0] != null) {
            return "::";
          }
          break;
      }
    }

    return line;
  }

  /**
   * Parses a chat message sent by the user for chat related commands.
   *
   * @param line a chat message sent by the user
   * @return a modified chat message
   */
  private static String processClientChatCommand(String line) {
    if (line.startsWith("::")) {
      String command = line.substring(2, line.length()).toLowerCase();

      if ("total".equals(command)) {
        return "My Total Level is " + getTotalLevel() + ".";
      } else if ("cmb".equals(command)) {
        // this command breaks character limits and might be bannable... would not recommend sending
        // this command over PM to rs2/rs3
        return "@whi@My Combat is Level "
            + "@gre@"
            +
            // melee stats
            ((base_level[SKILL_ATTACK]
                    + base_level[SKILL_STRENGTH]
                    + base_level[SKILL_DEFENSE]
                    + base_level[SKILL_HP])
                * 0.25)
            + " @lre@A:@whi@ "
            + base_level[SKILL_ATTACK]
            + " @lre@S:@whi@ "
            + base_level[SKILL_STRENGTH]
            + " @lre@D:@whi@ "
            + base_level[SKILL_DEFENSE]
            + " @lre@H:@whi@ "
            + base_level[SKILL_HP]
            + " @lre@R:@whi@ "
            + base_level[SKILL_RANGED]
            + " @lre@PG:@whi@ "
            + base_level[SKILL_PRAYGOOD]
            + " @lre@PE:@whi@ "
            + base_level[SKILL_PRAYEVIL]
            + " @lre@GM:@whi@ "
            + base_level[SKILL_GOODMAGIC]
            + " @lre@EM:@whi@ "
            + base_level[SKILL_EVILMAGIC];
      } else if ("cmbnocolor"
          .equals(command)) { // this command stays within character limits and is safe.
        return "My Combat is Level "
            // basic melee stats
            + ((base_level[SKILL_ATTACK]
                    + base_level[SKILL_STRENGTH]
                    + base_level[SKILL_DEFENSE]
                    + base_level[SKILL_HP])
                * 0.25)
            + " A:"
            + base_level[SKILL_ATTACK]
            + " S:"
            + base_level[SKILL_STRENGTH]
            + " D:"
            + base_level[SKILL_DEFENSE]
            + " H:"
            + base_level[SKILL_HP]
            + " R:"
            + base_level[SKILL_RANGED]
            + " PG:"
            + base_level[SKILL_PRAYGOOD]
            + " PE:"
            + base_level[SKILL_PRAYEVIL]
            + " GM:"
            + base_level[SKILL_GOODMAGIC]
            + " EM:"
            + base_level[SKILL_EVILMAGIC];
      } else if ("bank".equals(command)) {
        return "Hey, everyone, I just tried to do something very silly!";
      } else if ("update".equals(command)) {
        checkForUpdate(true);
      } else if (command.startsWith("xmas ")) {
        int randomStart = (int) System.currentTimeMillis();
        if (randomStart < 0) {
          randomStart *= -1; // casting to long to int sometimes results in a negative number
        }
        String subline = "";
        String[] colours = {"@red@", "@whi@", "@gre@", "@whi@"};
        int spaceCounter = 0;
        for (int i = 0; i < line.length() - 7; i++) {
          if (" ".equals(line.substring(7 + i, 8 + i))) {
            spaceCounter += 1;
          }
          subline += colours[(i - spaceCounter + randomStart) % 4];
          subline += line.substring(7 + i, 8 + i);
        }
        return subline;
      } else if (command.startsWith("rainbow ")) { // @red@A@ora@B@yel@C etc
        int randomStart = (int) System.currentTimeMillis();
        if (randomStart < 0) {
          randomStart *= -1; // casting to long to int sometimes results in a negative number
        }
        String subline = "";
        String[] colours = {"@red@", "@ora@", "@yel@", "@gre@", "@cya@", "@mag@"};
        int spaceCounter = 0;
        for (int i = 0; i < line.length() - 10; i++) {
          if (" ".equals(line.substring(10 + i, 11 + i))) {
            spaceCounter += 1;
          }
          subline += colours[(i - spaceCounter + randomStart) % 6];
          subline += line.substring(10 + i, 11 + i);
        }
        return subline;
      }

      for (int skill = 0; skill < NUM_SKILLS; skill++) {
        if (command.equalsIgnoreCase(skill_name[skill]))
          return "My " + skill_name[skill] + " level is " + base_level[skill];
      }
    }

    return line;
  }

  /**
   * Prints a client-side message in chat.
   *
   * @param message a message to print
   * @param chat_type the type of message to send
   */
  public static synchronized void displayMessage(String message, int chat_type) {
    int messageType = MESSAGE_GAME;
    switch (chat_type) {
      case CHAT_QUEST:
        message = "@que@" + message;
        messageType = MESSAGE_QUEST;
        break;
      case CHAT_CHAT:
        messageType = MESSAGE_CHAT;
        break;
      case CHAT_PRIVATE:
      case CHAT_PRIVATE_OUTGOING:
        messageType = MESSAGE_PRIVATE;
        break;
    }
    if (Client.state != Client.STATE_GAME || Reflection.displayMessage == null) return;

    try {
      Reflection.displayMessage.invoke(Client.instance, message, messageType);
    } catch (Exception e) {
    }
  }

  /**
   * Sets the client text response status. In the login screen this is the information shown above
   * the login controls In the register and recover screens is the replacement of control text in
   * the respective panels
   *
   * @param line1 the bottom line of text
   * @param line2 the top part of text
   */
  public static void setResponseMessage(String line1, String line2) {
    if (Reflection.setResponseMessage == null) return;

    try {
      Reflection.setResponseMessage.invoke(Client.instance, line1, line2);
    } catch (Exception e) {
    }
  }

  public static void setInterlace(boolean value) {
    if (Reflection.interlace == null) return;

    try {
      Reflection.interlace.set(Renderer.instance, value);
    } catch (Exception e) {
    }
  }

  public static boolean getInterlace() {
    try {
      return (boolean) Reflection.interlace.get(Renderer.instance);
    } catch (Exception e) {
    }
    return false;
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
    // if (Replay.isPlaying || Replay.isSeeking || Replay.isRestarting) return;

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

  public static void printAndShowActionString(String actionString) {
    if (Settings.SHOW_LAST_MENU_ACTION.get(Settings.currentProfile)) {
      menu_timer = System.currentTimeMillis() + 3500L;
      lastAction = actionString;
      Logger.Info(actionString);
    }
  }

  public static void drawNPC(
      int x,
      int y,
      int width,
      int height,
      String name,
      int currentHits,
      int maxHits,
      int id,
      int id2) {
    // ILOAD 5 is index
    npc_list.add(
        new NPC(
            x,
            y + Renderer.GAME_RENDER_OFFSET,
            width,
            height,
            name,
            NPC.TYPE_MOB,
            currentHits,
            maxHits,
            id,
            id2));
  }

  public static void drawPlayer(
      int x, int y, int width, int height, String name, int currentHits, int maxHits, int id2) {
    npc_list.add(
        new NPC(
            x,
            y + Renderer.GAME_RENDER_OFFSET,
            width,
            height,
            name,
            NPC.TYPE_PLAYER,
            currentHits,
            maxHits,
            0,
            id2));
  }

  public static void drawItem(int x, int y, int width, int height, String name, int id) {
    item_list.add(new Item(x, y + Renderer.GAME_RENDER_OFFSET, width, height, name, id));
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
   * Returns the user's current level in a specified skill. This number is affected by skills boosts
   * and debuffs.
   *
   * @param skill an integer corresponding to a skill
   * @return the user's current level in the specified skill
   * @see #getBaseLevel(int)
   */
  public static int getCurrentLevel(int skill) {
    return current_level[skill];
  }

  /**
   * Returns the sum of the user's base skill levels.
   *
   * @return the user's total level
   */
  public static int getTotalLevel() {
    int total = 0;
    for (int skill = 0; skill < NUM_SKILLS; skill++) total += Client.base_level[skill];
    return total;
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

  public static void resetXPDrops(boolean resetSession) {
    if (player_name.equals("")) {
      return;
    }

    xpUsername = Util.formatString(player_name, 50);
    if (lvlGoals.get(xpUsername) == null) {
      lvlGoals.put(xpUsername, new Float[NUM_SKILLS]);
    }
  }

  /**
   * Checks if a specified player is on the user's friend list.
   *
   * @param name the player's display name
   * @return if the player is the user's friend
   */
  public static boolean isFriend(String name) {
    for (int i = 0; i < friends_count; i++) {
      if (friends_hash[i] != 0 && Util.hash2username(friends_hash[i]).equals(name)) return true;
    }

    return false;
  }

  /**
   * Returns if the user is currently in combat. Recently being in combat does not count as in
   * combat.
   *
   * @return if the user is in combat
   */
  public static boolean isInCombat() {
    return combat_timer == 499;
  }

  public static boolean isInCombatWithNPC(NPC npc) {
    if (npc == null) {
      return false;
    }

    int bottom_posY_npc = npc.y + npc.height;
    int bottom_posY_player = player_posY + player_height;

    // NPC's in combat with the player are always on the same bottom y coord, however
    // when moving the screen around they can be slightly off for a moment. To prevent
    // flickering, just give them a very small buffer of difference.
    boolean inCombatCandidate = (Math.abs(bottom_posY_npc - bottom_posY_player) < 5);

    // Hitboxes will intersect on the X axis from what I've tested, giving this a small
    // buffer as well just in case there are edge cases with very small monsters that
    // don't follow this pattern exactly.
    boolean hitboxesIntersectOnXAxis = (player_posX - 10) < (npc.x + npc.width);

    // The NPC you're fighting is always on the left side of the player.
    boolean isOnLeftOfPlayer = (player_posX + player_width) > npc.x;

    // TODO: verify why other conditions return false
    return isInCombat()
        && npc.currentHits != 0
        && npc.maxHits != 0
        && !player_name.equalsIgnoreCase(npc.name);

    /*return isInCombat()
    && npc.currentHits != 0
    && npc.maxHits != 0
    && !player_name.equals(npc.name)
    && inCombatCandidate
    && isOnLeftOfPlayer
    && hitboxesIntersectOnXAxis;*/
  }

  /**
   * Returns if an in-game interface, window, menu, etc. is currently displayed.
   *
   * @return if an interface is showing
   */
  public static boolean isInterfaceOpen() {
    return show_shop || show_trade || show_friends != 0 || show_changepk == 1 || show_visitad;
  }

  public static void displayMotivationalQuote() {
    // TODO: more motivational quotes
    int colorIdx = ((int) (Math.random() * ((Client.colorDict.length / 2) - 1)) * 2);
    String color = colorDict[colorIdx].substring(4);
    // A coward dies a thousand deaths, but the valiant tastes death but once.
    displayMessage(color + "You are beautiful today, " + player_name + ".", CHAT_QUEST);
  }
}
