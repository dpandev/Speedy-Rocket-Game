package dpandev;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.BitSet;

/**
 * Represents an {@link Input} object. This class assigns Key Event Listeners to the {@code Scene}
 * node to determine if any of the specified keyboard keys are pressed/released.
 */
public class Input {

    private BitSet keyboardBitSet = new BitSet();

    private KeyCode leftKey = KeyCode.A;
    private KeyCode rightKey = KeyCode.D;
    private KeyCode spaceKey = KeyCode.SPACE;

    private Scene scene;

    /**
     * Constructs a new {@link Input} object.
     * @param scene the {@code Scene} node to add/remove event listeners to
     */
    public Input(Scene scene) {
        this.scene = scene;
    }

    /**
     * Registers event filters for the node.
     */
    public void addListeners() {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
    }

    /**
     * Removes event filters from the node.
     */
    public void removeListeners() {
        scene.removeEventFilter(KeyEvent.KEY_PRESSED, keyPressedEventHandler);
        scene.removeEventFilter(KeyEvent.KEY_RELEASED, keyReleasedEventHandler);
    }

    /**
     * Sets the applicable {@link #keyboardBitSet} index value to {@code True} if the key event
     * corresponds to one of the specified key being pressed down.
     */
    private EventHandler<KeyEvent> keyPressedEventHandler = event -> {
        //registers key being pressed down
        keyboardBitSet.set(event.getCode().ordinal(), true);
    };

    /**
     * Sets the applicable {@link #keyboardBitSet} index value to {@code False} if the key event
     * corresponds to one of the specified key being released.
     */
    private EventHandler<KeyEvent> keyReleasedEventHandler = event -> {
        //registers key being released
        keyboardBitSet.set(event.getCode().ordinal(), false);
    };

    /**
     * Checks if the 'A' key is being pressed down.
     * @return {@code True} if the 'A' key is pressed down. {@code False} otherwise.
     */
    public boolean isMoveLeft() {
        return keyboardBitSet.get(leftKey.ordinal()) && !keyboardBitSet.get(rightKey.ordinal()) && !keyboardBitSet.get(spaceKey.ordinal());
    }

    /**
     * Checks if the 'D' key is being pressed down.
     * @return {@code True} if the 'D' key is pressed down. {@code False} otherwise.
     */
    public boolean isMoveRight() {
        return keyboardBitSet.get(rightKey.ordinal()) && !keyboardBitSet.get(leftKey.ordinal()) && !keyboardBitSet.get(spaceKey.ordinal());
    }

    /**
     * Checks if the 'SPACE' key is being pressed down.
     * @return {@code True} if the 'SPACE' key is pressed down. {@code False} otherwise.
     */
    public boolean isSpaceKey() {
        return keyboardBitSet.get(spaceKey.ordinal()) && !keyboardBitSet.get(leftKey.ordinal()) && !keyboardBitSet.get(rightKey.ordinal());
    }
}
