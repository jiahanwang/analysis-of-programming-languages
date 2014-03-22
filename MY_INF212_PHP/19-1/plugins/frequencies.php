<?php

function top_25($words){
	$word_freqs = array();
	foreach($words as $word){
		if(array_key_exists($word, $word_freqs))
			$word_freqs[$word] ++ ;
		else
			$word_freqs[$word] = 1;
	}
	arsort($word_freqs);
	return array_slice($word_freqs, 0, 25);
}