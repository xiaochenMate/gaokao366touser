package com.gaokao366.gaokao366touser.model.ui.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.adapter.MyBaseAdapter;
import com.gaokao366.gaokao366touser.model.ui.main.bean.ExamPaperBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

public class ExamPaperAdapter extends MyBaseAdapter<ExamPaperBean> {

      private ArrayList<String> list_carseatId = new ArrayList<String>();

    //	private ICheckedChangeListener l ;
    public ExamPaperAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = null;
        if (convertView == null) {
            convertView = View.inflate(ct, R.layout.gv_item_paperexam, null);


            holder = new Holder(convertView);
            convertView.setTag(holder);
            AutoUtils.autoSize(convertView);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.setData(position);
        return convertView;
    }


    private class Holder {

        @ViewInject(R.id.gv_name)
        TextView gv_name;


        public Holder(View view) {
            ViewUtils.inject(this, view);
        }

        public void setData(final int position) {
            gv_name.setText(getItem(position).name);
//			ct_gv_it_seat.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//					if(l!=null)
//						l.onCheckedChanged(position+"", isChecked);
//				}
//			}) ;
        }
    }

//	public void setOncheckedhangeListener(ICheckedChangeListener l ){
//		this.l= l ;
//	}
//
//	public interface ICheckedChangeListener{
//		public void onCheckedChanged(String carSeatId, boolean isChecked) ;
//	}

}
