package uk.tanton.streaming.lambda.fanout;

public class Config {

    private String transcoderSnsArn;

    public String getTranscoderSnsArn() {
        return transcoderSnsArn;
    }

    public void setTranscoderSnsArn(String transcoderSnsArn) {
        this.transcoderSnsArn = transcoderSnsArn;
    }
}
