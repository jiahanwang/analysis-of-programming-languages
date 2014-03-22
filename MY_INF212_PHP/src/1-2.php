<?php
/**
 * User: Jiahan
 * Date: 1/9/14
 * Time: 2:45 PM
 * To translate the Old time style
 */
error_reporting(E_ERROR);

# fopen in PHP does not provide buffer size parameter, so this function has only two parameters
function touchOpen($filename, $args){
    try{
        if(!unlink($filename))
            throw new Exception('Cannot delete the file');
    }catch(Exception $e){
        # do nothing just like the Python code
    }
    fclose(fopen($filename, 'a'));
    return fopen($filename, $args);
}

# PART 1
# Load the list of stop words
$data = array();
$f = fopen('stop_words.txt','rb');
$data[0] = preg_split('/,/', fread($f, 1024));
fclose($f);
array_push($data, array());
array_push($data, null);
array_push($data, 0);
array_push($data, false);
array_push($data, '');
array_push($data, '');
array_push($data, 0);
# $data[8] is the temporary identifier to eliminate $c
array_push($data, null);
# Open the secondary memory
$word_freqs = touchOpen('word_freqs', 'rb+');
# Open the input file, use input.txt
$f = fopen('input.txt','rb');
# Loop over input file's lines
while(true){
    $data[1] = str_split(fgets($f));
    if($data[1] === array(''))
        break;
    $data[2] = null;
    $data[3] = 0;
    # Loop over characters in the line
    # use $data[8] to eliminate $c
    foreach($data[1] as $data[8]){
        if($data[2] === null){
            if(ctype_alnum($data[8])){
                $data[2] = $data[3];
            }
        }
        else
            if(!ctype_alnum($data[8])){
            $data[4] = false;
            $data[5] = strtolower(implode(array_slice($data[1], $data[2], $data[3]-$data[2])));
            if(strlen($data[5]) >= 2 & !in_array($data[5], $data[0])){
                while(true){
                    $data[6] = trim(fgets($word_freqs));
                    if($data[6] === '')
                        break;
                    $data[7] = (int)array_pop(preg_split('/,/',$data[6]));
                    $data[6] = trim(array_shift(preg_split('/,/',$data[6])));
                    if ($data[5] === $data[6]){
                        $data[7] += 1;
                        $data[4] = true;
                        break;
                    }
                }
                if(!$data[4]){
                    fseek($word_freqs, 0, SEEK_CUR);
                    fwrite($word_freqs,sprintf("%1$20s,%2$04d\n", $data[5],1));
                    rewind($word_freqs);
                }else{
                    fseek($word_freqs, -26, SEEK_CUR);
                    fwrite($word_freqs,sprintf("%1$20s,%2$04d\n", $data[5],$data[7]));
                    rewind($word_freqs);
                }
            }
            $data[2] = null;
        }
        $data[3] += 1;
    }
}
fclose($f);
fflush($word_freqs);

# PART 2
unset($data);
# $data[25] is firstly used as a temporary identifier to eliminate $data[27]
$data[25] = null;
# use $data[25] to eliminate $data[27]
foreach (range(0,24) as $data[25]){
    $data[$data[25]] = array();
}
$data[26] = 0;
while(true){
    $data[25] = trim(fgets($word_freqs));
    if($data[25] === '')
        break;
    $data[26] = (int)array_pop(preg_split('/,/',$data[25]));
    $data[25] = trim(array_shift(preg_split('/,/',$data[25])));
    # $data[27] is a temporary identifier to eliminate $i
    $data[27] = 0;
    # use $data[27] to eliminate $i
    foreach(range(0,24) as $data[27]){
        if($data[$data[27]] === array() || $data[$data[27]][1] < $data[26]){
            $data = array_merge(array_slice($data, 0, $data[27]),array(array($data[25], $data[26])),array_slice($data, $data[27]));
            array_pop($data);
            break;
        }
    }
}
# $data[25] is the temporary identifier to eliminate $tf
foreach($data as $data[25]){
    if(count($data[25]) === 2)
        print_r($data[25][0].' - '.$data[25][1].'<br/>');
}