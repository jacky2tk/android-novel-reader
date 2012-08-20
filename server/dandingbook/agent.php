<?php
	header("Content-Type:text/html; charset=utf-8");
?>
<?php
include("_func/config.inc.php");

// GET 傳入值
$m_case = $_GET['case'] ; 	// 取得傳入值 case
$m_data = $_GET['data'] ;	// 取得使用者輸入資訊

$CONST_SPLITER = ",";		// 資料分隔符號

mysql_query("SET NAMES 'utf8'"); 
mysql_query("SET CHARACTER_SET_CLIENT=utf8"); 
mysql_query("SET CHARACTER_SET_RESULTS=utf8");

switch ($m_case) {
	// --------------------------------------------------------------
	// 書本清單
	// 回傳值: 逗號分隔的書本名稱
	case "book_list":
		$m_query = DB_QUERY("SELECT b_name FROM $GLOBALS[DB_BOOK]");
		$m_rows = mysql_num_rows($m_query);	// 取得資料筆數
		
		// 將資料組成以逗號相隔的字串
		$m_result = "";
		for ($i = 0; $i < $m_rows; $i++) {
			$m_q = mysql_fetch_array($m_query);
			
			if ($m_result == "")
				$m_result = $m_q[b_name];
			else
				$m_result = $m_result. $CONST_SPLITER. $m_q[b_name];
		}
		
		echo $m_result;
		break;
		
	// --------------------------------------------------------------
	// 書本詳細資料
	// 回傳值: 以逗號分隔的所有欄位
	case "book_detail":
		$m_id = $_GET["b_id"];
		$m_query = DB_QUERY("SELECT * FROM $GLOBALS[DB_BOOK] WHERE b_id = $m_id");
		$m_q = mysql_fetch_array($m_query);
		
		// 將資料組成以逗號相隔的字串
		$m_result = $m_q[b_id]. $CONST_SPLITER;					// 編號
		$m_result = $m_result. $m_q[b_name]. $CONST_SPLITER;	// 姓名
		$m_result = $m_result. $m_q[b_intro]. $CONST_SPLITER;	// 簡介
		$m_result = $m_result. $m_q[b_author]. $CONST_SPLITER;	// 作者
		$m_result = $m_result. $m_q[b_issue]. $CONST_SPLITER;	// 出版商
		$m_result = $m_result. $m_q[b_image];					// 小說封面圖案
		
		echo $m_result;
		break;
		
	// --------------------------------------------------------------
	// 使用者註冊
	// 回傳值:  1 - 成功,  0 - 失敗
	case "user_add":
		$m_name = $_GET["name"];
		$m_account = $_GET["account"];
		$m_pwd = $_GET["pwd"];
		
		$m_query = DB_QUERY("SELECT b_id FROM $GLOBALS[DB_USER] WHERE b_account='".$m_account."'");
		$m_row = mysql_num_rows($m_query) ;
		
		if($m_row==0){
			
			$m_filed = Array("b_name","b_account","b_pwd") ;
			$m_value = Array($m_name,$m_account,$m_pwd) ;
			
			DB_INSERT($GLOBALS[DB_USER],$m_filed,$m_value); //新增
			echo "1" ;
		}
		else{
			echo "0" ;
		}
		
		break;
		
	// --------------------------------------------------------------
	// 使用者登入
	// 回傳值:  1 - 成功,  0 - 失敗
	case "user_login":
		$m_account = $_GET["account"];
		$m_pwd = $_GET["pwd"];
		
		// 比對帳號及密碼 (不分大小寫)
		// TODO: 未來可考慮帳號及密碼區分大小寫
		//   有嘗試使用 cast varbinary 及 collate, 但伺服器都回應錯誤
		//   參考資料: http://blog.csdn.net/jesse621/article/details/7857333
		$m_query = DB_QUERY("SELECT * FROM $GLOBALS[DB_USER] WHERE ".
			"b_account = '$m_account' and b_pwd = '$m_pwd'");
		
		// 回傳是否登入成功 (布林值)
		echo (mysql_num_rows($m_query) > 0)? "1": "0";
			
		break;
}

?>