package com.book.dandingbook;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BookDetail extends Activity {
	private static final String TAG = "Danding_BookDetail";
	
	private String strBookDetail = "";	// �s��q Server �U�����p���M��
	private String strImageFile = "";	// �p�����ʭ��Ϥ��ɦW
	private String strBookFile = "";	// �p�����ɦW
	
	private ListView lstBook;			// ��ܤp���M�檺���
	private CustomAdapter adapter;
	private Thread thread;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        
        lstBook = (ListView)findViewById(R.id.lstBook);
        
     // �إ� Bundle (�Ѷ} Intent �]�ϥ�)
        Bundle extras = getIntent().getExtras();
    	
        // �q Server �U���p���ԲӸ��
        getBookDetail(extras.getCharSequence("ID"));

    	// ��l�Ƥp���M��
    	initBookList();
    }
    
    private ProgressDialog progress;
    
    // �U���p���M��᪺ callback, �N�p���ʺA�[��M�椤 
    final Handler BookDetailHandler = new Handler();
    final Runnable BookDetailCallback = new Runnable() {

		public void run() {
			//progress.dismiss();
			//Toast.makeText(BookDetail.this, "Download Complete!", Toast.LENGTH_LONG).show();
			Log.d(TAG, "Handler: " + strBookDetail);
			
			String[] aryBookDetail = strBookDetail.split(",");
			strImageFile = aryBookDetail[5];
			
			if (aryBookDetail.length > 0) {						
				adapter.clear();
				
				try {
					adapter.addSeparator(getString(R.string.book_detail_title));
					adapter.addListItem2(aryBookDetail[1], aryBookDetail[2]);
					adapter.addLabItem(getString(R.string.book_author) + "�G" + aryBookDetail[3]);
					adapter.addLabItem(getString(R.string.book_issue) + "�G" + aryBookDetail[4]);
					adapter.addLabItem(getString(R.string.book_image));
					
					if (strImageFile == "") {
						adapter.addLabItem(getString(R.string.detail_noimage));
						adapter.addButton(getString(R.string.detail_download));
						lstBook.setAdapter(adapter);
						return;
					}
					
					//adapter.addImage(aryBookDetail[5]);
					//adapter.addButton("�U���ɮ�");					
					
					//lstBook.setAdapter(adapter);
					
					
					// �U���Ϥ�    		
					thread = new Thread() {
						public void run() {
							try {
								if (Debug.On) Log.d(TAG, "Sent GET request");
								Network.getFile(
										getString(R.string.url_host) + getString(R.string.url_image), 
										strImageFile);

								if (Debug.On) Log.d(TAG, "Callback to main loop");
								DownloadImageHandler.post(DownloadImageCallback);
							} catch (Exception e) {
								Log.e(TAG, e.getMessage());
								System.out.println(e.getMessage());
							}
						}
					};
					
					try {
						//progress.show();
					    thread.start();
					} catch (Exception e) {
						Log.e(TAG, e.getMessage());
						System.out.println(e.getMessage());
					}
				
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
        
    // �U���p���ʭ��Ϥ��� SD Card 
    final Handler DownloadImageHandler = new Handler();
    final Runnable DownloadImageCallback = new Runnable() {

		public void run() {
			//progress.dismiss();
			//Toast.makeText(BookDetail.this, "Download Complete!", Toast.LENGTH_LONG).show();
			Log.d(TAG, "Handler: DownloadImage");
			
			try {
				adapter.addImage(strImageFile);
				adapter.addButton(getString(R.string.detail_download));
				
				lstBook.setAdapter(adapter);
			
			} catch (Exception e) {
				Log.e(TAG, "ImageFileName: " + strImageFile + "\n" + e.getMessage());
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
    	
    };    
   
    // �q Server �U���p���M��
	private void getBookDetail(final CharSequence id) {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE); 
    	if (Network.haveNetworkConnection(cm)){
    		
    		// �z�L Server �� PHP Ū�X MySQL ����Ƥ��e
    		thread = new Thread() {
    			public void run() {
    				try {
    					if (Debug.On) Log.d(TAG, "Sent GET request");
    					strBookDetail = Network.getData(
    							getString(R.string.url_host) + getString(R.string.url_agent), 
    							"case=book_detail&b_id=" + id);
    					
    					if (Debug.On) Log.d(TAG, "Callback to main loop");
    					BookDetailHandler.post(BookDetailCallback);
    				} catch (Exception e) {
    					Log.e(TAG, e.getMessage());
    					System.out.println(e.getMessage());
    				}
    			}
    		};
    		
    		try {
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
	
	// ��l�Ƥp���M��
	private void initBookList() {		
		//�����CustomAdapter
        adapter = new CustomAdapter((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        
        View ListLabView = adapter.addLabItem("�L���");
        
        lstBook.setAdapter(adapter);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list, menu);
        return true;
    }
    
}
