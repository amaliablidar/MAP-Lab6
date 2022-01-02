package com.Repository;
import java.sql.Statement;
import java.util.ArrayList;

import com.Model.Course;
import com.Model.Person;
import com.Model.Student;
import com.Model.Teacher;
import com.Repository.JDBC_repository;

import java.util.List;
import java.sql.*;


public class JDBC_CourseRepo extends JDBC_repository<Course> {

    public JDBC_CourseRepo(String url) {
        super( url);
    };

    /**
     *
     * @param courseID cursul care trebuie gasit
     * @return cursul daca a fost gasit, null in caz contrar
     */
    @Override
    public Course findOne(int courseID){
        ArrayList<Student> studentsList = new ArrayList<>();
        Course course=new Course();
        Teacher teacher=new Teacher();

        String courseQuery="select c.id, c.title, c.credits, c.teacherId, c.maxStudents, t.teacherId from course c join teacher t on c.teacherId = t.teacherId where c.id ="+courseID;

        try {
            Statement statement = createConnection().createStatement();
            ResultSet result = statement.executeQuery(courseQuery);

            if (result.next()) {
                course.setId(result.getInt("id"));
                course.setTitle(result.getString("title"));
                course.setCredits(result.getInt("credits"));
                course.setTeacher(result.getInt("teacherId"));
                course.setMaxStudents(result.getInt("maxStudents"));

            }
            String studentsListQuery="select s.studentId, s.totalCredits,p.id, p.firstName, p.lastName from Student s join Person p on p.id = s.personId join Enrolment e on e.studentId=s.studentId where e.courseId ="+courseID;
            ResultSet resultStudentsList = statement.executeQuery(studentsListQuery);
            while (resultStudentsList.next()) {
                Student student= new Student();
                student.setId(resultStudentsList.getInt("studentId"));
                student.setTotalCredits(resultStudentsList.getInt("totalCredits"));
                student.setPersonalId(resultStudentsList.getInt("id"));
                student.setFirstName(resultStudentsList.getString("firstName"));
                student.setLastName(resultStudentsList.getString("lastName"));

                studentsList.add(student);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        course.setStudentsList(studentsList);
        if (course.getId() == courseID)
            return course;
        else
            return null;
    }

    /**
     *
     * @return intreaga lista de cursuri
     */
    @Override
    public ArrayList<Course> findAll()
    {
        ArrayList<Course>courseList=new ArrayList<>();
        String courseQuery="select c.id, c.title, c.credits, c.teacherId, c.maxStudents from Course c";

        try {

            Statement statement = createConnection().createStatement();
            ResultSet result = statement.executeQuery(courseQuery);

            while (result.next()) {
                Course course=new Course();
                course.setId(result.getInt("id"));
                course.setTitle(result.getString("title"));
                course.setCredits(result.getInt("credits"));
                course.setTeacher(result.getInt("teacherId"));
                course.setMaxStudents(result.getInt("maxStudents"));


                String studentsListQuery="Select s.studentId, s.totalCredits, s.personId, p.firstName, p.lastName from Student s join Person p on p.ID = s.personId join Enrolment e on e.studentId = s.studentId join Course c on c.id = e.courseId";
                ArrayList<Student> studentsList = new ArrayList<>();
                Statement statement1 = createConnection().createStatement();
                ResultSet resultStudentList = statement1.executeQuery(studentsListQuery);

                while (resultStudentList.next()) {
                    Student student= new Student();

                    student.setId(resultStudentList.getInt("studentId"));
                    student.setTotalCredits(resultStudentList.getInt("totalCredits"));
                    student.setPersonalId(resultStudentList.getInt("personId"));
                    student.setFirstName(resultStudentList.getString("firstName"));
                    student.setLastName(resultStudentList.getString("lastName"));
                    studentsList.add(student);
                }
                course.setStudentsList(studentsList);

                courseList.add(course);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;
    }

    /**
     *
     * @param course cursul care trebuie salvat
     * @return cursul daca acesta exista deja, null daca a fost salvat
     */
    @Override
    public Course save(Course course) {
        Course courseFound=findOne(course.getId());
        if (courseFound !=null)
        {
            return courseFound;
        }
        else {

            try (
                    PreparedStatement prepStatement = createConnection().prepareStatement("INSERT INTO course(id,title,credits,teacherId,maxStudents) values (?,?,?,?,?)")

            ) {
                prepStatement.setInt(1, course.getId());
                prepStatement.setString(2, course.getTitle());
                prepStatement.setInt(3, course.getCredits());
                prepStatement.setInt(4, course.getTeacher());
                prepStatement.setInt(5, course.getMaxStudents());
                prepStatement.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }


            return null;
        }

    /**
     *
     * @param courseId id-ul cursului care trebuie sters
     * @return cursul daca acesta a fost sters, null in caz contrar
     */
    @Override
    public Course delete(int courseId)  {
        Course courseFound=findOne(courseId);

        String deleteCourseQuery = "delete from Course where id="+courseId;
        String deleteEnrolmentQuery = "delete from Enrolment where courseId=" + courseId;
        if (courseFound != null) {

            try {
                Statement statement = createConnection().createStatement();
                int result = statement.executeUpdate(deleteEnrolmentQuery);
                if (result != 0) {
                    try {
                        Statement statement2 = createConnection().createStatement();
                        statement2.executeUpdate(deleteCourseQuery);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return courseFound;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            try {
                Statement statement2 = createConnection().createStatement();
                statement2.executeUpdate(deleteCourseQuery);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return courseFound;
        }
            return null;
    }

    /**
     *
     * @param course cursul cu proprietatile noi
     * @return null daca cursul nu a fost updatat
     */
    @Override
    public Course update (Course course){
        Course courseFound=findOne(course.getId());
        if (courseFound ==null)
        {
            return null;
        }
        else {
            try (
                    PreparedStatement prepStatement = createConnection().prepareStatement("update course set id=?,title=?,credits=?,maxStudents=? WHERE id="+course.getId());

            ) {
                prepStatement.setLong(1,course.getId());
                prepStatement.setString(2, course.getTitle());
                prepStatement.setInt(3,course.getCredits());
                prepStatement.setInt(4,course.getMaxStudents());
                prepStatement.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return null;

    }




}