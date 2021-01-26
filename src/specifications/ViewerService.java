/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: specifications/ViewerService.java 2015-03-11 buixuan.
 * ******************************************************/
package specifications;

import javafx.scene.Parent;
import tools.User;


public interface ViewerService{
  public void init();
  public Parent getPanel();
  public void setMainWindowWidth(double w);
  public void setMainWindowHeight(double h);
  public void setHeroesCommand(User.COMMAND c);
  public void releaseHeroesCommand(User.COMMAND c);


}
