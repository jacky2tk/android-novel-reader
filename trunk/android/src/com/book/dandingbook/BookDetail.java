package com.book.dandingbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.book.dandingbook.OffReadList.BookSchema;

public class BookDetail extends Activity {
	private static final String TAG = "Danding_BookDetail";
	
	private String strBookDetail = "";	// �s��q Server �U�����p���M��
	private String[] aryBookDetail;		// �p���ԲӸ�ƪ��}�C
	
	private ListView lstBook;			// ��ܤp���M�檺���
	private CustomAdapter adapter;
	private Thread thread;
	private ProgressDialog progress;		// �U���ɮפ����i�״���
	private ConnectivityManager cm;			// �s�u�޲z�{��
	
	private final int GET_BOOK_DETAIL = 1;
	private final int DOWNLOAD_BOOK_FILE = 2;
	
	// aryBookDetail �}�C Index:
	// Server �^�Ǥp���ԲӸ����춶��: 
	// �s��[0],�ѦW[1],²��[2],�@��[3],�X����[4],�p���ɸ��|[5],�p���ʭ��Ϯ׸��|[6]
	private final int B_ID		= 0;
	private final int B_NAME	= 1;
	private final int B_INTRO	= 2;
	private final int B_AUTHOR	= 3;
	private final int B_ISSUE	= 4;
	private final int B_PATH	= 5;
	private final int B_IMAGE	= 6;
	private final int B_TOTAL_FIELDS	= 7;	// �`���ƶq
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        
        lstBook = (ListView)findViewById(R.id.lstBook);
        
        // �إ� Bundle (�Ѷ} Intent �]�ϥ�)
        Bundle extras = getIntent().getExtras();
        
    	// ��l�Ƥp���M��
    	initBookList();
    	
