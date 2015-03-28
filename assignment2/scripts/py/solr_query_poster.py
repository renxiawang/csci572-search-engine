from urllib2 import *
import json

from solr_queries import questions

for question in questions:
    print 'Question: ', question['question']

    for query in question['queries']:
        print '\n================================================='
        print 'Query: ', query['query']
        print '================================================='

        content_based_query = query['content_based']
        link_based_query = query['link_based']

        print '-------------------------------------------------'
        print 'Content-based Query Results:'
        conn = urlopen(content_based_query)
        rsp = json.loads(conn.read())

        print "number of matches=", rsp['response']['numFound']
        print 'score\tpagerank\ttitle\tkeywords'
        for doc in rsp['response']['docs']:
            print '%s\t%s\t%s\t%s' % (doc.get('score'), doc.get('pagerank'), doc.get('title'), doc.get('keywords'))

        print '-------------------------------------------------'
        print 'Link-based Query Results:'
        conn = urlopen(link_based_query)
        rsp = json.loads(conn.read())

        print "number of matches=", rsp['response']['numFound']
        print 'score\tpagerank\ttitle\tkeywords'
        for doc in rsp['response']['docs']:
            print '%s\t%s\t%s\t%s' % (doc.get('score'), doc.get('pagerank'), doc.get('title'), doc.get('keywords'))

