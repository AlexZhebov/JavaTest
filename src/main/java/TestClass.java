public class TestClass {
    public static void main(String[] args) {
        // создаем массив 2х мерный 10х10
        int[][] twoArr = new int[10][10];

        // заполняем таблицей умножения
        for (int i = 0; i < 10; i++ ) {
            for (int j = 0; j < 10; j++) {
                twoArr[i][j] = i * j;
            }
        }

        // выводим таблицу умножения
        String linT = "";
        String Sp = " ";
        for (int i = 0; i < 10; i++ ) {
            if (i == 0) {
                linT = "  | ";
            } else {
                linT = i + " | ";
            }
            for (int j = 1; j < 10; j++) {
                Integer x;
                if (i == 0) {
                    x = j;
                } else {
                    x = twoArr[i][j];
                }
                if (x < 10) {Sp = " " + x.toString();} else { Sp = x.toString();}
                linT = linT + Sp + " ";
            }
            if (i == 0) {
                linT = linT + "\n------------------------------";
            }
            System.out.println(linT);
        }

    }
}