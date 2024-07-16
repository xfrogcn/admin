package com.xfrog.platform.domain.base.aggregate;

public class UserParameterFixtures {
    public static UserParameter.UserParameterBuilder createDefaultUserParameter()
    {
        return UserParameter.builder()
                .userId(1L)
                .application("test")
                .parameterName("test")
                .parameterValue("test");
    }
}
