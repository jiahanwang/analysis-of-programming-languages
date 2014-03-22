<?php

function extract_words($path, $stop_path){
		$words = preg_split("/\s+/", trim(strtolower(preg_replace('/[\W]+/', ' ', file_get_contents($path)))));
		$stop_words = preg_split('/,/', file_get_contents($stop_path));
		return array_diff($words, array_merge($stop_words, range('a', 'z')));
}