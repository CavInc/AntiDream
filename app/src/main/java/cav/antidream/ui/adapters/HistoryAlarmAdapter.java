package cav.antidream.ui.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cav.antidream.R;
import cav.antidream.data.models.AlarmModel;
import cav.antidream.utils.Utils;

/**
 * Created by cav on 17.05.18.
 */

public class HistoryAlarmAdapter extends ArrayAdapter<AlarmModel> {
    private LayoutInflater mInflater;
    private int resLayout;

    private boolean lockRec = true;

    private HistoryAlarmCheckChange mHistoryAlarmCheckChange;


    public HistoryAlarmAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<AlarmModel> objects) {
        super(context, resource, objects);
        resLayout = resource;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        if (row == null) {
            row = mInflater.inflate(resLayout, parent, false);
            holder = new ViewHolder();
            holder.mDateTime = (TextView) row.findViewById(R.id.item_datetime);
            holder.mTitle = (TextView) row.findViewById(R.id.item_title);
            holder.mStatus = (TextView) row.findViewById(R.id.item_mode);
            holder.mSwitch = (SwitchCompat) row.findViewById(R.id.switch1);
            holder.mSwitch.setOnCheckedChangeListener(mChangeListener);
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }

        AlarmModel record = getItem(position);
        holder.mTitle.setText(record.getAlarmName());
        holder.mDateTime.setText(Utils.dateToStr("HH:mm  EEE,",record.getAlarmDate()));
        holder.mStatus.setText((record.isUsed() ? "Открыто":"Закрыто"));
        lockRec = true;
        holder.mSwitch.setChecked(record.isUsed());
        holder.mSwitch.setTag(position);
        lockRec = false;
        return row;

    }

    public void setData(ArrayList<AlarmModel> model) {
        this.clear();
        this.addAll(model);
    }


    CompoundButton.OnCheckedChangeListener mChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Log.d("HAD","Statr : "+b);
            if (compoundButton.getTag() == null) return;
            if (lockRec) return;

            int position = (int) compoundButton.getTag();
            Log.d("HAD","POS :"+position);
            if (mHistoryAlarmCheckChange != null){
                AlarmModel data = HistoryAlarmAdapter.this.getItem(position);
                HistoryAlarmAdapter.this.getItem(position).setUsed(b);
                mHistoryAlarmCheckChange.CheckChange(data,b);
            }
        }
    };

    public void setHistoryAlarmCheckChange(HistoryAlarmCheckChange historyAlarmCheckChange) {
        mHistoryAlarmCheckChange = historyAlarmCheckChange;
    }

    public interface HistoryAlarmCheckChange {
        public void CheckChange(AlarmModel model,boolean mode);
    }

    class ViewHolder{
        public TextView mDateTime;
        public TextView mTitle;
        public TextView mStatus;
        public SwitchCompat mSwitch;
    }
}
