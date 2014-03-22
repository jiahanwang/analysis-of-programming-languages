<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Jiahan
 * Date: 1/21/14
 * Time: 11:32 PM
 * To change this template use File | Settings | File Templates.
 */
class TFTheOne{

    private $value;

    function __construct($v){
        $this->value = $v;
    }
    public function bind($func){
        $this->value = $func($this->value);
        //var_dump($this->value);
        return $this;
    }

    public function print_me(){
        print $this->value;
    }
}

function read_file($path){
    return file_get_contents($path);
}

function filter_chars($words_string){
    return preg_replace('/[\W_]+/', ' ', $words_string);
}

function normalize($words_string){
    return strtolower($words_string);
}

function scan($words_string){
    return preg_split('/\s+/', trim($words_string));
}

function remove_stop_words($words){
    $stop_words = preg_split('/,/', file_get_contents('stop_words.txt'));
    return array_diff($words, array_merge ($stop_words, range('a', 'z')));
}

function frequencies($words){
    $word_freqs = array();
    foreach($words as $word){
        if(array_key_exists($word, $word_freqs))
            $word_freqs[$word] ++ ;
        else
            $word_freqs[$word] = 1;
    }
    return $word_freqs;
}

function freq_sort($word_freqs){
    arsort($word_freqs);
    return $word_freqs;
}

function top25_freqs($word_freqs){
    $top_25 = "";
    $count  = 0;
    foreach($word_freqs as $key => $val){
        $top_25 .= $key.' - '.$val."\n";
        if(++$count >= 25) break;
    }
    return $top_25;
}

$tf_the_one  = new TFTheOne($argv[1]);

$tf_the_one->bind("read_file")->bind("filter_chars")->bind("normalize")->bind("scan")
           ->bind("remove_stop_words")->bind("frequencies")->bind("freq_sort")->bind("top25_freqs")->print_me();
