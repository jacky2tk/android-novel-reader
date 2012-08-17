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
	
	$m_path = "BOOK_".$m_maxid ;		//重新給檔案路徑名稱
	($m_image!="")?$m_image = "IMG_".$m_maxid:$m_image="" ;  //重新給img檔名，先判斷是否有要上傳圖片
	
	
##產品圖片
	$m_boolen = false ;
	if(!($_FILES['file']['name']=='')){
		$m_file = $m_image ;
		if($_FILES['file']['error']>0){
			switch($_FILES['file']['error']){
				// 注意下面的 case 之下不能下 break , 否則會造成某一項之錯誤不會被偵測出來
				case 1 : 
					die("檔案大小超出 php.ini:upload_max_filesize 限制");
				case 2 : 
					die("檔案大小不能超出 ".strval($_POST[MAX_FILE_SIZE]/1000)." K Byte");
				case 3 : 
					die("檔案僅部分被上傳");
				case 4 : 
					die("檔案未被上傳");
			}
		}
		if(is_uploaded_file($_FILES['file']['tmp_name'])){
			echo "Wait...."."<br>";
			$Destdir="upload/images/";
			if(!is_dir($Destdir)){
				echo "傳輸失敗 : 目錄不存在" ;
			}
			elseif(!is_writeable($Destdir)){
				echo "傳輸失敗 : 無法寫入" ;
			}
			else {
				copy($_FILES['file']['tmp_name'] , $Destdir."/".$m_file);
				$m_file_name = 'upload/images/'.$m_file ;
				$size = getimagesize($m_file_name) ;
				if(!($size[2]==1)&&!($size[2]==2)){ // 1為GIF檔　２為JPG/JPEG檔
					umsg("圖檔格式僅限 GIF,JPEG,JPG");
					$m_boolen = true ;
					unlink($m_file_name) ;
				}
				else{
					copy($_FILES['file']['tmp_name'] , $m_file_name);
				}
			}
		}
	}
	
	$m_filed = Array("b_name","b_intro","b_author","b_issue","b_path","b_image") ;
	$m_value = Array($m_name,$m_intro,$m_author,$m_issue,$m_path,$m_image) ;
	
	//DB_INSERT($GLOBALS["DB_BOOK"],$m_filed,$m_value); //新增
	echo true ;
	exit ;
}
?>