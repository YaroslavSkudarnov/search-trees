package ru.yaroslavskudarnov.BST.test.classic;

import ru.yaroslavskudarnov.BST.classic.SimpleBinarySearchTree;
import ru.yaroslavskudarnov.BST.test.GeneralSearchTreesTest;

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
