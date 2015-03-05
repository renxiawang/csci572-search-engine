package org.apache.nutch.urlfilter.neardup;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by renxia on 2/26/15.
 */
public class ParsedMetaReaderCallable implements Callable {

    private static final Logger LOG = LoggerFactory
            .getLogger(ParsedMetaReaderCallable.class);

    private static final String PARSE_DATA_KEY = "pd";
    private static final Pattern p = Pattern.compile("Parse Metadata: (.*)");

    private SegmentReader reader;
    private Path path;
    private String url;

    public ParsedMetaReaderCallable(SegmentReader reader, Path path, String url) {
        this.reader = reader;
        this.path = path;
        this.url = url;
    }

    @Override
    public String call() throws Exception {
        String metaData = null;

        Map<String, List<Writable>> results = Maps.newHashMap();
        try {
            reader.get(path, new Text(url),
                    new OutputStreamWriter(System.out, "UTF-8"),
                    results);

            if (results.containsKey(PARSE_DATA_KEY)) {
                List<Writable> res = results.get(PARSE_DATA_KEY);
                for (int i = 0; i < res.size(); i++) {
                    metaData = getParseMeta(res.get(i).toString());
                }
            }
        } catch (Exception e) {
            LOG.error("Unable to get data of " + url + " from " + path, e);
        }
        return metaData;
    }

    private String getParseMeta(String parseData) {
        Matcher m = p.matcher(parseData);
        StringBuilder stringBuilder = new StringBuilder();

        while (m.find()) {
            stringBuilder.append(m.group());
        }

        return stringBuilder.toString();
    }
}
