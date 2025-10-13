package abs;

public abstract class AbstractTree<T extends Comparable<T>> {
    protected AbstractNode<T> root;

    public AbstractTree() {
        this.root = null;
    }

    public abstract T search(T data);
    public abstract AbstractNode<T> insert(T data);
    public abstract AbstractNode<T> delete(T data);

}
