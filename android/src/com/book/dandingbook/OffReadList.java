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
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class OffReadList extends Activity {
	private static final String TAG = "Danding_OffReadList";
	
	public ArrayList<CharSequence> Namelist;
	public ArrayList<CharSequence> IDlist;
	public ArrayList<CharSequence> SDlist;
	
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
	
	//SQLiteOpenHelper-�إ߸�ƮwPhoneBookDB�MUsers Table
   public class DBConnection extends SQLiteOpenHelper{
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
    
    //�ŧiDBConnection��SQLiteDatabase���`
	DBConnection helper;
	public static SQLiteDatabase db;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);
        Log.d(TAG, "OffReadList_OnCreate");
  		//�i��DDMS -> FileExplorer -> data -> data ->��A���M��package �ݦ��_�@��database��Ƨ��A�Y���S���إߡA�N���|��
  		helper = new DBConnection(this); //�NDBConnection�����
  		Log.d(TAG, "DBConnection");
  		//���o�iŪ�g��Ʈw
  		db = helper.getWritableDatabase();
  		//���o�Ҧ���ƪ�BOOK_NAME�A�w�m�blist[]�W
  		//query(TABLE�W�١A���}�C)
  		Cursor c = db.query(BookSchema.TABLE_NAME.toString()
  							,new String[]{BookSchema.ID.toString(),BookSchema.BOOK_NAME.toString(),BookSchema.SDCARD.toString()}
  							,null ,null ,null ,null ,null) ;
  		
  		//���USER_NAME�bSpinner���-spinner�W
  		Namelist = new ArrayList<CharSequence>() ; //�����Namelist
  		IDlist = new ArrayList<CharSequence>() ;
  		SDlist = new ArrayList<CharSequence>() ;
  		
  		c.moveToFirst() ; //�NCursor���ʨ�Ĥ@��
  		for(int i=0 ; i<c.getCount() ; i++){  //getCount()��ƪ�@���X�����
  			IDlist.add(c.getString(0)) ;
  			Namelist.add(c.getString(1)) ; //���X��ơA0�N����Ĥ@�����
  			SDlist.add(c.getString(2)) ;
  			c.moveToNext() ; //�NCursor���ʨ�U�@��
  		}
  		c.close();//����cursor
  		
  		ListView ListBook = (ListView)findViewById(R.id.lstBook) ;
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
        		OffReadList.this,
        		android.R.layout.simple_list_item_1,
        		Namelist
        	);
		
		ListBook.setAdapter(adapter);
		/*
		//�s�W�y�k
		ContentValues values = new ContentValues() ;
		values.put(BookSchema.BOOK_NAME.toString(), "aaa") ;
		values.put(BookSchema.SDCARD.toString(), "book_1.txt") ;
		db.insert(BookSchema.TABLE_NAME.toString(), null, values) ;
		*/
		
		ListBook.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0 ,View view ,int position ,long id) {
				Intent intent = new Intent(OffReadList.this, BookRead.class); //�I�sRoundList����
	            intent.putExtra("SDcard", SDlist.get(position).toString() );  //�ǤJDB�����|
	            startActivity(intent);	//�I�sActivity
			}
		}) ;
  	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}