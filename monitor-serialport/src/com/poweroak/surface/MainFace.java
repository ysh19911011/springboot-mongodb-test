package com.poweroak.surface;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.border.EmptyBorder;

import com.poweroak.util.HexUtil;
import com.poweroak.util.Util;

public class MainFace extends JFrame {

	private JPanel contentPane;
	private final JPanel panel_1 = new JPanel();
	private JLabel textField;
	private JComboBox<String> selectBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFace frame = new MainFace();
					frame.setVisible(true);
					frame.setTitle("德兰明海");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("rawtypes")
	public MainFace() {
		URL url = getClass().getResource("logo.png");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 809, 376);
		setLocationRelativeTo(null);
		setIconImage(Toolkit.getDefaultToolkit().getImage(url));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBounds(0, 259, 783, 52);
		contentPane.add(panel_2);
		
		JButton button = new JButton("确定");
		button.setBounds(329, 10, 124, 32);
		panel_2.add(button);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 63, 783, 52);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel pathLable = new JLabel();
		pathLable.setBounds(507, 10, 236, 32);
		panel.add(pathLable);
		
		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.setBounds(289, 10, 204, 32);
		btnNewButton_1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser=new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.showSaveDialog(null);
				File file=chooser.getSelectedFile();
				if(Util.checkNotNull(file)) {
					if(file.getName().endsWith(".bin")||file.getName().endsWith(".hex")) {
						pathLable.setText(file.getAbsolutePath());
						readAndSave(file);
					}else {
						Tips tip=new Tips("此文件无法解析");
						tip.setVisible(true);
					}
					
				}
				
			}

			private void readAndSave(File file) {
				List<String>list=new HexUtil(512,file).parseHexFile();
				for(int i=188;i<list.size();i++) {
//					if(i==203) {
						System.out.println(list.get(i));
//					}
				}
				System.out.println(list.size());
			}
		});
		panel.add(btnNewButton_1);
		
		JLabel label = new JLabel("请添加文件:");
		label.setBounds(85, 10, 115, 38);
		panel.add(label);
		panel_1.setBounds(0, 146, 783, 58);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		textField = new JLabel("请选择下传方式:");
		textField.setBounds(86, 9, 115, 38);
		panel_1.add(textField);
		
		selectBox = new JComboBox<String>();
		selectBox.setModel(new DefaultComboBoxModel(new String[] {"请选择"}));
		selectBox.addItem("111");
		selectBox.addItem("222");
		selectBox.addItem("333");
		selectBox.setBounds(291, 9, 201, 38);
		panel_1.add(selectBox);
	}
}
