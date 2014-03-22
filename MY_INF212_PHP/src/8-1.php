<?php
/**
 * Created by JetBrains PhpStorm.
 * User: Jiahan
 * Date: 1/21/14
 * Time: 10:12 PM
 * To change this template use File | Settings | File Templates.
 */

function read_file($path, &$word_freqs, $func){
    $func(file_get_contents($path), $word_freqs, 'normalize');
}

function filter_chars($words_string, &$word_freqs, $func){
    $func(preg_replace('/[\W_]+/', ' ', $words_string), $word_freqs, 'scan');
}

function normalize($words_string, &$word_freqs, $func){
    $func(strtolower($words_string), $word_freqs, 'remove_stop_words');
}

function scan($words_string, &$word_freqs, $func){
    $func(preg_split('/\s+/', trim($words_string)), $word_freqs, 'frequencies');
}

function remove_stop_words($words, &$word_freqs, $func){
    $stop_words = preg_split('/,/', file_get_contents('stop_words.txt'));
    $func(array_diff($words, array_merge ($stop_words, range('a', 'z'))), $word_freqs, 'freq_sort');
}

function frequencies($words, &$word_freqs, $func){
    $wf = array();
    foreach($words as $word){
        if(array_key_exists($word, $wf))
            $wf[$word] ++ ;
        else
            $wf[$word] = 1;
    }
    $func($wf, $word_freqs, 'no_op');
}

function freq_sort($wf, &$word_freqs, $func){
    arsort($wf);
    $word_freqs = $func($wf, null);
}

function no_op($wf, $func){
    return $wf;
}

$word_freqs = array();
read_file($argv[1], $word_freqs, 'filter_chars');
$count  = 0;
foreach($word_freqs as $key => $val){
    print $key.' - '.$val."\n";
    if(++$count >= 25) break;
}