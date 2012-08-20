package com.book.dandingbook;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddMem extends Activity {
	private static final String TAG = "Danding_AddMem";
	private String strAddMem = "";
	private String Message = "" ;
	private String reply = "" ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mem);
        
        //���o���󱱨
        Button btnAbort = (Button)findViewById(R.id.btnAbort);
        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        final EditText txt_name = (EditText)findViewById(R.id.edtMemName) ;
        final EditText txt_account = (EditText)findViewById(R.id.edtAccount) ;
        final EditText txt_pwd = (EditText)findViewById(R.id.edtPassword) ;
        final EditText txt_pwd2 = (EditText)findViewById(R.id.edtPassword2) ;
		
        btnAbort.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(AddMem.this, Main.class);
				startActivity(intent);
			}
		});
        
        btnRegister.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				TextView pwd_warn = (TextView)findViewById(R.id.pwd_warn) ;
				
				pwd_warn.setText("") ;
				if(!(txt_pwd.getText().toString()).equals((txt_pwd2.getText().toString()))){
					pwd_warn.setText(R.string.pwd_warn) ;
				}
				else{
					Message = "&name="+ txt_name.getText().toString() + "&account=" + txt_account.getText().toString() + "&pwd=" + txt_pwd.getText().toString() ; 
					getAddMem(Message) ;
				}
			}
		});
    }
    
    final Handler DownloadHandler = new Handler();
    final Runnable DownloadCallback = new Runnable() {
		public void run() {
			//Toast.makeText(AddMem.this, "Download Complete!", Toast.LENGTH_LONG).show();
			Log.d(TAG, "Handler: " + strAddMem);
			
			TextView account_warn = (TextView)findViewById(R.id.pwd_warn) ;
			account_warn.setText("") ;
			if(strAddMem.equals("0")){  //�P�_�b���O�_����
				account_warn.setText(R.string.account_warn) ;
			}
			else{
				Button Dialog_btn = new Button(AddMem.this) ;
				Dialog_btn.setText("�T�w") ;
				final Dialog dialog = new Dialog(AddMem.this) ;
				dialog.setTitle("�z���b���w���U���\!") ;				
				dialog.setContentView(Dialog_btn) ;//�]�m���e
				dialog.show() ; //���
				Dialog_btn.setOnClickListener(new OnClickListener() {//���U��ܵ����W�����s������Dialog����
					public void onClick(View v) {
						dialog.dismiss();
						//��ܭ���
						Intent intent = new Intent(AddMem.this, Main.class);
						startActivity(intent);
					}
				});
			}
		}
    	
    };
    
	//�ˬd�O�_�s�W����
    private void getAddMem(final CharSequence contents) {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE); 
    	if (Network.haveNetworkConnection(cm)){
    		Thread thread = new Thread() {
    			public void run() {
    				try {
    					
    					if (Debug.On) Log.d(TAG, "Sent GET request");
    					strAddMem = Network.getData(
    							getString(R.string.url_host) + getString(R.string.url_agent), 
    							"case=user_add"+contents);
    					
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
    		Toast.makeText(AddMem.this, "�Х��s�W����!", Toast.LENGTH_LONG).show();
    	}
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_mem, menu);
        return true;
    }

    
}
