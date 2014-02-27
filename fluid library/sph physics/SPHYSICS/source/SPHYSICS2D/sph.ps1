$a = dir ;
$my_regex = [regex] "iopt_movingObject" ;
$file_name = [regex] "PART_"; 
$file_log = [regex] "log_"; 
# $value = "iopt_movingObject `n"; 
$xmin = [regex] "\bxmin\s*=";
$exe_ = [regex] ".exe"; 
$object_  = [regex] ".o";
$flag = [regex] "iflag\(\w\)"; 
$ibox = [regex] "\bibox";
$bc = [regex] "\bnc\(\w"; 
$itime = [regex] "\bitime\s*="; 
$kind = [regex] "\bkind_p"; 

$nc = [regex] "\bncx\s*=";
$kindp = [regex] "\bkind_p";

$xdot = [regex] "xdot"; 
$xcor = [regex] "xcor\(\w\)"; 

$awen = [regex] "\bAwen"; 

$viscdt = [regex] "\bvisc_dt\s*="; 

$eta2 = [regex] "\beta2\s*="; 
$eta= [regex] "\beta\s*="; 

$odedelta  = [regex] "\bod_Wdeltap\s*="; 
$ux = [regex] "\bux\(\w\)\s*=";
$ux1 = [regex] "\bux\(\w\)"; 

$TEp = [regex] "\bTEp\(\w\)\s*="; 

$DT = [regex] "dt\s*=";

$GRX = [regex] "\bgrx\s*=";  

$IPeriodic = [regex] "\bi_periodicOB"; 

$LUdec = [regex] "LU_decomposition"; 

$density = [regex] "densityFilter";  

$indextensile = [regex] "index_tensile"; 

$od_Wdeltap = [regex] "od_Wdeltap";

$press = [regex] "\bp\(\w\)\s*="

$periodicity = [regex] "call periodicityCorrection"; 

$viscos = [regex] "call viscosity";

$index_tensile = [regex] "index_tensile"; 

$ly2  = [regex] "ly2"; 

$xcor = [regex] "xcor"; 

$cs = [regex] "\bcs\(\w\)";

$Eki_b = [regex] "Eki_b"; 

$energi = [regex] "call energy"; 

$grx = [regex] "grx"; 

$up = [regex] "up\(\w\)\s*"; 

$xp = [regex] "xp\(\w\)\s*"; 

$visc_dt = [regex] "visc_dt"; 

$viscos_val = [regex] "viscos_val"; 

$poute_ = [regex] "call poute"; 


$dudx_CSPH = [regex] "dudx_CSPH"; 

$pVol = [regex] "pVol"; 

$index_tensile = [regex] "index_tensile"; 

$cs0 = [regex] "cs0"; 

$TE0 = [regex] "TE0"; 

$rho0 = [regex] "rho0"; 
foreach($item in $a ){
	$hasil_2 = $file_name.matches($item.name);
	$hasil_3 = $file_log.matches($item.name); 
	$hasil_exe = $exe_.matches($item.name); 
	$hasil_object = $object_.matches($item.name); 
	
	if($hasil_2[0].success -or $hasil_3[0].success -or $hasil_exe[0].success -or $hasil_object[0].success){
		continue; 
	}
	$content = get-content $item; 
	$i = 0 ; 
	foreach($line in $content){
		$hasilcari = $rho0.matches($line) ; 
		$i++; 
		if($hasilcari[0].success){
			write-host "dapat di file $item,  pada baris $i,  pada perintah: $line"
			$value = "$value dapat di file $item,  pada baris $i,  pada perintah: $line`n"
		}
	}
}
set-content -value $value -Path ./log_6.txt
