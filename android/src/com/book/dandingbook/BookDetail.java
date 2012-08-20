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
	
	private String strBookList = "";	// 存放從 Server 下載的小說清單
	private ListView lstBook;			// 顯示小說清單的控制項
	private CustomAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        
        lstBook = (ListView)findViewById(R.id.lstBook);
        
     // 建立 Bundle (解開 Intent 包使用)
        Bundle extras = getIntent().getExtras();
    	
        // 從 Server 下載小說詳細資料
        getBookDetail(extras.getCharSequence("ID"));

    	// 初始化小說清單
    	initBookList();
    }
    
    private ProgressDialog progress;
    
    // 下載小說清單後的 callback, 將小說動態加到清單中 
    final Handler DownloadHandler = new Handler();
    final Runnable DownloadCallback = new Runnable() {

		public void run() {
			//progress.dismiss();
			//edtAccount.setText(GetResult);
			Toast.makeText(BookDetail.this, "Download Complete!", Toast.LENGTH_LONG).show();
			Log.d(TAG, "Handler: " + strBookList);
			
			String[] aryBookList = strBookList.split(",");
			
			if (aryBookList.length > 0) {						
				adapter.clear();
				
				try {
					adapter.addSeparator("書本資料");
					adapter.addListItem2(aryBookList[1], aryBookList[2]);
					adapter.addLabItem("作者：" + aryBookList[3]);
					adapter.addLabItem("出版商：" + aryBookList[4]);
					adapter.addLabItem("小說封面圖案");
					adapter.addImage(aryBookList[5]);
					adapter.addButton("下載檔案");					
					
					lstBook.setAdapter(adapter);
				
				} catch (Exception e) {
					Log.e(TAG, "Count: " + aryBookList.length + "\n" + e.getMessage());
					System.out.println("888888888: " + aryBookList.length + " * " + e.getMessage());
				}
				
				lstBook.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Toast.makeText(BookDetail.this, "第 " + position + " 個項目", Toast.LENGTH_SHORT).show();
//						Intent intent = new Intent(BookList.this, BookDetail.class);
//						startActivity(intent);
					}
					
				});
			}
		}
    	
    };    
   
    // 從 Server 下載小說清單
	private void getBookDetail(final CharSequence id) {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE); 
    	if (Network.haveNetworkConnection(cm)){
    		// 透過 Server 的 PHP 讀出 MySQL 的資料內容
    		Thread thread = new Thread() {
    			public void run() {
    				try {
    					if (Debug.On) Log.d(TAG, "Sent GET request");
    					strBookList = Network.getData(getString(R.string.agent_url), "case=book_detail&b_id=" + id);
    					
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
    		Toast.makeText(BookDetail.this, "請先連上網路!", Toast.LENGTH_LONG).show();
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
