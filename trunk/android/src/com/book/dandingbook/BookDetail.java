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
	
	private String strBookList = "";	// �s��q Server �U�����p���M��
	private ListView lstBook;			// ��ܤp���M�檺���
	private CustomAdapter adapter;
    
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
					adapter.addSeparator("�ѥ����");
					adapter.addListItem2(aryBookList[1], aryBookList[2]);
					adapter.addLabItem("�@�̡G" + aryBookList[3]);
					adapter.addLabItem("�X���ӡG" + aryBookList[4]);
					adapter.addLabItem("�p���ʭ��Ϯ�");
					adapter.addImage(aryBookList[5]);
					adapter.addButton("�U���ɮ�");					
					
					lstBook.setAdapter(adapter);
				
				} catch (Exception e) {
					Log.e(TAG, "Count: " + aryBookList.length + "\n" + e.getMessage());
					System.out.println("888888888: " + aryBookList.length + " * " + e.getMessage());
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
   
    // �q Server �U���p���M��
	private void getBookDetail(final CharSequence id) {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE); 
    	if (Network.haveNetworkConnection(cm)){
    		// �z�L Server �� PHP Ū�X MySQL ����Ƥ��e
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
