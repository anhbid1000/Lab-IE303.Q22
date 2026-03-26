import java.util.Scanner;


public class Bai1 {
    public static double dienTichHinhTron(double banKinh, int tongDiem) {
        int tongDiemTrong = 0;

        for (int i = 0; i < tongDiem; i++) {
            double x = Math.random() * (2 * banKinh) - banKinh;
            double y = Math.random() * (2 * banKinh) - banKinh;

            if ((x * x + y * y) <= (banKinh * banKinh)) {
                tongDiemTrong++;
            }
        }

        // Tinhs tỉ lệ điểm nằm trong hình tròn so với tổng số điểm
        double tiLe = (double) tongDiemTrong / tongDiem;
        double dienTich = tiLe * 4 * banKinh * banKinh;

        return dienTich;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int tongDiem = 1000000;
        double banKinh = sc.nextDouble();

        double dienTich = dienTichHinhTron(banKinh, tongDiem);


        System.out.println("Diện tích hình tròn xấp xỉ: " + dienTich);
        sc.close();
    }
}