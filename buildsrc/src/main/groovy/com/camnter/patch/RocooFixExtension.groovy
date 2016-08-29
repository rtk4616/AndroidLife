/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.camnter.patch

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.tasks.Input

/**
 * https://github.com/dodola/RocooFix/blob/master/buildsrc/src/main/groovy/com/dodola/rocoofix/RocooFixExtension.groovy
 */
@CompileStatic
class RocooFixExtension {
    @Input
    boolean enable = false

    @Input
    boolean scanref = false

    @Input
    boolean showLog = false

    @Input
    HashSet<String> includePackage = [];

    @Input
    HashSet<String> excludePackage = [];

    @Input
    HashSet<String> excludeClass = [];

    @Input
    String preVersionPath

    public static RocooFixExtension getConfig(Project project) {
        RocooFixExtension config =
                project.getExtensions().findByType(RocooFixExtension.class);
        if (config == null) {
            config = new RocooFixExtension();
        }
        return config;
    }

}
