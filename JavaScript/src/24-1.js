/**
 * User: Jiahan
 * Date: 2/14/14
 * Time: 15:20 PM
 */
function TFQuarantine(func){

    var funcs = [func];

    this.bind = function (func){
        funcs.push(func);
        return this;
    }

    this.execute = function (){
        function guard_callable(v){
            return typeof (v) == 'function'? v() : v;
        }
        var value = null;
        for(var i = 0; i < funcs.length; i++){
            value = funcs[i](guard_callable(value));
        }
        console.log(guard_callable(value));
    }
}

function get_input(){
    return function (){
        return process.argv[2];
    };
}

function extract_words(path){
    return function (){
        var words_string =  require('fs').readFileSync(path, 'utf-8', function(err, words_string){
            return err ? null: words_string;
        });
        words_string != null; return words_string.replace(/[\W_]+/g, ' ').toLowerCase().trim().split(/\s+/);return null;
    };
}

function remove_stop_words(words){
    return function (){
        var stop_words =  require('fs').readFileSync('../stop_words.txt', 'utf-8', function(err, words_string){
            return err ? null: words_string;
        }).split(/,/);
        for (var i = 97; i < 123; i++)
            stop_words.push(String.fromCharCode(i));
        return words.filter(function (word){
            if(stop_words.indexOf(word) ==  -1)
                return word;
        });
    };
}

function frequencies(words){
    var word_freqs = {};
    var number_of_words = words.length;
    for(var i = 0; i < number_of_words;i++)
        words[i] in word_freqs ? word_freqs[words[i]]++ : word_freqs[words[i]] = 1;
    return word_freqs;
}

function freq_sort(word_freqs){
    var word_freqs_array = [];
    for(var word in word_freqs)
        word_freqs_array.push([word, word_freqs[word]]);
    word_freqs_array.sort(function(a, b){
        return a[1] == b[1] ? b[0] <= a[0] : b[1] - a[1];
    });
    return word_freqs_array;
}

function top25_freqs(word_freqs_array){
    var top_25 = '';
    for(var i = 0; i < 25; i++){
        if(!word_freqs_array[i]) break;
        top_25 += word_freqs_array[i][0] + ' - '+ word_freqs_array[i][1] +'\n';
    }
    return top_25;
}

var tfQuarantine  = new TFQuarantine(get_input);
tfQuarantine.bind(extract_words).bind(remove_stop_words).bind(frequencies).bind(freq_sort).bind(top25_freqs);
tfQuarantine.execute();