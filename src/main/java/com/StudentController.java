package com;
import com.Exception.InvalidInputException;
import com.Exception.NullException;
import com.Model.Student;
import com.Model.Teacher;
import com.Model.Course;
import com.Repository.JDBC_CourseRepo;
import com.Repository.JDBC_StudentRepo;
import com.Repository.JDBC_TeacherRepo;
import com.Controller.RegistrationSystemController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.URL;
import java.util.ResourceBundle;


public class StudentController  implements Initializable {
    @FXML
    private Pane registerPane;
    @FXML
    private Pane creditPane;
    @FXML
    private Label messageLabelRegister;
    @FXML
    private TextField studentIDTextField;
    @FXML
    private TextField studentIdTextFieldCredit;
    @FXML
    private TextField courseIDTextField;
    @FXML
    private TableView<Course> tableViewCourse;
    @FXML
    private TableColumn<Course,String> Course;
    @FXML
    private TableColumn<Course, Integer> CourseId;
    @FXML
    private TableColumn<Course,Integer> Credits;
    @FXML
    private TableView<Student> tableViewCredit;
    @FXML
    private TableColumn<Student, Integer> CreditsStud;
    @FXML
    private TableColumn<Student, Integer> ID;
    @FXML
    private TableColumn<Student, String> Name;

    private final JDBC_StudentRepo studRepo=new JDBC_StudentRepo("jdbc:mysql://localhost:3306/university");
    private final JDBC_CourseRepo courseRepo=new JDBC_CourseRepo("jdbc:mysql://localhost:3306/university");
    private final JDBC_TeacherRepo teacherRepo=new JDBC_TeacherRepo("jdbc:mysql://localhost:3306/university");
    private final RegistrationSystemController controller=new RegistrationSystemController(studRepo,teacherRepo,courseRepo);
    private static  Integer savedStudID;



    /**
     * arata creditele studentului
     */
    public void setCreditPane()
    {
        registerPane.setVisible(false);
        creditPane.setVisible(true);

    }

    /**
     * deschide fereastra student
     * @throws IOException
     */
    public void openStudentView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("student-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Stage stage = new Stage();
        stage.setTitle("Welcome!");
        StudentController studentController=fxmlLoader.getController();
        studentController.studentIdTextFieldCredit.setText(savedStudID.toString());
        studentController.studentIDTextField.setText(savedStudID.toString());
        studentController.studentIDTextField.setEditable(false);
        studentController.studentIdTextFieldCredit.setEditable(false);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * deschide fereastra de inregistrare
     *
     */
    @FXML
    public void registerButtonAction(ActionEvent event){
        messageLabelRegister.setText("");
        creditPane.setVisible(false);
        registerPane.setVisible(true);

    }
    /**
     * schimba mesajul din registerMessage
     * @param searchObject obiectul in functie de care se schimba mesajul
     */
    private  void changeMessage(Object searchObject)
    {
        if(searchObject==null)
            messageLabelRegister.setText("Please try again");
        else
            messageLabelRegister.setText("Data not found");

    }
    /**
     * inregistreaza un student dupa ce butonul este apasat
     * @throws InvalidInputException
     * @throws NullException
     */
    @FXML
    public void registerStudent(ActionEvent event) throws InvalidInputException, NullException {

        Student student=checkIfStudent();
        changeMessage(student);
        Course course=checkCourse();
        changeMessage(course);
        if(student !=null && course!=null)
            if(controller.register(student,course)){
                messageLabelRegister.setText("Success");
                messageLabelRegister.setVisible(true);
                courseIDTextField.setText("");
            }

            else{
                messageLabelRegister.setText("Something went wrong");
                messageLabelRegister.setVisible(true);
                courseIDTextField.setText("");}

    }


    /**
     * arata detalii despre student
     */
    public void showCreditNr(ActionEvent event) {

        ObservableList<Student> list= FXCollections.observableArrayList();
        if (isNumeric(studentIdTextFieldCredit.getText())){
            Student foundStudent=controller.findStudent(Integer.parseInt(studentIdTextFieldCredit.getText()));
            if (foundStudent!=null)
                list.add(foundStudent);
        }
        Name.setCellValueFactory(cellData-> cellData.getValue().lastNameProperty());
        ID.setCellValueFactory(cellData->cellData.getValue().studIdProperty().asObject());
        CreditsStud.setCellValueFactory(cellData-> cellData.getValue().creditsProperty().asObject());

        tableViewCredit.setItems(list);

    }



    /**
     * verifica daca studentul este in baza de date
     * @return Studentul daca acesta a fost gasit, null in caz contrar
     */
    private  Student checkIfStudent()
    {
        if (isNumeric(studentIDTextField.getText())){
            return controller.findStudent(Integer.parseInt(studentIDTextField.getText()));
        }
        return null;
    }
    /**
     * verifica daca cursul este in baza de date
     *      * @return cursul daca acesta a fost gasit, null in caz contrar
     */
    private  Course checkCourse()
    {
        if (isNumeric(courseIDTextField.getText())){
            return controller.findCourse(Integer.parseInt(courseIDTextField.getText()));
        }
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

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



    /**
     * salveaza id-ul studentului
     * @param id ul studentului
     */
    public void setSavedStudID(int id)
    {
        this.savedStudID=id;
    }
    /**
     * arata detalii despre cursuri
     */
    public void showCoursesButton(ActionEvent event){
        ObservableList<Course> list= FXCollections.observableArrayList();
        list.addAll(controller.retrieveCoursesWithFreePlaces());
        Course.setCellValueFactory(cellData-> cellData.getValue().nameProperty());
        CourseId.setCellValueFactory(cellData->cellData.getValue().courseIdProperty().asObject());
        Credits.setCellValueFactory(cellData-> cellData.getValue().creditsProperty().asObject());

        tableViewCourse.setItems(list);

    }
}