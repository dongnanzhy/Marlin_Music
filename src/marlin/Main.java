package marlin;

import marlin.graphicsLib.Window;
import marlin.sandbox.Paint;

public class Main {
  public static void main(String[] args) {
    Window.PANEL = new Paint();
    Window.launch();
  }
}
