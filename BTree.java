// package col106.a3;

import java.util.List;
import java.util.*;
import java.util.Vector;

// class bNotEvenException extends Exception {
//     public bNotEvenException() {
//         super("bNotEvenException");
//     }
// }

@SuppressWarnings("unchecked")
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
class BTreeNode <Key extends Comparable<Key>, Value>{
    int pos = -1; //This will point to the index of the array upto which the keys are filled
    int num_of_children = 0; 
    Pair <Key,Value>[] arr;
    BTreeNode <Key,Value>[] children_set; //
    BTreeNode <Key,Value> parent;


    public BTreeNode(int b){
        arr = (Pair <Key,Value>[]) new Pair[b];
        children_set = (BTreeNode <Key,Value>[]) new BTreeNode[b+1];
        parent = null;
    }

    public BTreeNode getParent(){
        return parent;
    }

    public void deleteElement(Key k){
        int c1 =-1;
        int c2=-1;
        for(int i=0;i<this.pos;i++){
            int a = k.compareTo(this.arr[i].getKey());
            if(a>0){continue;}
            if(a<0){
                c1 = i;
                // return c1;
            }
            if(a==0){
                c1=i;
                c2=i+1;
                break;
            }
        }

        // if(c1==-1){return c1 = (this.pos+1);}

        while((k.compareTo(this.arr[c2].getKey())) == 0){
            c2++;
            if(c2==this.pos){
                this.pos=c1-1;
                break;
            }
        }        
        int t=c2-1;
        for(int j=c1;j<=this.pos;j++){
            if(c2==this.pos){
                this.arr[j]=this.arr[c2];
                this.pos=j;
                break;
            }
            else{
                this.arr[j] = this.arr[c2];
                c2++;
            }
        }
        //return c1;    
    }

    public void addElement(Key k, Value v){
        Pair <Key,Value> temp = new Pair(k,v);
        int c2=-1;
        for(int i =0; i<= this.pos;i++){
            int a = k.compareTo(this.arr[i].getKey());
            if(a>0){continue;}
            else {
                c2 = i;
                break;
                }
        }
        if(c2==-1){c2=(this.pos + 1);}

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

    public void addChild(BTreeNode n){ // Adds the child at the relevant index  in the children set. 
        if(n == null){
            return;
        }
        n.setParent(this);
        int c2=-1;
        for(int i =0; i<= this.pos;i++){
            int a = n.arr[n.pos].getKey().compareTo(this.arr[i].getKey());
            if(a>=0){continue;}
            else {
                c2 = i;
                break;
                }
        }
        if(c2==-1){c2=(this.pos + 1);}
        this.children_set[c2] = n;
        this.num_of_children++;
        
    }

    public BTreeNode copyElements(int a,int b){//It copies the elements of this node from index a to b to a new node and returns it
        BTreeNode <Key,Value> temp = new BTreeNode(this.arr.length);
        int t = a-1;
        // while(this.arr[t].getKey().compareTo(this.arr[a].getKey()) == 0){

        //     a++;
        // }
        if(a>b){
            return null;
        }
        int count =0;
        for(int i=a;i<=b;i++){
            temp.addElement(this.arr[i].getKey(),this.arr[i].getValue());
            count++;
            if(this.children_set[i] != null){
                temp.addChild(this.children_set[i]);
                this.children_set[i] = null;
                this.num_of_children--;
            }
        }
        if(this.children_set[this.pos + 1] != null){
                temp.addChild(this.children_set[this.pos + 1]);
                this.children_set[this.pos + 1] = null;
                this.num_of_children--;
            }
        this.pos = t-1;
        return temp;
    }

    public int lastChildAt(){//Gives the index of the last node in the children set. 
        int count=-1;
        for(int i=0;i<this.children_set.length;i++){
            if(this.children_set[i] != null){
                count = i;
            }
        }
        return count;
    }

    public int indexAsChild(){//Returns the index of this node in it's children set 
        int val=-1;
        for(int i=0;i<(this.parent.children_set.length);i++){
            if(this.parent.children_set[i] != null){
                if(this.arr[0].getKey().compareTo(this.parent.children_set[i].arr[0].getKey()) == 0){
                    val=i;
                }
            }
        }
        return val;
    }

    public void rightShift(int n){ // Shifts all the nodes to the right of the node at index n by one place to the right
        for(int i=(this.lastChildAt() + 1); i>(n+1);i--){
            if(this.children_set[i-1] != null){
                this.children_set[i] = this.children_set[i-1];
            }
        }
    }
}

@SuppressWarnings("unchecked")
public class BTree <Key extends Comparable<Key>,Value> implements DuplicateBTree<Key,Value> {
    
