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

class NPC {

  public static final int TYPE_MOB = 0;
  public static final int TYPE_PLAYER = 1;

  public int id;
  public int id2;
  public int x;
  public int y;
  public int width;
  public int height;
  public String name;
  public int type;
  public int currentHits;
  public int maxHits;

  public NPC(
      int x,
      int y,
      int width,
      int height,
      String name,
      int type,
      int currentHits,
      int maxHits,
      int id,
      int id2) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.name = name;
    this.type = type;
    this.currentHits = currentHits;
    this.maxHits = maxHits;
    this.id = id;
    this.id2 = id2;
  }
}
