package cn.edu.zucc.TPF.remoteConnection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

public class RemoteServerWriter {
	private Context context;
	static private Properties ps;
	
	public RemoteServerWriter(Context context){
		this.context = context;
		init();
	}
	
	private void init(){
		ps =  new Properties();
		try {
			InputStream input = context.getAssets().open("serverip/server.properties");
			ps.load(input);
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public RemoteServerWriter() {
		// TODO Auto-generated constructor stub
	}

}
