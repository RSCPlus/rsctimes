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

import Game.Client;
import Game.Game;
import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import javax.swing.*;

/** Singleton main class which renders a loading window and the game client window. */
public class Launcher extends JFrame implements Runnable {

  // Singleton
  private static Launcher instance;
  private static ConfigWindow configWindow;
  private static WorldMapWindow worldMapWindow;

  public static ImageIcon icon = null;
  public static ImageIcon icon_warn = null;

  private JProgressBar m_progressBar;
  private JClassLoader m_classLoader;

  private Launcher() {
    // Empty private constructor to prevent extra instances from being created.
  }

  /** Renders the launcher progress bar window, then calls {@link #run()}. */
  public void init() {
    Logger.start();
    Logger.Info("Starting rsctimes");

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setBackground(Color.BLACK);

    // Set window icon
    URL iconURL = getResource("/assets/RSCX.logo.png");
    if (iconURL != null) {
      icon = new ImageIcon(iconURL);
      setIconImage(icon.getImage());
    }


    // Set size
    getContentPane().setPreferredSize(new Dimension(280, 32));
    setTitle("rsctimes Launcher");
    setResizable(false);
    pack();
    setLocationRelativeTo(null);

    // Add progress bar
    m_progressBar = new JProgressBar();
    m_progressBar.setStringPainted(true);
    m_progressBar.setBorderPainted(true);
    m_progressBar.setForeground(Color.GRAY.brighter());
    m_progressBar.setBackground(Color.BLACK);
    m_progressBar.setString("Initializing");
    getContentPane().add(m_progressBar);

    setVisible(true);
    new Thread(this).start();
  }

