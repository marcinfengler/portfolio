import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

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
            panelOfButtons.add(sanitizeButton);
            panelOfButtons.add(conversionButton);
            panelOfButtons.add(copyToClipboardButton);
            pack();
            addFilesButton.addActionListener((ActionEvent e) -> {
                addFileMethod();
            });
            rmvFilesButton.addActionListener((ActionEvent e) -> {
                removeFileMethod();
            });
            sanitizeButton.addActionListener((ActionEvent e) -> {
                sanitizeInputMethod();
            });
            conversionButton.addActionListener((ActionEvent e) -> {
                convertFileMethod();
            });
            copyToClipboardButton.addActionListener((ActionEvent e) -> {
                copyToClipboardMethod();
            });
            conversionButton.setEnabled(false);
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
    private void sanitizeInputMethod()
    {
        // this code shall provide simple sanitation of imput by removing files that are not .txt files
        for(int j=0;j<localListModel.getSize();j++)
        {
            if(!((File)localListModel.get(j)).getName().substring(((File)localListModel.get(j)).getName().length()-3).equals("txt"))
            {
                localListModel.remove(j);
                j--;
            }
            // else
            // to do: add code that will check if the contents of files formatting is the same as e-mapa export
        }
        // this code shall fix the bug with multiple e-mapa exports within a minute
        for(int j=0;j<localListModel.getSize();j++)
        {
            if(!(((File)localListModel.get(j)).getName().contains(" (") && ((File)localListModel.get(j)).getName().contains(").")))
            {
                ArrayList<File> listOfFilesToDebug = new ArrayList<File>();
                for(int i=0;i<localListModel.getSize();i++)
                {
                    if((!(((File)localListModel.get(j)).getName().equals(((File)localListModel.get(i)).getName()))) && (((File)localListModel.get(j)).getName().substring(0,((File)localListModel.get(j)).getName().length()-4).equals(((File)localListModel.get(i)).getName().substring(0,((File)localListModel.get(j)).getName().length()-4))))
                    {
                        if(listOfFilesToDebug.size()!=0)
                            listOfFilesToDebug.add((File)localListModel.get(i));
                        else
                        {
                            listOfFilesToDebug.add((File)localListModel.get(j));
                            listOfFilesToDebug.add((File)localListModel.get(i));
                        }
                    }
                    if(i==(localListModel.getSize()-1) && listOfFilesToDebug.size()!=0)
                    {
                        listOfFilesToDebug.sort((File o1, File o2) -> {
                            if(o1.getName().length() < o2.getName().length())  return -1;
                            if(o1.getName().length() > o2.getName().length())  return 1;
                            if(o1.getName().length() == o2.getName().length())
                            {
                                int o1Iterator=Integer.parseInt((String)(o1.getName().substring((o1.getName().indexOf('('))+1,(o1.getName().indexOf(')')))));
                                int o2Iterator=Integer.parseInt((o2.getName().substring((o2.getName().indexOf('('))+1,(o2.getName().indexOf(')')))));
                                return o1Iterator-o2Iterator;
                            }
                            return 0;
                        });
                        for(int k=listOfFilesToDebug.size()-1;k>0;k--)
                        {
                            File fileToFix = listOfFilesToDebug.get(k);
                            File fileControl = listOfFilesToDebug.get(k-1);
                            int linesToFix=0;
                            int linesControl=0;
                            try
                            {
                                BufferedReader readerToFix = new BufferedReader(new FileReader(fileToFix));
                                BufferedReader readerControl = new BufferedReader(new FileReader(fileControl));
                                String lineToFix = "";
                                String lineControl = "";
                                while((lineControl=readerControl.readLine())!=null)
                                {
                                    if(((lineToFix=readerToFix.readLine())!=null) && (lineToFix.equals(lineControl)))
                                        linesToFix++;
                                    linesControl++;
                                }
                                // due to small size of files ArrayList is acceptable
                                ArrayList<String> linesAfterFix = new ArrayList<String>();
                                String lineAfterFix = "";
                                while((lineAfterFix=readerToFix.readLine())!=null)
                                {
                                    linesAfterFix.add(lineAfterFix);
                                }
                                if (linesToFix==linesControl)
                                {
                                    BufferedWriter writerToFix = new BufferedWriter(new FileWriter(fileToFix));
                                    for(int l=0;l<linesAfterFix.size();l++)
                                    {
                                        writerToFix.write(linesAfterFix.get(l));
                                        if(l<linesAfterFix.size()-1)writerToFix.newLine();
                                    }
                                    writerToFix.close();
                                }
                            }
                            catch (IOException e)
                            {
                                JOptionPane.showMessageDialog(rootPane,e.getMessage());
                            }
                        }
                    }
                }
            }
        }
        conversionButton.setEnabled(true);
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
    private JButton sanitizeButton = new JButton("Sprawdź i napraw");
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
    private GregorianCalendar endOfTimes = new GregorianCalendar(2023,4,15);
}
