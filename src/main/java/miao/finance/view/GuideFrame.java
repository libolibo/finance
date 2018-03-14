package miao.finance.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;

public class GuideFrame extends JFrame{

	public static String LEFT_PATH = "/Users/libo/Desktop/2017至2018年银行帐 3.3.xls";
	public static String RIGHT_PATH = "/Users/libo/Desktop/2017至2018年银行帐.xls";
	
	private static int FRAME_WIDTH = 300;
	private static int FRAME_HEIGHT = 200;
	
	private JButton openLeft;
	private JButton openRight;
	
	public static void main(String[] args) {  
	    new GuideFrame();  
	}  
	
	public GuideFrame(){  
		this.setLayout(new GridLayout(1, 2));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		
		this.setLocation((int)(screenSize.getWidth() / 2 - FRAME_WIDTH / 2), 
				(int)(screenSize.getHeight() / 2 - FRAME_HEIGHT / 2));
		
	   
		openLeft = new JButton("打开文件");
		openRight = new JButton("打开文件");
	    this.add(openLeft);  
	    this.add(openRight);  
	    
	    openLeft.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc=new JFileChooser();  

				jfc.setFileFilter(new FileFilter() {
					@Override
					public String getDescription() {
						return null;
					}
					
					@Override
					public boolean accept(File f) {
						return f.getPath().endsWith(".xls");
					}
				});
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
				jfc.showDialog(new JLabel(), "选择");  
				File file=jfc.getSelectedFile();  
			
				if(null != file){
					LEFT_PATH = file.getPath();
					openLeft.setText(file.getName());
			    }
			}
		});
	    
	    openRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc=new JFileChooser();  

				jfc.setFileFilter(new FileFilter() {
					@Override
					public String getDescription() {
						return null;
					}
					
					@Override
					public boolean accept(File f) {
						return f.getPath().endsWith(".xls");
					}
				});
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
				jfc.showDialog(new JLabel(), "选择");  
				File file=jfc.getSelectedFile();  
			
				if(null != file){
					RIGHT_PATH = file.getPath();
					GuideFrame.this.setVisible(false);
					new MainFrame();
			    }
			}
		});
	    
	    this.setVisible(true);  
	    this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);  
	}  
}
