package marlin.reaction;

import marlin.I;

import marlin.graphicsLib.G;
import marlin.sandbox.Ink;
import marlin.sandbox.Shape;

import java.util.ArrayList;

public class Gesture{
  public Shape shape;
  public G.VS vs;

  private static List UNDO = new List();

  private Gesture(Shape shape, G.VS vs){this.shape = shape; this.vs = vs;}

  public static Gesture getNew(Ink ink){ // can return null
    Shape s = Shape.recognize(ink);
    return (s==null) ? null : new Gesture(s,ink.vs);
  }


  private void doGesture(){
    Reaction r = Reaction.best(this);
    if(r != null){ r.act(this);}
  }
  private void undoableGesture(){
    Reaction r = Reaction.best(this);
    if(r != null){ UNDO.add(this); r.act(this);}
  }
  // undo function
  public static void undo(){
    if(UNDO.size() > 0){
      UNDO.remove(UNDO.size()-1); // remove last element
      Layer.nuke();  // eliminates all the masses
      Reaction.nuke();  // clears byShape then reloads initial reactions
      UNDO.redo();
    }
  }

  // --- List of Gesture (used in undo) ---
  public static class List extends ArrayList<Gesture>{
    private void redo(){ for(Gesture g : this){g.doGesture();}}
  };

  //----  A Gesture Area (anonymous class) ------
  public static I.Area AREA = new I.Area(){
    public boolean hit(int x, int y){ return true; }
    public void dn(int x, int y){Ink.BUFFER.dn(x,y);}
    public void drag(int x, int y){Ink.BUFFER.drag(x,y);}
    public void up(int x, int y){
      Ink.BUFFER.add(x,y);
      Ink ink = new Ink();
      Gesture g = Gesture.getNew(ink); // this can fail - s can be null if unrecognized
      Ink.BUFFER.clear();
      if(g != null){
        if(g.shape.name.equals("N-N")){ // hardwired UNDO
          undo();
        }else {
          g.undoableGesture();
        }
      }
    }
  } ;

}