#!/usr/bin/python
import os
import sys
import getopt
import glob
import re

import nutchpy

from links import *

# nutchpy sequence data reader
seq_reader = nutchpy.sequence_reader

# segments data path pattern
path_tail = '/segments/*/parse_data/part-00000/data'

# Content Type regex pattern
parsed_metadata_regex = 'Parse Metadata: (.*)'
parsed_metadata_pattern = re.compile(parsed_metadata_regex)
content_metadata_regex = 'Content Metadata: (.*)'
content_metadata_pattern = re.compile(content_metadata_regex)

def extract_mime_types(inputdir):
    if not os.path.exists(inputdir):
        print 'Input dir %s not exists, exiting...' % inputdir
        return

    seq_data_paths = glob.glob(inputdir + path_tail)

    for seq_data_path in seq_data_paths:
        read_seq_data(seq_data_path)


def read_seq_data(seq_data_path):
    items = seq_reader.read(seq_data_path)

    for item in items:
        url = item[0]

        if url in nasa:
            meta = item[1]

            print '============================================='
            print url
            print '------------------'

            matched_group = parsed_metadata_pattern.search(meta)
            if matched_group and len(matched_group.groups()) > 0:
                parsed_metadata = matched_group.groups()[0]
                print '------------------'
                print 'Parsed Metadata: '
                print parsed_metadata.encode('utf8')

            matched_group = content_metadata_pattern.search(meta)
            if matched_group and len(matched_group.groups()) > 0:
                content_metadata = matched_group.groups()[0]
                print '------------------'
                print 'Content Metadata: '
                print content_metadata.encode('utf8')

            print '============================================='

def main(argv):
    inputdir = ''
    try:
        opts, args = getopt.getopt(argv,"hi:",["input="])
    except getopt.GetoptError:
        print 'extract_metadata.py -i <inputdir>'
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print 'extract_metadata.py -i <inputdir>'
            sys.exit()
        elif opt in ("-i", "--input"):
            inputdir = arg
    print 'Input dir is: ', inputdir

    extract_mime_types(inputdir)

if __name__ == "__main__":
    main(sys.argv[1:])