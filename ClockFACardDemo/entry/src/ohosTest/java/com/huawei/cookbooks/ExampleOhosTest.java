package com.huawei.cookbooks;

import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;


public class ExampleOhosTest {
    @Test
    public void testBundleName() {
        final String actualBundleName = AbilityDelegatorRegistry.getArguments().getTestBundleName();
        assertEquals("com.huawei.clockcard", actualBundleName);
    }
}