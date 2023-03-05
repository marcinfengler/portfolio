import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

@SuppressWarnings(value = "unchecked")
public class Main extends JFrame {

    public Main()
    {
        initComponents();
    }
    public void initComponents()
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane,ex.getMessage(), "Błąd",JOptionPane.ERROR_MESSAGE);
            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            JOptionPane.showMessageDialog(rootPane,ex.getMessage(), "Błąd",JOptionPane.ERROR_MESSAGE);
            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            JOptionPane.showMessageDialog(rootPane,ex.getMessage(), "Błąd",JOptionPane.ERROR_MESSAGE);
            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(rootPane,ex.getMessage(), "Błąd",JOptionPane.ERROR_MESSAGE);
            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        SwingUtilities.updateComponentTreeUI(this);
        this.setTitle("Konwersja współrzędnych e-mapy");
        if(currentDate.after(endOfTimes)){
            this.setBounds(300,300,400,200);
            JOptionPane.showMessageDialog(rootPane,"Licencja oprogramowania wygasła.", "Błąd",JOptionPane.ERROR_MESSAGE);
        } else{
            this.getContentPane().add(panelOfButtons, BorderLayout.NORTH);
            panelOfFields.add(scrollerOfFiles, BorderLayout.WEST);
            panelOfFields.add(scrollPaneForTextField, BorderLayout.EAST);
            this.getContentPane().add(panelOfFields,BorderLayout.CENTER );
            panelOfButtons.add(addFilesButton);
            panelOfButtons.add(rmvFilesButton);
            panelOfButtons.add(conversionButton);
            panelOfButtons.add(copyToClipboardButton);
            pack();
            addFilesButton.addActionListener((ActionEvent e) -> {
                addFileMethod();
            });
            rmvFilesButton.addActionListener((ActionEvent e) -> {
                removeFileMethod();
            });
            conversionButton.addActionListener((ActionEvent e) -> {
                convertFileMethod();
            });
            copyToClipboardButton.addActionListener((ActionEvent e) -> {
                copyToClipboardMethod();
            });
        }
        this.setDefaultCloseOperation(3);
    }
    private void addFileMethod()
    {
        localFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        localFileChooser.setDialogTitle("Dodaj pobrane koordynaty do konwersji");
        localFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        localFileChooser.setMultiSelectionEnabled(true);

        int tmp = localFileChooser.showDialog(rootPane, "Dodaj");

        if(tmp == JFileChooser.APPROVE_OPTION)
        {
            File[] selectedPaths = localFileChooser.getSelectedFiles();
            for(int i=0;i<selectedPaths.length;i++)
            {
                if(!isFileAlreadySelected(selectedPaths[i].getPath()))
                    localListModel.addElement(selectedPaths[i]);
            }
        }
    }
    private boolean isFileAlreadySelected(String pathInQuestion)
    {
        for(int j=0;j<localListModel.getSize();j++)
            if(((File)localListModel.get(j)).getPath().equals(pathInQuestion))return true;
        return false;
    }
    private void removeFileMethod()
    {
        for(int j=0;j<localListModel.getSize();j++)
        {
            if(listOfFiles.isSelectedIndex(j))
            {
                localListModel.remove(j);
                j--;
            }
        }
    }
    private void convertFileMethod()
    {
        for (int k=0; k<localListModel.getSize(); k++)
        {
            try
            {
                textField1.requestFocus();
                textField1.append("_PLINE"+"\n");
                BufferedReader reader = new BufferedReader(new FileReader((File)localListModel.get(k)));
                String line = " ";
                while((line=reader.readLine())!=null)
                {
                    //textField1.append(line);
                    String secondCoordinate;
                    String firstCoordinate;
                    StringTokenizer token=new StringTokenizer(line," ");
                    secondCoordinate=token.nextToken();
                    firstCoordinate=token.nextToken();
                    textField1.requestFocus();
                    textField1.append(firstCoordinate+","+secondCoordinate+"\n");
                }
                textField1.append("\n");
            }
            catch (IOException e)
            {
                JOptionPane.showMessageDialog(rootPane,e.getMessage());
            }
            //if(!((File)localListModel.get(k)).isDirectory())

        }
    }
    private void copyToClipboardMethod()
    {
        textField1.requestFocus();
        textField1.selectAll();
        textField1.copy();
    }
    public static void main(String[] args) {
        new Main().setVisible(true);
    }
    private DefaultListModel localListModel = new DefaultListModel()
    {

    };
    private JButton addFilesButton = new JButton("Dodaj pliki");
    private JButton rmvFilesButton = new JButton("Usuń pliki");
    private JButton conversionButton = new JButton("Konwertuj");
    private JButton copyToClipboardButton = new JButton("Kopiuj");
    private JPanel panelOfButtons = new JPanel();
    private JPanel panelOfFields = new JPanel();
    private JTextArea textField1 = new JTextArea(8,50);
    private JScrollPane scrollPaneForTextField = new JScrollPane(textField1);
    private JList listOfFiles = new JList(localListModel);
    private JScrollPane scrollerOfFiles = new JScrollPane(listOfFiles);
    private JFileChooser localFileChooser = new JFileChooser();
    private GregorianCalendar currentDate = new GregorianCalendar();
    //simplest of DRMs
    private GregorianCalendar endOfTimes = new GregorianCalendar(2023,3,15);
}
