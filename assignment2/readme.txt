conf
├── nutch-to-solr                         <----------- configuration files for indexing data using bin/nutch solrindex
│   ├── nutch                             <----------- for Nutch
│   │   ├── schema.xml
│   │   └── solrindex-mapping.xml
│   └── solr                              <----------- for Solr
│       ├── schema.xml
│       ├── solrconfig.xml
│       └── stopwords.txt
└── solrCell                              <----------- configuration files for indexing data with SorlCell
    ├── build-confs                       <----------- for building Solr with required libraries
    │   ├── extraction
    │   │   └── ivy.xml
    │   └── versions
    │       └── ivy-versions.properties
    ├── schema.xml
    ├── solrconfig.xml
    └── stopwords.txt

scripts
├── pig
│   ├── build_graph_performance.txt       <----------- Performance record of graph building job
│   ├── build_graph.pig                   <----------- Pig script for building graph
│   ├── extract_filename_and_rank.pig     <----------- Pig script for extracting the file and its pagerank, stores in tab delimited file
│   ├── inverted_index_performance.txt    <----------- Performance record of inverted index job
│   ├── inverted_index.pig                <----------- Pig script for inverting index
│   ├── mergebag.py                       <----------- Pig UDF in Python to generate linked files of each file
│   ├── pagerank_performance.txt          <----------- Performance record of pagerank job
│   ├── pagerank.py                       <----------- Pig script for computing pagerank (ref: https://techblug.wordpress.com/2011/07/29/pagerank-implementation-in-pig/)
│   ├── pageranks.txt                     <----------- Pagerank of files
│   └── pig_util.py                       <----------- Python Util for Pig UDF (ref: https://svn.apache.org/repos/asf/pig/trunk/src/python/streaming/pig_util.py)
└── py
    ├── extract_metadata_for_building_graph.py      <----------- script for extracting metadata of files for building graph purpose
    ├── extract_metadata_from_dump.py               <----------- script to extract metadata from dump file
    ├── extract_metadata.py                         <----------- script to extract metadata from Nutch sequence files
    ├── how_to_use_tika_with_python.txt             <----------- an tutorial about how to use tika in Python
    ├── keywords.py                                 <----------- keywords used to inverted index
    ├── links.py                                    <----------- sample links for analysis purpose
    ├── post_all_data.py                            <----------- script to post all dump data to SolrCell
    ├── post_pagerank.py                            <----------- script to update pagerank value for files in Solr
    ├── query_result.txt                            <----------- query results
    ├── solr_queries.py                             <----------- queries
    └── solr_query_poster.py                        <----------- script to post query to solr