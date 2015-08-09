package com.movie.ui.widget.dialog;

import android.content.Context;
import android.widget.TextView;

import com.ztt.project.R;


public class AlertDialog extends AlertDialogBase
{
    private TextView messageView;
    
    /**
     * 
     * 构�?�函数�??
     * 不再提示CheckBox默认为隐�?
     * 
     * @param context
     */
    public AlertDialog(Context context)
    {
        super(context);
        initView(R.layout.alertdialog);
        messageView = (TextView) alertDialog.findViewById(R.id.message);
    }
    
    public void setMessage(int resId)
    {
        messageView.setText(resId);
    }

    public void setMessage(String message)
    {
        messageView.setText(message);
    }
    
}