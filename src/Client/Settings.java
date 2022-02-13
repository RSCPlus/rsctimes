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
import Game.XPBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Properties;

public class Settings {
  public static boolean versionCheckRequired = true;
  public static int javaVersion = 0;
  public static final double VERSION_NUMBER = 20210811.150444;
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
  public static HashMap<String, Boolean> CHECK_UPDATES = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> COLORIZE_CONSOLE_TEXT = new HashMap<String, Boolean>();
  public static HashMap<String, Integer> LOG_VERBOSITY = new HashMap<String, Integer>();
  public static HashMap<String, Boolean> LOG_SHOW_TIMESTAMPS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> LOG_SHOW_LEVEL = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> LOG_FORCE_TIMESTAMPS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> LOG_FORCE_LEVEL = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> PREFERS_XDG_OPEN = new HashMap<String, Boolean>();

  //// Notifications
  public static HashMap<String, Boolean> TRAY_NOTIFS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> TRAY_NOTIFS_ALWAYS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> NOTIFICATION_SOUNDS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> SOUND_NOTIFS_ALWAYS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> USE_SYSTEM_NOTIFICATIONS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> FATIGUE_NOTIFICATIONS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> PM_NOTIFICATIONS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> TRADE_NOTIFICATIONS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> UNDER_ATTACK_NOTIFICATIONS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> LOGOUT_NOTIFICATIONS = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> LOW_HP_NOTIFICATIONS = new HashMap<String, Boolean>();
  public static HashMap<String, Integer> LOW_HP_NOTIF_VALUE = new HashMap<String, Integer>();
  public static HashMap<String, Boolean> HIGHLIGHTED_ITEM_NOTIFICATIONS =
          new HashMap<String, Boolean>();
  public static HashMap<String, Integer> HIGHLIGHTED_ITEM_NOTIF_VALUE =
          new HashMap<String, Integer>();

  //// OVERLAYS
  public static HashMap<String, Boolean> SHOW_XP_BAR = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> CENTER_XPDROPS = new HashMap<String, Boolean>();

  //// world list
  public static HashMap<Integer, String> WORLD_URLS = new HashMap<Integer, String>();
  public static HashMap<Integer, String> WORLD_NAMES = new HashMap<Integer, String>();
  public static HashMap<Integer, Integer> WORLD_PORTS = new HashMap<Integer, Integer>();
  public static HashMap<Integer, String> WORLD_FILE_PATHS = new HashMap<Integer, String>();
  public static int WORLDS_TO_DISPLAY = 5;
  public static boolean noWorldsConfigured = true;

  //// nogui
  public static HashMap<String, Integer> WORLD = new HashMap<String, Integer>();
  public static HashMap<String, Boolean> FIRST_TIME = new HashMap<String, Boolean>();
  public static HashMap<String, Boolean> UPDATE_CONFIRMATION = new HashMap<String, Boolean>();
  
