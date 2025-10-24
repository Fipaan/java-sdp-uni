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
import java.util.function.*;
import java.util.ArrayList;

public class App {
    public Data data;
    public AppState state = AppState.CHOOSE_STUDENT;

    public ArrayList<StudentPortalFacade> students = new ArrayList<>();
    public StudentPortalFacade currentStudent;
    
    public CourseFactory courseFactory = new CourseFactory();
    public static final String SELECTED_COURSE_NONE = "None";
    public String selectedCourse = SELECTED_COURSE_NONE;
    boolean isCertificate = false, isGamification = false, isMentor = false;

    public Course selectedCourseS = null;


    private final double titleWidth = 0.66;
    private final float  fontRatio  = 0.7f;

    public <T extends Component> T updateFontSize(T comp, float size) {
        comp.setFont(data.font.deriveFont(size));
        return comp;
    }

    public boolean checkState(AppState state) { return this.state == state; }
    public boolean checkState(AppState... states) {
        for (AppState s : states) {
            if (this.state == s) {
                return true;
            }
        }
        return false;
    }
    public void updateOnResizeEnroll(Data data) {
        boolean isEnrolling = checkState(AppState.ENROLL);
        double sncW = isEnrolling ? 0.3 : 0.0;
        data
        .modifyBoundsAndReset("[SELEC] [OLIST] Course", fontRatio, rect -> rect
            .scale (titleWidth,  0.1)
            .scaleL(0.5, 0.4)
            .centerX()
            .scaleW(1 - sncW)
        )
        .modifyBoundsAndReset("[ENROLL] [BTN] New Course", fontRatio, rect -> rect
            .scale (titleWidth,  0.1)
            .scaleL(0.5, 0.4)
            .centerX()
            .scaleW(sncW, true)
        );
        if (!isEnrolling) return;
        data
        .modifyBoundsAndReset("[ENROLL] [OLIST] Available courses", fontRatio, rect -> rect
            .scale (titleWidth,  0.1)
            .scaleL(0.5, 0.5)
            .centerX()
        )
        .modifyBoundsAndReset("[ENROLL] [CB] Certificate", fontRatio, rect -> rect
            .scale (titleWidth,  0.1)
            .scaleL(0.5, 0.6)
            .centerX()
        )
        .modifyBoundsAndReset("[ENROLL] [CB] Gamification", fontRatio, rect -> rect
            .scale (titleWidth,  0.1)
            .scaleL(0.5, 0.7)
            .centerX()
        )
        .modifyBoundsAndReset("[ENROLL] [CB] Mentor", fontRatio, rect -> rect
            .scale (titleWidth,  0.1)
            .scaleL(0.5, 0.8)
            .centerX()
        );
    }
    private void updateOnResizeEnroll() { updateOnResizeEnroll(this.data); }
    public void updateOnResizeStudy(Data data) {
        if (checkState(AppState.STUDY)) {
            if (!selectedCourseS.isStartedLearning()) {
                data
                .modifyBoundsAndReset("[LEARN] [BTN] Start Learning", fontRatio, rect -> rect
                    .scale (titleWidth,  0.1)
                    .scaleL(0.5, 0.5)
                    .centerX()
                )
                .modifyBoundsAndReset("[LEARN] [BTN] Finish Learning", fontRatio, rect -> rect
                    .scale (0)
                    .scaleL(0.5, 0.5)
                    .centerX()
                );
            } else {
                data
                .modifyBoundsAndReset("[LEARN] [BTN] Start Learning", fontRatio, rect -> rect
                    .scale (0)
                    .scaleL(0.5, 0.5)
                    .centerX()
                )
                .modifyBoundsAndReset("[LEARN] [BTN] Finish Learning", fontRatio, rect -> rect
                    .scale (titleWidth,  0.1)
                    .scaleL(0.5, 0.5)
                    .centerX()
                );
            }
        }
    }
    public void updateOnResizeStudy() { updateOnResizeStudy(this.data); }
    public void updateOnResize(Data data) {
        data
        .modifyBoundsAndReset("Title", 1, rect -> rect
            .scale (titleWidth,  0.1)
            .scaleL(0.5, 0.03)
            .centerX()
        )
        .modifyBoundsAndReset("[NONE] [TB] Student name", fontRatio, rect -> rect
            .scale (titleWidth,  0.1)
            .scaleL(0.5, 0.2)
            .centerX()
        )
        .modifyBoundsAndReset("[OLIST] Student", fontRatio, rect -> rect
            .scale (titleWidth,  0.1)
            .scaleL(0.5, 0.3)
            .centerX()
        );
        updateOnResizeEnroll(data);
        updateOnResizeStudy(data); 
    }
    public void updateOnResize() {
        updateOnResize(data);
    }

