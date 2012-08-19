package com.book.dandingbook;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BookList extends Activity {
	private static final String TAG = "Danding_BookList";
	
	private String strBookList = "";	// �s��q Server �U�����p���M��
	private ListView lstBook;			// ��ܤp���M�檺���
    
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
			//edtAccount.setText(GetResult);
			Toast.makeText(BookList.this, "Download Complete!", Toast.LENGTH_LONG).show();
			Log.d(TAG, "Handler: " + strBookList);
			
			String[] aryBookList = strBookList.split(",");
			
			if (aryBookList.length > 0) {
				List<String> list = new ArrayList<String>();
				
				for (int i = 0; i < aryBookList.length; i++)
					list.add(aryBookList[i]);
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
		        		BookList.this,
		        		android.R.layout.simple_list_item_1,
		        		list
		        		);
				
				lstBook.setAdapter(adapter);
			}
		}
    	
    };
    
    // �z�L Server �� PHP Ū�X MySQL ����Ƥ��e
	Thread thread = new Thread() {
		public void run() {
			try {
				// ���o���
				if (Debug.On) Log.d(TAG, "Sent GET request");
				strBookList = Network.getData(getString(R.string.agent_url), "case=book_list");
				
				if (Debug.On) Log.d(TAG, "Callback to main loop");
				DownloadHandler.post(DownloadCallback);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				System.out.println(e.getMessage());
			}
		}
	};
	
	// �q Server �U���p���M��
	private void getBookList() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE); 
    	if (Network.haveNetworkConnection(cm)){
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
		// �إߪŪ� ListView        
        Log.d(TAG, "Create empty ListView");
    	ListView lstBook = (ListView)findViewById(R.id.lstBook);
        
        //CharSequence[] list = {};
    	Log.d(TAG, "Add element of ListView");
    	List<String> list = new ArrayList<String>();
    	list.add("�L���");
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        		BookList.this,
        		android.R.layout.simple_list_item_1,
        		list
        		);
        
        lstBook.setAdapter(adapter);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list, menu);
        return true;
    }
    
}
