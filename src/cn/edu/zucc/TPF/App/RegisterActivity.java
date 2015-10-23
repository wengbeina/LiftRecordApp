package cn.edu.zucc.TPF.App;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.edu.zucc.TPF.Bean.RegisterLiftBean;
import cn.edu.zucc.TPF.liftdatarecordactivity.R;

/**
 * Created by aqi on 15/10/5.
 */
public class RegisterActivity extends Activity {
    private EditText mId;
    private EditText mName;
    private EditText mPwd;
    private EditText mAddress;
    private EditText mXlimit;
    private EditText mYlimit;
    private EditText mZLimit;
    private EditText mCode;
    private Button mOkbtn;
    private Button mResetbtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        init();
        okbtnListener();
        resetbtnListener();


    }

    private void okbtnListener() {
        mOkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("4423".equals(mCode.getText().toString())) {
                    RegisterLiftBean lift = new RegisterLiftBean();
                    lift.setLiftid(mId.getText().toString());
                    lift.setLiftname(mName.getText().toString());
                    lift.setPwd(mPwd.getText().toString());
                    lift.setAddress(mAddress.getText().toString());
                    lift.setAccxLimit(Float.parseFloat(mXlimit.getText().toString()));
                    lift.setAccyLimit(Float.parseFloat(mYlimit.getText().toString()));
                    lift.setAcczLimit(Float.parseFloat(mZLimit.getText().toString()));

                    RegisterDeal deal = new RegisterDeal(RegisterActivity.this, lift);
                    deal.register();
                } else{
                    Toast.makeText(RegisterActivity.this,"授权码不正确，请核对！",Toast.LENGTH_SHORT).show();
                    mCode.setText("");
                }
            }
        });
    }

    private void resetbtnListener() {
        mResetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
    }

    private void init() {
        mId = (EditText) findViewById(R.id.lift_id);
        mName = (EditText) findViewById(R.id.litf_name);
        mPwd = (EditText) findViewById(R.id.lift_pwd);
        mAddress = (EditText) findViewById(R.id.address);
        mXlimit = (EditText) findViewById(R.id.xlimit);
        mYlimit = (EditText) findViewById(R.id.ylimit);
        mZLimit = (EditText) findViewById(R.id.zlimit);
        mCode = (EditText) findViewById(R.id.code);
        mOkbtn = (Button) findViewById(R.id.okBtn);
        mResetbtn = (Button) findViewById(R.id.resetBtn);
    }

    private void reset() {
        mId.setText("");
        mName.setText("");
        mPwd.setText("");
        mAddress.setText("");
        mCode.setText("");
    }
}