 //these are variables that are injected with JClassPatcher
 public static int COMBAT_STYLE_INT = Client.COMBAT_AGGRESSIVE;
 public static boolean HIDE_ROOFS_BOOL = false;
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
        Client.xpGoals.put(goalerUsernames[usernameID], new Integer[Client.NUM_SKILLS]);
        Client.lvlGoals.put(goalerUsernames[usernameID], new Float[Client.NUM_SKILLS]);
        for (int skill = 0; skill < Client.NUM_SKILLS; skill++) {
          Client.xpGoals.get(goalerUsernames[usernameID])[skill] =
                  getPropInt(props, String.format("xpGoal%02d%03d", skill, usernameID), 0);
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
    CUSTOM_CLIENT_SIZE_X.put("vanilla_resizable", 512);
    CUSTOM_CLIENT_SIZE_X.put("lite", 512);
    CUSTOM_CLIENT_SIZE_X.put("default", 512);
    CUSTOM_CLIENT_SIZE_X.put("heavy", 512);
    CUSTOM_CLIENT_SIZE_X.put("all", 512);
    CUSTOM_CLIENT_SIZE_X.put(
            "custom", getPropInt(props, "custom_client_size_x", CUSTOM_CLIENT_SIZE_X.get("default")));

    CUSTOM_CLIENT_SIZE_Y.put("vanilla", 334);
    CUSTOM_CLIENT_SIZE_Y.put("vanilla_resizable", 334);
    CUSTOM_CLIENT_SIZE_Y.put("lite", 334);
    CUSTOM_CLIENT_SIZE_Y.put("default", 334);
    CUSTOM_CLIENT_SIZE_Y.put("heavy", 334);
    CUSTOM_CLIENT_SIZE_Y.put("all", 334);
    CUSTOM_CLIENT_SIZE_Y.put(
            "custom", getPropInt(props, "custom_client_size_y", CUSTOM_CLIENT_SIZE_Y.get("default")));

    CHECK_UPDATES.put("vanilla", true);
    CHECK_UPDATES.put("vanilla_resizable", true);
    CHECK_UPDATES.put("lite", true);
    CHECK_UPDATES.put("default", true);
    CHECK_UPDATES.put("heavy", true);
    CHECK_UPDATES.put("all", true);
    CHECK_UPDATES.put(
            "custom", getPropBoolean(props, "check_updates", CHECK_UPDATES.get("default")));

    COLORIZE_CONSOLE_TEXT.put("vanilla", true);
    COLORIZE_CONSOLE_TEXT.put("vanilla_resizable", true);
    COLORIZE_CONSOLE_TEXT.put("lite", true);
    COLORIZE_CONSOLE_TEXT.put("default", true);
    COLORIZE_CONSOLE_TEXT.put("heavy", true);
    COLORIZE_CONSOLE_TEXT.put("all", true);
    COLORIZE_CONSOLE_TEXT.put(
            "custom", getPropBoolean(props, "colorize", COLORIZE_CONSOLE_TEXT.get("default")));


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
            "custom", getPropBoolean(props, "under_attack_notifications", UNDER_ATTACK_NOTIFICATIONS.get("default")));

    LOGOUT_NOTIFICATIONS.put("vanilla", false);
    LOGOUT_NOTIFICATIONS.put("vanilla_resizable", false);
    LOGOUT_NOTIFICATIONS.put("lite", false);
    LOGOUT_NOTIFICATIONS.put("default", true);
    LOGOUT_NOTIFICATIONS.put("heavy", true);
    LOGOUT_NOTIFICATIONS.put("all", true);
    LOGOUT_NOTIFICATIONS.put(
            "custom",
            getPropBoolean(props, "logout_notifications", LOGOUT_NOTIFICATIONS.get("default")));

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
    
    FATIGUE_NOTIFICATIONS.put("vanilla", false);
    FATIGUE_NOTIFICATIONS.put("vanilla_resizable", false);
    FATIGUE_NOTIFICATIONS.put("lite", false);
    FATIGUE_NOTIFICATIONS.put("default", true);
    FATIGUE_NOTIFICATIONS.put("heavy", true);
    FATIGUE_NOTIFICATIONS.put("all", true);
    FATIGUE_NOTIFICATIONS.put(
        "custom",
        getPropBoolean(props, "fatigue_notifications", FATIGUE_NOTIFICATIONS.get("default")));

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
    SHOW_XP_BAR.put("vanilla", false);
    SHOW_XP_BAR.put("vanilla_resizable", false);
    SHOW_XP_BAR.put("lite", false);
    SHOW_XP_BAR.put("default", true);
    SHOW_XP_BAR.put("heavy", true);
    SHOW_XP_BAR.put("all", true);
    SHOW_XP_BAR.put("custom", getPropBoolean(props, "show_xp_bar", SHOW_XP_BAR.get("default")));

    CENTER_XPDROPS.put("vanilla", false);
    CENTER_XPDROPS.put("vanilla_resizable", false);
    CENTER_XPDROPS.put("lite", false);
    CENTER_XPDROPS.put("default", false);
    CENTER_XPDROPS.put("heavy", true);
    CENTER_XPDROPS.put("all", true);
    CENTER_XPDROPS.put(
            "custom", getPropBoolean(props, "center_xpdrops", CENTER_XPDROPS.get("default")));

    UPDATE_CONFIRMATION.put("vanilla", false);
    UPDATE_CONFIRMATION.put("vanilla_resizable", false);
    UPDATE_CONFIRMATION.put("lite", false);
    UPDATE_CONFIRMATION.put("default", false);
    UPDATE_CONFIRMATION.put("heavy", false);
    UPDATE_CONFIRMATION.put("all", false);
    UPDATE_CONFIRMATION.put("custom", getPropBoolean(props, "update_confirmation", true));
    
