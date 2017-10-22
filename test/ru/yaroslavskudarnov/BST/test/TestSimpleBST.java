package ru.yaroslavskudarnov.BST.test;

import org.junit.Assert;
import org.junit.Test;
import ru.yaroslavskudarnov.BST.main.BinarySearchTree;
import ru.yaroslavskudarnov.BST.main.SimpleBinarySearchTree;

import java.util.List;

/**
 * User: Skudarnov Yaroslav
 * Date: 10/8/2017
 * Time: 1:24 PM
 */
public class TestSimpleBST {
    @Test
    public void smallTestAddContainsAndRemove() {
        BinarySearchTree<Integer> tree = new SimpleBinarySearchTree<>();

        List<Integer> tmpList1 = List.of(1, 12, 3, -4, 5, 0, 6, 17, 8, -9, 10, 0);
        tree.add(1); tree.add(3); tree.add(5);
        Assert.assertTrue(tree.addAll(tmpList1));
        Assert.assertFalse(tree.addAll(tmpList1));
        Assert.assertTrue(tree.containsAll(tmpList1));
        Assert.assertFalse(tree.contains(-1)); Assert.assertFalse(tree.contains(2)); Assert.assertFalse(tree.contains(7));

        List<Integer> tmpList2 = List.of(1, 3, 5);
        tree.retainAll(tmpList2);
        Assert.assertTrue(tree.containsAll(tmpList2));
        Assert.assertFalse(tree.contains(0)); Assert.assertFalse(tree.contains(2)); Assert.assertFalse(tree.contains(10)); Assert.assertFalse(tree.contains(-9));

        tree.removeAll(tmpList2);
        Assert.assertFalse(tree.contains(1)); Assert.assertFalse(tree.contains(3)); Assert.assertFalse(tree.contains(5));
    }
}
