import os
import csv

os.environ['CLASSPATH'] = "/home/renxia/apache/nutch/runtime/local/tika-app-1.8-SNAPSHOT.jar"

from jnius import autoclass

fields = ["Content-Type",
          "title",
          "Profile Date/Time",
          "meta:save-date",
          "meta:creation-date",
          "Last-Save-Date",
          "keywords",
          "Image Description",
          "Exposure Time",
          "exif:ExposureTime",
          "exif:DateTimeOriginal",
          "Device Model Description",
          "Device Mfg Description",
          "description",
          "DC.title",
          "dc:title",
          "DC.publisher.address",
          "DC.publisher",
          "DC.description",
          "dc:description",
          "DC.date.reviewed",
          "DC.date.created",
          "DC.creator",
          "Date/Time Original",
          "Date/Time Digitized",
          "Date/Time",
          "date",
          "Creation-Date",
          "xmp:CreatorTool",
          "Total-Time",
          "tIME",
          "Sub-Sec Time Original",
          "Sub-Sec Time Digitized",
          "Sub-Sec Time",
          "publisher",
          "meta:print-date",
          "meta:last-author",
          "meta:keyword",
          "meta:author",
          "Last-Author",
          "Keywords",
          "GPS Version ID",
          "GPS Track Ref",
          "GPS Track",
          "GPS Time-Stamp",
          "GPS Status",
          "GPS Speed Ref",
          "GPS Speed",
          "GPS Measure Mode",
          "GPS Map Datum",
          "GPS Longitude Ref",
          "GPS Longitude",
          "GPS Latitude Ref",
          "GPS Latitude",
          "GPS Differential",
          "GPS Date Stamp",
          "GPS Altitude Ref",
          "GPS Altitude",
          "Global Altitude",
          "extended-properties:TotalTime",
          "Edit-Time",
          "Document ImageModificationTime",
          "DESCRIPTION",
          "dc:publisher",
          "dc:creator",
          "creator",
          "Author",
          "author",
          "ajs-app-title",
          "KEYWORDS",
          "Description",
          "Time Created",
          "NumberOfLongitudesInGrid",
          "NumberOfLatitudesInGrid",
          "Date Created",
          "Title-1",
          "Target Exposure Time",
          "Self Timer Delay",
          "Self Timer 2",
          "og:title",
          "keyword",
          "_FV_Longitude",
          "_FV_Latitude",
          "Description-3",
          "Description-2",
          "Description-1",
          "DescendingGridStartTimeUTC",
          "DescendingGridEndTimeUTC",
          "AscendingGridStartTimeUTC",
          "AscendingGridEndTimeUTC",
          "Windows XP Title",
          "StartTime",
          "RangeEndingTime",
          "RangeBeginningTime",
          "ProductionTime",
          "PGE_StartTime",
          "PGE_EndTime",
          "GranuleEndingDateTime",
          "GranuleBeginningDateTime",
          "EndingTime",
          "GRIB_VALID_TIME",
          "GRIB_REF_TIME",
          "KeyWords",
          "Date Stamp Mode",
          "AUTHOR"]

## Import the Java classes we are going to need
Tika = autoclass('org.apache.tika.Tika')
Metadata = autoclass('org.apache.tika.metadata.Metadata')
FileInputStream = autoclass('java.io.FileInputStream')

tika = Tika()

result = open('sample_metadata.csv', 'w')
csv_writer = csv.writer(result, delimiter = "\t", lineterminator='\n',
                        quotechar="\"", quoting=csv.QUOTE_MINIMAL)

csv_writer.writerow(fields)

i = 0
for root, dirnames, filenames in os.walk('all'):
    for filename in filenames:
        filepath = os.path.join(root, filename)

        meta = Metadata()
        tika.parseToString(FileInputStream(filepath), meta)
        values = []
        for field in fields:
            value = meta.get(field) if meta.get(field) else ''
            values.append(value)

        csv_writer.writerow(values)
        i += 1

    if i == 10000:
        break




