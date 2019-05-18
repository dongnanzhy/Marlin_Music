package marlin.reaction;

import marlin.sandbox.Shape;
import marlin.I;
import marlin.UC;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Reaction implements I.React{
  public Shape shape;
  private static Map byShape = new Map();
  public static List initialReactions = new List(); // used by Undo to restart everything.

  public Reaction(String shapeName){
    shape = Shape.DB.get(shapeName);
    if(shape == null){System.out.println("WTF? - Shape.DB don't know about: "+shapeName);}
  }

  public void enable() {
    List list = byShape.getList(shape);
    if(!list.contains(this)){list.add(this);}
  }
  public void disable() {
    List list = byShape.getList(shape);
    list.remove(this);
  }

  // used to reset for UNDO
  public static void nuke(){
    // throw away all the reactions.
    byShape = new Map();
    // enable insures that the reaction is in the byShape Map
    initialReactions.enable();
  }

  public static Reaction best(Gesture g){return byShape.getList(g.shape).loBid(g);}

  // --- List of Reactions class ---
  public static class List extends ArrayList<Reaction> {
    public void addReaction(Reaction r){add(r); r.enable();}
    public void removeReaction(Reaction r){ remove(r); r.disable();}
    public void clearAll(){for(Reaction r : this){r.disable();} this.clear();}

    // enables entire list
    public void enable(){for(Reaction r : this){r.enable();}}

    public Reaction loBid(Gesture g){ // can return null - list is Empty or no one wants to bid.
      Reaction res = null; int bestSoFar = UC.noBid;
      for(Reaction r : this){
        int b = r.bid(g);
        if(b < bestSoFar){bestSoFar = b; res = r;}
      }
      return res;
    }
  }

  // --- Shape to Reactions class ---
  public static class Map extends HashMap<Shape, List> {
    public List getList(Shape s){
      List res = get(s);
      // always succeeds
      if(res == null){res = new List(); put(s,res);}
      return res;
    }
  }
}