    BTreeNode <Key,Value> root;

    public BTree(int b) { /* Initializes an empty b-tree. Assume b is even. */
        if ( b%2 == 0){
            root = new BTreeNode(b);    
        }
    }

    @Override
    public boolean isEmpty() {
        return this.root.pos == -1;
    }

    public BTree <Key,Value> subtree(int n){
        BTree <Key,Value> temp = new BTree(this.root.arr.length);
        temp.root = this.root.children_set[n];
        return temp;

    }
    
    public int subtree_size(BTreeNode n){
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

    
    public int height_of(BTreeNode n){
        int count=0;
        count+= 1;
        if(n.children_set[0] != null){
            count+= height_of(n.children_set[0]);
        }
        return count;
    }

    @Override
    public int height() {
        return height_of(root);
    }

    
    public List<Value> search_in(BTreeNode n, Key key){
        List<Value> temp = new Vector<Value>();
        for(int i=0;i<=n.pos;i++){
                int a = key.compareTo((Key)n.arr[i].getKey());
                if(a<0){break;}
                if(a==0){
                    temp.add((Value)n.arr[i].getValue());
                }
            }
        for(int i=0;i<n.children_set.length;i++){
            if(n.children_set[i] != null){
                temp.addAll(search_in(n.children_set[i],key));
            }
        }
        return temp;
    }
    @Override
    public List<Value> search(Key key)throws IllegalKeyException {

        // try {return search_in(root,key);
        // }
        // catch(IllegalKeyException e) {

        // }
        return search_in(root,key);
    }

    public BTreeNode fixOverflow(BTreeNode n){
        BTreeNode <Key,Value> new_root = new BTreeNode(n.arr.length);
        Pair <Key,Value> temp1 ;
        int c = (n.pos + 1)/2;
        temp1 = n.arr[c];
        if(n.parent == null){
            // System.out.println("Entering overflow root = " );
            // for(int i=0; i<=n.pos;i++)
            //     System.out.println(n.arr[i].getKey());
            n.parent = new BTreeNode(n.arr.length);
            n.parent.addElement(temp1.getKey(),temp1.getValue());
            BTreeNode <Key,Value> temp2 = new BTreeNode(n.arr.length);
            temp2 = n.copyElements(c+1,n.pos);
            n.parent.addChild(temp2);
            n.parent.addChild(n);
            // this.root = this.root.parent;
            new_root = n.parent;
            // System.out.println("Exiting overflow root = " );
            // for(int i=0; i<=n.pos;i++)
            //     System.out.println(n.arr[i].getKey());
            // System.out.println("Exiting child0 of root = " );
            // for(int i=0; i<=n.children_set[0].pos;i++)
            //     System.out.println(n.children_set[0].arr[i].getKey());
            // System.out.println("Exiting child1 of root = " );
            // for(int i=0; i<=n.children_set[1].pos;i++)
            //     System.out.println(n.children_set[1].arr[i].getKey());
        }
        else{
            n.parent.addElement(temp1.getKey(),temp1.getValue());
            n.parent.rightShift(n.indexAsChild());
            BTreeNode <Key,Value> temp2 = new BTreeNode(n.arr.length);
            temp2 = n.copyElements(c+1,n.pos);
            n.parent.addChild(temp2);
            new_root = root;
            if(n.parent.arr.length == (n.parent.pos + 1)) { new_root = this.fixOverflow(n.parent);}
        }
        return new_root;
    }

    public BTreeNode insert_item(BTreeNode n, Key key, Value val) {
        BTreeNode <Key,Value> new_root = new BTreeNode(n.arr.length);
        if(n.num_of_children != 0){
            int c1=-1;
            for(int i =0; i<= n.pos;i++){
                int a = key.compareTo((Key)n.arr[i].getKey());
                if(a>0){continue;}
                else {
                    c1 = i;
                    break;
                }
            }
            if(c1==-1){c1=(this.root.pos + 1);}
            new_root = insert_item(n.children_set[c1],key,val);
        }
        else {
            n.addElement(key,val);
            new_root = root;
            if(n.arr.length == (n.pos + 1)) {
                //System.out.println("overflow seen");
                new_root = this.fixOverflow(n);
            }
            // System.out.println("INSERTING overflow root = " );
            // for(int i=0; i<=n.pos;i++)
            //     System.out.println(n.arr[i].getKey());
        }
        return new_root;
    }
    
    @Override
    public void insert(Key key, Value val) {
        //System.out.println("Insert " + key);
        Pair <Key,Value> temp = new Pair(key,val); //Converting the key and the corresponding value to the pair which will further be added. 
        if(this.root.pos == -1){
                (this.root.pos)+= 1;
                this.root.arr[this.root.pos] = temp;
            }
        else{
            root = insert_item(root, key, val);
            // if(this.root.num_of_children != 0){
            //     for(int i =0; i<= this.root.pos;i++){
            //         int a = key.compareTo(this.root.arr[i].getKey());
            //         if(a>0){continue;}
            //         else {
            //             c1 = i;
            //             break;
            //         }
            //     }
            //     if(c1==-1){c1=(this.root.pos + 1);}
            //     this.subtree(c1).insert(key,val);
            //     System.out.println("INSERTING overflow root = " );
            //     for(int i=0; i<=root.pos;i++)
            //         System.out.println(root.arr[i].getKey());
            //     System.out.println("INSERTING child0 of root = " );
            //     for(int i=0; i<=root.children_set[0].pos;i++)
            //         System.out.println(root.children_set[0].arr[i].getKey());
            //     System.out.println("INSERTING child1 of root = " );
            //     for(int i=0; i<=root.children_set[1].pos;i++)
            //         System.out.println(root.children_set[1].arr[i].getKey());
            // }
            // else {
            //         this.root.addElement(key,val);
            //         if(this.root.arr.length == (this.root.pos + 1)) { this.fixOverflow(this.root);}
            //         System.out.println("INSERTING overflow root = " );
            //         for(int i=0; i<=root.pos;i++)
            //             System.out.println(root.arr[i].getKey()); 
            // }
        }

    }
    @Override
    public String toString(){
        return print(root);
    }

    public String print(BTreeNode n){
        //System.out.println("Printing");
        String temp = "[";
        for(int i=0;i<=n.pos;i++){
            if(i !=0){
                temp = temp + ",";
            }
            if(n.children_set[i] != null){
                temp = temp + (print(n.children_set[i])) + ",";
            }
            temp = temp + (Key)n.arr[i].getKey().toString();
            // if( i == n.pos){
            //     temp = temp + "]";
            // }
        }
        if(n.children_set[n.pos + 1] != null){
            temp = temp + "," + print(n.children_set[n.pos + 1]);
        }
        temp = temp + "]";
        return temp;
    }

    // @Override
    public void delete(Key key) throws IllegalKeyException {
        // int a = this.root.deleteElement(key);
        // if(this.root.parent != null){
        //     if(this.root.pos < ((this.root.arr.length)/2 - 1 )){
        //         this.fixUnderflow(this.root);
        //     }
        // }
        // if(this.root.children_set[a] != null){
        //     this.delete(key);
        // }
    }

    public static void main(String[] args) {
        BTree <Integer,String> test_alpha = new BTree(4);
        BTreeNode <Integer,String> test_child = new BTreeNode(12);
        BTreeNode <Integer,String> test_child2 = new BTreeNode(4);
        BTreeNode <Integer,String> test_child3 = new BTreeNode(6);
        BTreeNode <Integer,String> test_child4 = new BTreeNode(6);
        BTreeNode <Integer,String> test_split;


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
    //     test_alpha.insert(4,"4");
    //     test_alpha.insert(4,"4");
    //     test_alpha.insert(4,"4");
    //     test_alpha.insert(4,"4");
    //     test_alpha.insert(4,"4");
    //     test_alpha.insert(4,"4");
        // test_alpha.insert(457,"4");
        // test_alpha.insert(458,"4");
        // test_alpha.insert(459,"4");
        // test_alpha.insert(460,"4");
        // test_alpha.insert(470,"4");
        // test_alpha.insert(480,"4");
        // test_alpha.insert(481,"4");
        // test_alpha.insert(482,"4");
        // test_alpha.insert(483,"4");
        // test_alpha.insert(484,"4");
        // test_alpha.insert(485,"4");
        // test_alpha.insert(486,"4");
        // test_alpha.insert(487,"4");
        // test_alpha.insert(488,"4");
        // test_alpha.insert(489,"4");
        // test_alpha.insert(490,"4");
        // test_alpha.insert(491,"4");
        // test_alpha.insert(492,"4");

        // test_child.addElement(1,"25");
        // test_child.addElement(2,"45");
        // test_child.addElement(3,"5");
        // test_child.addElement(4,"55");
        // // test_child.addElement(4,"55");
        // // test_child.addElement(4,"55");
        // // test_child.addElement(4,"55");
        // // test_child.addElement(4,"55");
        // // test_child.addElement(4,"55");
        // // test_child.addElement(4,"55");
        // test_child.addElement(8,"55");
        // test_child.addElement(9,"55");
        // test_child2.addElement(1,"1");
        // test_child2.addElement(2,"2");
        // test_child2.addElement(3,"3");
        // test_child3.addElement(27,"27");
        // test_child3.addElement(35,"35");
        // test_child3.addElement(42,"42");
        // test_child4.addElement(59,"59");
        // test_child4.addElement(72,"72");
        // test_child4.addElement(75,"75");
        // test_child.addChild(test_child2);
        // test_child.addChild(test_child3);
        // test_child.addChild(test_child4);
        //System.out.println("num_of_children of test_alpha =  " + test_alpha.root.parent.num_of_children);
        //test_split = test_child.copyElements(2,3);
        //System.out.println("num_of_children of test_child =  " + test_child.num_of_children);
        //System.out.println("num_of_children of test_split =  " + test_split.num_of_children);

        // test.setParent(test_parent);
        //test_alpha.root.addChild(test_child);
        //test_alpha.root.addChild(test_child2);
        //BTreeNode <String,String> alpha = new BTreeNode(4);
        //BTreeNode <String,String> beta = new BTreeNode(4);
        //BTree <String,String> alpha_tree = new BTree(4);
        //BTree <String,String> beta_tree = new BTree(4);
       // BTreeNode <String,String> beta_beta = new BTreeNode(4);
        //alpha_tree = test_alpha.subtree(0);
        //beta_tree = test_alpha.subtree(1);
        //alpha = alpha_tree.root;
        //beta = beta_tree.root;

        // System.out.println("The number of children of the root are " + test_alpha.root.num_of_children);
        // // int a = alpha.pos;
        // // System.out.println(a);children_set[0]
        // // //beta_beta = beta.siblingSet()[0];


        // System.out.println("The left child is with pos " + test_child.pos );
        // for(int i=0;i<=(test_child.pos);i++){
        //     //System.out.println("Entered the alpha loop");
        //     System.out.println(test_child.arr[i].getKey());
        //     //System.out.println("the value is " + test_alpha.root.children_set[0].arr[i].getValue());
        // }
        // System.out.println(test_child.deleteElement(15));
        // System.out.println("The left child is with pos " + test_child.pos );
        // for(int i=0;i<=(test_child.pos);i++){
        //     //System.out.println("Entered the alpha loop");
        //     System.out.println(test_child.arr[i].getKey());
        //     //System.out.println("the value is " + test_alpha.root.children_set[0].arr[i].getValue());
        // }
        // System.out.println("The root is with pos " + test_alpha.root.pos);
        // for(int i=0; i<=test_alpha.root.pos;i++){
        //     System.out.println(test_alpha.root.arr[i].getKey());
        //     //System.out.println("the value is " + test_alpha.root.arr[i].getValue());
        // }
        // System.out.println("The middle child is " + test_alpha.root.children_set[1].children_set[1].pos);
        // for(int i= 0; i <= (test_alpha.root.children_set[1].children_set[1].pos);i++){
        //     System.out.println(test_alpha.root.children_set[1].children_set[1].arr[i].getKey());
            //System.out.println("the value is " + test_alpha.root.children_set[1].arr[i].getValue());
        // }
        // System.out.println("The right child is " );
        // for(int i= 0; i <= (test_alpha.root.children_set[4].pos);i++){
        //     System.out.println("the key is " + test_alpha.root.children_set[4].arr[i].getKey());
        //     System.out.println("the value is " + test_alpha.root.children_set[4
        //         ].arr[i].getValue());
        // }
        // System.out.println("The siblings are ");
        // for(int i=0; i<test.num_of_children ;i++){
        //     for(int j=0; j<=test.children_set[i].pos;j++){
        //         System.out.println("the key is " + test.children_set[i].arr[j].getKey());
        //         System.out.println("the value is " + test.children_set[i].arr[j].getValue());

        //     }
        // }
        // Enumeration e = test_alpha.search(4).elements();

        // while (e.hasMoreElements()) {         
        //     System.out.println("Number = " + e.nextElement());
        // }       

//         for(int i = 0; i < test_alpha.search(4).size(); i++) {
//             System.out.println(test_alpha.search(4).get(i));
//         }

//         boolean b = test_alpha.isEmpty();
//         System.out.println("The result of isEmpty is " + b );
//         int a = test_alpha.size();
//         System.out.println("The result of size is " + a);
//         int c = test_alpha.height();
//         System.out.println("The result of height is " + c);
//         // int a = test_alpha.subtree(0);
//         // int b = test_alpha.subtree(1)
//         // System.out.println("The size of first subtree is " + a + "The size of the second subtree is " + b );

//         System.out.println("Working");


   //}
}
