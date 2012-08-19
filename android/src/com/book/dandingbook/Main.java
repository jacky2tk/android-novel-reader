package com.book.dandingbook;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

public class Main extends Activity {
	private static final String TAG = "Danding_Main";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //移除 Title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        NotificationManager barManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification barMsg = new Notification(R.drawable.ic_launcher,"淡定書城",System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, 
        		new Intent(this,Main.class), 
        		PendingIntent.FLAG_UPDATE_CURRENT);

        barMsg.setLatestEventInfo(this, "我是通知訊息", "我是通知內容", contentIntent);
        barManager.cancelAll();
        barManager.notify(0,barMsg);
        
        findViews();		// 取得所有控制項
		setListeners();		// 設定所有按鈕 Listener
        
    }
	
	private ImageButton btnLogin;
	private ImageButton btnRegister;
	private ImageButton btnBookRead;
	
	// 取得所有控制項
	private void findViews() {
		btnLogin = (ImageButton)findViewById(R.id.btnLogin);
        btnRegister = (ImageButton)findViewById(R.id.btnRegister);
        btnBookRead = (ImageButton)findViewById(R.id.btnBookRead);
	}
	
	// 設定所有按鈕 Listener
	private void setListeners() {
		// 「登入」按鈕功能
        btnLogin.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(Main.this, BookList.class);
				startActivity(intent);
			}
        	
        });
               
        // 「註冊」按鈕功態
        btnRegister.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, AddMem.class);
				startActivity(intent);
			}
		});
        
        // 「離線閱讀」按鈕功能
        btnBookRead.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, BookRead.class);
				startActivity(intent);
			}
		});
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}

