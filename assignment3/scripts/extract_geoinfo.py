# -*- coding: utf-8 -*-
import os
import csv

from nltk.tokenize import RegexpTokenizer
from nltk.corpus import stopwords

os.environ['CLASSPATH'] = "/home/renxia/tools/CLAVIN/target/clavin-2.0.0-jar-with-dependencies.jar:/home/renxia/tika/tika-core/target/tika-core-1.8-SNAPSHOT.jar:/home/renxia/tika/tika-app/target/tika-app-1.8-SNAPSHOT.jar:/home/renxia/tika/tika-parsers/target/tika-parsers-1.8-SNAPSHOT.jar"

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

# tika
Tika = autoclass('org.apache.tika.Tika')
Metadata = autoclass('org.apache.tika.metadata.Metadata')
FileInputStream = autoclass('java.io.FileInputStream')

## init nltk
tika = Tika()
tokenizer = RegexpTokenizer(r'\b[a-zA-Z]{2,}\b')
stopwords_en = stopwords.words('english')

#  CLAVIN
parseFactory = autoclass("com.bericotech.clavin.GeoParserFactory")
parse = parseFactory.getDefault("/home/renxia/tools/CLAVIN/IndexDirectory")

# result file
result = open('geoinfo.csv', 'w')
csv_writer = csv.writer(result, delimiter = "\t", lineterminator='\n',
                        quotechar="\"", quoting=csv.QUOTE_MINIMAL)

numOfProcessed = 0
for root, dirnames, filenames in os.walk('all'):
    for filename in filenames:
        filepath = os.path.join(root, filename)

        meta = Metadata()
        text = None
        try:
            print 'Number of file processed: ', numOfProcessed
            numOfProcessed += 1
            text = tika.parseToString(FileInputStream(filepath), meta)
        except:
            print 'failed: ', filepath

        values = []
        for field in fields:
            value = meta.get(field) if meta.get(field) else ''
            values.append(value)

        if text:
            values.append(text)

        ## combine meta vlues and text
        meta_values = ','.join(values)

        ## extract all location info
        resolvedLocations = parse.parse(meta_values.decode('utf-8', 'ignore'))
        iterator = resolvedLocations.iterator()
        while iterator.hasNext():
            loc = iterator.next()
            geoname = loc.getGeoname()

            if geoname.getPreferredName() == 'Earth':
                continue

            csv_writer.writerow([filename, geoname.getPreferredName(), geoname.getLatitude(), geoname.getLongitude()])
            break