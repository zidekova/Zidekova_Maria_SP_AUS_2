package abs;

public abstract class AbstractNode<T extends Comparable<T>> {
    private final AbstractNode<T> parent;
    private final AbstractNode<T> leftSon;
    private final AbstractNode<T> rightSon;

    public AbstractNode() {
        this.parent = null;
        this.leftSon = null;
        this.rightSon = null;
    }

    protected AbstractNode<T> getParent() {
        return this.parent;
    }

    protected AbstractNode<T> getLeft() {
        return this.leftSon;
    }

    protected AbstractNode<T> getRight() {
        return this.rightSon;
    }
}
