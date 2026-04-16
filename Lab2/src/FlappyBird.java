import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int chieuRong = 360;
    int chieuCao = 640;

    Image anhNenChinh, anhChuot, anhOngTren, anhOngDuoi;

    // Đặt vị trí mặc định của chim và kích thước chim
    int toaDoXChim = chieuRong / 8;
    int toaDoYChim = chieuCao / 2;
    int chieuRongChim = 34;
    int chieuCaoChim = 24;

    private class Chim {
        int x = toaDoXChim, y = toaDoYChim, width = chieuRongChim, height = chieuCaoChim;
        Image img;
        Chim(Image img) { this.img = img; }
    }

    int toaDoXOng = chieuRong;
    int toaDoYOng = 0;
    int chieuRongOng = 64;
    int chieuCaoOng = 512;

    private class Ong {
        int x = toaDoXOng, y = toaDoYOng, width = chieuRongOng, height = chieuCaoOng;
        Image img;
        boolean daDi = false; // Kiểm tra xem chim đã vượt qua hay chưa
        Ong(Image img) { this.img = img; }
    }

    Chim chim;
    int vanTocY = 0;
    int trucTruong = 1;

    ArrayList<Ong> danhSachOng;

    Timer vongLapGame;
    Timer thoiGianDatOng;
    boolean ketThucGame = false;
    double diem = 0;

    // Constructor của FlappyBird
    public FlappyBird() {
        setPreferredSize(new Dimension(chieuRong, chieuCao));
        setFocusable(true);
        addKeyListener(this);

        anhNenChinh = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        anhChuot = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        anhOngTren = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        anhOngDuoi = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        chim = new Chim(anhChuot);
        danhSachOng = new ArrayList<>();

        // Tạo ống mới sau 2 giây
        thoiGianDatOng = new Timer(2000, e -> datOng());
        thoiGianDatOng.start();

        // Tạo vòng lặp trò chơi
        vongLapGame = new Timer(1000/60, this);
        vongLapGame.start();

    }

    // Phương thức tạo ống ngẫu nhiên
    public void datOng() {
        // Định nghĩa vị trí Y của ống trên
        int toaDoYOngNgauNhien = (int) (toaDoYOng - (chieuCaoOng/4.0) - Math.random()*(chieuCaoOng/2.0));
        // Tạo không gian mở cho chuột (Mặc định là chieuCao* 0.3)
        int khongGianMo = (int) (chieuCao*0.3);

        Ong ongTren = new Ong(anhOngTren);
        ongTren.y = toaDoYOngNgauNhien;
        danhSachOng.add(ongTren);

        // Định nghĩa ống dưới
        Ong ongDuoi = new Ong(anhOngDuoi);
        ongDuoi.y = ongTren.y + chieuCaoOng + khongGianMo;
        danhSachOng.add(ongDuoi);
    }

    // Phương thức vẽ giao diện
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ve(g);
    }

    // Phương thức vẽ đồ họa
    public void ve(Graphics g) {
        // vẽ nền
        g.drawImage(anhNenChinh, 0, 0, chieuRong, chieuCao, null);

        // vẽ chim
        g.drawImage(chim.img, chim.x, chim.y, chim.width, chim.height, null);

        // vẽ ống
        for (Ong ong : danhSachOng) {
            g.drawImage(ong.img, ong.x, ong.y, ong.width, ong.height, null);
        }

        // vẽ điểm
        g.setColor(Color.red);
        g.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        if (ketThucGame) {
            g.drawString("Kết thúc với " + (int) diem, 10, 35);
            g.drawString("Nhấn 'R' để bắt đầu lại", 10, 70);
        } else {
            g.drawString(String.valueOf((int) diem), 10, 35);
        }
    }

    // Logic chuyển động của chim
    public void chuyenDong() {
        vanTocY += trucTruong;
        chim.y += vanTocY;
        chim.y = Math.max(chim.y, 0);

        // Di chuyển ống
        for (Ong ong : danhSachOng) {
            if (diem >= 10) {
                ong.x -= 6;
            } else {
                ong.x -= 4;
            }

            if (!ong.daDi && chim.x > ong.x + ong.width) {
                ong.daDi = true;
                diem += 0.5; // Mỗi ống tại vị trí Y + 0.5 điểm
            }

            if (chamTruoc(chim, ong)) {
                ketThucGame = true;
            }

        }

        // Kết thúc trò chơi khi chim rơi xuống đất
        if (chim.y > chieuCao) {
            ketThucGame = true;
        }
    }

    // Phương thức kiểm tra va chạm
    private boolean chamTruoc(Chim chim, Ong ong) {
        return chim.x < ong.x + ong.width &&
                chim.x + chim.width > ong.x &&
                chim.y < ong.y + ong.height &&
                chim.y + chim.height > ong.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        chuyenDong();
        repaint();
        if (ketThucGame) {
            vongLapGame.stop();
            thoiGianDatOng.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
            vanTocY = -9;
        }

        if (ketThucGame && e.getKeyCode() == KeyEvent.VK_R) {
            chim.y = toaDoYChim;
            vanTocY = 0;
            danhSachOng.clear();
            diem = 0;
            ketThucGame = false;
            vongLapGame.start();
            thoiGianDatOng.start();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame cua_so = new JFrame("Chim Bay Flappy");
        cua_so.setResizable(false);
        cua_so.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyBird tro_choi = new FlappyBird();
        cua_so.add(tro_choi);
        cua_so.pack();

        tro_choi.requestFocus();
        cua_so.setVisible(true);
    }
}










