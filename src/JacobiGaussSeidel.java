import static java.lang.Math.abs;
import java.awt.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author Rhabi Mendoza
 */

public class JacobiGaussSeidel extends javax.swing.JFrame {
    // Create model variable
    DefaultTableModel model;
   
    public JacobiGaussSeidel() {      
        // Frame components
        initComponents();
        
        // Table layout
        TableLayout();
        
        // Create model to access table
        model = (DefaultTableModel) tbl_results.getModel();
    }
    
    public void TableLayout(){  
        // Acess table header
        JTableHeader head = tbl_results.getTableHeader();
        
        // Change table header font
        head.setFont(new Font("Dialog", Font.BOLD, 20));
        
        // Change table header font color
        head.setForeground(new Color(6,164,20));
        
        // Change table header background color
        head.setBackground(new Color(255,255,255));
             
        // Change table header text alignment
        ((DefaultTableCellRenderer)head.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
    
        // Change row height
        tbl_results.setRowHeight(tbl_results.getRowHeight() + 20);
       
        // Change column width
        tbl_results.getColumnModel().getColumn(0).setPreferredWidth(50);
        tbl_results.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbl_results.getColumnModel().getColumn(2).setPreferredWidth(200);
        tbl_results.getColumnModel().getColumn(3).setPreferredWidth(200);
        tbl_results.getColumnModel().getColumn(4).setPreferredWidth(200);
        tbl_results.getColumnModel().getColumn(5).setPreferredWidth(200);
    }
    
    public void Solve(String m){
        // Get input
        String s_size = (txt_size.getText() + "").trim();
        String s_error = (txt_error.getText() + "").trim();
        String s_equations = (txta_equations.getText() + "").trim();
        
        // Check if all fields have input
        if(s_size.length() == 0 || s_error.length() == 0 || s_equations.length() == 0){
            JOptionPane.showMessageDialog(null, "Please input size, error, and equations.");
            return;
        }
        
        // Create variable for size and error
        int size = 0;
        double error = 0;
        
        // Check if size and error is a numeric value
        try{
            size = Integer.parseInt(s_size);
            error = Double.parseDouble(s_error);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Please input numeric values in size and error.");
            return;
        }
        
        // Create variable for number of lines of input in equation
        String nl[] = s_equations.split("\\n");
        
        // Check if lines input of input in equation is same as size
        if(nl.length != size){
            JOptionPane.showMessageDialog(null, "Please input " + size + " equations.");
            return;
        }
        
        // Create variable to store contants
        double values[][] = new double[size][size + 1];

        // Check if number of constants is same as size plus one and all contants are numeric values
        try{
            int ind = 0;
            for(String line : s_equations.split("\\n")){            
                String splt[] = line.split("\\s+");            
                for(int ctr = 0; ctr < size + 1; ctr++){
                    values[ind][ctr] = Double.parseDouble(splt[ctr]);
                }            
                ind++;
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Please input " + (size + 1) + " numeric constants per equation separated by space.");
            return;
        }
        
        // Check if equation is diagonally dominant
        for(int ctr = 0; ctr < size; ctr++){
            int sum = 0;         
            for(int ctr2 = 0; ctr2 < size; ctr2++){
                sum += Math.abs(values[ctr][ctr2]);
            }           
            sum -= Math.abs(values[ctr][ctr]);           
            if(Math.abs(values[ctr][ctr]) < sum){
                JOptionPane.showMessageDialog(null, "Please input a diagonally dominant equation.");
                return;
            }
        }
        
        // Check if error is between zero to one hundred only
        if(error > 100){
            JOptionPane.showMessageDialog(null, "Please input error between 0-100.");
            return;
        }
        
        // Check if size is greater than one
        if(size <= 1){
            JOptionPane.showMessageDialog(null, "Number of equations should be >= 2.");
            return;
        }

        // Create array for previous, current, and error values
        double prev[] = new double[size];
        double curr[] = new double[size];
        double err[] = new double[size];
        
        // Fill previous and current array with zero
        Arrays.fill(prev, 0);
        Arrays.fill(curr, 0);
        
        // Create variable for error average and iteration counter
        double ave = 100;
        int counter = 0;
       
        // Iterate until error average is less than error and iteration counter is greater than one hundred
        while(ave >= error && counter < 100){
            
            // Increment counter
            counter++;
            
            // Set error average to zero
            ave = 0;
            
            // If jacobi button was pressed
            if(m.equals("JACOBI")){
                for(int ctr = 0; ctr < size; ctr++){
                    for(int ctr2 = 0; ctr2 < size + 1; ctr2++){
                        if(ctr != ctr2 && ctr2 != size){
                            curr[ctr] += ((values[ctr][ctr2] * -1) * prev[ctr2]);
                        }
                        if(ctr2 == size){
                            curr[ctr] += ((values[ctr][ctr2]));
                        }
                    }                   
                    curr[ctr] = curr[ctr] / values[ctr][ctr];
                }
            }
            
            // If gauss seider button was pressed
            if(m.equals("GAUSSSEIDEL")){           
                double temp[] = new double[size];
                temp = prev.clone();              
                for(int ctr = 0; ctr < size; ctr++){
                    for(int ctr2 = 0; ctr2 < size + 1; ctr2++){
                        if(ctr != ctr2 && ctr2 != size){
                            curr[ctr] += ((values[ctr][ctr2]) * -1) * temp[ctr2];                        
                        }
                        if(ctr2 == size){
                            curr[ctr] += ((values[ctr][ctr2]));
                        }
                    }
                    curr[ctr] = curr[ctr] / values[ctr][ctr];
                    temp[ctr] = curr[ctr];
                }
            }
            
            // Get error per variable and error average
            for(int ctr = 0; ctr < size; ctr++){
                err[ctr] = abs((curr[ctr] - prev[ctr]) / curr[ctr]);
                ave = err[ctr] + ave;
            }
            
            // Calculate error average
            ave = ave / size;
            
            // Get string value of result to display in error
            String result = (ave >= error)? "Fail": "Pass";
            
            // Get array structure and contents to string
            String p = Arrays.toString(prev);
            String c = Arrays.toString(curr);
            String e = Arrays.toString(err);
            
            // Display data to table
            model.addRow(new Object[]{counter, p, c, e, ave, result});
            
            // Create variable to store current values
            String ans = "";
            
            // Add current values to ans string and place current values to previous values
            for(int ctr = 0; ctr < size; ctr++){
                ans += curr[ctr] + "\n";
                prev[ctr] = curr[ctr];
               
            }
            
            // Display answers to answer text area
            txta_answers.setText(ans);

            // Fill array curr with 0
            Arrays.fill(curr, 0);
        } 
    }
     
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        main = new javax.swing.JPanel();
        equation = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txt_size = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_error = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txta_equations = new javax.swing.JTextArea();
        btn_jacobi = new javax.swing.JButton();
        btn_gasei = new javax.swing.JButton();
        answer = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txta_answers = new javax.swing.JTextArea();
        clear = new javax.swing.JPanel();
        btn_clear = new javax.swing.JButton();
        table = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_results = new javax.swing.JTable();
        minimize = new javax.swing.JLabel();
        close = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Systems of Equation");
        setUndecorated(true);

        main.setBackground(new java.awt.Color(230, 245, 240));
        main.setForeground(new java.awt.Color(230, 245, 240));
        main.setPreferredSize(new java.awt.Dimension(990, 837));

        equation.setBackground(new java.awt.Color(255, 255, 255));
        equation.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(6, 164, 120)));
        equation.setForeground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(6, 164, 120));
        jLabel7.setText("Size");

        txt_size.setBackground(new java.awt.Color(230, 245, 240));
        txt_size.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txt_size.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(6, 164, 120)));

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(6, 164, 120));
        jLabel4.setText("Error");

        txt_error.setBackground(new java.awt.Color(230, 245, 240));
        txt_error.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txt_error.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(6, 164, 120)));

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(6, 164, 120));
        jLabel11.setText("Equations");

        txta_equations.setBackground(new java.awt.Color(230, 245, 240));
        txta_equations.setColumns(20);
        txta_equations.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txta_equations.setRows(5);
        jScrollPane2.setViewportView(txta_equations);

        btn_jacobi.setBackground(new java.awt.Color(6, 164, 120));
        btn_jacobi.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btn_jacobi.setForeground(new java.awt.Color(255, 255, 255));
        btn_jacobi.setText("Jacobi");
        btn_jacobi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_jacobiActionPerformed(evt);
            }
        });

        btn_gasei.setBackground(new java.awt.Color(6, 164, 120));
        btn_gasei.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btn_gasei.setForeground(new java.awt.Color(255, 255, 255));
        btn_gasei.setText("Gauss Seidel");
        btn_gasei.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_gaseiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout equationLayout = new javax.swing.GroupLayout(equation);
        equation.setLayout(equationLayout);
        equationLayout.setHorizontalGroup(
            equationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(equationLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(equationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(equationLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addGroup(equationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(equationLayout.createSequentialGroup()
                                .addComponent(btn_jacobi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_gasei))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(equationLayout.createSequentialGroup()
                        .addGroup(equationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel4))
                        .addGap(77, 77, 77)
                        .addGroup(equationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_size)
                            .addComponent(txt_error))))
                .addGap(0, 20, Short.MAX_VALUE))
        );
        equationLayout.setVerticalGroup(
            equationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(equationLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(equationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_size, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(equationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_error, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(equationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(equationLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(equationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_gasei)
                    .addComponent(btn_jacobi))
                .addGap(14, 14, 14))
        );

        answer.setBackground(new java.awt.Color(255, 255, 255));
        answer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(6, 164, 120)));
        answer.setForeground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(6, 164, 120));
        jLabel10.setText("Answer");

        txta_answers.setEditable(false);
        txta_answers.setBackground(new java.awt.Color(230, 245, 240));
        txta_answers.setColumns(20);
        txta_answers.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txta_answers.setRows(5);
        jScrollPane3.setViewportView(txta_answers);

        javax.swing.GroupLayout answerLayout = new javax.swing.GroupLayout(answer);
        answer.setLayout(answerLayout);
        answerLayout.setHorizontalGroup(
            answerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(answerLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(answerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        answerLayout.setVerticalGroup(
            answerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(answerLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        clear.setBackground(new java.awt.Color(255, 255, 255));
        clear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(6, 164, 120)));

        btn_clear.setBackground(new java.awt.Color(6, 164, 120));
        btn_clear.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btn_clear.setForeground(new java.awt.Color(255, 255, 255));
        btn_clear.setText("Clear");
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout clearLayout = new javax.swing.GroupLayout(clear);
        clear.setLayout(clearLayout);
        clearLayout.setHorizontalGroup(
            clearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, clearLayout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );
        clearLayout.setVerticalGroup(
            clearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clearLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(btn_clear)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        table.setBackground(new java.awt.Color(255, 255, 255));
        table.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(6, 164, 120)));
        table.setForeground(new java.awt.Color(255, 255, 255));

        tbl_results.setBackground(new java.awt.Color(230, 245, 240));
        tbl_results.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        tbl_results.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "K", "Previous Values", "Current Values", "Errors", "Error Average", "Result"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbl_results);

        javax.swing.GroupLayout tableLayout = new javax.swing.GroupLayout(table);
        table.setLayout(tableLayout);
        tableLayout.setHorizontalGroup(
            tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tableLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1442, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        tableLayout.setVerticalGroup(
            tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tableLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        minimize.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        minimize.setForeground(new java.awt.Color(6, 164, 120));
        minimize.setText("-");
        minimize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                minimizeMouseClicked(evt);
            }
        });

        close.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        close.setForeground(new java.awt.Color(6, 164, 120));
        close.setText("x");
        close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout mainLayout = new javax.swing.GroupLayout(main);
        main.setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainLayout.createSequentialGroup()
                .addGap(159, 159, 159)
                .addComponent(equation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(answer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(minimize)
                        .addGap(18, 18, 18)
                        .addComponent(close)
                        .addGap(21, 21, 21))
                    .addGroup(mainLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainLayout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(table, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        mainLayout.setVerticalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainLayout.createSequentialGroup()
                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainLayout.createSequentialGroup()
                        .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainLayout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addComponent(answer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(close)
                                    .addComponent(minimize))
                                .addGap(79, 79, 79)
                                .addComponent(clear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 69, Short.MAX_VALUE))
                    .addGroup(mainLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(equation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(table, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(main, javax.swing.GroupLayout.DEFAULT_SIZE, 1515, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(main, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
 
    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        // Clear all fields
        txt_size.setText("");
        txt_error.setText("");
        txta_equations.setText("");
        txta_answers.setText("");
        
        // Delete all row
        model.setRowCount(0);
    }//GEN-LAST:event_btn_clearActionPerformed

    private void closeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeMouseClicked
        // Exit frame
        System.exit(0);
    }//GEN-LAST:event_closeMouseClicked

    private void minimizeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizeMouseClicked
        // Minimize frame
        this.setState(Frame.ICONIFIED);
    }//GEN-LAST:event_minimizeMouseClicked

    private void btn_gaseiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_gaseiActionPerformed
        // Create string for method of calculating
        String method = "GAUSSSEIDEL";

        // Pass string to method solve
        Solve(method);
    }//GEN-LAST:event_btn_gaseiActionPerformed

    private void btn_jacobiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_jacobiActionPerformed
        // Create string for method of calculating
        String method = "JACOBI";

        // Pass string to method solve
        Solve(method);
    }//GEN-LAST:event_btn_jacobiActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JacobiGaussSeidel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel answer;
    private javax.swing.JButton btn_clear;
    private javax.swing.JButton btn_gasei;
    private javax.swing.JButton btn_jacobi;
    private javax.swing.JPanel clear;
    private javax.swing.JLabel close;
    private javax.swing.JPanel equation;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel main;
    private javax.swing.JLabel minimize;
    private javax.swing.JPanel table;
    private javax.swing.JTable tbl_results;
    private javax.swing.JTextField txt_error;
    private javax.swing.JTextField txt_size;
    private javax.swing.JTextArea txta_answers;
    private javax.swing.JTextArea txta_equations;
    // End of variables declaration//GEN-END:variables
}