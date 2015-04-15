import subprocess

geoinfo_file = open('geoinfo.csv', 'r')

i = 0
for line in geoinfo_file.xreadlines():
    values = line.split('\t')

    filename = values[0].strip()
    locationName = values[1].strip()
    lat = values[2].strip()
    lon = values[3].strip()

    update_doc = '[{"id":"%s", "locationName":{"set":"%s"}, "locationLatLon":{"set":"%s,%s"}}]' % (filename, locationName, lat, lon)

    subprocess.call(['curl', 'http://localhost:8983/solr/solrCell/update', '-H', 'Content-type:application/json', '-d', update_doc])

    i += 1
    print 'finished: ', i