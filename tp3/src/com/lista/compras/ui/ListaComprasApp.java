package com.lista.compras.ui;

import com.lista.compras.dao.ListaCompraDAO;
import com.lista.compras.model.ListaCompra;
import com.lista.compras.model.Producto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class ListaComprasApp {
    private JFrame frame;
    private JPanel panel;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem crearListaMenuItem;
    private JMenuItem verHistorialMenuItem;
    private ListaCompraDAO listaCompraDAO;

    public ListaComprasApp() {
        listaCompraDAO = new ListaCompraDAO();
        frame = new JFrame("Lista de Compras");
        panel = new JPanel();
        menuBar = new JMenuBar();
        menu = new JMenu("Opciones");
        crearListaMenuItem = new JMenuItem("Crear Lista");
        verHistorialMenuItem = new JMenuItem("Ver Historial");

        menu.add(crearListaMenuItem);
        menu.add(verHistorialMenuItem);
        menuBar.add(menu);

        frame.setJMenuBar(menuBar);
        frame.add(panel);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        crearListaMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioCrearLista();
            }
        });

        verHistorialMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarHistorialCompras();
            }
        });
    }

    private void mostrarFormularioCrearLista() {
        JFrame crearListaFrame = new JFrame("Crear Lista de Compras");
        crearListaFrame.setSize(300, 150);
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JLabel nombreLabel = new JLabel("Nombre:");
        JTextField nombreTextField = new JTextField();
        JButton crearButton = new JButton("Crear");

        panel.add(nombreLabel);
        panel.add(nombreTextField);
        panel.add(new JLabel());
        panel.add(crearButton);

        crearListaFrame.add(panel);
        crearListaFrame.setVisible(true);

        crearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombreLista = nombreTextField.getText();
                if (!nombreLista.isEmpty()) {
                    ListaCompra nuevaLista = new ListaCompra(0, nombreLista);
                    listaCompraDAO.agregarListaCompra(nuevaLista);
                    JOptionPane.showMessageDialog(crearListaFrame, "Lista creada exitosamente!");
                    crearListaFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(crearListaFrame, "El nombre de la lista no puede estar vacío.");
                }
            }
        });
    }

    private void mostrarHistorialCompras() {
        JFrame historialFrame = new JFrame("Historial de Compras");
        historialFrame.setSize(400, 300);
        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<ListaCompra> listModel = new DefaultListModel<>();
        JList<ListaCompra> listaHistorial = new JList<>(listModel);
        listaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaHistorial.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof ListaCompra) {
                    ((JLabel) renderer).setText(((ListaCompra) value).getNombre());
                }
                return renderer;
            }
        });

        List<ListaCompra> listasCompras = listaCompraDAO.obtenerListasCompras();
        for (ListaCompra lista : listasCompras) {
            listModel.addElement(lista);
        }

        listaHistorial.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    ListaCompra selectedList = listaHistorial.getSelectedValue();
                    if (selectedList != null) {
                    	mostrarDetalleListaCompra(selectedList);
                    }
                }
            }
        });

        panel.add(new JScrollPane(listaHistorial), BorderLayout.CENTER);
        historialFrame.add(panel);
        historialFrame.setVisible(true);
    }

    private void mostrarFormularioAgregarProducto(ListaCompra listaCompra, DefaultListModel<Producto> listModel) {
        JFrame agregarProductoFrame = new JFrame("Agregar Producto");
        agregarProductoFrame.setSize(300, 200);
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JLabel nombreLabel = new JLabel("Nombre:");
        JTextField nombreTextField = new JTextField();
        JLabel cantidadLabel = new JLabel("Cantidad:");
        JTextField cantidadTextField = new JTextField();
        JLabel categoriaLabel = new JLabel("Categoría:");
        String[] categorias = {"Carnicería", "Verdulería", "Limpieza", "Almacén", "Fresco"};
        JComboBox<String> categoriaComboBox = new JComboBox<>(categorias);
        JButton agregarButton = new JButton("Agregar");

        panel.add(nombreLabel);
        panel.add(nombreTextField);
        panel.add(cantidadLabel);
        panel.add(cantidadTextField);
        panel.add(categoriaLabel);
        panel.add(categoriaComboBox);
        panel.add(new JLabel());
        panel.add(agregarButton);

        agregarProductoFrame.add(panel);
        agregarProductoFrame.setVisible(true);

        agregarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombreProducto = nombreTextField.getText();
                String cantidadStr = cantidadTextField.getText();
                String categoria = (String) categoriaComboBox.getSelectedItem();

                if (!nombreProducto.isEmpty() && !cantidadStr.isEmpty() && categoria != null) {
                    try {
                        int cantidad = Integer.parseInt(cantidadStr);
                        Producto nuevoProducto = new Producto(0, nombreProducto, cantidad, categoria,false);
                        listaCompraDAO.agregarProducto(listaCompra.getId(), nuevoProducto);
                        listModel.addElement(nuevoProducto);
                        JOptionPane.showMessageDialog(agregarProductoFrame, "Producto agregado exitosamente!");
                        agregarProductoFrame.dispose();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(agregarProductoFrame, "La cantidad debe ser un número entero.");
                    }
                } else {
                    JOptionPane.showMessageDialog(agregarProductoFrame, "Todos los campos deben ser completados.");
                }
            }
        });
    }
    
    private void mostrarFormularioEditarProducto(Producto producto, DefaultListModel<Producto> listModel) {
        JFrame editarProductoFrame = new JFrame("Editar Producto");
        editarProductoFrame.setSize(300, 200);
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JLabel nombreLabel = new JLabel("Nombre:");
        JTextField nombreTextField = new JTextField(producto.getNombre());
        JLabel cantidadLabel = new JLabel("Cantidad:");
        JTextField cantidadTextField = new JTextField(String.valueOf(producto.getCantidad()));
        JLabel categoriaLabel = new JLabel("Categoría:");
        String[] categorias = {"Carnicería", "Verdulería", "Limpieza", "Almacén", "Fresco"};
        JComboBox<String> categoriaComboBox = new JComboBox<>(categorias);
        categoriaComboBox.setSelectedItem(producto.getCategoria());
        JButton guardarButton = new JButton("Guardar");
        JButton marcarCompradoButton = new JButton("Marcar como Comprado");

        panel.add(nombreLabel);
        panel.add(nombreTextField);
        panel.add(cantidadLabel);
        panel.add(cantidadTextField);
        panel.add(categoriaLabel);
        panel.add(categoriaComboBox);
        panel.add(guardarButton);
        panel.add(marcarCompradoButton);

        editarProductoFrame.add(panel);
        editarProductoFrame.setVisible(true);

        guardarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombreProducto = nombreTextField.getText();
                String cantidadStr = cantidadTextField.getText();
                String categoria = (String) categoriaComboBox.getSelectedItem();

                if (!nombreProducto.isEmpty() && !cantidadStr.isEmpty() && categoria != null) {
                    try {
                        int cantidad = Integer.parseInt(cantidadStr);
                        producto.setNombre(nombreProducto);
                        producto.setCantidad(cantidad);
                        producto.setCategoria(categoria);
                        listaCompraDAO.editarProducto(producto);
                        listModel.setElementAt(producto, listModel.indexOf(producto));
                        JOptionPane.showMessageDialog(editarProductoFrame, "Producto editado exitosamente!");
                        editarProductoFrame.dispose();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(editarProductoFrame, "La cantidad debe ser un número entero.");
                    }
                } else {
                    JOptionPane.showMessageDialog(editarProductoFrame, "Todos los campos deben ser completados.");
                }
            }
        });
        
        marcarCompradoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Producto productoSeleccionado = producto;
                if (productoSeleccionado != null) {
                    productoSeleccionado.setComprado(true);
                    try {
                        listaCompraDAO.actualizarEstadoProducto(productoSeleccionado.getId(), true);
                        listModel.setElementAt(producto, listModel.indexOf(producto));
                        JOptionPane.showMessageDialog(editarProductoFrame, "Producto editado exitosamente!");
                        editarProductoFrame.dispose();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(editarProductoFrame, "Error al actualizar estado del producto: " + ex.getMessage());
                    }
            }
            }});
    }

    private void mostrarConfirmacionEliminarProducto(Producto producto, DefaultListModel<Producto> listModel) {
        int confirm = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar este producto?", "Eliminar Producto", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            listaCompraDAO.eliminarProducto(producto.getId());
            listModel.removeElement(producto);
            JOptionPane.showMessageDialog(null, "Producto eliminado exitosamente!");
        }
    }

    private void mostrarDetalleListaCompra(ListaCompra listaCompra) {
        JFrame detalleFrame = new JFrame("Detalle de la Lista de Compras: " + listaCompra.getNombre());
        detalleFrame.setSize(500, 400);
        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<Producto> listModel = new DefaultListModel<>();
        JList<Producto> productoJList = new JList<>(listModel);
        for (Producto producto : listaCompraDAO.obtenerProductos(listaCompra.getId())) {
            listModel.addElement(producto);
        }
        productoJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(productoJList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton agregarProductoButton = new JButton("Agregar Producto");
        JButton editarProductoButton = new JButton("Editar Producto");
        JButton eliminarProductoButton = new JButton("Eliminar Producto");

        buttonPanel.add(agregarProductoButton);
        buttonPanel.add(editarProductoButton);
        buttonPanel.add(eliminarProductoButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        detalleFrame.add(panel);
        detalleFrame.setVisible(true);

        agregarProductoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioAgregarProducto(listaCompra, listModel);
            }
        });

        editarProductoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Producto productoSeleccionado = productoJList.getSelectedValue();
                if (productoSeleccionado != null) {
                    mostrarFormularioEditarProducto(productoSeleccionado, listModel);
                } else {
                    JOptionPane.showMessageDialog(detalleFrame, "Seleccione un producto para editar.");
                }
            }
        });

        eliminarProductoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Producto productoSeleccionado = productoJList.getSelectedValue();
                if (productoSeleccionado != null) {
                    mostrarConfirmacionEliminarProducto(productoSeleccionado, listModel);
                } else {
                    JOptionPane.showMessageDialog(detalleFrame, "Seleccione un producto para eliminar.");
                }
            }
        });
    }


    public static void main(String[] args) {
        new ListaComprasApp();
    }
}

