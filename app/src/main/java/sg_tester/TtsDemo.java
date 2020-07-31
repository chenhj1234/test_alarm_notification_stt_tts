package sg_tester;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hkl.myapplication.R;

public class TtsDemo extends Activity {
  static Button speakButton;
  private TextToSpeech mSynthesizer;
  Toast mToast;
  Context mContext = null;
  private final String TAG = this.getClass().getSimpleName();

  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case 1:
          mToast.setText((String) msg.obj);
          mToast.show();
          break;
      }
    }
  };

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.ttsdemo);
    mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

    mSynthesizer =
            new TextToSpeech(this, new TextToSpeech.OnInitListener() {
              @Override
              public void onInit(int status) {
                if (status!= TextToSpeech.SUCCESS) {
                  mSynthesizer = null;
                  mHandler.sendMessage(mHandler.obtainMessage(1, "無法語音合成\n請確認已安裝語音合成服務"));
                }
              }
            });
    speakButton = (Button) findViewById(R.id.speakButton);
    mContext = this;
    final Runnable r = new Runnable(){
      @Override
      public void run() {
        for(int i = 0; i < 30; i++) {
          Log.e(TAG, "isSpeaking:" + (mSynthesizer.isSpeaking() ? "true" : "false"));
          //if(mSynthesizer.isSpeaking() == false) break;
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    };
    speakButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (null == mSynthesizer) return;
        mHandler.sendMessage(mHandler.obtainMessage(1, "發聲中"));
        mSynthesizer.speak("這是一個極其簡單的測試", TextToSpeech.QUEUE_FLUSH, null);
        //
        // To test Pico TTS engine:
        //
        //mSynthesizer.setLanguage(Locale.UK);
        //mSynthesizer.speak("This is a really simple example.", TextToSpeech.QUEUE_FLUSH, null);
        Thread t = new Thread(r);
        t.start();
      }
    });
  }

}
