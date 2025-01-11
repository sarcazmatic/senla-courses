package com.senla.courses.service.courses;

import com.senla.courses.dao.CourseDAO;
import com.senla.courses.dao.TeacherDAO;
import com.senla.courses.dto.CourseDTO;
import com.senla.courses.mapper.CourseMapper;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.Course;
import com.senla.courses.model.Teacher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseMapper courseMapper;
    private final CourseDAO courseDAO;
    private final TeacherDAO teacherDAO;

    @Override
    public Long addCourse(CourseDTO courseDTO) {
        Course course = courseMapper.fromCourseDTO(courseDTO);
        course.setTeachers(new HashSet<>());
        Long id = courseDAO.save(course);
        logger.info("Создан курс с названием {}", courseDTO.getName());
        return id;
    }

    @Override
    public CourseDTO editCourse(CourseDTO courseDTO, Long id) {
        Course course = courseDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не нашли курс по id " + id));
        Course courseUpd = courseMapper.updateCourse(course, courseDTO);
        CourseDTO courseDTOResult = courseMapper.fromCourse(courseDAO.update(courseUpd));
        logger.info("Обновлен курс с id {}", id);
        return courseDTOResult;
    }

    @Override
    public CourseDTO addTeachers(Long id, List<Long> ids) {
        Course course = courseDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не нашли курс по id " + id));
        Set<Teacher> teachers = new HashSet<>(course.getTeachers());
        Set<Teacher> addTeachers = ids.stream().map(i -> teacherDAO.find(i)
                .orElseThrow(() -> new NotFoundException("Не нашли преподавателя с id " + i))).collect(Collectors.toSet());
        teachers.addAll(addTeachers);
        course.setTeachers(teachers);
        courseDAO.update(course);
        logger.info("Учителя с id {} добавлены в курс {}", ids, course.getName());
        return courseMapper.fromCourse(course);
    }

    @Override
    public CourseDTO removeTeachers(Long id, List<Long> ids) {
        Course course = courseDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не нашли курс по id " + id));
        Set<Teacher> teachers = new HashSet<>(course.getTeachers());
        if (teachers.isEmpty()) {
            throw new EmptyListException("Список преподавателей курса пуст");
        }
        Set<Teacher> newTeachers = teachers.stream().filter(t -> !ids.contains(t.getId())).collect(Collectors.toSet());
        course.setTeachers(newTeachers);
        courseDAO.update(course);
        logger.info("Учителя с id {} удалены из курса {}", ids, course.getName());
        return courseMapper.fromCourse(course);
    }

    @Override
    public CourseDTO findById(Long id) {
        Course course = courseDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не нашли курс по id " + id));
        CourseDTO courseDTO = courseMapper.fromCourse(course);
        logger.info("Найден курс с id {}", id);
        return courseDTO;
    }

    public List<CourseDTO> findCourses(int from, int size) {
        List<Course> courses = courseDAO.findAll(from, size);
        if (courses.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<CourseDTO> courseDTOS = courses.stream().map(courseMapper::fromCourse).toList();
        logger.info("Собран список всех курсов");
        return courseDTOS;
    }

    @Override
    public List<CourseDTO> findCoursesByText(String text, int from, int size) {
        List<Course> courses = courseDAO.findAllByText(text, from, size);
        if (courses.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<CourseDTO> courseDTOS = courses.stream().map(courseMapper::fromCourse).toList();
        logger.info("Собран список всех курсов по запросу: {}", text);
        return courseDTOS;
    }

    @Override
    public void deleteCourse(Long id) {
        courseDAO.deleteById(id);
        logger.info("Курс с id {} удален", id);
    }

}
