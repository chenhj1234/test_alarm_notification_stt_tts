package sg_tester;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.hkl.myapplication.R;

public class MainActivity extends Activity implements OnClickListener {
  @SuppressLint("ShowToast")
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.main);
    ((ListView) findViewById(R.id.listview_main)).setAdapter(new SimpleAdapter());
  }

  @Override
  public void onClick(View view) {
    int tag = Integer.parseInt(view.getTag().toString());
    Intent intent = null;
    switch (tag) {
      case 0:
        // 語音辨識
        intent = new Intent(this, SttDemo.class);
        break;
      case 1:
        // 語音合成
        intent = new Intent(this, TtsDemo.class);
        break;
      default:
        intent = new Intent(this, SttDemo.class);
        break;
    }
    if (intent != null) {
      startActivity(intent);
    }
  }


  //Menu 列表
  String items[] = {"語音辨識", "語音合成"};

  private class SimpleAdapter extends BaseAdapter {
    public View getView(int position, View convertView, ViewGroup parent) {
      if (null == convertView) convertView =
                                       LayoutInflater.from(MainActivity.this).inflate(R.layout.list_items, null);
      Button btn = (Button) convertView.findViewById(R.id.btn);
      btn.setOnClickListener(MainActivity.this);
      btn.setTag(position);
      btn.setText(items[position]);
      return convertView;
    }

    @Override
    public int getCount() {
      return items.length;
    }

    @Override
    public Object getItem(int position) {
      return null;
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }
  }

}
