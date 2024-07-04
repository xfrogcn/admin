package com.xfrog.platform.domain.base.repository;

import com.xfrog.platform.domain.base.aggregate.DicItem;
import com.xfrog.platform.domain.repository.DomainRepository;

import java.util.List;

public interface DicItemDomainRepository  extends DomainRepository<DicItem> {
    boolean existsByDisplayText(String displayText, List<Long> excludeIds);
}
