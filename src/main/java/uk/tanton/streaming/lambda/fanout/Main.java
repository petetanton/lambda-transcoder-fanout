package uk.tanton.streaming.lambda.fanout;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main implements RequestHandler<S3Event, String> {
    private final AmazonS3Client amazonS3Client;
    private final AmazonSNSClient amazonSNSClient;
    private final Gson gson;
    private final ProfileProvider profileProvider;
    private final Config config;

    public Main() throws FileNotFoundException {
        this.amazonS3Client = new AmazonS3Client();
        this.amazonSNSClient = new AmazonSNSClient();
        this.amazonSNSClient.setRegion(Region.getRegion(Regions.EU_WEST_1));
        this.gson = new Gson();
        this.config = gson.fromJson(new FileReader(this.getClass().getClassLoader().getResource("config.json").getPath()), Config.class);
        this.profileProvider = new ProfileProvider(this.gson);
    }

    public String handleRequest(S3Event s3Event, Context context) {

        for (S3EventNotification.S3EventNotificationRecord record : s3Event.getRecords()) {
            final S3EventNotification.S3Entity s3 = record.getS3();
            context.getLogger().log("s3 key:  " + s3.getObject().getKey());
            final ObjectMetadata metadata = amazonS3Client.getObjectMetadata(s3.getBucket().getName(), s3.getObject().getKey());
            final List<Profile> profileList = parseProfileList(metadata.getUserMetaDataOf("profiles"), context);
            final String streamId = metadata.getUserMetaDataOf("streamId");

            for (final Profile profile : profileList) {
                final TranscodeMessage transcodeMessage = new TranscodeMessage(s3.getBucket().getName(), streamId, profile, s3.getObject().getKey());
                sendMessageToTranscoder(transcodeMessage, context);

            }
        }

        return "finished";
    }

    private List<Profile> parseProfileList(final String profileCsv, final Context context) {
        final List<Profile> profileList = new ArrayList<>();
        for (final String profile : profileCsv.split(",")) {
            try {
                profileList.add(profileProvider.getProfile(profile));
            } catch (ProfileNotFoundException e) {
                context.getLogger().log("Could not find a profile for key '" + profile + "'");
                e.printStackTrace();
            }
        }
        return profileList;
    }

    public void sendMessageToTranscoder(final TranscodeMessage transcodeMessage, final Context context) {
        final PublishResult publish = this.amazonSNSClient.publish(new PublishRequest(config.getTranscoderSnsArn(), gson.toJson(transcodeMessage)));
        context.getLogger().log(String.format("Transcode message sent: %s (message ID: %s)", transcodeMessage.toString(), publish.getMessageId()));
    }
}
