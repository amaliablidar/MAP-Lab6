package com;
import com.Model.Course;
import com.Model.Student;
import com.Model.Teacher;
import com.Repository.JDBC_CourseRepo;
import com.Repository.JDBC_StudentRepo;
import com.Repository.JDBC_TeacherRepo;
import com.Controller.RegistrationSystemController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TeacherController implements Initializable {
    @FXML
    private TableColumn<Student,String> firstName;
    @FXML
    private TableColumn<Student,Integer> studID;
    @FXML
    private TableColumn<Student,Integer> credits;
    @FXML
    private TextField searchCourseIdTextField;
    @FXML
    private TableView<Student> tableViewStudent;
    @FXML
    private TableColumn<Student,String> lastName;


    private final JDBC_StudentRepo studentRepository=new JDBC_StudentRepo("jdbc:mysql://localhost:3306/university");
    private final JDBC_CourseRepo courseRepository=new JDBC_CourseRepo("jdbc:mysql://localhost:3306/university");
    private final JDBC_TeacherRepo teacherRepository=new JDBC_TeacherRepo("jdbc:mysql://localhost:3306/university");
    private final RegistrationSystemController controller=new RegistrationSystemController(studentRepository,teacherRepository,courseRepository);

    private static Integer id;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}


    /**
     * verifica daca un curs exista
     * @return cursul daca a fost gasit, null in caz contrar
     */
    private Course checkCourse()
    {
        if (isNumeric(searchCourseIdTextField.getText())){
            return controller.findCourse(Integer.parseInt(searchCourseIdTextField.getText()));
        }
        return null;
    }
    /**
     * arata studentii care sunt inscrisi la un anumit curs
     */
    public void showEnrolledStudents()
    {

        ObservableList<Student> list= FXCollections.observableArrayList();
        Course foundCourse=checkCourse();
        if (foundCourse!=null && foundCourse.getTeacher() == (id) ){
            list.addAll(controller.studentsEnrolled(foundCourse.getId()));
            lastName.setCellValueFactory(cellData-> cellData.getValue().lastNameProperty());
            firstName.setCellValueFactory(cellData->cellData.getValue().firstNameProperty());
            studID.setCellValueFactory(cellData-> cellData.getValue().studIdProperty().asObject());
            credits.setCellValueFactory(cellData->cellData.getValue().creditsProperty().asObject());
            tableViewStudent.setItems(list);}
        else{

            searchCourseIdTextField.setText("");
        }
    }

    /**
     * deschide fereastra profesorului
     * @throws IOException
     */
    public void openTeacherView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("teacher-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Stage stage = new Stage();
        stage.setTitle("Welcome");
        stage.setScene(scene);
        stage.show();
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

    /**
     * salveaza id-ul profesorului
     * @param id ul profesorului
     */
    public void setSavedTeacherID(int id)
    {
        this.id=id;
    }
}