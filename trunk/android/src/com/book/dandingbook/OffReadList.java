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
	
	private ListView lstBook;			// ��ܤp���M�檺���
	private CustomAdapter adapter;
	
    // �ŧi DBConnection �� SQLiteDatabase ���`
	private static DBConnection helper;
	private static SQLiteDatabase db;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        
        lstBook = (ListView)findViewById(R.id.lstBook);
        
        // ��l�Ƥp���M��
    	initBookList();
        
        // ���X off_book ��ƪ����ѦW, �[�� List �� 
        loadData();
  	}
	
	// ��l�Ƥp���M��
	private void initBookList() {		
		//�����CustomAdapter
        adapter = new CustomAdapter((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        
        View ListLabView = adapter.addLabItem("�L���");
        
        lstBook.setAdapter(adapter);
	}
	
	// ���X off_book ��ƪ����ѦW, �[�� List �� 
	private void loadData() {
		
		adapter.clear();
  		adapter.addSeparator(getString(R.string.off_read_list_title));	// List Title �]�w
  		
  		//query(TABLE�W�١A���}�C)
  		Cursor c = db.query(BookSchema.TABLE_NAME.toString()
  							,new String[]{BookSchema.ID.toString(),BookSchema.BOOK_NAME.toString(),BookSchema.SDCARD.toString()}
  							,null ,null ,null ,null ,null) ;
  		
  		c.moveToFirst() ; //�NCursor���ʨ�Ĥ@��
  		for(int i = 0 ; i < c.getCount(); i++){  //getCount()��ƪ�@���X�����
  			// c.getString(0) --> ID
  			// c.getString(1) --> Book name
  			// c.getString(2) --> Book file
  			View ListLabView = adapter.addLabItem(c.getString(1));
  			ListLabView.setTag(c.getString(2).toString());
  			c.moveToNext(); //�NCursor���ʨ�U�@��
  		}
  		
  		c.close();	// ����cursor
  		
  		lstBook.setAdapter(adapter);
		
  		lstBook.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0 ,View view ,int position ,long id) {
				Intent intent = new Intent(OffReadList.this, BookRead.class); //�I�sRoundList����
	            intent.putExtra("BOOK_FILENAME", view.getTag().toString() );  //�ǤJDB�����|
	            startActivity(intent);	//�I�sActivity
			}
		}) ;
	}
	
	//�إߦC�|, ��K�H��ե�
	public enum BookSchema {
		TABLE_NAME("off_book"), 
		ID("b_id"), 
		BOOK_NAME("b_name"), 
		SDCARD("b_sdcard");
		private String Column;
		private BookSchema(String column){  //�C�|���غc���u��ϥ�PRIVATE
			this.Column = column;
		}
		public String toString(){
			return Column;
		}
	}
	
   public static class DBConnection extends SQLiteOpenHelper {
    	//�]���n�����h���O�ϥ�,�ҥH���ݫŧi��static final
    	
    	private static final String DATABASE_NAME = "danding_book"; //��Ʈw�W��
        private static final int DATABASE_VERSION = 1;  //����
        
		public DBConnection(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION); //����SQLiteOpenHelper���غc��
		}
		//��@�}�l�إ߸�Ʈw�ɪ��y�k, �p�G��Ʈw�s�b�N���|�A����
		@Override
		public void onCreate(SQLiteDatabase db) {
			
			String sql = "CREATE TABLE " + BookSchema.TABLE_NAME.toString() + " (" 
					+ BookSchema.ID.toString()  + " INTEGER primary key autoincrement, " 
					+ BookSchema.BOOK_NAME.toString() + " varchar(20) not null, " 
					+ BookSchema.SDCARD.toString() + " varchar(50) not null"+ ");";
			
			db.execSQL(sql); //�NSQL�y�k�ᵹ��Ʈw
		}
		
		//��Ʈw��������s��, �ݭn�ק諸�y�k��o
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
    }
	
	public static void connectDB(Context context) {
  		// �i��DDMS -> FileExplorer -> data -> data ->��A���M��package �ݦ��_�@��database��Ƨ��A�Y���S���إߡA�N���|��
  		helper = new DBConnection(context); //�NDBConnection�����
  		
  		// ���o�iŪ�g��Ʈw
  		db = helper.getWritableDatabase();
	}
    
    // �s�W�@���������ƪ�
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