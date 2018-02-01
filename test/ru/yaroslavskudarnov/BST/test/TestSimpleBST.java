package ru.yaroslavskudarnov.BST.test;

import org.junit.Assert;
import org.junit.Test;
import ru.yaroslavskudarnov.BST.main.SimpleBinarySearchTree;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Skudarnov Yaroslav
 * Date: 10/8/2017
 * Time: 1:24 PM
 */
public class TestSimpleBST {
    @Test
    public void smallTestAddContainsAndRemove() {
        SimpleBinarySearchTree<Integer> tree = new SimpleBinarySearchTree<>();

        List<Integer> tmpList1 = List.of(1, 12, 3, -4, 5, 0, 6, 17, 8, -9, 10, 0);
        Assert.assertTrue(tree.addAll(tmpList1));
        Assert.assertFalse(tree.add(1));
        Assert.assertFalse(tree.add(3));
        Assert.assertFalse(tree.add(5));
        Assert.assertTrue(tree.add(7));
        Assert.assertFalse(tree.addAll(tmpList1));
        Assert.assertTrue(tree.containsAll(tmpList1));
        Assert.assertFalse(tree.contains(-1)); Assert.assertFalse(tree.contains(2)); Assert.assertFalse(tree.contains(87));
        Assert.assertTrue(tree.contains(12)); Assert.assertTrue(tree.contains(-9)); Assert.assertTrue(tree.contains(7));

        List<Integer> tmpList2 = List.of(1, 3, 5);
        tree.retainAll(tmpList2);
        Assert.assertTrue(tree.containsAll(tmpList2));
        Assert.assertFalse(tree.contains(0)); Assert.assertFalse(tree.contains(2)); Assert.assertFalse(tree.contains(10)); Assert.assertFalse(tree.contains(-9));

        tree.removeAll(tmpList2);
        Assert.assertFalse(tree.contains(1)); Assert.assertFalse(tree.contains(3)); Assert.assertFalse(tree.contains(5));
    }

    @Test
    public void bigRandomTest() {
        for (int i = 0; i < 10; ++i) {
            List<Integer> firstList = new ArrayList<>(), secondList = new ArrayList<>();
            for (int j = 0; j < 100000; ++j) {
                firstList.add((int) (Math.random() * 200000)); secondList.add((int) (Math.random() * 200000));
            }
            List<Integer> thirdList = new ArrayList<>(firstList);

            SimpleBinarySearchTree<Integer> tree = new SimpleBinarySearchTree<>(); tree.addAll(firstList); tree.retainAll(secondList); firstList.retainAll(secondList);
            Assert.assertTrue(firstList.containsAll(tree)); Assert.assertTrue(tree.containsAll(firstList));

            tree = new SimpleBinarySearchTree<>(thirdList); tree.removeAll(secondList); thirdList.removeAll(secondList);
            Assert.assertTrue(thirdList.containsAll(tree)); Assert.assertTrue(tree.containsAll(thirdList));
        }
    }
}
