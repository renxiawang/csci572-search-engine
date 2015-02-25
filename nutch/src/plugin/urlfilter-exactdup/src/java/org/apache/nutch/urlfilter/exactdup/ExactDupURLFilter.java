package org.apache.nutch.urlfilter.exactdup;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.crawl.CrawlDbReader;
import org.apache.nutch.crawl.Inlinks;
import org.apache.nutch.crawl.LinkDbReader;
import org.apache.nutch.net.URLFilter;
import org.apache.nutch.segment.SegmentReader;
import org.apache.nutch.util.HadoopFSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by renxia on 2/21/15.
 */
public class ExactDupURLFilter implements URLFilter {

    private static final Logger LOG = LoggerFactory
            .getLogger(ExactDupURLFilter.class);

    private static final String MAPRED_INPUT_DIR = "mapred.input.dir";
    private static final String CRAWLDB = "crawldb";
    private static final String CURRENT = "current";
    private static final String LINKDB = "linkdb";
    private static final String SEGMENTS = "segments";
    private static final String CRAWL_FETCH = "crawl_fetch";
    private static final String CRAWL_PARSE = "crawl_parse";
    private static final String PARSE_DATA = "parse_data";
    private static final String PARSE_TEXT = "parse_text";
    private static final String FILE_PROTOCOL = "file:";

    private Configuration conf;
    private FileSystem fs;
    private LinkDbReader linkdbReader;
    private CrawlDbReader crawlDbReader;
    private SegmentReader segmentReader;

    private String basePath;
    private Path crawlDbPath;
    private Path linkDbPath;
    private List<Path> segmentPaths;

    private boolean initialized = false;
    private long duplicateCounter = 0;

    public ExactDupURLFilter() {}

    public void init() {
        try {
            fs = FileSystem.get(conf);

            segmentPaths = Lists.newArrayList();

            // get crawldb, linkdb, parse_data, parse_text paths
            String paths = conf.get(MAPRED_INPUT_DIR);
            generatePaths(paths);

            // init readers
            linkdbReader = new LinkDbReader(conf, linkDbPath);
            crawlDbReader = new CrawlDbReader();
            segmentReader = new SegmentReader(conf, false, false, false, false, true, true);

            initialized = true;
        } catch (Exception e) {
            LOG.error("Initialization failed. ", e);
            return;
        }
    }

    public void generatePaths(String paths) {
        String[] pathAry = paths.split(",");

        basePath = pathAry[0].replace(FILE_PROTOCOL, "").replace(String.format("%s/%s", CRAWLDB, CURRENT), "");

        crawlDbPath = new Path(basePath, CRAWLDB);
        linkDbPath = new Path(basePath, LINKDB);


        try {
            FileStatus[] fstats = new FileStatus[0];
            fstats = fs.listStatus(new Path(basePath, SEGMENTS),
                    HadoopFSUtil.getPassDirectoriesFilter(fs));

            Path[] files = HadoopFSUtil.getPaths(fstats);
            if (files != null && files.length > 0) {
                segmentPaths.addAll(Arrays.asList(files));
            }

        } catch (IOException e) {
            LOG.error("Unable to get segment paths", e);
        }

        LOG.info("crawldb path: " + crawlDbPath);
        LOG.info("linkdb path: " + linkDbPath);
        LOG.info("segment paths " + Joiner.on(",").join(segmentPaths));
    }

    @Override
    public String filter(String urlString) {

        if (!initialized) {
            LOG.info("Initializing URlFilter, ID: " + System.identityHashCode(this));
            init();
        }

        /*try {
            LOG.info("Reading anchors for url: " + urlString);
            Text url = new Text(urlString);
            String[] anchors = linkdbReader.getAnchors(url);
            for (String s : anchors) {
                LOG.info("anchors: " + s);
            }

            LOG.info("Reading inlinks for url: " + urlString);
            Inlinks inLinks = linkdbReader.getInlinks(url);
            LOG.info("Inlinks: " + inLinks.toString());
        } catch (IOException e) {
            LOG.error("Unable to get data of " + urlString + " from linkdb", e);
        }

        try {
            LOG.info("Reading crawldb data for url: " + urlString);
            CrawlDatum data = crawlDbReader.get(crawlDbPath.toString(), urlString, conf);
            LOG.info("Crawl data: " + data.toString());
        } catch (IOException e) {
            LOG.error("Unable to get data of " + urlString + " from crawldb", e);
        }*/

        try {
            LOG.info("Reading parse data and text for url: " + urlString);
            for (Path path : segmentPaths) {
                Map<String, List<Writable>> results = Maps.newHashMap();
                segmentReader.get(path, new Text(urlString),
                        new OutputStreamWriter(System.out, "UTF-8"),
                        results);

                if (results.containsKey("pd")) {
                    List<Writable> res = results.get("pd");
                    boolean isParseDataEmpty = true;
                    for (int i = 0; i < res.size(); i++) {
                        isParseDataEmpty = false;
                        LOG.info("===================================" + res.get(i));
                    }

                    if (!isParseDataEmpty) {
                        break;
                    }
                }
            }

        } catch (Exception e) {
            LOG.error("Unable to get data of " + urlString + " from parse_data", e);
        }

        return urlString;
    }

    @Override
    public void setConf(Configuration configuration) {
        this.conf = configuration;
    }

    @Override
    public Configuration getConf() {
        return conf;
    }
}
