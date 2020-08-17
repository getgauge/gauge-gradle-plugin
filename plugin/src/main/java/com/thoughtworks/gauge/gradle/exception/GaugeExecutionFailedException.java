/*----------------------------------------------------------------
 *  Copyright (c) ThoughtWorks, Inc.
 *  Licensed under the Apache License, Version 2.0
 *  See LICENSE.txt in the project root for license information.
 *----------------------------------------------------------------*/

package com.thoughtworks.gauge.gradle.exception;

import org.gradle.api.GradleException;

public class GaugeExecutionFailedException extends GradleException {
    public GaugeExecutionFailedException(Throwable throwable) {
        super(throwable.getMessage(), throwable);
    }

    public GaugeExecutionFailedException(String message) {
        super(message);
    }
}
