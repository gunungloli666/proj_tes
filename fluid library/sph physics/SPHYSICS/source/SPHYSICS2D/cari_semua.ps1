$a = dir; 
$file_sumber = get-content "getdata_2D.f"; 

$i = 1; 
$potongan = [regex] "read\(11\,\*\)";
$pertama = [regex] "\b";  
$splitter = [regex] "\s+";

clear-content ./test.txt; 
foreach($item in $file_sumber){
	if($item -match $potongan){
		$kata_kunci = $item -replace  "read\(11\,\*\)", "" -split "\s+";  
		write-host $kata_kunci[1]; 
		add-content -value $kata_kunci[1]  -path ./test.txt;
	}
}