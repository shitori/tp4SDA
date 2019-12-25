import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int level = 50;
        ArrayList<Integer> array = new ArrayList<Integer>();
        Analyzer time_avl = new Analyzer();
        Analyzer time_b = new Analyzer();
        long beforeB, beforeAVL, afterB, afterAVL;
        AVLTree avl = new AVLTree<Integer>();
        BTree b = new BTree<Integer>(level);
        Random rd = new Random(11500697);
        for (int i = 0; i < 1000000; i++) {
            boolean bool = rd.nextBoolean();
            if (!bool && array.size() != 0) {
                int value = array.remove(0);
                beforeAVL = System.nanoTime();
                avl.remove(value);
                afterAVL = System.nanoTime();
                beforeB = System.nanoTime();
                b.remove(value);
                afterB = System.nanoTime();
                time_avl.append(afterAVL - beforeAVL);
                time_b.append(afterB - beforeB);
            } else {
                int value = Math.abs(rd.nextInt());
                //int value = i;
                array.add(value);
                beforeAVL = System.nanoTime();
                avl.add(value);
                afterAVL = System.nanoTime();
                beforeB = System.nanoTime();
                b.add(value);
                afterB = System.nanoTime();
                time_avl.append(afterAVL - beforeAVL);
                time_b.append(afterB - beforeB);
            }

        }
        /*time_avl.save_values("avlTimeI_" + level + ".plot");
        time_b.save_values("bTimeI_" + level + ".plot");*/
        /*time_avl.save_values("avlTimeI.plot");
        time_b.save_values("bTimeI.plot");*/
        time_avl.save_values("avlTimeR_" + level + ".plot");
        time_b.save_values("bTimeR_" + level + ".plot");
    }
}
