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
import specifications.DataService;
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
  private ReadService data2;

  private ImageView heroesAvatar;
  private ImageView phantomAvatar;
  private ImageView phantomAvatarR;

  private Image heroesSpriteSheet;
  private Image phantomSpriteSheet;

  private ArrayList<Rectangle> heroesAvatarViewports;
  private ArrayList<Circle> phantomAvatarViewports;

  private ArrayList<Integer> heroesAvatarXModifiers;
  private ArrayList<Integer> heroesAvatarYModifiers;
  private ArrayList<Integer> phantomAvatarXModifiers;
  private ArrayList<Integer> phantomAvatarYModifiers;
  private int heroesAvatarViewportIndex;
  private int phantomAvatarViewportIndex;

  private double xShrink,yShrink,shrink,xModifier,yModifier,heroesScale,phantomScale;
  private boolean moveLeft,moveRight,moveUp,moveDown;
  Image avatarRight=new Image("file:src/images/avatarRight.png");
  Image avatarLeft=new Image("file:src/images/avatarLeft.png");
  Image avatarUp=new Image("file:src/images/avatarUp.png");
  Image avatarDown=new Image("file:src/images/avatarDown.png");
  Image phantomImg=new Image("file:src/images/phantom.png");
  Image phantomImgR=new Image("file:src/images/phantomR.png");

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
 
    heroesAvatar = new ImageView(avatarRight);
    phantomAvatar = new ImageView(phantomImg);
    phantomAvatarR = new ImageView(phantomImgR);



    //heroesAvatar = new Circle(20,  Color.rgb(255,238,0));
    //heroesAvatar.setEffect(new Lighting());
    heroesAvatar.setTranslateX(data.getHeroesPosition().x);
    heroesAvatar.setTranslateY(data.getHeroesPosition().y);

    heroesAvatarViewports = new ArrayList<Rectangle>();
    heroesAvatarXModifiers = new ArrayList<Integer>();
    heroesAvatarYModifiers = new ArrayList<Integer>();

    heroesAvatarViewportIndex=0;
    
    //phantomAvatar.setTranslateX(data.getPhantoms().get(0).getPosition().x);
    //phantomAvatar.setTranslateX(data.getPhantoms().get(0).getPosition().y);

    phantomAvatarViewports = new ArrayList<Circle>();
    phantomAvatarXModifiers = new ArrayList<Integer>();
    phantomAvatarYModifiers = new ArrayList<Integer>();

    phantomAvatarViewportIndex=0;
    
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
    heroesAvatarViewports.add(new Rectangle(HardCodedParameters.heroesHeight,HardCodedParameters.heroesWidth,  Color.rgb(255,238,0)));

    heroesAvatarXModifiers.add(0);heroesAvatarYModifiers.add(0);
    
    
    double radius=.5*Math.min(shrink*data.getPhantomWidth(),shrink*data.getPhantomHeight());

    phantomAvatarViewports.add(new Circle(radius+2000,  Color.rgb(255,238,0)));

    phantomAvatarXModifiers.add(0);phantomAvatarYModifiers.add(0);
   /* heroesAvatarXModifiers.add(6);heroesAvatarYModifiers.add(-6);
    heroesAvatarXModifiers.add(2);heroesAvatarYModifiers.add(-8);
    heroesAvatarXModifiers.add(1);heroesAvatarYModifiers.add(-10);
    heroesAvatarXModifiers.add(1);heroesAvatarYModifiers.add(-13);
    heroesAvatarXModifiers.add(5);heroesAvatarYModifiers.add(-15);
    heroesAvatarXModifiers.add(5);heroesAvatarYModifiers.add(-13);
    heroesAvatarXModifiers.add(0);heroesAvatarYModifiers.add(-9);
    heroesAvatarXModifiers.add(0);heroesAvatarYModifiers.add(-6);*/
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
    Rectangle map = new Rectangle(-2*xModifier+shrink*defaultMainWidth+130,
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
                           "\n PacSquare");
    greets.setFont(new Font(.05*shrink*defaultMainHeight));
    
    greets.setFill(Color.WHITE);

    
    Text score = new Text(-0.1*shrink*defaultMainHeight+.5*shrink*defaultMainWidth,
                           -0.05*shrink*defaultMainWidth+shrink*defaultMainHeight,
                           "\n Score: "+data.getScore());
    score.setFont(new Font(.05*shrink*defaultMainHeight));
    score.setFill(Color.WHITE);

    
    int index=heroesAvatarViewportIndex/spriteSlowDownRate;
    heroesScale=data.getHeroesHeight()*shrink/heroesAvatarViewports.get(index).getHeight();
    phantomScale=data.getPhantomHeight()*shrink/phantomAvatarViewports.get(index).getRadius();

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
//

    /*phantomAvatar.setTranslateX(shrink*data.getPhantomPosition().x+
                               shrink*xModifier+
                               -phantomScale*0.5*phantomAvatarViewports.get(index).getHeight()+
                               shrink*phantomScale*phantomAvatarXModifiers.get(index)
                              );
    phantomAvatar.setTranslateY(shrink*data.getPhantomPosition().y+
                               shrink*yModifier+
                               -phantomScale*0.5*phantomAvatarViewports.get(index).getWidth()+
                               shrink*phantomScale*phantomAvatarYModifiers.get(index)
                              );*/
    phantomAvatarViewportIndex=(phantomAvatarViewportIndex+1)%(phantomAvatarViewports.size()*spriteSlowDownRate);
