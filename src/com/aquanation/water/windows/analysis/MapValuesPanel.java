package com.aquanation.water.windows.analysis;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.aquanation.water.AquaNation;
import com.aquanation.water.helpers.DataSet;
import com.aquanation.water.windows.MainFrame;

/**
 * 
 * @author Matthew Meacham
 * 
 *         The panel that will contian the table displaying the values for the entire map
 *
 */
public class MapValuesPanel {

	@SuppressWarnings("unused")
	private JFrame parentFrame;

	private JPanel contentPane;

	public MapValuesPanel(JFrame parentFrame) {
		this.parentFrame = parentFrame;

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		JTable table = new JTable();
		DefaultTableModel tableModel = new DefaultTableModel();
		String[] stateNames = new String[AquaNation.getStates().size()];

		for (int i = 0; i < AquaNation.getStates().size(); i++) {
			stateNames[i] = AquaNation.getStates().get(i).getFullName();
		}
		tableModel.addColumn("State", stateNames);

		for (DataSet dataSet : AquaNation.getStates().get(0).getWaterValues()) {
			Double[] values = new Double[AquaNation.getStates().size()];
			for (int i = 0; i < AquaNation.getStates().size(); i++) {
				values[i] = AquaNation.getStates().get(i).getWaterValue(dataSet.getDataType());
			}
			tableModel.addColumn(dataSet.getDataType(), values);
		}

		table.setModel(tableModel);
		table.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				int column = e.getColumn();
				TableModel model = (TableModel) e.getSource();

				Double value = Double.valueOf((String) model.getValueAt(row, column));

				AquaNation.getStates().get(row).setWaterValue(model.getColumnName(column), value, AquaNation.getStates().get(row).getWaterValues().size() - 1);
				MainFrame.getImagePanel().repaint();
				MainFrame.colorer.fillImage(MainFrame.getDefaultFullMapImage());
			}
		});

		scrollPane.setViewportView(table);
	}

	public void render() {

	}

	public JPanel getContentPane() {
		return contentPane;
	}

}
