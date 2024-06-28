package com.lista.compras.model;
//Clase que representa un producto
public class Producto {
	private int id;
    private String nombre;
    private int cantidad;
    private String categoria;
    private boolean comprado;

 // Constructor completo
    public Producto(int id, String nombre, int cantidad, String categoria, boolean comprado) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.comprado = comprado;
    }

    // Constructor sin ID
    public Producto(String nombre, int cantidad, String categoria, boolean comprado) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.comprado = comprado;
    }
    // Getters y setters para los atributos privados (encapsulamiento)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String isComprado() {
        return comprado? "Comprado":"Faltante";
    }

    public void setComprado(boolean comprado) {
        this.comprado = comprado;
    }

    @Override
    public String toString() {
        return nombre + " - " + cantidad + " - " + categoria + " - "  + isComprado();
    }
}

