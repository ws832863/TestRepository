package game.cliente.hero;

/**
 * @author Erickzanardo
 */
public enum CharacterPosition {
    FACE_DOWN(0), FACE_LEFT(1), FACE_RIGHT(2), FACE_UP(3);

    private int index;

    private CharacterPosition(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
