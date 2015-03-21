register 'mergebag.py' using jython as myfuncs;

SET mapreduce.task.io.sort.mb 1000
SET mapreduce.map.memory.mb 2816
SET mapreduce.reduce.memory.mb 2816
SET mapreduce.job.queuename search

%default JAVA_XMX 2816m
SET mapreduce.map.java.opts -Xmx$JAVA_XMX
SET mapreduce.reduce.java.opts -Xmx$JAVA_XMX

SET mapred.compress.map.output true
SET mapred.map.output.compression.codec org.apache.hadoop.io.compress.SnappyCodec

-- load data
inverted_index = LOAD '/user/rwang/inverted_index_parts/*' USING PigStorage() AS (token:chararray, files: { file : ( filename:chararray )});

flattened = FOREACH inverted_index GENERATE 
    FLATTEN(files) as filename:chararray, 
    files;

grouped = GROUP flattened BY filename;
result = FOREACH grouped GENERATE group as from_url, 1 as pagerank, myfuncs.merge_bag(group, flattened.files) as to_files;

STORE result INTO '/user/rwang/pagerank_input_2';
