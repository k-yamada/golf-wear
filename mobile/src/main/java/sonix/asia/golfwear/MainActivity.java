package sonix.asia.golfwear;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, MessageApi.MessageListener{
  private static final String TAG = MainActivity.class.getName();
  private final String[] SEND_MESSAGES = {"/Action/NONE", "/Action/PUNCH", "/Action/UPPER", "/Action/HOOK"};

  private GoogleApiClient mGoogleApiClient;
  //private MySurfaceVeiw mSurfaceView;
  private SoundPool mSoundPool;
  private int mSE1, mSE2, mSE3;
  private ClearGLSurfaceView _surfaceView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    _surfaceView = new ClearGLSurfaceView(this);
    setContentView(_surfaceView);

    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
          @Override
          public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.d(TAG, "onConnectionFailed:" + connectionResult.toString());
          }
        })
        .addApi(Wearable.API)
        .build();

//        Button punch = (Button)findViewById(R.id.button_p);
//        punch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSurfaceView.punch();
//                mSoundPool.play(mSE1,1.0f, 1.0f, 0, 0, 1.0f);
//            }
//        });
//
//        Button upper = (Button)findViewById(R.id.button_u);
//        upper.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSurfaceView.upper();
//                mSoundPool.play(mSE2, 1.0f, 1.0f, 0, 0, 1.0f);
//            }
//        });
//
//        Button hook = (Button)findViewById(R.id.button_h);
//        hook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSurfaceView.hook();
//                mSoundPool.play(mSE3, 1.0f, 1.0f, 0, 0, 1.0f);
//            }
//        });
  }

  @Override
  protected void onStart() {
    super.onStart();
    mGoogleApiClient.connect();
    mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
//    mSE1 = mSoundPool.load(this, R.raw.se1, 1);
//    mSE2 = mSoundPool.load(this, R.raw.se2, 1);
//    mSE3 = mSoundPool.load(this, R.raw.se3, 1);
  }

  @Override
  protected void onPause() {
    super.onPause();
    _surfaceView.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
    _surfaceView.onResume();
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
      Wearable.MessageApi.removeListener(mGoogleApiClient, this);
      mGoogleApiClient.disconnect();
    }
    mSoundPool.release();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onConnected(Bundle bundle) {
    Log.d(TAG, "onConnected");
    Wearable.MessageApi.addListener(mGoogleApiClient, this);
  }

  @Override
  public void onConnectionSuspended(int i) {
    Log.d(TAG, "onConnectionSuspended");

  }

  @Override
  public void onMessageReceived(MessageEvent messageEvent) {
    Log.d(TAG, "onMessageReceived : " + messageEvent.getPath());

    String msg = messageEvent.getPath();
    if (SEND_MESSAGES[1].equals(msg)) {
      //mSurfaceView.punch();
      _surfaceView.moveBall();
      mSoundPool.play(mSE1,1.0f, 1.0f, 0, 0, 1.0f);
    } else if (SEND_MESSAGES[2].equals(msg)) {
      //mSurfaceView.upper();
      _surfaceView.moveBall();
      mSoundPool.play(mSE2, 1.0f, 1.0f, 0, 0, 1.0f);
    } else if (SEND_MESSAGES[3].equals(msg)) {
      //mSurfaceView.hook();
      _surfaceView.moveBall();
      mSoundPool.play(mSE3, 1.0f, 1.0f, 0, 0, 1.0f);
    }
  }

}
