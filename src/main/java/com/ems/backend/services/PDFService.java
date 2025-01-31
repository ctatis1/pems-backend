package com.ems.backend.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import com.ems.backend.entities.ProductoPDF;

@Service
public class PDFService {
    public byte[] generateProductsPdf(List<ProductoPDF> productos) throws IOException {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 750);
            contentStream.showText("Reporte de Inventario");
            contentStream.endText();

            contentStream.setFont(PDType1Font.HELVETICA, 12);
            int yPosition = 720;

            for (ProductoPDF producto : productos) {
                if (yPosition < 100) { 
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = 720;
                }

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("ID: " + producto.getId() + " - " + producto.getNombre());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Código: " + producto.getCodigo() + ", Stock: " + producto.getStock());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Precio (COP): " + producto.getPrecios().get("COP") + ", USD: " + producto.getPrecios().get("USD"));
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Empresa: " + producto.getEmpresa().getNombre() + " (NIT: " + producto.getEmpresa().getNit() + ")");
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Categorías: " + String.join(", ", producto.getCategoriasNombres()));
                contentStream.newLineAtOffset(0, -30);
                contentStream.endText();

                yPosition -= 100;
            }

            contentStream.close();
            document.save(baos);
            return baos.toByteArray();
        }
    }
}
