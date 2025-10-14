package bst;

import abs.AbstractTree;

import java.util.List;

public class BSTree<T extends Comparable<T>> extends AbstractTree<T> {
    public BSTree() {
        super();
    }

    protected BSTNode<T> createNode(T data) {
        return new BSTNode<>(data);
    }

    @Override
    public BSTNode<T> insert(T data) {
        BSTNode<T> newNode = this.createNode(data);

        // if BST is empty
        if (this.root == null) {
            this.root = newNode;
            return newNode;
        }

        BSTNode<T> current = this.getRoot();
        BSTNode<T> parent = null;

        // find a parent
        while (current != null) {
            parent = current;
            int compareResult = data.compareTo(current.getData());
            if (compareResult == 0) {
                return null;
            } else if (compareResult < 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }

        // insert new node
        if (data.compareTo(parent.getData()) < 0) {
            parent.setLeft(newNode);
        } else {
            parent.setRight(newNode);
        }

        newNode.setParent(parent);
        return newNode;
    }

    @Override
    public T search(T data) {
        BSTNode<T> current = this.getRoot();

        while (current != null) {
            int compareResult = data.compareTo(current.getData());
            if (compareResult == 0) {
                return current.getData();
            } else if (compareResult < 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }

        return null; // if node is not in the tree
    }

    @Override
    public BSTNode<T> delete(T data) {
        // if BST is empty
        if (this.root == null) return null;

        BSTNode<T> current = this.getRoot();
        BSTNode<T> parent = null;

        // find a parent
        while (current != null && data.compareTo(current.getData()) != 0) {
            parent = current;
            if (data.compareTo(current.getData()) < 0) {
                current = current.getLeft();
            } else  {
                current = current.getRight();
            }
        }

        // if node is not in the tree
        if (current == null) return null;

        // if node is a leaf
        if (current.getLeft() == null && current.getRight() == null) {
            if (current == root) {
                root = null;
                return null;
            } else if ((parent != null ? parent.getLeft() : null) == current) {
                parent.setLeft(null);
            } else {
                if (parent != null) {
                    parent.setRight(null);
                }
            }
            return parent;
        }

        // if node has only 1 child
        if (current.getLeft() == null || current.getRight() == null) {
            BSTNode<T> child = (current.getLeft() != null) ? current.getLeft() : current.getRight();

            if (current == root) {
                root = child;
            } else if ((parent != null ? parent.getLeft() : null) == current) {
                parent.setLeft(child);
            } else {
                if (parent != null) {
                    parent.setRight(child);
                }
            }

            child.setParent(parent);
            return child;
        }

        // if node has 2 children
        BSTNode<T> successorParent = current;
        BSTNode<T> successor = current.getRight();

        // find the most left son (min in right subtree)
        while (successor.getLeft() != null) {
            successorParent = successor;
            successor = successor.getLeft();
        }

        current.setData(successor.getData());

        if (successorParent.getLeft() == successor) {
            successorParent.setLeft(successor.getRight());
            if (successor.getRight() != null)
                successor.getRight().setParent(successorParent);
        } else {
            // if right subtree doesn't have any left son
            successorParent.setRight(successor.getRight());
            if (successor.getRight() != null)
                successor.getRight().setParent(successorParent);
        }

        return successor;
    }

    public BSTNode<T> findMin() {
        if (this.root == null) return null;

        BSTNode<T> current = (BSTNode<T>) this.root;

        while (current.getLeft() != null) {
            current = current.getLeft();

        }
        return current;
    }

    public BSTNode<T> findMax() {
        if (this.root == null) return null;

        BSTNode<T> current = (BSTNode<T>) this.root;

        while (current.getRight() != null) {
            current = current.getRight();
        }

        return current;
    }

    public List<T> intervalSearch(T from, T to) {
        // TODO
        return null;
    }

    protected BSTNode<T> getRoot() {
        return (BSTNode<T>) this.root;
    }

    protected int getTreeHeight() {
        return calculateHeight(this.getRoot());
    }

    protected int calculateHeight(BSTNode<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(calculateHeight(node.getLeft()), calculateHeight(node.getRight()));
    }

    public void printTree() {
        printTree(this.getRoot(), "", true, true);
    }

    private void printTree(BSTNode<T> node, String prefix, boolean isTail, boolean isRoot) {
        if (node == null) return;

        if (isRoot) {
            System.out.println(node.getData() + " ──┐");
        } else {
            String connector = isTail ? "└── " : "├── ";
            String direction = isTail ? "L:" : "R:";
            System.out.println(prefix + connector + direction + node.getData());
        }

        String newPrefix = prefix + (isTail ? "    " : "│   ");

        if (node.getRight() != null) {
            printTree(node.getRight(), newPrefix, false, false);
        }
        if (node.getLeft() != null) {
            printTree(node.getLeft(), newPrefix, true, false);
        }
    }

}
