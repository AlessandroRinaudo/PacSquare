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
import specifications.PhantomService;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.ArrayList;

public class Engine implements EngineService, RequireDataService {
  private static final double friction = HardCodedParameters.friction, heroesStep = HardCodedParameters.heroesStep,
      fruitStep = HardCodedParameters.fruitStep, phantomStep = HardCodedParameters.phantomStep;
  private Timer engineClock;
  private DataService data;
  private DataService data2;

  private User.COMMAND command;
  private Random gen;
  public boolean moveLeft, moveRight, moveUp, moveDown, gameon = true;
  private double heroesVX, heroesVY;
  public int cpt = 0;
  int score = 0, vitesseJeu = 0;

  public Engine() {
  }

  @Override
  public void bindDataService(DataService service) {
    data = service; // hellooo

  }

  @Override
  public void init() {
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
  public void start() {
    engineClock.schedule(new TimerTask() {
      boolean rep = true;

      public void run() {

        // System.out.println(score);
        // System.out.println("Game step #"+data.getStepNumber()+": checked.");

        cpt++;
        if (cpt % 100 == 0)
          spawnPhantom();
        if (data.getFruits().size() < 4 && cpt % 100 == 0)
          spawnFruit();

        updateSpeedHeroes();
        updateCommandHeroes();
        updatePositionHeroes();
        if (rep) {
          try {
            Thread.sleep(1000);
            rep = false;
          } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
          }
        }

        ArrayList<PhantomService> phantomsR = new ArrayList<PhantomService>();
        ArrayList<PhantomService> phantomsL = new ArrayList<PhantomService>();

        data.setSoundEffect(Sound.SOUND.None);
        int score = 0;

        for (PhantomService p : data.getPhantoms()) {
          moveLeft(p, score);

          if (collisionHeroesPhantom(p)) {
            data.setSoundEffect(Sound.SOUND.HeroesGotHit);
            gameon = false;
            stop();
          } else {

            if (p.getPosition().x > 0 && p.getPosition().x < HardCodedParameters.maxX)
              phantomsR.add(p);
          }
        }
        for (PhantomService p : data.getPhantoms2()) {
          moveRight(p);

          if (collisionHeroesPhantom(p)) {
            data.setSoundEffect(Sound.SOUND.HeroesGotHit);
            gameon = false;
            stop();
          } else {

            if (p.getPosition().x < HardCodedParameters.maxX - 50 && p.getPosition().x > 0)
              phantomsL.add(p);
          }
        }

        ArrayList<FruitService> fruits = new ArrayList<FruitService>();
        

        data.setSoundEffect(Sound.SOUND.None);

        for (FruitService f : data.getFruits()) {

          if (collisionHeroesFruit(f)) {
            data.setSoundEffect(Sound.SOUND.HeroesGotHit);
            score++;
          } else {
            fruits.add(f);
          }
        }
        
        vitesseJeu += score;
        data.addScore(score);

        data.setFruits(fruits);

        data.setStepNumber(data.getStepNumber() + 1);
      }
    }, 0, HardCodedParameters.enginePaceMillis);
  }

  @Override
  public void stop() {
    engineClock.cancel();
  }

  @Override
  public void setHeroesCommand(User.COMMAND c) {
    if (c == User.COMMAND.LEFT)
      moveLeft = true;
    if (c == User.COMMAND.RIGHT)
      moveRight = true;
    if (c == User.COMMAND.UP)
      moveUp = true;
    if (c == User.COMMAND.DOWN)
      moveDown = true;
  }

  @Override
  public void releaseHeroesCommand(User.COMMAND c) {
    if (c == User.COMMAND.LEFT)
      moveLeft = false;
    if (c == User.COMMAND.RIGHT)
      moveRight = false;
    if (c == User.COMMAND.UP)
      moveUp = false;
    if (c == User.COMMAND.DOWN)
      moveDown = false;
  }

  private void updateSpeedHeroes() {
    heroesVX *= friction;
    heroesVY *= friction;
  }

  private void updateCommandHeroes() {

    if (moveLeft)
      heroesVX -= heroesStep;
    if (moveRight)
      heroesVX += heroesStep;
    if (moveUp)
      heroesVY -= heroesStep;
    if (moveDown)
      heroesVY += heroesStep;
  }

