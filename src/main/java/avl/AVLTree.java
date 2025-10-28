package avl;

import bst.BSTNode;
import bst.BSTree;

public class AVLTree<T extends Comparable<T>> extends BSTree<T> {

    public AVLTree() {
        super();
    }

    @Override
    protected BSTNode<T> createNode(T data) {
        return new AVLNode<>(data);
    }

    @Override
    public AVLNode<T> insert(T data) {
        AVLNode<T> inserted = (AVLNode<T>) super.insert(data);

        // if inserted node was a duplicate
        if (inserted == null) return null;

        AVLNode<T> current = inserted.getParent();
        while (current != null) {
            this.updateHeight(current);
            int balanceFactor = this.getBalanceFactor(current);

            if (balanceFactor < -1 || balanceFactor > 1) {
                this.balance(current);
            }

            current = current.getParent();
        }

        return inserted;
    }

    @Override
    public T search(T data) {
        return super.search(data);
    }

    @Override
    public AVLNode<T> delete(T data) {
        AVLNode<T> current = (AVLNode<T>) super.delete(data);

        if (current == null) return null;

        while (current != null) {
            this.updateHeight(current);
            int balanceFactor = this.getBalanceFactor(current);

            if (balanceFactor < -1 || balanceFactor > 1) {
                this.balance(current);
            }

            current = current.getParent();
        }

        return current;
    }

    @Override
    public boolean verifyTree() {
        return verifyAVL((AVLNode<T>) this.getRoot());
    }

    private boolean verifyAVL(AVLNode<T> node) {
        if (node == null) return true;

        boolean leftOK = verifyAVL(node.getLeft());
        boolean rightOK = verifyAVL(node.getRight());
        if (!leftOK || !rightOK) return false;

        int leftHeight = (node.getLeft() != null) ? node.getLeft().getHeight() : 0;
        int rightHeight = (node.getRight() != null) ? node.getRight().getHeight() : 0;
        int balance = rightHeight - leftHeight;

        // verify height
        int expectedHeight = 1 + Math.max(leftHeight, rightHeight);
        if (node.getHeight() != expectedHeight) {
            System.err.println("Height error at node " + node.getData() +
                    " (expected=" + expectedHeight + ", actual=" + node.getHeight() + ")");
            return false;
        }

        // verify balance
        if (Math.abs(balance) > 1) {
            System.err.println("Balance error at node " + node.getData() +
                    " (LH=" + leftHeight + ", RH=" + rightHeight + ")");
            return false;
        }

        return true;
    }

    private void leftRotation(AVLNode<T> node) {
        AVLNode<T> rightSon = node.getRight();
        AVLNode<T> leftSonOfRightSon = rightSon.getLeft();
        AVLNode<T> parent = node.getParent();

        // rotation
        rightSon.setLeft(node);
        node.setRight(leftSonOfRightSon);

        // update parents
        node.setParent(rightSon);
        rightSon.setParent(parent);
        if (leftSonOfRightSon != null) {
            leftSonOfRightSon.setParent(node);
        }

        // update parent's references
        if (parent == null) {
            this.root = rightSon;
        } else if (parent.getLeft() == node) {
            parent.setLeft(rightSon);
        } else {
            parent.setRight(rightSon);
        }

        // update heights and balance factors
        updateHeight(node);
        updateHeight(rightSon);

    }

    private void rightRotation(AVLNode<T> node) {
        AVLNode<T> leftSon = node.getLeft();
        AVLNode<T> rightSonOfLeftSon = leftSon.getRight();
        AVLNode<T> parent = node.getParent();

        // rotation
        leftSon.setRight(node);
        node.setLeft(rightSonOfLeftSon);

        // update parents
        node.setParent(leftSon);
        leftSon.setParent(parent);
        if (rightSonOfLeftSon != null) {
            rightSonOfLeftSon.setParent(node);
        }

        // update parent's references
        if (parent == null) {
            this.root = leftSon;
        } else if (parent.getRight() == node) {
            parent.setRight(leftSon);
        } else {
            parent.setLeft(leftSon);
        }

        // update heights and balance factors
        updateHeight(node);
        updateHeight(leftSon);
    }

    private void balance(AVLNode<T> node) {
        int heightLeft = (node.getLeft() != null) ? node.getLeft().getHeight() : 0;
        int heightRight = (node.getRight() != null) ? node.getRight().getHeight() : 0;
        int balanceFactor = heightRight - heightLeft;

        // L
        if (balanceFactor < -1) {
            AVLNode<T> leftSon = node.getLeft();

            // LR
            if (this.getBalanceFactor(leftSon) > 0) {
                this.leftRotation(leftSon);
            }
            // LL
            rightRotation(node);
        }

        // R
        if (balanceFactor > 1) {
            AVLNode<T> rightSon = node.getRight();

            // RL
            if (this.getBalanceFactor(rightSon) < 0) {
                this.rightRotation(rightSon);
            }
            // RR
            leftRotation(node);
        }
    }

    private int getBalanceFactor(AVLNode<T> node) {
        int leftHeight = (node.getLeft() != null) ? node.getLeft().getHeight() : 0;
        int rightHeight = (node.getRight() != null) ? node.getRight().getHeight() : 0;
        return rightHeight - leftHeight;
    }

    private void updateHeight(AVLNode<T> node) {
        int heightLeft = (node.getLeft() != null) ? (node.getLeft()).getHeight() : 0;
        int heightRight = (node.getRight() != null) ? (node.getRight()).getHeight() : 0;
        node.setHeight(1 + Math.max(heightLeft, heightRight));
    }
}
