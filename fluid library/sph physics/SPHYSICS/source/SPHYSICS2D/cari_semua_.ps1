$aa = dir; 
$m = get-content "test.txt"; 

clear-content "testx.txt";

foreach($it in $m){
	write-host $it; 
	foreach($item in $aa){
		if($item.name -match ".f\b"){
			$x = "$it ==> $item ==> "; 
			$i = 1; 
			$state = 1; 
			$content = get-content $item; 
			foreach($line in $content){
				if($line -match "\b$it\b"){
					$x = "$x $i |";
					$state = $state + 1; 
				}
				$i = $i  + 1; 
			}
			if($state -gt 1){
				add-content -value $x -path ./testx.txt;
			}
		}
		
	}
}

