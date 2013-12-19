package ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import documentmap.document.Document;

public class DocumentInfoPanel extends JPanel {
	private static final long serialVersionUID = 7989366696951548221L;

	public DocumentInfoPanel(List<Document> documents) {
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setSize(250, 50);

		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);

		int gridx = 0;
		int gridy = 0;
//		for (Document document : documents) {
		Document document = documents.get(0);
			JLabel lblNewLabel = new JLabel("Autor:");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(5, 5, 5, 5);
			gbc_lblNewLabel.gridx = gridx;
			gbc_lblNewLabel.gridy = gridy;
			add(lblNewLabel, gbc_lblNewLabel);

			JLabel authorLabel = new JLabel(document == null ? "" : document.getAuthor());
			lblNewLabel.setLabelFor(authorLabel);
			GridBagConstraints gbc_authorLabel = new GridBagConstraints();
			gbc_authorLabel.fill = GridBagConstraints.HORIZONTAL;
			gbc_authorLabel.weightx = 0.1;
			gbc_authorLabel.insets = new Insets(5, 5, 5, 5);
			gbc_authorLabel.gridx = gridx + 1;
			gbc_authorLabel.gridy = gridy;
			add(authorLabel, gbc_authorLabel);

			JLabel lblNewLabel_1 = new JLabel("Tytu\u0142:");
			GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
			gbc_lblNewLabel_1.insets = new Insets(5, 5, 5, 5);
			gbc_lblNewLabel_1.gridx = gridx;
			gbc_lblNewLabel_1.gridy = gridy + 1;
			add(lblNewLabel_1, gbc_lblNewLabel_1);

			JLabel titleLabel = new JLabel(document == null ? "" : document.getTitle());
			lblNewLabel_1.setLabelFor(titleLabel);
			GridBagConstraints gbc_titleLabel = new GridBagConstraints();
			gbc_titleLabel.weightx = 0.1;
			gbc_titleLabel.fill = GridBagConstraints.HORIZONTAL;
			gbc_titleLabel.insets = new Insets(5, 5, 5, 5);
			gbc_titleLabel.gridx = gridx + 1;
			gbc_titleLabel.gridy = gridy + 1;
			add(titleLabel, gbc_titleLabel);

			gridx += 2;
			gridy += 2;
//		}
	}
}