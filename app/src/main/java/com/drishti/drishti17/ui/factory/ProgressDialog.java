package com.drishti.drishti17.ui.factory;

import android.content.Context;

import com.drishti.drishti17.R;


public class ProgressDialog extends android.app.ProgressDialog{

    Context context;
    boolean initialised = false;

    public ProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    public void showProgressDialog() {
        showProgressDialog(R.string.loading,false);
    }
    public void showProgressDialog(int message_id,boolean isCancelable) {
        if (!initialised) {

            setMessage(context.getString(message_id));
            setCancelable(isCancelable);
            setIndeterminate(true);
            initialised = true;
        }

        show();
    }

    public void hideProgressDialog() {
        if (initialised && isShowing()) {
            hide();
        }
    }
    public void disMissProgressDialog() {
        if (isShowing()) {
            dismiss();
        }
    }


}

