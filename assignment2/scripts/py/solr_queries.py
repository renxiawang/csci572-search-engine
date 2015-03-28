questions = [
    {
        "question":"Questions: What time-based trends exist for discussion of water resource in Arctic?",
        "queries":[
            {
                "query":"Discussion of oil in Artic in the last 5 years.",
                "content_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Awater+OR+title%3Aarctic)%5E10+OR+(keywords%3Awater+OR+keywords%3Aarctic)%5E8+OR+(description%3Awater+OR+description%3Aarctic)%5E5+OR+(text%3Awater+AND+text%3Aarctic)%5E3)+AND+datetime%3A%5BNOW-5YEAR+TO+NOW%5D&rows=10&fl=score%2Cpagerank%2Ctitle%2Ckeywords%2Cdatetime&wt=json&indent=true",
                "link_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Awater+OR+title%3Aarctic)+OR+(keywords%3Awater+OR+keywords%3Aarctic)+OR+(description%3Awater+OR+description%3Aarctic)+OR+(text%3Awater+AND+text%3Aarctic))+AND+datetime%3A%5BNOW-5YEAR+TO+NOW%5D&sort=pagerank+desc&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true"
            },
            {
                "query":"Discussion of oil in Artic in the last 10 years to the last 5 years.",
                "content_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Awater+OR+title%3Aarctic)%5E10+OR+(keywords%3Awater+OR+keywords%3Aarctic)%5E8+OR+(description%3Awater+OR+description%3Aarctic)%5E5+OR+(text%3Awater+AND+text%3Aarctic)%5E3)+AND+datetime%3A%5BNOW-10YEAR+TO+NOW-5YEAR%5D&rows=10&fl=score%2Cpagerank%2Ctitle%2Ckeywords%2Cdatetime&wt=json&indent=true",
                "link_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Awater+OR+title%3Aarctic)+OR+(keywords%3Awater+OR+keywords%3Aarctic)+OR+(description%3Awater+OR+description%3Aarctic)+OR+(text%3Awater+AND+text%3Aarctic))+AND+datetime%3A%5BNOW-10YEAR+TO+NOW-5YEAR%5D&sort=pagerank+desc&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true"
            },
            {
                "query":"Discussion of oil in Artic in the last 15 years to the last 10 years.",
                "content_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Awater+OR+title%3Aarctic)%5E10+OR+(keywords%3Awater+OR+keywords%3Aarctic)%5E8+OR+(description%3Awater+OR+description%3Aarctic)%5E5+OR+(text%3Awater+AND+text%3Aarctic)%5E3)+AND+datetime%3A%5BNOW-10YEAR+TO+NOW-5YEAR%5D&rows=10&fl=score%2Cpagerank%2Ctitle%2Ckeywords%2Cdatetime&wt=json&indent=true",
                "link_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Awater+OR+title%3Aarctic)+OR+(keywords%3Awater+OR+keywords%3Aarctic)+OR+(description%3Awater+OR+description%3Aarctic)+OR+(text%3Awater+AND+text%3Aarctic))+AND+datetime%3A%5BNOW-15YEAR+TO+NOW-10YEAR%5D&sort=pagerank+desc&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true"
            }
        ]
    },
    {
        "question":"Which region has the most security discussions than others?",
        "queries":[
            {
                "query":"Security discussion in Arctic.",
                "content_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Asecurity+OR+title%3Aarctic)%5E10+OR+(keywords%3Asecurity+OR+keywords%3Aarctic)%5E8+OR+(description%3Asecurity+OR+description%3Aarctic)%5E5+OR+(text%3Asecurity+AND+text%3Aarctic)%5E3)&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true",
                "link_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Asecurity+OR+title%3Aarctic)+OR+(keywords%3Asecurity+OR+keywords%3Aarctic)+OR+(description%3Asecurity+OR+description%3Aarctic)+OR+(text%3Asecurity+AND+text%3Aarctic))&sort=pagerank+desc&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true"
            },
            {
                "query":"Security discussion in Antarctica.",
                "content_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Asecurity+OR+title%3Aantarctica)%5E10+OR+(keywords%3Asecurity+OR+keywords%3Aantarctica)%5E8+OR+(description%3Asecurity+OR+description%3Aantarctica)%5E5+OR+(text%3Asecurity+AND+text%3Aantarctica)%5E3)&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true",
                "link_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Asecurity+OR+title%3Aantarctica)+OR+(keywords%3Asecurity+OR+keywords%3Aantarctica)+OR+(description%3Asecurity+OR+description%3Aantarctica)+OR+(text%3Asecurity+AND+text%3Aantarctica))&sort=pagerank+desc&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true"
            },
            {
                "query":"Security discussion in Southern Ocean.",
                "content_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Asecurity+OR+title%3A%22southern+ocean%22)%5E10+OR+(keywords%3Asecurity+OR+keywords%3A%22southern+ocean%22)%5E8+OR+(description%3Asecurity+OR+description%3A%22southern+ocean%22)%5E5+OR+(text%3Asecurity+AND+text%3A%22southern+ocean%22)%5E3)&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true",
                "link_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Asecurity+OR+title%3A%22southern+ocean%22)+OR+(keywords%3Asecurity+OR+keywords%3A%22southern+ocean%22)+OR+(description%3Asecurity+OR+description%3A%22southern+ocean%22)+OR+(text%3Asecurity+AND+text%3A%22southern+ocean%22))&sort=pagerank+desc&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true"
            }
        ]

    },
    {
        "question":"Does the frequency of climate change discussion change based on time, or region?",
        "queries":[
            {
                "query":"Climate change discussion in Arctic, Antarctica and Southern Ocean in the last 10 years.",
                "content_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Aclimate+OR+title%3Atemperature+OR+title%3A%22southern+ocean%22+OR+title%3Aarctic+OR+title%3Aantarctica)%5E10+OR+(keywords%3Aclimate+OR+keywords%3Atemperature+OR+keywords%3A%22southern+ocean%22+OR+keywords%3Aarctic+OR+keywords%3Aantarctica)%5E8+OR+(description%3Aclimate+OR+description%3Atemperature+OR+description%3A%22southern+ocean%22+OR+description%3Aarctic+OR+description%3Aantarctica)%5E5+OR+(text%3Aclimate+AND+text%3Atemperature+AND+(text%3A%22southern+ocean%22+OR+text%3Aarctic+OR+text%3Aantarctica))%5E3)+AND+datetime%3A%5B+NOW-10YEAR+TO+NOW%5D&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true",
                "link_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Aclimate+OR+title%3Atemperature+OR+title%3A%22southern+ocean%22+OR+title%3Aarctic+OR+title%3Aantarctica)+OR+(keywords%3Aclimate+OR+keywords%3Atemperature+OR+keywords%3A%22southern+ocean%22+OR+keywords%3Aarctic+OR+keywords%3Aantarctica)+OR+(description%3Aclimate+OR+description%3Atemperature+OR+description%3A%22southern+ocean%22+OR+description%3Aarctic+OR+description%3Aantarctica)+OR+(text%3Aclimate+AND+text%3Atemperature+AND+(text%3A%22southern+ocean%22+OR+text%3Aarctic+OR+text%3Aantarctica)))+AND+datetime%3A%5B+NOW-10YEAR+TO+NOW%5D&sort=pagerank+desc&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true"
            },
            {
                "query":"Climate change discussion in Arctic, Antarctica and Southern Ocean in the last 20 years to the last 10 years.",
                "content_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Aclimate+OR+title%3Atemperature+OR+title%3A%22southern+ocean%22+OR+title%3Aarctic+OR+title%3Aantarctica)%5E10+OR+(keywords%3Aclimate+OR+keywords%3Atemperature+OR+keywords%3A%22southern+ocean%22+OR+keywords%3Aarctic+OR+keywords%3Aantarctica)%5E8+OR+(description%3Aclimate+OR+description%3Atemperature+OR+description%3A%22southern+ocean%22+OR+description%3Aarctic+OR+description%3Aantarctica)%5E5+OR+(text%3Aclimate+AND+text%3Atemperature+AND+(text%3A%22southern+ocean%22+OR+text%3Aarctic+OR+text%3Aantarctica))%5E3)+AND+datetime%3A%5B+NOW-20YEAR+TO+NOW-10YEAR%5D&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true",
                "link_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Aclimate+OR+title%3Atemperature+OR+title%3A%22southern+ocean%22+OR+title%3Aarctic+OR+title%3Aantarctica)+OR+(keywords%3Aclimate+OR+keywords%3Atemperature+OR+keywords%3A%22southern+ocean%22+OR+keywords%3Aarctic+OR+keywords%3Aantarctica)+OR+(description%3Aclimate+OR+description%3Atemperature+OR+description%3A%22southern+ocean%22+OR+description%3Aarctic+OR+description%3Aantarctica)+OR+(text%3Aclimate+AND+text%3Atemperature+AND+(text%3A%22southern+ocean%22+OR+text%3Aarctic+OR+text%3Aantarctica)))+AND+datetime%3A%5B+NOW-20YEAR+TO+NOW-10YEAR%5D&sort=pagerank+desc&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true"
            },
            {
                "query":"Climate change discussion in Arctic, Antarctica and Southern Ocean in the last 30 years to the last 20 years.",
                "content_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Aclimate+OR+title%3Atemperature+OR+title%3A%22southern+ocean%22+OR+title%3Aarctic+OR+title%3Aantarctica)%5E10+OR+(keywords%3Aclimate+OR+keywords%3Atemperature+OR+keywords%3A%22southern+ocean%22+OR+keywords%3Aarctic+OR+keywords%3Aantarctica)%5E8+OR+(description%3Aclimate+OR+description%3Atemperature+OR+description%3A%22southern+ocean%22+OR+description%3Aarctic+OR+description%3Aantarctica)%5E5+OR+(text%3Aclimate+AND+text%3Atemperature+AND+(text%3A%22southern+ocean%22+OR+text%3Aarctic+OR+text%3Aantarctica))%5E3)+AND+datetime%3A%5B+NOW-30YEAR+TO+NOW-20YEAR%5D&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true",
                "link_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Aclimate+OR+title%3Atemperature+OR+title%3A%22southern+ocean%22+OR+title%3Aarctic+OR+title%3Aantarctica)+OR+(keywords%3Aclimate+OR+keywords%3Atemperature+OR+keywords%3A%22southern+ocean%22+OR+keywords%3Aarctic+OR+keywords%3Aantarctica)+OR+(description%3Aclimate+OR+description%3Atemperature+OR+description%3A%22southern+ocean%22+OR+description%3Aarctic+OR+description%3Aantarctica)+OR+(text%3Aclimate+AND+text%3Atemperature+AND+(text%3A%22southern+ocean%22+OR+text%3Aarctic+OR+text%3Aantarctica)))+AND+datetime%3A%5B+NOW-30YEAR+TO+NOW-20YEAR%5D&sort=pagerank+desc&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true"
            },
            {
                "query":"Climate change discussion in Arctic all the time.",
                "content_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Aclimate+OR+title%3Atemperature+OR+title%3Aarctic)%5E10+OR+(keywords%3Aclimate+OR+keywords%3Atemperature+OR+keywords%3Aarctic)%5E8+OR+(description%3Aclimate+OR+description%3Atemperature+OR+description%3Aarctic)%5E5+OR+(text%3Aclimate+AND+text%3Atemperature+AND+text%3Aarctic)%5E3)&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true",
                "link_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Aclimate+OR+title%3Atemperature+OR+title%3Aarctic)+OR+(keywords%3Aclimate+OR+keywords%3Atemperature+OR+keywords%3Aarctic)+OR+(description%3Aclimate+OR+description%3Atemperature+OR+description%3Aarctic)+OR+(text%3Aclimate+AND+text%3Atemperature+AND+text%3Aarctic))&sort=pagerank+desc&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true"
            },
            {
                "query":"Climate change discussion in Antarctica all the time.",
                "content_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Aclimate+OR+title%3Atemperature+OR+title%3Aantarctica)%5E10+OR+(keywords%3Aclimate+OR+keywords%3Atemperature+OR+keywords%3Aantarctica)%5E8+OR+(description%3Aclimate+OR+description%3Atemperature+OR+description%3Aantarctica)%5E5+OR+(text%3Aclimate+AND+text%3Atemperature+AND+text%3Aantarctica)%5E3)&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true",
                "link_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Aclimate+OR+title%3Atemperature+OR+title%3Aantarctica)+OR+(keywords%3Aclimate+OR+keywords%3Atemperature+OR+keywords%3Aantarctica)+OR+(description%3Aclimate+OR+description%3Atemperature+OR+description%3Aantarctica)+OR+(text%3Aclimate+AND+text%3Atemperature+AND+text%3Aantarctica))&sort=pagerank+desc&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true"
            },
            {
                "query":"Climate change discussion in Southern Ocean all the time.",
                "content_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Aclimate+OR+title%3Atemperature+OR+title%3A%22southern+ocean%22)%5E10+OR+(keywords%3Aclimate+OR+keywords%3Atemperature+OR+keywords%3A%22southern+ocean%22)%5E8+OR+(description%3Aclimate+OR+description%3Atemperature+OR+description%3A%22southern+ocean%22)%5E5+OR+(text%3Aclimate+AND+text%3Atemperature+AND+text%3A%22southern+ocean%22)%5E3)&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true",
                "link_based":"http://192.168.1.143:8983/solr/solrCell/select?q=((title%3Aclimate+OR+title%3Atemperature+OR+title%3A%22southern+ocean%22)+OR+(keywords%3Aclimate+OR+keywords%3Atemperature+OR+keywords%3A%22southern+ocean%22)+OR+(description%3Aclimate+OR+description%3Atemperature+OR+description%3A%22southern+ocean%22)+OR+(text%3Aclimate+AND+text%3Atemperature+AND+text%3A%22southern+ocean%22))&sort=pagerank+desc&fl=score%2Cpagerank%2Ctitle%2Ckeywords&wt=json&indent=true"
            }
        ]
    }
]