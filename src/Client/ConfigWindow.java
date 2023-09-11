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

import static Client.Util.isDarkThemeFlatLAF;
import static Client.Util.isUsingFlatLAFTheme;
import static Client.Util.osScaleDiv;
import static Client.Util.osScaleMul;

import Client.KeybindSet.KeyModifier;
import Game.Camera;
import Game.Client;
import Game.Game;
import Game.KeyboardHandler;
import Game.Renderer;
import com.formdev.flatlaf.ui.FlatRoundBorder;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * GUI designed for the RSCPlus client that manages configuration options and keybind values from
 * within an interface.
 *
 * <p><b>To add a new configuration option to the GUI,</b> <br>
 * 1.) Declare an instance variable to hold the gui element (eg checkbox) and add it to the GUI from
 * ConfigWindow.initialize() (see existing examples) <br>
 * 1.5.) If there is a helper method such as addCheckbox, use that method to create and store the
 * element that is returned in the ConfigWindow.initialize() method. See existing code for examples.
 * <br>
 * 2.) ^Add an appropriate variable to the Settings class as a class variable, <i>and</i> as an
 * assignment in the appropriate restore default method below. <br>
 * 3.) Add an entry in the ConfigWindow.synchronizeGuiValues() method that references the variable,
 * as per the already-existing examples.<br>
 * 4.) Add an entry in the ConfigWindow.saveSettings() method referencing the variable, as per the
 * already-existing examples.<br>
 * 5.) ^Add an entry in the Settings.Save() class save method to save the option to file.<br>
 * 6.) ^Add an entry in the Settings.Load() class load method to load the option from file.<br>
 * 7.) (Optional) If a method needs to be called to adjust settings other than the setting value
 * itself, add it to the ConfigWindow.applySettings() method below.<br>
 * <br>
 * <i>Entries marked with a ^ are steps used to add settings that are not included in the GUI.</i>
 * <br>
 * <br>
 * <b>To add a new keybind,</b><br>
 * 1.) Add a call in the initialize method to addKeybind with appropriate parameters.<br>
 * 2.) Add an entry to the command switch statement in Settings to process the command when its
 * keybind is pressed.<br>
 * 3.) Optional, recommended: Separate the command from its functionality by making a toggleBlah
 * method and calling it from the switch statement.<br>
 */
public class ConfigWindow {

  private JFrame frame;

  ClickListener clickListener = new ClickListener();
  RebindListener rebindListener = new RebindListener();

  ButtonFocusListener focusListener = new ButtonFocusListener();
  JTabbedPane tabbedPane;

  // Tooltip-related components
  private final AWTEventListener eventQueueListener;
  private final String toolTipInitText =
      "Click here to display additional information about settings";
  private boolean isListeningForEventQueue = false;
  private JPanel toolTipPanel;
  private JLabel toolTipTextLabel;
  private String toolTipTextString;

  /*
   * JComponent variables which hold configuration data
   */

  //// General tab
  private JCheckBox generalPanelClientSizeCheckbox;
  private JSpinner generalPanelClientSizeXSpinner;
  private JSpinner generalPanelClientSizeYSpinner;
  private SpinnerNumberModel spinnerWinXModel;
  private SpinnerNumberModel spinnerWinYModel;
  private JCheckBox generalPanelScaleWindowCheckbox;
  private JRadioButton generalPanelIntegerScalingFocusButton;
  private JSpinner generalPanelIntegerScalingSpinner;
  private JRadioButton generalPanelBilinearScalingFocusButton;
  private JSpinner generalPanelBilinearScalingSpinner;
  private JRadioButton generalPanelBicubicScalingFocusButton;
  private JSpinner generalPanelBicubicScalingSpinner;
  private JCheckBox generalPanelCheckUpdates;
  private JCheckBox generalPanelWelcomeEnabled;
  // private JCheckBox generalPanelChatHistoryCheckbox;
  private JCheckBox generalPanelCombatXPMenuCheckbox;
  private JCheckBox generalPanelCombatXPMenuHiddenCheckbox;
  private JCheckBox generalPanelInventoryFullAlertCheckbox;
  private JSlider generalPanelNamePatchModeSlider;
  private JSlider generalPanelLogVerbositySlider;
  private JCheckBox generalPanelLogLevelCheckbox;
  private JCheckBox generalPanelLogTimestampsCheckbox;
  private JCheckBox generalPanelLogForceLevelCheckbox;
  private JCheckBox generalPanelPrefersXdgOpenCheckbox;
  private JCheckBox generalPanelLogForceTimestampsCheckbox;
  private JCheckBox generalPanelCommandPatchQuestCheckbox;
  private JCheckBox generalPanelCommandPatchEdibleRaresCheckbox;
  private JCheckBox generalPanelCommandPatchDiskOfReturningCheckbox;
  private JCheckBox generalPanelBypassAttackCheckbox;
  private JCheckBox generalPanelSortFriendsCheckbox;
  private JCheckBox generalPanelRoofHidingCheckbox;
  private JCheckBox generalPanelDisableUndergroundLightingCheckbox;
  private JCheckBox generalPanelCameraZoomableCheckbox;
  private JCheckBox generalPanelCameraRotatableCheckbox;
  private JCheckBox generalPanelCameraMovableCheckbox;
  private JCheckBox generalPanelCameraMovableRelativeCheckbox;
  private JCheckBox generalPanelColoredTextCheckbox;
  private JSlider generalPanelFoVSlider;
  private JCheckBox generalPanelCustomCursorCheckbox;
  private JCheckBox generalPanelShiftScrollCameraRotationCheckbox;
  private JSlider generalPanelTrackpadRotationSlider;
  private JSlider generalPanelViewDistanceSlider;
  private JCheckBox generalPanelLimitFPSCheckbox;
  private JSpinner generalPanelLimitFPSSpinner;
  private JCheckBox generalPanelAutoScreenshotCheckbox;
  private JCheckBox generalPanelPatchGenderCheckbox;
  private JCheckBox generalPanelPatchHbar512LastPixelCheckbox;
  private JCheckBox generalPanelUseDarkModeCheckbox;
  private JCheckBox generalPanelUseNimbusThemeCheckbox;
  private JCheckBox generalPanelDebugModeCheckbox;
  private JCheckBox generalPanelExceptionHandlerCheckbox;
  private JLabel generalPanelNamePatchModeDesc;

  //// Overlays tab
  private JCheckBox overlayPanelStatusDisplayCheckbox;
  private JCheckBox overlayPanelBuffsCheckbox;
  private JCheckBox overlayPanelLastMenuActionCheckbox;
  private JCheckBox overlayPanelMouseTooltipCheckbox;
  private JCheckBox overlayPanelExtendedTooltipCheckbox;
  private JCheckBox overlayPanelInvCountCheckbox;
  private JCheckBox overlayPanelRscTimesButtonsCheckbox;
  private JCheckBox overlayPanelRscTimesButtonsFunctionalCheckbox;
  private JCheckBox overlayPanelWikiLookupOnMagicBookCheckbox;
  private JCheckBox overlayPanelWikiLookupOnHbarCheckbox;
  private JCheckBox overlayPanelToggleXPBarOnStatsButtonCheckbox;
  private JCheckBox overlayPanelToggleMotivationalQuotesCheckbox;
  private JCheckBox overlayPanelPositionCheckbox;
  private JCheckBox overlayPanelHideFpsCheckbox;
  private JCheckBox overlayPanelItemNamesCheckbox;
  private JCheckBox overlayPanelPlayerNamesCheckbox;
  private JCheckBox overlayPanelFriendNamesCheckbox;
  private JCheckBox overlayPanelNPCNamesCheckbox;
  private JCheckBox overlayPanelIDsCheckbox;
  private JCheckBox overlayPanelObjectInfoCheckbox;
  private JCheckBox overlayPanelHitboxCheckbox;
  private JCheckBox overlayPanelXPBarCheckbox;
  private JRadioButton overlayPanelXPCenterAlignFocusButton;
  private JRadioButton overlayPanelXPRightAlignFocusButton;
  private JCheckBox overlayPanelShowCombatInfoCheckbox;
  private JCheckBox overlayPanelUsePercentageCheckbox;
  private JCheckBox overlayPanelFoodHealingCheckbox;
  private JCheckBox overlayPanelHPRegenTimerCheckbox;
  private JCheckBox overlayPanelLagIndicatorCheckbox;
  private JTextField blockedItemsTextField;
  private JTextField highlightedItemsTextField;

  //// Notifications tab
  private JCheckBox notificationPanelPMNotifsCheckbox;
  private JCheckBox notificationPanelTradeNotifsCheckbox;
  private JCheckBox notificationPanelUnderAttackNotifsCheckbox;
  private JCheckBox notificationPanelLowHPNotifsCheckbox;
  private JSpinner notificationPanelLowHPNotifsSpinner;
  private JCheckBox notificationPanelHighlightedItemTimerCheckbox;
  private JSpinner notificationPanelHighlightedItemTimerSpinner;
  private JCheckBox notificationPanelNotifSoundsCheckbox;
  private JCheckBox notificationPanelUseSystemNotifsCheckbox;
  private JCheckBox notificationPanelTrayPopupCheckbox;
  private JRadioButton notificationPanelTrayPopupClientFocusButton;
  private JRadioButton notificationPanelNotifSoundClientFocusButton;
  private JRadioButton notificationPanelTrayPopupAnyFocusButton;
  private JRadioButton notificationPanelNotifSoundAnyFocusButton;

  //// Streaming & Privacy tab
  private JCheckBox streamingPanelTwitchChatCheckbox;
  private JCheckBox streamingPanelTwitchChatIntegrationEnabledCheckbox;
  private JTextField streamingPanelTwitchChannelNameTextField;
  private JTextField streamingPanelTwitchOAuthTextField;
  private JTextField streamingPanelTwitchUserTextField;
  private JCheckBox streamingPanelSaveLoginCheckbox;
  private JCheckBox streamingPanelStartLoginCheckbox;
  private JCheckBox streamingPanelSpeedrunnerCheckbox;
  // private JTextField streamingPanelSpeedrunnerUsernameTextField;

  //// Presets tab
  private JCheckBox presetsPanelCustomSettingsCheckbox;
  private JSlider presetsPanelPresetSlider;
  private JButton replaceConfigButton;
  private JButton resetPresetsButton;
  private int sliderValue = -1;

  //// World List tab
  private HashMap<Integer, JTextField> worldNamesJTextFields = new HashMap<Integer, JTextField>();
  private HashMap<Integer, JButton> worldDeleteJButtons = new HashMap<Integer, JButton>();
  private HashMap<Integer, JTextField> worldUrlsJTextFields = new HashMap<Integer, JTextField>();
  private HashMap<Integer, JTextField> worldPortsJTextFields = new HashMap<Integer, JTextField>();
  private HashMap<Integer, JPanel> worldListTitleTextFieldContainers =
      new HashMap<Integer, JPanel>();
  private HashMap<Integer, JPanel> worldListURLPortTextFieldContainers =
      new HashMap<Integer, JPanel>();
  private HashMap<Integer, JLabel> worldListSpacingLabels = new HashMap<Integer, JLabel>();
  private JPanel worldListPanel = new JPanel();

  public ConfigWindow() {
    Util.setUITheme();
    eventQueueListener = createConfigWindowEventQueueListener();
    initialize();
  }

  public void showConfigWindow() {
    this.synchronizeGuiValues();
    frame.setVisible(true);
  }

  public void hideConfigWindow() {
    resetToolTipListener();

    frame.setVisible(false);
  }

  public void toggleConfigWindow() {
    if (this.isShown()) {
      this.hideConfigWindow();
    } else {
      this.showConfigWindow();
    }
  }

  public boolean isShown() {
    return frame.isVisible();
  }

  /** Initialize the contents of the frame. */
  private void initialize() {
    Logger.Info("Creating configuration window");
    try {
      SwingUtilities.invokeAndWait(
          new Runnable() {

            @Override
            public void run() {
              runInit();
            }
          });
    } catch (InvocationTargetException e) {
      Logger.Error("There was a thread-related error while setting up the config window!");
      e.printStackTrace();
    } catch (InterruptedException e) {
      Logger.Error(
          "There was a thread-related error while setting up the config window! The window may not be initialized properly!");
      e.printStackTrace();
    }
  }

