<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>書籍檔案上傳系統</title>
<style type="text/css">
<!--
.style1 {color: #333333}
.style3 {font-size: 14px}
.style4 {
	font-size: 24px
}
-->
</style>
<script language="JavaScript" type="text/JavaScript" src="_func/_js/ajax.js"></script>
<script>
function jimgshow(lc_id, lc_image){
	if(lc_image!=""){
		//document.getElelemtById('myimg').style.display="" ;
		alert(lc_image) ;
		document.getElementById(lc_id).src = lc_image ;
	}
}
function jbookup(){
	m_name = document.getElementById('b_name').value ;
	m_path = document.getElementById('b_path').value ;
	m_str = "" ;
	if(m_name=="") m_str = " 書名 " ; 
	//if(m_path=="") m_str = m_str + " 路徑" ;
	if(m_str!=""){
		alert(m_str+" ,欄位不得空白喔!") ;
	}
	else{
		m_intro = document.getElementById('b_intro').value ;
		m_author = document.getElementById('b_author').value ;
		m_issue = document.getElementById('b_issue').value ;
		m_image = document.getElementById('b_image').value ;
		m_data = m_name+","+m_intro+","+m_author+","+m_issue+","+m_path+","+m_image ;
		m_return = jajax("bookup_ajax.php?case=insert&data="+m_data) ;
		m_resp = m_return.split(",") ;
		if(m_resp[0]=="OK"){
			document.getElementById('hidden_up').value = m_resp[1]+","+m_resp[2] ;
			document.form1.submit() ;
		}
		else{
			alert(m_return) ;
		}
	}
}
</script>
 
</head>

<body>
<form action="" method="post" enctype="multipart/form-data" name="form1" id="form1">
  <table width="682" height="248" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td colspan="3" align="center">
        <h1 class="style1 style4">書籍檔案上傳系統</h1>    </td>
    </tr>
    <tr>
      <td width="169" align="right"><span class="style3">書名&nbsp;</span></td>
      <td colspan="2">&nbsp;
      <input name="b_name" type="text" id="b_name" size="20" /></td>
    </tr>
    <tr>
      <td align="right"><span class="style3">作者&nbsp;</span></td>
      <td colspan="2"><label>
        &nbsp;
        <input name="b_author" type="text" id="b_author" size="20" />
      </label> </td>
    </tr>
    <tr>
      <td align="right"><span class="style3">出版社&nbsp;</span></td>
      <td colspan="2"><label>
        &nbsp;
        <input name="b_issue" type="text" id="b_issue" size="20" />
      </label></td>
    </tr>
    <tr>
      <td align="right"><span class="style3">簡介&nbsp;</span></td>
      <td colspan="2"><label>
        &nbsp;
        <input name="b_intro" type="text" id="b_intro" size="65" maxlength="60" />
      </label></td>
    </tr>
    <tr>
      <td height="34" align="right"><span class="style3">資料檔案路徑&nbsp;</span></td>
      <td colspan="2"><label>
      &nbsp;
      <input type="file" name="b_path" id="b_path" value="C:\Users\a\Desktop\3651472_160045041911_2.jpg" />
      </label></td>
    </tr>
    <tr>
      <td align="right" valign="top"><span class="style3">圖片檔案路徑&nbsp;</span></td>
      <td width="295" valign="top"><label>
      &nbsp;
      <input type="file" name="b_image" id="b_image" />
      <!--onchange="javascript:jimgshow('myimg',this.value);"-->
      </label></td>
      <td width="218" valign="top"><input name="myimg" type="image" id="myimg" src="C|/thumbnailCAX3K5WQ.jpg" height="200" style="display:none" /></td>
      <!-- style="display:none"-->
    </tr>
    
    <tr>
      <td>&nbsp;</td>
      <td colspan="2"><label>
        <input type="button" name="button3" id="button3" value="　上傳　" onclick="javascript:jbookup();" />
       　　　 
       <input type="reset" name="button4" id="button4" value="　重置　" />
       <input type="hidden" name="hidden_up" id="hidden_up" />
      </label></td>
    </tr>
    
  </table>
</form>
<?php
if($_POST['hidden_up']!=""){
	$m_boolen = false ;
	$m_upname = explode(",",$_POST['hidden_up']) ;
	
	//上傳TXT檔
	$m_path = "upload/book/".$m_upname[0] ;
	copy($_FILES['b_path']['tmp_name'] , $m_path);
	
	//上傳圖片
	if(!($_FILES['b_image']['name']=='')){	
		$m_file = $m_upname[1] ;
		if($_FILES['b_image']['error']>0){
			switch($_FILES['b_image']['error']){
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
		if(is_uploaded_file($_FILES['b_image']['tmp_name'])){
			//echo "Wait...."."<br>";
			$Destdir="upload/images/";
			if(!is_dir($Destdir)){
				echo "傳輸失敗 : 目錄不存在" ;
			}
			elseif(!is_writeable($Destdir)){
				echo "傳輸失敗 : 無法寫入" ;
			}
			else {
				copy($_FILES['b_image']['tmp_name'] , $Destdir."/".$m_file);
				$m_file_name = 'upload/images/'.$m_file ;
				$size = getimagesize($m_file_name) ;
				if(!($size[2]==1)&&!($size[2]==2)){ // 1為GIF檔　２為JPG/JPEG檔
					umsg("圖檔格式僅限 GIF,JPEG,JPG");
					$m_boolen = true ;
					unlink($m_file_name) ;
				}
				else{
					copy($_FILES['b_image']['tmp_name'] , $m_file_name);
				}
			}
		}
	}
	echo "<script>alert(' 書籍上傳完成! ');</script>" ;
}
?>
</body>
</html>
