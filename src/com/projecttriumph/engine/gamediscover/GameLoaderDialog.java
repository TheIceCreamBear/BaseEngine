package com.projecttriumph.engine.gamediscover;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameLoaderDialog {
	// TODO (Question Mark?)
	public static GameCandidate show(List<GameCandidate> candidates) {
		JPanel panel = new JPanel(new GridLayout(2, 1));
		JLabel prompt = new JLabel("Select the game you wish to play");
		panel.add(prompt);
		JComboBox<GameCandidate> chooser = new JComboBox<GameCandidate>(candidates.toArray(new GameCandidate[candidates.size()]));
		panel.add(chooser);
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Select Game", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			return chooser.getItemAt(chooser.getSelectedIndex());
		} else {
			return null;
		}
	}
}