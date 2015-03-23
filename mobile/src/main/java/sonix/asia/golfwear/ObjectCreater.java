package sonix.asia.golfwear;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class ObjectCreater {
  private FloatBuffer _vertexBuffer;
  private ShortBuffer _indexBuffer;
  private FloatBuffer _texBuffer;
  private int _vertexCount = 0;
  private boolean _hasTexture = false;
  private int[] _texture = new int[1];

  public ObjectCreater(float[] coords, float[] tcoords, short[] icoords, int vertexes) {
      this(coords, icoords, vertexes);
      _texBuffer = makeFloatBuffer(tcoords);
  }

  public ObjectCreater(float[] coords, short[] icoords, int vertexes) {
    _vertexCount = vertexes;
    _vertexBuffer = makeFloatBuffer(coords);
    _indexBuffer = makeShortBuffer(icoords);
  }

  protected static FloatBuffer makeFloatBuffer(float[] arr) {
    ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
    bb.order(ByteOrder.nativeOrder());
    FloatBuffer fb = bb.asFloatBuffer();
    fb.put(arr);
    fb.position(0);
    return fb;
  }

  protected static ShortBuffer makeShortBuffer(short[] arr) {
    ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
    bb.order(ByteOrder.nativeOrder());
    ShortBuffer ib = bb.asShortBuffer();
    ib.put(arr);
    ib.position(0);
    return ib;
  }

  public void loadTexture(GL10 gl, Context mContext, int mTex) {
    _hasTexture = true;
    gl.glGenTextures(1, _texture, 0);
    gl.glBindTexture(GL10.GL_TEXTURE_2D, _texture[0]);

    // Enable blending using premultiplied alpha.
    gl.glEnable(GL10.GL_BLEND);
    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

    Bitmap bitmap;
    bitmap = BitmapFactory.decodeResource(mContext.getResources(), mTex);
    GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
    bitmap.recycle();
    gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
    gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );
  }

  public void draw(GL10 gl) {
    if (_hasTexture) {
      gl.glEnable(GL10.GL_TEXTURE_2D);
      gl.glBindTexture(GL10.GL_TEXTURE_2D, _texture[0]);
      gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, _texBuffer);
    } else {
      gl.glDisable(GL10.GL_TEXTURE_2D);
    }
    gl.glFrontFace(GL10.GL_CCW);
    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBuffer);
    gl.glDrawElements(GL10.GL_TRIANGLE_FAN, _vertexCount, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
    gl.glDisable(GL10.GL_TEXTURE_2D);
  }

  public void draw(GL10 gl, float x, float y, float z, float rot, float scale) {
    this.draw(gl, x, y, z, rot, scale, scale);
  }

  public void draw(GL10 gl, float x, float y, float z, float rot, float scaleX, float scaleY) {
    gl.glPushMatrix();
    gl.glTranslatef(x, y, z);
    gl.glRotatef(rot, 0f, 0f, 1f);
    gl.glScalef(scaleX, scaleY, 1f);
    this.draw(gl);
    gl.glPopMatrix();
  }

}
