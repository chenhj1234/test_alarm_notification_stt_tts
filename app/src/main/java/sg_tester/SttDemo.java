package sg_tester;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hkl.myapplication.R;

import java.util.ArrayList;

public class SttDemo extends Activity {
  private static String TAG = SttDemo.class.getSimpleName();
  public static int listenMode = 0;
  static Button listenButton;
  static TextView textView1;
  android.speech.SpeechRecognizer mRecognizer;
  Toast mToast;

  public final static String listeningText[] = {"聽我說", "停止錄音"};

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.sttdemo);

    mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    textView1 = (TextView) findViewById(R.id.textView1);
    textView1.setMovementMethod(new ScrollingMovementMethod());

    listenButton = (Button) findViewById(R.id.listenButton);
    listenButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listenMode = 1 - listenMode;
        stop();
        if (0 == listenMode) {
          setListenButton(listeningText[listenMode]);
          mRecognizer = null;
        }
        else {
          if (!android.speech.SpeechRecognizer.isRecognitionAvailable(SttDemo.this)) {
            showTip("無法語音辨識\n請確認已安裝語音辨識服務");
            listenMode = 0;
            setListenButton(listeningText[listenMode]);
            return;
          }

          mRecognizer =
                  android.speech.SpeechRecognizer.createSpeechRecognizer(SttDemo.this);
          mRecognizer.setRecognitionListener(mSpeechListener);
          Intent intent =
                  new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
          //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
          //intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
          intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
          mRecognizer.startListening(intent);
        }
      }
    });
  }

  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case 1:
          mToast.setText((String) msg.obj);
          mToast.show();
          break;
        case 2:
          listenButton.setText((String) msg.obj);
          break;
        case 3:
          textView1.setText((String) msg.obj);
      }
    }
  };

  void showTip(String s) {
    mHandler.sendMessage(mHandler.obtainMessage(1, s));
  }

  void setListenButton(String s) {
    mHandler.sendMessage(mHandler.obtainMessage(2, s));
  }

  void setTextView(String s) {
    mHandler.sendMessage(mHandler.obtainMessage(3, s));
  }

  private android.speech.RecognitionListener mSpeechListener =
          new android.speech.RecognitionListener() {

            public void onBeginningOfSpeech() {
            }

            public void onReadyForSpeech(Bundle params) {
              setListenButton(listeningText[listenMode]);
              setTextView("");
              showTip("請開始說話");
            }

            public void onRmsChanged(float rmsdB) {
            }

            public void onBufferReceived(byte[] buffer) {
            }

            public void onEndOfSpeech() {
              showTip("結束說話");
            }

            public void onError(int error) {
              showTip("onError Code:" + error);
              listenMode = 0;
              setListenButton(listeningText[listenMode]);
              stop();
            }

            public void onResults(Bundle results) {
              onPartialResults(results);
              listenMode = 0;
              setListenButton(listeningText[listenMode]);
              stop();
              showTip("成功辨識完畢");
            }

            public void onPartialResults(Bundle results) {
              StringBuffer ret = new StringBuffer();
              ArrayList data =
                      results.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION);
              for (int i = 0; i < data.size(); ++i)
                ret.append(data.get(i) + "\n");
              setTextView(ret.toString());
            }

            public void onEvent(int eventType, Bundle params) {
            }
          };

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (null != mRecognizer) {
      stop();
      mRecognizer = null;
    }
  }

  public void stop() {
    if (null == mRecognizer) return;
    try {
      mRecognizer.cancel();
      mRecognizer.destroy();
    }
    catch (Exception e) {
    }
  }
}
