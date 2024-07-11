package com.xfrog.platform.api.base.fixtures;

import com.xfrog.platform.domain.base.aggregate.Lang;
import com.xfrog.platform.domain.base.aggregate.LangCorpus;
import com.xfrog.platform.domain.base.aggregate.LangFixtures;
import com.xfrog.platform.domain.base.aggregate.LangLocal;
import com.xfrog.platform.domain.base.repository.LangCorpusDomainRepository;
import com.xfrog.platform.domain.base.repository.LangDomainRepository;
import com.xfrog.platform.domain.base.repository.LangLocalDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class LangApiFixtures {
    public final static String SQL_TRUNCATE_LANG_CORPUS = "truncate table lang_corpus";
    public final static String SQL_TRUNCATE_LANG = "truncate table langs";

    @Autowired
    private LangCorpusDomainRepository langCorpusDomainRepository;
    @Autowired
    private LangDomainRepository langDomainRepository;
    @Autowired
    private LangLocalDomainRepository langLocalDomainRepository;

    public LangCorpus createAndSaveLangCorpus() {
        LangCorpus langCorpus = LangFixtures.createDefaultCorpus().build();
        langCorpus.setId(null);
        langCorpus = langCorpusDomainRepository.save(langCorpus);

        return langCorpus;
    }

    public LangLocal saveLangLocale(LangLocal langLocal) {

        langLocal.setId(null);

        return langLocalDomainRepository.save(langLocal);
    }

    public Lang createAndSaveLang() {
        Lang lang = LangFixtures.createDefaultLang().build();
        lang.setId(null);
        lang = langDomainRepository.save(lang);

        return lang;
    }

    public Lang saveLang(Lang lang) {

        lang.setId(null);
        return langDomainRepository.save(lang);
    }
}
