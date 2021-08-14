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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/** Parses, stores, and retrieves values from a jav_config.ws file */
public class JConfig {
  // Server information
  /**
   * Stores the jav_config.ws 'param' value sets
   *
   * @see #m_data
   */
  public Map<String, String> parameters = new HashMap<>();

  /**
   * Stores the jav_config.ws value sets for everything but 'param' and 'msg'
   *
   * @see JConfig#parameters
   */
  private Map<String, String> m_data = new HashMap<>();

  public void create(int world) {
    if (world > 5) world = 5;
    else if (world < 1) world = 1;

    m_data.put("code", "mudclient.class");
    m_data.put("width", "512");
    m_data.put("height", "355");
    m_data.put("codebase", "http://penguin.local/");

    parameters.put("nodeid", "" + (5000 + world));
    parameters.put("modewhere", "0");
    parameters.put("modewhat", "0");
    if (world == 1) parameters.put("servertype", "" + 3);
    else parameters.put("servertype", "" + 1);
  }

  /**
   * Prepares the client to log into a given world and saves the choice in the config
   *
   * @param world The desired world to log into
   */
  public void changeWorld(int world) {
    // TODO: make compatible with rscplus world ini
    if (world == 1) {
      m_data.put("codebase", "http://game.openrsc.com/");
      Game.Client.connection_port = 43593;
      return;
    }
  }

  /**
   * Gets the corresponding String from the {@link #m_data} HashMap, given the key
   *
   * @param key A key for the {@link #m_data} HashMap
   * @return A String stored in the {@link #m_data} HashMap
   */
  public String getString(String key) {
    return m_data.get(key);
  }

  /**
   * Attempts to return a URL from {@link #m_data}, given the key
   *
   * @param key The key to a corresponding URL
   * @return A URL
   */
  public URL getURL(String key) {
    try {
      return new URL(m_data.get(key));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Gets values in the {@link #m_data} HashMap.
   *
   * @param key The HashMap key
   * @return The value of the {@link #m_data} HashMap for the supplied key
   */
  public int getInteger(String key) {
    String string = m_data.get(key);
    if (string != null) return Integer.valueOf(string);
    else return 0;
  }
}
