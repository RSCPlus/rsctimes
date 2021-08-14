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

import Client.Launcher;
import Client.Logger;
import Client.NotificationsHandler;
import Client.NotificationsHandler.NotifType;
import Client.Settings;
import Client.Util;
import Client.WorldMapWindow;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageConsumer;
import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;

/** Handles rendering overlays and client adjustments based on window size */
public class Renderer {

    public static Object instance = null;
    public static Graphics graphicsInstance = null;

    public static int width;
    public static int height;
    public static int height_client;
    public static int[] pixels;
    public static int sprite_media;

    public static int fps;
    public static float alpha_time;
    public static float delta_time;
    public static long time;

    public static ImageConsumer image_consumer = null;

    public static Color color_dynamic;
    public static Color color_text = new Color(240, 240, 240);
    public static Color color_shadow = new Color(15, 15, 15);
    public static Color color_gray = new Color(60, 60, 60);
    public static Color color_hp = new Color(0, 210, 0);
    public static Color color_fatigue = new Color(210, 210, 0);
    public static Color color_prayer = new Color(160, 160, 210);
    public static Color color_low = new Color(255, 0, 0);
    public static Color color_poison = new Color(155, 205, 50);
    public static Color color_item = new Color(245, 245, 245);
    public static Color color_item_highlighted = new Color(245, 196, 70);
    public static Color color_replay = new Color(100, 185, 178);
    public static Color color_white = new Color(255, 255, 255);
    public static Color color_yellow = new Color(255, 255, 0);

    public static Image image_border;
    public static Image image_bar_frame;
    public static Image image_bar_frame_short;
    public static Image image_cursor;
    public static Image image_highlighted_item;
    public static Image image_wiki_hbar_inactive;
    public static Image image_wiki_hbar_active;
    private static BufferedImage game_image;

    private static Dimension new_size = new Dimension(0, 0);

    public static Font font_main;
    public static Font font_big;

    private static int frames = 0;
    private static long fps_timer = 0;
    private static boolean screenshot = false;

    public static boolean combat_menu_shown = false;

    public static int replayOption = 0;

    public static String[] shellStrings;

    private static boolean macOS_resize_workaround = Util.isMacOS();

    public static boolean quietScreenshot = false;


    public static void init() {
        // Resize game window
        new_size.width = 512;
        new_size.height = 334;
        // TODO: handle_resize();

        // Load fonts
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            InputStream is = Launcher.getResourceAsStream("/assets/Helvetica-Bold.ttf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            ge.registerFont(font);
            font_main = font.deriveFont(Font.PLAIN, 11.0f);
            font_big = font.deriveFont(Font.PLAIN, 22.0f);

            is = Launcher.getResourceAsStream("/assets/TimesRoman.ttf");
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, is));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load images
        try {
            image_border = ImageIO.read(Launcher.getResource("/assets/hbar/border.png"));
            image_bar_frame = ImageIO.read(Launcher.getResource("/assets/hbar/bar.png"));
            image_bar_frame_short = ImageIO.read(Launcher.getResource("/assets/hbar/bar_short.png"));
            image_wiki_hbar_inactive =
                    ImageIO.read(Launcher.getResource("/assets/hbar/wiki_hbar_inactive.png"));
            image_wiki_hbar_active =
                    ImageIO.read(Launcher.getResource("/assets/hbar/wiki_hbar_active.png"));
            image_cursor = ImageIO.read(Launcher.getResource("/assets/cursor.png"));
            image_highlighted_item = ImageIO.read(Launcher.getResource("/assets/highlighted_item.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawShadowText(
            Graphics2D g, String text, int x, int y, Color textColor, boolean center) {
        int textX = x;
        int textY = y;
        if (center) {
            Dimension bounds = getStringBounds(g, text);
            textX -= (bounds.width / 2);
            textY += (bounds.height / 2);
        }

        g.setColor(color_shadow);
        g.drawString(text, textX + 1, textY);
        g.drawString(text, textX - 1, textY);
        g.drawString(text, textX, textY + 1);
        g.drawString(text, textX, textY - 1);

        g.setColor(textColor);
        g.drawString(text, textX, textY);
    }

    public static void drawColoredText(Graphics2D g, String text, int x, int y) {
        drawColoredText(g, text, x, y, false);
    }

    public static void drawColoredText(Graphics2D g, String text, int x, int y, boolean center) {
        int textX = x;
        int textY = y;

        if (center) {
            Dimension bounds = getStringBounds(g, text.replaceAll("@...@", ""));
            textX -= (bounds.width / 2);
            textY += (bounds.height / 2);
        }

        String outputText = "";
        Color outputColor = colorFromCode("@yel@");
        Color currentColor = outputColor;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '@' && text.charAt(i + 4) == '@') {
                outputColor = colorFromCode(text.substring(i, i + 4));
                i += 5;
                if (i >= text.length()) break;
            }

            if (currentColor != outputColor) {
                if (outputText.length() > 0) {
                    g.setColor(color_shadow);
                    g.drawString(outputText, textX + 1, textY);
                    g.drawString(outputText, textX - 1, textY);
                    g.drawString(outputText, textX, textY + 1);
                    g.drawString(outputText, textX, textY - 1);

                    g.setColor(currentColor);
                    g.drawString(outputText, textX, textY);
                    textX += getStringBounds(g, outputText).width;
                }
                currentColor = outputColor;
                outputText = "";
            }

            outputText += text.charAt(i);
        }

        g.setColor(color_shadow);
        g.drawString(outputText, textX + 1, textY);
        g.drawString(outputText, textX - 1, textY);
        g.drawString(outputText, textX, textY + 1);
        g.drawString(outputText, textX, textY - 1);

        g.setColor(currentColor);
        g.drawString(outputText, textX, textY);
    }

