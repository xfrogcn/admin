package com.xfrog.platform.api.base.fixtures;

import com.xfrog.platform.domain.base.aggregate.Dic;
import com.xfrog.platform.domain.base.aggregate.DicFixtures;
import com.xfrog.platform.domain.base.aggregate.DicItem;
import com.xfrog.platform.domain.base.aggregate.Lang;
import com.xfrog.platform.domain.base.aggregate.LangCorpus;
import com.xfrog.platform.domain.base.aggregate.LangFixtures;
import com.xfrog.platform.domain.base.aggregate.LangLocal;
import com.xfrog.platform.domain.base.repository.DicDomainRepository;
import com.xfrog.platform.domain.base.repository.DicItemDomainRepository;
import com.xfrog.platform.domain.base.repository.LangCorpusDomainRepository;
import com.xfrog.platform.domain.base.repository.LangDomainRepository;
import com.xfrog.platform.domain.base.repository.LangLocalDomainRepository;
import com.xfrog.platform.domain.base.repository.UserParameterDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseApiFixtures {
    public final static String SQL_TRUNCATE_DIC = "truncate table dics";
    public final static String SQL_TRUNCATE_DIC_ITEM = "truncate table dic_items";

    public final static String SQL_TRUNCATE_LANG_CORPUS = "truncate table lang_corpus";
    public final static String SQL_TRUNCATE_LANG = "truncate table langs";

    public final static String SQL_TRUNCATE_USER_PARAMTERS = "truncate table user_parameters";

    @Autowired
    private DicDomainRepository dicDomainRepository;

    @Autowired
    private DicItemDomainRepository dicItemDomainRepository;

    @Autowired
    private LangCorpusDomainRepository langCorpusDomainRepository;
    @Autowired
    private LangDomainRepository langDomainRepository;
    @Autowired
    private LangLocalDomainRepository langLocalDomainRepository;

    @Autowired
    private UserParameterDomainRepository userParameterDomainRepository;

    public Dic createAndSaveDic() {
        Dic dic = DicFixtures.createDefaultDic().build();
        dic.setId(null);
        return dicDomainRepository.save(dic);
    }

    public DicItem createAndSaveDicItem(Long dicId) {
        DicItem dicItem = DicFixtures.createDefaultDicItem(dicId).build();
        dicItem.setId(null);
        return dicItemDomainRepository.save(dicItem);
    }

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