  /** Generates a config file if needed and launches the main client window. */
  @Override
  public void run() {
    if (Settings.UPDATE_CONFIRMATION.get(Settings.currentProfile)) {
      Client.firstTimeRunningRSCTimes = true;
      int response =
              JOptionPane.showConfirmDialog(
                      this,
                      "rsctimes has an automatic update feature.\n"
                              + "\n"
                              + "When enabled, rsctimes will prompt for and install updates when launching the client.\n"
                              + "The updates are obtained from our 'Latest' release on GitHub.\n"
                              + "\n"
                              + "Would you like to enable this feature?\n"
                              + "\n"
                              + "NOTE: This option can be toggled in the Settings interface under the General tab.",
                      "rsctimes",
                      JOptionPane.YES_NO_OPTION,
                      JOptionPane.INFORMATION_MESSAGE,
                      icon);
      if (response == JOptionPane.YES_OPTION || response == JOptionPane.CLOSED_OPTION) {
        Settings.CHECK_UPDATES.put(Settings.currentProfile, true);
        JOptionPane.showMessageDialog(
                this,
                "rsctimes is set to check for updates on GitHub at every launch!",
                "rsctimes",
                JOptionPane.INFORMATION_MESSAGE,
                icon);
      } else if (response == JOptionPane.NO_OPTION) {
        Settings.CHECK_UPDATES.put(Settings.currentProfile, false);
        JOptionPane.showMessageDialog(
                this,
                "rsctimes will not check for updates automatically.\n"
                        + "\n"
                        + "You will not get notified when new releases are available. To update your client, you\n"
                        + "will need to do it manually by replacing 'rsctimes.jar' in your rsctimes directory.\n"
                        + "\n"
                        + "You can enable GitHub updates again in the Settings interface under the General tab.",
                "rsctimes",
                JOptionPane.INFORMATION_MESSAGE,
                icon_warn);
      }
      Settings.UPDATE_CONFIRMATION.put(Settings.currentProfile, false);
      Settings.save();
    }

    if (Settings.CHECK_UPDATES.get(Settings.currentProfile)) {
      setStatus("Checking for rsctimes update...");
      double latestVersion = Client.fetchLatestVersionNumber();
      if (Settings.VERSION_NUMBER < latestVersion) {
        setStatus("rsctimes update is available");
        // TODO: before Y10K update this to %9.6f
        int response =
                JOptionPane.showConfirmDialog(
                        this,
                        "An rsctimes client update is available!\n"
                                + "\n"
                                + "Latest: "
                                + String.format("%8.6f", latestVersion)
                                + "\n"
                                + "Installed: "
                                + String.format("%8.6f", Settings.VERSION_NUMBER)
                                + "\n"
                                + "\n"
                                + "Would you like to update now?",
                        "rsctimes",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        icon);
        if (response == JOptionPane.YES_OPTION) {
          if (updateJar()) {
            JOptionPane.showMessageDialog(
                    this,
                    "rsctimes has been updated successfully!\n"
                            + "\n"
                            + "The client requires a restart, and will now exit.",
                    "rsctimes",
                    JOptionPane.INFORMATION_MESSAGE,
                    icon);
            System.exit(0);
          } else {
            response =
                    JOptionPane.showConfirmDialog(
                            this,
                            "rsctimes has failed to update, please try again later.\n"
                                    + "\n"
                                    + "Would you like to continue without updating?",
                            "rsctimes",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.ERROR_MESSAGE,
                            icon_warn);
            if (response == JOptionPane.NO_OPTION || response == JOptionPane.CLOSED_OPTION) {
              System.exit(0);
            }
          }
        }
      }
    }



    JConfig config = Game.getInstance().getJConfig();
    config.create(1);

    m_classLoader = new JClassLoader();
    if (!m_classLoader.fetch("/assets/mudclient38-recreated.jar")) {
      error("Unable to fetch Jar");
    }

    setStatus("Launching game...");
    Game game = Game.getInstance();
    try {
      Class<?> client = m_classLoader.loadClass("mudclient");
      game.setApplet((Applet) client.newInstance());
      Toolkit toolkit = Toolkit.getDefaultToolkit();

      AWTEventListener listener =
          new AWTEventListener() {
            public void eventDispatched(AWTEvent event) {
              if (event instanceof KeyEvent) {
            	  // list of places to not call consumption of key
            	  if (Launcher.getConfigWindow().getJFrame().isActive()) {
            		  return;
            	  }
                KeyEvent evt = (KeyEvent) event;
                if (evt.getID() == 401) {
                  Client.handler_keyboard.keyPressed(evt);
                } else if (evt.getID() == 402) {
                  Client.handler_keyboard.keyReleased(evt);
                }
                
                evt.consume();
              }
            }
          };
      toolkit.addAWTEventListener(listener, AWTEvent.KEY_EVENT_MASK);
    } catch (Exception e) {
      e.printStackTrace();
      error("Unable to launch game");
      return;
    }
    setVisible(false);
    dispose();
    game.start();
  }

  public boolean updateJar() {
    boolean success = true;

    setStatus("Starting rsctimes update...");
    setProgress(0, 1);

    try {
      URL url = new URL("https://github.com/RSCPlus/rsctimes/releases/download/Latest/rsctimes.jar");

      // Open connection
      URLConnection connection = url.openConnection();
      connection.setConnectTimeout(3000);
      connection.setReadTimeout(3000);

      int size = connection.getContentLength();
      int offset = 0;
      byte[] data = new byte[size];

      InputStream input = url.openStream();

      int readSize;
      while ((readSize = input.read(data, offset, size - offset)) != -1) {
        offset += readSize;
        setStatus("Updating rsctimes (" + (offset / 1024) + "KiB / " + (size / 1024) + "KiB)");
        setProgress(offset, size);
      }

      if (offset != size) {
        success = false;
      } else {
        // TODO: Get the jar filename in Settings.initDir
        File file = new File(Settings.Dir.JAR + "/rsctimes.jar");
        FileOutputStream output = new FileOutputStream(file);
        output.write(data);
        output.close();

        setStatus("rsctimes update complete");
      }
    } catch (Exception e) {
      success = false;
    }

    return success;
  }


  /**
   * Changes the launcher progress bar text and pauses the thread for 5 seconds.
   *
   * @param text the text to change the progress bar text to
   */
  public void error(String text) {
    setStatus("Error: " + text);
    try {
      Thread.sleep(5000);
      System.exit(0);
    } catch (Exception e) {
    }
  }

