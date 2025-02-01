package com.ems.backend.controllers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ems.backend.entities.ProductoPDF;
import com.ems.backend.services.EmailService;
import com.ems.backend.services.PDFService;

@RestController
@RequestMapping("/inventario")
@CrossOrigin(originPatterns = "*")
public class PDFController {
    @Autowired
    private PDFService pdfService;

    private final EmailService emailService;

    public PDFController() {
        this.emailService = new EmailService();
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> generatePdf(@RequestBody List<ProductoPDF> productos) throws IOException {
        byte[] pdfBytes = pdfService.generateProductsPdf(productos);

        String timestamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String fileName = "ems_inventario_" + timestamp + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ fileName)
                .body(pdfBytes);
    }

    @PostMapping("/enviar")
    public String enviarEmailConPdf(@RequestParam String emailDestino, @RequestParam String rutaPdf) {
        File archivoPdf = new File(rutaPdf);

        if (!archivoPdf.exists()) {
            return "El archivo no existe.";
        }

        emailService.enviarEmailConPDF(emailDestino, archivoPdf);
        return "Correo enviado con éxito.";
    }
}
