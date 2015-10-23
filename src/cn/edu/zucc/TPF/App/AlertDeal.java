package cn.edu.zucc.TPF.App;

import android.content.Context;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import cn.edu.zucc.TPF.Bean.LiftDataBean;
import cn.edu.zucc.TPF.remoteConnection.RemoteServerReader;
import cn.edu.zucc.TPF.util.CrcCompute;

/**
 * Created by aqi on 15/10/23.
 */
public class AlertDeal {
    private Context mContext;
    private LiftDataBean mLiftData;

    private RemoteServerReader mServerReadeer;
    private Socket mSocket;
    private String mIp;
    private int mPort;
    private ObjectMapper mMap;
    private String mMessage;
    private InetSocketAddress mAddr;
    private CrcCompute mCrcCompute;
    private String mResult;

    public AlertDeal(Context context,LiftDataBean liftDataBean){
        this.mContext = context;
        this.mLiftData = liftDataBean;

        mMap = new ObjectMapper();
        mServerReadeer = new RemoteServerReader(mContext);
        mIp = mServerReadeer.get("remoteip");
        mPort = Integer.parseInt(mServerReadeer.get("remoteport"));
        mCrcCompute = new CrcCompute(CrcCompute.CRC_16);
        mAddr = new InetSocketAddress(mIp, mPort);
        mSocket = new Socket();

    }

    public void alert(){
        try {
            mMessage = mMap.writeValueAsString(mLiftData);
            String type = "09";
            mMessage = type + mMessage;
            int crcResult = mCrcCompute.GetDataCrc(mMessage.getBytes());
            String crcToHex = mCrcCompute.ChangeToHexCrc(crcResult);
            mMessage = mMessage + crcToHex;
            mSocket.connect(mAddr, 10000);

            PrintWriter pw = getWriter(mSocket);
            pw.println(mMessage);
            BufferedReader br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            mResult = br.readLine();
            //1：成功，2：失败

            if("1".equals(mResult)) {
                Toast.makeText(mContext, "警报发送成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "服务器去外星球了！", Toast.LENGTH_SHORT).show();
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private PrintWriter getWriter(Socket mSocket) throws IOException {
        OutputStream socketOut = mSocket.getOutputStream();
        return new PrintWriter(socketOut, true);
    }
}
