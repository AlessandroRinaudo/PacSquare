/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: userInterface/Viewer.java 2015-03-11 buixuan.
 * ******************************************************/
package userInterface;

import tools.HardCodedParameters;
import tools.User;


import specifications.ViewerService;
import specifications.ReadService;
import specifications.RequireReadService;
import specifications.FruitService;
import specifications.PhantomService;


import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;

public class Viewer implements ViewerService, RequireReadService{
  private static final int spriteSlowDownRate=HardCodedParameters.spriteSlowDownRate;
  private static final double defaultMainWidth=HardCodedParameters.defaultWidth,
                              defaultMainHeight=HardCodedParameters.defaultHeight;
  private ReadService data;
  private ImageView heroesAvatar;
  
  private Image heroesSpriteSheet;
  private ArrayList<Rectangle> heroesAvatarViewports;
  private ArrayList<Integer> heroesAvatarXModifiers;
  private ArrayList<Integer> heroesAvatarYModifiers;
  private int heroesAvatarViewportIndex;
  private double xShrink,yShrink,shrink,xModifier,yModifier,heroesScale;
  private boolean moveLeft,moveRight,moveUp,moveDown;
  Image avatarRight=new Image("file:src/images/avatarRight.png");
  Image avatarLeft=new Image("file:src/images/avatarLeft.png");
  Image avatarUp=new Image("file:src/images/avatarUp.png");
  Image avatarDown=new Image("file:src/images/avatarDown.png");

  Image cyclope=new Image("file:src/images/cyclope.png");


  public Viewer(){}
  
  @Override
  public void bindReadService(ReadService service){
    data=service;
  }


  @Override
  public void init(){
    xShrink=1;
    yShrink=1;
    xModifier=0;
    yModifier=0;
    moveLeft = false;
    moveRight = false;
    moveUp = false;
    moveDown = false;
    heroesAvatar = new ImageView(cyclope);
    heroesAvatar.setTranslateX(data.getHeroesPosition().x);
    heroesAvatar.setTranslateY(data.getHeroesPosition().y);

    heroesAvatarViewports = new ArrayList<Rectangle>();
    heroesAvatarXModifiers = new ArrayList<Integer>();
    heroesAvatarYModifiers = new ArrayList<Integer>();

    heroesAvatarViewportIndex=0;
    heroesAvatarViewports.add(new Rectangle(HardCodedParameters.heroesHeight,HardCodedParameters.heroesWidth,  Color.rgb(255,238,0)));

    heroesAvatarXModifiers.add(0);heroesAvatarYModifiers.add(0);
  }

