package spell;

public class Node implements INode {

    private int count;

    //I did this different... UPDATE?
    private INode[] children = new Node[26];

    //constructor
    public Node() {
        count = 0;
    }

    @Override
    public int getValue() {
        //value = count is the number of occurrences of the word if it is the end of a word
        return count;
    }

    @Override
    public void incrementValue() {
        count = count + 1;
    }

    public INode[] getChildrenArray() {
        return children;
    }

    @Override
    public INode[] getChildren() {
        return children;
    }
}
