package com.tariq.example;

import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.model.ExternalDependency;
import org.gradle.tooling.model.eclipse.EclipseProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by tariq.ibrahim on 09-12-2015.
 */
public class GradleInvoke {

    public static void main(String[] args) {

        GradleConnector connector = GradleConnector.newConnector();
        String targetProject = System.getProperty("targetProject");
        File target = new File(targetProject);
        connector.forProjectDirectory(target);
        ProjectConnection connection = null;

        try {
            connection = connector.connect();

            EclipseProject eclipseProject = connection.getModel(EclipseProject.class);
            String gradleClasspath = getClassPathFromProject(eclipseProject);

            PrintWriter pw = null;
            try {
                pw = new PrintWriter("classpath.cp");
                pw.write(gradleClasspath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            pw.close();


        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private static String getClassPathFromProject(final EclipseProject eclipseProject) {

        StringBuilder gradleClasspath = new StringBuilder();
        for (ExternalDependency externalDependency : eclipseProject.getClasspath()) {
            gradleClasspath.append(externalDependency.getFile().getAbsolutePath() + File.pathSeparator);
        }

        if (!eclipseProject.getChildren().isEmpty()) {
            for (EclipseProject childProject : eclipseProject.getChildren()) {
                gradleClasspath.append(getClassPathFromProject(childProject));
            }
        }
        return gradleClasspath.toString();
    }

}


