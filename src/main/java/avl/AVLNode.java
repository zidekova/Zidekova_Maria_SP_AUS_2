package avl;

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
    public AVLNode<T> getLeft() {
        return (AVLNode<T>) super.getLeft();
    }

    @Override
    public AVLNode<T> getRight() {
        return (AVLNode<T>) super.getRight();
    }

    @Override
    public AVLNode<T> getParent() {
        return (AVLNode<T>) super.getParent();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBalanceFactor() {
        return this.balanceFactor;
    }

    public void setBalanceFactor(int balanceFactor) {
        this.balanceFactor = balanceFactor;
    }
}
