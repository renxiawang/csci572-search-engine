# csci572-search-engine
Course project of CSCI 572

### Files
There are 5 dirs: ```failed_urls```, ```scripts```, ```stats```, ```conf``` and ```nutch```.

#### 100 Failed urls
Failed urls in the 1st crawl:

- failed_urls/1st_failed_urls.txt
Failed urls in the 2nd crawl:

- failed_urls/2nd_failed_urls.txt

#### MIME Type extract script
scripts/extract_mime_types.py

To use this script, copy and paste it under the your nutch runtime/local dir, then

```
python extract_mime_types.py -i <input_dir> -o <output_file_name>
```

```<input_dir>``` is the directory of your crawl data, which contains sub directories ```crawldb```, ```linkdb``` and ```segments```. E.g.

```
python extract_mime_types.py -i nasa -o nasa-mime
```

will extract the MIME types from the nasa dir.

#### MIME Type Lists
MIME types lists of NASA AMD:

- 1st crawl data: stats/nasa-1st-mime
- 2nd crawl data: stats/nasa-2nd-mime

MIME types lists of NSF ACADIS:

- 1st crawl data: stats/aoncadis-1st-mime
- 2nd crawl data from FTP sites: stats/aoncadis-2nd-ftp-mime
- 2nd crawl data from HTTP sites: stats/aoncadis-2nd-http-mime

MIME types lists of NSIDC ADE:

- 1st crawl data: stats/ade-1st-mime
- 2nd crawl data from FTP sites: stats/ade-2nd-ftp-mime
- 2nd crawl data from HTTP sites: stats/ade-2nd-http-mime

#### Crawl Stats
1st and 2nd crawl stats of NASA AMD:

- stats/AMD_stats.txt

1st and 2nd crawl stats of NSF ACADIS:

- stats/AONCADIS_stats.txt

1st and 2nd crawl stats of NSIDC ADE:

- stats/ADE_stats.txt


#### Nutch conf
Confs of NASA AMD:

- conf/NASA_AMD

Confs of NSF ACADIS:

- conf/NSF_ACADIS

Confs of NSIDC ADE:

- conf/NSIDC_ADE

#### Nutch URLFilter Plugins
The Nutch version used in the project is Nutch trunk, commit version: b477bcbc782cd2aa6759918bc278f6d97a2da21a

To use the plugin, make sure you are using the the same version with all the required plugin/tool installed and follow instructions:

1. copy nutch/ivy/ivy.xml to path/to/your/nutch/ivy, overwrite the ```ivy.xml```
2. copy nutch/build.xml to path/to/your/nutch, overwrite the ```build.xml```
3. copy nutch/default.properties to path/to/your/nutch, overwrite the ```default.properties```
4. copy nutch/src/plugin/urlfilter-exactdup to path/to/your/nutch/src/plugin
5. copy nutch/src/plugin/urlfilter-neardup to path/to/your/nutch/src/plugin
6. copy nutch/src/plugin/build.xml to path/to/your/nutch/src/plugin, overwrite the ```build.xml```

Then in your path/to/your/nutch, execute:

```
ant clean jar
ant runtime
```

If you want to use the plugin with your own Nutch version, do steps **4** and **5** (while are source codes), then configure by yourself (recommended as files in Nutch trunk may changes).

### Simulate Deduplication Process
In path/to/your/nutch/runtime/local, use the my submitted conf files and run:

```
bin/nutch updatedb <input_dir>/crawldb -dir <input_dir>/segments -filter
```

to start using URLfilters to update db.

To get the number of duplicate urls detected so far, execute:

```
grep duplicate logs/hadoop.log
```

The logs of duplicate detection will printed:

    2015-02-27 09:55:43,778 INFO  exactdup.ExactDupURLFilter - Exact duplicate count: 2175
    2015-02-27 09:55:48,781 INFO  exactdup.ExactDupURLFilter - Exact duplicate count: 2176
    2015-02-27 09:55:53,794 INFO  exactdup.ExactDupURLFilter - Exact duplicate count: 2177
    2015-02-27 09:56:03,802 INFO  neardup.NearDupURLFilter - Near duplicate count: 1278
    2015-02-27 09:56:13,815 INFO  neardup.NearDupURLFilter - Near duplicate count: 1279
    2015-02-27 09:56:18,821 INFO  exactdup.ExactDupURLFilter - Exact duplicate count: 2178
    2015-02-27 09:56:23,829 INFO  exactdup.ExactDupURLFilter - Exact duplicate count: 2179
    2015-02-27 09:56:38,885 INFO  exactdup.ExactDupURLFilter - Exact duplicate count: 2180
    2015-02-27 09:56:43,887 INFO  exactdup.ExactDupURLFilter - Exact duplicate count: 2181


So you can get the number of duplicates of each type, exact and near.

To get the number of processed urls so far, execute:

```
grep Total logs/hadoop.log
```

The logs of total processed urls will printed:

    2015-02-27 09:57:33,956 INFO  exactdup.ExactDupURLFilter - Total processed offer: 4861
    2015-02-27 09:57:39,030 INFO  neardup.NearDupURLFilter - Total processed offer: 2672
    2015-02-27 09:57:44,034 INFO  exactdup.ExactDupURLFilter - Total processed offer: 4862
    2015-02-27 09:57:49,038 INFO  neardup.NearDupURLFilter - Total processed offer: 2673
    2015-02-27 09:57:54,046 INFO  exactdup.ExactDupURLFilter - Total processed offer: 4863
    2015-02-27 09:57:59,057 INFO  neardup.NearDupURLFilter - Total processed offer: 2674
    2015-02-27 09:58:04,062 INFO  exactdup.ExactDupURLFilter - Total processed offer: 4864
    2015-02-27 09:58:04,698 INFO  exactdup.ExactDupURLFilter - Total processed offer: 4865

### Work Divide
- Renxia Wang: Configure Nucth, install tools and plugins, crawl, implement mime script, URLFilter, integrate deduplication algorithms and generate stats.
- Zhenni Huang: Implement deduplication algorithms.
- Shaoyi Li: Crawl, analysis failed url, report
