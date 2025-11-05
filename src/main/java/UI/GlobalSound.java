package UI;

import javafx.scene.media.AudioClip;

// Quản lý âm thanh toàn cục
public class GlobalSound {
    private static boolean enabled = true;

    public static boolean isEnabled() {
        return enabled;
    }

    public static void toggle() {
        enabled = !enabled;
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
