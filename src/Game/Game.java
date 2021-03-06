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

import Client.JConfig;
import Client.Launcher;
import Client.Logger;
import Client.NotificationsHandler;
import Client.Settings;
import Client.TrayHandler;
import Client.Util;
import Client.WorldMapWindow;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.net.URL;
import javax.swing.JFrame;

/** Singleton class that handles packaging the client into a JFrame and starting the applet. */
public class Game extends JFrame
    implements AppletStub, ComponentListener, WindowListener, FocusListener {

  // Singleton
  private static Game instance = null;

  private final JConfig m_config = new JConfig();
  private Applet m_applet = null;
  private String m_title = "";

  private Game() {
    // Empty private constructor to prevent extra instances from being created.
  }

  public void setApplet(Applet applet) {
    m_applet = applet;
    m_applet.setStub(this);
  }

  public Applet getApplet() {
    return m_applet;
  }

  /** Builds the main game client window and adds the applet to it. */
  public void start() {
    if (m_applet == null) return;

    // m_applet.addMouseListener(new MouseHandler());
    // Set window icon
    setIconImage(Launcher.icon.getImage());

    // Set window properties
    setResizable(true);
    addWindowListener(this);
    setMinimumSize(new Dimension(1, 1));

    // Add applet to window
    setContentPane(m_applet);
    getContentPane().setBackground(Color.BLACK);
    getContentPane().setPreferredSize(new Dimension(512, 357));
    addComponentListener(this);
    pack();

    // Hide cursor if software cursor
    Settings.checkSoftwareCursor(true);

    // Position window and make it visible
    setLocationRelativeTo(null);
    setVisible(true);

    updateTitle();

    Reflection.Load();
    Game.getInstance().resizeFrameWithContents();
    Renderer.init();

    if (!Util.isMacOS() && Settings.CUSTOM_CLIENT_SIZE.get(Settings.currentProfile)) {
      Game.getInstance().resizeFrameWithContents();
    }
  }

  public JConfig getJConfig() {
    return m_config;
  }

  /** Starts the game applet. */
  public void launchGame() {
    m_applet.addFocusListener(this);
    m_applet.init();
    m_applet.start();
  }

  public void updateTitle() {
    String title = "rsctimes (";

    if (!Replay.isPlaying) {
      title += Settings.WORLD_NAMES.get(Settings.WORLD.get(Settings.currentProfile));

      if (Client.player_name.length() != 0) {
        title += "; " + Client.player_name;
      }
    } /*else {
        String elapsed = Util.formatTimeDuration(Replay.elapsedTimeMillis(), Replay.endTimeMillis());
        String end = Util.formatTimeDuration(Replay.endTimeMillis(), Replay.endTimeMillis());
        title += elapsed + " / " + end;
        title += ", Speed: " + new DecimalFormat("##.##").format(Replay.fpsPlayMultiplier) + "x";
        if (Replay.paused) title += ", Paused";
      }

      if (Replay.isRecording) {
        String elapsed =
            Util.formatTimeDuration(Replay.elapsedTimeMillis(), Replay.elapsedTimeMillis());
        title += "; Recording: " + elapsed;
      }*/

    title += ")";

    if (m_title.equals(title)) {
      return;
    }
    m_title = title;
    super.setTitle(m_title);
  }

  /*
   * FocusListener methods
   */

  @Override
  public final void focusGained(FocusEvent e) {}

  @Override
  public final void focusLost(FocusEvent e) {
    KeyboardHandler.keyUp = false;
    KeyboardHandler.keyDown = false;
    KeyboardHandler.keyLeft = false;
    KeyboardHandler.keyRight = false;
    KeyboardHandler.keyShift = false;
  }

  /*
   * AppletStub methods
   */

  @Override
  public final URL getCodeBase() {
    return m_config.getURL("codebase");
  }

  @Override
  public final URL getDocumentBase() {
    return getCodeBase();
  }

  @Override
  public final String getParameter(String key) {
    return m_config.parameters.get(key);
  }

  @Override
  public final AppletContext getAppletContext() {
    return null;
  }

  @Override
  public final void appletResize(int width, int height) {}

  /*
   * WindowListener methods
   */

  @Override
  public final void windowClosed(WindowEvent e) {
    if (m_applet == null) return;

    m_applet.stop();
    m_applet.destroy();

    Logger.stop();
  }

  @Override
  public final void windowClosing(WindowEvent e) {
    dispose();
    Launcher.getConfigWindow().disposeJFrame();
    WorldMapWindow.disposeJFrame();
    TrayHandler.removeTrayIcon();
    NotificationsHandler.closeNotificationSoundClip();
    NotificationsHandler.disposeNotificationHandler();
  }

  @Override
  public final void windowOpened(WindowEvent e) {}

  @Override
  public final void windowDeactivated(WindowEvent e) {}

  @Override
  public final void windowActivated(WindowEvent e) {}

  @Override
  public final void windowDeiconified(WindowEvent e) {}

  @Override
  public final void windowIconified(WindowEvent e) {}

  /*
   * ComponentListener methods
   */

  @Override
  public final void componentHidden(ComponentEvent e) {}

  @Override
  public final void componentMoved(ComponentEvent e) {}

  @Override
  public final void componentResized(ComponentEvent e) {
    if (m_applet == null) return;

    // Handle minimum size and launch game
    // TODO: This is probably a bad spot and should be moved
    if (getMinimumSize().width == 1) {
      setMinimumSize(getSize());
      launchGame();

      // This workaround appears to be for a bug in the macOS JVM
      // Without it, mac users get very angry
      if (Util.isMacOS()) {
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
      }
    }

    Renderer.resize(
        getContentPane().getWidth(), getContentPane().getHeight() - Renderer.GAME_RENDER_OFFSET);
  }

  @Override
  public final void componentShown(ComponentEvent e) {}

  /**
   * Gets the game client instance. It makes one if one doesn't exist.
   *
   * @return The game client instance
   */
  public static Game getInstance() {
    if (instance == null) {
      synchronized (Game.class) {
        instance = new Game();
      }
    }
    return instance;
  }

  /**
   * Resizes the Game window to match the X and Y values stored in Settings. The applet's size will
   * be recalculated on the next rendering tick.
   */
  public void resizeFrameWithContents() {
    int windowWidth =
        Settings.CUSTOM_CLIENT_SIZE_X.get(Settings.currentProfile)
            + getInsets().left
            + getInsets().right;
    int windowHeight =
        Settings.CUSTOM_CLIENT_SIZE_Y.get(Settings.currentProfile)
            + getInsets().top
            + getInsets().bottom;
    // int windowWidth = 512;
    // int windowHeight = 396;//357;
    setSize(windowWidth, windowHeight);
    setLocationRelativeTo(null);
  }
}
