import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/** Demo de notificaciones: Toast (estético) + Snackbar (con color). */
public class cosas extends JFrame {
    enum Type { INFO, SUCCESS, WARN, ERROR }

    public cosas() {
        super("Demo: Toast + Snackbar");

        // Intentar FlatLaf si está disponible
        try {
            LookAndFeel laf = (LookAndFeel) Class.forName("com.formdev.flatlaf.FlatLightLaf")
                    .getDeclaredConstructor().newInstance();
            UIManager.setLookAndFeel(laf);
        } catch (Throwable ignore) {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
        }
        SwingUtilities.updateComponentTreeUI(this);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridLayout(0, 2, 12, 12));
        p.setBorder(new EmptyBorder(16,16,16,16));

        // TOASTS
        p.add(btn("Toast INFO", e -> Toast.show(this, "Información general", Type.INFO)));
        p.add(btn("Toast SUCCESS", e -> Toast.show(this, "Documento creado correctamente", Type.SUCCESS)));
        p.add(btn("Toast WARN", e -> Toast.show(this, "El tamaño está cerca del límite", Type.WARN)));
        p.add(btn("Toast ERROR", e -> Toast.show(this, "No se pudo guardar el archivo", Type.ERROR)));

        // SNACKBARS
        p.add(btn("Snackbar INFO", e -> Snackbar.show(this, "Cambios disponibles", Type.INFO)));
        p.add(btn("Snackbar SUCCESS", e -> Snackbar.show(this, "Cambios guardados", Type.SUCCESS)));
        p.add(btn("Snackbar WARN", e -> Snackbar.show(this, "Revisa los campos obligatorios", Type.WARN)));
        p.add(btn("Snackbar ERROR", e -> Snackbar.show(this, "Permiso denegado", Type.ERROR)));

