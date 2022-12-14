import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class FXMLController {

    @FXML
    private Button startStopButton;

    @FXML
    private Label timeLabel;

    private boolean isRunnig;
    private long lastTick;
    private long currentTime;
    private final PomodoroTimer timer;
    private final MediaPlayer player;

    public FXMLController() {
        timer = new PomodoroTimer();

        var path = getClass().getResource("clock-sound-effect.mp3").toExternalForm();
        var media = new Media(path);

        player = new MediaPlayer(media);
    }

    @FXML
    public void initialize() {
        startStopButton.setOnMouseClicked((event) -> {
            if (isRunnig) {
                stopTimer();
                startStopButton.setText("START");
            } else {
                startTimer();
                startStopButton.setText("CANCEL");
            }
        });
        reset();
    }

    private void startTimer() {
        isRunnig = true;
        lastTick = System.currentTimeMillis();
        timer.start();
    }

    private void stopTimer() {
        reset();
        timer.stop();
    }

    private void reset() {
        isRunnig = false;
        lastTick = 0;
        currentTime = 5000;
        startStopButton.setText("START");
        updateUI();
    }

    private void updateUI() {
        var minutes = currentTime / 1000 / 60;
        var seconds = currentTime / 1000 % 60;
        var formatedTime = String.format("%02d:%02d", minutes, seconds);

        timeLabel.setText(formatedTime);
    }

    private class PomodoroTimer extends AnimationTimer {

        @Override
        public void handle(long now) {
            if (!isRunnig) {
                return;
            }
            
            var delta = System.currentTimeMillis() - lastTick;

            if (delta < 1000) {
                return;
            }

            lastTick = System.currentTimeMillis();
            currentTime -= 1000;

            if (currentTime <= 0) {
                player.setOnEndOfMedia(() -> {
                    player.stop();
                    player.seek(Duration.ZERO);
                });
                player.play();
                stopTimer();
            }
            updateUI();
        }
        
    }
}
