<?php
/**
 * User: Jiahan
 * Date: 2/11/14
 * Time: 18:37 PM
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
        function guard_callable($v){
            return is_callable($v)? $v() : $v;
        }
        $value = null;
        foreach($this->funcs as $func){
            $value = $func(guard_callable($value));
        }
        guard_callable($value);
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
    return function () use($word_freqs){
        $count  = 0;
        foreach($word_freqs as $key => $val){
            if($count++ >= 25) break;
            print  $key.' - '.$val."\n";
        }
    };
}

$tf_quarantine  = new TFQuarantine('get_input');
$tf_quarantine ->bind('extract_words')->bind('remove_stop_words')->bind('frequencies')->bind('freq_sort')->bind('top25_freqs');
$tf_quarantine->execute();