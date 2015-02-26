package org.apache.nutch.urlfilter.neardup;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.nutch.net.URLFilter;
import org.apache.nutch.segment.SegmentReader;
import org.apache.nutch.util.HadoopFSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final String PARSE_DATA_KEY = "pd";

    private static final Pattern p = Pattern.compile("Parse Metadata: (.*)");

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

            for (Path path : segmentPaths) {
                // if not parse_text under current segment dir
                // skip
                if (!fs.exists(new Path(path, PARSE_DATA))) {
                    continue;
                }

                Map<String, List<Writable>> results = Maps.newHashMap();
                segmentReader.get(path, new Text(urlString),
                        new OutputStreamWriter(System.out, "UTF-8"),
                        results);

                if (results.containsKey(PARSE_DATA_KEY)) {
                    List<Writable> res = results.get(PARSE_DATA_KEY);
                    boolean isParseDataEmpty = true;
                    for (int i = 0; i < res.size(); i++) {
                        isParseDataEmpty = false;
                        metaData = getParseMeta(res.get(i).toString());
                        // LOG.info("Retrieved meta data: " + metaData);
                    }

                    if (!isParseDataEmpty) {
                        break;
                    }
                }
            }

            // filter out the duplicate url
            if (simHashMap.isDuplicate(metaData)) {
                duplicateCounter++;
                LOG.info("Near duplicate count: " + duplicateCounter);
                return null;
            }

        } catch (Exception e) {
            LOG.error("Unable to get data of " + urlString + " from parse_data", e);
        }

        return urlString;
    }

    private String getParseMeta(String parseData) {
        Matcher m = p.matcher (parseData);
        StringBuilder stringBuilder = new StringBuilder();

        while (m.find()) {
            stringBuilder.append(m.group());
        }

        return stringBuilder.toString();
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
