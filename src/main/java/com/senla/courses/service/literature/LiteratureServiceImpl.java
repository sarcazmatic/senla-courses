package com.senla.courses.service.literature;

import com.senla.courses.dao.LiteratureDAO;
import com.senla.courses.dao.ModuleDAO;
import com.senla.courses.dto.LiteratureDTO;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.mapper.LiteratureMapper;
import com.senla.courses.model.Literature;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class LiteratureServiceImpl implements LiteratureService {

    private final ModuleDAO moduleDAO;
    private final LiteratureDAO literatureDAO;
    private final LiteratureMapper literatureMapper;

    @Override
    public Long add(LiteratureDTO literatureDTO, Long id) {
        Literature literature = literatureMapper.fromLiteratureDTO(literatureDTO);
        literature.setModule(moduleDAO.find(id).orElseThrow(()
                -> new NotFoundException("На нашли модуля по id " + id)));
        Long idReturn = literatureDAO.save(literature);
        log.info("Сохранена литература с названием {} для модуля с id {}", literatureDTO.getName(), id);
        return idReturn;
    }

    @Override
    public LiteratureDTO edit(LiteratureDTO literatureDTO, Long id) {
        Literature literature = literatureDAO.find(id).orElseThrow(()
                -> new NotFoundException("Не удалось найти литературу"));
        Literature literatureResult = literatureDAO.update(literatureMapper.updateLiterature(literature, literatureDTO));
        log.info("Отредактирована литература с id {}", id);
        return literatureMapper.fromLiterature(literatureResult);
    }

    @Override
    public LiteratureDTO findById(Long id) {
        Literature literature = literatureDAO.find(id).orElseThrow(()
                -> new NotFoundException("Не удалось найти литературу"));
        LiteratureDTO literatureDTO = literatureMapper.fromLiterature(literature);
        log.info("Найдена литература с id {}", id);
        return literatureDTO;
    }

    @Override
    public List<LiteratureDTO> findByText(String text, int from, int size) {
        List<Literature> literatureList = literatureDAO.findAllByText(text, from, size);
        if (literatureList.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<LiteratureDTO> literatureDTOList = literatureList.stream().map(literatureMapper::fromLiterature).toList();
        log.info("Собран список литературы по тексту {}", text);
        return literatureDTOList;
    }

    @Override
    public List<LiteratureDTO> findByAuthor(String author, int from, int size) {
        List<Literature> literatureList = literatureDAO.findAllByAuthor(author, from, size);
        if (literatureList.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<LiteratureDTO> literatureDTOList = literatureList.stream().map(literatureMapper::fromLiterature).toList();
        log.info("Собран список литературы по автору {}", author);
        return literatureDTOList;
    }

    @Override
    public List<LiteratureDTO> findByModuleId(Long moduleId, int from, int size) {
        List<Literature> literatureList = literatureDAO.findByModuleId(moduleId, from, size);
        if (literatureList.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<LiteratureDTO> literatureDTOList =  literatureList.stream().map(literatureMapper::fromLiterature).toList();
        log.info("Собран список литературы модуля с id {}", moduleId);
        return literatureDTOList;
    }

    @Override
    public void delete(Long id) {
        literatureDAO.deleteById(id);
        log.info("Удалена литература с id {}", id);
    }

}
