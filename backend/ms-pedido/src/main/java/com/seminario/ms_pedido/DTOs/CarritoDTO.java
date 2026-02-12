package com.seminario.ms_pedido.DTOs;

import java.util.ArrayList;

import lombok.Data;

@Data
public class CarritoDTO {
    private String vendedorId;
    private Double montoTotal;
    private Double montoTotalProductos;
    private ArrayList<DetalleCarritoDTO> detallesCarrito;
}
