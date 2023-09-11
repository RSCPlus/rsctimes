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
package Client;

import Client.Settings.Dir;
import Game.Client;
import Game.Game;
import Game.KeyboardHandler;
import Game.Renderer;
import Game.XPBar;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Properties;

public class Settings {
  // Internally used variables
  public static boolean fovUpdateRequired;
  public static boolean renderingScalarUpdateRequired;
  public static boolean versionCheckRequired = true;
  public static int javaVersion = 0;
  public static final double VERSION_NUMBER = 20230907.163309;
  public static boolean successfullyInitted = false;
  /**
   * A time stamp corresponding to the current version of this source code. Used as a sophisticated
   * versioning system.
   *
   * <p>This variable follows ISO 8601 yyyyMMdd.HHmmss format. The version number will actually be
   * read from this source file, so please don't change the name of this variable and keep the
   * assignment near the top for scanning.
   *
   * <p>This variable can be set automatically by ant by issuing `ant setversion` before you push
   * your changes, so there's no need to update it manually.
   */
  //// General
  public static HashMap<String, Boolean> CUSTOM_CLIENT_SIZE = new HashMap<String, Boolean>();

  public static HashMap<String, Integer> CUSTOM_CLIENT_SIZE_X = new HashMap<String, Integer>();
  public static HashMap<String, Integer> CUSTOM_CLIENT_SIZE_Y = new HashMap<String, Integer>();
  public static HashMap<String, Boolean> SCALED_CLIENT_WINDOW = new HashMap<String, Boolean>();
  public static HashMap<String, Integer> SCALING_ALGORITHM = new HashMap<String, Integer>();
  public static HashMap<String, Integer> INTEGER_SCALING_FACTOR = new HashMap<String, Integer>();
  public static HashMap<String, Float> BILINEAR_SCALING_FACTOR = new HashMap<String, Float>();
  public static HashMap<String, Float> BICUBIC_SCALING_FACTOR = new HashMap<String, Float>();
  public static HashMap<String, Boolean> CHECK_UPDATES = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> REMIND_HOW_TO_OPEN_SETTINGS =
      new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> COMBAT_MENU_SHOWN = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> COMBAT_MENU_HIDDEN = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> CENTER_XPDROPS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> ATTACK_ALWAYS_LEFT_CLICK = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SORT_FRIENDS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> HIDE_ROOFS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> DISABLE_UNDERGROUND_LIGHTING =
      new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> CAMERA_ZOOMABLE = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> CAMERA_ROTATABLE = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> CAMERA_MOVABLE = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> CAMERA_MOVABLE_RELATIVE = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> COLORIZE_CONSOLE_TEXT = new HashMap<String, Boolean>();
  public static HashMap<String, Integer> FOV = new HashMap<String, Integer>();
  public static HashMap<String, Boolean> SOFTWARE_CURSOR = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SHIFT_SCROLL_CAMERA_ROTATION =
      new HashMap<String, Boolean>();
  public static HashMap<String, Integer> TRACKPAD_ROTATION_SENSITIVITY =
      new HashMap<String, Integer>();
  public static HashMap<String, Boolean> FPS_LIMIT_ENABLED = new HashMap<String, Boolean>();
  public static HashMap<String, Integer> FPS_LIMIT = new HashMap<String, Integer>();
  public static HashMap<String, Boolean> AUTO_SCREENSHOT = new HashMap<String, Boolean>();
  public static HashMap<String, Integer> VIEW_DISTANCE = new HashMap<String, Integer>();
  public static HashMap<String, Boolean> PATCH_GENDER = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> PATCH_HBAR_512_LAST_PIXEL = new HashMap<String, Boolean>();
  public static HashMap<String, Integer> LOG_VERBOSITY = new HashMap<String, Integer>();
  public static HashMap<String, Boolean> LOG_SHOW_TIMESTAMPS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> LOG_SHOW_LEVEL = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> LOG_FORCE_TIMESTAMPS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> LOG_FORCE_LEVEL = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> PREFERS_XDG_OPEN = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> INVENTORY_FULL_ALERT = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> USE_DARK_FLATLAF = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> USE_NIMBUS_THEME = new HashMap<String, Boolean>();

  //// Notifications
  public static HashMap<String, Boolean> TRAY_NOTIFS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> TRAY_NOTIFS_ALWAYS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> NOTIFICATION_SOUNDS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SOUND_NOTIFS_ALWAYS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> USE_SYSTEM_NOTIFICATIONS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> PM_NOTIFICATIONS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> TRADE_NOTIFICATIONS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> UNDER_ATTACK_NOTIFICATIONS =
      new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> LOW_HP_NOTIFICATIONS = new HashMap<String, Boolean>();
  public static HashMap<String, Integer> LOW_HP_NOTIF_VALUE = new HashMap<String, Integer>();
  public static HashMap<String, Boolean> HIGHLIGHTED_ITEM_NOTIFICATIONS =
      new HashMap<String, Boolean>();
  public static HashMap<String, Integer> HIGHLIGHTED_ITEM_NOTIF_VALUE =
      new HashMap<String, Integer>();

  public static HashMap<String, Boolean> SHOW_XP_BAR = new HashMap<String, Boolean>();

  //// overlays
  public static HashMap<String, Boolean> SHOW_HP_OVERLAY = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SHOW_MOUSE_TOOLTIP = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SHOW_EXTENDED_TOOLTIP = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SHOW_BUFFS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SHOW_LAST_MENU_ACTION = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SHOW_INVCOUNT = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SHOW_RSCTIMES_BUTTONS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> RSCTIMES_BUTTONS_FUNCTIONAL =
      new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> WIKI_LOOKUP_ON_MAGIC_BOOK = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> MOTIVATIONAL_QUOTES_BUTTON =
      new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> TOGGLE_XP_BAR_ON_STATS_BUTTON =
      new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> WIKI_LOOKUP_ON_HBAR = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SHOW_ITEM_GROUND_OVERLAY = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SHOW_PLAYER_NAME_OVERLAY = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SHOW_FRIEND_NAME_OVERLAY = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SHOW_NPC_NAME_OVERLAY = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> EXTEND_IDS_OVERLAY = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> TRACE_OBJECT_INFO = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SHOW_COMBAT_INFO = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SHOW_PLAYER_POSITION = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> HIDE_FPS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> NPC_HEALTH_SHOW_PERCENTAGE =
      new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SHOW_HITBOX = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> LAG_INDICATOR = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SHOW_FOOD_HEAL_OVERLAY = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SHOW_TIME_UNTIL_HP_REGEN = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> DEBUG = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> EXCEPTION_HANDLER = new HashMap<String, Boolean>();
  public static HashMap<String, ArrayList<String>> HIGHLIGHTED_ITEMS =
      new HashMap<String, ArrayList<String>>();
  public static HashMap<String, ArrayList<String>> BLOCKED_ITEMS =
      new HashMap<String, ArrayList<String>>();

  //// streaming
  public static HashMap<String, Boolean> TWITCH_CHAT_ENABLED = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> TWITCH_HIDE_CHAT = new HashMap<String, Boolean>();
  public static HashMap<String, String> TWITCH_CHANNEL = new HashMap<String, String>();
  public static HashMap<String, String> TWITCH_OAUTH = new HashMap<String, String>();
  public static HashMap<String, String> TWITCH_USERNAME = new HashMap<String, String>();
  public static HashMap<String, Boolean> SAVE_LOGININFO = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> START_LOGINSCREEN = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SPEEDRUNNER_MODE_ACTIVE = new HashMap<String, Boolean>();

  //// world list
  public static HashMap<Integer, String> WORLD_URLS = new HashMap<Integer, String>();
  public static HashMap<Integer, String> WORLD_NAMES = new HashMap<Integer, String>();
  public static HashMap<Integer, Integer> WORLD_PORTS = new HashMap<Integer, Integer>();
  public static HashMap<Integer, String> WORLD_FILE_PATHS = new HashMap<Integer, String>();
  public static int WORLDS_TO_DISPLAY = 5;
  public static boolean noWorldsConfigured = true;

  //// nogui
  public static HashMap<String, Integer> COMBAT_STYLE = new HashMap<String, Integer>();
  public static HashMap<String, Integer> WORLD = new HashMap<String, Integer>();
  public static HashMap<String, Boolean> FIRST_TIME = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> UPDATE_CONFIRMATION = new HashMap<String, Boolean>();

  // these are variables that are injected with JClassPatcher
  public static int COMBAT_STYLE_INT = Client.COMBAT_AGGRESSIVE;
  public static boolean HIDE_ROOFS_BOOL = false;
  public static boolean DISABLE_UNDERGROUND_LIGHTING_BOOL = false;
  public static boolean COMBAT_MENU_SHOWN_BOOL = false;
  public static boolean COMBAT_MENU_HIDDEN_BOOL = false;
  public static boolean CAMERA_ZOOMABLE_BOOL = false;
  public static boolean CAMERA_ROTATABLE_BOOL = false;
  public static boolean CAMERA_MOVABLE_BOOL = false;
  public static boolean VIEW_DISTANCE_BOOL = false;
  public static boolean FOV_BOOL = false;

  // determines which preset to load, or your custom settings :-)
  public static String currentProfile = "custom";

  public static ArrayList<String> presetTable = new ArrayList<String>();

  private Settings() {
    // Empty private constructor to prevent instantiation.
  }

