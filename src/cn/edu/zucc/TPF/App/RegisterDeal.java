package cn.edu.zucc.TPF.App;

import android.app.Activity;
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

import cn.edu.zucc.TPF.Bean.RegisterLiftBean;
import cn.edu.zucc.TPF.remoteConnection.RemoteServerReader;
import cn.edu.zucc.TPF.util.CrcCompute;

/**
 * Created by aqi on 15/10/9.
 */
public class RegisterDeal {
    private Context mContext;
    private RegisterLiftBean mLift;

    private RemoteServerReader mServerReadeer;
    private Socket mSocket;
    private String mIp;
    private int mPort;
    private ObjectMapper mMap;
    private String mMessage;
    private InetSocketAddress mAddr;
    private CrcCompute mCrcCompute;
    private String mResult;

    public RegisterDeal(Context context, RegisterLiftBean lift) {
        this.mContext = context;
        this.mLift = lift;

        mMap = new ObjectMapper();
        mServerReadeer = new RemoteServerReader(mContext);
        mIp = mServerReadeer.get("remoteip");
        mPort = Integer.parseInt(mServerReadeer.get("remoteport"));
        mCrcCompute = new CrcCompute(CrcCompute.CRC_16);
        mAddr = new InetSocketAddress(mIp, mPort);
        mSocket = new Socket();
    }

    public void register() {
        try {
            mMessage = mMap.writeValueAsString(mLift);
            String type = "06";
            mMessage = type + mMessage;
            int crcResult = mCrcCompute.GetDataCrc(mMessage.getBytes());
            String crcToHex = mCrcCompute.ChangeToHexCrc(crcResult);
            mMessage = mMessage + crcToHex;
            mSocket.connect(mAddr, 10000);

            PrintWriter pw = getWriter(mSocket);
            pw.println(mMessage);
            BufferedReader br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            mResult = br.readLine();
            //1��ע��ɹ���2�����ݱ���ظ�
            if ("2".equals(mResult)) {
                Toast toast = Toast.makeText(mContext, "���ݱ���ظ�����������ƣ�", Toast.LENGTH_SHORT);
                toast.show();
            } else if("1".equals(mResult)) {
                Toast.makeText(mContext, "ע��ɹ���", Toast.LENGTH_SHORT).show();
                Activity activity = (Activity) mContext;
                activity.finish();
            } else {
                Toast.makeText(mContext, "ע��ʧ�ܣ�", Toast.LENGTH_SHORT).show();
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
