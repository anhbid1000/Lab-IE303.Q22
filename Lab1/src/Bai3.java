// Giải theo phương pháp Graham Scan
import java.util.*;

public class Bai3 {
    static final class Diem {
        final int x;
        final int y;

        Diem(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Diem)) return false;
            Diem diem = (Diem) o;
            return x == diem.x && y == diem.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    // Hàm tính tích có hướng (cross product) của OA x OB.
    // > 0: quay trái, < 0: quay phải, = 0: thẳng hàng
    private static long tichCoHuong(Diem o, Diem a, Diem b) {
        return (long) (a.x - o.x) * (b.y - o.y) - (long) (a.y - o.y) * (b.x - o.x);
    }

    // Khoảng cách bình phương (đủ để so sánh, không cần căn bậc hai)
    private static long khoangCachBinhPhuong(Diem a, Diem b) {
        long dx = (long) a.x - b.x;
        long dy = (long) a.y - b.y;
        return dx * dx + dy * dy;
    }

    // Tìm điểm trụ: y nhỏ nhất, nếu hoà thì x nhỏ nhất
    private static Diem timDiemTru(List<Diem> cacDiem) {
        Diem diemTru = cacDiem.get(0);
        for (Diem p : cacDiem) {
            if (p.y < diemTru.y || (p.y == diemTru.y && p.x < diemTru.x)) {
                diemTru = p;
            }
        }
        return diemTru;
    }

    // Trả về các điểm thuộc bao lồi theo thứ tự ngược chiều kim đồng hồ.
    // Lưu ý: các điểm thẳng hàng trên cạnh sẽ được loại bớt (chỉ giữ điểm ngoài cùng).
    private static List<Diem> grahamScan(List<Diem> cacDiem) {
        if (cacDiem == null || cacDiem.isEmpty()) {
            return Collections.emptyList();
        }

        // Loại điểm trùng để tránh lỗi và cho kết quả ổn định
        List<Diem> ds = new ArrayList<>(new LinkedHashSet<>(cacDiem));
        if (ds.size() <= 2) {
            return new ArrayList<>(ds);
        }

        Diem diemTru = timDiemTru(ds);

        // Sắp xếp theo góc so với điểm trụ; nếu thẳng hàng thì điểm gần hơn đứng trước
        // để khi build hull ta sẽ loại bớt điểm ở giữa và giữ điểm ngoài cùng.
        ds.sort((a, b) -> {
            if (a.equals(diemTru)) return -1;
            if (b.equals(diemTru)) return 1;

            long c = tichCoHuong(diemTru, a, b);
            if (c == 0) {
                return Long.compare(khoangCachBinhPhuong(diemTru, a), khoangCachBinhPhuong(diemTru, b));
            }
            return c > 0 ? -1 : 1;
        });

        Deque<Diem> nganXep = new ArrayDeque<>();
        nganXep.push(ds.get(0));
        nganXep.push(ds.get(1));

        for (int i = 2; i < ds.size(); i++) {
            Diem top = nganXep.pop();

            while (!nganXep.isEmpty() && tichCoHuong(nganXep.peek(), top, ds.get(i)) <= 0) {
                top = nganXep.pop();
            }

            nganXep.push(top);
            nganXep.push(ds.get(i));
        }

        // Đổi từ stack (LIFO) sang list theo thứ tự duyệt (đảo lại)
        List<Diem> baoLoi = new ArrayList<>(nganXep);
        Collections.reverse(baoLoi);

        // Trường hợp tất cả điểm thẳng hàng: kết quả ở trên có thể chỉ còn 2 điểm.
        // Đây là hành vi mong muốn cho "bao lồi" (đoạn thẳng ngoài cùng).
        return baoLoi;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        List<Diem> cacDiem = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            cacDiem.add(new Diem(sc.nextInt(), sc.nextInt()));
        }

        List<Diem> ketQua = grahamScan(cacDiem);

        for (Diem p : ketQua) {
            System.out.println(p.x + " " + p.y);
        }

        sc.close();
    }
}
