package com;
import com.Model.Student;
import com.Model.Teacher;
import com.Repository.JDBC_CourseRepo;
import com.Repository.JDBC_StudentRepo;
import com.Repository.JDBC_TeacherRepo;
import com.Controller.RegistrationSystemController;
import com.StudentController;
import com.TeacherController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.event. ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField firstNameTextField;
    @FXML
    public TextField idTextField;
    @FXML
    private TextField lastNameTextField;


    private final JDBC_StudentRepo studentRepository=new JDBC_StudentRepo("jdbc:mysql://localhost:3306/university");
    private final JDBC_CourseRepo courseRepository=new JDBC_CourseRepo("jdbc:mysql://localhost:3306/university");
    private final JDBC_TeacherRepo teacherRepository=new JDBC_TeacherRepo("jdbc:mysql://localhost:3306/university");
    private final RegistrationSystemController controller=new RegistrationSystemController(studentRepository,teacherRepository,courseRepository);


    void loginFailed(){
        lastNameTextField.setText("");
        firstNameTextField.setText("");
        idTextField.setText("");
        loginMessageLabel.setText("Failed to login.");
    }

    private int savedLogId;

    /***
     * initializeaza mesajul de login cu un string gol
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loginMessageLabel.setText("");
    }

    /**
     * verifica daca datele de logare sunt valide
     * @return 1 daca datele sunt ale unui student, 2 daca sunt ale unui profesor
     */
    private int validateLogin() {

        String id=idTextField.getText();
        if (isNumeric(id)){
            Student foundStudent=controller.findStudent(Integer.parseInt(idTextField.getText()));
            Teacher foundTeacher= controller.findTeacher(Integer.parseInt(idTextField.getText()));
            if (foundStudent!=null && lastNameTextField.getText().equals(foundStudent.getLastName()) && firstNameTextField.getText().equals(foundStudent.getFirstName())){
                savedLogId= foundStudent.getId();
                return 1;
            }
            if (foundTeacher!=null && lastNameTextField.getText().equals(foundTeacher.getLastName()) && firstNameTextField.getText().equals(foundTeacher.getFirstName()))
            {
                savedLogId= foundTeacher.getId();
                return 2;}
        }
        return 0;
    }

    /**
     * deschide fereastra pentru student sau profesor
     * @throws IOException
     */
    public void loginButtonAction(ActionEvent event) throws IOException {

        if (!lastNameTextField.getText().isBlank() && !firstNameTextField.getText().isBlank() && !idTextField.getText().isBlank()) {
            if (loggingIn() == 1) {
                lastNameTextField.setText("");
                firstNameTextField.setText("");
                lastNameTextField.setText("");
                StudentController studentController = new StudentController();
                studentController.setSavedStudID(savedLogId);
                studentController.openStudentView();


            }
            if (loggingIn() == 2) {
                lastNameTextField.setText("");
                firstNameTextField.setText("");
                lastNameTextField.setText("");
                TeacherController teacherController = new TeacherController();
                teacherController.setSavedTeacherID(savedLogId);
                teacherController.openTeacherView();
            } else {
                loginFailed();
            }
        }
        else{
                loginFailed();
            }
        }





    /**
     * schimba mesajul dupa logare
     * @return 0-daca datele nu au fost gasite, 1-daca datele sunt ale unui Student, 2-daca datele sunt ale unui profesor
     */

    private int loggingIn()
    {
        int foundPerson=validateLogin();
        if (foundPerson==0){
            loginMessageLabel.setText("Data not found");
            return 0;
        }
        if (foundPerson==1){
            loginMessageLabel.setText("Success");
            return 1;
        }
        loginMessageLabel.setText("Success");
        return 2;

    }

    /**
     * verifica daca un sir de caractere poate fi numar
     * @param strNum-input
     * @return true-daca number este numerit, false in caz contrar
     */
    public  boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int number = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


}