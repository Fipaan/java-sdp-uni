package lfs.gui;

import lfs.*;
import lfs.courses.*;
import lfs.decorators.*;
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
    public Color BACKGROUND = new Color(0xFF181818);
    public Color FOREGROUND = new Color(0xFFE7E7E7);
    public Font FONT;
    public JFrame frame;
    public FTextbox textbox;
    public RContainer<JLabel> title = new RContainer<>();
    public RContainer<JCheckBox> checkboxCertificate = new RContainer<>();
    public RContainer<JCheckBox> checkboxGamification = new RContainer<>();
    public RContainer<JCheckBox> checkboxMentor = new RContainer<>();
    boolean isCertificate = false, isGamification = false, isMentor = false;
    public FOList studentList;
    public ArrayList<StudentPortalFacade> students = new ArrayList<>();
    public StudentPortalFacade currentStudent;
    public FOList currentStudentCourses;
    public FOList courseList;
    public String selectedCourse = "None";
    public RContainer<JButton> submitNewCourse = new RContainer<>();
    public boolean enrolling = false;

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
        double sncW = enrolling ? 0.3 : 0.0;
        currentStudentCourses.modifyBoundsAndReset(size, rect -> rect
            .scale (tW,  0.1)
            .scaleL(0.5, 0.4)
            .centerX()
            .scaleW(1 - sncW)
        );
        FGUI.modifyBoundsAndReset(size, submitNewCourse, rect -> rect
            .scale (tW,  0.1)
            .scaleL(0.5, 0.4)
            .centerX()
            .scaleW(sncW, true)
        );
        courseList.modifyBoundsAndReset(size, rect -> rect
            .scale (tW,  0.1)
            .scaleL(0.5, 0.5)
            .centerX()
        );
        FGUI.modifyBoundsAndReset(size, checkboxCertificate, rect -> rect
            .scale (tW,  0.1)
            .scaleL(0.5, 0.6)
            .centerX()
        );
        FGUI.modifyBoundsAndReset(size, checkboxGamification, rect -> rect
            .scale (tW,  0.1)
            .scaleL(0.5, 0.7)
            .centerX()
        );
        FGUI.modifyBoundsAndReset(size, checkboxMentor, rect -> rect
            .scale (tW,  0.1)
            .scaleL(0.5, 0.8)
            .centerX()
        );
        
        float fontSize = FGUI.getBaseFontSize(size);
        updateFontSize(title.obj,                           fontSize);
        updateFontSize(studentList.getComboBox(),           fontSize * 0.7f);
        updateFontSize(currentStudentCourses.getComboBox(), fontSize * 0.7f);
        updateFontSize(courseList.getComboBox(),            fontSize * 0.7f);
        updateFontSize(submitNewCourse.obj,                 fontSize * 0.7f);
        updateFontSize(checkboxCertificate.obj,             fontSize * 0.7f);
        updateFontSize(checkboxGamification.obj,            fontSize * 0.7f);
        updateFontSize(checkboxMentor.obj,                  fontSize * 0.7f);
    }

    private void initTitle() {
        title.obj = new JLabel("Hello, Student!", JLabel.CENTER);
        title.obj.setForeground(FOREGROUND);
        title.obj.setFont(FONT);
        title.obj.setVerticalAlignment(SwingConstants.TOP);
        title.obj.setHorizontalAlignment(SwingConstants.CENTER);
        
        frame.add(title.obj);
    }
    private void updateCurrentStudent() {
        DefaultComboBoxModel<String> model;
        model = currentStudentCourses.getComboBoxModel();
        for (int i = model.getSize() - 1; i > 0; i--) {
            model.removeElementAt(i);
        }
        for (Course course : currentStudent.getCourses()) {
            model.addElement(course.getName());
        }
        currentStudentCourses.setVisible(true);
    }
    private void drawEnroll() {
        updateOnResize();
        if (!enrolling) {
            courseList.getComboBox().setVisible(false);
            checkboxCertificate.obj.setVisible(false);
            checkboxGamification.obj.setVisible(false);
            checkboxMentor.obj.setVisible(false);
            submitNewCourse.obj.setVisible(false);
        } else {
            isCertificate = false;
            isGamification = false;
            isMentor = false;
            courseList.getComboBox().setVisible(true);
            checkboxCertificate.obj.setVisible(true);
            checkboxGamification.obj.setVisible(true);
            checkboxMentor.obj.setVisible(true);
            submitNewCourse.obj.setVisible(true);
        }
    }
    private void initTextbox() {
        textbox = new FTextboxBuilder()
            .setParent(frame, size)
            .setTextField(20, Color.BLACK, 0.65f)
            .setButton("Submit", 0.3f, text -> {
                String name = text.trim();
                if (name.length() == 0)  {
                    FGUI.notifyWarn("Student name is empty!");
                    return;
                }
                if (name.length() < 4)  {
                    FGUI.notifyWarn("Student name is too small!");
                    return;
                }
                if (name.length() > 30)  {
                    FGUI.notifyWarn("Student name is too big!");
                    return;
                }
                if (!StringUtils.isValidName(name)) {
                    FGUI.notifyWarn("Invalid characters!");
                    return;
                }
                if (ArrayUtils.findElement(students, student -> student.getName().equals(name)) != null) {
                    FGUI.notifyWarn("Student %s already exists!", name);
                    return;
                }
                studentList.addElement(name);
                students.add(new StudentPortalFacade(name));
                FGUI.notifyInfo("Succesfully added student: %s", name);
                textbox.setText("");
            })
            .setHint("Student name...", Color.GRAY)
            .setHintBorder(0.07f, 0.05f)
            .setFont(FONT, 0.8f)
            .build();
    }
    private void toggleCheckbox(RContainer<JCheckBox> checkbox, String name) {
        boolean result = checkbox.obj.isSelected();
        if (name.equals("Certificate")) isCertificate = result;
        else if (name.equals("Gamification")) isGamification = result;
        else if (name.equals("Mentor")) isMentor = result;
        else throw FError.New("Unknown checkbox: " + name);
    }
    private <T extends Container> void addActionListenerToCheckbox(T parent, RContainer<JCheckBox> checkbox, String name) {
        checkbox.obj = new JCheckBox(name);
        checkbox.obj.setBackground(BACKGROUND);
        checkbox.obj.setForeground(FOREGROUND);

        checkbox.obj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleCheckbox(checkbox, name);
            }
        });
        checkbox.obj.setVisible(false);
        parent.add(checkbox.obj);
    }
    private void initStudentList() {
        studentList = new FOList(frame, name -> {
            StudentPortalFacade student = ArrayUtils.findElement(students, s -> s.getName().equals(name));
            if (student == null) {
                FGUI.notifyWarn("Student %s doesn't exist! Deleting...", name);
                studentList.removeElement(name);
                studentList.setSelectedIndex(0);
                return;
            }
            currentStudent = student;
            updateCurrentStudent();
            enrolling = true;
            drawEnroll();
        }, () -> {
            currentStudent = null;
            currentStudentCourses.setVisible(false);
            enrolling = false;
            drawEnroll();
        }, "(none)");

        currentStudentCourses = new FOList(frame, name -> {
            Course course = ArrayUtils.findElement(currentStudent.getCourses(), c -> c.getName().equals(name));
            if (course == null) {
                FGUI.notifyWarn("Course %s doesn't exist! Deleting...", name);
                currentStudentCourses.removeElement(name);
                currentStudentCourses.setSelectedIndex(0);
                return;
            }
            enrolling = false;
            drawEnroll();
        }, () -> {
            enrolling = true;
            drawEnroll();
        }, "Enroll")
            .setVisible(false);
        courseList = new FOList(frame, name -> {
            selectedCourse = name;
        }, null, "None")
            .addElement("Math")
            .addElement("Programming")
            .setVisible(false);
            
        submitNewCourse.obj = new JButton("Enroll!");
        submitNewCourse.obj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String opt = (String)currentStudentCourses.getSelectedItem();
                if (opt != "Enroll") {
                    FGUI.notifyWarn("Can't enroll existing course! (%s)", opt);
                    return;
                }
                Course course;
                if (selectedCourse == "Math") {
                    course = new MathCourse();
                } else if (selectedCourse == "Programming") {
                    course = new ProgrammingCourse();
                } else throw FError.New("Unknown selectedCourse: " + selectedCourse);
                if (isCertificate)  course = new CertificateDecorator(course);
                if (isGamification) course = new GamificationDecorator(course);
                if (isMentor)       course = new MentorSupportDecorator(course);
                currentStudent.enrollInCourse(course);
            }
        });
        submitNewCourse.obj.setVisible(false);
        frame.add(submitNewCourse.obj);
        addActionListenerToCheckbox(frame, checkboxCertificate,  "Certificate");
        addActionListenerToCheckbox(frame, checkboxGamification, "Gamification");
        addActionListenerToCheckbox(frame, checkboxMentor,       "Mentor");
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
        frame.getContentPane().setBackground(BACKGROUND);

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
