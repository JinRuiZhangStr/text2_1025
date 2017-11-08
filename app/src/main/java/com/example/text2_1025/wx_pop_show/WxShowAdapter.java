package com.example.text2_1025.wx_pop_show;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.text2_1025.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 张金瑞 on 2017/11/2.
 */

public class WxShowAdapter extends BaseAdapter {

    private ArrayList<String> list;
    private Context context;

    public WxShowAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.item,null);
            holder = new Holder(convertView);

            convertView.setTag(holder);


        }else {
            holder = (Holder) convertView.getTag();
        }

        holder.idInfo.setText(list.get(position));


        return convertView;
    }

    class Holder {
        @BindView(R.id.id_info)
        TextView idInfo;

        Holder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
