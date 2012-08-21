package com.book.dandingbook;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	private static final String TAG = "Danding_Main";
	private String strMain = "";
	private String Message = "" ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // ���� Title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        // �N�{�����U��q���C
        NotificationManager barManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification barMsg = new Notification(
        		R.drawable.ic_launcher,
        		getString(R.string.app_name),
        		System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, 
        		new Intent(this,Main.class), 
        		PendingIntent.FLAG_UPDATE_CURRENT);

        barMsg.setLatestEventInfo(this, "�ڬO�q���T��", "�ڬO�q�����e", contentIntent);
        barManager.cancelAll();
        barManager.notify(0,barMsg);
        
        findViews();		// ���o�Ҧ����
		setListeners();		// �]�w�Ҧ����s Listener
		
		// �}�o Debug �Ҧ���, �۰ʶ�J���ժ��b���αK�X
		if (Debug.On) {
			txt_account.setText("a");
			txt_pwd.setText("a");
		}
        
    }
	
	private ImageButton btnLogin;
	private ImageButton btnRegister;
	private ImageButton btnBookRead;
	private EditText txt_account ;
	private EditText txt_pwd ;
	
	// ���o�Ҧ����
	private void findViews() {
		btnLogin = (ImageButton)findViewById(R.id.btnLogin);
        btnRegister = (ImageButton)findViewById(R.id.btnRegister);
        btnBookRead = (ImageButton)findViewById(R.id.btnBookRead);
        txt_account = (EditText)findViewById(R.id.edtAccount) ;
        txt_pwd = (EditText)findViewById(R.id.edtPassword) ;
	}
	
	// �]�w�Ҧ����s Listener
	private void setListeners() {
		// �u�n�J�v���s�\��
        btnLogin.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				Message = "&account=" + txt_account.getText().toString() + "&pwd=" + txt_pwd.getText().toString() ; 
				getMain(Message) ;
			}
        	
        });
               
        // �u���U�v���s�\�A
        btnRegister.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, AddMem.class);
				startActivity(intent);
			}
		});
        
        // �u���u�\Ū�v���s�\��
        btnBookRead.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, OffReadList.class);
				startActivity(intent);
			}
		});
	}
	
	// �u�n�J�v�� callback, �ˬd�O�_�n�J���\
	final Handler DownloadHandler = new Handler();
    final Runnable DownloadCallback = new Runnable() {
		public void run() {
			//Toast.makeText(AddMem.this, "Download Complete!", Toast.LENGTH_LONG).show();
			Log.d(TAG, "Handler: " + strMain);
			
			TextView login_warn = (TextView)findViewById(R.id.login_warn) ;
			login_warn.setText("") ;
			if(strMain.equals("0")){  //�P�_�b�K�O�_��J���T
				login_warn.setText(R.string.login_warn) ;
			}
			else{
				Intent intent = new Intent(Main.this, BookList.class);
				startActivity(intent);
			}
		}
    	
    };
	//�ˬd�O�_�s�W����
    private void getMain(final CharSequence contents) {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE); 
    	if (Network.haveNetworkConnection(cm)){
    		Thread thread = new Thread() {
    			public void run() {
    				try {
    					
    					if (Debug.On) Log.d(TAG, "Sent GET request");
    					strMain = Network.getData(
    							getString(R.string.url_host) + getString(R.string.url_agent), 
    							"case=user_login"+contents);
    					
    					if (Debug.On) Log.d(TAG, "Callback to main loop");
    					DownloadHandler.post(DownloadCallback);
    				} catch (Exception e) {
    					//Log.e(TAG, e.getMessage());
    					System.out.println(e.getMessage());
    				}
    			}
    		};
    		try {
    			Log.d(TAG, "AddMem_Connectivity");
	    	    thread.start();
    		} catch (Exception e) {
    			Log.e(TAG, e.getMessage());
    			System.out.println(e.getMessage());
    		}
    	} else {
    		Toast.makeText(Main.this, "�Х��s�W����!", Toast.LENGTH_LONG).show();
    	}
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}

