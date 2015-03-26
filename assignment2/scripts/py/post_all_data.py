import os
import subprocess

os.environ['CLASSPATH'] = "/home/renxia/tika/tika-core/target/tika-core-1.8-SNAPSHOT.jar:/home/renxia/tika/tika-app/target/tika-app-1.8-SNAPSHOT.jar:/home/renxia/tika/tika-parsers/target/tika-parsers-1.8-SNAPSHOT.jar"

from jnius import autoclass

## Import the Java classes we are going to need
Tika = autoclass('org.apache.tika.Tika')
Metadata = autoclass('org.apache.tika.metadata.Metadata')
FileInputStream = autoclass('java.io.FileInputStream')

tika = Tika()

i = 0
for root, dirnames, filenames in os.walk('all'):
    for filename in filenames:
        filepath = os.path.join(root, filename)

        meta = Metadata()
        try:
            print 'processing: ', filepath
            tika.parseToString(FileInputStream(filepath), meta)
        except:
            print 'failed: ', filepath
            continue
        # print meta.names()
        subprocess.call(['curl', 'http://localhost:8983/solr/solrCell/update/extract?literal.id='+filename, '--data-binary', '@' + filepath, '-H', 'Content-type:'+meta.get('Content-Type')])

        i += 1
        print 'finished: ', i