package ru.yaroslavskudarnov.BST.test;

import ru.yaroslavskudarnov.BST.main.SimpleBinarySearchTree;

/**
 * User: Skudarnov Yaroslav
 * Date: 2/10/2018
 * Time: 2:15 AM
 */
public class TestSimpleBST extends GeneralSearchTreesTest<SimpleBinarySearchTree<Integer>> {
    @Override
    protected SimpleBinarySearchTree<Integer> getTree() {
        return new SimpleBinarySearchTree<>();
    }
}
