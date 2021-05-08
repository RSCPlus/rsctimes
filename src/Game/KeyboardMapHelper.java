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

import java.util.HashMap;
import java.util.Map;

public class KeyboardMapHelper {

  private static final Map<Integer, Integer> map =
      new HashMap<Integer, Integer>() {
        {
          put(19, 1024); // pause/break
          put(20, 1022); // caps lock
          put(33, 1002); // page up
          put(34, 1003); // page down
          put(35, 1001); // end
          put(36, 1000); // home
          put(37, 1006); // left arrow
          put(38, 1004); // up arrow
          put(39, 1007); // right arrow
          put(40, 1005); // down arrow
          put(112, 1008); // f1
          put(113, 1009); // f2
          put(114, 1010); // f3
          put(115, 1011); // f4
          put(116, 1012); // f5
          put(117, 1013); // f6
          put(118, 1014); // f7
          put(119, 1015); // f8
          put(120, 1016); // f9
          put(121, 1017); // f10
          put(122, 1018); // f11
          put(123, 1019); // f12
          put(144, 1023); // num lock
          put(145, 1021); // scroll lock
        }
      };

  public static Integer convert(int modernId) {
    return map.getOrDefault(modernId, modernId);
  }
}
