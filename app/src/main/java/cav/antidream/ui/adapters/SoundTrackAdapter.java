package cav.antidream.ui.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cav.antidream.R;
import cav.antidream.data.models.SoundTrackModel;

/**
 * Created by cav on 18.05.18.
 */

public class SoundTrackAdapter extends ArrayAdapter<SoundTrackModel> {
    private LayoutInflater mInflater;
    private int resLayout;

    public SoundTrackAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<SoundTrackModel> objects) {
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
            holder.mTitle = (TextView) row.findViewById(R.id.track_title);
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }

        SoundTrackModel rec = getItem(position);
        holder.mTitle.setText(rec.getTrack());
        return row;
    }

    class ViewHolder {
        public TextView mTitle;
    }
}