        // �q Server �U���p���ԲӸ��
    	cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    	if (Network.haveNetworkConnection(cm)){
     		// ���Ū����Ƥ�����
     		progress = ProgressDialog.show(
					this, 
					getString(R.string.pgs_title), 
					getString(R.string.pgs_reading));
     		
     		sendGetRequest(GET_BOOK_DETAIL, extras.getCharSequence("ID").toString());
    	}
    }
    
    // ��l�Ƥp���M��
 	private void initBookList() {		
 		//�����CustomAdapter
         adapter = new CustomAdapter((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE));
         
         View ListLabView = adapter.addLabItem("�L���");
         
         lstBook.setAdapter(adapter);
 	}
    
    private void sendGetRequest(final int fun, final String params) {
     	if (Network.haveNetworkConnection(cm)){
     		// �z�L Server �� PHP Ū�X MySQL ����Ƥ��e
     		thread = new Thread() {
     			public void run() {
     				try {
     					switch (fun) {
	     					case GET_BOOK_DETAIL:	// ���o�p���ԲӸ��
	     						
		     					if (Debug.On) Log.d(TAG, "Sent request: Get detail");
		     					strBookDetail = Network.getData(
		     							getString(R.string.url_host) + getString(R.string.url_agent), 
		     							"case=book_detail&b_id=" + params);
		     					
		     					// �N���쪺�r���Ʃ�}�C, �]�w�}�C���������n�� B_TOTAL_FIELDS ��
		     					// �קK�����ɳQ split ����, �y����쪺�첾
		     					aryBookDetail = strBookDetail.split(",", B_TOTAL_FIELDS);
		     					if (Debug.On) Log.d(TAG, "[BookDetail Handler] Receive: " + strBookDetail);
		     					if (Debug.On) Log.d(TAG, "[BookDetail Handler] Split Count: " + aryBookDetail.length);
		     					
		     					// �U���p���Ϯ�
		     					if (aryBookDetail[B_IMAGE] != "") {
		     						if (Debug.On) Log.d(TAG, "Sent request: Get image file '" + aryBookDetail[B_IMAGE] + "'");
			     					Network.getFile(
		     								getString(R.string.url_host) + getString(R.string.url_image), 
		     								aryBookDetail[B_IMAGE]);
		     					}

		     					// ���� Callback �禡
	     						if (Debug.On) Log.d(TAG, "Callback: Get book detail");
	     						BookDetailHandler.post(BookDetailCallback);
		    					break;
	     						
	     					case DOWNLOAD_BOOK_FILE:	// �U���p���ɮ�
	     						
	     						// �e�X�U���� GET Request
	     						Network.getData(
	     								getString(R.string.url_host) + getString(R.string.url_agent), 
	     								"case=book_download&uid=" + Main.strUID + "&bkid=" + aryBookDetail[B_ID]);
	     						
	     						// �U���ɮ�
	     						Network.getFile(
	     								getString(R.string.url_host) + getString(R.string.url_book), 
	     								params);

	     						if (Debug.On) Log.d(TAG, "Callback to main loop");
	     						DownloadBookHandler.post(DownloadBookCallback);
	     						break;
     					}
     				} catch (Exception e) {
     					Log.e(TAG, e.getMessage());
     					System.out.println(e.getMessage());
     				}
     			}
     		};
     		
     		try { 	    		
 	    		Log.d(TAG, "Show Progress Dialog.....");
 	    		//progress.show();
 	    	    thread.start();
     		} catch (Exception e) {
     			Log.e(TAG, e.getMessage());
     			System.out.println(e.getMessage());
     		}
     		
     	} else {
     		Toast.makeText(BookDetail.this, "�Х��s�W����!", Toast.LENGTH_LONG).show();
     	}
 	}
    
    // ------------------------------------------------------------------------------------------
    // 1. �q Server �U���p���M�檺 Callback
    // ------------------------------------------------------------------------------------------
    
    // �U���p���M��᪺ callback, �N�p���ʺA�[��M�椤 
    final Handler BookDetailHandler = new Handler();
    final Runnable BookDetailCallback = new Runnable() {

		public void run() {
			progress.dismiss();
			//Toast.makeText(BookDetail.this, "Download Complete!", Toast.LENGTH_LONG).show();			
			
			if (aryBookDetail.length > 0) {				
				adapter.clear();
				
				try {
					// ��ܤp���U�����
					adapter.addSeparator(getString(R.string.book_detail_title));
					adapter.addListItem2(aryBookDetail[B_NAME], aryBookDetail[B_INTRO]);					// �ѦW + ²��
					adapter.addLabItem(getString(R.string.book_author) + "�G" + aryBookDetail[B_AUTHOR]);	// �@��
					adapter.addLabItem(getString(R.string.book_issue) + "�G" + aryBookDetail[B_ISSUE]);		// �X����
					
					if (aryBookDetail[B_IMAGE] == "") {						
						// �Y�S���p���ʭ����ɥi�U��, �h���L���ɪ��U��, �H��r�T�����N						
						// �p���ʭ��Ϯ�(���D): �|�L�Ϥ�
						adapter.addLabItem(getString(R.string.book_image) + "�G" + getString(R.string.detail_noimage));					
					} else {
						// ��ܤp���ʭ��Ϯ�
						adapter.addLabItem(getString(R.string.book_image));		// �p���ʭ��Ϯ�(���D)
						adapter.addImage(aryBookDetail[B_IMAGE].toString());	// �p���ʭ��Ϯ�
					}
					
					// ���s:�p���U��
					adapter.addButton(getString(R.string.detail_download), 
						new OnClickListener() {

						   public void onClick(View v) {
							   progress = ProgressDialog.show(
	     								BookDetail.this, 
	     								"�U���p����", 
	     								"�U�� '" + aryBookDetail[B_NAME] + "' ���A�еy��...");
	     						
							   Log.d(TAG, "Progress Dialog.....");
	     						
							   sendGetRequest(DOWNLOAD_BOOK_FILE, aryBookDetail[B_PATH]);
						   }
					
				       }
					);	
					
					lstBook.setAdapter(adapter);
				
				} catch (Exception e) {
					Log.e(TAG, "Count: " + aryBookDetail.length + "\n" + e.getMessage());
				}				
				
				lstBook.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Toast.makeText(BookDetail.this, "�� " + position + " �Ӷ���", Toast.LENGTH_SHORT).show();
//						Intent intent = new Intent(BookList.this, BookDetail.class);
//						startActivity(intent);
					}
					
				});
			}
		}
    	
    };    
    
    
    // ------------------------------------------------------------------------------------------
    // 2. Download �p����r�ɨ� SD Card �� Callback 
    // ------------------------------------------------------------------------------------------
    final Handler DownloadBookHandler = new Handler();
    final Runnable DownloadBookCallback = new Runnable() {

		public void run() {
			Log.d(TAG, "Handler: DownloadBook");
			progress.dismiss();
			Toast.makeText(BookDetail.this, "�U������!", Toast.LENGTH_LONG).show();
			
			// �W�[��u�w�U������Ʈw�v
			OffReadList.addData(aryBookDetail[B_NAME], aryBookDetail[B_PATH]);

			// �]�w Alert ��ܮ�
			AlertDialog.Builder builder = new AlertDialog.Builder(BookDetail.this);
			builder.setTitle("�U���ɮק���");
			builder.setMessage("�U�� '" + aryBookDetail[B_NAME] + "' �ɮפw�g�����A�п�ܤU�@�B�ʧ@�H");		
			
			// ���s: �\Ū
			builder.setPositiveButton(getString(R.string.detail_read), new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(BookDetail.this, BookRead.class);
					intent.putExtra("SDcard", aryBookDetail[B_PATH]);
					startActivity(intent);
				}
			});
			
			// ���s: �U���䥦
			builder.setNegativeButton(getString(R.string.detail_others), new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			
			builder.show();		
		}
    }; 
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list, menu);
        return true;
    }
    
    /*private class sameClickFunc implements OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
    
    }*/
}
