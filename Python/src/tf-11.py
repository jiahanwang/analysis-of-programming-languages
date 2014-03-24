import sys, re, operator, string
from abc import ABCMeta

#
# The classes
#
class TFExercise():
    __metaclass__ = ABCMeta

    def info(self):
        return self.__class__.__name__

class DataStorageManager():
    """ Models the contents of the file """
    
    def __init__(self, path_to_file, tf):
        with open(path_to_file) as f:
            self._data = f.read()
        pattern = re.compile('[\W_]+')
        self._data = pattern.sub(' ', self._data).lower()
        self._tfexercise = tf

    def words(self):
        """ Returns the list words in storage """
        return self._data.split()
    
    def __getattr__(self, attr):
        # print('Trace:', attr)
        return  getattr(self._tfexercise, attr)
    
class StopWordManager():
    """ Models the stop word filter """
    
    def __init__(self, tf):
        with open('../stop_words.txt') as f:
            self._stop_words = f.read().split(',')
        # add single-letter words
        self._stop_words.extend(list(string.ascii_lowercase))
        self._tfexercise = tf

    def is_stop_word(self, word):
        return word in self._stop_words
    
    def __getattr__(self, attr):
        # print('Trace:', attr)
        return  getattr(self._tfexercise, attr)


class WordFrequencyManager():
    """ Keeps the word frequency data """
    
    def __init__(self, tf):
        self._word_freqs = {}
        self._tfexercise = tf

    def increment_count(self, word):
        if word in self._word_freqs:
            self._word_freqs[word] += 1
        else:
            self._word_freqs[word] = 1

    def sorted(self):
        return sorted(self._word_freqs.iteritems(), key=operator.itemgetter(1), reverse=True)
    
    def __getattr__(self, attr):
        # print('Trace:', attr)
        return  getattr(self._tfexercise, attr)

 
class WordFrequencyController():
    def __init__(self, path_to_file,tf):
        self._tfexercise = tf
        self._storage_manager = DataStorageManager(path_to_file, tf)
        self._stop_word_manager = StopWordManager(tf)
        self._word_freq_manager = WordFrequencyManager(tf)

    def run(self):
        for w in self._storage_manager.words():
            if not self._stop_word_manager.is_stop_word(w):
                self._word_freq_manager.increment_count(w)

        word_freqs = self._word_freq_manager.sorted()
        for (w, c) in word_freqs[0:25]:
            print w, ' - ', c

    def __getattr__(self, attr):
        # print('Trace:', attr)
        return  getattr(self._tfexercise, attr)

#
# The main function
#
#DataStorageManager(sys.argv[1], TFExercise()).info()
#StopWordManager(TFExercise()).info()
#WordFrequencyManager(TFExercise()).info()

WordFrequencyController(sys.argv[1], TFExercise()).run()
