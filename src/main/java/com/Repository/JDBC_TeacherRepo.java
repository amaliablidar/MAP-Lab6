package com.Repository;

import com.Model.Course;
import com.Model.Person;
import com.Model.Teacher;
import com.Model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JDBC_TeacherRepo extends JDBC_repository<Teacher> {

    public JDBC_TeacherRepo(String url) {
        super( url);
    };
    /**
     *
     * @param teacherID profesorul care trebuie gasit
     * @return profesorul daca acesta a fost gasit, null in caz contrar
     */
    @Override
    public Teacher findOne(int teacherID) {

        String teacherQuery="select t.teacherId,t.personId,p.firstName, p.lastName FROM Teacher t join Person p on p.ID = t.personId where t.teacherId="+teacherID;
        String coursesListQuery = " select c.id, c.title, c.credits,c.teacherId,c.maxStudents from course c where c.teacherId="+teacherID;

        Teacher teacher=new Teacher();
        ArrayList<Course> coursesList = new ArrayList<>();

        try {

            Statement statement = createConnection().createStatement();
            ResultSet result = statement.executeQuery(teacherQuery);

            if (result.next()) {
                teacher.setId(result.getInt("teacherId"));
                teacher.setPersonalId(result.getInt("personId"));
                teacher.setFirstName(result.getString("firstName"));
                teacher.setLastName(result.getString("lastName"));
            }

            Statement statement2 = createConnection().createStatement();
            ResultSet result2 = statement2.executeQuery(coursesListQuery);

            while (result2.next()) {
                Course course = new Course();
                Teacher newTeacher=new Teacher();
                newTeacher.setId(teacher.getId());
                newTeacher.setFirstName(teacher.getFirstName());
                newTeacher.setLastName(teacher.getLastName());
                course.setId(result2.getInt("id"));
                course.setTitle(result2.getString("title"));
                course.setCredits(result2.getInt("credits"));
                course.setTeacher(teacherID);
                course.setMaxStudents(result2.getInt("maxStudents"));
                coursesList.add(course);
            }
            teacher.setCourses(coursesList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (teacher.getId()== teacherID)
            return teacher;
        else
            return null;
    }

    /**
     *
     * @return lista de profesori
     */
    @Override
    public ArrayList<Teacher> findAll()
    {
        ArrayList<Teacher>teacherList=new ArrayList<>();
        String sqlQuery=" select t.teacherId,t.personId, p.firstName, p.lastName FROM Teacher t join Person p on p.id=t.personId ";


        try {

            Statement statement = createConnection().createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);

            while (result.next()) {
                Teacher teacher=new Teacher();
                teacher.setId(result.getInt("teacherId"));
                teacher.setPersonalId(result.getInt("personId"));
                teacher.setFirstName(result.getString("firstName"));
                teacher.setLastName(result.getString("lastName"));

                List<Course> teacherCourseList = new ArrayList<>();
                String coursesListQuery = " select c.id, c.title, c.credits,c.maxStudents from course c where c.teacherId="+teacher.getId();
                Statement statement2 = createConnection().createStatement();
                ResultSet result2 = statement2.executeQuery(coursesListQuery);

                while (result2.next()) {
                    Course course = new Course();
                    Teacher newTeacher=new Teacher();
                    newTeacher.setId(teacher.getId());
                    newTeacher.setFirstName(teacher.getFirstName());
                    newTeacher.setLastName(teacher.getLastName());
                    course.setId(result2.getInt("id"));
                    course.setTitle(result2.getString("title"));
                    course.setCredits(result2.getInt("credits"));
                    course.setTeacher(newTeacher.getId());
                    course.setMaxStudents(result2.getInt("maxStudents"));
                    teacherCourseList.add(course);
                }
                teacher.setCourses(teacherCourseList);
                teacherList.add(teacher);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teacherList;
    }

    /**
     *
     * @param newTeacher profesorul care trebuie salvat
     * @return profesorul daca acesta exista deja, null daca a fost salvat
     */
    @Override
    public Teacher save(Teacher newTeacher) {
        Teacher foundTeacher=findOne(newTeacher.getId());
        if (foundTeacher !=null)
        {
            return foundTeacher;
        }
        else {
            try (
                    PreparedStatement ps = createConnection().prepareStatement("INSERT INTO Person(ID,firstName, lastName) values (?,?,?)")

            ) {
                ps.setInt(1,newTeacher.getPersonalId());

                ps.setString(2,newTeacher.getFirstName());
                ps.setString(3,newTeacher.getLastName());
                ps.execute();
            }
             catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try (
                    PreparedStatement ps = createConnection().prepareStatement("INSERT INTO Teacher(teacherId, personId) values (?,?)")

            ) {
                ps.setInt(1,newTeacher.getId());
                ps.setInt(2,newTeacher.getPersonalId());
                ps.execute();
            }catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return null;
    }

    /**
     *
     * @param id id-ul profesorului care trebuie sters
     * @return profesorul daca acesta a fost sters, null in caz contrar
     */
    @Override
    public Teacher delete(int id) {
        Teacher teacherFound=findOne(id);
        String deleteTeacherQuery = "delete from teacher Where teacherId="+id;

        if (teacherFound !=null)
        {
            try
            {
                Statement statement = createConnection().createStatement();
                statement.executeUpdate(deleteTeacherQuery);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return teacherFound;
        }
        return null;
    }


    /**
     *
      * @param teacher profesorul cu noile proprietati
     * @return null daca acesta nu a fost updatat
     */
    @Override
    public Teacher update (Teacher teacher){
        Teacher teacherFound=findOne(teacher.getId());
        if (teacherFound ==null)
        {
            return null;
        }
        else {
            try (
                    PreparedStatement ps = createConnection().prepareStatement("UPDATE person SET ID=?, firstName=?, lastName=? WHERE ID="+teacher.getPersonalId())

            ) {
                ps.setInt(1,teacher.getPersonalId());
                ps.setString(2,teacher.getFirstName());
                ps.setString(3,teacher.getLastName());
                ps.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try (
                    PreparedStatement ps = createConnection().prepareStatement("UPDATE teacher SET teacherId=?, personId=? WHERE teacherId="+teacher.getId())

            ) {
                ps.setInt(1,teacher.getId());
                ps.setInt(2,teacher.getPersonalId());
                ps.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        }
        return null;
    }


}