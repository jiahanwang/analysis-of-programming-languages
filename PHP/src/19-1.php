<?php

function load_plugins(){
	$config  = parse_ini_file("config.ini");
	require_once $config["frequencies"];
	require_once $config["words"];
}

load_plugins();
$word_freqs = top_25(extract_words($argv[1], $argv[2]));
$count  = 0;
foreach($word_freqs as $key => $val){
	print $key.' - '.$val."\n";
	if(++$count >= 25) break;
}