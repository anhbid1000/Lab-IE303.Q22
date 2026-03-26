public class Bai2 {

    public static double tinhPi(double dienTich, double banKinh) {
        double pi = dienTich / Math.pow(banKinh,2);

        return pi;
    }

    public static void main(String[] args) {
        int tongDiem = 1000000;
        double banKinh =1.0;

        double dienTich = Bai1.dienTichHinhTron(banKinh, tongDiem);
        double pi = tinhPi(dienTich, banKinh);

        System.out.println("PI: " + pi);
    }
}