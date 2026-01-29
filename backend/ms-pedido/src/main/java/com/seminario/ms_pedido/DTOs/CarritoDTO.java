package com.seminario.ms_pedido.DTOs;

import lombok.Data;
import java.util.ArrayList;

@Data
public class CarritoDTO {
    private String id;
    private String vendedorId;
    private String clienteId;
    private Double montoTotal;
    private Double montoTotalProductos;
    private ArrayList<DetalleCarritoDTO> detallesCarrito;
}
