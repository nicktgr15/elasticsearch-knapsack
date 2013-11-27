package org.xbib.elasticsearch.s3;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class S3Impl extends AbstractComponent implements S3 {

    private final AmazonS3 s3client;

    @Inject
    public S3Impl(Settings settings) {
        super(settings);
        AWSCredentialsProviderChain providerChain = new AWSCredentialsProviderChain(
                new ClasspathPropertiesFileCredentialsProvider(), new DefaultAWSCredentialsProviderChain());
        this.s3client = new AmazonS3Client(providerChain);
    }
    
    @Override
    public void writeToS3(String target, String s3Path) {
        String bucketName = settings.get("s3.bucket_name");
        
        logger.debug("Uploading " + target + " to " + bucketName + " under " + s3Path);
        File file = new File(target);
        s3client.putObject(new PutObjectRequest(bucketName, s3Path, file));
        file.delete();
    }

    @Override
    public void readFromS3(String target, String s3Path) throws IOException {
       String bucketName = settings.get("s3.bucket_name");

       S3Object object = s3client.getObject(new GetObjectRequest(bucketName, s3Path));
       saveS3ObjectToDisk(object, target);
    }
    
    private void saveS3ObjectToDisk(S3Object object, String filePath) throws IOException{
        InputStream reader = new BufferedInputStream(object.getObjectContent());
        File file = new File(filePath);

        OutputStream writer = new BufferedOutputStream(new FileOutputStream(file));

        int read = -1;
        while ((read = reader.read()) != -1) {
            writer.write(read);
        }

        writer.flush();
        writer.close();
        reader.close();
    }

}
