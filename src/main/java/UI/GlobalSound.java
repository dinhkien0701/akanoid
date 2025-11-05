package UI;

import javafx.scene.media.AudioClip;
import java.util.ArrayList;
import java.util.List;

public class GlobalSound {

    private static boolean enabled = true;
    private static final List<AudioClip> clips = new ArrayList<>();

    public static boolean isEnabled() {
        return enabled;
    }

    // Thêm clip vào quản lý
    public static void register(AudioClip clip) {
        if (clip != null && !clips.contains(clip)) {
            clips.add(clip);
        }
    }

    public static void toggle() {
        enabled = !enabled;

        for (AudioClip clip : clips) {
            if (enabled) {
                clip.play();  // bật tất cả clip
            } else {
                clip.stop();  // tắt tất cả clip đang chạy
            }
        }
    }

    public static void play(AudioClip clip) {
        if (enabled && clip != null) {
            clip.play();
        }
    }

    public static void stop(AudioClip clip) {
        if (clip != null) {
            clip.stop();
        }
    }
}
