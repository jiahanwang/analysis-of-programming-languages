<?php
/**
 * User: Jiahan
 * Date: 2/1/14
 * Time: 6:30 PM
 * Notes: In this program, the framework provides interfaces so that other classes can implement
 *        these interfaces and be called by the framework.
 */
// Interfaces provided by the framework. Any class wanting to be called by the framework should implement these interfaces
interface LoadEvent{
    public function load($property);
}

interface DoWorkEvent{
    public function do_work();
}

interface EndWorkEvent{
    public function end_work();
}
//Controller of the Framework
class WordFrequencyFramework{
    private $load_event_handlers = array();
    private $dowork_event_handlers = array();
    private $end_event_handlers = array();
    public function register_for_load_event($handler){
        array_push($this->load_event_handlers, $handler);
    }
    public function register_for_dowork_event($handler){
        array_push($this->dowork_event_handlers, $handler);
    }
    public function register_for_end_event($handler){
        array_push($this->end_event_handlers, $handler);
    }
    public function run($path){
        foreach($this->load_event_handlers as $h)
            $h->load($path);
        foreach($this->dowork_event_handlers as $h)
            $h->do_work();
        foreach($this->end_event_handlers as $h)
            $h->end_work();
    }
}

// Interface provided by DataStorage. Any class wanting to be called by DataStorage should implement this interface
interface WordEvent{
    public function process_word($word);
}
class DataStorage implements LoadEvent, DoWorkEvent{
    public $data = array();
    private $stop_word_filter = null;
    private $word_event_handler = array();
    function __construct($wfapp, $stop_word_filter){
        $this->stop_word_filter = $stop_word_filter;
        $wfapp->register_for_load_event($this);
        $wfapp->register_for_dowork_event($this);
    }
    //Implements load
    public function load($path){
        $this->data = preg_split('/\s+/',trim(strtolower(preg_replace('/[\W_]+/', ' ', file_get_contents($path)))));
    }
    //Implements do_work
    public function do_work(){
        foreach($this->data as $word)
            if(!$this->stop_word_filter->is_stop_word($word)){
                foreach($this->word_event_handler as $h){
                    $h->process_word($word);
                }
            }
    }
    public function register_for_word_event($handler){
        array_push($this->word_event_handler, $handler);
    }
}

class StopWordFilter implements LoadEvent{
    public $stop_words = array();
    function __construct($wfapp){
        $wfapp->register_for_load_event($this);
    }
    //Implements load
    public function load($ignore){
        $this->stop_words = array_merge(preg_split('/,/', file_get_contents('stop_words.txt')), range('a', 'z'));
    }

    public function is_stop_word($word){
        return in_array($word, $this->stop_words);
    }
}

class WordFrequencyCounter implements WordEvent, EndWorkEvent{
    public $word_freqs = array();
    function __construct($wfapp, $data_storage){
        $data_storage->register_for_word_event($this);
        $wfapp->register_for_end_event($this);
    }
    //Implements process_word
    public function process_word($word){
            if(array_key_exists($word, $this->word_freqs))
                $this->word_freqs[$word] ++;
            else
                $this->word_freqs[$word] = 1;
    }
    //Implements end_work
    public function end_work(){
        $count = 0;
        arsort($this->word_freqs);
        foreach($this->word_freqs as $key => $val){
            print $key.' - '.$val."\n";
            if(++$count >= 25) break;
        }
    }
}
$wfapp = new WordFrequencyFramework();
$stop_word_filter = new StopWordFilter($wfapp);
$data_storage = new DataStorage($wfapp, $stop_word_filter);
$word_freq_counter = new WordFrequencyCounter($wfapp, $data_storage);
$wfapp->run($argv[1]);