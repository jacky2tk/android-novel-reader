<?php
include("_func/config.inc.php");
$m_case = $_GET['case'] ; //���o�ǤJ��case
$m_data = $_GET['data'] ;	//���o�ϥΪ̿�J��T

mysql_query("SET NAMES 'utf8'"); 
mysql_query("SET CHARACTER_SET_CLIENT=utf8"); 
mysql_query("SET CHARACTER_SET_RESULTS=utf8");

switch ($m_case){
	case "insert" :
	//���X�̤j��id��
	$m_query = DB_QUERY("SELECT MAX(b_id) as max_id FROM $GLOBALS[DB_BOOK]");
	$m_row = mysql_fetch_array($m_query) ;
	$m_maxid = $m_row['max_id']+1 ;  //�̤jid+1
	
	//m_data���e : ID,�m�W,�s���N�X,���A
	$m_data = explode(",",$m_data) ;
	$m_name = $m_data[0] ;
	$m_intro = $m_data[1] ;
	$m_author = $m_data[2] ;
	$m_issue = $m_data[3] ;
	$m_path = $m_data[4] ;
	$m_image = $m_data[5] ;
	
	$m_path = "BOOK_".$m_maxid ;		//���s���ɮ׸��|�W��
	($m_image!="")?$m_image = "IMG_".$m_maxid:$m_image="" ;  //���s��img�ɦW�A���P�_�O�_���n�W�ǹϤ�
	
	
##���~�Ϥ�
	$m_boolen = false ;
	if(!($_FILES['file']['name']=='')){
		$m_file = $m_image ;
		if($_FILES['file']['error']>0){
			switch($_FILES['file']['error']){
				// �`�N�U���� case ���U����U break , �_�h�|�y���Y�@�������~���|�Q�����X��
				case 1 : 
					die("�ɮפj�p�W�X php.ini:upload_max_filesize ����");
				case 2 : 
					die("�ɮפj�p����W�X ".strval($_POST[MAX_FILE_SIZE]/1000)." K Byte");
				case 3 : 
					die("�ɮ׶ȳ����Q�W��");
				case 4 : 
					die("�ɮץ��Q�W��");
			}
		}
		if(is_uploaded_file($_FILES['file']['tmp_name'])){
			echo "Wait...."."<br>";
			$Destdir="upload/images/";
			if(!is_dir($Destdir)){
				echo "�ǿ饢�� : �ؿ����s�b" ;
			}
			elseif(!is_writeable($Destdir)){
				echo "�ǿ饢�� : �L�k�g�J" ;
			}
			else {
				copy($_FILES['file']['tmp_name'] , $Destdir."/".$m_file);
				$m_file_name = 'upload/images/'.$m_file ;
				$size = getimagesize($m_file_name) ;
				if(!($size[2]==1)&&!($size[2]==2)){ // 1��GIF�ɡ@����JPG/JPEG��
					umsg("���ɮ榡�ȭ� GIF,JPEG,JPG");
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
	
	//DB_INSERT($GLOBALS["DB_BOOK"],$m_filed,$m_value); //�s�W
	echo true ;
	exit ;
}
?>