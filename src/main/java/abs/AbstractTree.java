package abs;

public abstract class AbstractTree<T extends Comparable<T>> {
    protected AbstractNode<T> root;

    public AbstractTree() {
        this.root = null;
    }

    protected abstract T search(T data);
    protected abstract AbstractNode<T> insert(T data);
    protected abstract AbstractNode<T> delete(T data);

}
