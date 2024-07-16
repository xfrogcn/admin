package com.xfrog.platform.api.base;

import com.xfrog.platform.api.BaseApiTest;
import com.xfrog.platform.api.base.fixtures.BaseApiFixtures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({BaseApiFixtures.class})
public abstract class BaseBaseApiTest extends BaseApiTest {
    @Autowired
    protected BaseApiFixtures baseApiFixtures;
}
