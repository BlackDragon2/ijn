package ui;

import javax.swing.JPanel;

import documentmap.document.Document;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class DocumentInfoPanel extends JPanel {
	private static final long serialVersionUID = 7989366696951548221L;
	private Document document;

	public DocumentInfoPanel(Document document) {
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setSize(190,50);
		this.document = document;

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel("Autor:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(5, 5, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);

		JLabel authorLabel = new JLabel(document == null ? "" : document.getAuthor());
		lblNewLabel.setLabelFor(authorLabel);
		GridBagConstraints gbc_authorLabel = new GridBagConstraints();
		gbc_authorLabel.fill = GridBagConstraints.BOTH;
		gbc_authorLabel.weightx = 0.1;
		gbc_authorLabel.insets = new Insets(5, 5, 5, 5);
		gbc_authorLabel.gridx = 1;
		gbc_authorLabel.gridy = 0;
		add(authorLabel, gbc_authorLabel);

		JLabel lblNewLabel_1 = new JLabel("Tytu\u0142:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(5, 5, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		add(lblNewLabel_1, gbc_lblNewLabel_1);

		JLabel titleLabel = new JLabel(document == null ? "" : document.getTitle());
		lblNewLabel_1.setLabelFor(titleLabel);
		GridBagConstraints gbc_titleLabel = new GridBagConstraints();
		gbc_titleLabel.weightx = 0.1;
		gbc_titleLabel.fill = GridBagConstraints.BOTH;
		gbc_titleLabel.insets = new Insets(5, 5, 5, 5);
		gbc_titleLabel.gridx = 1;
		gbc_titleLabel.gridy = 1;
		add(titleLabel, gbc_titleLabel);
	}
}