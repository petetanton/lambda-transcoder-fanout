package uk.tanton.streaming.lambda.fanout;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class ProfileProvider {

    final Map<String, Profile> profileMap;
    final Gson gson;

    public ProfileProvider(final Gson gson) {
        this.gson = gson;
        this.profileMap = new HashMap<String, Profile>();
    }

    public Profile getProfile(final String name) throws ProfileNotFoundException {
        return this.profileMap.getOrDefault(name, getProfileFromFile(name));
    }

    private Profile getProfileFromFile(final String name) throws ProfileNotFoundException {
        final Profile profile;
        try {
            profile = gson.fromJson(new FileReader(this.getClass().getClassLoader().getResource("profiles/" + name + ".json").getPath()), Profile.class);
        } catch (final NullPointerException | FileNotFoundException e) {
            throw new ProfileNotFoundException("Could not find profile");
        }
        this.profileMap.put(name, profile);
        return profile;
    }
}