  /**
   * Changes the launcher progress bar text.
   *
   * @param text the text to change the progress bar text to
   */
  public void setStatus(final String text) {
    SwingUtilities.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            m_progressBar.setString(text);
          }
        });
  }

  /**
   * Sets the progress value of the launcher progress bar.
   *
   * @param value the number of tasks that have been completed
   * @param total the total number of tasks to complete
   */
  public void setProgress(final int value, final int total) {
    SwingUtilities.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            if (total == 0) {
              m_progressBar.setValue(0);
              return;
            }

            m_progressBar.setValue(value * 100 / total);
            try {Thread.sleep(100); } catch (Exception e ) {System.out.println("AAAAAAAA interupt");}
          }
        });
  }

  public JClassLoader getClassLoader() {
    return m_classLoader;
  }

  public static void main(String[] args) {
    Logger.start();
    Settings.initDir();
    Properties props = Settings.initSettings();

    if (Settings.javaVersion >= 9) {
      Logger.Error(
          "rsc wasn't designed for Java version "
              + Settings.javaVersion
              + ". You may encounter additional bugs, for best results use version 8.");
    } else if (Settings.javaVersion == -1) {
      Logger.Error(
          "rsc wasn't designed for your Java version. "
              + "You may encounter additional bugs, for best results use version 8.");
    }

    setConfigWindow(new ConfigWindow());
    Settings.loadKeybinds(props);
    Settings.successfullyInitted = true;
    setWorldMapWindow(new WorldMapWindow());
    TrayHandler.initTrayIcon();
    NotificationsHandler.initialize();
    Launcher.getInstance().init();

  }

  public static Launcher getInstance() {
    if (instance == null) {
      synchronized (Launcher.class) {
        instance = new Launcher();
      }
    }
    return instance;
  }

  /**
   * Creates a URL object that points to a specified file relative to the codebase, which is
   * typically either the jar or location of the package folders.
   *
   * @param fileName the file to parse as a URL
   * @return a URL that points to the specified file
   */
  public static URL getResource(String fileName) {
    URL url = null;
    try {
      url = Game.getInstance().getClass().getResource(fileName);
    } catch (Exception e) {
    }

    // Try finding assets
    if (url == null) {
      try {
        url = new URL("file://" + Util.findDirectoryReverse("/assets") + fileName);
      } catch (Exception e) {
      }
    }

    Logger.Info("Loading resource: " + fileName);

    return url;
  }

  /**
   * Creates an InputStream object that streams the contents of a specified file relative to the
   * codebase, which is typically either the jar or location of the package folders.
   *
   * @param fileName the file to open as an InputStream
   * @return an InputStream that streams the contents of the specified file
   */
  public static InputStream getResourceAsStream(String fileName) {
    InputStream stream = null;
    try {
      stream = Game.getInstance().getClass().getResourceAsStream(fileName);
    } catch (Exception e) {
    }

    // Try finding assets
    if (stream == null) {
      try {
        stream = new FileInputStream(Util.findDirectoryReverse("/assets") + fileName);
      } catch (Exception e) {
      }
    }

    Logger.Info("Loading resource as stream: " + fileName);

    if (fileName.equals("/assets/cache/maps14.jag")) {
      finishedLoading();
    }

    return stream;
  }

  public static void finishedLoading() {
    Game.getInstance().getJConfig().changeWorld(1);
  }


  /** @return the window */
  public static ConfigWindow getConfigWindow() {
    return configWindow;
  }

  /** @param configWindow the window to set */
  public static void setConfigWindow(ConfigWindow configWindow) {
    Launcher.configWindow = configWindow;
  }

  /** @return the window */
  public static WorldMapWindow getWorldMapWindow() {
    return worldMapWindow;
  }

  /** @param worldMapWindow the window to set */
  public static void setWorldMapWindow(WorldMapWindow worldMapWindow) {
    Launcher.worldMapWindow = worldMapWindow;
  }

}
