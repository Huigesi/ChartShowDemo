package lenovo.agriculture.chartshow.util;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.ColoursXYSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

/**
 * 图表绘制类，负责曲线图的呈现
 * 
 * @author asus
 *
 */
public class ChartView {
	/**
	 * 绘制图表方法
	 * 
	 * @param context
	 *            上下文对象
	 * @param chartPagerBean
	 *            图表容器
	 * @param layout
	 *            展示图表的Layout
	 * @param xAxisMax
	 *            最大X轴的值
	 */
	public void draw(Context context, ChartPagerBean chartPagerBean,
			LinearLayout layout, int xAxisMax) {

		// 曲线图表信息对象，最多存储两个曲线图的信息
		ChartPagerBean mChartPagerBean = chartPagerBean;
		// 图表数据集合
		XYMultipleSeriesDataset mDataset;
		// 图表数据渲染器
		XYMultipleSeriesRenderer mRenderer;
		// 图表view
		GraphicalView mChartView;
		// 创建图表数据集合
		mDataset = new XYMultipleSeriesDataset();
		mRenderer = setRenderer();

		int yAxisMin = mChartPagerBean.majorMin;// Y轴的最小值
		int yAxisMax = mChartPagerBean.majorMax;// Y轴的最大值

		// 创建一个数据子集
		XYSeries seriesMajor = new XYSeries(mChartPagerBean.majorName);
		int i = 1;
		// 将数据添加到数据子集中，并动态修改最大值和最小值
		for (int value : mChartPagerBean.majorValueList) {
			seriesMajor.add(i, value);
			if (value < yAxisMin) {
				yAxisMin = value;
			}
			if (value > yAxisMax) {
				yAxisMax = value;
			}
			i++;
		}
		// 将数据子集添加到图表数据集合中
		mDataset.addSeries(seriesMajor);
		ColoursXYSeriesRenderer rendererMajor = setMajorSeriesRenderer(mChartPagerBean);
		// 将曲线渲染对象添加到图表渲染器中
		mRenderer.addSeriesRenderer(rendererMajor);

		// 如果图表中存在两条曲线，则再分别创建一个数据子集和一个曲线渲染对象
		if (mChartPagerBean.isHasSlave) {
			// 创建一个数据子集
			XYSeries seriesSlave = new XYSeries(mChartPagerBean.slaveName);
			i = 1;
			// 将数据添加到数据子集中，并动态修改最大值和最小值
			for (int value : mChartPagerBean.slaveValueList) {
				value = mChartPagerBean.slaveValueList.get(i - 1);
				seriesSlave.add(i, value);
				if (value < yAxisMin) {
					yAxisMin = value;
				}
				if (value > yAxisMax) {
					yAxisMax = value;
				}
				i++;
			}
			// 将数据子集添加到图表数据集合中
			mDataset.addSeries(seriesSlave);
			// ColoursXYSeriesRenderer是在Achartengine开源的项目中自定义的类
			ColoursXYSeriesRenderer rendererSlave = setSlaveSeriesRenderer(mChartPagerBean);
			// 将曲线渲染对象添加到图表渲染器中
			mRenderer.addSeriesRenderer(rendererSlave);
		}

		setMaxMinVallue(xAxisMax, mRenderer, yAxisMin, yAxisMax);

		layout.removeAllViews();
		mChartView = setView(context, mDataset, mRenderer);
		// 将图表view添加图表容器中
		layout.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		// 图表重绘
		mChartView.repaint();

	}

	/**
	 * 设置图表XY轴的最大值和最小值
	 * 
	 * @param xAxisMax
	 *            X轴最大值
	 * @param mRenderer
	 *            渲染器
	 * @param yAxisMin
	 *            Y轴最小值
	 * @param yAxisMax
	 *            Y轴最大值
	 */
	private void setMaxMinVallue(int xAxisMax,
			XYMultipleSeriesRenderer mRenderer, int yAxisMin, int yAxisMax) {
		mRenderer.setYAxisMin(yAxisMin);
		mRenderer.setYAxisMax(yAxisMax);
		mRenderer.setXAxisMin(1);
		mRenderer.setXAxisMax(xAxisMax + 1);
	}

	/**
	 * 创建图表View
	 * 
	 * @param context
	 *            上下文对象
	 * @param mDataset
	 *            数据集
	 * @param mRenderer
	 *            渲染器
	 * @return 图表视图
	 */
	private GraphicalView setView(Context context,
			XYMultipleSeriesDataset mDataset, XYMultipleSeriesRenderer mRenderer) {
		GraphicalView mChartView;
		mChartView = ChartFactory
				.getLineChartView(context, mDataset, mRenderer);
		mRenderer.setApplyBackgroundColor(true);// 开启背景颜色
		mRenderer.setBackgroundColor(Color.TRANSPARENT);// 设置背景透明
		mRenderer.setMarginsColor(Color.TRANSPARENT);// 设置边缘透明
		mRenderer.setSelectableBuffer(10);
		return mChartView;
	}

