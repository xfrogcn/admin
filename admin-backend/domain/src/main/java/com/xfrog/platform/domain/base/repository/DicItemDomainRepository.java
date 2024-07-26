package com.xfrog.platform.domain.base.repository;

import com.xfrog.platform.domain.base.aggregate.DicItem;
import com.xfrog.framework.repository.DomainRepository;

import java.util.List;

public interface DicItemDomainRepository  extends DomainRepository<DicItem> {
    boolean existsByDisplayText(Long dicId, String displayText, List<Long> excludeIds);
}
