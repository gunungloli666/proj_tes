# untuk mencari file yang paling  panjang isinya

$directory = dir; 
$number = 0; 
$nama  = ""; 
$file_name = [regex] "PART_*"; 

write-host ""
write-host ""

foreach($item in $directory ){
	$i=0; 
	$hasil_2 = $file_name.matches($item.name);
	if($hasil_2[0].success){
		continue; 
	}
	$data = get-content $item ; 
	foreach($line in $data){
		$i++; 
	}
	write-host "nama file: $item.name. Panjang baris: $i";
	if($i -gt $number){
		$number = $i;
		$nama = $item.name; 
	}
}
write-host ""
write-host "nama file terpanjang:  $nama,     panjang  = $number";
write-host ""