package sonix.asia.golfwear;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class ClearGLSurfaceView extends GLSurfaceView {
  ClearRenderer _refRenderer;
  public ClearGLSurfaceView(Context context) {
    super(context);
    _refRenderer = new ClearRenderer(context);
    setRenderer(_refRenderer);
  }

  public boolean onTouchEvent(final MotionEvent event) {
    _refRenderer.setSize(this.getWidth(),this.getHeight());
    queueEvent(new Runnable(){
      public void run() {
        _refRenderer.getPhysicsWorld().moveBall();
        //_refRenderer.touchEvent(event.getX(), event.getY(), event.getAction());
      }
    });
    try {
      Thread.sleep(20);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return true;
  }

  public void moveBall() {
    _refRenderer.getPhysicsWorld().moveBall();
  }
}
