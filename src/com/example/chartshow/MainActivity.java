package com.example.chartshow;

import java.util.LinkedList;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.chartshow.util.ChartPagerBean;
import com.example.chartshow.util.ChartView;

public class MainActivity extends Activity {
	private ChartView mChartView;
	private ChartPagerBean mBean;
	private LinkedList<Integer> mList;
	private Random random = new Random();
	private Handler handler = new Handler();
	private Runnable runnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow();
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);
		mList = new LinkedList<Integer>();
		mBean = new ChartPagerBean("温度");
		mBean.isHasSlave = false;
		mBean.majorMax = 500;
		mBean.majorMin = 50;
		mBean.majorWarningMax = 400;
		mBean.majorWarningMin = 100;
		mBean.majorValueList = mList;
		for (int i = 0; i < 8; i++) {
			mList.add(i + random.nextInt(8) * 100);
		}
		mChartView = new ChartView();
		runnable = new Runnable() {

			@Override
			public void run() {
				mList.poll();
				mList.add(random.nextInt(8) * 100);
				mChartView.chartView(MainActivity.this, mBean,
						(LinearLayout) findViewById(R.id.chart_show), 8);
				handler.postDelayed(runnable, 1000);
			}
		};
		handler.postDelayed(runnable, 1000);
	}
}