    ////world list
   initWorlds();
   
   WORLD.put("vanilla", 1);
   WORLD.put("vanilla_resizable", 1);
   WORLD.put("lite", 1);
   WORLD.put("default", 1);
   WORLD.put("heavy", 1);
   WORLD.put("all", 1);
   WORLD.put("custom", getPropInt(props, "world", WORLD.get("default")));

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
        // TODO: Renderer.takeScreenshot(false);
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
      case "toggle_colorize":
        // TODO: Settings.toggleColorTerminal();
        return true;
      case "toggle_combat_xp_menu":
        // TODO: Settings.toggleCombatMenuShown();
        return true;
      case "toggle_debug":
        // TODO: Settings.toggleDebug();
        return true;
      case "toggle_inventory_full_alert":
        // TODO: Settings.toggleInventoryFullAlert();
        return true;
      case "toggle_food_heal_overlay":
        // TODO: Settings.toggleFoodOverlay();
        return true;
      case "toggle_friend_name_overlay":
        // TOOD: Settings.toggleShowFriendNameOverlay();
        return true;
      case "toggle_buffs_display":
        // TODO: Settings.toggleBuffs();
        return true;
      case "toggle_hp_display":
        // TODO: Settings.toggleHpPrayerFatigueOverlay();
        return true;
      case "toggle_position_overlay":
        // TODO: Settings.togglePosition();
        return true;
      case "toggle_retro_fps_overlay":
        // TODO: ? Settings.toggleRetroFPS();
        return true;
      case "toggle_inven_count_overlay":
        // TODO: Settings.toggleInvCount();
        return true;
      case "toggle_ipdns":
        // TODO: Settings.toggleShowLoginIpAddress();
        return true;
      case "toggle_item_overlay":
        // TODO: Settings.toggleShowItemGroundOverlay();
        return true;
      case "toggle_hitboxes":
        // TODO: Settings.toggleShowHitbox();
        return true;
      case "toggle_npc_name_overlay":
        // TODO: Settings.toggleShowNPCNameOverlay();
        return true;
      case "toggle_ids_overlay":
        // TODO: Settings.toggleExtendIdsOverlay();
        return true;
      case "toggle_trace_object_info":
        // TODO: Settings.toggleTraceObjectInfo();
        return true;
      case "toggle_player_name_overlay":
        // TODO: Settings.toggleShowPlayerNameOverlay();
        return true;
      case "toggle_roof_hiding":
        // TODO: Settings.toggleHideRoofs();
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
      case "toggle_xp_drops":
        // TODO: Settings.toggleXpDrops();
        return true;
      case "show_config_window":
        Launcher.getConfigWindow().showConfigWindow();
        return true;
      case "show_worldmap_window":
        Launcher.getWorldMapWindow().showWorldMapWindow();
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
        /* TODO: world choosing
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

         */
      case "toggle_wiki_hbar_button":
        // TODO: Settings.toggleWikiHbar();
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
        // TODO: Settings.toggleXPBar();
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
    // Users on Windows 8.1 or 10 are recommend to set USE_SYSTEM_NOTIFICATIONS = true
    if (System.getProperty("os.name").contains("Windows")) {
      return "Windows 10".equals(System.getProperty("os.name"))
              || "Windows 8.1".equals(System.getProperty("os.name"));
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
    if (currentProfile.equals("custom")) {
      save("custom");
    }
  }

