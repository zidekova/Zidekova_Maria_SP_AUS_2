package bst;

import abs.AbstractNode;

public class BSTNode<T extends Comparable<T>> extends AbstractNode<T> {
    private T data;
    private BSTNode<T> parent;
    private BSTNode<T> left;
    private BSTNode<T> right;

    public BSTNode(T data) {
        this.data = data;
        this.parent = null;
        this.left = null;
        this.right = null;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public BSTNode<T> getParent() {
        return this.parent;
    }

    public void setParent(BSTNode<T> parent) {
        this.parent = parent;
    }

    @Override
    public BSTNode<T> getLeft() {
        return this.left;
    }

    public void setLeft(BSTNode<T> left) {
        this.left = left;
    }

    @Override
    public BSTNode<T> getRight() {
        return this.right;
    }

    public void setRight(BSTNode<T> right) {
        this.right = right;
    }
}
