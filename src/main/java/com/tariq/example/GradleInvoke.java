package com.tariq.example;

import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.model.ExternalDependency;
import org.gradle.tooling.model.eclipse.EclipseProject;
import org.gradle.tooling.model.idea.IdeaDependency;
import org.gradle.tooling.model.idea.IdeaProject;
import org.gradle.tooling.model.idea.IdeaSingleEntryLibraryDependency;

import java.io.*;

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
           IdeaProject project = connection.getModel(IdeaProject.class);
            StringBuilder gradleClasspath = new StringBuilder();
            boolean isIdeaProject = false;
            boolean isEclipseProject = false;

            for(File file : target.listFiles()) {
                if(file.getName().endsWith(".iml")) {
                    isIdeaProject = true;
                    break;
                }
                if(file.getName().endsWith(".classpath")) {
                    isEclipseProject = true;
                    break;
                }
            }

            if(isIdeaProject) {
                for (IdeaDependency dependency : project.getModules().getAt(0).getDependencies()) {
                    IdeaSingleEntryLibraryDependency libraryDependency = (IdeaSingleEntryLibraryDependency) dependency;
                    gradleClasspath.append(libraryDependency.getFile().getAbsolutePath() + File.pathSeparator);
                }
            }

            if(isEclipseProject) {
                EclipseProject eclipseProject = connection.getModel(EclipseProject.class);
                for (ExternalDependency externalDependency : eclipseProject.getClasspath()) {
                    gradleClasspath.append(externalDependency.getFile().getAbsolutePath() + File.pathSeparator);
                }
            }
            PrintWriter pw = null;
            try {
                pw = new PrintWriter("classpath.cp");
                pw.write(gradleClasspath.toString());
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


}
