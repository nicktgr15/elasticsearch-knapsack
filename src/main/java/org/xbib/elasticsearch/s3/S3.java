package org.xbib.elasticsearch.s3;

import java.io.IOException;

public interface S3 {
    /**
     * Transfer a local file to S3
     * @param target The path of the file we want to copy to S3
     * @param s3Path The path inside the S3 bucket where the file should be saved
     */
    public void writeToS3(String target, String s3Path);
    
    /**
     * Save an artifact from S3 to the local disk
     * @param target The local path where the S3 artifact should be saved
     * @param s3Path The path inside the S3 bucket from where the file should be downloaded
     * @throws IOException An IOException is thrown when is unable to write to the target
     */
    public void readFromS3(String target, String s3Path) throws IOException;
}
