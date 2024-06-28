package com.lista.compras.model;
//Clase que representa un producto
public class Producto {
	private int id;
    private String nombre;
    private int cantidad;
    private String categoria;

 // Constructor completo
    public Producto(int id, String nombre, int cantidad, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.categoria = categoria;
    }

    // Constructor sin ID
    public Producto(String nombre, int cantidad, String categoria) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.categoria = categoria;
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

    @Override
    public String toString() {
        return nombre + " - " + cantidad + " - " + categoria;
    }
}

