import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

public class ChangeDateFile extends JFrame {

    private final JFormattedTextField createDateTxt;
    static private File f;
    static private String command;

    public static void main(String[] args) {
        new ChangeDateFile();
        String filePath = (args[1].substring(0, (args[1].length()))).replace("\\", "\\\\");
        command = args[0];
        System.out.println(command);
        f = new File(filePath);

    }

    public String getTextFromFormatedTextField () {
        return createDateTxt.getText();
    }

    ChangeDateFile () {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400,200);
        setLocationRelativeTo(null);
        JPanel changeCreateDate = new JPanel();
        changeCreateDate.setLayout(new BorderLayout());
        createDateTxt = tamplateMethodEntry();
        JLabel dateFormat = new JLabel("Введите дату:");
        JButton button = new JButton("Изменить");
        button.addActionListener((ActionEvent e) -> {
            try {
                System.out.println(command);
                if (command.equals("1")) {
                    changeCreateDate(f, getTextFromFormatedTextField());
                } else changeLastModifiedDate(f, getTextFromFormatedTextField());
            } catch (Exception ignore) {}
            finally {
                System.exit(0);
            }
        });
        changeCreateDate.add(createDateTxt,BorderLayout.CENTER);
        changeCreateDate.add(dateFormat, BorderLayout.NORTH);
        changeCreateDate.add(button, BorderLayout.SOUTH);
        add(changeCreateDate);
        setVisible(true);
        pack();
    }


    JFormattedTextField tamplateMethodEntry() {
        JFormattedTextField text;
        MaskFormatter dateMask = null;
        try {
            dateMask = new MaskFormatter("##-##-####-##-##-##");
            dateMask.setPlaceholderCharacter('_');
            dateMask.setValidCharacters("0123456789");
        } catch (ParseException e) {
        }
        text = new JFormattedTextField(dateMask);
        text.setPreferredSize(new Dimension(150,25));
        return text;
    }

    public void changeCreateDate(File file, String newDate) throws IOException, ParseException {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        Date localDate = format.parse(newDate);

        if (!file.exists()) {
            System.out.println("One of files is failed");
            return;
        }
        FileTime time = FileTime.fromMillis(localDate.getTime());
        BasicFileAttributeView attributes = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
        attributes.setTimes(null, null, time);

    }

    public void changeLastModifiedDate(File file, String newDate) throws IOException,ParseException {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        Date localDate = format.parse(newDate);

        if (!file.exists()) {
            System.out.printf("Файл не найден.", file.getCanonicalPath());
        }
        FileTime time = FileTime.fromMillis(localDate.getTime());
        BasicFileAttributeView attributes = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);
        attributes.setTimes(time, time,null);
    }
}
