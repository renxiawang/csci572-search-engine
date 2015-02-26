package org.apache.nutch.urlfilter.exactdup;

import com.google.common.collect.Maps;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.nutch.segment.SegmentReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by renxia on 2/26/15.
 */
public class ParsedTextReaderCallable implements Callable {

    private static final Logger LOG = LoggerFactory
            .getLogger(ParsedTextReaderCallable.class);

    private static final String PARSE_TEXT_KEY = "pt";

    private SegmentReader reader;
    private Path path;
    private String url;

    public ParsedTextReaderCallable(SegmentReader reader, Path path, String url) {
        this.reader = reader;
        this.path = path;
        this.url = url;
    }

    @Override
    public String call() throws Exception {
        String parseText = null;

        Map<String, List<Writable>> results = Maps.newHashMap();
        try {
            reader.get(path, new Text(url),
                    new OutputStreamWriter(System.out, "UTF-8"),
                    results);

            if (results.containsKey(PARSE_TEXT_KEY)) {
                List<Writable> res = results.get(PARSE_TEXT_KEY);
                for (int i = 0; i < res.size(); i++) {
                    parseText = res.get(i).toString();
                }
            }
        } catch (Exception e) {
            LOG.error("Unable to get data of " + url + " from " + path, e);
        }
        return parseText;
    }
}
