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
	
	private MediaPlayer mPlayer;	// ���ּ��� 
	private TextView Lab_Content;	// ��ܮ��y���e
	private File[] musicFileList;	// �����ɮײM��
	private boolean blnPlayMusic;	// �P�_�O�_�n����I������
	private static final int MENU_MUSIC = Menu.FIRST,
							 MENU_PLAY_MUSIC = Menu.FIRST +1,
							 MENU_STOP_MUSIC = Menu.FIRST +2;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //���� Title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.book_read);
        
        // ���J�]�w��
        SharedPreferences settings = getSharedPreferences(BOOK_READ_PREF, MODE_PRIVATE);
        blnPlayMusic = (settings.getInt(PLAY_MUSIC, 1) == 1 ? true : false);
        boolean blnNoMusicFile = (settings.getInt(ALERT_NO_MUSIC_FILE, 1) == 1 ? true : false);
        if (Debug.On) Log.d(TAG, "Play Music: " + String.valueOf(blnPlayMusic));
        
		// �I�����ֳ]�w
        mPlayer = new MediaPlayer();
        try {        
        	// ���o���ֲM��
        	musicFileList = getFileList(Network.getSDPath("/music"), ".mp3");
        	if ((musicFileList.length == 0) && blnNoMusicFile) {
        		settings.edit().putInt(ALERT_NO_MUSIC_FILE, 0).commit();
        		
        		String msg = "�ثe�|�L����I�������ɡA�i�ۦ�� MP3 �� SD �d dandingbook/music ��Ƨ���";
        		AlertDialog.Builder builder = new AlertDialog.Builder(BookRead.this);
    			builder.setTitle("�L�I��������");
    			builder.setMessage(msg);		
    			
    			// ���s: �T�w
    			builder.setPositiveButton("�T�w", new DialogInterface.OnClickListener() {
    				
    				public void onClick(DialogInterface dialog, int which) {
    					// Do Nothing
    				}
    			});
    			
    			builder.show(); 			
        	}
            
        	// ��l�ƭ��ּ���
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
        
        // Ū������ܮ��y���
        String FILE_NAME = Network.getSDPath("").concat(getIntent().getStringExtra("BOOK_FILENAME"));
        Log.d(TAG, FILE_NAME);
        
        Lab_Content = (TextView)findViewById(R.id.txtBookRead);
        String File_Txt = FileReader_Code(FILE_NAME) ;
        Lab_Content.setText(File_Txt) ;
        
	}
    
    // Ū���ɮצC��
    public File[] getFileList(String folderPath, final String extList) {
    	File[] fileList = null;
    	
    	// �]�w�I�����֥i���������ɦW
    	FilenameFilter filter = new FilenameFilter() {
    		private String[] ext = extList.split(" ");

    		// �^�ǭ�
    		// 		true  -> ���ɦW�ŦX
    		// 		false -> ���ɦW���ŦX
			public boolean accept(File dir, String filename) {
				for (int i = 0; i < ext.length; i++) {
					// �Y�ɮפ��������w�����ɦW, �h�^�� true (���ɦW�ŦX)
					if (filename.lastIndexOf(ext[i]) != -1) return true;
				}
				return false;	// (���ɦW���ŦX)
			}
    		
    	};
    	
    	// ���o�ɮײM��
    	try {
    		File folder = new File(folderPath);
    		fileList = folder.listFiles(filter);
    		
    	} catch(Exception e) {
    		Log.e(TAG, e.getMessage());
    	}
    	
    	return fileList;
    }

    // �H������I������
    private String getRandomMusic() {
    	String filename = "";
    	
    	if (musicFileList.length > 0) {
	    	int idx = (int)(Math.random() * 100 % musicFileList.length);
	    	filename =  musicFileList[idx].toString();
    	}
    	
    	return filename;
    }


    //Ū���ɮסA�çP�_�ɮ׽s�X�A�H��s�X���
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
                //���r�`�e�T�X�P�_�s�X
                if (b[0] == -17 && b[1] == -69 && b[2] == -65) { //�з�UTF-8�榡
                    reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        
                }else if (b[0] == -25 && b[1] == -84 && b[2] == -84) { //UTF-8�LBON�榡
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
        SubMenu subMenu = menu.addSubMenu(0,MENU_MUSIC,0,"�I������");
        subMenu.add(0,MENU_PLAY_MUSIC,0,"���񭵼�");
        subMenu.add(0,MENU_STOP_MUSIC,0,"�����");
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
		
		// �x�s�]�w��
		if (blnChanged) {
			SharedPreferences settings = getSharedPreferences(BOOK_READ_PREF, MODE_PRIVATE);
			settings.edit()
				.putInt(PLAY_MUSIC, (blnPlayMusic ? 1 : 0))		// �I�����ּ���
				.commit();
			
			if (Debug.On) Log.d(TAG, "Play Music: " + String.valueOf(blnPlayMusic));
		}
		
		return super.onOptionsItemSelected(item);
	}

	public void onCompletion(MediaPlayer mp) {	
		Log.i(TAG, "Enter onCompletion....");
		try {
			// �@�w�n��reset
			mp.reset();
			
			// �H����ܤU�@��
			mp.setDataSource(getRandomMusic());
			
			// �}�l����
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
