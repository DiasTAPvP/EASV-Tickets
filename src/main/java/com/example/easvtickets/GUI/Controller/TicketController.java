package com.example.easvtickets.GUI.Controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;

public class TicketController {


    /**Implement:
     * The ability to send a ticket to a user through their given email
     * Making sure the checkboxes reflect the provided custom ticket types
     * The ability to generate a ticket for a user
     * The ability to add a special "Free" ticket type and send that as an extra
     * if the event has a special ticket
     */



    private CoordScreenController setCoordScreenController;

    public void setCoordScreenController(CoordScreenController coordScreenController) {this.setCoordScreenController = coordScreenController;}

    public static BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }



}