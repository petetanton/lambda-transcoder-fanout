package uk.tanton.streaming.lambda.fanout;

import java.io.FileNotFoundException;

public class ProfileNotFoundException extends FileNotFoundException {
    public ProfileNotFoundException(String s) {
        super(s);
    }
}
