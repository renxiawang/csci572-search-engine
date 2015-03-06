# Assignment 2 Solr Indexing and Ranking

### Install Solr with Tika 1.8-SNAPSHOT
1. Pull tika truck and build:
```
git clone https://github.com/apache/tika.git
cd tika
mvn clean install
```
2. Pull solr 4.10:
```
svn co http://svn.apache.org/repos/asf/lucene/dev/branches/lucene_solr_4_10/
```

3. Update ```lucene_solr_4_10/lucene/ivy-settings.xml``` and ```lucene_solr_4_10/lucene/ivy-versions.properties```:

    - Change line 138 ```org.apache.tika.version = 1.5``` to ```org.apache.tika.version = 1.8-SNAPSHOT```.
    - Uncomment line 46 - 51:

    ```
     46     <filesystem name="local-maven-2" m2compatible="true" local="true">
     47       <artifact
     48           pattern="${local-maven2-dir}/[organisation]/[module]/[revision]/[module]-[revision].[ext]" />
     49       <ivy
     50           pattern="${local-maven2-dir}/[organisation]/[module]/[revision]/[module]-[revision].pom" />
     51     </filesystem>
    ```
    - Uncomment line 56:
    ```
    <resolver ref="local-maven-2" />
    ```
4. Build Solr:
```
cd lucene_solr_4_10
ant compile
```

