package com.book.dandingbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
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
	private static NotificationManager barManager = null;
	private static Notification barMsg = null;

	public static String strUID = "";
	private String Message = "";

	@Override
	protected void onDestroy() {
		super.onDestroy();
		barManager.cancel(0);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 移除 Title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		findViews(); // 取得所有控制項
		setListeners(); // 設定所有按鈕 Listener

		// 開發 Debug 模式時, 自動填入測試的帳號及密碼
		if (Debug.On) {
			txt_account.setText("a");
			txt_pwd.setText("a");
		}
		// 將程式註冊到通知列
		barManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Intent it_bar = new Intent(Main.this, Main.class);
		it_bar.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		barMsg = new Notification(R.drawable.ic_launcher,
				getString(R.string.app_name), System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				it_bar, PendingIntent.FLAG_UPDATE_CURRENT);

		barMsg.setLatestEventInfo(this, "淡定書城", "Danding Book", contentIntent);
		barManager.cancelAll();
		barManager.notify(0, barMsg);
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		
	}

	private ImageButton btnLogin;
	private ImageButton btnRegister;
	private ImageButton btnBookRead;
	private EditText txt_account;
	private EditText txt_pwd;

	// 取得所有控制項
	private void findViews() {
		btnLogin = (ImageButton) findViewById(R.id.btnLogin);
		btnRegister = (ImageButton) findViewById(R.id.btnRegister);
		btnBookRead = (ImageButton) findViewById(R.id.btnBookRead);
		txt_account = (EditText) findViewById(R.id.edtAccount);
		txt_pwd = (EditText) findViewById(R.id.edtPassword);
	}

	// 設定所有按鈕 Listener
	private void setListeners() {
		// 「登入」按鈕功能
		btnLogin.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Message = "&account=" + txt_account.getText().toString()
						+ "&pwd=" + txt_pwd.getText().toString();
				getMain(Message);
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
				Intent intent = new Intent(Main.this, OffReadList.class);
				startActivity(intent);
			}
		});
	}

	// 「登入」的 callback, 檢查是否登入成功
	final Handler DownloadHandler = new Handler();
	final Runnable DownloadCallback = new Runnable() {
		public void run() {
			// Toast.makeText(AddMem.this, "Download Complete!",
			// Toast.LENGTH_LONG).show();
			Log.d(TAG, "Handler: " + strUID);

			TextView login_warn = (TextView) findViewById(R.id.login_warn);
			login_warn.setText("");
			if (strUID.equals("0")) { // 判斷帳密是否輸入正確
				login_warn.setText(R.string.login_warn);
			} else {
				Intent intent = new Intent(Main.this, BookList.class);
				startActivity(intent);
			}
		}

	};

	// 檢查是否連上網路
	private void getMain(final CharSequence contents) {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		if (Network.haveNetworkConnection(cm)) {
			Thread thread = new Thread() {
				public void run() {
					try {

						if (Debug.On)
							Log.d(TAG, "Sent GET request");
						strUID = Network.getData(getString(R.string.url_host)
								+ getString(R.string.url_agent),
								"case=user_login" + contents);

						if (Debug.On)
							Log.d(TAG, "Callback to main loop");
						DownloadHandler.post(DownloadCallback);
					} catch (Exception e) {
						// Log.e(TAG, e.getMessage());
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
			Toast.makeText(Main.this, "請先連上網路!", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setTitle("注意")
					.setMessage("確定要退出嗎？")
					.setPositiveButton("確定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									barManager.cancel(0);
									android.os.Process
											.killProcess(android.os.Process
													.myPid());
									
									finish();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
								}
							}).show();
			break;
		default:
			break;
		}
		return false;
	}
}
