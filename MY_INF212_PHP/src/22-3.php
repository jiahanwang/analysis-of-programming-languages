<?php
/**
 * User: Jiahan
 * Date: 2/11/14
 * Time: 14:32 PM
 */
class TFTheOne{

    private $value;

    function __construct($v){
        $this->value = $v;
    }

    public function bind($func){
        try{
            $this->value = $func($this->value);
        }catch(Exception $e){
            exit($e->getMessage() . '   in   ' . $func);
        }
        return $this;
    }

    public function print_me(){
        print $this->value;
    }
}
// Take over default error handler
set_error_handler( 'my_error_handler' );

function my_error_handler($errno, $errmsg, $filename, $linenum, $vars){
    if (!(error_reporting() & $errno))
        return;
    throw new Exception($errmsg);
}

function get_input(){
    global $argv;
    if(count($argv) < 2) throw new Exception("You idiot! I need an input file! I quit!");
    return $argv[1];
}

function extract_words($path){
    if(gettype($path) != 'string') throw new Exception('I need a string! I quit!');
    if(strlen($path) == 0) throw new Exception('I need a non-empty string! I quit!');

    return preg_split('/\s+/', trim(strtolower(preg_replace('/[\W_]+/', ' ', file_get_contents($path)))));
}

function remove_stop_words($words){
    if(gettype($words) != 'array') throw new Exception('I need a list of words! I quit!');

    $stop_words = preg_split('/,/', file_get_contents('../stop_words.txt'));
    return array_diff($words, array_merge ($stop_words, range('a', 'z')));
}

function frequencies($words){
    if(gettype($words) != 'array') throw new Exception('I need a list of words! I quit!');
    if(count($words) == 0) throw new Exception('I need a non-empty list! I quit!');

    $word_freqs = array();
    foreach($words as $word)
        if(array_key_exists($word, $word_freqs))
            $word_freqs[$word] ++ ;
        else
            $word_freqs[$word] = 1;
    return $word_freqs;
}

function freq_sort($word_freqs){
    if(gettype($word_freqs) != 'array' ) throw new Exception('I need a dictionary! I quit!');
    if(count($word_freqs) == 0) throw new Exception('I need a non-empty dictionary! I quit!');
    if(gettype(key($word_freqs)) != 'string' ) throw new Exception('I need a dictionary! I quit!');

    arsort($word_freqs);
    return $word_freqs;
}

function top25_freqs($word_freqs){
    if(gettype($word_freqs) != 'array' ) throw new Exception('I need a dictionary! I quit!');
    if(count($word_freqs) == 0) throw new Exception('I need a non-empty dictionary! I quit!');
    if(gettype(key($word_freqs)) != 'string' ) throw new Exception('I need a dictionary! I quit!');

    $top_25 = "";
    $count  = 0;
    foreach($word_freqs as $key => $val){
        $top_25 .= $key.' - '.$val."\n";
        if(++$count >= 25) break;
    }
    return $top_25;
}

$tf_the_one  = new TFTheOne(null);
$tf_the_one->bind("get_input")->bind("extract_words")->bind("remove_stop_words")->bind("frequencies")->bind("freq_sort")->bind("top25_freqs")->print_me();