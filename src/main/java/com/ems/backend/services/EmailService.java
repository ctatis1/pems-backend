package com.ems.backend.services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.RawMessage;
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;

public class EmailService {
    @Value("${spring.mail.username}")
    private String accessKey;
    @Value("${spring.mail.password}")
    private String secretKey;
    private final SesClient sesClient;

    public EmailService() {
        this.sesClient = SesClient.builder()
                .region(Region.US_EAST_2) 
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("AKIATNZM4MOYPFUWFQ6L", "BMOS5Z2fniw9NMabREnzFhcpDRoC+VkzWVmHlIs7QNL8")))
                .build();
    }

    public void enviarEmailConPDF(String destinatario, File archivoPdf) {
        String remitente = "camilotatism@ibm.com"; 
        String asunto = "Informe PDF adjunto";
        String mensajeTexto = "Adjunto el archivo PDF con la información solicitada.";

        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(archivoPdf.getAbsolutePath()));
            String base64FileContent = Base64.getEncoder().encodeToString(fileContent);

            RawMessage rawMessage = RawMessage.builder()
                    .data(SdkBytes.fromByteArray(createRawEmail(remitente, destinatario, asunto, mensajeTexto, base64FileContent)))
                    .build();

            // Enviar el email
            SendRawEmailRequest sendRawEmailRequest = SendRawEmailRequest.builder()
                    .rawMessage(rawMessage)
                    .build();

            sesClient.sendRawEmail(sendRawEmailRequest);
            System.out.println("Correo enviado con éxito a " + destinatario);
        } catch (Exception e) {
            System.err.println("Error al enviar el email: " + e.getMessage());
        }
    }

    private byte[] createRawEmail(String remitente, String destinatario, String asunto, String mensajeTexto, String base64FileContent) {
        // Crear el contenido de email en formato raw
        String rawEmail = "From: " + remitente + "\n"
                + "To: " + destinatario + "\n"
                + "Subject: " + asunto + "\n"
                + "MIME-Version: 1.0\n"
                + "Content-Type: multipart/mixed; boundary=\"--boundary\"\n\n"
                + "--boundary\n"
                + "Content-Type: text/plain; charset=UTF-8\n\n"
                + mensajeTexto + "\n\n"
                + "--boundary\n"
                + "Content-Type: application/pdf; name=\"" + "informe.pdf" + "\"\n"
                + "Content-Transfer-Encoding: base64\n"
                + "Content-Disposition: attachment; filename=\"" + "informe.pdf" + "\"\n\n"
                + base64FileContent + "\n\n"
                + "--boundary--";

        return rawEmail.getBytes();
    }
}
