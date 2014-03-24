<?php
function lines($filename){
	$f = fopen($filename, "r");
	try{
		while(($line = fgets($f)) !== false)
			yield $line;
	} finally {
		fclose($f);
	}
}

function words_in_line($filename){
	foreach(lines($filename) as $line)
		yield  preg_split('/\s+/', trim(strtolower(preg_replace('/[\W_]+/', ' ', $line))));
}

function non_stop_words($filename){
	$stop_words = array_merge(range('a', 'z'), preg_split('/,/', file_get_contents('../stop_words.txt')));
	foreach(words_in_line($filename) as $words_in_line){
		foreach($words_in_line as $w)
			if(!in_array($w, $stop_words) && strlen($w) !== 0)
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
