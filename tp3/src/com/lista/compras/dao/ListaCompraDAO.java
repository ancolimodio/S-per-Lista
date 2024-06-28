package com.lista.compras.dao;

import com.lista.compras.model.ListaCompra;
import com.lista.compras.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListaCompraDAO {
    private Connection connection;

    public ListaCompraDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void agregarListaCompra(ListaCompra listaCompra) {
        String sql = "INSERT INTO lista_compra (nombre) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, listaCompra.getNombre());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    listaCompra.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ListaCompra> obtenerListasCompras() {
        List<ListaCompra> listas = new ArrayList<>();
        String sql = "SELECT * FROM lista_compra";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ListaCompra lista = new ListaCompra(rs.getInt("id"), rs.getString("nombre"));
                listas.add(lista);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listas;
    }

    public void agregarProducto(int listaId, Producto producto) {
        String sql = "INSERT INTO producto (nombre, cantidad, categoria, lista_compra_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, producto.getNombre());
            stmt.setInt(2, producto.getCantidad());
            stmt.setString(3, producto.getCategoria());
            stmt.setInt(4, listaId);
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    producto.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Producto> obtenerProductos(int listaId) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM producto WHERE lista_compra_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, listaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Producto producto = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("cantidad"),
                        rs.getString("categoria"),
                        rs.getBoolean("comprado")
                    );
                    productos.add(producto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }
    public void editarProducto(Producto producto) {
        String sql = "UPDATE producto SET nombre = ?, cantidad = ?, categoria = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setInt(2, producto.getCantidad());
            stmt.setString(3, producto.getCategoria());
            stmt.setInt(4, producto.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarProducto(int productoId) {
        String sql = "DELETE FROM producto WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void actualizarEstadoProducto(int idProducto, boolean comprado) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE producto SET comprado = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, comprado);
            stmt.setInt(2, idProducto);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}
