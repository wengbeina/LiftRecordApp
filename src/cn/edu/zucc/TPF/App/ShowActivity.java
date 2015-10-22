package cn.edu.zucc.TPF.App;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import cn.edu.zucc.TPF.Bean.AccCheckValue;
import cn.edu.zucc.TPF.Bean.AccRecordBean;
import cn.edu.zucc.TPF.Bean.UserBean;
import cn.edu.zucc.TPF.SQLiteDB.LiftDataDAO;
import cn.edu.zucc.TPF.liftdatarecordactivity.R;
import cn.edu.zucc.TPF.remoteConnection.LiftDataConveyHandle;
import cn.edu.zucc.TPF.util.AchartBuilder;
import cn.edu.zucc.TPF.util.DataDeal;

public class ShowActivity extends Activity implements OnTabChangeListener,
        SensorEventListener {
    private UserBean user;
    private TabHost myTabhost;
    private SensorManager sensorManager;
    private float[] acc = new float[3];
    private float[] mag = new float[3];
    private float[] rotateValues = new float[3];
    private float[] rotateMatrix = new float[9];
    private AccRecordBean accBean = new AccRecordBean();
    private AccCheckValue accCheck = new AccCheckValue();

    private LinearLayout linearDynamicLayout;
    private LinearLayout selectResultLayout;
    private AchartBuilder achartDynamic;
    private AchartBuilder achartStatic;

    private GraphicalView chartDynamic;
    private GraphicalView chartStatic;

    private EditText fromDate;
    private EditText fromTime;
    private EditText toDate;
    private EditText toTime;
    private Timestamp from;
    private Timestamp to;
    private Calendar fromCalendar = Calendar.getInstance();
    private Calendar toCalendar = Calendar.getInstance();

    private String selectType = "LIST";

    private RadioGroup mRadioGroup;
    private int radioListId;
    private Button selectBtn;

    private ListView listView;
    private List<Map<String, Object>> list;
    private List<Map<String, Object>> childList;
    private LayoutInflater inflater;
    private int pageSize = 9;
    private int pageNum = 1;
    private int totalCount = 0;
    private LiftDataDAO liftDao;
    private SimpleAdapter simpleAdapter = null;
    private TextView totalShow;
    private TextView currentShow;
    private ImageButton preBtn;
    private ImageButton nextBtn;
    private Button checkBtn;

    private ToggleButton slipSave;
    private ToggleButton slipTransport;

    private int mBackKeyPressedTimes = 0;
    //锁屏cpu控制
    private PowerManager.WakeLock mWakeLock = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_layout);

        acquireWakeLock();

        //登录成功后，传过来的user
        //user = (UserBean)getIntent().getSerializableExtra("USER");
        String id = (String) getIntent().getSerializableExtra("liftid");
        this.setTitle("电梯编号：" + id);
        user = new UserBean();
        user.setId(id);

        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        linearDynamicLayout = (LinearLayout) this.findViewById(R.id.layout_1);
        selectResultLayout = (LinearLayout) findViewById(R.id.resultlayout);

        slipSave = (ToggleButton) findViewById(R.id.saveSwitch);
        slipSave.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveToLocalChange(b);
            }
        });

        slipTransport = (ToggleButton) findViewById(R.id.transportSwitch);
        slipTransport.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                transportChange(b);
            }
        });

        inflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(R.layout.showacc_layout, null);
        View view2 = inflater.inflate(R.layout.select_layout, null);
        View view3 = inflater.inflate(R.layout.set_layout, null);

        ListViewInit();

        achartDynamic = new AchartBuilder();
        achartDynamic.getRender().setPanEnabled(true, true);
        achartDynamic.getRender().setPanLimits(new double[]{0, 10, -10, 10});
        achartDynamic.getRender().setZoomLimits(new double[]{0, 10, -10, 10});
        achartDynamic.getRender().setYAxisMax(3);
        achartDynamic.getRender().setYAxisMin(-3);
        achartDynamic.getRender().setYLabels(12);

        achartStatic = new AchartBuilder();
        achartStatic.getRender().setPanEnabled(true, true);
        achartStatic.getRender().setXAxisMax(5);
        achartStatic.getRender().setXAxisMin(0);
        achartStatic.getRender().setYAxisMax(3);
        achartStatic.getRender().setYAxisMin(-3);
        achartStatic.getRender().setXLabels(10);

        AccDynamiclineInit();
        AccStaticlineInit();

        selectResultLayout.addView(listView);

        mRadioGroup = (RadioGroup) findViewById(R.id.menu);
        radioListId = R.id.showlist;
        mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                radioChange();
            }
        });

        selectBtn = (Button) findViewById(R.id.selectbtn);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                select();
            }
        });

        checkBtn = (Button) findViewById(R.id.checkBtn);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                check();
            }
        });

        myTabhost = (TabHost) findViewById(R.id.tabhost);
        myTabhost.setup();
        myTabhost.addTab(myTabhost.newTabSpec("Tab1").setIndicator(view1).setContent(R.id.layout_0));
        myTabhost.addTab(myTabhost.newTabSpec("Tab2").setIndicator(view2).setContent(R.id.layout_2));
        myTabhost.addTab(myTabhost.newTabSpec("Tab3").setIndicator(view3).setContent(R.id.layout_3));


        final TabWidget tw = myTabhost.getTabWidget();
