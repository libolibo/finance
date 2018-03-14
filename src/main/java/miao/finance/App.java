package miao.finance;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class App extends JFrame
{
    private JTable table;
    
    public void init(){
        Object[][] data = {{1,2,3},{4,5,6},{7,8,9},{10,11,12},{13,14,15}};
        Object[] column = {"第一列","第二列","第三列"};
        table = new JTable(new DefaultTableModel(data,column));
        
        table.getColumn(column[1]).setCellRenderer(new MyTableCellRenderrer());
        
        JScrollPane pane = new JScrollPane(table);
        this.add(pane);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
    }
    public static void main(String[] args)
    {
        new App().init();
    }
    class MyTableCellRenderrer extends DefaultTableCellRenderer{
        
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row,
                    int column)
            {
                // TODO Auto-generated method stub
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                //隔行换色
                if(row%2 ==0){
                    comp.setBackground(Color.RED);
                }else if(row%2 ==1){
                    comp.setBackground(Color.WHITE);
                }
                return comp;
            }
    }
    
}
