package Parafarm;

import java.util.ArrayList;
import java.io.FileWriter;
import java.io.PrintWriter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class InvoiceScreen {

    private VBox root;
    private Stage stage;

    java.util.LinkedHashMap<String, Integer> quantities = new java.util.LinkedHashMap<>();
    java.util.LinkedHashMap<String, Double> prices = new java.util.LinkedHashMap<>();

    private static final String GREEN       = "#2e7d32";
    private static final String GREEN_LIGHT = "#43a047";
    private static final String GREEN_SOFT  = "#e8f5e9";
    private static final String BG          = "#f5f6f8";
    private static final String WHITE       = "#ffffff";
    private static final String BORDER      = "#e0e0e0";
    private static final String TEXT_MAIN   = "#1a1a1a";
    private static final String TEXT_MUTED  = "#757575";

    public Scene getScene(Stage stage) {
        this.stage = stage;
        quantities = new java.util.LinkedHashMap<>();
        prices = new java.util.LinkedHashMap<>();

        Order currentOrder =Main.newOrderScreen.getOrder();
        currentOrder.make_invoice();
        Invoice invoice = currentOrder.getInvoice();
        invoice.setTotalPrice(calculateFinalPriceWithFPA(getTotalPrice()));

        Label invNumberLbl = new Label("Αριθμός Τιμολογίου");
        invNumberLbl.setFont(Font.font("Arial", 11));
        invNumberLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        Label invNumberVal = new Label(invoice.getInvoiceNumber());
        invNumberVal.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        invNumberVal.setStyle(
            "-fx-text-fill: " + TEXT_MAIN + ";" +
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 6 12;"
        );

        VBox invNumberBox = new VBox(4, invNumberLbl, invNumberVal);

        Label dateLbl = new Label("Ημερομηνία Έκδοσης");
        dateLbl.setFont(Font.font("Arial", 11));
        dateLbl.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        Label dateVal = new Label(invoice.getDate());
        dateVal.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        dateVal.setStyle(
            "-fx-text-fill: " + TEXT_MAIN + ";" +
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 6 12;"
        );

        VBox dateBox = new VBox(4, dateLbl, dateVal);

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        Button autoBtn = new Button("Εκδόθηκε Αυτόματα");
        autoBtn.setPrefHeight(38);
        autoBtn.setStyle(
            "-fx-background-color: " + GREEN_LIGHT + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 0 20;"
        );

        HBox topInfoRow = new HBox(16, invNumberBox, dateBox, topSpacer, autoBtn);
        topInfoRow.setAlignment(Pos.CENTER_LEFT);
        topInfoRow.setPadding(new Insets(16, 16, 12, 16));
        topInfoRow.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 0 0 1 0;"
        );

        Label issuerTitle = new Label("Εκδότης");
        issuerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        issuerTitle.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        Label issuerName = new Label("ParaFarm");
        issuerName.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        issuerName.setStyle("-fx-text-fill: " + TEXT_MAIN + ";");

        Label issuerAfm = new Label("ΑΦΜ: 98/654321");
        issuerAfm.setFont(Font.font("Arial", 12));
        issuerAfm.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        Label issuerCity = new Label("Θεσσαλονίκη");
        issuerCity.setFont(Font.font("Arial", 12));
        issuerCity.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        VBox issuerBox = new VBox(3, issuerTitle, issuerName, issuerAfm, issuerCity);
        issuerBox.setPadding(new Insets(12));
        issuerBox.setStyle(
            "-fx-background-color: " + BG + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );
        HBox.setHgrow(issuerBox, Priority.ALWAYS);

        Label recipientTitle = new Label("Παραλήπτης");
        recipientTitle.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        recipientTitle.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        String customerName = currentOrder.getCustomer() != null ?
            currentOrder.getCustomer().getName() : "—";
        Label recipientName = new Label(customerName);
        recipientName.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        recipientName.setStyle("-fx-text-fill: " + TEXT_MAIN + ";");

        Label recipientAfm = new Label("ΑΦΜ: —");
        recipientAfm.setFont(Font.font("Arial", 12));
        recipientAfm.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        Label recipientCity = new Label("—");
        recipientCity.setFont(Font.font("Arial", 12));
        recipientCity.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        VBox recipientBox = new VBox(3, recipientTitle, recipientName, recipientAfm, recipientCity);
        recipientBox.setPadding(new Insets(12));
        recipientBox.setStyle(
            "-fx-background-color: " + BG + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );
        HBox.setHgrow(recipientBox, Priority.ALWAYS);

        HBox partiesRow = new HBox(12, issuerBox, recipientBox);
        partiesRow.setPadding(new Insets(12, 16, 12, 16));
        partiesRow.setStyle("-fx-background-color: " + WHITE + ";");

        String[] headers = {"Προϊόν", "Τιμή", "Ποσότητα", "Κωδικός", "Σύνολο"};
        int[]    widths  = {200, 80, 100, 140, 100};

        HBox tableHeader = new HBox();
        tableHeader.setPadding(new Insets(10, 16, 10, 16));
        tableHeader.setStyle("-fx-background-color: " + GREEN_SOFT + ";");
        for (int i = 0; i < headers.length; i++) {
            Label lbl = new Label(headers[i].toUpperCase());
            lbl.setPrefWidth(widths[i]);
            lbl.setFont(Font.font("Arial", FontWeight.BOLD, 11));
            lbl.setStyle("-fx-text-fill: " + GREEN + ";");
            lbl.setAlignment(i == 0 ? Pos.CENTER_LEFT : Pos.CENTER);
            tableHeader.getChildren().add(lbl);
        }

        VBox tableBody = new VBox(6);
        tableBody.setPadding(new Insets(8, 12, 8, 12));
        tableBody.setStyle("-fx-background-color: " + BG + ";");

        showOrderData(currentOrder, tableBody);

        ScrollPane tableScroll = new ScrollPane(tableBody);
        tableScroll.setFitToWidth(true);
        tableScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tableScroll.setStyle("-fx-background-color: " + BG + "; -fx-background: " + BG + ";");
        tableScroll.setPrefHeight(200);

        float total = getTotalPrice();
        float withFpa = calculateFinalPriceWithFPA(total);

        Label fpaLabel = new Label("+ΦΠΑ 24%");
        fpaLabel.setFont(Font.font("Arial", 12));
        fpaLabel.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        Label fpaAmount = new Label(String.format("%.2f€", withFpa - total));
        fpaAmount.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        fpaAmount.setStyle("-fx-text-fill: " + TEXT_MAIN + ";");

        Region fpaSpacer = new Region();
        HBox.setHgrow(fpaSpacer, Priority.ALWAYS);

        HBox fpaRow = new HBox(8, fpaSpacer, fpaLabel, fpaAmount);
        fpaRow.setAlignment(Pos.CENTER_RIGHT);
        fpaRow.setPadding(new Insets(6, 16, 6, 16));
        fpaRow.setStyle("-fx-background-color: " + BG + ";");

        Label totalLbl = new Label("Σύνολο με ΦΠΑ");
        totalLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        totalLbl.setStyle("-fx-text-fill: " + TEXT_MAIN + ";");

        Region totalSpacer = new Region();
        HBox.setHgrow(totalSpacer, Priority.ALWAYS);

        Label totalAmount = new Label(String.format("%.2f €", withFpa));
        totalAmount.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        totalAmount.setStyle("-fx-text-fill: " + GREEN + ";");

        HBox totalRow = new HBox(totalSpacer, totalLbl, new Label("   "), totalAmount);
        totalRow.setAlignment(Pos.CENTER);
        totalRow.setPadding(new Insets(14, 20, 14, 20));
        totalRow.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1 0 1 0;"
        );

        Button backBtn = new Button("Επιστροφή");
        backBtn.setPrefHeight(40);
        backBtn.setPrefWidth(140);
        backBtn.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-text-fill: " + TEXT_MAIN + ";" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;"
        );
        backBtn.setOnAction(e -> {
        	Main.invoices.remove(currentOrder.getInvoice());
            currentOrder.currentInvoice = null;
            Main.orders.remove(currentOrder);
            stage.setScene(Main.ordersScreen.getScene(stage));
        });

        Button pdfBtn = new Button("Αποθήκευση σε TXT");
        pdfBtn.setPrefHeight(40);
        pdfBtn.setPrefWidth(160);
        pdfBtn.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-text-fill: " + TEXT_MAIN + ";" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;"
        );
        pdfBtn.setOnAction(e -> {
            String filename = invoice.getInvoiceNumber() + ".txt";
            try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
                pw.println("========================================");
                pw.println("           ΤΙΜΟΛΟΓΙΟ PARAFARM           ");
                pw.println("========================================"); //ligo periergo alla koble kserw gw gia txt prospathisa pdf kai word alla hthele para para polu prama
                pw.println("Αριθμός: " + invoice.getInvoiceNumber());
                pw.println("Ημερομηνία: " + invoice.getDate());
                pw.println("----------------------------------------");
                pw.println("Εκδότης: ParaFarm | ΑΦΜ: 98/654321 | Θεσσαλονίκη");
                pw.println("Παραλήπτης: " + customerName);
                pw.println("----------------------------------------");
                pw.println("ΠΡΟΪΟΝΤΑ:");
                for (String name : quantities.keySet()) {
                    int qty = quantities.get(name);
                    double price = prices.get(name);
                    pw.printf("  %-30s x%d  %.2f€  = %.2f€%n", name, qty, price, price * qty);
                }
                pw.println("----------------------------------------");
                pw.printf("+ΦΠΑ 24%%: %.2f€%n", withFpa - total);
                pw.printf("ΣΥΝΟΛΟ ΜΕ ΦΠΑ: %.2f €%n", withFpa);
                pw.println("========================================");
                System.out.println("Saved: " + filename);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button printBtn = new Button("Εκτύπωση");
        printBtn.setPrefHeight(40);
        printBtn.setPrefWidth(130);
        printBtn.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-text-fill: " + TEXT_MAIN + ";" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-cursor: hand;"
        );
        printBtn.setOnAction(e -> {
            javafx.print.PrinterJob job = javafx.print.PrinterJob.createPrinterJob();
            if (job != null && job.showPrintDialog(stage)) {
                boolean success = job.printPage(root);
                if (success) job.endJob();
            }
        });

        Button accountingBtn = new Button("Αποστολή στο Λογιστήριο");
        accountingBtn.setPrefHeight(40);
        accountingBtn.setPrefWidth(190);
        accountingBtn.setStyle(
            "-fx-background-color: " + GREEN_LIGHT + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-font-size: 13px;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;"
        );
        accountingBtn.setOnAction(e -> stage.setScene(Main.mainScreen.getScene(stage)));

        HBox bottomButtons = new HBox(10, backBtn, pdfBtn, printBtn, accountingBtn);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setPadding(new Insets(16));
        bottomButtons.setStyle("-fx-background-color: " + WHITE + ";");

        VBox tableSection = new VBox(0, tableHeader, tableScroll, fpaRow);
        tableSection.setStyle(
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;"
        );

        root = new VBox(0, topInfoRow, partiesRow, tableSection, totalRow, bottomButtons);
        root.setStyle("-fx-background-color: " + BG + ";");
        VBox.setVgrow(tableSection, Priority.ALWAYS);
        root.setUserData("no-logout");
        return new Scene(root, 800, 600);
    }

    public ArrayList<Product> showOrderData(Order order, VBox tableBody) {
        ArrayList<Product> products = order.getProducts();
        for (Product p : products) {
            quantities.merge(p.getName(), 1, Integer::sum);
            prices.put(p.getName(), p.getPrice());
        }
        for (String name : quantities.keySet()) {
            int qty = quantities.get(name);
            double price = prices.get(name);
            double lineTotal = price * qty;
            tableBody.getChildren().add(buildProductRow(
                name,
                String.format("%.2f€", price),
                String.valueOf(qty),
                "",
                String.format("%.2f€", lineTotal)
            ));
        }
        return products;
    }

    private HBox buildProductRow(String name, String price, String qty, String code, String total) {
        HBox row = new HBox();
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        int[] widths = {200, 80, 100, 140, 100};
        String[] values = {name, price, qty, code, total};

        for (int i = 0; i < values.length; i++) {
            Label lbl = new Label(values[i]);
            lbl.setPrefWidth(widths[i]);
            lbl.setFont(Font.font("Arial", i == 0 ? FontWeight.BOLD : FontWeight.NORMAL, 13));
            lbl.setStyle("-fx-text-fill: " + (i == 4 ? GREEN : TEXT_MAIN) + "; " +
                         (i == 4 ? "-fx-font-weight: bold;" : ""));
            lbl.setAlignment(i == 0 ? Pos.CENTER_LEFT : Pos.CENTER);
            row.getChildren().add(lbl);
        }
        return row;
    }

    public float getTotalPrice() {
        java.util.HashMap<String, Integer> q = new java.util.HashMap<>();
        java.util.HashMap<String, Double> p = new java.util.HashMap<>();
        for (Product prod : Main.newOrderScreen.getOrder().getProducts()) {
            q.merge(prod.getName(), 1, Integer::sum);
            p.put(prod.getName(), prod.getPrice());
        }
        float total = 0;
        for (String name : q.keySet()) total += p.get(name) * q.get(name);
        return total;
    }

    public float calculateFinalPriceWithFPA(float total_price) {
        return total_price * 1.24f;
    }
}