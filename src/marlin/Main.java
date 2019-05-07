package marlin;

import marlin.graphicsLib.Window;
import marlin.sandbox.Paint;
import marlin.sandbox.PaintInk;
import marlin.sandbox.Squares;

public class Main {
  public static void main(String[] args) {
    // Window.PANEL = new Paint();
    // Window.PANEL = new Squares();
    Window.PANEL = new PaintInk();
    Window.launch();
  }
}
