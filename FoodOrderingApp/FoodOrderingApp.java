
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class FoodOrderingApp extends JFrame {

    private final Color BG = new Color(247, 248, 250);
    private final Color DARK = new Color(30, 33, 43);
    private final Color ORANGE = new Color(255, 112, 67);
    private final Color MUTED = new Color(130, 137, 150);

    private JPanel menuPanel, cartPanel, categoryPanel;
    private JTextField searchField;
    private JLabel totalLabel, countLabel;
    private String selectedCategory = "All";
    private final Map<String, Integer> cart = new LinkedHashMap<>();
    private final Map<String, Food> foods = new LinkedHashMap<>();
    private final List<JButton> categoryButtons = new ArrayList<>();

    static class Food {
        String name, category, emoji, description;
        int price;
        Food(String name, String category, String emoji,
             String description, int price) {
            this.name = name;
            this.category = category;
            this.emoji = emoji;
            this.description = description;
            this.price = price;
        }
    }

    public FoodOrderingApp() {
        setTitle("Crave - Food Ordering");
        setSize(1280, 820);
        setMinimumSize(new Dimension(1000, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadFoods();
        buildUI();
        renderMenu();
        renderCart();
    }

    private void loadFoods() {
        addFood("Classic Burger", "Burgers", "🍔",
                "Juicy patty, fresh lettuce & cheese", 179);
        addFood("Cheese Pizza", "Pizza", "🍕",
                "Loaded cheese with Italian herbs", 249);
        addFood("Crispy Fries", "Sides", "🍟",
                "Golden, crispy and perfectly salted", 99);
        addFood("Chicken Biryani", "Meals", "🍛",
                "Aromatic rice with rich spices", 229);
        addFood("Creamy Pasta", "Meals", "🍝",
                "Creamy sauce with Italian seasoning", 199);
        addFood("Fresh Sandwich", "Sides", "🥪",
                "Fresh veggies and signature sauce", 129);
        addFood("Chocolate Shake", "Drinks", "🥤",
                "Thick chocolate milkshake", 119);
        addFood("Choco Dessert", "Desserts", "🍰",
                "Sweet, rich and irresistible", 149);
        addFood("Tasty Tacos", "Meals", "🌮",
                "Crunchy tacos with fresh fillings", 189);
        addFood("Hot Coffee", "Drinks", "☕",
                "Freshly brewed aromatic coffee", 79);
        addFood("Donuts", "Desserts", "🍩",
                "Soft donuts with chocolate glaze", 89);
        addFood("Noodles", "Meals", "🍜",
                "Hot noodles tossed with vegetables", 159);
    }

    private void addFood(String n, String c, String e, String d, int p) {
        foods.put(n, new Food(n, c, e, d, p));
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        setContentPane(root);

        root.add(createSidebar(), BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout(0, 20));
        main.setBackground(BG);
        main.setBorder(new EmptyBorder(25, 28, 22, 22));
        root.add(main, BorderLayout.CENTER);

        main.add(createHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(22, 0));
        content.setOpaque(false);
        main.add(content, BorderLayout.CENTER);

        JPanel left = new JPanel(new BorderLayout(0, 18));
        left.setOpaque(false);
        content.add(left, BorderLayout.CENTER);

        left.add(createWelcome(), BorderLayout.NORTH);

        JPanel menuArea = new JPanel(new BorderLayout(0, 16));
        menuArea.setOpaque(false);
        left.add(menuArea, BorderLayout.CENTER);

        categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 0));
        categoryPanel.setOpaque(false);
        String[] categories = {
            "All", "Burgers", "Pizza", "Meals", "Sides", "Drinks", "Desserts"
        };

        for (String cat : categories) {
            JButton b = new JButton(cat);
            styleCategory(b, cat.equals("All"));
            b.addActionListener(e -> {
                selectedCategory = cat;
                for (JButton cb : categoryButtons) {
                    styleCategory(cb, cb.getText().equals(cat));
                }
                renderMenu();
            });
            categoryButtons.add(b);
            categoryPanel.add(b);
        }
        menuArea.add(categoryPanel, BorderLayout.NORTH);

        menuPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        menuPanel.setOpaque(false);

        JScrollPane menuScroll = new JScrollPane(menuPanel);
        menuScroll.setBorder(null);
        menuScroll.setOpaque(false);
        menuScroll.getViewport().setOpaque(false);
        menuScroll.getVerticalScrollBar().setUnitIncrement(18);
        menuArea.add(menuScroll, BorderLayout.CENTER);

        JPanel right = createCartArea();
        right.setPreferredSize(new Dimension(310, 100));
        content.add(right, BorderLayout.EAST);
    }

    private JPanel createSidebar() {
        JPanel side = new JPanel();
        side.setBackground(DARK);
        side.setPreferredSize(new Dimension(205, 100));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(28, 18, 24, 18));

        JLabel logo = new JLabel("  🍊 Crave");
        logo.setFont(new Font("SansSerif", Font.BOLD, 25));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(logo);

        JLabel tagline = new JLabel("  FOOD, MOOD & MORE");
        tagline.setFont(new Font("SansSerif", Font.BOLD, 9));
        tagline.setForeground(new Color(175, 180, 194));
        tagline.setBorder(new EmptyBorder(7, 0, 35, 0));
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(tagline);

        side.add(sideLabel("  MAIN MENU"));
        side.add(Box.createVerticalStrut(12));
        side.add(navItem("⌂", "Dashboard", true));
        side.add(navItem("▦", "Explore Food", false));
        side.add(navItem("♡", "Favourites", false));
        side.add(navItem("◷", "Order History", false));

        side.add(Box.createVerticalStrut(30));
        side.add(sideLabel("  YOUR ACCOUNT"));
        side.add(Box.createVerticalStrut(12));
        side.add(navItem("♙", "My Profile", false));
        side.add(navItem("⚙", "Settings", false));

        side.add(Box.createVerticalGlue());

        JPanel offer = new JPanel();
        offer.setLayout(new BoxLayout(offer, BoxLayout.Y_AXIS));
        offer.setBackground(new Color(48, 51, 64));
        offer.setBorder(new EmptyBorder(15, 12, 15, 12));
        offer.setMaximumSize(new Dimension(180, 125));
        offer.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel gift = new JLabel("🎁 Special offer");
        gift.setForeground(Color.WHITE);
        gift.setFont(new Font("SansSerif", Font.BOLD, 14));
        JLabel off = new JLabel("Get 20% OFF");
        off.setForeground(new Color(255, 155, 119));
        off.setFont(new Font("SansSerif", Font.BOLD, 18));
        JLabel code = new JLabel("Use code CRAVE20");
        code.setForeground(new Color(205, 208, 218));
        code.setFont(new Font("SansSerif", Font.PLAIN, 11));

        offer.add(gift);
        offer.add(Box.createVerticalStrut(8));
        offer.add(off);
        offer.add(Box.createVerticalStrut(5));
        offer.add(code);
        side.add(offer);
        side.add(Box.createVerticalStrut(18));

        JLabel footer = new JLabel("  Made with ♥ for foodies");
        footer.setForeground(new Color(170, 175, 190));
        footer.setFont(new Font("SansSerif", Font.PLAIN, 10));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(footer);

        return side;
    }

    private JLabel sideLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(145, 151, 168));
        l.setFont(new Font("SansSerif", Font.BOLD, 10));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JPanel navItem(String icon, String name, boolean active) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setMaximumSize(new Dimension(190, 43));
        p.setPreferredSize(new Dimension(170, 43));
        p.setBackground(active ? ORANGE : DARK);
        p.setBorder(new EmptyBorder(0, 12, 0, 8));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel i = new JLabel(icon);
        i.setForeground(Color.WHITE);
        i.setFont(new Font("SansSerif", Font.BOLD, 19));

        JLabel t = new JLabel(name);
        t.setForeground(active ? Color.WHITE : new Color(204, 208, 218));
        t.setFont(new Font("SansSerif",
                active ? Font.BOLD : Font.PLAIN, 13));

        p.add(i, BorderLayout.WEST);
        p.add(t, BorderLayout.CENTER);

        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        p.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (name.equals("Explore Food") || name.equals("Dashboard")) {
                    selectedCategory = "All";
                    for (JButton b : categoryButtons) {
                        styleCategory(b, b.getText().equals("All"));
                    }
                    renderMenu();
                } else if (name.equals("Order History")) {
                    JOptionPane.showMessageDialog(FoodOrderingApp.this,
                            "Your recent orders will appear here.",
                            "Order History", JOptionPane.INFORMATION_MESSAGE);
                } else if (name.equals("Favourites")) {
                    JOptionPane.showMessageDialog(FoodOrderingApp.this,
                            "Your favourite food list is coming soon!",
                            "Favourites", JOptionPane.INFORMATION_MESSAGE);
                } else if (name.equals("My Profile") || name.equals("Settings")) {
                    JOptionPane.showMessageDialog(FoodOrderingApp.this,
                            name + " section is coming soon!",
                            name, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        return p;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(15, 0));
        header.setOpaque(false);

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Discover delicious food");
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setForeground(DARK);

        JLabel sub = new JLabel("Find your favourite meal, made fresh for you.");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(MUTED);

        titles.add(title);
        titles.add(Box.createVerticalStrut(6));
        titles.add(sub);
        header.add(titles, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        searchField = new JTextField(16);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchField.setPreferredSize(new Dimension(205, 40));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 236)),
                new EmptyBorder(5, 12, 5, 12)));
        searchField.setToolTipText("Search food...");
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { renderMenu(); }
            public void removeUpdate(DocumentEvent e) { renderMenu(); }
            public void changedUpdate(DocumentEvent e) { renderMenu(); }
        });

        JLabel avatar = new JLabel("  👤  Guest  ");
        avatar.setOpaque(true);
        avatar.setBackground(Color.WHITE);
        avatar.setFont(new Font("SansSerif", Font.BOLD, 13));
        avatar.setBorder(new EmptyBorder(11, 8, 11, 8));

        right.add(searchField);
        right.add(avatar);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JPanel createWelcome() {
        JPanel banner = new JPanel(new BorderLayout(15, 0));
        banner.setBackground(new Color(255, 231, 218));
        banner.setBorder(new EmptyBorder(20, 24, 20, 20));
        banner.setPreferredSize(new Dimension(500, 135));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel small = new JLabel("  ✨  TODAY'S SPECIAL");
        small.setForeground(new Color(187, 80, 45));
        small.setFont(new Font("SansSerif", Font.BOLD, 10));

        JLabel big = new JLabel("Good food, good mood!");
        big.setForeground(new Color(70, 43, 35));
        big.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel desc = new JLabel("Delicious meals delivered to your cravings.");
        desc.setForeground(new Color(111, 83, 74));
        desc.setFont(new Font("SansSerif", Font.PLAIN, 12));

        text.add(small);
        text.add(Box.createVerticalStrut(8));
        text.add(big);
        text.add(Box.createVerticalStrut(6));
        text.add(desc);
        banner.add(text, BorderLayout.CENTER);

        JLabel foodArt = new JLabel("🍔  🍕", SwingConstants.CENTER);
        foodArt.setFont(new Font("SansSerif", Font.PLAIN, 38));
        banner.add(foodArt, BorderLayout.EAST);
        return banner;
    }

    private JPanel createCartArea() {
        JPanel outer = new JPanel(new BorderLayout(0, 14));
        outer.setBackground(Color.WHITE);
        outer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(235, 236, 240)),
                new EmptyBorder(18, 16, 18, 16)));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel title = new JLabel("Your Cart  🛒");
        title.setFont(new Font("SansSerif", Font.BOLD, 19));
        title.setForeground(DARK);

        countLabel = new JLabel("0 items");
        countLabel.setForeground(MUTED);
        countLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        top.add(title, BorderLayout.NORTH);
        top.add(countLabel, BorderLayout.SOUTH);
        outer.add(top, BorderLayout.NORTH);

        cartPanel = new JPanel();
        cartPanel.setBackground(Color.WHITE);
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(cartPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        outer.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(235, 236, 240));

        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(false);
        totalRow.setBorder(new EmptyBorder(14, 0, 15, 0));

        JLabel totalText = new JLabel("Total Amount");
        totalText.setFont(new Font("SansSerif", Font.BOLD, 14));

        totalLabel = new JLabel("₹0");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 23));
        totalLabel.setForeground(ORANGE);

        totalRow.add(totalText, BorderLayout.WEST);
        totalRow.add(totalLabel, BorderLayout.EAST);

        JButton checkout = new JButton("Proceed to Checkout  →");
        checkout.setFont(new Font("SansSerif", Font.BOLD, 13));
        checkout.setForeground(Color.WHITE);
        checkout.setBackground(ORANGE);
        checkout.setFocusPainted(false);
        checkout.setBorderPainted(false);
        checkout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        checkout.setPreferredSize(new Dimension(250, 45));
        checkout.addActionListener(e -> checkout());

        JButton clear = new JButton("Clear cart");
        clear.setFont(new Font("SansSerif", Font.PLAIN, 12));
        clear.setForeground(MUTED);
        clear.setFocusPainted(false);
        clear.setBorderPainted(false);
        clear.setContentAreaFilled(false);
        clear.addActionListener(e -> {
            cart.clear();
            renderCart();
        });

        bottom.add(sep);
        bottom.add(totalRow);
        bottom.add(checkout);
        bottom.add(clear);
        outer.add(bottom, BorderLayout.SOUTH);

        return outer;
    }

    private void styleCategory(JButton b, boolean active) {
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        active ? ORANGE : new Color(230, 232, 237)),
                new EmptyBorder(8, 13, 8, 13)));
        b.setBackground(active ? ORANGE : Color.WHITE);
        b.setForeground(active ? Color.WHITE : new Color(85, 91, 105));
        b.setOpaque(true);
    }

    private void renderMenu() {
        if (menuPanel == null) return;

        menuPanel.removeAll();
        String query = searchField == null ? "" :
                searchField.getText().trim().toLowerCase();

        int found = 0;
        for (Food f : foods.values()) {
            boolean categoryMatch = selectedCategory.equals("All")
                    || f.category.equals(selectedCategory);
            boolean searchMatch = f.name.toLowerCase().contains(query)
                    || f.category.toLowerCase().contains(query)
                    || f.description.toLowerCase().contains(query);

            if (categoryMatch && searchMatch) {
                menuPanel.add(createFoodCard(f));
                found++;
            }
        }

        if (found == 0) {
            JPanel empty = new JPanel(new BorderLayout());
            empty.setOpaque(false);
            JLabel msg = new JLabel("No food found. Try another search.",
                    SwingConstants.CENTER);
            msg.setForeground(MUTED);
            msg.setFont(new Font("SansSerif", Font.PLAIN, 14));
            empty.add(msg, BorderLayout.NORTH);
            menuPanel.setLayout(new BorderLayout());
            menuPanel.add(empty, BorderLayout.NORTH);
        } else {
            menuPanel.setLayout(new GridLayout(0, 3, 15, 15));
        }

        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private JPanel createFoodCard(Food f) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(235, 236, 240)),
                new EmptyBorder(12, 12, 12, 12)));

        JPanel picture = new JPanel(new BorderLayout());
        picture.setBackground(new Color(255, 243, 235));
        picture.setPreferredSize(new Dimension(150, 100));

        JLabel emoji = new JLabel(f.emoji, SwingConstants.CENTER);
        emoji.setFont(new Font("SansSerif", Font.PLAIN, 48));
        picture.add(emoji, BorderLayout.CENTER);

        JLabel tag = new JLabel("  " + f.category + "  ");
        tag.setOpaque(true);
        tag.setBackground(Color.WHITE);
        tag.setForeground(new Color(179, 85, 56));
        tag.setFont(new Font("SansSerif", Font.BOLD, 10));
        tag.setBorder(new EmptyBorder(5, 4, 5, 4));
        picture.add(tag, BorderLayout.NORTH);

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(f.name);
        name.setFont(new Font("SansSerif", Font.BOLD, 14));
        name.setForeground(DARK);

        JLabel description = new JLabel(
                "<html><div style='width:140px'>" + f.description + "</div></html>");
        description.setFont(new Font("SansSerif", Font.PLAIN, 10));
        description.setForeground(MUTED);

        details.add(name);
        details.add(Box.createVerticalStrut(5));
        details.add(description);
        details.add(Box.createVerticalStrut(10));

        JPanel priceRow = new JPanel(new BorderLayout(5, 0));
        priceRow.setOpaque(false);

        JLabel price = new JLabel("₹" + f.price);
        price.setFont(new Font("SansSerif", Font.BOLD, 17));
        price.setForeground(DARK);

        JButton add = new JButton("+ Add");
        add.setFont(new Font("SansSerif", Font.BOLD, 11));
        add.setForeground(ORANGE);
        add.setBackground(new Color(255, 240, 233));
        add.setFocusPainted(false);
        add.setBorder(BorderFactory.createEmptyBorder(7, 10, 7, 10));
        add.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add.addActionListener(e -> {
            cart.put(f.name, cart.getOrDefault(f.name, 0) + 1);
            renderCart();
        });

        priceRow.add(price, BorderLayout.WEST);
        priceRow.add(add, BorderLayout.EAST);
        details.add(priceRow);

        card.add(picture, BorderLayout.NORTH);
        card.add(details, BorderLayout.CENTER);
        return card;
    }

    private void renderCart() {
        if (cartPanel == null) return;

        cartPanel.removeAll();
        int total = 0;
        int itemCount = 0;

        if (cart.isEmpty()) {
            JLabel empty = new JLabel(
                    "<html><div style='text-align:center;width:210px'>"
                    + "🛍️<br><br>Your cart is empty<br>"
                    + "<font color='#858b98'>Add something delicious!</font>"
                    + "</div></html>", SwingConstants.CENTER);
            empty.setForeground(MUTED);
            empty.setFont(new Font("SansSerif", Font.PLAIN, 14));
            empty.setBorder(new EmptyBorder(35, 0, 35, 0));
            cartPanel.add(empty);
        } else {
            for (String name : cart.keySet()) {
                Food f = foods.get(name);
                int qty = cart.get(name);
                total += f.price * qty;
                itemCount += qty;

                JPanel row = new JPanel(new BorderLayout(6, 8));
                row.setBackground(Color.WHITE);
                row.setBorder(new EmptyBorder(12, 0, 12, 0));
                row.setMaximumSize(new Dimension(300, 82));

                JLabel emoji = new JLabel(f.emoji);
                emoji.setFont(new Font("SansSerif", Font.PLAIN, 25));
                row.add(emoji, BorderLayout.WEST);

                JPanel info = new JPanel();
                info.setOpaque(false);
                info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

                JLabel n = new JLabel(f.name);
                n.setFont(new Font("SansSerif", Font.BOLD, 12));

                JLabel p = new JLabel("₹" + f.price + " each");
                p.setFont(new Font("SansSerif", Font.PLAIN, 10));
                p.setForeground(MUTED);

                info.add(n);
                info.add(Box.createVerticalStrut(5));
                info.add(p);
                row.add(info, BorderLayout.CENTER);

                JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 7));
                qtyPanel.setOpaque(false);

                JButton minus = new JButton("−");
                JButton plus = new JButton("+");
                styleQtyButton(minus);
                styleQtyButton(plus);

                JLabel q = new JLabel(String.valueOf(qty));
                q.setFont(new Font("SansSerif", Font.BOLD, 12));

                minus.addActionListener(e -> {
                    int current = cart.get(name);
                    if (current <= 1) cart.remove(name);
                    else cart.put(name, current - 1);
                    renderCart();
                });

                plus.addActionListener(e -> {
                    cart.put(name, cart.get(name) + 1);
                    renderCart();
                });

                qtyPanel.add(minus);
                qtyPanel.add(q);
                qtyPanel.add(plus);
                row.add(qtyPanel, BorderLayout.SOUTH);

                cartPanel.add(row);
                cartPanel.add(new JSeparator());
            }
        }

        countLabel.setText(itemCount + (itemCount == 1 ? " item" : " items"));
        totalLabel.setText("₹" + total);
        cartPanel.revalidate();
        cartPanel.repaint();
    }

    private void styleQtyButton(JButton b) {
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setForeground(ORANGE);
        b.setBackground(new Color(255, 240, 233));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(27, 25));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please add food to your cart first!",
                    "Cart is empty", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JTextField name = new JTextField();
        JTextField phone = new JTextField();
        JTextArea address = new JTextArea(3, 18);
        address.setLineWrap(true);
        address.setWrapStyleWord(true);

        JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
        form.add(new JLabel("Customer name:"));
        form.add(name);
        form.add(new JLabel("Mobile number (10 digits):"));
        form.add(phone);
        form.add(new JLabel("Delivery address:"));
        form.add(new JScrollPane(address));

        int result = JOptionPane.showConfirmDialog(this, form,
                "Checkout - Delivery Details",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        if (name.getText().trim().isEmpty()
                || !phone.getText().trim().matches("[0-9]{10}")
                || address.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your name, a valid 10-digit mobile number "
                    + "and delivery address.",
                    "Invalid details", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder summary = new StringBuilder();
        int total = 0;
        summary.append("Thank you, ").append(name.getText().trim())
                .append("!\n\nYour order:\n");

        for (String item : cart.keySet()) {
            Food f = foods.get(item);
            int qty = cart.get(item);
            summary.append("• ").append(item).append(" x")
                    .append(qty).append(" = ₹")
                    .append(f.price * qty).append("\n");
            total += f.price * qty;
        }

        summary.append("\nTotal: ₹").append(total)
                .append("\n\nDelivering to:\n")
                .append(address.getText().trim())
                .append("\n\nOrder placed successfully! 🎉")
                .append("\n(This is a demo app; no real delivery is created.)");

        JOptionPane.showMessageDialog(this, summary.toString(),
                "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);

        cart.clear();
        renderCart();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            FoodOrderingApp app = new FoodOrderingApp();
            app.setVisible(true);
        });
    }
}
