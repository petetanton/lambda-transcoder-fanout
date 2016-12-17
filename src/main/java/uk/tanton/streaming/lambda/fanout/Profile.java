package uk.tanton.streaming.lambda.fanout;

public class Profile {

    private String profileName;
    private String videoCodec;
    private String x264Opts;
    private String audioCodec;
    private int videoBitrate;

    public String getAudioCodec() {
        return audioCodec;
    }

    public void setAudioCodec(String audioCodec) {
        this.audioCodec = audioCodec;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public int getVideoBitrate() {
        return videoBitrate;
    }

    public void setVideoBitrate(int videoBitrate) {
        this.videoBitrate = videoBitrate;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public String getX264Opts() {
        return x264Opts;
    }

    public void setX264Opts(String x264Opts) {
        this.x264Opts = x264Opts;
    }
}
