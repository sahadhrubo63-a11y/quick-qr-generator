import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class QuickQRGenerator extends JFrame {

    private final JTextField inputField;
    private final JLabel qrImageLabel;

    public QuickQRGenerator() {
        setTitle("QuickQR Generator");
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        JLabel titleLabel = new JLabel("QuickQR Generator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        inputField = new JTextField("https://google.com");
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton generateButton = new JButton("Generate");
        generateButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        generateButton.setFocusPainted(false);

        JPanel inputGroup = new JPanel(new BorderLayout(5, 5));
        inputGroup.add(inputField, BorderLayout.CENTER);
        inputGroup.add(generateButton, BorderLayout.EAST);

        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(inputGroup, BorderLayout.SOUTH);

        qrImageLabel = new JLabel("", SwingConstants.CENTER);
        qrImageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));
        centerPanel.add(qrImageLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        generateButton.addActionListener(e -> generateQR());
        inputField.addActionListener(e -> generateQR());

        generateQR();
    }

    private void generateQR() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter text or a URL.", "Input Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            BufferedImage qrImage = createZXingQRCode(text, 320, 320);
            qrImageLabel.setIcon(new ImageIcon(qrImage));
            qrImageLabel.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generating QR Code: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private BufferedImage createZXingQRCode(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new QuickQRGenerator().setVisible(true);
        });
    }
}
