import javax.swing.*;
import java.awt.*;
import java.util.Random;

long count = 0;
long size = 0;
int batch = 100000;
long startTime = System.currentTimeMillis();
long endTime = startTime;

public class sorts extends JPanel {
    int[] array;
    public sorts(int size) {
        array = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(batch);
        }
        setPreferredSize(new Dimension(600, 20));
    }
    public void sort(String algorithm) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        switch (algorithm) {
            case "selection":
                for (int i = 0; i < array.length - 1; i++) {
                    int min = i;
                    for (int j = i + 1; j < array.length; j++) {
                        if (array[j] < array[min]) {
                            min = j;
                        }
                        count++;
                    }
                    int temp = array[i];
                    array[i] = array[min];
                    array[min] = temp;
                }
                break;
            case "insertion":
                int[] sorted = new int[array.length];
                for (int i = 0; i < array.length; i++) {
                    size++;
                    int index = array[i];
                    int j = i - 1;
                    while (j >= 0 && array[j] > index) {
                        array[j + 1] = array[j];
                        j = j - 1;
                        count++;
                    }
                    array[j + 1] = index;
                }
                break;
            default:
                for (int i = 0; i < array.length - 1; i++) {
                    for (int j = 0; j < array.length - i - 1; j++) {
                        if (array[j] > array[j + 1]) {
                            int temp = array[j];
                            array[j] = array[j + 1];
                            array[j + 1] = temp;
                            count++;
                        }
                    }
                }
        }
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Algorithm: " + algorithm + ", Duration: " + duration + "ms, Steps: " + count + ", Size: " + size);
    }

    public void reset() {
        count = 0;
        size = 0;

        Random rand = new Random();

        array = new int[batch];

        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextInt(batch);
        }
    }
}
public void main(String[] args) {
    String[] algs = {"bubble", "selection", "insertion"};

    sorts sorter = new sorts(batch);

    JFrame frame = new JFrame();
    JComboBox < String > algorithms = new JComboBox < > (algs);
    JButton startB = new JButton("Start Sort");
    JButton batchB = new JButton("Find Best Size");
    JPanel controls = new JPanel();
    JLabel report = new JLabel("Report will show here");
    JLabel size = new JLabel("...");

    controls.add(report);
    controls.add(algorithms);
    controls.add(startB);
    controls.add(batchB);
    controls.add(size);

    frame.setLayout(new BorderLayout());
    frame.add(sorter, BorderLayout.CENTER);
    frame.add(controls, BorderLayout.SOUTH);
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    startB.addActionListener(e -> {
        sorter.reset();
        String selected = (String) algorithms.getSelectedItem();new Thread(() -> {
            try {
                long startTime = System.currentTimeMillis();
                sorter.sort(selected);
                long duration = System.currentTimeMillis() - startTime;
                SwingUtilities.invokeLater(() -> {
                    report.setText(
                            "Alg: " + selected +
                                    " | Moves: " + count +
                                    " | Time: " + duration + ("ms")
                    );
                });
            } catch (InterruptedException ee) {
                ee.printStackTrace();
            }
        }).start();
    });
    batchB.addActionListener(e -> {
        try {
            for (int i = 1; endTime - startTime < 1000; i++) {
                startTime = System.currentTimeMillis();
                batch = 10000 * i;

                sorts benchmark = new sorts(batch);

                benchmark.reset();
                benchmark.sort("insertion");
                endTime = System.currentTimeMillis();
                System.out.println(endTime - startTime + "ms");

                size.setText(String.valueOf(batch));
            }
        } catch (InterruptedException ee) {
            ee.printStackTrace();
        }
    });
}