    public static void drawShadowTextBorder(
            Graphics2D g,
            String text,
            int x,
            int y,
            Color textColor,
            float alpha,
            float boxAlpha,
            boolean border,
            int borderSize) {
        int textX = x;
        int textY = y;
        Dimension bounds = getStringBounds(g, text);
        textX -= (bounds.width / 2);
        textY += (bounds.height / 2);

        g.setColor(color_shadow);
        int rectX = x - (bounds.width / 2) - 2 - borderSize;
        int rectY = y - (bounds.height / 2) + 2 - borderSize;
        int rectWidth = bounds.width + 2 + (borderSize * 2);
        int rectHeight = bounds.height + (borderSize * 2);
        if (border) {
            setAlpha(g, 1.0f);
            g.drawRect(rectX, rectY, rectWidth, rectHeight);
        }
        setAlpha(g, boxAlpha);
        g.fillRect(rectX, rectY, rectWidth, rectHeight);
        setAlpha(g, alpha);
        g.drawString(text, textX + 1, textY);
        g.drawString(text, textX - 1, textY);
        g.drawString(text, textX, textY + 1);
        g.drawString(text, textX, textY - 1);

        g.setColor(textColor);
        g.drawString(text, textX, textY);
    }

    public static void setAlpha(Graphics2D g, float alpha) {
        g.setComposite(AlphaComposite.SrcOver.derive(alpha));
    }

    private static Dimension getStringBounds(Graphics2D g, String str) {
        FontRenderContext context = g.getFontRenderContext();
        Rectangle2D bounds = g.getFont().getStringBounds(str, context);
        return new Dimension((int) bounds.getWidth(), (int) bounds.getHeight());
    }


    private static Color colorFromCode(String s) {
        int hexCode = 0xffffff;

        if (s.substring(1, 4).equalsIgnoreCase("red")) hexCode = 0xff0000;
        else if (s.substring(1, 4).equalsIgnoreCase("lre")) hexCode = 0xff9040;
        else if (s.substring(1, 4).equalsIgnoreCase("yel")) hexCode = 0xffff00;
        else if (s.substring(1, 4).equalsIgnoreCase("gre")) hexCode = 65280;
        else if (s.substring(1, 4).equalsIgnoreCase("blu")) hexCode = 255;
        else if (s.substring(1, 4).equalsIgnoreCase("cya")) hexCode = 65535;
        else if (s.substring(1, 4).equalsIgnoreCase("mag")) hexCode = 0xff00ff;
        else if (s.substring(1, 4).equalsIgnoreCase("whi")) hexCode = 0xffffff;
        else if (s.substring(1, 4).equalsIgnoreCase("bla")) hexCode = 0;
        else if (s.substring(1, 4).equalsIgnoreCase("dre")) hexCode = 0xc00000;
        else if (s.substring(1, 4).equalsIgnoreCase("ora")) hexCode = 0xff9040;
        else if (s.substring(1, 4).equalsIgnoreCase("ran")) hexCode = (int) (Math.random() * 16777215D);
        else if (s.substring(1, 4).equalsIgnoreCase("or1")) hexCode = 0xffb000;
        else if (s.substring(1, 4).equalsIgnoreCase("or2")) hexCode = 0xff7000;
        else if (s.substring(1, 4).equalsIgnoreCase("or3")) hexCode = 0xff3000;
        else if (s.substring(1, 4).equalsIgnoreCase("gr1")) hexCode = 0xc0ff00;
        else if (s.substring(1, 4).equalsIgnoreCase("gr2")) hexCode = 0x80ff00;
        else if (s.substring(1, 4).equalsIgnoreCase("gr3")) hexCode = 0x40ff00;

        return new Color(hexCode);
    }

    public static void drawStringCenter(String text, int x, int y, int font, int color) {
        // TODO: find hook, reimplement
        Logger.Info("Unimplemented drawStringCenter called with text: " + text);

        /*
        if (Reflection.drawStringCenter == null) return;


        try {
            Reflection.drawStringCenter.invoke(instance, x, text, color, 0, font, y);
        } catch (Exception e) {
        }
         */
    }


    public static void drawBox(int x, int y, int w, int h, int color) {
        // TODO: find hook, reimplement
        Logger.Debug("Unimplemented drawBox called");

        /*
        if (Reflection.drawBox == null) return;

        try {
            Reflection.drawBox.invoke(instance, x, (byte) -127, color, y, h, w);
        } catch (Exception e) {
        }
         */
    }

    public static void drawBoxBorder(int x, int y, int w, int h, int color) {
        // TODO: find hook, reimplement
        Logger.Debug("Unimplemented drawBoxBorder called");

        /*
        if (Reflection.drawBoxBorder == null) return;

        try {
            Reflection.drawBoxBorder.invoke(instance, x, w, y, 27785, h, color);
        } catch (Exception e) {
        }
         */
    }

}
