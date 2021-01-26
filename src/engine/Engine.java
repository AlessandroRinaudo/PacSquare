/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: engine/Engine.java 2015-03-11 buixuan.
 * ******************************************************/
package engine;

import javafx.scene.shape.Rectangle;
import tools.HardCodedParameters;
import tools.User;
import tools.Position;
import tools.Sound;

import specifications.EngineService;
import specifications.DataService;
import specifications.RequireDataService;
import specifications.PhantomService;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.ArrayList;

public class Engine implements EngineService, RequireDataService{
  private static final double friction=HardCodedParameters.friction,
                              heroesStep=HardCodedParameters.heroesStep,
                              phantomStep=HardCodedParameters.phantomStep;
  private Timer engineClock;
  private DataService data;
  private User.COMMAND command;
  private Random gen;
  private boolean moveLeft,moveRight,moveUp,moveDown;
  private double heroesVX,heroesVY;

  public Engine(){}

  @Override
  public void bindDataService(DataService service){
    data=service;
  }
  
  @Override
  public void init(){
    engineClock = new Timer();
    command = User.COMMAND.NONE;
    gen = new Random();
    moveLeft = false;
    moveRight = false;
    moveUp = false;
    moveDown = false;
    heroesVX = 0;
    heroesVY = 0;
  }

  @Override
  public void start(){
    engineClock.schedule(new TimerTask(){
      public void run() {
        System.out.println(data.getHeroesPosition().x);
        //System.out.println("Game step #"+data.getStepNumber()+": checked.");
        
        if (gen.nextInt(10)<0.01 && data.getPhantoms().size()<5) spawnPhantom();

        updateSpeedHeroes();
        updateCommandHeroes();
        updatePositionHeroes();

        ArrayList<PhantomService> phantoms = new ArrayList<PhantomService>();
        int score=0;

        data.setSoundEffect(Sound.SOUND.None);
        /*
        if (ding()) {
        	if (collisionHeroesPhantom(p)){
                data.setSoundEffect(Sound.SOUND.HeroesGotHit);
                score++;
              } else {
                if (p.getPosition().x>0) phantoms.add(p);
              }
        }
        */
        
        

        for (PhantomService p:data.getPhantoms()){
       /*   if (p.getAction()==PhantomService.MOVE.LEFT) moveLeft(p);
          if (p.getAction()==PhantomService.MOVE.RIGHT) moveRight(p);
          if (p.getAction()==PhantomService.MOVE.UP) moveUp(p);
          if (p.getAction()==PhantomService.MOVE.DOWN) moveDown(p);*/

          if (collisionHeroesPhantom(p)){
            data.setSoundEffect(Sound.SOUND.HeroesGotHit);
            score++;
          } else {
            if (p.getPosition().x>0) phantoms.add(p);
          }
        }

        data.addScore(score);

        data.setPhantoms(phantoms);

        data.setStepNumber(data.getStepNumber()+1);
      }
    },0,HardCodedParameters.enginePaceMillis);
  }

  @Override
  public void stop(){
    engineClock.cancel();
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

  private void updateSpeedHeroes(){
    heroesVX*=friction;
    heroesVY*=friction;
  }

  private void updateCommandHeroes(){
   /* double xShrink=1;
    double yShrink=1;
    double shrink=Math.min(xShrink,yShrink);
    double xModifier=.01*shrink*HardCodedParameters.defaultWidth;
    double yModifier=.01*shrink*HardCodedParameters.defaultHeight;
    xModifier=-2*xModifier+shrink*HardCodedParameters.defaultWidth;
    yModifier=-.2*shrink*HardCodedParameters.defaultHeight+shrink*HardCodedParameters.defaultHeight;
    if (moveLeft && data.getHeroesPosition().x >= 45) heroesVX-=heroesStep;
    if (moveRight &&  data.getHeroesPosition().x <= xModifier*0.95) heroesVX+=heroesStep;
    if (moveUp && data.getHeroesPosition().y >= 80) heroesVY-=heroesStep;
    if (moveDown && data.getHeroesPosition().y <= yModifier*0.85) heroesVY+=heroesStep;*/
	    if (moveLeft) heroesVX-=heroesStep;
	    if (moveRight) heroesVX+=heroesStep;
	    if (moveUp) heroesVY-=heroesStep;
	    if (moveDown) heroesVY+=heroesStep;
  }
  
  private void updatePositionHeroes(){
		data.setHeroesPosition(new Position(data.getHeroesPosition().x+heroesVX,data.getHeroesPosition().y+heroesVY));

	  
	  if (data.getHeroesPosition().x-(HardCodedParameters.heroesWidth/2)<0) data.setHeroesPosition(new Position((int)HardCodedParameters.heroesWidth/2,data.getHeroesPosition().y));
	  if (data.getHeroesPosition().x+(HardCodedParameters.heroesWidth/2)>HardCodedParameters.maxX) data.setHeroesPosition(new Position((int)HardCodedParameters.maxX-(int)HardCodedParameters.heroesWidth/2,data.getHeroesPosition().y));
	  if (data.getHeroesPosition().y-(HardCodedParameters.heroesHeight/2)<30) data.setHeroesPosition(new Position(data.getHeroesPosition().x,40));
	  if (data.getHeroesPosition().y+(HardCodedParameters.heroesHeight/2)>HardCodedParameters.maxY*.8) data.setHeroesPosition(new Position(data.getHeroesPosition().x,(int)HardCodedParameters.maxY*.8-(int)HardCodedParameters.heroesHeight/2));

  }

  private void spawnPhantom(){
    int x=0;
    int y=0;
    boolean cont=true;
    while (cont) {
      y=(int)(gen.nextInt((int)(HardCodedParameters.defaultHeight*.6))+HardCodedParameters.defaultHeight*.1);
      x=(int)(gen.nextInt((int)(HardCodedParameters.defaultWidth*.6))+HardCodedParameters.defaultWidth*.1);

      cont=false;
      for (PhantomService p:data.getPhantoms()){
        if (p.getPosition().equals(new Position(x,y))) cont=true;
      }
    }
    data.addPhantom(new Position(x,y));
  }

  private void moveLeft(PhantomService p){
    p.setPosition(new Position(p.getPosition().x-phantomStep,p.getPosition().y));
  }

  private void moveRight(PhantomService p){
    if(data.getHeroesPosition().x <= 95) {
      p.setPosition(new Position(p.getPosition().x + phantomStep, p.getPosition().y));
    }
    else{ }
  }

  private void moveUp(PhantomService p){
    p.setPosition(new Position(p.getPosition().x,p.getPosition().y-phantomStep));
  }

  private void moveDown(PhantomService p){
    if(HardCodedParameters.defaultHeight > p.getPosition().y) {
      p.setPosition(new Position(p.getPosition().x, p.getPosition().y + phantomStep));
    }
  }

  private boolean collisionHeroesPhantom(PhantomService p){
    return (
      (data.getHeroesPosition().x-p.getPosition().x)*(data.getHeroesPosition().x-p.getPosition().x)+
      (data.getHeroesPosition().y-p.getPosition().y)*(data.getHeroesPosition().y-p.getPosition().y) <
      0.25*(data.getHeroesWidth()+data.getPhantomWidth())*(data.getHeroesWidth()+data.getPhantomWidth())
    );
  }
  
  private boolean collisionHeroesPhantoms(){
    for (PhantomService p:data.getPhantoms()) if (collisionHeroesPhantom(p)) return true; return false;
  }
}
