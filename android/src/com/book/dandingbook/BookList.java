package com.book.dandingbook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class BookList extends Activity {
	private static final String TAG = "Danding_BookList";
	
	private String strBookList = "";	// �s��q Server �U�����p���M��
	private ListView lstBook;			// ��ܤp���M�檺���
	private CustomAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        
        lstBook = (ListView)findViewById(R.id.lstBook);
    	
        // �q Server �U���p���M��
    	getBookList();

    	// ��l�Ƥp���M��
    	initBookList();
    }
    
    private ProgressDialog progress;
    
    // �U���p���M��᪺ callback, �N�p���ʺA�[��M�椤 
    final Handler DownloadHandler = new Handler();
    final Runnable DownloadCallback = new Runnable() {

		public void run() {
			//progress.dismiss();
			Log.d(TAG, "Handler: " + strBookList);
			
			String[] aryBookList = strBookList.split(",");
			
			if (aryBookList.length > 0) {								
				adapter.clear();
				
				adapter.addSeparator(getString(R.string.book_list_title));
				for (int i = 0; i < aryBookList.length; i += 2) {
					View ListLabView = adapter.addLabItem(aryBookList[i+1]);
					ListLabView.setTag(aryBookList[i]);
				}
				lstBook.setAdapter(adapter);
				
				lstBook.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						//Toast.makeText(BookList.this, "�� " + position + " �Ӷ���", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(BookList.this, BookDetail.class);
						//intent.putExtra("ID", String.valueOf(position+1));
						intent.putExtra("ID", view.getTag().toString());
						startActivity(intent);
					}
					
				});
			}
		}
    	
    };
    
    // �q Server �U���p���M��
	private void getBookList() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE); 
    	if (Network.haveNetworkConnection(cm)){
    		// �z�L Server �� PHP Ū�X MySQL ����Ƥ��e
    		Thread thread = new Thread() {
    			public void run() {
    				try {
    					if (Debug.On) Log.d(TAG, "Sent GET request");
    					strBookList = Network.getData(
    							getString(R.string.url_host) + getString(R.string.url_agent), 
    							"case=book_list");

    					if (Debug.On) Log.d(TAG, "Callback to main loop");
    					DownloadHandler.post(DownloadCallback);
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
    		Toast.makeText(BookList.this, "�Х��s�W����!", Toast.LENGTH_LONG).show();
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
