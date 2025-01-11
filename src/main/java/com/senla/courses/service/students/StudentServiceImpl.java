package com.senla.courses.service.students;

import com.senla.courses.dao.CourseDAO;
import com.senla.courses.dao.StudentDAO;
import com.senla.courses.dao.StudentsCoursesDAO;
import com.senla.courses.dao.UserDAO;
import com.senla.courses.dto.StudentDTO;
import com.senla.courses.dto.StudentsCoursesDTO;
import com.senla.courses.exception.ValidationException;
import com.senla.courses.mapper.StudentMapper;
import com.senla.courses.dto.UserDTO;
import com.senla.courses.mapper.StudentsCoursesMapper;
import com.senla.courses.mapper.UserMapper;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.Course;
import com.senla.courses.model.Role;
import com.senla.courses.model.Student;
import com.senla.courses.model.StudentsCourses;
import com.senla.courses.model.StudentsCoursesPK;
import com.senla.courses.model.User;
import com.senla.courses.util.enums.StudentsCoursesRequestEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {


    private final UserMapper userMapper;
    private final StudentMapper studentMapper;
    private final StudentDAO studentDAO;
    private final UserDAO userDAO;
    private final CourseDAO courseDAO;
    private final StudentsCoursesDAO studentCoursesDAO;
    private final StudentsCoursesMapper studentsCoursesMapper;


    @Override
    public Long registerStudent(UserDTO userDTO) {
        User user = userMapper.fromUserDTO(userDTO);
        user.setDateTimeRegistered(LocalDateTime.now());
        Long userPk = userDAO.save(user);
        User userStudent = userDAO.find(userPk)
                .orElseThrow(() -> new RuntimeException("Не смогли найти такого пользовтеля"));
        Student student = Student.builder()
                .id(userPk)
                .user(userStudent)
                .rating(0.0)
                .build();
        Long id = studentDAO.save(student);
        log.info("Студент с логином {} зарегестрирован", userDTO.getLogin());
        return id;
    }

    @Override
    public StudentDTO updateStudent(org.springframework.security.core.userdetails.User userSec, StudentDTO studentDTO, Long id) {
        Student student = studentDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не нашли студента по id " + id));
        if (studentValidate(student, userSec)) {
            throw new ValidationException("Поменять можно только информацию о себе");
        }
        Student studentUpd = studentMapper.updateStudent(student, studentDTO);
        if (studentDTO.getUserDTO() != null) {
            User user = userMapper.updateUser(student.getUser(), studentDTO.getUserDTO());
            studentUpd.setUser(user);
        }
        studentDAO.update(studentUpd);
        log.info("Студент с id {} обновлен", id);
        return studentMapper.fromStudent(studentUpd);
    }

    @Override
    public StudentDTO findById(Long id) {
        Student student = studentDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не нашли студента по id " + id));
        log.info("Студент с id {} найден", id);
        return studentMapper.fromStudent(student);
    }

    @Override
    public List<UserDTO> findStudentsByName(String name, int from, int size) {
        List<UserDTO> userDTOList = studentDAO.findAllByText(name, from, size)
                .stream()
                .map(s -> userMapper.fromUser(s.getUser()))
                .toList();
        if (userDTOList.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        log.info("Список студентов с именем {} собран", name);
        return userDTOList;
    }

    @Override
    public void deleteStudent(org.springframework.security.core.userdetails.User userSec, Long id) {
        Student student = studentDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не нашли студента по id " + id));
        if (studentValidate(student, userSec)) {
            throw new ValidationException("Удалить можно только информацию о себе");
        }
        studentDAO.deleteById(id);
        log.info("Студент с id {} удален", id);
    }

    @Override
    public Long registerCourseRequest(org.springframework.security.core.userdetails.User userSec, Long studentId, Long courseId) {
        Course course = courseDAO.find(courseId).orElseThrow(()
                -> new NotFoundException("Не нашли курс по id " + courseId));
        Student student = studentDAO.find(studentId).orElseThrow(()
                -> new NotFoundException("Не нашли студента по id " + studentId));
        if (studentValidate(student, userSec)) {
            throw new ValidationException("Заявку можно подать только от своего лица");
        }
        StudentsCoursesPK studentCoursesPK = new StudentsCoursesPK(student.getId(), course.getId());
        StudentsCourses studentsCourses = StudentsCourses.builder()
                .course(course)
                .student(student)
                .courseStatus(StudentsCoursesRequestEnum.REQUESTED)
                .build();
        studentsCourses.setId(studentCoursesPK);
        Long id = studentCoursesDAO.save(studentsCourses);
        log.info("Заявка на курс {} от студента {} зарегестрирована", course.getName(), student.getUser().getName());
        return id;
    }

    @Override
    public StudentsCoursesDTO findStudentsCoursesById(org.springframework.security.core.userdetails.User userSec, Long studentId, Long courseId) {
        Student student = studentDAO.find(studentId).orElseThrow(()
                -> new NotFoundException("Не нашли студента по id " + studentId));
        if (studentValidate(student, userSec)) {
            throw new ValidationException("Заявку можно подать только от своего лица");
        }
        StudentsCourses studentsCourses = studentCoursesDAO.findByIds(studentId, courseId);
        log.info("Заявка по id студента {} и курса {} найдена", studentId, courseId);
        return studentsCoursesMapper.fromStudentCourses(studentsCourses);
    }

    @Override
    public List<StudentsCoursesDTO> findStudentsCoursesByCourseId(Long courseId) {
        List<StudentsCoursesDTO> studentsCoursesDTOS = studentCoursesDAO.findAllByCourseId(courseId).stream().map(studentsCoursesMapper::fromStudentCourses).toList();
        log.info("Список заявок на курс {} собран", courseId);
        return studentsCoursesDTOS;
    }

    @Override
    public Integer updateRequest(Long courseId, List<Long> ids, String response) {
        if (response.toUpperCase().equals(StudentsCoursesRequestEnum.APPROVED.toString())
                || response.toUpperCase().equals(StudentsCoursesRequestEnum.DECLINED.toString())) {
            StudentsCoursesRequestEnum newResponse = StudentsCoursesRequestEnum.valueOf(response.toUpperCase());
            Integer affectedRows = studentCoursesDAO.updateRequest(courseId, ids, newResponse);
            log.info("Заявки на курс {} от студентов {} переведены в статус {}", courseId, ids, response);
            return affectedRows;
        } else {
            throw new ValidationException("Передано неверное значение response -- " + response);
        }
    }

    private boolean studentValidate(Student student, org.springframework.security.core.userdetails.User userSec) {
        boolean isValid = !student.getUser().getLogin().equals(userSec.getUsername())
                && !userSec.getAuthorities().equals(Role.ADMIN.getAuthorities());
        if (isValid) {
            log.info("Валидация студента успешна");
        } else {
            log.info("Валидация студента не была успешна");
        }
        return isValid;
    }

}
