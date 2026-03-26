import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Bai4 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int tongCanDat = sc.nextInt();

        int[] mang = new int[n];
        for (int i = 0; i < n; i++) {
            mang[i] = sc.nextInt();
        }

        System.out.println("Output:");
        System.out.println(timDayConDaiNhatCoTong(n, tongCanDat, mang));

        sc.close();
    }

    /*
     Tìm 1 dãy con (subsequence) dài nhất sao cho tổng các phần tử đúng bằng tongCanDat.
     Mỗi phần tử chỉ được chọn hoặc không chọn (giống bài toán subset sum), và vẫn giữ thứ tự tương đối.
     @return chuỗi các phần tử của dãy con tìm được (cách nhau bởi ", "), hoặc thông báo nếu không tồn tại.
     */
    public static String timDayConDaiNhatCoTong(int n, int tongCanDat, int[] mang) {
        if (n < 0 || mang == null || mang.length < n) {
            return "Dữ liệu không hợp lệ";
        }
        if (tongCanDat < 0) {
            return "Không có dãy con nào thỏa mãn";
        }

        // dp[i][j] = độ dài lớn nhất có thể đạt tổng j khi xét i phần tử đầu (mang[0..i-1]).
        // -1 nghĩa là không thể tạo được tổng j.
        int[][] dp = new int[n + 1][tongCanDat + 1];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= tongCanDat; j++) {
                dp[i][j] = -1;
            }
        }

        // Base: tổng = 0 thì luôn đạt được với độ dài 0
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 0;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= tongCanDat; j++) {
                // Không chọn mang[i-1]
                dp[i][j] = dp[i - 1][j];

                // Chọn mang[i-1] (nếu tạo được)
                int giaTri = mang[i - 1];
                if (giaTri >= 0 && j >= giaTri && dp[i - 1][j - giaTri] != -1) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - giaTri] + 1);
                }
            }
        }

        if (dp[n][tongCanDat] == -1) {
            return "Không có dãy con nào thỏa mãn";
        }

        // Truy vết để lấy các phần tử đã chọn
        List<Integer> dayCon = new ArrayList<>();
        int i = n;
        int j = tongCanDat;

        while (i > 0 && j > 0) {
            int giaTri = mang[i - 1];

            // Nếu chọn mang[i-1] mà vẫn khớp dp thì lấy
            if (giaTri >= 0 && j >= giaTri && dp[i - 1][j - giaTri] != -1
                    && dp[i][j] == dp[i - 1][j - giaTri] + 1) {
                dayCon.add(giaTri);
                j -= giaTri;
            }

            // Dù chọn hay không, ta luôn lùi i (quay lại trạng thái xét i-1 phần tử)
            i--;
        }

        // Vì truy vết từ cuối về đầu nên cần đảo lại để đúng thứ tự ban đầu
        Collections.reverse(dayCon);

        StringBuilder sb = new StringBuilder();
        for (int idx = 0; idx < dayCon.size(); idx++) {
            sb.append(dayCon.get(idx));
            if (idx < dayCon.size() - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }
}