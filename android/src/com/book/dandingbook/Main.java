package com.book.dandingbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	private static final String TAG = "Danding_Main";
	private static NotificationManager barManager = null;
	private static Notification barMsg = null;
	private static final int MENU_ABOUT = Menu.FIRST,
							 MENU_QUIT = Menu.FIRST + 1;

	public static String strUID = "";
	private String Message = "";
	
	private ProgressDialog progress;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		barManager.cancel(0);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ���� Title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		findViews(); // ���o�Ҧ����
		setListeners(); // �]�w�Ҧ����s Listener

		// �N�{�����U��q���C
		barManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Intent it_bar = new Intent(Main.this, Main.class);
		it_bar.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		barMsg = new Notification(R.drawable.ic_launcher,
				getString(R.string.app_name), System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				it_bar, PendingIntent.FLAG_UPDATE_CURRENT);

		barMsg.setLatestEventInfo(this, "�H�w�ѫ�", "Danding Book", contentIntent);
		barManager.cancelAll();
		barManager.notify(0, barMsg);
		
		// �إ߸�Ʈw�s�u
		OffReadList.connectDB(this);
		
		// �إ߭��ָ�Ƨ� (�Y��Ƨ����s�b)
		Network.getSDPath("music");
		
		// �}�o Debug �Ҧ���, �۰ʶ�J���ժ��b���αK�X
		if (Debug.On) {
			txt_account.setText("a");
			txt_pwd.setText("a");
		}		
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

	// ���o�Ҧ����
	private void findViews() {
		btnLogin = (ImageButton) findViewById(R.id.btnLogin);
		btnRegister = (ImageButton) findViewById(R.id.btnRegister);
		btnBookRead = (ImageButton) findViewById(R.id.btnBookRead);
		txt_account = (EditText) findViewById(R.id.edtAccount);
		txt_pwd = (EditText) findViewById(R.id.edtPassword);
	}

	// �]�w�Ҧ����s Listener
	private void setListeners() {
		// �u�n�J�v���s�\��
		btnLogin.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Message = "&account=" + txt_account.getText().toString()
						+ "&pwd=" + txt_pwd.getText().toString();
				getMain(Message);
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
			// Toast.makeText(AddMem.this, "Download Complete!",
			// Toast.LENGTH_LONG).show();
			Log.d(TAG, "Handler: " + strUID);
			progress.dismiss();

			TextView login_warn = (TextView) findViewById(R.id.login_warn);
			login_warn.setText("");
			if (strUID.equals("0")) { // �P�_�b�K�O�_��J���T
				login_warn.setText(R.string.login_warn);
			} else {
				Intent intent = new Intent(Main.this, BookList.class);
				startActivity(intent);
			}
		}

	};

	// �ˬd�O�_�s�W����
	private void getMain(final CharSequence contents) {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		if (Network.haveNetworkConnection(cm)) {
			// ��ܳs�u������
			progress = ProgressDialog.show(
					this, 
					getString(R.string.pgs_title), 
					getString(R.string.pgs_connecting));
			
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
				progress.show();
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
		//getMenuInflater().inflate(R.menu.main, menu);
		menu.addSubMenu(0, MENU_ABOUT, 0, "���󥻵{��")
		.setIcon(android.R.drawable.ic_menu_info_details);
		menu.addSubMenu(0, MENU_QUIT, 0, "���}")
		.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String versionName = "";
		int versionCode;
		
		switch (item.getItemId()) {
		case MENU_ABOUT:
			// ���o�{�������T��
			// �ѦҨӷ�: http://www.wretch.cc/blog/tacor/22070804
			try {
				// versionName �����W�� (��r)
				versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;

				//versionCode �����N�X (���)
				// TODO: �i�z�L�������N�X�P�_�n��O�_�ӧ�s
				versionCode = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionCode;
				

			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			StringBuilder sb = new StringBuilder();
			sb.append("�{���W�١G" + getString(R.string.app_name) + "\n");
			sb.append("�����G" + versionName + "\n");
			
			// �]�w Alert ��ܮ�
			AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
			builder.setTitle("���󥻵{��");
			builder.setMessage(sb.toString());
			
			// ���s�G�T�w
			builder.setPositiveButton("�T�w", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// Do Nothing...
				}
			});
			
			// ���s: ����
			builder.setNegativeButton("����", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_host)));
					startActivity(intent);
				}
			});
			
			builder.show();	
			break;
			
		case MENU_QUIT:
			finish();
			break;
		
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setTitle("�`�N")
				.setMessage("�T�w�n�h�X�ܡH")
				.setPositiveButton("�T�w",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								barManager.cancel(0);
								android.os.Process.killProcess(
										android.os.Process.myPid());								
								finish();
							}
						})
				.setNegativeButton("����",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						})
				.show();
			break;
			
		default:
			break;
		}
		return false;
	}
}
