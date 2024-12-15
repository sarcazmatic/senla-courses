package com.senla.courses.service.courses;

import com.senla.courses.dao.CourseDAO;
import com.senla.courses.dao.TeacherDAO;
import com.senla.courses.dto.CourseDTO;
import com.senla.courses.dto.CourseMapper;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.Course;
import com.senla.courses.model.Teacher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;
    private final CourseDAO courseDAO;
    private final TeacherDAO teacherDAO;

    @Override
    public Long addCourse(CourseDTO courseDTO) {
        return courseDAO.save(courseMapper.fromCourseDTO(courseDTO));
    }

    @Override
    public CourseDTO editCourse(CourseDTO courseDTO, Long id) {
        Course course = courseDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не нашли курс по id " + id));
        Course courseUpd = courseMapper.updateCourse(course, courseDTO);
        return courseMapper.fromCourse(courseUpd);
    }

    @Override
    public CourseDTO addTeachers(Long id, List<Long> ids) {
        Course course = courseDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не нашли курс по id " + id));
        Set<Teacher> teachers;
        try {
            teachers = new HashSet<>(course.getTeachers());
        } catch (NullPointerException e) {
            teachers = new HashSet<>();
        }
        for (Long i : ids) {
            Teacher teacher = teacherDAO.find(i)
                    .orElseThrow(() -> new NotFoundException("Не нашли преподавателя по id " + i));
            teachers.add(teacher);
        }
        course.setTeachers(teachers);
        return courseMapper.fromCourse(course);
    }

    @Override
    public CourseDTO findById(Long id) {
        Course course = courseDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не нашли курс по id " + id));
        return courseMapper.fromCourse(course);
    }

    public List<CourseDTO> findCourses(int from, int size) {
        List<Course> courses = courseDAO.findAll(from, size);
        if (courses.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        return courses.stream().map(courseMapper::fromCourse).toList();
    }

    @Override
    public List<CourseDTO> findCoursesByText(String text, int from, int size) {
        List<Course> courses = courseDAO.findAllByText(text, from, size);
        if (courses.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        return courses.stream().map(courseMapper::fromCourse).toList();
    }

    @Override
    public void deleteCourse(Long id) {
        courseDAO.deleteById(id);
    }

}
