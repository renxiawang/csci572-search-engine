import os
import csv
import re

import nltk
from nltk.tokenize import RegexpTokenizer
from nltk.corpus import stopwords

from keywords import keywords

os.environ['CLASSPATH'] = "/home/renxia/tika/tika-core/target/tika-core-1.8-SNAPSHOT.jar:/home/renxia/tika/tika-app/target/tika-app-1.8-SNAPSHOT.jar:/home/renxia/tika/tika-parsers/target/tika-parsers-1.8-SNAPSHOT.jar"

from jnius import autoclass

fields = ["title",
          "DC.title",
          "dc:title",
          "og:title",
          "Title-1",
          "description",
          "keyword",
          "keywords",
          "Keywords",
          "KeyWords",
          "meta:keyword",
          "KEYWORDS",
          "DC.description",
          "dc:description",
          "Description",
          "DESCRIPTION",
          "Description-3",
          "Description-2",
          "Description-1",]


## Import the Java classes we are going to need
Tika = autoclass('org.apache.tika.Tika')
Metadata = autoclass('org.apache.tika.metadata.Metadata')
FileInputStream = autoclass('java.io.FileInputStream')

## init tika
tika = Tika()

## init nltk
tokenizer = RegexpTokenizer(r'\b[a-zA-Z]{2,}\b')
stopwords_en = stopwords.words('english')

## result file
result = open('meta_for_graph.csv', 'w')
csv_writer = csv.writer(result, delimiter = "\t", lineterminator='\n',
                        quotechar="\"", quoting=csv.QUOTE_MINIMAL)

numOfProcessed = 0
for root, dirnames, filenames in os.walk('all'):
    for filename in filenames:
        filepath = os.path.join(root, filename)

        meta = Metadata()
        try:
            print 'Number of file processed: ', numOfProcessed
            numOfProcessed += 1
            tika.parseToString(FileInputStream(filepath), meta)
        except:
            print 'failed: ', filepath

        values = []
        for field in fields:
            value = meta.get(field) if meta.get(field) else ''
            values.append(value)

        ## tokenize and remove punc/stopwords
        meta_values = ','.join(values)

        meta_values = meta_values.lower()
        tokens = tokenizer.tokenize(meta_values)
        filtered_words = [w for w in tokens if not w in stopwords_en]

        # extract tokens exists in keywords
        filtered_words = set(filtered_words)
        filtered_keywords = keywords & filtered_words

        output_text = ' '.join(list(filtered_keywords))

        csv_writer.writerow([filename, output_text])
