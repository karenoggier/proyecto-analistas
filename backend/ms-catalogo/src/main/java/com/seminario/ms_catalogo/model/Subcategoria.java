package com.seminario.ms_catalogo.model;


public enum Subcategoria {
    // --- COMIDAS ---
    HAMBURGUESA(Categoria.COMIDA),
    PIZZA(Categoria.COMIDA),
    EMPANADA(Categoria.COMIDA),
    MILANESA(Categoria.COMIDA),
    PASTA(Categoria.COMIDA),
    PARRILLA(Categoria.COMIDA),
    ENSALADA(Categoria.COMIDA),
    VEGANO(Categoria.COMIDA),
    SUSHI(Categoria.COMIDA),
    POSTRE(Categoria.COMIDA),
    
    // --- BEBIDAS ---
    GASEOSA(Categoria.BEBIDA),
    AGUA(Categoria.BEBIDA),
    CERVEZA(Categoria.BEBIDA),
    VINO(Categoria.BEBIDA),
    TRAGO(Categoria.BEBIDA),
    CAFE(Categoria.BEBIDA);

    // Atributo para vincular con el padre
    private final Categoria categoriaPadre;

    Subcategoria(Categoria categoriaPadre) {
        this.categoriaPadre = categoriaPadre;
    }

    // Método getter para saber el padre
    public Categoria getCategoriaPadre() {
        return categoriaPadre;
    }
    
    // Método útil para validaciones
    public boolean esDeTipo(Categoria categoria) {
        return this.categoriaPadre == categoria;
    }
}
