package com.senla.courses.service;

import com.senla.courses.dao.CourseDAO;
import com.senla.courses.dao.TeacherDAO;
import com.senla.courses.dto.CourseDTO;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.mapper.CourseMapper;
import com.senla.courses.model.Course;
import com.senla.courses.model.Role;
import com.senla.courses.model.Teacher;
import com.senla.courses.model.User;
import com.senla.courses.service.courses.CourseServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
class CourseServiceTest {

    @Mock
    private CourseMapper courseMapper;
    @Mock
    private CourseDAO courseDAO;
    @Mock
    private TeacherDAO teacherDAO;
    @InjectMocks
    private CourseServiceImpl courseService;

    private CourseDTO courseDTO;
    private Course course;
    private Teacher teacher;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        courseDTO = CourseDTO.builder()
                .name("Курс тест 1")
                .description("Описание курса 1")
                .field("Поле курса 1")
                .complexity(3)
                .duration(6)
                .build();


        course = Course.builder()
                .id(1L)
                .name("Курс тест 1")
                .description("Описание курса 1")
                .field("Поле курса 1")
                .complexity(3)
                .duration(6)
                .teachers(new HashSet<>())
                .modules(new HashSet<>())
                .build();

        user = User.builder()
                .age(100)
                .id(1L)
                .description("Тестовое описание юзера 1")
                .email("user@test.ru")
                .password("{noop}123321")
                .login("testUser")
                .dateTimeRegistered(LocalDateTime.now())
                .role(Role.TEACHER)
                .build();

        teacher = Teacher.builder()
                .id(1L)
                .user(user)
                .build();

        ReflectionTestUtils.setField(courseService, "courseMapper", courseMapper);
        ReflectionTestUtils.setField(courseService, "courseDAO", courseDAO);
        ReflectionTestUtils.setField(courseService, "teacherDAO", teacherDAO);

    }

    @Test
    void testAddCourse() {

        when(courseMapper.fromCourseDTO(courseDTO)).thenReturn(course);
        when(courseDAO.save(course)).thenReturn(1L);

        Long courseId = courseService.addCourse(courseDTO);

        assertEquals(1L, courseId);
        verify(courseDAO, times(1)).save(course);
        verify(courseMapper, times(1)).fromCourseDTO(courseDTO);
    }

    @Test
    void testEditCourse() {
        CourseDTO updatedCourseDTO = CourseDTO.builder().build();
        updatedCourseDTO.setName("Обновленный курс");
        updatedCourseDTO.setDescription("Обновленное описание");

        Course updatedCourse = new Course();
        updatedCourse.setName("Обновленный курс");
        updatedCourse.setDescription("Обновленное описание");

        when(courseDAO.find(1L)).thenReturn(Optional.of(course));
        when(courseMapper.updateCourse(course, updatedCourseDTO)).thenReturn(updatedCourse);
        when(courseDAO.update(updatedCourse)).thenReturn(updatedCourse);
        when(courseMapper.fromCourse(updatedCourse)).thenReturn(updatedCourseDTO);

        CourseDTO result = courseService.editCourse(updatedCourseDTO, 1L);

        assertNotNull(result);
        assertEquals("Обновленный курс", result.getName());
        verify(courseDAO, times(1)).find(1L);
        verify(courseMapper, times(1)).updateCourse(course, updatedCourseDTO);
    }

    @Test
    void testAddTeachers() {
        List<Long> teacherIds = Arrays.asList(1L);
        Set<Teacher> teacherSet = new HashSet<>();
        teacherSet.add(teacher);

        when(courseDAO.find(1L)).thenReturn(Optional.of(course));
        when(teacherDAO.find(1L)).thenReturn(Optional.of(teacher));
        when(courseDAO.update(course)).thenReturn(course);
        when(courseMapper.fromCourse(course)).thenReturn(courseDTO);

        CourseDTO result = courseService.addTeachers(1L, teacherIds);

        assertNotNull(result);
        assertTrue(course.getTeachers().contains(teacher));
        verify(courseDAO, times(1)).find(1L);
        verify(teacherDAO, times(1)).find(1L);
    }

    @Test
    void testRemoveTeachers() {
        List<Long> teacherIdsToRemove = Arrays.asList(1L);
        Set<Teacher> teacherSet = new HashSet<>();
        teacherSet.add(teacher);
        course.setTeachers(teacherSet);

        when(courseDAO.find(1L)).thenReturn(Optional.of(course));
        when(courseDAO.update(course)).thenReturn(course);
        when(courseMapper.fromCourse(course)).thenReturn(courseDTO);

        CourseDTO result = courseService.removeTeachers(1L, teacherIdsToRemove);

        assertNotNull(result);
        assertFalse(course.getTeachers().contains(teacher));
        verify(courseDAO, times(1)).find(1L);
    }

    @Test
    void testFindById() {
        when(courseDAO.find(1L)).thenReturn(Optional.of(course));
        when(courseMapper.fromCourse(course)).thenReturn(courseDTO);

        CourseDTO result = courseService.findById(1L);

        assertNotNull(result);
        assertEquals("Курс тест 1", result.getName());
        verify(courseDAO, times(1)).find(1L);
    }

    @Test
    void testFindCourses() {
        List<Course> courseList = Arrays.asList(course);
        when(courseDAO.findAll(0, 10)).thenReturn(courseList);
        when(courseMapper.fromCourse(course)).thenReturn(courseDTO);

        List<CourseDTO> result = courseService.findCourses(0, 10);

        assertEquals(1, result.size());
        verify(courseDAO, times(1)).findAll(0, 10);
    }

    @Test
    public void testCourseNotFound() {
        when(courseDAO.find(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                courseService.findById(1L));
        assertEquals("Не нашли курс по id 1", exception.getMessage());
    }

    @Test
    void testFindCoursesByText() {
        List<Course> courseList = Arrays.asList(course);
        when(courseDAO.findAllByText("Sample", 0, 10)).thenReturn(courseList);
        when(courseMapper.fromCourse(course)).thenReturn(courseDTO);

        List<CourseDTO> result = courseService.findCoursesByText("Sample", 0, 10);

        assertEquals(1, result.size());
        verify(courseDAO, times(1)).findAllByText("Sample", 0, 10);
    }

    @Test
    void testDeleteCourse() {
        when(courseDAO.find(1L)).thenReturn(Optional.of(course));

        courseService.deleteCourse(1L);

        verify(courseDAO, times(1)).deleteById(1L);
    }
}
