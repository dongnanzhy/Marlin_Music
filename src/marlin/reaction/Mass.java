package marlin.reaction;

import marlin.I;

/*
abstract class
Mass is a type of obj that lives in a layer
Mass两个用途
  1.Output用途：implements I.show，可以show自己
  2.Input用途：list of Reactions, 用来根据Gesture找到对应的Mass

注意：这里Mass是abstract，定义比较简单
     子类必须完成两个功能，1.show() 2.加入Reaction到这个Mass里
     e.g.：SimpleReaction.java中的Box类
 */
public abstract class Mass extends Reaction.List implements I.Show{
  public Layer layer;
  public Mass(String layerName){
    this.layer = Layer.byName.get(layerName);
    if(layer!=null){layer.add(this);} else {System.out.println("BAD LAYER NAME-" + layerName);}
  }
  public void delete(){
    clearAll();
    // remove self from layers.
    layer.remove(this);
  }
}
