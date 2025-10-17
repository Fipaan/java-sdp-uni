package lfs.gui;

import lfs.*;
import lfs.gui.elems.*;
import lfs.gui.elems.builder.*;
import lfs.print.*;
import lfs.utils.*;
import lfs.errors.*;
import lfs.gui.wrapper.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;
import java.util.ArrayList;

public class App {
    public Dimension size = new Dimension(800, 600);
    public final FRectangle LABEL_RECT = new FRectangle();
    public Font FONT;
    public JFrame frame;
    public FTextbox textbox;
    public RContainer<JLabel> title = new RContainer<>();
    public FOList studentList;
    public ArrayList<StudentPortalFacade> students = new ArrayList<>();
    public StudentPortalFacade currentStudent;

    public <T extends Component> T updateFontSize(T comp, float size) {
        comp.setFont(FONT.deriveFont(size));
        return comp;
    }
    
    public void updateOnResize() {
        final double tW = 0.66;
        FGUI.modifyBoundsAndReset(size, title, rect -> rect
            .scale (tW,  0.1)
            .scaleL(0.5, 0.03)
            .centerX()
        );
        textbox.modifyBoundsAndReset(rect -> rect
            .scale (tW,  0.1)
            .scaleL(0.5, 0.2)
            .centerX()
        );
        studentList.modifyBoundsAndReset(size, rect -> rect
            .scale (tW,  0.1)
            .scaleL(0.5, 0.3)
            .centerX()
        );
        
        float fontSize = FGUI.getBaseFontSize(size);
        updateFontSize(title.obj, fontSize);
        updateFontSize(studentList.getComboBox(), fontSize * 0.7f);
    }

    private void initTitle() {
        title.obj = new JLabel("Hello, Student!", JLabel.CENTER);
        title.obj.setForeground(Color.WHITE);
        title.obj.setFont(FONT);
        title.obj.setVerticalAlignment(SwingConstants.TOP);
        title.obj.setHorizontalAlignment(SwingConstants.CENTER);
        
        frame.add(title.obj);
    }
    private void initTextbox() {
        textbox = new FTextboxBuilder()
            .setParent(frame, size)
            .setTextField(20, Color.BLACK, 0.65f)
            .setButton("Submit", 0.3f, text -> {
                if (ArrayUtils.findElement(students, student -> student.getName().equals(text)) != null) {
                    FGUI.notifyWarn("Student %s already exists!", text);
                    return;
                }
                studentList.addElement(text);
                students.add(new StudentPortalFacade(text));
                FGUI.notifyInfo("Succesfully added student: %s", text);
            })
            .setHint("Student name...", Color.GRAY)
            .setHintBorder(0.07f, 0.05f)
            .setFont(FONT, 0.8f)
            .build();
    }
    private void initStudentList() {
        studentList = new FOList(frame, opt -> {
                JOptionPane.showMessageDialog(
                    null,
                    "Your choice: " + opt,
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
                );
        })
            .addElement("(none)")
            .addElement("Foo")
            .addElement("Bar")
            .addElement("Baz")
            .removeElement("Bar");
    }

    public void init() {
        FONT  = new Font("Arial", Font.PLAIN, 48);
        frame = new JFrame("Aboba");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setLayout(null);
        
        initTitle();
        initTextbox();
        initStudentList();
        
        frame.setSize(size.width, size.height);
        frame.setLocationRelativeTo(null);
        updateOnResize();
        frame.getContentPane().setBackground(new Color(0xFF181818));

        FGUI.bindKey(frame, "ESCAPE", (Object e) -> frame.dispose());

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                size.setSize(frame.getSize());
                updateOnResize();
            }
        });

        // Mouse listener on the frame
        frame.getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                frame.requestFocus();
            }
        });

        frame.setVisible(true);
    }
}
