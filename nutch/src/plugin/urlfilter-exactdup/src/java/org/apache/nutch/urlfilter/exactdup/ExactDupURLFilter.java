package org.apache.nutch.urlfilter.exactdup;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.crawl.CrawlDbReader;
import org.apache.nutch.crawl.Inlinks;
import org.apache.nutch.crawl.LinkDbReader;
import org.apache.nutch.net.URLFilter;
import org.apache.nutch.segment.SegmentReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by renxia on 2/21/15.
 */
public class ExactDupURLFilter implements URLFilter {

    private static final Logger LOG = LoggerFactory
            .getLogger(ExactDupURLFilter.class);

    private Configuration conf;
    private LinkDbReader linkdbReader;
    private CrawlDbReader crawlDbReader;
    private SegmentReader segmentReader;
    private long count = 0;

    public ExactDupURLFilter() {
        super();
        try {
            /*linkdbReader = new LinkDbReader(conf, new Path("/home/renxia/urlfilter/nutch/runtime/local/aaa/linkdb"));
            crawlDbReader = new CrawlDbReader();
            segmentReader = new SegmentReader(conf, false, false, false, false, true, true);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String filter(String urlString) {
        LOG.info("URlFilter ID: " + System.identityHashCode(this));
        count++;

        /*try {
            Text url = new Text(urlString);
            String[] anchors = linkdbReader.getAnchors(url);
            for (String s : anchors) {
                LOG.info("anchors: " + s);
            }

            Inlinks inLinks = linkdbReader.getInlinks(url);
            LOG.info("Inlinks: " + inLinks.toString());

            CrawlDatum data = crawlDbReader.get("/home/renxia/urlfilter/nutch/runtime/local/aaa/crawldb", urlString, conf);
            LOG.info("Crawl data: " + data.toString());

            segmentReader.get(new Path("/home/renxia/urlfilter/nutch/runtime/local/aaa/aaa/segments"), new Text(urlString), new OutputStreamWriter(
                    System.out, "UTF-8"), new HashMap<String, List<Writable>>());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /*Iterator it = this.conf.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            LOG.info("entry key: " + entry.getKey());
            LOG.info("entry value: " + entry.getValue());
        }*/

        LOG.info("Processed " + count + " links");
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