        add(p, BorderLayout.CENTER);
        setSize(720, 420);
        setLocationRelativeTo(null);
    }

    private JButton btn(String txt, java.util.function.Consumer<ActionEvent> onClick) {
        return new JButton(new AbstractAction(txt) {
            @Override public void actionPerformed(ActionEvent e) { onClick.accept(e); }
        });
    }

    /* =======================  TOAST  ======================= */
    static final class Toast {
        public static void show(JFrame owner, String text, Type type) {
            final JWindow w = new JWindow(owner);
            JPanel card = buildCard(text, type);
            w.setContentPane(card);
            w.pack();

            // Esquina inferior derecha del frame
            Point p = owner.getLocationOnScreen();
            int x = p.x + owner.getWidth() - w.getWidth() - 20;
            int y = p.y + owner.getHeight() - w.getHeight() - 20;
            w.setLocation(x, y);
            w.setAlwaysOnTop(true);

            // Soporte de opacidad (fade)
            boolean tmp;
            try { w.setOpacity(0f); tmp = true; } catch (UnsupportedOperationException ex) { tmp = false; }
            final boolean opacitySupported = tmp;

            w.setVisible(true);
            if (opacitySupported) animateOpacity(w, 0f, 1f, 160);

            // Auto-cierre 2.6 s
            Timer t = new Timer(2600, new AbstractAction() {
                @Override public void actionPerformed(ActionEvent e) {
                    if (opacitySupported) animateOpacity(w, 1f, 0f, 160);
                    w.setVisible(false);
                    w.dispose();
                }
            });
            t.setRepeats(false);
            t.start();
        }

        private static JPanel buildCard(String text, Type type) {
            Color accent = accentColor(type);
            Color bgTop = new Color(38, 38, 38);
            Color bgBottom = new Color(28, 28, 28);
            Color fg = Color.WHITE;

            // Icono (fallback usando OptionPane)
            Icon icon;
            switch (type) {
                case SUCCESS: icon = makeCheckIcon(new Color(0x2ECC71)); break;
                case WARN:    icon = UIManager.getIcon("OptionPane.warningIcon"); break;
                case ERROR:   icon = UIManager.getIcon("OptionPane.errorIcon"); break;
                default:      icon = UIManager.getIcon("OptionPane.informationIcon"); break;
            }

            JLabel lbl = new JLabel(text, icon, SwingConstants.LEFT);
            lbl.setForeground(fg);

            JPanel card = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Sombra suave “fake blur”
                    for (int i=6;i>=1;i--) {
                        g2.setColor(new Color(0,0,0, 12 - i*2));
                        g2.fillRoundRect(i, i, getWidth()-i, getHeight()-i, 18, 18);
                    }

                    // Fondo con leve gradiente
                    GradientPaint gp = new GradientPaint(0,0,bgTop, 0,getHeight(),bgBottom);
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, getWidth()-5, getHeight()-5, 18, 18);

                    // Borde sutil
                    g2.setColor(new Color(255,255,255,30));
                    g2.drawRoundRect(0, 0, getWidth()-5, getHeight()-5, 18, 18);

                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            card.setOpaque(false);
            card.setBorder(new EmptyBorder(10, 14, 10, 16));
            card.setLayout(new BorderLayout(10,0));

            // Franja lateral de acento
            JPanel left = new JPanel();
            left.setBackground(accent);
            left.setPreferredSize(new Dimension(6, 1));
            left.setOpaque(true);

            JPanel content = new JPanel(new BorderLayout());
            content.setOpaque(false);
            content.add(lbl, BorderLayout.CENTER);

            card.add(left, BorderLayout.WEST);
            card.add(content, BorderLayout.CENTER);
            return card;
        }

        private static Icon makeCheckIcon(Color color) {
            // Icono simple de check circular (vectorial)
            return new Icon() {
                final int sz = 18;
                @Override public void paintIcon(Component c, Graphics g, int x, int y) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 60));
                    g2.fillOval(x, y, sz, sz);
                    g2.setColor(color);
                    g2.drawOval(x, y, sz, sz);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawLine(x+5, y+10, x+8, y+13);
                    g2.drawLine(x+8, y+13, x+13, y+6);
                    g2.dispose();
                }
                @Override public int getIconWidth() { return sz; }
                @Override public int getIconHeight() { return sz; }
            };
        }

        private static Color accentColor(Type t) {
            switch (t) {
                case SUCCESS: return new Color(0x2ECC71);
                case WARN:    return new Color(0xF1C40F);
                case ERROR:   return new Color(0xE74C3C);
                default:      return new Color(0x3498DB);
            }
        }

        private static void animateOpacity(Window w, float from, float to, int ms) {
            try {
                int frames = Math.max(1, ms/15);
                final int[] count = {0};
                final float step = (to - from) / frames;
                try { w.setOpacity(from); } catch (UnsupportedOperationException ignore) { return; }
                new Timer(15, new AbstractAction() {
                    @Override public void actionPerformed(ActionEvent e) {
                        try { w.setOpacity(Math.max(0f, Math.min(1f, from + step*(++count[0])))); }
                        catch (UnsupportedOperationException ignore2) {}
                        if (count[0] >= frames) ((Timer)e.getSource()).stop();
                    }
                }).start();
            } catch (Exception ignore) { /* sin fade si no se puede */ }
        }
    }

    /* =======================  SNACKBAR  ======================= */
    static final class Snackbar {
        public static void show(JFrame frame, String text, Type type) {
            JComponent glass = (JComponent) frame.getGlassPane();
            glass.setVisible(true);
            glass.setLayout(new GridBagLayout());

            JPanel bar = buildBar(text, type);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; gbc.gridy = 0;
            gbc.weightx = 1; gbc.weighty = 1;
            gbc.anchor = GridBagConstraints.SOUTH;  // centrado abajo
            gbc.insets = new Insets(0, 0, 18, 0);

            glass.removeAll();
            glass.add(bar, gbc);
            glass.revalidate(); glass.repaint();

            Timer t = new Timer(2600, new AbstractAction() {
                @Override public void actionPerformed(ActionEvent e) {
                    glass.setVisible(false);
                    glass.removeAll();
                }
            });
            t.setRepeats(false);
            t.start();
        }

        private static JPanel buildBar(String text, Type type) {
            Color bg, accent, fg = Color.WHITE;
            switch (type) {
                case SUCCESS: bg = new Color(39, 174, 96);  accent = new Color(30,130,72); break;
                case WARN:    bg = new Color(243, 156, 18); accent = new Color(200,120,15); break;
                case ERROR:   bg = new Color(231, 76, 60);  accent = new Color(192,57,43); break;
                default:      bg = new Color(52, 152, 219); accent = new Color(41,128,185); break;
            }

            JLabel lbl = new JLabel(text);
            lbl.setForeground(fg);

            JPanel bar = new JPanel(new BorderLayout());
            bar.setOpaque(false);
            bar.setBorder(new EmptyBorder(0, 0, 0, 0));

            JPanel card = new JPanel(new BorderLayout()) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    // sombra
                    g2.setColor(new Color(0,0,0,60));
                    g2.fillRoundRect(3, 3, getWidth()-3, getHeight()-3, 10, 10);
                    // cuerpo
                    g2.setColor(bg);
                    g2.fillRoundRect(0, 0, getWidth()-6, getHeight()-6, 10, 10);
                    // franja superior (accent)
                    g2.setColor(accent);
                    g2.fillRoundRect(0, 0, getWidth()-6, 6, 10, 10);
                    g2.dispose();
                }
            };
            card.setOpaque(false);
            card.setBorder(new EmptyBorder(12,16,12,16));
            card.add(lbl, BorderLayout.CENTER);

            // Botón cerrar (opcional)
            JButton close = new JButton("\u2715"); // ×
            close.setForeground(fg);
            close.setOpaque(false);
            close.setContentAreaFilled(false);
            close.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
            close.addActionListener(e -> SwingUtilities.getWindowAncestor(bar).setVisible(false));
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));
            right.setOpaque(false);
            right.add(close);
            card.add(right, BorderLayout.EAST);

            bar.add(card, BorderLayout.CENTER);
            return bar;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new cosas().setVisible(true));
    }
}
