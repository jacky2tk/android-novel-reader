package com.book.dandingbook;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.Window;
import android.widget.TextView;

public class BookRead extends Activity {
	private static final int MENU_MUSIC = Menu.FIRST,
										MENU_PLAY_MUSIC = Menu.FIRST +1 ,
										MENU_STOP_MUSIC = Menu.FIRST +2 ;
	MediaPlayer mPlayer; 
	private static final String TAG = "Danding_BookRead";
	private TextView Lab_Content;
	 public String getSDPath(){
     	StringBuilder SB = new StringBuilder() ;
     	//取得外部裝置目錄的絕對路徑
     	SB.append(android.os.Environment.getExternalStorageDirectory().getAbsolutePath());
     	SB.append("/dandingbook/") ;
     	
     	File mydatapath = new File(SB.toString()) ;
     	if(!mydatapath.exists()){ //如果找不到這個資料夾
     		mydatapath.mkdir() ; //就新建一個資料夾
     	}
     	
     	return SB.toString() ;
     }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //移除 Title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.book_read);
        
		
        try {
        	mPlayer = MediaPlayer.create(this, R.raw.ss); 
        	mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); 
        	mPlayer.setLooping(true); 
        	
        } catch (IllegalStateException e) {
        	e.printStackTrace(); 
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        String FILE_NAME = getSDPath().concat(getIntent().getStringExtra("SDcard")); //取得SD卡的路徑
        Log.d(TAG, FILE_NAME);
        
        Lab_Content = (TextView)findViewById(R.id.txtBookRead);
        String File_Txt = FileReader_Code(FILE_NAME) ;
        Lab_Content.setText(File_Txt) ;
        
	}
    //讀取檔案，並判斷檔案編碼，以其編碼顯示
    public String FileReader_Code(String FilePath) {
        File file = new File(FilePath);
        BufferedReader reader;
        String text = "";
        try {
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream in = new BufferedInputStream(fis);
                in.mark(4);
                byte[] b = new byte[3];
                in.read(b);
                in.reset();
                Log.d(TAG, FilePath+"_"+b[0]+"_"+b[1]+"_"+b[2]);
                //取字節前三碼判斷編碼
                if (b[0] == -17 && b[1] == -69 && b[2] == -65) { //標準UTF-8格式
                    reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        
                }else if (b[0] == -25 && b[1] == -84 && b[2] == -84) { //UTF-8無BON格式
                    reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    
                }else {
                    reader = new BufferedReader(new InputStreamReader(in, "Big5"));
                }
                String str = reader.readLine();
                while (str != null) {
                        text = text + str + "\n";
                        str = reader.readLine();
                }
                reader.close();
        } catch(Exception e){
			Log.d(TAG,"Error_"+e.getMessage());
			System.out.println(e.getMessage()) ;
		}
        return text;
    }

	@Override  
	protected void onResume() {  
    	// TODO Auto-generated method stub            
    	super.onResume();    
    	mPlayer.start();   
    } 
	
    @Override  
    protected void onPause() {  
    	// TODO Auto-generated method stub            
    	super.onPause();   
    	mPlayer.pause();   
    } 
    
    @Override  
    protected void onDestroy() {    
    	// TODO Auto-generated method stub       
    	super.onDestroy();       
    	mPlayer.release();   
    } 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_read, menu);
        SubMenu subMenu = menu.addSubMenu(0,MENU_MUSIC,0,"背景音樂");
        subMenu.add(0,MENU_PLAY_MUSIC,0,"播放音樂");
        subMenu.add(0,MENU_STOP_MUSIC,0,"停止音樂");
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
			case MENU_STOP_MUSIC:
				super.onPause();   
		    	mPlayer.pause();
		    	break;
			case MENU_PLAY_MUSIC:
				super.onResume();    
		    	mPlayer.start();
		    	break;
		}
		return super.onOptionsItemSelected(item);
	}

}
