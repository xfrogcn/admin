package com.xfrog.platform.api.permission;

import com.xfrog.platform.api.BaseApiTest;
import com.xfrog.platform.api.permission.fixtures.PermissionApiFixtures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({ PermissionApiFixtures.class })
public abstract class BasePermissionApiTest  extends BaseApiTest {
    @Autowired
    protected PermissionApiFixtures permissionApiFixtures;
}
