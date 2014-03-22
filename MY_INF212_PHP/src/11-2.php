<?php
/**
 * User: Jiahan
 * Date: 1/28/14
 * Time: 11:42 PM
 */
class TFExercise{
    public function dispatch($message){
        if($message[0] == 'info')
            return $this->_info($message[1]);
        else
            throw new Exception('Message not understood '.$message[0]);
    }
    private function _info(){
        return get_called_class();
    }
}
class DataStorageManager{
    private $data = array();
    public function dispatch($message){
        if($message[0] == 'init')
            return $this->_init($message[1]);
        elseif($message[0] == 'words')
                return $this->_words();
             elseif($message[0] == 'info')
                    return $message[1]->dispatch(array('info')).': My major data structure is a '.get_called_class();
                 else
                     throw new Exception('Message not understood '.$message[0]);
    }
    private function _init($path){
        $this->data = preg_split('/\s+/',trim(strtolower(preg_replace('/[\W_]+/', ' ', file_get_contents($path)))));
    }
    private function _words(){
        return $this->data;
    }
}

class StopWordManager{
    private $stop_words = array();
    public function dispatch($message){
        if($message[0] == 'init')
            return $this->_init($message[1]);
        elseif($message[0] == 'is_stop_word')
                return $this->_is_stop_word($message[1]);
            elseif($message[0] == 'info')
                    return $message[1]->dispatch(array('info')).': My major data structure is a '.get_called_class();
                else
                    throw new Exception('Message not understood '.$message[0]);
    }
    private function _init(){
        $this->stop_words = array_merge(preg_split('/,/', file_get_contents('stop_words.txt')), range('a', 'z'));
    }
    private function _is_stop_word($word){
        return in_array($word, $this->stop_words);
    }
}

class WordFrequencyManager{
    private $word_freqs = array();
    public function dispatch($message){
        if($message[0] == 'increment_count')
            return $this->_increment_count($message[1]);
        elseif ($message[0] == 'sorted')
                return $this->_sorted();
            elseif($message[0] == 'info')
                return $message[1]->dispatch(array('info')).': My major data structure is a '.get_called_class();
            else
                throw new Exception('Message not understood '.$message[0]);
    }
    private function _increment_count($word){
        if(array_key_exists($word, $this->word_freqs)){
            $this->word_freqs[$word] ++;
        }
        else
            $this->word_freqs[$word] = 1;
    }
    private function _sorted(){
        arsort($this->word_freqs);
        return $this->word_freqs;
    }
}

class WordFrequencyController{
    public function dispatch($message){
        if($message[0] == 'init')
            return $this->_init($message[1]);
        elseif ($message[0] == 'run')
                return $this->_run($message[1]);
            elseif($message[0] == 'info')
                return $message[1]->dispatch(array('info')).': My major data structure is a '.get_called_class();
            else
                throw new Exception('Message not understood '.$message[0]);
    }
    private function _init($path){
        $this->storage_manager = new DataStorageManager();
        $this->stop_word_manager = new StopWordManager();
        $this->word_freq_counter = new WordFrequencyManager();
        $this->storage_manager->dispatch(array('init', $path));
        $this->stop_word_manager->dispatch(array('init'));
    }
    private function _run(){
        foreach($this->storage_manager->dispatch(array('words')) as $word){
            if(!$this->stop_word_manager->dispatch(array('is_stop_word', $word)))
                $this->word_freq_counter->dispatch(array('increment_count', $word));
        }
        $word_freqs = $this->word_freq_counter->dispatch(array('sorted'));
        $count  = 0;
        foreach($word_freqs as $key => $val){
            print $key.' - '.$val."\n";
            if(++$count >= 25) break;
        }
    }
}

$word_frequency_controller = new WordFrequencyController();
$word_frequency_controller->dispatch(array('init', $argv[1]));
$word_frequency_controller->dispatch(array('run'));

/*
 * info test
 */
$tf = new TFExercise();
$data_storage_manager = new DataStorageManager();
$stop_word_manager = new StopWordManager();
$word_frequency_manager = new WordFrequencyManager();
$word_frequency_controller = new WordFrequencyController();
print($data_storage_manager->dispatch(array('info', $tf)) . "\n");
print($stop_word_manager->dispatch(array('info', $tf)) . "\n");
print($word_frequency_manager->dispatch(array('info', $tf)) . "\n");
print($word_frequency_controller->dispatch(array('info', $tf)) . "\n");