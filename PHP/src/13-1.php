<?php
/**
 * User: Jiahan
 * Date: 1/28/14
 * Time: 7:26 PM
 */
#
# The abstract things
#
abstract class IDataStorage{
  abstract protected function words();
}

abstract class IStopWordFilter{
    abstract protected function is_stop_word($words);
}

abstract class IWordFrequencyCounter{
    abstract protected function increment_count($word);
    abstract  protected function sorted();
}
#
# The concrete things
#
class DataStorageManager extends IDataStorage{
    private $data = array();
    function __construct($path){
        $this->data = preg_split('/\s+/',trim(strtolower(preg_replace('/[\W_]+/', ' ', file_get_contents($path)))));
    }
    public function words(){
        return $this->data;
    }
}

class StopWordManager extends IStopWordFilter{
    private $stop_words = array();
    function __construct(){
        $this->stop_words = array_merge(preg_split('/,/', file_get_contents('stop_words.txt')), range('a', 'z'));
    }
    public function is_stop_word($word){
        return in_array($word, $this->stop_words);
    }
}

class WordFrequencyManager extends IWordFrequencyCounter{
    private $word_freqs = array();
    public function increment_count($word){
        if(array_key_exists($word, $this->word_freqs))
            $this->word_freqs[$word] ++;
        else$this->word_freqs[$word] = 1;
    }
    public function sorted(){
        arsort($this->word_freqs);

        return $this->word_freqs;
    }
}
#
# The application object
#
class WordFrequencyController{
    function __construct($path){
        $this->storage = new DataStorageManager($path);
        $this->stop_word_manager = new StopWordManager();
        $this->word_freq_counter = new WordFrequencyManager();
    }
    public function run(){
        foreach($this->storage->words() as $word){
            if(!$this->stop_word_manager->is_stop_word($word))
                $this->word_freq_counter->increment_count($word);
        }
        $word_freqs = $this->word_freq_counter->sorted();
        $count  = 0;
        foreach($word_freqs as $key => $val){
            print $key.' - '.$val."\n";
            if(++$count >= 25) break;
        }
    }
}
#
# The main function
#
$word_frequency_controller = new WordFrequencyController($argv[1]);
$word_frequency_controller->run();