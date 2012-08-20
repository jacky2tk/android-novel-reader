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
	
	private String strBookDetail = "";	// 存放從 Server 下載的小說清單
	private String strImageFile = "";	// 小說的封面圖片檔名
	private String strBookFile = "";	// 小說檔檔名
	
	private ListView lstBook;			// 顯示小說清單的控制項
	private CustomAdapter adapter;
	private Thread thread;
    
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
					adapter.addLabItem(getString(R.string.book_author) + "：" + aryBookDetail[3]);
					adapter.addLabItem(getString(R.string.book_issue) + "：" + aryBookDetail[4]);
					adapter.addLabItem(getString(R.string.book_image));
					
					if (strImageFile == "") {
						adapter.addLabItem(getString(R.string.detail_noimage));
						adapter.addButton(getString(R.string.detail_download));
						lstBook.setAdapter(adapter);
						return;
					}
					
					//adapter.addImage(aryBookDetail[5]);
					//adapter.addButton("下載檔案");					
					
					//lstBook.setAdapter(adapter);
					
					
					// 下載圖片    		
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
						Toast.makeText(BookDetail.this, "第 " + position + " 個項目", Toast.LENGTH_SHORT).show();
//						Intent intent = new Intent(BookList.this, BookDetail.class);
//						startActivity(intent);
					}
					
				});
			}
		}
    	
    };    
        
    // 下載小說封面圖片到 SD Card 
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
					Toast.makeText(BookDetail.this, "第 " + position + " 個項目", Toast.LENGTH_SHORT).show();
//						Intent intent = new Intent(BookList.this, BookDetail.class);
//						startActivity(intent);
				}
				
			});
		}
    	
    };    
   
    // 從 Server 下載小說清單
	private void getBookDetail(final CharSequence id) {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE); 
    	if (Network.haveNetworkConnection(cm)){
    		
    		// 透過 Server 的 PHP 讀出 MySQL 的資料內容
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
