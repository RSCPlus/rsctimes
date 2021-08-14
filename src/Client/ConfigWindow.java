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

import Client.KeybindSet.KeyModifier;
// import Game.Camera;
import Game.Game;
// import Game.Item;
import Game.KeyboardHandler;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.SystemTray;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
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
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

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

    /*
     * JComponent variables which hold configuration data
     */

    //// General tab
    private JCheckBox generalPanelClientSizeCheckbox;
    private JSpinner generalPanelClientSizeXSpinner;
    private JSpinner generalPanelClientSizeYSpinner;
    private JCheckBox generalPanelCheckUpdates;
    private JCheckBox generalPanelAccountSecurityCheckbox;
    private JCheckBox generalPanelConfirmCancelRecoveryChangeCheckbox;
    private JCheckBox generalPanelShowSecurityTipsAtLoginCheckbox;
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
    private JCheckBox generalPanelKeepScrollbarPosGoodMagicEvilMagicCheckbox;
    private JCheckBox generalPanelRoofHidingCheckbox;
    private JCheckBox generalPanelCameraZoomableCheckbox;
    private JCheckBox generalPanelCameraRotatableCheckbox;
    private JCheckBox generalPanelCameraMovableCheckbox;
    private JCheckBox generalPanelCameraMovableRelativeCheckbox;
    private JCheckBox generalPanelColoredTextCheckbox;
    private JSlider generalPanelFoVSlider;
    private JCheckBox generalPanelCustomCursorCheckbox;
    private JSlider generalPanelViewDistanceSlider;
    private JCheckBox generalPanelLimitFPSCheckbox;
    private JSpinner generalPanelLimitFPSSpinner;
    private JCheckBox generalPanelAutoScreenshotCheckbox;
    private JCheckBox generalPanelPatchGenderCheckbox;
    private JCheckBox generalPanelPatchHbar512LastPixelCheckbox;
    private JCheckBox generalPanelPatchWrenchMenuSpacingCheckbox;
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
    private JCheckBox overlayPanelRemoveReportAbuseButtonHbarCheckbox;
    private JCheckBox overlayPanelPositionCheckbox;
    private JCheckBox overlayPanelRetroFpsCheckbox;
    private JCheckBox overlayPanelItemNamesCheckbox;
    private JCheckBox overlayPanelPlayerNamesCheckbox;
    private JCheckBox overlayPanelFriendNamesCheckbox;
    private JCheckBox overlayPanelNPCNamesCheckbox;
    private JCheckBox overlayPanelIDsCheckbox;
    private JCheckBox overlayPanelObjectInfoCheckbox;
    private JCheckBox overlayPanelHitboxCheckbox;
    private JCheckBox overlayPanelXPBarCheckbox;
    private JCheckBox overlayPanelXPDropsCheckbox;
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
    private JCheckBox notificationPanelLogoutNotifsCheckbox;
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
    private JCheckBox streamingPanelIPAtLoginCheckbox;
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
        try {
            // Set System L&F as a fall-back option.
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    NimbusLookAndFeel laf = (NimbusLookAndFeel) UIManager.getLookAndFeel();
                    laf.getDefaults().put("defaultFont", new Font(Font.SANS_SERIF, Font.PLAIN, 11));
                    break;
                }
            }
        } catch (UnsupportedLookAndFeelException e) {
            Logger.Error("Unable to set L&F: Unsupported look and feel");
        } catch (ClassNotFoundException e) {
            Logger.Error("Unable to set L&F: Class not found");
        } catch (InstantiationException e) {
            Logger.Error("Unable to set L&F: Class object cannot be instantiated");
        } catch (IllegalAccessException e) {
            Logger.Error("Unable to set L&F: Illegal access exception");
        }
        initialize();
    }

    public void showConfigWindow() {
        this.synchronizeGuiValues();
        frame.setVisible(true);
    }

    public void hideConfigWindow() {
        frame.setVisible(false);
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
        frame.setBounds(100, 100, 800, 650);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        URL iconURL = Launcher.getResource("/assets/icon.png");
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            frame.setIconImage(icon.getImage());
        }

        // Container declarations
        /** The tabbed pane holding the five configuration tabs */
        tabbedPane = new JTabbedPane();
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

        JPanel presetsPanel = new JPanel();
        JPanel generalPanel = new JPanel();
        JPanel overlayPanel = new JPanel();
        JPanel notificationPanel = new JPanel();
        JPanel streamingPanel = new JPanel();
        JPanel keybindPanel = new JPanel();
        worldListPanel = new JPanel();
        JPanel authorsPanel = new JPanel();

        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        frame.getContentPane().add(navigationPanel, BorderLayout.PAGE_END);

        // tabbedPane.addTab("Presets", null, presetsScrollPane, null);
        tabbedPane.addTab("General", null, generalScrollPane, null);
        // tabbedPane.addTab("Overlays", null, overlayScrollPane, null);
        // tabbedPane.addTab("Notifications", null, notificationScrollPane, null);
        // tabbedPane.addTab("Streaming & Privacy", null, streamingScrollPane, null);
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
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(7, 10, 10, 10));
        presetsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        generalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        overlayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        notificationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        streamingPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        keybindPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        worldListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        authorsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setScrollSpeed(presetsScrollPane, 20, 15);
        setScrollSpeed(generalScrollPane, 20, 15);
        setScrollSpeed(overlayScrollPane, 20, 15);
        setScrollSpeed(notificationScrollPane, 20, 15);
        setScrollSpeed(streamingScrollPane, 20, 15);
        setScrollSpeed(keybindScrollPane, 20, 15);
        setScrollSpeed(worldListScrollPane, 20, 15);
        setScrollSpeed(authorsScrollPane, 20, 15);

        /*
         * Navigation buttons
         */

        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));

        addButton("OK", navigationPanel, Component.LEFT_ALIGNMENT)
                .addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Launcher.getConfigWindow().saveSettings();
                                Launcher.getConfigWindow().hideConfigWindow();
                            }
                        });

        addButton("Cancel", navigationPanel, Component.LEFT_ALIGNMENT)
                .addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Launcher.getConfigWindow().applySettings();
                                Launcher.getConfigWindow().hideConfigWindow();
                            }
                        });

        addButton("Apply", navigationPanel, Component.LEFT_ALIGNMENT)
                .addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Launcher.getConfigWindow().applySettings();
                            }
                        });

        navigationPanel.add(Box.createHorizontalGlue());
        addButton("Restore Defaults", navigationPanel, Component.RIGHT_ALIGNMENT)
                .addActionListener(
                        new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int choice =
                                        JOptionPane.showConfirmDialog(
                                                Launcher.getConfigWindow().frame,
                                                "Are you sure you want to restore all settings to their defaults?",
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
        generalPanelClientSizePanel.setPreferredSize(new Dimension(0, 37));
        generalPanelClientSizePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // TODO: Perhaps change to "Save client size on close"?
        generalPanelClientSizeCheckbox =
                addCheckbox("Default client size:", generalPanelClientSizePanel);
        generalPanelClientSizeCheckbox.setToolTipText("Start the client with the supplied window size");
        generalPanelClientSizeCheckbox.setEnabled(false);

        generalPanelClientSizeXSpinner = new JSpinner();
        generalPanelClientSizePanel.add(generalPanelClientSizeXSpinner);
        generalPanelClientSizeXSpinner.setMaximumSize(new Dimension(58, 22));
        generalPanelClientSizeXSpinner.setMinimumSize(new Dimension(58, 22));
        generalPanelClientSizeXSpinner.setAlignmentY((float) 0.75);
        generalPanelClientSizeXSpinner.setToolTipText("Default client width (512 minimum)");
        generalPanelClientSizeXSpinner.putClientProperty("JComponent.sizeVariant", "mini");

        JLabel generalPanelClientSizeByLabel = new JLabel("x");
        generalPanelClientSizePanel.add(generalPanelClientSizeByLabel);
        generalPanelClientSizeByLabel.setAlignmentY((float) 0.9);
        generalPanelClientSizeByLabel.setBorder(new EmptyBorder(0, 2, 0, 2));

        generalPanelClientSizeYSpinner = new JSpinner();
        generalPanelClientSizePanel.add(generalPanelClientSizeYSpinner);
        generalPanelClientSizeYSpinner.setMaximumSize(new Dimension(58, 22));
        generalPanelClientSizeYSpinner.setMinimumSize(new Dimension(58, 22));
        generalPanelClientSizeYSpinner.setAlignmentY((float) 0.75);
        generalPanelClientSizeYSpinner.setToolTipText("Default client height (334 minimum)");
        generalPanelClientSizeYSpinner.putClientProperty("JComponent.sizeVariant", "mini");

        generalPanelClientSizeXSpinner.setEnabled(false);
        generalPanelClientSizeYSpinner.setEnabled(false);


        // Sanitize JSpinner values
        SpinnerNumberModel spinnerWinXModel = new SpinnerNumberModel();
        spinnerWinXModel.setMinimum(512);
        spinnerWinXModel.setValue(512);
        spinnerWinXModel.setStepSize(10);
        generalPanelClientSizeXSpinner.setModel(spinnerWinXModel);
        SpinnerNumberModel spinnerWinYModel = new SpinnerNumberModel();
        spinnerWinYModel.setMinimum(334);
        spinnerWinYModel.setValue(334);
        spinnerWinYModel.setStepSize(10);
        generalPanelClientSizeYSpinner.setModel(spinnerWinYModel);

        generalPanelCheckUpdates =
                addCheckbox("Check for rsctimes updates from GitHub at launch", generalPanel);
        generalPanelCheckUpdates.setToolTipText(
                "When enabled, rsctimes will check for client updates before launching the game and install them when prompted");

        generalPanelWelcomeEnabled =
                addCheckbox("Remind you how to open the Settings every time you log in", generalPanel);
        generalPanelWelcomeEnabled.setToolTipText(
                "When enabled, rsctimes will insert a message telling the current keybinding to open the settings menu and remind you about the tray icon");
        generalPanelWelcomeEnabled.setEnabled(false);

        generalPanelCustomCursorCheckbox = addCheckbox("Use custom mouse cursor", generalPanel);
        generalPanelCustomCursorCheckbox.setToolTipText(
                "Switch to using a custom mouse cursor instead of the system default");
        generalPanelCustomCursorCheckbox.setEnabled(false);

        generalPanelAutoScreenshotCheckbox =
                addCheckbox("Take a screenshot when you level up or complete a quest", generalPanel);
        generalPanelAutoScreenshotCheckbox.setToolTipText(
                "Takes a screenshot for you for level ups and quest completion");
        generalPanelAutoScreenshotCheckbox.setEnabled(false);

        JLabel generalPanelFoVLabel = new JLabel("Field of view (Default 9)");
        generalPanelFoVLabel.setToolTipText("Sets the field of view (not recommended past 10)");
        generalPanel.add(generalPanelFoVLabel);
        generalPanelFoVLabel.setAlignmentY((float) 0.9);

        generalPanelFoVSlider = new JSlider();
        generalPanelFoVLabel.setEnabled(false);
        generalPanelFoVSlider.setEnabled(false);

        generalPanel.add(generalPanelFoVSlider);
        generalPanelFoVSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        generalPanelFoVSlider.setMaximumSize(new Dimension(200, 55));
        generalPanelFoVSlider.setBorder(new EmptyBorder(0, 0, 5, 0));
        generalPanelFoVSlider.setMinimum(7);
        generalPanelFoVSlider.setMaximum(16);
        generalPanelFoVSlider.setMajorTickSpacing(1);
        generalPanelFoVSlider.setPaintTicks(true);
        generalPanelFoVSlider.setPaintLabels(true);

        JLabel generalPanelViewDistanceLabel = new JLabel("View distance");
        generalPanelViewDistanceLabel.setToolTipText(
                "Sets the max render distance of structures and landscape");
        generalPanel.add(generalPanelViewDistanceLabel);
        generalPanelViewDistanceLabel.setAlignmentY((float) 0.9);

        generalPanelViewDistanceSlider = new JSlider();
        generalPanelViewDistanceLabel.setEnabled(false);
        generalPanelViewDistanceSlider.setEnabled(false);

        generalPanel.add(generalPanelViewDistanceSlider);
        generalPanelViewDistanceSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        generalPanelViewDistanceSlider.setMaximumSize(new Dimension(200, 55));
        generalPanelViewDistanceSlider.setBorder(new EmptyBorder(0, 0, 5, 0));
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
        JPanel generalPanelLimitFPSPanel = new JPanel();
        generalPanel.add(generalPanelLimitFPSPanel);
        generalPanelLimitFPSPanel.setLayout(new BoxLayout(generalPanelLimitFPSPanel, BoxLayout.X_AXIS));
        generalPanelLimitFPSPanel.setPreferredSize(new Dimension(0, 37));
        generalPanelLimitFPSPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        generalPanelLimitFPSCheckbox =
                addCheckbox("FPS limit (doubled while F1 interlaced):", generalPanelLimitFPSPanel);
        generalPanelLimitFPSCheckbox.setToolTipText(
                "Limit FPS for a more 2001 feeling (or to save battery)");
        generalPanelLimitFPSCheckbox.setEnabled(false);

        generalPanelLimitFPSSpinner = new JSpinner();
        generalPanelLimitFPSPanel.add(generalPanelLimitFPSSpinner);
        generalPanelLimitFPSSpinner.setMaximumSize(new Dimension(45, 22));
        generalPanelLimitFPSSpinner.setMinimumSize(new Dimension(45, 22));
        generalPanelLimitFPSSpinner.setAlignmentY((float) 0.75);
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

        JPanel generalPanelLogVerbosityPanel = new JPanel();
        generalPanelLogVerbosityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        generalPanelLogVerbosityPanel.setMaximumSize(new Dimension(350, 128));
        generalPanelLogVerbosityPanel.setLayout(
                new BoxLayout(generalPanelLogVerbosityPanel, BoxLayout.Y_AXIS));
        generalPanel.add(generalPanelLogVerbosityPanel);

        JLabel generalPanelLogVerbosityTitle = new JLabel("Log verbosity maximum");
        generalPanelLogVerbosityTitle.setToolTipText(
                "What max level of log text will be shown in the rsctimes log/console");
        generalPanelLogVerbosityPanel.add(generalPanelLogVerbosityTitle);
        generalPanelLogVerbosityTitle.setAlignmentY((float) 0.9);

        Hashtable<Integer, JLabel> generalPanelLogVerbosityLabelTable =
                new Hashtable<Integer, JLabel>();
        generalPanelLogVerbosityLabelTable.put(new Integer(0), new JLabel("Error"));
        generalPanelLogVerbosityLabelTable.put(new Integer(1), new JLabel("Warning"));
        generalPanelLogVerbosityLabelTable.put(new Integer(2), new JLabel("Game"));
        generalPanelLogVerbosityLabelTable.put(new Integer(3), new JLabel("Info"));
        generalPanelLogVerbosityLabelTable.put(new Integer(4), new JLabel("Debug"));
        generalPanelLogVerbosityLabelTable.put(new Integer(5), new JLabel("Opcode"));

        generalPanelLogVerbositySlider = new JSlider();
        generalPanelLogVerbositySlider.setMajorTickSpacing(1);
        generalPanelLogVerbositySlider.setLabelTable(generalPanelLogVerbosityLabelTable);
        generalPanelLogVerbositySlider.setPaintLabels(true);
        generalPanelLogVerbositySlider.setPaintTicks(true);
        generalPanelLogVerbositySlider.setSnapToTicks(true);
        generalPanelLogVerbositySlider.setMinimum(0);
        generalPanelLogVerbositySlider.setMaximum(5);
        generalPanelLogVerbositySlider.setPreferredSize(new Dimension(200, 55));
        generalPanelLogVerbositySlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        generalPanelLogVerbositySlider.setBorder(new EmptyBorder(0, 0, 5, 0));
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

        generalPanelColoredTextCheckbox = addCheckbox("Colored console text", generalPanel);
        generalPanelColoredTextCheckbox.setToolTipText(
                "When running the client from a console, chat messages in the console will reflect the colors they are in game");

        generalPanelDebugModeCheckbox = addCheckbox("Enable debug mode", generalPanel);
        generalPanelDebugModeCheckbox.setToolTipText(
                "Shows debug overlays and enables debug text in the console");
        generalPanelDebugModeCheckbox.setEnabled(false);

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
        generalPanelCombatXPMenuCheckbox.setBorder(new EmptyBorder(7, 0, 10, 0));
        generalPanelCombatXPMenuCheckbox.setEnabled(false);

        generalPanelCombatXPMenuHiddenCheckbox =
                addCheckbox("Combat style menu hidden when in combat", generalPanel);
        generalPanelCombatXPMenuHiddenCheckbox.setToolTipText("Hide combat style menu when in combat");
        generalPanelCombatXPMenuHiddenCheckbox.setEnabled(false);

        generalPanelInventoryFullAlertCheckbox = addCheckbox("Inventory full alert", generalPanel);
        generalPanelInventoryFullAlertCheckbox.setToolTipText(
                "Displays a large notice when the inventory is full");
        generalPanelInventoryFullAlertCheckbox.setEnabled(false);

        generalPanelKeepScrollbarPosGoodMagicEvilMagicCheckbox =
                addCheckbox("Keep GoodMagic & EvilMagic scrollbar position", generalPanel);
        generalPanelKeepScrollbarPosGoodMagicEvilMagicCheckbox.setToolTipText(
                "Keeps the GoodMagic & EvilMagic scrollbar position when switching between tabs");
        generalPanelKeepScrollbarPosGoodMagicEvilMagicCheckbox.setEnabled(false);

        generalPanelRoofHidingCheckbox = addCheckbox("Roof hiding", generalPanel);
        generalPanelRoofHidingCheckbox.setToolTipText("Always hide rooftops");
        generalPanelRoofHidingCheckbox.setEnabled(false);

        generalPanelCameraZoomableCheckbox = addCheckbox("Camera zoom enhancement", generalPanel);
        generalPanelCameraZoomableCheckbox.setToolTipText(
                "Zoom the camera in and out with the mouse wheel, and no longer zooms in inside buildings");
        generalPanelCameraZoomableCheckbox.setEnabled(false);

        generalPanelCameraRotatableCheckbox = addCheckbox("Camera rotation enhancement", generalPanel);
        generalPanelCameraRotatableCheckbox.setToolTipText(
                "Rotate the camera with middle mouse click, among other things");
        generalPanelCameraRotatableCheckbox.setEnabled(false);

        generalPanelCameraMovableCheckbox = addCheckbox("Camera movement enhancement", generalPanel);
        generalPanelCameraMovableCheckbox.setToolTipText(
                "Makes the camera follow the player more closely, and allow camera movement while holding shift, and pressing arrow keys");
        generalPanelCameraMovableCheckbox.setEnabled(false);

        generalPanelCameraMovableRelativeCheckbox =
                addCheckbox("Camera movement is relative to player", generalPanel);
        generalPanelCameraMovableRelativeCheckbox.setToolTipText(
                "Camera movement will follow the player position");
        generalPanelCameraMovableRelativeCheckbox.setEnabled(false);

        addSettingsHeader(generalPanel, "Menu/Item patching");

        JPanel generalPanelNamePatchModePanel = new JPanel();
        generalPanelNamePatchModePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        generalPanelNamePatchModePanel.setMaximumSize(new Dimension(300, 60));
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
        generalPanelNamePatchModeSlider.setPreferredSize(new Dimension(33, 0));
        generalPanelNamePatchModeSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        generalPanelNamePatchModeSlider.setBorder(new EmptyBorder(7, 0, 10, 0));
        generalPanelNamePatchModeSlider.setOrientation(SwingConstants.VERTICAL);
        generalPanelNamePatchModePanel.add(generalPanelNamePatchModeSlider);
        generalPanelNamePatchModeSlider.setEnabled(false);

        JPanel generalPanelNamePatchModeTextPanel = new JPanel();
        generalPanelNamePatchModeTextPanel.setPreferredSize(new Dimension(255, 80));
        generalPanelNamePatchModeTextPanel.setLayout(new BorderLayout());
        generalPanelNamePatchModeTextPanel.setBorder(new EmptyBorder(7, 10, 0, 0));
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

        /*
         * Overlays tab
         */

        overlayPanel.setLayout(new BoxLayout(overlayPanel, BoxLayout.Y_AXIS));

        /// "Interface Overlays" are overlays that have a constant position on
        /// the screen because they are designed to modify just the interface of RSC+
        addSettingsHeader(overlayPanel, "Interface Overlays");
        overlayPanelStatusDisplayCheckbox = addCheckbox("Show HP display", overlayPanel);
        overlayPanelStatusDisplayCheckbox.setToolTipText("Toggle hits display");
        overlayPanelStatusDisplayCheckbox.setBorder(new EmptyBorder(0, 0, 10, 0));

        overlayPanelBuffsCheckbox =
                addCheckbox("Show combat (de)buffs and cooldowns display", overlayPanel);
        overlayPanelBuffsCheckbox.setToolTipText("Toggle combat (de)buffs and cooldowns display");

        overlayPanelLastMenuActionCheckbox = addCheckbox("Show last menu action display", overlayPanel);
        overlayPanelLastMenuActionCheckbox.setToolTipText("Toggle last menu action used display");

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
                addCheckbox("Able to click in-game buttons to activate RSCx features", overlayPanel);
        overlayPanelRscTimesButtonsFunctionalCheckbox.setToolTipText(
                "Able to click in-game buttons to activate RSCx features");

        JPanel overlayPanelRscTimesButtonsPanel = new JPanel();
        overlayPanel.add(overlayPanelRscTimesButtonsPanel);
        overlayPanelRscTimesButtonsPanel.setLayout(
                new BoxLayout(overlayPanelRscTimesButtonsPanel, BoxLayout.X_AXIS));
        overlayPanelRscTimesButtonsPanel.setPreferredSize(new Dimension(0, 37));
        overlayPanelRscTimesButtonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        overlayPanelRscTimesButtonsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        JLabel rsctimesButtonsSpacingLabel = new JLabel("");
        rsctimesButtonsSpacingLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
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
                addCheckbox(
                        "Search the RSC Wiki with a button in the bottom blue bar (replaces Report Abuse button at low width)",
                        overlayPanel);
        overlayPanelWikiLookupOnHbarCheckbox.setToolTipText(
                "Click the button on the bottom bar, then click on anything else, and it will look it up on the RSC wiki.");

        overlayPanelRemoveReportAbuseButtonHbarCheckbox =
                addCheckbox("Remove Report Abuse Button (Similar to prior to 2002-09-11)", overlayPanel);
        overlayPanelRemoveReportAbuseButtonHbarCheckbox.setToolTipText(
                "mudclient149 added the Report Abuse button. You will still be able to report players with right click menu if this option is enabled.");

        overlayPanelPositionCheckbox = addCheckbox("Display position", overlayPanel);
        overlayPanelPositionCheckbox.setToolTipText("Shows the player's global position");

        overlayPanelRetroFpsCheckbox = addCheckbox("Display FPS like early RSC", overlayPanel);
        overlayPanelRetroFpsCheckbox.setToolTipText(
                "Shows the FPS like it used to be displayed in RSC");

        overlayPanelShowCombatInfoCheckbox = addCheckbox("Show NPC HP info", overlayPanel);
        overlayPanelShowCombatInfoCheckbox.setToolTipText(
                "Shows the HP info for the NPC you're in combat with");

        overlayPanelUsePercentageCheckbox = addCheckbox("Use percentage for NPC HP info", overlayPanel);
        overlayPanelUsePercentageCheckbox.setToolTipText(
                "Uses percentage for NPC HP info instead of actual HP");

        overlayPanelLagIndicatorCheckbox = addCheckbox("Lag indicator", overlayPanel);
        overlayPanelLagIndicatorCheckbox.setToolTipText(
                "When there's a problem with your connection, rsctimes will tell you in the bottom right");

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

        /// XP Bar
        addSettingsHeader(overlayPanel, "XP Bar");
        overlayPanelXPBarCheckbox = addCheckbox("Show an XP bar", overlayPanel);
        overlayPanelXPBarCheckbox.setToolTipText("Show an XP bar to the left of the wrench");
        overlayPanelXPBarCheckbox.setBorder(new EmptyBorder(0, 0, 10, 0));

        overlayPanelXPDropsCheckbox = addCheckbox("Show XP drops", overlayPanel);
        overlayPanelXPDropsCheckbox.setToolTipText(
                "Show the XP gained as an overlay each time XP is received");

        ButtonGroup XPAlignButtonGroup = new ButtonGroup();
        overlayPanelXPRightAlignFocusButton = addRadioButton("Display on the right", overlayPanel, 20);
        overlayPanelXPRightAlignFocusButton.setToolTipText(
                "The XP bar and XP drops will be shown just left of the Settings menu.");
        overlayPanelXPCenterAlignFocusButton =
                addRadioButton("Display in the center", overlayPanel, 20);
        overlayPanelXPCenterAlignFocusButton.setToolTipText(
                "The XP bar and XP drops will be shown at the top-middle of the screen.");
        XPAlignButtonGroup.add(overlayPanelXPRightAlignFocusButton);
        XPAlignButtonGroup.add(overlayPanelXPCenterAlignFocusButton);

        /// "In World" Overlays move with the camera, and modify objects that the are rendered in the
        // world
        addSettingsHeader(overlayPanel, "\"In World\" Overlays");
        overlayPanelHitboxCheckbox =
                addCheckbox("Show hitboxes around NPCs, players, and items", overlayPanel);
        overlayPanelHitboxCheckbox.setToolTipText(
                "Shows the clickable areas on NPCs, players, and items");
        overlayPanelHitboxCheckbox.setBorder(new EmptyBorder(0, 0, 10, 0));

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

        // Blocked Items
        JPanel blockedItemsPanel = new JPanel();
        overlayPanel.add(blockedItemsPanel);
        blockedItemsPanel.setLayout(new BoxLayout(blockedItemsPanel, BoxLayout.X_AXIS));
        blockedItemsPanel.setPreferredSize(new Dimension(0, 37));
        blockedItemsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        blockedItemsPanel.setBorder(new EmptyBorder(0, 0, 9, 0));

        JLabel blockedItemsPanelNameLabel = new JLabel("Blocked items: ");
        blockedItemsPanel.add(blockedItemsPanelNameLabel);
        blockedItemsPanelNameLabel.setAlignmentY((float) 0.9);

        blockedItemsTextField = new JTextField();
        blockedItemsPanel.add(blockedItemsTextField);
        blockedItemsTextField.setMinimumSize(new Dimension(100, 28));
        blockedItemsTextField.setMaximumSize(new Dimension(Short.MAX_VALUE, 28));
        blockedItemsTextField.setAlignmentY((float) 0.75);

        // Highlighted Items
        JPanel highlightedItemsPanel = new JPanel();
        overlayPanel.add(highlightedItemsPanel);
        highlightedItemsPanel.setLayout(new BoxLayout(highlightedItemsPanel, BoxLayout.X_AXIS));
        highlightedItemsPanel.setPreferredSize(new Dimension(0, 37));
        highlightedItemsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        highlightedItemsPanel.setBorder(new EmptyBorder(0, 0, 9, 0));

        JLabel highlightedItemsPanelNameLabel = new JLabel("Highlighted items: ");
        highlightedItemsPanel.add(highlightedItemsPanelNameLabel);
        highlightedItemsPanelNameLabel.setAlignmentY((float) 0.9);

        highlightedItemsTextField = new JTextField();
        highlightedItemsPanel.add(highlightedItemsTextField);
        highlightedItemsTextField.setMinimumSize(new Dimension(100, 28));
        highlightedItemsTextField.setMaximumSize(new Dimension(Short.MAX_VALUE, 28));
        highlightedItemsTextField.setAlignmentY((float) 0.75);

        /*
         * Notifications tab
         */

        notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));

        addSettingsHeader(notificationPanel, "Notification Settings");

        notificationPanelTrayPopupCheckbox =
                addCheckbox("Enable notification tray popups", notificationPanel);
        notificationPanelTrayPopupCheckbox.setBorder(BorderFactory.createEmptyBorder(0, 0, 7, 0));
        notificationPanelTrayPopupCheckbox.setToolTipText(
                "Shows a system notification when a notification is triggered");

        ButtonGroup trayPopupButtonGroup = new ButtonGroup();
        notificationPanelTrayPopupClientFocusButton =
                addRadioButton("Only when client is not focused", notificationPanel, 20);
        notificationPanelTrayPopupAnyFocusButton =
                addRadioButton("Regardless of client focus", notificationPanel, 20);
        trayPopupButtonGroup.add(notificationPanelTrayPopupClientFocusButton);
        trayPopupButtonGroup.add(notificationPanelTrayPopupAnyFocusButton);

        notificationPanelNotifSoundsCheckbox =
                addCheckbox("Enable notification sounds", notificationPanel);
        notificationPanelNotifSoundsCheckbox.setBorder(BorderFactory.createEmptyBorder(0, 0, 7, 0));
        notificationPanelNotifSoundsCheckbox.setToolTipText(
                "Plays a sound when a notification is triggered");

        ButtonGroup notifSoundButtonGroup = new ButtonGroup();
        notificationPanelNotifSoundClientFocusButton =
                addRadioButton("Only when client is not focused", notificationPanel, 20);
        notificationPanelNotifSoundAnyFocusButton =
                addRadioButton("Regardless of client focus", notificationPanel, 20);
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
        notificationPanelPMNotifsCheckbox.setBorder(BorderFactory.createEmptyBorder(0, 0, 7, 0));
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

        notificationPanelLogoutNotifsCheckbox =
                addCheckbox("Enable logout notification", notificationPanel);
        notificationPanelLogoutNotifsCheckbox.setToolTipText(
                "Shows a system notification when about to idle out");

        JPanel notificationPanelLowHPNotifsPanel = new JPanel();
        notificationPanel.add(notificationPanelLowHPNotifsPanel);
        notificationPanelLowHPNotifsPanel.setLayout(
                new BoxLayout(notificationPanelLowHPNotifsPanel, BoxLayout.X_AXIS));
        notificationPanelLowHPNotifsPanel.setPreferredSize(new Dimension(0, 37));
        notificationPanelLowHPNotifsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        notificationPanelLowHPNotifsCheckbox =
                addCheckbox("Enable low HP notification at", notificationPanelLowHPNotifsPanel);
        notificationPanelLowHPNotifsCheckbox.setToolTipText(
                "Shows a system notification when your HP drops below the specified value");

        notificationPanelLowHPNotifsSpinner = new JSpinner();
        notificationPanelLowHPNotifsSpinner.setMaximumSize(new Dimension(45, 22));
        notificationPanelLowHPNotifsSpinner.setMinimumSize(new Dimension(45, 22));
        notificationPanelLowHPNotifsSpinner.setAlignmentY((float) 0.75);
        notificationPanelLowHPNotifsPanel.add(notificationPanelLowHPNotifsSpinner);
        notificationPanelLowHPNotifsSpinner.putClientProperty("JComponent.sizeVariant", "mini");

        JLabel notificationPanelLowHPNotifsEndLabel = new JLabel("% HP");
        notificationPanelLowHPNotifsPanel.add(notificationPanelLowHPNotifsEndLabel);
        notificationPanelLowHPNotifsEndLabel.setAlignmentY((float) 0.9);
        notificationPanelLowHPNotifsEndLabel.setBorder(new EmptyBorder(0, 2, 0, 0));

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
        warnHighlightedOnGroundPanel.setPreferredSize(new Dimension(0, 37));
        warnHighlightedOnGroundPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        notificationPanelHighlightedItemTimerCheckbox =
                addCheckbox(
                        "Warn if one of your highlighted items has been on the ground for more than",
                        warnHighlightedOnGroundPanel);
        notificationPanelHighlightedItemTimerCheckbox.setToolTipText(
                "Highlighted items can be configured in the Overlays tab");

        JLabel highlightedItemsSuggestionJLabel =
                new JLabel(
                        "<html><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
                                + "<strong>Note:</strong> Loot from kills despawns after about 2 minutes."
                                + "</p></html>");
        notificationPanel.add(highlightedItemsSuggestionJLabel);
        highlightedItemsSuggestionJLabel.setBorder(new EmptyBorder(0, 0, 8, 0));

        notificationPanelHighlightedItemTimerSpinner = new JSpinner();
        notificationPanelHighlightedItemTimerSpinner.setMaximumSize(new Dimension(55, 22));
        notificationPanelHighlightedItemTimerSpinner.setMinimumSize(new Dimension(55, 22));
        notificationPanelHighlightedItemTimerSpinner.setAlignmentY((float) 0.75);
        warnHighlightedOnGroundPanel.add(notificationPanelHighlightedItemTimerSpinner);
        notificationPanelHighlightedItemTimerSpinner.putClientProperty(
                "JComponent.sizeVariant", "mini");

        JLabel notificationPanelHighlightedItemEndLabel = new JLabel("seconds");
        warnHighlightedOnGroundPanel.add(notificationPanelHighlightedItemEndLabel);
        notificationPanelHighlightedItemEndLabel.setAlignmentY((float) 0.9);
        notificationPanelHighlightedItemEndLabel.setBorder(new EmptyBorder(0, 2, 0, 0));

        // Sanitize JSpinner values
        SpinnerNumberModel highlightedItemSecondsModel = new SpinnerNumberModel();
        highlightedItemSecondsModel.setMinimum(0);
        highlightedItemSecondsModel.setMaximum(630); // 10.5 minutes max
        highlightedItemSecondsModel.setValue(100);
        notificationPanelHighlightedItemTimerSpinner.setModel(highlightedItemSecondsModel);

        /*
         * Streaming & Privacy tab
         */

        streamingPanel.setLayout(new BoxLayout(streamingPanel, BoxLayout.Y_AXIS));

        addSettingsHeader(streamingPanel, "Streaming & Privacy");

        streamingPanelTwitchChatIntegrationEnabledCheckbox =
                addCheckbox("Enable Twitch chat integration", streamingPanel);
        streamingPanelTwitchChatIntegrationEnabledCheckbox.setToolTipText(
                "If this box is checked, and the 3 relevant text fields are filled out, you will connect to a chat channel on login.");
        streamingPanelTwitchChatIntegrationEnabledCheckbox.setBorder(new EmptyBorder(0, 0, 7, 0));

        streamingPanelTwitchChatCheckbox = addCheckbox("Hide incoming Twitch chat", streamingPanel);
        streamingPanelTwitchChatCheckbox.setToolTipText(
                "Don't show chat from other Twitch users, but still be able to send Twitch chat");

        JPanel streamingPanelTwitchChannelNamePanel = new JPanel();
        streamingPanel.add(streamingPanelTwitchChannelNamePanel);
        streamingPanelTwitchChannelNamePanel.setLayout(
                new BoxLayout(streamingPanelTwitchChannelNamePanel, BoxLayout.X_AXIS));
        streamingPanelTwitchChannelNamePanel.setPreferredSize(new Dimension(0, 37));
        streamingPanelTwitchChannelNamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        streamingPanelTwitchChannelNamePanel.setBorder(new EmptyBorder(0, 0, 7, 0));

        JLabel streamingPanelTwitchChannelNameLabel = new JLabel("Twitch channel to chat in: ");
        streamingPanelTwitchChannelNameLabel.setToolTipText("The Twitch channel you want to chat in");
        streamingPanelTwitchChannelNamePanel.add(streamingPanelTwitchChannelNameLabel);
        streamingPanelTwitchChannelNameLabel.setAlignmentY((float) 0.9);

        streamingPanelTwitchChannelNameTextField = new JTextField();
        streamingPanelTwitchChannelNamePanel.add(streamingPanelTwitchChannelNameTextField);
        streamingPanelTwitchChannelNameTextField.setMinimumSize(new Dimension(100, 28));
        streamingPanelTwitchChannelNameTextField.setMaximumSize(new Dimension(Short.MAX_VALUE, 28));
        streamingPanelTwitchChannelNameTextField.setAlignmentY((float) 0.75);

        JPanel streamingPanelTwitchUserPanel = new JPanel();
        streamingPanel.add(streamingPanelTwitchUserPanel);
        streamingPanelTwitchUserPanel.setLayout(
                new BoxLayout(streamingPanelTwitchUserPanel, BoxLayout.X_AXIS));
        streamingPanelTwitchUserPanel.setPreferredSize(new Dimension(0, 37));
        streamingPanelTwitchUserPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        streamingPanelTwitchUserPanel.setBorder(new EmptyBorder(0, 0, 7, 0));

        JLabel streamingPanelTwitchUserLabel = new JLabel("Your Twitch username: ");
        streamingPanelTwitchUserLabel.setToolTipText("The Twitch username you log into Twitch with");
        streamingPanelTwitchUserPanel.add(streamingPanelTwitchUserLabel);
        streamingPanelTwitchUserLabel.setAlignmentY((float) 0.9);

        streamingPanelTwitchUserTextField = new JTextField();
        streamingPanelTwitchUserPanel.add(streamingPanelTwitchUserTextField);
        streamingPanelTwitchUserTextField.setMinimumSize(new Dimension(100, 28));
        streamingPanelTwitchUserTextField.setMaximumSize(new Dimension(Short.MAX_VALUE, 28));
        streamingPanelTwitchUserTextField.setAlignmentY((float) 0.75);

        JPanel streamingPanelTwitchOAuthPanel = new JPanel();
        streamingPanel.add(streamingPanelTwitchOAuthPanel);
        streamingPanelTwitchOAuthPanel.setLayout(
                new BoxLayout(streamingPanelTwitchOAuthPanel, BoxLayout.X_AXIS));
        streamingPanelTwitchOAuthPanel.setPreferredSize(new Dimension(0, 37));
        streamingPanelTwitchOAuthPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        streamingPanelTwitchOAuthPanel.setBorder(new EmptyBorder(0, 0, 7, 0));

        JLabel streamingPanelTwitchOAuthLabel =
                new JLabel("Your Twitch OAuth token (not your Stream key): ");
        streamingPanelTwitchOAuthLabel.setToolTipText("Your Twitch OAuth token (not your Stream Key)");
        streamingPanelTwitchOAuthPanel.add(streamingPanelTwitchOAuthLabel);
        streamingPanelTwitchOAuthLabel.setAlignmentY((float) 0.9);

        streamingPanelTwitchOAuthTextField = new JPasswordField();
        streamingPanelTwitchOAuthPanel.add(streamingPanelTwitchOAuthTextField);
        streamingPanelTwitchOAuthTextField.setMinimumSize(new Dimension(100, 28));
        streamingPanelTwitchOAuthTextField.setMaximumSize(new Dimension(Short.MAX_VALUE, 28));
        streamingPanelTwitchOAuthTextField.setAlignmentY((float) 0.75);

        streamingPanelIPAtLoginCheckbox =
                addCheckbox("Show IP details at login welcome screen", streamingPanel);
        streamingPanelIPAtLoginCheckbox.setToolTipText(
                "Shows the last IP you last logged in from when you log in (Disable this if you're streaming)");

        streamingPanelSaveLoginCheckbox =
                addCheckbox("Save login information between logins (Requires restart)", streamingPanel);
        streamingPanelSaveLoginCheckbox.setToolTipText(
                "Preserves login details between logins (Disable this if you're streaming)");

        streamingPanelStartLoginCheckbox = addCheckbox("Start game at login screen", streamingPanel);
        streamingPanelStartLoginCheckbox.setToolTipText(
                "Starts the game at the login screen and return to it on logout");

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
        gbl_constraints.gridy = 0;
        gbl_constraints.gridwidth = 3;

        // Note: CTRL + every single letter on the keyboard is now used
        // consider using ALT instead.

        addKeybindCategory(keybindContainerPanel, "General");
        /*
        addKeybindSet(keybindContainerPanel, "Logout", "logout", KeyModifier.CTRL, KeyEvent.VK_L);
        addKeybindSet(
                keybindContainerPanel, "Take screenshot", "screenshot", KeyModifier.CTRL, KeyEvent.VK_S);
         */
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
        /*
        addKeybindSet(
                keybindContainerPanel,
                "Show queue window",
                "show_queue_window",
                KeyModifier.CTRL,
                KeyEvent.VK_Q);
        addKeybindSet(
                keybindContainerPanel,
                "Toggle combat XP menu persistence",
                "toggle_combat_xp_menu",
                KeyModifier.CTRL,
                KeyEvent.VK_C);
        addKeybindSet(
                keybindContainerPanel,
                "Toggle XP drops",
                "toggle_xp_drops",
                KeyModifier.CTRL,
                KeyEvent.VK_OPEN_BRACKET);
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

        /* TODO: overlays...
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
                keybindContainerPanel, "Toggle XP bar", "toggle_xp_bar", KeyModifier.CTRL, KeyEvent.VK_K);
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
                "Toggle retro fps overlay",
                "toggle_retro_fps_overlay",
                KeyModifier.ALT,
                KeyEvent.VK_F);
        addKeybindSet(
                keybindContainerPanel,
                "Toggle item name overlay",
                "toggle_item_overlay",
                KeyModifier.CTRL,
                KeyEvent.VK_I);
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
        addKeybindSet(
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
                KeyEvent.VK_X);
        addKeybindSet(
                keybindContainerPanel,
                "Toggle debug mode",
                "toggle_debug",
                KeyModifier.CTRL,
                KeyEvent.VK_D);
        addKeybindSet(
                keybindContainerPanel,
                "Toggle Wiki Hbar Button",
                "toggle_wiki_hbar_button",
                KeyModifier.ALT,
                KeyEvent.VK_W);
        */

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
                KeyEvent.VK_UP);

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
        presetsPanelPresetSliderPanel.setMaximumSize(new Dimension(400, 175));
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
        presetsPanelPresetSlider.setPreferredSize(new Dimension(100, 0));
        presetsPanelPresetSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        presetsPanelPresetSlider.setBorder(new EmptyBorder(0, 0, 5, 70));
        presetsPanelPresetSlider.setOrientation(SwingConstants.VERTICAL);
        presetsPanelPresetSliderPanel.add(presetsPanelPresetSlider);

        JPanel presetsButtonPanel = new JPanel();
        presetsButtonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        presetsButtonPanel.setMaximumSize(new Dimension(300, 50));
        presetsButtonPanel.setBorder(BorderFactory.createEmptyBorder(7, 10, 10, 0));
        presetsButtonPanel.setLayout(new BoxLayout(presetsButtonPanel, BoxLayout.X_AXIS));

        replaceConfigButton =
                addButton("Replace Config with Preset", presetsButtonPanel, Component.LEFT_ALIGNMENT);
        replaceConfigButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int choice =
                                JOptionPane.showConfirmDialog(
                                        Launcher.getConfigWindow().frame,
                                        "Warning: this will delete your old settings! Are you sure you want to delete your old settings?",
                                        "Confirm",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE);
                        if (choice == JOptionPane.CLOSED_OPTION || choice == JOptionPane.NO_OPTION) {
                            return;
                        }

                        Settings.save(Settings.currentProfile);
                    }
                });
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

        // World List Tab
        worldListPanel.setLayout(new BoxLayout(worldListPanel, BoxLayout.Y_AXIS));
        worldListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        worldListPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        addSettingsHeader(worldListPanel, "World List");

        JLabel spacingLabel = new JLabel("");
        spacingLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        worldListPanel.add(spacingLabel);

        for (int i = 1; i <= Settings.WORLDS_TO_DISPLAY; i++) {
            addWorldFields(i);
        }
        addAddWorldButton();

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
                    new JLabel(new ImageIcon(rsctimesLogo.getScaledInstance(250, 250, Image.SCALE_DEFAULT)));
            rsctimesLogoJLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 40));
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
                                "<html><div style=\"font-size:45px; padding-bottom:10px;\"<b>RSC</b>Times</div><div style=\"font-size:20px;\">v%8.6f </div></html>",
                                Settings.VERSION_NUMBER));

        rightPane.add(RSCTimesText);

        cR.gridy = 1;

        JLabel aboutText =
                new JLabel(
                        "<html><head><style>p{font-size:10px; padding-top:15px;}ul{padding-left:0px;margin-left:10px;}</style></head><p><b>RSC</b>Times is a RuneLite-like client " +
                                "based on mudclient38-recreated.<br/> Learn more at https://rsc.plus<br/><br/>" +
                                "Thanks to the authors who made this software possible:<br/>" +
                                "<ul><li><b>Luis</b>, for creating the client and finding lots of hooks</li>" +
                                "<li><b>Logg</b>, helped port features from RSC+, new interfaces & improvements</li>" +
                                "<li><b>Ornox</b>, for creating RSC+ & most of its features</li>" +
                                "<li><b>The RSC+ team of 2016 to 2018</li></b>" +
                                "<li><b>The Jagex team of 2000 to 2001</b></li></ul></p></html>");

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
        gbc.insets = new Insets(0, 0, 5, 0);
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
        gbc.insets = new Insets(0, 0, 5, 0);
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

        panel.add(Box.createVerticalStrut(7), gbc);
        JSeparator jsep = new JSeparator(SwingConstants.HORIZONTAL);
        panel.add(jsep, gbc);
        panel.add(Box.createVerticalStrut(7), gbc);
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
        else gbc.insets = new Insets(7, 0, 0, 0);
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
        jsep.setMaximumSize(new Dimension(Short.MAX_VALUE, 7));
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
        checkbox.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 5));
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
        radioButton.setBorder(BorderFactory.createEmptyBorder(0, leftIndent, 7, 5));
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
        /*
        generalPanelClientSizeCheckbox.setSelected(
                Settings.CUSTOM_CLIENT_SIZE.get(Settings.currentProfile));
        generalPanelClientSizeXSpinner.setValue(
                Settings.CUSTOM_CLIENT_SIZE_X.get(Settings.currentProfile));
        generalPanelClientSizeYSpinner.setValue(
                Settings.CUSTOM_CLIENT_SIZE_Y.get(Settings.currentProfile));
        */
        generalPanelCheckUpdates.setSelected(Settings.CHECK_UPDATES.get(Settings.currentProfile));
        /*
        generalPanelShowSecurityTipsAtLoginCheckbox.setSelected(
                Settings.SHOW_SECURITY_TIP_DAY.get(Settings.currentProfile));
        generalPanelWelcomeEnabled.setSelected(
                Settings.REMIND_HOW_TO_OPEN_SETTINGS.get(Settings.currentProfile));
        generalPanelCombatXPMenuCheckbox.setSelected(
                Settings.COMBAT_MENU_SHOWN.get(Settings.currentProfile));
        generalPanelCombatXPMenuHiddenCheckbox.setSelected(
                Settings.COMBAT_MENU_HIDDEN.get(Settings.currentProfile));
        generalPanelInventoryFullAlertCheckbox.setSelected(
                Settings.INVENTORY_FULL_ALERT.get(Settings.currentProfile));
        generalPanelNamePatchModeSlider.setValue(Settings.NAME_PATCH_TYPE.get(Settings.currentProfile));
        */
        generalPanelLogVerbositySlider.setValue(Settings.LOG_VERBOSITY.get(Settings.currentProfile));
        /*
        generalPanelCommandPatchQuestCheckbox.setSelected(
                Settings.COMMAND_PATCH_QUEST.get(Settings.currentProfile));
        generalPanelKeepScrollbarPosMagicPrayerCheckbox.setSelected(
                Settings.KEEP_SCROLLBAR_POS_MAGIC_PRAYER.get(Settings.currentProfile));
        generalPanelRoofHidingCheckbox.setSelected(Settings.HIDE_ROOFS.get(Settings.currentProfile));
        generalPanelCameraZoomableCheckbox.setSelected(
                Settings.CAMERA_ZOOMABLE.get(Settings.currentProfile));
        generalPanelCameraRotatableCheckbox.setSelected(
                Settings.CAMERA_ROTATABLE.get(Settings.currentProfile));
        generalPanelCameraMovableCheckbox.setSelected(
                Settings.CAMERA_MOVABLE.get(Settings.currentProfile));
        generalPanelCameraMovableRelativeCheckbox.setSelected(
                Settings.CAMERA_MOVABLE_RELATIVE.get(Settings.currentProfile));
        */
        generalPanelColoredTextCheckbox.setSelected(
                Settings.COLORIZE_CONSOLE_TEXT.get(Settings.currentProfile));
        generalPanelLogLevelCheckbox.setSelected(Settings.LOG_SHOW_LEVEL.get(Settings.currentProfile));
        generalPanelLogTimestampsCheckbox.setSelected(
                Settings.LOG_SHOW_TIMESTAMPS.get(Settings.currentProfile));
        generalPanelLogForceLevelCheckbox.setSelected(
                Settings.LOG_FORCE_LEVEL.get(Settings.currentProfile));
        generalPanelLogForceTimestampsCheckbox.setSelected(
                Settings.LOG_FORCE_TIMESTAMPS.get(Settings.currentProfile));
        /*
        generalPanelFoVSlider.setValue(Settings.FOV.get(Settings.currentProfile));
        generalPanelLimitFPSCheckbox.setSelected(
                Settings.FPS_LIMIT_ENABLED.get(Settings.currentProfile));
        generalPanelLimitFPSSpinner.setValue(Settings.FPS_LIMIT.get(Settings.currentProfile));
        generalPanelAutoScreenshotCheckbox.setSelected(
                Settings.AUTO_SCREENSHOT.get(Settings.currentProfile));
        generalPanelCustomCursorCheckbox.setSelected(
                Settings.SOFTWARE_CURSOR.get(Settings.currentProfile));
        generalPanelViewDistanceSlider.setValue(Settings.VIEW_DISTANCE.get(Settings.currentProfile));
        generalPanelPatchGenderCheckbox.setSelected(Settings.PATCH_GENDER.get(Settings.currentProfile));
        generalPanelPatchHbar512LastPixelCheckbox.setSelected(
                Settings.PATCH_HBAR_512_LAST_PIXEL.get(Settings.currentProfile));
        generalPanelPatchWrenchMenuSpacingCheckbox.setSelected(
                Settings.PATCH_WRENCH_MENU_SPACING.get(Settings.currentProfile));
        */
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
        /*
        overlayPanelBuffsCheckbox.setSelected(Settings.SHOW_BUFFS.get(Settings.currentProfile));
        overlayPanelLastMenuActionCheckbox.setSelected(
                Settings.SHOW_LAST_MENU_ACTION.get(Settings.currentProfile));
        overlayPanelMouseTooltipCheckbox.setSelected(
                Settings.SHOW_MOUSE_TOOLTIP.get(Settings.currentProfile));
        overlayPanelExtendedTooltipCheckbox.setSelected(
                Settings.SHOW_EXTENDED_TOOLTIP.get(Settings.currentProfile));
        overlayPanelInvCountCheckbox.setSelected(Settings.SHOW_INVCOUNT.get(Settings.currentProfile));
        overlayPanelRscPlusButtonsCheckbox.setSelected(
                Settings.SHOW_RSCPLUS_BUTTONS.get(Settings.currentProfile));
        overlayPanelRscPlusButtonsFunctionalCheckbox.setSelected(
                Settings.RSCPLUS_BUTTONS_FUNCTIONAL.get(Settings.currentProfile)
                        || Settings.SHOW_RSCPLUS_BUTTONS.get(Settings.currentProfile));
        overlayPanelWikiLookupOnMagicBookCheckbox.setSelected(
                Settings.WIKI_LOOKUP_ON_MAGIC_BOOK.get(Settings.currentProfile));
        overlayPanelWikiLookupOnHbarCheckbox.setSelected(
                Settings.WIKI_LOOKUP_ON_HBAR.get(Settings.currentProfile));
        overlayPanelRemoveReportAbuseButtonHbarCheckbox.setSelected(
                Settings.REMOVE_REPORT_ABUSE_BUTTON_HBAR.get(Settings.currentProfile));
        overlayPanelPositionCheckbox.setSelected(
                Settings.SHOW_PLAYER_POSITION.get(Settings.currentProfile));
        overlayPanelRetroFpsCheckbox.setSelected(Settings.SHOW_RETRO_FPS.get(Settings.currentProfile));
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
        overlayPanelXPDropsCheckbox.setSelected(Settings.SHOW_XPDROPS.get(Settings.currentProfile));
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
        */

        // Notifications tab
        notificationPanelPMNotifsCheckbox.setSelected(
                Settings.PM_NOTIFICATIONS.get(Settings.currentProfile));
        notificationPanelTradeNotifsCheckbox.setSelected(
                Settings.TRADE_NOTIFICATIONS.get(Settings.currentProfile));
        notificationPanelUnderAttackNotifsCheckbox.setSelected(
                Settings.UNDER_ATTACK_NOTIFICATIONS.get(Settings.currentProfile));
        notificationPanelLogoutNotifsCheckbox.setSelected(
                Settings.LOGOUT_NOTIFICATIONS.get(Settings.currentProfile));
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
        /*
        streamingPanelTwitchChatIntegrationEnabledCheckbox.setSelected(
                Settings.TWITCH_CHAT_ENABLED.get(Settings.currentProfile));
        streamingPanelTwitchChatCheckbox.setSelected(
                Settings.TWITCH_HIDE_CHAT.get(Settings.currentProfile));
        streamingPanelTwitchChannelNameTextField.setText(
                Settings.TWITCH_CHANNEL.get(Settings.currentProfile));
        streamingPanelTwitchOAuthTextField.setText(Settings.TWITCH_OAUTH.get(Settings.currentProfile));
        streamingPanelTwitchUserTextField.setText(
                Settings.TWITCH_USERNAME.get(Settings.currentProfile));
        streamingPanelIPAtLoginCheckbox.setSelected(
                Settings.SHOW_LOGIN_IP_ADDRESS.get(Settings.currentProfile));
        streamingPanelSaveLoginCheckbox.setSelected(
                Settings.SAVE_LOGININFO.get(Settings.currentProfile));
        streamingPanelStartLoginCheckbox.setSelected(
                Settings.START_LOGINSCREEN.get(Settings.currentProfile));
        streamingPanelSpeedrunnerCheckbox.setSelected(
                Settings.SPEEDRUNNER_MODE_ACTIVE.get(Settings.currentProfile));
         */
        // streamingPanelSpeedrunnerUsernameTextField.setText(Settings.SPEEDRUNNER_USERNAME.get(Settings.currentProfile));


        // World List tab
        synchronizeWorldTab();

        for (KeybindSet kbs : KeyboardHandler.keybindSetList) {
            setKeybindButtonText(kbs);
        }
    }

    /** Saves the settings from the GUI values to the settings class variables */
    public void saveSettings() {
        // General options
        /*
        Settings.CUSTOM_CLIENT_SIZE.put(
                Settings.currentProfile, generalPanelClientSizeCheckbox.isSelected());
        Settings.CUSTOM_CLIENT_SIZE_X.put(
                Settings.currentProfile,
                ((SpinnerNumberModel) (generalPanelClientSizeXSpinner.getModel())).getNumber().intValue());
        Settings.CUSTOM_CLIENT_SIZE_Y.put(
                Settings.currentProfile,
                ((SpinnerNumberModel) (generalPanelClientSizeYSpinner.getModel())).getNumber().intValue());
         */
        Settings.CHECK_UPDATES.put(Settings.currentProfile, generalPanelCheckUpdates.isSelected());
        /*
        Settings.SHOW_SECURITY_TIP_DAY.put(
                Settings.currentProfile, generalPanelShowSecurityTipsAtLoginCheckbox.isSelected());
        Settings.REMIND_HOW_TO_OPEN_SETTINGS.put(
                Settings.currentProfile, generalPanelWelcomeEnabled.isSelected());
        Settings.COMBAT_MENU_SHOWN.put(
                Settings.currentProfile, generalPanelCombatXPMenuCheckbox.isSelected());
        Settings.COMBAT_MENU_HIDDEN.put(
                Settings.currentProfile, generalPanelCombatXPMenuHiddenCheckbox.isSelected());
        Settings.SHOW_XPDROPS.put(Settings.currentProfile, overlayPanelXPDropsCheckbox.isSelected());
        Settings.CENTER_XPDROPS.put(
                Settings.currentProfile, overlayPanelXPCenterAlignFocusButton.isSelected());
        Settings.INVENTORY_FULL_ALERT.put(
                Settings.currentProfile, generalPanelInventoryFullAlertCheckbox.isSelected());
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
        Settings.KEEP_SCROLLBAR_POS_MAGIC_PRAYER.put(
                Settings.currentProfile, generalPanelKeepScrollbarPosMagicPrayerCheckbox.isSelected());
        Settings.HIDE_ROOFS.put(Settings.currentProfile, generalPanelRoofHidingCheckbox.isSelected());
        Settings.CAMERA_ZOOMABLE.put(
                Settings.currentProfile, generalPanelCameraZoomableCheckbox.isSelected());
        Settings.CAMERA_ROTATABLE.put(
                Settings.currentProfile, generalPanelCameraRotatableCheckbox.isSelected());
        Settings.CAMERA_MOVABLE.put(
                Settings.currentProfile, generalPanelCameraMovableCheckbox.isSelected());
        Settings.CAMERA_MOVABLE_RELATIVE.put(
                Settings.currentProfile, generalPanelCameraMovableRelativeCheckbox.isSelected());
        */
        Settings.COLORIZE_CONSOLE_TEXT.put(
                Settings.currentProfile, generalPanelColoredTextCheckbox.isSelected());
        /*
        Settings.FOV.put(Settings.currentProfile, generalPanelFoVSlider.getValue());
        Settings.SOFTWARE_CURSOR.put(
                Settings.currentProfile, generalPanelCustomCursorCheckbox.isSelected());
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
        Settings.PATCH_WRENCH_MENU_SPACING.put(
                Settings.currentProfile, generalPanelPatchWrenchMenuSpacingCheckbox.isSelected());
         */

        // Overlays options
        /*
        Settings.SHOW_BUFFS.put(Settings.currentProfile, overlayPanelBuffsCheckbox.isSelected());
        Settings.SHOW_LAST_MENU_ACTION.put(
                Settings.currentProfile, overlayPanelLastMenuActionCheckbox.isSelected());
        Settings.SHOW_MOUSE_TOOLTIP.put(
                Settings.currentProfile, overlayPanelMouseTooltipCheckbox.isSelected());
        Settings.SHOW_EXTENDED_TOOLTIP.put(
                Settings.currentProfile, overlayPanelExtendedTooltipCheckbox.isSelected());
        Settings.SHOW_INVCOUNT.put(Settings.currentProfile, overlayPanelInvCountCheckbox.isSelected());
        Settings.SHOW_RSCPLUS_BUTTONS.put(
                Settings.currentProfile, overlayPanelRscPlusButtonsCheckbox.isSelected());
        Settings.RSCPLUS_BUTTONS_FUNCTIONAL.put(
                Settings.currentProfile,
                overlayPanelRscPlusButtonsFunctionalCheckbox.isSelected()
                        || overlayPanelRscPlusButtonsCheckbox.isSelected());
        Settings.WIKI_LOOKUP_ON_MAGIC_BOOK.put(
                Settings.currentProfile, overlayPanelWikiLookupOnMagicBookCheckbox.isSelected());
        Settings.WIKI_LOOKUP_ON_HBAR.put(
                Settings.currentProfile, overlayPanelWikiLookupOnHbarCheckbox.isSelected());
        Settings.REMOVE_REPORT_ABUSE_BUTTON_HBAR.put(
                Settings.currentProfile, overlayPanelRemoveReportAbuseButtonHbarCheckbox.isSelected());
        Settings.SHOW_PLAYER_POSITION.put(
                Settings.currentProfile, overlayPanelPositionCheckbox.isSelected());
        Settings.SHOW_RETRO_FPS.put(Settings.currentProfile, overlayPanelRetroFpsCheckbox.isSelected());
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
        */


        // Notifications options
        Settings.PM_NOTIFICATIONS.put(
                Settings.currentProfile, notificationPanelPMNotifsCheckbox.isSelected());
        Settings.TRADE_NOTIFICATIONS.put(
                Settings.currentProfile, notificationPanelTradeNotifsCheckbox.isSelected());
        Settings.UNDER_ATTACK_NOTIFICATIONS.put(
                Settings.currentProfile, notificationPanelUnderAttackNotifsCheckbox.isSelected());
        Settings.LOGOUT_NOTIFICATIONS.put(
                Settings.currentProfile, notificationPanelLogoutNotifsCheckbox.isSelected());
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
        /*
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
        Settings.SHOW_LOGIN_IP_ADDRESS.put(
                Settings.currentProfile, streamingPanelIPAtLoginCheckbox.isSelected());
        Settings.SAVE_LOGININFO.put(
                Settings.currentProfile, streamingPanelSaveLoginCheckbox.isSelected());
        Settings.START_LOGINSCREEN.put(
                Settings.currentProfile, streamingPanelStartLoginCheckbox.isSelected());
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
        /*
        for (int i = 1; i <= Settings.WORLDS_TO_DISPLAY; i++) {
            Settings.WORLD_NAMES.put(
                    i, getTextWithDefault(worldNamesJTextFields, i, String.format("World %d", i)));
            Settings.WORLD_URLS.put(i, worldUrlsJTextFields.get(i).getText());

            Settings.WORLD_SERVER_TYPES.put(i, (Integer) worldTypesJComboBoxes.get(i).getSelectedIndex());

            String portString = worldPortsJTextFields.get(i).getText();
            if (portString.equals("")) {
                Settings.WORLD_PORTS.put(i, Replay.DEFAULT_PORT);
            } else {
                Settings.WORLD_PORTS.put(i, Integer.parseInt(portString));
            }
        }

        if (Client.state == Client.STATE_LOGIN)
            Game.getInstance().getJConfig().changeWorld(Settings.WORLD.get(Settings.currentProfile));
        */

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
        if (Settings.CUSTOM_CLIENT_SIZE.get(Settings.currentProfile))
            Game.getInstance().resizeFrameWithContents();
        // Tell the Renderer to update the FoV from its thread to avoid thread-safety issues.
        // Settings.fovUpdateRequired = true;
        // Settings.checkSoftwareCursor();
        // Camera.setDistance(Settings.VIEW_DISTANCE.get(Settings.currentProfile));
        synchronizeGuiValues();
        // QueueWindow.syncColumnsWithSettings();
        //bQueueWindow.playlistTable.repaint();
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
        worldNumberJLabel.setAlignmentY((float) 0.75);
        worldListTitleTextFieldContainers.get(i).add(worldNumberJLabel, cR);

        cR.weightx = 0.5;
        cR.gridwidth = 5;

        worldNamesJTextFields.put(i, new HintTextField("Name of World"));
        worldNamesJTextFields.get(i).setMinimumSize(new Dimension(80, 28));
        worldNamesJTextFields.get(i).setMaximumSize(new Dimension(300, 28));
        worldNamesJTextFields.get(i).setPreferredSize(new Dimension(200, 28));
        worldNamesJTextFields.get(i).setAlignmentY((float) 0.75);
        worldListTitleTextFieldContainers.get(i).add(worldNamesJTextFields.get(i), cR);

        cR.weightx = 0.1;
        cR.gridwidth = 1;
        cR.anchor = GridBagConstraints.LINE_END;

    /*
          JLabel spacingJLabel = new JLabel("");
          worldNumberJLabel.setAlignmentY((float) 0.75);
          worldListTitleTextFieldContainers.get(i).add(spacingJLabel, cR);
    */

        cR.weightx = 0.3;
        cR.gridwidth = 1;

        worldDeleteJButtons.put(i, new JButton("Delete World"));
        worldDeleteJButtons.get(i).setAlignmentY((float) 0.80);
        worldDeleteJButtons.get(i).setPreferredSize(new Dimension(50, 28));
        worldDeleteJButtons.get(i).setActionCommand(String.format("%d", i));
        worldDeleteJButtons
                .get(i)
                .addActionListener(
                        new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String actionCommandWorld = e.getActionCommand();
                                int choice =
                                        JOptionPane.showConfirmDialog(
                                                Launcher.getConfigWindow().frame,
                                                String.format(
                                                        "Warning: Are you sure you want to DELETE World %s?",
                                                        actionCommandWorld),
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

        worldListTitleTextFieldContainers.get(i).setMaximumSize(new Dimension(680, 40));
        worldListPanel.add(worldListTitleTextFieldContainers.get(i));

        //// URL/Ports line
        worldUrlsJTextFields.put(i, new HintTextField(String.format("World %d URL", i)));
        worldPortsJTextFields.put(
                i, new HintTextField(String.format("World %d Port (default: 43594)", i)));

        worldUrlsJTextFields.get(i).setMinimumSize(new Dimension(100, 28));
        worldUrlsJTextFields.get(i).setMaximumSize(new Dimension(500, 28));
        worldUrlsJTextFields.get(i).setPreferredSize(new Dimension(500, 28));
        worldUrlsJTextFields.get(i).setAlignmentY((float) 0.75);

        worldPortsJTextFields.get(i).setMinimumSize(new Dimension(100, 28));
        worldPortsJTextFields.get(i).setMaximumSize(new Dimension(180, 28));
        worldPortsJTextFields.get(i).setAlignmentY((float) 0.75);

        worldListURLPortTextFieldContainers.put(i, new JPanel());

        worldListURLPortTextFieldContainers
                .get(i)
                .setLayout(new BoxLayout(worldListURLPortTextFieldContainers.get(i), BoxLayout.X_AXIS));

        worldListURLPortTextFieldContainers.get(i).add(worldUrlsJTextFields.get(i));
        worldListURLPortTextFieldContainers.get(i).add(worldPortsJTextFields.get(i));
        worldListPanel.add(worldListURLPortTextFieldContainers.get(i));

        worldListSpacingLabels.put(i, new JLabel(""));
        worldListSpacingLabels.get(i).setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        worldListPanel.add(worldListSpacingLabels.get(i));

        if (i > Settings.WORLD_NAMES.size()) {
            Settings.createNewWorld(i);
        }
    }

    public void addAddWorldButton() {
        JButton addWorldButton = new JButton("Add New World");
        addWorldButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        worldListPanel.remove(addWorldButton);
                        ++Settings.WORLDS_TO_DISPLAY;
                        synchronizeWorldTab();
                        addAddWorldButton();
                    }
                });
        worldListPanel.add(addWorldButton);
        worldListPanel.revalidate();
        worldListPanel.repaint();
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
