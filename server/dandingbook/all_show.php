<?php
session_start() ;
if(!session_is_registered("LOGIN")){
	echo "<script>alert('請由正常管道登入');</script>" ;
	echo "<script>location.href= 'login.html';</script>" ;
}
include("_func/config.inc.php");

mysql_query("SET NAMES 'utf8'"); 
mysql_query("SET CHARACTER_SET_CLIENT=utf8"); 
mysql_query("SET CHARACTER_SET_RESULTS=utf8");
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>無標題文件</title>
<style type="text/css">
<!--



.style6 {color: #333333}
.style7 {font-size: 15px; color: #333333; }
.style11 {color: #FFFFFF; font-size: 17px; font-weight: bold; }
.style16 {font-size: 15px; color: #404040; }
.style8 {color: #333333; font-weight: bold; }
.style9 {color: #990000}
.style10 {
	font-size: 24px;
	font-weight: bold;
	color: #000000;
}
.style18 {
	font-size: 30px;
	color: #D85110;
}
.style20 {
	font-size: 24px;
	color: #804000;
	font-family: "Courier New", Courier, monospace;
}
.style27 {font-size: 15px; color: #804000; }
.style29 {
	font-size: 12px;
	color: #666666;
}
.style31 {font-size: 17px; font-weight: bold; color: #5C2D0A; cursor: pointer; }
.style33 {color: #990000; font-weight: bold; }
.style35 {
	font-size: 17px;
	font-weight: bold;
	color: #999999;
	cursor: pointer;
}
.style37 {
	font-size: 17px;
	font-weight: bold;
	color: #666666;
	cursor: pointer;
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
	if(m_path=="") m_str = m_str + " 路徑" ;
	if(m_str!=""){
		alert(m_str+" ,欄位不得空白喔!") ;
	}
	else{
		m_intro = document.getElementById('b_intro').value ;
		m_author = document.getElementById('b_author').value ;
		m_issue = document.getElementById('b_issue').value ;
		m_image = document.getElementById('b_image').value ;
		m_data = m_name+","+m_intro+","+m_author+","+m_issue+","+m_path+","+m_image ;
		m_return = jajax("sql_ajax.php?case=insert&data="+m_data) ;
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
function jdiv(m_divid){
	for(var m_i=0;m_i<=3;m_i++){
		document.getElementById('div_'+m_i).style.display = "none" ;
		document.getElementById('myspan_'+m_i).className = "style35" ;
	}
	document.getElementById('div_'+m_divid).style.display = "block" ;
	document.getElementById('myspan_'+m_divid).className = "style31" ;
}
function jlogout(){
	if(confirm("您確定要登出嗎?")){
		m_return = jajax("sql_ajax.php?case=logout") ;
		if(m_return){
			location.href="login.html" ;
		}
	}
}
function jselect_change(){
	m_select = document.getElementById('select1').value ;
	document.getElementById('sel_hidden').value = m_select ;
	document.form2.submit() ;
	
}
</script>
</head>

<body>
<h1 align="center" class="style10 style18">Danding Book 後台管理</h1>
<hr />
<table width="70%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
  	<span id="myspan_0" class="style31"></span>
    <td align="center"><span id="myspan_1" class="style35" onclick="jdiv(1);">書籍上傳系統</span></td>
    <td align="center"><span id="myspan_2" class="style35" onclick="jdiv(2);">書籍列表</span></td>
    <td align="center"><span id="myspan_3" class="style35" onclick="jdiv(3);">會員下載列表</span></td>
    <td align="center"><span id="myspan_4" class="style35" onclick="jlogout();">登出</span></td>
  </tr>
</table>
<hr />
<br />


<div id="div_1" style="display:none" align="center">
<form action="" method="post" enctype="multipart/form-data" name="form1" id="form1">
  <table width="682" height="269" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td colspan="3" align="center">
        <h1 class="style20">書籍檔案上傳系統</h1>    </td>
    </tr>
    <tr>
      <td width="169" align="right" valign="middle"><span class="style27">*書名&nbsp;</span></td>
      <td colspan="2">&nbsp;
      <input name="b_name" type="text" id="b_name" size="20" /></td>
    </tr>
    <tr>
      <td align="right" valign="middle"><span class="style27">作者&nbsp;</span></td>
      <td colspan="2"><label>
        &nbsp;
        <input name="b_author" type="text" id="b_author" size="20" />
      </label> </td>
    </tr>
    <tr>
      <td align="right" valign="middle"><span class="style27">出版社&nbsp;</span></td>
      <td colspan="2"><label>
        &nbsp;
        <input name="b_issue" type="text" id="b_issue" size="20" />
      </label></td>
    </tr>
    <tr>
      <td align="right" valign="middle"><span class="style27">簡介&nbsp;</span></td>
      <td colspan="2"><label>
        &nbsp;
        <input name="b_intro" type="text" id="b_intro" size="65" maxlength="60" />
      </label></td>
    </tr>
    <tr>
      <td height="34" align="right" valign="middle"><span class="style27">*資料檔案路徑&nbsp;</span></td>
      <td><label>
      &nbsp;
      <input type="file" name="b_path" id="b_path"/>
      </label></td>
      <td valign="middle"><span class="style29 style6">*注:格式限制.txt檔案</span></td>
    </tr>
    <tr>
      <td align="right" valign="middle"><span class="style27">圖片檔案路徑&nbsp;</span></td>
      <td width="281" valign="middle"><label>
      &nbsp;
      <input type="file" name="b_image" id="b_image" />
      <!--onchange="javascript:jimgshow('myimg',this.value);"-->
      </label></td>
      <td width="232" rowspan="2" align="left" valign="top"><span class="style29 style6">*注: 圖片格式，限制於GIF、JPG<br />
        　　圖片將自動壓縮至　寬200</span>        </td>
    </tr>
    
    <tr>
      <td height="54">&nbsp;</td>
      <td valign="bottom"><label>
        <input name="button3" type="button" class="style7" id="button3" onclick="javascript:jbookup();" value="　上傳　" />
       　　　 
       <input name="button4" type="reset" class="style7" id="button4" value="　重置　" />
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
					uimage_reduce($_FILES['b_image']['tmp_name'],$m_file_name,200,0);
					//copy($_FILES['b_image']['tmp_name'] , $m_file_name);
				}
			}
		}
	}
	echo "<script>alert(' 書籍上傳完成! ');</script>" ;
}
?>
</div>
<div id="div_2" style="display:none" align="center">
<?php
$m_book = DB_QUERY("SELECT * FROM $GLOBALS[DB_BOOK]");
?>
<h1 class="style20">書 籍 列 表</h1>
<table width="90%" height="69" border="1" cellpadding="0" cellspacing="0" bordercolor="#FFFFFF">
  <tr>
    <td width="5%" height="33" align="center" bgcolor="#B54E02"><span class="style11">編號</span></td>
    <td width="19%" align="center" bgcolor="#B54E02"><span class="style11">書名</span></td>
    <td width="11%" align="center" bgcolor="#B54E02"><span class="style11">資料檔名</span></td>
    <td width="11%" align="center" bgcolor="#B54E02"><span class="style11">圖片檔名</span></td>
    <td width="9%" align="center" bgcolor="#B54E02"><span class="style11">作者</span></td>
    <td width="10%" align="center" bgcolor="#B54E02"><span class="style11">出版社</span></td>
    <td width="35%" align="center" bgcolor="#B54E02"><span class="style11">簡介</span></td>
  </tr>
<?php
$m_k = 0 ;
while($m_book_row=mysql_fetch_array($m_book)){
	$m_k++ ;
	$m_bg = "#FFDBCA" ;
	if($m_k%2==0){
		$m_bg = "#FEEDC9" ;
	}
?>
  
  <tr bgcolor=<?php echo $m_bg ; ?> >
    <td height="33" align="center"><span class="style16">&nbsp;<?php echo $m_book_row['b_id'] ; ?></span></td>
    <td><span class="style16">&nbsp;<?php echo $m_book_row['b_name'] ; ?></span></td>
    <td><span class="style16">&nbsp;<?php echo $m_book_row['b_path'] ; ?></span></td>
    <td><span class="style16">&nbsp;<?php echo $m_book_row['b_image'] ; ?></span></td>
    <td><span class="style16">&nbsp;<?php echo $m_book_row['b_author'] ; ?></span></td>
    <td><span class="style16">&nbsp;<?php echo $m_book_row['b_issue'] ; ?></span></td>
    <td bgcolor="<?php echo $m_bg ; ?>"><span class="style16">&nbsp;<?php echo $m_book_row['b_intro'] ; ?></span></td>
  </tr>
<?php
}
?>
</table>
</div>
<form id="form2" name="form2" method="post" action="">
<?php
$m_where = "" ;
$m_div = "none" ;
$m_div0 = "block" ;
if($_POST['sel_hidden']!=""){
	if($_POST['sel_hidden']!="0"){
		$m_where = "WHERE B.b_id=".$_POST['select1'] ;
	}
	$m_div = "block" ;
	$m_div0 = "none" ;
}
?>
<div id="div_3" style="display:<?php echo $m_div ;?>" align="center">
<?php
$m_user = DB_QUERY("SELECT * FROM $GLOBALS[DB_USER]");
$m_all_std = "selected='selected'" ;
while($m_user_row=mysql_fetch_array($m_user)){
	$m_selected = "" ;
	if($m_user_row['b_id']==$_POST['sel_hidden']){
		$m_selected = "selected='selected'" ;
		$m_all_std = "" ;
	}
	$m_option = $m_option."<option ".$m_selected." value='".$m_user_row['b_id']."'>".$m_user_row['b_name']."</option>" ;
}
?>

<h1 class="style20"><strong>會員下載列表</strong></h1>
  <table width="50%" border="0">
    <tr>
      <td align="right"><span class="style33">選擇會員:</span>
        <select name="select1" id="select1" onchange="jselect_change();">
          <option value="0" <?php echo $m_all_std ;?> >全部顯示</option>
          <?php echo $m_option ; ?>
          </select>
        <input type="hidden" name="sel_hidden" id="sel_hidden" />      </td>
    </tr>
  </table>
  <table width="50%" border="0" cellpadding="0" cellspacing="0"  bordercolor="#FFFFFF">
    <tr bgcolor="#B54E02">
      <td width="10%" height="30" align="center"><span class="style11">編號</span></td>
      <td width="24%" align="center"><span class="style11">會員姓名</span></td>
      <td width="27%" align="center"><span class="style11">會員帳號</span></td>
      <td width="39%" align="center"><span class="style11">已下載書名</span></td>
    </tr>
<?php

$m_i = 0 ;
$m_query = DB_QUERY("SELECT A.b_uid AS uid_1 ,B.b_name AS user_name ,B.b_account AS user_id 
								FROM $GLOBALS[DB_USER_BOOK] A 
									INNER JOIN $GLOBALS[DB_USER] B ON B.b_id = A.b_uid 
									".$m_where."
									GROUP BY b_uid");
	
$m_num = mysql_num_rows($m_query) ;
if($m_num==0){
	echo "<tr><td colspan='4' align='center'><span class='style9'><b><br>此會員無資料</b></span></td></tr>" ;
}
while($m_row=mysql_fetch_array($m_query)){
	$m_i++ ;
	$m_query2 = DB_QUERY("SELECT A.b_id AS id ,A.b_uid AS uid ,C.b_name AS book_name
	FROM $GLOBALS[DB_USER] B
	INNER JOIN $GLOBALS[DB_USER_BOOK] A ON A.b_uid = B.b_id
	INNER JOIN $GLOBALS[DB_BOOK] C ON A.b_bkid = C.b_id
	WHERE A.b_uid = ".$m_row['uid_1']);
	$m_j = 0 ;
	while($m_row2=mysql_fetch_array($m_query2)){
		$m_j++ ;
		$m_bg = "#FFDBCA" ;
		if($m_j%2==0){
			$m_bg = "#FEEDC9" ;
		}
?>
        <tr>
          <td align="center" height="38" bgcolor="#FFDBCA" class="style6" id="<?php echo "td0_".$m_i."_".$m_j?>"><span class="style7">&nbsp;</span></td>
          <td align="center" bgcolor="#FFDBCA" id="<?php echo "td1_".$m_i."_".$m_j?>"><span class="style7">&nbsp;</span></td>
          <td align="center" bgcolor="#FFDBCA" id="<?php echo "td2_".$m_i."_".$m_j?>"><span class="style7">&nbsp;</span></td>
          <td bgcolor=<?php echo $m_bg ; ?>>
            <table width="100%" border="0" cellspacing="0" bordercolor="#FFFFFF">
              <tr>
                <td valign="bottom"><span class="style7"><?php echo $m_row2['book_name']; ?></span></td>
              </tr>
            </table></td>
        </tr>
<?php
	}
?>
	<tr>
      <td height="1" colspan="4" align="center" bgcolor="#666666"></td>
    </tr>
<?php
	echo "<script>document.getElementById('td0_".$m_i."_1').innerHTML='<span class=\'style7\'>".$m_i."</span>';</script>" ;
	echo "<script>document.getElementById('td1_".$m_i."_1').innerHTML='<span class=\'style7\'>".$m_row['user_name']."</span>';</script>" ;
	echo "<script>document.getElementById('td2_".$m_i."_1').innerHTML='<span class=\'style7\'>".$m_row['user_id']."</span>';</script>" ;
}
?>
  </table>

</div>

<div class="style8" id="div_0" style="display:<?php echo $m_div0; ?>">
  <div align="center" class="style9">請由上方列表選擇您要的工作</div>
</div>
</form>
</body>


</html>
