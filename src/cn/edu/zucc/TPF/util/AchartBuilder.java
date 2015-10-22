package cn.edu.zucc.TPF.util;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class AchartBuilder {
    private float[] accValues=new float[3];  
    private XYMultipleSeriesRenderer render = new XYMultipleSeriesRenderer();;
    private XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();;
    
    private String[] titles = new String[]{"加速度x","加速度y","加速度Z"};
    private int length;
    private int[]colors = new int[]{Color.BLUE, Color.RED ,Color.GREEN};
    private PointStyle[]  styles = new PointStyle[]{PointStyle.POINT, PointStyle.POINT, PointStyle.POINT};
    
	public AchartBuilder() {	
		// TODO Auto-generated constructor stub
		initStyle();
		initData();
	}
	
	private void initStyle(){		
		length = titles.length;
		
		render.setAxisTitleTextSize(15);
		render.setChartTitleTextSize(18);
		render.setLabelsTextSize(15);
		render.setLegendTextSize(15);
		render.setPointSize(5f);
		render.setShowGrid(true);
		render.setApplyBackgroundColor(true);
		render.setBackgroundColor(Color.WHITE);		
		render.setChartTitle("三方向加速度曲线图");
		
		render.setXTitle("时间(s)");
		render.setYTitle("加速度值(m/s2)");
		
		render.setXAxisMin(0);
		render.setXAxisMax(10);
		render.setYAxisMin(-3);
		render.setYAxisMax(3);
		
		render.setAxesColor(Color.WHITE);
		render.setLabelsColor(Color.WHITE);
		render.setTextTypeface("sans_serif", Typeface.BOLD);
		render.setXLabels(10);
		render.setYLabels(12);
		
		render.setXLabelsAlign(Align.CENTER);
		render.setYLabelsAlign(Align.RIGHT);
		render.setPanEnabled(true, true);
		//左右滑动和放大缩小的上下限
		render.setPanLimits(new double[]{0,10,-10,10});
		render.setZoomLimits(new double[]{0,10,-10,10});
		render.setZoomEnabled(true);
		render.setZoomButtonsVisible(true);
		render.setZoomRate(1.1f);
		render.setBarSpacing(0.5f);	
		
		for(int i=0;i<length;i++){
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			r.setLineWidth(2);
			render.addSeriesRenderer(r);
		}		
	}
	
	private void initData(){
		XYSeries dataX = new XYSeries(titles[0]);
		XYSeries dataY = new XYSeries(titles[1]);
		XYSeries dataZ = new XYSeries(titles[2]);
		
		dataX.add(0*5, 1);
		dataX.add(1*5, 2);
		dataX.add(2*5, 2.5);
		dataX.add(3*5, 2.0);
		dataX.add(4*5, 1.1);
		dataX.add(5*5, 1.1);
		dataX.add(6*5, 1.1);
		dataX.add(7*5, 1.4);
		dataX.add(8*5, 0.5);
		dataX.add(9*5, 0.6);
		dataX.add(10*5, 0.7);
		dataX.add(11*5, 0.8);
		dataX.add(12*5, 0.8);
		dataX.add(13*5, 0.8);
		
		dataY.add(0*5, -1);
		dataY.add(1*5, -0.5);
		dataY.add(2*5, 0.5);
		dataY.add(3*5, -0.5);
		dataY.add(4*5, -2);
		dataY.add(5*5, 1);
		dataY.add(6*5, 1.2);
		dataY.add(7*5, 1.4);
		dataY.add(8*5, 2.5);
		dataY.add(9*5, 2.5);
		dataY.add(10*5, 2.5);
		dataY.add(11*5, 0);
		
		dataZ.add(0*5, 0);
		dataZ.add(1*5, -2);
		dataZ.add(2*5, -1.5);
		dataZ.add(3*5, -0.5);
		dataZ.add(4*5, -0.5);
		dataZ.add(5*5, -1.1);
		dataZ.add(6*5, -1.1);
		dataZ.add(7*5, 1.4);
		dataZ.add(8*5, 2.5);
		dataZ.add(9*5, 3.6);
		dataZ.add(10*5, 3.7);
		dataZ.add(11*5, 3.8);
		
		
		dataSet.addSeries(dataX);
		dataSet.addSeries(dataY);
		dataSet.addSeries(dataZ);
		
		for(int i=0;i<3;i++){
        	accValues[i] = 0;
        }
	}

	public XYMultipleSeriesRenderer getRender() {
		return render;
	}

	public void setRender(XYMultipleSeriesRenderer render) {
		this.render = render;
	}

	public XYMultipleSeriesDataset getDataSet() {
		return dataSet;
	}

	public void setDataSet(XYMultipleSeriesDataset dataSet) {
		this.dataSet = dataSet;
	}
}
