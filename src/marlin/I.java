package marlin;

import java.awt.*;

// holds all the interfaces
public interface I{
  public interface Draw{ public void draw(Graphics g);}
  public interface Hit{ public boolean hit(int x, int y);}

  public interface Act{public void act();}
  public interface React extends Act{public int bid(Stroke s);}

  public interface Show{public void show(Graphics g);}

  public interface Area{
    public boolean hit(int x, int y);
    public void pressed(int x, int y);
    public void dragged(int x, int y);
    public void released(int x, int y);
  }
}