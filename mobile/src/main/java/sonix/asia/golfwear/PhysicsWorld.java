package sonix.asia.golfwear;


import android.util.Log;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

public class PhysicsWorld {
  final private int MAXBOX = 20;
  final private float FRAMERATE = 30f;
  private float _timeStep = (1f / FRAMERATE);
  private int _iterations = 5;
  private int _count = 0;

  private AABB _box2dWorld;
  private World _myWorld;
  private Body _ball;
  private static final String TAG = MainActivity.class.getName();

  public void createWorld() {
    _box2dWorld = new AABB();
    _box2dWorld.lowerBound.set(new Vec2(-100f, -100f));
    _box2dWorld.upperBound.set(new Vec2(100f, 100f));

    Vec2 gravity = new Vec2(0f, -10f);
    boolean doSleep = false;
    _myWorld = new World(_box2dWorld, gravity, doSleep);
  }

  public void setGrav(float x, float y) {
    _myWorld.setGravity(new Vec2(x,y));
  }

  public void addBox(float x, float y, float xr, float yr, float angle, boolean isBall) {
    if (_count < (MAXBOX-1)) {
      BodyDef _groundBody;
      _groundBody = new BodyDef();
      _groundBody.position.set(new Vec2(x, y));
      _groundBody.angle = angle;
      Body groundBody = _myWorld.createBody(_groundBody);

      PolygonDef _groundShape;
      _groundShape = new PolygonDef();
      _groundShape.setAsBox(xr, yr);
      _groundShape.density = 1.0f;
      _groundShape.restitution = 0.5f; // 反発係数

      groundBody.createShape(_groundShape);
      if (isBall) {
        Log.d(TAG, "setBall===========");
        groundBody.setMassFromShapes();
        //groundBody.applyForce(new Vec2(10000f, 250f), groundBody.getPosition());
        if (_ball == null) {
          _ball = groundBody;
        }
      }
      if (isBall) {
         _count++;
      }
    }
  }

  public void addBall(float x, float y, float angle) {
    BodyDef _groundBody;
    _groundBody = new BodyDef();
    _groundBody.position.set(new Vec2(x, y));
    _groundBody.angle = angle;
    Body groundBody = _myWorld.createBody(_groundBody);

    PolygonDef _groundShape;
    _groundShape = new PolygonDef();
    _groundShape.setAsBox(.5f, .5f);
    _groundShape.density = 1.0f;
    _groundShape.restitution = 0.5f; // 反発係数

    groundBody.createShape(_groundShape);
    groundBody.setMassFromShapes();
    _ball = groundBody;
  }

  public void moveBall() {
    Log.d(TAG, "moveBall");
    // 物体に力を与える

    _ball.applyForce(new Vec2(1000f, 420f), _ball.getPosition());
    //    b2Vec2(0,50), // (0,0)から(0,50)へ上向きに垂直の力を作成
    //    b->GetWorldCenter() // 起点を物体の中央にとる
  }

  public void update() {
    _myWorld.step(_timeStep, _iterations);
  }

  public int getCount() {
    return _count;
  }

  public Body getBodyList() {
    return _myWorld.getBodyList();
  }
}