  private void runInit() {
    frame = new JFrame();
    frame.setTitle("Settings");
    frame.setBounds(osScaleDiv(100), osScaleDiv(100), osScaleMul(800), osScaleMul(650));
    frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    frame.getContentPane().setLayout(new BorderLayout(0, 0));
    URL iconURL = Launcher.getResource("/assets/icon.png");
    if (iconURL != null) {
      ImageIcon icon = new ImageIcon(iconURL);
      frame.setIconImage(icon.getImage());
    }
    frame.addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            resetToolTipListener();
            super.windowClosed(e);
          }
        });

    // Container declarations
    /** The tabbed pane holding the five configuration tabs */
    tabbedPane = new JTabbedPane();
    if (isUsingFlatLAFTheme()) {
      tabbedPane.putClientProperty("JTabbedPane.tabType", "card");
    }
    tabbedPane.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (isListeningForEventQueue) {
              toolTipTextString = "Waiting for mouse hover...";
            } else {
              toolTipTextString = toolTipInitText;
            }
            toolTipTextLabel.setText(toolTipTextString);
          }
        });

    /* The JPanel containing the tooltip text components */
    toolTipPanel = new JPanel();
    resetToolTipBarPanelColors();

    /**
     * The JPanel containing the OK, Cancel, Apply, and Restore Defaults buttons at the bottom of
     * the window
     */
    JPanel navigationPanel = new JPanel();

    JScrollPane presetsScrollPane = new JScrollPane();
    JScrollPane generalScrollPane = new JScrollPane();
    JScrollPane overlayScrollPane = new JScrollPane();
    JScrollPane notificationScrollPane = new JScrollPane();
    JScrollPane streamingScrollPane = new JScrollPane();
    JScrollPane keybindScrollPane = new JScrollPane();
    JScrollPane worldListScrollPane = new JScrollPane();
    JScrollPane authorsScrollPane = new JScrollPane();

    if (isUsingFlatLAFTheme()) {
      Color navigationPanelBackgroundColor = null;

      if (isDarkThemeFlatLAF()) {
        navigationPanelBackgroundColor = new Color(60, 63, 65);
      } else if (Util.isLightThemeFlatLAF()) {
        navigationPanelBackgroundColor = new Color(225, 225, 225);
      }

      navigationPanel.setBackground(navigationPanelBackgroundColor);

      Color scrollPaneBorderColor = null;

      if (isDarkThemeFlatLAF()) {
        scrollPaneBorderColor = new Color(82, 86, 87);
      } else if (Util.isLightThemeFlatLAF()) {
        scrollPaneBorderColor = new Color(194, 194, 194);
      }

      MatteBorder scrollPaneBorder =
          BorderFactory.createMatteBorder(
              0, osScaleMul(1), osScaleMul(1), osScaleMul(1), scrollPaneBorderColor);

      presetsScrollPane.setBorder(scrollPaneBorder);
      generalScrollPane.setBorder(scrollPaneBorder);
      overlayScrollPane.setBorder(scrollPaneBorder);
      notificationScrollPane.setBorder(scrollPaneBorder);
      streamingScrollPane.setBorder(scrollPaneBorder);
      keybindScrollPane.setBorder(scrollPaneBorder);
      worldListScrollPane.setBorder(scrollPaneBorder);
      authorsScrollPane.setBorder(scrollPaneBorder);
    }

    JPanel presetsPanel = new JPanel();
    presetsPanel.setName("presets");
    JPanel generalPanel = new JPanel();
    generalPanel.setName("general");
    JPanel overlayPanel = new JPanel();
    overlayPanel.setName("overlays");
    JPanel notificationPanel = new JPanel();
    notificationPanel.setName("notifications");
    JPanel streamingPanel = new JPanel();
    streamingPanel.setName("streaming_privacy");
    JPanel keybindPanel = new JPanel();
    keybindPanel.setName("keybinds");
    worldListPanel = new JPanel();
    worldListPanel.setName("world_list");
    JPanel authorsPanel = new JPanel();
    authorsPanel.setName("authors");

    frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

    JPanel pageEndPanel = new JPanel();
    pageEndPanel.setLayout(new BoxLayout(pageEndPanel, BoxLayout.Y_AXIS));
    pageEndPanel.add(toolTipPanel);
    pageEndPanel.add(navigationPanel);
    frame.getContentPane().add(pageEndPanel, BorderLayout.PAGE_END);

    tabbedPane.addTab("Presets", null, presetsScrollPane, null);
    tabbedPane.addTab("General", null, generalScrollPane, null);
    tabbedPane.addTab("Overlays", null, overlayScrollPane, null);
    tabbedPane.addTab("Notifications", null, notificationScrollPane, null);
    tabbedPane.addTab("Streaming & Privacy", null, streamingScrollPane, null);
    tabbedPane.addTab("Keybinds", null, keybindScrollPane, null);
    tabbedPane.addTab("World List", null, worldListScrollPane, null);
    tabbedPane.addTab("Authors", null, authorsScrollPane, null);

    presetsScrollPane.setViewportView(presetsPanel);
    generalScrollPane.setViewportView(generalPanel);
    overlayScrollPane.setViewportView(overlayPanel);
    notificationScrollPane.setViewportView(notificationPanel);
    streamingScrollPane.setViewportView(streamingPanel);
    keybindScrollPane.setViewportView(keybindPanel);
    worldListScrollPane.setViewportView(worldListPanel);
    authorsScrollPane.setViewportView(authorsPanel);

    // Adding padding for aesthetics
    int border10 = osScaleMul(10);
    if (Util.isUsingFlatLAFTheme()) {
      Color borderColor = isDarkThemeFlatLAF() ? new Color(82, 86, 87) : new Color(194, 194, 194);
      toolTipPanel.setBorder(
          BorderFactory.createMatteBorder(
              0, osScaleMul(1), osScaleMul(1), osScaleMul(1), borderColor));
    } else {
      toolTipPanel.setBorder(
          BorderFactory.createCompoundBorder(
              BorderFactory.createEmptyBorder(0, osScaleMul(2), 0, osScaleMul(2)),
              BorderFactory.createLineBorder(new Color(146, 151, 161))));
    }
    navigationPanel.setBorder(
        BorderFactory.createEmptyBorder(osScaleMul(7), border10, border10, border10));
    presetsPanel.setBorder(BorderFactory.createEmptyBorder(border10, border10, border10, border10));
    generalPanel.setBorder(BorderFactory.createEmptyBorder(border10, border10, border10, border10));
    overlayPanel.setBorder(BorderFactory.createEmptyBorder(border10, border10, border10, border10));
    notificationPanel.setBorder(
        BorderFactory.createEmptyBorder(border10, border10, border10, border10));
    streamingPanel.setBorder(
        BorderFactory.createEmptyBorder(border10, border10, border10, border10));
    keybindPanel.setBorder(BorderFactory.createEmptyBorder(border10, border10, border10, border10));
    worldListPanel.setBorder(
        BorderFactory.createEmptyBorder(border10, border10, border10, border10));
    authorsPanel.setBorder(BorderFactory.createEmptyBorder(border10, border10, border10, border10));

    int verticalSpeed = osScaleMul(20);
    int horizontalSpeed = osScaleMul(15);

    setScrollSpeed(presetsScrollPane, verticalSpeed, horizontalSpeed);
    setScrollSpeed(generalScrollPane, verticalSpeed, horizontalSpeed);
    setScrollSpeed(overlayScrollPane, verticalSpeed, horizontalSpeed);
    setScrollSpeed(notificationScrollPane, verticalSpeed, horizontalSpeed);
    setScrollSpeed(streamingScrollPane, verticalSpeed, horizontalSpeed);
    setScrollSpeed(keybindScrollPane, verticalSpeed, horizontalSpeed);
    setScrollSpeed(worldListScrollPane, verticalSpeed, horizontalSpeed);
    setScrollSpeed(authorsScrollPane, verticalSpeed, horizontalSpeed);

    /*
     Tooltip panel
    */
    toolTipPanel.setLayout(new BoxLayout(toolTipPanel, BoxLayout.X_AXIS));
    toolTipTextLabel = new JLabel(toolTipInitText);
    toolTipTextLabel.setBorder(
        BorderFactory.createEmptyBorder(osScaleMul(2), border10, osScaleMul(2), border10));
    toolTipTextLabel.setText(toolTipInitText);
    toolTipTextLabel.setMinimumSize(osScaleMul(new Dimension(100, 28)));
    toolTipTextLabel.setMaximumSize(new Dimension(Short.MAX_VALUE, osScaleMul(28)));
    toolTipTextLabel.setAlignmentY(0.75f);
    toolTipPanel.add(toolTipTextLabel);

    toolTipPanel.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent e) {
            if (isListeningForEventQueue) {
              resetToolTipBarPanelColors();
              toolTipTextLabel.setText(toolTipInitText);
              removeConfigWindowEventQueueListener();
            } else {
              // Uses theme tooltip colors
              if (Util.isDarkThemeFlatLAF()) {
                toolTipPanel.setBackground(new Color(21, 23, 24));
              } else if (Util.isLightThemeFlatLAF()) {
                toolTipPanel.setBackground(new Color(250, 250, 250));
              } else {
                toolTipPanel.setBackground(new Color(242, 242, 189));
              }
              toolTipTextString = "Waiting for mouse hover...";
              toolTipTextLabel.setText(toolTipTextString);
              addConfigWindowEventQueueListener();
            }
          }
        });

    /*
     * Navigation buttons
     */

    navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));

    JButton okButton = addButton("OK", navigationPanel, Component.LEFT_ALIGNMENT);
    if (isDarkThemeFlatLAF()) {
      okButton.setBackground(new Color(42, 46, 48));
    }
    okButton.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Launcher.getConfigWindow().applySettings();
            Launcher.getConfigWindow().hideConfigWindow();
          }
        });

    if (isUsingFlatLAFTheme()) {
      navigationPanel.add(Box.createRigidArea(osScaleMul(new Dimension(4, 0))));
    }

    JButton cancelButton = addButton("Cancel", navigationPanel, Component.LEFT_ALIGNMENT);

    if (isDarkThemeFlatLAF()) {
      cancelButton.setBackground(new Color(42, 46, 48));
    }

    cancelButton.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Launcher.getConfigWindow().hideConfigWindow();
          }
        });

    if (isUsingFlatLAFTheme()) {
      navigationPanel.add(Box.createRigidArea(osScaleMul(new Dimension(4, 0))));
    }

    JButton applyButton = addButton("Apply", navigationPanel, Component.LEFT_ALIGNMENT);

    if (isDarkThemeFlatLAF()) {
      applyButton.setBackground(new Color(42, 46, 48));
    }

    applyButton.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            Launcher.getConfigWindow().applySettings();
          }
        });

    navigationPanel.add(Box.createHorizontalGlue());

    JButton restoreDefaultsButton =
        addButton("Restore Defaults", navigationPanel, Component.RIGHT_ALIGNMENT);

    if (isDarkThemeFlatLAF()) {
      restoreDefaultsButton.setBackground(new Color(42, 46, 48));
    }

    restoreDefaultsButton.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            JPanel confirmDefaultPanel =
                Util.createOptionMessagePanel(
                    "Are you sure you want to restore all settings to their defaults?");
            int choice =
                JOptionPane.showConfirmDialog(
                    Launcher.getConfigWindow().frame,
                    confirmDefaultPanel,
                    "Confirm",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.CLOSED_OPTION || choice == JOptionPane.NO_OPTION) {
              return;
            }

            Settings.initSettings(); // make sure "default" is really default
            Settings.save("default");
            synchronizeGuiValues();

            // Restore defaults
            /* TODO: reimplement per-tab defaults?
            switch (tabbedPane.getSelectedIndex()) {
            case 0:
            	Settings.restoreDefaultGeneral();
            	Game.getInstance().resizeFrameWithContents();
            	break;
            case 1:
            	Settings.restoreDefaultOverlays();
            	break;
            case 2:
            	Settings.restoreDefaultNotifications();
            	break;
            case 3:
            	Settings.restoreDefaultPrivacy();
            	break;
            case 4:
            	Settings.restoreDefaultKeybinds();
            	break;
                        //TODO more pages
            default:
            	Logger.Error("Restore defaults attempted to operate on a non-existent tab!");
            }
                        */
          }
        });

    /*
     * General tab
     */

    generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.Y_AXIS));

    /// "Client settings" are settings related to just setting up how the client behaves
    /// Not really anything related to gameplay, just being able to set up the client
    /// the way the user wants it
    addSettingsHeader(generalPanel, "Client settings");

    JPanel generalPanelClientSizePanel = new JPanel();
    generalPanel.add(generalPanelClientSizePanel);
    generalPanelClientSizePanel.setLayout(
        new BoxLayout(generalPanelClientSizePanel, BoxLayout.X_AXIS));
    generalPanelClientSizePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    // TODO: Perhaps change to "Save client size on close"?
    generalPanelClientSizeCheckbox =
        addCheckbox("Client window dimensions:", generalPanelClientSizePanel);
    generalPanelClientSizeCheckbox.setToolTipText("Set the client size to the supplied dimensions");

    generalPanelClientSizeXSpinner = new JSpinner();
    generalPanelClientSizePanel.add(generalPanelClientSizeXSpinner);
    generalPanelClientSizeXSpinner.setMaximumSize(osScaleMul(new Dimension(70, 23)));
    generalPanelClientSizeXSpinner.setMinimumSize(osScaleMul(new Dimension(70, 23)));
    generalPanelClientSizeXSpinner.setAlignmentY(0.7f);
    generalPanelClientSizeXSpinner.setToolTipText("Default client width (512 minimum at 1x scale)");
    generalPanelClientSizeXSpinner.putClientProperty("JComponent.sizeVariant", "mini");

    JLabel generalPanelClientSizeByLabel = new JLabel("x");
    generalPanelClientSizePanel.add(generalPanelClientSizeByLabel);
    generalPanelClientSizeByLabel.setAlignmentY(0.8f);

    int spinnerByMargin = isUsingFlatLAFTheme() ? 4 : 2;

    generalPanelClientSizeByLabel.setBorder(
        BorderFactory.createEmptyBorder(
            0, osScaleMul(spinnerByMargin), 0, osScaleMul(spinnerByMargin)));

    generalPanelClientSizeYSpinner = new JSpinner();
    generalPanelClientSizePanel.add(generalPanelClientSizeYSpinner);
    generalPanelClientSizeYSpinner.setMaximumSize(osScaleMul(new Dimension(70, 23)));
    generalPanelClientSizeYSpinner.setMinimumSize(osScaleMul(new Dimension(70, 23)));
    generalPanelClientSizeYSpinner.setAlignmentY(0.7f);
    generalPanelClientSizeYSpinner.setToolTipText(
        "Default client height (357 minimum at 1x scale)");
    generalPanelClientSizeYSpinner.putClientProperty("JComponent.sizeVariant", "mini");

    // Sanitize JSpinner values
    spinnerWinXModel = new SpinnerNumberModel();
    spinnerWinXModel.setMinimum(512);
    spinnerWinXModel.setValue(512);
    spinnerWinXModel.setStepSize(10);
    generalPanelClientSizeXSpinner.setModel(spinnerWinXModel);
    spinnerWinYModel = new SpinnerNumberModel();
    spinnerWinYModel.setMinimum(357);
    spinnerWinYModel.setValue(357);
    spinnerWinYModel.setStepSize(10);
    generalPanelClientSizeYSpinner.setModel(spinnerWinYModel);

    if (Util.isUsingFlatLAFTheme()) {
      generalPanelClientSizePanel.add(Box.createRigidArea(osScaleMul(new Dimension(6, 0))));
    }

    JButton generalPanelClientSizeMaxButton =
        addButton("Max", generalPanelClientSizePanel, Component.RIGHT_ALIGNMENT);
    generalPanelClientSizeMaxButton.setAlignmentY(0.7f);
    generalPanelClientSizeMaxButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            Dimension maximumWindowSize =
                ScaledWindow.getInstance().getMaximumEffectiveWindowSize();

            int windowWidth = maximumWindowSize.width;
            int windowHeight = maximumWindowSize.height;

            // This only changes the values in the boxes
            spinnerWinXModel.setValue(windowWidth);
            spinnerWinYModel.setValue(windowHeight);
          }
        });

    if (Util.isUsingFlatLAFTheme()) {
      generalPanelClientSizePanel.add(Box.createRigidArea(osScaleMul(new Dimension(6, 0))));
    }

    JButton generalPanelClientSizeResetButton =
        addButton("Reset", generalPanelClientSizePanel, Component.RIGHT_ALIGNMENT);
    generalPanelClientSizeResetButton.setAlignmentY(0.7f);
    generalPanelClientSizeResetButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // This only changes the values in the boxes
            Dimension scaledMinimumWindowSize =
                ScaledWindow.getInstance().getMinimumViewportSizeForScalar();
            spinnerWinXModel.setValue(scaledMinimumWindowSize.width);
            spinnerWinYModel.setValue(scaledMinimumWindowSize.height);
          }
        });

    JLabel generalPanelClientSizeScaleWarning =
        new JLabel("(Will be reset if window scale changes)");
    generalPanelClientSizeScaleWarning.setAlignmentY(0.8f);
    generalPanelClientSizeScaleWarning.setBorder(
        BorderFactory.createEmptyBorder(0, osScaleMul(6), 0, 0));
    generalPanelClientSizePanel.add(generalPanelClientSizeScaleWarning);

    // Scaling options
    JPanel generalPanelScalePanel = new JPanel();
    generalPanel.add(generalPanelScalePanel);
    generalPanelScalePanel.setLayout(new BoxLayout(generalPanelScalePanel, BoxLayout.Y_AXIS));
    generalPanelScalePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    generalPanelScaleWindowCheckbox = addCheckbox("Scale window:", generalPanelScalePanel);
    generalPanelScaleWindowCheckbox.setToolTipText("Enable to scale the game client");
    generalPanelScaleWindowCheckbox.setBorder(
        BorderFactory.createEmptyBorder(0, 0, osScaleMul(5), 0));

    ButtonGroup generalPanelScaleWindowTypeButtonGroup = new ButtonGroup();
    String scaleLargerThanResolutionToolTip =
        "This scale value will produce a window bigger than your screen resolution";

    // Integer scaling
    JPanel generalPanelIntegerScalingPanel = new JPanel();
    generalPanelScalePanel.add(generalPanelIntegerScalingPanel);
    generalPanelIntegerScalingPanel.setLayout(
        new BoxLayout(generalPanelIntegerScalingPanel, BoxLayout.X_AXIS));
    generalPanelIntegerScalingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    generalPanelIntegerScalingFocusButton =
        addRadioButton("Integer scaling", generalPanelIntegerScalingPanel, osScaleMul(20));
    generalPanelIntegerScalingFocusButton.setToolTipText(
        "Uses the nearest neighbor algorithm for pixel-perfect client scaling");
    generalPanelScaleWindowTypeButtonGroup.add(generalPanelIntegerScalingFocusButton);

    generalPanelIntegerScalingSpinner = new JSpinner();
    generalPanelIntegerScalingPanel.add(generalPanelIntegerScalingSpinner);
    String integerScalingSpinnerToolTip =
        "Integer scaling value " + (int) Renderer.minScalar + "-" + (int) Renderer.maxIntegerScalar;
    generalPanelIntegerScalingSpinner.setMaximumSize(osScaleMul(new Dimension(55, 26)));
    generalPanelIntegerScalingSpinner.setMinimumSize(osScaleMul(new Dimension(55, 26)));
    generalPanelIntegerScalingSpinner.setAlignmentY(0.625f);
    generalPanelIntegerScalingSpinner.setToolTipText(integerScalingSpinnerToolTip);
    generalPanelIntegerScalingSpinner.putClientProperty("JComponent.sizeVariant", "mini");
    if (Util.isUsingFlatLAFTheme()) {
      generalPanelIntegerScalingSpinner.setBorder(new FlatRoundBorder());
    } else {
      generalPanelIntegerScalingSpinner.setBorder(
          BorderFactory.createEmptyBorder(
              osScaleMul(2), osScaleMul(2), osScaleMul(2), osScaleMul(2)));
    }
    generalPanelIntegerScalingSpinner.addChangeListener(
        new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent e) {
            Dimension maximumWindowSize = ScaledWindow.getInstance().getMaximumWindowSize();
            int scalar = (int) generalPanelIntegerScalingSpinner.getValue();

            if (((512 * scalar) + ScaledWindow.getInstance().getWindowWidthInsets()
                    > maximumWindowSize.getWidth())
                || ((357 * scalar) + ScaledWindow.getInstance().getWindowHeightInsets()
                    > maximumWindowSize.getHeight())) {
              generalPanelIntegerScalingSpinner.setBorder(
                  new LineBorder(Color.orange, osScaleMul(2)));
              generalPanelIntegerScalingSpinner.setToolTipText(scaleLargerThanResolutionToolTip);
            } else {
              if (Util.isUsingFlatLAFTheme()) {
                generalPanelIntegerScalingSpinner.setBorder(new FlatRoundBorder());
              } else {
                generalPanelIntegerScalingSpinner.setBorder(
                    BorderFactory.createEmptyBorder(
                        osScaleMul(2), osScaleMul(2), osScaleMul(2), osScaleMul(2)));
              }
              generalPanelIntegerScalingSpinner.setToolTipText(integerScalingSpinnerToolTip);
            }
          }
        });

    SpinnerNumberModel spinnerLimitIntegerScaling =
        new SpinnerNumberModel(2, (int) Renderer.minScalar, (int) Renderer.maxIntegerScalar, 1);
    generalPanelIntegerScalingSpinner.setModel(spinnerLimitIntegerScaling);

    // Bilinear scaling
    JPanel generalPanelBilinearScalingPanel = new JPanel();
    generalPanelScalePanel.add(generalPanelBilinearScalingPanel);
    generalPanelBilinearScalingPanel.setLayout(
        new BoxLayout(generalPanelBilinearScalingPanel, BoxLayout.X_AXIS));
    generalPanelBilinearScalingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    generalPanelBilinearScalingFocusButton =
        addRadioButton("Bilinear interpolation", generalPanelBilinearScalingPanel, osScaleMul(20));
    generalPanelBilinearScalingFocusButton.setToolTipText(
        "Uses the bilinear interpolation algorithm for client scaling");
    generalPanelScaleWindowTypeButtonGroup.add(generalPanelBilinearScalingFocusButton);

    generalPanelBilinearScalingSpinner = new JSpinner();
    generalPanelBilinearScalingPanel.add(generalPanelBilinearScalingSpinner);
    String bilinearScalingSpinnerToolTip =
        "Bilinear scaling value " + Renderer.minScalar + "-" + Renderer.maxInterpolationScalar;
    generalPanelBilinearScalingSpinner.setMaximumSize(osScaleMul(new Dimension(55, 26)));
    generalPanelBilinearScalingSpinner.setMinimumSize(osScaleMul(new Dimension(55, 26)));
    generalPanelBilinearScalingSpinner.setAlignmentY(0.625f);
    generalPanelBilinearScalingSpinner.setToolTipText(bilinearScalingSpinnerToolTip);
    generalPanelBilinearScalingSpinner.putClientProperty("JComponent.sizeVariant", "mini");
    if (Util.isUsingFlatLAFTheme()) {
      generalPanelBilinearScalingSpinner.setBorder(new FlatRoundBorder());
    } else {
      generalPanelBilinearScalingSpinner.setBorder(
          BorderFactory.createEmptyBorder(
              osScaleMul(2), osScaleMul(2), osScaleMul(2), osScaleMul(2)));
    }
    generalPanelBilinearScalingSpinner.addChangeListener(
        new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent e) {
            Dimension maximumWindowSize = ScaledWindow.getInstance().getMaximumWindowSize();
            float scalar = (float) generalPanelBilinearScalingSpinner.getValue();

            if (((512 * scalar) + ScaledWindow.getInstance().getWindowWidthInsets()
                    > maximumWindowSize.getWidth())
                || ((357 * scalar) + ScaledWindow.getInstance().getWindowHeightInsets()
                    > maximumWindowSize.getHeight())) {
              generalPanelBilinearScalingSpinner.setBorder(
                  new LineBorder(Color.orange, osScaleMul(2)));
              generalPanelBilinearScalingSpinner.setToolTipText(scaleLargerThanResolutionToolTip);
            } else {
              if (Util.isUsingFlatLAFTheme()) {
                generalPanelBilinearScalingSpinner.setBorder(new FlatRoundBorder());
              } else {
                generalPanelBilinearScalingSpinner.setBorder(
                    BorderFactory.createEmptyBorder(
                        osScaleMul(2), osScaleMul(2), osScaleMul(2), osScaleMul(2)));
              }
              generalPanelBilinearScalingSpinner.setToolTipText(bilinearScalingSpinnerToolTip);
            }
          }
        });

    SpinnerNumberModel spinnerLimitBilinearScaling =
        new SpinnerNumberModel(
            new Float(1.5f),
            new Float(Renderer.minScalar),
            new Float(Renderer.maxInterpolationScalar),
            new Float(0.1f));
    generalPanelBilinearScalingSpinner.setModel(spinnerLimitBilinearScaling);

    JLabel bilinearInterpolationScalingWarning =
        new JLabel("(May affect performance at high scaling values)");
    bilinearInterpolationScalingWarning.setAlignmentY(0.75f);
    int bilinearInterpolationScalingMargin = isUsingFlatLAFTheme() ? 6 : 2;
    bilinearInterpolationScalingWarning.setBorder(
        BorderFactory.createEmptyBorder(0, osScaleMul(bilinearInterpolationScalingMargin), 0, 0));
    generalPanelBilinearScalingPanel.add(bilinearInterpolationScalingWarning);

    // Bicubic scaling
    JPanel generalPanelBicubicScalingPanel = new JPanel();
    generalPanelScalePanel.add(generalPanelBicubicScalingPanel);
    generalPanelBicubicScalingPanel.setLayout(
        new BoxLayout(generalPanelBicubicScalingPanel, BoxLayout.X_AXIS));
    generalPanelBicubicScalingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    generalPanelBicubicScalingFocusButton =
        addRadioButton("Bicubic interpolation", generalPanelBicubicScalingPanel, osScaleMul(20));
    generalPanelBicubicScalingFocusButton.setToolTipText(
        "Uses the bicubic interpolation algorithm for client scaling");
    generalPanelScaleWindowTypeButtonGroup.add(generalPanelBicubicScalingFocusButton);

    generalPanelBicubicScalingSpinner = new JSpinner();
    generalPanelBicubicScalingPanel.add(generalPanelBicubicScalingSpinner);
    String bicubicScalingSpinnerToolTip =
        "Bicubic scaling value " + Renderer.minScalar + "-" + Renderer.maxInterpolationScalar;
    generalPanelBicubicScalingSpinner.setMaximumSize(osScaleMul(new Dimension(55, 26)));
    generalPanelBicubicScalingSpinner.setMinimumSize(osScaleMul(new Dimension(55, 26)));
    generalPanelBicubicScalingSpinner.setAlignmentY(0.625f);
    generalPanelBicubicScalingSpinner.setToolTipText(bicubicScalingSpinnerToolTip);
    generalPanelBicubicScalingSpinner.putClientProperty("JComponent.sizeVariant", "mini");
    if (Util.isUsingFlatLAFTheme()) {
      generalPanelBicubicScalingSpinner.setBorder(new FlatRoundBorder());
    } else {
      generalPanelBicubicScalingSpinner.setBorder(
          BorderFactory.createEmptyBorder(
              osScaleMul(2), osScaleMul(2), osScaleMul(2), osScaleMul(2)));
    }
    generalPanelBicubicScalingSpinner.addChangeListener(
        new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent e) {
            Dimension maximumWindowSize = ScaledWindow.getInstance().getMaximumWindowSize();
            float scalar = (float) generalPanelBicubicScalingSpinner.getValue();

            if (((512 * scalar) + ScaledWindow.getInstance().getWindowWidthInsets()
                    > maximumWindowSize.getWidth())
                || ((357 * scalar) + ScaledWindow.getInstance().getWindowHeightInsets()
                    > maximumWindowSize.getHeight())) {
              generalPanelBicubicScalingSpinner.setBorder(new LineBorder(Color.orange, 2));
              generalPanelBicubicScalingSpinner.setToolTipText(scaleLargerThanResolutionToolTip);
            } else {
              if (Util.isUsingFlatLAFTheme()) {
                generalPanelBicubicScalingSpinner.setBorder(new FlatRoundBorder());
              } else {
                generalPanelBicubicScalingSpinner.setBorder(
                    BorderFactory.createEmptyBorder(
                        osScaleMul(2), osScaleMul(2), osScaleMul(2), osScaleMul(2)));
              }
              generalPanelBicubicScalingSpinner.setToolTipText(bicubicScalingSpinnerToolTip);
            }
          }
        });

    SpinnerNumberModel spinnerLimitBicubicScaling =
        new SpinnerNumberModel(
            new Float(1.5f),
            new Float(Renderer.minScalar),
            new Float(Renderer.maxInterpolationScalar),
            new Float(0.1f));
    generalPanelBicubicScalingSpinner.setModel(spinnerLimitBicubicScaling);

    JLabel bicubicInterpolationScalingWarning =
        new JLabel("(May affect performance at high scaling values)");
    bicubicInterpolationScalingWarning.setAlignmentY(0.75f);
    int bicubicInterpolationScalingMargin = isUsingFlatLAFTheme() ? 6 : 2;
    bicubicInterpolationScalingWarning.setBorder(
        BorderFactory.createEmptyBorder(0, osScaleMul(bicubicInterpolationScalingMargin), 0, 0));
    generalPanelBicubicScalingPanel.add(bicubicInterpolationScalingWarning);

    if (isUsingFlatLAFTheme()) {
      generalPanelScalePanel.add(Box.createRigidArea(osScaleMul(new Dimension(0, 5))));
    }
    // End scaling options

    generalPanelCheckUpdates =
        addCheckbox("Check for RSCTimes updates from GitHub at launch", generalPanel);
    generalPanelCheckUpdates.setToolTipText(
        "When enabled, RSCTimes will check for client updates before launching the game and install them when prompted");

    generalPanelWelcomeEnabled =
        addCheckbox(
            "<html><head><style>span{color:red;}</style></head>Remind you how to open the Settings every time you log in <span>(!!! Disable this if you know how to open the settings)</span></html>",
            generalPanel);
    generalPanelWelcomeEnabled.setToolTipText(
        "When enabled, RSCTimes will insert a message telling the current keybinding to open the settings menu and remind you about the tray icon");

    generalPanelCustomCursorCheckbox = addCheckbox("Use custom mouse cursor", generalPanel);
    generalPanelCustomCursorCheckbox.setToolTipText(
        "Switch to using a custom mouse cursor instead of the system default");

    generalPanelShiftScrollCameraRotationCheckbox =
        addCheckbox("Enable camera rotation with compatible trackpads", generalPanel);
    generalPanelShiftScrollCameraRotationCheckbox.setToolTipText(
        "Trackpads that send SHIFT-SCROLL WHEEL when swiping left or right with two fingers will be able to rotate the camera");

    JPanel generalPanelTrackPadRotationPanel = new JPanel();
    generalPanel.add(generalPanelTrackPadRotationPanel);
    generalPanelTrackPadRotationPanel.setLayout(
        new BoxLayout(generalPanelTrackPadRotationPanel, BoxLayout.Y_AXIS));
    generalPanelTrackPadRotationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel generalPanelTrackpadRotationLabel = new JLabel("Camera rotation trackpad sensitivity");
    generalPanelTrackpadRotationLabel.setToolTipText(
        "Sets the camera rotation trackpad sensitivity (Default: 8)");
    generalPanelTrackPadRotationPanel.add(generalPanelTrackpadRotationLabel);
    generalPanelTrackpadRotationLabel.setAlignmentY(1.0f);

    if (Util.isUsingFlatLAFTheme()) {
      generalPanelTrackPadRotationPanel.add(Box.createRigidArea(osScaleMul(new Dimension(0, 10))));
    }

    generalPanelTrackpadRotationSlider = new JSlider();

    generalPanelTrackPadRotationPanel.add(generalPanelTrackpadRotationSlider);
    generalPanelTrackpadRotationSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
    generalPanelTrackpadRotationSlider.setMaximumSize(osScaleMul(new Dimension(200, 55)));
    generalPanelTrackpadRotationSlider.setBorder(
        BorderFactory.createEmptyBorder(0, 0, osScaleMul(10), 0));
    generalPanelTrackpadRotationSlider.setMajorTickSpacing(2);
    generalPanelTrackpadRotationSlider.setMinorTickSpacing(1);
    generalPanelTrackpadRotationSlider.setMinimum(0);
    generalPanelTrackpadRotationSlider.setMaximum(16);
    generalPanelTrackpadRotationSlider.setPaintTicks(true);

    Hashtable<Integer, JLabel> generalPanelTrackpadRotationLabelTable =
        new Hashtable<Integer, JLabel>();
    generalPanelTrackpadRotationLabelTable.put(new Integer(0), new JLabel("0"));
    generalPanelTrackpadRotationLabelTable.put(new Integer(4), new JLabel("4"));
    generalPanelTrackpadRotationLabelTable.put(new Integer(8), new JLabel("8"));
    generalPanelTrackpadRotationLabelTable.put(new Integer(12), new JLabel("12"));
    generalPanelTrackpadRotationLabelTable.put(new Integer(16), new JLabel("16"));
    generalPanelTrackpadRotationSlider.setLabelTable(generalPanelTrackpadRotationLabelTable);
    generalPanelTrackpadRotationSlider.setPaintLabels(true);

    generalPanelAutoScreenshotCheckbox =
        addCheckbox("Take a screenshot when you level up or complete a quest", generalPanel);
    generalPanelAutoScreenshotCheckbox.setToolTipText(
        "Takes a screenshot for you for level ups and quest completion");

    generalPanelDebugModeCheckbox = addCheckbox("Enable debug mode", generalPanel);
    generalPanelDebugModeCheckbox.setToolTipText(
        "Shows debug overlays and enables debug text in the console");

    generalPanelExceptionHandlerCheckbox = addCheckbox("Enable exception handler", generalPanel);
    generalPanelExceptionHandlerCheckbox.setToolTipText(
        "Show's all of RSC's thrown exceptions in the log. (ADVANCED USERS)");
    generalPanelExceptionHandlerCheckbox.setEnabled(false);

    generalPanelPrefersXdgOpenCheckbox =
        addCheckbox("Use xdg-open to open URLs on Linux", generalPanel);
    generalPanelPrefersXdgOpenCheckbox.setToolTipText(
        "Does nothing on Windows or Mac, may improve URL opening experience on Linux");

    /// "Gameplay settings" are settings that can be seen inside the game
    addSettingsHeader(generalPanel, "Gameplay settings");

    // Commented out b/c probably no one will ever implement this
    // generalPanelChatHistoryCheckbox = addCheckbox("Load chat history after relogging (Not
    // implemented yet)", generalPanel);
    // generalPanelChatHistoryCheckbox.setToolTipText("Make chat history persist between logins");
    // generalPanelChatHistoryCheckbox.setEnabled(false); // TODO: Remove this line when chat
    // history is implemented

    generalPanelCombatXPMenuCheckbox =
        addCheckbox("Combat style menu shown outside of combat", generalPanel);
    generalPanelCombatXPMenuCheckbox.setToolTipText(
        "Always show the combat style menu when out of combat");

    generalPanelCombatXPMenuHiddenCheckbox =
        addCheckbox("Combat style menu hidden when in combat", generalPanel);
    generalPanelCombatXPMenuHiddenCheckbox.setToolTipText("Hide combat style menu when in combat");

    generalPanelInventoryFullAlertCheckbox = addCheckbox("Inventory full alert", generalPanel);
    generalPanelInventoryFullAlertCheckbox.setToolTipText(
        "Displays a large notice when the inventory is full");

    generalPanelSortFriendsCheckbox = addCheckbox("Friend online first", generalPanel);
    generalPanelSortFriendsCheckbox.setToolTipText(
        "Sort friends who are online to show at the top of the list");

    generalPanelRoofHidingCheckbox = addCheckbox("Roof hiding", generalPanel);
    generalPanelRoofHidingCheckbox.setToolTipText("Always hide rooftops");

    generalPanelCameraZoomableCheckbox = addCheckbox("Camera zoom enhancement", generalPanel);
    generalPanelCameraZoomableCheckbox.setToolTipText(
        "Zoom the camera in and out with the mouse wheel, and no longer zooms in inside buildings");

    generalPanelCameraRotatableCheckbox = addCheckbox("Camera rotation enhancement", generalPanel);
    generalPanelCameraRotatableCheckbox.setToolTipText(
        "Rotate the camera with middle mouse click, among other things");

    generalPanelCameraMovableCheckbox = addCheckbox("Camera movement enhancement", generalPanel);
    generalPanelCameraMovableCheckbox.setToolTipText(
        "Makes the camera follow the player more closely, and allow camera movement while holding shift, and pressing arrow keys");

    generalPanelCameraMovableRelativeCheckbox =
        addCheckbox("Camera movement is relative to player", generalPanel);
    generalPanelCameraMovableRelativeCheckbox.setToolTipText(
        "Camera movement will follow the player position");

    addSettingsHeader(generalPanel, "Graphical effect changes");

    JPanel generalPanelViewDistancePanel = new JPanel();
    generalPanel.add(generalPanelViewDistancePanel);
    generalPanelViewDistancePanel.setLayout(
        new BoxLayout(generalPanelViewDistancePanel, BoxLayout.Y_AXIS));
    generalPanelViewDistancePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel generalPanelViewDistanceLabel = new JLabel("View distance");
    generalPanelViewDistanceLabel.setToolTipText(
        "Sets the max render distance of structures and landscape");
    generalPanelViewDistancePanel.add(generalPanelViewDistanceLabel);
    generalPanelViewDistanceLabel.setAlignmentY(1.0f);

    if (Util.isUsingFlatLAFTheme()) {
      generalPanelViewDistancePanel.add(Box.createRigidArea(osScaleMul(new Dimension(0, 5))));
    }

    generalPanelViewDistanceSlider = new JSlider();

    generalPanelViewDistancePanel.add(generalPanelViewDistanceSlider);
    generalPanelViewDistanceSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
    generalPanelViewDistanceSlider.setMaximumSize(osScaleMul(new Dimension(350, 55)));
    generalPanelViewDistanceSlider.setBorder(
        BorderFactory.createEmptyBorder(0, 0, osScaleMul(10), 0));
    generalPanelViewDistanceSlider.setMinorTickSpacing(500);
    generalPanelViewDistanceSlider.setMajorTickSpacing(1000);
    generalPanelViewDistanceSlider.setMinimum(2300);
    generalPanelViewDistanceSlider.setMaximum(20000);
    generalPanelViewDistanceSlider.setPaintTicks(true);

    Hashtable<Integer, JLabel> generalPanelViewDistanceLabelTable =
        new Hashtable<Integer, JLabel>();
    generalPanelViewDistanceLabelTable.put(new Integer(2300), new JLabel("2,300"));
    generalPanelViewDistanceLabelTable.put(new Integer(10000), new JLabel("10,000"));
    generalPanelViewDistanceLabelTable.put(new Integer(20000), new JLabel("20,000"));
    generalPanelViewDistanceSlider.setLabelTable(generalPanelViewDistanceLabelTable);
    generalPanelViewDistanceSlider.setPaintLabels(true);
    //////

    // FOV slider
    JPanel generalPanelFoVPanel = new JPanel();
    generalPanel.add(generalPanelFoVPanel);
    generalPanelFoVPanel.setLayout(new BoxLayout(generalPanelFoVPanel, BoxLayout.Y_AXIS));
    generalPanelFoVPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel generalPanelFoVLabel = new JLabel("Field of view (Default 9)");
    generalPanelFoVLabel.setToolTipText("Sets the field of view (not recommended past 10)");
    generalPanelFoVPanel.add(generalPanelFoVLabel);
    generalPanelFoVLabel.setAlignmentY(1.0f);

    if (Util.isUsingFlatLAFTheme()) {
      generalPanelFoVPanel.add(Box.createRigidArea(osScaleMul(new Dimension(0, 5))));
    }

    generalPanelFoVSlider = new JSlider();

    generalPanelFoVPanel.add(generalPanelFoVSlider);
    generalPanelFoVSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
    generalPanelFoVSlider.setMaximumSize(osScaleMul(new Dimension(300, 55)));
    generalPanelFoVSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, osScaleMul(10), 0));
    generalPanelFoVSlider.setMinimum(7);
    generalPanelFoVSlider.setMaximum(16);
    generalPanelFoVSlider.setMajorTickSpacing(1);
    generalPanelFoVSlider.setPaintTicks(true);
    generalPanelFoVSlider.setPaintLabels(true);
    //////

    generalPanelDisableUndergroundLightingCheckbox =
        addCheckbox("Disable underground lighting flicker", generalPanel);
    generalPanelDisableUndergroundLightingCheckbox.setToolTipText(
        "Underground will no longer flicker, basically");

    // FPS limit
    JPanel generalPanelLimitFPSPanel = new JPanel();
    generalPanel.add(generalPanelLimitFPSPanel);
    generalPanelLimitFPSPanel.setLayout(new BoxLayout(generalPanelLimitFPSPanel, BoxLayout.X_AXIS));
    generalPanelLimitFPSPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    generalPanelLimitFPSCheckbox =
        addCheckbox("FPS limit (doubled while F1 interlaced):", generalPanelLimitFPSPanel);
    generalPanelLimitFPSCheckbox.setToolTipText(
        "Limit FPS for a more 2001 feeling (or to save battery)");

    generalPanelLimitFPSSpinner = new JSpinner();
    generalPanelLimitFPSPanel.add(generalPanelLimitFPSSpinner);
    generalPanelLimitFPSSpinner.setMaximumSize(osScaleMul(new Dimension(50, 22)));
    generalPanelLimitFPSSpinner.setMinimumSize(osScaleMul(new Dimension(50, 22)));
    generalPanelLimitFPSSpinner.setAlignmentY(0.75f);
    generalPanelLimitFPSSpinner.setToolTipText("Target FPS");
    generalPanelLimitFPSSpinner.putClientProperty("JComponent.sizeVariant", "mini");
    generalPanelLimitFPSSpinner.setEnabled(false);

    // Sanitize JSpinner value
    SpinnerNumberModel spinnerLimitFpsModel = new SpinnerNumberModel();
    spinnerLimitFpsModel.setMinimum(1);
    spinnerLimitFpsModel.setMaximum(50);
    spinnerLimitFpsModel.setValue(10);
    spinnerLimitFpsModel.setStepSize(1);
    generalPanelLimitFPSSpinner.setModel(spinnerLimitFpsModel);
    //////

    addSettingsHeader(generalPanel, "Menu/Item patching");

    generalPanelBypassAttackCheckbox = addCheckbox("Always left click to attack", generalPanel);
    generalPanelBypassAttackCheckbox.setToolTipText(
        "Left click attack monsters regardless of level difference");

    JPanel generalPanelNamePatchModePanel = new JPanel();
    generalPanelNamePatchModePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    generalPanelNamePatchModePanel.setMaximumSize(osScaleMul(new Dimension(300, 85)));
    generalPanelNamePatchModePanel.setLayout(
        new BoxLayout(generalPanelNamePatchModePanel, BoxLayout.X_AXIS));
    generalPanel.add(generalPanelNamePatchModePanel);

    generalPanelNamePatchModeSlider = new JSlider();
    generalPanelNamePatchModeSlider.setMajorTickSpacing(1);
    generalPanelNamePatchModeSlider.setPaintLabels(true);
    generalPanelNamePatchModeSlider.setPaintTicks(true);
    generalPanelNamePatchModeSlider.setSnapToTicks(true);
    generalPanelNamePatchModeSlider.setMinimum(0);
    generalPanelNamePatchModeSlider.setMaximum(3);
    generalPanelNamePatchModeSlider.setPreferredSize(osScaleMul(new Dimension(40, 0)));
    generalPanelNamePatchModeSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
    generalPanelNamePatchModeSlider.setBorder(
        BorderFactory.createEmptyBorder(0, 0, osScaleMul(10), 0));
    generalPanelNamePatchModeSlider.setOrientation(SwingConstants.VERTICAL);
    generalPanelNamePatchModePanel.add(generalPanelNamePatchModeSlider);
    generalPanelNamePatchModeSlider.setEnabled(false);

    JPanel generalPanelNamePatchModeTextPanel = new JPanel();
    generalPanelNamePatchModeTextPanel.setPreferredSize(osScaleMul(new Dimension(255, 80)));
    generalPanelNamePatchModeTextPanel.setLayout(new BorderLayout());
    generalPanelNamePatchModeTextPanel.setBorder(
        BorderFactory.createEmptyBorder(0, osScaleMul(10), 0, 0));
    generalPanelNamePatchModePanel.add(generalPanelNamePatchModeTextPanel);

    JLabel generalPanelNamePatchModeTitle =
        new JLabel("<html><b>Item name patch mode</b> (Requires restart)</html>");
    generalPanelNamePatchModeTitle.setToolTipText(
        "Replace certain item names with improved versions");
    generalPanelNamePatchModeTextPanel.add(generalPanelNamePatchModeTitle, BorderLayout.PAGE_START);
    generalPanelNamePatchModeDesc = new JLabel("");
    generalPanelNamePatchModeTextPanel.add(generalPanelNamePatchModeDesc, BorderLayout.CENTER);
    generalPanelNamePatchModeDesc.setEnabled(false);
    generalPanelNamePatchModeTitle.setEnabled(false);

    generalPanelNamePatchModeSlider.addChangeListener(
        new ChangeListener() {

          @Override
          public void stateChanged(ChangeEvent e) {
            switch (generalPanelNamePatchModeSlider.getValue()) {
              case 3:
                generalPanelNamePatchModeDesc.setText(
                    "<html>Reworded vague stuff to be more descriptive on top of type 1 & 2 changes</html>");
                break;
              case 2:
                generalPanelNamePatchModeDesc.setText(
                    "<html>Capitalizations and fixed spellings on top of type 1 changes</html>");
                break;
              case 1:
                generalPanelNamePatchModeDesc.setText(
                    "<html>Purely practical name changes (potion dosages, unidentified herbs, unfinished potions)</html>");
                break;
              case 0:
                generalPanelNamePatchModeDesc.setText("<html>No item name patching</html>");
                break;
              default:
                Logger.Error("Invalid name patch mode value");
                break;
            }
          }
        });

    generalPanelPatchGenderCheckbox =
        addCheckbox(
            "Correct \"Gender\" to \"Body Type\" on the appearance screen (Requires restart)",
            generalPanel);
    generalPanelPatchGenderCheckbox.setToolTipText(
        "When selected, says \"Body Type\" instead of \"Gender\" on the character creation/appearance screen");
    generalPanelPatchGenderCheckbox.setEnabled(false);

    generalPanelPatchHbar512LastPixelCheckbox =
        addCheckbox("Fix bottom bar's last pixel at 512 width", generalPanel);
    generalPanelPatchHbar512LastPixelCheckbox.setToolTipText(
        "Even in the very last versions of the client, the horizontal blue bar at the bottom was still misaligned so that 1 pixel shines through at the end");
    generalPanelPatchHbar512LastPixelCheckbox.setEnabled(false);

    // Logger settings

    addSettingsHeader(generalPanel, "Logging settings");

    JPanel generalPanelLogVerbosityPanel = new JPanel();
    generalPanelLogVerbosityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    generalPanelLogVerbosityPanel.setMaximumSize(osScaleMul(new Dimension(350, 128)));
    generalPanelLogVerbosityPanel.setLayout(
        new BoxLayout(generalPanelLogVerbosityPanel, BoxLayout.Y_AXIS));
    generalPanel.add(generalPanelLogVerbosityPanel);

    JLabel generalPanelLogVerbosityTitle = new JLabel("Log verbosity maximum");
    generalPanelLogVerbosityTitle.setToolTipText(
        "What max level of log text will be shown in the RSCTimes log/console");
    generalPanelLogVerbosityPanel.add(generalPanelLogVerbosityTitle);
    generalPanelLogVerbosityTitle.setAlignmentY(1.0f);

    Hashtable<Integer, JLabel> generalPanelLogVerbosityLabelTable =
        new Hashtable<Integer, JLabel>();
    generalPanelLogVerbosityLabelTable.put(new Integer(0), new JLabel("Error"));
    generalPanelLogVerbosityLabelTable.put(new Integer(1), new JLabel("Warning"));
    generalPanelLogVerbosityLabelTable.put(new Integer(2), new JLabel("Game"));
    generalPanelLogVerbosityLabelTable.put(new Integer(3), new JLabel("Info"));
    generalPanelLogVerbosityLabelTable.put(new Integer(4), new JLabel("Debug"));
    generalPanelLogVerbosityLabelTable.put(new Integer(5), new JLabel("Opcode"));

    if (Util.isUsingFlatLAFTheme()) {
      generalPanelLogVerbosityPanel.add(Box.createRigidArea(osScaleMul(new Dimension(0, 5))));
    }

    generalPanelLogVerbositySlider = new JSlider();
    generalPanelLogVerbositySlider.setMajorTickSpacing(1);
    generalPanelLogVerbositySlider.setLabelTable(generalPanelLogVerbosityLabelTable);
    generalPanelLogVerbositySlider.setPaintLabels(true);
    generalPanelLogVerbositySlider.setPaintTicks(true);
    generalPanelLogVerbositySlider.setSnapToTicks(true);
    generalPanelLogVerbositySlider.setMinimum(0);
    generalPanelLogVerbositySlider.setMaximum(5);
    generalPanelLogVerbositySlider.setPreferredSize(osScaleMul(new Dimension(200, 55)));
    generalPanelLogVerbositySlider.setAlignmentX(Component.LEFT_ALIGNMENT);
    generalPanelLogVerbositySlider.setBorder(
        BorderFactory.createEmptyBorder(0, 0, osScaleMul(5), 0));
    generalPanelLogVerbositySlider.setOrientation(SwingConstants.HORIZONTAL);
    generalPanelLogVerbosityPanel.add(generalPanelLogVerbositySlider);

    generalPanelLogTimestampsCheckbox = addCheckbox("Show timestamps in log", generalPanel);
    generalPanelLogTimestampsCheckbox.setToolTipText(
        "Displays the time text was output to the log");

    generalPanelLogLevelCheckbox = addCheckbox("Show log level in log", generalPanel);
    generalPanelLogLevelCheckbox.setToolTipText("Displays the log level of output in the log");

    generalPanelLogForceTimestampsCheckbox = addCheckbox("Force timestamps in log", generalPanel);
    generalPanelLogForceTimestampsCheckbox.setToolTipText(
        "Forces display of the time text was output to the log");

    generalPanelLogForceLevelCheckbox = addCheckbox("Force log level in log", generalPanel);
    generalPanelLogForceLevelCheckbox.setToolTipText(
        "Forces display of the log level of output in the log");

    generalPanelColoredTextCheckbox = addCheckbox("Coloured console text", generalPanel);
    generalPanelColoredTextCheckbox.setToolTipText(
        "When running the client from a console, chat messages in the console will reflect the colours they are in game");

    // UI Settings
    addSettingsHeader(generalPanel, "UI settings");

    generalPanelUseDarkModeCheckbox =
        addCheckbox(
            "Use dark mode for the interface (Requires restart & modern UI theme)", generalPanel);
    generalPanelUseDarkModeCheckbox.setToolTipText(
        "Uses the darker UI theme, unless the legacy theme is enabled");

    generalPanelUseNimbusThemeCheckbox =
        addCheckbox("Use legacy RSCx UI theme (Requires restart)", generalPanel);
    generalPanelUseNimbusThemeCheckbox.setToolTipText("Uses the legacy RSCx Nimbus look-and-feel");

    if (Util.isModernWindowsOS() && Launcher.OSScalingFactor != 1.0) {
      generalPanelUseNimbusThemeCheckbox.setEnabled(false);
      generalPanelUseNimbusThemeCheckbox.setText(
          "<html><strike>Use legacy RSCx UI theme</strike> You must disable OS level scaling in Windows to enable this option (Requires restart)</html>");
    }

    addPanelBottomGlue(generalPanel);

    /*
     * Overlays tab
     */

    overlayPanel.setLayout(new BoxLayout(overlayPanel, BoxLayout.Y_AXIS));

    /// "Interface Overlays" are overlays that have a constant position on
    /// the screen because they are designed to modify just the interface of RSC×
    addSettingsHeader(overlayPanel, "Interface Overlays");
    overlayPanelStatusDisplayCheckbox = addCheckbox("Show HP display", overlayPanel);
    overlayPanelStatusDisplayCheckbox.setToolTipText("Toggle hits display");

    overlayPanelBuffsCheckbox =
        addCheckbox("Show combat (de)buffs and cooldowns display", overlayPanel);
    overlayPanelBuffsCheckbox.setToolTipText("Toggle combat (de)buffs and cooldowns display");

    overlayPanelLastMenuActionCheckbox = addCheckbox("Show last menu action display", overlayPanel);
    overlayPanelLastMenuActionCheckbox.setToolTipText("Toggle last menu action used display");
    overlayPanelLastMenuActionCheckbox.setEnabled(false);

    overlayPanelMouseTooltipCheckbox =
        addCheckbox("Show mouse hover action at mouse cursor", overlayPanel);
    overlayPanelMouseTooltipCheckbox.setToolTipText(
        "Shows important actions from the text at the top left of the game near the mouse cursor");

    overlayPanelExtendedTooltipCheckbox =
        addCheckbox("Extend mouse hover action at mouse cursor", overlayPanel);
    overlayPanelExtendedTooltipCheckbox.setToolTipText(
        "Shows the text at the top left of the game near the mouse cursor");

    overlayPanelInvCountCheckbox = addCheckbox("Display inventory count", overlayPanel);
    overlayPanelInvCountCheckbox.setToolTipText("Shows the number of items in your inventory");

    overlayPanelRscTimesButtonsFunctionalCheckbox =
        addCheckbox("Able to click in-game buttons to activate RSC× features", overlayPanel);
    overlayPanelRscTimesButtonsFunctionalCheckbox.setToolTipText(
        "Able to click in-game buttons to activate RSC× features");

    JPanel overlayPanelRscTimesButtonsPanel = new JPanel();
    overlayPanel.add(overlayPanelRscTimesButtonsPanel);
    overlayPanelRscTimesButtonsPanel.setLayout(
        new BoxLayout(overlayPanelRscTimesButtonsPanel, BoxLayout.X_AXIS));
    overlayPanelRscTimesButtonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    overlayPanelRscTimesButtonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    JLabel rsctimesButtonsSpacingLabel = new JLabel("");
    rsctimesButtonsSpacingLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, osScaleMul(20)));
    overlayPanelRscTimesButtonsPanel.add(rsctimesButtonsSpacingLabel);
    overlayPanelRscTimesButtonsCheckbox =
        addCheckbox(
            "Also display x indicators over the in-game buttons", overlayPanelRscTimesButtonsPanel);
    overlayPanelRscTimesButtonsCheckbox.setToolTipText("Display x indicators over in-game buttons");

    overlayPanelWikiLookupOnMagicBookCheckbox =
        addCheckbox("Search the RSC Wiki by first clicking on the Magic Book", overlayPanel);
    overlayPanelWikiLookupOnMagicBookCheckbox.setToolTipText(
        "Click the spell book, then click on anything else, and it will look it up on the RSC wiki.");

    overlayPanelWikiLookupOnHbarCheckbox =
        addCheckbox("Search the RSC Wiki with a button in the bottom blue bar", overlayPanel);
    overlayPanelWikiLookupOnHbarCheckbox.setToolTipText(
        "Click the button on the bottom bar, then click on anything else, and it will look it up on the RSC wiki.");

    overlayPanelToggleXPBarOnStatsButtonCheckbox =
        addCheckbox("Display the Goal bar when clicking on the Stats button", overlayPanel);
    overlayPanelToggleXPBarOnStatsButtonCheckbox.setToolTipText(
        "Clicking shows/hides the Goal Bar");

    overlayPanelToggleMotivationalQuotesCheckbox =
        addCheckbox(
            "Right click on the Friends button to display a motivational quote", overlayPanel);
    overlayPanelToggleMotivationalQuotesCheckbox.setToolTipText(
        "Motivational quotes are displayed when you need motivation.");

    /// XP Bar
    addSettingsHeader(overlayPanel, "XP Bar");
    overlayPanelXPBarCheckbox = addCheckbox("Show a Goal bar", overlayPanel);
    overlayPanelXPBarCheckbox.setToolTipText("Show a Goal bar to the left of the wrench");

    ButtonGroup XPAlignButtonGroup = new ButtonGroup();
    overlayPanelXPRightAlignFocusButton =
        addRadioButton("Display on the right", overlayPanel, osScaleMul(20));
    overlayPanelXPRightAlignFocusButton.setToolTipText(
        "The Goal bar will be shown just left of the Settings menu.");
    overlayPanelXPCenterAlignFocusButton =
        addRadioButton("Display in the center", overlayPanel, osScaleMul(20));
    overlayPanelXPCenterAlignFocusButton.setToolTipText(
        "The Goal bar will be shown at the top-middle of the screen.");
    XPAlignButtonGroup.add(overlayPanelXPRightAlignFocusButton);
    XPAlignButtonGroup.add(overlayPanelXPCenterAlignFocusButton);

    overlayPanelPositionCheckbox = addCheckbox("Display position", overlayPanel);
    overlayPanelPositionCheckbox.setToolTipText("Shows the player's global position");

    overlayPanelHideFpsCheckbox = addCheckbox("Hide FPS like newer RSC", overlayPanel);
    overlayPanelHideFpsCheckbox.setToolTipText(
        "Hides the FPS like it would occur in newer RSC versions");

    overlayPanelShowCombatInfoCheckbox = addCheckbox("Show NPC HP info", overlayPanel);
    overlayPanelShowCombatInfoCheckbox.setToolTipText(
        "Shows the HP info for the NPC you're in combat with");

    overlayPanelUsePercentageCheckbox = addCheckbox("Use percentage for NPC HP info", overlayPanel);
    overlayPanelUsePercentageCheckbox.setToolTipText(
        "Uses percentage for NPC HP info instead of actual HP");

    overlayPanelLagIndicatorCheckbox = addCheckbox("Lag indicator", overlayPanel);
    overlayPanelLagIndicatorCheckbox.setToolTipText(
        "When there's a problem with your connection, RSCTimes will tell you in the bottom right");

    overlayPanelFoodHealingCheckbox =
        addCheckbox("Show food healing overlay (Not implemented yet)", overlayPanel);
    overlayPanelFoodHealingCheckbox.setToolTipText(
        "When hovering on food, shows the HP a consumable recovers");
    // TODO: Remove this line when food healing overlay is implemented
    overlayPanelFoodHealingCheckbox.setEnabled(false);

    overlayPanelHPRegenTimerCheckbox =
        addCheckbox("Display time until next HP regeneration (Not implemented yet)", overlayPanel);
    overlayPanelHPRegenTimerCheckbox.setToolTipText(
        "Shows the seconds until your HP will naturally regenerate");
    // TODO: Remove this line when the HP regen timer is implemented
    overlayPanelHPRegenTimerCheckbox.setEnabled(false);

    /// "In World" Overlays move with the camera, and modify objects that the are rendered in the
    // world
    addSettingsHeader(overlayPanel, "\"In World\" Overlays");
    overlayPanelHitboxCheckbox =
        addCheckbox("Show hitboxes around NPCs, players, and items", overlayPanel);
    overlayPanelHitboxCheckbox.setToolTipText(
        "Shows the clickable areas on NPCs, players, and items");

    overlayPanelPlayerNamesCheckbox =
        addCheckbox("Show player names over their heads", overlayPanel);
    overlayPanelPlayerNamesCheckbox.setToolTipText(
        "Shows players' display names over their character");

    overlayPanelFriendNamesCheckbox =
        addCheckbox("Show nearby friend names over their heads", overlayPanel);
    overlayPanelFriendNamesCheckbox.setToolTipText(
        "Shows your friends' display names over their character");

    // even the animated axe has an "axe head". All NPCs have a head until proven otherwise
    overlayPanelNPCNamesCheckbox = addCheckbox("Show NPC names over their heads", overlayPanel);
    overlayPanelNPCNamesCheckbox.setToolTipText("Shows NPC names over the NPC");

    overlayPanelIDsCheckbox = addCheckbox("Extend names by showing IDs", overlayPanel);
    overlayPanelIDsCheckbox.setToolTipText(
        "Displays IDs of NPCs and Players if their name overlay is present");

    overlayPanelObjectInfoCheckbox = addCheckbox("Trace object info", overlayPanel);
    overlayPanelObjectInfoCheckbox.setToolTipText(
        "Displays object information after their name on the right click examine");

    overlayPanelItemNamesCheckbox =
        addCheckbox("Display the names of items on the ground", overlayPanel);
    overlayPanelItemNamesCheckbox.setToolTipText("Shows the names of dropped items");

    int itemsTextHeight = isUsingFlatLAFTheme() ? 32 : 37;

    // Blocked Items
    JPanel blockedItemsPanel = new JPanel();
    overlayPanel.add(blockedItemsPanel);
    blockedItemsPanel.setLayout(new BoxLayout(blockedItemsPanel, BoxLayout.X_AXIS));
    blockedItemsPanel.setPreferredSize(osScaleMul(new Dimension(0, itemsTextHeight)));
    blockedItemsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    blockedItemsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, osScaleMul(9), 0));

    JLabel blockedItemsPanelNameLabel = new JLabel("Blocked items: ");
    blockedItemsPanel.add(blockedItemsPanelNameLabel);
    blockedItemsPanelNameLabel.setAlignmentY(0.9f);

    blockedItemsTextField = new JTextField();
    blockedItemsPanel.add(blockedItemsTextField);
    blockedItemsTextField.setMinimumSize(osScaleMul(new Dimension(100, 28)));
    blockedItemsTextField.setMaximumSize(new Dimension(Short.MAX_VALUE, osScaleMul(28)));
    blockedItemsTextField.setAlignmentY(0.75f);

    // Highlighted Items
    JPanel highlightedItemsPanel = new JPanel();
    overlayPanel.add(highlightedItemsPanel);
    highlightedItemsPanel.setLayout(new BoxLayout(highlightedItemsPanel, BoxLayout.X_AXIS));
    highlightedItemsPanel.setPreferredSize(osScaleMul(new Dimension(0, itemsTextHeight)));
    highlightedItemsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    highlightedItemsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, osScaleMul(9), 0));

    JLabel highlightedItemsPanelNameLabel = new JLabel("Highlighted items: ");
    highlightedItemsPanel.add(highlightedItemsPanelNameLabel);
    highlightedItemsPanelNameLabel.setAlignmentY(0.9f);

    highlightedItemsTextField = new JTextField();
    highlightedItemsPanel.add(highlightedItemsTextField);
    highlightedItemsTextField.setMinimumSize(osScaleMul(new Dimension(100, 28)));
    highlightedItemsTextField.setMaximumSize(new Dimension(Short.MAX_VALUE, osScaleMul(28)));
    highlightedItemsTextField.setAlignmentY(0.75f);

    addPanelBottomGlue(overlayPanel);

    /*
     * Notifications tab
     */

    notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));

    addSettingsHeader(notificationPanel, "Notification Settings");

    notificationPanelTrayPopupCheckbox =
        addCheckbox("Enable notification tray popups", notificationPanel);
    notificationPanelTrayPopupCheckbox.setBorder(
        BorderFactory.createEmptyBorder(0, 0, osScaleMul(7), 0));
    notificationPanelTrayPopupCheckbox.setToolTipText(
        "Shows a system notification when a notification is triggered");

    ButtonGroup trayPopupButtonGroup = new ButtonGroup();
    notificationPanelTrayPopupClientFocusButton =
        addRadioButton("Only when client is not focused", notificationPanel, osScaleMul(20));
    notificationPanelTrayPopupAnyFocusButton =
        addRadioButton("Regardless of client focus", notificationPanel, osScaleMul(20));
    trayPopupButtonGroup.add(notificationPanelTrayPopupClientFocusButton);
    trayPopupButtonGroup.add(notificationPanelTrayPopupAnyFocusButton);

    notificationPanelNotifSoundsCheckbox =
        addCheckbox("Enable notification sounds", notificationPanel);
    notificationPanelNotifSoundsCheckbox.setBorder(
        BorderFactory.createEmptyBorder(0, 0, osScaleMul(7), 0));
    notificationPanelNotifSoundsCheckbox.setToolTipText(
        "Plays a sound when a notification is triggered");

    ButtonGroup notifSoundButtonGroup = new ButtonGroup();
    notificationPanelNotifSoundClientFocusButton =
        addRadioButton("Only when client is not focused", notificationPanel, osScaleMul(20));
    notificationPanelNotifSoundAnyFocusButton =
        addRadioButton("Regardless of client focus", notificationPanel, osScaleMul(20));
    notifSoundButtonGroup.add(notificationPanelNotifSoundClientFocusButton);
    notifSoundButtonGroup.add(notificationPanelNotifSoundAnyFocusButton);

    if (SystemTray.isSupported())
      notificationPanelUseSystemNotifsCheckbox =
          addCheckbox("Use system notifications if available", notificationPanel);
    else {
      notificationPanelUseSystemNotifsCheckbox =
          addCheckbox("Use system notifications if available (INCOMPATIBLE OS)", notificationPanel);
      notificationPanelUseSystemNotifsCheckbox.setEnabled(false);
    }
    notificationPanelUseSystemNotifsCheckbox.setToolTipText(
        "Uses built-in system notifications. Enable this to attempt to use your operating system's notification system instead of the built-in pop-up");

    addSettingsHeader(notificationPanel, "Notifications");

    notificationPanelPMNotifsCheckbox = addCheckbox("Enable PM notifications", notificationPanel);
    notificationPanelPMNotifsCheckbox.setBorder(
        BorderFactory.createEmptyBorder(0, 0, osScaleMul(7), 0));
    notificationPanelPMNotifsCheckbox.setToolTipText(
        "Shows a system notification when a PM is received");

    notificationPanelTradeNotifsCheckbox =
        addCheckbox("Enable trade notifications", notificationPanel);
    notificationPanelTradeNotifsCheckbox.setToolTipText(
        "Shows a system notification when a trade request is received");

    notificationPanelUnderAttackNotifsCheckbox =
        addCheckbox("Enable PVP notifications", notificationPanel);
    notificationPanelUnderAttackNotifsCheckbox.setToolTipText(
        "Shows a system notification when a player attacks you");

    JPanel notificationPanelLowHPNotifsPanel = new JPanel();
    notificationPanel.add(notificationPanelLowHPNotifsPanel);
    notificationPanelLowHPNotifsPanel.setLayout(
        new BoxLayout(notificationPanelLowHPNotifsPanel, BoxLayout.X_AXIS));
    notificationPanelLowHPNotifsPanel.setPreferredSize(osScaleMul(new Dimension(0, 28)));
    notificationPanelLowHPNotifsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    notificationPanelLowHPNotifsCheckbox =
        addCheckbox("Enable low HP notification at", notificationPanelLowHPNotifsPanel);
    notificationPanelLowHPNotifsCheckbox.setToolTipText(
        "Shows a system notification when your HP drops below the specified value");

    notificationPanelLowHPNotifsSpinner = new JSpinner();
    notificationPanelLowHPNotifsSpinner.setMaximumSize(osScaleMul(new Dimension(55, 22)));
    notificationPanelLowHPNotifsSpinner.setMinimumSize(osScaleMul(new Dimension(55, 22)));
    notificationPanelLowHPNotifsSpinner.setAlignmentY(0.75f);
    notificationPanelLowHPNotifsPanel.add(notificationPanelLowHPNotifsSpinner);
    notificationPanelLowHPNotifsSpinner.putClientProperty("JComponent.sizeVariant", "mini");

    JLabel notificationPanelLowHPNotifsEndLabel = new JLabel("% HP");
    notificationPanelLowHPNotifsPanel.add(notificationPanelLowHPNotifsEndLabel);
    notificationPanelLowHPNotifsEndLabel.setAlignmentY(0.8f);
    notificationPanelLowHPNotifsEndLabel.setBorder(
        BorderFactory.createEmptyBorder(0, osScaleMul(2), 0, 0));

    // Sanitize JSpinner values
    SpinnerNumberModel spinnerHPNumModel = new SpinnerNumberModel();
    spinnerHPNumModel.setMinimum(1);
    spinnerHPNumModel.setMaximum(99);
    spinnerHPNumModel.setValue(25);
    notificationPanelLowHPNotifsSpinner.setModel(spinnerHPNumModel);

    // Sanitize JSpinner values
    JPanel warnHighlightedOnGroundPanel = new JPanel();
    notificationPanel.add(warnHighlightedOnGroundPanel);
    warnHighlightedOnGroundPanel.setLayout(
        new BoxLayout(warnHighlightedOnGroundPanel, BoxLayout.X_AXIS));
    warnHighlightedOnGroundPanel.setPreferredSize(osScaleMul(new Dimension(0, 28)));
    warnHighlightedOnGroundPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    // TODO: implement feature, remove "setEnabled(false)" below
    notificationPanelHighlightedItemTimerCheckbox =
        addCheckbox(
            "Warn if one of your highlighted items has been on the ground for more than",
            warnHighlightedOnGroundPanel);
    notificationPanelHighlightedItemTimerCheckbox.setToolTipText(
        "Highlighted items can be configured in the Overlays tab");
    notificationPanelHighlightedItemTimerCheckbox.setEnabled(false);

    JLabel highlightedItemsSuggestionJLabel =
        new JLabel(
            "<html><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                + "<strong>Note:</strong> Loot from kills despawns after about 2 minutes."
                + "</p></html>");
    notificationPanel.add(highlightedItemsSuggestionJLabel);
    highlightedItemsSuggestionJLabel.setBorder(
        BorderFactory.createEmptyBorder(0, 0, osScaleMul(8), 0));
    highlightedItemsSuggestionJLabel.setEnabled(false);

    notificationPanelHighlightedItemTimerSpinner = new JSpinner();
    notificationPanelHighlightedItemTimerSpinner.setMaximumSize(osScaleMul(new Dimension(65, 22)));
    notificationPanelHighlightedItemTimerSpinner.setMinimumSize(osScaleMul(new Dimension(65, 22)));
    notificationPanelHighlightedItemTimerSpinner.setAlignmentY(0.75f);
    warnHighlightedOnGroundPanel.add(notificationPanelHighlightedItemTimerSpinner);
    notificationPanelHighlightedItemTimerSpinner.putClientProperty(
        "JComponent.sizeVariant", "mini");
    notificationPanelHighlightedItemTimerSpinner.setEnabled(false);

    JLabel notificationPanelHighlightedItemEndLabel = new JLabel("seconds");
    warnHighlightedOnGroundPanel.add(notificationPanelHighlightedItemEndLabel);
    notificationPanelHighlightedItemEndLabel.setAlignmentY(0.8f);
    int secondsMargin = isUsingFlatLAFTheme() ? 4 : 2;
    notificationPanelHighlightedItemEndLabel.setBorder(
        BorderFactory.createEmptyBorder(0, osScaleMul(secondsMargin), 0, 0));
    notificationPanelHighlightedItemEndLabel.setEnabled(false);

    // Sanitize JSpinner values
    SpinnerNumberModel highlightedItemSecondsModel = new SpinnerNumberModel();
    highlightedItemSecondsModel.setMinimum(0);
    highlightedItemSecondsModel.setMaximum(630); // 10.5 minutes max
    highlightedItemSecondsModel.setValue(100);
    notificationPanelHighlightedItemTimerSpinner.setModel(highlightedItemSecondsModel);

    addPanelBottomGlue(notificationPanel);

    /*
     * Streaming & Privacy tab
     */

    streamingPanel.setLayout(new BoxLayout(streamingPanel, BoxLayout.Y_AXIS));

    addSettingsHeader(streamingPanel, "Streaming & Privacy");

    streamingPanelTwitchChatIntegrationEnabledCheckbox =
        addCheckbox("Enable Twitch chat integration", streamingPanel);
    streamingPanelTwitchChatIntegrationEnabledCheckbox.setToolTipText(
        "If this box is checked, and the 3 relevant text fields are filled out, you will connect to a chat channel on login.");

    streamingPanelTwitchChatCheckbox = addCheckbox("Hide incoming Twitch chat", streamingPanel);
    streamingPanelTwitchChatCheckbox.setToolTipText(
        "Don't show chat from other Twitch users, but still be able to send Twitch chat");

    int twitchTextHeight = isUsingFlatLAFTheme() ? 32 : 37;

    JPanel streamingPanelTwitchChannelNamePanel = new JPanel();
    streamingPanel.add(streamingPanelTwitchChannelNamePanel);
    streamingPanelTwitchChannelNamePanel.setLayout(
        new BoxLayout(streamingPanelTwitchChannelNamePanel, BoxLayout.X_AXIS));
    streamingPanelTwitchChannelNamePanel.setPreferredSize(
        osScaleMul(new Dimension(0, twitchTextHeight)));
    streamingPanelTwitchChannelNamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    streamingPanelTwitchChannelNamePanel.setBorder(
        BorderFactory.createEmptyBorder(0, 0, osScaleMul(7), 0));

    JLabel streamingPanelTwitchChannelNameLabel = new JLabel("Twitch channel to chat in: ");
    streamingPanelTwitchChannelNameLabel.setToolTipText("The Twitch channel you want to chat in");
    streamingPanelTwitchChannelNamePanel.add(streamingPanelTwitchChannelNameLabel);
    streamingPanelTwitchChannelNameLabel.setAlignmentY(0.9f);

    streamingPanelTwitchChannelNameTextField = new JTextField();
    streamingPanelTwitchChannelNamePanel.add(streamingPanelTwitchChannelNameTextField);
    streamingPanelTwitchChannelNameTextField.setMinimumSize(osScaleMul(new Dimension(100, 28)));
    streamingPanelTwitchChannelNameTextField.setMaximumSize(
        new Dimension(Short.MAX_VALUE, osScaleMul(28)));
    streamingPanelTwitchChannelNameTextField.setAlignmentY(0.75f);

    JPanel streamingPanelTwitchUserPanel = new JPanel();
    streamingPanel.add(streamingPanelTwitchUserPanel);
    streamingPanelTwitchUserPanel.setLayout(
        new BoxLayout(streamingPanelTwitchUserPanel, BoxLayout.X_AXIS));
    streamingPanelTwitchUserPanel.setPreferredSize(osScaleMul(new Dimension(0, twitchTextHeight)));
    streamingPanelTwitchUserPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    streamingPanelTwitchUserPanel.setBorder(
        BorderFactory.createEmptyBorder(0, 0, osScaleMul(7), 0));

    JLabel streamingPanelTwitchUserLabel = new JLabel("Your Twitch username: ");
    streamingPanelTwitchUserLabel.setToolTipText("The Twitch username you log into Twitch with");
    streamingPanelTwitchUserPanel.add(streamingPanelTwitchUserLabel);
    streamingPanelTwitchUserLabel.setAlignmentY(0.9f);

    streamingPanelTwitchUserTextField = new JTextField();
    streamingPanelTwitchUserPanel.add(streamingPanelTwitchUserTextField);
    streamingPanelTwitchUserTextField.setMinimumSize(osScaleMul(new Dimension(100, 28)));
    streamingPanelTwitchUserTextField.setMaximumSize(
        new Dimension(Short.MAX_VALUE, osScaleMul(28)));
    streamingPanelTwitchUserTextField.setAlignmentY(0.75f);

    JPanel streamingPanelTwitchOAuthPanel = new JPanel();
    streamingPanel.add(streamingPanelTwitchOAuthPanel);
    streamingPanelTwitchOAuthPanel.setLayout(
        new BoxLayout(streamingPanelTwitchOAuthPanel, BoxLayout.X_AXIS));
    streamingPanelTwitchOAuthPanel.setPreferredSize(osScaleMul(new Dimension(0, twitchTextHeight)));
    streamingPanelTwitchOAuthPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    streamingPanelTwitchOAuthPanel.setBorder(
        BorderFactory.createEmptyBorder(0, 0, osScaleMul(7), 0));

    JLabel streamingPanelTwitchOAuthLabel =
        new JLabel("Your Twitch OAuth token (not your Stream key): ");
    streamingPanelTwitchOAuthLabel.setToolTipText("Your Twitch OAuth token (not your Stream Key)");
    streamingPanelTwitchOAuthPanel.add(streamingPanelTwitchOAuthLabel);
    streamingPanelTwitchOAuthLabel.setAlignmentY(0.9f);

    streamingPanelTwitchOAuthTextField = new JPasswordField();
    streamingPanelTwitchOAuthPanel.add(streamingPanelTwitchOAuthTextField);
    streamingPanelTwitchOAuthTextField.setMinimumSize(osScaleMul(new Dimension(100, 28)));
    streamingPanelTwitchOAuthTextField.setMaximumSize(
        new Dimension(Short.MAX_VALUE, osScaleMul(28)));
    streamingPanelTwitchOAuthTextField.setAlignmentY(0.75f);

    streamingPanelSaveLoginCheckbox =
        addCheckbox("Save login information between logins (Requires restart)", streamingPanel);
    streamingPanelSaveLoginCheckbox.setToolTipText(
        "Preserves login details between logins (Disable this if you're streaming)");

    streamingPanelStartLoginCheckbox = addCheckbox("Start game at login screen", streamingPanel);
    streamingPanelStartLoginCheckbox.setToolTipText(
        "Starts the game at the login screen and return to it on logout");

    addPanelBottomGlue(streamingPanel);

    /*
     * Keybind tab
     */
    JPanel keybindContainerPanel = new JPanel(new GridBagLayout());

    JPanel keybindContainerContainerPanel = new JPanel(new GridBagLayout());
    GridBagConstraints con = new GridBagConstraints();
    con.gridy = 0;
    con.gridx = 0;
    con.fill = GridBagConstraints.HORIZONTAL;

    GridBagConstraints gbl_constraints = new GridBagConstraints();
    gbl_constraints.fill = GridBagConstraints.HORIZONTAL;
    gbl_constraints.anchor = GridBagConstraints.FIRST_LINE_START;
    gbl_constraints.weightx = 1;
    gbl_constraints.ipadx = 20;
    gbl_constraints.gridy = 0;
    gbl_constraints.gridwidth = 3;

    // Note: CTRL + every single letter on the keyboard is now used
    // consider using ALT instead.

    addKeybindCategory(keybindContainerPanel, "General");
    // addKeybindSet(keybindContainerPanel, "Logout", "logout", KeyModifier.CTRL, KeyEvent.VK_L);
    addKeybindSet(
        keybindContainerPanel, "Take screenshot", "screenshot", KeyModifier.CTRL, KeyEvent.VK_S);
    addKeybindSet(
        keybindContainerPanel, "Toggle scaling", "toggle_scaling", KeyModifier.ALT, KeyEvent.VK_S);
    addKeybindSet(
        keybindContainerPanel, "Increase scale", "increase_scale", KeyModifier.ALT, KeyEvent.VK_UP);
    addKeybindSet(
        keybindContainerPanel,
        "Decrease scale",
        "decrease_scale",
        KeyModifier.ALT,
        KeyEvent.VK_DOWN);
    addKeybindSet(
        keybindContainerPanel,
        "Show settings window",
        "show_config_window",
        KeyModifier.CTRL,
        KeyEvent.VK_O);
    addKeybindSet(
        keybindContainerPanel,
        "Show world map window",
        "show_worldmap_window",
        KeyModifier.ALT,
        KeyEvent.VK_M);
    addKeybindSet(
        keybindContainerPanel,
        "Toggle trackpad camera rotation",
        "toggle_trackpad_camera_rotation",
        KeyModifier.ALT,
        KeyEvent.VK_D);
    addKeybindSet(
        keybindContainerPanel,
        "Toggle combat XP menu persistence",
        "toggle_combat_xp_menu",
        KeyModifier.CTRL,
        KeyEvent.VK_C);
    addKeybindSet(
        keybindContainerPanel,
        "Toggle inventory full alert",
        "toggle_inventory_full_alert",
        KeyModifier.CTRL,
        KeyEvent.VK_V);
    addKeybindSet(
        keybindContainerPanel,
        "Toggle roof hiding",
        "toggle_roof_hiding",
        KeyModifier.CTRL,
        KeyEvent.VK_R);
    addKeybindSet(
        keybindContainerPanel,
        "Toggle bypass attack",
        "toggle_bypass_attack",
        KeyModifier.CTRL,
        KeyEvent.VK_A);
    /*
    addKeybindSet(
            keybindContainerPanel,
            "Show queue window",
            "show_queue_window",
            KeyModifier.CTRL,
            KeyEvent.VK_Q);
    addKeybindSet(
            keybindContainerPanel,
            "Toggle XP drops",
            "toggle_xp_drops",
            KeyModifier.CTRL,
            KeyEvent.VK_OPEN_BRACKET);
    addKeybindSet(
            keybindContainerPanel,
            "Toggle color coded text",
            "toggle_colorize",
            KeyModifier.CTRL,
            KeyEvent.VK_Z);
    /*
    addKeybindSet(
            keybindContainerPanel,
            "Toggle lag indicator",
            "toggle_indicators",
            KeyModifier.CTRL,
            KeyEvent.VK_W);
    addKeybindSet(
            keybindContainerPanel, "Reset camera zoom", "reset_zoom", KeyModifier.ALT, KeyEvent.VK_Z);
    addKeybindSet(
            keybindContainerPanel,
            "Reset camera rotation",
            "reset_rotation",
            KeyModifier.ALT,
            KeyEvent.VK_N);
    */

    addKeybindCategory(keybindContainerPanel, "Overlays");
    addKeybindSet(
        keybindContainerPanel,
        "Toggle HP display",
        "toggle_hp_display",
        KeyModifier.CTRL,
        KeyEvent.VK_U);
    addKeybindSet(
        keybindContainerPanel,
        "Toggle combat buffs and cooldowns display         ", // TODO: remove this spacing
        "toggle_buffs_display",
        KeyModifier.CTRL,
        KeyEvent.VK_Y);
    addKeybindSet(
        keybindContainerPanel, "Toggle Goal bar", "toggle_xp_bar", KeyModifier.CTRL, KeyEvent.VK_K);
    addKeybindSet(
        keybindContainerPanel,
        "Toggle inventory count overlay",
        "toggle_inven_count_overlay",
        KeyModifier.CTRL,
        KeyEvent.VK_E);
    addKeybindSet(
        keybindContainerPanel,
        "Toggle position overlay",
        "toggle_position_overlay",
        KeyModifier.ALT,
        KeyEvent.VK_P);
    addKeybindSet(
        keybindContainerPanel,
        "Toggle fps overlay",
        "toggle_fps_overlay",
        KeyModifier.ALT,
        KeyEvent.VK_F);
    /*addKeybindSet(
    keybindContainerPanel,
    "Toggle item name overlay",
    "toggle_item_overlay",
    KeyModifier.CTRL,
    KeyEvent.VK_I);
    */
    addKeybindSet(
        keybindContainerPanel,
        "Toggle player name overlay",
        "toggle_player_name_overlay",
        KeyModifier.CTRL,
        KeyEvent.VK_P);
    addKeybindSet(
        keybindContainerPanel,
        "Toggle friend name overlay",
        "toggle_friend_name_overlay",
        KeyModifier.CTRL,
        KeyEvent.VK_M);
    addKeybindSet(
        keybindContainerPanel,
        "Toggle NPC name overlay",
        "toggle_npc_name_overlay",
        KeyModifier.CTRL,
        KeyEvent.VK_N);
    addKeybindSet(
        keybindContainerPanel,
        "Toggle hitboxes",
        "toggle_hitboxes",
        KeyModifier.CTRL,
        KeyEvent.VK_H);
    /*addKeybindSet(
        keybindContainerPanel,
        "Toggle food heal overlay",
        "toggle_food_heal_overlay",
        KeyModifier.CTRL,
        KeyEvent.VK_G);
    addKeybindSet(
        keybindContainerPanel,
        "Toggle time until health regen",
        "toggle_health_regen_timer",
        KeyModifier.CTRL,
        KeyEvent.VK_X);*/
    // TODO: When replaced with "show items kept on death"
    // addKeybindSet(
    //    keybindContainerPanel,
    //    "Toggle debug mode",
    //    "toggle_debug",
    //    KeyModifier.CTRL,
    //    KeyEvent.VK_D);
    addKeybindSet(
        keybindContainerPanel,
        "Toggle Wiki Hbar Button",
        "toggle_wiki_hbar_button",
        KeyModifier.ALT,
        KeyEvent.VK_W);

    /* TODO: uncomment if twitch integration is implemented
    addKeybindCategory(keybindContainerPanel, "Streaming & Privacy");
    addKeybindSet(
            keybindContainerPanel,
            "Toggle Twitch chat",
            "toggle_twitch_chat",
            KeyModifier.CTRL,
            KeyEvent.VK_T);
    addKeybindSet(
            keybindContainerPanel,
            "Toggle IP shown at login screen",
            "toggle_ipdns",
            KeyModifier.CTRL,
            KeyEvent.VK_J);
    addKeybindSet(
            keybindContainerPanel,
            "End your current speedrun",
            "endrun",
            KeyModifier.CTRL,
            KeyEvent.VK_END);
     */

    // TODO: Uncomment the following line if this feature no longer requires a restart
    // addKeybindSet(keybindContainerPanel, "Toggle save login information",
    // "toggle_save_login_info",
    // KeyModifier.NONE, -1);

    /* TODO uncomment for replay rebinds
    addKeybindCategory(
            keybindContainerPanel, "Replay (only used while a recording is played back)");
    addKeybindSet(keybindContainerPanel, "Stop", "stop", KeyModifier.CTRL, KeyEvent.VK_B);
    addKeybindSet(keybindContainerPanel, "Restart", "restart", KeyModifier.ALT, KeyEvent.VK_R);
    addKeybindSet(keybindContainerPanel, "Pause", "pause", KeyModifier.NONE, KeyEvent.VK_SPACE);
    addKeybindSet(
            keybindContainerPanel,
            "Increase playback speed",
            "ff_plus",
            KeyModifier.CTRL,
            KeyEvent.VK_RIGHT);
    addKeybindSet(
            keybindContainerPanel,
            "Decrease playback speed",
            "ff_minus",
            KeyModifier.CTRL,
            KeyEvent.VK_LEFT);
    addKeybindSet(
            keybindContainerPanel,
            "Reset playback speed",
            "ff_reset",
            KeyModifier.CTRL,
            KeyEvent.VK_DOWN);
    addKeybindSet(
            keybindContainerPanel,
            "Toggle seek bar",
            "show_seek_bar",
            KeyModifier.CTRL,
            KeyEvent.VK_UP);
    addKeybindSet(
            keybindContainerPanel,
            "Show player controls",
            "show_player_controls",
            KeyModifier.ALT,
            KeyEvent.VK_C);

    */

    addKeybindCategory(keybindContainerPanel, "Miscellaneous");
    addKeybindSet(
        keybindContainerPanel,
        "Switch to world 1 at login screen",
        "world_1",
        KeyModifier.CTRL,
        KeyEvent.VK_1);
    addKeybindSet(
        keybindContainerPanel,
        "Switch to world 2 at login screen",
        "world_2",
        KeyModifier.CTRL,
        KeyEvent.VK_2);
    addKeybindSet(
        keybindContainerPanel,
        "Switch to world 3 at login screen",
        "world_3",
        KeyModifier.CTRL,
        KeyEvent.VK_3);
    addKeybindSet(
        keybindContainerPanel,
        "Switch to world 4 at login screen",
        "world_4",
        KeyModifier.CTRL,
        KeyEvent.VK_4);
    addKeybindSet(
        keybindContainerPanel,
        "Switch to world 5 at login screen",
        "world_5",
        KeyModifier.CTRL,
        KeyEvent.VK_5);
    addKeybindSet(
        keybindContainerPanel,
        "Switch to world 6 at login screen",
        "world_6",
        KeyModifier.CTRL,
        KeyEvent.VK_6);
    addKeybindSet(
        keybindContainerPanel,
        "Switch to world 7 at login screen",
        "world_7",
        KeyModifier.CTRL,
        KeyEvent.VK_7);
    addKeybindSet(
        keybindContainerPanel,
        "Switch to world 8 at login screen",
        "world_8",
        KeyModifier.CTRL,
        KeyEvent.VK_8);
    addKeybindSet(
        keybindContainerPanel,
        "Switch to world 9 at login screen",
        "world_9",
        KeyModifier.CTRL,
        KeyEvent.VK_9);
    addKeybindSet(
        keybindContainerPanel,
        "Switch to world 10 at login screen",
        "world_0",
        KeyModifier.CTRL,
        KeyEvent.VK_0);

    keybindContainerContainerPanel.add(keybindContainerPanel, gbl_constraints);
    keybindPanel.add(keybindContainerContainerPanel, con);

    addPanelBottomGlue(keybindPanel);

    /*
     * Presets tab
     */
    presetsPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    presetsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    presetsPanel.setLayout(new BoxLayout(presetsPanel, BoxLayout.Y_AXIS));

    addSettingsHeader(presetsPanel, "Presets");
    presetsPanelCustomSettingsCheckbox = addCheckbox("Custom Settings", presetsPanel);
    presetsPanelCustomSettingsCheckbox.setToolTipText(
        "Load settings from config.ini instead of using a preset");

    JPanel presetsPanelPresetSliderPanel = new JPanel();
    presetsPanelPresetSliderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    presetsPanelPresetSliderPanel.setMaximumSize(osScaleMul(new Dimension(300, 175)));
    presetsPanelPresetSliderPanel.setLayout(
        new BoxLayout(presetsPanelPresetSliderPanel, BoxLayout.X_AXIS));
    presetsPanel.add(presetsPanelPresetSliderPanel);

    // these JLabels are purposely mispelled to give it that authentic RS1 feel
    Hashtable<Integer, JLabel> presetsPanelPresetSliderLabelTable =
        new Hashtable<Integer, JLabel>();
    presetsPanelPresetSliderLabelTable.put(new Integer(0), new JLabel("All"));
    presetsPanelPresetSliderLabelTable.put(new Integer(1), new JLabel("Heavy"));
    presetsPanelPresetSliderLabelTable.put(new Integer(2), new JLabel("Recommended"));
    presetsPanelPresetSliderLabelTable.put(new Integer(3), new JLabel("Lite"));
    presetsPanelPresetSliderLabelTable.put(new Integer(4), new JLabel("Vanilla (Resizable)"));
    presetsPanelPresetSliderLabelTable.put(new Integer(5), new JLabel("Vanilla"));

    presetsPanelPresetSlider = new JSlider();
    presetsPanelPresetSlider.setMajorTickSpacing(1);
    presetsPanelPresetSlider.setLabelTable(presetsPanelPresetSliderLabelTable);
    presetsPanelPresetSlider.setPaintLabels(true);
    presetsPanelPresetSlider.setPaintTicks(true);
    presetsPanelPresetSlider.setSnapToTicks(true);
    presetsPanelPresetSlider.setMinimum(0);
    presetsPanelPresetSlider.setMaximum(5);
    presetsPanelPresetSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
    presetsPanelPresetSlider.setBorder(
        BorderFactory.createEmptyBorder(0, 0, osScaleMul(5), osScaleMul(70)));
    presetsPanelPresetSlider.setOrientation(SwingConstants.VERTICAL);

    if (Util.isUsingFlatLAFTheme()) {
      presetsPanelPresetSliderPanel.add(Box.createHorizontalStrut(osScaleMul(35)));
    }

    presetsPanelPresetSliderPanel.add(presetsPanelPresetSlider);

    JPanel presetsButtonPanel = new JPanel();
    presetsButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    presetsButtonPanel.setMaximumSize(osScaleMul(new Dimension(400, 50)));
    presetsButtonPanel.setBorder(
        BorderFactory.createEmptyBorder(osScaleMul(7), osScaleMul(10), osScaleMul(10), 0));
    presetsButtonPanel.setLayout(new BoxLayout(presetsButtonPanel, BoxLayout.X_AXIS));

    replaceConfigButton =
        addButton("Replace Config with Preset", presetsButtonPanel, Component.LEFT_ALIGNMENT);
    replaceConfigButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            String confirmPresetDefaultMessage =
                "<b>Warning</b>: this will delete your old settings!<br/>"
                    + "<br/>"
                    + "Are you sure you want to delete your old settings?";
            JPanel confirmPresetDefaultPanel =
                Util.createOptionMessagePanel(confirmPresetDefaultMessage);
            int choice =
                JOptionPane.showConfirmDialog(
                    Launcher.getConfigWindow().frame,
                    confirmPresetDefaultPanel,
                    "Confirm",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.CLOSED_OPTION || choice == JOptionPane.NO_OPTION) {
              return;
            }

            Settings.save(Settings.currentProfile);
          }
        });

    if (Util.isUsingFlatLAFTheme()) {
      presetsButtonPanel.add(Box.createRigidArea(osScaleMul(new Dimension(4, 0))));
    }

    resetPresetsButton = addButton("Reset Presets", presetsButtonPanel, Component.RIGHT_ALIGNMENT);
    resetPresetsButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            Logger.Info("Try saying that 10 times fast...");
            Settings.initSettings();
          }
        });
    presetsButtonPanel.add(Box.createHorizontalGlue());
    presetsPanel.add(presetsButtonPanel);

    presetsPanelCustomSettingsCheckbox.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            synchronizePresetOptions();
          }
        });

    addPanelBottomGlue(presetsPanel);

    // World List Tab
    worldListPanel.setLayout(new BoxLayout(worldListPanel, BoxLayout.Y_AXIS));
    worldListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    worldListPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    addSettingsHeader(worldListPanel, "World List");

    JLabel spacingLabel = new JLabel("");
    spacingLabel.setBorder(BorderFactory.createEmptyBorder(osScaleMul(15), 0, 0, 0));
    worldListPanel.add(spacingLabel);

    for (int i = 1; i <= Settings.WORLDS_TO_DISPLAY; i++) {
      addWorldFields(i);
    }
    addAddWorldButton();

    addPanelBottomGlue(worldListPanel);

    // Authors Tab
    JPanel logoPanel = new JPanel();

    JPanel thirdsPanel = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.gridy = 0;
    c.gridx = 0;
    c.fill = GridBagConstraints.HORIZONTAL;

    try {
      BufferedImage rsctimesLogo = ImageIO.read(Launcher.getResource("/assets/icon-large.png"));
      JLabel rsctimesLogoJLabel =
          new JLabel(
              new ImageIcon(
                  rsctimesLogo.getScaledInstance(
                      osScaleMul(250), osScaleMul(250), Image.SCALE_SMOOTH)));
      rsctimesLogoJLabel.setBorder(
          BorderFactory.createEmptyBorder(0, osScaleMul(10), osScaleMul(20), osScaleMul(40)));
      logoPanel.add(rsctimesLogoJLabel);
    } catch (Exception e) {
      e.printStackTrace();
    }

    thirdsPanel.add(logoPanel, c);

    JPanel rightPane = new JPanel(new GridBagLayout());
    GridBagConstraints cR = new GridBagConstraints();
    cR.fill = GridBagConstraints.VERTICAL;
    cR.anchor = GridBagConstraints.LINE_START;
    cR.weightx = 0.5;
    cR.gridy = 0;
    cR.gridwidth = 3;

    JLabel RSCTimesText =
        new JLabel(
            String.format(
                "<html><div style=\"font-size:%dpx; padding-bottom:%dpx;\"<b>RSC</b>Times</div><div style=\"font-size:%dpx;\">v%8.6f </div></html>",
                osScaleMul(45), osScaleMul(10), osScaleMul(20), Settings.VERSION_NUMBER));

    rightPane.add(RSCTimesText);

    cR.gridy = 1;

    JLabel aboutText =
        new JLabel(
            String.format(
                "<html><head><style>p{font-size:%dpx; padding-top:%dpx;}ul{list-style-type:none;padding-left:0px;margin-left:0px;}</style></head>"
                    + "<p><b>RSC</b>Times is a RuneLite-like client "
                    + "based on mudclient38-recreated.<br/> Learn more at https://rsc.plus<br/><br/>"
                    + "Thanks to the authors who made this software possible:<br/><ul>"
                    + "<li><b>● Luis</b>, for creating the client and finding lots of hooks</li>"
                    + "<li><b>● Logg</b>, helped port features from RSC+, new interfaces & improvements</li>"
                    + "<li><b>● conker</b>, ported scaling from RSC+, modern UI, and fixed bugs</li>"
                    + "<li><b>● Yumeko</b>, fixed Twitch chat integration in 2023</li>"
                    + "<li><b>● Ornox</b>, for creating RSC+ & most of its features</li>"
                    + "<li><b>● The RSC+ team of 2016 to 2018</b></li>"
                    + "<li><b>● The Jagex team of 2000 to 2001</b></li>"
                    + "</ul></p></html>",
                osScaleMul(10), osScaleMul(15)));

    rightPane.add(aboutText, cR);
    c.gridx = 2;
    thirdsPanel.add(rightPane, c);

    JPanel bottomPane = new JPanel(new GridBagLayout());
    GridBagConstraints cB = new GridBagConstraints();
    cB = new GridBagConstraints();
    cB.fill = GridBagConstraints.HORIZONTAL;
    cB.anchor = GridBagConstraints.NORTH;

    cB.gridx = 0;
    cB.weightx = 0.33;
    cB.gridwidth = 1;

    JLabel licenseText =
        new JLabel(
            "        This software is licensed under GPLv3. Visit https://www.gnu.org/licenses/gpl-3.0.en.html for more information.");
    bottomPane.add(licenseText, cB);

    cB.gridx = 5;
    cB.weightx = 1;
    cB.gridwidth = 20;
    JLabel blank = new JLabel("");
    bottomPane.add(blank, cB);

    c.gridy = 10;
    c.gridx = 0;
    c.gridwidth = 10;
    thirdsPanel.add(bottomPane, c);

    authorsPanel.add(thirdsPanel);

    addPanelBottomGlue(authorsPanel);

    //// End component creation ////
  }

  /** Resets the tooltip listener state */
  private void resetToolTipListener() {
    toolTipTextString = " ";
    toolTipTextLabel.setText(toolTipInitText);
    resetToolTipBarPanelColors();
    removeConfigWindowEventQueueListener();
  }

  /** Resets the tooltip bar panel colors */
  private void resetToolTipBarPanelColors() {
    if (Util.isDarkThemeFlatLAF()) {
      toolTipPanel.setBackground(new Color(52, 56, 58));
    } else if (Util.isLightThemeFlatLAF()) {
      toolTipPanel.setBackground(new Color(235, 235, 235));
    } else {
      toolTipPanel.setBackground(new Color(233, 236, 242));
    }
  }

  /**
   * Adds vertical glue to a settings panel to ensure that components do not shrink / grow when an
   * active search removes the scrollbar. This must be the very last component added to a settings
   * panel.
   *
   * @param panel The panel to which glue should be added
   */
  private static void addPanelBottomGlue(JPanel panel) {
    JComponent panelGlue = (JComponent) Box.createVerticalGlue();
    // These are named such that they can be identified in cases where the panel
    // layout dynamically changes, such as during search.
    panelGlue.setName(panel.getName().toLowerCase() + "PanelBottomGlue");
    panel.add(panelGlue);
  }

  /**
   * Adds a new keybind to the GUI and settings and registers it to be checked when keypresses are
   * sent to the applet.
   *
   * @param panel Panel to add the keybind label and button to
   * @param labelText Text describing the keybind's function as shown to the user on the config
   *     window.
   * @param commandID Unique String matching an entry in the processKeybindCommand switch statement.
   * @param defaultModifier Default modifier value. This can be one of the enum values of
   *     KeybindSet.KeyModifier, eg KeyModifier.CTRL
   * @param defaultKeyValue Default key value. This should match up with a KeyEvent.VK_ value. Set
   *     to -1 to set the default as NONE
   */
  private void addKeybindSet(
      JPanel panel,
      String labelText,
      String commandID,
      KeyModifier defaultModifier,
      int defaultKeyValue) {
    addKeybindLabel(panel, labelText);
    String buttonText = defaultModifier.toString() + " + " + KeyEvent.getKeyText(defaultKeyValue);
    if (defaultKeyValue == -1) buttonText = "NONE";
    JButton b = addKeybindButton(panel, buttonText);
    KeybindSet kbs = new KeybindSet(b, commandID, defaultModifier, defaultKeyValue);
    KeyboardHandler.keybindSetList.add(kbs);
    setKeybindButtonText(
        kbs); // Set the text of the keybind button now that it has been initialized properly
    b.addActionListener(this.clickListener);
    b.addKeyListener(this.rebindListener);
    b.addFocusListener(focusListener);
    b.setFocusable(false);

    // Default KeybindSet
    KeyboardHandler.defaultKeybindSetList.put(
        commandID, new KeybindSet(null, commandID, defaultModifier, defaultKeyValue));
  }

  /**
   * Tracks the number of keybind labels added to the keybind panel. Used to determine the gbc.gridy
   * and panel preferred height.
   */
  private int keybindLabelGridYCounter = 0;

  /**
   * Adds a new label to the keybinds list. This should be used in conjunction with adding a button
   * in a 1:1 ratio. The new label will be added below the existing ones.
   *
   * @param panel Panel to add the label to.
   * @param labelText Text of the label to add.
   * @return The label that was added.
   */
  private JLabel addKeybindLabel(JPanel panel, String labelText) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(0, 0, osScaleMul(5), 0);
    gbc.gridx = 0;
    gbc.gridy = keybindLabelGridYCounter++;
    gbc.weightx = 20;
    JLabel jlbl = new JLabel(labelText);
    panel.add(jlbl, gbc);
    return jlbl;
  }

  /**
   * Tracks the number of keybind buttons added to the keybind panel. Used to determine the
   * gbc.gridy.
   */
  private int keybindButtonGridYCounter = 0;

  /**
   * Adds a new button to the keybinds list. This should be used in conjunction with adding a label
   * in a 1:1 ratio. The new button will be added below the existing ones.
   *
   * @param panel Panel to add the button to.
   * @param buttonText Text of the label to add.
   * @return The label that was added.
   */
  private JButton addKeybindButton(JPanel panel, String buttonText) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.EAST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0, 0, osScaleMul(5), 0);
    gbc.gridx = 1;
    gbc.gridy = keybindButtonGridYCounter++;
    JButton jbtn = new JButton(buttonText);
    panel.add(jbtn, gbc);
    return jbtn;
  }

  /**
   * Adds a new category title to the keybinds list.
   *
   * @param panel Panel to add the title to.
   * @param categoryName Name of the category to add.
   */
  private void addKeybindCategory(JPanel panel, String categoryName) {
    addKeybindCategoryLabel(panel, "<html><b>" + categoryName + "</b></html>");
    addKeybindCategorySeparator(panel);
    keybindButtonGridYCounter++;
    keybindLabelGridYCounter++;
  }

  /**
   * Adds a new horizontal separator to the keybinds list. The JSeparator spans 2 columns.
   *
   * @param panel Panel to add the separator to.
   */
  private void addKeybindCategorySeparator(JPanel panel) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0, 0, 0, 0);
    gbc.gridx = 0;
    gbc.gridy = keybindButtonGridYCounter++;
    keybindLabelGridYCounter++;
    gbc.gridwidth = 2;

    JComponent spacer1 = (JComponent) Box.createVerticalStrut(osScaleMul(7));
    panel.add(spacer1, gbc);
    JSeparator jsep = new JSeparator(SwingConstants.HORIZONTAL);
    panel.add(jsep, gbc);
    JComponent spacer2 = (JComponent) Box.createVerticalStrut(osScaleMul(7));
    panel.add(spacer2, gbc);
  }

  /**
   * Adds a new category label to the keybinds list. The JLabel spans 2 columns.
   *
   * @param panel Panel to add the label to.
   * @param categoryName Name of the category to add.
   * @return The label that was added.
   */
  private JLabel addKeybindCategoryLabel(JPanel panel, String categoryName) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.WEST;
    gbc.gridx = 0;
    if (keybindLabelGridYCounter == 0) gbc.insets = new Insets(0, 0, 0, 0);
    else gbc.insets = new Insets(osScaleMul(7), 0, 0, 0);
    gbc.gridy = keybindLabelGridYCounter++;
    keybindButtonGridYCounter++;
    gbc.weightx = 20;
    gbc.gridwidth = 2;

    JLabel jlbl = new JLabel(categoryName);
    panel.add(jlbl, gbc);
    return jlbl;
  }

  /**
   * Adds a new category title to the notifications list.
   *
   * @param panel Panel to add the title to.
   * @param categoryName Name of the category to add.
   */
  private void addSettingsHeader(JPanel panel, String categoryName) {
    addSettingsHeaderLabel(panel, "<html><b>" + categoryName + "</b></html>");
    addSettingsHeaderSeparator(panel);
  }

  /**
   * Adds a new horizontal separator to the notifications list.
   *
   * @param panel Panel to add the separator to.
   */
  private void addSettingsHeaderSeparator(JPanel panel) {
    JSeparator jsep = new JSeparator(SwingConstants.HORIZONTAL);
    jsep.setPreferredSize(new Dimension(0, osScaleMul(7)));
    jsep.setMaximumSize(new Dimension(Short.MAX_VALUE, osScaleMul(7)));
    panel.add(jsep);
  }

  /**
   * Adds a new category label to the notifications list.
   *
   * @param panel Panel to add the label to.
   * @param categoryName Name of the category to add.
   * @return The label that was added.
   */
  private JLabel addSettingsHeaderLabel(JPanel panel, String categoryName) {
    JLabel jlbl = new JLabel(categoryName);
    panel.add(jlbl);
    return jlbl;
  }

  /**
   * Adds a preconfigured JCheckbox to the specified container, setting its alignment constraint to
   * left and adding an empty padding border.
   *
   * @param text The text of the checkbox
   * @param container The container to add the checkbox to.
   * @return The newly created JCheckBox.
   */
  private JCheckBox addCheckbox(String text, Container container) {
    JCheckBox checkbox = new JCheckBox(text);
    checkbox.setAlignmentX(Component.LEFT_ALIGNMENT);
    checkbox.setBorder(BorderFactory.createEmptyBorder(0, 0, osScaleMul(10), osScaleMul(5)));
    container.add(checkbox);
    return checkbox;
  }

  /**
   * Adds a preconfigured JButton to the specified container using the specified alignment
   * constraint. Does not modify the button's border.
   *
   * @param text The text of the button
   * @param container The container to add the button to
   * @param alignment The alignment of the button.
   * @return The newly created JButton.
   */
  private JButton addButton(String text, Container container, float alignment) {
    JButton button = new JButton(text);
    button.setAlignmentX(alignment);
    container.add(button);
    return button;
  }

  /**
   * Adds a preconfigured radio button to the specified container. Does not currently assign the
   * radio button to a group.
   *
   * @param text The text of the radio button
   * @param container The container to add the button to
   * @param leftIndent The amount of padding to add to the left of the radio button as an empty
   *     border argument.
   * @return The newly created JRadioButton
   */
  private JRadioButton addRadioButton(String text, Container container, int leftIndent) {
    JRadioButton radioButton = new JRadioButton(text);
    radioButton.setAlignmentX(Component.LEFT_ALIGNMENT);
    radioButton.setBorder(
        BorderFactory.createEmptyBorder(0, leftIndent, osScaleMul(7), osScaleMul(5)));
    container.add(radioButton);
    return radioButton;
  }

  /**
   * Sets the scroll speed of a JScrollPane
   *
   * @param scrollPane The JScrollPane to modify
   * @param horizontalInc The horizontal increment value
   * @param verticalInc The vertical increment value
   */
  private void setScrollSpeed(JScrollPane scrollPane, int horizontalInc, int verticalInc) {
    scrollPane.getVerticalScrollBar().setUnitIncrement(verticalInc);
    scrollPane.getHorizontalScrollBar().setUnitIncrement(horizontalInc);
  }

  /** Synchronizes all relevant values in the gui's elements to match those in Settings.java */
  public void synchronizeGuiValues() {

    // Presets tab (has to go first to properly synchronizeGui)
    presetsPanelCustomSettingsCheckbox.setSelected(Settings.currentProfile.equals("custom"));
    synchronizePresetOptions();

    if (!Settings.currentProfile.equals("custom")) {
      sliderValue = Settings.presetTable.indexOf(Settings.currentProfile);
    }
    if (sliderValue < 0 || sliderValue > Settings.presetTable.size()) {
      sliderValue = Settings.presetTable.indexOf("default");
    }
    presetsPanelPresetSlider.setValue(sliderValue);

    // General tab
    generalPanelClientSizeCheckbox.setSelected(
        Settings.CUSTOM_CLIENT_SIZE.get(Settings.currentProfile));
    generalPanelClientSizeXSpinner.setValue(
        Settings.CUSTOM_CLIENT_SIZE_X.get(Settings.currentProfile));
    generalPanelClientSizeYSpinner.setValue(
        Settings.CUSTOM_CLIENT_SIZE_Y.get(Settings.currentProfile));
    generalPanelScaleWindowCheckbox.setSelected(
        Settings.SCALED_CLIENT_WINDOW.get(Settings.currentProfile));
    if (Settings.SCALING_ALGORITHM.get(Settings.currentProfile)
        == AffineTransformOp.TYPE_NEAREST_NEIGHBOR) {
      generalPanelIntegerScalingFocusButton.setSelected(true);
    } else if (Settings.SCALING_ALGORITHM.get(Settings.currentProfile)
        == AffineTransformOp.TYPE_BILINEAR) {
      generalPanelBilinearScalingFocusButton.setSelected(true);
    } else if (Settings.SCALING_ALGORITHM.get(Settings.currentProfile)
        == AffineTransformOp.TYPE_BICUBIC) {
      generalPanelBicubicScalingFocusButton.setSelected(true);
    }
    generalPanelIntegerScalingSpinner.setValue(
        Settings.INTEGER_SCALING_FACTOR.get(Settings.currentProfile));
    generalPanelBilinearScalingSpinner.setValue(
        Settings.BILINEAR_SCALING_FACTOR.get(Settings.currentProfile));
    generalPanelBicubicScalingSpinner.setValue(
        Settings.BICUBIC_SCALING_FACTOR.get(Settings.currentProfile));
    generalPanelCheckUpdates.setSelected(Settings.CHECK_UPDATES.get(Settings.currentProfile));
    generalPanelWelcomeEnabled.setSelected(
        Settings.REMIND_HOW_TO_OPEN_SETTINGS.get(Settings.currentProfile));
    /*
    generalPanelShowSecurityTipsAtLoginCheckbox.setSelected(
            Settings.SHOW_SECURITY_TIP_DAY.get(Settings.currentProfile));
            */
    generalPanelCombatXPMenuCheckbox.setSelected(
        Settings.COMBAT_MENU_SHOWN.get(Settings.currentProfile));
    generalPanelCombatXPMenuHiddenCheckbox.setSelected(
        Settings.COMBAT_MENU_HIDDEN.get(Settings.currentProfile));
    generalPanelInventoryFullAlertCheckbox.setSelected(
        Settings.INVENTORY_FULL_ALERT.get(Settings.currentProfile));
    /*
    generalPanelNamePatchModeSlider.setValue(Settings.NAME_PATCH_TYPE.get(Settings.currentProfile));
    */
    generalPanelLogVerbositySlider.setValue(Settings.LOG_VERBOSITY.get(Settings.currentProfile));
    /*
    generalPanelCommandPatchQuestCheckbox.setSelected(
            Settings.COMMAND_PATCH_QUEST.get(Settings.currentProfile));
    generalPanelKeepScrollbarPosMagicPrayerCheckbox.setSelected(
            Settings.KEEP_SCROLLBAR_POS_MAGIC_PRAYER.get(Settings.currentProfile));
            */
    generalPanelBypassAttackCheckbox.setSelected(
        Settings.ATTACK_ALWAYS_LEFT_CLICK.get(Settings.currentProfile));
    generalPanelSortFriendsCheckbox.setSelected(Settings.SORT_FRIENDS.get(Settings.currentProfile));
    generalPanelRoofHidingCheckbox.setSelected(Settings.HIDE_ROOFS.get(Settings.currentProfile));
    generalPanelDisableUndergroundLightingCheckbox.setSelected(
        Settings.DISABLE_UNDERGROUND_LIGHTING.get(Settings.currentProfile));
    generalPanelCameraZoomableCheckbox.setSelected(
        Settings.CAMERA_ZOOMABLE.get(Settings.currentProfile));
    generalPanelCameraRotatableCheckbox.setSelected(
        Settings.CAMERA_ROTATABLE.get(Settings.currentProfile));
    generalPanelCameraMovableCheckbox.setSelected(
        Settings.CAMERA_MOVABLE.get(Settings.currentProfile));
    generalPanelCameraMovableRelativeCheckbox.setSelected(
        Settings.CAMERA_MOVABLE_RELATIVE.get(Settings.currentProfile));
    generalPanelColoredTextCheckbox.setSelected(
        Settings.COLORIZE_CONSOLE_TEXT.get(Settings.currentProfile));
    generalPanelLogLevelCheckbox.setSelected(Settings.LOG_SHOW_LEVEL.get(Settings.currentProfile));
    generalPanelLogTimestampsCheckbox.setSelected(
        Settings.LOG_SHOW_TIMESTAMPS.get(Settings.currentProfile));
    generalPanelLogForceLevelCheckbox.setSelected(
        Settings.LOG_FORCE_LEVEL.get(Settings.currentProfile));
    generalPanelLogForceTimestampsCheckbox.setSelected(
        Settings.LOG_FORCE_TIMESTAMPS.get(Settings.currentProfile));
    generalPanelFoVSlider.setValue(Settings.FOV.get(Settings.currentProfile));
    generalPanelLimitFPSCheckbox.setSelected(
        Settings.FPS_LIMIT_ENABLED.get(Settings.currentProfile));
    generalPanelLimitFPSSpinner.setValue(Settings.FPS_LIMIT.get(Settings.currentProfile));
    generalPanelAutoScreenshotCheckbox.setSelected(
        Settings.AUTO_SCREENSHOT.get(Settings.currentProfile));
    generalPanelCustomCursorCheckbox.setSelected(
        Settings.SOFTWARE_CURSOR.get(Settings.currentProfile));
    generalPanelShiftScrollCameraRotationCheckbox.setSelected(
        Settings.SHIFT_SCROLL_CAMERA_ROTATION.get(Settings.currentProfile));
    generalPanelTrackpadRotationSlider.setValue(
        Settings.TRACKPAD_ROTATION_SENSITIVITY.get(Settings.currentProfile));
    generalPanelViewDistanceSlider.setValue(Settings.VIEW_DISTANCE.get(Settings.currentProfile));
    generalPanelPatchGenderCheckbox.setSelected(Settings.PATCH_GENDER.get(Settings.currentProfile));
    generalPanelPatchHbar512LastPixelCheckbox.setSelected(
        Settings.PATCH_HBAR_512_LAST_PIXEL.get(Settings.currentProfile));
    generalPanelUseDarkModeCheckbox.setSelected(
        Settings.USE_DARK_FLATLAF.get(Settings.currentProfile));
    generalPanelUseNimbusThemeCheckbox.setSelected(
        Settings.USE_NIMBUS_THEME.get(Settings.currentProfile));
    generalPanelPrefersXdgOpenCheckbox.setSelected(
        Settings.PREFERS_XDG_OPEN.get(Settings.currentProfile));

    // Sets the text associated with the name patch slider.
    switch (generalPanelNamePatchModeSlider.getValue()) {
      case 3:
        generalPanelNamePatchModeDesc.setText(
            "<html>Reworded vague stuff to be more descriptive on top of type 1 & 2 changes</html>");
        break;
      case 2:
        generalPanelNamePatchModeDesc.setText(
            "<html>Capitalizations and fixed spellings on top of type 1 changes</html>");
        break;
      case 1:
        generalPanelNamePatchModeDesc.setText(
            "<html>Purely practical name changes (TODO: not sure there are any in this version)</html>");
        break;
      case 0:
        generalPanelNamePatchModeDesc.setText("<html>No item name patching</html>");
        break;
      default:
        Logger.Error("Invalid name patch mode value");
        break;
    }

    // Overlays tab
    overlayPanelStatusDisplayCheckbox.setSelected(
        Settings.SHOW_HP_OVERLAY.get(Settings.currentProfile));
    overlayPanelBuffsCheckbox.setSelected(Settings.SHOW_BUFFS.get(Settings.currentProfile));
    overlayPanelLastMenuActionCheckbox.setSelected(
        Settings.SHOW_LAST_MENU_ACTION.get(Settings.currentProfile));
    overlayPanelMouseTooltipCheckbox.setSelected(
        Settings.SHOW_MOUSE_TOOLTIP.get(Settings.currentProfile));
    overlayPanelExtendedTooltipCheckbox.setSelected(
        Settings.SHOW_EXTENDED_TOOLTIP.get(Settings.currentProfile));
    overlayPanelInvCountCheckbox.setSelected(Settings.SHOW_INVCOUNT.get(Settings.currentProfile));
    overlayPanelRscTimesButtonsCheckbox.setSelected(
        Settings.SHOW_RSCTIMES_BUTTONS.get(Settings.currentProfile));
    overlayPanelRscTimesButtonsFunctionalCheckbox.setSelected(
        Settings.RSCTIMES_BUTTONS_FUNCTIONAL.get(Settings.currentProfile)
            || Settings.SHOW_RSCTIMES_BUTTONS.get(Settings.currentProfile));
    overlayPanelWikiLookupOnMagicBookCheckbox.setSelected(
        Settings.WIKI_LOOKUP_ON_MAGIC_BOOK.get(Settings.currentProfile));
    overlayPanelWikiLookupOnHbarCheckbox.setSelected(
        Settings.WIKI_LOOKUP_ON_HBAR.get(Settings.currentProfile));
    overlayPanelToggleXPBarOnStatsButtonCheckbox.setSelected(
        Settings.TOGGLE_XP_BAR_ON_STATS_BUTTON.get(Settings.currentProfile));
    overlayPanelToggleMotivationalQuotesCheckbox.setSelected(
        Settings.MOTIVATIONAL_QUOTES_BUTTON.get(Settings.currentProfile));
    overlayPanelPositionCheckbox.setSelected(
        Settings.SHOW_PLAYER_POSITION.get(Settings.currentProfile));
    overlayPanelHideFpsCheckbox.setSelected(Settings.HIDE_FPS.get(Settings.currentProfile));
    overlayPanelItemNamesCheckbox.setSelected(
        Settings.SHOW_ITEM_GROUND_OVERLAY.get(Settings.currentProfile));
    overlayPanelPlayerNamesCheckbox.setSelected(
        Settings.SHOW_PLAYER_NAME_OVERLAY.get(Settings.currentProfile));
    overlayPanelFriendNamesCheckbox.setSelected(
        Settings.SHOW_FRIEND_NAME_OVERLAY.get(Settings.currentProfile));
    overlayPanelNPCNamesCheckbox.setSelected(
        Settings.SHOW_NPC_NAME_OVERLAY.get(Settings.currentProfile));
    overlayPanelIDsCheckbox.setSelected(Settings.EXTEND_IDS_OVERLAY.get(Settings.currentProfile));
    overlayPanelObjectInfoCheckbox.setSelected(
        Settings.TRACE_OBJECT_INFO.get(Settings.currentProfile));
    overlayPanelHitboxCheckbox.setSelected(Settings.SHOW_HITBOX.get(Settings.currentProfile));
    overlayPanelShowCombatInfoCheckbox.setSelected(
        Settings.SHOW_COMBAT_INFO.get(Settings.currentProfile));
    overlayPanelUsePercentageCheckbox.setSelected(
        Settings.NPC_HEALTH_SHOW_PERCENTAGE.get(Settings.currentProfile));
    overlayPanelXPBarCheckbox.setSelected(Settings.SHOW_XP_BAR.get(Settings.currentProfile));
    overlayPanelXPCenterAlignFocusButton.setSelected(
        Settings.CENTER_XPDROPS.get(Settings.currentProfile));
    overlayPanelXPRightAlignFocusButton.setSelected(
        !Settings.CENTER_XPDROPS.get(Settings.currentProfile));
    overlayPanelLagIndicatorCheckbox.setSelected(
        Settings.LAG_INDICATOR.get(Settings.currentProfile));
    overlayPanelFoodHealingCheckbox.setSelected(
        Settings.SHOW_FOOD_HEAL_OVERLAY.get(
            Settings.currentProfile)); // TODO: Implement this feature
    overlayPanelHPRegenTimerCheckbox.setSelected(
        Settings.SHOW_TIME_UNTIL_HP_REGEN.get(
            Settings.currentProfile)); // TODO: Implement this feature
    generalPanelDebugModeCheckbox.setSelected(Settings.DEBUG.get(Settings.currentProfile));
    generalPanelExceptionHandlerCheckbox.setSelected(
        Settings.EXCEPTION_HANDLER.get(Settings.currentProfile));
    highlightedItemsTextField.setText(
        Util.joinAsString(",", Settings.HIGHLIGHTED_ITEMS.get("custom")));
    blockedItemsTextField.setText(Util.joinAsString(",", Settings.BLOCKED_ITEMS.get("custom")));

    // Notifications tab
    notificationPanelPMNotifsCheckbox.setSelected(
        Settings.PM_NOTIFICATIONS.get(Settings.currentProfile));
    notificationPanelTradeNotifsCheckbox.setSelected(
        Settings.TRADE_NOTIFICATIONS.get(Settings.currentProfile));
    notificationPanelUnderAttackNotifsCheckbox.setSelected(
        Settings.UNDER_ATTACK_NOTIFICATIONS.get(Settings.currentProfile));
    notificationPanelLowHPNotifsCheckbox.setSelected(
        Settings.LOW_HP_NOTIFICATIONS.get(Settings.currentProfile));
    notificationPanelLowHPNotifsSpinner.setValue(
        Settings.LOW_HP_NOTIF_VALUE.get(Settings.currentProfile));
    notificationPanelHighlightedItemTimerCheckbox.setSelected(
        Settings.HIGHLIGHTED_ITEM_NOTIFICATIONS.get(Settings.currentProfile));
    notificationPanelHighlightedItemTimerSpinner.setValue(
        Settings.HIGHLIGHTED_ITEM_NOTIF_VALUE.get(Settings.currentProfile));
    notificationPanelNotifSoundsCheckbox.setSelected(
        Settings.NOTIFICATION_SOUNDS.get(Settings.currentProfile));
    notificationPanelUseSystemNotifsCheckbox.setSelected(
        Settings.USE_SYSTEM_NOTIFICATIONS.get(Settings.currentProfile));
    notificationPanelTrayPopupCheckbox.setSelected(
        Settings.TRAY_NOTIFS.get(Settings.currentProfile));
    notificationPanelTrayPopupClientFocusButton.setSelected(
        !Settings.TRAY_NOTIFS_ALWAYS.get(Settings.currentProfile));
    notificationPanelTrayPopupAnyFocusButton.setSelected(
        Settings.TRAY_NOTIFS_ALWAYS.get(Settings.currentProfile));
    notificationPanelNotifSoundClientFocusButton.setSelected(
        !Settings.SOUND_NOTIFS_ALWAYS.get(Settings.currentProfile));
    notificationPanelNotifSoundAnyFocusButton.setSelected(
        Settings.SOUND_NOTIFS_ALWAYS.get(Settings.currentProfile));

    // Streaming & Privacy tab
    streamingPanelTwitchChatIntegrationEnabledCheckbox.setSelected(
        Settings.TWITCH_CHAT_ENABLED.get(Settings.currentProfile));
    streamingPanelTwitchChatCheckbox.setSelected(
        Settings.TWITCH_HIDE_CHAT.get(Settings.currentProfile));
    streamingPanelTwitchChannelNameTextField.setText(
        Settings.TWITCH_CHANNEL.get(Settings.currentProfile));
    streamingPanelTwitchOAuthTextField.setText(Settings.TWITCH_OAUTH.get(Settings.currentProfile));
    streamingPanelTwitchUserTextField.setText(
        Settings.TWITCH_USERNAME.get(Settings.currentProfile));
    streamingPanelSaveLoginCheckbox.setSelected(
        Settings.SAVE_LOGININFO.get(Settings.currentProfile));
    streamingPanelStartLoginCheckbox.setSelected(
        Settings.START_LOGINSCREEN.get(Settings.currentProfile));
    /*
    streamingPanelSpeedrunnerCheckbox.setSelected(
            Settings.SPEEDRUNNER_MODE_ACTIVE.get(Settings.currentProfile));

    // streamingPanelSpeedrunnerUsernameTextField.setText(Settings.SPEEDRUNNER_USERNAME.get(Settings.currentProfile));
    */

    // World List tab
    synchronizeWorldTab();

    for (KeybindSet kbs : KeyboardHandler.keybindSetList) {
      setKeybindButtonText(kbs);
    }
  }

  /** Saves the settings from the GUI values to the settings class variables */
  public void saveSettings() {
    // General options
    Settings.CUSTOM_CLIENT_SIZE.put(
        Settings.currentProfile, generalPanelClientSizeCheckbox.isSelected());
    Settings.CUSTOM_CLIENT_SIZE_X.put(
        Settings.currentProfile,
        ((SpinnerNumberModel) (generalPanelClientSizeXSpinner.getModel())).getNumber().intValue());
    Settings.CUSTOM_CLIENT_SIZE_Y.put(
        Settings.currentProfile,
        ((SpinnerNumberModel) (generalPanelClientSizeYSpinner.getModel())).getNumber().intValue());
    Settings.SCALED_CLIENT_WINDOW.put(
        Settings.currentProfile, generalPanelScaleWindowCheckbox.isSelected());
    Settings.SCALING_ALGORITHM.put(
        Settings.currentProfile,
        generalPanelIntegerScalingFocusButton.isSelected()
            ? AffineTransformOp.TYPE_NEAREST_NEIGHBOR
            : generalPanelBilinearScalingFocusButton.isSelected()
                ? AffineTransformOp.TYPE_BILINEAR
                : AffineTransformOp.TYPE_BICUBIC);
    Settings.INTEGER_SCALING_FACTOR.put(
        Settings.currentProfile,
        ((SpinnerNumberModel) (generalPanelIntegerScalingSpinner.getModel()))
            .getNumber()
            .intValue());
    Settings.BILINEAR_SCALING_FACTOR.put(
        Settings.currentProfile,
        BigDecimal.valueOf(
                ((SpinnerNumberModel) (generalPanelBilinearScalingSpinner.getModel()))
                    .getNumber()
                    .floatValue())
            .setScale(1, RoundingMode.HALF_DOWN)
            .floatValue());
    Settings.BICUBIC_SCALING_FACTOR.put(
        Settings.currentProfile,
        BigDecimal.valueOf(
                ((SpinnerNumberModel) (generalPanelBicubicScalingSpinner.getModel()))
                    .getNumber()
                    .floatValue())
            .setScale(1, RoundingMode.HALF_DOWN)
            .floatValue());
    Settings.CHECK_UPDATES.put(Settings.currentProfile, generalPanelCheckUpdates.isSelected());
    Settings.REMIND_HOW_TO_OPEN_SETTINGS.put(
        Settings.currentProfile, generalPanelWelcomeEnabled.isSelected());
    /*
    Settings.SHOW_SECURITY_TIP_DAY.put(
            Settings.currentProfile, generalPanelShowSecurityTipsAtLoginCheckbox.isSelected());
    */
    Settings.COMBAT_MENU_SHOWN.put(
        Settings.currentProfile, generalPanelCombatXPMenuCheckbox.isSelected());
    Settings.COMBAT_MENU_HIDDEN.put(
        Settings.currentProfile, generalPanelCombatXPMenuHiddenCheckbox.isSelected());
    Settings.CENTER_XPDROPS.put(
        Settings.currentProfile, overlayPanelXPCenterAlignFocusButton.isSelected());
    Settings.INVENTORY_FULL_ALERT.put(
        Settings.currentProfile, generalPanelInventoryFullAlertCheckbox.isSelected());
    /*
    Settings.NAME_PATCH_TYPE.put(
            Settings.currentProfile, generalPanelNamePatchModeSlider.getValue());
    */
    Settings.LOG_VERBOSITY.put(Settings.currentProfile, generalPanelLogVerbositySlider.getValue());
    Settings.LOG_SHOW_TIMESTAMPS.put(
        Settings.currentProfile, generalPanelLogTimestampsCheckbox.isSelected());
    Settings.LOG_SHOW_LEVEL.put(Settings.currentProfile, generalPanelLogLevelCheckbox.isSelected());
    Settings.LOG_FORCE_TIMESTAMPS.put(
        Settings.currentProfile, generalPanelLogForceTimestampsCheckbox.isSelected());
    Settings.LOG_FORCE_LEVEL.put(
        Settings.currentProfile, generalPanelLogForceLevelCheckbox.isSelected());
    Settings.PREFERS_XDG_OPEN.put(
        Settings.currentProfile, generalPanelPrefersXdgOpenCheckbox.isSelected());
    /*
    Settings.COMMAND_PATCH_QUEST.put(
            Settings.currentProfile, generalPanelCommandPatchQuestCheckbox.isSelected());
            */
    Settings.ATTACK_ALWAYS_LEFT_CLICK.put(
        Settings.currentProfile, generalPanelBypassAttackCheckbox.isSelected());
    Settings.SORT_FRIENDS.put(
        Settings.currentProfile, generalPanelSortFriendsCheckbox.isSelected());
    Settings.HIDE_ROOFS.put(Settings.currentProfile, generalPanelRoofHidingCheckbox.isSelected());
    Settings.DISABLE_UNDERGROUND_LIGHTING.put(
        Settings.currentProfile, generalPanelDisableUndergroundLightingCheckbox.isSelected());
    Settings.CAMERA_ZOOMABLE.put(
        Settings.currentProfile, generalPanelCameraZoomableCheckbox.isSelected());
    Settings.CAMERA_ROTATABLE.put(
        Settings.currentProfile, generalPanelCameraRotatableCheckbox.isSelected());
    Settings.CAMERA_MOVABLE.put(
        Settings.currentProfile, generalPanelCameraMovableCheckbox.isSelected());
    Settings.CAMERA_MOVABLE_RELATIVE.put(
        Settings.currentProfile, generalPanelCameraMovableRelativeCheckbox.isSelected());
    Settings.COLORIZE_CONSOLE_TEXT.put(
        Settings.currentProfile, generalPanelColoredTextCheckbox.isSelected());
    Settings.FOV.put(Settings.currentProfile, generalPanelFoVSlider.getValue());
    Settings.SOFTWARE_CURSOR.put(
        Settings.currentProfile, generalPanelCustomCursorCheckbox.isSelected());
    Settings.SHIFT_SCROLL_CAMERA_ROTATION.put(
        Settings.currentProfile, generalPanelShiftScrollCameraRotationCheckbox.isSelected());
    Settings.TRACKPAD_ROTATION_SENSITIVITY.put(
        Settings.currentProfile, generalPanelTrackpadRotationSlider.getValue());
    Settings.AUTO_SCREENSHOT.put(
        Settings.currentProfile, generalPanelAutoScreenshotCheckbox.isSelected());
    Settings.VIEW_DISTANCE.put(Settings.currentProfile, generalPanelViewDistanceSlider.getValue());
    Settings.FPS_LIMIT_ENABLED.put(
        Settings.currentProfile, generalPanelLimitFPSCheckbox.isSelected());
    Settings.FPS_LIMIT.put(
        Settings.currentProfile,
        ((SpinnerNumberModel) (generalPanelLimitFPSSpinner.getModel())).getNumber().intValue());
    Settings.PATCH_GENDER.put(
        Settings.currentProfile, generalPanelPatchGenderCheckbox.isSelected());
    Settings.PATCH_HBAR_512_LAST_PIXEL.put(
        Settings.currentProfile, generalPanelPatchHbar512LastPixelCheckbox.isSelected());
    Settings.USE_DARK_FLATLAF.put(
        Settings.currentProfile, generalPanelUseDarkModeCheckbox.isSelected());
    Settings.USE_NIMBUS_THEME.put(
        Settings.currentProfile, generalPanelUseNimbusThemeCheckbox.isSelected());

    // Overlays options
    Settings.SHOW_HP_OVERLAY.put(
        Settings.currentProfile, overlayPanelStatusDisplayCheckbox.isSelected());
    Settings.SHOW_BUFFS.put(Settings.currentProfile, overlayPanelBuffsCheckbox.isSelected());
    Settings.SHOW_LAST_MENU_ACTION.put(
        Settings.currentProfile, overlayPanelLastMenuActionCheckbox.isSelected());
    Settings.SHOW_MOUSE_TOOLTIP.put(
        Settings.currentProfile, overlayPanelMouseTooltipCheckbox.isSelected());
    Settings.SHOW_EXTENDED_TOOLTIP.put(
        Settings.currentProfile, overlayPanelExtendedTooltipCheckbox.isSelected());
    Settings.SHOW_INVCOUNT.put(Settings.currentProfile, overlayPanelInvCountCheckbox.isSelected());
    Settings.SHOW_RSCTIMES_BUTTONS.put(
        Settings.currentProfile, overlayPanelRscTimesButtonsCheckbox.isSelected());
    Settings.RSCTIMES_BUTTONS_FUNCTIONAL.put(
        Settings.currentProfile,
        overlayPanelRscTimesButtonsFunctionalCheckbox.isSelected()
            || overlayPanelRscTimesButtonsCheckbox.isSelected());
    Settings.WIKI_LOOKUP_ON_MAGIC_BOOK.put(
        Settings.currentProfile, overlayPanelWikiLookupOnMagicBookCheckbox.isSelected());
    Settings.WIKI_LOOKUP_ON_HBAR.put(
        Settings.currentProfile, overlayPanelWikiLookupOnHbarCheckbox.isSelected());
    Settings.TOGGLE_XP_BAR_ON_STATS_BUTTON.put(
        Settings.currentProfile, overlayPanelToggleXPBarOnStatsButtonCheckbox.isSelected());
    Settings.MOTIVATIONAL_QUOTES_BUTTON.put(
        Settings.currentProfile, overlayPanelToggleMotivationalQuotesCheckbox.isSelected());
    Settings.SHOW_PLAYER_POSITION.put(
        Settings.currentProfile, overlayPanelPositionCheckbox.isSelected());
    Settings.HIDE_FPS.put(Settings.currentProfile, overlayPanelHideFpsCheckbox.isSelected());
    Settings.SHOW_ITEM_GROUND_OVERLAY.put(
        Settings.currentProfile, overlayPanelItemNamesCheckbox.isSelected());
    Settings.SHOW_PLAYER_NAME_OVERLAY.put(
        Settings.currentProfile, overlayPanelPlayerNamesCheckbox.isSelected());
    Settings.SHOW_FRIEND_NAME_OVERLAY.put(
        Settings.currentProfile, overlayPanelFriendNamesCheckbox.isSelected());
    Settings.SHOW_NPC_NAME_OVERLAY.put(
        Settings.currentProfile, overlayPanelNPCNamesCheckbox.isSelected());
    Settings.EXTEND_IDS_OVERLAY.put(Settings.currentProfile, overlayPanelIDsCheckbox.isSelected());
    Settings.TRACE_OBJECT_INFO.put(
        Settings.currentProfile, overlayPanelObjectInfoCheckbox.isSelected());
    Settings.SHOW_HITBOX.put(Settings.currentProfile, overlayPanelHitboxCheckbox.isSelected());
    Settings.SHOW_COMBAT_INFO.put(
        Settings.currentProfile, overlayPanelShowCombatInfoCheckbox.isSelected());
    Settings.NPC_HEALTH_SHOW_PERCENTAGE.put(
        Settings.currentProfile, overlayPanelUsePercentageCheckbox.isSelected());
    Settings.SHOW_XP_BAR.put(Settings.currentProfile, overlayPanelXPBarCheckbox.isSelected());
    Settings.SHOW_FOOD_HEAL_OVERLAY.put(
        Settings.currentProfile, overlayPanelFoodHealingCheckbox.isSelected());
    Settings.SHOW_TIME_UNTIL_HP_REGEN.put(
        Settings.currentProfile, overlayPanelHPRegenTimerCheckbox.isSelected());
    Settings.LAG_INDICATOR.put(
        Settings.currentProfile, overlayPanelLagIndicatorCheckbox.isSelected());
    Settings.DEBUG.put(Settings.currentProfile, generalPanelDebugModeCheckbox.isSelected());
    Settings.EXCEPTION_HANDLER.put(
        Settings.currentProfile, generalPanelExceptionHandlerCheckbox.isSelected());
    Settings.HIGHLIGHTED_ITEMS.put(
        "custom", new ArrayList<>(Arrays.asList(highlightedItemsTextField.getText().split(","))));
    Settings.BLOCKED_ITEMS.put(
        "custom", new ArrayList<>(Arrays.asList(blockedItemsTextField.getText().split(","))));

    // Notifications options
    Settings.PM_NOTIFICATIONS.put(
        Settings.currentProfile, notificationPanelPMNotifsCheckbox.isSelected());
    Settings.TRADE_NOTIFICATIONS.put(
        Settings.currentProfile, notificationPanelTradeNotifsCheckbox.isSelected());
    Settings.UNDER_ATTACK_NOTIFICATIONS.put(
        Settings.currentProfile, notificationPanelUnderAttackNotifsCheckbox.isSelected());
    Settings.LOW_HP_NOTIFICATIONS.put(
        Settings.currentProfile, notificationPanelLowHPNotifsCheckbox.isSelected());
    Settings.LOW_HP_NOTIF_VALUE.put(
        Settings.currentProfile,
        ((SpinnerNumberModel) (notificationPanelLowHPNotifsSpinner.getModel()))
            .getNumber()
            .intValue());
    Settings.HIGHLIGHTED_ITEM_NOTIFICATIONS.put(
        Settings.currentProfile, notificationPanelHighlightedItemTimerCheckbox.isSelected());
    Settings.HIGHLIGHTED_ITEM_NOTIF_VALUE.put(
        Settings.currentProfile,
        ((SpinnerNumberModel) (notificationPanelHighlightedItemTimerSpinner.getModel()))
            .getNumber()
            .intValue());
    Settings.NOTIFICATION_SOUNDS.put(
        Settings.currentProfile, notificationPanelNotifSoundsCheckbox.isSelected());
    Settings.USE_SYSTEM_NOTIFICATIONS.put(
        Settings.currentProfile, notificationPanelUseSystemNotifsCheckbox.isSelected());
    Settings.TRAY_NOTIFS.put(
        Settings.currentProfile, notificationPanelTrayPopupCheckbox.isSelected());
    Settings.TRAY_NOTIFS_ALWAYS.put(
        Settings.currentProfile, notificationPanelTrayPopupAnyFocusButton.isSelected());
    Settings.SOUND_NOTIFS_ALWAYS.put(
        Settings.currentProfile, notificationPanelNotifSoundAnyFocusButton.isSelected());

    // Streaming & Privacy
    Settings.TWITCH_CHAT_ENABLED.put(
        Settings.currentProfile, streamingPanelTwitchChatIntegrationEnabledCheckbox.isSelected());
    Settings.TWITCH_HIDE_CHAT.put(
        Settings.currentProfile, streamingPanelTwitchChatCheckbox.isSelected());
    Settings.TWITCH_CHANNEL.put(
        Settings.currentProfile, streamingPanelTwitchChannelNameTextField.getText());
    Settings.TWITCH_OAUTH.put(
        Settings.currentProfile, streamingPanelTwitchOAuthTextField.getText());
    Settings.TWITCH_USERNAME.put(
        Settings.currentProfile, streamingPanelTwitchUserTextField.getText());
    Settings.SAVE_LOGININFO.put(
        Settings.currentProfile, streamingPanelSaveLoginCheckbox.isSelected());
    Settings.START_LOGINSCREEN.put(
        Settings.currentProfile, streamingPanelStartLoginCheckbox.isSelected());
    /*
    Settings.SPEEDRUNNER_MODE_ACTIVE.put(
            Settings.currentProfile, streamingPanelSpeedrunnerCheckbox.isSelected());
    // Settings.SPEEDRUNNER_USERNAME.put(
    //    Settings.currentProfile, streamingPanelSpeedrunnerUsernameTextField.getText());
    */

    // Presets
    if (presetsPanelCustomSettingsCheckbox.isSelected()) {
      if (!Settings.currentProfile.equals("custom")) {
        Settings.currentProfile = "custom";
        Logger.Info("Changed to custom profile");
      }
    } else {
      String lastPresetValue = Settings.currentProfile;

      int presetValue = presetsPanelPresetSlider.getValue();
      if (presetValue >= 0 && presetValue <= Settings.presetTable.size()) {
        Settings.currentProfile = Settings.presetTable.get(presetValue);
      } else { // not custom, and also out of range for presetTable
        Logger.Error("presetsPanelPresetSlider out of range of Settings.presetTable");
      }

      if (!lastPresetValue.equals(Settings.currentProfile)) {
        String saveMe = Settings.currentProfile;
        Settings.initSettings(); // reset preset values to their defaults
        Settings.currentProfile = saveMe;
        Logger.Info("Changed to " + Settings.currentProfile + " preset");
      }
    }

    // World List

    for (int i = 1; i <= Settings.WORLDS_TO_DISPLAY; i++) {
      Settings.WORLD_NAMES.put(
          i, getTextWithDefault(worldNamesJTextFields, i, String.format("World %d", i)));
      Settings.WORLD_URLS.put(i, worldUrlsJTextFields.get(i).getText());

      // Settings.WORLD_SERVER_TYPES.put(i, (Integer)
      // worldTypesJComboBoxes.get(i).getSelectedIndex());

      String portString = worldPortsJTextFields.get(i).getText();
      if (portString.equals("")) {
        Settings.WORLD_PORTS.put(i, 43594 /*Replay.DEFAULT_PORT*/);
      } else {
        Settings.WORLD_PORTS.put(i, Integer.parseInt(portString));
      }
    }

    if (Client.state == Client.STATE_LOGIN)
      Game.getInstance().getJConfig().changeWorld(Settings.WORLD.get(Settings.currentProfile));

    if (Client.state == Client.STATE_GAME) Client.sortFriends();

    // Save Settings
    Settings.save();
  }

  private String getTextWithDefault(
      HashMap<Integer, JTextField> textFields, int index, String defaultValue) {
    String value = textFields.get(index).getText();
    if (value.equals("")) return defaultValue;
    else return value;
  }

  public void disposeJFrame() {
    frame.dispose();
  }

  public JFrame getJFrame() {
    return frame;
  }

  /**
   * Sets the text of the button to its keybind.
   *
   * @param kbs The KeybindSet object to set the button text of.
   */
  public static void setKeybindButtonText(KeybindSet kbs) {
    kbs.button.setText(kbs.getFormattedKeybindText());
  }

  /**
   * Applies the settings in the Config GUI to the Settings class variables. <br>
   *
   * <p>Note that this method should be used to apply any additional settings that are not applied
   * automatically, such as those already present. Also note that thread-unsafe operations affecting
   * the applet should not be done in this method, as this method is invoked by the AWT event queue.
   */
  public void applySettings() {
    saveSettings();
    // Tell the Renderer to update the scale from its thread to avoid thread-safety issues.
    Settings.renderingScalarUpdateRequired = true;
    // Tell the Renderer to update the FoV from its thread to avoid thread-safety issues.
    Settings.fovUpdateRequired = true;
    Settings.checkSoftwareCursor();
    Camera.setDistance(Settings.VIEW_DISTANCE.get(Settings.currentProfile));
    synchronizeGuiValues();
    // QueueWindow.syncColumnsWithSettings();
    // bQueueWindow.playlistTable.repaint();
    // Item.patchItemCommands();
  }

  public void synchronizePresetOptions() {
    if (presetsPanelCustomSettingsCheckbox.isSelected()) {
      if (sliderValue == -1) {
        presetsPanelPresetSlider.setValue(Settings.presetTable.indexOf("default"));
      } else {
        presetsPanelPresetSlider.setValue(sliderValue);
      }
      presetsPanelPresetSlider.setEnabled(false);
      replaceConfigButton.setEnabled(false);
      resetPresetsButton.setEnabled(false);
    } else {
      presetsPanelPresetSlider.setEnabled(true);
      replaceConfigButton.setEnabled(true);
      resetPresetsButton.setEnabled(true);
    }
  }

  public void addWorldFields(int i) {
    //// Name line
    worldListTitleTextFieldContainers.put(i, new JPanel());
    worldListTitleTextFieldContainers.get(i).setLayout(new GridBagLayout());
    GridBagConstraints cR = new GridBagConstraints();
    cR.fill = GridBagConstraints.HORIZONTAL;
    cR.anchor = GridBagConstraints.LINE_START;
    cR.weightx = 0.1;
    cR.gridy = 0;
    cR.gridwidth = 1;

    JLabel worldNumberJLabel = new JLabel(String.format("<html><b>World %d</b></html>", i));
    worldNumberJLabel.setAlignmentY(0.75f);
    worldListTitleTextFieldContainers.get(i).add(worldNumberJLabel, cR);

    cR.weightx = 0.5;
    cR.gridwidth = 5;

    if (Util.isUsingFlatLAFTheme()) {
      cR.insets = new Insets(0, 0, 0, osScaleMul(4));
    }

    worldNamesJTextFields.put(i, new HintTextField("Name of World"));
    worldNamesJTextFields.get(i).setMinimumSize(osScaleMul(new Dimension(80, 28)));
    worldNamesJTextFields.get(i).setMaximumSize(osScaleMul(new Dimension(300, 28)));
    worldNamesJTextFields.get(i).setPreferredSize(osScaleMul(new Dimension(202, 28)));
    worldNamesJTextFields.get(i).setAlignmentY(0.75f);

    worldListTitleTextFieldContainers.get(i).add(worldNamesJTextFields.get(i), cR);

    cR.weightx = 0.1;
    cR.gridwidth = 1;
    cR.anchor = GridBagConstraints.LINE_END;

    /*
          JLabel spacingJLabel = new JLabel("");
          worldNumberJLabel.setAlignmentY(0.75f);
          worldListTitleTextFieldContainers.get(i).add(spacingJLabel, cR);
    */

    cR.weightx = 0.3;
    cR.gridwidth = 1;

    if (Util.isUsingFlatLAFTheme()) {
      cR.insets = new Insets(0, 0, 0, 0);
    }

    worldDeleteJButtons.put(i, new JButton("Delete World"));
    worldDeleteJButtons.get(i).setAlignmentY(0.80f);
    worldDeleteJButtons.get(i).setPreferredSize(osScaleMul(new Dimension(50, 28)));
    worldDeleteJButtons.get(i).setActionCommand(String.format("%d", i));
    worldDeleteJButtons
        .get(i)
        .addActionListener(
            new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                String actionCommandWorld = e.getActionCommand();

                JPanel confirmDeleteWorldPanel =
                    Util.createOptionMessagePanel(
                        "<b>Warning</b>: Are you sure you want to <b>DELETE</b> World %s?",
                        actionCommandWorld);
                int choice =
                    JOptionPane.showConfirmDialog(
                        Launcher.getConfigWindow().frame,
                        confirmDeleteWorldPanel,
                        "Confirm",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.CLOSED_OPTION || choice == JOptionPane.NO_OPTION) {
                  return;
                }

                Logger.Info("Deleting World " + actionCommandWorld);
                Settings.removeWorld(Integer.parseInt(actionCommandWorld));
              }
            });

    worldListTitleTextFieldContainers.get(i).add(worldDeleteJButtons.get(i), cR);

    worldListTitleTextFieldContainers.get(i).setMaximumSize(osScaleMul(new Dimension(680, 40)));
    worldListPanel.add(worldListTitleTextFieldContainers.get(i));

    //// URL/Ports line
    worldUrlsJTextFields.put(i, new HintTextField(String.format("World %d URL", i)));
    worldPortsJTextFields.put(
        i, new HintTextField(String.format("World %d Port (default: 43594)", i)));

    worldUrlsJTextFields.get(i).setMinimumSize(osScaleMul(new Dimension(100, 28)));
    worldUrlsJTextFields.get(i).setMaximumSize(osScaleMul(new Dimension(500, 28)));
    worldUrlsJTextFields.get(i).setPreferredSize(osScaleMul(new Dimension(500, 28)));
    worldUrlsJTextFields.get(i).setAlignmentY(0.75f);

    int portOffset = Util.isUsingFlatLAFTheme() ? 4 : 0;
    worldPortsJTextFields.get(i).setMinimumSize(osScaleMul(new Dimension(100 - portOffset, 28)));
    worldPortsJTextFields.get(i).setMaximumSize(osScaleMul(new Dimension(180 - portOffset, 28)));
    worldPortsJTextFields.get(i).setAlignmentY(0.75f);

    worldListURLPortTextFieldContainers.put(i, new JPanel());

    worldListURLPortTextFieldContainers
        .get(i)
        .setLayout(new BoxLayout(worldListURLPortTextFieldContainers.get(i), BoxLayout.X_AXIS));

    worldListURLPortTextFieldContainers.get(i).add(worldUrlsJTextFields.get(i));
    if (Util.isUsingFlatLAFTheme()) {
      JLabel spacingLabel = new JLabel("");
      spacingLabel.setBorder(BorderFactory.createEmptyBorder(0, osScaleMul(4), 0, 0));
      worldListURLPortTextFieldContainers.get(i).add(spacingLabel);
      worldListURLPortTextFieldContainers
          .get(i)
          .setBorder(BorderFactory.createEmptyBorder(osScaleMul(4), 0, osScaleMul(4), 0));
    }
    worldListURLPortTextFieldContainers.get(i).add(worldPortsJTextFields.get(i));
    worldListPanel.add(worldListURLPortTextFieldContainers.get(i));

    worldListSpacingLabels.put(i, new JLabel(""));
    worldListSpacingLabels
        .get(i)
        .setBorder(BorderFactory.createEmptyBorder(osScaleMul(30), 0, 0, 0));
    worldListPanel.add(worldListSpacingLabels.get(i));

    if (i > Settings.WORLD_NAMES.size()) {
      Settings.createNewWorld(i);
    }
  }

  public void addAddWorldButton() {
    JButton addWorldButton = new JButton("Add New World");
    addWorldButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
    addWorldButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            worldListPanel.remove(addWorldButton);
            Component verticalGlue =
                Arrays.stream(worldListPanel.getComponents())
                    .filter(
                        c -> c.getName() != null && c.getName().equals("world_listPanelBottomGlue"))
                    .findFirst()
                    .orElse(null);
            if (verticalGlue != null) {
              worldListPanel.remove(verticalGlue);
            }
            ++Settings.WORLDS_TO_DISPLAY;
            synchronizeWorldTab();
            addAddWorldButton();
            if (verticalGlue != null) {
              worldListPanel.add(verticalGlue);
            }
          }
        });
    worldListPanel.add(addWorldButton);
    worldListPanel.revalidate();
    worldListPanel.repaint();
  }

  public void updateCustomClientSizeMinValues(Dimension updatedMinimumWindowSize) {
    spinnerWinXModel.setMinimum(updatedMinimumWindowSize.width);
    spinnerWinYModel.setMinimum(updatedMinimumWindowSize.height);
  }

  /**
   * Creates an EventQueue listener, used to capture {@link MouseEvent#MOUSE_ENTERED} events for
   * display tooltip text within the {@link #toolTipPanel}
   *
   * @return The constructed {@link AWTEventListener} instance
   */
  private AWTEventListener createConfigWindowEventQueueListener() {
    return new AWTEventListener() {
      @Override
      public void eventDispatched(AWTEvent e) {
        try {
          // Exit early if the label hasn't been initialized
          if (toolTipTextLabel == null) {
            return;
          }

          // Exit early for things that aren't MOUSE_ENTERED events
          if (e.getID() != MouseEvent.MOUSE_ENTERED) {
            return;
          }

          // Exit early for events that aren't on a JComponent
          if (!(e.getSource() instanceof JComponent)) {
            return;
          }

          // Exit early for events that did not originate from the ConfigWindow
          if (SwingUtilities.getWindowAncestor((JComponent) e.getSource()) != frame) {
            return;
          }

          String componentToolTipText = ((JComponent) e.getSource()).getToolTipText();

          if (componentToolTipText != null && !componentToolTipText.equals(toolTipTextString)) {
            toolTipTextString = componentToolTipText;
            toolTipTextLabel.setText(toolTipTextString);
          }
        } catch (Exception ex) {
          Logger.Error(
              "There was an error with processing the MOUSE_ENTERED event listener."
                  + "Please screenshot and report this error if possible.");
          ex.printStackTrace();
        }
      }
    };
  }

  /** Attaches the EventQueue listener */
  private void addConfigWindowEventQueueListener() {
    if (isListeningForEventQueue) {
      return;
    }

    // Disable tooltips
    ToolTipManager.sharedInstance().setEnabled(false);

    // Add listener
    Toolkit.getDefaultToolkit().addAWTEventListener(eventQueueListener, AWTEvent.MOUSE_EVENT_MASK);

    isListeningForEventQueue = true;
  }

  /** Detaches the EventQueue listener */
  private void removeConfigWindowEventQueueListener() {
    if (!isListeningForEventQueue) {
      return;
    }

    // Enable tooltips
    ToolTipManager.sharedInstance().setEnabled(true);

    // Remove listener
    Toolkit.getDefaultToolkit().removeAWTEventListener(eventQueueListener);

    isListeningForEventQueue = false;
  }

  // adds or removes world list text fields & fills them with their values
  public void synchronizeWorldTab() {
    int numberOfWorldsEver = worldUrlsJTextFields.size();
    // sync values from Settings (read in from file) & hide worlds that have gotten deleted
    if (Settings.WORLDS_TO_DISPLAY > numberOfWorldsEver) {
      addWorldFields(Settings.WORLDS_TO_DISPLAY);
    }
    for (int i = 1; (i <= numberOfWorldsEver) || (i <= Settings.WORLDS_TO_DISPLAY); i++) {
      if (i <= Settings.WORLDS_TO_DISPLAY) {
        worldNamesJTextFields.get(i).setText(Settings.WORLD_NAMES.get(i));
        worldUrlsJTextFields.get(i).setText(Settings.WORLD_URLS.get(i));
        try {
          worldPortsJTextFields.get(i).setText(Settings.WORLD_PORTS.get(i).toString());
        } catch (Exception e) {
          worldNamesJTextFields.get(i).setText(String.format("World %d", i));
          Settings.createNewWorld(i);
        }

        worldListTitleTextFieldContainers.get(i).setVisible(true);
        worldListURLPortTextFieldContainers.get(i).setVisible(true);
        worldListSpacingLabels.get(i).setVisible(true);
      } else {
        worldListTitleTextFieldContainers.get(i).setVisible(false);
        worldListURLPortTextFieldContainers.get(i).setVisible(false);
        worldListSpacingLabels.get(i).setVisible(false);
      }
    }
  }
}

