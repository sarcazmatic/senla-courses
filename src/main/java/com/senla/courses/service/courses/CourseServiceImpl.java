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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;
    private final CourseDAO courseDAO;
    private final TeacherDAO teacherDAO;

    @Override
    public Long addCourse(CourseDTO courseDTO) {
        Course course = courseMapper.fromCourseDTO(courseDTO);
        course.setTeachers(new HashSet<>());
        Long id = courseDAO.save(course);
        log.info("Создан курс с названием '{}' под id {}", courseDTO.getName(), id);
        return id;
    }

    @Override
    public CourseDTO editCourse(CourseDTO courseDTO, Long id) {
        Course course = courseDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не нашли курс по id " + id));
        Course courseUpd = courseMapper.updateCourse(course, courseDTO);
        CourseDTO courseDTOResult = courseMapper.fromCourse(courseDAO.update(courseUpd));
        log.info("Курс с id {} обновлен. Было: {}. Стало: {}", id, courseMapper.fromCourse(course).toString(), courseDTOResult.toString());
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
        log.info("Учителя {} добавлены в курс {}", teachers, course.getName());
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
        log.info("Учителя {} удалены из курса {}", teachers, course.getName());
        return courseMapper.fromCourse(course);
    }

    @Override
    public CourseDTO findById(Long id) {
        Course course = courseDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Не нашли курс по id " + id));
        CourseDTO courseDTO = courseMapper.fromCourse(course);
        log.info("Найден курс с id {}: {}", id, course);
        return courseDTO;
    }

    public List<CourseDTO> findCourses(int from, int size) {
        List<Course> courses = courseDAO.findAll(from, size);
        if (courses.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<CourseDTO> courseDTOS = courses.stream().map(courseMapper::fromCourse).toList();
        log.info("Собран список всех курсов. Найдено {} элементов", courseDTOS.size());
        return courseDTOS;
    }

    @Override
    public List<CourseDTO> findCoursesByText(String text, int from, int size) {
        List<Course> courses = courseDAO.findAllByText(text, from, size);
        if (courses.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<CourseDTO> courseDTOS = courses.stream().map(courseMapper::fromCourse).toList();
        log.info("Собран список всех курсов по запросу: '{}'. Найдено {} элементов", text, courseDTOS.size());
        return courseDTOS;
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = courseDAO.find(id).orElseThrow(()
                -> new NotFoundException("Не нашли курс по id " + id));
        courseDAO.deleteById(id);
        log.info("Курс с {} id {} удален", course, id);
    }

}
