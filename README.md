# BTree
In this code we implement the BTree with one deviation from the standard property.

## Overview 

This code is a simple implementation of a general M-way BTree with one deviation, duplicate keys are allowed i.e. there can be two keys "4" and "4" with values that may be different or same. This causes some difficulties in insertion, deletion and search. There are two methods to deal with this problem of duplicates. 

In the first one, for the given key k which we want to insert, we begin at the root and search for the key m which is greater than or equal to k. Once we have m, we recursively call the function on the child associated with the key before m. Hence even if the key k and m are same, k will be inserted in the child between m and m-1. 

In the second method, we search for m in the similar way. But if m is equal to key k, we continue our search for the next greater key. Also we maintain an array where we store the number of keys associated with the child of each key encountered as we go from m to the next greater key. For eg if the node of tree contains "m-2, m-1, m, m, m, m, m+1". Then when we reach the key m we start putting the value of the number of keys in every child associated with every m until we reach m+1. We chose the m which has the least number of keys in it's child and recursively call the function on that child. In the above case if the number of keys in every child are "2, 3, 5, 4, 7, 0, 8" then we chose the child with 0 keys and recursively call insert function on that child. 

Now in the first case the time complexity for search and delete operations would be O(log(n)), while in the second case the time complexity would be O(log(n) + log(k)). Consider a degeneratecase where all the keys are same, or most of the keys are same. In that case we might have a linked list instead of a tree, which might end up using a lot of space. We give space a more preference and hence employ the first method. 
 
## How to run
Compile using `javac BTree.java`

Run using `java BTree.java`

## Author 

+ [Vipul Khatana](https://github.com/vipul-khatana)

Course Project under [**Prof Mausam**](http://www.cse.iitd.ac.in/~mausam/)

## Contributing 

1) Fork it (https://github.com/vipul-khatana/Solving-Puzzle-with-Dijkstras-Algorithm/fork)
2) Create your feature branch git checkout -b feature/fooBar
3) Commit your changes git commit -am 'Add some fooBar'
4) Push to the branch git push origin feature/fooBar
5) Create a new pull request

