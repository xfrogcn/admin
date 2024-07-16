package com.xfrog.platform.infrastructure.base.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xfrog.platform.application.base.dto.LangLocalDTO;
import com.xfrog.platform.application.base.repository.LangLocalRepository;
import com.xfrog.platform.infrastructure.base.dataobject.LangLocalPO;
import com.xfrog.platform.infrastructure.base.mapper.LangLocalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class LangLocalRepositoryImpl implements LangLocalRepository {

    private final LangLocalMapper langLocalMapper;

    @Override
    public List<LangLocalDTO> queryByLangCorpusId(Long langCorpusId) {
        return langLocalMapper.queryByLangCorpusId(langCorpusId);
    }

    @Override
    public Map<String, String> queryByApplicationAndLangCode(String application, String langCode) {

        List<LangLocalPO> langLocalPOs = langLocalMapper.selectList(new LambdaUpdateWrapper<LangLocalPO>()
                .eq(LangLocalPO::getDeleted, false)
                .eq(LangLocalPO::getApplication, application)
                .eq(LangLocalPO::getLangCode, langCode));

        if (CollectionUtils.isEmpty(langLocalPOs)) {
            return new HashMap<>();
        }

        return langLocalPOs.stream()
                .filter(it -> it.getLocalValue() != null)
                .collect(Collectors.toMap(LangLocalPO::getLangCorpusCode, LangLocalPO::getLocalValue, (a, b) -> a));
    }
}
