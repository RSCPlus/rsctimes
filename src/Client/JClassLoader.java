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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/** Deals with fetching, loading, and patching a modified RSC jar. */
public class JClassLoader extends ClassLoader {

  /** Stores class names and the corresponding class byte data */
  private Map<String, byte[]> m_classData = new HashMap<>();

  /**
   * Fetches the game jar and loads and patches the classes
   *
   * @param jarURL The URL of the jar to be loaded and patched
   * @return If no exceptions occurred
   */
  public boolean fetch(String jarURL) {
    Logger.Info("Fetching Jar: " + jarURL);

    try {
      JarInputStream in = new JarInputStream(Launcher.getResourceAsStream(jarURL));
      Launcher.getInstance().setProgress(1, 1);

      JarEntry entry;
      while ((entry = in.getNextJarEntry()) != null) {
        // Check if file is needed
        String name = entry.getName();

        // Read class to byte array
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int readSize;
        while ((readSize = in.read(data, 0, data.length)) != -1) bOut.write(data, 0, readSize);
        byte[] classData = bOut.toByteArray();
        bOut.close();

        if (name.endsWith(".class")) {
          Logger.Info("Loading file: " + name);
          Launcher.getInstance().setStatus("Loading " + name + "...");

          name = name.replace(".class", "").replace('/', '.');
          classData = JClassPatcher.getInstance().patch(classData);
          m_classData.put(name, classData);
        }
      }
      in.close();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public final Class<?> findClass(String name) {
    byte[] data = m_classData.get(name);
    if (data == null) return null;

    return defineClass(name, data, 0, data.length);
  }
}
