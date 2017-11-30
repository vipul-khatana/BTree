/*
Implementing the BTree 
*/
import java.util.List;
import java.util.*;
import java.util.Vector;

@SuppressWarnings("unchecked")

// Creating the class Pair, which will be stored in the array of every node of tree
class Pair <Key extends Comparable<Key>, Value>{
    Key key;
    Value value;

    public Pair(Key k, Value v){
        this.key = k;
        this.value = v;
    }

    public Key getKey(){ return this.key;}

    public Value getValue(){ return this.value;}

    public void setValue(Value v){ this.value = v;}

    public void setKey(Key k){ this.key = k;}
}

@SuppressWarnings("unchecked")

// Class for a single node of the tree
class BTreeNode <Key extends Comparable<Key>, Value>{
    int pos = -1; //This will point to the index of the array upto which the keys are filled
    int num_of_children = 0; 
    Pair <Key,Value>[] arr;
    BTreeNode <Key,Value>[] children_set; //set of the children of a node
    BTreeNode <Key,Value> parent;

    //constructor
    public BTreeNode(int b){
        arr = (Pair <Key,Value>[]) new Pair[b];
        children_set = (BTreeNode <Key,Value>[]) new BTreeNode[b+1];
        parent = null;
    }

    public BTreeNode getParent(){
        return parent;
    }
    
    public Key keyAt(int i){
        return arr[i].getKey();
    }
    
    public Value valueAt(int i){
        return arr[i].getValue();
    }

    //find appropriate position of k for insertion, search. 
    public int find_pos(key k){ 
        int c2 = -1;                                                            
        for(int i =0; i<= pos;i++){
            int a = k.compareTo((Key)this.keyAt(i));
            if(a>0){continue;}
            else {
                c2 = i;
                break;
                }
        }
        if(c2==-1){c2=(pos + 1);}
        return c2;
    }
    
    public void deleteElement(Key k, Value v){
        int c1 = this.find_pos(k);
        
        //Finding if the key is repeated and the number of repetitions
        if (k.compareTo(this.keyAt(c1)) == 0)
            int c2= c1 + 1;
        while((k.compareTo(this.keyAt(c2)) == 0){
            c2++;
            if(c2==pos){ 
                pos=c1-1;
                break;
            }
        } 
        //Deleting all the keys 
        for(int j=c1;j<=pos;j++){
            if(c2==pos){
                this.arr[j]=this.arr[c2];
                this.pos=j;
                break;
            }
            else{
                this.arr[j] = this.arr[c2];
                c2++;
            }
        } 
    }
    
    public void addElement(Key k, Value v){
        Pair <Key,Value> temp = new Pair(k,v);
        int c2= this.find_pos(k);
        //shift the elements after position and insert
        for(int i= this.pos; i>=c2;i--){
            this.arr[i+1] = this.arr[i];
        }
        (this.pos)++;
        this.arr[c2] = temp;
    }

    public void setParent(BTreeNode n){
        this.parent = n;
    }

    public BTreeNode[] siblingSet(){
        return parent.children_set;
    }

    public void addChild(BTreeNode n){ // set current as parent of n, add n to children_set
        if(n == null){return;}
        n.setParent(this);
        int c2= this.find_pos(n.keyAt(n.pos));//finding the index in the children set
        children_set[c2] = n;
        num_of_children++; 
    }

    public BTreeNode RemoveElements(int a,int b){ //Remove elements and children of a node from index a to b
        BTreeNode <Key,Value> temp = new BTreeNode(this.arr.length);
        int t = a-1;
        if(a>b){return null;}
        int count =0;
        
        //Removing elements and children
        for(int i=a;i<=b;i++){
            temp.addElement(this.keyAt(i),this.valueAt(i));
            count++;
            if(children_set[i] != null){
                temp.addChild(children_set[i]);
                children_set[i] = null;
                num_of_children--;
            }
        }
        //corner case, b == pos
        if(children_set[pos + 1] != null){
                temp.addChild(children_set[pos + 1]);
                children_set[pos + 1] = null;
                num_of_children--;
            }
        pos = t-1;
        return temp;
    }

    public int lastChildAt(){//Gives the index of the last non null node in the children set. 
        int count=-1;
        for(int i=0;i<children_set.length;i++){
            if(children_set[i] != null){
                count = i;
            }
        }
        return count;
    }

    public int indexAsChild(){//Returns the index of this node in it's children set 
        int val=-1;
        for(int i=0;i<(parent.children_set.length);i++){
            if(parent.children_set[i] != null){
                if(this.keyAt(0).compareTo(parent.children_set[i].keyAt(0)) == 0){
                    val=i;
                }
            }
        }
        return val;
    }

    public void rightShift(int n){ // Shifts all the nodes after the node at index n in children set, by one place to the right
        for(int i=(this.lastChildAt() + 1); i>(n+1);i--){
            if(children_set[i-1] != null){
                children_set[i] = children_set[i-1];
            }
        }
    }
}

@SuppressWarnings("unchecked")
public class BTree <Key extends Comparable<Key>,Value> implements DuplicateBTree<Key,Value> {
    static BTreeNode <Key,Value> root;

