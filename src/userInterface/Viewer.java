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
  private ArrayList<Circle> heroesAvatarViewports;
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

    //Yucky hard-conding
 

    //heroesSpriteSheet =  cyclope;
 
    heroesAvatar = new ImageView(cyclope);
    //heroesAvatar = new Circle(20,  Color.rgb(255,238,0));
    //heroesAvatar.setEffect(new Lighting());
    heroesAvatar.setTranslateX(data.getHeroesPosition().x);
    heroesAvatar.setTranslateY(data.getHeroesPosition().y);

    heroesAvatarViewports = new ArrayList<Circle>();
    heroesAvatarXModifiers = new ArrayList<Integer>();
    heroesAvatarYModifiers = new ArrayList<Integer>();

    heroesAvatarViewportIndex=0;
    
    //TODO: replace the following with XML loader
    //heroesAvatarViewports.add(new Rectangle2D(301,386,95,192));
    /*heroesAvatarViewports.add(new Rectangle2D(570,194,115,190));
    heroesAvatarViewports.add(new Rectangle2D(398,386,133,192));
    heroesAvatarViewports.add(new Rectangle2D(155,194,147,190));
    heroesAvatarViewports.add(new Rectangle2D(785,386,127,194));
    heroesAvatarViewports.add(new Rectangle2D(127,582,135,198));
    heroesAvatarViewports.add(new Rectangle2D(264,582,111,200));
    heroesAvatarViewports.add(new Rectangle2D(2,582,123,198));
    heroesAvatarViewports.add(new Rectangle2D(533,386,115,192));*/
    heroesAvatarViewports.add(new Circle(20,  Color.rgb(255,238,0)));

    //heroesAvatarXModifiers.add(10);heroesAvatarYModifiers.add(-7);
    heroesAvatarXModifiers.add(6);heroesAvatarYModifiers.add(-6);
    heroesAvatarXModifiers.add(2);heroesAvatarYModifiers.add(-8);
    heroesAvatarXModifiers.add(1);heroesAvatarYModifiers.add(-10);
    heroesAvatarXModifiers.add(1);heroesAvatarYModifiers.add(-13);
    heroesAvatarXModifiers.add(5);heroesAvatarYModifiers.add(-15);
    heroesAvatarXModifiers.add(5);heroesAvatarYModifiers.add(-13);
    heroesAvatarXModifiers.add(0);heroesAvatarYModifiers.add(-9);
    heroesAvatarXModifiers.add(0);heroesAvatarYModifiers.add(-6);
    //heroesAvatarXModifiers.add(10);heroesAvatarYModifiers.add(-7);
    
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
    map.setFill(Color.WHITE);
    map.setStroke(Color.DIMGRAY);
    map.setStrokeWidth(.01*shrink*defaultMainHeight);
    map.setArcWidth(.04*shrink*defaultMainHeight);
    map.setArcHeight(.04*shrink*defaultMainHeight);
    map.setTranslateX(xModifier);
    map.setTranslateY(yModifier);
    
    Text greets = new Text(-0.1*shrink*defaultMainHeight+.5*shrink*defaultMainWidth,
                           -0.1*shrink*defaultMainWidth+shrink*defaultMainHeight,
                           "Round 1");
    greets.setFont(new Font(.05*shrink*defaultMainHeight));
    
    Text score = new Text(-0.1*shrink*defaultMainHeight+.5*shrink*defaultMainWidth,
                           -0.05*shrink*defaultMainWidth+shrink*defaultMainHeight,
                           "Score: "+data.getScore());
    score.setFont(new Font(.05*shrink*defaultMainHeight));
    
    int index=heroesAvatarViewportIndex/spriteSlowDownRate;
    heroesScale=data.getHeroesHeight()*shrink/heroesAvatarViewports.get(index).getRadius();
    //heroesAvatar.setViewport(heroesAvatarViewports.get(index));
    heroesAvatar.setFitHeight(data.getHeroesHeight()*shrink);
    heroesAvatar.setPreserveRatio(true);
    heroesAvatar.setTranslateX(shrink*data.getHeroesPosition().x+
                               shrink*xModifier+
                               -heroesScale*0.5*heroesAvatarViewports.get(index).getRadius()+
                               shrink*heroesScale*heroesAvatarXModifiers.get(index)
                              );
    heroesAvatar.setTranslateY(shrink*data.getHeroesPosition().y+
                               shrink*yModifier+
                               -heroesScale*0.5*heroesAvatarViewports.get(index).getRadius()+
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
      Circle phantomAvatar = new Circle(radius,Color.rgb(255,156,156));
      phantomAvatar.setEffect(new Lighting());
      phantomAvatar.setTranslateX(shrink*p.getPosition().x+shrink*xModifier-radius);
      phantomAvatar.setTranslateY(shrink*p.getPosition().y+shrink*yModifier-radius);
      panel.getChildren().add(phantomAvatar);
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
