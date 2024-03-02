import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TextEditor extends JFrame implements ActionListener {
    JTextArea textArea;
    JMenuBar menuBar;
    JMenu file, edit;
    JMenuItem openFile, saveFile, exitFile, color, size;
    JComboBox fontBox;
    TextEditor()
    {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1280, 720);
        this.setTitle("Text Editor JAVA-TARIK");
        this.setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setLayout(new FlowLayout());
        textArea.setPreferredSize(new Dimension(1200, 720));
        textArea.setFont(new Font("Arial", Font.PLAIN, 20));
        textArea.setLineWrap(true);
        textArea.setMargin(new Insets(10, 10, 10, 10));

        menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        file = new JMenu("File");
        edit = new JMenu("Edit");
        menuBar.add(file);
        menuBar.add(edit);

        openFile = new JMenuItem("Open File");
        saveFile = new JMenuItem("Save File");
        exitFile = new JMenuItem("Exit File");

        openFile.addActionListener(this);
        saveFile.addActionListener(this);
        exitFile.addActionListener(this);

        color = new JMenuItem("Color");
        size = new JMenuItem("Size");

        color.addActionListener(this);
        size.addActionListener(this);

        file.add(openFile);
        file.add(saveFile);
        file.add(exitFile);

        edit.add(color);
        edit.add(size);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        fontBox = new JComboBox(fonts);
        fontBox.setSelectedItem("Arial");
        fontBox.addActionListener(this);

        this.add(menuBar, BorderLayout.NORTH);
        this.add(textPanel, BorderLayout.CENTER);
        menuBar.add(fontBox);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openFile) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
            fileChooser.setFileFilter(filter);

            int response = fileChooser.showOpenDialog(null);
            if(response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner fileIn = null;

                try {
                    fileIn = new Scanner(file);
                    if(file.isFile()) {
                        while(fileIn.hasNextLine()) {
                            String line = fileIn.nextLine()+"\n";
                            textArea.append(line);
                        }
                    }

                } catch (FileNotFoundException err) {
                    err.getMessage();
                }

                finally {
                    assert fileIn != null;
                    fileIn.close();
                }
            }

        } else if (e.getSource() == saveFile) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));

            int response = fileChooser.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File file;
                PrintWriter fileOut = null;

                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    fileOut = new PrintWriter(file);
                    fileOut.println(textArea.getText());
                }
                catch (FileNotFoundException err) {
                    err.getMessage();
                }
                finally {
                    assert fileOut != null;
                    fileOut.close();
                }
            }

        } else if (e.getSource() == exitFile) {
            System.exit(0);

        } else if (e.getSource() == color) {
            JColorChooser colorChooser = new JColorChooser();

            Color color = colorChooser.showDialog(null, "Choose color", Color.BLACK);
            textArea.setForeground(color);

        } else if (e.getSource() == size) {
            JFrame windowEvent = new JFrame();
            JSpinner spinnerSizeText = new JSpinner();

            windowEvent.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            windowEvent.setSize(70, 70);
            windowEvent.setTitle("Text Size");
            windowEvent.setLocationRelativeTo(null);

            spinnerSizeText.setPreferredSize(new Dimension(50, 25));
            spinnerSizeText.setValue(20);
            spinnerSizeText.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent changeEvent) {
                    textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) spinnerSizeText.getValue()));
                }
            });

            windowEvent.add(spinnerSizeText);
            windowEvent.setVisible(true);

        } else if (e.getSource()==fontBox) {
            textArea.setFont(new Font((String)fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
        }
    }
}
