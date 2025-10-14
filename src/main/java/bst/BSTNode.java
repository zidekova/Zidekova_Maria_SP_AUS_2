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

    protected T getData() {
        return this.data;
    }

    protected void setData(T data) {
        this.data = data;
    }

    @Override
    protected BSTNode<T> getParent() {
        return this.parent;
    }

    protected void setParent(BSTNode<T> parent) {
        this.parent = parent;
    }

    @Override
    protected BSTNode<T> getLeft() {
        return this.left;
    }

    protected void setLeft(BSTNode<T> leftSon) {
        this.left = leftSon;
    }

    @Override
    protected BSTNode<T> getRight() {
        return this.right;
    }

    protected void setRight(BSTNode<T> rightSon) {
        this.right = rightSon;
    }
}
