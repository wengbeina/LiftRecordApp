package cn.edu.zucc.TPF.App;

import android.util.Log;

import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;

import java.util.List;

import cn.edu.zucc.TPF.Bean.AccCheckValue;
import cn.edu.zucc.TPF.Bean.AccRecordBean;

public class AcclineChangeHandle implements Runnable {
    private XYMultipleSeriesDataset dataSet;
    private AccRecordBean accBean;
    private GraphicalView chart;
    private AccCheckValue accCheck;
    private final  int blankTime = 200;

    public AcclineChangeHandle(XYMultipleSeriesDataset dataSet, AccRecordBean accBean,
                               AccCheckValue accCheck, GraphicalView chart) {
        this.dataSet = dataSet;
        this.accBean = accBean;
        this.accCheck = accCheck;
        this.chart = chart;
        // TODO Auto-generated constructor stub
    }

    public void run() {
        List<Float> accX, accY, accZ;
        /*float[] accXCheck = new  float[10];
		float[] accYCheck = new  float[10];
		float[] accZCheck = new  float[10];
		for(int i=0;i<10;i++){
			accXCheck[i] = accBean.getCurrentAccX();
			accYCheck[i] = accBean.getCurrentAccY();
			accZCheck[i] = accBean.getCurrentAccZ();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//校正初始值
		accCheck.setAccXCheck(DataDeal.getCheckedData(accXCheck));
		accCheck.setAccYCheck(DataDeal.getCheckedData(accYCheck));
		accCheck.setAccZCheck(DataDeal.getCheckedData(accZCheck));*/

        while (true) {
            XYSeries seriesX = new XYSeries("X方向的加速度");
            XYSeries seriesY = new XYSeries("Y方向的加速度");
            XYSeries seriesZ = new XYSeries("Z方向的加速度");
            accX = accBean.getAccX();
            accY = accBean.getAccY();
            accZ = accBean.getAccZ();

            Log.d("accX-size：", Integer.toString(accX.size()));
            Log.d("accY-size：", Integer.toString(accY.size()));
            Log.d("accZ-size：", Integer.toString(accZ.size()));

            try {
                for (int i = 0; i < accX.size(); i++) {
                    seriesX.add((i) * blankTime/1000.0, accX.get(i) - accCheck.getAccXCheck());
                }
                for (int j = 0; j < accY.size(); j++) {
                    seriesY.add((j) * blankTime/1000.0, accY.get(j) - accCheck.getAccYCheck());
                }
                for (int k = 0; k < accZ.size(); k++) {
                    seriesZ.add((k) * blankTime/1000.0, accZ.get(k) - accCheck.getAccZCheck());
                }
			
			/*if(dataSet.getSeriesCount()>0){
				int size = dataSet.getSeriesCount();
				for(int m=0;m<size;m++){
					dataSet.removeSeries(m);
				}
			}*/
                dataSet.removeSeries(2);
                dataSet.removeSeries(1);
                dataSet.removeSeries(0);

                dataSet.addSeries(seriesX);
                dataSet.addSeries(seriesY);
                dataSet.addSeries(seriesZ);

                chart.postInvalidate();

                Thread.sleep(blankTime);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                Log.d("AccLineChangeHandle 出错！", e.toString());
            }
        }
    }

}