  /** Loads properties from config.ini for use with definePresets */
  public static Properties initSettings() {
    // Load settings
    try {
      String versionText = System.getProperty("java.version");
      if (versionText.startsWith("1.")) {
        versionText = versionText.substring(2);
      }
      javaVersion = Integer.parseInt(versionText.substring(0, versionText.indexOf(".")));
    } catch (Exception e) {
      javaVersion = -1;
    }

    try {
      Properties props = loadProps();

      currentProfile = getPropString(props, "current_profile", "custom");
      definePresets(props);

      // World Map
      WorldMapWindow.showIcons = getPropBoolean(props, "worldmap_show_icons", true);
      WorldMapWindow.showLabels = getPropBoolean(props, "worldmap_show_labels", true);
      WorldMapWindow.showScenery = getPropBoolean(props, "worldmap_show_scenery", true);
      WorldMapWindow.renderChunkGrid = getPropBoolean(props, "worldmap_show_chunk_grid", false);
      WorldMapWindow.showOtherFloors = getPropBoolean(props, "worldmap_show_other_floors", false);

      updateInjectedVariables(); // TODO remove this function

      // Keybinds
      if (KeyboardHandler.keybindSetList.size() == 0) {
        Logger.Debug("No keybinds defined yet, config window not initialized");
      } else {
        loadKeybinds(props);
      }

      // XP
      int numberOfGoalers = getPropInt(props, "numberOfGoalers", 0);
      String[] goalerUsernames = new String[numberOfGoalers];
      for (int usernameID = 0; usernameID < numberOfGoalers; usernameID++) {
        goalerUsernames[usernameID] = getPropString(props, "username" + usernameID, "");
        Client.lvlGoals.put(goalerUsernames[usernameID], new Float[Client.NUM_SKILLS]);
        for (int skill = 0; skill < Client.NUM_SKILLS; skill++) {
          try {
            Client.lvlGoals.get(goalerUsernames[usernameID])[skill] =
                Float.parseFloat(
                    getPropString(props, String.format("lvlGoal%02d%03d", skill, usernameID), "0"));
          } catch (Exception e1) {
            Client.lvlGoals.get(goalerUsernames[usernameID])[skill] = new Float(0);
            Logger.Warn(
                "Couldn't parse settings key "
                    + String.format("lvlGoal%02d%03d", skill, usernameID));
          }
        }
      }
      XPBar.pinnedBar = getPropBoolean(props, "pinXPBar", false);
      XPBar.pinnedSkill = getPropInt(props, "pinnedSkill", -1);

      Logger.Info("Loaded settings");
      return props;

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void definePresets(Properties props) {
    if (presetTable.size() == 0) {
      presetTable.add("all"); // 0
      presetTable.add("heavy"); // 1
      presetTable.add("default"); // 2
      presetTable.add("lite"); // 3
      presetTable.add("vanilla_resizable"); // 4
      presetTable.add("vanilla"); // 5
    }

    //// general

    CUSTOM_CLIENT_SIZE.put("vanilla", true);
    CUSTOM_CLIENT_SIZE.put("vanilla_resizable", false);
    CUSTOM_CLIENT_SIZE.put("lite", false);
    CUSTOM_CLIENT_SIZE.put("default", false);
    CUSTOM_CLIENT_SIZE.put("heavy", false);
    CUSTOM_CLIENT_SIZE.put("all", false);
    CUSTOM_CLIENT_SIZE.put(
        "custom", getPropBoolean(props, "custom_client_size", CUSTOM_CLIENT_SIZE.get("default")));

    CUSTOM_CLIENT_SIZE_X.put("vanilla", 512);
    CUSTOM_CLIENT_SIZE_X.put("vanilla_resizable", 1024);
    CUSTOM_CLIENT_SIZE_X.put("lite", 1024);
    CUSTOM_CLIENT_SIZE_X.put("default", 1024);
    CUSTOM_CLIENT_SIZE_X.put("heavy", 1024);
    CUSTOM_CLIENT_SIZE_X.put("all", 1024);
    CUSTOM_CLIENT_SIZE_X.put(
        "custom", getPropInt(props, "custom_client_size_x", CUSTOM_CLIENT_SIZE_X.get("default")));

    CUSTOM_CLIENT_SIZE_Y.put("vanilla", 357);
    CUSTOM_CLIENT_SIZE_Y.put("vanilla_resizable", 714);
    CUSTOM_CLIENT_SIZE_Y.put("lite", 714);
    CUSTOM_CLIENT_SIZE_Y.put("default", 714);
    CUSTOM_CLIENT_SIZE_Y.put("heavy", 714);
    CUSTOM_CLIENT_SIZE_Y.put("all", 714);
    CUSTOM_CLIENT_SIZE_Y.put(
        "custom", getPropInt(props, "custom_client_size_y", CUSTOM_CLIENT_SIZE_Y.get("default")));

    SCALED_CLIENT_WINDOW.put("vanilla", false);
    SCALED_CLIENT_WINDOW.put("vanilla_resizable", true);
    SCALED_CLIENT_WINDOW.put("lite", true);
    SCALED_CLIENT_WINDOW.put("default", true);
    SCALED_CLIENT_WINDOW.put("heavy", true);
    SCALED_CLIENT_WINDOW.put("all", true);
    SCALED_CLIENT_WINDOW.put(
        "custom",
        getPropBoolean(props, "enable_window_scaling", SCALED_CLIENT_WINDOW.get("default")));

    SCALING_ALGORITHM.put("vanilla", AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    SCALING_ALGORITHM.put("vanilla_resizable", AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    SCALING_ALGORITHM.put("lite", AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    SCALING_ALGORITHM.put("default", AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    SCALING_ALGORITHM.put("heavy", AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    SCALING_ALGORITHM.put("all", AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    SCALING_ALGORITHM.put(
        "custom", getPropInt(props, "scaling_algorithm", SCALING_ALGORITHM.get("default")));

    INTEGER_SCALING_FACTOR.put("vanilla", 2);
    INTEGER_SCALING_FACTOR.put("vanilla_resizable", 2);
    INTEGER_SCALING_FACTOR.put("lite", 2);
    INTEGER_SCALING_FACTOR.put("default", 2);
    INTEGER_SCALING_FACTOR.put("heavy", 2);
    INTEGER_SCALING_FACTOR.put("all", 2);
    INTEGER_SCALING_FACTOR.put(
        "custom",
        getPropInt(props, "integer_scaling_factor", INTEGER_SCALING_FACTOR.get("default")));

    BILINEAR_SCALING_FACTOR.put("vanilla", 1.5f);
    BILINEAR_SCALING_FACTOR.put("vanilla_resizable", 1.5f);
    BILINEAR_SCALING_FACTOR.put("lite", 1.5f);
    BILINEAR_SCALING_FACTOR.put("default", 1.5f);
    BILINEAR_SCALING_FACTOR.put("heavy", 1.5f);
    BILINEAR_SCALING_FACTOR.put("all", 1.5f);
    BILINEAR_SCALING_FACTOR.put(
        "custom",
        getPropFloat(props, "bilinear_scaling_factor", BILINEAR_SCALING_FACTOR.get("default")));

    BICUBIC_SCALING_FACTOR.put("vanilla", 1.5f);
    BICUBIC_SCALING_FACTOR.put("vanilla_resizable", 1.5f);
    BICUBIC_SCALING_FACTOR.put("lite", 1.5f);
    BICUBIC_SCALING_FACTOR.put("default", 1.5f);
    BICUBIC_SCALING_FACTOR.put("heavy", 1.5f);
    BICUBIC_SCALING_FACTOR.put("all", 1.5f);
    BICUBIC_SCALING_FACTOR.put(
        "custom",
        getPropFloat(props, "bicubic_scaling_factor", BICUBIC_SCALING_FACTOR.get("default")));

    CHECK_UPDATES.put("vanilla", true);
    CHECK_UPDATES.put("vanilla_resizable", true);
    CHECK_UPDATES.put("lite", true);
    CHECK_UPDATES.put("default", true);
    CHECK_UPDATES.put("heavy", true);
    CHECK_UPDATES.put("all", true);
    CHECK_UPDATES.put(
        "custom", getPropBoolean(props, "check_updates", CHECK_UPDATES.get("default")));

    REMIND_HOW_TO_OPEN_SETTINGS.put("vanilla", false);
    REMIND_HOW_TO_OPEN_SETTINGS.put("vanilla_resizable", false);
    REMIND_HOW_TO_OPEN_SETTINGS.put("lite", false);
    REMIND_HOW_TO_OPEN_SETTINGS.put("default", true);
    REMIND_HOW_TO_OPEN_SETTINGS.put("heavy", true);
    REMIND_HOW_TO_OPEN_SETTINGS.put("all", true);
    REMIND_HOW_TO_OPEN_SETTINGS.put(
        "custom",
        getPropBoolean(props, "welcome_enabled", REMIND_HOW_TO_OPEN_SETTINGS.get("default")));

    COMBAT_MENU_SHOWN.put("vanilla", false);
    COMBAT_MENU_SHOWN.put("vanilla_resizable", false);
    COMBAT_MENU_SHOWN.put("lite", false);
    COMBAT_MENU_SHOWN.put("default", false);
    COMBAT_MENU_SHOWN.put("heavy", true);
    COMBAT_MENU_SHOWN.put("all", true);
    COMBAT_MENU_SHOWN.put(
        "custom", getPropBoolean(props, "combat_menu", COMBAT_MENU_SHOWN.get("default")));

    COMBAT_MENU_HIDDEN.put("vanilla", false);
    COMBAT_MENU_HIDDEN.put("vanilla_resizable", false);
    COMBAT_MENU_HIDDEN.put("lite", false);
    COMBAT_MENU_HIDDEN.put("default", false);
    COMBAT_MENU_HIDDEN.put("heavy", false);
    COMBAT_MENU_HIDDEN.put("all", true);
    COMBAT_MENU_HIDDEN.put(
        "custom", getPropBoolean(props, "combat_menu_hidden", COMBAT_MENU_HIDDEN.get("default")));

    CENTER_XPDROPS.put("vanilla", false);
    CENTER_XPDROPS.put("vanilla_resizable", false);
    CENTER_XPDROPS.put("lite", false);
    CENTER_XPDROPS.put("default", false);
    CENTER_XPDROPS.put("heavy", true);
    CENTER_XPDROPS.put("all", true);
    CENTER_XPDROPS.put(
        "custom", getPropBoolean(props, "center_xpdrops", CENTER_XPDROPS.get("default")));

    ATTACK_ALWAYS_LEFT_CLICK.put("vanilla", false);
    ATTACK_ALWAYS_LEFT_CLICK.put("vanilla_resizable", false);
    ATTACK_ALWAYS_LEFT_CLICK.put("lite", false);
    ATTACK_ALWAYS_LEFT_CLICK.put("default", false);
    ATTACK_ALWAYS_LEFT_CLICK.put("heavy", true);
    ATTACK_ALWAYS_LEFT_CLICK.put("all", true);
    ATTACK_ALWAYS_LEFT_CLICK.put(
        "custom", getPropBoolean(props, "bypass_attack", ATTACK_ALWAYS_LEFT_CLICK.get("default")));

    SORT_FRIENDS.put("vanilla", false);
    SORT_FRIENDS.put("vanilla_resizable", false);
    SORT_FRIENDS.put("lite", true);
    SORT_FRIENDS.put("default", true);
    SORT_FRIENDS.put("heavy", true);
    SORT_FRIENDS.put("all", true);
    SORT_FRIENDS.put("custom", getPropBoolean(props, "sort_friends", SORT_FRIENDS.get("default")));

    HIDE_ROOFS.put("vanilla", false);
    HIDE_ROOFS.put("vanilla_resizable", false);
    HIDE_ROOFS.put("lite", false);
    HIDE_ROOFS.put("default", false);
    HIDE_ROOFS.put("heavy", true);
    HIDE_ROOFS.put("all", true);
    HIDE_ROOFS.put("custom", getPropBoolean(props, "hide_roofs", HIDE_ROOFS.get("default")));

    DISABLE_UNDERGROUND_LIGHTING.put("vanilla", false);
    DISABLE_UNDERGROUND_LIGHTING.put("vanilla_resizable", false);
    DISABLE_UNDERGROUND_LIGHTING.put("lite", false);
    DISABLE_UNDERGROUND_LIGHTING.put("default", false);
    DISABLE_UNDERGROUND_LIGHTING.put("heavy", true);
    DISABLE_UNDERGROUND_LIGHTING.put("all", true);
    DISABLE_UNDERGROUND_LIGHTING.put(
        "custom",
        getPropBoolean(
            props, "disable_underground_lighting", DISABLE_UNDERGROUND_LIGHTING.get("default")));

    CAMERA_ZOOMABLE.put("vanilla", false);
    CAMERA_ZOOMABLE.put("vanilla_resizable", false);
    CAMERA_ZOOMABLE.put("lite", true);
    CAMERA_ZOOMABLE.put("default", true);
    CAMERA_ZOOMABLE.put("heavy", true);
    CAMERA_ZOOMABLE.put("all", true);
    CAMERA_ZOOMABLE.put(
        "custom", getPropBoolean(props, "camera_zoomable", CAMERA_ZOOMABLE.get("default")));

    CAMERA_ROTATABLE.put("vanilla", false);
    CAMERA_ROTATABLE.put("vanilla_resizable", false);
    CAMERA_ROTATABLE.put("lite", true);
    CAMERA_ROTATABLE.put("default", true);
    CAMERA_ROTATABLE.put("heavy", true);
    CAMERA_ROTATABLE.put("all", true);
    CAMERA_ROTATABLE.put(
        "custom", getPropBoolean(props, "camera_rotatable", CAMERA_ROTATABLE.get("default")));

    CAMERA_MOVABLE.put("vanilla", false);
    CAMERA_MOVABLE.put("vanilla_resizable", false);
    CAMERA_MOVABLE.put("lite", true);
    CAMERA_MOVABLE.put("default", true);
    CAMERA_MOVABLE.put("heavy", true);
    CAMERA_MOVABLE.put("all", true);
    CAMERA_MOVABLE.put(
        "custom", getPropBoolean(props, "camera_movable", CAMERA_MOVABLE.get("default")));

    CAMERA_MOVABLE_RELATIVE.put("vanilla", false);
    CAMERA_MOVABLE_RELATIVE.put("vanilla_resizable", false);
    CAMERA_MOVABLE_RELATIVE.put("lite", false);
    CAMERA_MOVABLE_RELATIVE.put("default", false);
    CAMERA_MOVABLE_RELATIVE.put("heavy", false);
    CAMERA_MOVABLE_RELATIVE.put("all", false);
    CAMERA_MOVABLE_RELATIVE.put(
        "custom",
        getPropBoolean(props, "camera_movable_relative", CAMERA_MOVABLE_RELATIVE.get("default")));

    COLORIZE_CONSOLE_TEXT.put("vanilla", true);
    COLORIZE_CONSOLE_TEXT.put("vanilla_resizable", true);
    COLORIZE_CONSOLE_TEXT.put("lite", true);
    COLORIZE_CONSOLE_TEXT.put("default", true);
    COLORIZE_CONSOLE_TEXT.put("heavy", true);
    COLORIZE_CONSOLE_TEXT.put("all", true);
    COLORIZE_CONSOLE_TEXT.put(
        "custom", getPropBoolean(props, "colorize", COLORIZE_CONSOLE_TEXT.get("default")));

    FOV.put("vanilla", 9);
    FOV.put("vanilla_resizable", 9);
    FOV.put("lite", 9);
    FOV.put("default", 9);
    FOV.put("heavy", 9);
    FOV.put("all", 9);
    FOV.put("custom", getPropInt(props, "fov", FOV.get("default")));

    FPS_LIMIT_ENABLED.put("vanilla", false);
    FPS_LIMIT_ENABLED.put("vanilla_resizable", false);
    FPS_LIMIT_ENABLED.put("lite", false);
    FPS_LIMIT_ENABLED.put("default", false);
    FPS_LIMIT_ENABLED.put("heavy", false);
    FPS_LIMIT_ENABLED.put("all", true);
    FPS_LIMIT_ENABLED.put(
        "custom", getPropBoolean(props, "fps_limit_enabled", FPS_LIMIT_ENABLED.get("default")));

    FPS_LIMIT.put("vanilla", 10);
    FPS_LIMIT.put("vanilla_resizable", 10);
    FPS_LIMIT.put("lite", 10);
    FPS_LIMIT.put("default", 10);
    FPS_LIMIT.put("heavy", 10);
    FPS_LIMIT.put("all", 10);
    FPS_LIMIT.put("custom", getPropInt(props, "fps_limit", FPS_LIMIT.get("default")));

    SOFTWARE_CURSOR.put("vanilla", false);
    SOFTWARE_CURSOR.put("vanilla_resizable", false);
    SOFTWARE_CURSOR.put("lite", false);
    SOFTWARE_CURSOR.put("default", false);
    SOFTWARE_CURSOR.put("heavy", false);
    SOFTWARE_CURSOR.put("all", true);
    SOFTWARE_CURSOR.put(
        "custom", getPropBoolean(props, "software_cursor", SOFTWARE_CURSOR.get("default")));

    SHIFT_SCROLL_CAMERA_ROTATION.put("vanilla", false);
    SHIFT_SCROLL_CAMERA_ROTATION.put("vanilla_resizable", false);
    SHIFT_SCROLL_CAMERA_ROTATION.put("lite", false);
    SHIFT_SCROLL_CAMERA_ROTATION.put("default", true);
    SHIFT_SCROLL_CAMERA_ROTATION.put("heavy", true);
    SHIFT_SCROLL_CAMERA_ROTATION.put("all", true);
    SHIFT_SCROLL_CAMERA_ROTATION.put(
        "custom",
        getPropBoolean(
            props, "shift_scroll_camera_rotation", SHIFT_SCROLL_CAMERA_ROTATION.get("default")));

    TRACKPAD_ROTATION_SENSITIVITY.put("vanilla", 8);
    TRACKPAD_ROTATION_SENSITIVITY.put("vanilla_resizable", 8);
    TRACKPAD_ROTATION_SENSITIVITY.put("lite", 8);
    TRACKPAD_ROTATION_SENSITIVITY.put("default", 8);
    TRACKPAD_ROTATION_SENSITIVITY.put("heavy", 8);
    TRACKPAD_ROTATION_SENSITIVITY.put("all", 8);
    TRACKPAD_ROTATION_SENSITIVITY.put(
        "custom",
        getPropInt(
            props, "trackpad_rotation_sensitivity", TRACKPAD_ROTATION_SENSITIVITY.get("default")));

    VIEW_DISTANCE.put("vanilla", 2300);
    VIEW_DISTANCE.put("vanilla_resizable", 3000);
    VIEW_DISTANCE.put("lite", 10000);
    VIEW_DISTANCE.put("default", 10000);
    VIEW_DISTANCE.put("heavy", 20000);
    VIEW_DISTANCE.put("all", 20000);
    VIEW_DISTANCE.put("custom", getPropInt(props, "view_distance", VIEW_DISTANCE.get("default")));

    AUTO_SCREENSHOT.put("vanilla", false);
    AUTO_SCREENSHOT.put("vanilla_resizable", false);
    AUTO_SCREENSHOT.put("lite", false);
    AUTO_SCREENSHOT.put("default", true);
    AUTO_SCREENSHOT.put("heavy", true);
    AUTO_SCREENSHOT.put("all", true);
    AUTO_SCREENSHOT.put(
        "custom", getPropBoolean(props, "auto_screenshot", AUTO_SCREENSHOT.get("default")));

    PATCH_GENDER.put("vanilla", false);
    PATCH_GENDER.put("vanilla_resizable", false);
    PATCH_GENDER.put("lite", false);
    PATCH_GENDER.put("default", true);
    PATCH_GENDER.put("heavy", true);
    PATCH_GENDER.put("all", true);
    PATCH_GENDER.put("custom", getPropBoolean(props, "patch_gender", PATCH_GENDER.get("default")));

    PATCH_HBAR_512_LAST_PIXEL.put("vanilla", false);
    PATCH_HBAR_512_LAST_PIXEL.put("vanilla_resizable", false);
    PATCH_HBAR_512_LAST_PIXEL.put("lite", false);
    PATCH_HBAR_512_LAST_PIXEL.put("default", false);
    PATCH_HBAR_512_LAST_PIXEL.put("heavy", true);
    PATCH_HBAR_512_LAST_PIXEL.put("all", true);
    PATCH_HBAR_512_LAST_PIXEL.put(
        "custom",
        getPropBoolean(
            props, "patch_hbar_512_last_pixel", PATCH_HBAR_512_LAST_PIXEL.get("default")));

    LOG_VERBOSITY.put("vanilla", Logger.Type.GAME.id);
    LOG_VERBOSITY.put("vanilla_resizable", Logger.Type.GAME.id);
    LOG_VERBOSITY.put("lite", Logger.Type.WARN.id);
    LOG_VERBOSITY.put("default", Logger.Type.INFO.id);
    LOG_VERBOSITY.put("heavy", Logger.Type.INFO.id);
    LOG_VERBOSITY.put("all", Logger.Type.DEBUG.id);
    LOG_VERBOSITY.put("custom", getPropInt(props, "log_verbosity", LOG_VERBOSITY.get("default")));

    LOG_SHOW_TIMESTAMPS.put("vanilla", true);
    LOG_SHOW_TIMESTAMPS.put("vanilla_resizable", true);
    LOG_SHOW_TIMESTAMPS.put("lite", true);
    LOG_SHOW_TIMESTAMPS.put("default", true);
    LOG_SHOW_TIMESTAMPS.put("heavy", true);
    LOG_SHOW_TIMESTAMPS.put("all", true);
    LOG_SHOW_TIMESTAMPS.put(
        "custom", getPropBoolean(props, "log_show_timestamps", LOG_SHOW_TIMESTAMPS.get("default")));

    LOG_SHOW_LEVEL.put("vanilla", true);
    LOG_SHOW_LEVEL.put("vanilla_resizable", true);
    LOG_SHOW_LEVEL.put("lite", true);
    LOG_SHOW_LEVEL.put("default", true);
    LOG_SHOW_LEVEL.put("heavy", true);
    LOG_SHOW_LEVEL.put("all", true);
    LOG_SHOW_LEVEL.put(
        "custom", getPropBoolean(props, "log_show_level", LOG_SHOW_LEVEL.get("default")));

    LOG_FORCE_TIMESTAMPS.put("vanilla", false);
    LOG_FORCE_TIMESTAMPS.put("vanilla_resizable", false);
    LOG_FORCE_TIMESTAMPS.put("lite", false);
    LOG_FORCE_TIMESTAMPS.put("default", false);
    LOG_FORCE_TIMESTAMPS.put("heavy", false);
    LOG_FORCE_TIMESTAMPS.put("all", true);
    LOG_FORCE_TIMESTAMPS.put(
        "custom",
        getPropBoolean(props, "log_force_timestamps", LOG_FORCE_TIMESTAMPS.get("default")));

    LOG_FORCE_LEVEL.put("vanilla", false);
    LOG_FORCE_LEVEL.put("vanilla_resizable", false);
    LOG_FORCE_LEVEL.put("lite", false);
    LOG_FORCE_LEVEL.put("default", false);
    LOG_FORCE_LEVEL.put("heavy", false);
    LOG_FORCE_LEVEL.put("all", true);
    LOG_FORCE_LEVEL.put(
        "custom", getPropBoolean(props, "log_force_level", LOG_FORCE_LEVEL.get("default")));

    Util.hasXdgOpen = Util.detectBinaryAvailable("xdg-open", "URL opening");
    PREFERS_XDG_OPEN.put("vanilla", false);
    PREFERS_XDG_OPEN.put("vanilla_resizable", false);
    PREFERS_XDG_OPEN.put("lite", false);
    PREFERS_XDG_OPEN.put("default", Util.hasXdgOpen);
    PREFERS_XDG_OPEN.put("heavy", true);
    PREFERS_XDG_OPEN.put("all", true);
    PREFERS_XDG_OPEN.put(
        "custom", getPropBoolean(props, "prefers_xdg_open", PREFERS_XDG_OPEN.get("default")));

    boolean defaultDarkMode = shouldDefaultDarkMode();

    USE_DARK_FLATLAF.put("vanilla", defaultDarkMode);
    USE_DARK_FLATLAF.put("vanilla_resizable", defaultDarkMode);
    USE_DARK_FLATLAF.put("lite", defaultDarkMode);
    USE_DARK_FLATLAF.put("default", defaultDarkMode);
    USE_DARK_FLATLAF.put("heavy", defaultDarkMode);
    USE_DARK_FLATLAF.put("all", defaultDarkMode);
    USE_DARK_FLATLAF.put(
        "custom", getPropBoolean(props, "use_dark_flatlaf", USE_DARK_FLATLAF.get("default")));

    USE_NIMBUS_THEME.put("vanilla", false);
    USE_NIMBUS_THEME.put("vanilla_resizable", false);
    USE_NIMBUS_THEME.put("lite", false);
    USE_NIMBUS_THEME.put("default", false);
    USE_NIMBUS_THEME.put("heavy", false);
    USE_NIMBUS_THEME.put("all", false);
    USE_NIMBUS_THEME.put(
        "custom", getPropBoolean(props, "use_nimbus_theme", USE_NIMBUS_THEME.get("default")));

    INVENTORY_FULL_ALERT.put("vanilla", false);
    INVENTORY_FULL_ALERT.put("vanilla_resizable", false);
    INVENTORY_FULL_ALERT.put("lite", false);
    INVENTORY_FULL_ALERT.put("default", false);
    INVENTORY_FULL_ALERT.put("heavy", false);
    INVENTORY_FULL_ALERT.put("all", true);
    INVENTORY_FULL_ALERT.put(
        "custom",
        getPropBoolean(props, "inventory_full_alert", INVENTORY_FULL_ALERT.get("default")));

    //// notifications
    TRAY_NOTIFS.put("vanilla", false);
    TRAY_NOTIFS.put("vanilla_resizable", false);
    TRAY_NOTIFS.put("lite", true);
    TRAY_NOTIFS.put("default", true);
    TRAY_NOTIFS.put("heavy", true);
    TRAY_NOTIFS.put("all", true);
    TRAY_NOTIFS.put("custom", getPropBoolean(props, "tray_notifs", TRAY_NOTIFS.get("default")));

    TRAY_NOTIFS_ALWAYS.put("vanilla", false);
    TRAY_NOTIFS_ALWAYS.put("vanilla_resizable", false);
    TRAY_NOTIFS_ALWAYS.put("lite", false);
    TRAY_NOTIFS_ALWAYS.put("default", false);
    TRAY_NOTIFS_ALWAYS.put("heavy", false);
    TRAY_NOTIFS_ALWAYS.put("all", true);
    TRAY_NOTIFS_ALWAYS.put(
        "custom", getPropBoolean(props, "tray_notifs_always", TRAY_NOTIFS_ALWAYS.get("default")));

    NOTIFICATION_SOUNDS.put("vanilla", false);
    NOTIFICATION_SOUNDS.put("vanilla_resizable", false);
    NOTIFICATION_SOUNDS.put("lite", !Settings.isRecommendedToUseSystemNotifs());
    NOTIFICATION_SOUNDS.put("default", !Settings.isRecommendedToUseSystemNotifs());
    NOTIFICATION_SOUNDS.put("heavy", !Settings.isRecommendedToUseSystemNotifs());
    NOTIFICATION_SOUNDS.put("all", true);
    NOTIFICATION_SOUNDS.put(
        "custom", getPropBoolean(props, "notification_sounds", NOTIFICATION_SOUNDS.get("default")));

    SOUND_NOTIFS_ALWAYS.put("vanilla", false);
    SOUND_NOTIFS_ALWAYS.put("vanilla_resizable", false);
    SOUND_NOTIFS_ALWAYS.put("lite", false);
    SOUND_NOTIFS_ALWAYS.put("default", false);
    SOUND_NOTIFS_ALWAYS.put("heavy", false);
    SOUND_NOTIFS_ALWAYS.put("all", true);
    SOUND_NOTIFS_ALWAYS.put(
        "custom", getPropBoolean(props, "sound_notifs_always", SOUND_NOTIFS_ALWAYS.get("default")));

    USE_SYSTEM_NOTIFICATIONS.put("vanilla", false);
    USE_SYSTEM_NOTIFICATIONS.put("vanilla_resizable", false);
    USE_SYSTEM_NOTIFICATIONS.put("lite", Settings.isRecommendedToUseSystemNotifs());
    USE_SYSTEM_NOTIFICATIONS.put("default", Settings.isRecommendedToUseSystemNotifs());
    USE_SYSTEM_NOTIFICATIONS.put("heavy", Settings.isRecommendedToUseSystemNotifs());
    USE_SYSTEM_NOTIFICATIONS.put("all", true);
    USE_SYSTEM_NOTIFICATIONS.put(
        "custom",
        getPropBoolean(props, "use_system_notifications", USE_SYSTEM_NOTIFICATIONS.get("default")));

    PM_NOTIFICATIONS.put("vanilla", false);
    PM_NOTIFICATIONS.put("vanilla_resizable", false);
    PM_NOTIFICATIONS.put("lite", false);
    PM_NOTIFICATIONS.put("default", true);
    PM_NOTIFICATIONS.put("heavy", true);
    PM_NOTIFICATIONS.put("all", true);
    PM_NOTIFICATIONS.put(
        "custom", getPropBoolean(props, "pm_notifications", PM_NOTIFICATIONS.get("default")));

    TRADE_NOTIFICATIONS.put("vanilla", false);
    TRADE_NOTIFICATIONS.put("vanilla_resizable", false);
    TRADE_NOTIFICATIONS.put("lite", false);
    TRADE_NOTIFICATIONS.put("default", true);
    TRADE_NOTIFICATIONS.put("heavy", true);
    TRADE_NOTIFICATIONS.put("all", true);
    TRADE_NOTIFICATIONS.put(
        "custom", getPropBoolean(props, "trade_notifications", TRADE_NOTIFICATIONS.get("default")));

    UNDER_ATTACK_NOTIFICATIONS.put("vanilla", false);
    UNDER_ATTACK_NOTIFICATIONS.put("vanilla_resizable", false);
    UNDER_ATTACK_NOTIFICATIONS.put("lite", false);
    UNDER_ATTACK_NOTIFICATIONS.put("default", true);
    UNDER_ATTACK_NOTIFICATIONS.put("heavy", true);
    UNDER_ATTACK_NOTIFICATIONS.put("all", true);
    UNDER_ATTACK_NOTIFICATIONS.put(
        "custom",
        getPropBoolean(
            props, "under_attack_notifications", UNDER_ATTACK_NOTIFICATIONS.get("default")));

    LOW_HP_NOTIFICATIONS.put("vanilla", false);
    LOW_HP_NOTIFICATIONS.put("vanilla_resizable", false);
    LOW_HP_NOTIFICATIONS.put("lite", false);
    LOW_HP_NOTIFICATIONS.put("default", true);
    LOW_HP_NOTIFICATIONS.put("heavy", true);
    LOW_HP_NOTIFICATIONS.put("all", true);
    LOW_HP_NOTIFICATIONS.put(
        "custom",
        getPropBoolean(props, "low_hp_notifications", LOW_HP_NOTIFICATIONS.get("default")));

    LOW_HP_NOTIF_VALUE.put("vanilla", 0);
    LOW_HP_NOTIF_VALUE.put("vanilla_resizable", 0);
    LOW_HP_NOTIF_VALUE.put("lite", 25);
    LOW_HP_NOTIF_VALUE.put("default", 25);
    LOW_HP_NOTIF_VALUE.put("heavy", 25);
    LOW_HP_NOTIF_VALUE.put("all", 30);
    LOW_HP_NOTIF_VALUE.put(
        "custom", getPropInt(props, "low_hp_notif_value", LOW_HP_NOTIF_VALUE.get("default")));

    HIGHLIGHTED_ITEM_NOTIFICATIONS.put("vanilla", false);
    HIGHLIGHTED_ITEM_NOTIFICATIONS.put("vanilla_resizable", false);
    HIGHLIGHTED_ITEM_NOTIFICATIONS.put("lite", false);
    HIGHLIGHTED_ITEM_NOTIFICATIONS.put("default", true);
    HIGHLIGHTED_ITEM_NOTIFICATIONS.put("heavy", true);
    HIGHLIGHTED_ITEM_NOTIFICATIONS.put("all", true);
    HIGHLIGHTED_ITEM_NOTIFICATIONS.put(
        "custom",
        getPropBoolean(
            props,
            "highlighted_item_notifications",
            HIGHLIGHTED_ITEM_NOTIFICATIONS.get("default")));

    HIGHLIGHTED_ITEM_NOTIF_VALUE.put("vanilla", 11000);
    HIGHLIGHTED_ITEM_NOTIF_VALUE.put("vanilla_resizable", 11000);
    HIGHLIGHTED_ITEM_NOTIF_VALUE.put("lite", 100);
    HIGHLIGHTED_ITEM_NOTIF_VALUE.put("default", 100);
    HIGHLIGHTED_ITEM_NOTIF_VALUE.put("heavy", 100);
    HIGHLIGHTED_ITEM_NOTIF_VALUE.put("all", 0);
    HIGHLIGHTED_ITEM_NOTIF_VALUE.put(
        "custom",
        getPropInt(
            props, "highlighted_item_notif_value", HIGHLIGHTED_ITEM_NOTIF_VALUE.get("default")));

    // OVERLAYS
    SHOW_HP_OVERLAY.put("vanilla", false);
    SHOW_HP_OVERLAY.put("vanilla_resizable", false);
    SHOW_HP_OVERLAY.put("lite", true);
    SHOW_HP_OVERLAY.put("default", true);
    SHOW_HP_OVERLAY.put("heavy", true);
    SHOW_HP_OVERLAY.put("all", true);
    SHOW_HP_OVERLAY.put(
        "custom", getPropBoolean(props, "show_statusdisplay", SHOW_HP_OVERLAY.get("default")));

    SHOW_BUFFS.put("vanilla", false);
    SHOW_BUFFS.put("vanilla_resizable", false);
    SHOW_BUFFS.put("lite", true);
    SHOW_BUFFS.put("default", true);
    SHOW_BUFFS.put("heavy", true);
    SHOW_BUFFS.put("all", true);
    SHOW_BUFFS.put("custom", getPropBoolean(props, "show_buffs", SHOW_BUFFS.get("default")));

    SHOW_LAST_MENU_ACTION.put("vanilla", false);
    SHOW_LAST_MENU_ACTION.put("vanilla_resizable", false);
    SHOW_LAST_MENU_ACTION.put("lite", false);
    SHOW_LAST_MENU_ACTION.put("default", false);
    SHOW_LAST_MENU_ACTION.put("heavy", true);
    SHOW_LAST_MENU_ACTION.put("all", true);
    SHOW_LAST_MENU_ACTION.put(
        "custom",
        getPropBoolean(props, "show_last_menu_action", SHOW_LAST_MENU_ACTION.get("default")));

    SHOW_MOUSE_TOOLTIP.put("vanilla", false);
    SHOW_MOUSE_TOOLTIP.put("vanilla_resizable", false);
    SHOW_MOUSE_TOOLTIP.put("lite", false);
    SHOW_MOUSE_TOOLTIP.put("default", false);
    SHOW_MOUSE_TOOLTIP.put("heavy", true);
    SHOW_MOUSE_TOOLTIP.put("all", true);
    SHOW_MOUSE_TOOLTIP.put(
        "custom", getPropBoolean(props, "show_mouse_tooltip", SHOW_MOUSE_TOOLTIP.get("default")));

    SHOW_EXTENDED_TOOLTIP.put("vanilla", false);
    SHOW_EXTENDED_TOOLTIP.put("vanilla_resizable", false);
    SHOW_EXTENDED_TOOLTIP.put("lite", false);
    SHOW_EXTENDED_TOOLTIP.put("default", true);
    SHOW_EXTENDED_TOOLTIP.put("heavy", true);
    SHOW_EXTENDED_TOOLTIP.put("all", true);
    SHOW_EXTENDED_TOOLTIP.put(
        "custom",
        getPropBoolean(props, "show_extended_tooltip", SHOW_EXTENDED_TOOLTIP.get("default")));

    SHOW_INVCOUNT.put("vanilla", false);
    SHOW_INVCOUNT.put("vanilla_resizable", false);
    SHOW_INVCOUNT.put("lite", true);
    SHOW_INVCOUNT.put("default", true);
    SHOW_INVCOUNT.put("heavy", true);
    SHOW_INVCOUNT.put("all", true);
    SHOW_INVCOUNT.put(
        "custom", getPropBoolean(props, "show_invcount", SHOW_INVCOUNT.get("default")));

    SHOW_RSCTIMES_BUTTONS.put("vanilla", false);
    SHOW_RSCTIMES_BUTTONS.put("vanilla_resizable", false);
    SHOW_RSCTIMES_BUTTONS.put("lite", true);
    SHOW_RSCTIMES_BUTTONS.put("default", true);
    SHOW_RSCTIMES_BUTTONS.put("heavy", true);
    SHOW_RSCTIMES_BUTTONS.put("all", true);
    SHOW_RSCTIMES_BUTTONS.put(
        "custom",
        getPropBoolean(props, "show_rsctimes_buttons", SHOW_RSCTIMES_BUTTONS.get("default")));

    RSCTIMES_BUTTONS_FUNCTIONAL.put("vanilla", false);
    RSCTIMES_BUTTONS_FUNCTIONAL.put("vanilla_resizable", false);
    RSCTIMES_BUTTONS_FUNCTIONAL.put("lite", true);
    RSCTIMES_BUTTONS_FUNCTIONAL.put("default", true);
    RSCTIMES_BUTTONS_FUNCTIONAL.put("heavy", true);
    RSCTIMES_BUTTONS_FUNCTIONAL.put("all", true);
    RSCTIMES_BUTTONS_FUNCTIONAL.put(
        "custom",
        getPropBoolean(
            props, "rsctimes_buttons_functional", RSCTIMES_BUTTONS_FUNCTIONAL.get("default")));

    WIKI_LOOKUP_ON_MAGIC_BOOK.put("vanilla", false);
    WIKI_LOOKUP_ON_MAGIC_BOOK.put("vanilla_resizable", false);
    WIKI_LOOKUP_ON_MAGIC_BOOK.put("lite", false);
    WIKI_LOOKUP_ON_MAGIC_BOOK.put("default", false);
    WIKI_LOOKUP_ON_MAGIC_BOOK.put("heavy", true);
    WIKI_LOOKUP_ON_MAGIC_BOOK.put("all", true);
    WIKI_LOOKUP_ON_MAGIC_BOOK.put(
        "custom",
        getPropBoolean(
            props, "wiki_lookup_on_magic_book", WIKI_LOOKUP_ON_MAGIC_BOOK.get("default")));

    MOTIVATIONAL_QUOTES_BUTTON.put("vanilla", false);
    MOTIVATIONAL_QUOTES_BUTTON.put("vanilla_resizable", false);
    MOTIVATIONAL_QUOTES_BUTTON.put("lite", false);
    MOTIVATIONAL_QUOTES_BUTTON.put("default", false);
    MOTIVATIONAL_QUOTES_BUTTON.put("heavy", true);
    MOTIVATIONAL_QUOTES_BUTTON.put("all", true);
    MOTIVATIONAL_QUOTES_BUTTON.put(
        "custom",
        getPropBoolean(
            props, "motivational_quotes_button", MOTIVATIONAL_QUOTES_BUTTON.get("default")));

    TOGGLE_XP_BAR_ON_STATS_BUTTON.put("vanilla", false);
    TOGGLE_XP_BAR_ON_STATS_BUTTON.put("vanilla_resizable", false);
    TOGGLE_XP_BAR_ON_STATS_BUTTON.put("lite", false);
    TOGGLE_XP_BAR_ON_STATS_BUTTON.put("default", false);
    TOGGLE_XP_BAR_ON_STATS_BUTTON.put("heavy", true);
    TOGGLE_XP_BAR_ON_STATS_BUTTON.put("all", true);
    TOGGLE_XP_BAR_ON_STATS_BUTTON.put(
        "custom",
        getPropBoolean(
            props, "toggle_xp_bar_on_stats_button", TOGGLE_XP_BAR_ON_STATS_BUTTON.get("default")));

    WIKI_LOOKUP_ON_HBAR.put("vanilla", false);
    WIKI_LOOKUP_ON_HBAR.put("vanilla_resizable", false);
    WIKI_LOOKUP_ON_HBAR.put("lite", false);
    WIKI_LOOKUP_ON_HBAR.put("default", true);
    WIKI_LOOKUP_ON_HBAR.put("heavy", true);
    WIKI_LOOKUP_ON_HBAR.put("all", true);
    WIKI_LOOKUP_ON_HBAR.put(
        "custom", getPropBoolean(props, "wiki_lookup_on_hbar", WIKI_LOOKUP_ON_HBAR.get("default")));

    SHOW_ITEM_GROUND_OVERLAY.put("vanilla", false);
    SHOW_ITEM_GROUND_OVERLAY.put("vanilla_resizable", false);
    SHOW_ITEM_GROUND_OVERLAY.put("lite", false);
    SHOW_ITEM_GROUND_OVERLAY.put("default", true);
    SHOW_ITEM_GROUND_OVERLAY.put("heavy", true);
    SHOW_ITEM_GROUND_OVERLAY.put("all", true);
    SHOW_ITEM_GROUND_OVERLAY.put(
        "custom", getPropBoolean(props, "show_iteminfo", SHOW_ITEM_GROUND_OVERLAY.get("default")));

    SHOW_PLAYER_NAME_OVERLAY.put("vanilla", false);
    SHOW_PLAYER_NAME_OVERLAY.put("vanilla_resizable", false);
    SHOW_PLAYER_NAME_OVERLAY.put("lite", false);
    SHOW_PLAYER_NAME_OVERLAY.put("default", false);
    SHOW_PLAYER_NAME_OVERLAY.put("heavy", false);
    SHOW_PLAYER_NAME_OVERLAY.put("all", true);
    SHOW_PLAYER_NAME_OVERLAY.put(
        "custom",
        getPropBoolean(props, "show_playerinfo", SHOW_PLAYER_NAME_OVERLAY.get("default")));

    SHOW_FRIEND_NAME_OVERLAY.put("vanilla", false);
    SHOW_FRIEND_NAME_OVERLAY.put("vanilla_resizable", false);
    SHOW_FRIEND_NAME_OVERLAY.put("lite", false);
    SHOW_FRIEND_NAME_OVERLAY.put("default", false);
    SHOW_FRIEND_NAME_OVERLAY.put("heavy", true);
    SHOW_FRIEND_NAME_OVERLAY.put("all", true);
    SHOW_FRIEND_NAME_OVERLAY.put(
        "custom",
        getPropBoolean(props, "show_friendinfo", SHOW_FRIEND_NAME_OVERLAY.get("default")));

    SHOW_NPC_NAME_OVERLAY.put("vanilla", false);
    SHOW_NPC_NAME_OVERLAY.put("vanilla_resizable", false);
    SHOW_NPC_NAME_OVERLAY.put("lite", false);
    SHOW_NPC_NAME_OVERLAY.put("default", false);
    SHOW_NPC_NAME_OVERLAY.put("heavy", false);
    SHOW_NPC_NAME_OVERLAY.put("all", true);
    SHOW_NPC_NAME_OVERLAY.put(
        "custom", getPropBoolean(props, "show_npcinfo", SHOW_NPC_NAME_OVERLAY.get("default")));

    EXTEND_IDS_OVERLAY.put("vanilla", false);
    EXTEND_IDS_OVERLAY.put("vanilla_resizable", false);
    EXTEND_IDS_OVERLAY.put("lite", false);
    EXTEND_IDS_OVERLAY.put("default", false);
    EXTEND_IDS_OVERLAY.put("heavy", false);
    EXTEND_IDS_OVERLAY.put("all", true);
    EXTEND_IDS_OVERLAY.put(
        "custom", getPropBoolean(props, "extend_idsinfo", EXTEND_IDS_OVERLAY.get("default")));

    TRACE_OBJECT_INFO.put("vanilla", false);
    TRACE_OBJECT_INFO.put("vanilla_resizable", false);
    TRACE_OBJECT_INFO.put("lite", false);
    TRACE_OBJECT_INFO.put("default", false);
    TRACE_OBJECT_INFO.put("heavy", false);
    TRACE_OBJECT_INFO.put("all", true);
    TRACE_OBJECT_INFO.put(
        "custom", getPropBoolean(props, "trace_objectinfo", TRACE_OBJECT_INFO.get("default")));

    SHOW_COMBAT_INFO.put("vanilla", false);
    SHOW_COMBAT_INFO.put("vanilla_resizable", false);
    SHOW_COMBAT_INFO.put("lite", false);
    SHOW_COMBAT_INFO.put("default", false);
    SHOW_COMBAT_INFO.put("heavy", true);
    SHOW_COMBAT_INFO.put("all", true);
    SHOW_COMBAT_INFO.put(
        "custom", getPropBoolean(props, "show_combat_info", SHOW_COMBAT_INFO.get("default")));

    LAG_INDICATOR.put("vanilla", false);
    LAG_INDICATOR.put("vanilla_resizable", false);
    LAG_INDICATOR.put("lite", false);
    LAG_INDICATOR.put("default", true);
    LAG_INDICATOR.put("heavy", true);
    LAG_INDICATOR.put("all", true);
    LAG_INDICATOR.put("custom", getPropBoolean(props, "indicators", LAG_INDICATOR.get("default")));

    SHOW_PLAYER_POSITION.put("vanilla", false);
    SHOW_PLAYER_POSITION.put("vanilla_resizable", false);
    SHOW_PLAYER_POSITION.put("lite", false);
    SHOW_PLAYER_POSITION.put("default", false);
    SHOW_PLAYER_POSITION.put("heavy", true);
    SHOW_PLAYER_POSITION.put("all", true);
    SHOW_PLAYER_POSITION.put(
        "custom",
        getPropBoolean(props, "show_player_position", SHOW_PLAYER_POSITION.get("default")));

    HIDE_FPS.put("vanilla", false);
    HIDE_FPS.put("vanilla_resizable", false);
    HIDE_FPS.put("lite", false);
    HIDE_FPS.put("default", false);
    HIDE_FPS.put("heavy", true);
    HIDE_FPS.put("all", true);
    HIDE_FPS.put("custom", getPropBoolean(props, "hide_fps", HIDE_FPS.get("default")));

    SHOW_XP_BAR.put("vanilla", false);
    SHOW_XP_BAR.put("vanilla_resizable", false);
    SHOW_XP_BAR.put("lite", false);
    SHOW_XP_BAR.put("default", false);
    SHOW_XP_BAR.put("heavy", true);
    SHOW_XP_BAR.put("all", true);
    SHOW_XP_BAR.put("custom", getPropBoolean(props, "show_xp_bar", SHOW_XP_BAR.get("default")));

    NPC_HEALTH_SHOW_PERCENTAGE.put("vanilla", false);
    NPC_HEALTH_SHOW_PERCENTAGE.put("vanilla_resizable", false);
    NPC_HEALTH_SHOW_PERCENTAGE.put("lite", false);
    NPC_HEALTH_SHOW_PERCENTAGE.put("default", false);
    NPC_HEALTH_SHOW_PERCENTAGE.put("heavy", false);
    NPC_HEALTH_SHOW_PERCENTAGE.put("all", true);
    NPC_HEALTH_SHOW_PERCENTAGE.put(
        "custom",
        getPropBoolean(props, "use_percentage", NPC_HEALTH_SHOW_PERCENTAGE.get("default")));

    SHOW_HITBOX.put("vanilla", false);
    SHOW_HITBOX.put("vanilla_resizable", false);
    SHOW_HITBOX.put("lite", false);
    SHOW_HITBOX.put("default", false);
    SHOW_HITBOX.put("heavy", false);
    SHOW_HITBOX.put("all", true);
    SHOW_HITBOX.put("custom", getPropBoolean(props, "show_hitbox", SHOW_HITBOX.get("default")));

    SHOW_FOOD_HEAL_OVERLAY.put("vanilla", false);
    SHOW_FOOD_HEAL_OVERLAY.put("vanilla_resizable", false);
    SHOW_FOOD_HEAL_OVERLAY.put("lite", false);
    SHOW_FOOD_HEAL_OVERLAY.put("default", false);
    SHOW_FOOD_HEAL_OVERLAY.put("heavy", true);
    SHOW_FOOD_HEAL_OVERLAY.put("all", true);
    SHOW_FOOD_HEAL_OVERLAY.put(
        "custom",
        getPropBoolean(props, "show_food_heal_overlay", SHOW_FOOD_HEAL_OVERLAY.get("default")));

    SHOW_TIME_UNTIL_HP_REGEN.put("vanilla", false);
    SHOW_TIME_UNTIL_HP_REGEN.put("vanilla_resizable", false);
    SHOW_TIME_UNTIL_HP_REGEN.put("lite", false);
    SHOW_TIME_UNTIL_HP_REGEN.put("default", false);
    SHOW_TIME_UNTIL_HP_REGEN.put("heavy", true);
    SHOW_TIME_UNTIL_HP_REGEN.put("all", true);
    SHOW_TIME_UNTIL_HP_REGEN.put(
        "custom",
        getPropBoolean(props, "show_time_until_hp_regen", SHOW_TIME_UNTIL_HP_REGEN.get("default")));

    DEBUG.put("vanilla", false);
    DEBUG.put("vanilla_resizable", false);
    DEBUG.put("lite", false);
    DEBUG.put("default", false);
    DEBUG.put("heavy", false);
    DEBUG.put("all", false);
    DEBUG.put("custom", getPropBoolean(props, "debug", DEBUG.get("default")));

    EXCEPTION_HANDLER.put("vanilla", false);
    EXCEPTION_HANDLER.put("vanilla_resizable", false);
    EXCEPTION_HANDLER.put("lite", false);
    EXCEPTION_HANDLER.put("default", false);
    EXCEPTION_HANDLER.put("heavy", false);
    EXCEPTION_HANDLER.put("all", false);
    EXCEPTION_HANDLER.put(
        "custom", getPropBoolean(props, "exception_handler", EXCEPTION_HANDLER.get("default")));

    HIGHLIGHTED_ITEMS.put("vanilla", new ArrayList<String>());
    HIGHLIGHTED_ITEMS.put("vanilla_resizable", new ArrayList<String>());
    HIGHLIGHTED_ITEMS.put("lite", new ArrayList<String>());
    HIGHLIGHTED_ITEMS.put("default", new ArrayList<String>());
    HIGHLIGHTED_ITEMS.put("heavy", new ArrayList<String>());
    HIGHLIGHTED_ITEMS.put("all", new ArrayList<String>());
    HIGHLIGHTED_ITEMS.put(
        "custom",
        getPropArrayListString(props, "highlighted_items", HIGHLIGHTED_ITEMS.get("default")));

    BLOCKED_ITEMS.put("vanilla", new ArrayList<String>());
    BLOCKED_ITEMS.put("vanilla_resizable", new ArrayList<String>());
    BLOCKED_ITEMS.put("lite", new ArrayList<String>());
    BLOCKED_ITEMS.put("default", new ArrayList<String>());
    BLOCKED_ITEMS.put("heavy", new ArrayList<String>());
    BLOCKED_ITEMS.put("all", new ArrayList<String>());
    BLOCKED_ITEMS.put(
        "custom", getPropArrayListString(props, "blocked_items", BLOCKED_ITEMS.get("default")));

    //// streaming
    TWITCH_CHAT_ENABLED.put("vanilla", false);
    TWITCH_CHAT_ENABLED.put("vanilla_resizable", false);
    TWITCH_CHAT_ENABLED.put("lite", true);
    TWITCH_CHAT_ENABLED.put("default", true);
    TWITCH_CHAT_ENABLED.put("heavy", true);
    TWITCH_CHAT_ENABLED.put("all", true);
    TWITCH_CHAT_ENABLED.put(
        "custom", getPropBoolean(props, "twitch_enabled", TWITCH_CHAT_ENABLED.get("default")));

    TWITCH_HIDE_CHAT.put("vanilla", true);
    TWITCH_HIDE_CHAT.put("vanilla_resizable", true);
    TWITCH_HIDE_CHAT.put("lite", false);
    TWITCH_HIDE_CHAT.put("default", false);
    TWITCH_HIDE_CHAT.put("heavy", false);
    TWITCH_HIDE_CHAT.put("all", false);
    TWITCH_HIDE_CHAT.put(
        "custom", getPropBoolean(props, "twitch_hide", TWITCH_HIDE_CHAT.get("default")));

    TWITCH_CHANNEL.put("vanilla", "");
    TWITCH_CHANNEL.put("vanilla_resizable", "");
    TWITCH_CHANNEL.put("lite", "");
    TWITCH_CHANNEL.put("default", "");
    TWITCH_CHANNEL.put("heavy", "");
    TWITCH_CHANNEL.put("all", "");
    TWITCH_CHANNEL.put(
        "custom", getPropString(props, "twitch_channel", TWITCH_CHANNEL.get("default")));

    TWITCH_OAUTH.put("vanilla", "");
    TWITCH_OAUTH.put("vanilla_resizable", "");
    TWITCH_OAUTH.put("lite", "");
    TWITCH_OAUTH.put("default", "");
    TWITCH_OAUTH.put("heavy", "");
    TWITCH_OAUTH.put("all", "");
    TWITCH_OAUTH.put("custom", getPropString(props, "twitch_oauth", TWITCH_OAUTH.get("default")));

    TWITCH_USERNAME.put("vanilla", "");
    TWITCH_USERNAME.put("vanilla_resizable", "");
    TWITCH_USERNAME.put("lite", "");
    TWITCH_USERNAME.put("default", "");
    TWITCH_USERNAME.put("heavy", "");
    TWITCH_USERNAME.put("all", "");
    TWITCH_USERNAME.put(
        "custom", getPropString(props, "twitch_username", TWITCH_USERNAME.get("default")));

    SAVE_LOGININFO.put("vanilla", false);
    SAVE_LOGININFO.put("vanilla_resizable", false);
    SAVE_LOGININFO.put("lite", true);
    SAVE_LOGININFO.put("default", true);
    SAVE_LOGININFO.put("heavy", true);
    SAVE_LOGININFO.put("all", true);
    SAVE_LOGININFO.put(
        "custom", getPropBoolean(props, "save_logininfo", SAVE_LOGININFO.get("default")));

    START_LOGINSCREEN.put("vanilla", false);
    START_LOGINSCREEN.put("vanilla_resizable", false);
    START_LOGINSCREEN.put("lite", true);
    START_LOGINSCREEN.put("default", true);
    START_LOGINSCREEN.put("heavy", true);
    START_LOGINSCREEN.put("all", true);
    START_LOGINSCREEN.put(
        "custom", getPropBoolean(props, "start_loginscreen", START_LOGINSCREEN.get("default")));

    SPEEDRUNNER_MODE_ACTIVE.put("vanilla", false);
    SPEEDRUNNER_MODE_ACTIVE.put("vanilla_resizable", false);
    SPEEDRUNNER_MODE_ACTIVE.put("lite", false);
    SPEEDRUNNER_MODE_ACTIVE.put("default", false);
    SPEEDRUNNER_MODE_ACTIVE.put("heavy", false);
    SPEEDRUNNER_MODE_ACTIVE.put("all", true);
    SPEEDRUNNER_MODE_ACTIVE.put(
        "custom", getPropBoolean(props, "speedrun_active", SPEEDRUNNER_MODE_ACTIVE.get("default")));

    UPDATE_CONFIRMATION.put("vanilla", false);
    UPDATE_CONFIRMATION.put("vanilla_resizable", false);
    UPDATE_CONFIRMATION.put("lite", false);
    UPDATE_CONFIRMATION.put("default", false);
    UPDATE_CONFIRMATION.put("heavy", false);
    UPDATE_CONFIRMATION.put("all", false);
    UPDATE_CONFIRMATION.put("custom", getPropBoolean(props, "update_confirmation", true));

    //// world list
    initWorlds();

    COMBAT_STYLE.put("vanilla", Client.COMBAT_AGGRESSIVE);
    COMBAT_STYLE.put("vanilla_resizable", Client.COMBAT_AGGRESSIVE);
    COMBAT_STYLE.put("lite", Client.COMBAT_AGGRESSIVE);
    COMBAT_STYLE.put("default", Client.COMBAT_AGGRESSIVE);
    COMBAT_STYLE.put("heavy", Client.COMBAT_AGGRESSIVE);
    COMBAT_STYLE.put("all", Client.COMBAT_AGGRESSIVE);
    COMBAT_STYLE.put("custom", getPropInt(props, "combat_style", COMBAT_STYLE.get("default")));

    WORLD.put("vanilla", 1);
    WORLD.put("vanilla_resizable", 1);
    WORLD.put("lite", 1);
    WORLD.put("default", 1);
    WORLD.put("heavy", 1);
    WORLD.put("all", 1);
    WORLD.put("custom", getPropInt(props, "world", WORLD.get("default")));

    FIRST_TIME.put("vanilla", false);
    FIRST_TIME.put("vanilla_resizable", false);
    FIRST_TIME.put("lite", false);
    FIRST_TIME.put("default", false);
    FIRST_TIME.put("heavy", false);
    FIRST_TIME.put("all", false);
    FIRST_TIME.put("custom", getPropBoolean(props, "first_time", true));

    UPDATE_CONFIRMATION.put("vanilla", false);
    UPDATE_CONFIRMATION.put("vanilla_resizable", false);
    UPDATE_CONFIRMATION.put("lite", false);
    UPDATE_CONFIRMATION.put("default", false);
    UPDATE_CONFIRMATION.put("heavy", false);
    UPDATE_CONFIRMATION.put("all", false);
    UPDATE_CONFIRMATION.put("custom", getPropBoolean(props, "update_confirmation", true));

    // Sanitize settings
    if (CUSTOM_CLIENT_SIZE_X.get("custom") < 512) {
      CUSTOM_CLIENT_SIZE_X.put("custom", 512);
      save("custom");
    }
    if (CUSTOM_CLIENT_SIZE_Y.get("custom") < 357) {
      CUSTOM_CLIENT_SIZE_Y.put("custom", 357);
      save("custom");
    }

    if (INTEGER_SCALING_FACTOR.get("custom") < (int) Renderer.minScalar) {
      INTEGER_SCALING_FACTOR.put("custom", (int) Renderer.minScalar);
    } else if (INTEGER_SCALING_FACTOR.get("custom") > (int) Renderer.maxIntegerScalar) {
      INTEGER_SCALING_FACTOR.put("custom", (int) Renderer.maxIntegerScalar);
    }

    if (BILINEAR_SCALING_FACTOR.get("custom") < Renderer.minScalar) {
      BILINEAR_SCALING_FACTOR.put("custom", Renderer.minScalar);
    } else if (BILINEAR_SCALING_FACTOR.get("custom") > Renderer.maxInterpolationScalar) {
      BILINEAR_SCALING_FACTOR.put("custom", Renderer.maxInterpolationScalar);
    }

    if (BICUBIC_SCALING_FACTOR.get("custom") < Renderer.minScalar) {
      BICUBIC_SCALING_FACTOR.put("custom", Renderer.minScalar);
    } else if (BICUBIC_SCALING_FACTOR.get("custom") > Renderer.maxInterpolationScalar) {
      BICUBIC_SCALING_FACTOR.put("custom", Renderer.maxInterpolationScalar);
    }

    if (WORLD.get("custom") < 0) {
      WORLD.put("custom", 0);
      save("custom");
    } else if (WORLD.get("custom") > Settings.WORLDS_TO_DISPLAY) {
      WORLD.put("custom", Settings.WORLDS_TO_DISPLAY);
      save("custom");
    }

    if (VIEW_DISTANCE.get("custom") < 2300) {
      VIEW_DISTANCE.put("custom", 2300);
      save("custom");
    } else if (VIEW_DISTANCE.get("custom") > 20000) {
      VIEW_DISTANCE.put("custom", 20000);
      save("custom");
    }

    if (COMBAT_STYLE.get("custom") < Client.COMBAT_CONTROLLED) {
      COMBAT_STYLE.put("custom", Client.COMBAT_CONTROLLED);
      save("custom");
    } else if (COMBAT_STYLE.get("custom") > Client.COMBAT_DEFENSIVE) {
      COMBAT_STYLE.put("custom", Client.COMBAT_DEFENSIVE);
      save("custom");
    }
  }

  /**
   * Determine whether we should default to dark mode for the app interface
   *
   * @return {@code boolean} indicating whether dark mode should be used
   */
  public static boolean shouldDefaultDarkMode() {
    // Detect via JNA/registry for Windows
    if (Util.isWindowsOS()) {
      return Util.isWindowsOSDarkTheme();
    }

    // Default to dark mode for other OS's
    return true;
  }

  public static void initDir() { // TODO: Consider moving to a more relevant place
    // Find JAR directory
    // TODO: Consider utilizing Util.makeDirectory()
    Dir.JAR = ".";
    try {
      Dir.JAR =
          Settings.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
      int indexFileSep1 = Dir.JAR.lastIndexOf('/');
      int indexFileSep2 = Dir.JAR.lastIndexOf('\\');
      int index = (indexFileSep1 > indexFileSep2) ? indexFileSep1 : indexFileSep2;
      if (index != -1) Dir.JAR = Dir.JAR.substring(0, index);
    } catch (Exception e) {
    }

    // Load other directories
    Dir.SCREENSHOT = Dir.JAR + "/screenshots";
    Util.makeDirectory(Dir.SCREENSHOT);
    Dir.REPLAY = Dir.JAR + "/replay";
    Util.makeDirectory(Dir.REPLAY);
    Dir.WORLDS = Dir.JAR + "/worlds";
    Util.makeDirectory(Dir.WORLDS);
  }

  /**
   * Processes the commands triggered by pressing keybinds
   *
   * @param commandName the name of a keybind command as defined by ConfigWindow#addKeybindSet
   */
  public static boolean processKeybindCommand(String commandName) {
    switch (commandName) {
      case "logout":
        // TODO: if (Client.state != Client.STATE_LOGIN) Client.logout();
        return true;
      case "screenshot":
        Renderer.takeScreenshot(false);
        return true;
      case "toggle_scaling":
        Settings.toggleWindowScaling();
        return true;
      case "increase_scale":
        Settings.increaseScale();
        return true;
      case "decrease_scale":
        Settings.decreaseScale();
        return true;
      case "toggle_indicators":
        // TODO: Settings.toggleLagIndicator();
        return true;
      case "reset_zoom":
        // TODO: Camera.resetZoom();
        return true;
      case "reset_rotation":
        // TODO: Camera.resetRotation();
        return true;
      case "toggle_trackpad_camera_rotation":
        Settings.toggleTrackpadRotation();
        return true;
      case "toggle_colorize":
        // TODO: Settings.toggleColorTerminal();
        return true;
      case "toggle_combat_xp_menu":
        Settings.toggleCombatMenuShown();
        return true;
      case "toggle_inventory_full_alert":
        Settings.toggleInventoryFullAlert();
        return true;
      case "toggle_food_heal_overlay":
        // TODO: Settings.toggleFoodOverlay();
        return true;
      case "toggle_friend_name_overlay":
        Settings.toggleShowFriendNameOverlay();
        return true;
      case "toggle_buffs_display":
        Settings.toggleBuffs();
        return true;
      case "toggle_hp_display":
        Settings.toggleHpOverlay();
        return true;
      case "toggle_position_overlay":
        Settings.togglePosition();
        return true;
      case "toggle_fps_overlay":
        Settings.toggleFPS();
        return true;
      case "toggle_inven_count_overlay":
        Settings.toggleInvCount();
        return true;
      case "toggle_bypass_attack":
        Settings.toggleAttackAlwaysLeftClick();
        return true;
      case "toggle_ipdns":
        // TODO: Settings.toggleShowLoginIpAddress();
        return true;
      case "toggle_item_overlay":
        // TODO: Settings.toggleShowItemGroundOverlay();
        return true;
      case "toggle_hitboxes":
        Settings.toggleShowHitbox();
        return true;
      case "toggle_npc_name_overlay":
        Settings.toggleShowNPCNameOverlay();
        return true;
      case "toggle_ids_overlay":
        Settings.toggleExtendIdsOverlay();
        return true;
      case "toggle_trace_object_info":
        Settings.toggleTraceObjectInfo();
        return true;
      case "toggle_player_name_overlay":
        Settings.toggleShowPlayerNameOverlay();
        return true;
      case "toggle_roof_hiding":
        Settings.toggleHideRoofs();
        return true;
      case "toggle_save_login_info":
        // TODO: Settings.toggleSaveLoginInfo();
        return true;
      case "toggle_health_regen_timer":
        // TODO: Settings.toggleHealthRegenTimer();
        return true;
      case "toggle_twitch_chat":
        // TODO: maybe someday Settings.toggleTwitchHide();
        return true;
        /*case "toggle_xp_drops":
        // TODO: Settings.toggleXpDrops();
        return true;*/
      case "show_config_window":
        Launcher.getConfigWindow().toggleConfigWindow();
        return true;
      case "show_worldmap_window":
        Launcher.getWorldMapWindow().toggleWorldMapWindow();
        return true;
      case "show_queue_window":
        // Try to not allow Replay window to appear while logged into the game :-)
        // (can still open while on login screen, then login to the game)
        /* TODO if replays are added
        if (Replay.isPlaying
                || Replay.isSeeking
                || Replay.isRestarting
                || Client.state == Client.STATE_LOGIN) Launcher.getQueueWindow().showQueueWindow();
         */
        return true;
      case "world_1":
        if (Client.state == Client.STATE_LOGIN) Game.getInstance().getJConfig().changeWorld(1);
        return true;
      case "world_2":
        if (Client.state == Client.STATE_LOGIN) Game.getInstance().getJConfig().changeWorld(2);
        return true;
      case "world_3":
        if (Client.state == Client.STATE_LOGIN) Game.getInstance().getJConfig().changeWorld(3);
        return true;
      case "world_4":
        if (Client.state == Client.STATE_LOGIN) Game.getInstance().getJConfig().changeWorld(4);
        return true;
      case "world_5":
        if (Client.state == Client.STATE_LOGIN) Game.getInstance().getJConfig().changeWorld(5);
        return true;
      case "world_6":
        if (Client.state == Client.STATE_LOGIN) Game.getInstance().getJConfig().changeWorld(6);
        return true;
      case "world_7":
        if (Client.state == Client.STATE_LOGIN) Game.getInstance().getJConfig().changeWorld(7);
        return true;
      case "world_8":
        if (Client.state == Client.STATE_LOGIN) Game.getInstance().getJConfig().changeWorld(8);
        return true;
      case "world_9":
        if (Client.state == Client.STATE_LOGIN) Game.getInstance().getJConfig().changeWorld(9);
        return true;
      case "world_10":
        if (Client.state == Client.STATE_LOGIN) Game.getInstance().getJConfig().changeWorld(10);
        return true;
      case "toggle_wiki_hbar_button":
        Settings.toggleWikiHbar();
        return true;
      case "stop":
      case "restart":
      case "pause":
      case "ff_plus":
      case "ff_minus":
      case "ff_reset":
      case "next":
      case "prev":
        /* TODO: if replays are implemented
        Replay.controlPlayback(commandName);
        return Replay.isPlaying;
         */
        return true;
      case "toggle_xp_bar":
        Settings.toggleGoalBar();
        return true;
      case "show_seek_bar":
        // TODO: if replays are implemented Settings.toggleShowSeekBar();
        return true;
      case "show_player_controls":
        // TODO: if replays are implemented Settings.toggleShowPlayerControls();
        return true;
      default:
        Logger.Error("An unrecognized command was sent to processCommand: " + commandName);
        break;
    }
    return false;
  }

  public static void toggleWindowScaling() {
    if (ScaledWindow.getInstance().isViewportLoaded()) {
      SCALED_CLIENT_WINDOW.put(
          currentProfile, new Boolean(!SCALED_CLIENT_WINDOW.get(currentProfile)));

      if (SCALED_CLIENT_WINDOW.get(currentProfile)) {
        Client.displayMessage("@cya@Client scaling is now enabled", Client.CHAT_NONE);
      } else {
        Client.displayMessage("@cya@Client scaling is now disabled", Client.CHAT_NONE);
      }

      Settings.renderingScalarUpdateRequired = true;

      save();
    }
  }

  public static void increaseScale() {
    if (ScaledWindow.getInstance().isViewportLoaded()) {
      float scalingDelta = 0f;

      if (!SCALED_CLIENT_WINDOW.get(currentProfile)) {
        Client.displayMessage(
            "@cya@Enable client scaling before attempting to increase the scale value",
            Client.CHAT_NONE);
        return;
      }

      String scaleLimitReached = "@cya@Cannot increase the scale further";

      if (SCALING_ALGORITHM.get(currentProfile).equals(AffineTransformOp.TYPE_NEAREST_NEIGHBOR)) {
        int currentIntegerScalingFactor = INTEGER_SCALING_FACTOR.get(currentProfile);
        scalingDelta = 1.0f;

        if (currentIntegerScalingFactor < (int) Renderer.maxIntegerScalar) {
          int newScale = currentIntegerScalingFactor + (int) scalingDelta;

          INTEGER_SCALING_FACTOR.put(currentProfile, newScale);
          Client.displayMessage("@cya@Increased scale to " + newScale + "x", Client.CHAT_NONE);
        } else {
          Client.displayMessage(scaleLimitReached, Client.CHAT_NONE);
          return;
        }
      } else if (SCALING_ALGORITHM.get(currentProfile).equals(AffineTransformOp.TYPE_BILINEAR)) {
        float currentBilinearScalingFactor = BILINEAR_SCALING_FACTOR.get(currentProfile);
        scalingDelta = 0.1f;

        if (currentBilinearScalingFactor < Renderer.maxInterpolationScalar) {
          float newScale =
              BigDecimal.valueOf(currentBilinearScalingFactor + scalingDelta)
                  .setScale(1, RoundingMode.HALF_DOWN)
                  .floatValue();
          BILINEAR_SCALING_FACTOR.put(currentProfile, newScale);
          Client.displayMessage("@cya@Increased scale to " + newScale + "x", Client.CHAT_NONE);
        } else {
          Client.displayMessage(scaleLimitReached, Client.CHAT_NONE);
          return;
        }
      } else if (SCALING_ALGORITHM.get(currentProfile).equals(AffineTransformOp.TYPE_BICUBIC)) {
        float currentBicubicScalingFactor = BICUBIC_SCALING_FACTOR.get(currentProfile);
        scalingDelta = 0.1f;

        if (currentBicubicScalingFactor < Renderer.maxInterpolationScalar) {
          float newScale =
              BigDecimal.valueOf(currentBicubicScalingFactor + scalingDelta)
                  .setScale(1, RoundingMode.HALF_DOWN)
                  .floatValue();
          BICUBIC_SCALING_FACTOR.put(currentProfile, newScale);
          Client.displayMessage("@cya@Increased scale to " + newScale + "x", Client.CHAT_NONE);
        } else {
          Client.displayMessage(scaleLimitReached, Client.CHAT_NONE);
          return;
        }
      }

      Settings.renderingScalarUpdateRequired = true;

      save();
    }
  }

  public static void decreaseScale() {
    if (ScaledWindow.getInstance().isViewportLoaded()) {
      float scalingDelta = 0;

      if (!SCALED_CLIENT_WINDOW.get(currentProfile)) {
        Client.displayMessage(
            "@cya@Enable client scaling before attempting to decrease the scale value",
            Client.CHAT_NONE);
        return;
      }

      String scaleLimitReached = "@cya@Cannot decrease the scale further";

      if (SCALING_ALGORITHM.get(currentProfile).equals(AffineTransformOp.TYPE_NEAREST_NEIGHBOR)) {
        int currentIntegerScalingFactor = INTEGER_SCALING_FACTOR.get(currentProfile);
        scalingDelta = 1.0f;

        if (currentIntegerScalingFactor > (int) Renderer.minScalar) {
          int newScale = currentIntegerScalingFactor - (int) scalingDelta;
          INTEGER_SCALING_FACTOR.put(currentProfile, newScale);
          Client.displayMessage("@cya@Decreased scale to " + newScale + "x", Client.CHAT_NONE);
        } else {
          Client.displayMessage(scaleLimitReached, Client.CHAT_NONE);
          return;
        }
      } else if (SCALING_ALGORITHM.get(currentProfile).equals(AffineTransformOp.TYPE_BILINEAR)) {
        float currentBilinearScalingFactor = BILINEAR_SCALING_FACTOR.get(currentProfile);
        scalingDelta = 0.1f;

        if (currentBilinearScalingFactor > Renderer.minScalar) {
          float newScale =
              BigDecimal.valueOf(currentBilinearScalingFactor - scalingDelta)
                  .setScale(1, RoundingMode.HALF_DOWN)
                  .floatValue();
          BILINEAR_SCALING_FACTOR.put(currentProfile, newScale);
          Client.displayMessage("@cya@Decreased scale to " + newScale + "x", Client.CHAT_NONE);
        } else {
          Client.displayMessage(scaleLimitReached, Client.CHAT_NONE);
          return;
        }
      } else if (SCALING_ALGORITHM.get(currentProfile).equals(AffineTransformOp.TYPE_BICUBIC)) {
        float currentBicubicScalingFactor = BICUBIC_SCALING_FACTOR.get(currentProfile);
        scalingDelta = 0.1f;

        if (currentBicubicScalingFactor > Renderer.minScalar) {
          float newScale =
              BigDecimal.valueOf(currentBicubicScalingFactor - scalingDelta)
                  .setScale(1, RoundingMode.HALF_DOWN)
                  .floatValue();
          BICUBIC_SCALING_FACTOR.put(currentProfile, newScale);
          Client.displayMessage("@cya@Decreased scale to " + newScale + "x", Client.CHAT_NONE);
        } else {
          Client.displayMessage(scaleLimitReached, Client.CHAT_NONE);
          return;
        }
      }

      Settings.renderingScalarUpdateRequired = true;

      save();
    }
  }

  public static void toggleAttackAlwaysLeftClick() {
    ATTACK_ALWAYS_LEFT_CLICK.put(
        currentProfile, new Boolean(!ATTACK_ALWAYS_LEFT_CLICK.get(currentProfile)));

    if (ATTACK_ALWAYS_LEFT_CLICK.get(currentProfile)) {
      Client.displayMessage(
          "@cya@You are now able to left click attack all monsters", Client.CHAT_NONE);
    } else {
      Client.displayMessage(
          "@cya@You are no longer able to left click attack all monsters", Client.CHAT_NONE);
    }

    save();
  }

  /*public static void toggleNumberedDialogue() {
    NUMBERED_DIALOGUE_OPTIONS.put(
        currentProfile, new Boolean(!NUMBERED_DIALOGUE_OPTIONS.get(currentProfile)));

    if (NUMBERED_DIALOGUE_OPTIONS.get(currentProfile)) {
      Client.displayMessage("@cya@Displaying numbered dialogue options", Client.CHAT_NONE);
    } else {
      Client.displayMessage(
          "@cya@No longer displaying numbered dialogue options", Client.CHAT_NONE);
    }

    save();
  }*/

  public static void toggleHideRoofs() {
    HIDE_ROOFS.put(currentProfile, !HIDE_ROOFS.get(currentProfile));

    if (HIDE_ROOFS.get(currentProfile)) {
      Client.displayMessage("@cya@Roofs are now hidden", Client.CHAT_NONE);
    } else {
      Client.displayMessage("@cya@Roofs are now shown", Client.CHAT_NONE);
    }

    save();
  }

  public static void toggleCombatMenuShown() {
    COMBAT_MENU_SHOWN.put(currentProfile, !COMBAT_MENU_SHOWN.get(currentProfile));

    if (COMBAT_MENU_SHOWN.get(currentProfile)) {
      Client.displayMessage("@cya@Combat style is now shown", Client.CHAT_NONE);
    } else {
      Client.displayMessage("@cya@Combat style is now hidden", Client.CHAT_NONE);
    }

    save();
  }

  public static void toggleShowFriendNameOverlay() {
    SHOW_FRIEND_NAME_OVERLAY.put(currentProfile, !SHOW_FRIEND_NAME_OVERLAY.get(currentProfile));

    if (SHOW_FRIEND_NAME_OVERLAY.get(currentProfile)) {
      Client.displayMessage("@cya@Friend Names overlay now shown", Client.CHAT_NONE);
    } else {
      Client.displayMessage("@cya@Friend Names overlay now hidden", Client.CHAT_NONE);
    }

    save();
  }

  public static void togglePosition() {
    SHOW_PLAYER_POSITION.put(currentProfile, !SHOW_PLAYER_POSITION.get(currentProfile));
    if (SHOW_PLAYER_POSITION.get(currentProfile))
      Client.displayMessage("@cya@Global Position is now shown", Client.CHAT_NONE);
    else Client.displayMessage("@cya@Global Position is now hidden", Client.CHAT_NONE);
    save();
  }

  public static void toggleFPS() {
    HIDE_FPS.put(currentProfile, !HIDE_FPS.get(currentProfile));
    if (HIDE_FPS.get(currentProfile))
      Client.displayMessage("@cya@FPS is now hidden", Client.CHAT_NONE);
    else Client.displayMessage("@cya@FPS is now shown", Client.CHAT_NONE);
    save();
  }

  public static void toggleXPBar() {
    SHOW_XP_BAR.put(currentProfile, !SHOW_XP_BAR.get(currentProfile));
    if (SHOW_XP_BAR.get(currentProfile))
      Client.displayMessage("@cya@XP Bar is now shown", Client.CHAT_NONE);
    else Client.displayMessage("@cya@XP Bar is now hidden", Client.CHAT_NONE);
    save();
  }

  /*public static void toggleSkillClickPinning() {
    XPBar.skillClickPinning = !XPBar.skillClickPinning;
    save();
  }*/

  public static void toggleXPBarPin() {
    SHOW_XP_BAR.put(currentProfile, true);
    if (!XPBar.pinnedBar) Client.displayMessage("@cya@XP Bar is now pinned", Client.CHAT_NONE);
    else Client.displayMessage("@cya@XP Bar is now unpinned", Client.CHAT_NONE);
    XPBar.pinnedBar = !XPBar.pinnedBar;
    save();
  }

  /*public static void toggleShowSeekBar() {
    SHOW_SEEK_BAR.put(currentProfile, !SHOW_SEEK_BAR.get(currentProfile));
    if (SHOW_SEEK_BAR.get(currentProfile))
      Client.displayMessage("@cya@Seek bar is now shown", Client.CHAT_NONE);
    else Client.displayMessage("@cya@Seek bar is now hidden", Client.CHAT_NONE);
    save();
  }

  public static void toggleShowPlayerControls() {
    SHOW_PLAYER_CONTROLS.put(currentProfile, !SHOW_PLAYER_CONTROLS.get(currentProfile));
    if (SHOW_PLAYER_CONTROLS.get(currentProfile))
      Client.displayMessage("@cya@Player controls are now shown", Client.CHAT_NONE);
    else Client.displayMessage("@cya@Player controls are now hidden", Client.CHAT_NONE);
    save();
  }*/

  public static void toggleInvCount() {
    SHOW_INVCOUNT.put(currentProfile, !SHOW_INVCOUNT.get(currentProfile));

    if (SHOW_INVCOUNT.get(currentProfile)) {
      Client.displayMessage("@cya@Inventory count is now shown", Client.CHAT_NONE);
    } else {
      Client.displayMessage("@cya@Inventory count is now hidden", Client.CHAT_NONE);
    }

    save();
  }

  public static void toggleBuffs() {
    SHOW_BUFFS.put(currentProfile, !SHOW_BUFFS.get(currentProfile));

    if (SHOW_BUFFS.get(currentProfile)) {
      Client.displayMessage("@cya@Combat (de)buffs and cooldowns are now shown", Client.CHAT_NONE);
    } else {
      Client.displayMessage("@cya@Combat (de)buffs and cooldowns are now hidden", Client.CHAT_NONE);
    }

    save();
  }

  public static void toggleHpOverlay() {
    SHOW_HP_OVERLAY.put(currentProfile, !SHOW_HP_OVERLAY.get(currentProfile));

    if (SHOW_HP_OVERLAY.get(currentProfile)) {
      Client.displayMessage("@cya@HP is now shown", Client.CHAT_NONE);
    } else {
      Client.displayMessage("@cya@HP is now hidden", Client.CHAT_NONE);
    }

    save();
  }

  public static void toggleShowHitbox() {
    SHOW_HITBOX.put(currentProfile, !SHOW_HITBOX.get(currentProfile));

    if (SHOW_HITBOX.get(currentProfile)) {
      Client.displayMessage("@cya@Hitboxes are now shown", Client.CHAT_NONE);
    } else {
      Client.displayMessage("@cya@Hitboxes are now hidden", Client.CHAT_NONE);
    }

    save();
  }

  public static void toggleShowNPCNameOverlay() {
    SHOW_NPC_NAME_OVERLAY.put(currentProfile, !SHOW_NPC_NAME_OVERLAY.get(currentProfile));
    if (SHOW_NPC_NAME_OVERLAY.get(currentProfile)) {
      Client.displayMessage("@cya@NPC names are now shown", Client.CHAT_NONE);
    } else {
      Client.displayMessage("@cya@NPC names are is now hidden", Client.CHAT_NONE);
    }

    save();
  }

  public static void toggleShowPlayerNameOverlay() {
    SHOW_PLAYER_NAME_OVERLAY.put(currentProfile, !SHOW_PLAYER_NAME_OVERLAY.get(currentProfile));

    if (SHOW_PLAYER_NAME_OVERLAY.get(currentProfile)) {
      Client.displayMessage("@cya@Player names are now shown", Client.CHAT_NONE);
    } else {
      Client.displayMessage("@cya@Player names are now hidden", Client.CHAT_NONE);
    }

    save();
  }

  public static void toggleExtendIdsOverlay() {
    EXTEND_IDS_OVERLAY.put(currentProfile, !EXTEND_IDS_OVERLAY.get(currentProfile));

    if (EXTEND_IDS_OVERLAY.get(currentProfile)) {
      Client.displayMessage("@cya@IDs are now shown", Client.CHAT_NONE);
    } else {
      Client.displayMessage("@cya@IDs are now hidden", Client.CHAT_NONE);
    }

    save();
  }

  public static void toggleTraceObjectInfo() {
    TRACE_OBJECT_INFO.put(currentProfile, !TRACE_OBJECT_INFO.get(currentProfile));

    if (TRACE_OBJECT_INFO.get(currentProfile)) {
      Client.displayMessage("@cya@Object info now shown", Client.CHAT_NONE);
    } else {
      Client.displayMessage("@cya@Object info now hidden", Client.CHAT_NONE);
    }

    save();
  }

  public static void toggleDebug() {
    DEBUG.put(currentProfile, !DEBUG.get(currentProfile));

    if (DEBUG.get(currentProfile)) {
      Client.displayMessage("@cya@Debug mode is on", Client.CHAT_NONE);
    } else {
      Client.displayMessage("@cya@Debug mode is off", Client.CHAT_NONE);
    }

    save();
  }

  public static void toggleInventoryFullAlert() {
    INVENTORY_FULL_ALERT.put(currentProfile, !INVENTORY_FULL_ALERT.get(currentProfile));

    if (INVENTORY_FULL_ALERT.get(currentProfile)) {
      Client.displayMessage("@cya@Inventory full alert is now on", Client.CHAT_NONE);
    } else {
      Client.displayMessage("@cya@Inventory full alert is now off", Client.CHAT_NONE);
    }

    save();
  }

  public static void toggleTwitchHide() {
    TWITCH_HIDE_CHAT.put(currentProfile, !TWITCH_HIDE_CHAT.get(currentProfile));

    if (TWITCH_HIDE_CHAT.get(currentProfile)) {
      Client.displayMessage("@cya@Twitch chat is now hidden", Client.CHAT_NONE);
    } else {
      Client.displayMessage("@cya@Twitch chat is now shown", Client.CHAT_NONE);
    }

    save();
  }

  public static void toggleTrackpadRotation() {
    SHIFT_SCROLL_CAMERA_ROTATION.put(
        currentProfile, !SHIFT_SCROLL_CAMERA_ROTATION.get(currentProfile));

    if (SHIFT_SCROLL_CAMERA_ROTATION.get(currentProfile)) {
      Client.displayMessage("@cya@Trackpad Camera Rotation is now enabled", Client.CHAT_NONE);
    } else {
      Client.displayMessage("@cya@Trackpad Camera Rotation is now disabled", Client.CHAT_NONE);
    }

    save();
  }

  public static void toggleGoalBar() {
    SHOW_XP_BAR.put(currentProfile, !SHOW_XP_BAR.get(currentProfile));
    if (SHOW_XP_BAR.get(currentProfile))
      Client.displayMessage("@cya@Goal Bar is now shown", Client.CHAT_NONE);
    else Client.displayMessage("@cya@Goal Bar is now hidden", Client.CHAT_NONE);
    save();
  }

  public static void toggleGoalBarPin() {
    SHOW_XP_BAR.put(currentProfile, true);
    if (!XPBar.pinnedBar) Client.displayMessage("@cya@Goal Bar is now pinned", Client.CHAT_NONE);
    else Client.displayMessage("@cya@Goal Bar is now unpinned", Client.CHAT_NONE);
    XPBar.pinnedBar = !XPBar.pinnedBar;
    save();
  }

  public static void toggleWikiHbar() {
    WIKI_LOOKUP_ON_HBAR.put(currentProfile, !WIKI_LOOKUP_ON_HBAR.get(currentProfile));

    if (WIKI_LOOKUP_ON_HBAR.get(currentProfile)) {
      Client.displayMessage("@cya@Wiki button in Hbar now shown", Client.CHAT_NONE);
    } else {
      Client.displayMessage("@cya@Wiki button in Hbar now hidden", Client.CHAT_NONE);
    }

    save();
  }

  public static void checkSoftwareCursor() {
    // Only load the software cursor if setting is enabled and
    // the game has begun rendering post-loading
    if (SOFTWARE_CURSOR.get(currentProfile) && ScaledWindow.getInstance().isViewportLoaded()) {
      ScaledWindow.getInstance()
          .setCursor(
              ScaledWindow.getInstance()
                  .getToolkit()
                  .createCustomCursor(
                      new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
                      new Point(0, 0),
                      "null"));
    } else {
      ScaledWindow.getInstance().setCursor(Cursor.getDefaultCursor());
    }
  }

  /**
   * Gets the String value of a Properties object for the specified key. If no value is defined for
   * that key, it returns the specified default value.
   *
   * @param props the Properties object to read
   * @param key the name of the property to lookup
   * @param defaultProp the default String value of the specified property
   * @return a String value corresponding to the specified property
   */
  private static String getPropString(Properties props, String key, String defaultProp) {
    String value = props.getProperty(key);
    if (value == null) {
      return defaultProp;
    }

    return value;
  }

  /**
   * Gets the ArrayList<String> value of a Properties object for the specified key. If no value is
   * defined for that key, it returns the specified default value.
   *
   * @param props the Properties object to read
   * @param key the name of the property to lookup
   * @param defaultProp the default ArrayList<String> value of the specified property
   * @return an ArrayList<String> value corresponding to the specified property
   */
  private static ArrayList<String> getPropArrayListString(
      Properties props, String key, ArrayList<String> defaultProp) {
    String valueString = props.getProperty(key);
    if (valueString == null) {
      return defaultProp;
    }

    return new ArrayList<>(Arrays.asList(valueString.split(",")));
  }

  /**
   * Gets the Integer value of a Properties object for the specified key. If no value is defined for
   * that key, it returns the specified default value.
   *
   * @param props the Properties object to read
   * @param key the name of the property to lookup
   * @param defaultProp the default Integer value of the specified property
   * @return a Integer value corresponding to the specified property
   */
  private static int getPropInt(Properties props, String key, int defaultProp) {
    String value = props.getProperty(key);
    if (value == null) return defaultProp;

    try {
      return Integer.parseInt(value);
    } catch (Exception e) {
      return defaultProp;
    }
  }

  /**
   * Gets the Float value of a Properties object for the specified key. If no value is defined for
   * that key, it returns the specified default value.
   *
   * @param props the Properties object to read
   * @param key the name of the property to lookup
   * @param defaultProp the default Float value of the specified property
   * @return a Float value corresponding to the specified property
   */
  private static float getPropFloat(Properties props, String key, float defaultProp) {
    String value = props.getProperty(key);
    if (value == null) return defaultProp;

    try {
      return Float.parseFloat(value);
    } catch (Exception e) {
      return defaultProp;
    }
  }

  /**
   * Gets the Boolean value of a Properties object for the specified key. If no value is defined for
   * that key, it returns the specified default value.
   *
   * @param props the Properties object to read
   * @param key the name of the property to lookup
   * @param defaultProp the default Boolean value of the specified property
   * @return a Boolean value corresponding to the specified property
   */
  private static boolean getPropBoolean(Properties props, String key, boolean defaultProp) {
    String value = props.getProperty(key);
    if (value == null) return defaultProp;

    try {
      return Boolean.parseBoolean(value);
    } catch (Exception e) {
      return defaultProp;
    }
  }

  /**
   * Returns if it is recommended for the OS to use system notifications.
   *
   * @return if it is recommended to use system notifications
   */
  public static boolean isRecommendedToUseSystemNotifs() {
    // Users on Windows 8.1+ are recommend to set USE_SYSTEM_NOTIFICATIONS = true
    if (Util.isWindowsOS()) {
      return Util.isModernWindowsOS();
    } else { // Linux, macOS, etc.
      return NotificationsHandler.hasNotifySend;
    }
  }

  public static Properties loadProps() {
    Properties props = new Properties();

    try {
      File configFile = new File(Dir.JAR + "/config.ini");
      if (!configFile.isDirectory()) {
        if (!configFile.exists()) {
          definePresets(props);
          successfullyInitted = true;
          save("custom");
        }
      }

      FileInputStream in = new FileInputStream(Dir.JAR + "/config.ini");
      props.load(in);
      in.close();
    } catch (Exception e) {
      Logger.Warn("Error loading config.ini");
      e.printStackTrace();
    }
    return props;
  }

  private static int getPropIntForKeyModifier(KeybindSet kbs) {
    switch (kbs.modifier) {
      case NONE:
        return 0;
      case CTRL:
        return 1;
      case ALT:
        return 2;
      case SHIFT:
        return 3;
      default:
        Logger.Error("Tried to save a keybind with an invalid modifier!");
        return 0;
    }
  }

  private static KeybindSet.KeyModifier getKeyModifierFromString(String savedKeybindSet) {
    switch (Integer.parseInt(savedKeybindSet.substring(0, 1))) {
      case 0:
        return KeybindSet.KeyModifier.NONE;
      case 1:
        return KeybindSet.KeyModifier.CTRL;
      case 2:
        return KeybindSet.KeyModifier.ALT;
      case 3:
        return KeybindSet.KeyModifier.SHIFT;
      default:
        Logger.Error("Unrecognized KeyModifier code");
        return KeybindSet.KeyModifier.NONE;
    }
  }

  public static void loadKeybinds(Properties props) {
    if (props == null) {
      props = loadProps();
    }
    for (KeybindSet kbs : KeyboardHandler.keybindSetList) {
      String keybindCombo =
          getPropString(
              props, "key_" + kbs.commandName, "" + getPropIntForKeyModifier(kbs) + "*" + kbs.key);
      kbs.modifier = getKeyModifierFromString(keybindCombo);
      kbs.key = Integer.parseInt(keybindCombo.substring(2));
    }
  }

  /** Writes all setting variables to config.ini. */
  public static void save() {
    updateInjectedVariables(); // TODO remove this function
    if (currentProfile.equals("custom")) {
      save("custom");
    }
  }

  public static void save(String preset) {
    if (!successfullyInitted) {
      Logger.Warn(
          "Prevented erroneous save, please report this along with the RSC× log file, set to debug logging mode");
      return;
    }
    try {
      Properties props = new Properties();

      updateInjectedVariables(); // TODO remove this function

      //// general
      props.setProperty("custom_client_size", Boolean.toString(CUSTOM_CLIENT_SIZE.get(preset)));
      props.setProperty("custom_client_size_x", Integer.toString(CUSTOM_CLIENT_SIZE_X.get(preset)));
      props.setProperty("custom_client_size_y", Integer.toString(CUSTOM_CLIENT_SIZE_Y.get(preset)));
      props.setProperty(
          "enable_window_scaling", Boolean.toString(SCALED_CLIENT_WINDOW.get(preset)));
      props.setProperty("scaling_algorithm", Integer.toString(SCALING_ALGORITHM.get(preset)));
      props.setProperty(
          "integer_scaling_factor", Integer.toString(INTEGER_SCALING_FACTOR.get(preset)));
      props.setProperty(
          "bilinear_scaling_factor", Float.toString(BILINEAR_SCALING_FACTOR.get(preset)));
      props.setProperty(
          "bicubic_scaling_factor", Float.toString(BICUBIC_SCALING_FACTOR.get(preset)));
      props.setProperty("check_updates", Boolean.toString(CHECK_UPDATES.get(preset)));
      props.setProperty("use_dark_flatlaf", Boolean.toString(USE_DARK_FLATLAF.get(preset)));
      props.setProperty("use_nimbus_theme", Boolean.toString(USE_NIMBUS_THEME.get(preset)));
      props.setProperty(
          "welcome_enabled", Boolean.toString(REMIND_HOW_TO_OPEN_SETTINGS.get(preset)));
      props.setProperty("combat_menu", Boolean.toString(COMBAT_MENU_SHOWN.get(preset)));
      props.setProperty("combat_menu_hidden", Boolean.toString(COMBAT_MENU_HIDDEN.get(preset)));
      props.setProperty("center_xpdrops", Boolean.toString(CENTER_XPDROPS.get(preset)));
      props.setProperty("inventory_full_alert", Boolean.toString(INVENTORY_FULL_ALERT.get(preset)));
      /*props.setProperty("name_patch_type", Integer.toString(NAME_PATCH_TYPE.get(preset)));
      props.setProperty("command_patch_quest", Boolean.toString(COMMAND_PATCH_QUEST.get(preset)));
      props.setProperty(
              "keep_scrollbar_pos_magic_prayer",
              Boolean.toString(KEEP_SCROLLBAR_POS_MAGIC_PRAYER.get(preset)));*/
      props.setProperty("bypass_attack", Boolean.toString(ATTACK_ALWAYS_LEFT_CLICK.get(preset)));
      props.setProperty("sort_friends", Boolean.toString(SORT_FRIENDS.get(preset)));
      props.setProperty("hide_roofs", Boolean.toString(HIDE_ROOFS.get(preset)));
      props.setProperty(
          "disable_underground_lighting",
          Boolean.toString(DISABLE_UNDERGROUND_LIGHTING.get(preset)));
      props.setProperty("camera_zoomable", Boolean.toString(CAMERA_ZOOMABLE.get(preset)));
      props.setProperty("camera_rotatable", Boolean.toString(CAMERA_ROTATABLE.get(preset)));
      props.setProperty("camera_movable", Boolean.toString(CAMERA_MOVABLE.get(preset)));
      props.setProperty(
          "camera_movable_relative", Boolean.toString(CAMERA_MOVABLE_RELATIVE.get(preset)));
      props.setProperty("colorize", Boolean.toString(COLORIZE_CONSOLE_TEXT.get(preset)));
      props.setProperty("fov", Integer.toString(FOV.get(preset)));
      props.setProperty("fps_limit_enabled", Boolean.toString(FPS_LIMIT_ENABLED.get(preset)));
      props.setProperty("fps_limit", Integer.toString(FPS_LIMIT.get(preset)));
      props.setProperty("software_cursor", Boolean.toString(SOFTWARE_CURSOR.get(preset)));
      props.setProperty(
          "shift_scroll_camera_rotation",
          Boolean.toString(SHIFT_SCROLL_CAMERA_ROTATION.get(preset)));
      props.setProperty(
          "trackpad_rotation_sensitivity",
          Integer.toString(TRACKPAD_ROTATION_SENSITIVITY.get(preset)));
      props.setProperty("auto_screenshot", Boolean.toString(AUTO_SCREENSHOT.get(preset)));
      props.setProperty("view_distance", Integer.toString(VIEW_DISTANCE.get(preset)));
      props.setProperty("patch_gender", Boolean.toString(PATCH_GENDER.get(preset)));
      props.setProperty(
          "patch_hbar_512_last_pixel", Boolean.toString(PATCH_HBAR_512_LAST_PIXEL.get(preset)));
      props.setProperty("log_verbosity", Integer.toString(LOG_VERBOSITY.get(preset)));
      props.setProperty("log_show_timestamps", Boolean.toString(LOG_SHOW_TIMESTAMPS.get(preset)));
      props.setProperty("log_show_level", Boolean.toString(LOG_SHOW_LEVEL.get(preset)));
      props.setProperty("log_force_timestamps", Boolean.toString(LOG_FORCE_TIMESTAMPS.get(preset)));
      props.setProperty("log_force_level", Boolean.toString(LOG_FORCE_LEVEL.get(preset)));
      props.setProperty("prefers_xdg_open", Boolean.toString(PREFERS_XDG_OPEN.get(preset)));

      //// overlays
      props.setProperty("show_statusdisplay", Boolean.toString(SHOW_HP_OVERLAY.get(preset)));
      props.setProperty("show_buffs", Boolean.toString(SHOW_BUFFS.get(preset)));
      props.setProperty(
          "show_last_menu_action", Boolean.toString(SHOW_LAST_MENU_ACTION.get(preset)));
      props.setProperty("show_mouse_tooltip", Boolean.toString(SHOW_MOUSE_TOOLTIP.get(preset)));
      props.setProperty(
          "show_extended_tooltip", Boolean.toString(SHOW_EXTENDED_TOOLTIP.get(preset)));
      props.setProperty("show_invcount", Boolean.toString(SHOW_INVCOUNT.get(preset)));
      props.setProperty(
          "show_rsctimes_buttons", Boolean.toString(SHOW_RSCTIMES_BUTTONS.get(preset)));
      props.setProperty(
          "rsctimes_buttons_functional", Boolean.toString(RSCTIMES_BUTTONS_FUNCTIONAL.get(preset)));
      props.setProperty(
          "wiki_lookup_on_magic_book", Boolean.toString(WIKI_LOOKUP_ON_MAGIC_BOOK.get(preset)));
      props.setProperty("wiki_lookup_on_hbar", Boolean.toString(WIKI_LOOKUP_ON_HBAR.get(preset)));
      props.setProperty(
          "motivational_quotes_button", Boolean.toString(MOTIVATIONAL_QUOTES_BUTTON.get(preset)));
      props.setProperty("toggle_xp_bar_on_stats_button", Boolean.toString(TOGGLE_XP_BAR_ON_STATS_BUTTON.get(preset)));
      props.setProperty("show_iteminfo", Boolean.toString(SHOW_ITEM_GROUND_OVERLAY.get(preset)));
      props.setProperty("show_playerinfo", Boolean.toString(SHOW_PLAYER_NAME_OVERLAY.get(preset)));
      props.setProperty("show_friendinfo", Boolean.toString(SHOW_FRIEND_NAME_OVERLAY.get(preset)));
      props.setProperty("show_npcinfo", Boolean.toString(SHOW_NPC_NAME_OVERLAY.get(preset)));
      props.setProperty("extend_idsinfo", Boolean.toString(EXTEND_IDS_OVERLAY.get(preset)));
      props.setProperty("trace_objectinfo", Boolean.toString(TRACE_OBJECT_INFO.get(preset)));
      props.setProperty("show_combat_info", Boolean.toString(SHOW_COMBAT_INFO.get(preset)));
      props.setProperty("show_player_position", Boolean.toString(SHOW_PLAYER_POSITION.get(preset)));
      props.setProperty("hide_fps", Boolean.toString(HIDE_FPS.get(preset)));
      props.setProperty("use_percentage", Boolean.toString(NPC_HEALTH_SHOW_PERCENTAGE.get(preset)));
      props.setProperty("show_hitbox", Boolean.toString(SHOW_HITBOX.get(preset)));
      props.setProperty(
          "show_food_heal_overlay", Boolean.toString(SHOW_FOOD_HEAL_OVERLAY.get(preset)));
      props.setProperty(
          "show_time_until_hp_regen", Boolean.toString(SHOW_TIME_UNTIL_HP_REGEN.get(preset)));
      props.setProperty("indicators", Boolean.toString(LAG_INDICATOR.get(preset)));
      props.setProperty("show_xp_bar", Boolean.toString(SHOW_XP_BAR.get(preset)));
      props.setProperty("debug", Boolean.toString(DEBUG.get(preset)));
      props.setProperty("exception_handler", Boolean.toString(EXCEPTION_HANDLER.get(preset)));
      props.setProperty("highlighted_items", Util.joinAsString(",", HIGHLIGHTED_ITEMS.get(preset)));
      props.setProperty("blocked_items", Util.joinAsString(",", BLOCKED_ITEMS.get(preset)));

      //// notifications
      props.setProperty("tray_notifs", Boolean.toString(TRAY_NOTIFS.get(preset)));
      props.setProperty("tray_notifs_always", Boolean.toString(TRAY_NOTIFS_ALWAYS.get(preset)));
      props.setProperty("notification_sounds", Boolean.toString(NOTIFICATION_SOUNDS.get(preset)));
      props.setProperty("sound_notifs_always", Boolean.toString(SOUND_NOTIFS_ALWAYS.get(preset)));
      props.setProperty(
          "use_system_notifications", Boolean.toString(USE_SYSTEM_NOTIFICATIONS.get(preset)));
      props.setProperty("pm_notifications", Boolean.toString(PM_NOTIFICATIONS.get(preset)));
      props.setProperty("trade_notifications", Boolean.toString(TRADE_NOTIFICATIONS.get(preset)));
      props.setProperty(
          "under_attack_notifications", Boolean.toString(UNDER_ATTACK_NOTIFICATIONS.get(preset)));
      props.setProperty("low_hp_notifications", Boolean.toString(LOW_HP_NOTIFICATIONS.get(preset)));
      props.setProperty("low_hp_notif_value", Integer.toString(LOW_HP_NOTIF_VALUE.get(preset)));
      props.setProperty(
          "highlighted_item_notifications",
          Boolean.toString(HIGHLIGHTED_ITEM_NOTIFICATIONS.get(preset)));
      props.setProperty(
          "highlighted_item_notif_value",
          Integer.toString(HIGHLIGHTED_ITEM_NOTIF_VALUE.get(preset)));

      //// streaming
      /*
      props.setProperty("twitch_enabled", Boolean.toString(TWITCH_CHAT_ENABLED.get(preset)));
      props.setProperty("twitch_hide", Boolean.toString(TWITCH_HIDE_CHAT.get(preset)));
      props.setProperty("twitch_channel", TWITCH_CHANNEL.get(preset));
      props.setProperty("twitch_oauth", TWITCH_OAUTH.get(preset));
      props.setProperty("twitch_username", TWITCH_USERNAME.get(preset));
      props.setProperty("show_logindetails", Boolean.toString(SHOW_LOGIN_IP_ADDRESS.get(preset)));
      */
      props.setProperty("save_logininfo", Boolean.toString(SAVE_LOGININFO.get(preset)));
      props.setProperty("start_loginscreen", Boolean.toString(START_LOGINSCREEN.get(preset)));
      props.setProperty("speedrun_active", Boolean.toString(SPEEDRUNNER_MODE_ACTIVE.get(preset)));

      //// replay
      /*
      props.setProperty("record_kb_mouse", Boolean.toString(RECORD_KB_MOUSE.get(preset)));
      props.setProperty("parse_opcodes", Boolean.toString(PARSE_OPCODES.get(preset)));
      props.setProperty("fast_disconnect", Boolean.toString(FAST_DISCONNECT.get(preset)));
      props.setProperty("record_automatically", Boolean.toString(RECORD_AUTOMATICALLY.get(preset)));
      props.setProperty(
              "hide_private_msgs_replay", Boolean.toString(HIDE_PRIVATE_MSGS_REPLAY.get(preset)));
      props.setProperty("show_seek_bar", Boolean.toString(SHOW_SEEK_BAR.get(preset)));
      props.setProperty("show_player_controls", Boolean.toString(SHOW_PLAYER_CONTROLS.get(preset)));
      props.setProperty(
              "trigger_alerts_replay", Boolean.toString(TRIGGER_ALERTS_REPLAY.get(preset)));
      props.setProperty("replay_base_path", REPLAY_BASE_PATH.get(preset));
      props.setProperty("preferred_date_format", PREFERRED_DATE_FORMAT.get(preset));
      props.setProperty("show_world_column", Boolean.toString(SHOW_WORLD_COLUMN.get(preset)));
      props.setProperty(
              "show_conversion_column", Boolean.toString(SHOW_CONVERSION_COLUMN.get(preset)));
      props.setProperty(
              "show_userfield_column", Boolean.toString(SHOW_USERFIELD_COLUMN.get(preset)));
      */

      //// world urls
      saveWorlds();

      //// presets
      props.setProperty("current_profile", currentProfile);

      //// no gui
      props.setProperty("combat_style", Integer.toString(COMBAT_STYLE.get(preset)));
      /*
      props.setProperty("world", Integer.toString(WORLD.get(preset)));
      */
      // This is set to false, as logically, saving the config would imply this is not a first-run.
      props.setProperty("first_time", Boolean.toString(false));
      props.setProperty("update_confirmation", Boolean.toString(UPDATE_CONFIRMATION.get(preset)));
      /*
      props.setProperty(
              "record_automatically_first_time",
              Boolean.toString(RECORD_AUTOMATICALLY_FIRST_TIME.get(preset)));
      props.setProperty("disassemble", Boolean.toString(DISASSEMBLE.get(preset)));
      props.setProperty("disassemble_directory", DISASSEMBLE_DIRECTORY.get(preset));
       */

      // Keybinds
      for (KeybindSet kbs : KeyboardHandler.keybindSetList) {
        props.setProperty(
            "key_" + kbs.commandName,
            Integer.toString(getPropIntForKeyModifier(kbs)) + "*" + kbs.key);
      }

      // Lvl Goals
      int usernameID = 0;
      for (String username : Client.lvlGoals.keySet()) {
        if (username.equals(XPBar.excludeUsername)) continue;
        for (int skill = 0; skill < Client.NUM_SKILLS; skill++) {
          float lvlgoal = (float) 0;
          try {
            lvlgoal = Client.lvlGoals.get(username)[skill];
          } catch (Exception noGoal) {
          }

          props.setProperty(
              String.format("lvlGoal%02d%03d", skill, usernameID), Float.toString(lvlgoal));
        }
        props.setProperty(String.format("username%d", usernameID), username);
        usernameID++;
      }
      props.setProperty("numberOfGoalers", String.format("%d", usernameID));

      props.setProperty("pinXPBar", Boolean.toString(XPBar.pinnedBar));
      props.setProperty("pinnedSkill", String.format("%d", XPBar.pinnedSkill));

      // World Map
      props.setProperty("worldmap_show_icons", Boolean.toString(WorldMapWindow.showIcons));
      props.setProperty("worldmap_show_labels", Boolean.toString(WorldMapWindow.showLabels));
      props.setProperty("worldmap_show_scenery", Boolean.toString(WorldMapWindow.showScenery));
      props.setProperty(
          "worldmap_show_chunk_grid", Boolean.toString(WorldMapWindow.renderChunkGrid));
      props.setProperty(
          "worldmap_show_other_floors", Boolean.toString(WorldMapWindow.showOtherFloors));

      FileOutputStream out = new FileOutputStream(Dir.JAR + "/config.ini");
      props.store(out, "---rsctimes config---");
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
      Logger.Error("Unable to save settings");
    }
  }

  public static void createNewWorld(int worldNum) {
    WORLD_NAMES.put(worldNum, String.format("World %d", worldNum));
    WORLD_URLS.put(worldNum, "");
    WORLD_PORTS.put(worldNum, 43594);

    String worldFileName =
        String.format(
            "%s%d_%s%s", worldNum < 10 ? "0" : "", worldNum, WORLD_NAMES.get(worldNum), ".ini");
    Properties worldProps = new Properties();

    worldProps.setProperty("name", WORLD_NAMES.get(worldNum));
    worldProps.setProperty("url", WORLD_URLS.get(worldNum));
    worldProps.setProperty("port", WORLD_PORTS.get(worldNum).toString());

    try {
      FileOutputStream out = new FileOutputStream(new File(Dir.WORLDS, worldFileName));
      worldProps.store(out, "---rscplus world config---");
      out.close();
    } catch (Exception e) {
      Logger.Warn("Error saving World config for " + worldFileName);
    }

    WORLD_FILE_PATHS.put(worldNum, new File(Dir.WORLDS, worldFileName).getAbsolutePath());
  }

  public static void removeWorld(int worldNum) {
    try {
      File oldFile = new File(WORLD_FILE_PATHS.get(worldNum));
      Logger.Info("Removed old file: " + oldFile.getName());
      oldFile.delete();
    } catch (Exception e) {
      Logger.Warn("Error deleting old file: " + WORLD_FILE_PATHS.get(worldNum));
    }

    int initialSize = WORLD_NAMES.size();
    for (int i = worldNum + 1; i <= initialSize; i++) {
      WORLD_NAMES.put(i - 1, WORLD_NAMES.remove(i));
      WORLD_URLS.put(i - 1, WORLD_URLS.remove(i));
      WORLD_PORTS.put(i - 1, WORLD_PORTS.remove(i));
      WORLD_FILE_PATHS.put(i - 1, WORLD_FILE_PATHS.remove(i));
    }
    WORLD_NAMES.remove(initialSize);
    WORLD_URLS.remove(initialSize);
    WORLD_PORTS.remove(initialSize);
    WORLD_FILE_PATHS.remove(initialSize);
    Settings.WORLDS_TO_DISPLAY--;
    Launcher.getConfigWindow().synchronizeWorldTab();
    saveWorlds();
  }

  public static void initWorlds() {
    File[] fList = new File(Dir.WORLDS).listFiles();

    // Sorts alphabetically
    Arrays.sort(
        fList,
        new Comparator<File>() {
          @Override
          public int compare(File o1, File o2) {
            return o1.getName().compareTo(o2.getName());
          }
        });

    int i = 1;
    if (fList != null) {
      for (File worldFile : fList) {
        if (!worldFile.isDirectory() && !worldFile.getName().equals(".DS_Store")) {
          Properties worldProps = new Properties();
          try {
            FileInputStream in = new FileInputStream(worldFile);
            worldProps.load(in);
            in.close();

            WORLD_FILE_PATHS.put(i, worldFile.getAbsolutePath());
            WORLD_NAMES.put(i, worldProps.getProperty("name"));
            WORLD_URLS.put(i, worldProps.getProperty("url"));
            WORLD_PORTS.put(i, Integer.parseInt(worldProps.getProperty("port")));
            // TODO?
            /*WORLD_SERVER_TYPES.put(
                i, Integer.parseInt((String) worldProps.getOrDefault("servertype", "1")));
            WORLD_RSA_PUB_KEYS.put(i, worldProps.getProperty("rsa_pub_key"));
            WORLD_RSA_EXPONENTS.put(i, worldProps.getProperty("rsa_exponent"));*/

            i++;
          } catch (Exception e) {
            Logger.Warn("Error loading World config for " + worldFile.getAbsolutePath());
          }
        }
      }
    }

    if (i > 1) {
      noWorldsConfigured = false;
      WORLDS_TO_DISPLAY = i - 1;
    } else {
      createNewWorld(1);
      WORLDS_TO_DISPLAY = 1;
    }
  }

  public static void saveWorlds() {
    // TODO: it would be nice if we only saved a new file if information is different
    for (int i = 1; i <= WORLD_NAMES.size(); i++) {
      String worldFileName =
          String.format("%s%d_%s%s", i < 10 ? "0" : "", i, WORLD_NAMES.get(i), ".ini");
      Properties worldProps = new Properties();

      worldProps.setProperty("name", WORLD_NAMES.get(i));
      worldProps.setProperty("url", WORLD_URLS.get(i));
      worldProps.setProperty("port", WORLD_PORTS.get(i).toString());

      try {
        FileOutputStream out = new FileOutputStream(new File(Dir.WORLDS, worldFileName));
        worldProps.store(out, "---rscplus world config---");
        out.close();
      } catch (Exception e) {
        Logger.Warn("Error saving World config for " + worldFileName);
      }
      try {
        File oldFile = new File(WORLD_FILE_PATHS.get(i));
        if (!worldFileName.equals(oldFile.getName())) {
          if (!oldFile.delete()) {
            Logger.Warn(
                String.format("Error deleting old file %d: %s", i, oldFile.getAbsolutePath()));
          }
          WORLD_FILE_PATHS.put(i, new File(Dir.WORLDS, worldFileName).getAbsolutePath());
        }
      } catch (Exception e) {
        Logger.Warn(String.format("Error deleting old file %d: %s", i, WORLD_FILE_PATHS.get(i)));
      }
    }
  }

  /** Contains variables which store folder paths. */
  public static class Dir {

    public static String JAR;
    public static String DUMP;
    public static String SCREENSHOT;
    public static String REPLAY;
    public static String WORLDS;
  }

  public static void updateInjectedVariables() {
    // TODO: get rid of these variables and this function if possible
    COMBAT_STYLE_INT = COMBAT_STYLE.get(currentProfile);
    HIDE_ROOFS_BOOL = HIDE_ROOFS.get(currentProfile);
    DISABLE_UNDERGROUND_LIGHTING_BOOL = DISABLE_UNDERGROUND_LIGHTING.get(currentProfile);
    COMBAT_MENU_SHOWN_BOOL = COMBAT_MENU_SHOWN.get(currentProfile);
    COMBAT_MENU_HIDDEN_BOOL = COMBAT_MENU_HIDDEN.get(currentProfile);
    CAMERA_ZOOMABLE_BOOL = CAMERA_ZOOMABLE.get(currentProfile);
    CAMERA_ROTATABLE_BOOL = CAMERA_ROTATABLE.get(currentProfile);
    CAMERA_MOVABLE_BOOL = CAMERA_MOVABLE.get(currentProfile);
  }

  public static void outputInjectedVariables() {
    // TODO: get rid of these variables and this function if possible
    COMBAT_STYLE.put(currentProfile, COMBAT_STYLE_INT);
  }
}
