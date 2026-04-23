import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ShopGUI extends JFrame {

    private static final int CARD_DESC_MAX_LENGTH = 26;

    private ImageLabel lblMainImage;
    private JLabel lblMainTitle;
    private JLabel lblMainPrice;
    private JLabel lblMainBrand;
    private JTextArea lblMainDesc;

    private List<Product> productList;
    private JPanel selectedCard;

    public ShopGUI() {
        setTitle("Product Store");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 0));
        getContentPane().setBackground(Color.WHITE);

        initData();
        setupLeftPanel();
        setupRightPanel();

        if (!productList.isEmpty()) {
            Product firstProduct = null;
            for (Product product : productList) {
                firstProduct = product;
                break;
            }
            if (firstProduct != null) {
                updateLeftPanel(firstProduct);
            }
        }
    }

    private void setupLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(360, 0));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        lblMainImage = new ImageLabel();
        lblMainImage.setPreferredSize(new Dimension(300, 230));
        lblMainImage.setMaximumSize(new Dimension(300, 230));
        lblMainImage.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMainImage.setHorizontalAlignment(SwingConstants.CENTER);

        JSeparator separator = new JSeparator();
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(new Color(215, 220, 228));
        
        lblMainTitle = new JLabel("Tên Sản Phẩm");
        lblMainTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblMainTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblMainPrice = new JLabel("$0.00");
        lblMainPrice.setFont(new Font("Arial", Font.BOLD, 20));
        lblMainPrice.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblMainBrand = new JLabel("Thương hiệu");
        lblMainBrand.setForeground(Color.GRAY);
        lblMainBrand.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblMainDesc = new JTextArea("Mô tả...");
        lblMainDesc.setFont(new Font("Arial", Font.PLAIN, 14));
        lblMainDesc.setForeground(Color.DARK_GRAY);
        lblMainDesc.setOpaque(false);
        lblMainDesc.setEditable(false);
        lblMainDesc.setFocusable(false);
        lblMainDesc.setLineWrap(true);
        lblMainDesc.setWrapStyleWord(true);
        lblMainDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMainDesc.setBorder(null);
        lblMainDesc.setMaximumSize(new Dimension(300, 120));

        leftPanel.add(lblMainImage);
        leftPanel.add(Box.createVerticalStrut(18));
        leftPanel.add(separator);
        leftPanel.add(Box.createVerticalStrut(18));
        leftPanel.add(lblMainTitle);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(lblMainPrice);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(lblMainBrand);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(lblMainDesc);

        add(leftPanel, BorderLayout.WEST);
    }

    private void setupRightPanel() {
        JPanel rightGrid = new JPanel(new GridLayout(0, 4, 12, 12));
        rightGrid.setBackground(Color.WHITE);
        rightGrid.setBorder(new EmptyBorder(30, 0, 30, 30));

        JPanel firstCard = null;
        for (Product p : productList) {
            JPanel card = createProductCard(p);
            rightGrid.add(card);
            if (firstCard == null) {
                firstCard = card;
            }
        }

        if (firstCard != null) {
            selectedCard = firstCard;
            applyCardAppearance(selectedCard, true, false);
        }

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.add(rightGrid, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setBorder(null); 
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createProductCard(Product p) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setOpaque(true);
        card.setPreferredSize(new Dimension(200, 240));
        applyCardAppearance(card, false, false);

        JPanel topText = new JPanel();
        topText.setLayout(new BoxLayout(topText, BoxLayout.Y_AXIS));
        topText.setOpaque(false);
        
        JLabel lblTitle = new JLabel(p.title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 15));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        topText.add(lblTitle);
        
        String shortDesc = shortenText(p.desc);
        JLabel lblDesc = new JLabel(shortDesc);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setForeground(Color.GRAY);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        topText.add(Box.createVerticalStrut(5));
        topText.add(lblDesc);
        
        card.add(topText, BorderLayout.NORTH);

        JLabel lblImg = new JLabel();
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);
        lblImg.setIcon(scaleImage(p.imagePath, 140, 120));
        card.add(lblImg, BorderLayout.CENTER);

        JPanel bottomText = new JPanel(new BorderLayout());
        bottomText.setOpaque(false);
        
        JLabel lblBrand = new JLabel(p.brand);
        lblBrand.setFont(new Font("Arial", Font.PLAIN, 12));
        lblBrand.setForeground(Color.GRAY);
        bottomText.add(lblBrand, BorderLayout.WEST);
        
        JLabel lblPrice = new JLabel(p.price);
        lblPrice.setFont(new Font("Arial", Font.BOLD, 16));
        bottomText.add(lblPrice, BorderLayout.EAST);
        
        card.add(bottomText, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedCard != null && selectedCard != card) {
                    applyCardAppearance(selectedCard, false, false);
                }
                selectedCard = card;
                applyCardAppearance(card, true, false);
                updateLeftPanel(p);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                applyCardAppearance(card, card == selectedCard, true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                applyCardAppearance(card, card == selectedCard, false);
            }
        });

        return card;
    }

    private void updateLeftPanel(Product p) {
        lblMainTitle.setText(p.title);
        lblMainPrice.setText(p.price);
        lblMainBrand.setText(p.brand);
        lblMainDesc.setText(p.desc);
        lblMainDesc.setCaretPosition(0);
        lblMainImage.setImageWithFade(scaleImage(p.imagePath, 300, 300));
    }

    private ImageIcon scaleImage(String path, int width, int height) {
        try {
            String resolvedPath = resolveImagePath(path);
            File file = new File(resolvedPath);
            if (!file.exists()) {
                return createPlaceholderIcon(width, height);
            }
            Image img = new ImageIcon(resolvedPath).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return createPlaceholderIcon(width, height);
        }
    }

    private String resolveImagePath(String path) {
        File direct = new File(path);
        if (direct.exists()) {
            return direct.getPath();
        }

        String normalized = path.replace("/", File.separator);
        if (normalized.startsWith(".." + File.separator)) {
            File relativeToProject = new File(normalized.substring(3));
            if (relativeToProject.exists()) {
                return relativeToProject.getPath();
            }
        }

        File underSrc = new File("src" + File.separator + normalized);
        if (underSrc.exists()) {
            return underSrc.getPath();
        }

        return path;
    }

    private ImageIcon createPlaceholderIcon(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(242, 244, 247));
        g2.fillRoundRect(0, 0, width - 1, height - 1, 18, 18);
        g2.setColor(new Color(198, 203, 210));
        g2.drawRoundRect(0, 0, width - 1, height - 1, 18, 18);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        String text = "No Image";
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int x = (width - textWidth) / 2;
        int y = (height + fm.getAscent()) / 2 - 4;
        g2.drawString(text, Math.max(10, x), y);
        g2.dispose();
        return new ImageIcon(image);
    }

    private void applyCardAppearance(JPanel card, boolean selected, boolean hovered) {
        Color background = selected ? new Color(243, 247, 255) : hovered ? new Color(239, 239, 239) : new Color(248, 249, 250);
        Color borderColor = selected ? new Color(75, 126, 255) : new Color(220, 220, 220);
        int borderThickness = selected ? 2 : 1;

        card.setBackground(background);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, borderThickness, true),
                new EmptyBorder(15, 15, 15, 15)));
    }

    private String shortenText(String text) {
        if (text == null) {
            return "";
        }
        return text.length() > CARD_DESC_MAX_LENGTH
                ? text.substring(0, Math.max(0, CARD_DESC_MAX_LENGTH - 3)) + "..."
                : text;
    }

    private void initData() {
        productList = new ArrayList<>();
        productList.add(new Product("4DFWD PULSE SHOES", "$160.00", "Adidas", "This product is excluded from all promotional discounts and offers.", "../images/img1.png"));
        productList.add(new Product("FORUM MID SHOES", "$100.00", "Adidas", "This product is excluded from all promotional discounts and offers.", "../images/img2.png"));
        productList.add(new Product("SUPERNOVA SHOES", "$150.00", "Adidas", "NMD City Stock 2", "../images/img3.png"));
        productList.add(new Product("Adidas", "$160.00", "Adidas", "NMD City Stock 2", "../images/img4.png"));
        productList.add(new Product("Adidas", "$120.00", "Adidas", "NMD City Stock 2", "../images/img5.png"));
        productList.add(new Product("4DFWD PULSE SHOES", "$160.00", "Adidas", "This product is excluded from all promotional discounts and offers.", "../images/img6.png"));
        productList.add(new Product("4DFWD PULSE SHOES", "$160.00", "Adidas", "This product is excluded from all promotional discounts and offers.", "../images/img1.png"));
        productList.add(new Product("FORUM MID SHOES", "$100.00", "Adidas", "This product is excluded from all promotional discounts and offers.", "../images/img2.png"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ShopGUI().setVisible(true));
    }
}