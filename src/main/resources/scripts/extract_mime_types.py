#!/usr/bin/python
import os
import sys
import getopt
import glob
import re

import nutchpy

# nutchpy sequence data reader
seq_reader = nutchpy.sequence_reader

# segments data path pattern
path_tail = '/segments/*/crawl_fetch/part-00000/data'

# Content Type regex pattern
content_type_regex = 'Content-Type=(.*)'
content_type_pattern = re.compile(content_type_regex)

# mime types map
mime_map = {}

def extract_mime_types(inputdir, outputfile):
    if not os.path.exists(inputdir):
        print 'Input dir %s not exists, exiting...' % inputdir
        return

    if os.path.exists(outputfile):
        print 'Output file %s already exists, exiting...' % outputfile
        return

    seq_data_paths = glob.glob(inputdir + path_tail)

    for seq_data_path in seq_data_paths:
        read_seq_data(seq_data_path)

    write_to_file(outputfile)

def read_seq_data(seq_data_path):
    items = seq_reader.read(seq_data_path)

    for item in items:
        meta = item[1]
        matched_group = content_type_pattern.search(meta)

        if matched_group and len(matched_group.groups()) > 0:
            mime_type = matched_group.groups()[0]

            # add to map
            if mime_type in mime_map:
                mime_map[mime_type] += 1
            else:
                mime_map[mime_type] = 1

def write_to_file(outputfile):
    output = open(outputfile, 'w')
    for key, value in mime_map.items():
        output.write("%s\t%s\n" % (key, value))

    output.close()

def main(argv):
    inputdir = ''
    outputfile = ''
    try:
        opts, args = getopt.getopt(argv,"hi:o:",["input=","output="])
    except getopt.GetoptError:
        print 'extract_mime_types.py -i <inputdir> -o <outputfile>'
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print 'extract_mime_types.py -i <inputdir> -o <outputfile>'
            sys.exit()
        elif opt in ("-i", "--input"):
            inputdir = arg
        elif opt in ("-o", "--output"):
            outputfile = arg
    print 'Input dir is "', inputdir
    print 'Output file is "', outputfile

    extract_mime_types(inputdir, outputfile)

if __name__ == "__main__":
    main(sys.argv[1:])