  public static void save(String preset) {
    if (!successfullyInitted) {
      Logger.Warn(
              "Prevented erroneous save, please report this along with the RSC+ log file, set to debug logging mode");
      return;
    }
    try {
      Properties props = new Properties();

      //// general
      /*
      props.setProperty("custom_client_size", Boolean.toString(CUSTOM_CLIENT_SIZE.get(preset)));
      props.setProperty("custom_client_size_x", Integer.toString(CUSTOM_CLIENT_SIZE_X.get(preset)));
      props.setProperty("custom_client_size_y", Integer.toString(CUSTOM_CLIENT_SIZE_Y.get(preset)));
      */
      props.setProperty("check_updates", Boolean.toString(CHECK_UPDATES.get(preset)));
      /*
      props.setProperty(
              "welcome_enabled", Boolean.toString(REMIND_HOW_TO_OPEN_SETTINGS.get(preset)));
      props.setProperty("combat_menu", Boolean.toString(COMBAT_MENU_SHOWN.get(preset)));
      props.setProperty("combat_menu_hidden", Boolean.toString(COMBAT_MENU_HIDDEN.get(preset)));
      props.setProperty("show_xpdrops", Boolean.toString(SHOW_XPDROPS.get(preset)));
      props.setProperty("center_xpdrops", Boolean.toString(CENTER_XPDROPS.get(preset)));
      props.setProperty("inventory_full_alert", Boolean.toString(INVENTORY_FULL_ALERT.get(preset)));
      props.setProperty("name_patch_type", Integer.toString(NAME_PATCH_TYPE.get(preset)));
      props.setProperty("command_patch_quest", Boolean.toString(COMMAND_PATCH_QUEST.get(preset)));
      props.setProperty(
              "keep_scrollbar_pos_magic_prayer",
              Boolean.toString(KEEP_SCROLLBAR_POS_MAGIC_PRAYER.get(preset)));
      props.setProperty("hide_roofs", Boolean.toString(HIDE_ROOFS.get(preset)));
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
      props.setProperty("auto_screenshot", Boolean.toString(AUTO_SCREENSHOT.get(preset)));
      props.setProperty("view_distance", Integer.toString(VIEW_DISTANCE.get(preset)));
      props.setProperty("patch_gender", Boolean.toString(PATCH_GENDER.get(preset)));
      props.setProperty(
              "patch_hbar_512_last_pixel", Boolean.toString(PATCH_HBAR_512_LAST_PIXEL.get(preset)));
      props.setProperty(
              "patch_wrench_menu_spacing", Boolean.toString(PATCH_WRENCH_MENU_SPACING.get(preset)));
      */
      props.setProperty("log_verbosity", Integer.toString(LOG_VERBOSITY.get(preset)));
      props.setProperty("log_show_timestamps", Boolean.toString(LOG_SHOW_TIMESTAMPS.get(preset)));
      props.setProperty("log_show_level", Boolean.toString(LOG_SHOW_LEVEL.get(preset)));
      props.setProperty("log_force_timestamps", Boolean.toString(LOG_FORCE_TIMESTAMPS.get(preset)));
      props.setProperty("log_force_level", Boolean.toString(LOG_FORCE_LEVEL.get(preset)));
      props.setProperty("prefers_xdg_open", Boolean.toString(PREFERS_XDG_OPEN.get(preset)));

      //// overlays
      /*
      props.setProperty(
              "show_statusdisplay", Boolean.toString(SHOW_HP_PRAYER_FATIGUE_OVERLAY.get(preset)));
      props.setProperty("show_buffs", Boolean.toString(SHOW_BUFFS.get(preset)));
      props.setProperty(
              "show_last_menu_action", Boolean.toString(SHOW_LAST_MENU_ACTION.get(preset)));
      props.setProperty("show_mouse_tooltip", Boolean.toString(SHOW_MOUSE_TOOLTIP.get(preset)));
      props.setProperty(
              "show_extended_tooltip", Boolean.toString(SHOW_EXTENDED_TOOLTIP.get(preset)));
      props.setProperty("show_invcount", Boolean.toString(SHOW_INVCOUNT.get(preset)));
      props.setProperty("show_rsctimes_buttons", Boolean.toString(SHOW_RSCPLUS_BUTTONS.get(preset)));
      props.setProperty(
              "rsctimes_buttons_functional", Boolean.toString(RSCPLUS_BUTTONS_FUNCTIONAL.get(preset)));
      props.setProperty(
              "wiki_lookup_on_magic_book", Boolean.toString(WIKI_LOOKUP_ON_MAGIC_BOOK.get(preset)));
      props.setProperty("wiki_lookup_on_hbar", Boolean.toString(WIKI_LOOKUP_ON_HBAR.get(preset)));
      props.setProperty(
              "remove_report_abuse_button_hbar",
              Boolean.toString(REMOVE_REPORT_ABUSE_BUTTON_HBAR.get(preset)));
      props.setProperty("show_iteminfo", Boolean.toString(SHOW_ITEM_GROUND_OVERLAY.get(preset)));
      props.setProperty("show_playerinfo", Boolean.toString(SHOW_PLAYER_NAME_OVERLAY.get(preset)));
      props.setProperty("show_friendinfo", Boolean.toString(SHOW_FRIEND_NAME_OVERLAY.get(preset)));
      props.setProperty("show_npcinfo", Boolean.toString(SHOW_NPC_NAME_OVERLAY.get(preset)));
      props.setProperty("extend_idsinfo", Boolean.toString(EXTEND_IDS_OVERLAY.get(preset)));
      props.setProperty("trace_objectinfo", Boolean.toString(TRACE_OBJECT_INFO.get(preset)));
      props.setProperty("show_combat_info", Boolean.toString(SHOW_COMBAT_INFO.get(preset)));
      props.setProperty("show_player_position", Boolean.toString(SHOW_PLAYER_POSITION.get(preset)));
      props.setProperty("show_retro_fps", Boolean.toString(SHOW_RETRO_FPS.get(preset)));
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
      */

      //// notifications
      props.setProperty("tray_notifs", Boolean.toString(TRAY_NOTIFS.get(preset)));
      props.setProperty("tray_notifs_always", Boolean.toString(TRAY_NOTIFS_ALWAYS.get(preset)));
      props.setProperty("notification_sounds", Boolean.toString(NOTIFICATION_SOUNDS.get(preset)));
      props.setProperty("sound_notifs_always", Boolean.toString(SOUND_NOTIFS_ALWAYS.get(preset)));
      props.setProperty(
              "use_system_notifications", Boolean.toString(USE_SYSTEM_NOTIFICATIONS.get(preset)));
      props.setProperty("pm_notifications", Boolean.toString(PM_NOTIFICATIONS.get(preset)));
      props.setProperty("trade_notifications", Boolean.toString(TRADE_NOTIFICATIONS.get(preset)));
      props.setProperty("under_attack_notifications", Boolean.toString(UNDER_ATTACK_NOTIFICATIONS.get(preset)));
      props.setProperty("logout_notifications", Boolean.toString(LOGOUT_NOTIFICATIONS.get(preset)));
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
      props.setProperty("save_logininfo", Boolean.toString(SAVE_LOGININFO.get(preset)));
      props.setProperty("start_loginscreen", Boolean.toString(START_LOGINSCREEN.get(preset)));
      props.setProperty("speedrun_active", Boolean.toString(SPEEDRUNNER_MODE_ACTIVE.get(preset)));
      */

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
      /*
      props.setProperty("combat_style", Integer.toString(COMBAT_STYLE.get(preset)));
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

      // XP Goals
      int usernameID = 0;
      for (String username : Client.xpGoals.keySet()) {
        if (username.equals(XPBar.excludeUsername)) continue;
        for (int skill = 0; skill < Client.NUM_SKILLS; skill++) {
          int skillgoal = 0;
          try {
            skillgoal = Client.xpGoals.get(username)[skill];
          } catch (Exception noGoal) {
          }

          float lvlgoal = (float) 0;
          try {
            lvlgoal = Client.lvlGoals.get(username)[skill];
          } catch (Exception noGoal) {
          }

          props.setProperty(
                  String.format("xpGoal%02d%03d", skill, usernameID), Integer.toString(skillgoal));
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
	        if (!worldFile.isDirectory()) {
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
	  	// TODO: place like rsc+?
	    /*COMBAT_STYLE_INT = COMBAT_STYLE.get(currentProfile);
	    HIDE_ROOFS_BOOL = HIDE_ROOFS.get(currentProfile);
	    COMBAT_MENU_SHOWN_BOOL = COMBAT_MENU_SHOWN.get(currentProfile);
	    COMBAT_MENU_HIDDEN_BOOL = COMBAT_MENU_HIDDEN.get(currentProfile);
	    CAMERA_ZOOMABLE_BOOL = CAMERA_ZOOMABLE.get(currentProfile);
	    CAMERA_ROTATABLE_BOOL = CAMERA_ROTATABLE.get(currentProfile);
	    CAMERA_MOVABLE_BOOL = CAMERA_MOVABLE.get(currentProfile);*/
	  }
}
