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

import java.io.PrintWriter;
import org.fusesource.jansi.AnsiConsole;

public class Logger {
  private static PrintWriter m_logWriter;

  public static void start() {
    AnsiConsole.systemInstall();
    // File file = new File(Settings.Dir.JAR + "/log.txt");
    /*try {
      m_logWriter = new PrintWriter(new FileOutputStream(file));
    } catch (Exception e) {
    }*/
  }

  public static void stop() {
    /*try {
      m_logWriter.close();
    } catch (Exception e) {
    }*/
    AnsiConsole.systemUninstall();
  }

  public enum Type {
    DEBUG(0),
    INFO(1),
    ERROR(2);

    Type(int id) {
      this.id = id;
    }

    public int id;
  };

  public static void Log(Type type, String message) {
    // if(!Settings.DEBUG && type == Type.DEBUG)
    //	return;

    System.out.println("[" + m_logTypeName[type.id] + "] " + message);
  }

  public static void Debug(String message) {
    Log(Type.DEBUG, message);
  }

  public static void Info(String message) {
    Log(Type.INFO, message);
  }

  public static void Error(String message) {
    Log(Type.ERROR, message);
  }

  private static final String m_logTypeName[] = {"DEBUG", " INFO", "ERROR"};
}