//

    Group panel = new Group();
    panel.getChildren().addAll(map,greets,score,heroesAvatar);
    
    ArrayList<FruitService> fruits = data.getFruits();    
    FruitService f;


    for (int j=0; j<fruits.size();j++){
      f=fruits.get(j);
      double radius=.5*Math.min(shrink*data.getPhantomWidth(),shrink*data.getPhantomHeight());

      Circle fruitAvatar = new Circle(radius+1,Color.YELLOW);
      fruitAvatar.setEffect(new Lighting());
      fruitAvatar.setTranslateX(shrink*f.getPosition().x+shrink*xModifier-radius);
      fruitAvatar.setTranslateY(shrink*f.getPosition().y+shrink*yModifier-radius);
      panel.getChildren().add(fruitAvatar);
    }

    ArrayList<PhantomService> phantoms = data.getPhantoms();
    ArrayList<PhantomService> phantoms2 = data.getPhantoms2();

    PhantomService p;
    PhantomService p2;


    for (int i=0; i<phantoms.size();i++){
      p=phantoms.get(i);
      p2=phantoms2.get(i);

      

      phantomAvatar = new ImageView(phantomImgR);
      phantomAvatar.setFitHeight(data.getPhantomHeight()*shrink*3);
      phantomAvatar.setPreserveRatio(true);
      phantomAvatar.setTranslateX(shrink*p2.getPosition().x+
              shrink*xModifier+
              -phantomScale*0.5*phantomAvatarViewports.get(index).getRadius()+
              shrink*phantomScale*phantomAvatarXModifiers.get(index)
             );
phantomAvatar.setTranslateY(shrink*p2.getPosition().y+
              shrink*yModifier+
              -phantomScale*0.5*phantomAvatarViewports.get(index).getRadius()+
              shrink*phantomScale*phantomAvatarYModifiers.get(index)
             );



      panel.getChildren().add(phantomAvatar);
      
      phantomAvatar = new ImageView(phantomImg);
      phantomAvatar.setFitHeight(data.getPhantomHeight()*shrink*3);
      phantomAvatar.setPreserveRatio(true);
      phantomAvatar.setTranslateX(shrink*p.getPosition().x+
              shrink*xModifier+
              -phantomScale*0.5*phantomAvatarViewports.get(index).getRadius()+
              shrink*phantomScale*phantomAvatarXModifiers.get(index)
             );
phantomAvatar.setTranslateY(shrink*p.getPosition().y+
              shrink*yModifier+
              -phantomScale*0.5*phantomAvatarViewports.get(index).getRadius()+
              shrink*phantomScale*phantomAvatarYModifiers.get(index)
             );



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
