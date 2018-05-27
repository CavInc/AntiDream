package cav.antidream.ui;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import cav.antidream.R;
import cav.antidream.data.models.SoundTrackModel;
import cav.antidream.ui.adapters.SoundTrackAdapter;

/**
 * Created by cav on 18.05.18.
 */

public class SelectSoundAlarmDialog extends DialogFragment implements AdapterView.OnItemClickListener{

    private OnSoundChangeListener mOnSoundChangeListener;
    private ListView mListView;

    private Context mContext;

    public static SelectSoundAlarmDialog newInstance(Context context){

        SelectSoundAlarmDialog dialog = new SelectSoundAlarmDialog();
        return dialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        View v = inflater.inflate(R.layout.select_sound_dialog, null);

        mListView = (ListView) v.findViewById(R.id.select_sound_lv);
        ArrayList<SoundTrackModel> data = getTrack();

        SoundTrackAdapter adapter = new SoundTrackAdapter(mContext,R.layout.sountselect_item,data);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(this);

        return v;
    }

    private ArrayList<SoundTrackModel> getTrack(){
        ArrayList<SoundTrackModel> rec = new ArrayList<>();
        final Uri mediaSrc = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        CursorLoader cursorLoader = new CursorLoader(mContext,mediaSrc, null, null, null,MediaStore.Audio.Media.TITLE);
        Cursor cursor = cursorLoader.loadInBackground();

        Uri playableUri = null;
        int ic = cursor.getCount();
        for (int i=0;i<ic;i++) {
            cursor.moveToPosition(i);
            String _id = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));
            // Дополнительная информация
            String title = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE));
            playableUri = Uri.withAppendedPath(mediaSrc, _id);
            rec.add(new SoundTrackModel(title,playableUri.toString()));
        }

        return rec;
    }

    public void setOnSoundChangeListener(OnSoundChangeListener onSoundChangeListener) {
        mOnSoundChangeListener = onSoundChangeListener;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (mOnSoundChangeListener != null){
            SoundTrackModel rec = (SoundTrackModel) adapterView.getItemAtPosition(position);
            mOnSoundChangeListener.onSoundChange(rec.getTrack(),rec.getFile());
        }
        dismiss();
    }

    public interface OnSoundChangeListener {
        public void onSoundChange(String title,String url);
    }

}
