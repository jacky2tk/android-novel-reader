<?php
	header("Content-Type:text/html; charset=utf-8");
?>
<?php
include("_func/config.inc.php");
$m_case = $_GET['case'] ; 	//取得傳入值case
$m_data = $_GET['data'] ;	//取得使用者輸入資訊

mysql_query("SET NAMES 'utf8'"); 
mysql_query("SET CHARACTER_SET_CLIENT=utf8"); 
mysql_query("SET CHARACTER_SET_RESULTS=utf8");

switch ($m_case) {
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
				$m_result = $m_result. ",". $m_q[b_name];
		}
		
		echo $m_result;
		break;
}
?>