package com.xfrog.platform.api.base.fixtures;

import com.xfrog.platform.domain.base.aggregate.Dic;
import com.xfrog.platform.domain.base.aggregate.DicFixtures;
import com.xfrog.platform.domain.base.aggregate.DicItem;
import com.xfrog.platform.domain.base.repository.DicDomainRepository;
import com.xfrog.platform.domain.base.repository.DicItemDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class DicApiFixtures {
    public final static String SQL_TRUNCATE_DIC = "truncate table dics";
    public final static String SQL_TRUNCATE_DIC_ITEM = "truncate table dic_items";

    @Autowired
    private DicDomainRepository dicDomainRepository;

    @Autowired
    private DicItemDomainRepository dicItemDomainRepository;

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
}
