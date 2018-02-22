package miao.finance.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;

public class GuideFrame extends JFrame implements ActionListener{

	private JButton open=null;  
	public static String CURRENT_PATH;
	
	public static void main(String[] args) {  
	    new GuideFrame();  
	}  
	
	public GuideFrame(){  
	    open=new JButton("打开文件");  
	    this.add(open);  
	    this.setBounds(400, 200, 100, 100);  
	    this.setVisible(true);  
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	    open.addActionListener(this);  
	}  
	
	@Override  
	public void actionPerformed(ActionEvent e) {  
	    // TODO Auto-generated method stub  
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
			CURRENT_PATH = file.getPath();
			this.setVisible(false);

			new MainFrame();
			System.out.println(CURRENT_PATH);
	    }
	}  
}
