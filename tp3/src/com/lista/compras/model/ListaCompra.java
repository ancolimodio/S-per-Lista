package com.lista.compras.model;

import java.util.ArrayList;
import java.util.List;
//Clase que representa una lista de compras
public class ListaCompra {
    private int id;
    private String nombre;
    private List<Producto> productos;

    public ListaCompra(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.productos = new ArrayList<>();
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

    public List<Producto> getProductos() {
        return productos;
    }
    
    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
    
    // Metodo para agregar un producto a la lista
    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }
    // Metodo para editar un producto de la lista
    public void editarProducto(Producto producto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getNombre().equals(producto.getNombre())) {
                productos.set(i, producto);
                break;
            }
        }
    }
    // Metodo para e un producto de la lista
    public void eliminarProducto(Producto producto) {
        productos.remove(producto);
    }

    public Producto getProductoByName(String nombre) {
        for (Producto producto : productos) {
            if (producto.getNombre().equals(nombre)) {
                return producto;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return nombre;
    }
}



