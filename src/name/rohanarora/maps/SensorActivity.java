package name.rohanarora.maps;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class SensorActivity extends Activity implements SensorEventListener {
  /** Called when the activity is first created. */
  public static final String TAG = "Sensor Activity";

  private File mRoot, mDir;
  private File mFileAccelerometer;
  // private File mFileGyroscope;

  private static final String FILENAME_ACCELEROMETER = "accelerometer.csv";
  // private static final String FILENAME_GYROSCOPE = "gyroscope.csv";

  private SensorManager mSensorManager;
  private Sensor mAccelerometer;

  // private Sensor mGyroscope;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");
    setContentView(R.layout.activity_sensor);

    // create an instance of sensor service
    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    // enable/disable features based on sensor availability
    if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null
	&& mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
      // There's a accelerometer and gyroscope
      // If not display a dialog and quit
      mAccelerometer = mSensorManager
	.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
      // mGyroscope =
      // mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
	}

    mRoot = Environment.getExternalStorageDirectory();
    mDir = new File(mRoot + "/Rithmio");
    prepareStorage();
    mFileAccelerometer = new File(mDir, FILENAME_ACCELEROMETER);
    // mFileGyroscope = new File(mDir, FILENAME_GYROSCOPE);
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
      Log.d(TAG,
	  "Accelerometer" + " " + String.valueOf(event.timestamp)
	  + " " + String.valueOf(event.values[0]) + " "
	  + String.valueOf(event.values[1])
	  + String.valueOf(event.values[2]));
      writeToFile(String.valueOf(event.timestamp) + ","
	  + String.valueOf(event.values[0]) + ","
	  + String.valueOf(event.values[1]) + ","
	  + String.valueOf(event.values[2]));

    } // else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
    // Log.d(TAG, "Gyroscope" + " " + String.valueOf(event.timestamp) +
    // " "
    // + String.valueOf(event.values[0])
    // + " " + String.valueOf(event.values[1]) +
    // String.valueOf(event.values[2]));
    // }

  }

  public void writeToFile(String data) {
    try {
      FileWriter f = new FileWriter(mFileAccelerometer, true);
      f.write(data);
      f.write("\n");
      f.flush();
      f.close();
      Log.d(TAG, "Write successful ");
    } catch (IOException e) {
      Log.e(TAG, "Write failure " + e.toString());

    }

  }

  public String prepareStorage() {
    try {
      Log.d(TAG, "Creating: " + mDir.getAbsolutePath());
      mDir.mkdirs(); // ignore return value, they may already exist
      mFileAccelerometer.delete(); // may or may not already exist
      // StatFs stat = new StatFs(mRoot.getPath());
      // if (stat.getAvailableBlocks() == 0) {
      // return "SD Card Storage is full";
      // }
      return "";
    } catch (Exception e) {
      Log.e(TAG, "Storage Error: ", e);
      return "Storage Error:" + e.getMessage();
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    // TODO Auto-generated method stub

  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.d(TAG, "onResume");
    // register sensor listeners
    mSensorManager.registerListener(this, mAccelerometer,
	SensorManager.SENSOR_DELAY_GAME);
    // mSensorManager.registerListener(this, mGyroscope,
    // SensorManager.SENSOR_DELAY_GAME);
    // TODO Auto-generated method stub

  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d(TAG, "onPause");
    mSensorManager.unregisterListener(this);
    // unregister sensor listeners
    // TODO Auto-generated method stub

  }
}
