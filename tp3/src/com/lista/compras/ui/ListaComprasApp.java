package com.lista.compras.ui;

import com.lista.compras.dao.ListaCompraDAO;
import com.lista.compras.model.ListaCompra;
import com.lista.compras.model.Producto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
//Clase principal de la aplicacion de lista de compras
public class ListaComprasApp {
    private JFrame frame; // Encapsulamiento: se declara el JFrame como atributo privado
    private JPanel panel;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem crearListaMenuItem;
    private JMenuItem verHistorialMenuItem;
    private ListaCompraDAO listaCompraDAO;

    public ListaComprasApp() {
        listaCompraDAO = new ListaCompraDAO();
        frame = new JFrame("Lista de Compras"); // Encapsulamiento: la ventana principal se encapsula en el atributo frame
        panel = new JPanel();
        menuBar = new JMenuBar();
        menu = new JMenu("Opciones");
        crearListaMenuItem = new JMenuItem("Crear Lista");
        verHistorialMenuItem = new JMenuItem("Ver Historial");

        menu.add(crearListaMenuItem);
        menu.add(verHistorialMenuItem);
        menuBar.add(menu);

        frame.setJMenuBar(menuBar);// Encapsulamiento: se configura la barra de menu en la ventana principal
        frame.add(panel);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        // Manejadores de eventos para las opciones del menu
        crearListaMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioCrearLista(); // Abstraccion: se llama a un metodo para mostrar el formulario de creacion de lista
            }
        });

        verHistorialMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarHistorialCompras(); // Abstraccion: se llama a un metodo para mostrar el historial de compras
            }
        });
    }
    // Metodo para mostrar el formulario de creacion de lista
    private void mostrarFormularioCrearLista() {
        JFrame crearListaFrame = new JFrame("Crear Nueva Lista");
        JPanel crearListaPanel = new JPanel(new GridLayout(2, 2));
        JLabel nombreLabel = new JLabel("Nombre de la Lista:");
        JTextField nombreTextField = new JTextField();
        JButton crearButton = new JButton("Crear");

        crearListaPanel.add(nombreLabel);
        crearListaPanel.add(nombreTextField);
        crearListaPanel.add(new JLabel());  // Espaciador
        crearListaPanel.add(crearButton);

        crearListaFrame.add(crearListaPanel);
        crearListaFrame.setSize(300, 150);
        crearListaFrame.setVisible(true);

        crearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombreLista = nombreTextField.getText();
                if (!nombreLista.isEmpty()) {
                    ListaCompra nuevaLista = new ListaCompra(listaCompraDAO.obtenerListasCompras().size() + 1, nombreLista);
                    listaCompraDAO.agregarListaCompra(nuevaLista);
                    crearListaFrame.dispose();
                    JOptionPane.showMessageDialog(frame, "Lista creada exitosamente!");
                    mostrarFormularioAgregarProducto(nuevaLista);
                } else {
                    JOptionPane.showMessageDialog(crearListaFrame, "El nombre de la lista no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    // Metodo para mostrar el formulario de agregar producto a una lista
    private void mostrarFormularioAgregarProducto(ListaCompra lista) {
        JFrame agregarProductoFrame = new JFrame("Agregar Producto a " + lista.getNombre());
        JPanel agregarProductoPanel = new JPanel(new GridLayout(4, 2));
        JLabel nombreProductoLabel = new JLabel("Nombre del Producto:");
        JTextField nombreProductoTextField = new JTextField();
        JLabel cantidadLabel = new JLabel("Cantidad:");
        JTextField cantidadTextField = new JTextField();
        JLabel categoriaLabel = new JLabel("Categoría:");
        String[] categorias = {"Carnicería", "Verdulería", "Limpieza", "Almacén", "Fresco"};
        JComboBox<String> categoriaComboBox = new JComboBox<>(categorias);
        JButton agregarButton = new JButton("Agregar");

        agregarProductoPanel.add(nombreProductoLabel);
        agregarProductoPanel.add(nombreProductoTextField);
        agregarProductoPanel.add(cantidadLabel);
        agregarProductoPanel.add(cantidadTextField);
        agregarProductoPanel.add(categoriaLabel);
        agregarProductoPanel.add(categoriaComboBox);
        agregarProductoPanel.add(new JLabel());  // Espaciador
        agregarProductoPanel.add(agregarButton);

        agregarProductoFrame.add(agregarProductoPanel);
        agregarProductoFrame.setSize(400, 200);
        agregarProductoFrame.setVisible(true);

        agregarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombreProducto = nombreProductoTextField.getText();
                String cantidadStr = cantidadTextField.getText();
                String categoria = (String) categoriaComboBox.getSelectedItem();

                if (!nombreProducto.isEmpty() && !cantidadStr.isEmpty()) {
                    try {
                        int cantidad = Integer.parseInt(cantidadStr);
                        Producto nuevoProducto = new Producto(nombreProducto, cantidad, categoria);
                        lista.agregarProducto(nuevoProducto);
                        JOptionPane.showMessageDialog(agregarProductoFrame, "Producto agregado exitosamente!");
                        nombreProductoTextField.setText("");
                        cantidadTextField.setText("");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(agregarProductoFrame, "La cantidad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(agregarProductoFrame, "Todos los campos deben estar completos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    // Metodo para mostrar el formulario de edicion de un producto en una lista
    private void mostrarFormularioEditarProducto(ListaCompra lista, Producto producto) {
        JFrame editarProductoFrame = new JFrame("Editar Producto: " + producto.getNombre());
        JPanel editarProductoPanel = new JPanel(new GridLayout(4, 2));
        JLabel nombreProductoLabel = new JLabel("Nombre del Producto:");
        JTextField nombreProductoTextField = new JTextField(producto.getNombre());
        JLabel cantidadLabel = new JLabel("Cantidad:");
        JTextField cantidadTextField = new JTextField(String.valueOf(producto.getCantidad()));
        JLabel categoriaLabel = new JLabel("Categoría:");
        String[] categorias = {"Carnicería", "Verdulería", "Limpieza", "Almacén", "Fresco"};
        JComboBox<String> categoriaComboBox = new JComboBox<>(categorias);
        categoriaComboBox.setSelectedItem(producto.getCategoria());
        JButton guardarButton = new JButton("Guardar");

        editarProductoPanel.add(nombreProductoLabel);
        editarProductoPanel.add(nombreProductoTextField);
        editarProductoPanel.add(cantidadLabel);
        editarProductoPanel.add(cantidadTextField);
        editarProductoPanel.add(categoriaLabel);
        editarProductoPanel.add(categoriaComboBox);
        editarProductoPanel.add(new JLabel());  // Espaciador
        editarProductoPanel.add(guardarButton);

        editarProductoFrame.add(editarProductoPanel);
        editarProductoFrame.setSize(400, 200);
        editarProductoFrame.setVisible(true);

        guardarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nuevoNombre = nombreProductoTextField.getText();
                String nuevaCantidadStr = cantidadTextField.getText();
                String nuevaCategoria = (String) categoriaComboBox.getSelectedItem();

                if (!nuevoNombre.isEmpty() && !nuevaCantidadStr.isEmpty()) {
                    try {
                        int nuevaCantidad = Integer.parseInt(nuevaCantidadStr);
                        producto.setNombre(nuevoNombre);
                        producto.setCantidad(nuevaCantidad);
                        producto.setCategoria(nuevaCategoria);
                        editarProductoFrame.dispose();
                        JOptionPane.showMessageDialog(frame, "Producto editado exitosamente!");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(editarProductoFrame, "La cantidad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(editarProductoFrame, "Todos los campos deben estar completos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    // Metodo para mostrar el historial de compras
    private void mostrarHistorialCompras() {
        JFrame historialFrame = new JFrame("Historial de Compras");
        JPanel historialPanel = new JPanel(new BorderLayout());
        DefaultListModel<ListaCompra> listModel = new DefaultListModel<>();
        for (ListaCompra lista : listaCompraDAO.obtenerListasCompras()) {
            listModel.addElement(lista);
        }
        JList<ListaCompra> listaJList = new JList<>(listModel);

        listaJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaJList.setLayoutOrientation(JList.VERTICAL);

        historialPanel.add(new JScrollPane(listaJList), BorderLayout.CENTER);

        JButton verDetalleButton = new JButton("Ver Detalle");
        historialPanel.add(verDetalleButton, BorderLayout.SOUTH);

        historialFrame.add(historialPanel);
        historialFrame.setSize(400, 300);
        historialFrame.setVisible(true);

        verDetalleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListaCompra listaSeleccionada = listaJList.getSelectedValue();
                if (listaSeleccionada != null) {
                    mostrarDetalleLista(listaSeleccionada);
                } else {
                    JOptionPane.showMessageDialog(historialFrame, "Seleccione una lista para ver los detalles.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    // Metodo para mostrar el detalle de una lista de compras
    private void mostrarDetalleLista(ListaCompra lista) {
        JFrame detalleFrame = new JFrame("Detalle de " + lista.getNombre());
        JPanel detallePanel = new JPanel(new BorderLayout());
        DefaultListModel<Producto> productoListModel = new DefaultListModel<>();
        for (Producto producto : lista.getProductos()) {
            productoListModel.addElement(producto);
        }
        JList<Producto> productoJList = new JList<>(productoListModel);

        productoJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productoJList.setLayoutOrientation(JList.VERTICAL);

        JPanel botonesPanel = new JPanel();
        JButton editarButton = new JButton("Editar Producto");
        JButton eliminarButton = new JButton("Eliminar Producto");

        botonesPanel.add(editarButton);
        botonesPanel.add(eliminarButton);

        detallePanel.add(new JScrollPane(productoJList), BorderLayout.CENTER);
        detallePanel.add(botonesPanel, BorderLayout.SOUTH);

        detalleFrame.add(detallePanel);
        detalleFrame.setSize(400, 300);
        detalleFrame.setVisible(true);

        editarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Producto productoSeleccionado = productoJList.getSelectedValue();
                if (productoSeleccionado != null) {
                    mostrarFormularioEditarProducto(lista, productoSeleccionado);
                } else {
                    JOptionPane.showMessageDialog(detalleFrame, "Seleccione un producto para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Producto productoSeleccionado = productoJList.getSelectedValue();
                if (productoSeleccionado != null) {
                    int confirm = JOptionPane.showConfirmDialog(detalleFrame, "¿Está seguro de que desea eliminar este producto?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        lista.eliminarProducto(productoSeleccionado);
                        productoListModel.removeElement(productoSeleccionado);
                        JOptionPane.showMessageDialog(detalleFrame, "Producto eliminado exitosamente!");
                    }
                } else {
                    JOptionPane.showMessageDialog(detalleFrame, "Seleccione un producto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        new ListaComprasApp();
    }
}


