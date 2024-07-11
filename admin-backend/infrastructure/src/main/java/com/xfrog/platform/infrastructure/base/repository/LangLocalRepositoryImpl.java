package com.xfrog.platform.infrastructure.base.repository;

import com.xfrog.platform.application.base.dto.LangLocalDTO;
import com.xfrog.platform.application.base.repository.LangLocalRepository;
import com.xfrog.platform.infrastructure.base.mapper.LangLocalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LangLocalRepositoryImpl implements LangLocalRepository {

    private final LangLocalMapper langLocalMapper;

    @Override
    public List<LangLocalDTO> queryByLangCorpusId(Long langCorpusId) {
        return langLocalMapper.queryByLangCorpusId(langCorpusId);
    }
}
