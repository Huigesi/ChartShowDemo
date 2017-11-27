package lenovo.agriculture.chartshow.activity;

import java.util.LinkedList;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import lenovo.agriculture.chartshow.R;
import lenovo.agriculture.chartshow.util.ChartPagerBean;
import lenovo.agriculture.chartshow.util.ChartView;
import lenovo.agriculture.chartshow.util.JsonTool;
import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * App运行控制主类
 * 
 * @author asus
 *
 */
public class MainActivity extends Activity {
	private ChartView mChartView;
	private ChartPagerBean mBean;
	private LinkedList<Integer> mList;
	private Random mRandom = new Random();
	private Handler mHandler = new Handler();
	private Runnable mRunnable;
	private Thread mThread;
	private String result="";
	private int pm=0;
	private String path="http://192.168.1.243:8080/transportservice/type/jason/action/GetAllSense.do";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
		
		initData();
		mChartView = new ChartView();
		initpm();
		setRunnable();
		mHandler.postDelayed(mRunnable, 50);
	}

	private void initpm() {
		mThread=new Thread(){
		@Override
		public void run() {
			result=JsonTool.sendMessage(path, "{}");
			try {
				JSONObject jsonObject =new JSONObject(result);
				String jString=jsonObject.getString("serverinfo");
				JSONObject jObject=new JSONObject(jString);
				
				pm=jObject.getInt("pm2.5");
				mList.add(pm);
				Log.i("dd",pm+"");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			super.run();
		}	
		};
		
	}
	/**
	 * 设置Runnable对象
	 * 
	 */
	private void setRunnable() {
		mRunnable = new Runnable() {
			@Override
			public void run() {
				if (mList.size() <= 8) {
					//mList.add(pm);
					mChartView.draw(MainActivity.this, mBean,
							(LinearLayout) findViewById(R.id.chart_show), 6);
					mHandler.postDelayed(mRunnable, 1500);
				} else {
					mList.poll();
					//mList.add(pm);
					mChartView.draw(MainActivity.this, mBean,
							(LinearLayout) findViewById(R.id.chart_show), 6);
					mHandler.postDelayed(mRunnable, 1500);
				}
			}
		};
	}

	/**
	 * 初始化mBean
	 * 
	 */
	private void initData() {
		mList = new LinkedList<Integer>();
		mBean = new ChartPagerBean(getString(R.string.temp));
		mBean.isHasSlave = false;
		mBean.majorMax = 800;
		mBean.majorMin = 50;
		mBean.majorWarningMax = 500;
		mBean.majorWarningMin = 100;
		mBean.majorValueList = mList;
	}
}
