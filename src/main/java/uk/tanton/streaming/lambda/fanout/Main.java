package uk.tanton.streaming.lambda.fanout;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.util.Map;

public class Main implements RequestHandler<S3Event, String> {
    private AmazonS3Client amazonS3Client = new AmazonS3Client();

    public String handleRequest(S3Event s3Event, Context context) {
        for (S3EventNotification.S3EventNotificationRecord record : s3Event.getRecords()) {
            final S3EventNotification.S3Entity s3 = record.getS3();
            System.out.println("s3 key:  " + s3.getObject().getKey());
            final ObjectMetadata metadata = amazonS3Client.getObjectMetadata(s3.getBucket().getName(), s3.getObject().getKey());
            for (Map.Entry<String, String> entry : metadata.getUserMetadata().entrySet()) {
                System.out.println("\t" + entry.getKey() + ": " + entry.getValue());
            }
        }
        return "finished";
    }
}