    public BTree(int b) { /* Initializes an empty b-tree. Assume b is even. */
        if ( b%2 == 0){
            root = new BTreeNode(b);    
        }
    }

    @Override
    public boolean isEmpty() {
        return root.pos == -1;
    }

    public BTree <Key,Value> subtree(int n){ // Tree with child at index n as root
        BTree <Key,Value> temp = new BTree(root.arr.length);
        temp.root = root.children_set[n];
        return temp;
    }
    
    public int subtree_size(BTreeNode n){ // Size of tree with n as root
        int count = 0;
        count += n.pos + 1;
        for(int i = 0; i <= n.lastChildAt(); i++) {
            count += subtree_size(n.children_set[i]);
        }
        return count;
    }
    
    @Override
    public int size() {
        return subtree_size(root);
    }
    
    public int height_of(BTreeNode n){ // Height of tree with root at n 
        int count=0;
        count+= 1;
        if(n.children_set[0] != null)
            count+= height_of(n.children_set[0]);
        return count;
    }

    @Override
    public int height() {
        return height_of(root);
    }

    public List<Value> search_in(BTreeNode n, Key key){ // Search key in a tree with n as root
        List<Value> temp = new Vector<Value>();
        //Search in n
        for(int i=0;i<=n.pos;i++){
                int a = key.compareTo((Key)n.keyAt(i));
                if(a<0){break;}
                if(a==0)
                    temp.add((Value)n.valueAt(i));
            }
        // Recursively search in all children of n
        for(int i=0;i<n.children_set.length;i++){
            if(n.children_set[i] != null)
                temp.addAll(search_in(n.children_set[i],key));
        }
        return temp;
    }
    
    @Override
    public List<Value> search(Key key)throws IllegalKeyException {
        return search_in(root,key);
    }

    public BTreeNode fixOverflow(BTreeNode n){ // Fix overflow
        BTreeNode <Key,Value> new_root = new BTreeNode(n.arr.length);
        Pair <Key,Value> temp1 ;
        int c = (n.pos + 1)/2;
        temp1 = n.arr[c];
        
        // Overflow at root
        if(n.parent == null){
            n.parent = new BTreeNode(n.arr.length);
            n.parent.addElement(temp1.getKey(),temp1.getValue());
            BTreeNode <Key,Value> temp2 = new BTreeNode(n.arr.length);
            temp2 = n.RemoveElements(c+1,n.pos);
            n.parent.addChild(temp2);
            n.parent.addChild(n);
            new_root = n.parent;
        }
        //Overflow at any other node
        else{
            n.parent.addElement(temp1.getKey(),temp1.getValue());
            n.parent.rightShift(n.indexAsChild());
            BTreeNode <Key,Value> temp2 = new BTreeNode(n.arr.length);
            temp2 = n.RemoveElements(c+1,n.pos);
            n.parent.addChild(temp2);
            new_root = root;
            if(n.parent.arr.length == (n.parent.pos + 1)) { new_root = this.fixOverflow(n.parent);}
        }
        return new_root;
    }

    public BTreeNode insert_item(BTreeNode n, Key key, Value val) {
        BTreeNode <Key,Value> new_root = new BTreeNode(n.arr.length);
        // n not a leaf
        if(n.num_of_children != 0){
            int c1= n.find_pos(k);
            new_root = insert_item(n.children_set[c1],key,val);
        }
        // n is a leaf
        else { 
            n.addElement(key,val);
            new_root = root;
            if(n.arr.length == (n.pos + 1)) 
                new_root = this.fixOverflow(n);
        }
        return new_root;
    }
    
    @Override
    public void insert(Key key, Value val) {
        Pair <Key,Value> temp = new Pair(key,val);
        if(root.pos == -1){
                (root.pos)+= 1;
                root.arr[root.pos] = temp;
            }
        else
            root = insert_item(root, key, val);
    }
    
    @Override
    public String toString(){ // Prints the tree using in order traversal
        return print(root);
    }

    public String print(BTreeNode n){ // In order traversal of the tree
        String temp = "[";
        for(int i=0;i<=n.pos;i++){
            if(i !=0)
                temp = temp + ",";
            if(n.children_set[i] != null)
                temp = temp + (print(n.children_set[i])) + ",";
            temp = temp + (Key)n.keyAt(i).toString();
        }
        if(n.children_set[n.pos + 1] != null)
            temp = temp + "," + print(n.children_set[n.pos + 1]);
        temp = temp + "]";
        return temp;
    }
 
    //testing the function 
    public static void main(String[] args) {
        BTree <Integer,String> test_alpha = new BTree(4);
        test_alpha.insert(4,"4");
        test_alpha.insert(5,"5");
        test_alpha.insert(10,"10");
        test_alpha.insert(13,"13");
        test_alpha.insert(25,"25");
        test_alpha.insert(6,"6");
        test_alpha.insert(13,"13");
        test_alpha.insert(18,"18");
        test_alpha.insert(15,"15");
        test_alpha.insert(17,"17");
        test_alpha.insert(84,"84");
        test_alpha.insert(44,"4");
        test_alpha.insert(45,"4");
        System.out.println( test_alpha.toString());
    }
}
