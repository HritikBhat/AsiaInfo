package com.hritik.asiainfo.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.hritik.asiainfo.Database.DBInterface;
import com.hritik.asiainfo.R;

import static android.content.Context.MODE_PRIVATE;

public class DeleteDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    private DBInterface dbInterface;
    private Context context;
    SharedPreferences sharedPreferences;


    public DeleteDialog(@NonNull Context context, DBInterface dbInterface) {
        super(context);
        this.dbInterface=dbInterface;
        this.context=context;
        this.sharedPreferences= context.getSharedPreferences("MyShared",MODE_PRIVATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.deletedialog_layout);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                dbInterface.deleteAll();
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        Glide.get(context).clearDiskCache();
                        sharedPreferences.edit().putBoolean("offlineDataPresentStatus",false).apply();
                    }
                }.start();

                dismiss();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
