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
    
    // 判斷目前是否有網路可用
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
    
    // 送出 Requst
	private static HttpURLConnection doRequest(URL url, String requestMethod, String content){
    	HttpURLConnection conn = null;
    	
    	try{ 
    		conn = (HttpURLConnection)url.openConnection();
			// 設定請求方法 GET or POST
			conn.setRequestMethod(requestMethod);
			// 設定可以使用 InputStream，下載檔案
			conn.setDoInput(true);
		
			// 設定可以使用 OutputStream，上傳檔案
			conn.setDoOutput(true);
			conn.connect();
						
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
		}
    	
    	return conn;
    }	
	
	// 讀取資料
	public static String getData(String url, String content) {
		String result = "";
		
		try {
			// 組合 Request URL
			URL reqUrl = new URL(url + "?" + content);
			
			// 請求送出 Request 給 Server
			HttpURLConnection conn = doRequest(reqUrl, "GET", content);
		
			// 設定下載檔案的 InputStream
			InputStream in = conn.getInputStream();
			
			// 設定下載暫存記憶區 buffer
			byte[] buffer = new byte[1024];			
			
			while ( in.read(buffer) != -1) {
				result += new String(buffer).trim();
			}
			
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 將陣列轉成字串物件
    	return result;
	}
	
	// 下載檔案
	// TODO: 解決無法下載檔案的問題 (建議改成 POST 方式)
	public static void getFile(String url, String FileName) {
		try {
			// 組合 Request URL
			URL reqUrl = new URL(url);
			
			// 請求送出 Request 給 Server
			HttpURLConnection conn = doRequest(reqUrl, "GET", "");
			
			// 定義下載檔案路徑，寫入 FileOutputStream 物件
			FileOutputStream out = new FileOutputStream(
				new File(getSDPath(), FileName)
			); 
		
			// 設定下載檔案的 InputStream
			InputStream in = conn.getInputStream(); 
			
			// 設定下載暫存記憶區 buffer
			byte[] buffer = new byte[1024];			
			
			// 開始下載檔案...，並寫入  FileOutputStream 物件
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

