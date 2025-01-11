package com.senla.courses.service.literature;

import com.senla.courses.dao.LiteratureDAO;
import com.senla.courses.dao.ModuleDAO;
import com.senla.courses.dto.LiteratureDTO;
import com.senla.courses.exception.EmptyListException;
import com.senla.courses.exception.NotFoundException;
import com.senla.courses.mapper.LiteratureMapper;
import com.senla.courses.model.Literature;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LiteratureServiceImpl implements LiteratureService {

    private static final Logger logger = LoggerFactory.getLogger(LiteratureServiceImpl.class);

    private final ModuleDAO moduleDAO;
    private final LiteratureDAO literatureDAO;
    private final LiteratureMapper literatureMapper;

    @Override
    public Long add(LiteratureDTO literatureDTO, Long id) {
        Literature literature = literatureMapper.fromLiteratureDTO(literatureDTO);
        literature.setModule(moduleDAO.find(id).orElseThrow(()
                -> new NotFoundException("На нашли модуля по id " + id)));
        Long idReturn = literatureDAO.save(literature);
        logger.info("Сохранена литература с названием {} для модуля с id {}", literatureDTO.getName(), id);
        return idReturn;
    }

    @Override
    public LiteratureDTO edit(LiteratureDTO literatureDTO, Long id) {
        Literature literature = literatureDAO.find(id).orElseThrow(()
                -> new NotFoundException("Не удалось найти литературу"));
        Literature literatureResult = literatureDAO.update(literatureMapper.updateLiterature(literature, literatureDTO));
        logger.info("Отредактирована литература с id {}", id);
        return literatureMapper.fromLiterature(literatureResult);
    }

    @Override
    public LiteratureDTO findById(Long id) {
        Literature literature = literatureDAO.find(id).orElseThrow(()
                -> new NotFoundException("Не удалось найти литературу"));
        LiteratureDTO literatureDTO = literatureMapper.fromLiterature(literature);
        logger.info("Найдена литература с id {}", id);
        return literatureDTO;
    }

    @Override
    public List<LiteratureDTO> findByText(String text, int from, int size) {
        List<Literature> literatureList = literatureDAO.findAllByText(text, from, size);
        if (literatureList.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<LiteratureDTO> literatureDTOList = literatureList.stream().map(literatureMapper::fromLiterature).toList();
        logger.info("Собран список литературы по тексту {}", text);
        return literatureDTOList;
    }

    @Override
    public List<LiteratureDTO> findByAuthor(String author, int from, int size) {
        List<Literature> literatureList = literatureDAO.findAllByAuthor(author, from, size);
        if (literatureList.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<LiteratureDTO> literatureDTOList = literatureList.stream().map(literatureMapper::fromLiterature).toList();
        logger.info("Собран список литературы по автору {}", author);
        return literatureDTOList;
    }

    @Override
    public List<LiteratureDTO> findByModuleId(Long moduleId, int from, int size) {
        List<Literature> literatureList = literatureDAO.findByModuleId(moduleId, from, size);
        if (literatureList.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        List<LiteratureDTO> literatureDTOList =  literatureList.stream().map(literatureMapper::fromLiterature).toList();
        logger.info("Собран список литературы модуля с id {}", moduleId);
        return literatureDTOList;
    }

    @Override
    public void delete(Long id) {
        literatureDAO.deleteById(id);
        logger.info("Удалена литература с id {}", id);
    }

}
