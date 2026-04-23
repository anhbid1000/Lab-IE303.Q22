import java.awt.*;
import javax.swing.*;

public class ImageLabel extends JLabel {
    private float alpha = 1f;
    private Timer timer;
    private ImageIcon nextIcon;

    public void setImageWithFade(ImageIcon icon) {
        this.nextIcon = icon;
        alpha = 0f; 
        if (timer != null && timer.isRunning()) timer.stop();

        timer = new Timer(20, e -> {
            alpha += 0.05f;
            if (alpha >= 1f) {
                alpha = 1f;
                timer.stop();
            }
            repaint(); 
        });
        setIcon(nextIcon);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        super.paintComponent(g2d);
        g2d.dispose();
    }
}