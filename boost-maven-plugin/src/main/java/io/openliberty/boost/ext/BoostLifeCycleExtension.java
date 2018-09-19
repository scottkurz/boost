package io.openliberty.boost.ext;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.classworlds.realm.DuplicateRealmException;
import org.codehaus.plexus.component.annotations.Component;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
//import org.eclipse.aether.artifact.Artifact;
//import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.filter.DependencyFilterUtils;

@Component(role = AbstractMavenLifecycleParticipant.class, hint ="boost")
public class BoostLifeCycleExtension extends AbstractMavenLifecycleParticipant {
	
	//@Inject
	//private RepositorySystem repositorySystem;

	//@Parameter(defaultValue = "${repositorySystemSession}", readonly = true)
	//@Inject
	//private RepositorySystemSession repositorySystemSession;  
	
	//@Parameter(defaultValue = "${project.remotePluginRepositories}", readonly = true)
	//@Inject
	//private List<RemoteRepository> remoteRepositories;

	@Override
  	public void afterProjectsRead(MavenSession session) {
		
		System.out.println("Hello from the Boost Maven Plugin as Extension...All MavenProject instances are created.");
		System.out.println("Offline building: " + session.isOffline());

		MavenProject proj = session.getCurrentProject();
		try {
			System.out.println("AJM: current project classpath elements for this proj: \n"
					+ proj.getRuntimeClasspathElements() + "\n\n");
		} catch (DependencyResolutionRequiredException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String buildLevel = null;
		System.out.println("AJM: getting the boost BOM\n");
		DependencyManagement depMgmt = proj.getOriginalModel().getDependencyManagement();
		for (Dependency depmgtdep : depMgmt.getDependencies()) {
			System.out.println("BoostExt: found this artifact in dependencyMgmt section-> " + depmgtdep.getArtifactId()
					+ ":" + depmgtdep.getVersion() + "\n");
			if (depmgtdep.getArtifactId().equals("boost-javaee7-bom")) {
				buildLevel = "ee7";
			} else if (depmgtdep.getArtifactId().equals("boost-javaee8-bom")) {
				buildLevel = "ee8";
			} else {
				System.out.println("AJM: unknown build bom");
			}

		}

		String artifactId = null;
		String groupId = null;
		String version = null;
		
		//repositorySystemSession = session.getRepositorySession();
		//remoteRepositories = proj.	getRemoteProjectRepositories();
		//final File repo = repositorySystemSession.getLocalRepository().getBasedir();
		//DependencyResult dependencyResult = null;
		//System.out.println("AJM - local repo location: " + repo.getAbsolutePath());
		//System.out.println("AJM - remote repo size: " + remoteRepositories.size());
		//System.out.println("AJM: listing out the dependencies: \n");
		
		List<Dependency> deps = proj.getDependencies();
		List<Dependency> resolvedDeps = new ArrayList<Dependency>();
		
		for (Dependency dep : deps) {
			System.out.println("BoostExt: found this artifact-> " + dep.getArtifactId() + "\n");
			if (dep.getArtifactId().equals("boost-jaxrs")) {
				System.out.println("BoostExt: found a generic boost dependency: " + dep.getArtifactId());
				//groupId = "io.openliberty.features";
				groupId = "javax.ws.rs";
				artifactId = "javax.ws.rs-api";
				if (buildLevel.equals("ee7")) {
					version = "2.0";
				} else { // can only be ee8 then
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
			else {
				resolvedDeps.add(dep);
			}
			
			proj.setDependencies(resolvedDeps);
			
		}
		
		
		//DEAD CODE but keeping for reference purposes
		
		/*
		
		final CollectRequest collectRequest = new CollectRequest();
		//final Artifact artifact = new DefaultArtifact(groupId, artifactId,"esa",version);
		final Artifact artifact = new DefaultArtifact(groupId, artifactId,"jar",version);

		collectRequest.setRoot(new org.eclipse.aether.graph.Dependency(artifact, JavaScopes.COMPILE));
		collectRequest.setRepositories(remoteRepositories);

		final DependencyRequest dependencyRequest = new DependencyRequest(collectRequest,
				DependencyFilterUtils.classpathFilter(JavaScopes.COMPILE));
		dependencyResult = null;
		try {
			dependencyResult = repositorySystem.resolveDependencies(repositorySystemSession, dependencyRequest);
		} catch (DependencyResolutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("AJM: found - " + dependencyResult.toString());
		//final List<ArtifactResult> artifactResults = dependencyResult.getArtifactResults();
		// Create a classloader with the downloaded artifact.
	    //ClassLoader classLoader = new URLClassLoader(new URL[] { dependencyResult.  .getFile().toURI().toURL() });
	    */
			/*
			
			// Now, lets try to set up the classpath
			
			//ClassWorld world = new ClassWorld();
			ClassRealm realm = null;
			ClassRealm updateRealm = null;
			try {
				//realm = world.newRealm("io.openliberty.boost.project", Thread.currentThread().getContextClassLoader());
				//updateRealm = realm.createChildRealm("boostersrealm");
				System.out.println("AJM: project class realm before: ");
				proj.getClassRealm().display();
				updateRealm = proj.getClassRealm().createChildRealm("boosterRealm");
				Iterator<ArtifactResult> itor = dependencyResult.getArtifactResults().iterator();
				while (itor.hasNext())
					//System.out.println("AJM: adding: " +(itor.next()).getArtifact().getFile().toURI().toURL().toString() + " to classrealm?");
					updateRealm.addURL((itor.next()).getArtifact().getFile().toURI().toURL());
				//make the child realm the ContextClassLoader
				//Thread.currentThread().setContextClassLoader(updateRealm);
				proj.setClassRealm(updateRealm);
				System.out.println("AJM: project class realm after: ");
				proj.getClassRealm().display();
				//System.out.println("AJM - classloader contents: " + Thread.currentThread().getContextClassLoader().toString());
				//System.out.println("AJM: here is the updateRealm?: ");
				//updateRealm.display();

			} catch (DuplicateRealmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			
   	}

	@Override
  	public void afterSessionEnd(MavenSession session) throws MavenExecutionException {
    	System.out.println("All Maven projects are built.");
  	}

	@Override
	public void afterSessionStart(MavenSession session) throws MavenExecutionException {
		System.out.println("afterSessionStart???????");
	}
}