//        tw.setBackgroundColor(Color.RED);

        datePickInit();
        Thread save = new Thread(new LiftDataToLocalDBHandle(liftDao, accBean, accCheck, user.getId()));
        save.start();
        Thread transport = new Thread(new LiftDataConveyHandle(ShowActivity.this, user.getId(), accBean, accCheck));
        transport.start();

    }

    /**
     * 切换查询结果显示形式的监听事件代码
     */
    private void radioChange() {
        int id = mRadioGroup.getCheckedRadioButtonId();
        if (id == radioListId) {
            selectType = "LIST";
            selectResultLayout.removeAllViews();
            selectResultLayout.addView(listView);
            Toast.makeText(ShowActivity.this, selectType, Toast.LENGTH_SHORT).show();
        } else {
            selectType = "GRPHY";
            selectResultLayout.removeAllViews();
            selectResultLayout.addView(chartStatic);
            Toast.makeText(ShowActivity.this, selectType, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 查询的监听事件代码
     */
    private void select() {
        totalCount = liftDao.getLiftdataCount(user.getId(), from, to);
        totalShow.setText("总条数：" + totalCount);
        pageNum = 1;
        list = liftDao.SelectLiftdata(user.getId(), from, to);
        //childList =liftDao.getLiftdataList(user.getId(), from, to, pageNum, pageSize);
        childList = getCurrentPageList(list);
        simpleAdapter = new SimpleAdapter(ShowActivity.this, childList, R.layout.list_item, new String[]{"liftid",
                "accx", "accy", "accz", "recordtime"}, new int[]{R.id.liftid, R.id.accx, R.id.accy,
                R.id.accz, R.id.recordtime});
        listView.setAdapter(simpleAdapter);

        XYSeries seriesX = new XYSeries("X方向的加速度");
        XYSeries seriesY = new XYSeries("Y方向的加速度");
        XYSeries seriesZ = new XYSeries("Z方向的加速度");
        int k = 0;
        for (Map<String, Object> i : list) {
            seriesX.add((k) * 0.5, ((Float) i.get("accx")).floatValue());
            seriesY.add((k) * 0.5, ((Float) i.get("accy")).floatValue());
            seriesZ.add((k) * 0.5, ((Float) i.get("accz")).floatValue());
            k++;
        }
        XYMultipleSeriesDataset dataSetTemp = achartStatic.getDataSet();
        dataSetTemp.removeSeries(2);
        dataSetTemp.removeSeries(1);
        dataSetTemp.removeSeries(0);

        dataSetTemp.addSeries(seriesX);
        dataSetTemp.addSeries(seriesY);
        dataSetTemp.addSeries(seriesZ);
        chartStatic.postInvalidate();

        if (selectType.equals("LIST")) {
            selectResultLayout.removeAllViews();
            selectResultLayout.addView(listView);
        } else {
            selectResultLayout.removeAllViews();
            selectResultLayout.addView(chartStatic);
        }
    }

    /**
     * 校正的监听事件代码
     */
    private void check() {
        accCheck.setPause(true);
        float[] accXCheck = new float[10];
        float[] accYCheck = new float[10];
        float[] accZCheck = new float[10];
        for (int i = 0; i < 10; i++) {
            accXCheck[i] = acc[0];
            accYCheck[i] = acc[1];
            accZCheck[i] = acc[2];
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
        accCheck.setAccZCheck(DataDeal.getCheckedData(accZCheck));
        accCheck.setPause(false);
    }

    /**
     * 存储到本地数据的滑动开关监听事件代码
     */
    private void saveToLocalChange(boolean checkState) {
        if (checkState == true) {
            accBean.setSaveOn(true);
            Toast toast = Toast.makeText(this, "本地存储启动!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            accBean.setTransportOn(false);
            Toast toast = Toast.makeText(this, "本地存储暂停!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    /**
     * 向服务器传送数据的滑动开关监听事件代码
     */
    private void transportChange(boolean checkState) {
        if (checkState == true) {
            accBean.setTransportOn(true);
            Toast toast = Toast.makeText(this, "传输功能启动！", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            accBean.setTransportOn(false);
            Toast toast = Toast.makeText(this, "传输功能关闭", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void AccDynamiclineInit() {
        chartDynamic = ChartFactory.getLineChartView(this, achartDynamic.getDataSet(), achartDynamic.getRender());
        linearDynamicLayout.addView(chartDynamic);
        Thread thread = new Thread(new AcclineChangeHandle(achartDynamic.getDataSet(), accBean, accCheck, chartDynamic));
        thread.start();
    }

    private void AccStaticlineInit() {
        chartStatic = ChartFactory.getLineChartView(this, achartStatic.getDataSet(), achartStatic.getRender());
    }

    private List<Map<String, Object>> getCurrentPageList(List<Map<String, Object>> allList) {
        List<Map<String, Object>> childList = new ArrayList<Map<String, Object>>();
        if (pageSize * pageNum <= allList.size()) {
            for (int i = 0; i < pageSize; i++) {
                childList.add(allList.get((pageNum - 1) * pageSize + i));
            }
        } else {
            for (int i = 0; i < allList.size() - (pageNum - 1) * pageSize; i++) {
                childList.add(allList.get((pageNum - 1) * pageSize + i));
            }
        }

        return childList;
    }

    private void ListViewInit() {
        LinearLayout listTemp = (LinearLayout) inflater.inflate(R.layout.list_layout, null);
        View forpageView = inflater.inflate(R.layout.forpage, null);
        listView = (ListView) listTemp.findViewById(R.id.mListView);
        listTemp.removeView(listView);

        Calendar calFrom = Calendar.getInstance();
        Calendar calTo = Calendar.getInstance();
        calFrom.set(2014, 2, 1, 10, 0, 0);
        calTo.set(2014, 3, 20, 10, 0, 0);
        from = new Timestamp(calFrom.getTimeInMillis());
        to = new Timestamp(calTo.getTimeInMillis());

        listView.addFooterView(forpageView);
        liftDao = new LiftDataDAO(this);
        list = liftDao.SelectLiftdata(user.getId(), from, to);
        //childList =liftDao.getLiftdataList(user.getId(), from, to, pageNum, pageSize);
        childList = getCurrentPageList(list);
      /*  list = liftDao.getLiftdataList(from, to, pageNum, pageSize); */
        simpleAdapter = new SimpleAdapter(this, childList, R.layout.list_item, new String[]{"liftid",
                "accx", "accy", "accz", "recordtime"}, new int[]{R.id.liftid, R.id.accx, R.id.accy,
                R.id.accz, R.id.recordtime});
        listView.setAdapter(simpleAdapter);

        totalShow = (TextView) forpageView.findViewById(R.id.total);
        currentShow = (TextView) forpageView.findViewById(R.id.current);
        totalCount = liftDao.getLiftdataCount(user.getId(), from, to);
        totalShow.setText("总条数：" + totalCount);
        currentShow.setText("当前页：" + pageNum);

        preBtn = (ImageButton) forpageView.findViewById(R.id.preView);
        nextBtn = (ImageButton) forpageView.findViewById(R.id.nextView);

        preBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                nextBtn.setEnabled(true);
                if (pageNum > 1) {
                    pageNum--;
                    //childList = liftDao.getLiftdataList(user.getId(), from, to, pageNum, pageSize);
                    childList = getCurrentPageList(list);
                    simpleAdapter = new SimpleAdapter(ShowActivity.this, childList, R.layout.list_item, new String[]{"liftid",
                            "accx", "accy", "accz", "recordtime"}, new int[]{R.id.liftid, R.id.accx, R.id.accy,
                            R.id.accz, R.id.recordtime});
                    listView.setAdapter(simpleAdapter);
                    currentShow.setText("当前页：" + pageNum);
                     /* totalCount = liftDao.getLiftdataCount(user.getId(), from, to);
                   totalShow.setText("总条数："+totalCount);*/

                }
                if (pageNum == 1) {
                    preBtn.setEnabled(false);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                preBtn.setEnabled(true);
                if (pageNum < getDealer(totalCount, pageSize)) {
                    pageNum++;
                    //childList = liftDao.getLiftdataList(user.getId(), from, to, pageNum, pageSize);
                    childList = getCurrentPageList(list);
                    simpleAdapter = new SimpleAdapter(ShowActivity.this, childList, R.layout.list_item, new String[]{"liftid",
                            "accx", "accy", "accz", "recordtime"}, new int[]{R.id.liftid, R.id.accx, R.id.accy,
                            R.id.accz, R.id.recordtime});
                    listView.setAdapter(simpleAdapter);
                    currentShow.setText("当前页：" + pageNum);
                          /*totalCount = liftDao.getLiftdataCount(user.getId(), from, to);
			           totalShow.setText("总条数："+totalCount);*/
                }
                if (pageNum == getDealer(totalCount, pageSize)) {
                    nextBtn.setEnabled(false);
                }
            }
        });

    }

    //得到总页数，即总记录数和页面记录数的商
    private int getDealer(int host, int client) {
        if (host % client == 0)
            return host / client;
        else
            return host / client + 1;
    }

    private void selectHistory() {

    }

    private void otherSet() {

    }

    private void datePickInit() {
        fromDate = (EditText) findViewById(R.id.fromdate);
        fromTime = (EditText) findViewById(R.id.fromtime);
        toDate = (EditText) findViewById(R.id.todate);
        toTime = (EditText) findViewById(R.id.totime);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        final int curyear = c.get(Calendar.YEAR);
        final int curmonth = c.get(Calendar.MONTH);
        final int curday = c.get(Calendar.DAY_OF_MONTH);

        final int curhour = c.get(Calendar.HOUR_OF_DAY);
        final int curminute = c.get(Calendar.MINUTE);

        fromCalendar.set(2010, 0, 1, 0, 0);
        toCalendar.set(2015, 0, 1, 0, 0);

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDate.setText("");
                new DatePickerDialog(ShowActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        fromDate.setText(year + "-" + formatTwoNum(month + 1) + "-" + formatTwoNum(day));
                        int fromhour = fromCalendar.get(Calendar.HOUR);
                        int fromminute = fromCalendar.get(Calendar.MINUTE);
                        fromCalendar.set(year, month, day, fromhour, fromminute);
                        from = new Timestamp(fromCalendar.getTimeInMillis());
                        // TODO Auto-generated method stub

                    }
                }, curyear, curmonth, curday).show();
            }
        });

        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromTime.setText("");
                new TimePickerDialog(ShowActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker arg0, int hour, int minute) {
                        // TODO Auto-generated method stub
                        fromTime.setText(formatTwoNum(hour) + ":" + formatTwoNum(minute));
                        int fromyear = fromCalendar.get(Calendar.YEAR);
                        int frommonth = fromCalendar.get(Calendar.MONTH);
                        int fromday = fromCalendar.get(Calendar.DAY_OF_MONTH);
                        fromCalendar.set(fromyear, frommonth, fromday, hour, minute);
                        from = new Timestamp(fromCalendar.getTimeInMillis());
                    }
                }, curhour, curminute, true).show();
                // TODO Auto-generated method stub
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDate.setText("");
                new DatePickerDialog(ShowActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        toDate.setText(year + "-" + formatTwoNum(month + 1) + "-" + formatTwoNum(day));
                        int tohour = toCalendar.get(Calendar.HOUR);
                        int tominute = toCalendar.get(Calendar.MINUTE);
                        toCalendar.set(year, month, day, tohour, tominute);
                        to = new Timestamp(toCalendar.getTimeInMillis());
                        // TODO Auto-generated method stub

                    }
                }, curyear, curmonth, curday).show();
            }
        });

        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTime.setText("");
                new TimePickerDialog(ShowActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker arg0, int hour, int minute) {
                        // TODO Auto-generated method stub
                        toTime.setText(formatTwoNum(hour) + ":" + formatTwoNum(minute));
                        int toyear = toCalendar.get(Calendar.YEAR);
                        int tomonth = toCalendar.get(Calendar.MONTH);
                        int today = toCalendar.get(Calendar.DAY_OF_MONTH);
                        toCalendar.set(toyear, tomonth, today, hour, minute);
                        to = new Timestamp(toCalendar.getTimeInMillis());
                    }
                }, curhour, curminute, true).show();
                // TODO Auto-generated method stub
            }
        });
    }

    //日期和时间都显示为两位数
    public String formatTwoNum(int x) {
        String s = Integer.toString(x);
        if (s.length() == 1)
            s = "0" + s;
        return s;
    }

    @Override
    public void onTabChanged(String arg0) {
        // TODO Auto-generated method stub

    }

    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();
        if (type == Sensor.TYPE_ACCELEROMETER) {
            if (!accCheck.isPause()) {
                accBean.addDataToAccX(event.values[0]);
                accBean.addDataToAccY(event.values[1]);
                accBean.addDataToAccZ(event.values[2]);
            }
            for (int i = 0; i < 3; i++) {
                acc[i] = event.values[i];
            }

        } else if (type == Sensor.TYPE_MAGNETIC_FIELD) {
            mag = event.values;
        }
        SensorManager.getRotationMatrix(rotateMatrix, null, acc, mag);
        SensorManager.getOrientation(rotateMatrix, rotateValues);

        accBean.setRotateX((float) Math.toDegrees(rotateValues[0]));
        accBean.setRotateY((float) Math.toDegrees(rotateValues[1]));
        accBean.setRotateZ((float) Math.toDegrees(rotateValues[2]));
		/*textViewO.setText("X轴方向夹角："+accValues[0]+"\nY轴方向夹角："+
		            accValues[1]+"\nZ轴方向夹角为:"+accValues[2]);	*/
    }

    public void onResume() {
        super.onResume();
        //按类型取得传感器，取得角速度和加速度传感器
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        List<Sensor> sensors1 = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        if (sensors.size() > 0) {
            sensorManager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "无可用的加速度传感器！", Toast.LENGTH_SHORT).show();
        }

        if (sensors1.size() > 0) {
            sensorManager.registerListener(this, sensors1.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "无可用的磁力传感器！", Toast.LENGTH_SHORT).show();
        }

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onBackPressed() {
        if (mBackKeyPressedTimes == 0) {
            Toast.makeText(this, "连续按两次退出 ", Toast.LENGTH_SHORT).show();
            mBackKeyPressedTimes = 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        mBackKeyPressedTimes = 0;
                    }
                }
            }.start();
            return;
        } else {
            ShowActivity.this.finish();
        }

        super.onBackPressed();
    }

    //获取电源锁，锁屏是cpu运行
    private void acquireWakeLock() {
        if (mWakeLock == null) {
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "PostLocationService");
            if (mWakeLock != null) {
                mWakeLock.acquire();
            }
        }
    }

    private void relaseWakeLock() {
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }


}
