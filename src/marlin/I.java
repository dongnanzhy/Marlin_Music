package marlin;

import marlin.reaction.Gesture;

import java.awt.*;

// holds all the interfaces
public interface I{
  public interface Draw{ public void draw(Graphics g);}
  public interface Hit{ public boolean hit(int x, int y);}

//  public interface Act{public void act();}
//  public interface React extends Act{public int bid(Stroke s);}

  public interface Show{public void show(Graphics g);}

  public interface Area{
    public boolean hit(int x, int y);
    public void dn(int x, int y);
    public void drag(int x, int y);
    public void up(int x, int y);
  }

  // what you do if you react to some gesture
  public interface Act{public void act(Gesture g);}
  // how badly you want to so something
  public interface React extends Act{public int bid(Gesture g);}
}