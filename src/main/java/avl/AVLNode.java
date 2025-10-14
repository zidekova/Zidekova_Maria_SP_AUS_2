package avl;

import abs.AbstractNode;
import bst.BSTNode;

public class AVLNode<T extends Comparable<T>> extends BSTNode<T> {
    private int height;
    private int balanceFactor;

    public AVLNode(T data) {
        super(data);
        this.height = 1;
        this.balanceFactor = 0;
    }

    @Override
    protected AVLNode<T> getLeft() {
        return (AVLNode<T>) super.getLeft();
    }

    @Override
    protected void setLeft(BSTNode<T> leftSon) {
        super.setLeft(leftSon);
    }

    @Override
    protected AVLNode<T> getRight() {
        return (AVLNode<T>) super.getRight();
    }

    @Override
    protected void setRight(BSTNode<T> rightSon) {
        super.setRight(rightSon);
    }

    @Override
    protected AVLNode<T> getParent() {
        return (AVLNode<T>) super.getParent();
    }

    @Override
    protected void setParent(BSTNode<T> parent) {
        super.setParent(parent);
    }

    protected int getHeight() {
        return height;
    }

    protected void setHeight(int height) {
        this.height = height;
    }

    protected int getBalanceFactor() {
        return this.balanceFactor;
    }

    protected void setBalanceFactor(int balanceFactor) {
        this.balanceFactor = balanceFactor;
    }
}
