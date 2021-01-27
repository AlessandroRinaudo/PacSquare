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
import specifications.FruitService;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.ArrayList;

public class Engine implements EngineService, RequireDataService{
  private static final double friction=HardCodedParameters.friction,
                              heroesStep=HardCodedParameters.heroesStep,
                              fruitStep=HardCodedParameters.fruitStep;
  private Timer engineClock;
  private DataService data;
  private User.COMMAND command;
  private Random gen;
  private boolean moveLeft,moveRight,moveUp,moveDown;
  private double heroesVX,heroesVY;

  public Engine(){}

  @Override
  public void bindDataService(DataService service){
    data=service; //hellooo
    
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
        
        if (gen.nextInt(10)<0.01 && data.getFruits().size()<5) spawnFruit();

        updateSpeedHeroes();
        updateCommandHeroes();
        updatePositionHeroes();

        ArrayList<FruitService> fruits = new ArrayList<FruitService>();
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
        
        

        for (FruitService p:data.getFruits()){
       /*   if (p.getAction()==PhantomService.MOVE.LEFT) moveLeft(p);
          if (p.getAction()==PhantomService.MOVE.RIGHT) moveRight(p);
          if (p.getAction()==PhantomService.MOVE.UP) moveUp(p);
          if (p.getAction()==PhantomService.MOVE.DOWN) moveDown(p);*/

          if (collisionHeroesFruit(p)){
            data.setSoundEffect(Sound.SOUND.HeroesGotHit);
            score++;
          } else {
            if (p.getPosition().x>0) fruits.add(p);
          }
        }

        data.addScore(score);

        data.setFruits(fruits);

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

	  
	  if (data.getHeroesPosition().x-(HardCodedParameters.heroesWidth/(double)3)<0) data.setHeroesPosition(new Position((int)HardCodedParameters.heroesWidth/3,data.getHeroesPosition().y));
	  if (data.getHeroesPosition().x+(HardCodedParameters.heroesWidth/(double)2)>HardCodedParameters.maxX) data.setHeroesPosition(new Position((int)HardCodedParameters.maxX-(int)HardCodedParameters.heroesWidth/(double)2,data.getHeroesPosition().y));
	  if (data.getHeroesPosition().y-(HardCodedParameters.heroesHeight/(double)3)<0) data.setHeroesPosition(new Position(data.getHeroesPosition().x,(int)HardCodedParameters.heroesHeight/(double)3));
	  if (data.getHeroesPosition().y+(HardCodedParameters.heroesHeight/(double)3)>HardCodedParameters.maxY*.8) data.setHeroesPosition(new Position(data.getHeroesPosition().x,(int)HardCodedParameters.maxY*.8-(int)HardCodedParameters.heroesHeight/(double)3));

  }

  private void spawnFruit(){
    int x=0;
    int y=0;
    boolean cont=true;
    while (cont) {
      y=(int)(gen.nextInt((int)(HardCodedParameters.defaultHeight*.6))+HardCodedParameters.defaultHeight*.1);
      x=(int)(gen.nextInt((int)(HardCodedParameters.defaultWidth*.6))+HardCodedParameters.defaultWidth*.1);

      cont=false;
      for (FruitService p:data.getFruits()){
        if (p.getPosition().equals(new Position(x,y))) cont=true;
      }
    }
    data.addFruit(new Position(x,y));
  }

  private void moveLeft(FruitService p){
    p.setPosition(new Position(p.getPosition().x-fruitStep,p.getPosition().y));
  }

  private void moveRight(FruitService p){
    if(data.getHeroesPosition().x <= 95) {
      p.setPosition(new Position(p.getPosition().x + fruitStep, p.getPosition().y));
    }
    else{ }
  }

  private void moveUp(FruitService p){
    p.setPosition(new Position(p.getPosition().x,p.getPosition().y-fruitStep));
  }

  private void moveDown(FruitService p){
    if(HardCodedParameters.defaultHeight > p.getPosition().y) {
      p.setPosition(new Position(p.getPosition().x, p.getPosition().y + fruitStep));
    }
  }

  private boolean collisionHeroesFruit(FruitService p){
   return (
      (data.getHeroesPosition().x-p.getPosition().x)*(data.getHeroesPosition().x-p.getPosition().x)+
      (data.getHeroesPosition().y-p.getPosition().y)*(data.getHeroesPosition().y-p.getPosition().y) <
      (data.getHeroesWidth()*.33+data.getFruitWidth())*(data.getHeroesWidth()*.33+data.getFruitWidth())
    );
    
	
			
  }
  
  private boolean collisionHeroesFruits(){
    for (FruitService p:data.getFruits()) if (collisionHeroesFruit(p)) return true; return false;
  }
}
