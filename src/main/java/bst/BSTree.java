package bst;

import abs.AbstractTree;

import java.io.*;
import java.util.*;
import java.util.function.Function;

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

            if (current == this.root) {
                this.root = child;
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

    public T findMin() {
        if (this.root == null) return null;

        BSTNode<T> current = (BSTNode<T>) this.root;

        while (current.getLeft() != null) {
            current = current.getLeft();

        }
        return current.getData();
    }

    public T findMax() {
        if (this.root == null) return null;

        BSTNode<T> current = (BSTNode<T>) this.root;

        while (current.getRight() != null) {
            current = current.getRight();
        }

        return current.getData();
    }

    public List<T> intervalSearch(T from, T to) {
        List<T> result = new ArrayList<>();

        if (this.root == null || from.compareTo(to) > 0) {
            return result;
        }

        BSTNode<T> current = findFirstInRange(this.getRoot(), from, to);

        while (current != null) {
            result.add(current.getData());
            current = getNextInRange(current, from, to);
        }

        return result;
    }

    private BSTNode<T> findFirstInRange(BSTNode<T> node, T from, T to) {
        BSTNode<T> current = node;
        BSTNode<T> firstInRange = null;

        while (current != null) {
            int cmpLow = from.compareTo(current.getData());
            int cmpHigh = to.compareTo(current.getData());

            if (cmpLow <= 0 && cmpHigh >= 0) {
                firstInRange = current;
                current = current.getLeft();
            } else if (cmpLow > 0) {
                current = current.getRight();
            } else {
                current = current.getLeft();
            }
        }

        return firstInRange;
    }


    private BSTNode<T> getNextInRange(BSTNode<T> node, T from, T to) {
        BSTNode<T> current = node;
        BSTNode<T> successor;

        if (current.getRight() != null) {
            BSTNode<T> temp = current.getRight();
            while (temp.getLeft() != null) {
                temp = temp.getLeft();
            }
            current = temp;
        } else {
            BSTNode<T> parent = current.getParent();
            while (parent != null && current == parent.getRight()) {
                current = parent;
                parent = parent.getParent();
            }
            current = parent;
        }

        while (current != null && (current.getData().compareTo(to) > 0 || current.getData().compareTo(from) < 0)) {
            if (current.getData().compareTo(to) > 0) {
                BSTNode<T> parent = current.getParent();
                while (parent != null && current == parent.getRight()) {
                    current = parent;
                    parent = parent.getParent();
                }
                current = parent;
            } else { // current < from
                if (current.getRight() != null) {
                    BSTNode<T> temp = current.getRight();
                    while (temp.getLeft() != null) {
                        temp = temp.getLeft();
                    }
                    current = temp;
                } else {
                    BSTNode<T> parent = current.getParent();
                    while (parent != null && current == parent.getRight()) {
                        current = parent;
                        parent = parent.getParent();
                    }
                    current = parent;
                }
            }
        }

        successor = current;
        return successor;
    }

    public int size() {
        return this.inorder().size();
    }

    public List<T> inorder() {
        List<T> result = new ArrayList<>();
        if (this.root == null) return result;

        BSTNode<T> current = this.getRoot();
        BSTNode<T> predecessor;

        while (current != null) {
            if (current.getLeft() == null) {
                result.add(current.getData());
                current = current.getRight();
            } else {
                predecessor = current.getLeft();
                while (predecessor.getRight() != null && predecessor.getRight() != current) {
                    predecessor = predecessor.getRight();
                }

                if (predecessor.getRight() == null) {
                    predecessor.setRight(current);
                    current = current.getLeft();
                } else {
                    predecessor.setRight(null);
                    result.add(current.getData());
                    current = current.getRight();
                }
            }
        }

        return result;
    }

    private List<T> levelorder() {
        List<T> result = new ArrayList<>();
        if (this.root == null) return result;

        Queue<BSTNode<T>> queue = new LinkedList<>();
        queue.add(this.getRoot());

        while (!queue.isEmpty()) {
            BSTNode<T> current = queue.poll();
            result.add(current.getData());

            if (current.getLeft() != null) {
                queue.add(current.getLeft());
            }
            if (current.getRight() != null) {
                queue.add(current.getRight());
            }
        }

        return result;
    }


    protected BSTNode<T> getRoot() {
        return (BSTNode<T>) this.root;
    }

    public boolean verifyTree() {
        return verifyBST(this.getRoot(), null, null);
    }

    // verifies, that all values in left subtree are less than in the right subtree
    private boolean verifyBST(BSTNode<T> node, T min, T max) {
        if (node == null) return true;

        T value = node.getData();

        if ((min != null && value.compareTo(min) <= 0) || (max != null && value.compareTo(max) >= 0)) {
            System.err.println("BST error at node " + value + " (min=" + min + ", max=" + max + ")");
            return false;
        }

        return verifyBST(node.getLeft(), min, value) && verifyBST(node.getRight(), value, max);
    }

    public void saveToCSV(String filename, boolean includeHeaders) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            if (includeHeaders) {
                writer.println("data");
            }

            List<T> elements = this.levelorder();
            for (T element : elements) {
                writer.println(element.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving to CSV: " + e.getMessage());
        }
    }

    public void loadFromCSV(String filename, Function<String, T> parser) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (!line.trim().isEmpty()) {
                    T element = parser.apply(line.trim());
                    if (element != null) {
                        this.insert(element);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading from CSV: " + e.getMessage());
        }
    }
}
