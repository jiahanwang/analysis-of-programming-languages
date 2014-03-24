<?php
/**
 * User: Jiahan
 * Date: 2/11/14
 * Time: 18:10 PM
 */
class TFQuarantine{

    private $funcs;

    function __construct($func){
        $this->funcs = array($func);
    }

    public function bind($func){
        array_push($this->funcs, $func);
        return $this;
    }

    public function execute(){
        // check the array of function names after binding but before execution
        $output =  'Functions in the array before exectuion: ';
    		foreach($this->funcs as $func)
    			$output .= $func.', ';
    	$output .= "\n\n";
    	print $output;
        
  		// execute all the functions
        function guard_callable($v){
        	return is_callable($v)? $v() : $v;
        }
        $value = null;
        foreach($this->funcs as $key => $func){
            $value = $func(guard_callable($value));
            // delete the func name in the array after it is executed
            unset($this->funcs[$key]);
        }
        print guard_callable($value);
        
        // check the array of function names after execution
        $output =  "\nFunctions in the array after exectuion: ";
        foreach($this->funcs as $func)
        	$output .=  $func.', ';
        print $output;
    }
}

function get_input(){
    return function (){
        global $argv;
        return $argv[1];
    };
}

function extract_words($path){
    return function () use ($path) {
        return preg_split('/\s+/', trim(strtolower(preg_replace('/[\W_]+/', ' ', file_get_contents($path)))));
    };
}

function remove_stop_words($words){
    return function () use($words){
        $stop_words = preg_split('/,/', file_get_contents('../stop_words.txt'));
        return array_diff($words, array_merge ($stop_words, range('a', 'z')));
    };
}

function frequencies($words){
    $word_freqs = array();
    foreach($words as $word)
        if(array_key_exists($word, $word_freqs))
            $word_freqs[$word] ++ ;
        else
            $word_freqs[$word] = 1;
    return $word_freqs;
}

function freq_sort($word_freqs){
    arsort($word_freqs);
    return $word_freqs;
}

function top25_freqs($word_freqs){
    $top_25 = '';
    $count  = 0;
    foreach($word_freqs as $key => $val){
        $top_25 .= $key.' - '.$val."\n";
        if(++$count >= 25) break;
    }
    return $top_25;
}

$tf_quarantine  = new TFQuarantine('get_input');
$tf_quarantine->bind('extract_words')->bind('remove_stop_words')->bind('frequencies')->bind('freq_sort')->bind('top25_freqs');
$tf_quarantine->execute();