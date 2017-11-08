package com.example.text2_1025.wx_pop_show;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.text2_1025.R;
import com.example.text2_1025.util.NativaHttpCallback;
import com.example.text2_1025.util.NativaHttpUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 张金瑞 on 2017/11/2.
 */

public class WxPopShowActivity extends AppCompatActivity {
    ArrayList<String> strList = new ArrayList<>();
    @BindView(R.id.edt)
    EditText edt;
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.tv)
    TextView tv;

    private static final String TAG = "WxPopShowActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpopshow);
        ButterKnife.bind(this);
        showtext();

    }

    private void showtext() {

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = edt.getText().toString().trim();

                NativaHttpUtils.getInstance().getCertificateSafe(WxPopShowActivity.this, trim, new NativaHttpCallback() {
                    @Override
                    public void Success(final Object bean) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(bean.toString());
                            }
                        });
                    }

                    @Override
                    public void Faield(Throwable ex) {
                        Log.e(TAG, "Faield: "+ex.getMessage() );
                    }

                    @Override
                    public void onShowProgress() {

                    }

                    @Override
                    public void onHideProgress() {

                    }

                    @Override
                    public Object onCreateBean(String requestResult) {
                        return null;
                    }
                });

            }
        });
    }

}
