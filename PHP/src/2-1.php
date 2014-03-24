<?php
/**
 * User: Jiahan
 * Date: 1/11/14
 * Time: 12:31 AM
 * To translate the Go Forth style
 */
$stack = array();

$heap = array();

function read_file(){
    global $stack;
    array_push($stack, file_get_contents(array_pop($stack)));
}

function filter_chars(){
    global $stack;
    array_push($stack, strtolower(preg_replace('/[\W_]+/', ' ', array_pop($stack))));
}

function scan(){
    global $stack;
    $stack = preg_split('/\s+/',array_pop($stack));
}

function remove_stop_words(){
    global $stack;
    global $heap;
    array_push($stack, preg_split('/,/', file_get_contents('stop_words.txt')));
    array_push($stack, array_merge(array_pop($stack), range('a', 'z')));
    $heap['stop_words'] = array_pop($stack);
    $heap['words'] = array();
    while(count($stack) > 0){
        if(in_array(end($stack), $heap['stop_words'])){
            array_pop($stack);
        }else
            array_push($heap['words'], array_pop($stack));
    }
    $stack = array_merge($stack, $heap['words']);
    unset($heap['stop_words']);
    unset($heap['words']);
}
function frequencies(){
    global $stack;
    global $heap;
    $heap['word_freqs'] = array();
    while(count($stack) > 0){
        if(array_key_exists(end($stack), $heap['word_freqs'])){
            array_push($stack, $heap['word_freqs'][end($stack)]);
            array_push($stack, 1);
            array_push($stack, array_pop($stack) + array_pop($stack));
        }else
            array_push($stack, 1);
        end($stack);
        $heap['word_freqs'][prev($stack)] = next($stack); # the calculation is from left to right
        array_pop($stack);
        array_pop($stack);
    }
    $stack = array_merge($stack, $heap['word_freqs']);
    unset($heap['word_freqs']);
}

function stack_sort(){
    global $stack;
    asort($stack);
}

array_push($stack,'input.txt');
read_file();
filter_chars();
scan();
remove_stop_words();
frequencies();
stack_sort();
array_push($stack, 0);
while(end($stack) < 25){
    $heap['i'] = array_pop($stack);
    end($stack);
    print_r(key($stack).' - '.end($stack).'<br/>');
    array_pop($stack);
    array_push($stack, $heap['i']);
    array_push($stack, 1);
    array_push($stack, array_pop($stack) + array_pop($stack));
}