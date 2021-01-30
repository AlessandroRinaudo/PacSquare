/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: specifications/WriteService.java 2015-03-11 buixuan.
 * ******************************************************/
package specifications;

import tools.Position;
import tools.Sound;

import java.util.ArrayList;

public interface WriteService {
  public void setHeroesPosition(Position p);
  public void setStepNumber(int n);
  public void addFruit(Position p);
  public void setFruits(ArrayList<FruitService> fruits);
  public void addPhantom(Position p);
  public void setPhantoms(ArrayList<PhantomService> phantoms);
  public void setSoundEffect(Sound.SOUND s);
  public void addScore(int score);
  public void setPhantoms2(ArrayList<PhantomService> phantoms);
  public void addPhantom2(Position p);
}
