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
	
	private String strBookList = "";	// 存放從 Server 下載的小說清單
	private ListView lstBook;			// 顯示小說清單的控制項
	private CustomAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        
        lstBook = (ListView)findViewById(R.id.lstBook);
    	
        // 從 Server 下載小說清單
    	getBookList();

    	// 初始化小說清單
    	initBookList();
    }
    
    private ProgressDialog progress;
    
    // 下載小說清單後的 callback, 將小說動態加到清單中 
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
						//Toast.makeText(BookList.this, "第 " + position + " 個項目", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(BookList.this, BookDetail.class);
						//intent.putExtra("ID", String.valueOf(position+1));
						intent.putExtra("ID", view.getTag().toString());
						startActivity(intent);
					}
					
				});
			}
		}
    	
    };
    
    // 從 Server 下載小說清單
	private void getBookList() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE); 
    	if (Network.haveNetworkConnection(cm)){
    		// 透過 Server 的 PHP 讀出 MySQL 的資料內容
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
    		Toast.makeText(BookList.this, "請先連上網路!", Toast.LENGTH_LONG).show();
    	}
	}
	
	// 初始化小說清單
	private void initBookList() {		
		//實體化CustomAdapter
        adapter = new CustomAdapter((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        
        View ListLabView = adapter.addLabItem("無資料");
        
        lstBook.setAdapter(adapter);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list, menu);
        return true;
    }
    
}
