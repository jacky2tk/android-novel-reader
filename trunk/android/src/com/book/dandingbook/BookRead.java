package com.book.dandingbook;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class BookRead extends Activity implements
	MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
	MediaPlayer.OnCompletionListener {
	
	private static final String TAG = "Danding_BookRead";
	
	private final String BOOK_READ_PREF = "BOOK_READ_PREF";
	private final String PLAY_MUSIC = "PLAY_MUSIC";
	private final String ALERT_NO_MUSIC_FILE = "ALERT_NO_MUSIC_FILE";
	
	private MediaPlayer mPlayer;	// 音樂播放器 
	private TextView Lab_Content;	// 顯示書籍內容
	private File[] musicFileList;	// 音樂檔案清單
	private boolean blnPlayMusic;	// 判斷是否要播放背景音樂
	private static final int MENU_MUSIC = Menu.FIRST,
							 MENU_PLAY_MUSIC = Menu.FIRST +1,
							 MENU_STOP_MUSIC = Menu.FIRST +2;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //移除 Title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.book_read);
        
        // 載入設定值
        SharedPreferences settings = getSharedPreferences(BOOK_READ_PREF, MODE_PRIVATE);
        blnPlayMusic = (settings.getInt(PLAY_MUSIC, 1) == 1 ? true : false);
        boolean blnNoMusicFile = (settings.getInt(ALERT_NO_MUSIC_FILE, 1) == 1 ? true : false);
        if (Debug.On) Log.d(TAG, "Play Music: " + String.valueOf(blnPlayMusic));
        
		// 背景音樂設定
        mPlayer = new MediaPlayer();
        try {        
        	// 取得音樂清單
        	musicFileList = getFileList(Network.getSDPath("/music"), ".mp3");
        	if ((musicFileList.length == 0) && blnNoMusicFile) {
        		settings.edit().putInt(ALERT_NO_MUSIC_FILE, 0).commit();
        		
        		String msg = "目前尚無任何背景音樂檔，可自行放 MP3 到 SD 卡 dandingbook/music 資料夾內";
        		AlertDialog.Builder builder = new AlertDialog.Builder(BookRead.this);
    			builder.setTitle("無背景音樂檔");
    			builder.setMessage(msg);		
    			
    			// 按鈕: 確定
    			builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
    				
    				public void onClick(DialogInterface dialog, int which) {
    					// Do Nothing
    				}
    			});
    			
    			builder.show(); 			
        	}
            
        	// 初始化音樂播放器
        	mPlayer.setDataSource(getRandomMusic());
        	mPlayer.prepareAsync();
        	mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); 
        	mPlayer.setLooping(true); 
        	
        } catch (IllegalArgumentException e) {
        	e.printStackTrace(); 
        	Log.e(TAG, e.getMessage());
        } catch (IllegalStateException e) {
        	e.printStackTrace(); 
        	Log.e(TAG, e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
        	Log.e(TAG, e.getMessage());
        }
        
        // 讀取並顯示書籍資料
        String FILE_NAME = Network.getSDPath("").concat(getIntent().getStringExtra("BOOK_FILENAME"));
        Log.d(TAG, FILE_NAME);
        
        Lab_Content = (TextView)findViewById(R.id.txtBookRead);
        String File_Txt = FileReader_Code(FILE_NAME) ;
        Lab_Content.setText(File_Txt) ;
        
	}
    
    // 讀取檔案列表
    public File[] getFileList(String folderPath, final String extList) {
    	File[] fileList = null;
    	
    	// 設定背景音樂可接受的副檔名
    	FilenameFilter filter = new FilenameFilter() {
    		private String[] ext = extList.split(" ");

    		// 回傳值
    		// 		true  -> 副檔名符合
    		// 		false -> 副檔名不符合
			public boolean accept(File dir, String filename) {
				for (int i = 0; i < ext.length; i++) {
					// 若檔案中有找到指定的副檔名, 則回傳 true (副檔名符合)
					if (filename.lastIndexOf(ext[i]) != -1) return true;
				}
				return false;	// (副檔名不符合)
			}
    		
    	};
    	
    	// 取得檔案清單
    	try {
    		File folder = new File(folderPath);
    		fileList = folder.listFiles(filter);
    		
    	} catch(Exception e) {
    		Log.e(TAG, e.getMessage());
    	}
    	
    	return fileList;
    }

    // 隨機選取背景音樂
    private String getRandomMusic() {
    	String filename = "";
    	
    	if (musicFileList.length > 0) {
	    	int idx = (int)(Math.random() * 100 % musicFileList.length);
	    	filename =  musicFileList[idx].toString();
    	}
    	
    	return filename;
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
    	super.onResume();    
    	if (blnPlayMusic) {
    		mPlayer.start();
    	}
    } 
	
    @Override  
    protected void onPause() {  
    	super.onPause();   
    	if (blnPlayMusic) {
    		mPlayer.pause();
    	}
    } 
    
    @Override  
    protected void onDestroy() {    
    	super.onDestroy();       
    	mPlayer.release();   
    } 

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.book_read, menu);
        SubMenu subMenu = menu.addSubMenu(0,MENU_MUSIC,0,"背景音樂");
        subMenu.add(0,MENU_PLAY_MUSIC,0,"播放音樂");
        subMenu.add(0,MENU_STOP_MUSIC,0,"停止音樂");
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean blnChanged = false;
		
		switch(item.getItemId()){
			case MENU_STOP_MUSIC:
				mPlayer.pause();
				blnPlayMusic = false;		    	
		    	blnChanged = true;
		    	break;
		    	
			case MENU_PLAY_MUSIC:
				mPlayer.start();
				blnPlayMusic = true;		    	
		    	blnChanged = true;
		    	break;
		}
		
		// 儲存設定值
		if (blnChanged) {
			SharedPreferences settings = getSharedPreferences(BOOK_READ_PREF, MODE_PRIVATE);
			settings.edit()
				.putInt(PLAY_MUSIC, (blnPlayMusic ? 1 : 0))		// 背景音樂播放
				.commit();
			
			if (Debug.On) Log.d(TAG, "Play Music: " + String.valueOf(blnPlayMusic));
		}
		
		return super.onOptionsItemSelected(item);
	}

	public void onCompletion(MediaPlayer mp) {	
		Log.i(TAG, "Enter onCompletion....");
		try {
			// 一定要先reset
			mp.reset();
			
			// 隨機選擇下一首
			mp.setDataSource(getRandomMusic());
			
			// 開始播放
			mp.prepareAsync();
	    	
		} catch (IllegalArgumentException e) {
        	e.printStackTrace(); 
        	Log.e(TAG, e.getMessage());
        } catch (IllegalStateException e) {
        	e.printStackTrace(); 
        	Log.e(TAG, e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
        	Log.e(TAG, e.getMessage());
        }
	}

	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.i(TAG, "Enter onError....");
		mp.release();
		return false;
	}

	public void onPrepared(MediaPlayer mp) {
		Log.i(TAG, "Enter onPrepared....");
		if (blnPlayMusic) {
			mp.start();
		} else if (mp.isPlaying()) {
			mp.pause();
		}
	}

}
