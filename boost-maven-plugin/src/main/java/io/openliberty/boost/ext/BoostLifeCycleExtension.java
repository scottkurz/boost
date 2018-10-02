package io.openliberty.boost.ext;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

@Component(role = AbstractMavenLifecycleParticipant.class, hint = "boost")
public class BoostLifeCycleExtension extends AbstractMavenLifecycleParticipant {

    @Requirement
    private Logger logger;

    @Override
    public void afterProjectsRead(MavenSession session) {

        ClassRealm cl = (ClassRealm) this.getClass().getClassLoader();
        if (!cl.getId().startsWith("extension")) {
            logger.info("Skipping extension loaded by classloader = " + cl.getId());
            return;
        }

        System.out.println("88          88 88");
        System.out.println("88          \"\" 88                                  ,d");
        System.out.println("88             88                                  88");
        System.out.println("88          88 88,dPPYba,   ,adPPYba, 8b,dPPYba, MM88MMM 8b       d8");
        System.out.println("88          88 88P'    \"8a a8P_____88 88P'   \"Y8   88    `8b     d8'");
        System.out.println("88          88 88       d8 8PP\"\"\"\"\"\"\" 88           88     `8b   d8'");
        System.out.println("88          88 88b,   ,a8\" \"8b,   ,aa 88           88,     `8b,d8'");
        System.out.println("88888888888 88 8Y\"Ybbd8\"'   `\"Ybbd8\"' 88           \"Y888     Y88'");
        System.out.println("                                                             d8'");
        System.out.println("                                                            d8'");
        System.out.println("                                                           d8'");

        System.out.println("88888888ba                                            88");
        System.out.println("88      \"8b                                     ,d    88");
        System.out.println("88      ,8P                                     88    88");
        System.out.println("88aaaaaa8P'  ,adPPYba,   ,adPPYba,  ,adPPYba, MM88MMM 88");
        System.out.println("88\"\"\"\"\"\"8b, a8\"     \"8a a8\"     \"8a I8[    \"\"   88    88");
        System.out.println("88      `8b 8b       d8 8b       d8  `\"Y8ba,    88    \"\"");
        System.out.println("88      a8P \"8a,   ,a8\" \"8a,   ,a8\" aa    ]8I   88,   aa");
        System.out.println("88888888P\"   `\"YbbdP\"'   `\"YbbdP\"'  `\"YbbdP\"'   \"Y888 88");

        System.out.println("AJM: plugin booster config proposal");

        MavenProject proj = session.getCurrentProject();

        List<String> buildEnvStrings = null;

        // List<Dependency> resolvedDeps = retrieveDependencies(proj);
        buildEnvStrings = getBuildEnvString(proj);
        List<String> boosters = getBoosters(proj.getPlugin("io.openliberty.boost:boost-maven-plugin"));

        // need booster entries to know how to set up the compile path
        if (boosters != null) {

            List<Dependency> projDeps = proj.getDependencies();
            List<Dependency> compileDeps = configCompileEnv(buildEnvStrings, boosters);
            for (Dependency dep : compileDeps) {
                projDeps.add(dep);
            }
            proj.setDependencies(projDeps);
        } else {
            logger.warn("No booster technology specified in plugin config - cannot set up compile env for EE apps");
        }

    }

    @Override
    public void afterSessionEnd(MavenSession session) throws MavenExecutionException {
        logger.debug("All Maven projects are built.");
    }

    @Override
    public void afterSessionStart(MavenSession session) throws MavenExecutionException {
        logger.debug("afterSessionStart");
    }

    private List<String> getBuildEnvString(MavenProject proj) {

        List<String> buildEnvsFound = new ArrayList<String>();

        logger.debug("Looking for a runtime build env dependency\n");

        for (Dependency dep : proj.getDependencies()) {
            logger.debug("BoostExt: found this artifact in dependency section-> " + dep.getArtifactId() + ":"
                    + dep.getVersion() + "\n");
            if (dep.getArtifactId().equals("javaee-api") && dep.getVersion().equals("7.0")) {
                logger.debug("BoostExt: EE7 dependency found");
                buildEnvsFound.add("ee7");
            } else if (dep.getArtifactId().equals("microprofile") && dep.getVersion().equals("1.3")) {
                logger.debug("BoostExt: Microprofile 1.3 dependency found");
                buildEnvsFound.add("mp13");
            } else if (dep.getArtifactId().equals("microprofile") && dep.getVersion().equals("1.4")) {
                logger.debug("BoostExt: Microprofile 1.4 dependency found");
                buildEnvsFound.add("mp14");
            } else {
                logger.debug("BoostExt: not a build env dependency");
            }
        }

        if (buildEnvsFound.isEmpty()) {
            // default is ee8
            logger.debug("BoostExt: no build env dependency explicitly listed - defaulting to ee8");
            buildEnvsFound.add("ee8");
        }

        return buildEnvsFound;
    }

    private List<String> getBoosters(Plugin plugin) {

        List<String> result = null;

        // this only works if the extension is narrowed down to the current classloader
        Xpp3Dom config = (Xpp3Dom) plugin.getConfiguration();

        if (config != null) {
            logger.debug("Extracted strings config section: " + config.toString());

            result = new ArrayList<String>();

            Xpp3Dom[] subelement = config.getChildren(); // .getChild("booster");
            for (Xpp3Dom xppdom : subelement) {
                logger.debug("AJM: entry found -> " + xppdom.toString());
                logger.debug("AJM: found entry val -> " + xppdom.getValue());
                result.add(xppdom.getValue());

            }

            logger.debug("Extracted strings from plugin booster section: " + result);
        }

        return result;

    }

    private List<Dependency> configCompileEnv(List<String> beStrings, List<String> boosterStrings) {

        String artifactId = null;
        String groupId = null;
        String version = null;

        List<Dependency> resolvedDeps = new ArrayList<Dependency>();

        if (boosterStrings.contains((String) "jaxrs")) {
            groupId = "javax.ws.rs";
            artifactId = "javax.ws.rs-api";
            if (beStrings.contains((String) "ee7") || beStrings.contains((String) "mp13")) {
                version = "2.0";
            }
            if (beStrings.contains((String) "ee8") || beStrings.contains((String) "mp14")) {
                version = "2.1";
            }

            // add the api as a compile dependency
            Dependency dependency = new Dependency();
            dependency.setGroupId(groupId);
            dependency.setArtifactId(artifactId);
            dependency.setVersion(version);
            dependency.setScope("compile");
            resolvedDeps.add(dependency);
        }

        return resolvedDeps;
    }
}