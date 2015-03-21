-- load data
files = LOAD 'file_meta.tsv' USING PigStorage() AS (filename:chararray, text:chararray);

filtered = FILTER files BY text is not null;

tokenized = FOREACH filtered GENERATE filename, FLATTEN(TOKENIZE(text)) AS token;
token_groups = GROUP tokenized BY token;
inverted_index = FOREACH token_groups GENERATE group AS token, tokenized.filename;

STORE inverted_index INTO 'inverted_index.tsv';
