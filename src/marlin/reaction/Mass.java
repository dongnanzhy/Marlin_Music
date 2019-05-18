package marlin.reaction;

import marlin.I;

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
