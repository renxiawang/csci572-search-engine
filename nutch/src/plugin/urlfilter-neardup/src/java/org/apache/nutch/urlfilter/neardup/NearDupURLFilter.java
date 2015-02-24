package org.apache.nutch.urlfilter.neardup;

import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.net.URLFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by renxia on 2/21/15.
 */
public class NearDupURLFilter implements URLFilter {

    private static final Logger LOG = LoggerFactory
            .getLogger(NearDupURLFilter.class);
    private Configuration conf;

    @Override
    public String filter(String urlString) {
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
