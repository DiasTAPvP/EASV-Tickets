package com.example.easvtickets.GUI.Controller;

import com.example.easvtickets.BE.Events;
import com.example.easvtickets.BE.TicketType;
import com.example.easvtickets.GUI.Model.TicketTypeModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;

import java.awt.*;
import java.io.File;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import javafx.fxml.FXML;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.List;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.Document;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;


public class TicketGenController {

    @FXML public TextField customerEmail;
    @FXML public TextField customerName;
    @FXML public TextField ticketAmount;
    @FXML public Checkbox specialTickets;
    @FXML public ComboBox ticketTypePicker;

    private TicketTypeModel ticketTypeModel;
    private TicketType selectedTicketType;
    private Events selectedEvent;

    public TicketGenController() throws Exception {
        ticketTypeModel = new TicketTypeModel();
    }

    private CoordScreenController setCoordScreenController;

    public void setCoordScreenController(CoordScreenController coordScreenController) {
        this.setCoordScreenController = coordScreenController;
    }

    private void generateAndDisplayTicket() {
        Events selectedEvent = setCoordScreenController.getSelectedEvent();
        String email = customerEmail.getText();

        if (email == null || email.isEmpty()) {
            System.out.println("Please enter a valid email.");
            return;
        }

        if (selectedEvent == null) {
            System.out.println("Please select an event first.");
            return;
        }

        // Generate the QR and PDF
        processQRCode(selectedEvent, email);

        // Display the ticket
        File ticketFile = new File("ticket_for_" + email + ".pdf");
        if (ticketFile.exists()) {
            displayPDF(ticketFile);
        } else {
            System.out.println("Ticket PDF was not created.");
        }
    }

     public void onGenerateTicketPress() {
         generateAndDisplayTicket();
    }


    public void onPrintTicketPressed() {
        generateAndDisplayTicket();
    }


