package org.apache.nutch.urlfilter.neardup;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.nutch.net.URLFilter;
import org.apache.nutch.segment.SegmentReader;
import org.apache.nutch.util.HadoopFSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by renxia on 2/21/15.
 */
public class NearDupURLFilter implements URLFilter {

    private static final Logger LOG = LoggerFactory
            .getLogger(NearDupURLFilter.class);

    private static final String MAPRED_INPUT_DIR = "mapred.input.dir";
    private static final String CRAWLDB = "crawldb";
    private static final String CURRENT = "current";
    private static final String SEGMENTS = "segments";
    private static final String FILE_PROTOCOL = "file:";
    private static final String PARSE_DATA = "parse_data";

    private static SimHashMap simHashMap = new SimHashMap(64, 3);

    private Configuration conf;
    private FileSystem fs;
    private SegmentReader segmentReader;

    private String basePath;
    private List<Path> segmentPaths;


    private boolean initialized = false;
    private long duplicateCounter = 0;

    public NearDupURLFilter() {
    }

    public void init() {
        try {
            fs = FileSystem.get(conf);

            segmentPaths = Lists.newArrayList();

            // get segments paths
            String paths = conf.get(MAPRED_INPUT_DIR);
            generatePaths(paths);

            // init readers for parse_data
            segmentReader = new SegmentReader(conf, false, false, false, false, true, false);

            initialized = true;
        } catch (Exception e) {
            LOG.error("Initialization failed. ", e);
            return;
        }
    }

    public void generatePaths(String paths) {
        String[] pathAry = paths.split(",");

        basePath = pathAry[0].replace(FILE_PROTOCOL, "")
                .replace(String.format("%s/%s", CRAWLDB, CURRENT), "");

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

        LOG.info("segment paths " + Joiner.on(",").join(segmentPaths));
    }

    @Override
    public String filter(String urlString) {

        if (!initialized) {
            LOG.info("Initializing URlFilter, ID: " + System.identityHashCode(this));
            init();
        }

        try {
            LOG.info("Reading parse data and text for url: " + urlString);

            String metaData = null;

            // multi threading to speed up the searching
            ExecutorService pool = Executors.newFixedThreadPool(segmentPaths.size());
            Set<Future<String>> set = new HashSet<Future<String>>();

            for (Path path : segmentPaths) {
                // if not parse_text under current segment dir
                // skip
                if (!fs.exists(new Path(path, PARSE_DATA))) {
                    continue;
                }

                // concurrent run searching on all segments
                Callable<String> callable = new ParsedMetaReaderCallable(segmentReader, path, urlString);
                Future<String> future = pool.submit(callable);
                set.add(future);
            }

            for (Future<String> future : set) {
                String tmp = future.get();
                if (tmp != null) {
                    LOG.info("data retrieved, killing other searching...");
                    metaData = tmp.trim();
                    pool.shutdownNow(); // stop all other searching
                }
            }

            // filter out the duplicate url
            if (!Strings.isNullOrEmpty(metaData) && simHashMap.isDuplicate(metaData)) {
                duplicateCounter++;
                LOG.info("Near duplicate count: " + duplicateCounter);
                return null;
            }

        } catch (Exception e) {
            LOG.error("Unable to get data of " + urlString + " from parse_data", e);
        }

        return urlString;
    }

    @Override
    public Configuration getConf() {
        return conf;
    }

    @Override
    public void setConf(Configuration configuration) {
        this.conf = configuration;
    }
}
