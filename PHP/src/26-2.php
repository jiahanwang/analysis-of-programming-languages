<?php
// The columns.
$all_words = array(array(), null);
$stop_words = array(array(), null);
$non_stop_words = array(array(), 
						function () { 
							global $stop_words;
							global $all_words;
							return array_diff($all_words[0], $stop_words[0]);
						});
$unique_words = array(array(), 
						function () { 
							global $non_stop_words;
							return array_values(array_unique($non_stop_words[0]));
						});
$counts = array(array(), 
						function() {
							global $non_stop_words;
							global $unique_words;
							$counts_in_unique = array();
							$counts_in_all = array_count_values($non_stop_words[0]);
							foreach($unique_words[0] as $word)
								array_push($counts_in_unique, $counts_in_all[$word]);
							return $counts_in_unique;
						});
$sorted_data = array(array(), 
						function () {
							global $unique_words;
							global $counts;
							$frequencies = array();
							foreach($unique_words[0] as $key => $word)
								$frequencies[$word] = $counts[0][$key];
							arsort($frequencies);
							return $frequencies;
						});

// The entire spreadsheet
$all_columns = array(&$all_words, &$stop_words, &$non_stop_words, &$unique_words, &$counts, &$sorted_data);

// The update function
function update(){
	global $all_columns;
	foreach($all_columns as &$row){
		if($row[1] != null)
			$row[0] = $row[1]();
	}
}

// Add the stop words to the second column
$stop_words[0]= array_merge(range('a', 'z'), preg_split('/,/', file_get_contents('../stop_words.txt')));
while(1){
	print "Input a new file path:";
	$path = trim(fgets(STDIN));
	if(! $file = @file_get_contents($path)){
		print "Cannot find this file, please check!\n";
		continue;
	}
	// Add the data into the first column
	$all_words[0]= array_merge($all_words[0], preg_split('/\s+/', trim(strtolower(preg_replace('/[\W_]+/', ' ', $file)))));
	// Update the columns with formulas
	update();
	// Print the results
	$count  = 0;
	print "\nThe result for ".$path.":\n";
	foreach($sorted_data[0] as $key => $val){
		if($count++ >= 25) break;
		print  $key.' - '.$val."\n";
	}
	print "\n";
}