/** Implements ActionListener; to be used for the buttons in the keybinds tab. */
class ClickListener implements ActionListener {

  @Override
  public void actionPerformed(ActionEvent e) {
    JButton button = (JButton) e.getSource();
    button.setText("...");
    button.setFocusable(true);
    button.requestFocusInWindow();
  }
}

class ButtonFocusListener implements FocusListener {

  @Override
  public void focusGained(FocusEvent arg0) {}

  @Override
  public void focusLost(FocusEvent arg0) {
    JButton button = (JButton) arg0.getSource();

    for (KeybindSet kbs : KeyboardHandler.keybindSetList) {
      if (button == kbs.button) {
        ConfigWindow.setKeybindButtonText(kbs);
        kbs.button.setFocusable(false);
      }
    }
  }
}

class RebindListener implements KeyListener {

  @Override
  public void keyPressed(KeyEvent arg0) {
    KeyModifier modifier = KeyModifier.NONE;

    if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
      for (KeybindSet kbs : KeyboardHandler.keybindSetList) {
        if (arg0.getSource() == kbs.button) {
          kbs.modifier = KeyModifier.NONE;
          kbs.key = -1;
          ConfigWindow.setKeybindButtonText(kbs);
          kbs.button.setFocusable(false);
        }
      }
      return;
    }

