package com.book.dandingbook;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.Window;

public class BookRead extends Activity {
	private static final int MENU_MUSIC = Menu.FIRST,
										MENU_PLAY_MUSIC = Menu.FIRST +1 ,
										MENU_STOP_MUSIC = Menu.FIRST +2 ;
	MediaPlayer mPlayer; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //移除 Title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.book_read);
        
		
        try {
        	//mPlayer = MediaPlayer.create(this, R.raw.ss); 
        	mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); 
        	mPlayer.setLooping(true); 
        	
        } catch (IllegalStateException e) {
        	e.printStackTrace(); 
        } catch (Exception e) {
        	e.printStackTrace();
        }
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
