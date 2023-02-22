package spell;
import java.lang.*;

public class Trie implements ITrie{

    //Declaring member variables
    private Node root;
    private int wordCount; //number of UNIQUE WORDS
    private int wordCountNU; //number of words(not unique)
    private int nodeCount;

    //constructor
    public Trie(){
        wordCountNU = 0;
        wordCount = 0;
        nodeCount = 1;
        root = new Node();
    }
    public Node getRoot(){
        return root;
    }

    //member functions
    public void add_Helper(Node node, String word){

        //Creating a letter var and an alpha index for that letter var for the first character in the word
        char letter = word.charAt(0);
        int alphindex = letter - 'a';

        //if there is no child at the letter alpha index, add one
        if(node.getChildrenArray()[alphindex] == null){
            node.getChildrenArray()[alphindex] = new Node();
            this.incrementNodeCount();
        }

        //change the node we are working with to the child node at alphaindex
        node = (Node) node.getChildrenArray()[alphindex];

        //if the word is not over, recurse!
        if(word.substring(1) != ""){
            add_Helper(node, word.substring(1));
        }
        //if the word is over, increment the count on the node
        else if(word.substring(1) == ""){
            node.incrementValue();
            this.incrementWordCountNU();
            if(node.getValue() == 1){
                this.incrementWordCount();
            }
        }
    }

    @Override
    public void add(String word) {
        add_Helper(root, word);
    }

    public int hash_Helper(Node node, int mult){

        for(int i = 0; i < 26; i++) {

            //if the child array at index i is not null
            if (node.getChildrenArray()[i] != null) {

                //multiplying mult by i
                mult = mult * i;

                //resetting node as the child node
                //node = (Node) node.getChildrenArray()[i];
            }
        }
        return mult;
    }

    @Override
    public int hashCode() { //UPDATE I think this works...
        int mult = hash_Helper(root, 1);
        return wordCount*wordCountNU*nodeCount*mult;

        //Combine the following values
        //1. wordCount
        //2. nodeCount
        //3. The index of each of the root node's non-null children
    }


    private boolean equals_Helper(INode n1, INode n2){
        //if n1 and n2 don't have the same count, return false.
        if(n1.getValue() != n2.getValue()){
            return false;
        }

        int j = 0;
        INode[] child1 = n1.getChildren();
        INode[] child2 = n2.getChildren();
        Integer[] nonempty = new Integer[26];

        //if they do have the same count, then look if they have non-null children in the same indices
        for(int i = 0; i <child1.length; i++){
            if(child1[i] != null && child2[i] != null){
                nonempty[j] = i;
                j++;
            }
            else if((child1[i] == null && child2[i] != null) || (child1[i] != null && child2[i] == null)){
                return false;
            }
        }

        //recursion
        boolean same = true;
        for(int i = 0; i < nonempty.length; i++){

            //if we reach the end of the values we put in
            if(nonempty[i] == null){
                break;
            }

            INode next1 = child1[nonempty[i]];
            INode next2 = child2[nonempty[i]];
            same = equals_Helper(next1, next2);

            if(same == false){
                break;
            }
        }

        return same;
    }

    @Override
    public boolean equals(Object obj) {

        //UPDATE

        //checking if the two tries are equal

        //checking if the object is empty
        if(obj == null){
            return false;
        }

        //checking if they are comparing your object to itself (will be same reference)
        if(obj == this){
            return true;
        }

        //checking if objects are of the same class
        if(obj.getClass() != this.getClass()){
            return false;
        }

        //DOWNCAST:: casting object type to type of class because if we get here we know it's the same class
        Trie t = (Trie) obj;


        //checking if the trees are equal
        //worst case, you have to traverse both the trees?

        //check if they have the same node count, non unique word count, and unique word count
        //this.wordCountNU != t.wordCountNU) ||
        if((this.nodeCount != t.nodeCount) || (this.wordCount != t.wordCount)){
            return false;
        }

        //now we have to traverse the trees
        return equals_Helper(this.root, t.root);
    }

    private StringBuilder toString_Helper(Node n, StringBuilder currWord, StringBuilder output){
        //assume passed in node is not null because they all have a root, so they're never null

        //check if it has a nonzero count
        if(n.getValue() > 0){
            output.append(currWord.toString());
            //output.append(n.getValue()); //UPDATE take this out, this is just a check. THIS WORKS!
            output.append("\n");

        }

        //now recurse on the children
        //preorder traversal
        for(int i = 0; i < 26; i++){
            Node child = (Node) n.getChildrenArray()[i];

            if (child != null){
                //adding the letter from the child
                char childLetter = (char)('a' + i);
                currWord.append(childLetter);

                toString_Helper(child, currWord, output);

                //deleting the child letter from currword
                currWord.deleteCharAt(currWord.length() - 1);
            }
        }
        return output;
    }

    @Override
    public String toString() {

        //create two empty stringbuilders
        StringBuilder currWord = new StringBuilder(); //this will hold the word that we are currently on
        StringBuilder output = new StringBuilder(); //this is what we append all the words to output


        //returns a string with an alphabatized list of all the unique words in the trie (separate lines)
        //traverse tree, and for each node with a nonzero count, append that word to the output
        output = toString_Helper(root, currWord, output);
        //currword and output start out empty but that is good becuase the root doesn't have a word and theres no output yet

        return output.toString();
    }

    @Override
    public INode find(String word) {

        //UPDATE THIS MAY ALWAYS BE RETURNING ROOT> POSSIBLE BUG
        Node currnode = root;

        for(int i = 0; i < word.length(); i++){
            char letter = word.charAt(i);
            int alphindex = letter - 'a';

            //if on one of the letters you find a null
            if(currnode.getChildrenArray()[alphindex] == null){
                return null;
            }

            else{

                //if we've reached the end of the word
                if(i == word.length() - 1){
                    if(currnode.getChildrenArray()[alphindex].getValue() != 0){
                        return currnode.getChildrenArray()[alphindex];
                    }
                    else{
                        return null;
                    }
                }

                //replacing node with its child node
                currnode = (Node) currnode.getChildrenArray()[alphindex];
            }
        }
        //if you get through that whole thing without returning anything?
        return null;

        /*The user’s input string will be compared against the Trie using the Trie.find(String).
                If the input string (independent of case) is found in the Trie, the program will indicate
        that it is spelled correctly by returning the final INode of the word. If the case-independent
        version of the input string is not found, your program will return the final INode of the most “similar”
        word as defined below. If the input string is not found and there is no word “similar” to it, your
        program should return null. */


        //UPDATE this is just always returning the root! unless its not in the trie then it returns null

    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

   /* public int getWordCountNU() {
        return wordCountNU;
    }*/

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    public void incrementWordCountNU(){
        wordCountNU++;
    }

    public void incrementWordCount(){
        wordCount++;
    }

    public void incrementNodeCount(){
        nodeCount++;
    }
}
