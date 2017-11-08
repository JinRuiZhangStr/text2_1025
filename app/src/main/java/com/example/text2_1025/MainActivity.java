package com.example.text2_1025;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.text2_1025.customview.CustomActivity;
import com.example.text2_1025.wx_pop_show.WxPopShowActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.intent_page)
    Button intentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }


    @OnClick({R.id.intent_page,R.id.intent_page2})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.intent_page:
                startActivity(new Intent(this, WxPopShowActivity.class));
            break;
            case R.id.intent_page2:
                startActivity(new Intent(this, CustomActivity.class));
                break;
        }
    }
}