    private void initTitle(Data data) {
        JLabel title = new JLabel("Hello, Student!", JLabel.CENTER);
        data.put("Title", title);
        title.setForeground(data.foreground);
        title.setFont(data.font);
        title.setVerticalAlignment(SwingConstants.TOP);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        data.frame.add(title);
    }
    private void updateCurrentStudent() {
        DefaultComboBoxModel<String> model;
        FOList olist = data.getOList("[SELEC] [OLIST] Course");
        model = olist.getComboBoxModel();
        for (int i = model.getSize() - 1; i > 0; i--) {
            model.removeElementAt(i);
        }
        for (Course course : currentStudent.getCourses()) {
            model.addElement(course.deliverContent());
        }
        olist.setVisible(true);
    }
    private void setVisibilityForState(Data data, String substring, boolean visible) {
        for (Container cont : data.getContainersBySubstring(substring)) {
            cont.setVisible(visible);
        }
    }
    private void resetEnrollmentFlags() {
        isCertificate = false;
        isGamification = false;
        isMentor = false;
    }
    private void updateState(Data data) {
        setVisibilityForState(data, "[ENROLL]",  checkState(AppState.ENROLL));
        setVisibilityForState(data, "[SELEC]",  !checkState(AppState.CHOOSE_STUDENT));
        setVisibilityForState(data, "[NONE]",    checkState(AppState.CHOOSE_STUDENT));
        setVisibilityForState(data, "[LEARN]",   checkState(AppState.STUDY));
        
        if (checkState(AppState.ENROLL)) {
            resetEnrollmentFlags();
        }
        updateOnResize(data);
    }
    private void updateState() {
        updateState(data);
    }
    private void updateState(AppState state) {
        if (state == AppState.CHOOSE_STUDENT) currentStudent = null;
        if (this.state == AppState.STUDY &&
                 state != AppState.STUDY) selectedCourseS = null;
        this.state = state;
        updateState(data);
    }
    private boolean verifyStudentName(String name) {
        if (name.length() == 0)  {
            FGUI.notifyWarn("Student name is empty!");
            return false;
        }
        if (name.length() < 4)  {
            FGUI.notifyWarn("Student name is too small!");
            return false;
        }
        if (name.length() > 30)  {
            FGUI.notifyWarn("Student name is too big!");
            return false;
        }
        if (!StringUtils.isValidName(name)) {
            FGUI.notifyWarn("Invalid characters!");
            return false;
        }
        if (ArrayUtils.findElement(students, student -> student.getName().equals(name)) != null) {
            FGUI.notifyWarn("Student %s already exists!", name);
            return false;
        }
        return true;
    }
    private void initTextbox(Data data) {
        FTextbox textbox = new FTextboxBuilder()
            .setParent(data.frame, data.screenSize)
            .setTextField(20, Color.BLACK, 0.65f)
            .setButton("Submit", 0.3f, text -> {
                String name = text.trim();
                if (!verifyStudentName(name)) return;
                data.getOList("[OLIST] Student").addElement(name);
                students.add(new StudentPortalFacade(name));
                FGUI.notifyInfo("Succesfully added student: %s", name);
                data.getTextbox("[NONE] [TB] Student name").setText("");
            })
            .setHint("Student name...", Color.GRAY)
            .setHintBorder(0.07f, 0.05f)
            .setFont(data.font, 0.8f)
            .build();
        data.put("[NONE] [TB] Student name", textbox);
    }
    private void toggleCheckbox(JCheckBox checkbox, String name) {
        boolean result = checkbox.isSelected();
        if (name.equals("Certificate")) isCertificate = result;
        else if (name.equals("Gamification")) isGamification = result;
        else if (name.equals("Mentor")) isMentor = result;
        else throw FError.New("Unknown checkbox: " + name);
    }
    private void initStudentList(Data data) {
        final FOList[] studentList = new FOList[1];
        final FOList[] courseList = new FOList[2];

        studentList[0] = new FOList(data.frame, name -> {
            StudentPortalFacade student = ArrayUtils.findElement(students, s -> s.getName().equals(name));
            if (student == null) {
                FGUI.notifyWarn("Student %s doesn't exist! Deleting...", name);
                studentList[0].removeElement(name);
                studentList[0].setSelectedIndex(0);
                return;
            }
            currentStudent = student;
            updateCurrentStudent();
            updateState(AppState.ENROLL);
        }, () -> {
            updateState(AppState.CHOOSE_STUDENT);
        }, "(none)");
        data.put("[OLIST] Student", studentList[0]);

        courseList[0] = new FOList(data.frame, name -> {
            Course course = ArrayUtils.findElement(currentStudent.getCourses(), c -> c.deliverContent().equals(name));
            if (course == null) {
                FGUI.notifyWarn("Course %s doesn't exist! Deleting...", name);
                courseList[0].removeElement(name);
                courseList[0].setSelectedIndex(0);
                return;
            }
            selectedCourseS = course;
            updateState(AppState.STUDY);
        }, () -> {
            updateState(AppState.ENROLL);
        }, "Enroll");
        data.put("[SELEC] [OLIST] Course", courseList[0]);
        courseList[1] = new FOList(data.frame, name -> {
            selectedCourse = name;
        }, () -> {
            selectedCourse = SELECTED_COURSE_NONE;
        }, SELECTED_COURSE_NONE)
            .addElement("Math")
            .addElement("Programming");
        data.put("[ENROLL] [OLIST] Available courses", courseList[1]);
        
        JButton button;
        button = new JButton("Start Learning!");
        data.put("[LEARN] [BTN] Start Learning", button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentStudent.startLearning(selectedCourseS);
                updateOnResize();
            }
        });
        data.frame.add(button);

        button = new JButton("Finish Learning");
        data.put("[LEARN] [BTN] Finish Learning", button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentStudent.completeCourse(selectedCourseS);
                courseList[0].removeElement(selectedCourseS.deliverContent());
                courseList[0].setSelectedIndex(0);
                updateState(AppState.ENROLL);
            }
        });
        data.frame.add(button);
        
        button = new JButton("Enroll!");
        data.put("[ENROLL] [BTN] New Course", button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String opt = (String)courseList[0].getSelectedItem();
                if (opt != "Enroll") {
                    FGUI.notifyWarn("Can't enroll existing course! (%s)", opt);
                    return;
                }
                if (selectedCourse.equals(SELECTED_COURSE_NONE)) {
                    FGUI.notifyWarn("You didn't specify course!");
                    return;
                }
                Course course = courseFactory.build(selectedCourse);
                if (isCertificate)  course = new CertificateDecorator(course);
                if (isGamification) course = new GamificationDecorator(course);
                if (isMentor)       course = new MentorSupportDecorator(course);
                currentStudent.enrollInCourse(course);
                data.getOList("[SELEC] [OLIST] Course").getComboBoxModel().addElement(course.deliverContent());
            }
        });
        data.frame.add(button);
        BiConsumer<JCheckBox, String> toggle = (checkbox, name) -> toggleCheckbox(checkbox, name);
        data
        .addActionListenerToCheckbox("Certificate",  "[ENROLL] [CB] Certificate",  toggle)
        .addActionListenerToCheckbox("Gamification", "[ENROLL] [CB] Gamification", toggle)
        .addActionListenerToCheckbox("Mentor",       "[ENROLL] [CB] Mentor",       toggle);
    }

    public void init() {
        data = new DataBuilder()
            .setInitScreenSize(new Dimension(800, 600))
            .setFont(new Font("Arial", Font.PLAIN, 48))
            .setParent(new JFrame("LFS"))
            .setBackground(new Color(0xFF181818))
            .setForeground(new Color(0xFFE7E7E7))
            .actionOnBuild((data) -> {
                initTitle(data);
                initTextbox(data);
                initStudentList(data);
                updateState(data);
            })
            .actionOnResize(() -> updateOnResize())
            .build();
        

        data.frame.setVisible(true);
    }
}
