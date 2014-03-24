<?php
function characters($filename){
	$f = fopen($filename, "r");
	try{
		while (($line = fgets($f)) !== false) {
			$line = str_split($line);
			foreach($line as $c)
				yield $c;
		}
	} finally {
		fclose($f);
	}
}

function all_words($filename){
	$start_char = true;
	foreach(characters($filename) as $c){
		if($start_char == true){
			$word = '';
			if(ctype_alnum($c)){
				$word = strtolower($c);
				$start_char = false;
			}
			
		}elseif(ctype_alnum($c))
				$word .= strtolower($c);
			 else{
			 	$start_char = true;
			 	yield $word;
			 }
	}
}

function non_stop_words($filename){
	$stop_words = array_merge(range('a', 'z'), preg_split('/,/', file_get_contents('../stop_words.txt')));
	foreach(all_words($filename) as $w){
		if(!in_array($w, $stop_words))
			yield $w;
	}
}

function count_and_sort($filename){
	$freqs = array();
	$i = 1;
	foreach(non_stop_words($filename) as $w){
		if(array_key_exists($w, $freqs))
			$freqs[$w] ++ ;
		else
			$freqs[$w] = 1;
		if($i++ % 5000 == 0){
			arsort($freqs);
			yield $freqs;
		}
	}
	arsort($freqs);
	yield $freqs;
}

foreach(count_and_sort($argv[1]) as $word_freqs){
	print "-----------------------------\n";
	foreach(array_slice($word_freqs, 0, 25) as $key => $value)
		print  $key.' - '.$value."\n";
}