    if (arg0.getKeyCode() == KeyEvent.VK_CONTROL
        || arg0.getKeyCode() == KeyEvent.VK_SHIFT
        || arg0.getKeyCode() == KeyEvent.VK_ALT) {
      return;
    }

    if (arg0.isControlDown()) {
      modifier = KeyModifier.CTRL;
    } else if (arg0.isShiftDown()) {
      modifier = KeyModifier.SHIFT;
    } else if (arg0.isAltDown()) {
      modifier = KeyModifier.ALT;
    }

    int key = arg0.getKeyCode();
    JButton jbtn = (JButton) arg0.getSource();

    if (key != -1)
      for (KeybindSet kbs : KeyboardHandler.keybindSetList) {
        if ((jbtn != kbs.button) && kbs.isDuplicateKeybindSet(modifier, key)) {
          jbtn.setText("DUPLICATE!");
          return;
        }
      }

    for (KeybindSet kbs : KeyboardHandler.keybindSetList) {
      if (jbtn == kbs.button) {
        kbs.modifier = modifier;
        kbs.key = key;
        ConfigWindow.setKeybindButtonText(kbs);
        kbs.button.setFocusable(false);
      }
    }
  }

  @Override
  public void keyReleased(KeyEvent arg0) {}

  @Override
  public void keyTyped(KeyEvent arg0) {}
}

class HintTextField extends JTextField {
  public HintTextField(String hint) {
    _hint = hint;
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    if (getText().length() == 0) {
      int h = getHeight();
      ((Graphics2D) g)
          .setRenderingHint(
              RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      Insets ins = getInsets();
      FontMetrics fm = g.getFontMetrics();
      int c0 = getBackground().getRGB();
      int c1 = getForeground().getRGB();
      int m = 0xfefefefe;
      int c2 = ((c0 & m) >>> 1) + ((c1 & m) >>> 1);
      g.setColor(new Color(c2, true));
      g.drawString(_hint, ins.left, h / 2 + fm.getAscent() / 2 - 2);
    }
  }

  private final String _hint;
}
