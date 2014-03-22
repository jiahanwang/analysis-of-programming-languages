/**
 * User: Jiahan
 * Date: 1/24/14
 * Time: 7:51 PM
 */

function read_file(path, func){
    // Use Node.js File system to read file
    func(require('fs').readFileSync(path, 'utf-8',
        function(err, words_string){
    	return err ? null: words_string;
        }), normalize);
}

function filter_chars(words_string, func){
    func(words_string.replace(/[\W_]+/g, ' '), scan);
}

function normalize(words_string, func){
	console.log(words_string);
    func(words_string.toLowerCase(), remove_stop_words);
}

function scan(words_string, func){
    func(words_string.trim().split(/\s+/), frequencies);
}

function remove_stop_words(words, func){
    var stop_words =  require('fs').readFileSync('../stop_words.txt', 'utf-8',
        function(err, words_string){
            return err ? null: words_string;
        }).split(/,/);
    for (var i = 97; i < 123; i++)
        stop_words.push(String.fromCharCode(i));
    func(words.filter(
        function (word){
            if(stop_words.indexOf(word) ==  -1)
                return word;
        }), freq_sort);
}

function frequencies(words, func){
    var word_freqs = {};
    var number_of_words = words.length;
    for(var i = 0; i < number_of_words;i++)
        words[i] in word_freqs ? word_freqs[words[i]]++ : word_freqs[words[i]] = 1;
    func(word_freqs, no_op);
}

function freq_sort(word_freqs, func){
    var word_freqs_array = [];
    for(var word in word_freqs)
        word_freqs_array.push([word, word_freqs[word]]);
    word_freqs_array.sort(
        function(a, b){
            return a[1] == b[1] ? b[0] <= a[0] : b[1] - a[1];
        });
    word_freqs_global = func(word_freqs_array, null);
}

function no_op(wf, func){
    return wf;
}

var word_freqs_global;
// Use Node.js Process to get runtime variables
read_file(process.argv[2], filter_chars);
for(var i = 0; i < 25; i++){
    if(!word_freqs_global[i]) break;
    console.log(word_freqs_global[i][0] + ' - '+ word_freqs_global[i][1]);
}
