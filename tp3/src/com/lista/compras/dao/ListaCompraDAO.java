package com.lista.compras.dao;

import com.lista.compras.model.ListaCompra;

import java.util.ArrayList;
import java.util.List;
//Clase para acceder a los datos de las listas de compras
public class ListaCompraDAO {
    private List<ListaCompra> listasCompras;

    public ListaCompraDAO() {
        listasCompras = new ArrayList<>();
    }
    // Metodo para obtener todas las listas de compras (encapsulamiento)
    public List<ListaCompra> obtenerListasCompras() {
        return listasCompras;
    }
    // Metodo para agregar una lista de compras (encapsulamiento)
    public void agregarListaCompra(ListaCompra listaCompra) {
        listasCompras.add(listaCompra);
    }
}



