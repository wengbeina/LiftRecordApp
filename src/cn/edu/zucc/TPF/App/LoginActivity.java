package cn.edu.zucc.TPF.App;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.edu.zucc.TPF.Bean.LiftBean;
import cn.edu.zucc.TPF.liftdatarecordactivity.R;

public class LoginActivity extends Activity {
	private EditText idEdit;
    private EditText pwdEdit;
    private Button loginBtn;
    private Button registerBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		idEdit = (EditText)findViewById(R.id.userid);
		pwdEdit = (EditText)findViewById(R.id.userpwd);		
		loginBtn = (Button)findViewById(R.id.loginBtn);
		registerBtn = (Button)findViewById(R.id.registerBtn);
		loginBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LiftBean liftBean = new LiftBean();
				liftBean.setLiftid(idEdit.getText().toString());
				liftBean.setPwd(pwdEdit.getText().toString());				
				LoginDeal loginDeal = new LoginDeal(LoginActivity.this, liftBean);
				loginDeal.login();


			}
		 });
		registerBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}


}
