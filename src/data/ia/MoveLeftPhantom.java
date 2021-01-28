/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: data/ia/MoveLeftPhantom.java 2015-03-11 buixuan.
 * ******************************************************/
package data.ia;

import tools.Position;

import specifications.PhantomService;
import java.util.Random;

public class MoveLeftPhantom implements PhantomService {
  private Position position;
  private Random gen;

  public MoveLeftPhantom(Position p) {
    gen = new Random();

    position = p;
  }

  @Override
  public Position getPosition() {
    return position;
  }

  @Override
  public PhantomService.MOVE getAction() {

    switch (gen.nextInt(4)) {
      case 0:
        return PhantomService.MOVE.LEFT;
      case 1:
        return PhantomService.MOVE.RIGHT;
      default:
        return PhantomService.MOVE.LEFT;

      // case 2:
      // simulator.moveUp();
      // break;
      // default:
      // simulator.moveDown();
      // break;
    }
  }

  @Override
  public void setPosition(Position p) {
    position = p;
  }
}
