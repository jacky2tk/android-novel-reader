/***********************************************************************************************
 * ADB 會將 Android 模擬器的執行過程紀錄起來, 其中包含了許多種類的訊息, 透過此機制可用在程式除
 * 錯上, 在程式碼中以 Log 指令加上文字描述 (不支援中文訊息), 以便在 LogCat 中找尋程式出錯的點，
 * 協助程式除錯。
 *
 * Log 除錯方法:
 *   設定 TAG (標籤)
 *     private static final String TAG = "Danding_頁面檔名"
 * 
 *   程式碼建立 Debug 訊息:
 *     if (Debug.On) Log.x(TAG, 訊息);
 *
 *     x 有底下 4 種
 *       d - Debug 除錯		i - Info  訊息
 *       w - Warn  警告		e - Error 錯誤
 *
 * 增加 Log 過濾器步驟:
 *   1. 在 LogCat 視窗按 + 號, 新增一個 Log filter
 *   2. 輸入參數:
 *      Filter Name: Danding
 *      by Log Tag:  Danding
 *
 ***********************************************************************************************/
package com.book.dandingbook;

public class Debug {
	// 設定是否開啟 Debug 功能, true 開啟, false 關閉	
	public static final boolean On = true;
}
