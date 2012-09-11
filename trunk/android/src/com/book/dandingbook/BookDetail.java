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
	
	private String strBookDetail = "";	// 存放從 Server 下載的小說清單
	private String[] aryBookDetail;		// 小說詳細資料的陣列
	
	private ListView lstBook;			// 顯示小說清單的控制項
	private CustomAdapter adapter;
	private Thread thread;
	private ProgressDialog progress;		// 下載檔案中的進度提示
	private ConnectivityManager cm;			// 連線管理程式
	
	private final int GET_BOOK_DETAIL = 1;
	private final int DOWNLOAD_BOOK_FILE = 2;
	
	// aryBookDetail 陣列 Index:
	// Server 回傳小說詳細資料欄位順序: 
	// 編號[0],書名[1],簡介[2],作者[3],出版商[4],小說檔路徑[5],小說封面圖案路徑[6]
	private final int B_ID		= 0;
	private final int B_NAME	= 1;
	private final int B_INTRO	= 2;
	private final int B_AUTHOR	= 3;
	private final int B_ISSUE	= 4;
	private final int B_PATH	= 5;
	private final int B_IMAGE	= 6;
	private final int B_TOTAL_FIELDS	= 7;	// 總欄位數量
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        
        lstBook = (ListView)findViewById(R.id.lstBook);
        
        // 建立 Bundle (解開 Intent 包使用)
        Bundle extras = getIntent().getExtras();
        
    	// 初始化小說清單
    	initBookList();
    	
        // 從 Server 下載小說詳細資料
    	cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    	if (Network.haveNetworkConnection(cm)){
     		// 顯示讀取資料中視窗
     		progress = ProgressDialog.show(
					this, 
					getString(R.string.pgs_title), 
					getString(R.string.pgs_reading));
     		
     		sendGetRequest(GET_BOOK_DETAIL, extras.getCharSequence("ID").toString());
    	}
    }
    
    // 初始化小說清單
 	private void initBookList() {		
 		//實體化CustomAdapter
         adapter = new CustomAdapter((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE));
         
         View ListLabView = adapter.addLabItem("無資料");
         
         lstBook.setAdapter(adapter);
 	}
    
    private void sendGetRequest(final int fun, final String params) {
     	if (Network.haveNetworkConnection(cm)){
     		// 透過 Server 的 PHP 讀出 MySQL 的資料內容
     		thread = new Thread() {
     			public void run() {
     				try {
     					switch (fun) {
	     					case GET_BOOK_DETAIL:	// 取得小說詳細資料
	     						
		     					if (Debug.On) Log.d(TAG, "Sent request: Get detail");
		     					strBookDetail = Network.getData(
		     							getString(R.string.url_host) + getString(R.string.url_agent), 
		     							"case=book_detail&b_id=" + params);
		     					
		     					// 將收到的字串資料拆成陣列, 設定陣列元素必須要有 B_TOTAL_FIELDS 個
		     					// 避免空欄位時被 split 忽略, 造成欄位的位移
		     					aryBookDetail = strBookDetail.split(",", B_TOTAL_FIELDS);
		     					if (Debug.On) Log.d(TAG, "[BookDetail Handler] Receive: " + strBookDetail);
		     					if (Debug.On) Log.d(TAG, "[BookDetail Handler] Split Count: " + aryBookDetail.length);
		     					
		     					// 下載小說圖案
		     					if (aryBookDetail[B_IMAGE] != "") {
		     						if (Debug.On) Log.d(TAG, "Sent request: Get image file '" + aryBookDetail[B_IMAGE] + "'");
			     					Network.getFile(
		     								getString(R.string.url_host) + getString(R.string.url_image), 
		     								aryBookDetail[B_IMAGE]);
		     					}

		     					// 執行 Callback 函式
	     						if (Debug.On) Log.d(TAG, "Callback: Get book detail");
	     						BookDetailHandler.post(BookDetailCallback);
		    					break;
	     						
	     					case DOWNLOAD_BOOK_FILE:	// 下載小說檔案
	     						
	     						// 送出下載的 GET Request
	     						Network.getData(
	     								getString(R.string.url_host) + getString(R.string.url_agent), 
	     								"case=book_download&uid=" + Main.strUID + "&bkid=" + aryBookDetail[B_ID]);
	     						
	     						// 下載檔案
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
     		Toast.makeText(BookDetail.this, "請先連上網路!", Toast.LENGTH_LONG).show();
     	}
 	}
    
    // ------------------------------------------------------------------------------------------
    // 1. 從 Server 下載小說清單的 Callback
    // ------------------------------------------------------------------------------------------
    
    // 下載小說清單後的 callback, 將小說動態加到清單中 
    final Handler BookDetailHandler = new Handler();
    final Runnable BookDetailCallback = new Runnable() {

		public void run() {
			progress.dismiss();
			//Toast.makeText(BookDetail.this, "Download Complete!", Toast.LENGTH_LONG).show();			
			
			if (aryBookDetail.length > 0) {				
				adapter.clear();
				
				try {
					// 顯示小說各項資料
					adapter.addSeparator(getString(R.string.book_detail_title));
					adapter.addListItem2(aryBookDetail[B_NAME], aryBookDetail[B_INTRO]);					// 書名 + 簡介
					adapter.addLabItem(getString(R.string.book_author) + "：" + aryBookDetail[B_AUTHOR]);	// 作者
					adapter.addLabItem(getString(R.string.book_issue) + "：" + aryBookDetail[B_ISSUE]);		// 出版社
					
					if (aryBookDetail[B_IMAGE] == "") {						
						// 若沒有小說封面圖檔可下載, 則跳過圖檔的下載, 以文字訊息取代						
						// 小說封面圖案(標題): 尚無圖片
						adapter.addLabItem(getString(R.string.book_image) + "：" + getString(R.string.detail_noimage));					
					} else {
						// 顯示小說封面圖案
						adapter.addLabItem(getString(R.string.book_image));		// 小說封面圖案(標題)
						adapter.addImage(aryBookDetail[B_IMAGE].toString());	// 小說封面圖案
					}
					
					// 按鈕:小說下載
					adapter.addButton(getString(R.string.detail_download), 
						new OnClickListener() {

						   public void onClick(View v) {
							   progress = ProgressDialog.show(
	     								BookDetail.this, 
	     								"下載小說檔", 
	     								"下載 '" + aryBookDetail[B_NAME] + "' 中，請稍後...");
	     						
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
						Toast.makeText(BookDetail.this, "第 " + position + " 個項目", Toast.LENGTH_SHORT).show();
//						Intent intent = new Intent(BookList.this, BookDetail.class);
//						startActivity(intent);
					}
					
				});
			}
		}
    	
    };    
    
    
    // ------------------------------------------------------------------------------------------
    // 2. Download 小說文字檔到 SD Card 的 Callback 
    // ------------------------------------------------------------------------------------------
    final Handler DownloadBookHandler = new Handler();
    final Runnable DownloadBookCallback = new Runnable() {

		public void run() {
			Log.d(TAG, "Handler: DownloadBook");
			progress.dismiss();
			Toast.makeText(BookDetail.this, "下載完成!", Toast.LENGTH_LONG).show();
			
			// 增加到「已下載的資料庫」
			OffReadList.addData(aryBookDetail[B_NAME], aryBookDetail[B_PATH]);

			// 設定 Alert 對話框
			AlertDialog.Builder builder = new AlertDialog.Builder(BookDetail.this);
			builder.setTitle("下載檔案完成");
			builder.setMessage("下載 '" + aryBookDetail[B_NAME] + "' 檔案已經完成，請選擇下一步動作？");		
			
			// 按鈕: 閱讀
			builder.setPositiveButton(getString(R.string.detail_read), new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(BookDetail.this, BookRead.class);
					intent.putExtra("SDcard", aryBookDetail[B_PATH]);
					startActivity(intent);
				}
			});
			
			// 按鈕: 下載其它
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
