package com.fidegamestore.service;

import com.fidegamestore.domain.Orden;
import com.fidegamestore.domain.OrdenLlave;
import com.fidegamestore.domain.Plataforma;
import com.fidegamestore.domain.Producto;
import com.fidegamestore.domain.Region;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CorreoService {

    private final RestClient restClient;
    private final String apiKey;
    private final String correoRemitente;
    private final String nombreRemitente;

    public CorreoService(
            @Value("${brevo.api-key}") String apiKey,
            @Value("${brevo.sender-email}") String correoRemitente,
            @Value("${brevo.sender-name}") String nombreRemitente) {

        this.apiKey = apiKey;
        this.correoRemitente = correoRemitente;
        this.nombreRemitente = nombreRemitente;

        this.restClient = RestClient.builder()
                .baseUrl("https://api.brevo.com/v3")
                .build();
    }

    public void enviarCorreoHtml(
            String para,
            String asunto,
            String contenido) {

        Map<String, Object> body = Map.of(
                "sender", Map.of(
                        "name", nombreRemitente,
                        "email", correoRemitente
                ),
                "to", new Object[]{
                    Map.of("email", para)
                },
                "subject", asunto,
                "htmlContent", contenido
        );

        restClient.post()
                .uri("/smtp/email")
                .header("api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    private String generarHtmlOrden(Orden orden) {

        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("</head>");

        html.append("<body style='margin:0; padding:20px; background-color:#f5f5f5; font-family:Arial, sans-serif;'>");

        html.append("<div style='max-width:900px; margin:auto; background-color:white; border-radius:10px; overflow:hidden; box-shadow:0 4px 12px rgba(0,0,0,0.15);'>");

        // HEADER
        html.append("<div style='background-color:#198754; color:white; padding:25px;'>");

        html.append("<h1 style='margin:0;'>¡Compra confirmada!</h1>");

        html.append("<p style='margin:10px 0 0 0;'>");
        html.append("Gracias por comprar en FideGameStore.");
        html.append("</p>");

        html.append("</div>");

        // BODY
        html.append("<div style='padding:30px;'>");

        // ORDER INFORMATION
        html.append("<h2>Detalles de la orden</h2>");

        html.append("<p>");
        html.append("<strong>Número de orden:</strong> ");
        html.append(orden.getIdOrden());
        html.append("</p>");

        html.append("<p>");
        html.append("<strong>Fecha:</strong> ");
        html.append(orden.getFechaCreacion());
        html.append("</p>");

        html.append("<p>");
        html.append("<strong>Cliente:</strong> ");
        html.append(orden.getUsuario().getUsername());
        html.append("</p>");

        html.append("<p>");
        html.append("<strong>Correo:</strong> ");
        html.append(orden.getUsuario().getCorreo());
        html.append("</p>");

        html.append("<hr>");

        // PRODUCTS
        html.append("<h2>Productos adquiridos</h2>");

        html.append("<table style='width:100%; border-collapse:collapse;'>");

        html.append("<thead>");
        html.append("<tr style='background-color:#f0f0f0;'>");

        html.append("<th style='padding:12px; border:1px solid #ddd; text-align:left;'>Producto</th>");
        html.append("<th style='padding:12px; border:1px solid #ddd;'>Región</th>");
        html.append("<th style='padding:12px; border:1px solid #ddd;'>Plataforma</th>");
        html.append("<th style='padding:12px; border:1px solid #ddd;'>Precio</th>");

        html.append("</tr>");
        html.append("</thead>");

        html.append("<tbody>");

        for (OrdenLlave ordenLlave : orden.getOrdenLlaves()) {

            Producto producto = ordenLlave
                    .getLlave()
                    .getVarianteProducto()
                    .getProducto();

            Region region = ordenLlave
                    .getLlave()
                    .getVarianteProducto()
                    .getRegion();

            Plataforma plataforma = ordenLlave
                    .getLlave()
                    .getVarianteProducto()
                    .getPlataforma();

            html.append("<tr>");

            html.append("<td style='padding:12px; border:1px solid #ddd;'>");
            html.append(producto.getNombreProducto());
            html.append("</td>");

            html.append("<td style='padding:12px; border:1px solid #ddd; text-align:center;'>");
            html.append(region.getNombreRegion());
            html.append("</td>");

            html.append("<td style='padding:12px; border:1px solid #ddd; text-align:center;'>");
            html.append(plataforma.getNombrePlataforma());
            html.append("</td>");

            html.append("<td style='padding:12px; border:1px solid #ddd; text-align:right;'>");
            html.append(ordenLlave.getPrecioPagado());
            html.append("</td>");

            html.append("</tr>");
        }

        html.append("</tbody>");
        html.append("</table>");

        // KEYS
        html.append("<h2 style='margin-top:30px;'>Tus claves</h2>");

        html.append("<p>");
        html.append("Estas son las claves adquiridas con esta orden. ");
        html.append("Guárdalas en un lugar seguro.");
        html.append("</p>");

        for (OrdenLlave ordenLlave : orden.getOrdenLlaves()) {

            Producto producto = ordenLlave
                    .getLlave()
                    .getVarianteProducto()
                    .getProducto();

            html.append("<div style='margin:15px 0; padding:15px; background-color:#f8f9fa; border:1px solid #ddd; border-radius:6px;'>");

            html.append("<p style='margin:0 0 8px 0;'>");
            html.append("<strong>");
            html.append(producto.getNombreProducto());
            html.append("</strong>");
            html.append("</p>");

            html.append("<div style='padding:12px; background-color:#e9ecef; border-radius:5px; font-family:monospace; font-size:16px;'>");

            html.append(ordenLlave
                    .getLlave()
                    .getLlave());

            html.append("</div>");

            html.append("</div>");
        }

        // TOTAL
        html.append("<div style='margin-top:30px; padding:20px; background-color:#e9f7ef; border-radius:6px; text-align:right;'>");

        html.append("<strong style='font-size:20px;'>");
        html.append("Total: ");
        html.append(orden.getPrecioTotal());
        html.append("</strong>");

        html.append("</div>");

        html.append("<hr style='margin-top:30px;'>");

        html.append("<p style='text-align:center; color:#666;'>");
        html.append("Gracias por confiar en FideGameStore.");
        html.append("</p>");

        html.append("</div>");
        html.append("</div>");

        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    public void enviarCorreoOrden(Orden orden) {
        String html = generarHtmlOrden(orden);
        enviarCorreoHtml(
                orden.getUsuario().getCorreo(),
                "Orden #" + orden.getIdOrden() + " - FideGameStore",
                html
        );
    }
}
