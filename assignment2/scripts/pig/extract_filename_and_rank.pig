data = LOAD '/tmp/rwang/pagerank_data_5' USING PigStorage() AS (filename:chararray, pagerank:float, files: { file : ( filename:chararray )});

result = FOREACH data GENERATE filename, pagerank;

STORE result INTO '/tmp/rwang/rank_only';