  private void updatePositionHeroes() {
    data.setHeroesPosition(new Position(data.getHeroesPosition().x + heroesVX, data.getHeroesPosition().y + heroesVY));

    if (data.getHeroesPosition().x - (HardCodedParameters.heroesWidth / (double) 3) < 0)
      data.setHeroesPosition(new Position((int) HardCodedParameters.heroesWidth / 3, data.getHeroesPosition().y));
    if (data.getHeroesPosition().x + (HardCodedParameters.heroesWidth / (double) 2) > HardCodedParameters.maxX+120)
      data.setHeroesPosition(new Position((int) HardCodedParameters.maxX +120- (int) HardCodedParameters.heroesWidth / (double) 2,data.getHeroesPosition().y));
    if (data.getHeroesPosition().y - (HardCodedParameters.heroesHeight / (double) 3) < 0)
      data.setHeroesPosition(new Position(data.getHeroesPosition().x, (int) HardCodedParameters.heroesHeight / (double) 3));
    if (data.getHeroesPosition().y + (HardCodedParameters.heroesHeight / (double) 3) > HardCodedParameters.maxY * .8)
      data.setHeroesPosition(new Position(data.getHeroesPosition().x,(int) HardCodedParameters.maxY * .8 - (int) HardCodedParameters.heroesHeight / (double) 3));

  }

  private void spawnPhantom() {
    int x = 0;
    int y = 0;
    boolean cont = true;
    while (cont) {
      y = (int) (gen.nextInt((int) (HardCodedParameters.defaultHeight * .6)) + HardCodedParameters.defaultHeight * .1);
      x = HardCodedParameters.defaultWidth;

      cont = false;
      for (PhantomService p : data.getPhantoms()) {
        if (p.getPosition().equals(new Position(x, y)))
          cont = true;
      }
    }
    data.addPhantom(new Position(x, y));

    cont = true;
    while (cont) {
      y = (int) (gen.nextInt((int) (HardCodedParameters.defaultHeight * .6)) + HardCodedParameters.defaultHeight * .1);
      x = HardCodedParameters.minX;

      cont = false;
      for (PhantomService p : data.getPhantoms2()) {
        if (p.getPosition().equals(new Position(x, y)))
          cont = true;
      }
    }
    data.addPhantom2(new Position(x, y));

  }

  private void spawnFruit() {
    int x = 0;
    int y = 0;
    boolean cont = true;
    while (cont) {
      y = (int) (gen.nextInt((int) (HardCodedParameters.defaultHeight * .6)) + HardCodedParameters.defaultHeight * .1);
      x = (int) (gen.nextInt((int) (HardCodedParameters.defaultWidth * .6)) + HardCodedParameters.defaultWidth * .1);

      cont = false;
      for (FruitService p : data.getFruits()) {
        if (p.getPosition().equals(new Position(x, y)))
          cont = true;
      }
    }
    data.addFruit(new Position(x, y));
  }

  private void moveLeft(PhantomService p, int score) {
    p.setPosition(new Position(p.getPosition().x - (phantomStep + (vitesseJeu * .2)), p.getPosition().y));
  }

  private void moveRight(PhantomService p) {
    p.setPosition(new Position(p.getPosition().x + (phantomStep + (vitesseJeu * .2)), p.getPosition().y));

  }


  private boolean collisionHeroesFruit(FruitService p) {
    return (((data.getHeroesPosition().x - p.getPosition().x + 17)
        * (data.getHeroesPosition().x - p.getPosition().x + 17)
        + (data.getHeroesPosition().y - p.getPosition().y + 15)
            * (data.getHeroesPosition().y - p.getPosition().y + 15)) < (data.getHeroesWidth() * .1
                + data.getFruitWidth()) * (data.getHeroesWidth() * .1 + data.getFruitWidth()));

  }

  private boolean collisionHeroesPhantom(PhantomService p) {
    return (((data.getHeroesPosition().x - p.getPosition().x - 30)
        * (data.getHeroesPosition().x - p.getPosition().x - 30)
        + (data.getHeroesPosition().y - p.getPosition().y - 60) * (data.getHeroesPosition().y - p.getPosition().y)
        - 60) < (data.getHeroesWidth() * .25 + data.getPhantomWidth()) * (data.getHeroesWidth() * .25 + data.getPhantomWidth()));

  }

  private boolean collisionHeroesPhantom() {
    for (PhantomService p : data.getPhantoms())
      if (collisionHeroesPhantom(p))
        return true;
    return false;
  }

  private boolean collisionHeroesFruits() {
    for (FruitService p : data.getFruits())
      if (collisionHeroesFruit(p))
        return true;
    return false;
  }

  public boolean gameON() {
    return gameon;
  }
}
