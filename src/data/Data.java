/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: data/Data.java 2015-03-11 buixuan.
 * ******************************************************/
package data;

import tools.HardCodedParameters;
import tools.Position;
import tools.Sound;

import specifications.DataService;
import specifications.FruitService;

import data.ia.Fruit;

import java.util.ArrayList;

public class Data implements DataService{
  //private Heroes hercules;
  private Position heroesPosition;
  private int stepNumber, score;
  private ArrayList<FruitService> fruits;
  private double heroesWidth,heroesHeight,fruitWidth,fruitHeight;
  private Sound.SOUND sound;
  double minX,maxX,minY,maxY;


  

  public Data(){}

  @Override
  public void init(){
    //hercules = new Heroes;
    heroesPosition = new Position(HardCodedParameters.heroesStartX,HardCodedParameters.heroesStartY);
    fruits = new ArrayList<FruitService>();
    stepNumber = 0;
    score = 0;
    heroesWidth = HardCodedParameters.heroesWidth;
    heroesHeight = HardCodedParameters.heroesHeight;
    fruitWidth = HardCodedParameters.fruitWidth;
    fruitHeight = HardCodedParameters.fruitHeight;
    sound = Sound.SOUND.None;
    minX = HardCodedParameters.minX;
    maxX = HardCodedParameters.maxX;
    minY = HardCodedParameters.minY;
    maxY = HardCodedParameters.maxY;


  }

  @Override
  public Position getHeroesPosition(){ return heroesPosition; }
  
  @Override
  public double getHeroesWidth(){ return heroesWidth; }
  
  @Override
  public double getHeroesHeight(){ return heroesHeight; }
  
  @Override
  public double getFruitWidth(){ return fruitWidth; }
  
  @Override
  public double getFruitHeight(){ return fruitHeight; }

  @Override
  public int getStepNumber(){ return stepNumber; }
  
  @Override
  public int getScore(){ return score; }

  @Override
  public ArrayList<FruitService> getFruits(){ return fruits; }
  
  @Override
  public Sound.SOUND getSoundEffect() { return sound; }

  @Override
  public void setHeroesPosition(Position p) { heroesPosition=p; }
  
  @Override
  public void setStepNumber(int n){ stepNumber=n; }
  
  @Override
  public void addScore(int score){ this.score+=score; }

  @Override
  public void addFruit(Position p) { fruits.add(new Fruit(p)); }
  
  @Override
  public void setFruits(ArrayList<FruitService> fruits) { this.fruits=fruits; }
  
  @Override
  public void setSoundEffect(Sound.SOUND s) { sound=s; }
}
