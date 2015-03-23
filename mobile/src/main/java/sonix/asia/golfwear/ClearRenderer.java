package sonix.asia.golfwear;



import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.MotionEvent;

import org.jbox2d.collision.PolygonShape;
import org.jbox2d.collision.Shape;
import org.jbox2d.collision.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ClearRenderer implements GLSurfaceView.Renderer {
  private PhysicsWorld _physicsWorld;
  private ObjectCreater _refBox;
  private Context _context;
  private int _width;
  private int _height;

  public ClearRenderer(Context newContext)
  {
    _context = newContext;
    _refBox = new ObjectCreater(new float[]{-1,-1,0,1,-1,0,1,1,0,-1,1,0}, new float[]{ 0f,1f, 1f,1f, 1f,0f,0f,0f},new short[]{0,1,2,3,0},5);
    _physicsWorld = new PhysicsWorld();
    _physicsWorld.createWorld();
    // 底面
    _physicsWorld.addBox(0f, -25f, 50f, 10f, 0f, false);

    // 左側面
    _physicsWorld.addBox(-12f, -25f, 0.1f, 50f, 0f, false);

    // 右側面
    _physicsWorld.addBox(13f, -25f, 0.1f, 50f, 0f, false);

    // 上面
    _physicsWorld.addBox(0f, 25f, 50f, 0.1f, 0f, false);

    // add Golf ball
    _physicsWorld.addBox(-2f, 10f, .98f, .98f, 0f, true);
  }

  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    GLU.gluOrtho2D(gl, -12f, 12f, -20f, 20f);
    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
    GL10.GL_REPEAT);
    gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
    GL10.GL_REPEAT);
    _refBox.loadTexture(gl, _context, R.drawable.golfball);
  }

  public void onSurfaceChanged(GL10 gl, int w, int h) {
    gl.glViewport(0, 0, w, h);
  }

  public void onDrawFrame(GL10 gl) {
    gl.glClearColor(0f, 0, 0.5f, 1.0f);
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
    GL10.GL_REPLACE);
    gl.glColor4f(1f, 1f, 1f, 1f);
    Vec2 vec;
    Body _body = _physicsWorld.getBodyList();
    do {
      Shape _shape = _body.getShapeList();
      if (_shape != null) {
        vec = _body.getPosition();
        float rot = _body.getAngle() * 57f;
        if (ShapeType.POLYGON_SHAPE == _shape.getType()) {
          Vec2[] _vertexes = ((PolygonShape)_shape).getVertices();
          _refBox.draw(gl, vec.x, vec.y, 0f, rot, _vertexes[2].x, _vertexes[2].y);
        }
      }
      _body = _body.getNext();
    }
    while (_body != null);
    _physicsWorld.update();
  }

  public void touchEvent(float x, float y, int eventCode) {
    float _worldX = ((x-(this._width/2))*12f)/(this._width/2);
    float _worldY = ((y-(this._height/2))*-20f)/(this._height/2);
    if (eventCode == MotionEvent.ACTION_UP) {
      _physicsWorld.addBox(_worldX, _worldY, .98f, .98f, 0f, true);
    }
  }

  public PhysicsWorld getPhysicsWorld() {
    return _physicsWorld;
  }

  public void addBall(float x, float y) {
    float _worldX = ((x-(this._width/2))*12f)/(this._width/2);
    float _worldY = ((y-(this._height/2))*-20f)/(this._height/2);
    _physicsWorld.addBox(_worldX, _worldY, .98f, .98f, 0f, true);
  }

  public void setSize(int x, int y) {
    this._width = x;
    this._height = y;
  }
}
