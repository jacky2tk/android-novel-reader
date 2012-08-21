<?php
include("_func/config.inc.php");
$m_case = $_GET['case'] ; //取得傳入值case
$m_data = $_GET['data'] ;	//取得使用者輸入資訊

mysql_query("SET NAMES 'utf8'"); 
mysql_query("SET CHARACTER_SET_CLIENT=utf8"); 
mysql_query("SET CHARACTER_SET_RESULTS=utf8");

switch ($m_case){
	case "insert" :
	//取出最大的id值
	$m_query = DB_QUERY("SELECT MAX(b_id) as max_id FROM $GLOBALS[DB_BOOK]");
	$m_row = mysql_fetch_array($m_query) ;
	$m_maxid = $m_row['max_id']+1 ;  //最大id+1
	
	//m_data內容 : ID,姓名,存取代碼,型態
	$m_data = explode(",",$m_data) ;
	$m_name = $m_data[0] ;
	$m_intro = $m_data[1] ;
	$m_author = $m_data[2] ;
	$m_issue = $m_data[3] ;
	$m_path = $m_data[4] ;
	$m_image = $m_data[5] ;
	
	$m_path_ext = substr($m_path,strrpos($m_path,".")) ;
	$m_image_ext = substr($m_image,strrpos($m_image,".")) ;
	
	$m_path = "book_".$m_maxid.strtolower($m_path_ext) ;		//重新給檔案路徑名稱
	($m_image!="")?$m_image = "img_".$m_maxid.strtolower($m_image_ext):$m_image="" ;  //重新給img檔名，先判斷是否有要上傳圖片
	
	
	$m_filed = Array("b_name","b_intro","b_author","b_issue","b_path","b_image") ;
	$m_value = Array($m_name,$m_intro,$m_author,$m_issue,$m_path,$m_image) ;
	
	DB_INSERT($GLOBALS["DB_BOOK"],$m_filed,$m_value); //新增
	
	echo "OK,".$m_path.",".$m_image ;
	exit ;
}
?>