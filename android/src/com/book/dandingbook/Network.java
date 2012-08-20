package com.book.dandingbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Network {
	private static final String TAG = "Danding_Network";
	
    public Network(){
	
    }
    
    // �P�_�ثe�O�_�������i��
    public static boolean haveNetworkConnection(ConnectivityManager cm) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected()){
                    haveConnectedWifi = true;
                }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected()){
                    haveConnectedMobile = true;
                }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    
    // �e�X Requst
	private static HttpURLConnection doRequest(URL url, String requestMethod, String content){
    	HttpURLConnection conn = null;
    	
    	try{ 
    		conn = (HttpURLConnection)url.openConnection();
			// �]�w�ШD��k GET or POST
			conn.setRequestMethod(requestMethod);
			// �]�w�i�H�ϥ� InputStream�A�U���ɮ�
			conn.setDoInput(true);
		
			// �]�w�i�H�ϥ� OutputStream�A�W���ɮ�
			conn.setDoOutput(true);
			conn.connect();
						
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
		}
    	
    	return conn;
    }	
	
	// Ū�����
	public static String getData(String url, String content) {
		String result = "";
		
		try {
			// �զX Request URL
			URL reqUrl = new URL(url + "?" + content);
			
			// �ШD�e�X Request �� Server
			HttpURLConnection conn = doRequest(reqUrl, "GET", content);
		
			// �]�w�U���ɮת� InputStream
			InputStream in = conn.getInputStream();
			
			// �]�w�U���Ȧs�O�а� buffer
			byte[] buffer = new byte[1024];			
			
			while ( in.read(buffer) != -1) {
				result += new String(buffer).trim();
			}
			
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// �N�}�C�ন�r�ꪫ��
    	return result;
	}
	
	// �U���ɮ�
	// TODO: �ѨM�L�k�U���ɮת����D (��ĳ�令 POST �覡)
	public static void getFile(String url, String FileName) {
		try {
			// �զX Request URL
			URL reqUrl = new URL(url);
			
			// �ШD�e�X Request �� Server
			HttpURLConnection conn = doRequest(reqUrl, "GET", "");
			
			// �w�q�U���ɮ׸��|�A�g�J FileOutputStream ����
			FileOutputStream out = new FileOutputStream(
				new File(getSDPath(), FileName)
			); 
		
			// �]�w�U���ɮת� InputStream
			InputStream in = conn.getInputStream(); 
			
			// �]�w�U���Ȧs�O�а� buffer
			byte[] buffer = new byte[1024];			
			
			// �}�l�U���ɮ�...�A�üg�J  FileOutputStream ����
			int len = 0; 
			while ( (len = in.read(buffer)) != -1 ) { 
				  out.write(buffer,0, len); 
			} 
			
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public static String getSDPath(){
    	StringBuilder SB = new StringBuilder();
		SB.append(android.os.Environment.getExternalStorageDirectory().getAbsolutePath());
		SB.append("/dandingbook/");
		
		File myDataPath = new File(SB.toString());
    	if (!myDataPath.exists()) {
			myDataPath.mkdirs();
		}
    	
		return SB.toString();
    }
}

