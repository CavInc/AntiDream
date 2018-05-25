package cav.antidream.ui.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import cav.antidream.R;

/**
 * Created by cav on 25.05.18.
 */

public class SetNameDialog extends DialogFragment implements View.OnClickListener {
    private EditText mName;
    private Button mButton;

    private  OnSetNameLisneter mOnSetNameLisneter;

    public SetNameDialog newInstance(){
        SetNameDialog dialog = new SetNameDialog();
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("Имя сигнала");
        View v = inflater.inflate(R.layout.setname_dialog, null);
        mName = (EditText) v.findViewById(R.id.alarm_name);
        mButton = (Button) v.findViewById(R.id.dialog_ok);

        mButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        if (mOnSetNameLisneter!=null) {
            mOnSetNameLisneter.SetName(mName.getText().toString());
        }
        dismiss();
    }

    public void setOnSetNameLisneter(OnSetNameLisneter onSetNameLisneter) {
        mOnSetNameLisneter = onSetNameLisneter;
    }

    public interface OnSetNameLisneter{
        public void SetName(String name);
    }
}
