package com.seminario.ms_pago.service;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.seminario.ms_pago.client.PedidoClient;
import com.seminario.ms_pago.dto.PedidoResponseDTO;
import com.seminario.ms_pago.model.EstadoTransaccion;
import com.seminario.ms_pago.model.MetodoPago;
import com.seminario.ms_pago.model.Pago;
import com.seminario.ms_pago.repository.PagoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PagoService {
    private final PagoRepository pagoRepository;
    private final PedidoClient pedidoClient;

    public Map<String, String> crearPreferencia(String pedidoId) {
        try {
            PedidoResponseDTO pedido = pedidoClient.obtenerPedidoPorId(pedidoId);

            PreferenceClient client = new PreferenceClient();

            List<PreferenceItemRequest> items = new ArrayList<>();

            items.add(PreferenceItemRequest.builder()
                .title("Pedido #" + pedido.getId())
                .quantity(1)
                .unitPrice(pedido.getMontoTotal()) 
                .currencyId("ARS")
                .build());

            PreferenceRequest request = PreferenceRequest.builder()
                .items(items)
                .externalReference(pedidoId)
                .notificationUrl("https://aspen-vicious-neda.ngrok-free.dev/pagoMs/api/pagos/webhook")
                .backUrls(PreferenceBackUrlsRequest.builder()
                        .success("https://aspen-vicious-neda.ngrok-free.dev/cliente/proceso-pedido/paso5") 
                        .pending("https://aspen-vicious-neda.ngrok-free.dev/cliente/proceso-pedido/paso5")
                        .failure("https://aspen-vicious-neda.ngrok-free.dev/cliente/proceso-pedido/paso5")
                        .build())
                .autoReturn("approved") 
                .build();

            // Enviar la solicitud a Mercado Pago
            Preference preference = client.create(request);

            // Guardamos el intento de pago
            Pago pago = new Pago();
            pago.setPedidoId(pedidoId);
            pago.setPreferenciaId(preference.getId());
            pago.setMonto(pedido.getMontoTotal()); 
            pago.setEstado(EstadoTransaccion.PENDIENTE); 
            pago.setFechaCreacion(LocalDateTime.now());
            
            pagoRepository.save(pago);

            return Map.of(
                    "preferenceId", preference.getId(),
                    "url", preference.getInitPoint()
                );

        } catch (MPApiException e) {
            System.out.println("Status: " + e.getStatusCode());
            System.out.println("Response: " + e.getApiResponse().getContent()); 
            throw new RuntimeException("Error en MP: " + e.getApiResponse().getContent());
        } catch (MPException e) {
            e.printStackTrace();
            throw new RuntimeException("Error de conexión: " + e.getMessage());
        }
    }

    @Transactional
    public void procesarNotificacionPago(String paymentId) {
        try {
            PaymentClient client = new PaymentClient();
            Payment payment = client.get(Long.parseLong(paymentId));

            if ("approved".equals(payment.getStatus())) {
                String pedidoId = payment.getExternalReference(); 

                Pago pago = pagoRepository.findByPedidoId(pedidoId)
                    .orElseThrow(() -> new RuntimeException("No se encontró el registro local para el pedido: " + pedidoId));

                pago.setIdMP(paymentId); 
                pago.setEstado(EstadoTransaccion.APROBADO);
                pago.setFechaAprobacion(payment.getDateApproved().toLocalDateTime());
                      
                pago.setMetodoDePago(mapearMetodo(payment.getPaymentTypeId()));
                
                pagoRepository.save(pago); 

                pedidoClient.confirmarPago(pedidoId);
                
                log.info("Pedido {} marcado como PAGADO. Transacción MP: {}", pedidoId, paymentId);
            }
        } catch (Exception e) {
            log.error("Error procesando Webhook de MP para el pago {}: {}", paymentId, e.getMessage());
        }
    }

    private MetodoPago mapearMetodo(String mpType) {
        return switch (mpType) {
            case "credit_card" -> MetodoPago.TARJETA_CREDITO;
            case "debit_card" -> MetodoPago.TARJETA_DEBITO;
            default -> MetodoPago.DINERO_CUENTA;
        };
    }
}