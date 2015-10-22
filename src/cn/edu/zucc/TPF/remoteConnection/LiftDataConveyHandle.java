package cn.edu.zucc.TPF.remoteConnection;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.edu.zucc.TPF.Bean.AccCheckValue;
import cn.edu.zucc.TPF.Bean.AccRecordBean;
import cn.edu.zucc.TPF.Bean.LiftDataBean;
import cn.edu.zucc.TPF.util.CrcCompute;
import android.content.Context;

public class LiftDataConveyHandle implements Runnable{
    private RemoteServerReader serverReader;
    private Context context;
    private String liftid;
    private Socket socket;
    private String ip;
    private int port;
    private AccRecordBean accBean;
    private ObjectMapper map = new ObjectMapper();
    private String message;
    private InetSocketAddress addr;
    private CrcCompute crcCompute;
    private AccCheckValue accCheck;
    
    private List <LiftDataBean> liftDataList = new ArrayList<LiftDataBean>();
    private int limitSize = 20;
    private long period = 500;
    
    private DecimalFormat myformat = new  DecimalFormat("####.000"); 
    
	public LiftDataConveyHandle(Context context, String liftid, AccRecordBean accBean, 
			AccCheckValue accCheck) {
		this.context = context;
		this.accBean = accBean;
		this.liftid = liftid;
		this.accCheck = accCheck;
		// TODO Auto-generated constructor stub
	}
	
	public void run(){
		serverReader = new RemoteServerReader(context);
		ip = serverReader.get("remoteip");
		port = Integer.parseInt(serverReader.get("remoteport"));
		addr = new InetSocketAddress(ip, port);
		crcCompute =  new CrcCompute(CrcCompute.CRC_16);
		while(true){
			try {				
			while(!accBean.isTransportOn()){
				Thread.sleep(3*1000);
			}
			while(accCheck.isPause()){
				Thread.sleep(3*1000);
			}
			
			LiftDataBean liftData =  new LiftDataBean();		
			liftData.setLiftid(liftid);
			int sizeX = accBean.getAccX().size();
			int sizeY = accBean.getAccY().size();
			int sizeZ = accBean.getAccZ().size();
			
			if(sizeX>0 && sizeY>0 && sizeZ>0 ){
			   liftData.setAccx(ThreeDecimalPoint(accBean.getAccX().get(sizeX-1) - accCheck.getAccXCheck()));
			   liftData.setAccy(ThreeDecimalPoint(accBean.getAccY().get(sizeY-1) - accCheck.getAccYCheck()));
			   liftData.setAccz(ThreeDecimalPoint(accBean.getAccZ().get(sizeZ-1) - accCheck.getAccZCheck()));
			   liftData.setRotatex(ThreeDecimalPoint(accBean.getRotateX()));
			   liftData.setRotatey(ThreeDecimalPoint(accBean.getRotateY()));
			   liftData.setRotatez(ThreeDecimalPoint(accBean.getRotateZ()));
			   liftData.setRecordtime(new Timestamp(System.currentTimeMillis()));
			   liftDataList.add(liftData);			   
			}
			
			/**数据打包成文本字符串Gson格式进行传送*/
			if((liftDataList.size()==limitSize)){
			   conveyData();
			   liftDataList.clear();
			}				
			
			Thread.sleep(period);
			
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private PrintWriter getWriter(Socket socket) throws IOException{
		OutputStream socketOut = socket.getOutputStream();
		return new PrintWriter(socketOut,true);
	}
	
	//判断是否异常，也可以建立一个模型判断，设计一个接口，根据不同的需求使用不用的实现类
	private boolean isDataUnusual(LiftDataBean liftData){
		if(liftData.getAccx() >= 1.4 ||liftData.getAccy()>=1.4 || liftData.getAccz()>=1.4){
			return true;
		}
		else
			return false;
	} 
	
	private void conveyData(){	
		socket = new Socket();		 
		try {
			message = map.writeValueAsString(liftDataList);
			String type = "01";
			message = type + message;
			int crcResult = crcCompute.GetDataCrc(message.getBytes());
			String crcToHex = crcCompute.ChangeToHexCrc(crcResult);
			message = message + crcToHex;
			socket.connect(addr, 10000);
			PrintWriter pw = getWriter(socket);
			pw.println(message);	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			/*Toast toast = Toast.makeText(context, "与服务器连接中断！", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();*/
		}finally{
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//保留3位小数
	private float ThreeDecimalPoint(float source){		
    	String result = myformat.format(source);
    	float fresult=Float.parseFloat(result); 
    	return fresult;
	}
}
