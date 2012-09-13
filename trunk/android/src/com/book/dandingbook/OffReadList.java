package com.book.dandingbook;


import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class OffReadList extends Activity {
	private static final String TAG = "Danding_OffReadList";
	
	private ListView lstBook;			// 顯示小說清單的控制項
	private CustomAdapter adapter;
	
    // 宣告 DBConnection 及 SQLiteDatabase 握柄
	private static DBConnection helper;
	private static SQLiteDatabase db;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        
        lstBook = (ListView)findViewById(R.id.lstBook);
        
        // 初始化小說清單
    	initBookList();
        
        // 取出 off_book 資料表內的書名, 加到 List 內 
        loadData();
  	}
	
	// 初始化小說清單
	private void initBookList() {		
		//實體化CustomAdapter
        adapter = new CustomAdapter((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        
        View ListLabView = adapter.addLabItem("無資料");
        
        lstBook.setAdapter(adapter);
	}
	
	// 取出 off_book 資料表內的書名, 加到 List 內 
	private void loadData() {
		
		adapter.clear();
  		adapter.addSeparator(getString(R.string.off_read_list_title));	// List Title 設定
  		
  		//query(TABLE名稱，欄位陣列)
  		Cursor c = db.query(BookSchema.TABLE_NAME.toString()
  							,new String[]{BookSchema.ID.toString(),BookSchema.BOOK_NAME.toString(),BookSchema.SDCARD.toString()}
  							,null ,null ,null ,null ,null) ;
  		
  		c.moveToFirst() ; //將Cursor移動到第一筆
  		for(int i = 0 ; i < c.getCount(); i++){  //getCount()資料表共有幾筆資料
  			// c.getString(0) --> ID
  			// c.getString(1) --> Book name
  			// c.getString(2) --> Book file
  			View ListLabView = adapter.addLabItem(c.getString(1));
  			ListLabView.setTag(c.getString(2).toString());
  			c.moveToNext(); //將Cursor移動到下一筆
  		}
  		
  		c.close();	// 關閉cursor
  		
  		lstBook.setAdapter(adapter);
		
  		lstBook.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0 ,View view ,int position ,long id) {
				Intent intent = new Intent(OffReadList.this, BookRead.class); //呼叫RoundList頁面
	            intent.putExtra("BOOK_FILENAME", view.getTag().toString() );  //傳入DB的路徑
	            startActivity(intent);	//呼叫Activity
			}
		}) ;
	}
	
	//建立列舉, 方便以後調用
	public enum BookSchema {
		TABLE_NAME("off_book"), 
		ID("b_id"), 
		BOOK_NAME("b_name"), 
		SDCARD("b_sdcard");
		private String Column;
		private BookSchema(String column){  //列舉的建構式只能使用PRIVATE
			this.Column = column;
		}
		public String toString(){
			return Column;
		}
	}
	
   public static class DBConnection extends SQLiteOpenHelper {
    	//因為要給父層類別使用,所以必需宣告成static final
    	
    	private static final String DATABASE_NAME = "danding_book"; //資料庫名稱
        private static final int DATABASE_VERSION = 1;  //版本
        
		public DBConnection(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION); //執行SQLiteOpenHelper的建構式
		}
		//當一開始建立資料庫時的語法, 如果資料庫存在就不會再執行
		@Override
		public void onCreate(SQLiteDatabase db) {
			
			String sql = "CREATE TABLE " + BookSchema.TABLE_NAME.toString() + " (" 
					+ BookSchema.ID.toString()  + " INTEGER primary key autoincrement, " 
					+ BookSchema.BOOK_NAME.toString() + " varchar(20) not null, " 
					+ BookSchema.SDCARD.toString() + " varchar(50) not null"+ ");";
			
			db.execSQL(sql); //將SQL語法丟給資料庫
		}
		
		//資料庫版本有更新時, 需要修改的語法放這
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
    }
	
	public static void connectDB(Context context) {
  		// 可至DDMS -> FileExplorer -> data -> data ->找你的專案package 看有否一個database資料夾，若都沒有建立，就不會有
  		helper = new DBConnection(context); //將DBConnection實體化
  		
  		// 取得可讀寫資料庫
  		db = helper.getWritableDatabase();
	}
    
    // 新增一筆紀錄到資料表
    public static void addData(String bookName, String filename) {		
		ContentValues values = new ContentValues() ;
		values.put(BookSchema.BOOK_NAME.toString(), bookName) ;
		values.put(BookSchema.SDCARD.toString(), filename) ;
		db.insert(BookSchema.TABLE_NAME.toString(), null, values) ;
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}