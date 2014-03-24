<?php
/**
 * User: Jiahan
 * Date: 1/29/14
 * Time: 6:35 PM
 */

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
        foreach($this->load_event_handlers as $h){
            $h($path);
        }
        foreach($this->dowork_event_handlers as $h){
            $h();
        }
        foreach($this->end_event_handlers as $h)
            $h();
    }
}
class DataStorage{
    public $data = array();
    public $stop_word_filter = null;
    public $word_event_handler = array();
    // Closure in an object in PHP can be only declared as a property
    public $load;
    public $produce_words;
    function __construct($wfapp, $stop_word_filter){
        // PHP 5.3 doesn't support $this in closure, but we can use it in this way.
        $self = $this;
        // load closure
        $this->load = function ($path) use($self){
            $self->data = preg_split('/\s+/',trim(strtolower(preg_replace('/[\W_]+/', ' ', file_get_contents($path)))));
        };
        // produce_words closure
        $this->produce_words = function() use($self){
            foreach($self->data as $word)
                if(!$self->stop_word_filter->is_stop_word($word)){
                    foreach($self->word_event_handler as $h){
                        $h($word);
                    }
                }
        };
        $this->stop_word_filter = $stop_word_filter;
        $wfapp->register_for_load_event($this->load);
        $wfapp->register_for_dowork_event($this->produce_words);
    }

    public function register_for_word_event($handler){
        array_push($this->word_event_handler, $handler);
    }
}

class StopWordFilter{
    public $stop_words = array();
    public $load;
    function __construct($wfapp){
        $self = $this;
        // load closure
        $this->load = function ($ignore)use($self){
            $self->stop_words = array_merge(preg_split('/,/', file_get_contents('stop_words.txt')), range('a', 'z'));
        };
        $wfapp->register_for_load_event($this->load);
    }

    public function is_stop_word($word){
        return in_array($word, $this->stop_words);
    }
}
class WordFrequencyCounter{
    public $word_freqs = array();
    public $increment_count;
    public $print_freqs;
    function __construct($wfapp, $data_storage){
        $self = $this;
        // increment_count closure
        $this->increment_count = function($word) use($self){
            if(array_key_exists($word, $self->word_freqs))
                $self->word_freqs[$word] ++;
            else
                $self->word_freqs[$word] = 1;
        };
        // print_freqs closure
        $this->print_freqs = function () use($self){
            $count = 0;
            arsort($self->word_freqs);
            foreach($self->word_freqs as $key => $val){
                print $key.' - '.$val."\n";
                if(++$count >= 25) break;
            }
        };
        $data_storage->register_for_word_event($this->increment_count);
        $wfapp->register_for_end_event($this->print_freqs);
    }
}

class WordsWithZ{
    public $data_storage;
    public $stop_word_filter;
    public $print_words_z;
    function __construct($wfapp, $data_storage, $stop_word_filter){
        $this->data_storage = $data_storage;
        $this->stop_word_filter = $stop_word_filter;
        $self = $this;
        // print_words_z closure
        $this->print_words_z = function() use($self){
            $count = 0;
            foreach($self->data_storage->data as $word)
                if(!$self->stop_word_filter->is_stop_word($word) && preg_match('/.*z+.*/', $word)){
                    $count ++;
                }
            print('Total number of words with z: '. $count);
        };
        $wfapp->register_for_end_event($this->print_words_z);
    }
}

$wfapp = new WordFrequencyFramework();
$stop_word_filter = new StopWordFilter($wfapp);
$data_storage = new DataStorage($wfapp, $stop_word_filter);
$word_freq_counter = new WordFrequencyCounter($wfapp, $data_storage);
$words_with_z = new WordsWithZ($wfapp, $data_storage, $stop_word_filter);
$wfapp->run($argv[1]);