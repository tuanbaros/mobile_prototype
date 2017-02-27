package com.framgia.mobileprototype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tuannt on 27/02/2017.
 * Project: mobile_prototype
 * Package: com.framgia
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterPermission {
    String[] permissions();
}
