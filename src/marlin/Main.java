package marlin;

import marlin.graphicsLib.Window;
import marlin.reaction.SimpleReaction;
import marlin.sandbox.ShapeTrainer;

public class Main {
  public static void main(String[] args) {
    // deprecated
    // Window.PANEL = new Paint();
    // Window.PANEL = new Squares();

    // PainInk test
    // Window.PANEL = new PaintInk();

    // Shape trainer test
    //Window.PANEL = new ShapeTrainer();

    // Reaction test
    Window.PANEL = new SimpleReaction();

    Window.launch();
  }
}
