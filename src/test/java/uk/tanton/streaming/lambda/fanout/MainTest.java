package uk.tanton.streaming.lambda.fanout;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainTest {

    private static final String S3_KEY = "/some/path/to/a/seg.ts";
    private static final String S3_BUCKET = "some-ingest-bucket";

    private Main underTest;
    private List<S3EventNotification.S3EventNotificationRecord> records = new ArrayList<S3EventNotification.S3EventNotificationRecord>();

    @Mock private S3Event s3Event;
    @Mock private Context context;
    @Mock private S3EventNotification.S3EventNotificationRecord record;
    @Mock private S3EventNotification.S3Entity s3Entity;
    @Mock private S3EventNotification.S3ObjectEntity s3ObjectEntity;
    @Mock private S3EventNotification.S3BucketEntity s3BucketEntity;


    @Before
    public void setup() throws FileNotFoundException {
        underTest = new Main();
        records.add(record);
        when(record.getS3()).thenReturn(s3Entity);
        when(s3Entity.getObject()).thenReturn(s3ObjectEntity);
        when(s3ObjectEntity.getKey()).thenReturn(S3_KEY);
        when(s3Entity.getBucket()).thenReturn(s3BucketEntity);
        when(s3BucketEntity.getName()).thenReturn(S3_BUCKET);
    }

    @Ignore
    @Test
    public void it() {
        when(s3Event.getRecords()).thenReturn(records);



        final String result = underTest.handleRequest(s3Event, context);


        verify(s3Event).getRecords();
        verifyNoMoreInteractions(s3Event, context, record);
    }
}
