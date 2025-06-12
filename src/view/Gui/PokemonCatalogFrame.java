package view.Gui;

import models.names.Name;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;

public class PokemonCatalogFrame extends JFrame {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final JComboBox<String> typeFilter;

    public PokemonCatalogFrame() {
        setTitle("Catálogo de Pokémon");
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Crear modelo de tabla
        tableModel = new DefaultTableModel(new Object[]{"Nombre", "Tipo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };

        // Llenar la tabla con los Pokémon
        for (Name name : Name.values()) {
            tableModel.addRow(new Object[]{name.name(), name.getType()});
        }

        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Configurar ordenamiento
        sorter.setComparator(0, Comparator.naturalOrder());
        sorter.setComparator(1, Comparator.naturalOrder());

        // Panel de filtros
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Filtro por tipo
        filterPanel.add(new JLabel("Filtrar por tipo:"));
        typeFilter = new JComboBox<>();
        typeFilter.addItem("Todos"); // Opción para mostrar todos
        Arrays.stream(Name.values())
                .map(Name::getType)
                .distinct()
                .forEach(typeFilter::addItem);
        typeFilter.addActionListener(e -> applyFilters());
        filterPanel.add(typeFilter);

        // Filtro por nombre
        filterPanel.add(new JLabel("Buscar por nombre:"));
        JTextField searchField = new JTextField(15);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { applyFilters(); }
            @Override public void removeUpdate(DocumentEvent e) { applyFilters(); }
            @Override public void changedUpdate(DocumentEvent e) { applyFilters(); }
        });
        filterPanel.add(searchField);

        add(filterPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void applyFilters() {
        String selectedType = (String) typeFilter.getSelectedItem();
        String searchText = ""; // Implementar búsqueda si se agrega campo de búsqueda

        // Crear filtro combinado
        RowFilter<DefaultTableModel, Object> filter = new RowFilter<>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                String name = entry.getStringValue(0).toLowerCase();
                String type = entry.getStringValue(1);

                // Filtrar por tipo
                if (!"Todos".equals(selectedType) && !selectedType.equals(type)) {
                    return false;
                }

                // Filtrar por nombre (si se implementa)
                if (!searchText.isEmpty() && !name.contains(searchText.toLowerCase())) {
                    return false;
                }

                return true;
            }
        };

        sorter.setRowFilter(filter);
    }

    public static void showCatalog() {
        SwingUtilities.invokeLater(() -> {
            PokemonCatalogFrame frame = new PokemonCatalogFrame();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}