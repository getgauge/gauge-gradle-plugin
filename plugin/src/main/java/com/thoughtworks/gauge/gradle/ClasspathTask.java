/*
 *  Copyright 2015 Manu Sunny
 *
 *  This file is part of Gauge-gradle-plugin.
 *
 *  Gauge-gradle-plugin is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Gauge-gradle-plugin is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Gauge-gradle-plugin.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.thoughtworks.gauge.gradle;

import com.thoughtworks.gauge.gradle.util.PropertyManager;

import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.testing.Test;

@SuppressWarnings("WeakerAccess")
public class ClasspathTask extends Test {

    @TaskAction
    public void classpath() {
        Project project = getProject();
        GaugeExtension extension = project.getExtensions().findByType(GaugeExtension.class);
        PropertyManager propertyManager = new PropertyManager(project, extension);
        propertyManager.setProperties();
        System.out.println(extension.getClasspath());
    }
}