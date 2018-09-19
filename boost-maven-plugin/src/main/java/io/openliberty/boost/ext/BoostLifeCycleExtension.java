package io.openliberty.boost.ext;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;


@Component(role = AbstractMavenLifecycleParticipant.class, hint ="boost")
public class BoostLifeCycleExtension extends AbstractMavenLifecycleParticipant {
	
	@Requirement
    private Logger logger;
	
	@Override
  	public void afterProjectsRead(MavenSession session) {
		
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
		
		logger.debug("Offline building: " + session.isOffline());

		MavenProject proj = session.getCurrentProject();
		
		List<String> buildEnvStrings = null;
		
		buildEnvStrings = getBuildEnvString(proj);
		
			List<Dependency> resolvedDeps = resolveDependencies(proj.getDependencies(), buildEnvStrings);

				proj.setDependencies(resolvedDeps);
			}
		
   	

	@Override
  	public void afterSessionEnd(MavenSession session) throws MavenExecutionException {
		logger.debug("All Maven projects are built.");
  	}

	@Override
	public void afterSessionStart(MavenSession session) throws MavenExecutionException {
		logger.debug("afterSessionStart");
	}
	
	private List<String> getBuildEnvString(MavenProject project) {

		List<String> BoostBOMsFound = new ArrayList<String>();

		logger.debug("Looking for a boost BOM\n");

		DependencyManagement depMgmt = project.getOriginalModel().getDependencyManagement();
		if (depMgmt != null) { // if not an EE app there might not be a
								// dependency mgmt section
			for (Dependency depmgtdep : depMgmt.getDependencies()) {
				logger.debug("BoostExt: found this artifact in dependencyMgmt section-> " + depmgtdep.getArtifactId()
						+ ":" + depmgtdep.getVersion() + "\n");
				if (depmgtdep.getArtifactId().equals("boost-javaee7-bom")) {
					logger.debug("BoostExt: EE7 Boost bom found");
					BoostBOMsFound.add("ee7");
				} else if (depmgtdep.getArtifactId().equals("boost-javaee8-bom")) {
					logger.debug("BoostExt: EE8 Boost bom found");
					BoostBOMsFound.add("ee8");
				} else if (depmgtdep.getArtifactId().equals("boost-microprofile13-bom")) {
					logger.debug("BoostExt: Microprofile 1.3 Boost bom found");
					BoostBOMsFound.add("mp13");
				} else if (depmgtdep.getArtifactId().equals("boost-microprofile14-bom")) {
					logger.debug("BoostExt: Microprofile 1.4 Boost bom found");
					BoostBOMsFound.add("mp14");
				} else {
					logger.debug("No Boost bom found");
				}

			}
		}
		
		return BoostBOMsFound;
	}
	
	private List<Dependency> resolveDependencies(List<Dependency> unresolvedDependencies, List<String> buildBOMs){
		
		String artifactId = null;
		String groupId = null;
		String version = null;
		
		List<Dependency> resolvedDeps = new ArrayList<Dependency>();

		
		for (Dependency dep : unresolvedDependencies) {
			logger.debug("BoostExt: found this artifact-> " + dep.getArtifactId() + "\n");
			if (dep.getArtifactId().equals("boost-jaxrs")) {
				logger.debug("BoostExt: found a generic boost dependency: " + dep.getArtifactId());
				// groupId = "io.openliberty.features";
				groupId = "javax.ws.rs";
				artifactId = "javax.ws.rs-api";
				if (buildBOMs.contains("ee7") && buildBOMs.contains("ee8")){
					System.out.println("AJM: can't contain both ee7 and ee8 boms...");
				}
				else if (buildBOMs.contains("mp13") && buildBOMs.contains("mp14")){
					System.out.println("AJM: can't contain both mp13 and mp14 boms...");
				}
				else if (buildBOMs.contains("ee7") || buildBOMs.contains("mp13")) {
					version = "2.0";
				} else { // can only be ee8 AND/OR mp14 then
					version = "2.1";
				}

				// keep the generic dependency in place
				resolvedDeps.add(dep);

				// add the api as a compile dependency
				Dependency dependency = new Dependency();
				dependency.setGroupId(groupId);
				dependency.setArtifactId(artifactId);
				dependency.setVersion(version);
				dependency.setScope("compile");
				resolvedDeps.add(dependency);
			} else {
				resolvedDeps.add(dep);
			}
		}
		
		return resolvedDeps;
	}
}