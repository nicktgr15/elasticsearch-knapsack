package org.xbib.elasticsearch.s3;

public class S3Noop implements S3 {

    @Override
    public void writeToS3(String target, String s3Path) {
    }

    @Override
    public void readFromS3(String target, String s3Path) {
    }

}
