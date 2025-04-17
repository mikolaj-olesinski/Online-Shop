package org.example.produktylist.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.produktylist.Entity.Order;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Serwis generujący etykiety przewozowe.
 */
@Service
public class ShippingLabelService {

    public byte[] generateShippingLabel(Order order) throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);

        document.open();

        // Dodaj logo lub nagłówek
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

        // Nagłówek
        Paragraph header = new Paragraph("LIST PRZEWOZOWY", headerFont);
        header.setAlignment(Element.ALIGN_CENTER);
        header.setSpacingAfter(20f);
        document.add(header);

        // Numer zamówienia i data
        document.add(new Paragraph("Nr zamówienia: " + order.getId(), boldFont));
        document.add(new Paragraph("Data nadania: " + order.getStatusDate(), normalFont));
        document.add(new Paragraph("\n"));

        // Dane nadawcy
        document.add(new Paragraph("NADAWCA:", boldFont));
        document.add(new Paragraph("Sklep internetowy XYZ", normalFont));
        document.add(new Paragraph("ul. Przykładowa 1", normalFont));
        document.add(new Paragraph("00-001 Warszawa", normalFont));
        document.add(new Paragraph("\n"));

        // Dane odbiorcy
        document.add(new Paragraph("ODBIORCA:", boldFont));
        document.add(new Paragraph(order.getDataForm().getFirstName() + " " + order.getDataForm().getLastName(), normalFont));
        if (order.getDataForm().getCompany() != null && !order.getDataForm().getCompany().isEmpty()) {
            document.add(new Paragraph(order.getDataForm().getCompany(), normalFont));
        }
        document.add(new Paragraph(order.getDataForm().getAddress(), normalFont));
        document.add(new Paragraph(order.getDataForm().getPostalCode() + " " + order.getDataForm().getCity(), normalFont));
        document.add(new Paragraph(order.getDataForm().getState(), normalFont));
        document.add(new Paragraph(order.getDataForm().getCountry(), normalFont));
        document.add(new Paragraph("\n"));


        document.add(new Paragraph("INFORMACJE O PRZESYŁCE:", boldFont));
        document.add(new Paragraph("Sposób dostawy: " + order.getDataForm().getDeliveryType(), normalFont));
        document.add(new Paragraph("Liczba paczek: 1", normalFont));
        document.add(new Paragraph("Waga: " + calculateTotalWeight(order) + " kg", normalFont));

        document.close();
        return outputStream.toByteArray();
    }

    private double calculateTotalWeight(Order order) {
        // Przykładowa logika obliczania wagi
        return 1.5; // Przykładowa waga w kg
    }
}