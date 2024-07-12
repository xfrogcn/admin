package com.xfrog.platform.api.base;

import com.xfrog.platform.api.BaseApiTest;
import com.xfrog.platform.api.base.fixtures.DicApiFixtures;
import com.xfrog.platform.api.base.fixtures.LangApiFixtures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({DicApiFixtures.class, LangApiFixtures.class})
public abstract class BaseBaseApiTest extends BaseApiTest {
    @Autowired
    protected DicApiFixtures dicApiFixtures;

    @Autowired
    protected LangApiFixtures langApiFixtures;
}
