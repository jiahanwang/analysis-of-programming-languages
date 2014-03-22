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
    foreach($data[1] as $c){
        if($data[2] === null){
            if(ctype_alnum($c)){
                $data[2] = $data[3];
            }
        }elseif(!ctype_alnum($c)){
            $data[4] = false;
            $data[5] = strtolower(implode(array_slice($data[1], $data[2], $data[3]-$data[2])));

            if(strlen($data[5]) >= 2 & !in_array($data[5], $data[0])){
                while(true){
                    $data[6] = trim(fgets($word_freqs));
                    if($data[6] === ''){
                        break;
                    }
                    $data[7] = (int)array_pop(preg_split('/,/',$data[6]));
                    $data[6] = trim(array_shift(preg_split('/,/',$data[6])));
                    if ($data[5] == $data[6]){
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
foreach (range(0,24) as $i){
    $data[$i] = array();
}
array_push($data, '');
array_push($data, 0);
while(true){
    $data[25] = trim(fgets($word_freqs));
    if($data[25] === '')
        break;
    $data[26] = (int)array_pop(preg_split('/,/',$data[25]));
    $data[25] = trim(array_shift(preg_split('/,/',$data[25])));
    foreach(range(0,24) as $i){
        if($data[$i] === array() || $data[$i][1] < $data[26]){
            $data = array_merge(array_slice($data, 0, $i),array(array($data[25], $data[26])),array_slice($data, $i));
            array_pop($data);
            break;
        }
    }
}
foreach($data as $tf){
    if(count($tf) === 2)
        print_r($tf[0].' - '.$tf[1].'<br/>');
}