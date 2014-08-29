package net.sarazan.bismarck.i;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Aaron Sarazan on 8/28/14
 * Copyright(c) 2014 Level, Inc.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Fetch {

    // TODO I really don't want to have to declare this, but since we currently wrap in response I'm not sure what to do
    public Class value();
}
