# cari jumlah file PART_*

$regex = [regex] "PART_0*"; 
$direc = dir;
$a = 0;  
foreach($item  in $direc){
	$content = get-content $item; 
	$hasil = $regex.matches($item.name); 
	if($hasil[0].success){
		$a++; 
		write-host "nama: $item"; 
	}
}


write-host "jumlah file: $a"; 