package term.rjb.x2l.lessoncheck.zxing.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import term.rjb.x2l.lessoncheck.R;
import term.rjb.x2l.lessoncheck.activity.StudentCheckActivity;
import term.rjb.x2l.lessoncheck.activity.UniversalStudentCheckActivity;
import term.rjb.x2l.lessoncheck.manager.ActivityManager;
import term.rjb.x2l.lessoncheck.presenter.StudentPresenter;
import term.rjb.x2l.lessoncheck.zxing.decode.DecodeThread;


public class ResultActivity extends Activity {

	private ImageView mResultImage;
	private TextView mResultText;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 17:
					Toast.makeText(ResultActivity.this,"签到成功！",Toast.LENGTH_SHORT).show();
					ActivityManager.getAppManager().finishActivity(ResultActivity.this);
					break;
				case 16:
					Toast.makeText(ResultActivity.this,"签到失败！",Toast.LENGTH_SHORT).show();
					ActivityManager.getAppManager().finishActivity(ResultActivity.this);
					break;
				case 18:
					Toast.makeText(ResultActivity.this,"签到失败，口令出错！",Toast.LENGTH_SHORT).show();
					ActivityManager.getAppManager().finishActivity(ResultActivity.this);
					break;
				case 15:
					Toast.makeText(ResultActivity.this,"签到失败，签到时间已过！",Toast.LENGTH_SHORT).show();
					ActivityManager.getAppManager().finishActivity(ResultActivity.this);
					break;
				case 21:
					Toast.makeText(ResultActivity.this,"签到失败，不在有效范围内签到！",Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_result);

		Bundle extras = getIntent().getExtras();

		mResultImage = (ImageView) findViewById(R.id.result_image);
		mResultText = (TextView) findViewById(R.id.result_text);

		if (null != extras) {
			int width = extras.getInt("width");
			int height = extras.getInt("height");

			String classNum =extras.getString("classNum");

			LayoutParams lps = new LayoutParams(width, height);
			lps.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
			lps.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
			lps.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
			
//			mResultImage.setLayoutParams(lps);

			String result = extras.getString("result");
//			mResultText.setText(result);

			System.out.println("classNum -- res" + classNum);

			// 扫到的结果mResultText
			StudentPresenter studentPresenter = new StudentPresenter();
			studentPresenter.setSignBySignNumber(result, classNum,handler);

			Bitmap barcode = null;
			byte[] compressedBitmap = extras.getByteArray(DecodeThread.BARCODE_BITMAP);
			if (compressedBitmap != null) {
				barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
				// Mutable copy:
				barcode = barcode.copy(Bitmap.Config.RGB_565, true);
			}

//			mResultImage.setImageBitmap(barcode);
		}
	}
}
