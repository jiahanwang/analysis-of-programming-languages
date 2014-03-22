/**
 * User: Jiahan
 * Date: 1/25/14
 * Time: 5:43 PM
 */

function TFTheOne(v){

    var value = v;

    this.bind  = function (func){
        value = func(value);
        return this;
    };
    this.print_me = function (){
        console.log(value);
    };
}

function read_file(path){
    return require('fs').readFileSync(path, 'utf-8', function(err, words_string){
        return err ? null: words_string;
    });
}

function filter_chars(words_string){
    return words_string.replace(/[\W_]+/g, ' ');
}

function normalize(words_string){
    return words_string.toLowerCase();
}

function scan(words_string){
    return words_string.trim().split(/\s+/);
}

function remove_stop_words(words){
    var stop_words =  require('fs').readFileSync('stop_words.txt', 'utf-8', function(err, words_string){
        return err ? null: words_string;
    }).split(/,/);
    for (var i = 97; i < 123; i++)
        stop_words.push(String.fromCharCode(i));
    return words.filter(function (word){
        if(stop_words.indexOf(word) ==  -1)
            return word;
    });
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

var tfTheOne  = new TFTheOne(process.argv[2]);
tfTheOne.bind(read_file).bind(filter_chars).bind(normalize).bind(scan)
        .bind(remove_stop_words).bind(frequencies).bind(freq_sort).bind(top25_freqs).print_me();
