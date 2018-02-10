package ru.yaroslavskudarnov.BST.test;

import ru.yaroslavskudarnov.BST.main.AVLTree;

/**
 * User: Skudarnov Yaroslav
 * Date: 2/10/2018
 * Time: 2:46 PM
 */
public class TestAVLTree extends GeneralSearchTreesTest<AVLTree<Integer>> {
    @Override
    protected AVLTree<Integer> getTree() {
        return new AVLTree<>();
    }
}
