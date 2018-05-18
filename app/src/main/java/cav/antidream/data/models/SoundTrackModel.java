package cav.antidream.data.models;

/**
 * Created by cav on 18.05.18.
 */

public class SoundTrackModel {
    private String mTrack;
    private String mFile;

    public SoundTrackModel(String track, String file) {
        mTrack = track;
        mFile = file;
    }

    public String getTrack() {
        return mTrack;
    }

    public String getFile() {
        return mFile;
    }
}