  @Override
  public Parent getPanel(){
	  
	


    if(moveLeft ){
        heroesAvatar = new ImageView(avatarLeft);

    }
     
    if(moveRight ){
        heroesAvatar = new ImageView(avatarRight);

    }
    
    if(moveUp ){
        heroesAvatar = new ImageView(avatarUp);

    } 
    if(moveDown ){
        heroesAvatar = new ImageView(avatarDown);

    }

    shrink=Math.min(xShrink,yShrink);
    xModifier=.01*shrink*defaultMainHeight;
    yModifier=.01*shrink*defaultMainHeight;

    //Yucky hard-conding
    Rectangle map = new Rectangle(-2*xModifier+shrink*defaultMainWidth,
                                  -.2*shrink*defaultMainHeight+shrink*defaultMainHeight);
    map.setFill(Color.BLACK);
    map.setStroke(Color.YELLOW);
    map.setStrokeWidth(.01*shrink*defaultMainHeight);
    map.setArcWidth(.04*shrink*defaultMainHeight);
    map.setArcHeight(.04*shrink*defaultMainHeight);
    map.setTranslateX(xModifier);
    map.setTranslateY(yModifier);
    
    Text greets = new Text(-0.1*shrink*defaultMainHeight+.5*shrink*defaultMainWidth,
                           -0.1*shrink*defaultMainWidth+shrink*defaultMainHeight,
                           "PacSquare");
    greets.setFont(new Font(.05*shrink*defaultMainHeight));
    
    Text score = new Text(-0.1*shrink*defaultMainHeight+.5*shrink*defaultMainWidth,
                           -0.05*shrink*defaultMainWidth+shrink*defaultMainHeight,
                           "Score: "+data.getScore());
    score.setFont(new Font(.05*shrink*defaultMainHeight));
    
    int index=heroesAvatarViewportIndex/spriteSlowDownRate;
    heroesScale=data.getHeroesHeight()*shrink/heroesAvatarViewports.get(index).getHeight();
    //heroesAvatar.setViewport(heroesAvatarViewports.get(index));
    heroesAvatar.setFitHeight(data.getHeroesHeight()*shrink);
    heroesAvatar.setPreserveRatio(true);
    heroesAvatar.setTranslateX(shrink*data.getHeroesPosition().x+
                               shrink*xModifier+
                               -heroesScale*0.5*heroesAvatarViewports.get(index).getHeight()+
                               shrink*heroesScale*heroesAvatarXModifiers.get(index)
                              );
    heroesAvatar.setTranslateY(shrink*data.getHeroesPosition().y+
                               shrink*yModifier+
                               -heroesScale*0.5*heroesAvatarViewports.get(index).getWidth()+
                               shrink*heroesScale*heroesAvatarYModifiers.get(index)
                              );
    heroesAvatarViewportIndex=(heroesAvatarViewportIndex+1)%(heroesAvatarViewports.size()*spriteSlowDownRate);

    Group panel = new Group();
    panel.getChildren().addAll(map,greets,score,heroesAvatar);

    ArrayList<PhantomService> phantoms = data.getPhantoms();
    PhantomService p;

    for (int i=0; i<phantoms.size();i++){
      p=phantoms.get(i);
      double radius=.5*Math.min(shrink*data.getPhantomWidth(),shrink*data.getPhantomHeight());
      Circle phantomAvatar = new Circle(radius+12,Color.RED);
      phantomAvatar.setEffect(new Lighting());
      phantomAvatar.setTranslateX(shrink*p.getPosition().x+shrink*xModifier-radius);
      phantomAvatar.setTranslateY(shrink*p.getPosition().y+shrink*yModifier-radius+2);
      panel.getChildren().add(phantomAvatar);
      
    }
    

    ArrayList<FruitService> fruits = data.getFruits();    
    FruitService f;


    for (int j=0; j<fruits.size();j++){
      f=fruits.get(j);
      double radius=.5*Math.min(shrink*data.getPhantomWidth(),shrink*data.getPhantomHeight());

      Circle fruitAvatar = new Circle(radius+1,Color.YELLOW);
      fruitAvatar.setEffect(new Lighting());
      fruitAvatar.setTranslateX(shrink*f.getPosition().x+shrink*xModifier-radius+13);
      fruitAvatar.setTranslateY(shrink*f.getPosition().y+shrink*yModifier-radius+15);
      panel.getChildren().add(fruitAvatar);
    }

    return panel;
  
  }

  @Override
  public void setMainWindowWidth(double width){
    xShrink=width/defaultMainWidth;
  }
  
  @Override
  public void setMainWindowHeight(double height){
    yShrink=height/defaultMainHeight;
  }


  @Override
  public void setHeroesCommand(User.COMMAND c){
    if (c==User.COMMAND.LEFT) moveLeft=true;
    if (c==User.COMMAND.RIGHT) moveRight=true;
    if (c==User.COMMAND.UP) moveUp=true;
    if (c==User.COMMAND.DOWN) moveDown=true;
  }
  
  @Override
  public void releaseHeroesCommand(User.COMMAND c){
    if (c==User.COMMAND.LEFT) moveLeft=false;
    if (c==User.COMMAND.RIGHT) moveRight=false;
    if (c==User.COMMAND.UP) moveUp=false;
    if (c==User.COMMAND.DOWN) moveDown=false;
  }

  
}