	/**
	 * 创建次曲线渲染对象
	 * 
	 * @param mChartPagerBean
	 *            图表容器
	 * @return 曲线渲染对象
	 */
	private ColoursXYSeriesRenderer setSlaveSeriesRenderer(
			ChartPagerBean mChartPagerBean) {
		ColoursXYSeriesRenderer rendererSlave = new ColoursXYSeriesRenderer();
		rendererSlave.setPointStyle(PointStyle.CIRCLE);// 设置为曲线图
		rendererSlave.setFillPoints(true);// 数据点被填充
		rendererSlave.setDisplayChartValues(true);// 在图表中显示点的值
		rendererSlave.setDisplayChartValuesDistance(1);// 在X轴上，设置两点之间的距离
		rendererSlave.setColor(Color.YELLOW);// 设置点的颜色
		rendererSlave.setChartValuesTextSize(25);// 设置点值文本的尺寸大小
		rendererSlave.setChartValuesSpacing(15f);// 实时数据文本到中心点的间距
		rendererSlave.setUseColor(true);// 设置当前为双色曲线图
		rendererSlave.setWarningMinValue(mChartPagerBean.slaveWarningMin);// 设置告警最小值
		rendererSlave.setWarningMaxValue(mChartPagerBean.slaveWarningMax);// 设置告警最大值
		return rendererSlave;
	}

	/**
	 * 创建主曲线渲染对象
	 * 
	 * @param mChartPagerBean
	 *            图表容器
	 * @return 曲线渲染对象
	 */
	private ColoursXYSeriesRenderer setMajorSeriesRenderer(
			ChartPagerBean mChartPagerBean) {
		ColoursXYSeriesRenderer rendererMajor = new ColoursXYSeriesRenderer();
		rendererMajor.setPointStyle(PointStyle.CIRCLE);// 设置为曲线图
		rendererMajor.setFillPoints(true);// 数据点被填充
		rendererMajor.setDisplayChartValues(true);// 在图表中显示点的值
		rendererMajor.setDisplayChartValuesDistance(1);// 在X轴上，设置两点之间的距离
		rendererMajor.setColor(Color.BLUE);// 设置点的颜色
		rendererMajor.setChartValuesTextSize(25);// 设置点值文本的尺寸大小
		rendererMajor.setChartValuesSpacing(15f);// 实时数据文本到中心点的间距
		rendererMajor.setUseColor(true);// 设置当前为双色曲线图
		rendererMajor.setPointColor(Color.GREEN);// 设置点的颜色
		rendererMajor.setChartValueTextColor(Color.WHITE);// 设置数值文本的颜色
		rendererMajor.setWarningMinValue(mChartPagerBean.majorWarningMin);// 设置告警最小值
		rendererMajor.setWarningMaxValue(mChartPagerBean.majorWarningMax);// 设置告警最大值
		return rendererMajor;
	}

	/**
	 * 创建图表数据渲染器
	 * 
	 * @return 图表数据渲染器
	 */
	private XYMultipleSeriesRenderer setRenderer() {
		XYMultipleSeriesRenderer mRenderer;
		mRenderer = new XYMultipleSeriesRenderer();

		mRenderer.setApplyBackgroundColor(true);// 开启背景颜色
		mRenderer.setAxisTitleTextSize(25);// 设置坐标文字尺寸大小
		mRenderer.setAxesColor(Color.BLACK);// /设置坐标文字颜色
		mRenderer.setChartTitleTextSize(20);// 设置图表标题文字尺寸大小
		mRenderer.setLabelsTextSize(18);// 设置刻度显示文字的大小(XY轴都会被设置)
		mRenderer.setLabelsColor(Color.BLACK);// 设置XY轴线的颜色
		mRenderer.setLegendTextSize(25);// 图例文字大小
		mRenderer.setMargins(new int[] { 0, 20, 0, 20 });// 设置图表的外边框(上/左/下/右)
		mRenderer.setZoomButtonsVisible(true);// 是否显示放大缩小按钮
		mRenderer.setPointSize(10);// 设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)

		mRenderer.setXLabelsColor(Color.WHITE);// 设置X轴标签文本的颜色
		mRenderer.setYLabelsColor(0, Color.WHITE);// 设置Y轴标签文本的颜色
		return mRenderer;
	}
}
