package com.book.dandingbook;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.support.v4.app.NavUtils;

public class BookRead extends Activity {
	
	MediaPlayer mPlayer; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //²¾°£title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.book_read);
        try{
        	mPlayer = MediaPlayer.create(this, R.raw.ss); 
        	mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); 
        	mPlayer.setLooping(true); 
           }catch (IllegalStateException e) 
	           {
		       		e.printStackTrace(); 
	           }
}
 
	@Override  protected void onResume()    
    {  
    	// TODO Auto-generated method stub            
    	super.onResume();    
    	mPlayer.start();   
    } 
    @Override  protected void onPause()   
    {  
    	// TODO Auto-generated method stub            
    	super.onPause();   
    	mPlayer.pause();   
    } 
    @Override  protected void onDestroy()   
    {    
    	// TODO Auto-generated method stub       
    	super.onDestroy();       
    	mPlayer.release();   
    } 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_read, menu);
        return true;
    }

    
}
