package com.weiyuanstudio.swing.spy;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FloatingWindow extends JWindow {
    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    private static final Font FONT = new Font("Microsoft YaHei", Font.PLAIN, 20);
    private static final LayoutManager LAYOUT = new BorderLayout();
    public static final Color BG_COLOR = new Color(255, 255, 255, 204);

    private final Timer autoCloseTimer;

    public FloatingWindow(String text, Point location, String clipboardText) {
        // 1. 基础窗口设置
        setLayout(LAYOUT);
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(FONT);
        label.setBorder(EMPTY_BORDER);
        add(label);
        pack();
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        // 2. 半透明效果
        setBackground(BG_COLOR);

        // 3. 鼠标事件监听
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    dispose(); // 左键关闭
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    // 右键复制文本
                    StringSelection selection = new StringSelection(clipboardText);
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (autoCloseTimer.isRunning()) {
                    autoCloseTimer.stop();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (autoCloseTimer.isRunning()) {
                    autoCloseTimer.stop();
                }

                autoCloseTimer.start();
            }
        });

        // 4. 自动关闭计时器
        autoCloseTimer = new Timer(1000, e -> {
            dispose();
        });
        autoCloseTimer.setRepeats(false);
        autoCloseTimer.start();

        setLocation(location);
    }
}
