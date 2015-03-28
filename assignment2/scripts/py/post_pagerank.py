import os
import subprocess

pagerank_file = open('pageranks.txt', 'r')
pagerank_map = {}
for line in pagerank_file.xreadlines():
    key_value = line.split('\t')
    pagerank_map[key_value[0].strip()] = float(key_value[1].strip())

i = 0
for root, dirnames, filenames in os.walk('all'):
    for filename in filenames:
        filepath = os.path.join(root, filename)

        pagerank = pagerank_map.get(filename) if pagerank_map.has_key(filename) else 0.0
        print 'Processing: %s Pagerank: %f' % (filename, pagerank)
        subprocess.call(['curl', 'http://localhost:8983/solr/solrCell/update', '-H', 'Content-type:application/json', '-d', '[{"id":"%s", "pagerank":{"set":%f}}]' % (filename, pagerank)])

        i += 1
        print 'finished: ', i