    private void displayPDF(File file) {
        try {
            // Option 1: open in system default viewer
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                System.out.println("Desktop is not supported. Cannot open PDF.");
            }
        } catch (IOException e) {
            System.err.println("Failed to open PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private BufferedImage matrixToImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Set colors - black for QR code points, white for background
        int black = 0xFF000000;
        int white = 0xFFFFFFFF;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? black : white);
            }
        }
        return image;
    }

    private BitMatrix generateQRCode(String data, int width, int height) {
        try {
            // Create hint map to improve QR code generation
            java.util.Map<EncodeHintType, Object> hints = new java.util.HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1); // Smaller margins for better use of space
            hints.put(EncodeHintType.ERROR_CORRECTION, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M);

            // Generate the QR code
            return new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height, hints);
        } catch (WriterException e) {
            System.err.println("Error generating QR code: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /*public void generateQRHash() {
        String generatedQRHash = generateRandomQRHash();
    }*/

    private String generateRandomQRHash() {
        int length = 6;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!?";
        Random random = new Random();
        StringBuilder QRHash = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            QRHash.append(characters.charAt(random.nextInt(characters.length())));
        }
        return QRHash.toString();
    }


    private void processQRCode(Events event, String customerEmail) {
        // Generate QR code for a ticket
        String generatedHash = generateRandomQRHash();
        String ticketData = "Event: " + event.getEventName() +
                ", ID: " + event.getEventId() +
                ", Date: " + event.getEventDate() +
                ", Email: " + customerEmail +
                ", QRHash: " + generatedHash;

        BitMatrix qrMatrix = generateQRCode(ticketData, 77, 77);



        // Check if QR code was generated successfully
        if (qrMatrix != null) {
            try {
                // Convert matrix to image
                BufferedImage qrImage = matrixToImage(qrMatrix);

                // Save to temporary file
                File qrFile = File.createTempFile("ticket_qr_", ".png");
                ImageIO.write(qrImage, "PNG", qrFile);

                // Now you can use this image for the ticket (e.g., embed in PDF, email)
                System.out.println("QR code generated successfully: " + qrFile.getAbsolutePath());

                // Here you would call your method to create the full ticket with the QR code
                createTicketWithQR(event, customerEmail, qrFile);

            } catch (IOException e) {
                System.err.println("Failed to save QR code image: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // Handle the error case
            System.err.println("Failed to generate QR code");
        }
    }

    private void processSpecialQRCode(Events event, String customerEmail) {
        String generatedHash = generateRandomQRHash();
        String SpecialTicketData = "Event: " + event.getEventName() +
                ", ID: " + event.getEventId() +
                ", Date: " + event.getEventDate() +
                ", Email: " + customerEmail +
                ", QRHash: " + generatedHash;

        BitMatrix qrMatrix = generateQRCode(SpecialTicketData, 77, 77);



        // Check if QR code was generated successfully
        if (qrMatrix != null) {
            try {
                // Convert matrix to image
                BufferedImage qrImage = matrixToImage(qrMatrix);

                // Save to temporary file
                File qrFile = File.createTempFile("special_ticket_qr_", ".png");
                ImageIO.write(qrImage, "PNG", qrFile);

                // Now you can use this image for the ticket (e.g., embed in PDF, email)
                System.out.println("QR code generated successfully: " + qrFile.getAbsolutePath());

                // Here you would call your method to create the full ticket with the QR code
                createTicketWithQR(event, customerEmail, qrFile);

            } catch (IOException e) {
                System.err.println("Failed to save QR code image: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // Handle the error case
            System.err.println("Failed to generate QR code");
        }
    }


    // This method would be implemented to create the complete ticket using the QR code
    private void createTicketWithQR(Events event, String recipientEmail, File qrCodeFile) {
        System.out.println("Creating ticket for " + recipientEmail + " for event " + event.getEventName());

        // PDF file to store the ticket
        File ticketFile = new File("ticket_for_" + recipientEmail + ".pdf");

        try {
            // Create PdfWriter instance
            PdfWriter writer = new PdfWriter(ticketFile);

            // Create PdfDocument instance
            PdfDocument pdfDoc = new PdfDocument(writer);

            // Create a Document to hold content
            Document document = new Document(pdfDoc);

            // Add ticket details
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            document.add(new Paragraph("Ticket for: " + event.getEventName()).setFontSize(18).setBold());
            document.add(new Paragraph("Date: " + event.getEventDate().toLocalDateTime().format(formatter)));
            document.add(new Paragraph("Location: " +event.getLocation() ));
            document.add(new Paragraph("Notes: " +event.getNotes()));


            // Add some space
            document.add(new Paragraph("\n"));

            // Add the QR code image
            Image qrImage = new Image(ImageDataFactory.create(qrCodeFile.getAbsolutePath()));
            qrImage.setAutoScale(true); // Resize the image to fit the document
            document.add(qrImage);

            // Close the document
            document.close();

            // Print success message
            System.out.println("Ticket PDF created successfully: " + ticketFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Failed to create ticket PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Set up listener for ticket type selection
        ticketTypePicker.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedTicketType = (TicketType) newValue;
            }
        });
    }

    private void setupTicketTypePicker() {
        // Clear any existing items
        ticketTypePicker.getItems().clear();

        // Get the selected event from the coordinator screen
        Events selectedEvent = setCoordScreenController.getSelectedEvent();

        if (selectedEvent != null) {
            try {
                // Get ticket types for this specific event
                List<TicketType> eventTicketTypes = ticketTypeModel.getTicketTypesForEvent(selectedEvent.getEventId());

                // Add them to the combobox
                ticketTypePicker.getItems().addAll(eventTicketTypes);

                // Set a cell factory to display the ticket type names
                ticketTypePicker.setCellFactory(lv -> new ListCell<TicketType>() {
                    @Override
                    protected void updateItem(TicketType item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? "" : item.getTypeName());
                    }
                });

                ticketTypePicker.setButtonCell(new ListCell<TicketType>() {
                    @Override
                    protected void updateItem(TicketType item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? "" : item.getTypeName());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private TextArea newEventInfo;

    public void setEventDetails(Events selectedEvent) {
        if (selectedEvent != null) {
            StringBuilder details = new StringBuilder();
            details.append("Event. ").append(selectedEvent.getEventName()).append("\n");
            details.append("Description: ").append(selectedEvent.getDescription()).append("\n");
            details.append("Date: ").append(selectedEvent.getEventDate()).append("\n");
            details.append("Location: ").append(selectedEvent.getLocation()).append("\n");
            details.append("Notes: ").append(selectedEvent.getNotes()).append("\n");
            details.append("Available Tickets: ").append(selectedEvent.getAvailableTickets()).append("\n");
            details.append("Optional Info: ").append(selectedEvent.getOptionalInformation());

            newEventInfo.setText(details.toString());

            //Update ticket types for this event
            setupTicketTypePicker();

        }
    }


}



