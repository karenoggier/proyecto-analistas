package com.seminario.ms_usuarios.dto.eventos_ms_pedidio;

import lombok.Data;

@Data
public class DireccionResponseEvent {
    private String id;//se envia con este campo en nulo
    private String provincia;
    private String localidad;
    private String calle;//se envia con este campo en nulo
    private String numero; //se envia con este campo en nulo
    private String codigoPostal; //se envia con este campo en nulo
    private String observaciones;//se envia con este campo en nulo
    private Double latitud;
    private Double longitud;

}
