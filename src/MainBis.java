
import java.util.ArrayList;
import java.util.Random;

public class MainBis {
    public static void main(String[] args) {
        ArrayList a = new ArrayList<Integer>();
        AVLTree avl = new AVLTree<Integer>();
        BinarySearchTree bst = new BinarySearchTree<Integer>();
        Random rd = new Random(11500697);
        for (int i = 0; i < 10; i++) {
            boolean bool = true;
            int value = Math.abs(rd.nextInt(100));
            a.add(value);
            avl.add(value);
            bst.add(value);
            System.out.println(a);
            System.out.println(avl);
            System.out.println(bst);
        }

    }
}
