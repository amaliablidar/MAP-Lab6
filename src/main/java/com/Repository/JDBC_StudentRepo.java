package com.Repository;

import com.Model.Course;
import com.Model.Person;
import com.Model.Teacher;
import com.Model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBC_StudentRepo extends JDBC_repository<Student> {

    public JDBC_StudentRepo(String url) {
        super( url);
    };


    /**
     *
     * @param studentID id-ul studentului care trebuie gasit
     * @return student daca acesta a fost gasit, null in caz contrar
     */
    @Override
    public Student findOne(int studentID) {

        String studentQuery=" select s.studentId,s.totalCredits, p.id, p.firstName, p.lastName from Student s join Person p on p.ID = s.personId where s.studentId="+studentID;
        Student student=new Student();
        Person person = new Person();
        ArrayList<Course> studentCourses = new ArrayList<>();


        try {

            Statement statement = createConnection().createStatement();
            ResultSet result = statement.executeQuery(studentQuery);

            if (result.next()) {
                student.setId(result.getInt("studentId"));
                student.setTotalCredits(result.getInt("totalCredits"));
                student.setPersonalId(result.getInt("id"));
                student.setFirstName(result.getString("firstName"));
                student.setLastName(result.getString("lastName"));

            }

            String courseListQuery = "select c.id, c.title, c.credits, c.teacherId, c.maxStudents,p.firstName, p.lastName from Course c join Enrolment e on e.courseId = c.id join student s on s.studentId = e.StudentId join person p on p.id = s.personId where s.studentId="+studentID;
            Statement statement2 = createConnection().createStatement();
            ResultSet result2 = statement2.executeQuery(courseListQuery);

            while (result2.next()) {
                Course course=new Course();
                course.setId(result2.getInt("id"));
                course.setTitle(result2.getString("title"));
                course.setCredits(result2.getInt("credits"));
                int teacherId = result2.getInt("teacherId");
                course.setTeacher(teacherId);
                course.setMaxStudents(result2.getInt("maxStudents"));
                studentCourses.add(course);
            }
            student.setEnrolledCourses(studentCourses);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (student.getId() == studentID)
            return student;

            return null;

    }


    /**
     *
     * @return lista de studenti
     */
    @Override
    public ArrayList<Student> findAll()
    {
        String studentQuery=" select s.studentId,s.totalCredits, p.id, p.firstName, p.lastName from Student s join Person p on p.ID = s.personId";

        ArrayList<Student>studentList=new ArrayList<>();
        try {
            Statement statement = createConnection().createStatement();
            ResultSet result = statement.executeQuery(studentQuery);



            while (result.next()) {
                Student student = new Student();
                student.setId(result.getInt("studentId"));
                student.setTotalCredits(result.getInt("totalCredits"));
                student.setPersonalId(result.getInt("id"));
                student.setFirstName(result.getString("firstName"));
                student.setLastName(result.getString("lastName"));


                String courseListQuery = "select c.id, c.title, c.credits, c.teacherId, c.maxStudents,p.firstName, p.lastName from Course c join Enrolment e on e.courseId = c.id join student s on s.studentId = e.StudentId join person p on p.id = s.personId";
                ArrayList<Course> studentCourses = new ArrayList<>();
                Statement statement2 = createConnection().createStatement();
                ResultSet result2 = statement2.executeQuery(courseListQuery);

                while (result2.next()) {
                    Course course = new Course();
                    course.setId(result2.getInt("id"));
                    course.setTitle(result2.getString("title"));
                    course.setCredits(result2.getInt("credits"));
                    int teacherId = result2.getInt("teacherId");
                    course.setTeacher(teacherId);
                    course.setMaxStudents(result2.getInt("maxStudents"));
                    studentCourses.add(course);
                }
                student.setEnrolledCourses(studentCourses);
                studentList.add(student);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentList;



    }

    /**
     *
     * @param newStudent studentul ce trebuie salvat
     * @return studentul daca acesta exista deja, null daca a fost salvat
     */
    @Override
    public Student save(Student newStudent)  {
        Student studentFound=findOne(newStudent.getId());
        if (studentFound !=null)
        {
            return studentFound;
        }
        else {

            try (
                    PreparedStatement ps = createConnection().prepareStatement("INSERT INTO student(studentId, totalCredits, personId) values (?,?,?)");

            ) {
                ps.setInt(1,newStudent.getId());
                ps.setInt(2, newStudent.getTotalCredits());
                ps.setInt(3, newStudent.getPersonalId());
                ps.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return null;
    }

    /**
     *
     * @param id id-ul studentului ce trebuie sters
     * @return studentul daca acesta a fost sters, null in caz contrar
     */
    @Override
    public Student delete(int id) {
        Student foundStudent = findOne(id);
        String deleteStudentQuery = "delete from student where studentId=" + id;
        String deleteStudentFromEnrolment = "delete from enrolment where StudentId=" + id;
        if (foundStudent != null) {

            try {
                Statement statement = createConnection().createStatement();
                int result = statement.executeUpdate(deleteStudentFromEnrolment);
                if (result != 0) {
                    try {
                        Statement statement2 = createConnection().createStatement();
                        statement2.executeUpdate(deleteStudentQuery);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return foundStudent;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        try {
            Statement statement2 = createConnection().createStatement();
            statement2.executeUpdate(deleteStudentQuery);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return foundStudent;
    }
        return null;
    }


    /**
     *
     * @param newStudent studentul cu proprietatile noi
     * @return null daca acesta nu a fost updatat
     */
    @Override
    public Student update (Student newStudent){
        Student foundStudent=findOne(newStudent.getId());
        if (foundStudent ==null)
        {
            return null;
        }

        else {
            try (
                    PreparedStatement ps = createConnection().prepareStatement("update Student set studentId=?,totalCredits=?,personId=? WHERE studentId="+newStudent.getId());
                    PreparedStatement ps2 = createConnection().prepareStatement("update Person set ID=?,firstName=?,lastName=? WHERE ID="+newStudent.getPersonalId());

            ) {
                ps.setLong(1,newStudent.getId());
                ps.setInt(2,newStudent.getTotalCredits());
                ps.setInt(3,newStudent.getPersonalId());
                ps.execute();
                ps2.setInt(1,newStudent.getPersonalId());
                ps2.setString(2, newStudent.getFirstName());
                ps2.setString(3,newStudent.getLastName());
                ps2.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return null;
    }

    /**
     * metoda salveaza studentul si cursul la care s-a inscris
     * @param studentId id-ul studentului
     * @param courseId id-ul cursului
     */
    public void saveEnrolment(int studentId, int courseId){
        try (
                PreparedStatement ps = createConnection().prepareStatement("insert into enrolment(StudentId,CourseId) values (?,?)")

        ) {
            ps.setLong(1, studentId);
            ps.setLong(2, courseId);
            ps.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}